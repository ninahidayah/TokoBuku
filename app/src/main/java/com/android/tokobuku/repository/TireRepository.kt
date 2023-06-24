package com.android.tokobuku.repository

import com.android.tokobuku.dao.TireDao
import com.android.tokobuku.model.Tire
import kotlinx.coroutines.flow.Flow

class TireRepository(private val tireDao: TireDao) {
    val allTires: Flow<List<Tire>> = tireDao.getAllTire()
    suspend fun insertTire(tire: Tire){
        tireDao.insertTire(tire)
    }

    suspend fun deleteTire(tire: Tire){
        tireDao.deleteTire(tire)
    }

    suspend fun updateTire(tire: Tire){
        tireDao.updateTire(tire)
    }
}