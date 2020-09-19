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
/*    */ public class XmlDocumentStart
/*    */ {
/*    */   private final String encoding;
/*    */   private final String version;
/*    */   private final boolean standalone;
/*    */   private final String encodingScheme;
/*    */   
/*    */   public XmlDocumentStart(String encoding, String version, boolean standalone, String encodingScheme) {
/* 29 */     this.encoding = encoding;
/* 30 */     this.version = version;
/* 31 */     this.standalone = standalone;
/* 32 */     this.encodingScheme = encodingScheme;
/*    */   }
/*    */ 
/*    */   
/*    */   public String encoding() {
/* 37 */     return this.encoding;
/*    */   }
/*    */ 
/*    */   
/*    */   public String version() {
/* 42 */     return this.version;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean standalone() {
/* 47 */     return this.standalone;
/*    */   }
/*    */ 
/*    */   
/*    */   public String encodingScheme() {
/* 52 */     return this.encodingScheme;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object o) {
/* 57 */     if (this == o) return true; 
/* 58 */     if (o == null || getClass() != o.getClass()) return false;
/*    */     
/* 60 */     XmlDocumentStart that = (XmlDocumentStart)o;
/*    */     
/* 62 */     if (this.standalone != that.standalone) return false; 
/* 63 */     if ((this.encoding != null) ? !this.encoding.equals(that.encoding) : (that.encoding != null)) return false; 
/* 64 */     if ((this.encodingScheme != null) ? !this.encodingScheme.equals(that.encodingScheme) : (that.encodingScheme != null)) {
/* 65 */       return false;
/*    */     }
/* 67 */     if ((this.version != null) ? !this.version.equals(that.version) : (that.version != null)) return false;
/*    */     
/* 69 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 74 */     int result = (this.encoding != null) ? this.encoding.hashCode() : 0;
/* 75 */     result = 31 * result + ((this.version != null) ? this.version.hashCode() : 0);
/* 76 */     result = 31 * result + (this.standalone ? 1 : 0);
/* 77 */     result = 31 * result + ((this.encodingScheme != null) ? this.encodingScheme.hashCode() : 0);
/* 78 */     return result;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 83 */     return "XmlDocumentStart{encoding='" + this.encoding + '\'' + ", version='" + this.version + '\'' + ", standalone=" + this.standalone + ", encodingScheme='" + this.encodingScheme + '\'' + '}';
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\xml\XmlDocumentStart.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */