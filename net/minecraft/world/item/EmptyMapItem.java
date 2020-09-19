/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.stats.Stats;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.InteractionHand;
/*    */ import net.minecraft.world.InteractionResultHolder;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public class EmptyMapItem extends ComplexItem {
/*    */   public EmptyMapItem(Item.Properties debug1) {
/* 13 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public InteractionResultHolder<ItemStack> use(Level debug1, Player debug2, InteractionHand debug3) {
/* 18 */     ItemStack debug4 = MapItem.create(debug1, Mth.floor(debug2.getX()), Mth.floor(debug2.getZ()), (byte)0, true, false);
/*    */     
/* 20 */     ItemStack debug5 = debug2.getItemInHand(debug3);
/* 21 */     if (!debug2.abilities.instabuild) {
/* 22 */       debug5.shrink(1);
/*    */     }
/*    */     
/* 25 */     debug2.awardStat(Stats.ITEM_USED.get(this));
/* 26 */     debug2.playSound(SoundEvents.UI_CARTOGRAPHY_TABLE_TAKE_RESULT, 1.0F, 1.0F);
/*    */     
/* 28 */     if (debug5.isEmpty()) {
/* 29 */       return InteractionResultHolder.sidedSuccess(debug4, debug1.isClientSide());
/*    */     }
/* 31 */     if (!debug2.inventory.add(debug4.copy())) {
/* 32 */       debug2.drop(debug4, false);
/*    */     }
/*    */     
/* 35 */     return InteractionResultHolder.sidedSuccess(debug5, debug1.isClientSide());
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\item\EmptyMapItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */