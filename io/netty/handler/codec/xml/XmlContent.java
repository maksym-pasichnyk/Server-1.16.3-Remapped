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
/*    */ public abstract class XmlContent
/*    */ {
/*    */   private final String data;
/*    */   
/*    */   protected XmlContent(String data) {
/* 26 */     this.data = data;
/*    */   }
/*    */   
/*    */   public String data() {
/* 30 */     return this.data;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object o) {
/* 35 */     if (this == o) return true; 
/* 36 */     if (o == null || getClass() != o.getClass()) return false;
/*    */     
/* 38 */     XmlContent that = (XmlContent)o;
/*    */     
/* 40 */     if ((this.data != null) ? !this.data.equals(that.data) : (that.data != null)) return false;
/*    */     
/* 42 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 47 */     return (this.data != null) ? this.data.hashCode() : 0;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 52 */     return "XmlContent{data='" + this.data + '\'' + '}';
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\xml\XmlContent.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */