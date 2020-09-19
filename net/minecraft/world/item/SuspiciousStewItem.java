/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.nbt.ListTag;
/*    */ import net.minecraft.nbt.Tag;
/*    */ import net.minecraft.world.effect.MobEffect;
/*    */ import net.minecraft.world.effect.MobEffectInstance;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SuspiciousStewItem
/*    */   extends Item
/*    */ {
/*    */   public SuspiciousStewItem(Item.Properties debug1) {
/* 19 */     super(debug1);
/*    */   }
/*    */   
/*    */   public static void saveMobEffect(ItemStack debug0, MobEffect debug1, int debug2) {
/* 23 */     CompoundTag debug3 = debug0.getOrCreateTag();
/* 24 */     ListTag debug4 = debug3.getList("Effects", 9);
/* 25 */     CompoundTag debug5 = new CompoundTag();
/*    */     
/* 27 */     debug5.putByte("EffectId", (byte)MobEffect.getId(debug1));
/* 28 */     debug5.putInt("EffectDuration", debug2);
/* 29 */     debug4.add(debug5);
/* 30 */     debug3.put("Effects", (Tag)debug4);
/*    */   }
/*    */ 
/*    */   
/*    */   public ItemStack finishUsingItem(ItemStack debug1, Level debug2, LivingEntity debug3) {
/* 35 */     ItemStack debug4 = super.finishUsingItem(debug1, debug2, debug3);
/*    */     
/* 37 */     CompoundTag debug5 = debug1.getTag();
/* 38 */     if (debug5 != null && debug5.contains("Effects", 9)) {
/* 39 */       ListTag debug6 = debug5.getList("Effects", 10);
/*    */       
/* 41 */       for (int debug7 = 0; debug7 < debug6.size(); debug7++) {
/* 42 */         int debug8 = 160;
/* 43 */         CompoundTag debug9 = debug6.getCompound(debug7);
/* 44 */         if (debug9.contains("EffectDuration", 3)) {
/* 45 */           debug8 = debug9.getInt("EffectDuration");
/*    */         }
/* 47 */         MobEffect debug10 = MobEffect.byId(debug9.getByte("EffectId"));
/* 48 */         if (debug10 != null) {
/* 49 */           debug3.addEffect(new MobEffectInstance(debug10, debug8));
/*    */         }
/*    */       } 
/*    */     } 
/*    */     
/* 54 */     if (debug3 instanceof Player && ((Player)debug3).abilities.instabuild) {
/* 55 */       return debug4;
/*    */     }
/* 57 */     return new ItemStack(Items.BOWL);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\item\SuspiciousStewItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */