/*    */ package net.minecraft.world.level.levelgen.structure.templatesystem;
/*    */ 
/*    */ import com.mojang.datafixers.util.Either;
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.function.Function;
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.resources.RegistryFileCodec;
/*    */ 
/*    */ public interface StructureProcessorType<P extends StructureProcessor> {
/* 11 */   public static final StructureProcessorType<BlockIgnoreProcessor> BLOCK_IGNORE = register("block_ignore", BlockIgnoreProcessor.CODEC);
/* 12 */   public static final StructureProcessorType<BlockRotProcessor> BLOCK_ROT = register("block_rot", BlockRotProcessor.CODEC);
/* 13 */   public static final StructureProcessorType<GravityProcessor> GRAVITY = register("gravity", GravityProcessor.CODEC);
/* 14 */   public static final StructureProcessorType<JigsawReplacementProcessor> JIGSAW_REPLACEMENT = register("jigsaw_replacement", JigsawReplacementProcessor.CODEC);
/* 15 */   public static final StructureProcessorType<RuleProcessor> RULE = register("rule", RuleProcessor.CODEC);
/* 16 */   public static final StructureProcessorType<NopProcessor> NOP = register("nop", NopProcessor.CODEC);
/* 17 */   public static final StructureProcessorType<BlockAgeProcessor> BLOCK_AGE = register("block_age", BlockAgeProcessor.CODEC);
/* 18 */   public static final StructureProcessorType<BlackstoneReplaceProcessor> BLACKSTONE_REPLACE = register("blackstone_replace", BlackstoneReplaceProcessor.CODEC);
/* 19 */   public static final StructureProcessorType<LavaSubmergedBlockProcessor> LAVA_SUBMERGED_BLOCK = register("lava_submerged_block", LavaSubmergedBlockProcessor.CODEC);
/*    */   
/* 21 */   public static final Codec<StructureProcessor> SINGLE_CODEC = Registry.STRUCTURE_PROCESSOR.dispatch("processor_type", StructureProcessor::getType, StructureProcessorType::codec);
/*    */   
/* 23 */   public static final Codec<StructureProcessorList> LIST_OBJECT_CODEC = SINGLE_CODEC.listOf().xmap(StructureProcessorList::new, StructureProcessorList::list);
/*    */   
/*    */   public static final Codec<StructureProcessorList> DIRECT_CODEC;
/*    */ 
/*    */   
/*    */   static {
/* 29 */     DIRECT_CODEC = Codec.either(LIST_OBJECT_CODEC.fieldOf("processors").codec(), LIST_OBJECT_CODEC).xmap(debug0 -> (StructureProcessorList)debug0.map((), ()), Either::left);
/*    */   }
/*    */ 
/*    */   
/* 33 */   public static final Codec<Supplier<StructureProcessorList>> LIST_CODEC = (Codec<Supplier<StructureProcessorList>>)RegistryFileCodec.create(Registry.PROCESSOR_LIST_REGISTRY, DIRECT_CODEC);
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static <P extends StructureProcessor> StructureProcessorType<P> register(String debug0, Codec<P> debug1) {
/* 39 */     return (StructureProcessorType<P>)Registry.register(Registry.STRUCTURE_PROCESSOR, debug0, () -> debug0);
/*    */   }
/*    */   
/*    */   Codec<P> codec();
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\structure\templatesystem\StructureProcessorType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */