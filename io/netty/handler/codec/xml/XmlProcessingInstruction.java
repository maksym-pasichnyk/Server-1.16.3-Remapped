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
/*    */ public class XmlProcessingInstruction
/*    */ {
/*    */   private final String data;
/*    */   private final String target;
/*    */   
/*    */   public XmlProcessingInstruction(String data, String target) {
/* 27 */     this.data = data;
/* 28 */     this.target = target;
/*    */   }
/*    */   
/*    */   public String data() {
/* 32 */     return this.data;
/*    */   }
/*    */   
/*    */   public String target() {
/* 36 */     return this.target;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object o) {
/* 41 */     if (this == o) return true; 
/* 42 */     if (o == null || getClass() != o.getClass()) return false;
/*    */     
/* 44 */     XmlProcessingInstruction that = (XmlProcessingInstruction)o;
/*    */     
/* 46 */     if ((this.data != null) ? !this.data.equals(that.data) : (that.data != null)) return false; 
/* 47 */     if ((this.target != null) ? !this.target.equals(that.target) : (that.target != null)) return false;
/*    */     
/* 49 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 54 */     int result = (this.data != null) ? this.data.hashCode() : 0;
/* 55 */     result = 31 * result + ((this.target != null) ? this.target.hashCode() : 0);
/* 56 */     return result;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 61 */     return "XmlProcessingInstruction{data='" + this.data + '\'' + ", target='" + this.target + '\'' + '}';
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\xml\XmlProcessingInstruction.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */