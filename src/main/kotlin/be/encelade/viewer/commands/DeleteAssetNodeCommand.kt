package be.encelade.viewer.commands

import be.encelade.viewer.gui.AssetMenu
import be.encelade.viewer.scene.AssetNode

data class DeleteAssetNodeCommand(val id: String, val callback: () -> Unit) {

    constructor(assetNode: AssetNode, assetMenu: AssetMenu) :
            this(assetNode.id, {
                assetMenu.removeFromAssetList(assetNode.id)
                assetMenu.disableFocus()
            })

}
