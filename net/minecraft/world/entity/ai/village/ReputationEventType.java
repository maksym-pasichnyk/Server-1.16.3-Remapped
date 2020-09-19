/*    */ package net.minecraft.world.entity.ai.village;
/*    */ 
/*    */ public interface ReputationEventType {
/*  4 */   public static final ReputationEventType ZOMBIE_VILLAGER_CURED = register("zombie_villager_cured");
/*  5 */   public static final ReputationEventType GOLEM_KILLED = register("golem_killed");
/*  6 */   public static final ReputationEventType VILLAGER_HURT = register("villager_hurt");
/*  7 */   public static final ReputationEventType VILLAGER_KILLED = register("villager_killed");
/*  8 */   public static final ReputationEventType TRADE = register("trade");
/*    */   
/*    */   static ReputationEventType register(final String name) {
/* 11 */     return new ReputationEventType()
/*    */       {
/*    */         public String toString() {
/* 14 */           return name;
/*    */         }
/*    */       };
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\village\ReputationEventType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */