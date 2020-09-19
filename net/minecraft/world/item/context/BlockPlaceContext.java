/*    */ package net.minecraft.world.item.context;
/*    */ 
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.InteractionHand;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.phys.BlockHitResult;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class BlockPlaceContext
/*    */   extends UseOnContext
/*    */ {
/*    */   private final BlockPos relativePos;
/*    */   protected boolean replaceClicked = true;
/*    */   
/*    */   public BlockPlaceContext(Player debug1, InteractionHand debug2, ItemStack debug3, BlockHitResult debug4) {
/* 21 */     this(debug1.level, debug1, debug2, debug3, debug4);
/*    */   }
/*    */   
/*    */   public BlockPlaceContext(UseOnContext debug1) {
/* 25 */     this(debug1.getLevel(), debug1.getPlayer(), debug1.getHand(), debug1.getItemInHand(), debug1.getHitResult());
/*    */   }
/*    */   
/*    */   protected BlockPlaceContext(Level debug1, @Nullable Player debug2, InteractionHand debug3, ItemStack debug4, BlockHitResult debug5) {
/* 29 */     super(debug1, debug2, debug3, debug4, debug5);
/*    */     
/* 31 */     this.relativePos = debug5.getBlockPos().relative(debug5.getDirection());
/* 32 */     this.replaceClicked = debug1.getBlockState(debug5.getBlockPos()).canBeReplaced(this);
/*    */   }
/*    */   
/*    */   public static BlockPlaceContext at(BlockPlaceContext debug0, BlockPos debug1, Direction debug2) {
/* 36 */     return new BlockPlaceContext(debug0
/* 37 */         .getLevel(), debug0
/* 38 */         .getPlayer(), debug0
/* 39 */         .getHand(), debug0
/* 40 */         .getItemInHand(), new BlockHitResult(new Vec3(debug1
/*    */ 
/*    */             
/* 43 */             .getX() + 0.5D + debug2.getStepX() * 0.5D, debug1
/* 44 */             .getY() + 0.5D + debug2.getStepY() * 0.5D, debug1
/* 45 */             .getZ() + 0.5D + debug2.getStepZ() * 0.5D), debug2, debug1, false));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public BlockPos getClickedPos() {
/* 56 */     return this.replaceClicked ? super.getClickedPos() : this.relativePos;
/*    */   }
/*    */   
/*    */   public boolean canPlace() {
/* 60 */     return (this.replaceClicked || getLevel().getBlockState(getClickedPos()).canBeReplaced(this));
/*    */   }
/*    */   
/*    */   public boolean replacingClickedOnBlock() {
/* 64 */     return this.replaceClicked;
/*    */   }
/*    */   
/*    */   public Direction getNearestLookingDirection() {
/* 68 */     return Direction.orderedByNearest((Entity)getPlayer())[0];
/*    */   }
/*    */   
/*    */   public Direction[] getNearestLookingDirections() {
/* 72 */     Direction[] debug1 = Direction.orderedByNearest((Entity)getPlayer());
/*    */     
/* 74 */     if (this.replaceClicked) {
/* 75 */       return debug1;
/*    */     }
/*    */     
/* 78 */     Direction debug2 = getClickedFace();
/*    */ 
/*    */     
/* 81 */     int debug3 = 0;
/* 82 */     for (; debug3 < debug1.length && 
/* 83 */       debug1[debug3] != debug2.getOpposite(); debug3++);
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 88 */     if (debug3 > 0) {
/* 89 */       System.arraycopy(debug1, 0, debug1, 1, debug3);
/* 90 */       debug1[0] = debug2.getOpposite();
/*    */     } 
/* 92 */     return debug1;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\item\context\BlockPlaceContext.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */