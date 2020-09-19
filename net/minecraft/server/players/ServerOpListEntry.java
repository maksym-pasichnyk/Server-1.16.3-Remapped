/*    */ package net.minecraft.server.players;
/*    */ 
/*    */ import com.google.gson.JsonObject;
/*    */ import com.mojang.authlib.GameProfile;
/*    */ import java.util.UUID;
/*    */ 
/*    */ public class ServerOpListEntry
/*    */   extends StoredUserEntry<GameProfile>
/*    */ {
/*    */   private final int level;
/*    */   private final boolean bypassesPlayerLimit;
/*    */   
/*    */   public ServerOpListEntry(GameProfile debug1, int debug2, boolean debug3) {
/* 14 */     super(debug1);
/* 15 */     this.level = debug2;
/* 16 */     this.bypassesPlayerLimit = debug3;
/*    */   }
/*    */   
/*    */   public ServerOpListEntry(JsonObject debug1) {
/* 20 */     super(createGameProfile(debug1));
/* 21 */     this.level = debug1.has("level") ? debug1.get("level").getAsInt() : 0;
/* 22 */     this.bypassesPlayerLimit = (debug1.has("bypassesPlayerLimit") && debug1.get("bypassesPlayerLimit").getAsBoolean());
/*    */   }
/*    */   
/*    */   public int getLevel() {
/* 26 */     return this.level;
/*    */   }
/*    */   
/*    */   public boolean getBypassesPlayerLimit() {
/* 30 */     return this.bypassesPlayerLimit;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void serialize(JsonObject debug1) {
/* 35 */     if (getUser() == null) {
/*    */       return;
/*    */     }
/* 38 */     debug1.addProperty("uuid", (getUser().getId() == null) ? "" : getUser().getId().toString());
/* 39 */     debug1.addProperty("name", getUser().getName());
/* 40 */     debug1.addProperty("level", Integer.valueOf(this.level));
/* 41 */     debug1.addProperty("bypassesPlayerLimit", Boolean.valueOf(this.bypassesPlayerLimit));
/*    */   }
/*    */   private static GameProfile createGameProfile(JsonObject debug0) {
/*    */     UUID debug2;
/* 45 */     if (!debug0.has("uuid") || !debug0.has("name")) {
/* 46 */       return null;
/*    */     }
/* 48 */     String debug1 = debug0.get("uuid").getAsString();
/*    */     
/*    */     try {
/* 51 */       debug2 = UUID.fromString(debug1);
/* 52 */     } catch (Throwable debug3) {
/* 53 */       return null;
/*    */     } 
/* 55 */     return new GameProfile(debug2, debug0.get("name").getAsString());
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\players\ServerOpListEntry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */