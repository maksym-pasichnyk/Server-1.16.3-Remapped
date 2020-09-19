/*    */ package net.minecraft.server.players;
/*    */ 
/*    */ import com.google.gson.JsonObject;
/*    */ import java.text.ParseException;
/*    */ import java.text.SimpleDateFormat;
/*    */ import java.util.Date;
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.network.chat.Component;
/*    */ 
/*    */ public abstract class BanListEntry<T>
/*    */   extends StoredUserEntry<T> {
/* 12 */   public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
/*    */   
/*    */   protected final Date created;
/*    */   
/*    */   protected final String source;
/*    */   protected final Date expires;
/*    */   protected final String reason;
/*    */   
/*    */   public BanListEntry(T debug1, @Nullable Date debug2, @Nullable String debug3, @Nullable Date debug4, @Nullable String debug5) {
/* 21 */     super(debug1);
/* 22 */     this.created = (debug2 == null) ? new Date() : debug2;
/* 23 */     this.source = (debug3 == null) ? "(Unknown)" : debug3;
/* 24 */     this.expires = debug4;
/* 25 */     this.reason = (debug5 == null) ? "Banned by an operator." : debug5;
/*    */   }
/*    */   
/*    */   protected BanListEntry(T debug1, JsonObject debug2) {
/* 29 */     super(debug1);
/*    */     Date debug3, debug4;
/*    */     try {
/* 32 */       debug3 = debug2.has("created") ? DATE_FORMAT.parse(debug2.get("created").getAsString()) : new Date();
/* 33 */     } catch (ParseException parseException) {
/* 34 */       debug3 = new Date();
/*    */     } 
/* 36 */     this.created = debug3;
/* 37 */     this.source = debug2.has("source") ? debug2.get("source").getAsString() : "(Unknown)";
/*    */     
/*    */     try {
/* 40 */       debug4 = debug2.has("expires") ? DATE_FORMAT.parse(debug2.get("expires").getAsString()) : null;
/* 41 */     } catch (ParseException debug5) {
/* 42 */       debug4 = null;
/*    */     } 
/* 44 */     this.expires = debug4;
/* 45 */     this.reason = debug2.has("reason") ? debug2.get("reason").getAsString() : "Banned by an operator.";
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getSource() {
/* 53 */     return this.source;
/*    */   }
/*    */   
/*    */   public Date getExpires() {
/* 57 */     return this.expires;
/*    */   }
/*    */   
/*    */   public String getReason() {
/* 61 */     return this.reason;
/*    */   }
/*    */ 
/*    */   
/*    */   public abstract Component getDisplayName();
/*    */   
/*    */   boolean hasExpired() {
/* 68 */     if (this.expires == null) {
/* 69 */       return false;
/*    */     }
/* 71 */     return this.expires.before(new Date());
/*    */   }
/*    */ 
/*    */   
/*    */   protected void serialize(JsonObject debug1) {
/* 76 */     debug1.addProperty("created", DATE_FORMAT.format(this.created));
/* 77 */     debug1.addProperty("source", this.source);
/* 78 */     debug1.addProperty("expires", (this.expires == null) ? "forever" : DATE_FORMAT.format(this.expires));
/* 79 */     debug1.addProperty("reason", this.reason);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\players\BanListEntry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */