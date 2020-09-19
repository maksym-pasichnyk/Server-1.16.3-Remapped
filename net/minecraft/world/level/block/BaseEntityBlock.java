/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.MenuProvider;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public abstract class BaseEntityBlock extends Block implements EntityBlock {
/*    */   protected BaseEntityBlock(BlockBehaviour.Properties debug1) {
/* 13 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public RenderShape getRenderShape(BlockState debug1) {
/* 18 */     return RenderShape.INVISIBLE;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean triggerEvent(BlockState debug1, Level debug2, BlockPos debug3, int debug4, int debug5) {
/* 23 */     super.triggerEvent(debug1, debug2, debug3, debug4, debug5);
/*    */     
/* 25 */     BlockEntity debug6 = debug2.getBlockEntity(debug3);
/* 26 */     if (debug6 == null) {
/* 27 */       return false;
/*    */     }
/* 29 */     return debug6.triggerEvent(debug4, debug5);
/*    */   }
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public MenuProvider getMenuProvider(BlockState debug1, Level debug2, BlockPos debug3) {
/* 35 */     BlockEntity debug4 = debug2.getBlockEntity(debug3);
/* 36 */     return (debug4 instanceof MenuProvider) ? (MenuProvider)debug4 : null;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\BaseEntityBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */