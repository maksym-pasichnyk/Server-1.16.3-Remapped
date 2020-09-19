/*    */ package net.minecraft.world.level.levelgen.structure.templatesystem;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.Random;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class AlwaysTrueTest
/*    */   extends RuleTest {
/*  9 */   public static final Codec<AlwaysTrueTest> CODEC = Codec.unit(() -> INSTANCE);
/*    */   
/* 11 */   public static final AlwaysTrueTest INSTANCE = new AlwaysTrueTest();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean test(BlockState debug1, Random debug2) {
/* 18 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   protected RuleTestType<?> getType() {
/* 23 */     return RuleTestType.ALWAYS_TRUE_TEST;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\structure\templatesystem\AlwaysTrueTest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */