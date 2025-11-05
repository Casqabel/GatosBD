package com.example.catdeployer.model

import java.util.UUID

data class CatUiModel (
    val id: Int,
    val gender: Gender,
    val name: String,
    val biography: String,
    val imageUrl: String,

)