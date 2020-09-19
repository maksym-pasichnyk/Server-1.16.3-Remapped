package net.minecraft.network.protocol.game;

import net.minecraft.network.PacketListener;

public interface ClientGamePacketListener extends PacketListener {
  void handleAddEntity(ClientboundAddEntityPacket paramClientboundAddEntityPacket);
  
  void handleAddExperienceOrb(ClientboundAddExperienceOrbPacket paramClientboundAddExperienceOrbPacket);
  
  void handleAddMob(ClientboundAddMobPacket paramClientboundAddMobPacket);
  
  void handleAddObjective(ClientboundSetObjectivePacket paramClientboundSetObjectivePacket);
  
  void handleAddPainting(ClientboundAddPaintingPacket paramClientboundAddPaintingPacket);
  
  void handleAddPlayer(ClientboundAddPlayerPacket paramClientboundAddPlayerPacket);
  
  void handleAnimate(ClientboundAnimatePacket paramClientboundAnimatePacket);
  
  void handleAwardStats(ClientboundAwardStatsPacket paramClientboundAwardStatsPacket);
  
  void handleAddOrRemoveRecipes(ClientboundRecipePacket paramClientboundRecipePacket);
  
  void handleBlockDestruction(ClientboundBlockDestructionPacket paramClientboundBlockDestructionPacket);
  
  void handleOpenSignEditor(ClientboundOpenSignEditorPacket paramClientboundOpenSignEditorPacket);
  
  void handleBlockEntityData(ClientboundBlockEntityDataPacket paramClientboundBlockEntityDataPacket);
  
  void handleBlockEvent(ClientboundBlockEventPacket paramClientboundBlockEventPacket);
  
  void handleBlockUpdate(ClientboundBlockUpdatePacket paramClientboundBlockUpdatePacket);
  
  void handleChat(ClientboundChatPacket paramClientboundChatPacket);
  
  void handleChunkBlocksUpdate(ClientboundSectionBlocksUpdatePacket paramClientboundSectionBlocksUpdatePacket);
  
  void handleMapItemData(ClientboundMapItemDataPacket paramClientboundMapItemDataPacket);
  
  void handleContainerAck(ClientboundContainerAckPacket paramClientboundContainerAckPacket);
  
  void handleContainerClose(ClientboundContainerClosePacket paramClientboundContainerClosePacket);
  
  void handleContainerContent(ClientboundContainerSetContentPacket paramClientboundContainerSetContentPacket);
  
  void handleHorseScreenOpen(ClientboundHorseScreenOpenPacket paramClientboundHorseScreenOpenPacket);
  
  void handleContainerSetData(ClientboundContainerSetDataPacket paramClientboundContainerSetDataPacket);
  
  void handleContainerSetSlot(ClientboundContainerSetSlotPacket paramClientboundContainerSetSlotPacket);
  
  void handleCustomPayload(ClientboundCustomPayloadPacket paramClientboundCustomPayloadPacket);
  
  void handleDisconnect(ClientboundDisconnectPacket paramClientboundDisconnectPacket);
  
  void handleEntityEvent(ClientboundEntityEventPacket paramClientboundEntityEventPacket);
  
  void handleEntityLinkPacket(ClientboundSetEntityLinkPacket paramClientboundSetEntityLinkPacket);
  
  void handleSetEntityPassengersPacket(ClientboundSetPassengersPacket paramClientboundSetPassengersPacket);
  
  void handleExplosion(ClientboundExplodePacket paramClientboundExplodePacket);
  
  void handleGameEvent(ClientboundGameEventPacket paramClientboundGameEventPacket);
  
  void handleKeepAlive(ClientboundKeepAlivePacket paramClientboundKeepAlivePacket);
  
  void handleLevelChunk(ClientboundLevelChunkPacket paramClientboundLevelChunkPacket);
  
  void handleForgetLevelChunk(ClientboundForgetLevelChunkPacket paramClientboundForgetLevelChunkPacket);
  
  void handleLevelEvent(ClientboundLevelEventPacket paramClientboundLevelEventPacket);
  
  void handleLogin(ClientboundLoginPacket paramClientboundLoginPacket);
  
  void handleMoveEntity(ClientboundMoveEntityPacket paramClientboundMoveEntityPacket);
  
  void handleMovePlayer(ClientboundPlayerPositionPacket paramClientboundPlayerPositionPacket);
  
  void handleParticleEvent(ClientboundLevelParticlesPacket paramClientboundLevelParticlesPacket);
  
  void handlePlayerAbilities(ClientboundPlayerAbilitiesPacket paramClientboundPlayerAbilitiesPacket);
  
  void handlePlayerInfo(ClientboundPlayerInfoPacket paramClientboundPlayerInfoPacket);
  
  void handleRemoveEntity(ClientboundRemoveEntitiesPacket paramClientboundRemoveEntitiesPacket);
  
  void handleRemoveMobEffect(ClientboundRemoveMobEffectPacket paramClientboundRemoveMobEffectPacket);
  
  void handleRespawn(ClientboundRespawnPacket paramClientboundRespawnPacket);
  
  void handleRotateMob(ClientboundRotateHeadPacket paramClientboundRotateHeadPacket);
  
  void handleSetCarriedItem(ClientboundSetCarriedItemPacket paramClientboundSetCarriedItemPacket);
  
  void handleSetDisplayObjective(ClientboundSetDisplayObjectivePacket paramClientboundSetDisplayObjectivePacket);
  
  void handleSetEntityData(ClientboundSetEntityDataPacket paramClientboundSetEntityDataPacket);
  
  void handleSetEntityMotion(ClientboundSetEntityMotionPacket paramClientboundSetEntityMotionPacket);
  
  void handleSetEquipment(ClientboundSetEquipmentPacket paramClientboundSetEquipmentPacket);
  
  void handleSetExperience(ClientboundSetExperiencePacket paramClientboundSetExperiencePacket);
  
  void handleSetHealth(ClientboundSetHealthPacket paramClientboundSetHealthPacket);
  
  void handleSetPlayerTeamPacket(ClientboundSetPlayerTeamPacket paramClientboundSetPlayerTeamPacket);
  
  void handleSetScore(ClientboundSetScorePacket paramClientboundSetScorePacket);
  
  void handleSetSpawn(ClientboundSetDefaultSpawnPositionPacket paramClientboundSetDefaultSpawnPositionPacket);
  
  void handleSetTime(ClientboundSetTimePacket paramClientboundSetTimePacket);
  
  void handleSoundEvent(ClientboundSoundPacket paramClientboundSoundPacket);
  
  void handleSoundEntityEvent(ClientboundSoundEntityPacket paramClientboundSoundEntityPacket);
  
  void handleCustomSoundEvent(ClientboundCustomSoundPacket paramClientboundCustomSoundPacket);
  
  void handleTakeItemEntity(ClientboundTakeItemEntityPacket paramClientboundTakeItemEntityPacket);
  
  void handleTeleportEntity(ClientboundTeleportEntityPacket paramClientboundTeleportEntityPacket);
  
  void handleUpdateAttributes(ClientboundUpdateAttributesPacket paramClientboundUpdateAttributesPacket);
  
  void handleUpdateMobEffect(ClientboundUpdateMobEffectPacket paramClientboundUpdateMobEffectPacket);
  
  void handleUpdateTags(ClientboundUpdateTagsPacket paramClientboundUpdateTagsPacket);
  
  void handlePlayerCombat(ClientboundPlayerCombatPacket paramClientboundPlayerCombatPacket);
  
  void handleChangeDifficulty(ClientboundChangeDifficultyPacket paramClientboundChangeDifficultyPacket);
  
  void handleSetCamera(ClientboundSetCameraPacket paramClientboundSetCameraPacket);
  
  void handleSetBorder(ClientboundSetBorderPacket paramClientboundSetBorderPacket);
  
  void handleSetTitles(ClientboundSetTitlesPacket paramClientboundSetTitlesPacket);
  
  void handleTabListCustomisation(ClientboundTabListPacket paramClientboundTabListPacket);
  
  void handleResourcePack(ClientboundResourcePackPacket paramClientboundResourcePackPacket);
  
  void handleBossUpdate(ClientboundBossEventPacket paramClientboundBossEventPacket);
  
  void handleItemCooldown(ClientboundCooldownPacket paramClientboundCooldownPacket);
  
  void handleMoveVehicle(ClientboundMoveVehiclePacket paramClientboundMoveVehiclePacket);
  
  void handleUpdateAdvancementsPacket(ClientboundUpdateAdvancementsPacket paramClientboundUpdateAdvancementsPacket);
  
  void handleSelectAdvancementsTab(ClientboundSelectAdvancementsTabPacket paramClientboundSelectAdvancementsTabPacket);
  
  void handlePlaceRecipe(ClientboundPlaceGhostRecipePacket paramClientboundPlaceGhostRecipePacket);
  
  void handleCommands(ClientboundCommandsPacket paramClientboundCommandsPacket);
  
  void handleStopSoundEvent(ClientboundStopSoundPacket paramClientboundStopSoundPacket);
  
  void handleCommandSuggestions(ClientboundCommandSuggestionsPacket paramClientboundCommandSuggestionsPacket);
  
  void handleUpdateRecipes(ClientboundUpdateRecipesPacket paramClientboundUpdateRecipesPacket);
  
  void handleLookAt(ClientboundPlayerLookAtPacket paramClientboundPlayerLookAtPacket);
  
  void handleTagQueryPacket(ClientboundTagQueryPacket paramClientboundTagQueryPacket);
  
  void handleLightUpdatePacked(ClientboundLightUpdatePacket paramClientboundLightUpdatePacket);
  
  void handleOpenBook(ClientboundOpenBookPacket paramClientboundOpenBookPacket);
  
  void handleOpenScreen(ClientboundOpenScreenPacket paramClientboundOpenScreenPacket);
  
  void handleMerchantOffers(ClientboundMerchantOffersPacket paramClientboundMerchantOffersPacket);
  
  void handleSetChunkCacheRadius(ClientboundSetChunkCacheRadiusPacket paramClientboundSetChunkCacheRadiusPacket);
  
  void handleSetChunkCacheCenter(ClientboundSetChunkCacheCenterPacket paramClientboundSetChunkCacheCenterPacket);
  
  void handleBlockBreakAck(ClientboundBlockBreakAckPacket paramClientboundBlockBreakAckPacket);
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\protocol\game\ClientGamePacketListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */