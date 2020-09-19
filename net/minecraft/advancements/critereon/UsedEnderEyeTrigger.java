/*    */ package net.minecraft.advancements.critereon;
/*    */ 
/*    */ import com.google.gson.JsonObject;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.resources.ResourceLocation;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ 
/*    */ public class UsedEnderEyeTrigger extends SimpleCriterionTrigger<UsedEnderEyeTrigger.TriggerInstance> {
/*  9 */   private static final ResourceLocation ID = new ResourceLocation("used_ender_eye");
/*    */ 
/*    */   
/*    */   public ResourceLocation getId() {
/* 13 */     return ID;
/*    */   }
/*    */ 
/*    */   
/*    */   public TriggerInstance createInstance(JsonObject debug1, EntityPredicate.Composite debug2, DeserializationContext debug3) {
/* 18 */     MinMaxBounds.Floats debug4 = MinMaxBounds.Floats.fromJson(debug1.get("distance"));
/* 19 */     return new TriggerInstance(debug2, debug4);
/*    */   }
/*    */   
/*    */   public void trigger(ServerPlayer debug1, BlockPos debug2) {
/* 23 */     double debug3 = debug1.getX() - debug2.getX();
/* 24 */     double debug5 = debug1.getZ() - debug2.getZ();
/* 25 */     double debug7 = debug3 * debug3 + debug5 * debug5;
/* 26 */     trigger(debug1, debug2 -> debug2.matches(debug0));
/*    */   }
/*    */   
/*    */   public static class TriggerInstance extends AbstractCriterionTriggerInstance {
/*    */     private final MinMaxBounds.Floats level;
/*    */     
/*    */     public TriggerInstance(EntityPredicate.Composite debug1, MinMaxBounds.Floats debug2) {
/* 33 */       super(UsedEnderEyeTrigger.ID, debug1);
/* 34 */       this.level = debug2;
/*    */     }
/*    */     
/*    */     public boolean matches(double debug1) {
/* 38 */       return this.level.matchesSqr(debug1);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\advancements\critereon\UsedEnderEyeTrigger.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */