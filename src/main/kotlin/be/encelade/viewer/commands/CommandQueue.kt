package be.encelade.viewer.commands

/**
 * Ensure the Scene is modified only by the JME thread, so the GUI sends commands that the JME thread will execute,
 * instead of the GUI directly changing the scene.
 */
class CommandQueue {

    private val importAssetCommands = mutableListOf<ImportAssetCommand>()
    private val deleteAssetNodeCommands = mutableListOf<DeleteAssetNodeCommand>()
    private val cloneCommands = mutableListOf<CloneCommand>()

    private val translationCommands = mutableListOf<TranslationCommand>()
    private val rotationCommands = mutableListOf<RotationCommand>()
    private val scaleCommands = mutableListOf<ScaleCommand>()

    fun push(command: ImportAssetCommand) {
        importAssetCommands += command
    }

    fun push(command: DeleteAssetNodeCommand) {
        deleteAssetNodeCommands += command
    }

    fun push(command: CloneCommand) {
        cloneCommands += command
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
