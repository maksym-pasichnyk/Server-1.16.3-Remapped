/*    */ package net.minecraft.world.level.levelgen.structure.templatesystem;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.Random;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Registry;
/*    */ 
/*    */ public abstract class PosRuleTest
/*    */ {
/* 10 */   public static final Codec<PosRuleTest> CODEC = Registry.POS_RULE_TEST.dispatch("predicate_type", PosRuleTest::getType, PosRuleTestType::codec);
/*    */   
/*    */   public abstract boolean test(BlockPos paramBlockPos1, BlockPos paramBlockPos2, BlockPos paramBlockPos3, Random paramRandom);
/*    */   
/*    */   protected abstract PosRuleTestType<?> getType();
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\structure\templatesystem\PosRuleTest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */