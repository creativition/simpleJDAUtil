package dev.creativition.simplejdautil.objects

import net.dv8tion.jda.api.interactions.commands.OptionType

data class SlashCommandOption(
    val optionName: String,
    val optionDescription: String,
    val optionType: OptionType,
    val isRequired: Boolean,
    val hasCompletions: Boolean
) {
}