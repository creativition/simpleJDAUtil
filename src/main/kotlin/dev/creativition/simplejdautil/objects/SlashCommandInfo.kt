package dev.creativition.simplejdautil.objects

import com.google.common.collect.ImmutableMap

/**
 * Collective class for slash command information.
 */
data class SlashCommandInfo(
    val commandName: String,
    val description: String,
    val isSubCommand: Boolean = false,
    val subCommands: List<SlashCommandInfo>,
    val options: ImmutableMap<String, SlashCommandOption>
) {

}