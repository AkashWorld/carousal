package com.carousel.server.model

data class Media constructor(private val id: String)

enum class Action {
    PLAY,
    PAUSE,
    SEEK
}

data class MediaSubscriptionResult(private val action: Action, private val currentTime: Float?, private val user: String)

