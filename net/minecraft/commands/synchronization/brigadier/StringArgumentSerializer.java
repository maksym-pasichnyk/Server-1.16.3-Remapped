/*    */ package net.minecraft.commands.synchronization.brigadier;
/*    */ 
/*    */ import com.google.gson.JsonObject;
/*    */ import com.mojang.brigadier.arguments.ArgumentType;
/*    */ import com.mojang.brigadier.arguments.StringArgumentType;
/*    */ import net.minecraft.commands.synchronization.ArgumentSerializer;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ 
/*    */ public class StringArgumentSerializer implements ArgumentSerializer<StringArgumentType> {
/*    */   public void serializeToNetwork(StringArgumentType debug1, FriendlyByteBuf debug2) {
/* 11 */     debug2.writeEnum((Enum)debug1.getType());
/*    */   }
/*    */ 
/*    */   
/*    */   public StringArgumentType deserializeFromNetwork(FriendlyByteBuf debug1) {
/* 16 */     StringArgumentType.StringType debug2 = (StringArgumentType.StringType)debug1.readEnum(StringArgumentType.StringType.class);
/* 17 */     switch (debug2) {
/*    */       case SINGLE_WORD:
/* 19 */         return StringArgumentType.word();
/*    */       case QUOTABLE_PHRASE:
/* 21 */         return StringArgumentType.string();
/*    */     } 
/*    */     
/* 24 */     return StringArgumentType.greedyString();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void serializeToJson(StringArgumentType debug1, JsonObject debug2) {
/* 30 */     switch (debug1.getType()) {
/*    */       case SINGLE_WORD:
/* 32 */         debug2.addProperty("type", "word");
/*    */         return;
/*    */       case QUOTABLE_PHRASE:
/* 35 */         debug2.addProperty("type", "phrase");
/*    */         return;
/*    */     } 
/*    */     
/* 39 */     debug2.addProperty("type", "greedy");
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\commands\synchronization\brigadier\StringArgumentSerializer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */