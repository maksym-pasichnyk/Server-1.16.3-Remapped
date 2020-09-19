/*    */ package net.minecraft.server.players;
/*    */ 
/*    */ import com.google.gson.JsonObject;
/*    */ import com.mojang.authlib.GameProfile;
/*    */ import java.io.File;
/*    */ 
/*    */ public class ServerOpList
/*    */   extends StoredUserList<GameProfile, ServerOpListEntry>
/*    */ {
/*    */   public ServerOpList(File debug1) {
/* 11 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   protected StoredUserEntry<GameProfile> createEntry(JsonObject debug1) {
/* 16 */     return new ServerOpListEntry(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public String[] getUserList() {
/* 21 */     String[] debug1 = new String[getEntries().size()];
/* 22 */     int debug2 = 0;
/* 23 */     for (StoredUserEntry<GameProfile> debug4 : getEntries()) {
/* 24 */       debug1[debug2++] = ((GameProfile)debug4.getUser()).getName();
/*    */     }
/* 26 */     return debug1;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean canBypassPlayerLimit(GameProfile debug1) {
/* 40 */     ServerOpListEntry debug2 = get(debug1);
/*    */     
/* 42 */     if (debug2 != null) {
/* 43 */       return debug2.getBypassesPlayerLimit();
/*    */     }
/*    */     
/* 46 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   protected String getKeyForUser(GameProfile debug1) {
/* 51 */     return debug1.getId().toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\players\ServerOpList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */