/*    */ package org.apache.logging.log4j.core.jmx;
/*    */ 
/*    */ import java.util.Objects;
/*    */ import javax.management.ObjectName;
/*    */ import org.apache.logging.log4j.core.selector.ContextSelector;
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
/*    */ public class ContextSelectorAdmin
/*    */   implements ContextSelectorAdminMBean
/*    */ {
/*    */   private final ObjectName objectName;
/*    */   private final ContextSelector selector;
/*    */   
/*    */   public ContextSelectorAdmin(String contextName, ContextSelector selector) {
/* 47 */     this.selector = Objects.<ContextSelector>requireNonNull(selector, "ContextSelector");
/*    */     try {
/* 49 */       String mbeanName = String.format("org.apache.logging.log4j2:type=%s,component=ContextSelector", new Object[] { Server.escape(contextName) });
/* 50 */       this.objectName = new ObjectName(mbeanName);
/* 51 */     } catch (Exception e) {
/* 52 */       throw new IllegalStateException(e);
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ObjectName getObjectName() {
/* 63 */     return this.objectName;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getImplementationClassName() {
/* 68 */     return this.selector.getClass().getName();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\jmx\ContextSelectorAdmin.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */