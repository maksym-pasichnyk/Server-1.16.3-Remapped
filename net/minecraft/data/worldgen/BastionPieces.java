/*    */ package net.minecraft.data.worldgen;
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import java.util.List;
/*    */ import net.minecraft.resources.ResourceLocation;
/*    */ import net.minecraft.world.level.levelgen.feature.structures.StructurePoolElement;
/*    */ import net.minecraft.world.level.levelgen.feature.structures.StructureTemplatePool;
/*    */ 
/*    */ public class BastionPieces {
/* 10 */   public static final StructureTemplatePool START = Pools.register(new StructureTemplatePool(new ResourceLocation("bastion/starts"), new ResourceLocation("empty"), 
/*    */ 
/*    */         
/* 13 */         (List)ImmutableList.of(
/* 14 */           Pair.of(StructurePoolElement.single("bastion/units/air_base", ProcessorLists.BASTION_GENERIC_DEGRADATION), Integer.valueOf(1)), 
/* 15 */           Pair.of(StructurePoolElement.single("bastion/hoglin_stable/air_base", ProcessorLists.BASTION_GENERIC_DEGRADATION), Integer.valueOf(1)), 
/* 16 */           Pair.of(StructurePoolElement.single("bastion/treasure/big_air_full", ProcessorLists.BASTION_GENERIC_DEGRADATION), Integer.valueOf(1)), 
/* 17 */           Pair.of(StructurePoolElement.single("bastion/bridge/starting_pieces/entrance_base", ProcessorLists.BASTION_GENERIC_DEGRADATION), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static void bootstrap() {
/* 23 */     BastionHousingUnitsPools.bootstrap();
/* 24 */     BastionHoglinStablePools.bootstrap();
/* 25 */     BastionTreasureRoomPools.bootstrap();
/* 26 */     BastionBridgePools.bootstrap();
/* 27 */     BastionSharedPools.bootstrap();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\data\worldgen\BastionPieces.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */