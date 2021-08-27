package be.encelade.viewer.persistence

import be.encelade.viewer.scene.AssetNodeManager
import be.encelade.viewer.utils.LazyLogging
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature.INDENT_OUTPUT
import com.fasterxml.jackson.module.kotlin.KotlinModule
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.concurrent.thread
import kotlin.text.Charsets.UTF_8

class SavedSceneManager(private val assetNodeManager: AssetNodeManager) : LazyLogging {

    private val jsonMapper = ObjectMapper()
            .registerModule(KotlinModule())
            .configure(INDENT_OUTPUT, true)

    fun persist() {
        val dtos = assetNodeManager
                .listAllSceneNodes()
                .map { sceneNode ->
                    SceneNodeDto(
                            sceneNode.id(),
                            sceneNode.assetNode.file.absolutePath,
                            sceneNode.node.localTranslation,
                            sceneNode.node.localRotation,
                            sceneNode.node.localScale)
                }

        thread {
            val json = jsonMapper.writeValueAsString(dtos)
            Files.write(Paths.get("scene.json"), json.toByteArray(UTF_8))
            logger.info("persisted to scene.json")
        }
    }

}
