package client.views.playerpage.chatfeed

import client.controllers.ChatController
import client.controllers.ImageLoaderController
import client.models.ContentType
import client.models.Message
import javafx.scene.image.ImageView
import javafx.scene.text.FontPosture
import javafx.scene.text.FontWeight
import javafx.util.Duration
import tornadofx.*

class MessageFragment : Fragment() {
    private val message: Message by param()
    private val textSize: Double by param()
    private val chatController: ChatController by inject()
    private val imageLoaderController: ImageLoaderController by inject()

    companion object {
        private val emojiLoader = EmojiLoader()
    }

    override val root = textflow {
        /**
         * Username
         */
        text(message.username) {
            style {
                this.fill = chatController.getColor(message.username)
                this.fontWeight = FontWeight.BOLD
                this.fontSize = textSize.px
            }
        }
        text(": ") {
            style {
                this.fill = ChatFeedStyles.chatTextColor
                this.fontSize = textSize.px
            }
        }
        /**
         * Content
         */
        if (message.contentType == ContentType.INFO) {
            text(message.content) {
                style {
                    this.fill = ChatFeedStyles.chatTextColor
                    this.fontStyle = FontPosture.ITALIC
                    this.fontSize = textSize.px
                }
            }
        } else if (message.contentType == ContentType.MESSAGE) {
            chatController.tokenizeMessage(message.content).map {
                val token = it
                val image = emojiLoader.getEmojiFromAlias(it, 25.0)
                if (image == null) {
                    text(it) {
                        style {
                            this.fontSize = textSize.px
                            this.fill = ChatFeedStyles.chatTextColor
                        }
                    }
                } else {
                    /** Emoji */
                    val iv = object : ImageView(image) {
                        override fun getBaselineOffset(): Double {
                            return this.image.height * 0.75
                        }
                    }
                    iv.tooltip(token) {
                        this.showDelay = Duration.ZERO
                    }
                    this.add(iv)
                }
            }
        } else if (message.contentType == ContentType.IMAGE) {
            val image = imageLoaderController.getImageFromEncoding(message.content)
            if (image != null) {
                try {
                    val iv = ImageView(image)
                    this.add(iv)
                } catch (e: Exception) {
                    text("could not load image") {
                        style {
                            this.fill = ChatFeedStyles.chatTextColor
                            this.fontStyle = FontPosture.ITALIC
                            this.fontSize = textSize.px
                        }
                    }
                }
            } else {
                text("could not load image") {
                    style {
                        this.fill = ChatFeedStyles.chatTextColor
                        this.fontStyle = FontPosture.ITALIC
                        this.fontSize = textSize.px
                    }
                }
            }
        }
        style {
            lineSpacing = 5.0
            maxWidth = 335.px
        }
    }
}