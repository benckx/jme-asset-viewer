package be.encelade.viewer.persistence

import be.encelade.viewer.gui.AssetMenu
import be.encelade.viewer.scene.AssetNodeManager
import be.encelade.viewer.scene.SceneNode
import be.encelade.viewer.utils.LazyLogging
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature.INDENT_OUTPUT
import com.fasterxml.jackson.module.kotlin.KotlinModule
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.concurrent.thread
import kotlin.text.Charsets.UTF_8

class SavedSceneManager(private val assetNodeManager: AssetNodeManager,
                        private val assetMenu: AssetMenu) : LazyLogging {

    private val jsonMapper = ObjectMapper()
            .registerModule(KotlinModule())
            .registerModule(JmeModule())
            .configure(INDENT_OUTPUT, true)

    fun persistToFile() {
        val dtos = assetNodeManager
                .listAllSceneNodes()
                .map { sceneNode -> toDto(sceneNode) }

        thread {
            val json = jsonMapper.writeValueAsString(dtos)
            Files.write(Paths.get(SAVED_SCENE_FILE_NAME), json.toByteArray(UTF_8))
            logger.info("persisted to $SAVED_SCENE_FILE_NAME")
        }
    }

    fun loadFromFile() {
        val savedSceneFile = File(SAVED_SCENE_FILE_NAME)
        if (savedSceneFile.exists()) {
            val json = Files.readAllLines(Paths.get(SAVED_SCENE_FILE_NAME)).joinToString("\n")
            val typeRef = object : TypeReference<List<SceneNodeDto>>() {}
            jsonMapper
                    .readValue(json, typeRef)!!
                    .forEach { sceneNodeDto ->
                        val file = File(sceneNodeDto.fileName)
                        val sceneNode = assetNodeManager.importAsset(file)
                        sceneNode.node.localTranslation = sceneNodeDto.translation
                        sceneNode.node.localRotation = sceneNodeDto.rotation
                        sceneNode.node.localScale = sceneNodeDto.scale
                        assetMenu.addToAssetList(sceneNode)
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

    }

}
