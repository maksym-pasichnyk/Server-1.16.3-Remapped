/*    */ package net.minecraft.data.worldgen;
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import java.util.List;
/*    */ import net.minecraft.resources.ResourceLocation;
/*    */ import net.minecraft.world.level.levelgen.feature.structures.StructurePoolElement;
/*    */ import net.minecraft.world.level.levelgen.feature.structures.StructureTemplatePool;
/*    */ 
/*    */ public class BastionBridgePools {
/*    */   static {
/* 11 */     Pools.register(new StructureTemplatePool(new ResourceLocation("bastion/bridge/starting_pieces"), new ResourceLocation("empty"), 
/*    */ 
/*    */           
/* 14 */           (List)ImmutableList.of(
/* 15 */             Pair.of(StructurePoolElement.single("bastion/bridge/starting_pieces/entrance", ProcessorLists.ENTRANCE_REPLACEMENT), Integer.valueOf(1)), 
/* 16 */             Pair.of(StructurePoolElement.single("bastion/bridge/starting_pieces/entrance_face", ProcessorLists.BASTION_GENERIC_DEGRADATION), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 21 */     Pools.register(new StructureTemplatePool(new ResourceLocation("bastion/bridge/bridge_pieces"), new ResourceLocation("empty"), 
/*    */ 
/*    */           
/* 24 */           (List)ImmutableList.of(
/* 25 */             Pair.of(StructurePoolElement.single("bastion/bridge/bridge_pieces/bridge", ProcessorLists.BRIDGE), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 30 */     Pools.register(new StructureTemplatePool(new ResourceLocation("bastion/bridge/legs"), new ResourceLocation("empty"), 
/*    */ 
/*    */           
/* 33 */           (List)ImmutableList.of(
/* 34 */             Pair.of(StructurePoolElement.single("bastion/bridge/legs/leg_0", ProcessorLists.BASTION_GENERIC_DEGRADATION), Integer.valueOf(1)), 
/* 35 */             Pair.of(StructurePoolElement.single("bastion/bridge/legs/leg_1", ProcessorLists.BASTION_GENERIC_DEGRADATION), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 40 */     Pools.register(new StructureTemplatePool(new ResourceLocation("bastion/bridge/walls"), new ResourceLocation("empty"), 
/*    */ 
/*    */           
/* 43 */           (List)ImmutableList.of(
/* 44 */             Pair.of(StructurePoolElement.single("bastion/bridge/walls/wall_base_0", ProcessorLists.RAMPART_DEGRADATION), Integer.valueOf(1)), 
/* 45 */             Pair.of(StructurePoolElement.single("bastion/bridge/walls/wall_base_1", ProcessorLists.RAMPART_DEGRADATION), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 50 */     Pools.register(new StructureTemplatePool(new ResourceLocation("bastion/bridge/ramparts"), new ResourceLocation("empty"), 
/*    */ 
/*    */           
/* 53 */           (List)ImmutableList.of(
/* 54 */             Pair.of(StructurePoolElement.single("bastion/bridge/ramparts/rampart_0", ProcessorLists.RAMPART_DEGRADATION), Integer.valueOf(1)), 
/* 55 */             Pair.of(StructurePoolElement.single("bastion/bridge/ramparts/rampart_1", ProcessorLists.RAMPART_DEGRADATION), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 60 */     Pools.register(new StructureTemplatePool(new ResourceLocation("bastion/bridge/rampart_plates"), new ResourceLocation("empty"), 
/*    */ 
/*    */           
/* 63 */           (List)ImmutableList.of(
/* 64 */             Pair.of(StructurePoolElement.single("bastion/bridge/rampart_plates/plate_0", ProcessorLists.RAMPART_DEGRADATION), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 69 */     Pools.register(new StructureTemplatePool(new ResourceLocation("bastion/bridge/connectors"), new ResourceLocation("empty"), 
/*    */ 
/*    */           
/* 72 */           (List)ImmutableList.of(
/* 73 */             Pair.of(StructurePoolElement.single("bastion/bridge/connectors/back_bridge_top", ProcessorLists.BASTION_GENERIC_DEGRADATION), Integer.valueOf(1)), 
/* 74 */             Pair.of(StructurePoolElement.single("bastion/bridge/connectors/back_bridge_bottom", ProcessorLists.BASTION_GENERIC_DEGRADATION), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*    */   }
/*    */   
/*    */   public static void bootstrap() {}
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\data\worldgen\BastionBridgePools.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */