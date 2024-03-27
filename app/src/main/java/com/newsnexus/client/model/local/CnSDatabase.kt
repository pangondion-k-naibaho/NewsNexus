package com.newsnexus.client.model.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.newsnexus.client.model.dataclass.dummy.CritiqueSuggestions

@Database(
    entities = [CritiqueSuggestions::class],
    version = 1
)
abstract class CnSDatabase: RoomDatabase() {
    companion object{
        private var INSTANCE : CnSDatabase?= null

        fun getDatabase(context: Context): CnSDatabase?{
            if(INSTANCE == null){
                synchronized(CnSDatabase::class.java){
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        CnSDatabase::class.java,
                        "CnS"
                    ).allowMainThreadQueries().build()
                }
            }
            return INSTANCE
        }
    }

    abstract fun cnsDao():CnsDao
}