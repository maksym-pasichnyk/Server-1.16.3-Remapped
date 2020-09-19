/*    */ package io.netty.handler.codec.xml;
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
/*    */ public class XmlNamespace
/*    */ {
/*    */   private final String prefix;
/*    */   private final String uri;
/*    */   
/*    */   public XmlNamespace(String prefix, String uri) {
/* 27 */     this.prefix = prefix;
/* 28 */     this.uri = uri;
/*    */   }
/*    */   
/*    */   public String prefix() {
/* 32 */     return this.prefix;
/*    */   }
/*    */   
/*    */   public String uri() {
/* 36 */     return this.uri;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object o) {
/* 41 */     if (this == o) return true; 
/* 42 */     if (o == null || getClass() != o.getClass()) return false;
/*    */     
/* 44 */     XmlNamespace that = (XmlNamespace)o;
/*    */     
/* 46 */     if ((this.prefix != null) ? !this.prefix.equals(that.prefix) : (that.prefix != null)) return false; 
/* 47 */     if ((this.uri != null) ? !this.uri.equals(that.uri) : (that.uri != null)) return false;
/*    */     
/* 49 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 54 */     int result = (this.prefix != null) ? this.prefix.hashCode() : 0;
/* 55 */     result = 31 * result + ((this.uri != null) ? this.uri.hashCode() : 0);
/* 56 */     return result;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 61 */     return "XmlNamespace{prefix='" + this.prefix + '\'' + ", uri='" + this.uri + '\'' + '}';
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\xml\XmlNamespace.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */