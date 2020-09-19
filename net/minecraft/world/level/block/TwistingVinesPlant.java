/*    */ package net.minecraft.world.level.block;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class TwistingVinesPlant extends GrowingPlantBodyBlock {
/*  7 */   public static final VoxelShape SHAPE = Block.box(4.0D, 0.0D, 4.0D, 12.0D, 16.0D, 12.0D);
/*    */   
/*    */   public TwistingVinesPlant(BlockBehaviour.Properties debug1) {
/* 10 */     super(debug1, Direction.UP, SHAPE, false);
/*    */   }
/*    */ 
/*    */   
/*    */   protected GrowingPlantHeadBlock getHeadBlock() {
/* 15 */     return (GrowingPlantHeadBlock)Blocks.TWISTING_VINES;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\TwistingVinesPlant.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */