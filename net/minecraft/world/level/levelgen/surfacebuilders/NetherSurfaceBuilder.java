/*    */ package net.minecraft.world.level.levelgen.surfacebuilders;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.Random;
/*    */ import java.util.stream.IntStream;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.biome.Biome;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.chunk.ChunkAccess;
/*    */ import net.minecraft.world.level.levelgen.WorldgenRandom;
/*    */ import net.minecraft.world.level.levelgen.synth.PerlinNoise;
/*    */ 
/*    */ public class NetherSurfaceBuilder
/*    */   extends SurfaceBuilder<SurfaceBuilderBaseConfiguration>
/*    */ {
/* 17 */   private static final BlockState AIR = Blocks.CAVE_AIR.defaultBlockState();
/* 18 */   private static final BlockState GRAVEL = Blocks.GRAVEL.defaultBlockState();
/* 19 */   private static final BlockState SOUL_SAND = Blocks.SOUL_SAND.defaultBlockState();
/*    */   
/*    */   protected long seed;
/*    */   protected PerlinNoise decorationNoise;
/*    */   
/*    */   public NetherSurfaceBuilder(Codec<SurfaceBuilderBaseConfiguration> debug1) {
/* 25 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public void apply(Random debug1, ChunkAccess debug2, Biome debug3, int debug4, int debug5, int debug6, double debug7, BlockState debug9, BlockState debug10, int debug11, long debug12, SurfaceBuilderBaseConfiguration debug14) {
/* 30 */     int debug15 = debug11;
/* 31 */     int debug16 = debug4 & 0xF;
/* 32 */     int debug17 = debug5 & 0xF;
/*    */     
/* 34 */     double debug18 = 0.03125D;
/* 35 */     boolean debug20 = (this.decorationNoise.getValue(debug4 * 0.03125D, debug5 * 0.03125D, 0.0D) * 75.0D + debug1.nextDouble() > 0.0D);
/* 36 */     boolean debug21 = (this.decorationNoise.getValue(debug4 * 0.03125D, 109.0D, debug5 * 0.03125D) * 75.0D + debug1.nextDouble() > 0.0D);
/* 37 */     int debug22 = (int)(debug7 / 3.0D + 3.0D + debug1.nextDouble() * 0.25D);
/*    */     
/* 39 */     BlockPos.MutableBlockPos debug23 = new BlockPos.MutableBlockPos();
/*    */     
/* 41 */     int debug24 = -1;
/*    */     
/* 43 */     BlockState debug25 = debug14.getTopMaterial();
/* 44 */     BlockState debug26 = debug14.getUnderMaterial();
/*    */     
/* 46 */     for (int debug27 = 127; debug27 >= 0; debug27--) {
/* 47 */       debug23.set(debug16, debug27, debug17);
/*    */       
/* 49 */       BlockState debug28 = debug2.getBlockState((BlockPos)debug23);
/* 50 */       if (debug28.isAir()) {
/* 51 */         debug24 = -1;
/* 52 */       } else if (debug28.is(debug9.getBlock())) {
/* 53 */         if (debug24 == -1) {
/* 54 */           boolean debug29 = false;
/* 55 */           if (debug22 <= 0) {
/* 56 */             debug29 = true;
/* 57 */             debug26 = debug14.getUnderMaterial();
/* 58 */           } else if (debug27 >= debug15 - 4 && debug27 <= debug15 + 1) {
/* 59 */             debug25 = debug14.getTopMaterial();
/* 60 */             debug26 = debug14.getUnderMaterial();
/* 61 */             if (debug21) {
/* 62 */               debug25 = GRAVEL;
/* 63 */               debug26 = debug14.getUnderMaterial();
/*    */             } 
/* 65 */             if (debug20) {
/* 66 */               debug25 = SOUL_SAND;
/* 67 */               debug26 = SOUL_SAND;
/*    */             } 
/*    */           } 
/*    */           
/* 71 */           if (debug27 < debug15 && debug29) {
/* 72 */             debug25 = debug10;
/*    */           }
/*    */           
/* 75 */           debug24 = debug22;
/* 76 */           if (debug27 >= debug15 - 1) {
/* 77 */             debug2.setBlockState((BlockPos)debug23, debug25, false);
/*    */           } else {
/* 79 */             debug2.setBlockState((BlockPos)debug23, debug26, false);
/*    */           } 
/* 81 */         } else if (debug24 > 0) {
/* 82 */           debug24--;
/* 83 */           debug2.setBlockState((BlockPos)debug23, debug26, false);
/*    */         } 
/*    */       } 
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void initNoise(long debug1) {
/* 91 */     if (this.seed != debug1 || this.decorationNoise == null) {
/* 92 */       this.decorationNoise = new PerlinNoise(new WorldgenRandom(debug1), IntStream.rangeClosed(-3, 0));
/*    */     }
/* 94 */     this.seed = debug1;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\surfacebuilders\NetherSurfaceBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */