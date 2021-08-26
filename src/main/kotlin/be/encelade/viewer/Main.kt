package be.encelade.viewer

import be.encelade.viewer.utils.PropertiesFile
import be.encelade.viewer.utils.PropertiesKey.HEIGHT
import be.encelade.viewer.utils.PropertiesKey.WIDTH
import com.jme3.system.AppSettings
import org.apache.commons.lang3.StringUtils.isNumeric
import javax.swing.UIManager

const val PROPERTIES_FILE = "preferences.properties"
const val DEFAULT_WIDTH = 1280
const val DEFAULT_HEIGHT = 720

fun main(args: Array<String>) {
    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName())

    val properties = PropertiesFile(PROPERTIES_FILE)
    val persistedWidth = properties.getProperty(WIDTH)
    val persistedHeight = properties.getProperty(HEIGHT)
    val width = if (persistedWidth != null && isNumeric(persistedWidth)) persistedWidth.toInt() else DEFAULT_WIDTH
    val height = if (persistedHeight != null && isNumeric(persistedHeight)) persistedHeight.toInt() else DEFAULT_HEIGHT

    val settings = AppSettings(true)
    settings.title = "JME Asset Viewer"
    settings.isFullscreen = false
    settings.samples = 16
    settings.isVSync = false
    settings.isGammaCorrection = false
    settings.setResolution(width, height)

    val simpleApp = ViewerJmeApp(properties, !args.contains("no-lighting"))
    simpleApp.setSettings(settings)
    simpleApp.isShowSettings = !args.contains("skip-jme")
    simpleApp.isPauseOnLostFocus = false
    simpleApp.start()
}
