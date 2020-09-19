/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import java.util.Map;
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.item.context.BlockPlaceContext;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.phys.shapes.CollisionContext;
/*    */ 
/*    */ public class StandingAndWallBlockItem extends BlockItem {
/*    */   protected final Block wallBlock;
/*    */   
/*    */   public StandingAndWallBlockItem(Block debug1, Block debug2, Item.Properties debug3) {
/* 18 */     super(debug1, debug3);
/* 19 */     this.wallBlock = debug2;
/*    */   }
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   protected BlockState getPlacementState(BlockPlaceContext debug1) {
/* 25 */     BlockState debug2 = this.wallBlock.getStateForPlacement(debug1);
/*    */     
/* 27 */     BlockState debug3 = null;
/*    */     
/* 29 */     Level level = debug1.getLevel();
/* 30 */     BlockPos debug5 = debug1.getClickedPos();
/* 31 */     for (Direction debug9 : debug1.getNearestLookingDirections()) {
/* 32 */       if (debug9 != Direction.UP) {
/*    */ 
/*    */ 
/*    */         
/* 36 */         BlockState debug10 = (debug9 == Direction.DOWN) ? getBlock().getStateForPlacement(debug1) : debug2;
/* 37 */         if (debug10 != null && debug10.canSurvive((LevelReader)level, debug5)) {
/* 38 */           debug3 = debug10;
/*    */           break;
/*    */         } 
/*    */       } 
/*    */     } 
/* 43 */     return (debug3 != null && level.isUnobstructed(debug3, debug5, CollisionContext.empty())) ? debug3 : null;
/*    */   }
/*    */ 
/*    */   
/*    */   public void registerBlocks(Map<Block, Item> debug1, Item debug2) {
/* 48 */     super.registerBlocks(debug1, debug2);
/*    */     
/* 50 */     debug1.put(this.wallBlock, debug2);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\item\StandingAndWallBlockItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */