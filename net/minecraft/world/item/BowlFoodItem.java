/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public class BowlFoodItem extends Item {
/*    */   public BowlFoodItem(Item.Properties debug1) {
/*  9 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public ItemStack finishUsingItem(ItemStack debug1, Level debug2, LivingEntity debug3) {
/* 14 */     ItemStack debug4 = super.finishUsingItem(debug1, debug2, debug3);
/* 15 */     if (debug3 instanceof Player && ((Player)debug3).abilities.instabuild) {
/* 16 */       return debug4;
/*    */     }
/* 18 */     return new ItemStack(Items.BOWL);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\item\BowlFoodItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */