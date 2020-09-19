/*    */ package net.minecraft.world.level.levelgen;
/*    */ 
/*    */ import java.util.Random;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.stats.ServerStatsCounter;
/*    */ import net.minecraft.stats.Stats;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.DifficultyInstance;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.MobSpawnType;
/*    */ import net.minecraft.world.entity.SpawnGroupData;
/*    */ import net.minecraft.world.entity.monster.Phantom;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.CustomSpawner;
/*    */ import net.minecraft.world.level.GameRules;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.NaturalSpawner;
/*    */ import net.minecraft.world.level.ServerLevelAccessor;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.material.FluidState;
/*    */ 
/*    */ public class PhantomSpawner implements CustomSpawner {
/*    */   private int nextTick;
/*    */   
/*    */   public int tick(ServerLevel debug1, boolean debug2, boolean debug3) {
/* 30 */     if (!debug2) {
/* 31 */       return 0;
/*    */     }
/*    */     
/* 34 */     if (!debug1.getGameRules().getBoolean(GameRules.RULE_DOINSOMNIA)) {
/* 35 */       return 0;
/*    */     }
/*    */     
/* 38 */     Random debug4 = debug1.random;
/*    */     
/* 40 */     this.nextTick--;
/* 41 */     if (this.nextTick > 0) {
/* 42 */       return 0;
/*    */     }
/* 44 */     this.nextTick += (60 + debug4.nextInt(60)) * 20;
/*    */     
/* 46 */     if (debug1.getSkyDarken() < 5 && debug1.dimensionType().hasSkyLight()) {
/* 47 */       return 0;
/*    */     }
/*    */     
/* 50 */     int debug5 = 0;
/* 51 */     for (Player debug7 : debug1.players()) {
/* 52 */       if (debug7.isSpectator()) {
/*    */         continue;
/*    */       }
/* 55 */       BlockPos debug8 = debug7.blockPosition();
/* 56 */       if (debug1.dimensionType().hasSkyLight() && (debug8.getY() < debug1.getSeaLevel() || !debug1.canSeeSky(debug8))) {
/*    */         continue;
/*    */       }
/* 59 */       DifficultyInstance debug9 = debug1.getCurrentDifficultyAt(debug8);
/* 60 */       if (!debug9.isHarderThan(debug4.nextFloat() * 3.0F)) {
/*    */         continue;
/*    */       }
/*    */       
/* 64 */       ServerStatsCounter debug10 = ((ServerPlayer)debug7).getStats();
/* 65 */       int debug11 = Mth.clamp(debug10.getValue(Stats.CUSTOM.get(Stats.TIME_SINCE_REST)), 1, 2147483647);
/* 66 */       int debug12 = 24000;
/* 67 */       if (debug4.nextInt(debug11) < 72000) {
/*    */         continue;
/*    */       }
/*    */       
/* 71 */       BlockPos debug13 = debug8.above(20 + debug4.nextInt(15)).east(-10 + debug4.nextInt(21)).south(-10 + debug4.nextInt(21));
/* 72 */       BlockState debug14 = debug1.getBlockState(debug13);
/* 73 */       FluidState debug15 = debug1.getFluidState(debug13);
/* 74 */       if (!NaturalSpawner.isValidEmptySpawnBlock((BlockGetter)debug1, debug13, debug14, debug15, EntityType.PHANTOM)) {
/*    */         continue;
/*    */       }
/*    */       
/* 78 */       SpawnGroupData debug16 = null;
/* 79 */       int debug17 = 1 + debug4.nextInt(debug9.getDifficulty().getId() + 1);
/* 80 */       for (int debug18 = 0; debug18 < debug17; debug18++) {
/* 81 */         Phantom debug19 = (Phantom)EntityType.PHANTOM.create((Level)debug1);
/* 82 */         debug19.moveTo(debug13, 0.0F, 0.0F);
/* 83 */         debug16 = debug19.finalizeSpawn((ServerLevelAccessor)debug1, debug9, MobSpawnType.NATURAL, debug16, null);
/* 84 */         debug1.addFreshEntityWithPassengers((Entity)debug19);
/*    */       } 
/* 86 */       debug5 += debug17;
/*    */     } 
/*    */     
/* 89 */     return debug5;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\PhantomSpawner.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */