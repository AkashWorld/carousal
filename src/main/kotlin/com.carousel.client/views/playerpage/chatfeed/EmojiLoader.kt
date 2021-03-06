package com.carousel.client.views.playerpage.chatfeed

import com.vdurmont.emoji.EmojiManager
import com.vdurmont.emoji.EmojiParser
import javafx.collections.ObservableList
import javafx.scene.image.Image
import me.xdrop.fuzzywuzzy.FuzzySearch.*
import org.slf4j.LoggerFactory
import tornadofx.Controller
import java.io.InputStream

enum class EmojiType {
    TWITTER,
    CUSTOM
}

class EmojiLoader : Controller() {
    private val logger = LoggerFactory.getLogger(this::class.qualifiedName)
    private val emojiCache = mutableMapOf<Pair<String, Double>, Image>()
    private val customEmoji = mapOf<String, String>()

    private fun getEmojiInputStream(emojiName: String, emojiType: EmojiType): InputStream? {
        if (emojiType == EmojiType.TWITTER) {
            return this::class.java.classLoader.getResourceAsStream("twemoji/svg/$emojiName.svg")
        } else if (emojiType == EmojiType.CUSTOM) {
            return this::class.java.classLoader.getResourceAsStream("emoji/$emojiName.svg")
        }
        return null
    }

    fun getEmojiFromAlias(alias: String, size: Double?): Image? {
        val hw = size ?: 20.0
        val key = Pair(alias, hw)
        if (emojiCache.contains(key)) {
            return emojiCache[key]
        }
        var inputStream: InputStream? = null
        val unicode = EmojiParser.parseToUnicode(alias)
        if (unicode == alias) {
            if (customEmoji.containsKey(alias)) {
                val emojiFilename = customEmoji[alias]
                inputStream = if (emojiFilename != null) getEmojiInputStream(
                    emojiFilename, EmojiType.CUSTOM
                ) else null
            }
        } else {
            /**
             * Twitter emoji filename is HEX Codepoint
             */
            val chars = unicode.toCharArray()
            val emojiFilename = Character.codePointAt(chars, 0x0).toString(16)
            inputStream = getEmojiInputStream(emojiFilename, EmojiType.TWITTER)
        }
        if (inputStream == null) {
            return null
        }
        return try {
            val image = Image(inputStream, hw, hw, true, true)
            emojiCache[key] = image
            image
        } catch (e: Exception) {
            logger.error(e.message, e.cause)
            null
        }
    }

    fun defaultAliases(): List<String> {
        val emojiAliases = EmojiManager.getAll().map {
            ":${it.aliases.first()}:"
        }
        /**
         * Reordering because at index 365 is when the Emoji's that should be at the start (think Smile, Laugh, etc)
         * are. Instead, we push the first 365 to the back of the list.
         */
        val betterList = emojiAliases.slice(365 until emojiAliases.size).toMutableList()
        val backList = emojiAliases.slice(0 until 365)
        betterList.addAll(backList)
        val customAliases = customEmoji.keys.toMutableList()
        customAliases.addAll(betterList)
        return customAliases
    }

    fun setEmojiAliasesBySearch(searchQuery: String, observableList: ObservableList<String>) {
        if (searchQuery == "") {
            observableList.setAll(defaultAliases())
            return
        }
        val fuzzyRatioList: MutableList<Pair<String, Int>> = EmojiManager.getAll().map {
            Pair(it.aliases.first(), partialRatio(searchQuery, it.description + " " + it.aliases.first()))
        }.filter { it.second > 70 }.toMutableList()
        val fuzzyCustomRatioList: List<Pair<String, Int>> = customEmoji.map {
            Pair(it.value, partialRatio(searchQuery, it.value + " " + it.key))
        }.filter { it.second > 70 }
        fuzzyRatioList.addAll(fuzzyCustomRatioList)
        observableList.setAll(fuzzyRatioList.map { ":${it.first}:" })
    }
}