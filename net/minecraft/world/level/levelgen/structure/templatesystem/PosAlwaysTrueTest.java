/*    */ package net.minecraft.world.level.levelgen.structure.templatesystem;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.Random;
/*    */ import net.minecraft.core.BlockPos;
/*    */ 
/*    */ public class PosAlwaysTrueTest
/*    */   extends PosRuleTest {
/*  9 */   public static final Codec<PosAlwaysTrueTest> CODEC = Codec.unit(() -> INSTANCE);
/*    */   
/* 11 */   public static final PosAlwaysTrueTest INSTANCE = new PosAlwaysTrueTest();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean test(BlockPos debug1, BlockPos debug2, BlockPos debug3, Random debug4) {
/* 18 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   protected PosRuleTestType<?> getType() {
/* 23 */     return PosRuleTestType.ALWAYS_TRUE_TEST;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\structure\templatesystem\PosAlwaysTrueTest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */