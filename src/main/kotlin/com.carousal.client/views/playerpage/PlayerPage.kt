package com.carousal.client.views.playerpage

import com.carousal.client.controllers.ChatController
import com.carousal.client.models.ClientContextImpl
import com.carousal.client.views.ViewUtils
import com.carousal.client.views.intropage.IntroPage
import com.carousal.client.views.playerpage.chatfeed.ChatFragment
import com.carousal.client.views.playerpage.fileloader.FileLoaderView
import javafx.scene.layout.StackPane
import tornadofx.*

class PlayerPage : View() {

    private val introPageView: IntroPage by inject()
    private val fileLoaderView: FileLoaderView by inject()
    private val chatController: ChatController by inject()

    override val root = borderpane {
        setMinSize(0.0, 0.0)
        center {
            this.add(fileLoaderView)
        }
        right {
            chatController.isChatShown().addListener { _, _, newValue ->
                if (!newValue) {
                    this.children.remove(this.right)
                } else {
                    this.right = find<ChatFragment>().root
                }
            }
        }
    }

    fun navigateToIntroPage() {
        if (isDocked) {
            root.replaceWith(introPageView.root, ViewTransition.Fade(1000.millis))
        }
    }

    fun navigateToFileLoader() {
        if (isDocked && root.center != fileLoaderView.root) {
            root.center = fileLoaderView.root
        }
    }

    override fun onDock() {
        super.onDock()
        root.right = find<ChatFragment>().root
        chatController.subscribeToMessages {
            ViewUtils.showErrorDialog("Connection with chat is lost, please restart this application.", primaryStage.scene.root as StackPane)
        }
    }

    override fun onUndock() {
        super.onUndock()
        chatController.cleanUp()
        root.center = fileLoaderView.root
        root.children.remove(root.right)
        ClientContextImpl.getInstance().clearContext()
    }
}
