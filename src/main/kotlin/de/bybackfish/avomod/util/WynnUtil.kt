package de.bybackfish.avomod.util

import de.bybackfish.avomod.WynncraftMod
import de.bybackfish.avomod.extensions.toComponent
import de.bybackfish.wynnapi.guilds.Guild
import net.minecraft.client.Minecraft
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import java.net.URLConnection

fun getGuild(nameOrTag: String): Guild? {
    val name = getFromTag(nameOrTag) ?: nameOrTag
    return WynncraftMod.wynnstats.getGuild(name)
}

private fun getFromTag(tag: String): String? {
    try {
        val url = "http://avicia.ga/api/tag/?tag=$tag"

        val content = getContents(url)
        Minecraft.getMinecraft().player.sendMessage(("$url | $content").toComponent())
        if (content == "null") {
            return null
        }
        return content!!.replace("\"", "")
    } catch (e: Exception) {

        return null
    }
}

fun getContents(url: String): String? {
    val url1 = URL(url)
    val conn: URLConnection = url1.openConnection()
    conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 5.1; rv:19.0) Gecko/20100101 Firefox/19.0")
    conn.connect()
    val serverResponse = BufferedReader(InputStreamReader(conn.getInputStream()))
    val response = serverResponse.readLine()
    serverResponse.close()
    return response
}
