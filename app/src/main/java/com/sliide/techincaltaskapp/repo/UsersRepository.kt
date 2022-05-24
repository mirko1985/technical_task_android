package com.sliide.techincaltaskapp.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sliide.techincaltaskapp.data.User
import com.sliide.techincaltaskapp.data.Users
import com.sliide.techincaltaskapp.network.RetrofitHelper
import javax.inject.Inject

class UsersRepository @Inject constructor(){
    suspend fun getUsers() = RetrofitHelper.goRestService.getUsers()

    suspend fun addUser(user: User) = RetrofitHelper.goRestService.addUser(user)

    suspend fun removeUser(user:User) = RetrofitHelper.goRestService.removeUser(user.id!!)
}