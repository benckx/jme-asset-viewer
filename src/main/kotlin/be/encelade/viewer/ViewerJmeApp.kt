package be.encelade.viewer

import be.encelade.chimp.material.MaterialDefinitions
import be.encelade.chimp.utils.ColorHelperUtils.ColorRGBA
import be.encelade.ouistiti.CameraManager
import be.encelade.viewer.commands.CommandQueue
import be.encelade.viewer.gui.AssetMenu
import be.encelade.viewer.input.MouseInputManager
import be.encelade.viewer.input.MyActionListener
import be.encelade.viewer.input.MyActionListener.Companion.MOUSE_CLICK
import be.encelade.viewer.scene.AssetNodeManager
import be.encelade.viewer.scene.BoundingBoxManager
import be.encelade.viewer.scene.CommandExecutor
import be.encelade.viewer.scene.DecorNode
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

class ViewerJmeApp(private val lightingEnabled: Boolean) : SimpleApplication() {

    private lateinit var cameraManager: CameraManager

    private val mouseInputManager = MouseInputManager(this)
    private val assetNodeManager = AssetNodeManager(this)
    private val boundingBoxManager = BoundingBoxManager(this)

    private val commandQueue = CommandQueue()
    private val commandExecutor = CommandExecutor(this, commandQueue, assetNodeManager, boundingBoxManager)

    override fun simpleInitApp() {
        if (settings.isFullscreen) {
            throw IllegalArgumentException("The Viewer can not work in fullscreen mode, as you wouldn't see the GUI")
        }

        // init chimp-utils API for materials
        MaterialDefinitions.load(assetManager)

        // init CameraManager
        cameraManager = CameraManager(this)
        cameraManager.addDefaultKeyMappings()

        // build scene
        viewPort.backgroundColor = ColorRGBA("#1c3064")
        rootNode.attachChild(DecorNode())
        if (lightingEnabled) {
            addLighting()
        }

        // properties
        val propertiesFile = PropertiesFile("preferences.properties")

        // input and GUI
        val assetMenu = AssetMenu(propertiesFile, commandQueue)
        val actionListener = MyActionListener(rootNode, mouseInputManager, assetNodeManager, boundingBoxManager, assetMenu)

        // mappings
        inputManager.addListener(actionListener, MOUSE_CLICK)
        inputManager.addMapping(MOUSE_CLICK, MouseButtonTrigger(BUTTON_LEFT))
    }

    override fun simpleUpdate(tpf: Float) {
        cameraManager.simpleUpdate(tpf)
        mouseInputManager.simpleUpdate(tpf)
        commandExecutor.simpleUpdate(tpf)
    }

    override fun destroy() {
        super.destroy()

        // exit upon JME windows stopping
        // otherwise it keeps running with the only GUI
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
