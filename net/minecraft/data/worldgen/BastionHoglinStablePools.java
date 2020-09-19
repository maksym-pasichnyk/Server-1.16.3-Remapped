/*     */ package net.minecraft.data.worldgen;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import java.util.List;
/*     */ import net.minecraft.resources.ResourceLocation;
/*     */ import net.minecraft.world.level.levelgen.feature.structures.StructurePoolElement;
/*     */ import net.minecraft.world.level.levelgen.feature.structures.StructureTemplatePool;
/*     */ 
/*     */ public class BastionHoglinStablePools {
/*     */   static {
/*  11 */     Pools.register(new StructureTemplatePool(new ResourceLocation("bastion/hoglin_stable/starting_pieces"), new ResourceLocation("empty"), 
/*     */ 
/*     */           
/*  14 */           (List)ImmutableList.of(
/*  15 */             Pair.of(StructurePoolElement.single("bastion/hoglin_stable/starting_pieces/starting_stairs_0", ProcessorLists.STABLE_DEGRADATION), Integer.valueOf(1)), 
/*  16 */             Pair.of(StructurePoolElement.single("bastion/hoglin_stable/starting_pieces/starting_stairs_1", ProcessorLists.STABLE_DEGRADATION), Integer.valueOf(1)), 
/*  17 */             Pair.of(StructurePoolElement.single("bastion/hoglin_stable/starting_pieces/starting_stairs_2", ProcessorLists.STABLE_DEGRADATION), Integer.valueOf(1)), 
/*  18 */             Pair.of(StructurePoolElement.single("bastion/hoglin_stable/starting_pieces/starting_stairs_3", ProcessorLists.STABLE_DEGRADATION), Integer.valueOf(1)), 
/*  19 */             Pair.of(StructurePoolElement.single("bastion/hoglin_stable/starting_pieces/starting_stairs_4", ProcessorLists.STABLE_DEGRADATION), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  24 */     Pools.register(new StructureTemplatePool(new ResourceLocation("bastion/hoglin_stable/mirrored_starting_pieces"), new ResourceLocation("empty"), 
/*     */ 
/*     */           
/*  27 */           (List)ImmutableList.of(
/*  28 */             Pair.of(StructurePoolElement.single("bastion/hoglin_stable/starting_pieces/stairs_0_mirrored", ProcessorLists.STABLE_DEGRADATION), Integer.valueOf(1)), 
/*  29 */             Pair.of(StructurePoolElement.single("bastion/hoglin_stable/starting_pieces/stairs_1_mirrored", ProcessorLists.STABLE_DEGRADATION), Integer.valueOf(1)), 
/*  30 */             Pair.of(StructurePoolElement.single("bastion/hoglin_stable/starting_pieces/stairs_2_mirrored", ProcessorLists.STABLE_DEGRADATION), Integer.valueOf(1)), 
/*  31 */             Pair.of(StructurePoolElement.single("bastion/hoglin_stable/starting_pieces/stairs_3_mirrored", ProcessorLists.STABLE_DEGRADATION), Integer.valueOf(1)), 
/*  32 */             Pair.of(StructurePoolElement.single("bastion/hoglin_stable/starting_pieces/stairs_4_mirrored", ProcessorLists.STABLE_DEGRADATION), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  37 */     Pools.register(new StructureTemplatePool(new ResourceLocation("bastion/hoglin_stable/wall_bases"), new ResourceLocation("empty"), 
/*     */ 
/*     */           
/*  40 */           (List)ImmutableList.of(
/*  41 */             Pair.of(StructurePoolElement.single("bastion/hoglin_stable/walls/wall_base", ProcessorLists.STABLE_DEGRADATION), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  46 */     Pools.register(new StructureTemplatePool(new ResourceLocation("bastion/hoglin_stable/walls"), new ResourceLocation("empty"), 
/*     */ 
/*     */           
/*  49 */           (List)ImmutableList.of(
/*  50 */             Pair.of(StructurePoolElement.single("bastion/hoglin_stable/walls/side_wall_0", ProcessorLists.SIDE_WALL_DEGRADATION), Integer.valueOf(1)), 
/*  51 */             Pair.of(StructurePoolElement.single("bastion/hoglin_stable/walls/side_wall_1", ProcessorLists.SIDE_WALL_DEGRADATION), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  56 */     Pools.register(new StructureTemplatePool(new ResourceLocation("bastion/hoglin_stable/stairs"), new ResourceLocation("empty"), 
/*     */ 
/*     */           
/*  59 */           (List)ImmutableList.of(
/*  60 */             Pair.of(StructurePoolElement.single("bastion/hoglin_stable/stairs/stairs_1_0", ProcessorLists.STABLE_DEGRADATION), Integer.valueOf(1)), 
/*  61 */             Pair.of(StructurePoolElement.single("bastion/hoglin_stable/stairs/stairs_1_1", ProcessorLists.STABLE_DEGRADATION), Integer.valueOf(1)), 
/*  62 */             Pair.of(StructurePoolElement.single("bastion/hoglin_stable/stairs/stairs_1_2", ProcessorLists.STABLE_DEGRADATION), Integer.valueOf(1)), 
/*  63 */             Pair.of(StructurePoolElement.single("bastion/hoglin_stable/stairs/stairs_1_3", ProcessorLists.STABLE_DEGRADATION), Integer.valueOf(1)), 
/*  64 */             Pair.of(StructurePoolElement.single("bastion/hoglin_stable/stairs/stairs_1_4", ProcessorLists.STABLE_DEGRADATION), Integer.valueOf(1)), 
/*  65 */             Pair.of(StructurePoolElement.single("bastion/hoglin_stable/stairs/stairs_2_0", ProcessorLists.STABLE_DEGRADATION), Integer.valueOf(1)), 
/*  66 */             Pair.of(StructurePoolElement.single("bastion/hoglin_stable/stairs/stairs_2_1", ProcessorLists.STABLE_DEGRADATION), Integer.valueOf(1)), 
/*  67 */             Pair.of(StructurePoolElement.single("bastion/hoglin_stable/stairs/stairs_2_2", ProcessorLists.STABLE_DEGRADATION), Integer.valueOf(1)), 
/*  68 */             Pair.of(StructurePoolElement.single("bastion/hoglin_stable/stairs/stairs_2_3", ProcessorLists.STABLE_DEGRADATION), Integer.valueOf(1)), 
/*  69 */             Pair.of(StructurePoolElement.single("bastion/hoglin_stable/stairs/stairs_2_4", ProcessorLists.STABLE_DEGRADATION), Integer.valueOf(1)), 
/*  70 */             Pair.of(StructurePoolElement.single("bastion/hoglin_stable/stairs/stairs_3_0", ProcessorLists.STABLE_DEGRADATION), Integer.valueOf(1)), 
/*  71 */             Pair.of(StructurePoolElement.single("bastion/hoglin_stable/stairs/stairs_3_1", ProcessorLists.STABLE_DEGRADATION), Integer.valueOf(1)), (Object[])new Pair[] {
/*  72 */               Pair.of(StructurePoolElement.single("bastion/hoglin_stable/stairs/stairs_3_2", ProcessorLists.STABLE_DEGRADATION), Integer.valueOf(1)), 
/*  73 */               Pair.of(StructurePoolElement.single("bastion/hoglin_stable/stairs/stairs_3_3", ProcessorLists.STABLE_DEGRADATION), Integer.valueOf(1)), 
/*  74 */               Pair.of(StructurePoolElement.single("bastion/hoglin_stable/stairs/stairs_3_4", ProcessorLists.STABLE_DEGRADATION), Integer.valueOf(1))
/*     */             }), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */     
/*  79 */     Pools.register(new StructureTemplatePool(new ResourceLocation("bastion/hoglin_stable/small_stables/inner"), new ResourceLocation("empty"), 
/*     */ 
/*     */           
/*  82 */           (List)ImmutableList.of(
/*  83 */             Pair.of(StructurePoolElement.single("bastion/hoglin_stable/small_stables/inner_0", ProcessorLists.STABLE_DEGRADATION), Integer.valueOf(1)), 
/*  84 */             Pair.of(StructurePoolElement.single("bastion/hoglin_stable/small_stables/inner_1", ProcessorLists.STABLE_DEGRADATION), Integer.valueOf(1)), 
/*  85 */             Pair.of(StructurePoolElement.single("bastion/hoglin_stable/small_stables/inner_2", ProcessorLists.STABLE_DEGRADATION), Integer.valueOf(1)), 
/*  86 */             Pair.of(StructurePoolElement.single("bastion/hoglin_stable/small_stables/inner_3", ProcessorLists.STABLE_DEGRADATION), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  91 */     Pools.register(new StructureTemplatePool(new ResourceLocation("bastion/hoglin_stable/small_stables/outer"), new ResourceLocation("empty"), 
/*     */ 
/*     */           
/*  94 */           (List)ImmutableList.of(
/*  95 */             Pair.of(StructurePoolElement.single("bastion/hoglin_stable/small_stables/outer_0", ProcessorLists.STABLE_DEGRADATION), Integer.valueOf(1)), 
/*  96 */             Pair.of(StructurePoolElement.single("bastion/hoglin_stable/small_stables/outer_1", ProcessorLists.STABLE_DEGRADATION), Integer.valueOf(1)), 
/*  97 */             Pair.of(StructurePoolElement.single("bastion/hoglin_stable/small_stables/outer_2", ProcessorLists.STABLE_DEGRADATION), Integer.valueOf(1)), 
/*  98 */             Pair.of(StructurePoolElement.single("bastion/hoglin_stable/small_stables/outer_3", ProcessorLists.STABLE_DEGRADATION), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 103 */     Pools.register(new StructureTemplatePool(new ResourceLocation("bastion/hoglin_stable/large_stables/inner"), new ResourceLocation("empty"), 
/*     */ 
/*     */           
/* 106 */           (List)ImmutableList.of(
/* 107 */             Pair.of(StructurePoolElement.single("bastion/hoglin_stable/large_stables/inner_0", ProcessorLists.STABLE_DEGRADATION), Integer.valueOf(1)), 
/* 108 */             Pair.of(StructurePoolElement.single("bastion/hoglin_stable/large_stables/inner_1", ProcessorLists.STABLE_DEGRADATION), Integer.valueOf(1)), 
/* 109 */             Pair.of(StructurePoolElement.single("bastion/hoglin_stable/large_stables/inner_2", ProcessorLists.STABLE_DEGRADATION), Integer.valueOf(1)), 
/* 110 */             Pair.of(StructurePoolElement.single("bastion/hoglin_stable/large_stables/inner_3", ProcessorLists.STABLE_DEGRADATION), Integer.valueOf(1)), 
/* 111 */             Pair.of(StructurePoolElement.single("bastion/hoglin_stable/large_stables/inner_4", ProcessorLists.STABLE_DEGRADATION), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 116 */     Pools.register(new StructureTemplatePool(new ResourceLocation("bastion/hoglin_stable/large_stables/outer"), new ResourceLocation("empty"), 
/*     */ 
/*     */           
/* 119 */           (List)ImmutableList.of(
/* 120 */             Pair.of(StructurePoolElement.single("bastion/hoglin_stable/large_stables/outer_0", ProcessorLists.STABLE_DEGRADATION), Integer.valueOf(1)), 
/* 121 */             Pair.of(StructurePoolElement.single("bastion/hoglin_stable/large_stables/outer_1", ProcessorLists.STABLE_DEGRADATION), Integer.valueOf(1)), 
/* 122 */             Pair.of(StructurePoolElement.single("bastion/hoglin_stable/large_stables/outer_2", ProcessorLists.STABLE_DEGRADATION), Integer.valueOf(1)), 
/* 123 */             Pair.of(StructurePoolElement.single("bastion/hoglin_stable/large_stables/outer_3", ProcessorLists.STABLE_DEGRADATION), Integer.valueOf(1)), 
/* 124 */             Pair.of(StructurePoolElement.single("bastion/hoglin_stable/large_stables/outer_4", ProcessorLists.STABLE_DEGRADATION), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 129 */     Pools.register(new StructureTemplatePool(new ResourceLocation("bastion/hoglin_stable/posts"), new ResourceLocation("empty"), 
/*     */ 
/*     */           
/* 132 */           (List)ImmutableList.of(
/* 133 */             Pair.of(StructurePoolElement.single("bastion/hoglin_stable/posts/stair_post", ProcessorLists.STABLE_DEGRADATION), Integer.valueOf(1)), 
/* 134 */             Pair.of(StructurePoolElement.single("bastion/hoglin_stable/posts/end_post", ProcessorLists.STABLE_DEGRADATION), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 139 */     Pools.register(new StructureTemplatePool(new ResourceLocation("bastion/hoglin_stable/ramparts"), new ResourceLocation("empty"), 
/*     */ 
/*     */           
/* 142 */           (List)ImmutableList.of(
/* 143 */             Pair.of(StructurePoolElement.single("bastion/hoglin_stable/ramparts/ramparts_1", ProcessorLists.STABLE_DEGRADATION), Integer.valueOf(1)), 
/* 144 */             Pair.of(StructurePoolElement.single("bastion/hoglin_stable/ramparts/ramparts_2", ProcessorLists.STABLE_DEGRADATION), Integer.valueOf(1)), 
/* 145 */             Pair.of(StructurePoolElement.single("bastion/hoglin_stable/ramparts/ramparts_3", ProcessorLists.STABLE_DEGRADATION), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 150 */     Pools.register(new StructureTemplatePool(new ResourceLocation("bastion/hoglin_stable/rampart_plates"), new ResourceLocation("empty"), 
/*     */ 
/*     */           
/* 153 */           (List)ImmutableList.of(
/* 154 */             Pair.of(StructurePoolElement.single("bastion/hoglin_stable/rampart_plates/rampart_plate_1", ProcessorLists.STABLE_DEGRADATION), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 159 */     Pools.register(new StructureTemplatePool(new ResourceLocation("bastion/hoglin_stable/connectors"), new ResourceLocation("empty"), 
/*     */ 
/*     */           
/* 162 */           (List)ImmutableList.of(
/* 163 */             Pair.of(StructurePoolElement.single("bastion/hoglin_stable/connectors/end_post_connector", ProcessorLists.STABLE_DEGRADATION), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */   }
/*     */   
/*     */   public static void bootstrap() {}
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\data\worldgen\BastionHoglinStablePools.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */