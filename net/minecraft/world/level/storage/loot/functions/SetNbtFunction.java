/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ 
/*    */ import com.google.gson.JsonDeserializationContext;
/*    */ import com.google.gson.JsonObject;
/*    */ import com.google.gson.JsonSerializationContext;
/*    */ import com.google.gson.JsonSyntaxException;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.nbt.TagParser;
/*    */ import net.minecraft.util.GsonHelper;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ 
/*    */ public class SetNbtFunction extends LootItemConditionalFunction {
/*    */   private final CompoundTag tag;
/*    */   
/*    */   private SetNbtFunction(LootItemCondition[] debug1, CompoundTag debug2) {
/* 19 */     super(debug1);
/* 20 */     this.tag = debug2;
/*    */   }
/*    */ 
/*    */   
/*    */   public LootItemFunctionType getType() {
/* 25 */     return LootItemFunctions.SET_NBT;
/*    */   }
/*    */ 
/*    */   
/*    */   public ItemStack run(ItemStack debug1, LootContext debug2) {
/* 30 */     debug1.getOrCreateTag().merge(this.tag);
/* 31 */     return debug1;
/*    */   }
/*    */   
/*    */   public static LootItemConditionalFunction.Builder<?> setTag(CompoundTag debug0) {
/* 35 */     return simpleBuilder(debug1 -> new SetNbtFunction(debug1, debug0));
/*    */   }
/*    */   
/*    */   public static class Serializer
/*    */     extends LootItemConditionalFunction.Serializer<SetNbtFunction> {
/*    */     public void serialize(JsonObject debug1, SetNbtFunction debug2, JsonSerializationContext debug3) {
/* 41 */       super.serialize(debug1, debug2, debug3);
/*    */       
/* 43 */       debug1.addProperty("tag", debug2.tag.toString());
/*    */     }
/*    */ 
/*    */     
/*    */     public SetNbtFunction deserialize(JsonObject debug1, JsonDeserializationContext debug2, LootItemCondition[] debug3) {
/*    */       try {
/* 49 */         CompoundTag debug4 = TagParser.parseTag(GsonHelper.getAsString(debug1, "tag"));
/* 50 */         return new SetNbtFunction(debug3, debug4);
/* 51 */       } catch (CommandSyntaxException debug4) {
/* 52 */         throw new JsonSyntaxException(debug4.getMessage());
/*    */       } 
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\storage\loot\functions\SetNbtFunction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */