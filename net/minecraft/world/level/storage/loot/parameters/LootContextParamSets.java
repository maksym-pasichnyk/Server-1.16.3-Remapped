/*     */ package net.minecraft.world.level.storage.loot.parameters;
/*     */ 
/*     */ import com.google.common.collect.BiMap;
/*     */ import com.google.common.collect.HashBiMap;
/*     */ import java.util.function.Consumer;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.resources.ResourceLocation;
/*     */ 
/*     */ public class LootContextParamSets
/*     */ {
/*  11 */   private static final BiMap<ResourceLocation, LootContextParamSet> REGISTRY = (BiMap<ResourceLocation, LootContextParamSet>)HashBiMap.create(); public static final LootContextParamSet CHEST; public static final LootContextParamSet COMMAND;
/*     */   public static final LootContextParamSet SELECTOR;
/*  13 */   public static final LootContextParamSet EMPTY = register("empty", debug0 -> {
/*     */       
/*  15 */       }); public static final LootContextParamSet FISHING; static { CHEST = register("chest", debug0 -> debug0.required(LootContextParams.ORIGIN).optional(LootContextParams.THIS_ENTITY));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  20 */     COMMAND = register("command", debug0 -> debug0.required(LootContextParams.ORIGIN).optional(LootContextParams.THIS_ENTITY));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  25 */     SELECTOR = register("selector", debug0 -> debug0.required(LootContextParams.ORIGIN).required(LootContextParams.THIS_ENTITY));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  30 */     FISHING = register("fishing", debug0 -> debug0.required(LootContextParams.ORIGIN).required(LootContextParams.TOOL).optional(LootContextParams.THIS_ENTITY));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  36 */     ENTITY = register("entity", debug0 -> debug0.required(LootContextParams.THIS_ENTITY).required(LootContextParams.ORIGIN).required(LootContextParams.DAMAGE_SOURCE).optional(LootContextParams.KILLER_ENTITY).optional(LootContextParams.DIRECT_KILLER_ENTITY).optional(LootContextParams.LAST_DAMAGE_PLAYER));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  45 */     GIFT = register("gift", debug0 -> debug0.required(LootContextParams.ORIGIN).required(LootContextParams.THIS_ENTITY));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  50 */     PIGLIN_BARTER = register("barter", debug0 -> debug0.required(LootContextParams.THIS_ENTITY));
/*     */ 
/*     */ 
/*     */     
/*  54 */     ADVANCEMENT_REWARD = register("advancement_reward", debug0 -> debug0.required(LootContextParams.THIS_ENTITY).required(LootContextParams.ORIGIN));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  59 */     ADVANCEMENT_ENTITY = register("advancement_entity", debug0 -> debug0.required(LootContextParams.THIS_ENTITY).required(LootContextParams.ORIGIN));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  64 */     ALL_PARAMS = register("generic", debug0 -> debug0.required(LootContextParams.THIS_ENTITY).required(LootContextParams.LAST_DAMAGE_PLAYER).required(LootContextParams.DAMAGE_SOURCE).required(LootContextParams.KILLER_ENTITY).required(LootContextParams.DIRECT_KILLER_ENTITY).required(LootContextParams.ORIGIN).required(LootContextParams.BLOCK_STATE).required(LootContextParams.BLOCK_ENTITY).required(LootContextParams.TOOL).required(LootContextParams.EXPLOSION_RADIUS));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  77 */     BLOCK = register("block", debug0 -> debug0.required(LootContextParams.BLOCK_STATE).required(LootContextParams.ORIGIN).required(LootContextParams.TOOL).optional(LootContextParams.THIS_ENTITY).optional(LootContextParams.BLOCK_ENTITY).optional(LootContextParams.EXPLOSION_RADIUS)); }
/*     */   
/*     */   public static final LootContextParamSet ENTITY; public static final LootContextParamSet GIFT;
/*     */   public static final LootContextParamSet PIGLIN_BARTER;
/*     */   public static final LootContextParamSet ADVANCEMENT_REWARD;
/*     */   public static final LootContextParamSet ADVANCEMENT_ENTITY;
/*     */   public static final LootContextParamSet ALL_PARAMS;
/*     */   public static final LootContextParamSet BLOCK;
/*     */   
/*     */   private static LootContextParamSet register(String debug0, Consumer<LootContextParamSet.Builder> debug1) {
/*  87 */     LootContextParamSet.Builder debug2 = new LootContextParamSet.Builder();
/*  88 */     debug1.accept(debug2);
/*  89 */     LootContextParamSet debug3 = debug2.build();
/*  90 */     ResourceLocation debug4 = new ResourceLocation(debug0);
/*  91 */     LootContextParamSet debug5 = (LootContextParamSet)REGISTRY.put(debug4, debug3);
/*  92 */     if (debug5 != null) {
/*  93 */       throw new IllegalStateException("Loot table parameter set " + debug4 + " is already registered");
/*     */     }
/*  95 */     return debug3;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public static LootContextParamSet get(ResourceLocation debug0) {
/* 100 */     return (LootContextParamSet)REGISTRY.get(debug0);
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public static ResourceLocation getKey(LootContextParamSet debug0) {
/* 105 */     return (ResourceLocation)REGISTRY.inverse().get(debug0);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\storage\loot\parameters\LootContextParamSets.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */