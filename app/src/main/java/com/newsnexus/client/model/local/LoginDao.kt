package com.newsnexus.client.model.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.newsnexus.client.model.dataclass.dummy.User

@Dao
interface LoginDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addListUser(listUser: List<User>)

    @Query("SELECT * FROM User WHERE userEmail = :userEmail AND password = :userPassword")
    fun retrieveUserLogin(userEmail: String, userPassword: String): User

    @Query("DELETE FROM User")
    fun removeAllUser()
}