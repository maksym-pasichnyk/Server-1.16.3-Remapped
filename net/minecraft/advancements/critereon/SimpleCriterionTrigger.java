/*    */ package net.minecraft.advancements.critereon;
/*    */ import com.google.common.collect.Lists;
/*    */ import com.google.common.collect.Maps;
/*    */ import com.google.common.collect.Sets;
/*    */ import com.google.gson.JsonObject;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.Set;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.advancements.CriterionTrigger;
/*    */ import net.minecraft.advancements.CriterionTriggerInstance;
/*    */ import net.minecraft.server.PlayerAdvancements;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ 
/*    */ public abstract class SimpleCriterionTrigger<T extends AbstractCriterionTriggerInstance> implements CriterionTrigger<T> {
/* 18 */   private final Map<PlayerAdvancements, Set<CriterionTrigger.Listener<T>>> players = Maps.newIdentityHashMap();
/*    */ 
/*    */   
/*    */   public final void addPlayerListener(PlayerAdvancements debug1, CriterionTrigger.Listener<T> debug2) {
/* 22 */     ((Set<CriterionTrigger.Listener<T>>)this.players.computeIfAbsent(debug1, debug0 -> Sets.newHashSet())).add(debug2);
/*    */   }
/*    */ 
/*    */   
/*    */   public final void removePlayerListener(PlayerAdvancements debug1, CriterionTrigger.Listener<T> debug2) {
/* 27 */     Set<CriterionTrigger.Listener<T>> debug3 = this.players.get(debug1);
/* 28 */     if (debug3 != null) {
/* 29 */       debug3.remove(debug2);
/* 30 */       if (debug3.isEmpty()) {
/* 31 */         this.players.remove(debug1);
/*    */       }
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public final void removePlayerListeners(PlayerAdvancements debug1) {
/* 38 */     this.players.remove(debug1);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public final T createInstance(JsonObject debug1, DeserializationContext debug2) {
/* 45 */     EntityPredicate.Composite debug3 = EntityPredicate.Composite.fromJson(debug1, "player", debug2);
/* 46 */     return createInstance(debug1, debug3, debug2);
/*    */   }
/*    */   
/*    */   protected void trigger(ServerPlayer debug1, Predicate<T> debug2) {
/* 50 */     PlayerAdvancements debug3 = debug1.getAdvancements();
/* 51 */     Set<CriterionTrigger.Listener<T>> debug4 = this.players.get(debug3);
/*    */     
/* 53 */     if (debug4 == null || debug4.isEmpty()) {
/*    */       return;
/*    */     }
/*    */     
/* 57 */     LootContext debug5 = EntityPredicate.createContext(debug1, (Entity)debug1);
/*    */     
/* 59 */     List<CriterionTrigger.Listener<T>> debug6 = null;
/* 60 */     for (CriterionTrigger.Listener<T> debug8 : debug4) {
/* 61 */       AbstractCriterionTriggerInstance abstractCriterionTriggerInstance = (AbstractCriterionTriggerInstance)debug8.getTriggerInstance();
/* 62 */       if (abstractCriterionTriggerInstance.getPlayerPredicate().matches(debug5) && debug2.test((T)abstractCriterionTriggerInstance)) {
/* 63 */         if (debug6 == null) {
/* 64 */           debug6 = Lists.newArrayList();
/*    */         }
/* 66 */         debug6.add(debug8);
/*    */       } 
/*    */     } 
/*    */     
/* 70 */     if (debug6 != null)
/* 71 */       for (CriterionTrigger.Listener<T> debug8 : debug6)
/* 72 */         debug8.run(debug3);  
/*    */   }
/*    */   
/*    */   protected abstract T createInstance(JsonObject paramJsonObject, EntityPredicate.Composite paramComposite, DeserializationContext paramDeserializationContext);
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\advancements\critereon\SimpleCriterionTrigger.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */