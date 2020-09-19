/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.phys.shapes.CollisionContext;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class PotatoBlock extends CropBlock {
/* 12 */   private static final VoxelShape[] SHAPE_BY_AGE = new VoxelShape[] {
/* 13 */       Block.box(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D), 
/* 14 */       Block.box(0.0D, 0.0D, 0.0D, 16.0D, 3.0D, 16.0D), 
/* 15 */       Block.box(0.0D, 0.0D, 0.0D, 16.0D, 4.0D, 16.0D), 
/* 16 */       Block.box(0.0D, 0.0D, 0.0D, 16.0D, 5.0D, 16.0D), 
/* 17 */       Block.box(0.0D, 0.0D, 0.0D, 16.0D, 6.0D, 16.0D), 
/* 18 */       Block.box(0.0D, 0.0D, 0.0D, 16.0D, 7.0D, 16.0D), 
/* 19 */       Block.box(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D), 
/* 20 */       Block.box(0.0D, 0.0D, 0.0D, 16.0D, 9.0D, 16.0D)
/*    */     };
/*    */   
/*    */   public PotatoBlock(BlockBehaviour.Properties debug1) {
/* 24 */     super(debug1);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public VoxelShape getShape(BlockState debug1, BlockGetter debug2, BlockPos debug3, CollisionContext debug4) {
/* 34 */     return SHAPE_BY_AGE[((Integer)debug1.getValue((Property)getAgeProperty())).intValue()];
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\PotatoBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */