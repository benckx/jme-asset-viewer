package be.encelade.viewer

import be.encelade.viewer.gui.GuiUtils.buildFileChooser
import be.encelade.viewer.utils.PropertiesFile
import be.encelade.viewer.utils.PropertiesKey.*
import com.jme3.system.AppSettings
import org.apache.commons.lang3.StringUtils.isNumeric
import org.slf4j.LoggerFactory
import java.awt.Dimension
import javax.swing.JFileChooser
import javax.swing.UIManager
import kotlin.concurrent.thread
import kotlin.system.measureTimeMillis

const val PROPERTIES_FILE = "preferences.properties"
const val DEFAULT_WIDTH = 1280
const val DEFAULT_HEIGHT = 720

const val SAVED_SCENE_FILE_NAME = "scene.json"

private val logger = LoggerFactory.getLogger("Main")
private val properties = PropertiesFile(PROPERTIES_FILE)

fun main(args: Array<String>) {
    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName())
    preLoadFileChooser()

    val settings = AppSettings(true)
    settings.title = "JME Asset Viewer"
    settings.isFullscreen = false
    settings.samples = 16
    settings.isVSync = false
    settings.isGammaCorrection = false

    val dimension = getDimension()
    settings.setResolution(dimension.width, dimension.height)

    val app = ViewerJmeApp(properties, !args.contains("no-lighting"))
    app.setSettings(settings)
    app.isShowSettings = !args.contains("skip-jme")
    app.isPauseOnLostFocus = false
    app.start()
}

/**
 * [JFileChooser] first execution is very slow on Windows, so we pre-load it asynchronously.
 */
private fun preLoadFileChooser() {
    thread {
        logger.info("preloading JFileChooser")
        val millis = measureTimeMillis {
            val fileChooser: JFileChooser = buildFileChooser(properties.getProperty(DEFAULT_FOLDER))
            fileChooser.isVisible = false
        }
        logger.info("JFileChooser pre-loaded in $millis ms.")
    }
}

private fun getDimension(): Dimension {
    val persistedWidth = properties.getProperty(WIDTH)
    val persistedHeight = properties.getProperty(HEIGHT)
    val width = if (isNumeric(persistedWidth)) persistedWidth!!.toInt() else DEFAULT_WIDTH
    val height = if (isNumeric(persistedHeight)) persistedHeight!!.toInt() else DEFAULT_HEIGHT
    return Dimension(width, height)
}
