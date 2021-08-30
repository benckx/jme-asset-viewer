package be.encelade.viewer.commands

// FIXME: callback is always copy pasted
data class DeleteAssetNodeCommand(val id: String, val callback: () -> Unit)
