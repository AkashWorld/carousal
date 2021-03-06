package com.carousel.client.views.playerpage.mediaplayer

import javafx.scene.Cursor
import javafx.scene.paint.Color
import tornadofx.*

class MediaPlayerStyles : Stylesheet() {
    companion object {
        val trackSlider by cssclass()
        val volumeSlider by cssclass()
        val sliderValue by cssclass()
        val coloredTrack by cssclass()
        val animatedThumb by cssclass()
        val mediaPlayerButton by cssclass()
        val mediaBigPlayButton by cssclass()
        val fullScreenButton by cssclass()
        val chatSideButton by cssclass()
    }

    init {
        mediaPlayerButton {
            backgroundColor = multi(Color.TRANSPARENT)
            maxHeight = 30.px
            padding = box(0.px)
            and(hover) {
                cursor = Cursor.HAND
            }
        }
        mediaBigPlayButton {
            backgroundColor = multi(Color.TRANSPARENT)
            maxHeight = 100.px
            padding = box(0.px)
        }
        chatSideButton {
            backgroundColor = multi(Color.TRANSPARENT)
            maxHeight = 30.px
            padding = box(3.px, 0.px, 0.px, 0.px)
            and(hover) {
                cursor = Cursor.HAND
            }
        }
        fullScreenButton {
            backgroundColor = multi(Color.TRANSPARENT)
            maxHeight = 30.px
            padding = box((-5).px)
            and(hover) {
                cursor = Cursor.HAND
            }
        }
        trackSlider {
            track {
                prefHeight = 4.px
                backgroundColor = multi(Color.LIGHTGRAY)
            }
            coloredTrack {
                backgroundColor = multi(Color.RED)
            }
            animatedThumb {
                backgroundColor = multi(Color.RED)
            }
            and(hover) {
                thumb {
                    backgroundColor = multi(Color.RED)
                }
                cursor = Cursor.HAND
            }
            thumb {
                backgroundColor = multi(Color.TRANSPARENT)
            }
            animatedThumb {
                backgroundColor = multi(Color.TRANSPARENT)
            }
            sliderValue {
                fill = Color.TRANSPARENT
                stroke = Color.TRANSPARENT
            }
        }
        volumeSlider {
            padding = box(7.0.px, 0.0.px, 0.0.px, 0.0.px)
            track {
                prefHeight = 4.px
                backgroundColor = multi(Color.LIGHTGRAY)
            }
            coloredTrack {
                backgroundColor = multi(Color.WHITE)
            }
            animatedThumb {
                backgroundColor = multi(Color.TRANSPARENT)
            }
            thumb {
                backgroundColor = multi(Color.WHITE)
            }
            sliderValue {
                fill = Color.TRANSPARENT
                stroke = Color.TRANSPARENT
            }
            and(hover) {
                cursor = Cursor.HAND
            }
        }
    }
}