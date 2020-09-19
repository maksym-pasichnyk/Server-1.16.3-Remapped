/*    */ package net.minecraft.world.level.storage.loot.parameters;
/*    */ 
/*    */ import com.google.common.base.Joiner;
/*    */ import com.google.common.collect.ImmutableSet;
/*    */ import com.google.common.collect.Sets;
/*    */ import java.util.Collection;
/*    */ import java.util.Set;
/*    */ import net.minecraft.world.level.storage.loot.LootContextUser;
/*    */ import net.minecraft.world.level.storage.loot.ValidationContext;
/*    */ 
/*    */ public class LootContextParamSet
/*    */ {
/*    */   private final Set<LootContextParam<?>> required;
/*    */   private final Set<LootContextParam<?>> all;
/*    */   
/*    */   private LootContextParamSet(Set<LootContextParam<?>> debug1, Set<LootContextParam<?>> debug2) {
/* 17 */     this.required = (Set<LootContextParam<?>>)ImmutableSet.copyOf(debug1);
/* 18 */     this.all = (Set<LootContextParam<?>>)ImmutableSet.copyOf((Collection)Sets.union(debug1, debug2));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Set<LootContextParam<?>> getRequired() {
/* 26 */     return this.required;
/*    */   }
/*    */   
/*    */   public Set<LootContextParam<?>> getAllowed() {
/* 30 */     return this.all;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 35 */     return "[" + Joiner.on(", ").join(this.all.stream().map(debug1 -> (this.required.contains(debug1) ? "!" : "") + debug1.getName()).iterator()) + "]";
/*    */   }
/*    */   
/*    */   public void validateUser(ValidationContext debug1, LootContextUser debug2) {
/* 39 */     Set<LootContextParam<?>> debug3 = debug2.getReferencedContextParams();
/* 40 */     Sets.SetView setView = Sets.difference(debug3, this.all);
/* 41 */     if (!setView.isEmpty()) {
/* 42 */       debug1.reportProblem("Parameters " + setView + " are not provided in this context");
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static class Builder
/*    */   {
/* 51 */     private final Set<LootContextParam<?>> required = Sets.newIdentityHashSet();
/* 52 */     private final Set<LootContextParam<?>> optional = Sets.newIdentityHashSet();
/*    */     
/*    */     public Builder required(LootContextParam<?> debug1) {
/* 55 */       if (this.optional.contains(debug1)) {
/* 56 */         throw new IllegalArgumentException("Parameter " + debug1.getName() + " is already optional");
/*    */       }
/* 58 */       this.required.add(debug1);
/* 59 */       return this;
/*    */     }
/*    */     
/*    */     public Builder optional(LootContextParam<?> debug1) {
/* 63 */       if (this.required.contains(debug1)) {
/* 64 */         throw new IllegalArgumentException("Parameter " + debug1.getName() + " is already required");
/*    */       }
/* 66 */       this.optional.add(debug1);
/* 67 */       return this;
/*    */     }
/*    */     
/*    */     public LootContextParamSet build() {
/* 71 */       return new LootContextParamSet(this.required, this.optional);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\storage\loot\parameters\LootContextParamSet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */