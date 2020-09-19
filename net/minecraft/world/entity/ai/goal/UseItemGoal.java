/*    */ package net.minecraft.world.entity.ai.goal;
/*    */ 
/*    */ import java.util.function.Predicate;
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.sounds.SoundEvent;
/*    */ import net.minecraft.world.InteractionHand;
/*    */ import net.minecraft.world.entity.EquipmentSlot;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ public class UseItemGoal<T extends Mob>
/*    */   extends Goal {
/*    */   private final T mob;
/*    */   private final ItemStack item;
/*    */   private final Predicate<? super T> canUseSelector;
/*    */   private final SoundEvent finishUsingSound;
/*    */   
/*    */   public UseItemGoal(T debug1, ItemStack debug2, @Nullable SoundEvent debug3, Predicate<? super T> debug4) {
/* 19 */     this.mob = debug1;
/* 20 */     this.item = debug2;
/* 21 */     this.finishUsingSound = debug3;
/* 22 */     this.canUseSelector = debug4;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canUse() {
/* 27 */     return this.canUseSelector.test(this.mob);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canContinueToUse() {
/* 32 */     return this.mob.isUsingItem();
/*    */   }
/*    */ 
/*    */   
/*    */   public void start() {
/* 37 */     this.mob.setItemSlot(EquipmentSlot.MAINHAND, this.item.copy());
/* 38 */     this.mob.startUsingItem(InteractionHand.MAIN_HAND);
/*    */   }
/*    */ 
/*    */   
/*    */   public void stop() {
/* 43 */     this.mob.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
/*    */     
/* 45 */     if (this.finishUsingSound != null)
/* 46 */       this.mob.playSound(this.finishUsingSound, 1.0F, this.mob.getRandom().nextFloat() * 0.2F + 0.9F); 
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\goal\UseItemGoal.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */