/*    */ package net.minecraft.data.worldgen;
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import java.util.List;
/*    */ import net.minecraft.resources.ResourceLocation;
/*    */ import net.minecraft.world.level.levelgen.feature.structures.StructurePoolElement;
/*    */ import net.minecraft.world.level.levelgen.feature.structures.StructureTemplatePool;
/*    */ 
/*    */ public class BastionSharedPools {
/*    */   static {
/* 11 */     Pools.register(new StructureTemplatePool(new ResourceLocation("bastion/mobs/piglin"), new ResourceLocation("empty"), 
/*    */ 
/*    */           
/* 14 */           (List)ImmutableList.of(
/* 15 */             Pair.of(StructurePoolElement.single("bastion/mobs/melee_piglin"), Integer.valueOf(1)), 
/* 16 */             Pair.of(StructurePoolElement.single("bastion/mobs/sword_piglin"), Integer.valueOf(4)), 
/* 17 */             Pair.of(StructurePoolElement.single("bastion/mobs/crossbow_piglin"), Integer.valueOf(4)), 
/* 18 */             Pair.of(StructurePoolElement.single("bastion/mobs/empty"), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 23 */     Pools.register(new StructureTemplatePool(new ResourceLocation("bastion/mobs/hoglin"), new ResourceLocation("empty"), 
/*    */ 
/*    */           
/* 26 */           (List)ImmutableList.of(
/* 27 */             Pair.of(StructurePoolElement.single("bastion/mobs/hoglin"), Integer.valueOf(2)), 
/* 28 */             Pair.of(StructurePoolElement.single("bastion/mobs/empty"), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 33 */     Pools.register(new StructureTemplatePool(new ResourceLocation("bastion/blocks/gold"), new ResourceLocation("empty"), 
/*    */ 
/*    */           
/* 36 */           (List)ImmutableList.of(
/* 37 */             Pair.of(StructurePoolElement.single("bastion/blocks/air"), Integer.valueOf(3)), 
/* 38 */             Pair.of(StructurePoolElement.single("bastion/blocks/gold"), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 43 */     Pools.register(new StructureTemplatePool(new ResourceLocation("bastion/mobs/piglin_melee"), new ResourceLocation("empty"), 
/*    */ 
/*    */           
/* 46 */           (List)ImmutableList.of(
/* 47 */             Pair.of(StructurePoolElement.single("bastion/mobs/melee_piglin_always"), Integer.valueOf(1)), 
/* 48 */             Pair.of(StructurePoolElement.single("bastion/mobs/melee_piglin"), Integer.valueOf(5)), 
/* 49 */             Pair.of(StructurePoolElement.single("bastion/mobs/sword_piglin"), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*    */   }
/*    */   
/*    */   public static void bootstrap() {}
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\data\worldgen\BastionSharedPools.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */