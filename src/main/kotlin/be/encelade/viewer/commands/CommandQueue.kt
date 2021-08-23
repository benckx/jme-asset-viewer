package be.encelade.viewer.commands

/**
 * Ensure the Scene is modified only by the JME thread.
 */
class CommandQueue {

    private val importAssetCommands = mutableListOf<ImportAssetCommand>()
    private val deleteAssetNodeCommands = mutableListOf<DeleteAssetNodeCommand>()
    private val translationCommands = mutableListOf<TranslationCommand>()
    private val rotationCommands = mutableListOf<RotationCommand>()
    private val scaleCommands = mutableListOf<ScaleCommand>()

    fun push(command: ImportAssetCommand) {
        importAssetCommands += command
    }

    fun push(command: DeleteAssetNodeCommand) {
        deleteAssetNodeCommands += command
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

    fun flushDeletes() = flushCommands(deleteAssetNodeCommands)

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
