/*     */ package net.minecraft.world.level.levelgen.feature;
/*     */ import com.mojang.serialization.Codec;
/*     */ import java.util.Random;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelWriter;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.BlockStateConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
/*     */ import net.minecraft.world.level.material.Material;
/*     */ 
/*     */ public class IcebergFeature extends Feature<BlockStateConfiguration> {
/*     */   public IcebergFeature(Codec<BlockStateConfiguration> debug1) {
/*  20 */     super(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean place(WorldGenLevel debug1, ChunkGenerator debug2, Random debug3, BlockPos debug4, BlockStateConfiguration debug5) {
/*  25 */     debug4 = new BlockPos(debug4.getX(), debug2.getSeaLevel(), debug4.getZ());
/*  26 */     boolean debug6 = (debug3.nextDouble() > 0.7D);
/*  27 */     BlockState debug7 = debug5.state;
/*     */ 
/*     */     
/*  30 */     double debug8 = debug3.nextDouble() * 2.0D * Math.PI;
/*  31 */     int debug10 = 11 - debug3.nextInt(5);
/*  32 */     int debug11 = 3 + debug3.nextInt(3);
/*  33 */     boolean debug12 = (debug3.nextDouble() > 0.7D);
/*     */     
/*  35 */     int debug13 = 11;
/*  36 */     int debug14 = debug12 ? (debug3.nextInt(6) + 6) : (debug3.nextInt(15) + 3);
/*  37 */     if (!debug12 && debug3.nextDouble() > 0.9D) {
/*  38 */       debug14 += debug3.nextInt(19) + 7;
/*     */     }
/*     */     
/*  41 */     int debug15 = Math.min(debug14 + debug3.nextInt(11), 18);
/*  42 */     int debug16 = Math.min(debug14 + debug3.nextInt(7) - debug3.nextInt(5), 11);
/*  43 */     int debug17 = debug12 ? debug10 : 11;
/*     */     
/*     */     int i;
/*  46 */     for (i = -debug17; i < debug17; i++) {
/*  47 */       for (int debug19 = -debug17; debug19 < debug17; debug19++) {
/*  48 */         for (int debug20 = 0; debug20 < debug14; debug20++) {
/*  49 */           int debug21 = debug12 ? heightDependentRadiusEllipse(debug20, debug14, debug16) : heightDependentRadiusRound(debug3, debug20, debug14, debug16);
/*  50 */           if (debug12 || i < debug21)
/*     */           {
/*     */             
/*  53 */             generateIcebergBlock((LevelAccessor)debug1, debug3, debug4, debug14, i, debug20, debug19, debug21, debug17, debug12, debug11, debug8, debug6, debug7);
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/*  59 */     smooth((LevelAccessor)debug1, debug4, debug16, debug14, debug12, debug10);
/*     */ 
/*     */     
/*  62 */     for (i = -debug17; i < debug17; i++) {
/*  63 */       for (int debug19 = -debug17; debug19 < debug17; debug19++) {
/*  64 */         for (int debug20 = -1; debug20 > -debug15; debug20--) {
/*  65 */           int debug21 = debug12 ? Mth.ceil(debug17 * (1.0F - (float)Math.pow(debug20, 2.0D) / debug15 * 8.0F)) : debug17;
/*  66 */           int debug22 = heightDependentRadiusSteep(debug3, -debug20, debug15, debug16);
/*  67 */           if (i < debug22)
/*     */           {
/*     */             
/*  70 */             generateIcebergBlock((LevelAccessor)debug1, debug3, debug4, debug15, i, debug20, debug19, debug22, debug21, debug12, debug11, debug8, debug6, debug7);
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/*  76 */     boolean debug18 = debug12 ? ((debug3.nextDouble() > 0.1D)) : ((debug3.nextDouble() > 0.7D));
/*  77 */     if (debug18) {
/*  78 */       generateCutOut(debug3, (LevelAccessor)debug1, debug16, debug14, debug4, debug12, debug10, debug8, debug11);
/*     */     }
/*     */     
/*  81 */     return true;
/*     */   }
/*     */   
/*     */   private void generateCutOut(Random debug1, LevelAccessor debug2, int debug3, int debug4, BlockPos debug5, boolean debug6, int debug7, double debug8, int debug10) {
/*  85 */     int debug11 = debug1.nextBoolean() ? -1 : 1;
/*  86 */     int debug12 = debug1.nextBoolean() ? -1 : 1;
/*     */     
/*  88 */     int debug13 = debug1.nextInt(Math.max(debug3 / 2 - 2, 1));
/*  89 */     if (debug1.nextBoolean()) {
/*  90 */       debug13 = debug3 / 2 + 1 - debug1.nextInt(Math.max(debug3 - debug3 / 2 - 1, 1));
/*     */     }
/*  92 */     int debug14 = debug1.nextInt(Math.max(debug3 / 2 - 2, 1));
/*  93 */     if (debug1.nextBoolean()) {
/*  94 */       debug14 = debug3 / 2 + 1 - debug1.nextInt(Math.max(debug3 - debug3 / 2 - 1, 1));
/*     */     }
/*     */     
/*  97 */     if (debug6) {
/*  98 */       debug13 = debug14 = debug1.nextInt(Math.max(debug7 - 5, 1));
/*     */     }
/*     */     
/* 101 */     BlockPos debug15 = new BlockPos(debug11 * debug13, 0, debug12 * debug14);
/* 102 */     double debug16 = debug6 ? (debug8 + 1.5707963267948966D) : (debug1.nextDouble() * 2.0D * Math.PI);
/*     */     int debug18;
/* 104 */     for (debug18 = 0; debug18 < debug4 - 3; debug18++) {
/* 105 */       int debug19 = heightDependentRadiusRound(debug1, debug18, debug4, debug3);
/* 106 */       carve(debug19, debug18, debug5, debug2, false, debug16, debug15, debug7, debug10);
/*     */     } 
/*     */     
/* 109 */     for (debug18 = -1; debug18 > -debug4 + debug1.nextInt(5); debug18--) {
/* 110 */       int debug19 = heightDependentRadiusSteep(debug1, -debug18, debug4, debug3);
/* 111 */       carve(debug19, debug18, debug5, debug2, true, debug16, debug15, debug7, debug10);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void carve(int debug1, int debug2, BlockPos debug3, LevelAccessor debug4, boolean debug5, double debug6, BlockPos debug8, int debug9, int debug10) {
/* 116 */     int debug11 = debug1 + 1 + debug9 / 3;
/* 117 */     int debug12 = Math.min(debug1 - 3, 3) + debug10 / 2 - 1;
/*     */     
/* 119 */     for (int debug13 = -debug11; debug13 < debug11; debug13++) {
/* 120 */       for (int debug14 = -debug11; debug14 < debug11; debug14++) {
/* 121 */         double debug15 = signedDistanceEllipse(debug13, debug14, debug8, debug11, debug12, debug6);
/* 122 */         if (debug15 < 0.0D) {
/* 123 */           BlockPos debug17 = debug3.offset(debug13, debug2, debug14);
/* 124 */           Block debug18 = debug4.getBlockState(debug17).getBlock();
/* 125 */           if (isIcebergBlock(debug18) || debug18 == Blocks.SNOW_BLOCK) {
/* 126 */             if (debug5) {
/* 127 */               setBlock((LevelWriter)debug4, debug17, Blocks.WATER.defaultBlockState());
/*     */             } else {
/* 129 */               setBlock((LevelWriter)debug4, debug17, Blocks.AIR.defaultBlockState());
/* 130 */               removeFloatingSnowLayer(debug4, debug17);
/*     */             } 
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void removeFloatingSnowLayer(LevelAccessor debug1, BlockPos debug2) {
/* 139 */     if (debug1.getBlockState(debug2.above()).is(Blocks.SNOW)) {
/* 140 */       setBlock((LevelWriter)debug1, debug2.above(), Blocks.AIR.defaultBlockState());
/*     */     }
/*     */   }
/*     */   
/*     */   private void generateIcebergBlock(LevelAccessor debug1, Random debug2, BlockPos debug3, int debug4, int debug5, int debug6, int debug7, int debug8, int debug9, boolean debug10, int debug11, double debug12, boolean debug14, BlockState debug15) {
/* 145 */     double debug16 = debug10 ? signedDistanceEllipse(debug5, debug7, BlockPos.ZERO, debug9, getEllipseC(debug6, debug4, debug11), debug12) : signedDistanceCircle(debug5, debug7, BlockPos.ZERO, debug8, debug2);
/* 146 */     if (debug16 < 0.0D) {
/* 147 */       BlockPos debug18 = debug3.offset(debug5, debug6, debug7);
/* 148 */       double debug19 = debug10 ? -0.5D : (-6 - debug2.nextInt(3));
/* 149 */       if (debug16 > debug19 && debug2.nextDouble() > 0.9D) {
/*     */         return;
/*     */       }
/* 152 */       setIcebergBlock(debug18, debug1, debug2, debug4 - debug6, debug4, debug10, debug14, debug15);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void setIcebergBlock(BlockPos debug1, LevelAccessor debug2, Random debug3, int debug4, int debug5, boolean debug6, boolean debug7, BlockState debug8) {
/* 157 */     BlockState debug9 = debug2.getBlockState(debug1);
/* 158 */     if (debug9.getMaterial() == Material.AIR || debug9.is(Blocks.SNOW_BLOCK) || debug9.is(Blocks.ICE) || debug9.is(Blocks.WATER)) {
/* 159 */       boolean debug10 = (!debug6 || debug3.nextDouble() > 0.05D);
/* 160 */       int debug11 = debug6 ? 3 : 2;
/* 161 */       if (debug7 && !debug9.is(Blocks.WATER) && debug4 <= debug3.nextInt(Math.max(1, debug5 / debug11)) + debug5 * 0.6D && debug10) {
/* 162 */         setBlock((LevelWriter)debug2, debug1, Blocks.SNOW_BLOCK.defaultBlockState());
/*     */       } else {
/* 164 */         setBlock((LevelWriter)debug2, debug1, debug8);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private int getEllipseC(int debug1, int debug2, int debug3) {
/* 170 */     int debug4 = debug3;
/* 171 */     if (debug1 > 0 && debug2 - debug1 <= 3) {
/* 172 */       debug4 -= 4 - debug2 - debug1;
/*     */     }
/*     */     
/* 175 */     return debug4;
/*     */   }
/*     */   
/*     */   private double signedDistanceCircle(int debug1, int debug2, BlockPos debug3, int debug4, Random debug5) {
/* 179 */     float debug6 = 10.0F * Mth.clamp(debug5.nextFloat(), 0.2F, 0.8F) / debug4;
/* 180 */     return debug6 + Math.pow((debug1 - debug3.getX()), 2.0D) + Math.pow((debug2 - debug3.getZ()), 2.0D) - Math.pow(debug4, 2.0D);
/*     */   }
/*     */   
/*     */   private double signedDistanceEllipse(int debug1, int debug2, BlockPos debug3, int debug4, int debug5, double debug6) {
/* 184 */     return Math.pow(((debug1 - debug3.getX()) * Math.cos(debug6) - (debug2 - debug3.getZ()) * Math.sin(debug6)) / debug4, 2.0D) + Math.pow(((debug1 - debug3.getX()) * Math.sin(debug6) + (debug2 - debug3.getZ()) * Math.cos(debug6)) / debug5, 2.0D) - 1.0D;
/*     */   }
/*     */   
/*     */   private int heightDependentRadiusRound(Random debug1, int debug2, int debug3, int debug4) {
/* 188 */     float debug5 = 3.5F - debug1.nextFloat();
/* 189 */     float debug6 = (1.0F - (float)Math.pow(debug2, 2.0D) / debug3 * debug5) * debug4;
/*     */     
/* 191 */     if (debug3 > 15 + debug1.nextInt(5)) {
/* 192 */       int debug7 = (debug2 < 3 + debug1.nextInt(6)) ? (debug2 / 2) : debug2;
/* 193 */       debug6 = (1.0F - debug7 / debug3 * debug5 * 0.4F) * debug4;
/*     */     } 
/*     */     
/* 196 */     return Mth.ceil(debug6 / 2.0F);
/*     */   }
/*     */   
/*     */   private int heightDependentRadiusEllipse(int debug1, int debug2, int debug3) {
/* 200 */     float debug4 = 1.0F;
/* 201 */     float debug5 = (1.0F - (float)Math.pow(debug1, 2.0D) / debug2 * 1.0F) * debug3;
/* 202 */     return Mth.ceil(debug5 / 2.0F);
/*     */   }
/*     */   
/*     */   private int heightDependentRadiusSteep(Random debug1, int debug2, int debug3, int debug4) {
/* 206 */     float debug5 = 1.0F + debug1.nextFloat() / 2.0F;
/* 207 */     float debug6 = (1.0F - debug2 / debug3 * debug5) * debug4;
/* 208 */     return Mth.ceil(debug6 / 2.0F);
/*     */   }
/*     */   
/*     */   private boolean isIcebergBlock(Block debug1) {
/* 212 */     return (debug1 == Blocks.PACKED_ICE || debug1 == Blocks.SNOW_BLOCK || debug1 == Blocks.BLUE_ICE);
/*     */   }
/*     */   
/*     */   private boolean belowIsAir(BlockGetter debug1, BlockPos debug2) {
/* 216 */     return (debug1.getBlockState(debug2.below()).getMaterial() == Material.AIR);
/*     */   }
/*     */   
/*     */   private void smooth(LevelAccessor debug1, BlockPos debug2, int debug3, int debug4, boolean debug5, int debug6) {
/* 220 */     int debug7 = debug5 ? debug6 : (debug3 / 2);
/*     */     
/* 222 */     for (int debug8 = -debug7; debug8 <= debug7; debug8++) {
/* 223 */       for (int debug9 = -debug7; debug9 <= debug7; debug9++) {
/* 224 */         for (int debug10 = 0; debug10 <= debug4; debug10++) {
/* 225 */           BlockPos debug11 = debug2.offset(debug8, debug10, debug9);
/* 226 */           Block debug12 = debug1.getBlockState(debug11).getBlock();
/*     */ 
/*     */           
/* 229 */           if (isIcebergBlock(debug12) || debug12 == Blocks.SNOW)
/* 230 */             if (belowIsAir((BlockGetter)debug1, debug11)) {
/* 231 */               setBlock((LevelWriter)debug1, debug11, Blocks.AIR.defaultBlockState());
/* 232 */               setBlock((LevelWriter)debug1, debug11.above(), Blocks.AIR.defaultBlockState());
/*     */ 
/*     */             
/*     */             }
/* 236 */             else if (isIcebergBlock(debug12)) {
/*     */               
/* 238 */               Block[] debug13 = { debug1.getBlockState(debug11.west()).getBlock(), debug1.getBlockState(debug11.east()).getBlock(), debug1.getBlockState(debug11.north()).getBlock(), debug1.getBlockState(debug11.south()).getBlock() };
/* 239 */               int debug14 = 0;
/* 240 */               for (Block debug18 : debug13) {
/* 241 */                 if (!isIcebergBlock(debug18)) {
/* 242 */                   debug14++;
/*     */                 }
/*     */               } 
/* 245 */               if (debug14 >= 3)
/* 246 */                 setBlock((LevelWriter)debug1, debug11, Blocks.AIR.defaultBlockState()); 
/*     */             }  
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\feature\IcebergFeature.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */