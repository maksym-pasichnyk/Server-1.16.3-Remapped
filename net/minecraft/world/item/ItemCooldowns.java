/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import com.google.common.collect.Maps;
/*    */ import java.util.Iterator;
/*    */ import java.util.Map;
/*    */ import net.minecraft.util.Mth;
/*    */ 
/*    */ public class ItemCooldowns
/*    */ {
/* 10 */   private final Map<Item, CooldownInstance> cooldowns = Maps.newHashMap();
/*    */   private int tickCount;
/*    */   
/*    */   public boolean isOnCooldown(Item debug1) {
/* 14 */     return (getCooldownPercent(debug1, 0.0F) > 0.0F);
/*    */   }
/*    */   
/*    */   public float getCooldownPercent(Item debug1, float debug2) {
/* 18 */     CooldownInstance debug3 = this.cooldowns.get(debug1);
/*    */     
/* 20 */     if (debug3 != null) {
/* 21 */       float debug4 = (debug3.endTime - debug3.startTime);
/* 22 */       float debug5 = debug3.endTime - this.tickCount + debug2;
/* 23 */       return Mth.clamp(debug5 / debug4, 0.0F, 1.0F);
/*    */     } 
/*    */     
/* 26 */     return 0.0F;
/*    */   }
/*    */   
/*    */   public void tick() {
/* 30 */     this.tickCount++;
/*    */     
/* 32 */     if (!this.cooldowns.isEmpty()) {
/* 33 */       for (Iterator<Map.Entry<Item, CooldownInstance>> debug1 = this.cooldowns.entrySet().iterator(); debug1.hasNext(); ) {
/* 34 */         Map.Entry<Item, CooldownInstance> debug2 = debug1.next();
/* 35 */         if (((CooldownInstance)debug2.getValue()).endTime <= this.tickCount) {
/* 36 */           debug1.remove();
/* 37 */           onCooldownEnded(debug2.getKey());
/*    */         } 
/*    */       } 
/*    */     }
/*    */   }
/*    */   
/*    */   public void addCooldown(Item debug1, int debug2) {
/* 44 */     this.cooldowns.put(debug1, new CooldownInstance(this.tickCount, this.tickCount + debug2));
/* 45 */     onCooldownStarted(debug1, debug2);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void onCooldownStarted(Item debug1, int debug2) {}
/*    */ 
/*    */   
/*    */   protected void onCooldownEnded(Item debug1) {}
/*    */ 
/*    */   
/*    */   class CooldownInstance
/*    */   {
/*    */     private final int startTime;
/*    */     
/*    */     private final int endTime;
/*    */ 
/*    */     
/*    */     private CooldownInstance(int debug2, int debug3) {
/* 64 */       this.startTime = debug2;
/* 65 */       this.endTime = debug3;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\item\ItemCooldowns.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */