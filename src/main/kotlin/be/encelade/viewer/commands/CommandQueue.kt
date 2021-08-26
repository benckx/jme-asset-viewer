package be.encelade.viewer.commands

import be.encelade.viewer.utils.LazyLogging

/**
 * Send commands from the GUI to the JME scene; ensuring the Scene is modified only by the JME thread.
 * The commands will be picked up the [com.jme3.app.SimpleApplication] during simpleUpdate
 */
class CommandQueue : LazyLogging {

    private val importAssetCommands = mutableListOf<ImportAssetCommand>()
    private val deleteAssetNodeCommands = mutableListOf<DeleteAssetNodeCommand>()
    private val cloneCommands = mutableListOf<CloneCommand>()

    private val translationCommands = mutableListOf<TranslationCommand>()
    private val rotationCommands = mutableListOf<RotationCommand>()
    private val scaleCommands = mutableListOf<ScaleCommand>()

    fun push(command: ImportAssetCommand) {
        importAssetCommands += command
        logger.debug("pushed $command")
    }

    fun push(command: DeleteAssetNodeCommand) {
        deleteAssetNodeCommands += command
        logger.debug("pushed $command")
    }

    fun push(command: CloneCommand) {
        cloneCommands += command
        logger.debug("pushed $command")
    }

    fun push(command: TranslationCommand) {
        translationCommands += command
        logger.debug("pushed $command")
    }

    fun push(command: RotationCommand) {
        rotationCommands += command
        logger.debug("pushed $command")
    }

    fun push(command: ScaleCommand) {
        scaleCommands += command
        logger.debug("pushed $command")
    }

    fun flushImportCommands() = flushCommands(importAssetCommands)

    fun flushDeleteCommands() = flushCommands(deleteAssetNodeCommands)

    fun flushCloneCommands() = flushCommands(cloneCommands)

    fun flushTranslationCommands() = flushCommands(translationCommands)

    fun flushRotationCommands() = flushCommands(rotationCommands)

    fun flushScaleCommands() = flushCommands(scaleCommands)

    private companion object {

        fun <C> flushCommands(commands: MutableList<C>): List<C> {
            return if (commands.isNotEmpty()) {
                val result = commands.toList()
                commands.clear()
                result
            } else {
                listOf()
            }
        }

    }

}
