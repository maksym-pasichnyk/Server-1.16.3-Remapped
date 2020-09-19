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
/*    */ public class WoodedBadlandsSurfaceBuilder
/*    */   extends BadlandsSurfaceBuilder {
/* 13 */   private static final BlockState WHITE_TERRACOTTA = Blocks.WHITE_TERRACOTTA.defaultBlockState();
/* 14 */   private static final BlockState ORANGE_TERRACOTTA = Blocks.ORANGE_TERRACOTTA.defaultBlockState();
/* 15 */   private static final BlockState TERRACOTTA = Blocks.TERRACOTTA.defaultBlockState();
/*    */   
/*    */   public WoodedBadlandsSurfaceBuilder(Codec<SurfaceBuilderBaseConfiguration> debug1) {
/* 18 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public void apply(Random debug1, ChunkAccess debug2, Biome debug3, int debug4, int debug5, int debug6, double debug7, BlockState debug9, BlockState debug10, int debug11, long debug12, SurfaceBuilderBaseConfiguration debug14) {
/* 23 */     int debug15 = debug4 & 0xF;
/* 24 */     int debug16 = debug5 & 0xF;
/*    */     
/* 26 */     BlockState debug17 = WHITE_TERRACOTTA;
/* 27 */     SurfaceBuilderConfiguration debug18 = debug3.getGenerationSettings().getSurfaceBuilderConfig();
/* 28 */     BlockState debug19 = debug18.getUnderMaterial();
/* 29 */     BlockState debug20 = debug18.getTopMaterial();
/* 30 */     BlockState debug21 = debug19;
/*    */     
/* 32 */     int debug22 = (int)(debug7 / 3.0D + 3.0D + debug1.nextDouble() * 0.25D);
/* 33 */     boolean debug23 = (Math.cos(debug7 / 3.0D * Math.PI) > 0.0D);
/* 34 */     int debug24 = -1;
/* 35 */     boolean debug25 = false;
/* 36 */     int debug26 = 0;
/*    */     
/* 38 */     BlockPos.MutableBlockPos debug27 = new BlockPos.MutableBlockPos();
/*    */     
/* 40 */     for (int debug28 = debug6; debug28 >= 0; debug28--) {
/* 41 */       if (debug26 < 15) {
/* 42 */         debug27.set(debug15, debug28, debug16);
/* 43 */         BlockState debug29 = debug2.getBlockState((BlockPos)debug27);
/*    */         
/* 45 */         if (debug29.isAir()) {
/* 46 */           debug24 = -1;
/* 47 */         } else if (debug29.is(debug9.getBlock())) {
/* 48 */           if (debug24 == -1) {
/* 49 */             debug25 = false;
/* 50 */             if (debug22 <= 0) {
/* 51 */               debug17 = Blocks.AIR.defaultBlockState();
/* 52 */               debug21 = debug9;
/* 53 */             } else if (debug28 >= debug11 - 4 && debug28 <= debug11 + 1) {
/* 54 */               debug17 = WHITE_TERRACOTTA;
/* 55 */               debug21 = debug19;
/*    */             } 
/*    */             
/* 58 */             if (debug28 < debug11 && (debug17 == null || debug17.isAir())) {
/* 59 */               debug17 = debug10;
/*    */             }
/*    */             
/* 62 */             debug24 = debug22 + Math.max(0, debug28 - debug11);
/* 63 */             if (debug28 >= debug11 - 1) {
/* 64 */               if (debug28 > 86 + debug22 * 2) {
/* 65 */                 if (debug23) {
/* 66 */                   debug2.setBlockState((BlockPos)debug27, Blocks.COARSE_DIRT.defaultBlockState(), false);
/*    */                 } else {
/* 68 */                   debug2.setBlockState((BlockPos)debug27, Blocks.GRASS_BLOCK.defaultBlockState(), false);
/*    */                 } 
/* 70 */               } else if (debug28 > debug11 + 3 + debug22) {
/*    */                 BlockState debug30;
/* 72 */                 if (debug28 < 64 || debug28 > 127) {
/* 73 */                   debug30 = ORANGE_TERRACOTTA;
/* 74 */                 } else if (debug23) {
/* 75 */                   debug30 = TERRACOTTA;
/*    */                 } else {
/* 77 */                   debug30 = getBand(debug4, debug28, debug5);
/*    */                 } 
/* 79 */                 debug2.setBlockState((BlockPos)debug27, debug30, false);
/*    */               } else {
/* 81 */                 debug2.setBlockState((BlockPos)debug27, debug20, false);
/* 82 */                 debug25 = true;
/*    */               } 
/*    */             } else {
/* 85 */               debug2.setBlockState((BlockPos)debug27, debug21, false);
/* 86 */               if (debug21 == WHITE_TERRACOTTA) {
/* 87 */                 debug2.setBlockState((BlockPos)debug27, ORANGE_TERRACOTTA, false);
/*    */               }
/*    */             } 
/* 90 */           } else if (debug24 > 0) {
/* 91 */             debug24--;
/*    */             
/* 93 */             if (debug25) {
/* 94 */               debug2.setBlockState((BlockPos)debug27, ORANGE_TERRACOTTA, false);
/*    */             } else {
/* 96 */               debug2.setBlockState((BlockPos)debug27, getBand(debug4, debug28, debug5), false);
/*    */             } 
/*    */           } 
/* 99 */           debug26++;
/*    */         } 
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\surfacebuilders\WoodedBadlandsSurfaceBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */