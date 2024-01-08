package com.bingebot.core.data.local

import com.bingebot.core.data.local.datastore.DataStoreDao
import com.bingebot.core.model.ActiveProfileId

interface ActiveProfileIdDao : ItemDao<ActiveProfileId>

class ActiveProfileIdDaoImpl : DataStoreDao.ItemDao<ActiveProfileId>("activeProfileId"), ActiveProfileIdDao