/*    */ package net.minecraft.world.level.levelgen.structure.templatesystem;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.core.Registry;
/*    */ 
/*    */ public interface PosRuleTestType<P extends PosRuleTest> {
/*  7 */   public static final PosRuleTestType<PosAlwaysTrueTest> ALWAYS_TRUE_TEST = register("always_true", PosAlwaysTrueTest.CODEC);
/*  8 */   public static final PosRuleTestType<LinearPosTest> LINEAR_POS_TEST = register("linear_pos", LinearPosTest.CODEC);
/*  9 */   public static final PosRuleTestType<AxisAlignedLinearPosTest> AXIS_ALIGNED_LINEAR_POS_TEST = register("axis_aligned_linear_pos", AxisAlignedLinearPosTest.CODEC);
/*    */ 
/*    */   
/*    */   Codec<P> codec();
/*    */   
/*    */   static <P extends PosRuleTest> PosRuleTestType<P> register(String debug0, Codec<P> debug1) {
/* 15 */     return (PosRuleTestType<P>)Registry.register(Registry.POS_RULE_TEST, debug0, () -> debug0);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\structure\templatesystem\PosRuleTestType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */