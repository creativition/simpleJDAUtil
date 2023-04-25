package dev.creativition.simplejdautil

import com.google.common.collect.ImmutableMap
import dev.creativition.simplejdautil.objects.SlashCommandInfo
import dev.creativition.simplejdautil.objects.SlashCommandOption
import net.dv8tion.jda.api.interactions.commands.OptionType
import kotlin.collections.HashMap

class SlashCommandBuilder private constructor(
    private val commandName: String,
    private val description: String,
    private val isSubCommand: Boolean = false
) {

    companion object {

        /**
         * Creates a command builder.
         * @throws IllegalArgumentException If the command name is empty.
         * @return SlashCommandBuilder The command builder.
         */
        @JvmStatic
        fun createCommand(commandName: String, description: String): SlashCommandBuilder {
            if (commandName.isEmpty())
                throw IllegalArgumentException("Command name cannot be empty")
            return SlashCommandBuilder(commandName, description)
        }

        /**
         * Creates a sub command builder.
         * @throws IllegalArgumentException If the sub command name is empty.
         * @return SlashCommandBuilder The sub command builder.
         */
        @JvmStatic
        fun createSubCommand(commandName: String, description: String): SlashCommandBuilder {
            if (commandName.isEmpty())
                throw IllegalArgumentException("Command name cannot be empty")
            return SlashCommandBuilder(commandName, description, true)
        }
    }

    private val subCommands = mutableListOf<SlashCommandInfo>()
    private val options: HashMap<String, SlashCommandOption> = HashMap()


    /**
     * Returns the subcommands of this command.
     * @return The list of subcommands.
     */
    fun getSubCommands(): List<SlashCommandInfo> {
        return subCommands
    }


    /**
     * Adds a subcommand to this command.
     * @param command SlashCommandInfo
     * @throws IllegalStateException When try to add a sub command to sub command instance.
     * @throws IllegalArgumentException If the provided command is not a sub command.
     * @return The current instance of SlashCommandBuilder.
     */
    fun addSubCommand(command: SlashCommandInfo): SlashCommandBuilder {
        if (isSubCommand)
            throw IllegalStateException("Adding sub command to sub command instance is prohibited.")
        if (!command.isSubCommand)
            throw IllegalArgumentException("Provided SlashCommandInfo is not a sub command. command name: ${command.commandName}")
        subCommands.add(command)
        return this
    }


    /**
     * Adds a subcommand to this command
     * @param subCommands List of SlashCommandInfo
     * @throws IllegalStateException When try to add a sub command to sub command instance.
     * @throws IllegalArgumentException If the provided command is not a sub command.
     * @return The current instance of SlashCommandBuilder.
     */
    fun addSubCommand(subCommands: List<SlashCommandInfo>): SlashCommandBuilder {
        if (isSubCommand)
            throw IllegalStateException("Adding sub command to sub command instance is prohibited.")
        subCommands.forEach {
            if (!it.isSubCommand)
                throw IllegalArgumentException("Provided SlashCommandInfo is not a sub command. command name: ${it.commandName}")
        }
        this.subCommands.addAll(subCommands)
        return this
    }


    /**
     * Remove a specified subcommand from this command.
     * @param subCommandName The name of the subcommand to remove.
     * @throws IllegalStateException When try to remove a sub command from sub command instance.
     * @return The current instance of SlashCommandBuilder.
     */
    fun removeSubCommand(subCommandName: String): SlashCommandBuilder {
        if (isSubCommand)
            throw IllegalStateException("You cannot modify sub commands in sub command instance.")
        subCommands.removeIf { it.commandName == subCommandName }
        return this
    }


    /**
     * Returns the options of this command.
     * @return Map<String, HashMap<String, Any>>
     */
    fun getOptions(): Map<String, SlashCommandOption> {
        return options
    }


    /**
     * Add an option to this command.
     * @param optionName The name of option.
     * @param description The description of option.
     * @param type The optionType of option.
     * @param required Whether the option is required.
     * @param completions Whether the option has completions.
     * @throws IllegalStateException When optionName is empty.
     * @return The current instance of SlashCommandBuilder.
     */
    fun addOption(optionName: String, description: String, type: OptionType, required: Boolean, completions: Boolean): SlashCommandBuilder {
        if (optionName.isEmpty())
            throw IllegalArgumentException("Option name cannot be empty")
        options[optionName] = SlashCommandOption(optionName, description, type, required, completions)
        return this
    }

    /**
     * Add an option to this command.
     * @param option SlashCommandOption.
     * @return The current instance of SlashCommandBuilder.
     */
    fun addOption(option: SlashCommandOption): SlashCommandBuilder {
        options[option.optionName] = option
        return this
    }


    /**
     * Build a SlashCommandInfo object with all the information of this command.
     * @return SlashCommandInfo The command object.
     */
    fun build(): SlashCommandInfo {
        return SlashCommandInfo(commandName, description, isSubCommand, subCommands, ImmutableMap.copyOf(options))
    }
}