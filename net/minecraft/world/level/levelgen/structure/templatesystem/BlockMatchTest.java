/*    */ package net.minecraft.world.level.levelgen.structure.templatesystem;
/*    */ import java.util.Random;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class BlockMatchTest extends RuleTest {
/*    */   public static final Codec<BlockMatchTest> CODEC;
/*    */   
/*    */   static {
/* 11 */     CODEC = Registry.BLOCK.fieldOf("block").xmap(BlockMatchTest::new, debug0 -> debug0.block).codec();
/*    */   }
/*    */   private final Block block;
/*    */   
/*    */   public BlockMatchTest(Block debug1) {
/* 16 */     this.block = debug1;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean test(BlockState debug1, Random debug2) {
/* 21 */     return debug1.is(this.block);
/*    */   }
/*    */ 
/*    */   
/*    */   protected RuleTestType<?> getType() {
/* 26 */     return RuleTestType.BLOCK_TEST;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\structure\templatesystem\BlockMatchTest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */