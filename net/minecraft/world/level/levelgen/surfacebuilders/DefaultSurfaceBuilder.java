/*    */ package net.minecraft.world.level.levelgen.surfacebuilders;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.Random;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.biome.Biome;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.chunk.ChunkAccess;
/*    */ 
/*    */ public class DefaultSurfaceBuilder
/*    */   extends SurfaceBuilder<SurfaceBuilderBaseConfiguration>
/*    */ {
/*    */   public DefaultSurfaceBuilder(Codec<SurfaceBuilderBaseConfiguration> debug1) {
/* 15 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public void apply(Random debug1, ChunkAccess debug2, Biome debug3, int debug4, int debug5, int debug6, double debug7, BlockState debug9, BlockState debug10, int debug11, long debug12, SurfaceBuilderBaseConfiguration debug14) {
/* 20 */     apply(debug1, debug2, debug3, debug4, debug5, debug6, debug7, debug9, debug10, debug14.getTopMaterial(), debug14.getUnderMaterial(), debug14.getUnderwaterMaterial(), debug11);
/*    */   }
/*    */   
/*    */   protected void apply(Random debug1, ChunkAccess debug2, Biome debug3, int debug4, int debug5, int debug6, double debug7, BlockState debug9, BlockState debug10, BlockState debug11, BlockState debug12, BlockState debug13, int debug14) {
/* 24 */     BlockState debug15 = debug11;
/* 25 */     BlockState debug16 = debug12;
/* 26 */     BlockPos.MutableBlockPos debug17 = new BlockPos.MutableBlockPos();
/*    */     
/* 28 */     int debug18 = -1;
/* 29 */     int debug19 = (int)(debug7 / 3.0D + 3.0D + debug1.nextDouble() * 0.25D);
/*    */     
/* 31 */     int debug20 = debug4 & 0xF;
/* 32 */     int debug21 = debug5 & 0xF;
/*    */     
/* 34 */     for (int debug22 = debug6; debug22 >= 0; debug22--) {
/* 35 */       debug17.set(debug20, debug22, debug21);
/* 36 */       BlockState debug23 = debug2.getBlockState((BlockPos)debug17);
/*    */       
/* 38 */       if (debug23.isAir()) {
/* 39 */         debug18 = -1;
/*    */ 
/*    */       
/*    */       }
/* 43 */       else if (debug23.is(debug9.getBlock())) {
/*    */ 
/*    */ 
/*    */         
/* 47 */         if (debug18 == -1) {
/* 48 */           if (debug19 <= 0) {
/* 49 */             debug15 = Blocks.AIR.defaultBlockState();
/* 50 */             debug16 = debug9;
/* 51 */           } else if (debug22 >= debug14 - 4 && debug22 <= debug14 + 1) {
/* 52 */             debug15 = debug11;
/* 53 */             debug16 = debug12;
/*    */           } 
/*    */           
/* 56 */           if (debug22 < debug14 && (debug15 == null || debug15.isAir())) {
/* 57 */             if (debug3.getTemperature((BlockPos)debug17.set(debug4, debug22, debug5)) < 0.15F) {
/* 58 */               debug15 = Blocks.ICE.defaultBlockState();
/*    */             } else {
/* 60 */               debug15 = debug10;
/*    */             } 
/* 62 */             debug17.set(debug20, debug22, debug21);
/*    */           } 
/*    */           
/* 65 */           debug18 = debug19;
/* 66 */           if (debug22 >= debug14 - 1) {
/* 67 */             debug2.setBlockState((BlockPos)debug17, debug15, false);
/* 68 */           } else if (debug22 < debug14 - 7 - debug19) {
/* 69 */             debug15 = Blocks.AIR.defaultBlockState();
/* 70 */             debug16 = debug9;
/* 71 */             debug2.setBlockState((BlockPos)debug17, debug13, false);
/*    */           } else {
/* 73 */             debug2.setBlockState((BlockPos)debug17, debug16, false);
/*    */           } 
/* 75 */         } else if (debug18 > 0) {
/* 76 */           debug18--;
/* 77 */           debug2.setBlockState((BlockPos)debug17, debug16, false);
/*    */ 
/*    */           
/* 80 */           if (debug18 == 0 && debug16.is(Blocks.SAND) && debug19 > 1) {
/* 81 */             debug18 = debug1.nextInt(4) + Math.max(0, debug22 - 63);
/* 82 */             debug16 = debug16.is(Blocks.RED_SAND) ? Blocks.RED_SANDSTONE.defaultBlockState() : Blocks.SANDSTONE.defaultBlockState();
/*    */           } 
/*    */         } 
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\surfacebuilders\DefaultSurfaceBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */