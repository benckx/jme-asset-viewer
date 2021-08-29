package be.encelade.viewer.persistence

import be.encelade.viewer.SAVED_SCENE_FILE_NAME
import be.encelade.viewer.gui.AssetMenu
import be.encelade.viewer.gui.LibraryMenu
import be.encelade.viewer.scene.AssetNodeManager
import be.encelade.viewer.utils.LazyLogging
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths

class SavedSceneReader(private val assetNodeManager: AssetNodeManager,
                       private val assetMenu: AssetMenu,
                       private val libraryMenu: LibraryMenu) : LazyLogging {

    private val jsonMapper = ObjectMapper()
            .registerModule(KotlinModule())

    fun loadFromFile() {
        val savedSceneFile = File(SAVED_SCENE_FILE_NAME)
        if (savedSceneFile.exists()) {
            val json = Files.readAllLines(Paths.get(SAVED_SCENE_FILE_NAME)).joinToString("\n")
            val savedScene = jsonMapper.readValue(json, SceneDto::class.java)!!

            savedScene
                    .nodes
                    .forEach { sceneNodeDto ->
                        val file = File(sceneNodeDto.fileName)
                        logger.info("loading $file...")
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

            savedScene
                    .libraryFiles
                    .map { fileName -> File(fileName) }
                    .forEach { file ->
                        if (file.exists()) {
                            libraryMenu.addFileToLibrary(file)
                        } else {
                            logger.error("$file does not exist")
                        }
                    }

            assetMenu.disableFocus()
            libraryMenu.disableFocus()
        }
    }

}
