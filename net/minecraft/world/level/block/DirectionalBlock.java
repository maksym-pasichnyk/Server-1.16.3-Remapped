/*    */ package net.minecraft.world.level.block;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*    */ import net.minecraft.world.level.block.state.properties.DirectionProperty;
/*    */ 
/*    */ public abstract class DirectionalBlock extends Block {
/*  7 */   public static final DirectionProperty FACING = BlockStateProperties.FACING;
/*    */   
/*    */   protected DirectionalBlock(BlockBehaviour.Properties debug1) {
/* 10 */     super(debug1);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\DirectionalBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */