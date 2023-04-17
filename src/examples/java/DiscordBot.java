package dev.creativition.simplejdautil.sample;

import dev.creativition.simplejdautil.SimpleJDAUtil;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;

public class DiscordBot {
    public static void main(String[] args) {
        final JDABuilder jdaBuilder = JDABuilder.createDefault(System.getenv("DISCORD_TOKEN"));

        jdaBuilder.enableIntents(GatewayIntent.MESSAGE_CONTENT);

        SimpleJDAUtil.addSearchPath("dev.creativition.simplejdautil.sample.commands.");

        final JDA jda = SimpleJDAUtil
                .registerListeners(jdaBuilder, ClassLoader.getPlatformClassLoader())
                .build();

        SimpleJDAUtil.registerSlashCommands(jda);

        try {
            jda.awaitReady();
            System.out.println("Connected to Discord.");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
