package com.newsnexus.client.model.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.newsnexus.client.model.dataclass.dummy.CritiqueSuggestions

@Dao
interface CnsDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addCnS(critiqueSuggestions: CritiqueSuggestions)

    @Query("SELECT * FROM CnS")
    fun getListCnS(): LiveData<List<CritiqueSuggestions>>
}