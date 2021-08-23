package be.encelade.viewer.scene

import com.jme3.math.Quaternion
import com.jme3.math.Vector3f
import com.jme3.scene.Geometry
import com.jme3.scene.Node
import com.jme3.scene.Spatial

/**
 * Reference to the imported asset in the scene.
 *
 * @param id is used to identified which asset has been selected by clicking in the scene
 * @param node is the actual [Node] in the scene, that only contains 1 entry (the asset as a [Spatial], a [Geometry] or a [Node]).
 */
data class AssetNode(val id: String,
                     val fileName: String,
                     val node: Node) {

    fun localTranslation(): Vector3f {
        return node.localTranslation
    }

    fun localRotation(): Quaternion {
        return node.localRotation
    }

}
