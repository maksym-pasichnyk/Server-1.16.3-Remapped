/*    */ package net.minecraft.server.players;
/*    */ 
/*    */ import com.google.gson.JsonObject;
/*    */ import java.io.File;
/*    */ import java.net.SocketAddress;
/*    */ 
/*    */ public class IpBanList
/*    */   extends StoredUserList<String, IpBanListEntry> {
/*    */   public IpBanList(File debug1) {
/* 10 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   protected StoredUserEntry<String> createEntry(JsonObject debug1) {
/* 15 */     return new IpBanListEntry(debug1);
/*    */   }
/*    */   
/*    */   public boolean isBanned(SocketAddress debug1) {
/* 19 */     String debug2 = getIpFromAddress(debug1);
/* 20 */     return contains(debug2);
/*    */   }
/*    */   
/*    */   public boolean isBanned(String debug1) {
/* 24 */     return contains(debug1);
/*    */   }
/*    */   
/*    */   public IpBanListEntry get(SocketAddress debug1) {
/* 28 */     String debug2 = getIpFromAddress(debug1);
/* 29 */     return get(debug2);
/*    */   }
/*    */   
/*    */   private String getIpFromAddress(SocketAddress debug1) {
/* 33 */     String debug2 = debug1.toString();
/* 34 */     if (debug2.contains("/")) {
/* 35 */       debug2 = debug2.substring(debug2.indexOf('/') + 1);
/*    */     }
/* 37 */     if (debug2.contains(":")) {
/* 38 */       debug2 = debug2.substring(0, debug2.indexOf(':'));
/*    */     }
/* 40 */     return debug2;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\players\IpBanList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */