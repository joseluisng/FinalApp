package com.techng.joseluisng.finalapp.models

import java.util.Date

data class Rate(
        val text: String = "",
        val rate: Float = 0f,
        val createdAt: Date = Date(),
        val profileImgURL: String = ""
        )

//En caso de querer que el usuario solo califique una vez agregar la siguiente variable a el contrustor

//val userId: String = "",
