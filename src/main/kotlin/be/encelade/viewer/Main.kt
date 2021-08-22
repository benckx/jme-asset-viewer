package be.encelade.viewer

import be.encelade.viewer.frames.AssetMenu
import be.encelade.viewer.managers.SceneManager
import com.jme3.system.AppSettings

fun main() {
    val settings = AppSettings(true)
    settings.title = "JME asset viewer"
    settings.isFullscreen = false
    settings.samples = 16
    settings.isVSync = false
    settings.isGammaCorrection = false
    settings.setResolution(1280, 720)

    val simpleApp = ViewerJmeApp()
    simpleApp.setSettings(settings)
    simpleApp.isShowSettings = false

    val sceneManager = SceneManager(simpleApp)

    // start
    AssetMenu(sceneManager)
    simpleApp.start()
}
