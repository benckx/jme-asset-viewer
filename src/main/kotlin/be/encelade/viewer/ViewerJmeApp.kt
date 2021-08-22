package be.encelade.viewer

import be.encelade.chimp.material.MaterialDefinitions
import be.encelade.chimp.utils.ColorHelperUtils.ColorRGBA
import be.encelade.ouistiti.CameraManager
import be.encelade.viewer.scene.SceneNode
import com.jme3.app.SimpleApplication

class ViewerJmeApp : SimpleApplication() {

    private lateinit var cameraManager: CameraManager

    override fun simpleInitApp() {
        // init chimp-utils API for materials
        MaterialDefinitions.load(assetManager)

        // init CameraManager
        cameraManager = CameraManager(this)
        cameraManager.addDefaultKeyMappings()

        // build scene
        viewPort.backgroundColor = ColorRGBA("#1c3064")
        rootNode.attachChild(SceneNode())
    }

    override fun simpleUpdate(tpf: Float) {
        cameraManager.simpleUpdate(tpf)
    }

}
