/*     */ package net.minecraft.data.worldgen;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import java.util.List;
/*     */ import net.minecraft.resources.ResourceLocation;
/*     */ import net.minecraft.world.level.levelgen.feature.structures.StructurePoolElement;
/*     */ import net.minecraft.world.level.levelgen.feature.structures.StructureTemplatePool;
/*     */ 
/*     */ public class BastionHousingUnitsPools {
/*     */   static {
/*  11 */     Pools.register(new StructureTemplatePool(new ResourceLocation("bastion/units/center_pieces"), new ResourceLocation("empty"), 
/*     */ 
/*     */           
/*  14 */           (List)ImmutableList.of(
/*  15 */             Pair.of(StructurePoolElement.single("bastion/units/center_pieces/center_0", ProcessorLists.HOUSING), Integer.valueOf(1)), 
/*  16 */             Pair.of(StructurePoolElement.single("bastion/units/center_pieces/center_1", ProcessorLists.HOUSING), Integer.valueOf(1)), 
/*  17 */             Pair.of(StructurePoolElement.single("bastion/units/center_pieces/center_2", ProcessorLists.HOUSING), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  22 */     Pools.register(new StructureTemplatePool(new ResourceLocation("bastion/units/pathways"), new ResourceLocation("empty"), 
/*     */ 
/*     */           
/*  25 */           (List)ImmutableList.of(
/*  26 */             Pair.of(StructurePoolElement.single("bastion/units/pathways/pathway_0", ProcessorLists.HOUSING), Integer.valueOf(1)), 
/*  27 */             Pair.of(StructurePoolElement.single("bastion/units/pathways/pathway_wall_0", ProcessorLists.HOUSING), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  32 */     Pools.register(new StructureTemplatePool(new ResourceLocation("bastion/units/walls/wall_bases"), new ResourceLocation("empty"), 
/*     */ 
/*     */           
/*  35 */           (List)ImmutableList.of(
/*  36 */             Pair.of(StructurePoolElement.single("bastion/units/walls/wall_base", ProcessorLists.HOUSING), Integer.valueOf(1)), 
/*  37 */             Pair.of(StructurePoolElement.single("bastion/units/walls/connected_wall", ProcessorLists.HOUSING), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  42 */     Pools.register(new StructureTemplatePool(new ResourceLocation("bastion/units/stages/stage_0"), new ResourceLocation("empty"), 
/*     */ 
/*     */           
/*  45 */           (List)ImmutableList.of(
/*  46 */             Pair.of(StructurePoolElement.single("bastion/units/stages/stage_0_0", ProcessorLists.HOUSING), Integer.valueOf(1)), 
/*  47 */             Pair.of(StructurePoolElement.single("bastion/units/stages/stage_0_1", ProcessorLists.HOUSING), Integer.valueOf(1)), 
/*  48 */             Pair.of(StructurePoolElement.single("bastion/units/stages/stage_0_2", ProcessorLists.HOUSING), Integer.valueOf(1)), 
/*  49 */             Pair.of(StructurePoolElement.single("bastion/units/stages/stage_0_3", ProcessorLists.HOUSING), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  54 */     Pools.register(new StructureTemplatePool(new ResourceLocation("bastion/units/stages/stage_1"), new ResourceLocation("empty"), 
/*     */ 
/*     */           
/*  57 */           (List)ImmutableList.of(
/*  58 */             Pair.of(StructurePoolElement.single("bastion/units/stages/stage_1_0", ProcessorLists.HOUSING), Integer.valueOf(1)), 
/*  59 */             Pair.of(StructurePoolElement.single("bastion/units/stages/stage_1_1", ProcessorLists.HOUSING), Integer.valueOf(1)), 
/*  60 */             Pair.of(StructurePoolElement.single("bastion/units/stages/stage_1_2", ProcessorLists.HOUSING), Integer.valueOf(1)), 
/*  61 */             Pair.of(StructurePoolElement.single("bastion/units/stages/stage_1_3", ProcessorLists.HOUSING), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  67 */     Pools.register(new StructureTemplatePool(new ResourceLocation("bastion/units/stages/rot/stage_1"), new ResourceLocation("empty"), 
/*     */ 
/*     */           
/*  70 */           (List)ImmutableList.of(
/*  71 */             Pair.of(StructurePoolElement.single("bastion/units/stages/rot/stage_1_0", ProcessorLists.HOUSING), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  76 */     Pools.register(new StructureTemplatePool(new ResourceLocation("bastion/units/stages/stage_2"), new ResourceLocation("empty"), 
/*     */ 
/*     */           
/*  79 */           (List)ImmutableList.of(
/*  80 */             Pair.of(StructurePoolElement.single("bastion/units/stages/stage_2_0", ProcessorLists.HOUSING), Integer.valueOf(1)), 
/*  81 */             Pair.of(StructurePoolElement.single("bastion/units/stages/stage_2_1", ProcessorLists.HOUSING), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  86 */     Pools.register(new StructureTemplatePool(new ResourceLocation("bastion/units/stages/stage_3"), new ResourceLocation("empty"), 
/*     */ 
/*     */           
/*  89 */           (List)ImmutableList.of(
/*  90 */             Pair.of(StructurePoolElement.single("bastion/units/stages/stage_3_0", ProcessorLists.HOUSING), Integer.valueOf(1)), 
/*  91 */             Pair.of(StructurePoolElement.single("bastion/units/stages/stage_3_1", ProcessorLists.HOUSING), Integer.valueOf(1)), 
/*  92 */             Pair.of(StructurePoolElement.single("bastion/units/stages/stage_3_2", ProcessorLists.HOUSING), Integer.valueOf(1)), 
/*  93 */             Pair.of(StructurePoolElement.single("bastion/units/stages/stage_3_3", ProcessorLists.HOUSING), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  98 */     Pools.register(new StructureTemplatePool(new ResourceLocation("bastion/units/fillers/stage_0"), new ResourceLocation("empty"), 
/*     */ 
/*     */           
/* 101 */           (List)ImmutableList.of(
/* 102 */             Pair.of(StructurePoolElement.single("bastion/units/fillers/stage_0", ProcessorLists.HOUSING), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 107 */     Pools.register(new StructureTemplatePool(new ResourceLocation("bastion/units/edges"), new ResourceLocation("empty"), 
/*     */ 
/*     */           
/* 110 */           (List)ImmutableList.of(
/* 111 */             Pair.of(StructurePoolElement.single("bastion/units/edges/edge_0", ProcessorLists.HOUSING), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 116 */     Pools.register(new StructureTemplatePool(new ResourceLocation("bastion/units/wall_units"), new ResourceLocation("empty"), 
/*     */ 
/*     */           
/* 119 */           (List)ImmutableList.of(
/* 120 */             Pair.of(StructurePoolElement.single("bastion/units/wall_units/unit_0", ProcessorLists.HOUSING), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 125 */     Pools.register(new StructureTemplatePool(new ResourceLocation("bastion/units/edge_wall_units"), new ResourceLocation("empty"), 
/*     */ 
/*     */           
/* 128 */           (List)ImmutableList.of(
/* 129 */             Pair.of(StructurePoolElement.single("bastion/units/wall_units/edge_0_large", ProcessorLists.HOUSING), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 134 */     Pools.register(new StructureTemplatePool(new ResourceLocation("bastion/units/ramparts"), new ResourceLocation("empty"), 
/*     */ 
/*     */           
/* 137 */           (List)ImmutableList.of(
/* 138 */             Pair.of(StructurePoolElement.single("bastion/units/ramparts/ramparts_0", ProcessorLists.HOUSING), Integer.valueOf(1)), 
/* 139 */             Pair.of(StructurePoolElement.single("bastion/units/ramparts/ramparts_1", ProcessorLists.HOUSING), Integer.valueOf(1)), 
/* 140 */             Pair.of(StructurePoolElement.single("bastion/units/ramparts/ramparts_2", ProcessorLists.HOUSING), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 145 */     Pools.register(new StructureTemplatePool(new ResourceLocation("bastion/units/large_ramparts"), new ResourceLocation("empty"), 
/*     */ 
/*     */           
/* 148 */           (List)ImmutableList.of(
/* 149 */             Pair.of(StructurePoolElement.single("bastion/units/ramparts/ramparts_0", ProcessorLists.HOUSING), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 154 */     Pools.register(new StructureTemplatePool(new ResourceLocation("bastion/units/rampart_plates"), new ResourceLocation("empty"), 
/*     */ 
/*     */           
/* 157 */           (List)ImmutableList.of(
/* 158 */             Pair.of(StructurePoolElement.single("bastion/units/rampart_plates/plate_0", ProcessorLists.HOUSING), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */   }
/*     */   
/*     */   public static void bootstrap() {}
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\data\worldgen\BastionHousingUnitsPools.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */