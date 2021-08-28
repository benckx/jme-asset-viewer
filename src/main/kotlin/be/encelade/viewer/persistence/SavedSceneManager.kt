package be.encelade.viewer.persistence

import be.encelade.viewer.gui.AssetMenu
import be.encelade.viewer.scene.AssetNodeManager
import be.encelade.viewer.scene.SceneNode
import be.encelade.viewer.utils.LazyLogging
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature.INDENT_OUTPUT
import com.fasterxml.jackson.module.kotlin.KotlinModule
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.concurrent.thread
import kotlin.system.measureTimeMillis
import kotlin.text.Charsets.UTF_8

class SavedSceneManager(private val assetNodeManager: AssetNodeManager,
                        private val assetMenu: AssetMenu) : LazyLogging {

    private val jsonMapper = ObjectMapper()
            .registerModule(KotlinModule())
            .registerModule(JmeModule())
            .configure(INDENT_OUTPUT, true)

    fun persistToFile() {
        val sceneNodeDTOs = assetNodeManager
                .listAllSceneNodes()
                .map { sceneNode -> toDto(sceneNode) }

        val sceneDTO = SceneDto(sceneNodeDTOs)

        thread {
            val millis = measureTimeMillis {
                val json = jsonMapper.writeValueAsString(sceneDTO)
                Files.write(sceneFilePath, json.toByteArray(UTF_8))
            }
            logger.info("persisted to $SAVED_SCENE_FILE_NAME in $millis ms.")
        }
    }

    fun loadFromFile() {
        val savedSceneFile = File(SAVED_SCENE_FILE_NAME)
        if (savedSceneFile.exists()) {
            val json = Files.readAllLines(sceneFilePath).joinToString("\n")
            jsonMapper
                    .readValue(json, SceneDto::class.java)!!
                    .nodes
                    .forEach { sceneNodeDto ->
                        val file = File(sceneNodeDto.fileName)
                        if (file.exists()) {
                            val sceneNode = assetNodeManager.importAsset(file)
                            sceneNode.node.localTranslation = sceneNodeDto.translation
                            sceneNode.node.localRotation = sceneNodeDto.rotation
                            sceneNode.node.localScale = sceneNodeDto.scale
                            assetMenu.addToAssetList(sceneNode)
                        } else {
                            logger.error("$file does not exist")
                        }
                    }

            assetMenu.disableFocus()
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

    private companion object {

        const val SAVED_SCENE_FILE_NAME = "scene.json"
        val sceneFilePath: Path = Paths.get(SAVED_SCENE_FILE_NAME)

    }

}
