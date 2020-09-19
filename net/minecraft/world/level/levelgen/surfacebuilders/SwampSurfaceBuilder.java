/*    */ package net.minecraft.world.level.levelgen.surfacebuilders;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.Random;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.biome.Biome;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.chunk.ChunkAccess;
/*    */ 
/*    */ public class SwampSurfaceBuilder
/*    */   extends SurfaceBuilder<SurfaceBuilderBaseConfiguration> {
/*    */   public SwampSurfaceBuilder(Codec<SurfaceBuilderBaseConfiguration> debug1) {
/* 13 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public void apply(Random debug1, ChunkAccess debug2, Biome debug3, int debug4, int debug5, int debug6, double debug7, BlockState debug9, BlockState debug10, int debug11, long debug12, SurfaceBuilderBaseConfiguration debug14) {
/* 18 */     double debug15 = Biome.BIOME_INFO_NOISE.getValue(debug4 * 0.25D, debug5 * 0.25D, false);
/* 19 */     if (debug15 > 0.0D) {
/* 20 */       int debug17 = debug4 & 0xF;
/* 21 */       int debug18 = debug5 & 0xF;
/*    */       
/* 23 */       BlockPos.MutableBlockPos debug19 = new BlockPos.MutableBlockPos();
/*    */       
/* 25 */       for (int debug20 = debug6; debug20 >= 0; debug20--) {
/* 26 */         debug19.set(debug17, debug20, debug18);
/* 27 */         if (!debug2.getBlockState((BlockPos)debug19).isAir()) {
/* 28 */           if (debug20 == 62 && !debug2.getBlockState((BlockPos)debug19).is(debug10.getBlock())) {
/* 29 */             debug2.setBlockState((BlockPos)debug19, debug10, false);
/*    */           }
/*    */           
/*    */           break;
/*    */         } 
/*    */       } 
/*    */     } 
/* 36 */     SurfaceBuilder.DEFAULT.apply(debug1, debug2, debug3, debug4, debug5, debug6, debug7, debug9, debug10, debug11, debug12, debug14);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\surfacebuilders\SwampSurfaceBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */