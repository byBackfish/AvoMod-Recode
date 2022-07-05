package de.bybackfish.avomod.commands.util

import de.bybackfish.avomod.WynncraftMod
import de.bybackfish.avomod.commands.base.Kommand
import de.bybackfish.avomod.util.getGuild
import net.minecraft.util.text.TextComponentString
import net.minecraft.util.text.event.ClickEvent
import java.util.*

class OnlineMembers : Kommand(k@{
    if (it.isEmpty()) {
        sendUsage()
        return@k
    }
    val guild = getGuild(it.joinToString(" "))


    if (guild == null) {
        send("Guild not found")
        return@k
    }

    val onlineServers = WynncraftMod.wynnstats.getServers()!!

    val onlineMembers = guild.members.filter { player ->
        findInOnlineList(player.name!!, onlineServers)
    }.sortedBy { player ->
        getRankWeight(player.rank)
    }.reversed()
    send("§6---")

    send("§b${guild.name} [${guild.prefix}] §7has §b${onlineMembers.size} §7online members")
    send("");

    val component = TextComponentString("");
    onlineMembers.forEach { player ->
        component.appendSibling(
            createComponent(
                "§6${player.name}${"*".repeat(getRankWeight(player.rank))}, ",
                clickEvent = ClickEvent(ClickEvent.Action.RUN_COMMAND, "/playerstats ${player.name}")
            )
        )
    }

    send(component)
    send("§6---")

}, "onlinemembers", alias = arrayOf("om")) {
}


fun getRankWeight(rank: String?): Int {
    if (rank == null) return 0;
    val name = rank.uppercase(Locale.getDefault());
    if (name == "OWNER") return 5;
    if (name == "CHIEF") return 4;
    if (name == "STRATEGIST") return 3;
    if (name == "CAPTAIN") return 2;
    if (name == "RECRUITER") return 1;
    return 0;
}

fun findInOnlineList(user: String, servers: de.bybackfish.wynnapi.network.ServerList): Boolean {
    return servers.servers.any { server ->
        server.value.contains(user)
    }
}
