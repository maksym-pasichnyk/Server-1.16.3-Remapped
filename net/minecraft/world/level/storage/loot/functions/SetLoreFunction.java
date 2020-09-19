/*     */ package net.minecraft.world.level.storage.loot.functions;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.google.common.collect.Streams;
/*     */ import com.google.gson.JsonArray;
/*     */ import com.google.gson.JsonDeserializationContext;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.google.gson.JsonSerializationContext;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.function.UnaryOperator;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.ListTag;
/*     */ import net.minecraft.nbt.StringTag;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.util.GsonHelper;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.storage.loot.LootContext;
/*     */ import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
/*     */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*     */ 
/*     */ 
/*     */ public class SetLoreFunction
/*     */   extends LootItemConditionalFunction
/*     */ {
/*     */   private final boolean replace;
/*     */   private final List<Component> lore;
/*     */   @Nullable
/*     */   private final LootContext.EntityTarget resolutionContext;
/*     */   
/*     */   public SetLoreFunction(LootItemCondition[] debug1, boolean debug2, List<Component> debug3, @Nullable LootContext.EntityTarget debug4) {
/*  36 */     super(debug1);
/*  37 */     this.replace = debug2;
/*  38 */     this.lore = (List<Component>)ImmutableList.copyOf(debug3);
/*  39 */     this.resolutionContext = debug4;
/*     */   }
/*     */ 
/*     */   
/*     */   public LootItemFunctionType getType() {
/*  44 */     return LootItemFunctions.SET_LORE;
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<LootContextParam<?>> getReferencedContextParams() {
/*  49 */     return (this.resolutionContext != null) ? (Set<LootContextParam<?>>)ImmutableSet.of(this.resolutionContext.getParam()) : (Set<LootContextParam<?>>)ImmutableSet.of();
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack run(ItemStack debug1, LootContext debug2) {
/*  54 */     ListTag debug3 = getLoreTag(debug1, !this.lore.isEmpty());
/*     */     
/*  56 */     if (debug3 != null) {
/*  57 */       if (this.replace) {
/*  58 */         debug3.clear();
/*     */       }
/*     */       
/*  61 */       UnaryOperator<Component> debug4 = SetNameFunction.createResolver(debug2, this.resolutionContext);
/*  62 */       this.lore.stream().map(debug4).map(Component.Serializer::toJson).map(StringTag::valueOf).forEach(debug3::add);
/*     */     } 
/*     */     
/*  65 */     return debug1;
/*     */   }
/*     */   @Nullable
/*     */   private ListTag getLoreTag(ItemStack debug1, boolean debug2) {
/*     */     CompoundTag debug3;
/*     */     CompoundTag debug4;
/*  71 */     if (debug1.hasTag()) {
/*  72 */       debug3 = debug1.getTag();
/*  73 */     } else if (debug2) {
/*  74 */       debug3 = new CompoundTag();
/*  75 */       debug1.setTag(debug3);
/*     */     } else {
/*  77 */       return null;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/*  82 */     if (debug3.contains("display", 10)) {
/*  83 */       debug4 = debug3.getCompound("display");
/*  84 */     } else if (debug2) {
/*  85 */       debug4 = new CompoundTag();
/*  86 */       debug3.put("display", (Tag)debug4);
/*     */     } else {
/*  88 */       return null;
/*     */     } 
/*     */     
/*  91 */     if (debug4.contains("Lore", 9))
/*  92 */       return debug4.getList("Lore", 8); 
/*  93 */     if (debug2) {
/*  94 */       ListTag debug5 = new ListTag();
/*  95 */       debug4.put("Lore", (Tag)debug5);
/*  96 */       return debug5;
/*     */     } 
/*  98 */     return null;
/*     */   }
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
/*     */   public static class Serializer
/*     */     extends LootItemConditionalFunction.Serializer<SetLoreFunction>
/*     */   {
/*     */     public void serialize(JsonObject debug1, SetLoreFunction debug2, JsonSerializationContext debug3) {
/* 140 */       super.serialize(debug1, debug2, debug3);
/*     */       
/* 142 */       debug1.addProperty("replace", Boolean.valueOf(debug2.replace));
/*     */       
/* 144 */       JsonArray debug4 = new JsonArray();
/* 145 */       for (Component debug6 : debug2.lore) {
/* 146 */         debug4.add(Component.Serializer.toJsonTree(debug6));
/*     */       }
/* 148 */       debug1.add("lore", (JsonElement)debug4);
/*     */       
/* 150 */       if (debug2.resolutionContext != null) {
/* 151 */         debug1.add("entity", debug3.serialize(debug2.resolutionContext));
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public SetLoreFunction deserialize(JsonObject debug1, JsonDeserializationContext debug2, LootItemCondition[] debug3) {
/* 157 */       boolean debug4 = GsonHelper.getAsBoolean(debug1, "replace", false);
/* 158 */       List<Component> debug5 = (List<Component>)Streams.stream((Iterable)GsonHelper.getAsJsonArray(debug1, "lore")).map(Component.Serializer::fromJson).collect(ImmutableList.toImmutableList());
/* 159 */       LootContext.EntityTarget debug6 = (LootContext.EntityTarget)GsonHelper.getAsObject(debug1, "entity", null, debug2, LootContext.EntityTarget.class);
/* 160 */       return new SetLoreFunction(debug3, debug4, debug5, debug6);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\storage\loot\functions\SetLoreFunction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */