/*    */ package net.minecraft.server.players;
/*    */ 
/*    */ import com.google.gson.JsonObject;
/*    */ import java.util.Date;
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.TextComponent;
/*    */ 
/*    */ public class IpBanListEntry
/*    */   extends BanListEntry<String> {
/*    */   public IpBanListEntry(String debug1) {
/* 12 */     this(debug1, null, null, null, null);
/*    */   }
/*    */   
/*    */   public IpBanListEntry(String debug1, @Nullable Date debug2, @Nullable String debug3, @Nullable Date debug4, @Nullable String debug5) {
/* 16 */     super(debug1, debug2, debug3, debug4, debug5);
/*    */   }
/*    */ 
/*    */   
/*    */   public Component getDisplayName() {
/* 21 */     return (Component)new TextComponent(getUser());
/*    */   }
/*    */   
/*    */   public IpBanListEntry(JsonObject debug1) {
/* 25 */     super(createIpInfo(debug1), debug1);
/*    */   }
/*    */   
/*    */   private static String createIpInfo(JsonObject debug0) {
/* 29 */     return debug0.has("ip") ? debug0.get("ip").getAsString() : null;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void serialize(JsonObject debug1) {
/* 34 */     if (getUser() == null) {
/*    */       return;
/*    */     }
/* 37 */     debug1.addProperty("ip", getUser());
/* 38 */     super.serialize(debug1);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\players\IpBanListEntry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */