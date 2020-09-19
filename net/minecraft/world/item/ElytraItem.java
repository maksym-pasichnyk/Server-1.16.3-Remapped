/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import net.minecraft.world.InteractionHand;
/*    */ import net.minecraft.world.InteractionResultHolder;
/*    */ import net.minecraft.world.entity.EquipmentSlot;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.DispenserBlock;
/*    */ 
/*    */ public class ElytraItem extends Item implements Wearable {
/*    */   public ElytraItem(Item.Properties debug1) {
/* 13 */     super(debug1);
/*    */     
/* 15 */     DispenserBlock.registerBehavior(this, ArmorItem.DISPENSE_ITEM_BEHAVIOR);
/*    */   }
/*    */   
/*    */   public static boolean isFlyEnabled(ItemStack debug0) {
/* 19 */     return (debug0.getDamageValue() < debug0.getMaxDamage() - 1);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isValidRepairItem(ItemStack debug1, ItemStack debug2) {
/* 24 */     return (debug2.getItem() == Items.PHANTOM_MEMBRANE);
/*    */   }
/*    */ 
/*    */   
/*    */   public InteractionResultHolder<ItemStack> use(Level debug1, Player debug2, InteractionHand debug3) {
/* 29 */     ItemStack debug4 = debug2.getItemInHand(debug3);
/* 30 */     EquipmentSlot debug5 = Mob.getEquipmentSlotForItem(debug4);
/* 31 */     ItemStack debug6 = debug2.getItemBySlot(debug5);
/*    */     
/* 33 */     if (debug6.isEmpty()) {
/* 34 */       debug2.setItemSlot(debug5, debug4.copy());
/* 35 */       debug4.setCount(0);
/* 36 */       return InteractionResultHolder.sidedSuccess(debug4, debug1.isClientSide());
/*    */     } 
/*    */     
/* 39 */     return InteractionResultHolder.fail(debug4);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\item\ElytraItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */