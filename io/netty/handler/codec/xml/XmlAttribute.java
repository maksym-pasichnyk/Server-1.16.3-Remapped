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
/*    */ 
/*    */ public class XmlAttribute
/*    */ {
/*    */   private final String type;
/*    */   private final String name;
/*    */   private final String prefix;
/*    */   private final String namespace;
/*    */   private final String value;
/*    */   
/*    */   public XmlAttribute(String type, String name, String prefix, String namespace, String value) {
/* 31 */     this.type = type;
/* 32 */     this.name = name;
/* 33 */     this.prefix = prefix;
/* 34 */     this.namespace = namespace;
/* 35 */     this.value = value;
/*    */   }
/*    */   
/*    */   public String type() {
/* 39 */     return this.type;
/*    */   }
/*    */   
/*    */   public String name() {
/* 43 */     return this.name;
/*    */   }
/*    */   
/*    */   public String prefix() {
/* 47 */     return this.prefix;
/*    */   }
/*    */   
/*    */   public String namespace() {
/* 51 */     return this.namespace;
/*    */   }
/*    */   
/*    */   public String value() {
/* 55 */     return this.value;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object o) {
/* 60 */     if (this == o) return true; 
/* 61 */     if (o == null || getClass() != o.getClass()) return false;
/*    */     
/* 63 */     XmlAttribute that = (XmlAttribute)o;
/*    */     
/* 65 */     if (!this.name.equals(that.name)) return false; 
/* 66 */     if ((this.namespace != null) ? !this.namespace.equals(that.namespace) : (that.namespace != null)) return false; 
/* 67 */     if ((this.prefix != null) ? !this.prefix.equals(that.prefix) : (that.prefix != null)) return false; 
/* 68 */     if ((this.type != null) ? !this.type.equals(that.type) : (that.type != null)) return false; 
/* 69 */     if ((this.value != null) ? !this.value.equals(that.value) : (that.value != null)) return false;
/*    */     
/* 71 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 76 */     int result = (this.type != null) ? this.type.hashCode() : 0;
/* 77 */     result = 31 * result + this.name.hashCode();
/* 78 */     result = 31 * result + ((this.prefix != null) ? this.prefix.hashCode() : 0);
/* 79 */     result = 31 * result + ((this.namespace != null) ? this.namespace.hashCode() : 0);
/* 80 */     result = 31 * result + ((this.value != null) ? this.value.hashCode() : 0);
/* 81 */     return result;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 86 */     return "XmlAttribute{type='" + this.type + '\'' + ", name='" + this.name + '\'' + ", prefix='" + this.prefix + '\'' + ", namespace='" + this.namespace + '\'' + ", value='" + this.value + '\'' + '}';
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\xml\XmlAttribute.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */