package be.encelade.viewer.scene

import com.jme3.math.Quaternion
import com.jme3.math.Vector3f
import com.jme3.scene.Node

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
