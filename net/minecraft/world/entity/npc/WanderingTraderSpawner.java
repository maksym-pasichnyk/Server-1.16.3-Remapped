/*     */ package net.minecraft.world.entity.npc;
/*     */ 
/*     */ import java.util.Optional;
/*     */ import java.util.Random;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.MobSpawnType;
/*     */ import net.minecraft.world.entity.SpawnPlacements;
/*     */ import net.minecraft.world.entity.ai.village.poi.PoiManager;
/*     */ import net.minecraft.world.entity.ai.village.poi.PoiType;
/*     */ import net.minecraft.world.entity.animal.horse.TraderLlama;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.CustomSpawner;
/*     */ import net.minecraft.world.level.GameRules;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.NaturalSpawner;
/*     */ import net.minecraft.world.level.biome.Biomes;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ import net.minecraft.world.level.storage.ServerLevelData;
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
/*     */ public class WanderingTraderSpawner
/*     */   implements CustomSpawner
/*     */ {
/*  38 */   private final Random random = new Random();
/*     */   private final ServerLevelData serverLevelData;
/*     */   private int tickDelay;
/*     */   private int spawnDelay;
/*     */   private int spawnChance;
/*     */   
/*     */   public WanderingTraderSpawner(ServerLevelData debug1) {
/*  45 */     this.serverLevelData = debug1;
/*  46 */     this.tickDelay = 1200;
/*  47 */     this.spawnDelay = debug1.getWanderingTraderSpawnDelay();
/*  48 */     this.spawnChance = debug1.getWanderingTraderSpawnChance();
/*     */     
/*  50 */     if (this.spawnDelay == 0 && this.spawnChance == 0) {
/*  51 */       this.spawnDelay = 24000;
/*  52 */       debug1.setWanderingTraderSpawnDelay(this.spawnDelay);
/*  53 */       this.spawnChance = 25;
/*  54 */       debug1.setWanderingTraderSpawnChance(this.spawnChance);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int tick(ServerLevel debug1, boolean debug2, boolean debug3) {
/*  60 */     if (!debug1.getGameRules().getBoolean(GameRules.RULE_DO_TRADER_SPAWNING)) {
/*  61 */       return 0;
/*     */     }
/*     */     
/*  64 */     if (--this.tickDelay > 0) {
/*  65 */       return 0;
/*     */     }
/*  67 */     this.tickDelay = 1200;
/*     */     
/*  69 */     this.spawnDelay -= 1200;
/*  70 */     this.serverLevelData.setWanderingTraderSpawnDelay(this.spawnDelay);
/*  71 */     if (this.spawnDelay > 0) {
/*  72 */       return 0;
/*     */     }
/*  74 */     this.spawnDelay = 24000;
/*     */     
/*  76 */     if (!debug1.getGameRules().getBoolean(GameRules.RULE_DOMOBSPAWNING)) {
/*  77 */       return 0;
/*     */     }
/*     */     
/*  80 */     int debug4 = this.spawnChance;
/*  81 */     this.spawnChance = Mth.clamp(this.spawnChance + 25, 25, 75);
/*  82 */     this.serverLevelData.setWanderingTraderSpawnChance(this.spawnChance);
/*     */     
/*  84 */     if (this.random.nextInt(100) > debug4) {
/*  85 */       return 0;
/*     */     }
/*     */     
/*  88 */     if (spawn(debug1)) {
/*  89 */       this.spawnChance = 25;
/*  90 */       return 1;
/*     */     } 
/*     */     
/*  93 */     return 0;
/*     */   }
/*     */   
/*     */   private boolean spawn(ServerLevel debug1) {
/*  97 */     ServerPlayer serverPlayer = debug1.getRandomPlayer();
/*  98 */     if (serverPlayer == null) {
/*  99 */       return true;
/*     */     }
/*     */     
/* 102 */     if (this.random.nextInt(10) != 0) {
/* 103 */       return false;
/*     */     }
/*     */     
/* 106 */     BlockPos debug3 = serverPlayer.blockPosition();
/* 107 */     int debug4 = 48;
/*     */     
/* 109 */     PoiManager debug5 = debug1.getPoiManager();
/* 110 */     Optional<BlockPos> debug6 = debug5.find(PoiType.MEETING.getPredicate(), debug0 -> true, debug3, 48, PoiManager.Occupancy.ANY);
/*     */     
/* 112 */     BlockPos debug7 = debug6.orElse(debug3);
/* 113 */     BlockPos debug8 = findSpawnPositionNear((LevelReader)debug1, debug7, 48);
/*     */     
/* 115 */     if (debug8 != null && hasEnoughSpace((BlockGetter)debug1, debug8)) {
/* 116 */       if (debug1.getBiomeName(debug8).equals(Optional.of(Biomes.THE_VOID))) {
/* 117 */         return false;
/*     */       }
/*     */       
/* 120 */       WanderingTrader debug9 = (WanderingTrader)EntityType.WANDERING_TRADER.spawn(debug1, null, null, null, debug8, MobSpawnType.EVENT, false, false);
/*     */       
/* 122 */       if (debug9 != null) {
/* 123 */         for (int debug10 = 0; debug10 < 2; debug10++) {
/* 124 */           tryToSpawnLlamaFor(debug1, debug9, 4);
/*     */         }
/* 126 */         this.serverLevelData.setWanderingTraderId(debug9.getUUID());
/* 127 */         debug9.setDespawnDelay(48000);
/*     */         
/* 129 */         debug9.setWanderTarget(debug7);
/* 130 */         debug9.restrictTo(debug7, 16);
/* 131 */         return true;
/*     */       } 
/*     */     } 
/* 134 */     return false;
/*     */   }
/*     */   
/*     */   private void tryToSpawnLlamaFor(ServerLevel debug1, WanderingTrader debug2, int debug3) {
/* 138 */     BlockPos debug4 = findSpawnPositionNear((LevelReader)debug1, debug2.blockPosition(), debug3);
/* 139 */     if (debug4 == null) {
/*     */       return;
/*     */     }
/*     */     
/* 143 */     TraderLlama debug5 = (TraderLlama)EntityType.TRADER_LLAMA.spawn(debug1, null, null, null, debug4, MobSpawnType.EVENT, false, false);
/* 144 */     if (debug5 == null) {
/*     */       return;
/*     */     }
/*     */     
/* 148 */     debug5.setLeashedTo((Entity)debug2, true);
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private BlockPos findSpawnPositionNear(LevelReader debug1, BlockPos debug2, int debug3) {
/* 153 */     BlockPos debug4 = null;
/*     */     
/* 155 */     for (int debug5 = 0; debug5 < 10; debug5++) {
/* 156 */       int debug6 = debug2.getX() + this.random.nextInt(debug3 * 2) - debug3;
/* 157 */       int debug7 = debug2.getZ() + this.random.nextInt(debug3 * 2) - debug3;
/* 158 */       int debug8 = debug1.getHeight(Heightmap.Types.WORLD_SURFACE, debug6, debug7);
/* 159 */       BlockPos debug9 = new BlockPos(debug6, debug8, debug7);
/*     */       
/* 161 */       if (NaturalSpawner.isSpawnPositionOk(SpawnPlacements.Type.ON_GROUND, debug1, debug9, EntityType.WANDERING_TRADER)) {
/* 162 */         debug4 = debug9;
/*     */         break;
/*     */       } 
/*     */     } 
/* 166 */     return debug4;
/*     */   }
/*     */   
/*     */   private boolean hasEnoughSpace(BlockGetter debug1, BlockPos debug2) {
/* 170 */     for (BlockPos debug4 : BlockPos.betweenClosed(debug2, debug2.offset(1, 2, 1))) {
/* 171 */       if (!debug1.getBlockState(debug4).getCollisionShape(debug1, debug4).isEmpty()) {
/* 172 */         return false;
/*     */       }
/*     */     } 
/* 175 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\npc\WanderingTraderSpawner.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */