/*    */ package net.minecraft.server.players;
/*    */ 
/*    */ import com.google.gson.JsonObject;
/*    */ import com.mojang.authlib.GameProfile;
/*    */ import java.util.Date;
/*    */ import java.util.Objects;
/*    */ import java.util.UUID;
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.TextComponent;
/*    */ 
/*    */ public class UserBanListEntry
/*    */   extends BanListEntry<GameProfile> {
/*    */   public UserBanListEntry(GameProfile debug1) {
/* 15 */     this(debug1, null, null, null, null);
/*    */   }
/*    */   
/*    */   public UserBanListEntry(GameProfile debug1, @Nullable Date debug2, @Nullable String debug3, @Nullable Date debug4, @Nullable String debug5) {
/* 19 */     super(debug1, debug2, debug3, debug4, debug5);
/*    */   }
/*    */   
/*    */   public UserBanListEntry(JsonObject debug1) {
/* 23 */     super(createGameProfile(debug1), debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void serialize(JsonObject debug1) {
/* 28 */     if (getUser() == null) {
/*    */       return;
/*    */     }
/* 31 */     debug1.addProperty("uuid", (getUser().getId() == null) ? "" : getUser().getId().toString());
/* 32 */     debug1.addProperty("name", getUser().getName());
/* 33 */     super.serialize(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public Component getDisplayName() {
/* 38 */     GameProfile debug1 = getUser();
/* 39 */     return (Component)new TextComponent((debug1.getName() != null) ? debug1.getName() : Objects.toString(debug1.getId(), "(Unknown)"));
/*    */   }
/*    */   private static GameProfile createGameProfile(JsonObject debug0) {
/*    */     UUID debug2;
/* 43 */     if (!debug0.has("uuid") || !debug0.has("name")) {
/* 44 */       return null;
/*    */     }
/* 46 */     String debug1 = debug0.get("uuid").getAsString();
/*    */     
/*    */     try {
/* 49 */       debug2 = UUID.fromString(debug1);
/* 50 */     } catch (Throwable debug3) {
/* 51 */       return null;
/*    */     } 
/* 53 */     return new GameProfile(debug2, debug0.get("name").getAsString());
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\players\UserBanListEntry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */