package be.encelade.viewer

import be.encelade.chimp.material.MaterialDefinitions
import be.encelade.chimp.utils.ColorHelperUtils.ColorRGBA
import be.encelade.ouistiti.CameraManager
import be.encelade.viewer.commands.CommandQueue
import be.encelade.viewer.gui.AssetMenu
import be.encelade.viewer.input.MouseClickActionListener
import be.encelade.viewer.input.MouseClickActionListener.Companion.LEFT_CLICK
import be.encelade.viewer.input.MouseInputManager
import be.encelade.viewer.persistence.SavedSceneReader
import be.encelade.viewer.persistence.SavedSceneWriter
import be.encelade.viewer.scene.AssetNodeManager
import be.encelade.viewer.scene.BoundingBoxManager
import be.encelade.viewer.scene.CommandExecutor
import be.encelade.viewer.scene.DecorNode
import be.encelade.viewer.utils.PropertiesFile
import be.encelade.viewer.utils.PropertiesKey.HEIGHT
import be.encelade.viewer.utils.PropertiesKey.WIDTH
import com.jme3.app.SimpleApplication
import com.jme3.input.MouseInput.BUTTON_LEFT
import com.jme3.input.controls.MouseButtonTrigger
import com.jme3.light.AmbientLight
import com.jme3.light.DirectionalLight
import com.jme3.math.ColorRGBA.White
import com.jme3.math.Vector3f
import com.jme3.shadow.DirectionalLightShadowRenderer
import java.awt.Point
import java.awt.Toolkit
import kotlin.system.exitProcess

class ViewerJmeApp(private val properties: PropertiesFile,
                   private val lightingEnabled: Boolean) :
        SimpleApplication() {

    private lateinit var cameraManager: CameraManager

    private val mouseInputManager = MouseInputManager(this)
    private val assetNodeManager = AssetNodeManager(this)
    private val boundingBoxManager = BoundingBoxManager(this)
    private val savedSceneWriter = SavedSceneWriter(assetNodeManager)

    private val commandQueue = CommandQueue()
    private val commandExecutor = CommandExecutor(this, commandQueue, assetNodeManager, boundingBoxManager, savedSceneWriter)

    private lateinit var assetMenu: AssetMenu

    override fun simpleInitApp() {
        if (settings.isFullscreen) {
            throw IllegalArgumentException("The Viewer can not work in fullscreen mode, as you wouldn't see the GUI")
        }

        // location of JME windows
        // this assumes the JME windows appears centered on the screen
        val screenDimension = Toolkit.getDefaultToolkit().screenSize
        val x = ((screenDimension.width - settings.width) / 2)
        val y = ((screenDimension.height - settings.height) / 2)
        val jmeLocation = Point(x, y)

        // input and GUI
        assetMenu = AssetMenu(properties, commandQueue, jmeLocation)

        // persist selected dimension
        properties.persistProperty(WIDTH, settings.width.toString())
        properties.persistProperty(HEIGHT, settings.height.toString())

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

        // actions and mappings
        val mouseClickActionListener = MouseClickActionListener(rootNode, mouseInputManager, assetNodeManager, boundingBoxManager, assetMenu)
        inputManager.addListener(mouseClickActionListener, LEFT_CLICK)
        inputManager.addMapping(LEFT_CLICK, MouseButtonTrigger(BUTTON_LEFT))

        // re-load if exists
        SavedSceneReader(assetNodeManager, assetMenu).loadFromFile()
    }

    override fun simpleUpdate(tpf: Float) {
        cameraManager.simpleUpdate(tpf)
        mouseInputManager.simpleUpdate(tpf)
        commandExecutor.simpleUpdate(tpf)
        savedSceneWriter.simpleUpdate(tpf)
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
