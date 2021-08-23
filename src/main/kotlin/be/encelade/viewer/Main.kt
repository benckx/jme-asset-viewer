package be.encelade.viewer

import com.jme3.system.AppSettings
import javax.swing.UIManager

fun main(args: Array<String>) {
    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName())

    val settings = AppSettings(true)
    settings.title = "JME Asset Viewer"
    settings.isFullscreen = false
    settings.samples = 16
    settings.isVSync = false
    settings.isGammaCorrection = false
    settings.setResolution(1280, 720)

    val simpleApp = ViewerJmeApp()
    simpleApp.setSettings(settings)
    simpleApp.isShowSettings = !args.contains("skip-jme")
    simpleApp.isPauseOnLostFocus = false
    simpleApp.start()
}
