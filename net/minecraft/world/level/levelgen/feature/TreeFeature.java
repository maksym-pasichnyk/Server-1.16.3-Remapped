/*     */ package net.minecraft.world.level.levelgen.feature;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Sets;
/*     */ import com.mojang.serialization.Codec;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import java.util.OptionalInt;
/*     */ import java.util.Random;
/*     */ import java.util.Set;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.tags.Tag;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelSimulatedRW;
/*     */ import net.minecraft.world.level.LevelSimulatedReader;
/*     */ import net.minecraft.world.level.LevelWriter;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
/*     */ import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
/*     */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
/*     */ import net.minecraft.world.level.material.Material;
/*     */ import net.minecraft.world.phys.shapes.BitSetDiscreteVoxelShape;
/*     */ import net.minecraft.world.phys.shapes.DiscreteVoxelShape;
/*     */ 
/*     */ public class TreeFeature extends Feature<TreeConfiguration> {
/*     */   public TreeFeature(Codec<TreeConfiguration> debug1) {
/*  39 */     super(debug1);
/*     */   }
/*     */   
/*     */   public static boolean isFree(LevelSimulatedReader debug0, BlockPos debug1) {
/*  43 */     return (validTreePos(debug0, debug1) || debug0.isStateAtPosition(debug1, debug0 -> debug0.is((Tag)BlockTags.LOGS)));
/*     */   }
/*     */   
/*     */   private static boolean isVine(LevelSimulatedReader debug0, BlockPos debug1) {
/*  47 */     return debug0.isStateAtPosition(debug1, debug0 -> debug0.is(Blocks.VINE));
/*     */   }
/*     */   
/*     */   private static boolean isBlockWater(LevelSimulatedReader debug0, BlockPos debug1) {
/*  51 */     return debug0.isStateAtPosition(debug1, debug0 -> debug0.is(Blocks.WATER));
/*     */   }
/*     */   
/*     */   public static boolean isAirOrLeaves(LevelSimulatedReader debug0, BlockPos debug1) {
/*  55 */     return debug0.isStateAtPosition(debug1, debug0 -> (debug0.isAir() || debug0.is((Tag)BlockTags.LEAVES)));
/*     */   }
/*     */   
/*     */   private static boolean isGrassOrDirtOrFarmland(LevelSimulatedReader debug0, BlockPos debug1) {
/*  59 */     return debug0.isStateAtPosition(debug1, debug0 -> {
/*     */           Block debug1 = debug0.getBlock();
/*  61 */           return (isDirt(debug1) || debug1 == Blocks.FARMLAND);
/*     */         });
/*     */   }
/*     */   
/*     */   private static boolean isReplaceablePlant(LevelSimulatedReader debug0, BlockPos debug1) {
/*  66 */     return debug0.isStateAtPosition(debug1, debug0 -> {
/*     */           Material debug1 = debug0.getMaterial();
/*     */           return (debug1 == Material.REPLACEABLE_PLANT);
/*     */         });
/*     */   }
/*     */   
/*     */   public static void setBlockKnownShape(LevelWriter debug0, BlockPos debug1, BlockState debug2) {
/*  73 */     debug0.setBlock(debug1, debug2, 19);
/*     */   }
/*     */   
/*     */   public static boolean validTreePos(LevelSimulatedReader debug0, BlockPos debug1) {
/*  77 */     return (isAirOrLeaves(debug0, debug1) || isReplaceablePlant(debug0, debug1) || isBlockWater(debug0, debug1));
/*     */   }
/*     */   private boolean doPlace(LevelSimulatedRW debug1, Random debug2, BlockPos debug3, Set<BlockPos> debug4, Set<BlockPos> debug5, BoundingBox debug6, TreeConfiguration debug7) {
/*     */     BlockPos debug12;
/*  81 */     int debug8 = debug7.trunkPlacer.getTreeHeight(debug2);
/*  82 */     int debug9 = debug7.foliagePlacer.foliageHeight(debug2, debug8, debug7);
/*  83 */     int debug10 = debug8 - debug9;
/*     */     
/*  85 */     int debug11 = debug7.foliagePlacer.foliageRadius(debug2, debug10);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  90 */     if (!debug7.fromSapling) {
/*  91 */       int k, i = debug1.getHeightmapPos(Heightmap.Types.OCEAN_FLOOR, debug3).getY();
/*  92 */       int j = debug1.getHeightmapPos(Heightmap.Types.WORLD_SURFACE, debug3).getY();
/*     */       
/*  94 */       if (j - i > debug7.maxWaterDepth) {
/*  95 */         return false;
/*     */       }
/*     */ 
/*     */       
/*  99 */       if (debug7.heightmap == Heightmap.Types.OCEAN_FLOOR) {
/* 100 */         k = i;
/* 101 */       } else if (debug7.heightmap == Heightmap.Types.WORLD_SURFACE) {
/* 102 */         k = j;
/*     */       } else {
/* 104 */         k = debug1.getHeightmapPos(debug7.heightmap, debug3).getY();
/*     */       } 
/*     */       
/* 107 */       debug12 = new BlockPos(debug3.getX(), k, debug3.getZ());
/*     */     } else {
/* 109 */       debug12 = debug3;
/*     */     } 
/*     */     
/* 112 */     if (debug12.getY() < 1 || debug12.getY() + debug8 + 1 > 256) {
/* 113 */       return false;
/*     */     }
/*     */     
/* 116 */     if (!isGrassOrDirtOrFarmland((LevelSimulatedReader)debug1, debug12.below())) {
/* 117 */       return false;
/*     */     }
/*     */     
/* 120 */     OptionalInt debug13 = debug7.minimumSize.minClippedHeight();
/*     */     
/* 122 */     int debug14 = getMaxFreeTreeHeight((LevelSimulatedReader)debug1, debug8, debug12, debug7);
/* 123 */     if (debug14 < debug8 && (!debug13.isPresent() || debug14 < debug13.getAsInt())) {
/* 124 */       return false;
/*     */     }
/*     */     
/* 127 */     List<FoliagePlacer.FoliageAttachment> debug15 = debug7.trunkPlacer.placeTrunk(debug1, debug2, debug14, debug12, debug4, debug6, debug7);
/* 128 */     debug15.forEach(debug8 -> debug0.foliagePlacer.createFoliage(debug1, debug2, debug0, debug3, debug8, debug4, debug5, debug6, debug7));
/*     */ 
/*     */     
/* 131 */     return true;
/*     */   }
/*     */   
/*     */   private int getMaxFreeTreeHeight(LevelSimulatedReader debug1, int debug2, BlockPos debug3, TreeConfiguration debug4) {
/* 135 */     BlockPos.MutableBlockPos debug5 = new BlockPos.MutableBlockPos();
/*     */     
/* 137 */     for (int debug6 = 0; debug6 <= debug2 + 1; debug6++) {
/* 138 */       int debug7 = debug4.minimumSize.getSizeAtHeight(debug2, debug6);
/* 139 */       for (int debug8 = -debug7; debug8 <= debug7; debug8++) {
/* 140 */         for (int debug9 = -debug7; debug9 <= debug7; debug9++) {
/* 141 */           debug5.setWithOffset((Vec3i)debug3, debug8, debug6, debug9);
/* 142 */           if (!isFree(debug1, (BlockPos)debug5) || (!debug4.ignoreVines && isVine(debug1, (BlockPos)debug5))) {
/* 143 */             return debug6 - 2;
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 149 */     return debug2;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void setBlock(LevelWriter debug1, BlockPos debug2, BlockState debug3) {
/* 154 */     setBlockKnownShape(debug1, debug2, debug3);
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean place(WorldGenLevel debug1, ChunkGenerator debug2, Random debug3, BlockPos debug4, TreeConfiguration debug5) {
/* 159 */     Set<BlockPos> debug6 = Sets.newHashSet();
/* 160 */     Set<BlockPos> debug7 = Sets.newHashSet();
/* 161 */     Set<BlockPos> debug8 = Sets.newHashSet();
/* 162 */     BoundingBox debug9 = BoundingBox.getUnknownBox();
/* 163 */     boolean debug10 = doPlace((LevelSimulatedRW)debug1, debug3, debug4, debug6, debug7, debug9, debug5);
/*     */     
/* 165 */     if (debug9.x0 > debug9.x1 || !debug10 || debug6.isEmpty()) {
/* 166 */       return false;
/*     */     }
/*     */     
/* 169 */     if (!debug5.decorators.isEmpty()) {
/*     */       
/* 171 */       List<BlockPos> list1 = Lists.newArrayList(debug6);
/* 172 */       List<BlockPos> debug12 = Lists.newArrayList(debug7);
/* 173 */       list1.sort(Comparator.comparingInt(Vec3i::getY));
/* 174 */       debug12.sort(Comparator.comparingInt(Vec3i::getY));
/* 175 */       debug5.decorators.forEach(debug6 -> debug6.place(debug0, debug1, debug2, debug3, debug4, debug5));
/*     */     } 
/*     */     
/* 178 */     DiscreteVoxelShape debug11 = updateLeaves((LevelAccessor)debug1, debug9, debug6, debug8);
/* 179 */     StructureTemplate.updateShapeAtEdge((LevelAccessor)debug1, 3, debug11, debug9.x0, debug9.y0, debug9.z0);
/*     */     
/* 181 */     return true;
/*     */   }
/*     */   
/*     */   private DiscreteVoxelShape updateLeaves(LevelAccessor debug1, BoundingBox debug2, Set<BlockPos> debug3, Set<BlockPos> debug4) {
/* 185 */     List<Set<BlockPos>> debug5 = Lists.newArrayList();
/* 186 */     BitSetDiscreteVoxelShape bitSetDiscreteVoxelShape = new BitSetDiscreteVoxelShape(debug2.getXSpan(), debug2.getYSpan(), debug2.getZSpan());
/* 187 */     int debug7 = 6;
/* 188 */     for (int i = 0; i < 6; i++) {
/* 189 */       debug5.add(Sets.newHashSet());
/*     */     }
/*     */     
/* 192 */     BlockPos.MutableBlockPos debug8 = new BlockPos.MutableBlockPos();
/* 193 */     for (BlockPos debug10 : Lists.newArrayList(debug4)) {
/* 194 */       if (debug2.isInside((Vec3i)debug10)) {
/* 195 */         bitSetDiscreteVoxelShape.setFull(debug10.getX() - debug2.x0, debug10.getY() - debug2.y0, debug10.getZ() - debug2.z0, true, true);
/*     */       }
/*     */     } 
/*     */     
/* 199 */     for (BlockPos debug10 : Lists.newArrayList(debug3)) {
/* 200 */       if (debug2.isInside((Vec3i)debug10)) {
/* 201 */         bitSetDiscreteVoxelShape.setFull(debug10.getX() - debug2.x0, debug10.getY() - debug2.y0, debug10.getZ() - debug2.z0, true, true);
/*     */       }
/* 203 */       for (Direction debug14 : Direction.values()) {
/* 204 */         debug8.setWithOffset((Vec3i)debug10, debug14);
/* 205 */         if (!debug3.contains(debug8)) {
/* 206 */           BlockState debug15 = debug1.getBlockState((BlockPos)debug8);
/* 207 */           if (debug15.hasProperty((Property)BlockStateProperties.DISTANCE)) {
/* 208 */             ((Set<BlockPos>)debug5.get(0)).add(debug8.immutable());
/* 209 */             setBlockKnownShape((LevelWriter)debug1, (BlockPos)debug8, (BlockState)debug15.setValue((Property)BlockStateProperties.DISTANCE, Integer.valueOf(1)));
/* 210 */             if (debug2.isInside((Vec3i)debug8)) {
/* 211 */               bitSetDiscreteVoxelShape.setFull(debug8.getX() - debug2.x0, debug8.getY() - debug2.y0, debug8.getZ() - debug2.z0, true, true);
/*     */             }
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 217 */     for (int debug9 = 1; debug9 < 6; debug9++) {
/* 218 */       Set<BlockPos> debug10 = debug5.get(debug9 - 1);
/* 219 */       Set<BlockPos> debug11 = debug5.get(debug9);
/* 220 */       for (BlockPos debug13 : debug10) {
/* 221 */         if (debug2.isInside((Vec3i)debug13)) {
/* 222 */           bitSetDiscreteVoxelShape.setFull(debug13.getX() - debug2.x0, debug13.getY() - debug2.y0, debug13.getZ() - debug2.z0, true, true);
/*     */         }
/* 224 */         for (Direction debug17 : Direction.values()) {
/* 225 */           debug8.setWithOffset((Vec3i)debug13, debug17);
/* 226 */           if (!debug10.contains(debug8) && !debug11.contains(debug8)) {
/*     */ 
/*     */             
/* 229 */             BlockState debug18 = debug1.getBlockState((BlockPos)debug8);
/* 230 */             if (debug18.hasProperty((Property)BlockStateProperties.DISTANCE)) {
/*     */ 
/*     */               
/* 233 */               int debug19 = ((Integer)debug18.getValue((Property)BlockStateProperties.DISTANCE)).intValue();
/* 234 */               if (debug19 > debug9 + 1)
/* 235 */               { BlockState debug20 = (BlockState)debug18.setValue((Property)BlockStateProperties.DISTANCE, Integer.valueOf(debug9 + 1));
/* 236 */                 setBlockKnownShape((LevelWriter)debug1, (BlockPos)debug8, debug20);
/* 237 */                 if (debug2.isInside((Vec3i)debug8)) {
/* 238 */                   bitSetDiscreteVoxelShape.setFull(debug8.getX() - debug2.x0, debug8.getY() - debug2.y0, debug8.getZ() - debug2.z0, true, true);
/*     */                 }
/* 240 */                 debug11.add(debug8.immutable()); } 
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/* 245 */     }  return (DiscreteVoxelShape)bitSetDiscreteVoxelShape;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\feature\TreeFeature.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */