package dev.creativition.simplejdautil.sample.commands;

import dev.creativition.simplejdautil.SimpleJDAUtil;
import dev.creativition.simplejdautil.SlashCommandBuilder;
import dev.creativition.simplejdautil.objects.SlashCommandOption;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import org.jetbrains.annotations.NotNull;

public class EchoCommand implements EventListener {

    static {
        final SlashCommandBuilder cmd = SlashCommandBuilder.createCommand("echo", "Send a echo message");

        final SlashCommandOption option = new SlashCommandOption("message",
            "The message to send",
            OptionType.STRING,
            false,
            false
        );

        cmd.addOption(option);

        SimpleJDAUtil.addSlashCommand(cmd.build());
        SimpleJDAUtil.addListener(new EchoCommand());
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onEvent(@NotNull GenericEvent event) {
        if (event instanceof final SlashCommandInteractionEvent evt) {
            if (!evt.getName().equals("echo"))
                return;

            final String message = evt.getOption("message").getAsString();
            evt.reply(message).setEphemeral(true).queue();
        }
    }
}
