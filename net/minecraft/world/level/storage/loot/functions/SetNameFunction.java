/*     */ package net.minecraft.world.level.storage.loot.functions;
/*     */ 
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.google.gson.JsonDeserializationContext;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.google.gson.JsonSerializationContext;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import java.util.Set;
/*     */ import java.util.function.UnaryOperator;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.ComponentUtils;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.util.GsonHelper;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.storage.loot.LootContext;
/*     */ import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
/*     */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public class SetNameFunction
/*     */   extends LootItemConditionalFunction {
/*  26 */   private static final Logger LOGGER = LogManager.getLogger();
/*     */   
/*     */   private final Component name;
/*     */   
/*     */   @Nullable
/*     */   private final LootContext.EntityTarget resolutionContext;
/*     */   
/*     */   private SetNameFunction(LootItemCondition[] debug1, @Nullable Component debug2, @Nullable LootContext.EntityTarget debug3) {
/*  34 */     super(debug1);
/*  35 */     this.name = debug2;
/*  36 */     this.resolutionContext = debug3;
/*     */   }
/*     */ 
/*     */   
/*     */   public LootItemFunctionType getType() {
/*  41 */     return LootItemFunctions.SET_NAME;
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<LootContextParam<?>> getReferencedContextParams() {
/*  46 */     return (this.resolutionContext != null) ? (Set<LootContextParam<?>>)ImmutableSet.of(this.resolutionContext.getParam()) : (Set<LootContextParam<?>>)ImmutableSet.of();
/*     */   }
/*     */   
/*     */   public static UnaryOperator<Component> createResolver(LootContext debug0, @Nullable LootContext.EntityTarget debug1) {
/*  50 */     if (debug1 != null) {
/*  51 */       Entity debug2 = (Entity)debug0.getParamOrNull(debug1.getParam());
/*  52 */       if (debug2 != null) {
/*     */ 
/*     */         
/*  55 */         CommandSourceStack debug3 = debug2.createCommandSourceStack().withPermission(2);
/*  56 */         return debug2 -> {
/*     */             try {
/*     */               return (Component)ComponentUtils.updateForEntity(debug0, debug2, debug1, 0);
/*  59 */             } catch (CommandSyntaxException debug3) {
/*     */               LOGGER.warn("Failed to resolve text component", (Throwable)debug3);
/*     */               return debug2;
/*     */             } 
/*     */           };
/*     */       } 
/*     */     } 
/*  66 */     return debug0 -> debug0;
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack run(ItemStack debug1, LootContext debug2) {
/*  71 */     if (this.name != null) {
/*  72 */       debug1.setHoverName(createResolver(debug2, this.resolutionContext).apply(this.name));
/*     */     }
/*  74 */     return debug1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class Serializer
/*     */     extends LootItemConditionalFunction.Serializer<SetNameFunction>
/*     */   {
/*     */     public void serialize(JsonObject debug1, SetNameFunction debug2, JsonSerializationContext debug3) {
/*  88 */       super.serialize(debug1, debug2, debug3);
/*     */       
/*  90 */       if (debug2.name != null) {
/*  91 */         debug1.add("name", Component.Serializer.toJsonTree(debug2.name));
/*     */       }
/*     */       
/*  94 */       if (debug2.resolutionContext != null) {
/*  95 */         debug1.add("entity", debug3.serialize(debug2.resolutionContext));
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public SetNameFunction deserialize(JsonObject debug1, JsonDeserializationContext debug2, LootItemCondition[] debug3) {
/* 101 */       MutableComponent mutableComponent = Component.Serializer.fromJson(debug1.get("name"));
/* 102 */       LootContext.EntityTarget debug5 = (LootContext.EntityTarget)GsonHelper.getAsObject(debug1, "entity", null, debug2, LootContext.EntityTarget.class);
/* 103 */       return new SetNameFunction(debug3, (Component)mutableComponent, debug5);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\storage\loot\functions\SetNameFunction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */