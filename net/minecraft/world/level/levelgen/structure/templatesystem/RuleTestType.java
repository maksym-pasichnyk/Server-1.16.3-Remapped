/*    */ package net.minecraft.world.level.levelgen.structure.templatesystem;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.core.Registry;
/*    */ 
/*    */ public interface RuleTestType<P extends RuleTest> {
/*  7 */   public static final RuleTestType<AlwaysTrueTest> ALWAYS_TRUE_TEST = register("always_true", AlwaysTrueTest.CODEC);
/*  8 */   public static final RuleTestType<BlockMatchTest> BLOCK_TEST = register("block_match", BlockMatchTest.CODEC);
/*  9 */   public static final RuleTestType<BlockStateMatchTest> BLOCKSTATE_TEST = register("blockstate_match", BlockStateMatchTest.CODEC);
/* 10 */   public static final RuleTestType<TagMatchTest> TAG_TEST = register("tag_match", TagMatchTest.CODEC);
/* 11 */   public static final RuleTestType<RandomBlockMatchTest> RANDOM_BLOCK_TEST = register("random_block_match", RandomBlockMatchTest.CODEC);
/* 12 */   public static final RuleTestType<RandomBlockStateMatchTest> RANDOM_BLOCKSTATE_TEST = register("random_blockstate_match", RandomBlockStateMatchTest.CODEC);
/*    */ 
/*    */   
/*    */   Codec<P> codec();
/*    */   
/*    */   static <P extends RuleTest> RuleTestType<P> register(String debug0, Codec<P> debug1) {
/* 18 */     return (RuleTestType<P>)Registry.register(Registry.RULE_TEST, debug0, () -> debug0);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\structure\templatesystem\RuleTestType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */