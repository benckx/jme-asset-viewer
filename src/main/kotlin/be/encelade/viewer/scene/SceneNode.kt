package be.encelade.viewer.scene

import com.jme3.scene.Node

/**
 * Handle an [AssetNode] (a reference) and its corresponding scene [Node] as one object.
 */
data class SceneNode(val assetNode: AssetNode, val node: Node) {

    fun id() = assetNode.id

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SceneNode

        if (assetNode != other.assetNode) return false

        return true
    }

    override fun hashCode(): Int {
        return assetNode.hashCode()
    }

}
