/*     */ package net.minecraft.world.level.levelgen.feature.trunkplacers;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function3;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import java.util.Set;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.level.LevelSimulatedRW;
/*     */ import net.minecraft.world.level.LevelWriter;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
/*     */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*     */ 
/*     */ public class FancyTrunkPlacer extends TrunkPlacer {
/*     */   static {
/*  22 */     CODEC = RecordCodecBuilder.create(debug0 -> trunkPlacerParts(debug0).apply((Applicative)debug0, FancyTrunkPlacer::new));
/*     */   }
/*     */ 
/*     */   
/*     */   public static final Codec<FancyTrunkPlacer> CODEC;
/*     */ 
/*     */   
/*     */   public FancyTrunkPlacer(int debug1, int debug2, int debug3) {
/*  30 */     super(debug1, debug2, debug3);
/*     */   }
/*     */ 
/*     */   
/*     */   protected TrunkPlacerType<?> type() {
/*  35 */     return TrunkPlacerType.FANCY_TRUNK_PLACER;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<FoliagePlacer.FoliageAttachment> placeTrunk(LevelSimulatedRW debug1, Random debug2, int debug3, BlockPos debug4, Set<BlockPos> debug5, BoundingBox debug6, TreeConfiguration debug7) {
/*  40 */     int debug8 = 5;
/*  41 */     int debug9 = debug3 + 2;
/*  42 */     int debug10 = Mth.floor(debug9 * 0.618D);
/*     */     
/*  44 */     if (!debug7.fromSapling) {
/*  45 */       setDirtAt(debug1, debug4.below());
/*     */     }
/*     */     
/*  48 */     double debug11 = 1.0D;
/*  49 */     int debug13 = Math.min(1, Mth.floor(1.382D + Math.pow(1.0D * debug9 / 13.0D, 2.0D)));
/*     */     
/*  51 */     int debug14 = debug4.getY() + debug10;
/*  52 */     int debug15 = debug9 - 5;
/*     */     
/*  54 */     List<FoliageCoords> debug16 = Lists.newArrayList();
/*  55 */     debug16.add(new FoliageCoords(debug4.above(debug15), debug14));
/*     */     
/*  57 */     for (; debug15 >= 0; debug15--) {
/*  58 */       float f = treeShape(debug9, debug15);
/*  59 */       if (f >= 0.0F)
/*     */       {
/*     */ 
/*     */         
/*  63 */         for (int debug18 = 0; debug18 < debug13; debug18++) {
/*  64 */           double debug19 = 1.0D;
/*  65 */           double debug21 = 1.0D * f * (debug2.nextFloat() + 0.328D);
/*  66 */           double debug23 = (debug2.nextFloat() * 2.0F) * Math.PI;
/*     */           
/*  68 */           double debug25 = debug21 * Math.sin(debug23) + 0.5D;
/*  69 */           double debug27 = debug21 * Math.cos(debug23) + 0.5D;
/*     */           
/*  71 */           BlockPos debug29 = debug4.offset(debug25, (debug15 - 1), debug27);
/*  72 */           BlockPos debug30 = debug29.above(5);
/*     */ 
/*     */           
/*  75 */           if (makeLimb(debug1, debug2, debug29, debug30, false, debug5, debug6, debug7)) {
/*     */             
/*  77 */             int debug31 = debug4.getX() - debug29.getX();
/*  78 */             int debug32 = debug4.getZ() - debug29.getZ();
/*     */             
/*  80 */             double debug33 = debug29.getY() - Math.sqrt((debug31 * debug31 + debug32 * debug32)) * 0.381D;
/*  81 */             int debug35 = (debug33 > debug14) ? debug14 : (int)debug33;
/*  82 */             BlockPos debug36 = new BlockPos(debug4.getX(), debug35, debug4.getZ());
/*     */ 
/*     */             
/*  85 */             if (makeLimb(debug1, debug2, debug36, debug29, false, debug5, debug6, debug7))
/*     */             {
/*  87 */               debug16.add(new FoliageCoords(debug29, debug36.getY())); } 
/*     */           } 
/*     */         } 
/*     */       }
/*     */     } 
/*  92 */     makeLimb(debug1, debug2, debug4, debug4.above(debug10), true, debug5, debug6, debug7);
/*  93 */     makeBranches(debug1, debug2, debug9, debug4, debug16, debug5, debug6, debug7);
/*     */     
/*  95 */     List<FoliagePlacer.FoliageAttachment> debug17 = Lists.newArrayList();
/*  96 */     for (FoliageCoords debug19 : debug16) {
/*  97 */       if (trimBranches(debug9, debug19.getBranchBase() - debug4.getY())) {
/*  98 */         debug17.add(debug19.attachment);
/*     */       }
/*     */     } 
/* 101 */     return debug17;
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean makeLimb(LevelSimulatedRW debug1, Random debug2, BlockPos debug3, BlockPos debug4, boolean debug5, Set<BlockPos> debug6, BoundingBox debug7, TreeConfiguration debug8) {
/* 106 */     if (!debug5 && Objects.equals(debug3, debug4)) {
/* 107 */       return true;
/*     */     }
/*     */     
/* 110 */     BlockPos debug9 = debug4.offset(-debug3.getX(), -debug3.getY(), -debug3.getZ());
/*     */     
/* 112 */     int debug10 = getSteps(debug9);
/*     */     
/* 114 */     float debug11 = debug9.getX() / debug10;
/* 115 */     float debug12 = debug9.getY() / debug10;
/* 116 */     float debug13 = debug9.getZ() / debug10;
/*     */     
/* 118 */     for (int debug14 = 0; debug14 <= debug10; debug14++) {
/* 119 */       BlockPos debug15 = debug3.offset((0.5F + debug14 * debug11), (0.5F + debug14 * debug12), (0.5F + debug14 * debug13));
/* 120 */       if (debug5) {
/* 121 */         setBlock((LevelWriter)debug1, debug15, (BlockState)debug8.trunkProvider.getState(debug2, debug15).setValue((Property)RotatedPillarBlock.AXIS, (Comparable)getLogAxis(debug3, debug15)), debug7);
/* 122 */         debug6.add(debug15.immutable());
/*     */       
/*     */       }
/* 125 */       else if (!TreeFeature.isFree((LevelSimulatedReader)debug1, debug15)) {
/* 126 */         return false;
/*     */       } 
/*     */     } 
/*     */     
/* 130 */     return true;
/*     */   }
/*     */   
/*     */   private int getSteps(BlockPos debug1) {
/* 134 */     int debug2 = Mth.abs(debug1.getX());
/* 135 */     int debug3 = Mth.abs(debug1.getY());
/* 136 */     int debug4 = Mth.abs(debug1.getZ());
/*     */     
/* 138 */     return Math.max(debug2, Math.max(debug3, debug4));
/*     */   }
/*     */   
/*     */   private Direction.Axis getLogAxis(BlockPos debug1, BlockPos debug2) {
/* 142 */     Direction.Axis debug3 = Direction.Axis.Y;
/* 143 */     int debug4 = Math.abs(debug2.getX() - debug1.getX());
/* 144 */     int debug5 = Math.abs(debug2.getZ() - debug1.getZ());
/* 145 */     int debug6 = Math.max(debug4, debug5);
/*     */     
/* 147 */     if (debug6 > 0) {
/* 148 */       if (debug4 == debug6) {
/* 149 */         debug3 = Direction.Axis.X;
/*     */       } else {
/* 151 */         debug3 = Direction.Axis.Z;
/*     */       } 
/*     */     }
/* 154 */     return debug3;
/*     */   }
/*     */   
/*     */   private boolean trimBranches(int debug1, int debug2) {
/* 158 */     return (debug2 >= debug1 * 0.2D);
/*     */   }
/*     */   
/*     */   private void makeBranches(LevelSimulatedRW debug1, Random debug2, int debug3, BlockPos debug4, List<FoliageCoords> debug5, Set<BlockPos> debug6, BoundingBox debug7, TreeConfiguration debug8) {
/* 162 */     for (FoliageCoords debug10 : debug5) {
/* 163 */       int debug11 = debug10.getBranchBase();
/* 164 */       BlockPos debug12 = new BlockPos(debug4.getX(), debug11, debug4.getZ());
/*     */       
/* 166 */       if (!debug12.equals(debug10.attachment.foliagePos()) && trimBranches(debug3, debug11 - debug4.getY())) {
/* 167 */         makeLimb(debug1, debug2, debug12, debug10.attachment.foliagePos(), true, debug6, debug7, debug8);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private float treeShape(int debug1, int debug2) {
/* 174 */     if (debug2 < debug1 * 0.3F) {
/* 175 */       return -1.0F;
/*     */     }
/*     */     
/* 178 */     float debug3 = debug1 / 2.0F;
/* 179 */     float debug4 = debug3 - debug2;
/*     */     
/* 181 */     float debug5 = Mth.sqrt(debug3 * debug3 - debug4 * debug4);
/* 182 */     if (debug4 == 0.0F) {
/* 183 */       debug5 = debug3;
/* 184 */     } else if (Math.abs(debug4) >= debug3) {
/* 185 */       return 0.0F;
/*     */     } 
/*     */     
/* 188 */     return debug5 * 0.5F;
/*     */   }
/*     */   
/*     */   static class FoliageCoords {
/*     */     private final FoliagePlacer.FoliageAttachment attachment;
/*     */     private final int branchBase;
/*     */     
/*     */     public FoliageCoords(BlockPos debug1, int debug2) {
/* 196 */       this.attachment = new FoliagePlacer.FoliageAttachment(debug1, 0, false);
/* 197 */       this.branchBase = debug2;
/*     */     }
/*     */     
/*     */     public int getBranchBase() {
/* 201 */       return this.branchBase;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\feature\trunkplacers\FancyTrunkPlacer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */