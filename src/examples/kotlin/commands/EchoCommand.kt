package dev.creativition.simplejdautil.sample.commands

import dev.creativition.simplejdautil.SimpleJDAUtil
import dev.creativition.simplejdautil.SlashCommandBuilder
import dev.creativition.simplejdautil.objects.SlashCommandOption
import net.dv8tion.jda.api.events.GenericEvent
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.hooks.EventListener
import net.dv8tion.jda.api.interactions.commands.OptionType

class EchoCommand: EventListener {

    companion object {
        init {
            val cmd = SlashCommandBuilder.createCommand("echo", "Send a echo message")

            val option = SlashCommandOption("message",
                "The message to send",
                OptionType.STRING,
                isRequired = false,
                hasCompletions = false
            )

            cmd.addOption(option)

            SimpleJDAUtil.addSlashCommand(cmd.build())
            SimpleJDAUtil.addListener(EchoCommand())
        }
    }

    override fun onEvent(event: GenericEvent) {
        if (event !is SlashCommandInteractionEvent)
            return

        if (event.name != "echo")
            return

        event.reply(event.getOption("message")?.asString ?: "null")
            .setEphemeral(true).queue()
    }


}