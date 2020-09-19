/*    */ package net.minecraft.world.level.levelgen.surfacebuilders;
/*    */ 
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.List;
/*    */ import java.util.Random;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.biome.Biome;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.chunk.ChunkAccess;
/*    */ import net.minecraft.world.level.levelgen.WorldgenRandom;
/*    */ import net.minecraft.world.level.levelgen.synth.PerlinNoise;
/*    */ 
/*    */ public class NetherForestSurfaceBuilder
/*    */   extends SurfaceBuilder<SurfaceBuilderBaseConfiguration> {
/* 17 */   private static final BlockState AIR = Blocks.CAVE_AIR.defaultBlockState();
/*    */   
/*    */   protected long seed;
/*    */   private PerlinNoise decorationNoise;
/*    */   
/*    */   public NetherForestSurfaceBuilder(Codec<SurfaceBuilderBaseConfiguration> debug1) {
/* 23 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public void apply(Random debug1, ChunkAccess debug2, Biome debug3, int debug4, int debug5, int debug6, double debug7, BlockState debug9, BlockState debug10, int debug11, long debug12, SurfaceBuilderBaseConfiguration debug14) {
/* 28 */     int debug15 = debug11;
/* 29 */     int debug16 = debug4 & 0xF;
/* 30 */     int debug17 = debug5 & 0xF;
/*    */     
/* 32 */     double debug18 = this.decorationNoise.getValue(debug4 * 0.1D, debug11, debug5 * 0.1D);
/* 33 */     boolean debug20 = (debug18 > 0.15D + debug1.nextDouble() * 0.35D);
/* 34 */     double debug21 = this.decorationNoise.getValue(debug4 * 0.1D, 109.0D, debug5 * 0.1D);
/* 35 */     boolean debug23 = (debug21 > 0.25D + debug1.nextDouble() * 0.9D);
/* 36 */     int debug24 = (int)(debug7 / 3.0D + 3.0D + debug1.nextDouble() * 0.25D);
/*    */     
/* 38 */     BlockPos.MutableBlockPos debug25 = new BlockPos.MutableBlockPos();
/*    */     
/* 40 */     int debug26 = -1;
/*    */     
/* 42 */     BlockState debug27 = debug14.getUnderMaterial();
/*    */     
/* 44 */     for (int debug28 = 127; debug28 >= 0; debug28--) {
/* 45 */       debug25.set(debug16, debug28, debug17);
/* 46 */       BlockState debug29 = debug14.getTopMaterial();
/*    */       
/* 48 */       BlockState debug30 = debug2.getBlockState((BlockPos)debug25);
/* 49 */       if (debug30.isAir()) {
/* 50 */         debug26 = -1;
/* 51 */       } else if (debug30.is(debug9.getBlock())) {
/* 52 */         if (debug26 == -1) {
/* 53 */           boolean debug31 = false;
/* 54 */           if (debug24 <= 0) {
/* 55 */             debug31 = true;
/* 56 */             debug27 = debug14.getUnderMaterial();
/*    */           } 
/*    */           
/* 59 */           if (debug20) {
/* 60 */             debug29 = debug14.getUnderMaterial();
/* 61 */           } else if (debug23) {
/* 62 */             debug29 = debug14.getUnderwaterMaterial();
/*    */           } 
/*    */           
/* 65 */           if (debug28 < debug15 && debug31) {
/* 66 */             debug29 = debug10;
/*    */           }
/*    */           
/* 69 */           debug26 = debug24;
/* 70 */           if (debug28 >= debug15 - 1) {
/* 71 */             debug2.setBlockState((BlockPos)debug25, debug29, false);
/*    */           } else {
/* 73 */             debug2.setBlockState((BlockPos)debug25, debug27, false);
/*    */           } 
/* 75 */         } else if (debug26 > 0) {
/* 76 */           debug26--;
/* 77 */           debug2.setBlockState((BlockPos)debug25, debug27, false);
/*    */         } 
/*    */       } 
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void initNoise(long debug1) {
/* 85 */     if (this.seed != debug1 || this.decorationNoise == null) {
/* 86 */       this.decorationNoise = new PerlinNoise(new WorldgenRandom(debug1), (List)ImmutableList.of(Integer.valueOf(0)));
/*    */     }
/* 88 */     this.seed = debug1;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\surfacebuilders\NetherForestSurfaceBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */