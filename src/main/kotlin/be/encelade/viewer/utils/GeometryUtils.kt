package be.encelade.viewer.utils

import com.jme3.bounding.BoundingVolume
import com.jme3.scene.Geometry
import com.jme3.scene.Node

// TODO: move to chimps-utils?
object GeometryUtils {

    fun extractBoundingVolume(node: Node): BoundingVolume {
        val volumes = extractBoundingVolumes(node)
        var result = volumes.first()
        volumes.drop(1).forEach { volume ->
            result = result.merge(volume)
        }

        return result
    }

    private fun extractBoundingVolumes(node: Node): List<BoundingVolume> {
        return extractAllGeometries(node)
                .mapNotNull { geometry -> geometry.mesh }
                .mapNotNull { mesh -> mesh.bound }
    }

    private fun extractAllGeometries(node: Node): List<Geometry> {
        val geometries = mutableListOf<Geometry>()

        node.children.filterIsInstance<Geometry>().forEach { geometry ->
            geometries += geometry
        }

        node.children.filterIsInstance<Node>().forEach { childNode ->
            geometries += extractAllGeometries(childNode)
        }

        return geometries
    }

}
