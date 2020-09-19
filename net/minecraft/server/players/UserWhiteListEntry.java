/*    */ package net.minecraft.server.players;
/*    */ 
/*    */ import com.google.gson.JsonObject;
/*    */ import com.mojang.authlib.GameProfile;
/*    */ import java.util.UUID;
/*    */ 
/*    */ public class UserWhiteListEntry
/*    */   extends StoredUserEntry<GameProfile> {
/*    */   public UserWhiteListEntry(GameProfile debug1) {
/* 10 */     super(debug1);
/*    */   }
/*    */   
/*    */   public UserWhiteListEntry(JsonObject debug1) {
/* 14 */     super(createGameProfile(debug1));
/*    */   }
/*    */ 
/*    */   
/*    */   protected void serialize(JsonObject debug1) {
/* 19 */     if (getUser() == null) {
/*    */       return;
/*    */     }
/* 22 */     debug1.addProperty("uuid", (getUser().getId() == null) ? "" : getUser().getId().toString());
/* 23 */     debug1.addProperty("name", getUser().getName());
/*    */   }
/*    */   private static GameProfile createGameProfile(JsonObject debug0) {
/*    */     UUID debug2;
/* 27 */     if (!debug0.has("uuid") || !debug0.has("name")) {
/* 28 */       return null;
/*    */     }
/* 30 */     String debug1 = debug0.get("uuid").getAsString();
/*    */     
/*    */     try {
/* 33 */       debug2 = UUID.fromString(debug1);
/* 34 */     } catch (Throwable debug3) {
/* 35 */       return null;
/*    */     } 
/* 37 */     return new GameProfile(debug2, debug0.get("name").getAsString());
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\players\UserWhiteListEntry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */