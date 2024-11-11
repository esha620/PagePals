package com.example.pagepals1.data

data class UserSelection(
    val user: User,   // Wrap the User object
    var isSelected: Boolean = false  // Flag to track selection state
)