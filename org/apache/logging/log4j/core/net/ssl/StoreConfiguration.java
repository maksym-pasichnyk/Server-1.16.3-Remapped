/*    */ package org.apache.logging.log4j.core.net.ssl;
/*    */ 
/*    */ import org.apache.logging.log4j.status.StatusLogger;
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
/*    */ public class StoreConfiguration<T>
/*    */ {
/* 25 */   protected static final StatusLogger LOGGER = StatusLogger.getLogger();
/*    */   
/*    */   private String location;
/*    */   private String password;
/*    */   
/*    */   public StoreConfiguration(String location, String password) {
/* 31 */     this.location = location;
/* 32 */     this.password = password;
/*    */   }
/*    */   
/*    */   public String getLocation() {
/* 36 */     return this.location;
/*    */   }
/*    */   
/*    */   public void setLocation(String location) {
/* 40 */     this.location = location;
/*    */   }
/*    */   
/*    */   public String getPassword() {
/* 44 */     return this.password;
/*    */   }
/*    */   
/*    */   public char[] getPasswordAsCharArray() {
/* 48 */     return (this.password == null) ? null : this.password.toCharArray();
/*    */   }
/*    */   
/*    */   public void setPassword(String password) {
/* 52 */     this.password = password;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected T load() throws StoreConfigurationException {
/* 59 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 64 */     int prime = 31;
/* 65 */     int result = 1;
/* 66 */     result = 31 * result + ((this.location == null) ? 0 : this.location.hashCode());
/* 67 */     result = 31 * result + ((this.password == null) ? 0 : this.password.hashCode());
/* 68 */     return result;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object obj) {
/* 73 */     if (this == obj) {
/* 74 */       return true;
/*    */     }
/* 76 */     if (obj == null) {
/* 77 */       return false;
/*    */     }
/* 79 */     if (!(obj instanceof StoreConfiguration)) {
/* 80 */       return false;
/*    */     }
/* 82 */     StoreConfiguration<?> other = (StoreConfiguration)obj;
/* 83 */     if (this.location == null) {
/* 84 */       if (other.location != null) {
/* 85 */         return false;
/*    */       }
/* 87 */     } else if (!this.location.equals(other.location)) {
/* 88 */       return false;
/*    */     } 
/* 90 */     if (this.password == null) {
/* 91 */       if (other.password != null) {
/* 92 */         return false;
/*    */       }
/* 94 */     } else if (!this.password.equals(other.password)) {
/* 95 */       return false;
/*    */     } 
/* 97 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\net\ssl\StoreConfiguration.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */