/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import net.minecraft.world.level.block.AbstractBannerBlock;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import org.apache.commons.lang3.Validate;
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
/*    */ public class BannerItem
/*    */   extends StandingAndWallBlockItem
/*    */ {
/*    */   public BannerItem(Block debug1, Block debug2, Item.Properties debug3) {
/* 23 */     super(debug1, debug2, debug3);
/*    */     
/* 25 */     Validate.isInstanceOf(AbstractBannerBlock.class, debug1);
/* 26 */     Validate.isInstanceOf(AbstractBannerBlock.class, debug2);
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
/*    */   public DyeColor getColor() {
/* 48 */     return ((AbstractBannerBlock)getBlock()).getColor();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\item\BannerItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */