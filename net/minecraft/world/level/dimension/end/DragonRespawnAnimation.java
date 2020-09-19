/*    */ package net.minecraft.world.level.dimension.end;
/*    */ 
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import java.util.List;
/*    */ import java.util.Random;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
/*    */ import net.minecraft.world.level.Explosion;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.levelgen.feature.Feature;
/*    */ import net.minecraft.world.level.levelgen.feature.SpikeFeature;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.SpikeConfiguration;
/*    */ 
/*    */ public enum DragonRespawnAnimation {
/* 18 */   START
/*    */   {
/*    */     public void tick(ServerLevel debug1, EndDragonFight debug2, List<EndCrystal> debug3, int debug4, BlockPos debug5) {
/* 21 */       BlockPos debug6 = new BlockPos(0, 128, 0);
/* 22 */       for (EndCrystal debug8 : debug3) {
/* 23 */         debug8.setBeamTarget(debug6);
/*    */       }
/* 25 */       debug2.setRespawnStage(PREPARING_TO_SUMMON_PILLARS);
/*    */     }
/*    */   },
/* 28 */   PREPARING_TO_SUMMON_PILLARS
/*    */   {
/*    */     public void tick(ServerLevel debug1, EndDragonFight debug2, List<EndCrystal> debug3, int debug4, BlockPos debug5) {
/* 31 */       if (debug4 < 100) {
/* 32 */         if (debug4 == 0 || debug4 == 50 || debug4 == 51 || debug4 == 52 || debug4 >= 95) {
/* 33 */           debug1.levelEvent(3001, new BlockPos(0, 128, 0), 0);
/*    */         }
/*    */       } else {
/* 36 */         debug2.setRespawnStage(SUMMONING_PILLARS);
/*    */       } 
/*    */     }
/*    */   },
/* 40 */   SUMMONING_PILLARS
/*    */   {
/*    */     public void tick(ServerLevel debug1, EndDragonFight debug2, List<EndCrystal> debug3, int debug4, BlockPos debug5) {
/* 43 */       int debug6 = 40;
/* 44 */       boolean debug7 = (debug4 % 40 == 0);
/* 45 */       boolean debug8 = (debug4 % 40 == 39);
/* 46 */       if (debug7 || debug8) {
/* 47 */         List<SpikeFeature.EndSpike> debug9 = SpikeFeature.getSpikesForLevel((WorldGenLevel)debug1);
/* 48 */         int debug10 = debug4 / 40;
/* 49 */         if (debug10 < debug9.size()) {
/* 50 */           SpikeFeature.EndSpike debug11 = debug9.get(debug10);
/*    */           
/* 52 */           if (debug7) {
/* 53 */             for (EndCrystal debug13 : debug3) {
/* 54 */               debug13.setBeamTarget(new BlockPos(debug11.getCenterX(), debug11.getHeight() + 1, debug11.getCenterZ()));
/*    */             }
/*    */           } else {
/* 57 */             int debug12 = 10;
/* 58 */             for (BlockPos debug14 : BlockPos.betweenClosed(new BlockPos(debug11
/* 59 */                   .getCenterX() - 10, debug11.getHeight() - 10, debug11.getCenterZ() - 10), new BlockPos(debug11
/* 60 */                   .getCenterX() + 10, debug11.getHeight() + 10, debug11.getCenterZ() + 10)))
/*    */             {
/* 62 */               debug1.removeBlock(debug14, false);
/*    */             }
/* 64 */             debug1.explode(null, (debug11.getCenterX() + 0.5F), debug11.getHeight(), (debug11.getCenterZ() + 0.5F), 5.0F, Explosion.BlockInteraction.DESTROY);
/*    */             
/* 66 */             SpikeConfiguration debug13 = new SpikeConfiguration(true, (List)ImmutableList.of(debug11), new BlockPos(0, 128, 0));
/* 67 */             Feature.END_SPIKE.configured((FeatureConfiguration)debug13).place((WorldGenLevel)debug1, debug1.getChunkSource().getGenerator(), new Random(), new BlockPos(debug11.getCenterX(), 45, debug11.getCenterZ()));
/*    */           } 
/* 69 */         } else if (debug7) {
/* 70 */           debug2.setRespawnStage(SUMMONING_DRAGON);
/*    */         } 
/*    */       } 
/*    */     }
/*    */   },
/* 75 */   SUMMONING_DRAGON
/*    */   {
/*    */     public void tick(ServerLevel debug1, EndDragonFight debug2, List<EndCrystal> debug3, int debug4, BlockPos debug5) {
/* 78 */       if (debug4 >= 100) {
/* 79 */         debug2.setRespawnStage(END);
/* 80 */         debug2.resetSpikeCrystals();
/* 81 */         for (EndCrystal debug7 : debug3) {
/* 82 */           debug7.setBeamTarget(null);
/* 83 */           debug1.explode((Entity)debug7, debug7.getX(), debug7.getY(), debug7.getZ(), 6.0F, Explosion.BlockInteraction.NONE);
/* 84 */           debug7.remove();
/*    */         } 
/* 86 */       } else if (debug4 >= 80) {
/* 87 */         debug1.levelEvent(3001, new BlockPos(0, 128, 0), 0);
/* 88 */       } else if (debug4 == 0) {
/* 89 */         for (EndCrystal debug7 : debug3) {
/* 90 */           debug7.setBeamTarget(new BlockPos(0, 128, 0));
/*    */         }
/* 92 */       } else if (debug4 < 5) {
/* 93 */         debug1.levelEvent(3001, new BlockPos(0, 128, 0), 0);
/*    */       } 
/*    */     }
/*    */   },
/* 97 */   END {
/*    */     public void tick(ServerLevel debug1, EndDragonFight debug2, List<EndCrystal> debug3, int debug4, BlockPos debug5) {}
/*    */   };
/*    */   
/*    */   public abstract void tick(ServerLevel paramServerLevel, EndDragonFight paramEndDragonFight, List<EndCrystal> paramList, int paramInt, BlockPos paramBlockPos);
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\dimension\end\DragonRespawnAnimation.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */