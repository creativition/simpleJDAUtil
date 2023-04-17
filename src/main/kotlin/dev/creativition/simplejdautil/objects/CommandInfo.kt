package dev.creativition.simplejdautil.objects

import com.google.common.collect.ImmutableMap

data class CommandInfo(
    val commandName: String,
    val description: String,
    val isSubCommand: Boolean = false,
    val subCommands: List<CommandInfo>,
    val options: ImmutableMap<String, ImmutableMap<String, Any>>
) {

}