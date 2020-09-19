/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.projectile.AbstractArrow;
/*    */ import net.minecraft.world.entity.projectile.SpectralArrow;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public class SpectralArrowItem extends ArrowItem {
/*    */   public SpectralArrowItem(Item.Properties debug1) {
/* 10 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public AbstractArrow createArrow(Level debug1, ItemStack debug2, LivingEntity debug3) {
/* 15 */     return (AbstractArrow)new SpectralArrow(debug1, debug3);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\item\SpectralArrowItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */