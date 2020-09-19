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
/*    */ public class XmlEntityReference
/*    */ {
/*    */   private final String name;
/*    */   private final String text;
/*    */   
/*    */   public XmlEntityReference(String name, String text) {
/* 27 */     this.name = name;
/* 28 */     this.text = text;
/*    */   }
/*    */   
/*    */   public String name() {
/* 32 */     return this.name;
/*    */   }
/*    */   
/*    */   public String text() {
/* 36 */     return this.text;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object o) {
/* 41 */     if (this == o) return true; 
/* 42 */     if (o == null || getClass() != o.getClass()) return false;
/*    */     
/* 44 */     XmlEntityReference that = (XmlEntityReference)o;
/*    */     
/* 46 */     if ((this.name != null) ? !this.name.equals(that.name) : (that.name != null)) return false; 
/* 47 */     if ((this.text != null) ? !this.text.equals(that.text) : (that.text != null)) return false;
/*    */     
/* 49 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 54 */     int result = (this.name != null) ? this.name.hashCode() : 0;
/* 55 */     result = 31 * result + ((this.text != null) ? this.text.hashCode() : 0);
/* 56 */     return result;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 61 */     return "XmlEntityReference{name='" + this.name + '\'' + ", text='" + this.text + '\'' + '}';
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\xml\XmlEntityReference.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */