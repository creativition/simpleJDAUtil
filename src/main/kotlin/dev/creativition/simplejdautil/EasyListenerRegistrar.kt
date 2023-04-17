package dev.creativition.simplejdautil

import com.google.common.reflect.ClassPath
import dev.creativition.simplejdautil.objects.SlashCommandInfo
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.hooks.EventListener
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.CommandData
import net.dv8tion.jda.api.interactions.commands.build.Commands
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData

object EasyListenerRegistrar {
    private val listeners: MutableList<EventListener> = mutableListOf()
    private val adapters: MutableList<ListenerAdapter> = mutableListOf()
    private val slashCommands: MutableList<SlashCommandInfo> = mutableListOf()
    private val packageList: MutableList<String> = mutableListOf()
    private var logError = false

    @JvmStatic
    fun addListener(listener: EventListener) {
        listeners.add(listener)
    }

    @JvmStatic
    private fun getListeners(): List<EventListener> {
        return listeners
    }

    @JvmStatic
    fun addAdapter(adapter: ListenerAdapter) {
        adapters.add(adapter)
    }

    @JvmStatic
    fun getAdapters(): List<ListenerAdapter> {
        return adapters
    }

    @JvmStatic
    fun getSlashCommands(): List<SlashCommandInfo> {
        return slashCommands
    }

    @JvmStatic
    fun addSlashCommand(command: SlashCommandInfo) {
        if (command.isSubCommand)
            throw IllegalArgumentException("Cannot add a subcommand instance to slash command")

        slashCommands.add(command)
    }

    @JvmStatic
    fun addSearchPath(packagePath: String) {
        if (!packagePath.endsWith("."))
            throw IllegalArgumentException("Package path \"$packagePath\" is must be end with a dot. ex: ex.example.discord.commands.")

        packageList.add(packagePath)
    }

    @JvmStatic
    fun setSearchPath(packagePathList: List<String>) {
        if (packageList.isEmpty())
            throw IllegalArgumentException("Package path list is empty.")

        packageList.forEach { path ->
            if (!path.endsWith("."))
                throw IllegalArgumentException("Package path \"$path\" is must be end with a dot. ex: ex.example.discord.commands.")
        }

        packageList.clear()
        packageList.addAll(packagePathList)
    }

    @JvmStatic
    fun setErrorLogging(logError: Boolean) {
        this.logError = logError
    }

    @JvmStatic
    fun registerSlashCommands(jdaInstance: JDA){
        val commands = mutableListOf<CommandData>()

        val commandList = getSlashCommands()

        commandList.forEach { commandInfo ->
            val slashCommand = Commands.slash(commandInfo.commandName, commandInfo.description)

            commandInfo.subCommands.forEach { subCommands ->
                val subCmd = SubcommandData(subCommands.commandName, subCommands.description)

                subCommands.options.forEach { options ->
                    subCmd.addOption(
                        options.value["type"] as OptionType,
                        options.key,
                        options.value["description"] as String,
                        options.value["required"] as Boolean,
                        options.value["completions"] as Boolean
                    )
                }
                slashCommand.addSubcommands(subCmd)
            }

            commandInfo.options.forEach { options ->
                slashCommand.addOption(
                    options.value["type"] as OptionType,
                    options.key,
                    options.value["description"] as String,
                    options.value["required"] as Boolean,
                    options.value["completions"] as Boolean
                )
            }

            commands.add(slashCommand)
        }

        jdaInstance.updateCommands().addCommands(commands).queue()
    }

    @JvmStatic
    fun registerListeners(jdaBuilder: JDABuilder, classLoader: ClassLoader): JDABuilder {
        initClasses(classLoader)

        getListeners().forEach {
            jdaBuilder.addEventListeners(it)
        }

        getAdapters().forEach {
            jdaBuilder.addEventListeners(it)
        }
        return jdaBuilder
    }

    @JvmStatic
    private fun forceInit(klazz: Class<*>){
        try {
            Class.forName(klazz.name, true, klazz.classLoader)
        } catch (e: ClassNotFoundException) {
            if (logError)
                println("Class not found: ${klazz.name}")
                e.printStackTrace()
        }
    }

    @JvmStatic
    private fun initClasses(classLoader: ClassLoader) {
        for (info in ClassPath.from(classLoader).topLevelClasses) {
            if (packageList.contains(info.packageName)) {
                val klazz = info.load()
                forceInit(klazz)
            }
        }
    }
}