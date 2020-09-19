/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.item.Wearable;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ import net.minecraft.world.level.block.entity.SkullBlockEntity;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*    */ 
/*    */ public abstract class AbstractSkullBlock
/*    */   extends BaseEntityBlock implements Wearable {
/*    */   public AbstractSkullBlock(SkullBlock.Type debug1, BlockBehaviour.Properties debug2) {
/* 15 */     super(debug2);
/* 16 */     this.type = debug1;
/*    */   }
/*    */   private final SkullBlock.Type type;
/*    */   
/*    */   public BlockEntity newBlockEntity(BlockGetter debug1) {
/* 21 */     return (BlockEntity)new SkullBlockEntity();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isPathfindable(BlockState debug1, BlockGetter debug2, BlockPos debug3, PathComputationType debug4) {
/* 30 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\AbstractSkullBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */