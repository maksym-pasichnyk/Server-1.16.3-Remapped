/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.item.DyeColor;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.entity.BannerBlockEntity;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public abstract class AbstractBannerBlock extends BaseEntityBlock {
/*    */   private final DyeColor color;
/*    */   
/*    */   protected AbstractBannerBlock(DyeColor debug1, BlockBehaviour.Properties debug2) {
/* 19 */     super(debug2);
/* 20 */     this.color = debug1;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isPossibleToRespawnInThis() {
/* 25 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockEntity newBlockEntity(BlockGetter debug1) {
/* 30 */     return (BlockEntity)new BannerBlockEntity(this.color);
/*    */   }
/*    */ 
/*    */   
/*    */   public void setPlacedBy(Level debug1, BlockPos debug2, BlockState debug3, @Nullable LivingEntity debug4, ItemStack debug5) {
/* 35 */     if (debug5.hasCustomHoverName()) {
/* 36 */       BlockEntity debug6 = debug1.getBlockEntity(debug2);
/* 37 */       if (debug6 instanceof BannerBlockEntity) {
/* 38 */         ((BannerBlockEntity)debug6).setCustomName(debug5.getHoverName());
/*    */       }
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
/*    */   public DyeColor getColor() {
/* 54 */     return this.color;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\AbstractBannerBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */