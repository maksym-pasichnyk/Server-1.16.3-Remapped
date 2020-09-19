/*    */ package org.apache.logging.log4j.core.appender;
/*    */ 
/*    */ import java.util.Objects;
/*    */ import java.util.concurrent.atomic.AtomicLong;
/*    */ import org.apache.logging.log4j.core.Layout;
/*    */ import org.apache.logging.log4j.core.LogEvent;
/*    */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*    */ import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
/*    */ import org.apache.logging.log4j.core.config.plugins.PluginFactory;
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
/*    */ @Plugin(name = "CountingNoOp", category = "Core", elementType = "appender", printObject = true)
/*    */ public class CountingNoOpAppender
/*    */   extends AbstractAppender
/*    */ {
/* 36 */   private final AtomicLong total = new AtomicLong();
/*    */   
/*    */   public CountingNoOpAppender(String name, Layout<?> layout) {
/* 39 */     super(name, null, (Layout)layout);
/*    */   }
/*    */   
/*    */   public long getCount() {
/* 43 */     return this.total.get();
/*    */   }
/*    */ 
/*    */   
/*    */   public void append(LogEvent event) {
/* 48 */     this.total.incrementAndGet();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @PluginFactory
/*    */   public static CountingNoOpAppender createAppender(@PluginAttribute("name") String name) {
/* 56 */     return new CountingNoOpAppender(Objects.<String>requireNonNull(name), null);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\appender\CountingNoOpAppender.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */