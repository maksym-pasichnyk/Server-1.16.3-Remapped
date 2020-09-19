/*    */ package net.minecraft.world.item.enchantment;
/*    */ 
/*    */ import net.minecraft.world.entity.EquipmentSlot;
/*    */ import net.minecraft.world.item.ArmorItem;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public enum EnchantmentCategory
/*    */ {
/* 17 */   ARMOR
/*    */   {
/*    */     public boolean canEnchant(Item debug1) {
/* 20 */       return debug1 instanceof ArmorItem;
/*    */     }
/*    */   },
/* 23 */   ARMOR_FEET
/*    */   {
/*    */     public boolean canEnchant(Item debug1) {
/* 26 */       return (debug1 instanceof ArmorItem && ((ArmorItem)debug1).getSlot() == EquipmentSlot.FEET);
/*    */     }
/*    */   },
/* 29 */   ARMOR_LEGS
/*    */   {
/*    */     public boolean canEnchant(Item debug1) {
/* 32 */       return (debug1 instanceof ArmorItem && ((ArmorItem)debug1).getSlot() == EquipmentSlot.LEGS);
/*    */     }
/*    */   },
/* 35 */   ARMOR_CHEST
/*    */   {
/*    */     public boolean canEnchant(Item debug1) {
/* 38 */       return (debug1 instanceof ArmorItem && ((ArmorItem)debug1).getSlot() == EquipmentSlot.CHEST);
/*    */     }
/*    */   },
/* 41 */   ARMOR_HEAD
/*    */   {
/*    */     public boolean canEnchant(Item debug1) {
/* 44 */       return (debug1 instanceof ArmorItem && ((ArmorItem)debug1).getSlot() == EquipmentSlot.HEAD);
/*    */     }
/*    */   },
/* 47 */   WEAPON
/*    */   {
/*    */     public boolean canEnchant(Item debug1) {
/* 50 */       return debug1 instanceof net.minecraft.world.item.SwordItem;
/*    */     }
/*    */   },
/* 53 */   DIGGER
/*    */   {
/*    */     public boolean canEnchant(Item debug1) {
/* 56 */       return debug1 instanceof net.minecraft.world.item.DiggerItem;
/*    */     }
/*    */   },
/* 59 */   FISHING_ROD
/*    */   {
/*    */     public boolean canEnchant(Item debug1) {
/* 62 */       return debug1 instanceof net.minecraft.world.item.FishingRodItem;
/*    */     }
/*    */   },
/* 65 */   TRIDENT
/*    */   {
/*    */     public boolean canEnchant(Item debug1) {
/* 68 */       return debug1 instanceof net.minecraft.world.item.TridentItem;
/*    */     }
/*    */   },
/* 71 */   BREAKABLE
/*    */   {
/*    */     public boolean canEnchant(Item debug1) {
/* 74 */       return debug1.canBeDepleted();
/*    */     }
/*    */   },
/* 77 */   BOW
/*    */   {
/*    */     public boolean canEnchant(Item debug1) {
/* 80 */       return debug1 instanceof net.minecraft.world.item.BowItem;
/*    */     }
/*    */   },
/* 83 */   WEARABLE
/*    */   {
/*    */     public boolean canEnchant(Item debug1) {
/* 86 */       return (debug1 instanceof net.minecraft.world.item.Wearable || Block.byItem(debug1) instanceof net.minecraft.world.item.Wearable);
/*    */     }
/*    */   },
/* 89 */   CROSSBOW
/*    */   {
/*    */     public boolean canEnchant(Item debug1) {
/* 92 */       return debug1 instanceof net.minecraft.world.item.CrossbowItem;
/*    */     }
/*    */   },
/* 95 */   VANISHABLE
/*    */   {
/*    */     public boolean canEnchant(Item debug1) {
/* 98 */       return (debug1 instanceof net.minecraft.world.item.Vanishable || Block.byItem(debug1) instanceof net.minecraft.world.item.Vanishable || BREAKABLE.canEnchant(debug1));
/*    */     }
/*    */   };
/*    */   
/*    */   public abstract boolean canEnchant(Item paramItem);
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\item\enchantment\EnchantmentCategory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */