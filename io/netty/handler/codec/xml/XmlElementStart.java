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
/*    */ public class XmlElementStart
/*    */   extends XmlElement
/*    */ {
/* 26 */   private final List<XmlAttribute> attributes = new LinkedList<XmlAttribute>();
/*    */   
/*    */   public XmlElementStart(String name, String namespace, String prefix) {
/* 29 */     super(name, namespace, prefix);
/*    */   }
/*    */   
/*    */   public List<XmlAttribute> attributes() {
/* 33 */     return this.attributes;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object o) {
/* 38 */     if (this == o) return true; 
/* 39 */     if (o == null || getClass() != o.getClass()) return false; 
/* 40 */     if (!super.equals(o)) return false;
/*    */     
/* 42 */     XmlElementStart that = (XmlElementStart)o;
/*    */     
/* 44 */     if ((this.attributes != null) ? !this.attributes.equals(that.attributes) : (that.attributes != null)) return false;
/*    */     
/* 46 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 51 */     int result = super.hashCode();
/* 52 */     result = 31 * result + ((this.attributes != null) ? this.attributes.hashCode() : 0);
/* 53 */     return result;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 58 */     return "XmlElementStart{attributes=" + this.attributes + super
/*    */       
/* 60 */       .toString() + "} ";
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\xml\XmlElementStart.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */