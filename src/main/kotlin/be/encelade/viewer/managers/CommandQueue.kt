package be.encelade.viewer.managers

import be.encelade.viewer.scene.RotationCommand
import be.encelade.viewer.scene.TranslationCommand

class CommandQueue {

    private val translations = mutableListOf<TranslationCommand>()
    private val rotations = mutableListOf<RotationCommand>()

    fun push(command: TranslationCommand) {
        translations += command
    }

    fun push(command: RotationCommand) {
        rotations += command
    }

    fun flushTranslations(): List<TranslationCommand> {
        val result = translations.toList()
        translations.clear()
        return result
    }

    fun flushRotations(): List<RotationCommand> {
        val result = rotations.toList()
        rotations.clear()
        return result
    }

}
