package fx

import tornadofx.*
import com.carousel.client.views.playerpage.chatfeed.ChatFeedStyles
import com.carousel.client.controllers.ChatController
import com.carousel.client.controllers.ClientContextController
import com.carousel.client.models.ClientContext
import com.carousel.client.models.ClientContextImpl
import com.carousel.client.models.ContentType
import com.carousel.client.models.Message
import com.carousel.client.views.playerpage.chatfeed.ChatFragment
import com.carousel.client.views.playerpage.chatfeed.GiphyPickerStyles
import de.codecentric.centerdevice.javafxsvg.SvgImageLoaderFactory
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import com.carousel.server.Server

class ChatViewFX {
    private val server: Server = Server.getInstance()

    class Application : App(ChatViewTest::class,  ChatFeedStyles::class, GiphyPickerStyles::class) {
        init {
            reloadStylesheetsOnFocus()
        }
    }

    class ChatViewTest : View() {
        private val testScope = Scope()
        private val clientContext: ClientContext = ClientContextImpl.getInstance()
        private val clientContextController = ClientContextController()
        private val chatController: ChatController by inject()

        init {
            val msglist = chatController.getMessages()
            msglist.add(Message("Dabessbeast", "This is wack, lets go to the real syncplay :angry:"))
            msglist.add(Message("Lone Hunt", ":pepe: Bruh I worked on this for 4 months :pepe:"))
            msglist.add(Message("awildwildboar", "paused at 3:54", ContentType.INFO))
            msglist.add(Message("awildwildboar", "paused at 0:00", ContentType.INFO))
            msglist.add(Message("awildwildboar", "loaded shady korean video", ContentType.INFO))
            msglist.add(Message("chauncey", "How do I even use this thing!!!!!"))
            msglist.add(Message("Wizardofozzie", "Spiderman is the greatest dont @ me :wink:"))
            msglist.add(Message("Voyboy", "test test test"))
            msglist.add(Message("Imaqtpie", "lorum ipsum something"))
            msglist.add(Message("tyler1", "what is this"))
            msglist.add(Message("yassuo", "hello world"))
            tornadofx.setInScope(chatController, testScope)
            tornadofx.setInScope(clientContextController, testScope)
            clientContext.requestSignInToken("test", "localhost:57423", null, {}, {})
            Thread.sleep(1000)
            chatController.subscribeToMessages {  }
        }

        override val root = hbox {
            val chatView = find<ChatFragment>(scope = testScope)
            this.add(chatView)
        }
    }

    @BeforeEach
    private fun init() {
        server.initialize()
        SvgImageLoaderFactory.install()
    }

    @AfterEach
    private fun close() {
        Server.clear()
    }

    @Test
    fun chatViewTest() {
        launch<Application>()
    }
}