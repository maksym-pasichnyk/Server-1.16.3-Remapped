/*    */ package net.minecraft.world.level.levelgen.placement;
/*    */ 
/*    */ import java.util.BitSet;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.ChunkPos;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*    */ import net.minecraft.world.level.chunk.ProtoChunk;
/*    */ import net.minecraft.world.level.levelgen.GenerationStep;
/*    */ import net.minecraft.world.level.levelgen.Heightmap;
/*    */ 
/*    */ public class DecorationContext
/*    */ {
/*    */   private final WorldGenLevel level;
/*    */   private final ChunkGenerator generator;
/*    */   
/*    */   public DecorationContext(WorldGenLevel debug1, ChunkGenerator debug2) {
/* 19 */     this.level = debug1;
/* 20 */     this.generator = debug2;
/*    */   }
/*    */   
/*    */   public int getHeight(Heightmap.Types debug1, int debug2, int debug3) {
/* 24 */     return this.level.getHeight(debug1, debug2, debug3);
/*    */   }
/*    */   
/*    */   public int getGenDepth() {
/* 28 */     return this.generator.getGenDepth();
/*    */   }
/*    */   
/*    */   public int getSeaLevel() {
/* 32 */     return this.generator.getSeaLevel();
/*    */   }
/*    */   
/*    */   public BitSet getCarvingMask(ChunkPos debug1, GenerationStep.Carving debug2) {
/* 36 */     return ((ProtoChunk)this.level.getChunk(debug1.x, debug1.z)).getOrCreateCarvingMask(debug2);
/*    */   }
/*    */   
/*    */   public BlockState getBlockState(BlockPos debug1) {
/* 40 */     return this.level.getBlockState(debug1);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\placement\DecorationContext.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */