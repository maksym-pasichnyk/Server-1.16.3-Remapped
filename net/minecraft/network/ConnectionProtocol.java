/*     */ package net.minecraft.network;
/*     */ 
/*     */ import com.google.common.collect.Iterables;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Maps;
/*     */ import it.unimi.dsi.fastutil.objects.Object2IntMap;
/*     */ import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.function.Supplier;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.Util;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.PacketFlow;
/*     */ import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundAddExperienceOrbPacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundAddMobPacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundAddPaintingPacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundAddPlayerPacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundAnimatePacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundAwardStatsPacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundBlockBreakAckPacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundBlockDestructionPacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundBlockEventPacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundBossEventPacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundChangeDifficultyPacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundChatPacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundCommandSuggestionsPacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundCommandsPacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundContainerAckPacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundContainerClosePacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundContainerSetContentPacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundContainerSetDataPacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundCooldownPacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundCustomPayloadPacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundCustomSoundPacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundDisconnectPacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundEntityEventPacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundExplodePacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundForgetLevelChunkPacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundGameEventPacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundHorseScreenOpenPacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundKeepAlivePacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundLevelChunkPacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundLevelEventPacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundLevelParticlesPacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundLightUpdatePacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundLoginPacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundMapItemDataPacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundMerchantOffersPacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundMoveEntityPacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundMoveVehiclePacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundOpenBookPacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundOpenScreenPacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundOpenSignEditorPacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundPlaceGhostRecipePacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundPlayerAbilitiesPacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundPlayerCombatPacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundPlayerInfoPacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundPlayerLookAtPacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundPlayerPositionPacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundRecipePacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundRemoveMobEffectPacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundResourcePackPacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundRespawnPacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundRotateHeadPacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundSectionBlocksUpdatePacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundSelectAdvancementsTabPacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundSetBorderPacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundSetCameraPacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundSetCarriedItemPacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundSetChunkCacheCenterPacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundSetChunkCacheRadiusPacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundSetDefaultSpawnPositionPacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundSetDisplayObjectivePacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundSetEntityLinkPacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundSetEquipmentPacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundSetExperiencePacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundSetHealthPacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundSetObjectivePacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundSetPassengersPacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundSetPlayerTeamPacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundSetScorePacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundSetTimePacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundSetTitlesPacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundSoundEntityPacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundSoundPacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundStopSoundPacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundTabListPacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundTagQueryPacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundTakeItemEntityPacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundTeleportEntityPacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundUpdateAdvancementsPacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundUpdateAttributesPacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundUpdateMobEffectPacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundUpdateRecipesPacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundUpdateTagsPacket;
/*     */ import net.minecraft.network.protocol.game.ServerboundAcceptTeleportationPacket;
/*     */ import net.minecraft.network.protocol.game.ServerboundBlockEntityTagQuery;
/*     */ import net.minecraft.network.protocol.game.ServerboundChangeDifficultyPacket;
/*     */ import net.minecraft.network.protocol.game.ServerboundChatPacket;
/*     */ import net.minecraft.network.protocol.game.ServerboundClientCommandPacket;
/*     */ import net.minecraft.network.protocol.game.ServerboundClientInformationPacket;
/*     */ import net.minecraft.network.protocol.game.ServerboundCommandSuggestionPacket;
/*     */ import net.minecraft.network.protocol.game.ServerboundContainerAckPacket;
/*     */ import net.minecraft.network.protocol.game.ServerboundContainerButtonClickPacket;
/*     */ import net.minecraft.network.protocol.game.ServerboundContainerClickPacket;
/*     */ import net.minecraft.network.protocol.game.ServerboundContainerClosePacket;
/*     */ import net.minecraft.network.protocol.game.ServerboundCustomPayloadPacket;
/*     */ import net.minecraft.network.protocol.game.ServerboundEditBookPacket;
/*     */ import net.minecraft.network.protocol.game.ServerboundEntityTagQuery;
/*     */ import net.minecraft.network.protocol.game.ServerboundInteractPacket;
/*     */ import net.minecraft.network.protocol.game.ServerboundJigsawGeneratePacket;
/*     */ import net.minecraft.network.protocol.game.ServerboundKeepAlivePacket;
/*     */ import net.minecraft.network.protocol.game.ServerboundLockDifficultyPacket;
/*     */ import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
/*     */ import net.minecraft.network.protocol.game.ServerboundMoveVehiclePacket;
/*     */ import net.minecraft.network.protocol.game.ServerboundPaddleBoatPacket;
/*     */ import net.minecraft.network.protocol.game.ServerboundPickItemPacket;
/*     */ import net.minecraft.network.protocol.game.ServerboundPlaceRecipePacket;
/*     */ import net.minecraft.network.protocol.game.ServerboundPlayerAbilitiesPacket;
/*     */ import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
/*     */ import net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket;
/*     */ import net.minecraft.network.protocol.game.ServerboundPlayerInputPacket;
/*     */ import net.minecraft.network.protocol.game.ServerboundRecipeBookChangeSettingsPacket;
/*     */ import net.minecraft.network.protocol.game.ServerboundRecipeBookSeenRecipePacket;
/*     */ import net.minecraft.network.protocol.game.ServerboundRenameItemPacket;
/*     */ import net.minecraft.network.protocol.game.ServerboundResourcePackPacket;
/*     */ import net.minecraft.network.protocol.game.ServerboundSeenAdvancementsPacket;
/*     */ import net.minecraft.network.protocol.game.ServerboundSelectTradePacket;
/*     */ import net.minecraft.network.protocol.game.ServerboundSetBeaconPacket;
/*     */ import net.minecraft.network.protocol.game.ServerboundSetCarriedItemPacket;
/*     */ import net.minecraft.network.protocol.game.ServerboundSetCommandBlockPacket;
/*     */ import net.minecraft.network.protocol.game.ServerboundSetCommandMinecartPacket;
/*     */ import net.minecraft.network.protocol.game.ServerboundSetCreativeModeSlotPacket;
/*     */ import net.minecraft.network.protocol.game.ServerboundSetJigsawBlockPacket;
/*     */ import net.minecraft.network.protocol.game.ServerboundSetStructureBlockPacket;
/*     */ import net.minecraft.network.protocol.game.ServerboundSignUpdatePacket;
/*     */ import net.minecraft.network.protocol.game.ServerboundSwingPacket;
/*     */ import net.minecraft.network.protocol.game.ServerboundTeleportToEntityPacket;
/*     */ import net.minecraft.network.protocol.game.ServerboundUseItemOnPacket;
/*     */ import net.minecraft.network.protocol.game.ServerboundUseItemPacket;
/*     */ import net.minecraft.network.protocol.handshake.ClientIntentionPacket;
/*     */ import net.minecraft.network.protocol.login.ClientboundCustomQueryPacket;
/*     */ import net.minecraft.network.protocol.login.ClientboundGameProfilePacket;
/*     */ import net.minecraft.network.protocol.login.ClientboundHelloPacket;
/*     */ import net.minecraft.network.protocol.login.ClientboundLoginCompressionPacket;
/*     */ import net.minecraft.network.protocol.login.ClientboundLoginDisconnectPacket;
/*     */ import net.minecraft.network.protocol.login.ServerboundCustomQueryPacket;
/*     */ import net.minecraft.network.protocol.login.ServerboundHelloPacket;
/*     */ import net.minecraft.network.protocol.login.ServerboundKeyPacket;
/*     */ import net.minecraft.network.protocol.status.ClientboundPongResponsePacket;
/*     */ import net.minecraft.network.protocol.status.ClientboundStatusResponsePacket;
/*     */ import net.minecraft.network.protocol.status.ServerboundPingRequestPacket;
/*     */ import net.minecraft.network.protocol.status.ServerboundStatusRequestPacket;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public enum ConnectionProtocol
/*     */ {
/* 173 */   HANDSHAKING(-1, 
/* 174 */     protocol()
/* 175 */     .addFlow(PacketFlow.SERVERBOUND, (new PacketSet<>())
/* 176 */       .addPacket(ClientIntentionPacket.class, ClientIntentionPacket::new))),
/*     */ 
/*     */   
/* 179 */   PLAY(0, 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 186 */     protocol()
/* 187 */     .<PacketListener>addFlow(PacketFlow.CLIENTBOUND, (new PacketSet<>())
/* 188 */       .<ClientboundAddEntityPacket>addPacket(ClientboundAddEntityPacket.class, ClientboundAddEntityPacket::new)
/* 189 */       .<ClientboundAddExperienceOrbPacket>addPacket(ClientboundAddExperienceOrbPacket.class, ClientboundAddExperienceOrbPacket::new)
/* 190 */       .<ClientboundAddMobPacket>addPacket(ClientboundAddMobPacket.class, ClientboundAddMobPacket::new)
/* 191 */       .<ClientboundAddPaintingPacket>addPacket(ClientboundAddPaintingPacket.class, ClientboundAddPaintingPacket::new)
/* 192 */       .<ClientboundAddPlayerPacket>addPacket(ClientboundAddPlayerPacket.class, ClientboundAddPlayerPacket::new)
/* 193 */       .<ClientboundAnimatePacket>addPacket(ClientboundAnimatePacket.class, ClientboundAnimatePacket::new)
/* 194 */       .<ClientboundAwardStatsPacket>addPacket(ClientboundAwardStatsPacket.class, ClientboundAwardStatsPacket::new)
/* 195 */       .<ClientboundBlockBreakAckPacket>addPacket(ClientboundBlockBreakAckPacket.class, ClientboundBlockBreakAckPacket::new)
/* 196 */       .<ClientboundBlockDestructionPacket>addPacket(ClientboundBlockDestructionPacket.class, ClientboundBlockDestructionPacket::new)
/* 197 */       .<ClientboundBlockEntityDataPacket>addPacket(ClientboundBlockEntityDataPacket.class, ClientboundBlockEntityDataPacket::new)
/* 198 */       .<ClientboundBlockEventPacket>addPacket(ClientboundBlockEventPacket.class, ClientboundBlockEventPacket::new)
/* 199 */       .<ClientboundBlockUpdatePacket>addPacket(ClientboundBlockUpdatePacket.class, ClientboundBlockUpdatePacket::new)
/* 200 */       .<ClientboundBossEventPacket>addPacket(ClientboundBossEventPacket.class, ClientboundBossEventPacket::new)
/* 201 */       .<ClientboundChangeDifficultyPacket>addPacket(ClientboundChangeDifficultyPacket.class, ClientboundChangeDifficultyPacket::new)
/* 202 */       .<ClientboundChatPacket>addPacket(ClientboundChatPacket.class, ClientboundChatPacket::new)
/* 203 */       .<ClientboundCommandSuggestionsPacket>addPacket(ClientboundCommandSuggestionsPacket.class, ClientboundCommandSuggestionsPacket::new)
/* 204 */       .<ClientboundCommandsPacket>addPacket(ClientboundCommandsPacket.class, ClientboundCommandsPacket::new)
/* 205 */       .<ClientboundContainerAckPacket>addPacket(ClientboundContainerAckPacket.class, ClientboundContainerAckPacket::new)
/* 206 */       .<ClientboundContainerClosePacket>addPacket(ClientboundContainerClosePacket.class, ClientboundContainerClosePacket::new)
/* 207 */       .<ClientboundContainerSetContentPacket>addPacket(ClientboundContainerSetContentPacket.class, ClientboundContainerSetContentPacket::new)
/* 208 */       .<ClientboundContainerSetDataPacket>addPacket(ClientboundContainerSetDataPacket.class, ClientboundContainerSetDataPacket::new)
/* 209 */       .<ClientboundContainerSetSlotPacket>addPacket(ClientboundContainerSetSlotPacket.class, ClientboundContainerSetSlotPacket::new)
/* 210 */       .<ClientboundCooldownPacket>addPacket(ClientboundCooldownPacket.class, ClientboundCooldownPacket::new)
/* 211 */       .<ClientboundCustomPayloadPacket>addPacket(ClientboundCustomPayloadPacket.class, ClientboundCustomPayloadPacket::new)
/* 212 */       .<ClientboundCustomSoundPacket>addPacket(ClientboundCustomSoundPacket.class, ClientboundCustomSoundPacket::new)
/* 213 */       .<ClientboundDisconnectPacket>addPacket(ClientboundDisconnectPacket.class, ClientboundDisconnectPacket::new)
/* 214 */       .<ClientboundEntityEventPacket>addPacket(ClientboundEntityEventPacket.class, ClientboundEntityEventPacket::new)
/* 215 */       .<ClientboundExplodePacket>addPacket(ClientboundExplodePacket.class, ClientboundExplodePacket::new)
/* 216 */       .<ClientboundForgetLevelChunkPacket>addPacket(ClientboundForgetLevelChunkPacket.class, ClientboundForgetLevelChunkPacket::new)
/* 217 */       .<ClientboundGameEventPacket>addPacket(ClientboundGameEventPacket.class, ClientboundGameEventPacket::new)
/* 218 */       .<ClientboundHorseScreenOpenPacket>addPacket(ClientboundHorseScreenOpenPacket.class, ClientboundHorseScreenOpenPacket::new)
/* 219 */       .<ClientboundKeepAlivePacket>addPacket(ClientboundKeepAlivePacket.class, ClientboundKeepAlivePacket::new)
/* 220 */       .<ClientboundLevelChunkPacket>addPacket(ClientboundLevelChunkPacket.class, ClientboundLevelChunkPacket::new)
/* 221 */       .<ClientboundLevelEventPacket>addPacket(ClientboundLevelEventPacket.class, ClientboundLevelEventPacket::new)
/* 222 */       .<ClientboundLevelParticlesPacket>addPacket(ClientboundLevelParticlesPacket.class, ClientboundLevelParticlesPacket::new)
/* 223 */       .<ClientboundLightUpdatePacket>addPacket(ClientboundLightUpdatePacket.class, ClientboundLightUpdatePacket::new)
/* 224 */       .<ClientboundLoginPacket>addPacket(ClientboundLoginPacket.class, ClientboundLoginPacket::new)
/* 225 */       .<ClientboundMapItemDataPacket>addPacket(ClientboundMapItemDataPacket.class, ClientboundMapItemDataPacket::new)
/* 226 */       .<ClientboundMerchantOffersPacket>addPacket(ClientboundMerchantOffersPacket.class, ClientboundMerchantOffersPacket::new)
/* 227 */       .<ClientboundMoveEntityPacket.Pos>addPacket(ClientboundMoveEntityPacket.Pos.class, net.minecraft.network.protocol.game.ClientboundMoveEntityPacket.Pos::new)
/* 228 */       .<ClientboundMoveEntityPacket.PosRot>addPacket(ClientboundMoveEntityPacket.PosRot.class, net.minecraft.network.protocol.game.ClientboundMoveEntityPacket.PosRot::new)
/* 229 */       .<ClientboundMoveEntityPacket.Rot>addPacket(ClientboundMoveEntityPacket.Rot.class, net.minecraft.network.protocol.game.ClientboundMoveEntityPacket.Rot::new)
/* 230 */       .<ClientboundMoveEntityPacket>addPacket(ClientboundMoveEntityPacket.class, ClientboundMoveEntityPacket::new)
/* 231 */       .<ClientboundMoveVehiclePacket>addPacket(ClientboundMoveVehiclePacket.class, ClientboundMoveVehiclePacket::new)
/* 232 */       .<ClientboundOpenBookPacket>addPacket(ClientboundOpenBookPacket.class, ClientboundOpenBookPacket::new)
/* 233 */       .<ClientboundOpenScreenPacket>addPacket(ClientboundOpenScreenPacket.class, ClientboundOpenScreenPacket::new)
/* 234 */       .<ClientboundOpenSignEditorPacket>addPacket(ClientboundOpenSignEditorPacket.class, ClientboundOpenSignEditorPacket::new)
/* 235 */       .<ClientboundPlaceGhostRecipePacket>addPacket(ClientboundPlaceGhostRecipePacket.class, ClientboundPlaceGhostRecipePacket::new)
/* 236 */       .<ClientboundPlayerAbilitiesPacket>addPacket(ClientboundPlayerAbilitiesPacket.class, ClientboundPlayerAbilitiesPacket::new)
/* 237 */       .<ClientboundPlayerCombatPacket>addPacket(ClientboundPlayerCombatPacket.class, ClientboundPlayerCombatPacket::new)
/* 238 */       .<ClientboundPlayerInfoPacket>addPacket(ClientboundPlayerInfoPacket.class, ClientboundPlayerInfoPacket::new)
/* 239 */       .<ClientboundPlayerLookAtPacket>addPacket(ClientboundPlayerLookAtPacket.class, ClientboundPlayerLookAtPacket::new)
/* 240 */       .<ClientboundPlayerPositionPacket>addPacket(ClientboundPlayerPositionPacket.class, ClientboundPlayerPositionPacket::new)
/* 241 */       .<ClientboundRecipePacket>addPacket(ClientboundRecipePacket.class, ClientboundRecipePacket::new)
/* 242 */       .<ClientboundRemoveEntitiesPacket>addPacket(ClientboundRemoveEntitiesPacket.class, ClientboundRemoveEntitiesPacket::new)
/* 243 */       .<ClientboundRemoveMobEffectPacket>addPacket(ClientboundRemoveMobEffectPacket.class, ClientboundRemoveMobEffectPacket::new)
/* 244 */       .<ClientboundResourcePackPacket>addPacket(ClientboundResourcePackPacket.class, ClientboundResourcePackPacket::new)
/* 245 */       .<ClientboundRespawnPacket>addPacket(ClientboundRespawnPacket.class, ClientboundRespawnPacket::new)
/* 246 */       .<ClientboundRotateHeadPacket>addPacket(ClientboundRotateHeadPacket.class, ClientboundRotateHeadPacket::new)
/* 247 */       .<ClientboundSectionBlocksUpdatePacket>addPacket(ClientboundSectionBlocksUpdatePacket.class, ClientboundSectionBlocksUpdatePacket::new)
/* 248 */       .<ClientboundSelectAdvancementsTabPacket>addPacket(ClientboundSelectAdvancementsTabPacket.class, ClientboundSelectAdvancementsTabPacket::new)
/* 249 */       .<ClientboundSetBorderPacket>addPacket(ClientboundSetBorderPacket.class, ClientboundSetBorderPacket::new)
/* 250 */       .<ClientboundSetCameraPacket>addPacket(ClientboundSetCameraPacket.class, ClientboundSetCameraPacket::new)
/* 251 */       .<ClientboundSetCarriedItemPacket>addPacket(ClientboundSetCarriedItemPacket.class, ClientboundSetCarriedItemPacket::new)
/* 252 */       .<ClientboundSetChunkCacheCenterPacket>addPacket(ClientboundSetChunkCacheCenterPacket.class, ClientboundSetChunkCacheCenterPacket::new)
/* 253 */       .<ClientboundSetChunkCacheRadiusPacket>addPacket(ClientboundSetChunkCacheRadiusPacket.class, ClientboundSetChunkCacheRadiusPacket::new)
/* 254 */       .<ClientboundSetDefaultSpawnPositionPacket>addPacket(ClientboundSetDefaultSpawnPositionPacket.class, ClientboundSetDefaultSpawnPositionPacket::new)
/* 255 */       .<ClientboundSetDisplayObjectivePacket>addPacket(ClientboundSetDisplayObjectivePacket.class, ClientboundSetDisplayObjectivePacket::new)
/* 256 */       .<ClientboundSetEntityDataPacket>addPacket(ClientboundSetEntityDataPacket.class, ClientboundSetEntityDataPacket::new)
/* 257 */       .<ClientboundSetEntityLinkPacket>addPacket(ClientboundSetEntityLinkPacket.class, ClientboundSetEntityLinkPacket::new)
/* 258 */       .<ClientboundSetEntityMotionPacket>addPacket(ClientboundSetEntityMotionPacket.class, ClientboundSetEntityMotionPacket::new)
/* 259 */       .<ClientboundSetEquipmentPacket>addPacket(ClientboundSetEquipmentPacket.class, ClientboundSetEquipmentPacket::new)
/* 260 */       .<ClientboundSetExperiencePacket>addPacket(ClientboundSetExperiencePacket.class, ClientboundSetExperiencePacket::new)
/* 261 */       .<ClientboundSetHealthPacket>addPacket(ClientboundSetHealthPacket.class, ClientboundSetHealthPacket::new)
/* 262 */       .<ClientboundSetObjectivePacket>addPacket(ClientboundSetObjectivePacket.class, ClientboundSetObjectivePacket::new)
/* 263 */       .<ClientboundSetPassengersPacket>addPacket(ClientboundSetPassengersPacket.class, ClientboundSetPassengersPacket::new)
/* 264 */       .<ClientboundSetPlayerTeamPacket>addPacket(ClientboundSetPlayerTeamPacket.class, ClientboundSetPlayerTeamPacket::new)
/* 265 */       .<ClientboundSetScorePacket>addPacket(ClientboundSetScorePacket.class, ClientboundSetScorePacket::new)
/* 266 */       .<ClientboundSetTimePacket>addPacket(ClientboundSetTimePacket.class, ClientboundSetTimePacket::new)
/* 267 */       .<ClientboundSetTitlesPacket>addPacket(ClientboundSetTitlesPacket.class, ClientboundSetTitlesPacket::new)
/* 268 */       .<ClientboundSoundEntityPacket>addPacket(ClientboundSoundEntityPacket.class, ClientboundSoundEntityPacket::new)
/* 269 */       .<ClientboundSoundPacket>addPacket(ClientboundSoundPacket.class, ClientboundSoundPacket::new)
/* 270 */       .<ClientboundStopSoundPacket>addPacket(ClientboundStopSoundPacket.class, ClientboundStopSoundPacket::new)
/* 271 */       .<ClientboundTabListPacket>addPacket(ClientboundTabListPacket.class, ClientboundTabListPacket::new)
/* 272 */       .<ClientboundTagQueryPacket>addPacket(ClientboundTagQueryPacket.class, ClientboundTagQueryPacket::new)
/* 273 */       .<ClientboundTakeItemEntityPacket>addPacket(ClientboundTakeItemEntityPacket.class, ClientboundTakeItemEntityPacket::new)
/* 274 */       .<ClientboundTeleportEntityPacket>addPacket(ClientboundTeleportEntityPacket.class, ClientboundTeleportEntityPacket::new)
/* 275 */       .<ClientboundUpdateAdvancementsPacket>addPacket(ClientboundUpdateAdvancementsPacket.class, ClientboundUpdateAdvancementsPacket::new)
/* 276 */       .<ClientboundUpdateAttributesPacket>addPacket(ClientboundUpdateAttributesPacket.class, ClientboundUpdateAttributesPacket::new)
/* 277 */       .<ClientboundUpdateMobEffectPacket>addPacket(ClientboundUpdateMobEffectPacket.class, ClientboundUpdateMobEffectPacket::new)
/* 278 */       .<ClientboundUpdateRecipesPacket>addPacket(ClientboundUpdateRecipesPacket.class, ClientboundUpdateRecipesPacket::new)
/* 279 */       .addPacket(ClientboundUpdateTagsPacket.class, ClientboundUpdateTagsPacket::new))
/*     */     
/* 281 */     .addFlow(PacketFlow.SERVERBOUND, (new PacketSet<>())
/* 282 */       .<ServerboundAcceptTeleportationPacket>addPacket(ServerboundAcceptTeleportationPacket.class, ServerboundAcceptTeleportationPacket::new)
/* 283 */       .<ServerboundBlockEntityTagQuery>addPacket(ServerboundBlockEntityTagQuery.class, ServerboundBlockEntityTagQuery::new)
/* 284 */       .<ServerboundChangeDifficultyPacket>addPacket(ServerboundChangeDifficultyPacket.class, ServerboundChangeDifficultyPacket::new)
/* 285 */       .<ServerboundChatPacket>addPacket(ServerboundChatPacket.class, ServerboundChatPacket::new)
/* 286 */       .<ServerboundClientCommandPacket>addPacket(ServerboundClientCommandPacket.class, ServerboundClientCommandPacket::new)
/* 287 */       .<ServerboundClientInformationPacket>addPacket(ServerboundClientInformationPacket.class, ServerboundClientInformationPacket::new)
/* 288 */       .<ServerboundCommandSuggestionPacket>addPacket(ServerboundCommandSuggestionPacket.class, ServerboundCommandSuggestionPacket::new)
/* 289 */       .<ServerboundContainerAckPacket>addPacket(ServerboundContainerAckPacket.class, ServerboundContainerAckPacket::new)
/* 290 */       .<ServerboundContainerButtonClickPacket>addPacket(ServerboundContainerButtonClickPacket.class, ServerboundContainerButtonClickPacket::new)
/* 291 */       .<ServerboundContainerClickPacket>addPacket(ServerboundContainerClickPacket.class, ServerboundContainerClickPacket::new)
/* 292 */       .<ServerboundContainerClosePacket>addPacket(ServerboundContainerClosePacket.class, ServerboundContainerClosePacket::new)
/* 293 */       .<ServerboundCustomPayloadPacket>addPacket(ServerboundCustomPayloadPacket.class, ServerboundCustomPayloadPacket::new)
/* 294 */       .<ServerboundEditBookPacket>addPacket(ServerboundEditBookPacket.class, ServerboundEditBookPacket::new)
/* 295 */       .<ServerboundEntityTagQuery>addPacket(ServerboundEntityTagQuery.class, ServerboundEntityTagQuery::new)
/* 296 */       .<ServerboundInteractPacket>addPacket(ServerboundInteractPacket.class, ServerboundInteractPacket::new)
/* 297 */       .<ServerboundJigsawGeneratePacket>addPacket(ServerboundJigsawGeneratePacket.class, ServerboundJigsawGeneratePacket::new)
/* 298 */       .<ServerboundKeepAlivePacket>addPacket(ServerboundKeepAlivePacket.class, ServerboundKeepAlivePacket::new)
/* 299 */       .<ServerboundLockDifficultyPacket>addPacket(ServerboundLockDifficultyPacket.class, ServerboundLockDifficultyPacket::new)
/* 300 */       .<ServerboundMovePlayerPacket.Pos>addPacket(ServerboundMovePlayerPacket.Pos.class, net.minecraft.network.protocol.game.ServerboundMovePlayerPacket.Pos::new)
/* 301 */       .<ServerboundMovePlayerPacket.PosRot>addPacket(ServerboundMovePlayerPacket.PosRot.class, net.minecraft.network.protocol.game.ServerboundMovePlayerPacket.PosRot::new)
/* 302 */       .<ServerboundMovePlayerPacket.Rot>addPacket(ServerboundMovePlayerPacket.Rot.class, net.minecraft.network.protocol.game.ServerboundMovePlayerPacket.Rot::new)
/* 303 */       .<ServerboundMovePlayerPacket>addPacket(ServerboundMovePlayerPacket.class, ServerboundMovePlayerPacket::new)
/* 304 */       .<ServerboundMoveVehiclePacket>addPacket(ServerboundMoveVehiclePacket.class, ServerboundMoveVehiclePacket::new)
/* 305 */       .<ServerboundPaddleBoatPacket>addPacket(ServerboundPaddleBoatPacket.class, ServerboundPaddleBoatPacket::new)
/* 306 */       .<ServerboundPickItemPacket>addPacket(ServerboundPickItemPacket.class, ServerboundPickItemPacket::new)
/* 307 */       .<ServerboundPlaceRecipePacket>addPacket(ServerboundPlaceRecipePacket.class, ServerboundPlaceRecipePacket::new)
/* 308 */       .<ServerboundPlayerAbilitiesPacket>addPacket(ServerboundPlayerAbilitiesPacket.class, ServerboundPlayerAbilitiesPacket::new)
/* 309 */       .<ServerboundPlayerActionPacket>addPacket(ServerboundPlayerActionPacket.class, ServerboundPlayerActionPacket::new)
/* 310 */       .<ServerboundPlayerCommandPacket>addPacket(ServerboundPlayerCommandPacket.class, ServerboundPlayerCommandPacket::new)
/* 311 */       .<ServerboundPlayerInputPacket>addPacket(ServerboundPlayerInputPacket.class, ServerboundPlayerInputPacket::new)
/* 312 */       .<ServerboundRecipeBookChangeSettingsPacket>addPacket(ServerboundRecipeBookChangeSettingsPacket.class, ServerboundRecipeBookChangeSettingsPacket::new)
/* 313 */       .<ServerboundRecipeBookSeenRecipePacket>addPacket(ServerboundRecipeBookSeenRecipePacket.class, ServerboundRecipeBookSeenRecipePacket::new)
/* 314 */       .<ServerboundRenameItemPacket>addPacket(ServerboundRenameItemPacket.class, ServerboundRenameItemPacket::new)
/* 315 */       .<ServerboundResourcePackPacket>addPacket(ServerboundResourcePackPacket.class, ServerboundResourcePackPacket::new)
/* 316 */       .<ServerboundSeenAdvancementsPacket>addPacket(ServerboundSeenAdvancementsPacket.class, ServerboundSeenAdvancementsPacket::new)
/* 317 */       .<ServerboundSelectTradePacket>addPacket(ServerboundSelectTradePacket.class, ServerboundSelectTradePacket::new)
/* 318 */       .<ServerboundSetBeaconPacket>addPacket(ServerboundSetBeaconPacket.class, ServerboundSetBeaconPacket::new)
/* 319 */       .<ServerboundSetCarriedItemPacket>addPacket(ServerboundSetCarriedItemPacket.class, ServerboundSetCarriedItemPacket::new)
/* 320 */       .<ServerboundSetCommandBlockPacket>addPacket(ServerboundSetCommandBlockPacket.class, ServerboundSetCommandBlockPacket::new)
/* 321 */       .<ServerboundSetCommandMinecartPacket>addPacket(ServerboundSetCommandMinecartPacket.class, ServerboundSetCommandMinecartPacket::new)
/* 322 */       .<ServerboundSetCreativeModeSlotPacket>addPacket(ServerboundSetCreativeModeSlotPacket.class, ServerboundSetCreativeModeSlotPacket::new)
/* 323 */       .<ServerboundSetJigsawBlockPacket>addPacket(ServerboundSetJigsawBlockPacket.class, ServerboundSetJigsawBlockPacket::new)
/* 324 */       .<ServerboundSetStructureBlockPacket>addPacket(ServerboundSetStructureBlockPacket.class, ServerboundSetStructureBlockPacket::new)
/* 325 */       .<ServerboundSignUpdatePacket>addPacket(ServerboundSignUpdatePacket.class, ServerboundSignUpdatePacket::new)
/* 326 */       .<ServerboundSwingPacket>addPacket(ServerboundSwingPacket.class, ServerboundSwingPacket::new)
/* 327 */       .<ServerboundTeleportToEntityPacket>addPacket(ServerboundTeleportToEntityPacket.class, ServerboundTeleportToEntityPacket::new)
/* 328 */       .<ServerboundUseItemOnPacket>addPacket(ServerboundUseItemOnPacket.class, ServerboundUseItemOnPacket::new)
/* 329 */       .addPacket(ServerboundUseItemPacket.class, ServerboundUseItemPacket::new))),
/*     */ 
/*     */   
/* 332 */   STATUS(1, 
/* 333 */     protocol()
/* 334 */     .<PacketListener>addFlow(PacketFlow.SERVERBOUND, (new PacketSet<>())
/* 335 */       .<ServerboundStatusRequestPacket>addPacket(ServerboundStatusRequestPacket.class, ServerboundStatusRequestPacket::new)
/* 336 */       .addPacket(ServerboundPingRequestPacket.class, ServerboundPingRequestPacket::new))
/*     */     
/* 338 */     .addFlow(PacketFlow.CLIENTBOUND, (new PacketSet<>())
/* 339 */       .<ClientboundStatusResponsePacket>addPacket(ClientboundStatusResponsePacket.class, ClientboundStatusResponsePacket::new)
/* 340 */       .addPacket(ClientboundPongResponsePacket.class, ClientboundPongResponsePacket::new))),
/*     */ 
/*     */   
/* 343 */   LOGIN(2, 
/*     */ 
/*     */     
/* 346 */     protocol()
/* 347 */     .<PacketListener>addFlow(PacketFlow.CLIENTBOUND, (new PacketSet<>())
/* 348 */       .<ClientboundLoginDisconnectPacket>addPacket(ClientboundLoginDisconnectPacket.class, ClientboundLoginDisconnectPacket::new)
/* 349 */       .<ClientboundHelloPacket>addPacket(ClientboundHelloPacket.class, ClientboundHelloPacket::new)
/* 350 */       .<ClientboundGameProfilePacket>addPacket(ClientboundGameProfilePacket.class, ClientboundGameProfilePacket::new)
/* 351 */       .<ClientboundLoginCompressionPacket>addPacket(ClientboundLoginCompressionPacket.class, ClientboundLoginCompressionPacket::new)
/* 352 */       .addPacket(ClientboundCustomQueryPacket.class, ClientboundCustomQueryPacket::new))
/*     */     
/* 354 */     .addFlow(PacketFlow.SERVERBOUND, (new PacketSet<>())
/* 355 */       .<ServerboundHelloPacket>addPacket(ServerboundHelloPacket.class, ServerboundHelloPacket::new)
/* 356 */       .<ServerboundKeyPacket>addPacket(ServerboundKeyPacket.class, ServerboundKeyPacket::new)
/* 357 */       .addPacket(ServerboundCustomQueryPacket.class, ServerboundCustomQueryPacket::new))); private static final ConnectionProtocol[] LOOKUP; private static final Map<Class<? extends Packet<?>>, ConnectionProtocol> PROTOCOL_BY_PACKET; private final int id; private final Map<PacketFlow, ? extends PacketSet<?>> flows;
/*     */   static class PacketSet<T extends PacketListener> { private final Object2IntMap<Class<? extends Packet<T>>> classToId;
/*     */     private final List<Supplier<? extends Packet<T>>> idToConstructor;
/*     */     
/*     */     private PacketSet() {
/* 362 */       this.classToId = (Object2IntMap<Class<? extends Packet<T>>>)Util.make(new Object2IntOpenHashMap(), debug0 -> debug0.defaultReturnValue(-1));
/* 363 */       this.idToConstructor = Lists.newArrayList();
/*     */     }
/*     */     public <P extends Packet<T>> PacketSet<T> addPacket(Class<P> debug1, Supplier<P> debug2) {
/* 366 */       int debug3 = this.idToConstructor.size();
/* 367 */       int debug4 = this.classToId.put(debug1, debug3);
/*     */       
/* 369 */       if (debug4 != -1) {
/* 370 */         String debug5 = "Packet " + debug1 + " is already registered to ID " + debug4;
/* 371 */         LogManager.getLogger().fatal(debug5);
/* 372 */         throw new IllegalArgumentException(debug5);
/*     */       } 
/*     */       
/* 375 */       this.idToConstructor.add(debug2);
/* 376 */       return this;
/*     */     }
/*     */     
/*     */     @Nullable
/*     */     public Integer getId(Class<?> debug1) {
/* 381 */       int debug2 = this.classToId.getInt(debug1);
/* 382 */       return (debug2 == -1) ? null : Integer.valueOf(debug2);
/*     */     }
/*     */     
/*     */     @Nullable
/*     */     public Packet<?> createPacket(int debug1) {
/* 387 */       Supplier<? extends Packet<T>> debug2 = this.idToConstructor.get(debug1);
/* 388 */       return (debug2 != null) ? debug2.get() : null;
/*     */     }
/*     */     
/*     */     public Iterable<Class<? extends Packet<?>>> getAllPackets() {
/* 392 */       return Iterables.unmodifiableIterable((Iterable)this.classToId.keySet());
/*     */     } }
/*     */ 
/*     */   
/*     */   private static ProtocolBuilder protocol() {
/* 397 */     return new ProtocolBuilder();
/*     */   }
/*     */   
/*     */   static class ProtocolBuilder {
/* 401 */     private final Map<PacketFlow, ConnectionProtocol.PacketSet<?>> flows = Maps.newEnumMap(PacketFlow.class);
/*     */     
/*     */     public <T extends PacketListener> ProtocolBuilder addFlow(PacketFlow debug1, ConnectionProtocol.PacketSet<T> debug2) {
/* 404 */       this.flows.put(debug1, debug2);
/* 405 */       return this;
/*     */     }
/*     */     
/*     */     private ProtocolBuilder() {}
/*     */   }
/*     */   
/* 411 */   static { LOOKUP = new ConnectionProtocol[4];
/* 412 */     PROTOCOL_BY_PACKET = Maps.newHashMap();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 437 */     for (ConnectionProtocol debug3 : values()) {
/* 438 */       int debug4 = debug3.getId();
/* 439 */       if (debug4 < -1 || debug4 > 2) {
/* 440 */         throw new Error("Invalid protocol ID " + Integer.toString(debug4));
/*     */       }
/* 442 */       LOOKUP[debug4 - -1] = debug3;
/*     */       
/* 444 */       debug3.flows.forEach((debug1, debug2) -> debug2.getAllPackets().forEach(()));
/*     */     }  }
/*     */   
/*     */   ConnectionProtocol(int debug3, ProtocolBuilder debug4) {
/*     */     this.id = debug3;
/*     */     this.flows = debug4.flows;
/*     */   }
/*     */   @Nullable
/*     */   public Integer getPacketId(PacketFlow debug1, Packet<?> debug2) {
/*     */     return ((PacketSet)this.flows.get(debug1)).getId(debug2.getClass());
/*     */   }
/*     */   @Nullable
/* 456 */   public static ConnectionProtocol getById(int debug0) { if (debug0 < -1 || debug0 > 2) {
/* 457 */       return null;
/*     */     }
/* 459 */     return LOOKUP[debug0 - -1]; }
/*     */   @Nullable
/*     */   public Packet<?> createPacket(PacketFlow debug1, int debug2) { return ((PacketSet)this.flows.get(debug1)).createPacket(debug2); }
/*     */   public int getId() { return this.id; } public static ConnectionProtocol getProtocolForPacket(Packet<?> debug0) {
/* 463 */     return PROTOCOL_BY_PACKET.get(debug0.getClass());
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\ConnectionProtocol.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */