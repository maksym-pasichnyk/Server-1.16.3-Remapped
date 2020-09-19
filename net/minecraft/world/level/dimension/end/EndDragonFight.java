/*     */ package net.minecraft.world.level.dimension.end;
/*     */ 
/*     */ import com.google.common.collect.ContiguousSet;
/*     */ import com.google.common.collect.DiscreteDomain;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Range;
/*     */ import com.google.common.collect.Sets;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import java.util.Set;
/*     */ import java.util.UUID;
/*     */ import java.util.function.Predicate;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.advancements.CriteriaTriggers;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.data.worldgen.Features;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.IntTag;
/*     */ import net.minecraft.nbt.ListTag;
/*     */ import net.minecraft.nbt.NbtUtils;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.TranslatableComponent;
/*     */ import net.minecraft.server.level.ChunkHolder;
/*     */ import net.minecraft.server.level.ServerBossEvent;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.server.level.TicketType;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.Unit;
/*     */ import net.minecraft.world.BossEvent;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntitySelector;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
/*     */ import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
/*     */ import net.minecraft.world.entity.boss.enderdragon.phases.EnderDragonPhase;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.state.pattern.BlockInWorld;
/*     */ import net.minecraft.world.level.block.state.pattern.BlockPattern;
/*     */ import net.minecraft.world.level.block.state.pattern.BlockPatternBuilder;
/*     */ import net.minecraft.world.level.block.state.predicate.BlockPredicate;
/*     */ import net.minecraft.world.level.chunk.ChunkAccess;
/*     */ import net.minecraft.world.level.chunk.ChunkStatus;
/*     */ import net.minecraft.world.level.chunk.LevelChunk;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ import net.minecraft.world.level.levelgen.feature.EndPodiumFeature;
/*     */ import net.minecraft.world.level.levelgen.feature.SpikeFeature;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public class EndDragonFight {
/*  65 */   private static final Logger LOGGER = LogManager.getLogger();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  74 */   private static final Predicate<Entity> VALID_PLAYER = EntitySelector.ENTITY_STILL_ALIVE.and(EntitySelector.withinDistance(0.0D, 128.0D, 0.0D, 192.0D));
/*     */   
/*  76 */   private final ServerBossEvent dragonEvent = (ServerBossEvent)(new ServerBossEvent((Component)new TranslatableComponent("entity.minecraft.ender_dragon"), BossEvent.BossBarColor.PINK, BossEvent.BossBarOverlay.PROGRESS)).setPlayBossMusic(true).setCreateWorldFog(true);
/*     */   private final ServerLevel level;
/*  78 */   private final List<Integer> gateways = Lists.newArrayList();
/*     */   private final BlockPattern exitPortalPattern;
/*     */   private int ticksSinceDragonSeen;
/*     */   private int crystalsAlive;
/*     */   private int ticksSinceCrystalsScanned;
/*     */   private int ticksSinceLastPlayerScan;
/*     */   private boolean dragonKilled;
/*     */   private boolean previouslyKilled;
/*     */   private UUID dragonUUID;
/*     */   private boolean needsStateScanning = true;
/*     */   private BlockPos portalLocation;
/*     */   private DragonRespawnAnimation respawnStage;
/*     */   private int respawnTime;
/*     */   private List<EndCrystal> respawnCrystals;
/*     */   
/*     */   public EndDragonFight(ServerLevel debug1, long debug2, CompoundTag debug4) {
/*  94 */     this.level = debug1;
/*  95 */     if (debug4.contains("DragonKilled", 99)) {
/*  96 */       if (debug4.hasUUID("Dragon")) {
/*  97 */         this.dragonUUID = debug4.getUUID("Dragon");
/*     */       }
/*  99 */       this.dragonKilled = debug4.getBoolean("DragonKilled");
/* 100 */       this.previouslyKilled = debug4.getBoolean("PreviouslyKilled");
/* 101 */       if (debug4.getBoolean("IsRespawning")) {
/* 102 */         this.respawnStage = DragonRespawnAnimation.START;
/*     */       }
/*     */       
/* 105 */       if (debug4.contains("ExitPortalLocation", 10)) {
/* 106 */         this.portalLocation = NbtUtils.readBlockPos(debug4.getCompound("ExitPortalLocation"));
/*     */       }
/*     */     } else {
/* 109 */       this.dragonKilled = true;
/* 110 */       this.previouslyKilled = true;
/*     */     } 
/*     */     
/* 113 */     if (debug4.contains("Gateways", 9)) {
/* 114 */       ListTag debug5 = debug4.getList("Gateways", 3);
/* 115 */       for (int debug6 = 0; debug6 < debug5.size(); debug6++) {
/* 116 */         this.gateways.add(Integer.valueOf(debug5.getInt(debug6)));
/*     */       }
/*     */     } else {
/* 119 */       this.gateways.addAll((Collection<? extends Integer>)ContiguousSet.create(Range.closedOpen(Integer.valueOf(0), Integer.valueOf(20)), DiscreteDomain.integers()));
/* 120 */       Collections.shuffle(this.gateways, new Random(debug2));
/*     */     } 
/*     */     
/* 123 */     this
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
/* 170 */       .exitPortalPattern = BlockPatternBuilder.start().aisle(new String[] { "       ", "       ", "       ", "   #   ", "       ", "       ", "       " }).aisle(new String[] { "       ", "       ", "       ", "   #   ", "       ", "       ", "       " }).aisle(new String[] { "       ", "       ", "       ", "   #   ", "       ", "       ", "       " }).aisle(new String[] { "  ###  ", " #   # ", "#     #", "#  #  #", "#     #", " #   # ", "  ###  " }).aisle(new String[] { "       ", "  ###  ", " ##### ", " ##### ", " ##### ", "  ###  ", "       " }).where('#', BlockInWorld.hasState((Predicate)BlockPredicate.forBlock(Blocks.BEDROCK))).build();
/*     */   }
/*     */   
/*     */   public CompoundTag saveData() {
/* 174 */     CompoundTag debug1 = new CompoundTag();
/*     */     
/* 176 */     if (this.dragonUUID != null) {
/* 177 */       debug1.putUUID("Dragon", this.dragonUUID);
/*     */     }
/*     */     
/* 180 */     debug1.putBoolean("DragonKilled", this.dragonKilled);
/* 181 */     debug1.putBoolean("PreviouslyKilled", this.previouslyKilled);
/*     */     
/* 183 */     if (this.portalLocation != null) {
/* 184 */       debug1.put("ExitPortalLocation", (Tag)NbtUtils.writeBlockPos(this.portalLocation));
/*     */     }
/*     */     
/* 187 */     ListTag debug2 = new ListTag();
/* 188 */     for (Iterator<Integer> iterator = this.gateways.iterator(); iterator.hasNext(); ) { int debug4 = ((Integer)iterator.next()).intValue();
/* 189 */       debug2.add(IntTag.valueOf(debug4)); }
/*     */     
/* 191 */     debug1.put("Gateways", (Tag)debug2);
/*     */     
/* 193 */     return debug1;
/*     */   }
/*     */   
/*     */   public void tick() {
/* 197 */     this.dragonEvent.setVisible(!this.dragonKilled);
/*     */     
/* 199 */     if (++this.ticksSinceLastPlayerScan >= 20) {
/* 200 */       updatePlayers();
/* 201 */       this.ticksSinceLastPlayerScan = 0;
/*     */     } 
/*     */     
/* 204 */     if (!this.dragonEvent.getPlayers().isEmpty()) {
/* 205 */       this.level.getChunkSource().addRegionTicket(TicketType.DRAGON, new ChunkPos(0, 0), 9, Unit.INSTANCE);
/*     */       
/* 207 */       boolean debug1 = isArenaLoaded();
/*     */       
/* 209 */       if (this.needsStateScanning && debug1) {
/* 210 */         scanState();
/* 211 */         this.needsStateScanning = false;
/*     */       } 
/*     */       
/* 214 */       if (this.respawnStage != null) {
/* 215 */         if (this.respawnCrystals == null && debug1) {
/* 216 */           this.respawnStage = null;
/* 217 */           tryRespawn();
/*     */         } 
/* 219 */         this.respawnStage.tick(this.level, this, this.respawnCrystals, this.respawnTime++, this.portalLocation);
/*     */       } 
/*     */       
/* 222 */       if (!this.dragonKilled) {
/* 223 */         if ((this.dragonUUID == null || ++this.ticksSinceDragonSeen >= 1200) && debug1) {
/* 224 */           findOrCreateDragon();
/* 225 */           this.ticksSinceDragonSeen = 0;
/*     */         } 
/*     */         
/* 228 */         if (++this.ticksSinceCrystalsScanned >= 100 && debug1) {
/* 229 */           updateCrystalCount();
/* 230 */           this.ticksSinceCrystalsScanned = 0;
/*     */         } 
/*     */       } 
/*     */     } else {
/* 234 */       this.level.getChunkSource().removeRegionTicket(TicketType.DRAGON, new ChunkPos(0, 0), 9, Unit.INSTANCE);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void scanState() {
/* 239 */     LOGGER.info("Scanning for legacy world dragon fight...");
/* 240 */     boolean debug1 = hasActiveExitPortal();
/* 241 */     if (debug1) {
/* 242 */       LOGGER.info("Found that the dragon has been killed in this world already.");
/* 243 */       this.previouslyKilled = true;
/*     */     } else {
/* 245 */       LOGGER.info("Found that the dragon has not yet been killed in this world.");
/* 246 */       this.previouslyKilled = false;
/* 247 */       if (findExitPortal() == null) {
/* 248 */         spawnExitPortal(false);
/*     */       }
/*     */     } 
/*     */     
/* 252 */     List<EnderDragon> debug2 = this.level.getDragons();
/* 253 */     if (debug2.isEmpty()) {
/* 254 */       this.dragonKilled = true;
/*     */     } else {
/* 256 */       EnderDragon debug3 = debug2.get(0);
/* 257 */       this.dragonUUID = debug3.getUUID();
/* 258 */       LOGGER.info("Found that there's a dragon still alive ({})", debug3);
/* 259 */       this.dragonKilled = false;
/*     */       
/* 261 */       if (!debug1) {
/* 262 */         LOGGER.info("But we didn't have a portal, let's remove it.");
/* 263 */         debug3.remove();
/* 264 */         this.dragonUUID = null;
/*     */       } 
/*     */     } 
/*     */     
/* 268 */     if (!this.previouslyKilled && this.dragonKilled)
/*     */     {
/* 270 */       this.dragonKilled = false;
/*     */     }
/*     */   }
/*     */   
/*     */   private void findOrCreateDragon() {
/* 275 */     List<EnderDragon> debug1 = this.level.getDragons();
/* 276 */     if (debug1.isEmpty()) {
/* 277 */       LOGGER.debug("Haven't seen the dragon, respawning it");
/* 278 */       createNewDragon();
/*     */     } else {
/* 280 */       LOGGER.debug("Haven't seen our dragon, but found another one to use.");
/* 281 */       this.dragonUUID = ((EnderDragon)debug1.get(0)).getUUID();
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void setRespawnStage(DragonRespawnAnimation debug1) {
/* 286 */     if (this.respawnStage == null) {
/* 287 */       throw new IllegalStateException("Dragon respawn isn't in progress, can't skip ahead in the animation.");
/*     */     }
/*     */     
/* 290 */     this.respawnTime = 0;
/* 291 */     if (debug1 == DragonRespawnAnimation.END) {
/* 292 */       this.respawnStage = null;
/* 293 */       this.dragonKilled = false;
/* 294 */       EnderDragon debug2 = createNewDragon();
/*     */       
/* 296 */       for (ServerPlayer debug4 : this.dragonEvent.getPlayers()) {
/* 297 */         CriteriaTriggers.SUMMONED_ENTITY.trigger(debug4, (Entity)debug2);
/*     */       }
/*     */     } else {
/* 300 */       this.respawnStage = debug1;
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean hasActiveExitPortal() {
/* 305 */     for (int debug1 = -8; debug1 <= 8; debug1++) {
/* 306 */       for (int debug2 = -8; debug2 <= 8; debug2++) {
/* 307 */         LevelChunk debug3 = this.level.getChunk(debug1, debug2);
/* 308 */         for (BlockEntity debug5 : debug3.getBlockEntities().values()) {
/* 309 */           if (debug5 instanceof net.minecraft.world.level.block.entity.TheEndPortalBlockEntity) {
/* 310 */             return true;
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 316 */     return false;
/*     */   }
/*     */   @Nullable
/*     */   private BlockPattern.BlockPatternMatch findExitPortal() {
/*     */     int debug1;
/* 321 */     for (debug1 = -8; debug1 <= 8; debug1++) {
/* 322 */       for (int i = -8; i <= 8; i++) {
/* 323 */         LevelChunk debug3 = this.level.getChunk(debug1, i);
/* 324 */         for (BlockEntity debug5 : debug3.getBlockEntities().values()) {
/* 325 */           if (debug5 instanceof net.minecraft.world.level.block.entity.TheEndPortalBlockEntity) {
/* 326 */             BlockPattern.BlockPatternMatch debug6 = this.exitPortalPattern.find((LevelReader)this.level, debug5.getBlockPos());
/* 327 */             if (debug6 != null) {
/* 328 */               BlockPos debug7 = debug6.getBlock(3, 3, 3).getPos();
/* 329 */               if (this.portalLocation == null && debug7.getX() == 0 && debug7.getZ() == 0) {
/* 330 */                 this.portalLocation = debug7;
/*     */               }
/* 332 */               return debug6;
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 339 */     debug1 = this.level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, EndPodiumFeature.END_PODIUM_LOCATION).getY();
/*     */     
/* 341 */     for (int debug2 = debug1; debug2 >= 0; debug2--) {
/* 342 */       BlockPattern.BlockPatternMatch debug3 = this.exitPortalPattern.find((LevelReader)this.level, new BlockPos(EndPodiumFeature.END_PODIUM_LOCATION.getX(), debug2, EndPodiumFeature.END_PODIUM_LOCATION.getZ()));
/* 343 */       if (debug3 != null) {
/* 344 */         if (this.portalLocation == null) {
/* 345 */           this.portalLocation = debug3.getBlock(3, 3, 3).getPos();
/*     */         }
/* 347 */         return debug3;
/*     */       } 
/*     */     } 
/*     */     
/* 351 */     return null;
/*     */   }
/*     */   
/*     */   private boolean isArenaLoaded() {
/* 355 */     for (int debug1 = -8; debug1 <= 8; debug1++) {
/* 356 */       for (int debug2 = 8; debug2 <= 8; debug2++) {
/* 357 */         ChunkAccess debug3 = this.level.getChunk(debug1, debug2, ChunkStatus.FULL, false);
/* 358 */         if (!(debug3 instanceof LevelChunk)) {
/* 359 */           return false;
/*     */         }
/* 361 */         ChunkHolder.FullChunkStatus debug4 = ((LevelChunk)debug3).getFullStatus();
/* 362 */         if (!debug4.isOrAfter(ChunkHolder.FullChunkStatus.TICKING)) {
/* 363 */           return false;
/*     */         }
/*     */       } 
/*     */     } 
/* 367 */     return true;
/*     */   }
/*     */   
/*     */   private void updatePlayers() {
/* 371 */     Set<ServerPlayer> debug1 = Sets.newHashSet();
/* 372 */     for (ServerPlayer debug3 : this.level.getPlayers(VALID_PLAYER)) {
/* 373 */       this.dragonEvent.addPlayer(debug3);
/* 374 */       debug1.add(debug3);
/*     */     } 
/* 376 */     Set<ServerPlayer> debug2 = Sets.newHashSet(this.dragonEvent.getPlayers());
/* 377 */     debug2.removeAll(debug1);
/* 378 */     for (ServerPlayer debug4 : debug2) {
/* 379 */       this.dragonEvent.removePlayer(debug4);
/*     */     }
/*     */   }
/*     */   
/*     */   private void updateCrystalCount() {
/* 384 */     this.ticksSinceCrystalsScanned = 0;
/* 385 */     this.crystalsAlive = 0;
/*     */     
/* 387 */     for (SpikeFeature.EndSpike debug2 : SpikeFeature.getSpikesForLevel((WorldGenLevel)this.level)) {
/* 388 */       this.crystalsAlive += this.level.getEntitiesOfClass(EndCrystal.class, debug2.getTopBoundingBox()).size();
/*     */     }
/*     */     
/* 391 */     LOGGER.debug("Found {} end crystals still alive", Integer.valueOf(this.crystalsAlive));
/*     */   }
/*     */   
/*     */   public void setDragonKilled(EnderDragon debug1) {
/* 395 */     if (debug1.getUUID().equals(this.dragonUUID)) {
/* 396 */       this.dragonEvent.setPercent(0.0F);
/* 397 */       this.dragonEvent.setVisible(false);
/* 398 */       spawnExitPortal(true);
/* 399 */       spawnNewGateway();
/*     */       
/* 401 */       if (!this.previouslyKilled) {
/* 402 */         this.level.setBlockAndUpdate(this.level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, EndPodiumFeature.END_PODIUM_LOCATION), Blocks.DRAGON_EGG.defaultBlockState());
/*     */       }
/*     */       
/* 405 */       this.previouslyKilled = true;
/* 406 */       this.dragonKilled = true;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void spawnNewGateway() {
/* 411 */     if (this.gateways.isEmpty()) {
/*     */       return;
/*     */     }
/*     */     
/* 415 */     int debug1 = ((Integer)this.gateways.remove(this.gateways.size() - 1)).intValue();
/* 416 */     int debug2 = Mth.floor(96.0D * Math.cos(2.0D * (-3.141592653589793D + 0.15707963267948966D * debug1)));
/* 417 */     int debug3 = Mth.floor(96.0D * Math.sin(2.0D * (-3.141592653589793D + 0.15707963267948966D * debug1)));
/* 418 */     spawnNewGateway(new BlockPos(debug2, 75, debug3));
/*     */   }
/*     */   
/*     */   private void spawnNewGateway(BlockPos debug1) {
/* 422 */     this.level.levelEvent(3000, debug1, 0);
/* 423 */     Features.END_GATEWAY_DELAYED.place((WorldGenLevel)this.level, this.level.getChunkSource().getGenerator(), new Random(), debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   private void spawnExitPortal(boolean debug1) {
/* 428 */     EndPodiumFeature debug2 = new EndPodiumFeature(debug1);
/*     */     
/* 430 */     if (this.portalLocation == null) {
/* 431 */       this.portalLocation = this.level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, EndPodiumFeature.END_PODIUM_LOCATION).below();
/* 432 */       while (this.level.getBlockState(this.portalLocation).is(Blocks.BEDROCK) && this.portalLocation.getY() > this.level.getSeaLevel()) {
/* 433 */         this.portalLocation = this.portalLocation.below();
/*     */       }
/*     */     } 
/*     */     
/* 437 */     debug2.configured((FeatureConfiguration)FeatureConfiguration.NONE).place((WorldGenLevel)this.level, this.level.getChunkSource().getGenerator(), new Random(), this.portalLocation);
/*     */   }
/*     */   
/*     */   private EnderDragon createNewDragon() {
/* 441 */     this.level.getChunkAt(new BlockPos(0, 128, 0));
/* 442 */     EnderDragon debug1 = (EnderDragon)EntityType.ENDER_DRAGON.create((Level)this.level);
/* 443 */     debug1.getPhaseManager().setPhase(EnderDragonPhase.HOLDING_PATTERN);
/* 444 */     debug1.moveTo(0.0D, 128.0D, 0.0D, this.level.random.nextFloat() * 360.0F, 0.0F);
/* 445 */     this.level.addFreshEntity((Entity)debug1);
/* 446 */     this.dragonUUID = debug1.getUUID();
/* 447 */     return debug1;
/*     */   }
/*     */   
/*     */   public void updateDragon(EnderDragon debug1) {
/* 451 */     if (debug1.getUUID().equals(this.dragonUUID)) {
/* 452 */       this.dragonEvent.setPercent(debug1.getHealth() / debug1.getMaxHealth());
/* 453 */       this.ticksSinceDragonSeen = 0;
/* 454 */       if (debug1.hasCustomName()) {
/* 455 */         this.dragonEvent.setName(debug1.getDisplayName());
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public int getCrystalsAlive() {
/* 461 */     return this.crystalsAlive;
/*     */   }
/*     */   
/*     */   public void onCrystalDestroyed(EndCrystal debug1, DamageSource debug2) {
/* 465 */     if (this.respawnStage != null && this.respawnCrystals.contains(debug1)) {
/* 466 */       LOGGER.debug("Aborting respawn sequence");
/* 467 */       this.respawnStage = null;
/* 468 */       this.respawnTime = 0;
/* 469 */       resetSpikeCrystals();
/* 470 */       spawnExitPortal(true);
/*     */     } else {
/* 472 */       updateCrystalCount();
/* 473 */       Entity debug3 = this.level.getEntity(this.dragonUUID);
/* 474 */       if (debug3 instanceof EnderDragon) {
/* 475 */         ((EnderDragon)debug3).onCrystalDestroyed(debug1, debug1.blockPosition(), debug2);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean hasPreviouslyKilledDragon() {
/* 481 */     return this.previouslyKilled;
/*     */   }
/*     */   
/*     */   public void tryRespawn() {
/* 485 */     if (this.dragonKilled && this.respawnStage == null) {
/* 486 */       BlockPos debug1 = this.portalLocation;
/* 487 */       if (debug1 == null) {
/* 488 */         LOGGER.debug("Tried to respawn, but need to find the portal first.");
/* 489 */         BlockPattern.BlockPatternMatch blockPatternMatch = findExitPortal();
/* 490 */         if (blockPatternMatch == null) {
/* 491 */           LOGGER.debug("Couldn't find a portal, so we made one.");
/* 492 */           spawnExitPortal(true);
/*     */         } else {
/* 494 */           LOGGER.debug("Found the exit portal & temporarily using it.");
/*     */         } 
/* 496 */         debug1 = this.portalLocation;
/*     */       } 
/*     */       
/* 499 */       List<EndCrystal> debug2 = Lists.newArrayList();
/* 500 */       BlockPos debug3 = debug1.above(1);
/* 501 */       for (Direction debug5 : Direction.Plane.HORIZONTAL) {
/* 502 */         List<EndCrystal> debug6 = this.level.getEntitiesOfClass(EndCrystal.class, new AABB(debug3.relative(debug5, 2)));
/* 503 */         if (debug6.isEmpty()) {
/*     */           return;
/*     */         }
/* 506 */         debug2.addAll(debug6);
/*     */       } 
/*     */       
/* 509 */       LOGGER.debug("Found all crystals, respawning dragon.");
/* 510 */       respawnDragon(debug2);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void respawnDragon(List<EndCrystal> debug1) {
/* 515 */     if (this.dragonKilled && this.respawnStage == null) {
/* 516 */       BlockPattern.BlockPatternMatch debug2 = findExitPortal();
/* 517 */       while (debug2 != null) {
/* 518 */         for (int debug3 = 0; debug3 < this.exitPortalPattern.getWidth(); debug3++) {
/* 519 */           for (int debug4 = 0; debug4 < this.exitPortalPattern.getHeight(); debug4++) {
/* 520 */             for (int debug5 = 0; debug5 < this.exitPortalPattern.getDepth(); debug5++) {
/* 521 */               BlockInWorld debug6 = debug2.getBlock(debug3, debug4, debug5);
/* 522 */               if (debug6.getState().is(Blocks.BEDROCK) || debug6.getState().is(Blocks.END_PORTAL)) {
/* 523 */                 this.level.setBlockAndUpdate(debug6.getPos(), Blocks.END_STONE.defaultBlockState());
/*     */               }
/*     */             } 
/*     */           } 
/*     */         } 
/* 528 */         debug2 = findExitPortal();
/*     */       } 
/*     */       
/* 531 */       this.respawnStage = DragonRespawnAnimation.START;
/* 532 */       this.respawnTime = 0;
/* 533 */       spawnExitPortal(false);
/* 534 */       this.respawnCrystals = debug1;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void resetSpikeCrystals() {
/* 539 */     for (SpikeFeature.EndSpike debug2 : SpikeFeature.getSpikesForLevel((WorldGenLevel)this.level)) {
/* 540 */       List<EndCrystal> debug3 = this.level.getEntitiesOfClass(EndCrystal.class, debug2.getTopBoundingBox());
/* 541 */       for (EndCrystal debug5 : debug3) {
/* 542 */         debug5.setInvulnerable(false);
/* 543 */         debug5.setBeamTarget(null);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\dimension\end\EndDragonFight.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */