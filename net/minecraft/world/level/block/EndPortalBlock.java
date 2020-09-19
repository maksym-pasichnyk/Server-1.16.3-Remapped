/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ import net.minecraft.world.level.block.entity.TheEndPortalBlockEntity;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.material.Fluid;
/*    */ import net.minecraft.world.phys.shapes.BooleanOp;
/*    */ import net.minecraft.world.phys.shapes.CollisionContext;
/*    */ import net.minecraft.world.phys.shapes.Shapes;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ 
/*    */ public class EndPortalBlock
/*    */   extends BaseEntityBlock
/*    */ {
/* 23 */   protected static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 12.0D, 16.0D);
/*    */   
/*    */   protected EndPortalBlock(BlockBehaviour.Properties debug1) {
/* 26 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockEntity newBlockEntity(BlockGetter debug1) {
/* 31 */     return (BlockEntity)new TheEndPortalBlockEntity();
/*    */   }
/*    */ 
/*    */   
/*    */   public VoxelShape getShape(BlockState debug1, BlockGetter debug2, BlockPos debug3, CollisionContext debug4) {
/* 36 */     return SHAPE;
/*    */   }
/*    */ 
/*    */   
/*    */   public void entityInside(BlockState debug1, Level debug2, BlockPos debug3, Entity debug4) {
/* 41 */     if (debug2 instanceof ServerLevel && !debug4.isPassenger() && !debug4.isVehicle() && debug4.canChangeDimensions() && 
/* 42 */       Shapes.joinIsNotEmpty(Shapes.create(debug4.getBoundingBox().move(-debug3.getX(), -debug3.getY(), -debug3.getZ())), debug1.getShape((BlockGetter)debug2, debug3), BooleanOp.AND)) {
/* 43 */       ResourceKey<Level> debug5 = (debug2.dimension() == Level.END) ? Level.OVERWORLD : Level.END;
/* 44 */       ServerLevel debug6 = ((ServerLevel)debug2).getServer().getLevel(debug5);
/* 45 */       if (debug6 == null) {
/*    */         return;
/*    */       }
/* 48 */       debug4.changeDimension(debug6);
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean canBeReplaced(BlockState debug1, Fluid debug2) {
/* 69 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\EndPortalBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */