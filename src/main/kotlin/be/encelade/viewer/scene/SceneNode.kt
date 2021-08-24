package be.encelade.viewer.scene

import com.jme3.scene.Node

/**
 * Handle an [AssetNode] (a reference) and its corresponding scene [Node] as one object.
 */
data class SceneNode(val assetNode: AssetNode, val node: Node)
