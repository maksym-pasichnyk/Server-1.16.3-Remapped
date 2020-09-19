/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import net.minecraft.world.InteractionHand;
/*    */ import net.minecraft.world.InteractionResult;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ 
/*    */ public class NameTagItem extends Item {
/*    */   public NameTagItem(Item.Properties debug1) {
/* 11 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public InteractionResult interactLivingEntity(ItemStack debug1, Player debug2, LivingEntity debug3, InteractionHand debug4) {
/* 16 */     if (debug1.hasCustomHoverName() && !(debug3 instanceof Player)) {
/* 17 */       if (!debug2.level.isClientSide && debug3.isAlive()) {
/* 18 */         debug3.setCustomName(debug1.getHoverName());
/* 19 */         if (debug3 instanceof Mob) {
/* 20 */           ((Mob)debug3).setPersistenceRequired();
/*    */         }
/*    */         
/* 23 */         debug1.shrink(1);
/*    */       } 
/*    */       
/* 26 */       return InteractionResult.sidedSuccess(debug2.level.isClientSide);
/*    */     } 
/* 28 */     return InteractionResult.PASS;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\item\NameTagItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */