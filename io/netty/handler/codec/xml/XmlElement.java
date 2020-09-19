/*    */ package io.netty.handler.codec.xml;
/*    */ 
/*    */ import java.util.LinkedList;
/*    */ import java.util.List;
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
/*    */ public abstract class XmlElement
/*    */ {
/*    */   private final String name;
/*    */   private final String namespace;
/*    */   private final String prefix;
/* 31 */   private final List<XmlNamespace> namespaces = new LinkedList<XmlNamespace>();
/*    */   
/*    */   protected XmlElement(String name, String namespace, String prefix) {
/* 34 */     this.name = name;
/* 35 */     this.namespace = namespace;
/* 36 */     this.prefix = prefix;
/*    */   }
/*    */   
/*    */   public String name() {
/* 40 */     return this.name;
/*    */   }
/*    */   
/*    */   public String namespace() {
/* 44 */     return this.namespace;
/*    */   }
/*    */   
/*    */   public String prefix() {
/* 48 */     return this.prefix;
/*    */   }
/*    */   
/*    */   public List<XmlNamespace> namespaces() {
/* 52 */     return this.namespaces;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object o) {
/* 57 */     if (this == o) return true; 
/* 58 */     if (o == null || getClass() != o.getClass()) return false;
/*    */     
/* 60 */     XmlElement that = (XmlElement)o;
/*    */     
/* 62 */     if (!this.name.equals(that.name)) return false; 
/* 63 */     if ((this.namespace != null) ? !this.namespace.equals(that.namespace) : (that.namespace != null)) return false; 
/* 64 */     if ((this.namespaces != null) ? !this.namespaces.equals(that.namespaces) : (that.namespaces != null)) return false; 
/* 65 */     if ((this.prefix != null) ? !this.prefix.equals(that.prefix) : (that.prefix != null)) return false;
/*    */     
/* 67 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 72 */     int result = this.name.hashCode();
/* 73 */     result = 31 * result + ((this.namespace != null) ? this.namespace.hashCode() : 0);
/* 74 */     result = 31 * result + ((this.prefix != null) ? this.prefix.hashCode() : 0);
/* 75 */     result = 31 * result + ((this.namespaces != null) ? this.namespaces.hashCode() : 0);
/* 76 */     return result;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 81 */     return ", name='" + this.name + '\'' + ", namespace='" + this.namespace + '\'' + ", prefix='" + this.prefix + '\'' + ", namespaces=" + this.namespaces;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\xml\XmlElement.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */