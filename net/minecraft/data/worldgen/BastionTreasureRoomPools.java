/*     */ package net.minecraft.data.worldgen;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import java.util.List;
/*     */ import net.minecraft.resources.ResourceLocation;
/*     */ import net.minecraft.world.level.levelgen.feature.structures.StructurePoolElement;
/*     */ import net.minecraft.world.level.levelgen.feature.structures.StructureTemplatePool;
/*     */ 
/*     */ public class BastionTreasureRoomPools {
/*     */   static {
/*  11 */     Pools.register(new StructureTemplatePool(new ResourceLocation("bastion/treasure/bases"), new ResourceLocation("empty"), 
/*     */ 
/*     */           
/*  14 */           (List)ImmutableList.of(
/*  15 */             Pair.of(StructurePoolElement.single("bastion/treasure/bases/lava_basin", ProcessorLists.TREASURE_ROOMS), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  20 */     Pools.register(new StructureTemplatePool(new ResourceLocation("bastion/treasure/stairs"), new ResourceLocation("empty"), 
/*     */ 
/*     */           
/*  23 */           (List)ImmutableList.of(
/*  24 */             Pair.of(StructurePoolElement.single("bastion/treasure/stairs/lower_stairs", ProcessorLists.TREASURE_ROOMS), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  29 */     Pools.register(new StructureTemplatePool(new ResourceLocation("bastion/treasure/bases/centers"), new ResourceLocation("empty"), 
/*     */ 
/*     */           
/*  32 */           (List)ImmutableList.of(
/*  33 */             Pair.of(StructurePoolElement.single("bastion/treasure/bases/centers/center_0", ProcessorLists.TREASURE_ROOMS), Integer.valueOf(1)), 
/*  34 */             Pair.of(StructurePoolElement.single("bastion/treasure/bases/centers/center_1", ProcessorLists.TREASURE_ROOMS), Integer.valueOf(1)), 
/*  35 */             Pair.of(StructurePoolElement.single("bastion/treasure/bases/centers/center_2", ProcessorLists.TREASURE_ROOMS), Integer.valueOf(1)), 
/*  36 */             Pair.of(StructurePoolElement.single("bastion/treasure/bases/centers/center_3", ProcessorLists.TREASURE_ROOMS), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  41 */     Pools.register(new StructureTemplatePool(new ResourceLocation("bastion/treasure/brains"), new ResourceLocation("empty"), 
/*     */ 
/*     */           
/*  44 */           (List)ImmutableList.of(
/*  45 */             Pair.of(StructurePoolElement.single("bastion/treasure/brains/center_brain", ProcessorLists.TREASURE_ROOMS), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  50 */     Pools.register(new StructureTemplatePool(new ResourceLocation("bastion/treasure/walls"), new ResourceLocation("empty"), 
/*     */ 
/*     */           
/*  53 */           (List)ImmutableList.of(
/*  54 */             Pair.of(StructurePoolElement.single("bastion/treasure/walls/lava_wall", ProcessorLists.TREASURE_ROOMS), Integer.valueOf(1)), 
/*  55 */             Pair.of(StructurePoolElement.single("bastion/treasure/walls/entrance_wall", ProcessorLists.HIGH_WALL), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  60 */     Pools.register(new StructureTemplatePool(new ResourceLocation("bastion/treasure/walls/outer"), new ResourceLocation("empty"), 
/*     */ 
/*     */           
/*  63 */           (List)ImmutableList.of(
/*  64 */             Pair.of(StructurePoolElement.single("bastion/treasure/walls/outer/top_corner", ProcessorLists.HIGH_WALL), Integer.valueOf(1)), 
/*  65 */             Pair.of(StructurePoolElement.single("bastion/treasure/walls/outer/mid_corner", ProcessorLists.HIGH_WALL), Integer.valueOf(1)), 
/*  66 */             Pair.of(StructurePoolElement.single("bastion/treasure/walls/outer/bottom_corner", ProcessorLists.HIGH_WALL), Integer.valueOf(1)), 
/*  67 */             Pair.of(StructurePoolElement.single("bastion/treasure/walls/outer/outer_wall", ProcessorLists.HIGH_WALL), Integer.valueOf(1)), 
/*  68 */             Pair.of(StructurePoolElement.single("bastion/treasure/walls/outer/medium_outer_wall", ProcessorLists.HIGH_WALL), Integer.valueOf(1)), 
/*  69 */             Pair.of(StructurePoolElement.single("bastion/treasure/walls/outer/tall_outer_wall", ProcessorLists.HIGH_WALL), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  74 */     Pools.register(new StructureTemplatePool(new ResourceLocation("bastion/treasure/walls/bottom"), new ResourceLocation("empty"), 
/*     */ 
/*     */           
/*  77 */           (List)ImmutableList.of(
/*  78 */             Pair.of(StructurePoolElement.single("bastion/treasure/walls/bottom/wall_0", ProcessorLists.TREASURE_ROOMS), Integer.valueOf(1)), 
/*  79 */             Pair.of(StructurePoolElement.single("bastion/treasure/walls/bottom/wall_1", ProcessorLists.TREASURE_ROOMS), Integer.valueOf(1)), 
/*  80 */             Pair.of(StructurePoolElement.single("bastion/treasure/walls/bottom/wall_2", ProcessorLists.TREASURE_ROOMS), Integer.valueOf(1)), 
/*  81 */             Pair.of(StructurePoolElement.single("bastion/treasure/walls/bottom/wall_3", ProcessorLists.TREASURE_ROOMS), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  86 */     Pools.register(new StructureTemplatePool(new ResourceLocation("bastion/treasure/walls/mid"), new ResourceLocation("empty"), 
/*     */ 
/*     */           
/*  89 */           (List)ImmutableList.of(
/*  90 */             Pair.of(StructurePoolElement.single("bastion/treasure/walls/mid/wall_0", ProcessorLists.TREASURE_ROOMS), Integer.valueOf(1)), 
/*  91 */             Pair.of(StructurePoolElement.single("bastion/treasure/walls/mid/wall_1", ProcessorLists.TREASURE_ROOMS), Integer.valueOf(1)), 
/*  92 */             Pair.of(StructurePoolElement.single("bastion/treasure/walls/mid/wall_2", ProcessorLists.TREASURE_ROOMS), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  97 */     Pools.register(new StructureTemplatePool(new ResourceLocation("bastion/treasure/walls/top"), new ResourceLocation("empty"), 
/*     */ 
/*     */           
/* 100 */           (List)ImmutableList.of(
/* 101 */             Pair.of(StructurePoolElement.single("bastion/treasure/walls/top/main_entrance", ProcessorLists.TREASURE_ROOMS), Integer.valueOf(1)), 
/* 102 */             Pair.of(StructurePoolElement.single("bastion/treasure/walls/top/wall_0", ProcessorLists.TREASURE_ROOMS), Integer.valueOf(1)), 
/* 103 */             Pair.of(StructurePoolElement.single("bastion/treasure/walls/top/wall_1", ProcessorLists.TREASURE_ROOMS), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 108 */     Pools.register(new StructureTemplatePool(new ResourceLocation("bastion/treasure/connectors"), new ResourceLocation("empty"), 
/*     */ 
/*     */           
/* 111 */           (List)ImmutableList.of(
/* 112 */             Pair.of(StructurePoolElement.single("bastion/treasure/connectors/center_to_wall_middle", ProcessorLists.TREASURE_ROOMS), Integer.valueOf(1)), 
/* 113 */             Pair.of(StructurePoolElement.single("bastion/treasure/connectors/center_to_wall_top", ProcessorLists.TREASURE_ROOMS), Integer.valueOf(1)), 
/* 114 */             Pair.of(StructurePoolElement.single("bastion/treasure/connectors/center_to_wall_top_entrance", ProcessorLists.TREASURE_ROOMS), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 119 */     Pools.register(new StructureTemplatePool(new ResourceLocation("bastion/treasure/entrances"), new ResourceLocation("empty"), 
/*     */ 
/*     */           
/* 122 */           (List)ImmutableList.of(
/* 123 */             Pair.of(StructurePoolElement.single("bastion/treasure/entrances/entrance_0", ProcessorLists.TREASURE_ROOMS), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 128 */     Pools.register(new StructureTemplatePool(new ResourceLocation("bastion/treasure/ramparts"), new ResourceLocation("empty"), 
/*     */ 
/*     */           
/* 131 */           (List)ImmutableList.of(
/* 132 */             Pair.of(StructurePoolElement.single("bastion/treasure/ramparts/mid_wall_main", ProcessorLists.TREASURE_ROOMS), Integer.valueOf(1)), 
/* 133 */             Pair.of(StructurePoolElement.single("bastion/treasure/ramparts/mid_wall_side", ProcessorLists.TREASURE_ROOMS), Integer.valueOf(1)), 
/* 134 */             Pair.of(StructurePoolElement.single("bastion/treasure/ramparts/bottom_wall_0", ProcessorLists.BOTTOM_RAMPART), Integer.valueOf(1)), 
/* 135 */             Pair.of(StructurePoolElement.single("bastion/treasure/ramparts/top_wall", ProcessorLists.HIGH_RAMPART), Integer.valueOf(1)), 
/* 136 */             Pair.of(StructurePoolElement.single("bastion/treasure/ramparts/lava_basin_side", ProcessorLists.TREASURE_ROOMS), Integer.valueOf(1)), 
/* 137 */             Pair.of(StructurePoolElement.single("bastion/treasure/ramparts/lava_basin_main", ProcessorLists.TREASURE_ROOMS), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 142 */     Pools.register(new StructureTemplatePool(new ResourceLocation("bastion/treasure/corners/bottom"), new ResourceLocation("empty"), 
/*     */ 
/*     */           
/* 145 */           (List)ImmutableList.of(
/* 146 */             Pair.of(StructurePoolElement.single("bastion/treasure/corners/bottom/corner_0", ProcessorLists.TREASURE_ROOMS), Integer.valueOf(1)), 
/* 147 */             Pair.of(StructurePoolElement.single("bastion/treasure/corners/bottom/corner_1", ProcessorLists.TREASURE_ROOMS), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 152 */     Pools.register(new StructureTemplatePool(new ResourceLocation("bastion/treasure/corners/edges"), new ResourceLocation("empty"), 
/*     */ 
/*     */           
/* 155 */           (List)ImmutableList.of(
/* 156 */             Pair.of(StructurePoolElement.single("bastion/treasure/corners/edges/bottom", ProcessorLists.HIGH_WALL), Integer.valueOf(1)), 
/* 157 */             Pair.of(StructurePoolElement.single("bastion/treasure/corners/edges/middle", ProcessorLists.HIGH_WALL), Integer.valueOf(1)), 
/* 158 */             Pair.of(StructurePoolElement.single("bastion/treasure/corners/edges/top", ProcessorLists.HIGH_WALL), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 163 */     Pools.register(new StructureTemplatePool(new ResourceLocation("bastion/treasure/corners/middle"), new ResourceLocation("empty"), 
/*     */ 
/*     */           
/* 166 */           (List)ImmutableList.of(
/* 167 */             Pair.of(StructurePoolElement.single("bastion/treasure/corners/middle/corner_0", ProcessorLists.TREASURE_ROOMS), Integer.valueOf(1)), 
/* 168 */             Pair.of(StructurePoolElement.single("bastion/treasure/corners/middle/corner_1", ProcessorLists.TREASURE_ROOMS), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 173 */     Pools.register(new StructureTemplatePool(new ResourceLocation("bastion/treasure/corners/top"), new ResourceLocation("empty"), 
/*     */ 
/*     */           
/* 176 */           (List)ImmutableList.of(
/* 177 */             Pair.of(StructurePoolElement.single("bastion/treasure/corners/top/corner_0", ProcessorLists.TREASURE_ROOMS), Integer.valueOf(1)), 
/* 178 */             Pair.of(StructurePoolElement.single("bastion/treasure/corners/top/corner_1", ProcessorLists.TREASURE_ROOMS), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 183 */     Pools.register(new StructureTemplatePool(new ResourceLocation("bastion/treasure/extensions/large_pool"), new ResourceLocation("empty"), 
/*     */ 
/*     */           
/* 186 */           (List)ImmutableList.of(
/* 187 */             Pair.of(StructurePoolElement.single("bastion/treasure/extensions/empty", ProcessorLists.TREASURE_ROOMS), Integer.valueOf(1)), 
/* 188 */             Pair.of(StructurePoolElement.single("bastion/treasure/extensions/empty", ProcessorLists.TREASURE_ROOMS), Integer.valueOf(1)), 
/* 189 */             Pair.of(StructurePoolElement.single("bastion/treasure/extensions/fire_room", ProcessorLists.TREASURE_ROOMS), Integer.valueOf(1)), 
/* 190 */             Pair.of(StructurePoolElement.single("bastion/treasure/extensions/large_bridge_0", ProcessorLists.TREASURE_ROOMS), Integer.valueOf(1)), 
/* 191 */             Pair.of(StructurePoolElement.single("bastion/treasure/extensions/large_bridge_1", ProcessorLists.TREASURE_ROOMS), Integer.valueOf(1)), 
/* 192 */             Pair.of(StructurePoolElement.single("bastion/treasure/extensions/large_bridge_2", ProcessorLists.TREASURE_ROOMS), Integer.valueOf(1)), 
/* 193 */             Pair.of(StructurePoolElement.single("bastion/treasure/extensions/large_bridge_3", ProcessorLists.TREASURE_ROOMS), Integer.valueOf(1)), 
/* 194 */             Pair.of(StructurePoolElement.single("bastion/treasure/extensions/roofed_bridge", ProcessorLists.TREASURE_ROOMS), Integer.valueOf(1)), 
/* 195 */             Pair.of(StructurePoolElement.single("bastion/treasure/extensions/empty", ProcessorLists.TREASURE_ROOMS), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 200 */     Pools.register(new StructureTemplatePool(new ResourceLocation("bastion/treasure/extensions/small_pool"), new ResourceLocation("empty"), 
/*     */ 
/*     */           
/* 203 */           (List)ImmutableList.of(
/* 204 */             Pair.of(StructurePoolElement.single("bastion/treasure/extensions/empty", ProcessorLists.TREASURE_ROOMS), Integer.valueOf(1)), 
/* 205 */             Pair.of(StructurePoolElement.single("bastion/treasure/extensions/fire_room", ProcessorLists.TREASURE_ROOMS), Integer.valueOf(1)), 
/* 206 */             Pair.of(StructurePoolElement.single("bastion/treasure/extensions/empty", ProcessorLists.TREASURE_ROOMS), Integer.valueOf(1)), 
/* 207 */             Pair.of(StructurePoolElement.single("bastion/treasure/extensions/small_bridge_0", ProcessorLists.TREASURE_ROOMS), Integer.valueOf(1)), 
/* 208 */             Pair.of(StructurePoolElement.single("bastion/treasure/extensions/small_bridge_1", ProcessorLists.TREASURE_ROOMS), Integer.valueOf(1)), 
/* 209 */             Pair.of(StructurePoolElement.single("bastion/treasure/extensions/small_bridge_2", ProcessorLists.TREASURE_ROOMS), Integer.valueOf(1)), 
/* 210 */             Pair.of(StructurePoolElement.single("bastion/treasure/extensions/small_bridge_3", ProcessorLists.TREASURE_ROOMS), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 215 */     Pools.register(new StructureTemplatePool(new ResourceLocation("bastion/treasure/extensions/houses"), new ResourceLocation("empty"), 
/*     */ 
/*     */           
/* 218 */           (List)ImmutableList.of(
/* 219 */             Pair.of(StructurePoolElement.single("bastion/treasure/extensions/house_0", ProcessorLists.TREASURE_ROOMS), Integer.valueOf(1)), 
/* 220 */             Pair.of(StructurePoolElement.single("bastion/treasure/extensions/house_1", ProcessorLists.TREASURE_ROOMS), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 225 */     Pools.register(new StructureTemplatePool(new ResourceLocation("bastion/treasure/roofs"), new ResourceLocation("empty"), 
/*     */ 
/*     */           
/* 228 */           (List)ImmutableList.of(
/* 229 */             Pair.of(StructurePoolElement.single("bastion/treasure/roofs/wall_roof", ProcessorLists.ROOF), Integer.valueOf(1)), 
/* 230 */             Pair.of(StructurePoolElement.single("bastion/treasure/roofs/corner_roof", ProcessorLists.ROOF), Integer.valueOf(1)), 
/* 231 */             Pair.of(StructurePoolElement.single("bastion/treasure/roofs/center_roof", ProcessorLists.ROOF), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */   }
/*     */   
/*     */   public static void bootstrap() {}
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\data\worldgen\BastionTreasureRoomPools.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */