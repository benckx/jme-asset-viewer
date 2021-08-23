package be.encelade.viewer.managers

import com.jme3.app.SimpleApplication
import com.jme3.collision.CollisionResults
import com.jme3.math.Ray
import com.jme3.math.Vector2f

class CursorCollisionsDetector(private val app: SimpleApplication) {

    private val rootNode by lazy { app.rootNode }
    private val camera by lazy { app.camera }
    private val inputManager by lazy { app.inputManager }

    fun detect(): CollisionResults {
        // convert screen click to 3d position
        val cursorPosition = Vector2f(inputManager.cursorPosition)
        val click3d = camera.getWorldCoordinates(cursorPosition, 0f).clone()
        val dir = camera.getWorldCoordinates(cursorPosition, 1f).subtractLocal(click3d).normalizeLocal()

        // aim the ray from the clicked spot forwards.
        val ray = Ray(click3d, dir)

        // collect intersections between ray and all nodes in results list.
        val results = CollisionResults()
        rootNode.collideWith(ray, results)
        return results
    }

    companion object {

        fun findObjectIds(collisionResults: CollisionResults): Set<String> {
            return collisionResults
                    .mapNotNull { collisionResult -> collisionResult.geometry.name }
                    .toSet()
        }

    }

}
