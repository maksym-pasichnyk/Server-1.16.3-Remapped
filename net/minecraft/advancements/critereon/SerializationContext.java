/*    */ package net.minecraft.advancements.critereon;
/*    */ 
/*    */ import com.google.gson.Gson;
/*    */ import com.google.gson.JsonElement;
/*    */ import net.minecraft.world.level.storage.loot.Deserializers;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ 
/*    */ public class SerializationContext
/*    */ {
/* 10 */   public static final SerializationContext INSTANCE = new SerializationContext();
/*    */   
/* 12 */   private final Gson predicateGson = Deserializers.createConditionSerializer().create();
/*    */   
/*    */   public final JsonElement serializeConditions(LootItemCondition[] debug1) {
/* 15 */     return this.predicateGson.toJsonTree(debug1);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\advancements\critereon\SerializationContext.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */