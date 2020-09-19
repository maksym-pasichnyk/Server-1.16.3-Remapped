/*    */ package net.minecraft.data.worldgen;
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import java.util.List;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.data.BuiltinRegistries;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.resources.ResourceLocation;
/*    */ import net.minecraft.world.level.levelgen.feature.structures.StructureTemplatePool;
/*    */ 
/*    */ public class Pools {
/* 11 */   public static final ResourceKey<StructureTemplatePool> EMPTY = ResourceKey.create(Registry.TEMPLATE_POOL_REGISTRY, new ResourceLocation("empty"));
/*    */   
/* 13 */   private static final StructureTemplatePool BUILTIN_EMPTY = register(new StructureTemplatePool(EMPTY.location(), EMPTY.location(), (List)ImmutableList.of(), StructureTemplatePool.Projection.RIGID));
/*    */   
/*    */   static {
/* 16 */     bootstrap();
/*    */   }
/*    */   
/*    */   public static StructureTemplatePool register(StructureTemplatePool debug0) {
/* 20 */     return (StructureTemplatePool)BuiltinRegistries.register(BuiltinRegistries.TEMPLATE_POOL, debug0.getName(), debug0);
/*    */   }
/*    */   
/*    */   public static StructureTemplatePool bootstrap() {
/* 24 */     BastionPieces.bootstrap();
/* 25 */     PillagerOutpostPools.bootstrap();
/* 26 */     VillagePools.bootstrap();
/* 27 */     return BUILTIN_EMPTY;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\data\worldgen\Pools.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */