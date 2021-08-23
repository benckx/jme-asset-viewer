package be.encelade.viewer.managers

import be.encelade.viewer.managers.commands.ImportAssetCommand
import be.encelade.viewer.managers.commands.RotationCommand
import be.encelade.viewer.managers.commands.ScaleCommand
import be.encelade.viewer.managers.commands.TranslationCommand

/**
 * Ensure the Scene is modified only by the JME thread.
 */
class CommandQueue {

    private val importAssetCommands = mutableListOf<ImportAssetCommand>()
    private val translationCommands = mutableListOf<TranslationCommand>()
    private val rotationCommands = mutableListOf<RotationCommand>()
    private val scaleCommands = mutableListOf<ScaleCommand>()

    fun push(command: ImportAssetCommand) {
        importAssetCommands += command
    }

    fun push(command: TranslationCommand) {
        translationCommands += command
    }

    fun push(command: RotationCommand) {
        rotationCommands += command
    }

    fun push(command: ScaleCommand) {
        scaleCommands += command
    }

    fun flushImports() = flushCommands(importAssetCommands)

    fun flushTranslations() = flushCommands(translationCommands)

    fun flushRotations() = flushCommands(rotationCommands)

    fun flushScales() = flushCommands(scaleCommands)

    private companion object {

        fun <C> flushCommands(commands: MutableList<C>): List<C> {
            val result = commands.toList()
            commands.clear()
            return result
        }

    }

}
