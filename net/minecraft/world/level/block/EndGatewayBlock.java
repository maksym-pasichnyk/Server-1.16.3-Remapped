/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ import net.minecraft.world.level.block.entity.TheEndGatewayBlockEntity;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.material.Fluid;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class EndGatewayBlock
/*    */   extends BaseEntityBlock
/*    */ {
/*    */   protected EndGatewayBlock(BlockBehaviour.Properties debug1) {
/* 17 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockEntity newBlockEntity(BlockGetter debug1) {
/* 22 */     return (BlockEntity)new TheEndGatewayBlockEntity();
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
/*    */   
/*    */   public boolean canBeReplaced(BlockState debug1, Fluid debug2) {
/* 60 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\EndGatewayBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */