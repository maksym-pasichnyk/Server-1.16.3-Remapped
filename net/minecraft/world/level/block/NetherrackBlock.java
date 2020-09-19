/*    */ package net.minecraft.world.level.block;
/*    */ import java.util.Random;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.tags.BlockTags;
/*    */ import net.minecraft.tags.Tag;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class NetherrackBlock extends Block implements BonemealableBlock {
/*    */   public NetherrackBlock(BlockBehaviour.Properties debug1) {
/* 14 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isValidBonemealTarget(BlockGetter debug1, BlockPos debug2, BlockState debug3, boolean debug4) {
/* 19 */     if (!debug1.getBlockState(debug2.above()).propagatesSkylightDown(debug1, debug2)) {
/* 20 */       return false;
/*    */     }
/*    */     
/* 23 */     for (BlockPos debug6 : BlockPos.betweenClosed(debug2.offset(-1, -1, -1), debug2.offset(1, 1, 1))) {
/* 24 */       if (debug1.getBlockState(debug6).is((Tag)BlockTags.NYLIUM)) {
/* 25 */         return true;
/*    */       }
/*    */     } 
/* 28 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isBonemealSuccess(Level debug1, Random debug2, BlockPos debug3, BlockState debug4) {
/* 33 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public void performBonemeal(ServerLevel debug1, Random debug2, BlockPos debug3, BlockState debug4) {
/* 38 */     boolean debug5 = false;
/* 39 */     boolean debug6 = false;
/* 40 */     for (BlockPos debug8 : BlockPos.betweenClosed(debug3.offset(-1, -1, -1), debug3.offset(1, 1, 1))) {
/* 41 */       BlockState debug9 = debug1.getBlockState(debug8);
/* 42 */       if (debug9.is(Blocks.WARPED_NYLIUM)) {
/* 43 */         debug6 = true;
/*    */       }
/*    */       
/* 46 */       if (debug9.is(Blocks.CRIMSON_NYLIUM)) {
/* 47 */         debug5 = true;
/*    */       }
/*    */       
/* 50 */       if (debug6 && debug5) {
/*    */         break;
/*    */       }
/*    */     } 
/*    */     
/* 55 */     if (debug6 && debug5) {
/* 56 */       debug1.setBlock(debug3, debug2.nextBoolean() ? Blocks.WARPED_NYLIUM.defaultBlockState() : Blocks.CRIMSON_NYLIUM.defaultBlockState(), 3);
/* 57 */     } else if (debug6) {
/* 58 */       debug1.setBlock(debug3, Blocks.WARPED_NYLIUM.defaultBlockState(), 3);
/* 59 */     } else if (debug5) {
/* 60 */       debug1.setBlock(debug3, Blocks.CRIMSON_NYLIUM.defaultBlockState(), 3);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\NetherrackBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */