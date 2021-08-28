package be.encelade.viewer.persistence

import be.encelade.chimp.tpf.TpfAccumulable
import be.encelade.chimp.tpf.TpfAccumulator
import be.encelade.viewer.SAVED_SCENE_FILE_NAME
import be.encelade.viewer.scene.AssetNodeManager
import be.encelade.viewer.scene.SceneNode
import be.encelade.viewer.utils.LazyLogging
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature.INDENT_OUTPUT
import com.fasterxml.jackson.module.kotlin.KotlinModule
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.concurrent.thread
import kotlin.system.measureTimeMillis
import kotlin.text.Charsets.UTF_8

class SavedSceneWriter(private val assetNodeManager: AssetNodeManager) : LazyLogging, TpfAccumulable {

    private val jsonMapper = ObjectMapper()
            .registerModule(KotlinModule())
            .registerModule(JmeModule())
            .configure(INDENT_OUTPUT, true)

    private var needPersist = false

    override val tpfAccumulator = TpfAccumulator(0.50f) {
        writeToFile()
    }

    /**
     * Schedule a persist operation, that will be done async (in 2 sec. max).
     */
    fun requestWriteToFile() {
        needPersist = true
    }

    private fun writeToFile() {
        val sceneNodeDTOs = assetNodeManager
                .listAllSceneNodes()
                .map { sceneNode -> toDto(sceneNode) }

        val sceneDTO = SceneDto(sceneNodeDTOs)

        thread {
            val millis = measureTimeMillis {
                val json = jsonMapper.writeValueAsString(sceneDTO)
                Files.write(Paths.get(SAVED_SCENE_FILE_NAME), json.toByteArray(UTF_8))
            }
            logger.info("persisted to $SAVED_SCENE_FILE_NAME in $millis ms.")
            needPersist = false
        }
    }

    private fun toDto(sceneNode: SceneNode): SceneNodeDto {
        return SceneNodeDto(
                sceneNode.id(),
                sceneNode.assetNode.file.absolutePath,
                sceneNode.node.localTranslation,
                sceneNode.node.localRotation,
                sceneNode.node.localScale)
    }

}
