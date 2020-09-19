/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.projectile.AbstractArrow;
/*    */ import net.minecraft.world.entity.projectile.Arrow;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public class ArrowItem extends Item {
/*    */   public ArrowItem(Item.Properties debug1) {
/* 10 */     super(debug1);
/*    */   }
/*    */   
/*    */   public AbstractArrow createArrow(Level debug1, ItemStack debug2, LivingEntity debug3) {
/* 14 */     Arrow debug4 = new Arrow(debug1, debug3);
/* 15 */     debug4.setEffectsFromItem(debug2);
/* 16 */     return (AbstractArrow)debug4;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\item\ArrowItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */