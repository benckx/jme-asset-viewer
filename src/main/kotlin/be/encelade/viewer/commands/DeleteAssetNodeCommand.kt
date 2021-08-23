package be.encelade.viewer.commands

data class DeleteAssetNodeCommand(val id: String, val callback: () -> Unit)
