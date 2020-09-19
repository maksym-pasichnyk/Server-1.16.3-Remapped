/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import net.minecraft.world.level.block.entity.BannerPattern;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class BannerPatternItem
/*    */   extends Item
/*    */ {
/*    */   private final BannerPattern bannerPattern;
/*    */   
/*    */   public BannerPatternItem(BannerPattern debug1, Item.Properties debug2) {
/* 17 */     super(debug2);
/* 18 */     this.bannerPattern = debug1;
/*    */   }
/*    */   
/*    */   public BannerPattern getBannerPattern() {
/* 22 */     return this.bannerPattern;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\item\BannerPatternItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */