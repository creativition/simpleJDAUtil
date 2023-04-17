package dev.creativition.simplejdautil.sample

import dev.creativition.simplejdautil.SimpleJDAUtil
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.requests.GatewayIntent

class DiscordBot {
    fun main(args: Array<String>) {
        val jdaBuilder = JDABuilder.createDefault(System.getenv("DISCORD_TOKEN"))

        jdaBuilder.enableIntents(GatewayIntent.MESSAGE_CONTENT)

        SimpleJDAUtil.addSearchPath("dev.creativition.simplejdautil.sample.commands.")
        val jda = SimpleJDAUtil
            .registerListeners(jdaBuilder, ClassLoader.getPlatformClassLoader())
            .build()

        SimpleJDAUtil.registerSlashCommands(jda)

        jda.awaitReady()

        println("Connected to Discord.")
    }
}