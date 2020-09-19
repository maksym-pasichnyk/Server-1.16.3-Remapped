/*    */ package net.minecraft.world.level.block.state.predicate;
/*    */ 
/*    */ import java.util.function.Predicate;
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class BlockPredicate
/*    */   implements Predicate<BlockState> {
/*    */   private final Block block;
/*    */   
/*    */   public BlockPredicate(Block debug1) {
/* 13 */     this.block = debug1;
/*    */   }
/*    */   
/*    */   public static BlockPredicate forBlock(Block debug0) {
/* 17 */     return new BlockPredicate(debug0);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean test(@Nullable BlockState debug1) {
/* 22 */     return (debug1 != null && debug1.is(this.block));
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\state\predicate\BlockPredicate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */