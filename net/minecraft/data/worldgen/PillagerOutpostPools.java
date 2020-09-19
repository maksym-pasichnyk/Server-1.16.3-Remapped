/*    */ package net.minecraft.data.worldgen;
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import java.util.List;
/*    */ import net.minecraft.resources.ResourceLocation;
/*    */ import net.minecraft.world.level.levelgen.feature.structures.StructurePoolElement;
/*    */ import net.minecraft.world.level.levelgen.feature.structures.StructureTemplatePool;
/*    */ 
/*    */ public class PillagerOutpostPools {
/* 10 */   public static final StructureTemplatePool START = Pools.register(new StructureTemplatePool(new ResourceLocation("pillager_outpost/base_plates"), new ResourceLocation("empty"), 
/*    */ 
/*    */         
/* 13 */         (List)ImmutableList.of(
/* 14 */           Pair.of(StructurePoolElement.legacy("pillager_outpost/base_plate"), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static {
/* 20 */     Pools.register(new StructureTemplatePool(new ResourceLocation("pillager_outpost/towers"), new ResourceLocation("empty"), 
/*    */ 
/*    */           
/* 23 */           (List)ImmutableList.of(
/* 24 */             Pair.of(StructurePoolElement.list((List)ImmutableList.of(
/* 25 */                   StructurePoolElement.legacy("pillager_outpost/watchtower"), 
/* 26 */                   StructurePoolElement.legacy("pillager_outpost/watchtower_overgrown", ProcessorLists.OUTPOST_ROT))), 
/* 27 */               Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 32 */     Pools.register(new StructureTemplatePool(new ResourceLocation("pillager_outpost/feature_plates"), new ResourceLocation("empty"), 
/*    */ 
/*    */           
/* 35 */           (List)ImmutableList.of(
/* 36 */             Pair.of(StructurePoolElement.legacy("pillager_outpost/feature_plate"), Integer.valueOf(1))), StructureTemplatePool.Projection.TERRAIN_MATCHING));
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 41 */     Pools.register(new StructureTemplatePool(new ResourceLocation("pillager_outpost/features"), new ResourceLocation("empty"), 
/*    */ 
/*    */           
/* 44 */           (List)ImmutableList.of(
/* 45 */             Pair.of(StructurePoolElement.legacy("pillager_outpost/feature_cage1"), Integer.valueOf(1)), 
/* 46 */             Pair.of(StructurePoolElement.legacy("pillager_outpost/feature_cage2"), Integer.valueOf(1)), 
/* 47 */             Pair.of(StructurePoolElement.legacy("pillager_outpost/feature_logs"), Integer.valueOf(1)), 
/* 48 */             Pair.of(StructurePoolElement.legacy("pillager_outpost/feature_tent1"), Integer.valueOf(1)), 
/* 49 */             Pair.of(StructurePoolElement.legacy("pillager_outpost/feature_tent2"), Integer.valueOf(1)), 
/* 50 */             Pair.of(StructurePoolElement.legacy("pillager_outpost/feature_targets"), Integer.valueOf(1)), 
/* 51 */             Pair.of(StructurePoolElement.empty(), Integer.valueOf(6))), StructureTemplatePool.Projection.RIGID));
/*    */   }
/*    */   
/*    */   public static void bootstrap() {}
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\data\worldgen\PillagerOutpostPools.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */