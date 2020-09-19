/*    */ package net.minecraft.world.level.levelgen.surfacebuilders;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.Random;
/*    */ import net.minecraft.world.level.biome.Biome;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.chunk.ChunkAccess;
/*    */ 
/*    */ public class GravellyMountainSurfaceBuilder
/*    */   extends SurfaceBuilder<SurfaceBuilderBaseConfiguration> {
/*    */   public GravellyMountainSurfaceBuilder(Codec<SurfaceBuilderBaseConfiguration> debug1) {
/* 12 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public void apply(Random debug1, ChunkAccess debug2, Biome debug3, int debug4, int debug5, int debug6, double debug7, BlockState debug9, BlockState debug10, int debug11, long debug12, SurfaceBuilderBaseConfiguration debug14) {
/* 17 */     if (debug7 < -1.0D || debug7 > 2.0D) {
/* 18 */       SurfaceBuilder.DEFAULT.apply(debug1, debug2, debug3, debug4, debug5, debug6, debug7, debug9, debug10, debug11, debug12, SurfaceBuilder.CONFIG_GRAVEL);
/* 19 */     } else if (debug7 > 1.0D) {
/* 20 */       SurfaceBuilder.DEFAULT.apply(debug1, debug2, debug3, debug4, debug5, debug6, debug7, debug9, debug10, debug11, debug12, SurfaceBuilder.CONFIG_STONE);
/*    */     } else {
/* 22 */       SurfaceBuilder.DEFAULT.apply(debug1, debug2, debug3, debug4, debug5, debug6, debug7, debug9, debug10, debug11, debug12, SurfaceBuilder.CONFIG_GRASS);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\surfacebuilders\GravellyMountainSurfaceBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */