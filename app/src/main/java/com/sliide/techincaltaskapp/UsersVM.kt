package com.sliide.techincaltaskapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.sliide.techincaltaskapp.data.User
import com.sliide.techincaltaskapp.repo.UsersRepository
import com.sliide.techincaltaskapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class UsersVM @Inject constructor() : ViewModel() {
    @Inject
    lateinit var usersRepository: UsersRepository
    var selectedUsers = arrayListOf<User>()
    var users = arrayListOf<User>()

    private fun removeUser(index: Int, user: User) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            usersRepository.removeUser(user)
            emit(Resource.success(data = users.indexOf(user)))
            users.remove(user)
            selectedUsers.remove(user)
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error!"))
        }
    }

    fun removeUser(index: Int) = removeUser(index, users[index])

    fun loadUsers() = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            val freshUsers = usersRepository.getUsers()
            users = arrayListOf()
            selectedUsers = arrayListOf()
            users.addAll(freshUsers)
            emit(Resource.success(data = users))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error!"))
        }
    }

    fun addUser(user: User) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            val userAdded = usersRepository.addUser(user)
            users.add(0, userAdded)
            emit(Resource.success(data = userAdded))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error!"))
        }
    }

    fun setSelected(user: User, checked: Boolean) {
        if(checked)
            selectedUsers.add(user)
        else
            selectedUsers.remove(user)
    }
}