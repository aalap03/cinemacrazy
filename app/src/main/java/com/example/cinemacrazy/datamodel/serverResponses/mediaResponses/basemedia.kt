package com.example.cinemacrazy.datamodel.serverResponses.mediaResponses

interface MovieMedia {
    fun getLinkKey(): String
    fun mediaType(): String
}

val IMAGE = "image"
val VIDEO = "video"