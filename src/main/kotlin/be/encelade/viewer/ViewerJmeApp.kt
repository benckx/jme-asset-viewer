package be.encelade.viewer

import be.encelade.chimp.material.MaterialDefinitions
import be.encelade.chimp.utils.ColorHelperUtils.ColorRGBA
import be.encelade.ouistiti.CameraManager
import be.encelade.viewer.input.MyActionListener
import be.encelade.viewer.input.MyActionListener.Companion.MOUSE_CLICK
import be.encelade.viewer.managers.MouseInputManager
import be.encelade.viewer.managers.SceneManager
import be.encelade.viewer.menus.AssetMenu
import be.encelade.viewer.scene.DecorNode
import be.encelade.viewer.utils.LazyLogging
import be.encelade.viewer.utils.PropertiesFile
import com.jme3.app.SimpleApplication
import com.jme3.input.MouseInput.BUTTON_LEFT
import com.jme3.input.controls.MouseButtonTrigger
import com.jme3.light.AmbientLight
import com.jme3.light.DirectionalLight
import com.jme3.math.ColorRGBA.White
import com.jme3.math.Vector3f
import com.jme3.shadow.DirectionalLightShadowRenderer
import kotlin.system.exitProcess

class ViewerJmeApp : SimpleApplication(), LazyLogging {

    private lateinit var cameraManager: CameraManager
    private lateinit var mouseInputManager: MouseInputManager
    private lateinit var assetMenu: AssetMenu

    override fun simpleInitApp() {
        // init chimp-utils API for materials
        MaterialDefinitions.load(assetManager)

        // init CameraManager
        cameraManager = CameraManager(this)
        cameraManager.addDefaultKeyMappings()

        // build scene
        viewPort.backgroundColor = ColorRGBA("#1c3064")
        rootNode.attachChild(DecorNode())
        addLighting()

        // properties
        val propertiesFile = PropertiesFile("preferences.properties")

        // load menus and managers
        val sceneManager = SceneManager(this)
        assetMenu = AssetMenu(propertiesFile, sceneManager)

        // input
        mouseInputManager = MouseInputManager(this)
        val actionListener = MyActionListener(mouseInputManager, sceneManager, assetMenu)
        inputManager.addListener(actionListener, MOUSE_CLICK)
        inputManager.addMapping(MOUSE_CLICK, MouseButtonTrigger(BUTTON_LEFT))
    }

    override fun simpleUpdate(tpf: Float) {
        cameraManager.simpleUpdate(tpf)
        mouseInputManager.simpleUpdate(tpf)
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
