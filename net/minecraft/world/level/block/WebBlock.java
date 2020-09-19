/*    */ package net.minecraft.world.level.block;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class WebBlock extends Block {
/*    */   public WebBlock(BlockBehaviour.Properties debug1) {
/* 11 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public void entityInside(BlockState debug1, Level debug2, BlockPos debug3, Entity debug4) {
/* 16 */     debug4.makeStuckInBlock(debug1, new Vec3(0.25D, 0.05000000074505806D, 0.25D));
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\WebBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */