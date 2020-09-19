/*     */ package org.apache.logging.log4j.core.appender.rewrite;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginElement;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginFactory;
/*     */ import org.apache.logging.log4j.core.impl.Log4jLogEvent;
/*     */ import org.apache.logging.log4j.core.util.KeyValuePair;
/*     */ import org.apache.logging.log4j.message.MapMessage;
/*     */ import org.apache.logging.log4j.message.Message;
/*     */ import org.apache.logging.log4j.status.StatusLogger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Plugin(name = "MapRewritePolicy", category = "Core", elementType = "rewritePolicy", printObject = true)
/*     */ public final class MapRewritePolicy
/*     */   implements RewritePolicy
/*     */ {
/*  43 */   protected static final Logger LOGGER = (Logger)StatusLogger.getLogger();
/*     */   
/*     */   private final Map<String, String> map;
/*     */   
/*     */   private final Mode mode;
/*     */   
/*     */   private MapRewritePolicy(Map<String, String> map, Mode mode) {
/*  50 */     this.map = map;
/*  51 */     this.mode = mode;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LogEvent rewrite(LogEvent source) {
/*  62 */     Message msg = source.getMessage();
/*  63 */     if (msg == null || !(msg instanceof MapMessage)) {
/*  64 */       return source;
/*     */     }
/*     */     
/*  67 */     Map<String, String> newMap = new HashMap<>(((MapMessage)msg).getData());
/*  68 */     switch (this.mode)
/*     */     { case Add:
/*  70 */         newMap.putAll(this.map);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*  81 */         message = ((MapMessage)msg).newInstance(newMap);
/*  82 */         log4jLogEvent = (new Log4jLogEvent.Builder(source)).setMessage((Message)message).build();
/*  83 */         return (LogEvent)log4jLogEvent; }  for (Map.Entry<String, String> entry : this.map.entrySet()) { if (newMap.containsKey(entry.getKey())) newMap.put(entry.getKey(), entry.getValue());  }  MapMessage message = ((MapMessage)msg).newInstance(newMap); Log4jLogEvent log4jLogEvent = (new Log4jLogEvent.Builder(source)).setMessage((Message)message).build(); return (LogEvent)log4jLogEvent;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public enum Mode
/*     */   {
/*  94 */     Add,
/*     */ 
/*     */ 
/*     */     
/*  98 */     Update;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 103 */     StringBuilder sb = new StringBuilder();
/* 104 */     sb.append("mode=").append(this.mode);
/* 105 */     sb.append(" {");
/* 106 */     boolean first = true;
/* 107 */     for (Map.Entry<String, String> entry : this.map.entrySet()) {
/* 108 */       if (!first) {
/* 109 */         sb.append(", ");
/*     */       }
/* 111 */       sb.append(entry.getKey()).append('=').append(entry.getValue());
/* 112 */       first = false;
/*     */     } 
/* 114 */     sb.append('}');
/* 115 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @PluginFactory
/*     */   public static MapRewritePolicy createPolicy(@PluginAttribute("mode") String mode, @PluginElement("KeyValuePair") KeyValuePair[] pairs) {
/* 128 */     Mode op = (mode == null) ? (op = Mode.Add) : Mode.valueOf(mode);
/* 129 */     if (pairs == null || pairs.length == 0) {
/* 130 */       LOGGER.error("keys and values must be specified for the MapRewritePolicy");
/* 131 */       return null;
/*     */     } 
/* 133 */     Map<String, String> map = new HashMap<>();
/* 134 */     for (KeyValuePair pair : pairs) {
/* 135 */       String key = pair.getKey();
/* 136 */       if (key == null) {
/* 137 */         LOGGER.error("A null key is not valid in MapRewritePolicy");
/*     */       } else {
/*     */         
/* 140 */         String value = pair.getValue();
/* 141 */         if (value == null)
/* 142 */         { LOGGER.error("A null value for key " + key + " is not allowed in MapRewritePolicy"); }
/*     */         else
/*     */         
/* 145 */         { map.put(pair.getKey(), pair.getValue()); } 
/*     */       } 
/* 147 */     }  if (map.isEmpty()) {
/* 148 */       LOGGER.error("MapRewritePolicy is not configured with any valid key value pairs");
/* 149 */       return null;
/*     */     } 
/* 151 */     return new MapRewritePolicy(map, op);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\appender\rewrite\MapRewritePolicy.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */