package com.newsnexus.client.model.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.newsnexus.client.model.dataclass.dummy.CritiqueSuggestions

@Dao
interface CnsDao {

    @Insert
    fun addCnS(critiqueSuggestions: CritiqueSuggestions)

    @Query("SELECT * FROM CnS")
    fun getListCnS(): List<CritiqueSuggestions>?
}