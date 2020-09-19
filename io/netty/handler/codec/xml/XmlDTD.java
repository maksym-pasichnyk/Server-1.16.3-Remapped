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
/*    */ public class XmlDTD
/*    */ {
/*    */   private final String text;
/*    */   
/*    */   public XmlDTD(String text) {
/* 26 */     this.text = text;
/*    */   }
/*    */   
/*    */   public String text() {
/* 30 */     return this.text;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object o) {
/* 35 */     if (this == o) return true; 
/* 36 */     if (o == null || getClass() != o.getClass()) return false;
/*    */     
/* 38 */     XmlDTD xmlDTD = (XmlDTD)o;
/*    */     
/* 40 */     if ((this.text != null) ? !this.text.equals(xmlDTD.text) : (xmlDTD.text != null)) return false;
/*    */     
/* 42 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 47 */     return (this.text != null) ? this.text.hashCode() : 0;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 52 */     return "XmlDTD{text='" + this.text + '\'' + '}';
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\xml\XmlDTD.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */