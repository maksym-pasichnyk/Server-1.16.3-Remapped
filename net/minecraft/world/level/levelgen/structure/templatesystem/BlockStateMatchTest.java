/*    */ package net.minecraft.world.level.levelgen.structure.templatesystem;
/*    */ import java.util.Random;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class BlockStateMatchTest extends RuleTest {
/*    */   public static final Codec<BlockStateMatchTest> CODEC;
/*    */   
/*    */   static {
/*  9 */     CODEC = BlockState.CODEC.fieldOf("block_state").xmap(BlockStateMatchTest::new, debug0 -> debug0.blockState).codec();
/*    */   }
/*    */   private final BlockState blockState;
/*    */   
/*    */   public BlockStateMatchTest(BlockState debug1) {
/* 14 */     this.blockState = debug1;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean test(BlockState debug1, Random debug2) {
/* 19 */     return (debug1 == this.blockState);
/*    */   }
/*    */ 
/*    */   
/*    */   protected RuleTestType<?> getType() {
/* 24 */     return RuleTestType.BLOCKSTATE_TEST;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\structure\templatesystem\BlockStateMatchTest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */