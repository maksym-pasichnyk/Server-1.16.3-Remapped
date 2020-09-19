/*    */ package net.minecraft.world.level.storage.loot;
/*    */ 
/*    */ import com.google.common.collect.HashMultimap;
/*    */ import com.google.common.collect.ImmutableMultimap;
/*    */ import com.google.common.collect.ImmutableSet;
/*    */ import com.google.common.collect.Multimap;
/*    */ import java.util.Set;
/*    */ import java.util.function.Function;
/*    */ import java.util.function.Supplier;
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.resources.ResourceLocation;
/*    */ import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ValidationContext
/*    */ {
/*    */   private final Multimap<String, String> problems;
/*    */   private final Supplier<String> context;
/*    */   private final LootContextParamSet params;
/*    */   private final Function<ResourceLocation, LootItemCondition> conditionResolver;
/*    */   private final Set<ResourceLocation> visitedConditions;
/*    */   private final Function<ResourceLocation, LootTable> tableResolver;
/*    */   private final Set<ResourceLocation> visitedTables;
/*    */   private String contextCache;
/*    */   
/*    */   public ValidationContext(LootContextParamSet debug1, Function<ResourceLocation, LootItemCondition> debug2, Function<ResourceLocation, LootTable> debug3) {
/* 31 */     this((Multimap<String, String>)HashMultimap.create(), () -> "", debug1, debug2, (Set<ResourceLocation>)ImmutableSet.of(), debug3, (Set<ResourceLocation>)ImmutableSet.of());
/*    */   }
/*    */   
/*    */   public ValidationContext(Multimap<String, String> debug1, Supplier<String> debug2, LootContextParamSet debug3, Function<ResourceLocation, LootItemCondition> debug4, Set<ResourceLocation> debug5, Function<ResourceLocation, LootTable> debug6, Set<ResourceLocation> debug7) {
/* 35 */     this.problems = debug1;
/* 36 */     this.context = debug2;
/* 37 */     this.params = debug3;
/* 38 */     this.conditionResolver = debug4;
/* 39 */     this.visitedConditions = debug5;
/* 40 */     this.tableResolver = debug6;
/* 41 */     this.visitedTables = debug7;
/*    */   }
/*    */   
/*    */   private String getContext() {
/* 45 */     if (this.contextCache == null) {
/* 46 */       this.contextCache = this.context.get();
/*    */     }
/*    */     
/* 49 */     return this.contextCache;
/*    */   }
/*    */   
/*    */   public void reportProblem(String debug1) {
/* 53 */     this.problems.put(getContext(), debug1);
/*    */   }
/*    */   
/*    */   public ValidationContext forChild(String debug1) {
/* 57 */     return new ValidationContext(this.problems, () -> getContext() + debug1, this.params, this.conditionResolver, this.visitedConditions, this.tableResolver, this.visitedTables);
/*    */   }
/*    */   
/*    */   public ValidationContext enterTable(String debug1, ResourceLocation debug2) {
/* 61 */     ImmutableSet<ResourceLocation> debug3 = ImmutableSet.builder().addAll(this.visitedTables).add(debug2).build();
/* 62 */     return new ValidationContext(this.problems, () -> getContext() + debug1, this.params, this.conditionResolver, this.visitedConditions, this.tableResolver, (Set<ResourceLocation>)debug3);
/*    */   }
/*    */   
/*    */   public ValidationContext enterCondition(String debug1, ResourceLocation debug2) {
/* 66 */     ImmutableSet<ResourceLocation> debug3 = ImmutableSet.builder().addAll(this.visitedConditions).add(debug2).build();
/* 67 */     return new ValidationContext(this.problems, () -> getContext() + debug1, this.params, this.conditionResolver, (Set<ResourceLocation>)debug3, this.tableResolver, this.visitedTables);
/*    */   }
/*    */   
/*    */   public boolean hasVisitedTable(ResourceLocation debug1) {
/* 71 */     return this.visitedTables.contains(debug1);
/*    */   }
/*    */   
/*    */   public boolean hasVisitedCondition(ResourceLocation debug1) {
/* 75 */     return this.visitedConditions.contains(debug1);
/*    */   }
/*    */   
/*    */   public Multimap<String, String> getProblems() {
/* 79 */     return (Multimap<String, String>)ImmutableMultimap.copyOf(this.problems);
/*    */   }
/*    */   
/*    */   public void validateUser(LootContextUser debug1) {
/* 83 */     this.params.validateUser(this, debug1);
/*    */   }
/*    */   
/*    */   @Nullable
/*    */   public LootTable resolveLootTable(ResourceLocation debug1) {
/* 88 */     return this.tableResolver.apply(debug1);
/*    */   }
/*    */   
/*    */   @Nullable
/*    */   public LootItemCondition resolveCondition(ResourceLocation debug1) {
/* 93 */     return this.conditionResolver.apply(debug1);
/*    */   }
/*    */   
/*    */   public ValidationContext setParams(LootContextParamSet debug1) {
/* 97 */     return new ValidationContext(this.problems, this.context, debug1, this.conditionResolver, this.visitedConditions, this.tableResolver, this.visitedTables);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\storage\loot\ValidationContext.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */