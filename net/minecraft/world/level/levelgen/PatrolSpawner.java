/*     */ package net.minecraft.world.level.levelgen;
/*     */ 
/*     */ import java.util.Random;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.MobSpawnType;
/*     */ import net.minecraft.world.entity.monster.PatrollingMonster;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.CustomSpawner;
/*     */ import net.minecraft.world.level.GameRules;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.NaturalSpawner;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ 
/*     */ public class PatrolSpawner
/*     */   implements CustomSpawner {
/*     */   public int tick(ServerLevel debug1, boolean debug2, boolean debug3) {
/*  24 */     if (!debug2) {
/*  25 */       return 0;
/*     */     }
/*     */     
/*  28 */     if (!debug1.getGameRules().getBoolean(GameRules.RULE_DO_PATROL_SPAWNING)) {
/*  29 */       return 0;
/*     */     }
/*     */     
/*  32 */     Random debug4 = debug1.random;
/*     */     
/*  34 */     this.nextTick--;
/*  35 */     if (this.nextTick > 0) {
/*  36 */       return 0;
/*     */     }
/*     */     
/*  39 */     this.nextTick += 12000 + debug4.nextInt(1200);
/*     */     
/*  41 */     long debug5 = debug1.getDayTime() / 24000L;
/*  42 */     if (debug5 < 5L || !debug1.isDay()) {
/*  43 */       return 0;
/*     */     }
/*     */     
/*  46 */     if (debug4.nextInt(5) != 0) {
/*  47 */       return 0;
/*     */     }
/*     */     
/*  50 */     int debug7 = debug1.players().size();
/*  51 */     if (debug7 < 1) {
/*  52 */       return 0;
/*     */     }
/*     */     
/*  55 */     Player debug8 = debug1.players().get(debug4.nextInt(debug7));
/*  56 */     if (debug8.isSpectator()) {
/*  57 */       return 0;
/*     */     }
/*     */     
/*  60 */     if (debug1.isCloseToVillage(debug8.blockPosition(), 2)) {
/*  61 */       return 0;
/*     */     }
/*     */     
/*  64 */     int debug9 = (24 + debug4.nextInt(24)) * (debug4.nextBoolean() ? -1 : 1);
/*  65 */     int debug10 = (24 + debug4.nextInt(24)) * (debug4.nextBoolean() ? -1 : 1);
/*  66 */     BlockPos.MutableBlockPos debug11 = debug8.blockPosition().mutable().move(debug9, 0, debug10);
/*     */     
/*  68 */     if (!debug1.hasChunksAt(debug11.getX() - 10, debug11.getY() - 10, debug11.getZ() - 10, debug11.getX() + 10, debug11.getY() + 10, debug11.getZ() + 10)) {
/*  69 */       return 0;
/*     */     }
/*     */     
/*  72 */     Biome debug12 = debug1.getBiome((BlockPos)debug11);
/*  73 */     Biome.BiomeCategory debug13 = debug12.getBiomeCategory();
/*  74 */     if (debug13 == Biome.BiomeCategory.MUSHROOM) {
/*  75 */       return 0;
/*     */     }
/*     */     
/*  78 */     int debug14 = 0;
/*     */     
/*  80 */     int debug15 = (int)Math.ceil(debug1.getCurrentDifficultyAt((BlockPos)debug11).getEffectiveDifficulty()) + 1;
/*  81 */     for (int debug16 = 0; debug16 < debug15; debug16++) {
/*  82 */       debug14++;
/*     */       
/*  84 */       debug11.setY(debug1.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, (BlockPos)debug11).getY());
/*     */       
/*  86 */       if (debug16 == 0) {
/*  87 */         if (!spawnPatrolMember(debug1, (BlockPos)debug11, debug4, true)) {
/*     */           break;
/*     */         }
/*     */       } else {
/*  91 */         spawnPatrolMember(debug1, (BlockPos)debug11, debug4, false);
/*     */       } 
/*     */       
/*  94 */       debug11.setX(debug11.getX() + debug4.nextInt(5) - debug4.nextInt(5));
/*  95 */       debug11.setZ(debug11.getZ() + debug4.nextInt(5) - debug4.nextInt(5));
/*     */     } 
/*     */     
/*  98 */     return debug14;
/*     */   }
/*     */   private int nextTick;
/*     */   private boolean spawnPatrolMember(ServerLevel debug1, BlockPos debug2, Random debug3, boolean debug4) {
/* 102 */     BlockState debug5 = debug1.getBlockState(debug2);
/* 103 */     if (!NaturalSpawner.isValidEmptySpawnBlock((BlockGetter)debug1, debug2, debug5, debug5.getFluidState(), EntityType.PILLAGER)) {
/* 104 */       return false;
/*     */     }
/*     */     
/* 107 */     if (!PatrollingMonster.checkPatrollingMonsterSpawnRules(EntityType.PILLAGER, (LevelAccessor)debug1, MobSpawnType.PATROL, debug2, debug3)) {
/* 108 */       return false;
/*     */     }
/*     */     
/* 111 */     PatrollingMonster debug6 = (PatrollingMonster)EntityType.PILLAGER.create((Level)debug1);
/* 112 */     if (debug6 != null) {
/* 113 */       if (debug4) {
/* 114 */         debug6.setPatrolLeader(true);
/* 115 */         debug6.findPatrolTarget();
/*     */       } 
/*     */       
/* 118 */       debug6.setPos(debug2.getX(), debug2.getY(), debug2.getZ());
/* 119 */       debug6.finalizeSpawn((ServerLevelAccessor)debug1, debug1.getCurrentDifficultyAt(debug2), MobSpawnType.PATROL, null, null);
/*     */       
/* 121 */       debug1.addFreshEntityWithPassengers((Entity)debug6);
/* 122 */       return true;
/*     */     } 
/*     */     
/* 125 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\PatrolSpawner.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */