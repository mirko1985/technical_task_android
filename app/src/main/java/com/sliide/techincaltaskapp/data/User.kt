package com.sliide.techincaltaskapp.data

val genders = arrayOf("Male","Female")
val statusArr = arrayOf("Active","Inactive")

data class User(
    val email: String,
    val gender: String? = genders[(0..1).random()],
    val id: Int? = null,
    val name: String,
    val status: String? = statusArr[(0..1).random()]
)