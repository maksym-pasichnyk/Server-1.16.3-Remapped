/*    */ package org.apache.logging.log4j.core.lookup;
/*    */ 
/*    */ import java.lang.management.ManagementFactory;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import org.apache.logging.log4j.core.config.plugins.Plugin;
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
/*    */ @Plugin(name = "jvmrunargs", category = "Lookup")
/*    */ public class JmxRuntimeInputArgumentsLookup
/*    */   extends MapLookup
/*    */ {
/*    */   public static final JmxRuntimeInputArgumentsLookup JMX_SINGLETON;
/*    */   
/*    */   static {
/* 35 */     List<String> argsList = ManagementFactory.getRuntimeMXBean().getInputArguments();
/* 36 */     JMX_SINGLETON = new JmxRuntimeInputArgumentsLookup(MapLookup.toMap(argsList));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public JmxRuntimeInputArgumentsLookup() {}
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public JmxRuntimeInputArgumentsLookup(Map<String, String> map) {
/* 49 */     super(map);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\lookup\JmxRuntimeInputArgumentsLookup.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */