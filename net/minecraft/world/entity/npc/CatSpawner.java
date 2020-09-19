/*    */ package net.minecraft.world.entity.npc;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.Random;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.MobSpawnType;
/*    */ import net.minecraft.world.entity.SpawnPlacements;
/*    */ import net.minecraft.world.entity.ai.village.poi.PoiManager;
/*    */ import net.minecraft.world.entity.ai.village.poi.PoiType;
/*    */ import net.minecraft.world.entity.animal.Cat;
/*    */ import net.minecraft.world.level.CustomSpawner;
/*    */ import net.minecraft.world.level.GameRules;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.NaturalSpawner;
/*    */ import net.minecraft.world.level.ServerLevelAccessor;
/*    */ import net.minecraft.world.level.levelgen.feature.StructureFeature;
/*    */ import net.minecraft.world.phys.AABB;
/*    */ 
/*    */ public class CatSpawner
/*    */   implements CustomSpawner
/*    */ {
/*    */   private int nextTick;
/*    */   
/*    */   public int tick(ServerLevel debug1, boolean debug2, boolean debug3) {
/* 30 */     if (!debug3 || !debug1.getGameRules().getBoolean(GameRules.RULE_DOMOBSPAWNING)) {
/* 31 */       return 0;
/*    */     }
/*    */     
/* 34 */     this.nextTick--;
/* 35 */     if (this.nextTick > 0) {
/* 36 */       return 0;
/*    */     }
/*    */     
/* 39 */     this.nextTick = 1200;
/*    */     
/* 41 */     ServerPlayer serverPlayer = debug1.getRandomPlayer();
/* 42 */     if (serverPlayer == null) {
/* 43 */       return 0;
/*    */     }
/*    */     
/* 46 */     Random debug5 = debug1.random;
/* 47 */     int debug6 = (8 + debug5.nextInt(24)) * (debug5.nextBoolean() ? -1 : 1);
/* 48 */     int debug7 = (8 + debug5.nextInt(24)) * (debug5.nextBoolean() ? -1 : 1);
/* 49 */     BlockPos debug8 = serverPlayer.blockPosition().offset(debug6, 0, debug7);
/*    */     
/* 51 */     if (!debug1.hasChunksAt(debug8.getX() - 10, debug8.getY() - 10, debug8.getZ() - 10, debug8.getX() + 10, debug8.getY() + 10, debug8.getZ() + 10)) {
/* 52 */       return 0;
/*    */     }
/*    */     
/* 55 */     if (NaturalSpawner.isSpawnPositionOk(SpawnPlacements.Type.ON_GROUND, (LevelReader)debug1, debug8, EntityType.CAT)) {
/* 56 */       if (debug1.isCloseToVillage(debug8, 2)) {
/* 57 */         return spawnInVillage(debug1, debug8);
/*    */       }
/*    */       
/* 60 */       if (debug1.structureFeatureManager().getStructureAt(debug8, true, (StructureFeature)StructureFeature.SWAMP_HUT).isValid()) {
/* 61 */         return spawnInHut(debug1, debug8);
/*    */       }
/*    */     } 
/*    */     
/* 65 */     return 0;
/*    */   }
/*    */   
/*    */   private int spawnInVillage(ServerLevel debug1, BlockPos debug2) {
/* 69 */     int debug3 = 48;
/* 70 */     if (debug1.getPoiManager().getCountInRange(PoiType.HOME.getPredicate(), debug2, 48, PoiManager.Occupancy.IS_OCCUPIED) > 4L) {
/* 71 */       List<Cat> debug4 = debug1.getEntitiesOfClass(Cat.class, (new AABB(debug2)).inflate(48.0D, 8.0D, 48.0D));
/* 72 */       if (debug4.size() < 5) {
/* 73 */         return spawnCat(debug2, debug1);
/*    */       }
/*    */     } 
/* 76 */     return 0;
/*    */   }
/*    */   
/*    */   private int spawnInHut(ServerLevel debug1, BlockPos debug2) {
/* 80 */     int debug3 = 16;
/* 81 */     List<Cat> debug4 = debug1.getEntitiesOfClass(Cat.class, (new AABB(debug2)).inflate(16.0D, 8.0D, 16.0D));
/* 82 */     if (debug4.size() < 1) {
/* 83 */       return spawnCat(debug2, debug1);
/*    */     }
/*    */     
/* 86 */     return 0;
/*    */   }
/*    */   
/*    */   private int spawnCat(BlockPos debug1, ServerLevel debug2) {
/* 90 */     Cat debug3 = (Cat)EntityType.CAT.create((Level)debug2);
/* 91 */     if (debug3 == null) {
/* 92 */       return 0;
/*    */     }
/*    */     
/* 95 */     debug3.finalizeSpawn((ServerLevelAccessor)debug2, debug2.getCurrentDifficultyAt(debug1), MobSpawnType.NATURAL, null, null);
/* 96 */     debug3.moveTo(debug1, 0.0F, 0.0F);
/* 97 */     debug2.addFreshEntityWithPassengers((Entity)debug3);
/* 98 */     return 1;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\npc\CatSpawner.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */