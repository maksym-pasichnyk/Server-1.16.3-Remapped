/*    */ package net.minecraft.server.level;
/*    */ 
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.tags.BlockTags;
/*    */ import net.minecraft.tags.Tag;
/*    */ import net.minecraft.world.level.ChunkPos;
/*    */ import net.minecraft.world.level.biome.Biome;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.chunk.LevelChunk;
/*    */ import net.minecraft.world.level.levelgen.Heightmap;
/*    */ 
/*    */ public class PlayerRespawnLogic {
/*    */   @Nullable
/*    */   protected static BlockPos getOverworldRespawnPos(ServerLevel debug0, int debug1, int debug2, boolean debug3) {
/* 16 */     BlockPos.MutableBlockPos debug4 = new BlockPos.MutableBlockPos(debug1, 0, debug2);
/* 17 */     Biome debug5 = debug0.getBiome((BlockPos)debug4);
/*    */     
/* 19 */     boolean debug6 = debug0.dimensionType().hasCeiling();
/*    */     
/* 21 */     BlockState debug7 = debug5.getGenerationSettings().getSurfaceBuilderConfig().getTopMaterial();
/* 22 */     if (debug3 && !debug7.getBlock().is((Tag)BlockTags.VALID_SPAWN)) {
/* 23 */       return null;
/*    */     }
/*    */ 
/*    */ 
/*    */     
/* 28 */     LevelChunk debug8 = debug0.getChunk(debug1 >> 4, debug2 >> 4);
/* 29 */     int debug9 = debug6 ? debug0.getChunkSource().getGenerator().getSpawnHeight() : debug8.getHeight(Heightmap.Types.MOTION_BLOCKING, debug1 & 0xF, debug2 & 0xF);
/*    */ 
/*    */     
/* 32 */     if (debug9 < 0) {
/* 33 */       return null;
/*    */     }
/*    */ 
/*    */     
/* 37 */     int debug10 = debug8.getHeight(Heightmap.Types.WORLD_SURFACE, debug1 & 0xF, debug2 & 0xF);
/* 38 */     if (debug10 <= debug9 && debug10 > debug8.getHeight(Heightmap.Types.OCEAN_FLOOR, debug1 & 0xF, debug2 & 0xF)) {
/* 39 */       return null;
/*    */     }
/*    */     
/* 42 */     for (int debug11 = debug9 + 1; debug11 >= 0; debug11--) {
/* 43 */       debug4.set(debug1, debug11, debug2);
/* 44 */       BlockState debug12 = debug0.getBlockState((BlockPos)debug4);
/*    */ 
/*    */       
/* 47 */       if (!debug12.getFluidState().isEmpty()) {
/*    */         break;
/*    */       }
/*    */       
/* 51 */       if (debug12.equals(debug7)) {
/* 52 */         return debug4.above().immutable();
/*    */       }
/*    */     } 
/* 55 */     return null;
/*    */   }
/*    */   
/*    */   @Nullable
/*    */   public static BlockPos getSpawnPosInChunk(ServerLevel debug0, ChunkPos debug1, boolean debug2) {
/* 60 */     for (int debug3 = debug1.getMinBlockX(); debug3 <= debug1.getMaxBlockX(); debug3++) {
/* 61 */       for (int debug4 = debug1.getMinBlockZ(); debug4 <= debug1.getMaxBlockZ(); debug4++) {
/* 62 */         BlockPos debug5 = getOverworldRespawnPos(debug0, debug3, debug4, debug2);
/* 63 */         if (debug5 != null) {
/* 64 */           return debug5;
/*    */         }
/*    */       } 
/*    */     } 
/*    */     
/* 69 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\level\PlayerRespawnLogic.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */