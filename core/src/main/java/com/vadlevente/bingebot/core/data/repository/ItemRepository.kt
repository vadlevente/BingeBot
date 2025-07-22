package com.vadlevente.bingebot.core.data.repository

import com.vadlevente.bingebot.core.data.cache.ItemCacheDataSource
import com.vadlevente.bingebot.core.data.local.datastore.PreferencesDataSource
import com.vadlevente.bingebot.core.data.local.db.ItemLocalDataSource
import com.vadlevente.bingebot.core.data.remote.ItemRemoteDataSource
import com.vadlevente.bingebot.core.data.remote.firestore.FirestoreItemDataSource
import com.vadlevente.bingebot.core.model.Credits
import com.vadlevente.bingebot.core.model.Department
import com.vadlevente.bingebot.core.model.Genre
import com.vadlevente.bingebot.core.model.GenreFactory
import com.vadlevente.bingebot.core.model.Item
import com.vadlevente.bingebot.core.model.ItemDetails
import com.vadlevente.bingebot.core.model.WatchList
import com.vadlevente.bingebot.core.model.WatchListFactory
import com.vadlevente.bingebot.core.model.WatchProviders
import com.vadlevente.bingebot.core.model.firestore.StoredItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import java.util.Date
import javax.inject.Inject

interface ItemRepository<T : Item> {
    fun getItems(): Flow<List<T>>
    fun getItemDetails(itemId: Int): Flow<ItemDetails<T>>
    fun getGenres(): Flow<List<Genre>>
    fun getSearchResult(): Flow<List<T>>
    fun getWatchLists(): Flow<List<WatchList>>
    fun getWatchListItems(watchListId: String): Flow<List<T>>
    fun getWatchProviders(itemId: Int): Flow<WatchProviders?>
    suspend fun updateGenres()
    suspend fun updateItems()
    suspend fun updateWatchLists()
    suspend fun searchItems(query: String)
    suspend fun updateItemLocalizations()
    suspend fun saveItem(item: T)
    suspend fun deleteItem(itemId: Int)
    suspend fun createWatchList(title: String): String
    suspend fun addItemToList(itemId: Int, watchListId: String): Boolean
    suspend fun removeItemFromList(itemId: Int, watchListId: String)
    suspend fun setItemWatchedDate(itemId: Int, watchedDate: Date?)
    suspend fun deleteWatchList(watchListId: String)
}

@OptIn(FlowPreview::class)
class ItemRepositoryImpl<T : Item> @Inject constructor(
    private val itemRemoteDataSource: ItemRemoteDataSource<T>,
    private val itemLocalDataSource: ItemLocalDataSource<T>,
    private val itemCacheDataSource: ItemCacheDataSource<T>,
    private val firestoreItemDataSource: FirestoreItemDataSource<T>,
    private val preferencesDataSource: PreferencesDataSource,
    private val watchListFactory: WatchListFactory<T>,
    private val genreFactory: GenreFactory<T>,
) : ItemRepository<T> {

    override fun getItems(): Flow<List<T>> =
        itemLocalDataSource.getAllItems().flowOn(Dispatchers.IO)

    override fun getItemDetails(itemId: Int): Flow<ItemDetails<T>> =
        combine(
            preferencesDataSource.language,
            itemLocalDataSource.getItem(itemId),
            ::Pair,
        ).flatMapConcat { (language, local) ->
            val remote = itemRemoteDataSource.getItemDetails(itemId, language).toItem()
            val updatedRemote = if (local != null) {
                var tempRemote = remote
                local.createdDate?.let { tempRemote = tempRemote.copyCreatedDate(it) }
                local.watchedDate?.let { tempRemote = tempRemote.copyWatchedDate(it) }
                tempRemote
            } else {
                remote
            }
            val remoteCredits = itemRemoteDataSource.getItemCredits(itemId, language)
            val credits = Credits(
                cast = remoteCredits.castMembers,
                director = remoteCredits.crewMembers.filter { it.job == Department.Director },
                writer = remoteCredits.crewMembers.filter {
                    it.job in listOf(
                        Department.Screenplay,
                        Department.Writer
                    )
                },
            )
            flowOf(
                ItemDetails(updatedRemote, credits)
            )
        }

    override fun getGenres(): Flow<List<Genre>> =
        itemLocalDataSource.getAllGenres().flowOn(Dispatchers.IO)

    override fun getSearchResult(): Flow<List<T>> = itemCacheDataSource.itemsState

    override fun getWatchLists(): Flow<List<WatchList>> =
        itemLocalDataSource.getAllWatchLists().flowOn(Dispatchers.IO)

    override fun getWatchListItems(watchListId: String): Flow<List<T>> =
        itemLocalDataSource.getWatchList(watchListId)
            .flatMapConcat { watchList ->
                itemLocalDataSource.getItems(watchList.itemIds)
            }.flowOn(Dispatchers.IO)

    override fun getWatchProviders(itemId: Int): Flow<WatchProviders?> = flow {
        emit(itemRemoteDataSource.getItemProviders(itemId).results.hu)
    }.flowOn(Dispatchers.IO)

    override suspend fun updateGenres() = withContext(Dispatchers.IO) {
        val language = preferencesDataSource.language.first()
        val genres = itemRemoteDataSource.getGenres(language).genres.map {
            genreFactory.setType(it)
        }
        itemLocalDataSource.deleteAllGenres()
        itemLocalDataSource.insertGenres(genres)
    }

    override suspend fun updateItems() = withContext(Dispatchers.IO) {
        val language = preferencesDataSource.language.first()
        val storedItems = itemLocalDataSource.getAllItems().first()
        val remoteItems = firestoreItemDataSource.getItems().first()
        val itemsToUpdate = storedItems.filter { storedItem ->
            storedItem.watchedDate != remoteItems.first { it.id.toInt() == storedItem.id }.watchDate
        }.map { storedItem ->
            storedItem.copyWatchedDate<T>(watchedDate = remoteItems.first { it.id.toInt() == storedItem.id }.watchDate)
        }
        val itemsToInsert = remoteItems
            .map { it.id.toInt() }
            .minus(storedItems.map { it.id }
                .toSet())
            .map { id ->
                val remoteItem = remoteItems.first { it.id.toInt() == id }
                itemRemoteDataSource.getItemDetails(id, language).toItem()
                    .copyLocale<T>(language.code)
                    .copyCreatedDate<T>(remoteItem.createdDate)
                    .copyWatchedDate<T>(remoteItem.watchDate)
            }
        itemLocalDataSource.updateItems(itemsToUpdate.plus(itemsToInsert))
    }

    override suspend fun updateWatchLists() = withContext(Dispatchers.IO) {
        itemLocalDataSource.deleteAllWatchLists()
        val remoteWatchLists = firestoreItemDataSource.getWatchLists().first()
        itemLocalDataSource.insertWatchLists(remoteWatchLists)
    }

    override suspend fun searchItems(query: String) = withContext(Dispatchers.IO) {
        val language = preferencesDataSource.language.first()
        val items = itemRemoteDataSource.searchItem(query, language)
        itemCacheDataSource.updateItems(
            items.map { it.toItem() }.filterUnwantedItems(),
            language
        )
    }

    override suspend fun updateItemLocalizations() = withContext(Dispatchers.IO) {
        val language = preferencesDataSource.language.first()
        val storedItemsWithIncorrectLocalization =
            itemLocalDataSource.getAllItemsWithIncorrectLocalization(language.code).first()
        val updatedItems = coroutineScope {
            storedItemsWithIncorrectLocalization.map { item ->
                async {
                    itemRemoteDataSource.getItemDetails(item.id, language).toItem()
                        .copyLocale<T>(language.code)
                        .copyCreatedDate<T>(item.createdDate!!)
                        .copyWatchedDate<T>(item.watchedDate)
                }
            }.awaitAll()
        }
        itemLocalDataSource.updateItems(updatedItems)
    }

    override suspend fun saveItem(item: T) = withContext(Dispatchers.IO) {
        val createdDate = Date()
        firestoreItemDataSource.addItem(
            StoredItem(
                id = item.id.toString(),
                createdDate = createdDate,
            )
        )
        itemLocalDataSource.insertItem(item.copyCreatedDate(createdDate))
    }

    override suspend fun deleteItem(itemId: Int) = withContext(Dispatchers.IO) {
        firestoreItemDataSource.deleteItem(itemId)
        itemLocalDataSource.deleteItem(itemId)
        itemLocalDataSource.getAllWatchLists().first()
            .filter { it.itemIds.contains(itemId) }
            .forEach { watchList ->
                itemLocalDataSource.insertWatchList(
                    watchList.copy(
                        itemIds = watchList.itemIds.minus(itemId)
                    )
                )
            }
    }

    override suspend fun createWatchList(title: String): String =
        withContext(Dispatchers.IO) {
            val watchListId = firestoreItemDataSource.addWatchList(title)
            itemLocalDataSource.insertWatchList(
                watchListFactory.create(
                    watchListId = watchListId,
                    title = title,
                )
            )
            return@withContext watchListId
        }

    override suspend fun addItemToList(itemId: Int, watchListId: String) =
        withContext(Dispatchers.IO) {
            val watchList = itemLocalDataSource.getWatchList(watchListId).first()
            if (watchList.itemIds.contains(itemId)) {
                return@withContext false
            }
            itemLocalDataSource.insertWatchList(
                watchList.copy(
                    itemIds = watchList.itemIds.plus(itemId)
                )
            )
            firestoreItemDataSource.addItemToWatchList(watchListId, itemId)
            return@withContext true
        }

    override suspend fun removeItemFromList(itemId: Int, watchListId: String) =
        withContext(Dispatchers.IO) {
            firestoreItemDataSource.removeItemFromWatchList(watchListId, itemId)
            val watchList = itemLocalDataSource.getWatchList(watchListId).first()
            itemLocalDataSource.insertWatchList(
                watchList.copy(
                    itemIds = watchList.itemIds.minus(itemId)
                )
            )
        }

    override suspend fun setItemWatchedDate(itemId: Int, watchedDate: Date?) =
        withContext(Dispatchers.IO) {
            firestoreItemDataSource.setItemWatchDate(itemId, watchedDate)
            itemLocalDataSource.setItemWatchedDate(itemId, watchedDate)
        }

    override suspend fun deleteWatchList(watchListId: String) = withContext(Dispatchers.IO) {
        firestoreItemDataSource.deleteWatchList(watchListId)
        itemLocalDataSource.deleteWatchList(watchListId)
    }

    private fun List<T>.filterUnwantedItems() = this
        .filter { it.genreCodes.isNotEmpty() }
        .filter { it.voteAverage != 0f }

}