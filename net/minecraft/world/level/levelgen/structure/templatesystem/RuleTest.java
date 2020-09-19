/*    */ package net.minecraft.world.level.levelgen.structure.templatesystem;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.Random;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public abstract class RuleTest
/*    */ {
/* 10 */   public static final Codec<RuleTest> CODEC = Registry.RULE_TEST.dispatch("predicate_type", RuleTest::getType, RuleTestType::codec);
/*    */   
/*    */   public abstract boolean test(BlockState paramBlockState, Random paramRandom);
/*    */   
/*    */   protected abstract RuleTestType<?> getType();
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\structure\templatesystem\RuleTest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */