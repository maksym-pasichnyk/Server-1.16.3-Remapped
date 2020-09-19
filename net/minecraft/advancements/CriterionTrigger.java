/*    */ package net.minecraft.advancements;
/*    */ 
/*    */ import com.google.gson.JsonObject;
/*    */ import net.minecraft.advancements.critereon.DeserializationContext;
/*    */ import net.minecraft.resources.ResourceLocation;
/*    */ import net.minecraft.server.PlayerAdvancements;
/*    */ 
/*    */ public interface CriterionTrigger<T extends CriterionTriggerInstance> {
/*    */   ResourceLocation getId();
/*    */   
/*    */   void addPlayerListener(PlayerAdvancements paramPlayerAdvancements, Listener<T> paramListener);
/*    */   
/*    */   void removePlayerListener(PlayerAdvancements paramPlayerAdvancements, Listener<T> paramListener);
/*    */   
/*    */   void removePlayerListeners(PlayerAdvancements paramPlayerAdvancements);
/*    */   
/*    */   T createInstance(JsonObject paramJsonObject, DeserializationContext paramDeserializationContext);
/*    */   
/*    */   public static class Listener<T extends CriterionTriggerInstance> {
/*    */     private final T trigger;
/*    */     private final Advancement advancement;
/*    */     private final String criterion;
/*    */     
/*    */     public Listener(T debug1, Advancement debug2, String debug3) {
/* 25 */       this.trigger = debug1;
/* 26 */       this.advancement = debug2;
/* 27 */       this.criterion = debug3;
/*    */     }
/*    */     
/*    */     public T getTriggerInstance() {
/* 31 */       return this.trigger;
/*    */     }
/*    */     
/*    */     public void run(PlayerAdvancements debug1) {
/* 35 */       debug1.award(this.advancement, this.criterion);
/*    */     }
/*    */ 
/*    */     
/*    */     public boolean equals(Object debug1) {
/* 40 */       if (this == debug1) {
/* 41 */         return true;
/*    */       }
/* 43 */       if (debug1 == null || getClass() != debug1.getClass()) {
/* 44 */         return false;
/*    */       }
/*    */       
/* 47 */       Listener<?> debug2 = (Listener)debug1;
/*    */       
/* 49 */       if (!this.trigger.equals(debug2.trigger)) {
/* 50 */         return false;
/*    */       }
/* 52 */       if (!this.advancement.equals(debug2.advancement)) {
/* 53 */         return false;
/*    */       }
/* 55 */       return this.criterion.equals(debug2.criterion);
/*    */     }
/*    */ 
/*    */     
/*    */     public int hashCode() {
/* 60 */       int debug1 = this.trigger.hashCode();
/* 61 */       debug1 = 31 * debug1 + this.advancement.hashCode();
/* 62 */       debug1 = 31 * debug1 + this.criterion.hashCode();
/* 63 */       return debug1;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\advancements\CriterionTrigger.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */