package dev.creativition.simplejdautil

import com.google.common.collect.ImmutableMap
import dev.creativition.simplejdautil.objects.CommandInfo
import net.dv8tion.jda.api.interactions.commands.OptionType
import java.util.*
import kotlin.collections.HashMap

class CommandBuilder private constructor(
    private val commandName: String,
    private val description: String,
    private val isSubCommand: Boolean = false
) {

    companion object {

        /**
         * Creates a command builder.
         * @return CommandBuilder The command builder.
         */
        @JvmStatic
        fun createCommand(commandName: String, description: String): CommandBuilder {
            if (commandName.isEmpty())
                throw IllegalArgumentException("Command name cannot be empty")
            return CommandBuilder(commandName, description)
        }

        /**
         * Creates a sub command builder.
         * @return CommandBuilder The sub command builder.
         */
        @JvmStatic
        fun createSubCommand(commandName: String, description: String): CommandBuilder {
            if (commandName.isEmpty())
                throw IllegalArgumentException("Command name cannot be empty")
            return CommandBuilder(commandName, description, true)
        }
    }

    private val subCommands = mutableListOf<CommandInfo>()
    private val options: HashMap<String, HashMap<String, Any>> = HashMap()


    /**
     * Returns the subcommands of this command.
     * @return The list of subcommands.
     */
    fun getSubCommands(): List<CommandInfo> {
        return subCommands
    }


    /**
     * Adds a subcommand to this command.
     * @param command CommandInfo
     * @return The current instance of CommandBuilder.
     */
    fun addSubCommand(command: CommandInfo): CommandBuilder {
        if (isSubCommand)
            throw IllegalArgumentException("Cannot add sub command to a sub command")
        subCommands.add(command)
        return this
    }


    /**
     * Adds a subcommand to this command
     * @param subCommands List of CommandInfo
     */
    fun addSubCommand(subCommands: List<CommandInfo>): CommandBuilder {
        this.subCommands.addAll(subCommands)
        return this
    }


    fun removeSubCommand(subCommandName: String): CommandBuilder {
        if (isSubCommand)
            throw IllegalArgumentException("Cannot use this method while creating a sub command")
        subCommands.removeIf { it.commandName == subCommandName }
        return this
    }


    /**
     * Returns the options of this command.
     * @return Map<String, HashMap<String, Any>>
     */
    fun getOptions(): Map<String, Map<String, Any>> {
        return options
    }


    /**
     * Add an option to this command.
     * @param optionName The name of option.
     * @param description The description of option.
     * @param type The optionType of option.
     */
    fun addOption(optionName: String, description: String, type: OptionType): CommandBuilder {
        options[optionName] = hashMapOf(Pair("description", description), Pair("type", type))
        options[optionName]!!["required"] = false
        options[optionName]!!["completions"] = false
        return this
    }


    /**
     * Add an option to this command.
     * @param optionName The name of option.
     * @param description The description of option.
     * @param type The optionType of option.
     * @param required Whether the option is required.
     * @param completions Whether the option has completions.
     */
    fun addOption(optionName: String, description: String, type: OptionType, required: Boolean, completions: Boolean): CommandBuilder {
        options[optionName] = hashMapOf(Pair("description", description), Pair("type", type), Pair("required", required), Pair("completions", completions))
        return this
    }

    /**
     * Remove option from this command.
     * @param optionName The name of option.
      */
    fun removeOption(optionName: String): CommandBuilder {
        options.remove(optionName)
        return this
    }

    /**
     * Defines whether the option is necessary for the execution of the command.
     * @param optionName The name of option.
     * @param isRequired Whether the option is required.
     */
    fun setOptionRequired(optionName: String, isRequired: Boolean): CommandBuilder {
        options[optionName]!!["required"] = isRequired
        return this
    }


    /**
     * Specify whether to enable options auto-completion.
     * @param optionName The name of option.
     * @param hasCompletion Whether the option has auto-completion.
     */
    fun setOptionHasCompletions(optionName: String, hasCompletion: Boolean): CommandBuilder {
        options[optionName]!!["completions"] = hasCompletion
        return this
    }

    fun build(): CommandInfo {
        val noptions = mutableMapOf<String, ImmutableMap<String, Any>>()
        options.forEach { option ->
            noptions[option.key] = ImmutableMap.copyOf(option.value)
        }
        return CommandInfo(commandName, description, isSubCommand, subCommands, ImmutableMap.copyOf(noptions))
    }
}