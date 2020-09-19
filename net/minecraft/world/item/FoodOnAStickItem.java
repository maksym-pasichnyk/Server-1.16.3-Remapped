/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import net.minecraft.stats.Stats;
/*    */ import net.minecraft.world.InteractionHand;
/*    */ import net.minecraft.world.InteractionResultHolder;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.ItemSteerable;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public class FoodOnAStickItem<T extends Entity & ItemSteerable> extends Item {
/*    */   private final EntityType<T> canInteractWith;
/*    */   private final int consumeItemDamage;
/*    */   
/*    */   public FoodOnAStickItem(Item.Properties debug1, EntityType<T> debug2, int debug3) {
/* 17 */     super(debug1);
/*    */     
/* 19 */     this.canInteractWith = debug2;
/* 20 */     this.consumeItemDamage = debug3;
/*    */   }
/*    */ 
/*    */   
/*    */   public InteractionResultHolder<ItemStack> use(Level debug1, Player debug2, InteractionHand debug3) {
/* 25 */     ItemStack debug4 = debug2.getItemInHand(debug3);
/* 26 */     if (debug1.isClientSide) {
/* 27 */       return InteractionResultHolder.pass(debug4);
/*    */     }
/*    */     
/* 30 */     Entity debug5 = debug2.getVehicle();
/*    */     
/* 32 */     if (debug2.isPassenger() && debug5 instanceof ItemSteerable && debug5.getType() == this.canInteractWith) {
/* 33 */       ItemSteerable debug6 = (ItemSteerable)debug5;
/*    */       
/* 35 */       if (debug6.boost()) {
/* 36 */         debug4.hurtAndBreak(this.consumeItemDamage, debug2, debug1 -> debug1.broadcastBreakEvent(debug0));
/* 37 */         if (debug4.isEmpty()) {
/* 38 */           ItemStack debug7 = new ItemStack(Items.FISHING_ROD);
/* 39 */           debug7.setTag(debug4.getTag());
/* 40 */           return InteractionResultHolder.success(debug7);
/*    */         } 
/* 42 */         return InteractionResultHolder.success(debug4);
/*    */       } 
/*    */     } 
/*    */     
/* 46 */     debug2.awardStat(Stats.ITEM_USED.get(this));
/*    */     
/* 48 */     return InteractionResultHolder.pass(debug4);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\item\FoodOnAStickItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */