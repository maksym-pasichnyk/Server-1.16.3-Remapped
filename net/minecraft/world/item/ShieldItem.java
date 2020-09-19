/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import net.minecraft.tags.ItemTags;
/*    */ import net.minecraft.world.InteractionHand;
/*    */ import net.minecraft.world.InteractionResultHolder;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.DispenserBlock;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ShieldItem
/*    */   extends Item
/*    */ {
/*    */   public ShieldItem(Item.Properties debug1) {
/* 22 */     super(debug1);
/*    */     
/* 24 */     DispenserBlock.registerBehavior(this, ArmorItem.DISPENSE_ITEM_BEHAVIOR);
/*    */   }
/*    */ 
/*    */   
/*    */   public String getDescriptionId(ItemStack debug1) {
/* 29 */     if (debug1.getTagElement("BlockEntityTag") != null) {
/* 30 */       return getDescriptionId() + '.' + getColor(debug1).getName();
/*    */     }
/* 32 */     return super.getDescriptionId(debug1);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public UseAnim getUseAnimation(ItemStack debug1) {
/* 42 */     return UseAnim.BLOCK;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getUseDuration(ItemStack debug1) {
/* 47 */     return 72000;
/*    */   }
/*    */ 
/*    */   
/*    */   public InteractionResultHolder<ItemStack> use(Level debug1, Player debug2, InteractionHand debug3) {
/* 52 */     ItemStack debug4 = debug2.getItemInHand(debug3);
/* 53 */     debug2.startUsingItem(debug3);
/* 54 */     return InteractionResultHolder.consume(debug4);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isValidRepairItem(ItemStack debug1, ItemStack debug2) {
/* 59 */     return (ItemTags.PLANKS.contains(debug2.getItem()) || super.isValidRepairItem(debug1, debug2));
/*    */   }
/*    */   
/*    */   public static DyeColor getColor(ItemStack debug0) {
/* 63 */     return DyeColor.byId(debug0.getOrCreateTagElement("BlockEntityTag").getInt("Base"));
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\item\ShieldItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */