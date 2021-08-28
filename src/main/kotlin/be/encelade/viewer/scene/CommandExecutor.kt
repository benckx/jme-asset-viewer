package be.encelade.viewer.scene

import be.encelade.chimp.tpf.TpfReceiver
import be.encelade.viewer.commands.CommandQueue
import be.encelade.viewer.persistence.SavedSceneWriter
import be.encelade.viewer.utils.LazyLogging
import com.jme3.app.SimpleApplication
import com.jme3.scene.Node

class CommandExecutor(app: SimpleApplication,
                      private val commandQueue: CommandQueue,
                      private val assetNodeManager: AssetNodeManager,
                      private val boundingBoxManager: BoundingBoxManager,
                      private val savedSceneWriter: SavedSceneWriter) : TpfReceiver, LazyLogging {

    private val rootNode by lazy { app.rootNode }

    override fun simpleUpdate(tpf: Float) {
        commandQueue.flushImportCommands().forEach { command ->
            logger.debug("executing $command")
            val sceneNode = assetNodeManager.importAsset(command.file)
            boundingBoxManager.drawBoundingBox(sceneNode)
            command.callback(sceneNode)
            savedSceneWriter.persistToFile()
        }

        commandQueue.flushDeleteCommands().forEach { command ->
            logger.debug("executing $command")
            assetNodeManager.delete(command.id)
            boundingBoxManager.deleteBoundingBox()
            command.callback()
            savedSceneWriter.persistToFile()
        }

        commandQueue.flushCloneCommands().forEach { command ->
            logger.debug("executing $command")
            assetNodeManager.clone(command.id)?.let { sceneNode ->
                sceneNode.node.move(1f, 1f, 0f)
                boundingBoxManager.drawBoundingBox(sceneNode)
                command.callback(sceneNode)
                savedSceneWriter.persistToFile()
            }
        }

        commandQueue.flushSelectionCommands().forEach { command ->
            logger.debug("executing $command")
            boundingBoxManager.drawBoundingBox(command.sceneNode)
            command.callback()
        }

        commandQueue.flushTranslationCommands().forEach { command ->
            logger.debug("executing $command")
            rootNode.getChild(command.id)?.let { spatial ->
                spatial.localTranslation = command.translation
                boundingBoxManager.reDrawBoundingBox(spatial as Node)
                savedSceneWriter.persistToFile()
            }
        }

        commandQueue.flushRotationCommands().forEach { command ->
            logger.debug("executing $command")
            rootNode.getChild(command.id)?.let { spatial ->
                spatial.localRotation = command.rotation
                boundingBoxManager.reDrawBoundingBox(spatial as Node)
                savedSceneWriter.persistToFile()
            }
        }

        commandQueue.flushScaleCommands().forEach { command ->
            logger.debug("executing $command")
            rootNode.getChild(command.id)?.let { spatial ->
                spatial.localScale = command.toVector3f()
                boundingBoxManager.reDrawBoundingBox(spatial as Node)
                savedSceneWriter.persistToFile()
            }
        }
    }

}
