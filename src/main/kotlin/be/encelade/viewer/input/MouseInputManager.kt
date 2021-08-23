package be.encelade.viewer.input

import be.encelade.chimp.tpf.TpfReceiver
import be.encelade.viewer.utils.LazyLogging
import com.jme3.app.SimpleApplication

class MouseInputManager(app: SimpleApplication) : TpfReceiver, LazyLogging {

    private val collisionsDetector = CursorCollisionsDetector(app)
    private var collisionIds = listOf<String>()

    override fun simpleUpdate(tpf: Float) {
        collisionIds = collisionsDetector.detectGeometryNames()
    }

    fun collisionIds(): List<String> {
        return collisionIds.toList()
    }

}
