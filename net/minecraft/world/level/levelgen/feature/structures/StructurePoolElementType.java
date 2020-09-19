/*    */ package net.minecraft.world.level.levelgen.feature.structures;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.core.Registry;
/*    */ 
/*    */ public interface StructurePoolElementType<P extends StructurePoolElement> {
/*  7 */   public static final StructurePoolElementType<SinglePoolElement> SINGLE = register("single_pool_element", SinglePoolElement.CODEC);
/*  8 */   public static final StructurePoolElementType<ListPoolElement> LIST = register("list_pool_element", ListPoolElement.CODEC);
/*  9 */   public static final StructurePoolElementType<FeaturePoolElement> FEATURE = register("feature_pool_element", FeaturePoolElement.CODEC);
/* 10 */   public static final StructurePoolElementType<EmptyPoolElement> EMPTY = register("empty_pool_element", EmptyPoolElement.CODEC);
/* 11 */   public static final StructurePoolElementType<LegacySinglePoolElement> LEGACY = register("legacy_single_pool_element", LegacySinglePoolElement.CODEC);
/*    */ 
/*    */   
/*    */   Codec<P> codec();
/*    */   
/*    */   static <P extends StructurePoolElement> StructurePoolElementType<P> register(String debug0, Codec<P> debug1) {
/* 17 */     return (StructurePoolElementType<P>)Registry.register(Registry.STRUCTURE_POOL_ELEMENT, debug0, () -> debug0);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\feature\structures\StructurePoolElementType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */