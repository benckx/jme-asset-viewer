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
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.system.measureTimeMillis
import kotlin.text.Charsets.UTF_8

class SavedSceneWriter(private val assetNodeManager: AssetNodeManager) : LazyLogging, TpfAccumulable {

    private val jsonMapper = ObjectMapper()
            .registerModule(KotlinModule())
            .registerModule(JmeModule())
            .configure(INDENT_OUTPUT, true)

    private var mustWrite = AtomicBoolean(false)

    override val tpfAccumulator = TpfAccumulator(0.50f) {
        if (mustWrite.get()) {
            writeToFile()
        }
    }

    /**
     * Schedule a persist operation, that will be done async (in 2 sec. max).
     */
    fun requestWriteToFile() {
        mustWrite.set(true)
    }

    private fun writeToFile() {
        val sceneNodeDTOs = assetNodeManager
                .listAllSceneNodes()
                .map { sceneNode -> toDto(sceneNode) }

        val sceneDTO = SceneDto(sceneNodeDTOs, listOf())
        val millis = measureTimeMillis {
            val json = jsonMapper.writeValueAsString(sceneDTO)
            Files.write(Paths.get(SAVED_SCENE_FILE_NAME), json.toByteArray(UTF_8))
        }
        logger.info("scene saved to $SAVED_SCENE_FILE_NAME in $millis ms.")
        mustWrite.set(false)
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
