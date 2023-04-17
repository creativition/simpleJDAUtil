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
     * @return The current instance of SlashCommandBuilder.
     */
    fun addSubCommand(command: SlashCommandInfo): SlashCommandBuilder {
        if (isSubCommand)
            throw IllegalArgumentException("Cannot add sub command to a sub command")
        subCommands.add(command)
        return this
    }


    /**
     * Adds a subcommand to this command
     * @param subCommands List of SlashCommandInfo
     * @return The current instance of SlashCommandBuilder.
     */
    fun addSubCommand(subCommands: List<SlashCommandInfo>): SlashCommandBuilder {
        this.subCommands.addAll(subCommands)
        return this
    }


    /**
     * Remove a specified subcommand from this command.
     * @param subCommandName The name of the subcommand to remove.
     * @return The current instance of SlashCommandBuilder.
     */
    fun removeSubCommand(subCommandName: String): SlashCommandBuilder {
        if (isSubCommand)
            throw IllegalArgumentException("Cannot use this method while creating a sub command")
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
     * @return The current instance of SlashCommandBuilder.
     */
    fun addOption(optionName: String, description: String, type: OptionType, required: Boolean, completions: Boolean): SlashCommandBuilder {
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