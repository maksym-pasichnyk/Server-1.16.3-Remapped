/*     */ package net.minecraft.world.level.storage.loot.functions;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.gson.JsonArray;
/*     */ import com.google.gson.JsonDeserializationContext;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.google.gson.JsonPrimitive;
/*     */ import com.google.gson.JsonSerializationContext;
/*     */ import com.google.gson.JsonSyntaxException;
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import java.util.UUID;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.Util;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.resources.ResourceLocation;
/*     */ import net.minecraft.util.GsonHelper;
/*     */ import net.minecraft.world.entity.EquipmentSlot;
/*     */ import net.minecraft.world.entity.ai.attributes.Attribute;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeModifier;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.storage.loot.LootContext;
/*     */ import net.minecraft.world.level.storage.loot.RandomValueBounds;
/*     */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*     */ 
/*     */ 
/*     */ public class SetAttributesFunction
/*     */   extends LootItemConditionalFunction
/*     */ {
/*     */   private final List<Modifier> modifiers;
/*     */   
/*     */   private SetAttributesFunction(LootItemCondition[] debug1, List<Modifier> debug2) {
/*  35 */     super(debug1);
/*  36 */     this.modifiers = (List<Modifier>)ImmutableList.copyOf(debug2);
/*     */   }
/*     */ 
/*     */   
/*     */   public LootItemFunctionType getType() {
/*  41 */     return LootItemFunctions.SET_ATTRIBUTES;
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack run(ItemStack debug1, LootContext debug2) {
/*  46 */     Random debug3 = debug2.getRandom();
/*  47 */     for (Modifier debug5 : this.modifiers) {
/*  48 */       UUID debug6 = debug5.id;
/*  49 */       if (debug6 == null) {
/*  50 */         debug6 = UUID.randomUUID();
/*     */       }
/*  52 */       EquipmentSlot debug7 = (EquipmentSlot)Util.getRandom((Object[])debug5.slots, debug3);
/*  53 */       debug1.addAttributeModifier(debug5.attribute, new AttributeModifier(debug6, debug5.name, debug5.amount.getFloat(debug3), debug5.operation), debug7);
/*     */     } 
/*  55 */     return debug1;
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
/*     */     extends LootItemConditionalFunction.Serializer<SetAttributesFunction>
/*     */   {
/*     */     public void serialize(JsonObject debug1, SetAttributesFunction debug2, JsonSerializationContext debug3) {
/* 120 */       super.serialize(debug1, debug2, debug3);
/*     */       
/* 122 */       JsonArray debug4 = new JsonArray();
/* 123 */       for (SetAttributesFunction.Modifier debug6 : debug2.modifiers) {
/* 124 */         debug4.add((JsonElement)debug6.serialize(debug3));
/*     */       }
/* 126 */       debug1.add("modifiers", (JsonElement)debug4);
/*     */     }
/*     */ 
/*     */     
/*     */     public SetAttributesFunction deserialize(JsonObject debug1, JsonDeserializationContext debug2, LootItemCondition[] debug3) {
/* 131 */       JsonArray debug4 = GsonHelper.getAsJsonArray(debug1, "modifiers");
/* 132 */       List<SetAttributesFunction.Modifier> debug5 = Lists.newArrayListWithExpectedSize(debug4.size());
/*     */       
/* 134 */       for (JsonElement debug7 : debug4) {
/* 135 */         debug5.add(SetAttributesFunction.Modifier.deserialize(GsonHelper.convertToJsonObject(debug7, "modifier"), debug2));
/*     */       }
/*     */       
/* 138 */       if (debug5.isEmpty()) {
/* 139 */         throw new JsonSyntaxException("Invalid attribute modifiers array; cannot be empty");
/*     */       }
/* 141 */       return new SetAttributesFunction(debug3, debug5);
/*     */     }
/*     */   }
/*     */   
/*     */   static class Modifier {
/*     */     private final String name;
/*     */     private final Attribute attribute;
/*     */     private final AttributeModifier.Operation operation;
/*     */     private final RandomValueBounds amount;
/*     */     @Nullable
/*     */     private final UUID id;
/*     */     private final EquipmentSlot[] slots;
/*     */     
/*     */     private Modifier(String debug1, Attribute debug2, AttributeModifier.Operation debug3, RandomValueBounds debug4, EquipmentSlot[] debug5, @Nullable UUID debug6) {
/* 155 */       this.name = debug1;
/* 156 */       this.attribute = debug2;
/* 157 */       this.operation = debug3;
/* 158 */       this.amount = debug4;
/* 159 */       this.id = debug6;
/* 160 */       this.slots = debug5;
/*     */     }
/*     */     
/*     */     public JsonObject serialize(JsonSerializationContext debug1) {
/* 164 */       JsonObject debug2 = new JsonObject();
/* 165 */       debug2.addProperty("name", this.name);
/* 166 */       debug2.addProperty("attribute", Registry.ATTRIBUTE.getKey(this.attribute).toString());
/* 167 */       debug2.addProperty("operation", operationToString(this.operation));
/* 168 */       debug2.add("amount", debug1.serialize(this.amount));
/* 169 */       if (this.id != null) {
/* 170 */         debug2.addProperty("id", this.id.toString());
/*     */       }
/* 172 */       if (this.slots.length == 1) {
/* 173 */         debug2.addProperty("slot", this.slots[0].getName());
/*     */       } else {
/* 175 */         JsonArray debug3 = new JsonArray();
/* 176 */         for (EquipmentSlot debug7 : this.slots) {
/* 177 */           debug3.add((JsonElement)new JsonPrimitive(debug7.getName()));
/*     */         }
/* 179 */         debug2.add("slot", (JsonElement)debug3);
/*     */       } 
/* 181 */       return debug2;
/*     */     }
/*     */     public static Modifier deserialize(JsonObject debug0, JsonDeserializationContext debug1) {
/*     */       EquipmentSlot[] debug7;
/* 185 */       String debug2 = GsonHelper.getAsString(debug0, "name");
/* 186 */       ResourceLocation debug3 = new ResourceLocation(GsonHelper.getAsString(debug0, "attribute"));
/* 187 */       Attribute debug4 = (Attribute)Registry.ATTRIBUTE.get(debug3);
/* 188 */       if (debug4 == null) {
/* 189 */         throw new JsonSyntaxException("Unknown attribute: " + debug3);
/*     */       }
/* 191 */       AttributeModifier.Operation debug5 = operationFromString(GsonHelper.getAsString(debug0, "operation"));
/* 192 */       RandomValueBounds debug6 = (RandomValueBounds)GsonHelper.getAsObject(debug0, "amount", debug1, RandomValueBounds.class);
/*     */       
/* 194 */       UUID debug8 = null;
/*     */       
/* 196 */       if (GsonHelper.isStringValue(debug0, "slot")) {
/* 197 */         debug7 = new EquipmentSlot[] { EquipmentSlot.byName(GsonHelper.getAsString(debug0, "slot")) };
/* 198 */       } else if (GsonHelper.isArrayNode(debug0, "slot")) {
/* 199 */         JsonArray debug9 = GsonHelper.getAsJsonArray(debug0, "slot");
/* 200 */         debug7 = new EquipmentSlot[debug9.size()];
/* 201 */         int debug10 = 0;
/* 202 */         for (JsonElement debug12 : debug9) {
/* 203 */           debug7[debug10++] = EquipmentSlot.byName(GsonHelper.convertToString(debug12, "slot"));
/*     */         }
/* 205 */         if (debug7.length == 0) {
/* 206 */           throw new JsonSyntaxException("Invalid attribute modifier slot; must contain at least one entry.");
/*     */         }
/*     */       } else {
/* 209 */         throw new JsonSyntaxException("Invalid or missing attribute modifier slot; must be either string or array of strings.");
/*     */       } 
/*     */       
/* 212 */       if (debug0.has("id")) {
/* 213 */         String debug9 = GsonHelper.getAsString(debug0, "id");
/*     */         try {
/* 215 */           debug8 = UUID.fromString(debug9);
/* 216 */         } catch (IllegalArgumentException debug10) {
/* 217 */           throw new JsonSyntaxException("Invalid attribute modifier id '" + debug9 + "' (must be UUID format, with dashes)");
/*     */         } 
/*     */       } 
/*     */       
/* 221 */       return new Modifier(debug2, debug4, debug5, debug6, debug7, debug8);
/*     */     }
/*     */     
/*     */     private static String operationToString(AttributeModifier.Operation debug0) {
/* 225 */       switch (debug0) {
/*     */         case ADDITION:
/* 227 */           return "addition";
/*     */         case MULTIPLY_BASE:
/* 229 */           return "multiply_base";
/*     */         case MULTIPLY_TOTAL:
/* 231 */           return "multiply_total";
/*     */       } 
/* 233 */       throw new IllegalArgumentException("Unknown operation " + debug0);
/*     */     }
/*     */     
/*     */     private static AttributeModifier.Operation operationFromString(String debug0) {
/* 237 */       switch (debug0) {
/*     */         case "addition":
/* 239 */           return AttributeModifier.Operation.ADDITION;
/*     */         case "multiply_base":
/* 241 */           return AttributeModifier.Operation.MULTIPLY_BASE;
/*     */         case "multiply_total":
/* 243 */           return AttributeModifier.Operation.MULTIPLY_TOTAL;
/*     */       } 
/* 245 */       throw new JsonSyntaxException("Unknown attribute modifier operation " + debug0);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\storage\loot\functions\SetAttributesFunction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */