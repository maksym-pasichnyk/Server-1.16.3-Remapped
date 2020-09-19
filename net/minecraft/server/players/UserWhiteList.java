/*    */ package net.minecraft.server.players;
/*    */ 
/*    */ import com.google.gson.JsonObject;
/*    */ import com.mojang.authlib.GameProfile;
/*    */ import java.io.File;
/*    */ 
/*    */ public class UserWhiteList
/*    */   extends StoredUserList<GameProfile, UserWhiteListEntry> {
/*    */   public UserWhiteList(File debug1) {
/* 10 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   protected StoredUserEntry<GameProfile> createEntry(JsonObject debug1) {
/* 15 */     return new UserWhiteListEntry(debug1);
/*    */   }
/*    */   
/*    */   public boolean isWhiteListed(GameProfile debug1) {
/* 19 */     return contains(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public String[] getUserList() {
/* 24 */     String[] debug1 = new String[getEntries().size()];
/* 25 */     int debug2 = 0;
/* 26 */     for (StoredUserEntry<GameProfile> debug4 : getEntries()) {
/* 27 */       debug1[debug2++] = ((GameProfile)debug4.getUser()).getName();
/*    */     }
/* 29 */     return debug1;
/*    */   }
/*    */ 
/*    */   
/*    */   protected String getKeyForUser(GameProfile debug1) {
/* 34 */     return debug1.getId().toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\players\UserWhiteList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */