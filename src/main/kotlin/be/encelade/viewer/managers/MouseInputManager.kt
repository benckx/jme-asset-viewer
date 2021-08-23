package be.encelade.viewer.managers

import be.encelade.chimp.tpf.TpfReceiver
import be.encelade.viewer.managers.CursorCollisionsDetector.Companion.findObjectIds
import be.encelade.viewer.utils.LazyLogging
import com.jme3.app.SimpleApplication

class MouseInputManager(app: SimpleApplication) : TpfReceiver, LazyLogging {

    private val collisionsDetector = CursorCollisionsDetector(app)
    private var collisionIds = listOf<String>()

    override fun simpleUpdate(tpf: Float) {
        val collisionResults = collisionsDetector.detect()
        collisionIds = findObjectIds(collisionResults).filter { it != "FLOOR" }.distinct()
        if (collisionIds.isNotEmpty()) {
            logger.info("collisions: ${collisionIds.joinToString(", ")}")
        }
    }

    fun collisionIds(): List<String> {
        return collisionIds.toList()
    }

}
