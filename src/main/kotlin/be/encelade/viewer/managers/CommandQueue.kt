package be.encelade.viewer.managers

import be.encelade.viewer.managers.commands.RotationCommand
import be.encelade.viewer.managers.commands.ScaleCommand
import be.encelade.viewer.managers.commands.TranslationCommand

class CommandQueue {

    private val translationCommands = mutableListOf<TranslationCommand>()
    private val rotationCommands = mutableListOf<RotationCommand>()
    private val scaleCommands = mutableListOf<ScaleCommand>()

    fun push(command: TranslationCommand) {
        translationCommands += command
    }

    fun push(command: RotationCommand) {
        rotationCommands += command
    }

    fun push(command: ScaleCommand) {
        scaleCommands += command
    }

    fun flushTranslations(): List<TranslationCommand> {
        val result = translationCommands.toList()
        translationCommands.clear()
        return result
    }

    fun flushRotations(): List<RotationCommand> {
        val result = rotationCommands.toList()
        rotationCommands.clear()
        return result
    }

    fun flushScales(): List<ScaleCommand> {
        val result = scaleCommands.toList()
        scaleCommands.clear()
        return result
    }

}
