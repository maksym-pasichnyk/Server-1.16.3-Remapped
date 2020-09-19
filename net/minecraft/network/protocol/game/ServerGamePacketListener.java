package net.minecraft.network.protocol.game;

import net.minecraft.network.PacketListener;

public interface ServerGamePacketListener extends PacketListener {
  void handleAnimate(ServerboundSwingPacket paramServerboundSwingPacket);
  
  void handleChat(ServerboundChatPacket paramServerboundChatPacket);
  
  void handleClientCommand(ServerboundClientCommandPacket paramServerboundClientCommandPacket);
  
  void handleClientInformation(ServerboundClientInformationPacket paramServerboundClientInformationPacket);
  
  void handleContainerAck(ServerboundContainerAckPacket paramServerboundContainerAckPacket);
  
  void handleContainerButtonClick(ServerboundContainerButtonClickPacket paramServerboundContainerButtonClickPacket);
  
  void handleContainerClick(ServerboundContainerClickPacket paramServerboundContainerClickPacket);
  
  void handlePlaceRecipe(ServerboundPlaceRecipePacket paramServerboundPlaceRecipePacket);
  
  void handleContainerClose(ServerboundContainerClosePacket paramServerboundContainerClosePacket);
  
  void handleCustomPayload(ServerboundCustomPayloadPacket paramServerboundCustomPayloadPacket);
  
  void handleInteract(ServerboundInteractPacket paramServerboundInteractPacket);
  
  void handleKeepAlive(ServerboundKeepAlivePacket paramServerboundKeepAlivePacket);
  
  void handleMovePlayer(ServerboundMovePlayerPacket paramServerboundMovePlayerPacket);
  
  void handlePlayerAbilities(ServerboundPlayerAbilitiesPacket paramServerboundPlayerAbilitiesPacket);
  
  void handlePlayerAction(ServerboundPlayerActionPacket paramServerboundPlayerActionPacket);
  
  void handlePlayerCommand(ServerboundPlayerCommandPacket paramServerboundPlayerCommandPacket);
  
  void handlePlayerInput(ServerboundPlayerInputPacket paramServerboundPlayerInputPacket);
  
  void handleSetCarriedItem(ServerboundSetCarriedItemPacket paramServerboundSetCarriedItemPacket);
  
  void handleSetCreativeModeSlot(ServerboundSetCreativeModeSlotPacket paramServerboundSetCreativeModeSlotPacket);
  
  void handleSignUpdate(ServerboundSignUpdatePacket paramServerboundSignUpdatePacket);
  
  void handleUseItemOn(ServerboundUseItemOnPacket paramServerboundUseItemOnPacket);
  
  void handleUseItem(ServerboundUseItemPacket paramServerboundUseItemPacket);
  
  void handleTeleportToEntityPacket(ServerboundTeleportToEntityPacket paramServerboundTeleportToEntityPacket);
  
  void handleResourcePackResponse(ServerboundResourcePackPacket paramServerboundResourcePackPacket);
  
  void handlePaddleBoat(ServerboundPaddleBoatPacket paramServerboundPaddleBoatPacket);
  
  void handleMoveVehicle(ServerboundMoveVehiclePacket paramServerboundMoveVehiclePacket);
  
  void handleAcceptTeleportPacket(ServerboundAcceptTeleportationPacket paramServerboundAcceptTeleportationPacket);
  
  void handleRecipeBookSeenRecipePacket(ServerboundRecipeBookSeenRecipePacket paramServerboundRecipeBookSeenRecipePacket);
  
  void handleRecipeBookChangeSettingsPacket(ServerboundRecipeBookChangeSettingsPacket paramServerboundRecipeBookChangeSettingsPacket);
  
  void handleSeenAdvancements(ServerboundSeenAdvancementsPacket paramServerboundSeenAdvancementsPacket);
  
  void handleCustomCommandSuggestions(ServerboundCommandSuggestionPacket paramServerboundCommandSuggestionPacket);
  
  void handleSetCommandBlock(ServerboundSetCommandBlockPacket paramServerboundSetCommandBlockPacket);
  
  void handleSetCommandMinecart(ServerboundSetCommandMinecartPacket paramServerboundSetCommandMinecartPacket);
  
  void handlePickItem(ServerboundPickItemPacket paramServerboundPickItemPacket);
  
  void handleRenameItem(ServerboundRenameItemPacket paramServerboundRenameItemPacket);
  
  void handleSetBeaconPacket(ServerboundSetBeaconPacket paramServerboundSetBeaconPacket);
  
  void handleSetStructureBlock(ServerboundSetStructureBlockPacket paramServerboundSetStructureBlockPacket);
  
  void handleSelectTrade(ServerboundSelectTradePacket paramServerboundSelectTradePacket);
  
  void handleEditBook(ServerboundEditBookPacket paramServerboundEditBookPacket);
  
  void handleEntityTagQuery(ServerboundEntityTagQuery paramServerboundEntityTagQuery);
  
  void handleBlockEntityTagQuery(ServerboundBlockEntityTagQuery paramServerboundBlockEntityTagQuery);
  
  void handleSetJigsawBlock(ServerboundSetJigsawBlockPacket paramServerboundSetJigsawBlockPacket);
  
  void handleJigsawGenerate(ServerboundJigsawGeneratePacket paramServerboundJigsawGeneratePacket);
  
  void handleChangeDifficulty(ServerboundChangeDifficultyPacket paramServerboundChangeDifficultyPacket);
  
  void handleLockDifficulty(ServerboundLockDifficultyPacket paramServerboundLockDifficultyPacket);
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\protocol\game\ServerGamePacketListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */