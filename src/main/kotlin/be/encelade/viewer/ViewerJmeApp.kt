package be.encelade.viewer

import be.encelade.chimp.material.MaterialDefinitions
import be.encelade.chimp.utils.ColorHelperUtils.ColorRGBA
import be.encelade.ouistiti.CameraManager
import be.encelade.viewer.scene.SceneNode
import com.jme3.app.SimpleApplication
import com.jme3.light.AmbientLight
import com.jme3.light.DirectionalLight
import com.jme3.math.ColorRGBA.White
import com.jme3.math.Vector3f
import com.jme3.shadow.DirectionalLightShadowRenderer
import kotlin.system.exitProcess

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
        addLighting()
    }

    override fun simpleUpdate(tpf: Float) {
        cameraManager.simpleUpdate(tpf)
    }

    override fun destroy() {
        super.destroy()
        exitProcess(0)
    }

    private fun addLighting() {
        val lightDirection = Vector3f(-1f, 1f, -2f).normalizeLocal()

        val light = DirectionalLight()
        light.direction = lightDirection
        light.color = White
        rootNode.addLight(light)

        val shadowMapSize = 1024 * 10

        val shadowRenderer = DirectionalLightShadowRenderer(assetManager, shadowMapSize, 4)
        shadowRenderer.light = light
        viewPort.addProcessor(shadowRenderer)

        val al = AmbientLight()
        al.color = White.mult(0.12f)
        rootNode.addLight(al)
    }

}
