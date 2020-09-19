/*      */ package net.minecraft.server.level;
/*      */ import com.google.common.collect.Lists;
/*      */ import com.mojang.authlib.GameProfile;
/*      */ import com.mojang.datafixers.util.Either;
/*      */ import com.mojang.serialization.DynamicOps;
/*      */ import io.netty.util.concurrent.Future;
/*      */ import java.util.Collection;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Optional;
/*      */ import java.util.OptionalInt;
/*      */ import java.util.Random;
/*      */ import java.util.UUID;
/*      */ import javax.annotation.Nullable;
/*      */ import net.minecraft.BlockUtil;
/*      */ import net.minecraft.ChatFormatting;
/*      */ import net.minecraft.CrashReport;
/*      */ import net.minecraft.CrashReportCategory;
/*      */ import net.minecraft.ReportedException;
/*      */ import net.minecraft.Util;
/*      */ import net.minecraft.advancements.CriteriaTriggers;
/*      */ import net.minecraft.commands.arguments.EntityAnchorArgument;
/*      */ import net.minecraft.core.BlockPos;
/*      */ import net.minecraft.core.Direction;
/*      */ import net.minecraft.core.NonNullList;
/*      */ import net.minecraft.core.SectionPos;
/*      */ import net.minecraft.core.Vec3i;
/*      */ import net.minecraft.nbt.CompoundTag;
/*      */ import net.minecraft.nbt.NbtOps;
/*      */ import net.minecraft.nbt.Tag;
/*      */ import net.minecraft.network.chat.ChatType;
/*      */ import net.minecraft.network.chat.Component;
/*      */ import net.minecraft.network.chat.HoverEvent;
/*      */ import net.minecraft.network.chat.MutableComponent;
/*      */ import net.minecraft.network.chat.Style;
/*      */ import net.minecraft.network.chat.TextComponent;
/*      */ import net.minecraft.network.chat.TranslatableComponent;
/*      */ import net.minecraft.network.protocol.Packet;
/*      */ import net.minecraft.network.protocol.game.ClientboundAddPlayerPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundAnimatePacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundChangeDifficultyPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundChatPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundContainerClosePacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundContainerSetContentPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundContainerSetDataPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundEntityEventPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundForgetLevelChunkPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundGameEventPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundHorseScreenOpenPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundLevelEventPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundMerchantOffersPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundOpenBookPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundOpenScreenPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundOpenSignEditorPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundPlayerAbilitiesPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundPlayerCombatPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundPlayerLookAtPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundRemoveMobEffectPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundResourcePackPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundRespawnPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundSetCameraPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundSetExperiencePacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundSetHealthPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundSoundPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundUpdateMobEffectPacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundClientInformationPacket;
/*      */ import net.minecraft.resources.ResourceKey;
/*      */ import net.minecraft.resources.ResourceLocation;
/*      */ import net.minecraft.server.MinecraftServer;
/*      */ import net.minecraft.server.PlayerAdvancements;
/*      */ import net.minecraft.server.network.ServerGamePacketListenerImpl;
/*      */ import net.minecraft.server.players.PlayerList;
/*      */ import net.minecraft.sounds.SoundEvent;
/*      */ import net.minecraft.sounds.SoundSource;
/*      */ import net.minecraft.stats.RecipeBook;
/*      */ import net.minecraft.stats.ServerRecipeBook;
/*      */ import net.minecraft.stats.ServerStatsCounter;
/*      */ import net.minecraft.stats.Stat;
/*      */ import net.minecraft.stats.Stats;
/*      */ import net.minecraft.util.Mth;
/*      */ import net.minecraft.util.Unit;
/*      */ import net.minecraft.world.Container;
/*      */ import net.minecraft.world.InteractionHand;
/*      */ import net.minecraft.world.MenuProvider;
/*      */ import net.minecraft.world.damagesource.DamageSource;
/*      */ import net.minecraft.world.effect.MobEffectInstance;
/*      */ import net.minecraft.world.effect.MobEffects;
/*      */ import net.minecraft.world.entity.Entity;
/*      */ import net.minecraft.world.entity.HumanoidArm;
/*      */ import net.minecraft.world.entity.LivingEntity;
/*      */ import net.minecraft.world.entity.Mob;
/*      */ import net.minecraft.world.entity.NeutralMob;
/*      */ import net.minecraft.world.entity.animal.horse.AbstractHorse;
/*      */ import net.minecraft.world.entity.item.ItemEntity;
/*      */ import net.minecraft.world.entity.monster.Monster;
/*      */ import net.minecraft.world.entity.player.ChatVisiblity;
/*      */ import net.minecraft.world.entity.player.Player;
/*      */ import net.minecraft.world.entity.projectile.AbstractArrow;
/*      */ import net.minecraft.world.inventory.AbstractContainerMenu;
/*      */ import net.minecraft.world.inventory.ContainerListener;
/*      */ import net.minecraft.world.inventory.HorseInventoryMenu;
/*      */ import net.minecraft.world.item.ComplexItem;
/*      */ import net.minecraft.world.item.Item;
/*      */ import net.minecraft.world.item.ItemCooldowns;
/*      */ import net.minecraft.world.item.ItemStack;
/*      */ import net.minecraft.world.item.Items;
/*      */ import net.minecraft.world.item.ServerItemCooldowns;
/*      */ import net.minecraft.world.item.WrittenBookItem;
/*      */ import net.minecraft.world.item.crafting.Recipe;
/*      */ import net.minecraft.world.item.trading.MerchantOffers;
/*      */ import net.minecraft.world.level.ChunkPos;
/*      */ import net.minecraft.world.level.GameRules;
/*      */ import net.minecraft.world.level.GameType;
/*      */ import net.minecraft.world.level.Level;
/*      */ import net.minecraft.world.level.biome.BiomeManager;
/*      */ import net.minecraft.world.level.block.Blocks;
/*      */ import net.minecraft.world.level.block.HorizontalDirectionalBlock;
/*      */ import net.minecraft.world.level.block.NetherPortalBlock;
/*      */ import net.minecraft.world.level.block.entity.BlockEntity;
/*      */ import net.minecraft.world.level.block.entity.CommandBlockEntity;
/*      */ import net.minecraft.world.level.block.entity.SignBlockEntity;
/*      */ import net.minecraft.world.level.block.state.BlockState;
/*      */ import net.minecraft.world.level.block.state.properties.Property;
/*      */ import net.minecraft.world.level.portal.PortalInfo;
/*      */ import net.minecraft.world.level.storage.LevelData;
/*      */ import net.minecraft.world.phys.AABB;
/*      */ import net.minecraft.world.phys.Vec3;
/*      */ import net.minecraft.world.scores.PlayerTeam;
/*      */ import net.minecraft.world.scores.Score;
/*      */ import net.minecraft.world.scores.Team;
/*      */ import net.minecraft.world.scores.criteria.ObjectiveCriteria;
/*      */ import org.apache.logging.log4j.LogManager;
/*      */ import org.apache.logging.log4j.Logger;
/*      */ 
/*      */ public class ServerPlayer extends Player implements ContainerListener {
/*  139 */   private static final Logger LOGGER = LogManager.getLogger();
/*      */   
/*      */   public ServerGamePacketListenerImpl connection;
/*      */   
/*      */   public final MinecraftServer server;
/*      */   public final ServerPlayerGameMode gameMode;
/*  145 */   private final List<Integer> entitiesToRemove = Lists.newLinkedList();
/*      */   private final PlayerAdvancements advancements;
/*      */   private final ServerStatsCounter stats;
/*  148 */   private float lastRecordedHealthAndAbsorption = Float.MIN_VALUE;
/*  149 */   private int lastRecordedFoodLevel = Integer.MIN_VALUE;
/*  150 */   private int lastRecordedAirLevel = Integer.MIN_VALUE;
/*  151 */   private int lastRecordedArmor = Integer.MIN_VALUE;
/*  152 */   private int lastRecordedLevel = Integer.MIN_VALUE;
/*  153 */   private int lastRecordedExperience = Integer.MIN_VALUE;
/*  154 */   private float lastSentHealth = -1.0E8F;
/*  155 */   private int lastSentFood = -99999999;
/*      */   private boolean lastFoodSaturationZero = true;
/*  157 */   private int lastSentExp = -99999999;
/*  158 */   private int spawnInvulnerableTime = 60;
/*      */   private ChatVisiblity chatVisibility;
/*      */   private boolean canChatColor = true;
/*  161 */   private long lastActionTime = Util.getMillis();
/*      */   private Entity camera;
/*      */   private boolean isChangingDimension;
/*      */   private boolean seenCredits;
/*  165 */   private final ServerRecipeBook recipeBook = new ServerRecipeBook();
/*      */   private Vec3 levitationStartPos;
/*      */   private int levitationStartTime;
/*      */   private boolean disconnected;
/*      */   @Nullable
/*      */   private Vec3 enteredNetherPosition;
/*  171 */   private SectionPos lastSectionPos = SectionPos.of(0, 0, 0);
/*      */   
/*  173 */   private ResourceKey<Level> respawnDimension = Level.OVERWORLD;
/*      */   
/*      */   @Nullable
/*      */   private BlockPos respawnPosition;
/*      */   private boolean respawnForced;
/*      */   private float respawnAngle;
/*      */   
/*      */   public ServerPlayer(MinecraftServer debug1, ServerLevel debug2, GameProfile debug3, ServerPlayerGameMode debug4) {
/*  181 */     super(debug2, debug2.getSharedSpawnPos(), debug2.getSharedSpawnAngle(), debug3);
/*  182 */     debug4.player = this;
/*  183 */     this.gameMode = debug4;
/*      */     
/*  185 */     this.server = debug1;
/*  186 */     this.stats = debug1.getPlayerList().getPlayerStats(this);
/*  187 */     this.advancements = debug1.getPlayerList().getPlayerAdvancements(this);
/*  188 */     this.maxUpStep = 1.0F;
/*      */     
/*  190 */     fudgeSpawnLocation(debug2);
/*      */   }
/*      */   private int containerCounter; public boolean ignoreSlotUpdateHack; public int latency; public boolean wonGame;
/*      */   private void fudgeSpawnLocation(ServerLevel debug1) {
/*  194 */     BlockPos debug2 = debug1.getSharedSpawnPos();
/*      */ 
/*      */     
/*  197 */     if (debug1.dimensionType().hasSkyLight() && debug1.getServer().getWorldData().getGameType() != GameType.ADVENTURE) {
/*  198 */       int debug3 = Math.max(0, this.server.getSpawnRadius(debug1));
/*  199 */       int debug4 = Mth.floor(debug1.getWorldBorder().getDistanceToBorder(debug2.getX(), debug2.getZ()));
/*  200 */       if (debug4 < debug3) {
/*  201 */         debug3 = debug4;
/*      */       }
/*  203 */       if (debug4 <= 1) {
/*  204 */         debug3 = 1;
/*      */       }
/*      */       
/*  207 */       long debug5 = (debug3 * 2 + 1);
/*  208 */       long debug7 = debug5 * debug5;
/*  209 */       int debug9 = (debug7 > 2147483647L) ? Integer.MAX_VALUE : (int)debug7;
/*  210 */       int debug10 = getCoprime(debug9);
/*  211 */       int debug11 = (new Random()).nextInt(debug9);
/*      */       
/*  213 */       for (int debug12 = 0; debug12 < debug9; debug12++) {
/*  214 */         int debug13 = (debug11 + debug10 * debug12) % debug9;
/*  215 */         int debug14 = debug13 % (debug3 * 2 + 1);
/*  216 */         int debug15 = debug13 / (debug3 * 2 + 1);
/*      */         
/*  218 */         BlockPos debug16 = PlayerRespawnLogic.getOverworldRespawnPos(debug1, debug2.getX() + debug14 - debug3, debug2.getZ() + debug15 - debug3, false);
/*      */         
/*  220 */         if (debug16 != null) {
/*  221 */           moveTo(debug16, 0.0F, 0.0F);
/*  222 */           if (debug1.noCollision((Entity)this)) {
/*      */             break;
/*      */           }
/*      */         } 
/*      */       } 
/*      */     } else {
/*  228 */       moveTo(debug2, 0.0F, 0.0F);
/*  229 */       while (!debug1.noCollision((Entity)this) && getY() < 255.0D) {
/*  230 */         setPos(getX(), getY() + 1.0D, getZ());
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private int getCoprime(int debug1) {
/*  237 */     return (debug1 <= 16) ? (debug1 - 1) : 17;
/*      */   }
/*      */ 
/*      */   
/*      */   public void readAdditionalSaveData(CompoundTag debug1) {
/*  242 */     super.readAdditionalSaveData(debug1);
/*      */     
/*  244 */     if (debug1.contains("playerGameType", 99)) {
/*  245 */       if (getServer().getForceGameType()) {
/*  246 */         this.gameMode.setGameModeForPlayer(getServer().getDefaultGameType(), GameType.NOT_SET);
/*      */       } else {
/*  248 */         this.gameMode.setGameModeForPlayer(GameType.byId(debug1.getInt("playerGameType")), debug1.contains("previousPlayerGameType", 3) ? GameType.byId(debug1.getInt("previousPlayerGameType")) : GameType.NOT_SET);
/*      */       } 
/*      */     }
/*      */     
/*  252 */     if (debug1.contains("enteredNetherPosition", 10)) {
/*  253 */       CompoundTag debug2 = debug1.getCompound("enteredNetherPosition");
/*  254 */       this.enteredNetherPosition = new Vec3(debug2.getDouble("x"), debug2.getDouble("y"), debug2.getDouble("z"));
/*      */     } 
/*      */     
/*  257 */     this.seenCredits = debug1.getBoolean("seenCredits");
/*      */     
/*  259 */     if (debug1.contains("recipeBook", 10)) {
/*  260 */       this.recipeBook.fromNbt(debug1.getCompound("recipeBook"), this.server.getRecipeManager());
/*      */     }
/*      */     
/*  263 */     if (isSleeping()) {
/*  264 */       stopSleeping();
/*      */     }
/*      */     
/*  267 */     if (debug1.contains("SpawnX", 99) && debug1.contains("SpawnY", 99) && debug1.contains("SpawnZ", 99)) {
/*  268 */       this.respawnPosition = new BlockPos(debug1.getInt("SpawnX"), debug1.getInt("SpawnY"), debug1.getInt("SpawnZ"));
/*  269 */       this.respawnForced = debug1.getBoolean("SpawnForced");
/*  270 */       this.respawnAngle = debug1.getFloat("SpawnAngle");
/*  271 */       if (debug1.contains("SpawnDimension")) {
/*  272 */         this.respawnDimension = Level.RESOURCE_KEY_CODEC.parse((DynamicOps)NbtOps.INSTANCE, debug1.get("SpawnDimension")).resultOrPartial(LOGGER::error).orElse(Level.OVERWORLD);
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void addAdditionalSaveData(CompoundTag debug1) {
/*  279 */     super.addAdditionalSaveData(debug1);
/*      */     
/*  281 */     debug1.putInt("playerGameType", this.gameMode.getGameModeForPlayer().getId());
/*  282 */     debug1.putInt("previousPlayerGameType", this.gameMode.getPreviousGameModeForPlayer().getId());
/*  283 */     debug1.putBoolean("seenCredits", this.seenCredits);
/*      */     
/*  285 */     if (this.enteredNetherPosition != null) {
/*  286 */       CompoundTag compoundTag = new CompoundTag();
/*  287 */       compoundTag.putDouble("x", this.enteredNetherPosition.x);
/*  288 */       compoundTag.putDouble("y", this.enteredNetherPosition.y);
/*  289 */       compoundTag.putDouble("z", this.enteredNetherPosition.z);
/*  290 */       debug1.put("enteredNetherPosition", (Tag)compoundTag);
/*      */     } 
/*      */     
/*  293 */     Entity debug2 = getRootVehicle();
/*  294 */     Entity debug3 = getVehicle();
/*  295 */     if (debug3 != null && debug2 != this && debug2.hasOnePlayerPassenger()) {
/*  296 */       CompoundTag debug4 = new CompoundTag();
/*  297 */       CompoundTag debug5 = new CompoundTag();
/*  298 */       debug2.save(debug5);
/*      */       
/*  300 */       debug4.putUUID("Attach", debug3.getUUID());
/*  301 */       debug4.put("Entity", (Tag)debug5);
/*  302 */       debug1.put("RootVehicle", (Tag)debug4);
/*      */     } 
/*      */     
/*  305 */     debug1.put("recipeBook", (Tag)this.recipeBook.toNbt());
/*      */     
/*  307 */     debug1.putString("Dimension", this.level.dimension().location().toString());
/*      */     
/*  309 */     if (this.respawnPosition != null) {
/*  310 */       debug1.putInt("SpawnX", this.respawnPosition.getX());
/*  311 */       debug1.putInt("SpawnY", this.respawnPosition.getY());
/*  312 */       debug1.putInt("SpawnZ", this.respawnPosition.getZ());
/*  313 */       debug1.putBoolean("SpawnForced", this.respawnForced);
/*  314 */       debug1.putFloat("SpawnAngle", this.respawnAngle);
/*  315 */       ResourceLocation.CODEC.encodeStart((DynamicOps)NbtOps.INSTANCE, this.respawnDimension.location()).resultOrPartial(LOGGER::error).ifPresent(debug1 -> debug0.put("SpawnDimension", debug1));
/*      */     } 
/*      */   }
/*      */   
/*      */   public void setExperiencePoints(int debug1) {
/*  320 */     float debug2 = getXpNeededForNextLevel();
/*  321 */     float debug3 = (debug2 - 1.0F) / debug2;
/*  322 */     this.experienceProgress = Mth.clamp(debug1 / debug2, 0.0F, debug3);
/*  323 */     this.lastSentExp = -1;
/*      */   }
/*      */   
/*      */   public void setExperienceLevels(int debug1) {
/*  327 */     this.experienceLevel = debug1;
/*  328 */     this.lastSentExp = -1;
/*      */   }
/*      */ 
/*      */   
/*      */   public void giveExperienceLevels(int debug1) {
/*  333 */     super.giveExperienceLevels(debug1);
/*  334 */     this.lastSentExp = -1;
/*      */   }
/*      */ 
/*      */   
/*      */   public void onEnchantmentPerformed(ItemStack debug1, int debug2) {
/*  339 */     super.onEnchantmentPerformed(debug1, debug2);
/*  340 */     this.lastSentExp = -1;
/*      */   }
/*      */   
/*      */   public void initMenu() {
/*  344 */     this.containerMenu.addSlotListener(this);
/*      */   }
/*      */ 
/*      */   
/*      */   public void onEnterCombat() {
/*  349 */     super.onEnterCombat();
/*      */     
/*  351 */     this.connection.send((Packet)new ClientboundPlayerCombatPacket(getCombatTracker(), ClientboundPlayerCombatPacket.Event.ENTER_COMBAT));
/*      */   }
/*      */ 
/*      */   
/*      */   public void onLeaveCombat() {
/*  356 */     super.onLeaveCombat();
/*      */     
/*  358 */     this.connection.send((Packet)new ClientboundPlayerCombatPacket(getCombatTracker(), ClientboundPlayerCombatPacket.Event.END_COMBAT));
/*      */   }
/*      */ 
/*      */   
/*      */   protected void onInsideBlock(BlockState debug1) {
/*  363 */     CriteriaTriggers.ENTER_BLOCK.trigger(this, debug1);
/*      */   }
/*      */ 
/*      */   
/*      */   protected ItemCooldowns createItemCooldowns() {
/*  368 */     return (ItemCooldowns)new ServerItemCooldowns(this);
/*      */   }
/*      */ 
/*      */   
/*      */   public void tick() {
/*  373 */     this.gameMode.tick();
/*      */     
/*  375 */     this.spawnInvulnerableTime--;
/*  376 */     if (this.invulnerableTime > 0) {
/*  377 */       this.invulnerableTime--;
/*      */     }
/*  379 */     this.containerMenu.broadcastChanges();
/*      */     
/*  381 */     if (!this.level.isClientSide && 
/*  382 */       !this.containerMenu.stillValid(this)) {
/*  383 */       closeContainer();
/*  384 */       this.containerMenu = (AbstractContainerMenu)this.inventoryMenu;
/*      */     } 
/*      */ 
/*      */     
/*  388 */     while (!this.entitiesToRemove.isEmpty()) {
/*  389 */       int i = Math.min(this.entitiesToRemove.size(), 2147483647);
/*  390 */       int[] debug2 = new int[i];
/*  391 */       Iterator<Integer> debug3 = this.entitiesToRemove.iterator();
/*  392 */       int debug4 = 0;
/*      */       
/*  394 */       while (debug3.hasNext() && debug4 < i) {
/*  395 */         debug2[debug4++] = ((Integer)debug3.next()).intValue();
/*  396 */         debug3.remove();
/*      */       } 
/*      */       
/*  399 */       this.connection.send((Packet)new ClientboundRemoveEntitiesPacket(debug2));
/*      */     } 
/*      */     
/*  402 */     Entity debug1 = getCamera();
/*  403 */     if (debug1 != this) {
/*  404 */       if (debug1.isAlive()) {
/*      */         
/*  406 */         absMoveTo(debug1.getX(), debug1.getY(), debug1.getZ(), debug1.yRot, debug1.xRot);
/*  407 */         getLevel().getChunkSource().move(this);
/*  408 */         if (wantsToStopRiding())
/*      */         {
/*  410 */           setCamera((Entity)this);
/*      */         }
/*      */       } else {
/*  413 */         setCamera((Entity)this);
/*      */       } 
/*      */     }
/*      */     
/*  417 */     CriteriaTriggers.TICK.trigger(this);
/*  418 */     if (this.levitationStartPos != null) {
/*  419 */       CriteriaTriggers.LEVITATION.trigger(this, this.levitationStartPos, this.tickCount - this.levitationStartTime);
/*      */     }
/*      */     
/*  422 */     this.advancements.flushDirty(this);
/*      */   }
/*      */   
/*      */   public void doTick() {
/*      */     try {
/*  427 */       if (!isSpectator() || this.level.hasChunkAt(blockPosition())) {
/*  428 */         super.tick();
/*      */       }
/*      */       
/*  431 */       for (int debug1 = 0; debug1 < this.inventory.getContainerSize(); debug1++) {
/*  432 */         ItemStack debug2 = this.inventory.getItem(debug1);
/*  433 */         if (debug2.getItem().isComplex()) {
/*  434 */           Packet<?> debug3 = ((ComplexItem)debug2.getItem()).getUpdatePacket(debug2, this.level, this);
/*  435 */           if (debug3 != null) {
/*  436 */             this.connection.send(debug3);
/*      */           }
/*      */         } 
/*      */       } 
/*      */       
/*  441 */       if (getHealth() != this.lastSentHealth || this.lastSentFood != this.foodData.getFoodLevel() || ((this.foodData.getSaturationLevel() == 0.0F)) != this.lastFoodSaturationZero) {
/*  442 */         this.connection.send((Packet)new ClientboundSetHealthPacket(getHealth(), this.foodData.getFoodLevel(), this.foodData.getSaturationLevel()));
/*  443 */         this.lastSentHealth = getHealth();
/*  444 */         this.lastSentFood = this.foodData.getFoodLevel();
/*  445 */         this.lastFoodSaturationZero = (this.foodData.getSaturationLevel() == 0.0F);
/*      */       } 
/*      */       
/*  448 */       if (getHealth() + getAbsorptionAmount() != this.lastRecordedHealthAndAbsorption) {
/*  449 */         this.lastRecordedHealthAndAbsorption = getHealth() + getAbsorptionAmount();
/*  450 */         updateScoreForCriteria(ObjectiveCriteria.HEALTH, Mth.ceil(this.lastRecordedHealthAndAbsorption));
/*      */       } 
/*      */       
/*  453 */       if (this.foodData.getFoodLevel() != this.lastRecordedFoodLevel) {
/*  454 */         this.lastRecordedFoodLevel = this.foodData.getFoodLevel();
/*  455 */         updateScoreForCriteria(ObjectiveCriteria.FOOD, Mth.ceil(this.lastRecordedFoodLevel));
/*      */       } 
/*      */       
/*  458 */       if (getAirSupply() != this.lastRecordedAirLevel) {
/*  459 */         this.lastRecordedAirLevel = getAirSupply();
/*  460 */         updateScoreForCriteria(ObjectiveCriteria.AIR, Mth.ceil(this.lastRecordedAirLevel));
/*      */       } 
/*      */       
/*  463 */       if (getArmorValue() != this.lastRecordedArmor) {
/*  464 */         this.lastRecordedArmor = getArmorValue();
/*  465 */         updateScoreForCriteria(ObjectiveCriteria.ARMOR, Mth.ceil(this.lastRecordedArmor));
/*      */       } 
/*      */       
/*  468 */       if (this.totalExperience != this.lastRecordedExperience) {
/*  469 */         this.lastRecordedExperience = this.totalExperience;
/*  470 */         updateScoreForCriteria(ObjectiveCriteria.EXPERIENCE, Mth.ceil(this.lastRecordedExperience));
/*      */       } 
/*      */       
/*  473 */       if (this.experienceLevel != this.lastRecordedLevel) {
/*  474 */         this.lastRecordedLevel = this.experienceLevel;
/*  475 */         updateScoreForCriteria(ObjectiveCriteria.LEVEL, Mth.ceil(this.lastRecordedLevel));
/*      */       } 
/*      */       
/*  478 */       if (this.totalExperience != this.lastSentExp) {
/*  479 */         this.lastSentExp = this.totalExperience;
/*  480 */         this.connection.send((Packet)new ClientboundSetExperiencePacket(this.experienceProgress, this.totalExperience, this.experienceLevel));
/*      */       } 
/*      */       
/*  483 */       if (this.tickCount % 20 == 0) {
/*  484 */         CriteriaTriggers.LOCATION.trigger(this);
/*      */       }
/*  486 */     } catch (Throwable debug1) {
/*  487 */       CrashReport debug2 = CrashReport.forThrowable(debug1, "Ticking player");
/*  488 */       CrashReportCategory debug3 = debug2.addCategory("Player being ticked");
/*      */       
/*  490 */       fillCrashReportCategory(debug3);
/*      */       
/*  492 */       throw new ReportedException(debug2);
/*      */     } 
/*      */   }
/*      */   
/*      */   private void updateScoreForCriteria(ObjectiveCriteria debug1, int debug2) {
/*  497 */     getScoreboard().forAllObjectives(debug1, getScoreboardName(), debug1 -> debug1.setScore(debug0));
/*      */   }
/*      */ 
/*      */   
/*      */   public void die(DamageSource debug1) {
/*  502 */     boolean debug2 = this.level.getGameRules().getBoolean(GameRules.RULE_SHOWDEATHMESSAGES);
/*      */     
/*  504 */     if (debug2) {
/*  505 */       Component component = getCombatTracker().getDeathMessage();
/*  506 */       this.connection.send((Packet)new ClientboundPlayerCombatPacket(getCombatTracker(), ClientboundPlayerCombatPacket.Event.ENTITY_DIED, component), debug2 -> {
/*      */             if (!debug2.isSuccess()) {
/*      */               int debug3 = 256;
/*      */               
/*      */               String debug4 = debug1.getString(256);
/*      */               
/*      */               TranslatableComponent translatableComponent = new TranslatableComponent("death.attack.message_too_long", new Object[] { (new TextComponent(debug4)).withStyle(ChatFormatting.YELLOW) });
/*      */               MutableComponent mutableComponent = (new TranslatableComponent("death.attack.even_more_magic", new Object[] { getDisplayName() })).withStyle(());
/*      */               this.connection.send((Packet)new ClientboundPlayerCombatPacket(getCombatTracker(), ClientboundPlayerCombatPacket.Event.ENTITY_DIED, (Component)mutableComponent));
/*      */             } 
/*      */           });
/*  517 */       Team debug4 = getTeam();
/*  518 */       if (debug4 == null || debug4.getDeathMessageVisibility() == Team.Visibility.ALWAYS) {
/*  519 */         this.server.getPlayerList().broadcastMessage(component, ChatType.SYSTEM, Util.NIL_UUID);
/*  520 */       } else if (debug4.getDeathMessageVisibility() == Team.Visibility.HIDE_FOR_OTHER_TEAMS) {
/*  521 */         this.server.getPlayerList().broadcastToTeam(this, component);
/*  522 */       } else if (debug4.getDeathMessageVisibility() == Team.Visibility.HIDE_FOR_OWN_TEAM) {
/*  523 */         this.server.getPlayerList().broadcastToAllExceptTeam(this, component);
/*      */       } 
/*      */     } else {
/*  526 */       this.connection.send((Packet)new ClientboundPlayerCombatPacket(getCombatTracker(), ClientboundPlayerCombatPacket.Event.ENTITY_DIED));
/*      */     } 
/*  528 */     removeEntitiesOnShoulder();
/*  529 */     if (this.level.getGameRules().getBoolean(GameRules.RULE_FORGIVE_DEAD_PLAYERS)) {
/*  530 */       tellNeutralMobsThatIDied();
/*      */     }
/*      */     
/*  533 */     if (!isSpectator()) {
/*  534 */       dropAllDeathLoot(debug1);
/*      */     }
/*      */     
/*  537 */     getScoreboard().forAllObjectives(ObjectiveCriteria.DEATH_COUNT, getScoreboardName(), Score::increment);
/*      */     
/*  539 */     LivingEntity debug3 = getKillCredit();
/*  540 */     if (debug3 != null) {
/*  541 */       awardStat(Stats.ENTITY_KILLED_BY.get(debug3.getType()));
/*  542 */       debug3.awardKillScore((Entity)this, this.deathScore, debug1);
/*      */       
/*  544 */       createWitherRose(debug3);
/*      */     } 
/*      */     
/*  547 */     this.level.broadcastEntityEvent((Entity)this, (byte)3);
/*      */     
/*  549 */     awardStat(Stats.DEATHS);
/*  550 */     resetStat(Stats.CUSTOM.get(Stats.TIME_SINCE_DEATH));
/*  551 */     resetStat(Stats.CUSTOM.get(Stats.TIME_SINCE_REST));
/*  552 */     clearFire();
/*  553 */     setSharedFlag(0, false);
/*  554 */     getCombatTracker().recheckStatus();
/*      */   }
/*      */   
/*      */   private void tellNeutralMobsThatIDied() {
/*  558 */     AABB debug1 = (new AABB(blockPosition())).inflate(32.0D, 10.0D, 32.0D);
/*  559 */     this.level.getLoadedEntitiesOfClass(Mob.class, debug1).stream()
/*  560 */       .filter(debug0 -> debug0 instanceof NeutralMob)
/*  561 */       .forEach(debug1 -> ((NeutralMob)debug1).playerDied(this));
/*      */   }
/*      */ 
/*      */   
/*      */   public void awardKillScore(Entity debug1, int debug2, DamageSource debug3) {
/*  566 */     if (debug1 == this) {
/*      */       return;
/*      */     }
/*  569 */     super.awardKillScore(debug1, debug2, debug3);
/*  570 */     increaseScore(debug2);
/*      */     
/*  572 */     String debug4 = getScoreboardName();
/*  573 */     String debug5 = debug1.getScoreboardName();
/*      */     
/*  575 */     getScoreboard().forAllObjectives(ObjectiveCriteria.KILL_COUNT_ALL, debug4, Score::increment);
/*      */     
/*  577 */     if (debug1 instanceof Player) {
/*  578 */       awardStat(Stats.PLAYER_KILLS);
/*  579 */       getScoreboard().forAllObjectives(ObjectiveCriteria.KILL_COUNT_PLAYERS, debug4, Score::increment);
/*      */     } else {
/*  581 */       awardStat(Stats.MOB_KILLS);
/*      */     } 
/*      */     
/*  584 */     handleTeamKill(debug4, debug5, ObjectiveCriteria.TEAM_KILL);
/*  585 */     handleTeamKill(debug5, debug4, ObjectiveCriteria.KILLED_BY_TEAM);
/*      */     
/*  587 */     CriteriaTriggers.PLAYER_KILLED_ENTITY.trigger(this, debug1, debug3);
/*      */   }
/*      */   
/*      */   private void handleTeamKill(String debug1, String debug2, ObjectiveCriteria[] debug3) {
/*  591 */     PlayerTeam debug4 = getScoreboard().getPlayersTeam(debug2);
/*  592 */     if (debug4 != null) {
/*  593 */       int debug5 = debug4.getColor().getId();
/*  594 */       if (debug5 >= 0 && debug5 < debug3.length) {
/*  595 */         getScoreboard().forAllObjectives(debug3[debug5], debug1, Score::increment);
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean hurt(DamageSource debug1, float debug2) {
/*  602 */     if (isInvulnerableTo(debug1)) {
/*  603 */       return false;
/*      */     }
/*      */     
/*  606 */     boolean debug3 = (this.server.isDedicatedServer() && isPvpAllowed() && "fall".equals(debug1.msgId));
/*  607 */     if (!debug3 && this.spawnInvulnerableTime > 0 && debug1 != DamageSource.OUT_OF_WORLD) {
/*  608 */       return false;
/*      */     }
/*      */     
/*  611 */     if (debug1 instanceof net.minecraft.world.damagesource.EntityDamageSource) {
/*  612 */       Entity debug4 = debug1.getEntity();
/*      */       
/*  614 */       if (debug4 instanceof Player && !canHarmPlayer((Player)debug4)) {
/*  615 */         return false;
/*      */       }
/*  617 */       if (debug4 instanceof AbstractArrow) {
/*  618 */         AbstractArrow debug5 = (AbstractArrow)debug4;
/*  619 */         Entity debug6 = debug5.getOwner();
/*  620 */         if (debug6 instanceof Player && !canHarmPlayer((Player)debug6)) {
/*  621 */           return false;
/*      */         }
/*      */       } 
/*      */     } 
/*  625 */     return super.hurt(debug1, debug2);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean canHarmPlayer(Player debug1) {
/*  630 */     if (!isPvpAllowed()) {
/*  631 */       return false;
/*      */     }
/*  633 */     return super.canHarmPlayer(debug1);
/*      */   }
/*      */   
/*      */   private boolean isPvpAllowed() {
/*  637 */     return this.server.isPvpAllowed();
/*      */   }
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   protected PortalInfo findDimensionEntryPoint(ServerLevel debug1) {
/*  643 */     PortalInfo debug2 = super.findDimensionEntryPoint(debug1);
/*      */     
/*  645 */     if (debug2 != null && this.level.dimension() == Level.OVERWORLD && debug1.dimension() == Level.END) {
/*      */       
/*  647 */       Vec3 debug3 = debug2.pos.add(0.0D, -1.0D, 0.0D);
/*      */       
/*  649 */       return new PortalInfo(debug3, Vec3.ZERO, 90.0F, 0.0F);
/*      */     } 
/*      */     
/*  652 */     return debug2;
/*      */   }
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public Entity changeDimension(ServerLevel debug1) {
/*  658 */     this.isChangingDimension = true;
/*  659 */     ServerLevel debug2 = getLevel();
/*  660 */     ResourceKey<Level> debug3 = debug2.dimension();
/*      */     
/*  662 */     if (debug3 == Level.END && debug1.dimension() == Level.OVERWORLD) {
/*  663 */       unRide();
/*  664 */       getLevel().removePlayerImmediately(this);
/*  665 */       if (!this.wonGame) {
/*  666 */         this.wonGame = true;
/*  667 */         this.connection.send((Packet)new ClientboundGameEventPacket(ClientboundGameEventPacket.WIN_GAME, this.seenCredits ? 0.0F : 1.0F));
/*  668 */         this.seenCredits = true;
/*      */       } 
/*  670 */       return (Entity)this;
/*      */     } 
/*      */ 
/*      */     
/*  674 */     LevelData debug4 = debug1.getLevelData();
/*  675 */     this.connection.send((Packet)new ClientboundRespawnPacket(debug1.dimensionType(), debug1.dimension(), BiomeManager.obfuscateSeed(debug1.getSeed()), this.gameMode.getGameModeForPlayer(), this.gameMode.getPreviousGameModeForPlayer(), debug1.isDebug(), debug1.isFlat(), true));
/*  676 */     this.connection.send((Packet)new ClientboundChangeDifficultyPacket(debug4.getDifficulty(), debug4.isDifficultyLocked()));
/*  677 */     PlayerList debug5 = this.server.getPlayerList();
/*      */     
/*  679 */     debug5.sendPlayerPermissionLevel(this);
/*      */     
/*  681 */     debug2.removePlayerImmediately(this);
/*      */     
/*  683 */     this.removed = false;
/*      */     
/*  685 */     PortalInfo debug6 = findDimensionEntryPoint(debug1);
/*  686 */     if (debug6 != null) {
/*  687 */       debug2.getProfiler().push("moving");
/*  688 */       if (debug3 == Level.OVERWORLD && debug1.dimension() == Level.NETHER) {
/*  689 */         this.enteredNetherPosition = position();
/*  690 */       } else if (debug1.dimension() == Level.END) {
/*  691 */         createEndPlatform(debug1, new BlockPos(debug6.pos));
/*      */       } 
/*  693 */       debug2.getProfiler().pop();
/*      */       
/*  695 */       debug2.getProfiler().push("placing");
/*      */       
/*  697 */       setLevel(debug1);
/*  698 */       debug1.addDuringPortalTeleport(this);
/*      */       
/*  700 */       setRot(debug6.yRot, debug6.xRot);
/*  701 */       moveTo(debug6.pos.x, debug6.pos.y, debug6.pos.z);
/*  702 */       debug2.getProfiler().pop();
/*      */       
/*  704 */       triggerDimensionChangeTriggers(debug2);
/*      */       
/*  706 */       this.gameMode.setLevel(debug1);
/*  707 */       this.connection.send((Packet)new ClientboundPlayerAbilitiesPacket(this.abilities));
/*  708 */       debug5.sendLevelInfo(this, debug1);
/*  709 */       debug5.sendAllPlayerInfo(this);
/*      */       
/*  711 */       for (MobEffectInstance debug8 : getActiveEffects()) {
/*  712 */         this.connection.send((Packet)new ClientboundUpdateMobEffectPacket(getId(), debug8));
/*      */       }
/*  714 */       this.connection.send((Packet)new ClientboundLevelEventPacket(1032, BlockPos.ZERO, 0, false));
/*      */       
/*  716 */       this.lastSentExp = -1;
/*  717 */       this.lastSentHealth = -1.0F;
/*  718 */       this.lastSentFood = -1;
/*      */     } 
/*  720 */     return (Entity)this;
/*      */   }
/*      */   
/*      */   private void createEndPlatform(ServerLevel debug1, BlockPos debug2) {
/*  724 */     BlockPos.MutableBlockPos debug3 = debug2.mutable();
/*      */     
/*  726 */     for (int debug4 = -2; debug4 <= 2; debug4++) {
/*  727 */       for (int debug5 = -2; debug5 <= 2; debug5++) {
/*  728 */         for (int debug6 = -1; debug6 < 3; debug6++) {
/*  729 */           BlockState debug7 = (debug6 == -1) ? Blocks.OBSIDIAN.defaultBlockState() : Blocks.AIR.defaultBlockState();
/*  730 */           debug1.setBlockAndUpdate((BlockPos)debug3.set((Vec3i)debug2).move(debug5, debug6, debug4), debug7);
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Optional<BlockUtil.FoundRectangle> getExitPortal(ServerLevel debug1, BlockPos debug2, boolean debug3) {
/*  741 */     Optional<BlockUtil.FoundRectangle> debug4 = super.getExitPortal(debug1, debug2, debug3);
/*  742 */     if (debug4.isPresent()) {
/*  743 */       return debug4;
/*      */     }
/*      */     
/*  746 */     Direction.Axis debug5 = this.level.getBlockState(this.portalEntrancePos).getOptionalValue((Property)NetherPortalBlock.AXIS).orElse(Direction.Axis.X);
/*  747 */     Optional<BlockUtil.FoundRectangle> debug6 = debug1.getPortalForcer().createPortal(debug2, debug5);
/*  748 */     if (!debug6.isPresent()) {
/*  749 */       LOGGER.error("Unable to create a portal, likely target out of worldborder");
/*      */     }
/*      */     
/*  752 */     return debug6;
/*      */   }
/*      */   
/*      */   private void triggerDimensionChangeTriggers(ServerLevel debug1) {
/*  756 */     ResourceKey<Level> debug2 = debug1.dimension();
/*  757 */     ResourceKey<Level> debug3 = this.level.dimension();
/*  758 */     CriteriaTriggers.CHANGED_DIMENSION.trigger(this, debug2, debug3);
/*      */     
/*  760 */     if (debug2 == Level.NETHER && debug3 == Level.OVERWORLD && this.enteredNetherPosition != null) {
/*  761 */       CriteriaTriggers.NETHER_TRAVEL.trigger(this, this.enteredNetherPosition);
/*      */     }
/*  763 */     if (debug3 != Level.NETHER) {
/*  764 */       this.enteredNetherPosition = null;
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean broadcastToPlayer(ServerPlayer debug1) {
/*  770 */     if (debug1.isSpectator()) {
/*  771 */       return (getCamera() == this);
/*      */     }
/*      */     
/*  774 */     if (isSpectator()) {
/*  775 */       return false;
/*      */     }
/*      */     
/*  778 */     return super.broadcastToPlayer(debug1);
/*      */   }
/*      */   
/*      */   private void broadcast(BlockEntity debug1) {
/*  782 */     if (debug1 != null) {
/*  783 */       ClientboundBlockEntityDataPacket debug2 = debug1.getUpdatePacket();
/*  784 */       if (debug2 != null) {
/*  785 */         this.connection.send((Packet)debug2);
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void take(Entity debug1, int debug2) {
/*  792 */     super.take(debug1, debug2);
/*  793 */     this.containerMenu.broadcastChanges();
/*      */   }
/*      */ 
/*      */   
/*      */   public Either<Player.BedSleepingProblem, Unit> startSleepInBed(BlockPos debug1) {
/*  798 */     Direction debug2 = (Direction)this.level.getBlockState(debug1).getValue((Property)HorizontalDirectionalBlock.FACING);
/*  799 */     if (isSleeping() || !isAlive()) {
/*  800 */       return Either.left(Player.BedSleepingProblem.OTHER_PROBLEM);
/*      */     }
/*      */     
/*  803 */     if (!this.level.dimensionType().natural())
/*      */     {
/*  805 */       return Either.left(Player.BedSleepingProblem.NOT_POSSIBLE_HERE);
/*      */     }
/*      */     
/*  808 */     if (!bedInRange(debug1, debug2)) {
/*  809 */       return Either.left(Player.BedSleepingProblem.TOO_FAR_AWAY);
/*      */     }
/*      */     
/*  812 */     if (bedBlocked(debug1, debug2)) {
/*  813 */       return Either.left(Player.BedSleepingProblem.OBSTRUCTED);
/*      */     }
/*      */ 
/*      */     
/*  817 */     setRespawnPosition(this.level.dimension(), debug1, this.yRot, false, true);
/*  818 */     if (this.level.isDay()) {
/*  819 */       return Either.left(Player.BedSleepingProblem.NOT_POSSIBLE_NOW);
/*      */     }
/*      */     
/*  822 */     if (!isCreative()) {
/*  823 */       double d1 = 8.0D;
/*  824 */       double debug5 = 5.0D;
/*  825 */       Vec3 debug7 = Vec3.atBottomCenterOf((Vec3i)debug1);
/*  826 */       List<Monster> debug8 = this.level.getEntitiesOfClass(Monster.class, new AABB(debug7.x() - 8.0D, debug7.y() - 5.0D, debug7.z() - 8.0D, debug7.x() + 8.0D, debug7.y() + 5.0D, debug7.z() + 8.0D), debug1 -> debug1.isPreventingPlayerRest(this));
/*  827 */       if (!debug8.isEmpty()) {
/*  828 */         return Either.left(Player.BedSleepingProblem.NOT_SAFE);
/*      */       }
/*      */     } 
/*      */     
/*  832 */     Either<Player.BedSleepingProblem, Unit> debug3 = super.startSleepInBed(debug1).ifRight(debug1 -> {
/*      */           awardStat(Stats.SLEEP_IN_BED);
/*      */           CriteriaTriggers.SLEPT_IN_BED.trigger(this);
/*      */         });
/*  836 */     ((ServerLevel)this.level).updateSleepingPlayerList();
/*  837 */     return debug3;
/*      */   }
/*      */ 
/*      */   
/*      */   public void startSleeping(BlockPos debug1) {
/*  842 */     resetStat(Stats.CUSTOM.get(Stats.TIME_SINCE_REST));
/*  843 */     super.startSleeping(debug1);
/*      */   }
/*      */   
/*      */   private boolean bedInRange(BlockPos debug1, Direction debug2) {
/*  847 */     return (isReachableBedBlock(debug1) || isReachableBedBlock(debug1.relative(debug2.getOpposite())));
/*      */   }
/*      */   
/*      */   private boolean isReachableBedBlock(BlockPos debug1) {
/*  851 */     Vec3 debug2 = Vec3.atBottomCenterOf((Vec3i)debug1);
/*  852 */     return (Math.abs(getX() - debug2.x()) <= 3.0D && Math.abs(getY() - debug2.y()) <= 2.0D && Math.abs(getZ() - debug2.z()) <= 3.0D);
/*      */   }
/*      */   
/*      */   private boolean bedBlocked(BlockPos debug1, Direction debug2) {
/*  856 */     BlockPos debug3 = debug1.above();
/*  857 */     return (!freeAt(debug3) || !freeAt(debug3.relative(debug2.getOpposite())));
/*      */   }
/*      */ 
/*      */   
/*      */   public void stopSleepInBed(boolean debug1, boolean debug2) {
/*  862 */     if (isSleeping()) {
/*  863 */       getLevel().getChunkSource().broadcastAndSend((Entity)this, (Packet<?>)new ClientboundAnimatePacket((Entity)this, 2));
/*      */     }
/*  865 */     super.stopSleepInBed(debug1, debug2);
/*  866 */     if (this.connection != null) {
/*  867 */       this.connection.teleport(getX(), getY(), getZ(), this.yRot, this.xRot);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean startRiding(Entity debug1, boolean debug2) {
/*  873 */     Entity debug3 = getVehicle();
/*      */     
/*  875 */     if (!super.startRiding(debug1, debug2)) {
/*  876 */       return false;
/*      */     }
/*      */     
/*  879 */     Entity debug4 = getVehicle();
/*  880 */     if (debug4 != debug3 && this.connection != null) {
/*  881 */       this.connection.teleport(getX(), getY(), getZ(), this.yRot, this.xRot);
/*      */     }
/*      */     
/*  884 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public void stopRiding() {
/*  889 */     Entity debug1 = getVehicle();
/*  890 */     super.stopRiding();
/*      */     
/*  892 */     Entity debug2 = getVehicle();
/*  893 */     if (debug2 != debug1 && this.connection != null) {
/*  894 */       this.connection.teleport(getX(), getY(), getZ(), this.yRot, this.xRot);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isInvulnerableTo(DamageSource debug1) {
/*  900 */     return (super.isInvulnerableTo(debug1) || isChangingDimension() || (this.abilities.invulnerable && debug1 == DamageSource.WITHER));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void checkFallDamage(double debug1, boolean debug3, BlockState debug4, BlockPos debug5) {}
/*      */ 
/*      */ 
/*      */   
/*      */   protected void onChangedBlock(BlockPos debug1) {
/*  910 */     if (!isSpectator()) {
/*  911 */       super.onChangedBlock(debug1);
/*      */     }
/*      */   }
/*      */   
/*      */   public void doCheckFallDamage(double debug1, boolean debug3) {
/*  916 */     BlockPos debug4 = getOnPos();
/*  917 */     if (!this.level.hasChunkAt(debug4)) {
/*      */       return;
/*      */     }
/*  920 */     super.checkFallDamage(debug1, debug3, this.level.getBlockState(debug4), debug4);
/*      */   }
/*      */ 
/*      */   
/*      */   public void openTextEdit(SignBlockEntity debug1) {
/*  925 */     debug1.setAllowedPlayerEditor(this);
/*  926 */     this.connection.send((Packet)new ClientboundOpenSignEditorPacket(debug1.getBlockPos()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void nextContainerCounter() {
/*  935 */     this.containerCounter = this.containerCounter % 100 + 1;
/*      */   }
/*      */ 
/*      */   
/*      */   public OptionalInt openMenu(@Nullable MenuProvider debug1) {
/*  940 */     if (debug1 == null) {
/*  941 */       return OptionalInt.empty();
/*      */     }
/*      */     
/*  944 */     if (this.containerMenu != this.inventoryMenu) {
/*  945 */       closeContainer();
/*      */     }
/*      */     
/*  948 */     nextContainerCounter();
/*      */     
/*  950 */     AbstractContainerMenu debug2 = debug1.createMenu(this.containerCounter, this.inventory, this);
/*  951 */     if (debug2 == null) {
/*  952 */       if (isSpectator()) {
/*  953 */         displayClientMessage((Component)(new TranslatableComponent("container.spectatorCantOpen")).withStyle(ChatFormatting.RED), true);
/*      */       }
/*  955 */       return OptionalInt.empty();
/*      */     } 
/*  957 */     this.connection.send((Packet)new ClientboundOpenScreenPacket(debug2.containerId, debug2.getType(), debug1.getDisplayName()));
/*  958 */     debug2.addSlotListener(this);
/*  959 */     this.containerMenu = debug2;
/*  960 */     return OptionalInt.of(this.containerCounter);
/*      */   }
/*      */ 
/*      */   
/*      */   public void sendMerchantOffers(int debug1, MerchantOffers debug2, int debug3, int debug4, boolean debug5, boolean debug6) {
/*  965 */     this.connection.send((Packet)new ClientboundMerchantOffersPacket(debug1, debug2, debug3, debug4, debug5, debug6));
/*      */   }
/*      */ 
/*      */   
/*      */   public void openHorseInventory(AbstractHorse debug1, Container debug2) {
/*  970 */     if (this.containerMenu != this.inventoryMenu) {
/*  971 */       closeContainer();
/*      */     }
/*  973 */     nextContainerCounter();
/*  974 */     this.connection.send((Packet)new ClientboundHorseScreenOpenPacket(this.containerCounter, debug2.getContainerSize(), debug1.getId()));
/*  975 */     this.containerMenu = (AbstractContainerMenu)new HorseInventoryMenu(this.containerCounter, this.inventory, debug2, debug1);
/*  976 */     this.containerMenu.addSlotListener(this);
/*      */   }
/*      */ 
/*      */   
/*      */   public void openItemGui(ItemStack debug1, InteractionHand debug2) {
/*  981 */     Item debug3 = debug1.getItem();
/*      */     
/*  983 */     if (debug3 == Items.WRITTEN_BOOK) {
/*      */ 
/*      */       
/*  986 */       if (WrittenBookItem.resolveBookComponents(debug1, createCommandSourceStack(), this)) {
/*  987 */         this.containerMenu.broadcastChanges();
/*      */       }
/*      */       
/*  990 */       this.connection.send((Packet)new ClientboundOpenBookPacket(debug2));
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void openCommandBlock(CommandBlockEntity debug1) {
/*  996 */     debug1.setSendToClient(true);
/*  997 */     broadcast((BlockEntity)debug1);
/*      */   }
/*      */ 
/*      */   
/*      */   public void slotChanged(AbstractContainerMenu debug1, int debug2, ItemStack debug3) {
/* 1002 */     if (debug1.getSlot(debug2) instanceof net.minecraft.world.inventory.ResultSlot) {
/*      */       return;
/*      */     }
/*      */     
/* 1006 */     if (debug1 == this.inventoryMenu) {
/* 1007 */       CriteriaTriggers.INVENTORY_CHANGED.trigger(this, this.inventory, debug3);
/*      */     }
/*      */     
/* 1010 */     if (this.ignoreSlotUpdateHack) {
/*      */       return;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1018 */     this.connection.send((Packet)new ClientboundContainerSetSlotPacket(debug1.containerId, debug2, debug3));
/*      */   }
/*      */   
/*      */   public void refreshContainer(AbstractContainerMenu debug1) {
/* 1022 */     refreshContainer(debug1, debug1.getItems());
/*      */   }
/*      */ 
/*      */   
/*      */   public void refreshContainer(AbstractContainerMenu debug1, NonNullList<ItemStack> debug2) {
/* 1027 */     this.connection.send((Packet)new ClientboundContainerSetContentPacket(debug1.containerId, debug2));
/* 1028 */     this.connection.send((Packet)new ClientboundContainerSetSlotPacket(-1, -1, this.inventory.getCarried()));
/*      */   }
/*      */ 
/*      */   
/*      */   public void setContainerData(AbstractContainerMenu debug1, int debug2, int debug3) {
/* 1033 */     this.connection.send((Packet)new ClientboundContainerSetDataPacket(debug1.containerId, debug2, debug3));
/*      */   }
/*      */ 
/*      */   
/*      */   public void closeContainer() {
/* 1038 */     this.connection.send((Packet)new ClientboundContainerClosePacket(this.containerMenu.containerId));
/* 1039 */     doCloseContainer();
/*      */   }
/*      */   
/*      */   public void broadcastCarriedItem() {
/* 1043 */     if (this.ignoreSlotUpdateHack) {
/*      */       return;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1050 */     this.connection.send((Packet)new ClientboundContainerSetSlotPacket(-1, -1, this.inventory.getCarried()));
/*      */   }
/*      */   
/*      */   public void doCloseContainer() {
/* 1054 */     this.containerMenu.removed(this);
/* 1055 */     this.containerMenu = (AbstractContainerMenu)this.inventoryMenu;
/*      */   }
/*      */   
/*      */   public void setPlayerInput(float debug1, float debug2, boolean debug3, boolean debug4) {
/* 1059 */     if (isPassenger()) {
/* 1060 */       if (debug1 >= -1.0F && debug1 <= 1.0F) {
/* 1061 */         this.xxa = debug1;
/*      */       }
/* 1063 */       if (debug2 >= -1.0F && debug2 <= 1.0F) {
/* 1064 */         this.zza = debug2;
/*      */       }
/* 1066 */       this.jumping = debug3;
/* 1067 */       setShiftKeyDown(debug4);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void awardStat(Stat<?> debug1, int debug2) {
/* 1073 */     this.stats.increment(this, debug1, debug2);
/* 1074 */     getScoreboard().forAllObjectives((ObjectiveCriteria)debug1, getScoreboardName(), debug1 -> debug1.add(debug0));
/*      */   }
/*      */ 
/*      */   
/*      */   public void resetStat(Stat<?> debug1) {
/* 1079 */     this.stats.setValue(this, debug1, 0);
/* 1080 */     getScoreboard().forAllObjectives((ObjectiveCriteria)debug1, getScoreboardName(), Score::reset);
/*      */   }
/*      */ 
/*      */   
/*      */   public int awardRecipes(Collection<Recipe<?>> debug1) {
/* 1085 */     return this.recipeBook.addRecipes(debug1, this);
/*      */   }
/*      */ 
/*      */   
/*      */   public void awardRecipesByKey(ResourceLocation[] debug1) {
/* 1090 */     List<Recipe<?>> debug2 = Lists.newArrayList();
/* 1091 */     for (ResourceLocation debug6 : debug1) {
/* 1092 */       this.server.getRecipeManager().byKey(debug6).ifPresent(debug2::add);
/*      */     }
/*      */     
/* 1095 */     awardRecipes(debug2);
/*      */   }
/*      */ 
/*      */   
/*      */   public int resetRecipes(Collection<Recipe<?>> debug1) {
/* 1100 */     return this.recipeBook.removeRecipes(debug1, this);
/*      */   }
/*      */ 
/*      */   
/*      */   public void giveExperiencePoints(int debug1) {
/* 1105 */     super.giveExperiencePoints(debug1);
/* 1106 */     this.lastSentExp = -1;
/*      */   }
/*      */   
/*      */   public void disconnect() {
/* 1110 */     this.disconnected = true;
/* 1111 */     ejectPassengers();
/* 1112 */     if (isSleeping()) {
/* 1113 */       stopSleepInBed(true, false);
/*      */     }
/*      */   }
/*      */   
/*      */   public boolean hasDisconnected() {
/* 1118 */     return this.disconnected;
/*      */   }
/*      */   
/*      */   public void resetSentInfo() {
/* 1122 */     this.lastSentHealth = -1.0E8F;
/*      */   }
/*      */ 
/*      */   
/*      */   public void displayClientMessage(Component debug1, boolean debug2) {
/* 1127 */     this.connection.send((Packet)new ClientboundChatPacket(debug1, debug2 ? ChatType.GAME_INFO : ChatType.CHAT, Util.NIL_UUID));
/*      */   }
/*      */ 
/*      */   
/*      */   protected void completeUsingItem() {
/* 1132 */     if (!this.useItem.isEmpty() && isUsingItem()) {
/* 1133 */       this.connection.send((Packet)new ClientboundEntityEventPacket((Entity)this, (byte)9));
/* 1134 */       super.completeUsingItem();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void lookAt(EntityAnchorArgument.Anchor debug1, Vec3 debug2) {
/* 1140 */     super.lookAt(debug1, debug2);
/* 1141 */     this.connection.send((Packet)new ClientboundPlayerLookAtPacket(debug1, debug2.x, debug2.y, debug2.z));
/*      */   }
/*      */   
/*      */   public void lookAt(EntityAnchorArgument.Anchor debug1, Entity debug2, EntityAnchorArgument.Anchor debug3) {
/* 1145 */     Vec3 debug4 = debug3.apply(debug2);
/* 1146 */     super.lookAt(debug1, debug4);
/* 1147 */     this.connection.send((Packet)new ClientboundPlayerLookAtPacket(debug1, debug2, debug3));
/*      */   }
/*      */   
/*      */   public void restoreFrom(ServerPlayer debug1, boolean debug2) {
/* 1151 */     if (debug2) {
/* 1152 */       this.inventory.replaceWith(debug1.inventory);
/*      */       
/* 1154 */       setHealth(debug1.getHealth());
/* 1155 */       this.foodData = debug1.foodData;
/*      */       
/* 1157 */       this.experienceLevel = debug1.experienceLevel;
/* 1158 */       this.totalExperience = debug1.totalExperience;
/* 1159 */       this.experienceProgress = debug1.experienceProgress;
/*      */       
/* 1161 */       setScore(debug1.getScore());
/* 1162 */       this.portalEntrancePos = debug1.portalEntrancePos;
/* 1163 */     } else if (this.level.getGameRules().getBoolean(GameRules.RULE_KEEPINVENTORY) || debug1.isSpectator()) {
/* 1164 */       this.inventory.replaceWith(debug1.inventory);
/*      */       
/* 1166 */       this.experienceLevel = debug1.experienceLevel;
/* 1167 */       this.totalExperience = debug1.totalExperience;
/* 1168 */       this.experienceProgress = debug1.experienceProgress;
/* 1169 */       setScore(debug1.getScore());
/*      */     } 
/*      */     
/* 1172 */     this.enchantmentSeed = debug1.enchantmentSeed;
/* 1173 */     this.enderChestInventory = debug1.enderChestInventory;
/* 1174 */     getEntityData().set(DATA_PLAYER_MODE_CUSTOMISATION, debug1.getEntityData().get(DATA_PLAYER_MODE_CUSTOMISATION));
/* 1175 */     this.lastSentExp = -1;
/* 1176 */     this.lastSentHealth = -1.0F;
/* 1177 */     this.lastSentFood = -1;
/* 1178 */     this.recipeBook.copyOverData((RecipeBook)debug1.recipeBook);
/* 1179 */     this.entitiesToRemove.addAll(debug1.entitiesToRemove);
/* 1180 */     this.seenCredits = debug1.seenCredits;
/* 1181 */     this.enteredNetherPosition = debug1.enteredNetherPosition;
/*      */     
/* 1183 */     setShoulderEntityLeft(debug1.getShoulderEntityLeft());
/* 1184 */     setShoulderEntityRight(debug1.getShoulderEntityRight());
/*      */   }
/*      */ 
/*      */   
/*      */   protected void onEffectAdded(MobEffectInstance debug1) {
/* 1189 */     super.onEffectAdded(debug1);
/* 1190 */     this.connection.send((Packet)new ClientboundUpdateMobEffectPacket(getId(), debug1));
/*      */     
/* 1192 */     if (debug1.getEffect() == MobEffects.LEVITATION) {
/* 1193 */       this.levitationStartTime = this.tickCount;
/* 1194 */       this.levitationStartPos = position();
/*      */     } 
/*      */     
/* 1197 */     CriteriaTriggers.EFFECTS_CHANGED.trigger(this);
/*      */   }
/*      */ 
/*      */   
/*      */   protected void onEffectUpdated(MobEffectInstance debug1, boolean debug2) {
/* 1202 */     super.onEffectUpdated(debug1, debug2);
/* 1203 */     this.connection.send((Packet)new ClientboundUpdateMobEffectPacket(getId(), debug1));
/*      */     
/* 1205 */     CriteriaTriggers.EFFECTS_CHANGED.trigger(this);
/*      */   }
/*      */ 
/*      */   
/*      */   protected void onEffectRemoved(MobEffectInstance debug1) {
/* 1210 */     super.onEffectRemoved(debug1);
/* 1211 */     this.connection.send((Packet)new ClientboundRemoveMobEffectPacket(getId(), debug1.getEffect()));
/*      */     
/* 1213 */     if (debug1.getEffect() == MobEffects.LEVITATION) {
/* 1214 */       this.levitationStartPos = null;
/*      */     }
/*      */     
/* 1217 */     CriteriaTriggers.EFFECTS_CHANGED.trigger(this);
/*      */   }
/*      */ 
/*      */   
/*      */   public void teleportTo(double debug1, double debug3, double debug5) {
/* 1222 */     this.connection.teleport(debug1, debug3, debug5, this.yRot, this.xRot);
/*      */   }
/*      */ 
/*      */   
/*      */   public void moveTo(double debug1, double debug3, double debug5) {
/* 1227 */     teleportTo(debug1, debug3, debug5);
/* 1228 */     this.connection.resetPosition();
/*      */   }
/*      */ 
/*      */   
/*      */   public void crit(Entity debug1) {
/* 1233 */     getLevel().getChunkSource().broadcastAndSend((Entity)this, (Packet<?>)new ClientboundAnimatePacket(debug1, 4));
/*      */   }
/*      */ 
/*      */   
/*      */   public void magicCrit(Entity debug1) {
/* 1238 */     getLevel().getChunkSource().broadcastAndSend((Entity)this, (Packet<?>)new ClientboundAnimatePacket(debug1, 5));
/*      */   }
/*      */ 
/*      */   
/*      */   public void onUpdateAbilities() {
/* 1243 */     if (this.connection == null) {
/*      */       return;
/*      */     }
/* 1246 */     this.connection.send((Packet)new ClientboundPlayerAbilitiesPacket(this.abilities));
/* 1247 */     updateInvisibilityStatus();
/*      */   }
/*      */   
/*      */   public ServerLevel getLevel() {
/* 1251 */     return (ServerLevel)this.level;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setGameMode(GameType debug1) {
/* 1256 */     this.gameMode.setGameModeForPlayer(debug1);
/* 1257 */     this.connection.send((Packet)new ClientboundGameEventPacket(ClientboundGameEventPacket.CHANGE_GAME_MODE, debug1.getId()));
/*      */     
/* 1259 */     if (debug1 == GameType.SPECTATOR) {
/* 1260 */       removeEntitiesOnShoulder();
/* 1261 */       stopRiding();
/*      */     } else {
/* 1263 */       setCamera((Entity)this);
/*      */     } 
/*      */     
/* 1266 */     onUpdateAbilities();
/* 1267 */     updateEffectVisibility();
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isSpectator() {
/* 1272 */     return (this.gameMode.getGameModeForPlayer() == GameType.SPECTATOR);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isCreative() {
/* 1277 */     return (this.gameMode.getGameModeForPlayer() == GameType.CREATIVE);
/*      */   }
/*      */ 
/*      */   
/*      */   public void sendMessage(Component debug1, UUID debug2) {
/* 1282 */     sendMessage(debug1, ChatType.SYSTEM, debug2);
/*      */   }
/*      */   
/*      */   public void sendMessage(Component debug1, ChatType debug2, UUID debug3) {
/* 1286 */     this.connection.send((Packet)new ClientboundChatPacket(debug1, debug2, debug3), debug4 -> {
/*      */           if (!debug4.isSuccess() && (debug1 == ChatType.GAME_INFO || debug1 == ChatType.SYSTEM)) {
/*      */             int debug5 = 256;
/*      */             String debug6 = debug2.getString(256);
/*      */             MutableComponent mutableComponent = (new TextComponent(debug6)).withStyle(ChatFormatting.YELLOW);
/*      */             this.connection.send((Packet)new ClientboundChatPacket((Component)(new TranslatableComponent("multiplayer.message_not_delivered", new Object[] { mutableComponent })).withStyle(ChatFormatting.RED), ChatType.SYSTEM, debug3));
/*      */           } 
/*      */         });
/*      */   }
/*      */ 
/*      */   
/*      */   public String getIpAddress() {
/* 1298 */     String debug1 = this.connection.connection.getRemoteAddress().toString();
/* 1299 */     debug1 = debug1.substring(debug1.indexOf("/") + 1);
/* 1300 */     debug1 = debug1.substring(0, debug1.indexOf(":"));
/* 1301 */     return debug1;
/*      */   }
/*      */ 
/*      */   
/*      */   public void updateOptions(ServerboundClientInformationPacket debug1) {
/* 1306 */     this.chatVisibility = debug1.getChatVisibility();
/* 1307 */     this.canChatColor = debug1.getChatColors();
/*      */     
/* 1309 */     getEntityData().set(DATA_PLAYER_MODE_CUSTOMISATION, Byte.valueOf((byte)debug1.getModelCustomisation()));
/* 1310 */     getEntityData().set(DATA_PLAYER_MAIN_HAND, Byte.valueOf((byte)((debug1.getMainHand() == HumanoidArm.LEFT) ? 0 : 1)));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ChatVisiblity getChatVisibility() {
/* 1318 */     return this.chatVisibility;
/*      */   }
/*      */   
/*      */   public void sendTexturePack(String debug1, String debug2) {
/* 1322 */     this.connection.send((Packet)new ClientboundResourcePackPacket(debug1, debug2));
/*      */   }
/*      */ 
/*      */   
/*      */   protected int getPermissionLevel() {
/* 1327 */     return this.server.getProfilePermissions(getGameProfile());
/*      */   }
/*      */   
/*      */   public void resetLastActionTime() {
/* 1331 */     this.lastActionTime = Util.getMillis();
/*      */   }
/*      */   
/*      */   public ServerStatsCounter getStats() {
/* 1335 */     return this.stats;
/*      */   }
/*      */   
/*      */   public ServerRecipeBook getRecipeBook() {
/* 1339 */     return this.recipeBook;
/*      */   }
/*      */   
/*      */   public void sendRemoveEntity(Entity debug1) {
/* 1343 */     if (debug1 instanceof Player) {
/* 1344 */       this.connection.send((Packet)new ClientboundRemoveEntitiesPacket(new int[] { debug1.getId() }));
/*      */     } else {
/* 1346 */       this.entitiesToRemove.add(Integer.valueOf(debug1.getId()));
/*      */     } 
/*      */   }
/*      */   
/*      */   public void cancelRemoveEntity(Entity debug1) {
/* 1351 */     this.entitiesToRemove.remove(Integer.valueOf(debug1.getId()));
/*      */   }
/*      */ 
/*      */   
/*      */   protected void updateInvisibilityStatus() {
/* 1356 */     if (isSpectator()) {
/* 1357 */       removeEffectParticles();
/* 1358 */       setInvisible(true);
/*      */     } else {
/* 1360 */       super.updateInvisibilityStatus();
/*      */     } 
/*      */   }
/*      */   
/*      */   public Entity getCamera() {
/* 1365 */     return (this.camera == null) ? (Entity)this : this.camera;
/*      */   }
/*      */   
/*      */   public void setCamera(Entity debug1) {
/* 1369 */     Entity debug2 = getCamera();
/* 1370 */     this.camera = (debug1 == null) ? (Entity)this : debug1;
/*      */     
/* 1372 */     if (debug2 != this.camera) {
/* 1373 */       this.connection.send((Packet)new ClientboundSetCameraPacket(this.camera));
/* 1374 */       teleportTo(this.camera.getX(), this.camera.getY(), this.camera.getZ());
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   protected void processPortalCooldown() {
/* 1380 */     if (!this.isChangingDimension) {
/* 1381 */       super.processPortalCooldown();
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void attack(Entity debug1) {
/* 1387 */     if (this.gameMode.getGameModeForPlayer() == GameType.SPECTATOR) {
/* 1388 */       setCamera(debug1);
/*      */     } else {
/* 1390 */       super.attack(debug1);
/*      */     } 
/*      */   }
/*      */   
/*      */   public long getLastActionTime() {
/* 1395 */     return this.lastActionTime;
/*      */   }
/*      */   
/*      */   @Nullable
/*      */   public Component getTabListDisplayName() {
/* 1400 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   public void swing(InteractionHand debug1) {
/* 1405 */     super.swing(debug1);
/* 1406 */     resetAttackStrengthTicker();
/*      */   }
/*      */   
/*      */   public boolean isChangingDimension() {
/* 1410 */     return this.isChangingDimension;
/*      */   }
/*      */   
/*      */   public void hasChangedDimension() {
/* 1414 */     this.isChangingDimension = false;
/*      */   }
/*      */   
/*      */   public PlayerAdvancements getAdvancements() {
/* 1418 */     return this.advancements;
/*      */   }
/*      */   
/*      */   public void teleportTo(ServerLevel debug1, double debug2, double debug4, double debug6, float debug8, float debug9) {
/* 1422 */     setCamera((Entity)this);
/* 1423 */     stopRiding();
/* 1424 */     if (debug1 == this.level) {
/* 1425 */       this.connection.teleport(debug2, debug4, debug6, debug8, debug9);
/*      */     } else {
/* 1427 */       ServerLevel debug10 = getLevel();
/*      */       
/* 1429 */       LevelData debug11 = debug1.getLevelData();
/* 1430 */       this.connection.send((Packet)new ClientboundRespawnPacket(debug1.dimensionType(), debug1.dimension(), BiomeManager.obfuscateSeed(debug1.getSeed()), this.gameMode.getGameModeForPlayer(), this.gameMode.getPreviousGameModeForPlayer(), debug1.isDebug(), debug1.isFlat(), true));
/* 1431 */       this.connection.send((Packet)new ClientboundChangeDifficultyPacket(debug11.getDifficulty(), debug11.isDifficultyLocked()));
/* 1432 */       this.server.getPlayerList().sendPlayerPermissionLevel(this);
/*      */       
/* 1434 */       debug10.removePlayerImmediately(this);
/*      */       
/* 1436 */       this.removed = false;
/*      */       
/* 1438 */       moveTo(debug2, debug4, debug6, debug8, debug9);
/*      */       
/* 1440 */       setLevel(debug1);
/* 1441 */       debug1.addDuringCommandTeleport(this);
/*      */       
/* 1443 */       triggerDimensionChangeTriggers(debug10);
/*      */       
/* 1445 */       this.connection.teleport(debug2, debug4, debug6, debug8, debug9);
/* 1446 */       this.gameMode.setLevel(debug1);
/* 1447 */       this.server.getPlayerList().sendLevelInfo(this, debug1);
/* 1448 */       this.server.getPlayerList().sendAllPlayerInfo(this);
/*      */     } 
/*      */   }
/*      */   
/*      */   @Nullable
/*      */   public BlockPos getRespawnPosition() {
/* 1454 */     return this.respawnPosition;
/*      */   }
/*      */   
/*      */   public float getRespawnAngle() {
/* 1458 */     return this.respawnAngle;
/*      */   }
/*      */   
/*      */   public ResourceKey<Level> getRespawnDimension() {
/* 1462 */     return this.respawnDimension;
/*      */   }
/*      */   
/*      */   public boolean isRespawnForced() {
/* 1466 */     return this.respawnForced;
/*      */   }
/*      */   
/*      */   public void setRespawnPosition(ResourceKey<Level> debug1, @Nullable BlockPos debug2, float debug3, boolean debug4, boolean debug5) {
/* 1470 */     if (debug2 != null) {
/* 1471 */       boolean debug6 = (debug2.equals(this.respawnPosition) && debug1.equals(this.respawnDimension));
/* 1472 */       if (debug5 && !debug6) {
/* 1473 */         sendMessage((Component)new TranslatableComponent("block.minecraft.set_spawn"), Util.NIL_UUID);
/*      */       }
/* 1475 */       this.respawnPosition = debug2;
/* 1476 */       this.respawnDimension = debug1;
/* 1477 */       this.respawnAngle = debug3;
/* 1478 */       this.respawnForced = debug4;
/*      */     } else {
/* 1480 */       this.respawnPosition = null;
/* 1481 */       this.respawnDimension = Level.OVERWORLD;
/* 1482 */       this.respawnAngle = 0.0F;
/* 1483 */       this.respawnForced = false;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void trackChunk(ChunkPos debug1, Packet<?> debug2, Packet<?> debug3) {
/* 1491 */     this.connection.send(debug3);
/* 1492 */     this.connection.send(debug2);
/*      */   }
/*      */ 
/*      */   
/*      */   public void untrackChunk(ChunkPos debug1) {
/* 1497 */     if (isAlive()) {
/* 1498 */       this.connection.send((Packet)new ClientboundForgetLevelChunkPacket(debug1.x, debug1.z));
/*      */     }
/*      */   }
/*      */   
/*      */   public SectionPos getLastSectionPos() {
/* 1503 */     return this.lastSectionPos;
/*      */   }
/*      */   
/*      */   public void setLastSectionPos(SectionPos debug1) {
/* 1507 */     this.lastSectionPos = debug1;
/*      */   }
/*      */ 
/*      */   
/*      */   public void playNotifySound(SoundEvent debug1, SoundSource debug2, float debug3, float debug4) {
/* 1512 */     this.connection.send((Packet)new ClientboundSoundPacket(debug1, debug2, getX(), getY(), getZ(), debug3, debug4));
/*      */   }
/*      */ 
/*      */   
/*      */   public Packet<?> getAddEntityPacket() {
/* 1517 */     return (Packet<?>)new ClientboundAddPlayerPacket(this);
/*      */   }
/*      */ 
/*      */   
/*      */   public ItemEntity drop(ItemStack debug1, boolean debug2, boolean debug3) {
/* 1522 */     ItemEntity debug4 = super.drop(debug1, debug2, debug3);
/* 1523 */     if (debug4 == null) {
/* 1524 */       return null;
/*      */     }
/*      */     
/* 1527 */     this.level.addFreshEntity((Entity)debug4);
/* 1528 */     ItemStack debug5 = debug4.getItem();
/* 1529 */     if (debug3) {
/* 1530 */       if (!debug5.isEmpty()) {
/* 1531 */         awardStat(Stats.ITEM_DROPPED.get(debug5.getItem()), debug1.getCount());
/*      */       }
/* 1533 */       awardStat(Stats.DROP);
/*      */     } 
/*      */     
/* 1536 */     return debug4;
/*      */   }
/*      */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\level\ServerPlayer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */