/*    */ package net.minecraft.world.level.block;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class WeepingVinesPlant extends GrowingPlantBodyBlock {
/*  7 */   public static final VoxelShape SHAPE = Block.box(1.0D, 0.0D, 1.0D, 15.0D, 16.0D, 15.0D);
/*    */   
/*    */   public WeepingVinesPlant(BlockBehaviour.Properties debug1) {
/* 10 */     super(debug1, Direction.DOWN, SHAPE, false);
/*    */   }
/*    */ 
/*    */   
/*    */   protected GrowingPlantHeadBlock getHeadBlock() {
/* 15 */     return (GrowingPlantHeadBlock)Blocks.WEEPING_VINES;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\WeepingVinesPlant.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */