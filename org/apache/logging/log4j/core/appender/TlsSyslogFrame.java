/*    */ package org.apache.logging.log4j.core.appender;
/*    */ 
/*    */ import java.nio.charset.StandardCharsets;
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
/*    */ 
/*    */ public class TlsSyslogFrame
/*    */ {
/*    */   private final String message;
/*    */   private final int byteLength;
/*    */   
/*    */   public TlsSyslogFrame(String message) {
/* 33 */     this.message = message;
/* 34 */     byte[] messageBytes = message.getBytes(StandardCharsets.UTF_8);
/* 35 */     this.byteLength = messageBytes.length;
/*    */   }
/*    */   
/*    */   public String getMessage() {
/* 39 */     return this.message;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 44 */     return Integer.toString(this.byteLength) + ' ' + this.message;
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 49 */     int prime = 31;
/* 50 */     int result = 1;
/* 51 */     result = 31 * result + ((this.message == null) ? 0 : this.message.hashCode());
/* 52 */     return result;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object obj) {
/* 57 */     if (this == obj) {
/* 58 */       return true;
/*    */     }
/* 60 */     if (obj == null) {
/* 61 */       return false;
/*    */     }
/* 63 */     if (!(obj instanceof TlsSyslogFrame)) {
/* 64 */       return false;
/*    */     }
/* 66 */     TlsSyslogFrame other = (TlsSyslogFrame)obj;
/* 67 */     if (this.message == null) {
/* 68 */       if (other.message != null) {
/* 69 */         return false;
/*    */       }
/* 71 */     } else if (!this.message.equals(other.message)) {
/* 72 */       return false;
/*    */     } 
/* 74 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\appender\TlsSyslogFrame.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */