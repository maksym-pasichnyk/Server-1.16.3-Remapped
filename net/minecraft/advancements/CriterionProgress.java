/*    */ package net.minecraft.advancements;
/*    */ 
/*    */ import com.google.gson.JsonElement;
/*    */ import com.google.gson.JsonNull;
/*    */ import com.google.gson.JsonPrimitive;
/*    */ import com.google.gson.JsonSyntaxException;
/*    */ import java.text.ParseException;
/*    */ import java.text.SimpleDateFormat;
/*    */ import java.util.Date;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ 
/*    */ public class CriterionProgress
/*    */ {
/* 14 */   private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
/*    */   
/*    */   private Date obtained;
/*    */   
/*    */   public boolean isDone() {
/* 19 */     return (this.obtained != null);
/*    */   }
/*    */   
/*    */   public void grant() {
/* 23 */     this.obtained = new Date();
/*    */   }
/*    */   
/*    */   public void revoke() {
/* 27 */     this.obtained = null;
/*    */   }
/*    */   
/*    */   public Date getObtained() {
/* 31 */     return this.obtained;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 36 */     return "CriterionProgress{obtained=" + ((this.obtained == null) ? "false" : (String)this.obtained) + '}';
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void serializeToNetwork(FriendlyByteBuf debug1) {
/* 42 */     debug1.writeBoolean((this.obtained != null));
/* 43 */     if (this.obtained != null) {
/* 44 */       debug1.writeDate(this.obtained);
/*    */     }
/*    */   }
/*    */   
/*    */   public JsonElement serializeToJson() {
/* 49 */     if (this.obtained != null) {
/* 50 */       return (JsonElement)new JsonPrimitive(DATE_FORMAT.format(this.obtained));
/*    */     }
/* 52 */     return (JsonElement)JsonNull.INSTANCE;
/*    */   }
/*    */ 
/*    */   
/*    */   public static CriterionProgress fromNetwork(FriendlyByteBuf debug0) {
/* 57 */     CriterionProgress debug1 = new CriterionProgress();
/* 58 */     if (debug0.readBoolean()) {
/* 59 */       debug1.obtained = debug0.readDate();
/*    */     }
/* 61 */     return debug1;
/*    */   }
/*    */   
/*    */   public static CriterionProgress fromJson(String debug0) {
/* 65 */     CriterionProgress debug1 = new CriterionProgress();
/*    */     try {
/* 67 */       debug1.obtained = DATE_FORMAT.parse(debug0);
/* 68 */     } catch (ParseException debug2) {
/* 69 */       throw new JsonSyntaxException("Invalid datetime: " + debug0, debug2);
/*    */     } 
/* 71 */     return debug1;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\advancements\CriterionProgress.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */