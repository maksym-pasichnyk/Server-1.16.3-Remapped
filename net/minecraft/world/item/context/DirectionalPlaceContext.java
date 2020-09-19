/*    */ package net.minecraft.world.item.context;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.world.InteractionHand;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.phys.BlockHitResult;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class DirectionalPlaceContext extends BlockPlaceContext {
/*    */   public DirectionalPlaceContext(Level debug1, BlockPos debug2, Direction debug3, ItemStack debug4, Direction debug5) {
/* 15 */     super(debug1, (Player)null, InteractionHand.MAIN_HAND, debug4, new BlockHitResult(Vec3.atBottomCenterOf((Vec3i)debug2), debug5, debug2, false));
/*    */     
/* 17 */     this.direction = debug3;
/*    */   }
/*    */   private final Direction direction;
/*    */   
/*    */   public BlockPos getClickedPos() {
/* 22 */     return getHitResult().getBlockPos();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canPlace() {
/* 27 */     return getLevel().getBlockState(getHitResult().getBlockPos()).canBeReplaced(this);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean replacingClickedOnBlock() {
/* 32 */     return canPlace();
/*    */   }
/*    */ 
/*    */   
/*    */   public Direction getNearestLookingDirection() {
/* 37 */     return Direction.DOWN;
/*    */   }
/*    */ 
/*    */   
/*    */   public Direction[] getNearestLookingDirections() {
/* 42 */     switch (this.direction)
/*    */     
/*    */     { default:
/* 45 */         return new Direction[] { Direction.DOWN, Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST, Direction.UP };
/*    */       case UP:
/* 47 */         return new Direction[] { Direction.DOWN, Direction.UP, Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST };
/*    */       case NORTH:
/* 49 */         return new Direction[] { Direction.DOWN, Direction.NORTH, Direction.EAST, Direction.WEST, Direction.UP, Direction.SOUTH };
/*    */       case SOUTH:
/* 51 */         return new Direction[] { Direction.DOWN, Direction.SOUTH, Direction.EAST, Direction.WEST, Direction.UP, Direction.NORTH };
/*    */       case WEST:
/* 53 */         return new Direction[] { Direction.DOWN, Direction.WEST, Direction.SOUTH, Direction.UP, Direction.NORTH, Direction.EAST };
/*    */       case EAST:
/* 55 */         break; }  return new Direction[] { Direction.DOWN, Direction.EAST, Direction.SOUTH, Direction.UP, Direction.NORTH, Direction.WEST };
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Direction getHorizontalDirection() {
/* 61 */     return (this.direction.getAxis() == Direction.Axis.Y) ? Direction.NORTH : this.direction;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isSecondaryUseActive() {
/* 66 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public float getRotation() {
/* 71 */     return (this.direction.get2DDataValue() * 90);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\item\context\DirectionalPlaceContext.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */