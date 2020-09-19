/*     */ package org.apache.logging.log4j.core.filter;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.apache.logging.log4j.Level;
/*     */ import org.apache.logging.log4j.Marker;
/*     */ import org.apache.logging.log4j.core.Filter;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.Logger;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginElement;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginFactory;
/*     */ import org.apache.logging.log4j.core.util.KeyValuePair;
/*     */ import org.apache.logging.log4j.message.Message;
/*     */ import org.apache.logging.log4j.message.StructuredDataMessage;
/*     */ import org.apache.logging.log4j.util.IndexedReadOnlyStringMap;
/*     */ import org.apache.logging.log4j.util.PerformanceSensitive;
/*     */ import org.apache.logging.log4j.util.StringBuilders;
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
/*     */ @Plugin(name = "StructuredDataFilter", category = "Core", elementType = "filter", printObject = true)
/*     */ @PerformanceSensitive({"allocation"})
/*     */ public final class StructuredDataFilter
/*     */   extends MapFilter
/*     */ {
/*     */   private static final int MAX_BUFFER_SIZE = 2048;
/*  49 */   private static ThreadLocal<StringBuilder> threadLocalStringBuilder = new ThreadLocal<>();
/*     */ 
/*     */   
/*     */   private StructuredDataFilter(Map<String, List<String>> map, boolean oper, Filter.Result onMatch, Filter.Result onMismatch) {
/*  53 */     super(map, oper, onMatch, onMismatch);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, Message msg, Throwable t) {
/*  59 */     if (msg instanceof StructuredDataMessage) {
/*  60 */       return filter((StructuredDataMessage)msg);
/*     */     }
/*  62 */     return Filter.Result.NEUTRAL;
/*     */   }
/*     */ 
/*     */   
/*     */   public Filter.Result filter(LogEvent event) {
/*  67 */     Message msg = event.getMessage();
/*  68 */     if (msg instanceof StructuredDataMessage) {
/*  69 */       return filter((StructuredDataMessage)msg);
/*     */     }
/*  71 */     return super.filter(event);
/*     */   }
/*     */   
/*     */   protected Filter.Result filter(StructuredDataMessage message) {
/*  75 */     boolean match = false;
/*  76 */     IndexedReadOnlyStringMap map = getStringMap();
/*  77 */     for (int i = 0; i < map.size(); i++) {
/*  78 */       StringBuilder toMatch = getValue(message, map.getKeyAt(i));
/*  79 */       if (toMatch != null) {
/*  80 */         match = listContainsValue((List<String>)map.getValueAt(i), toMatch);
/*     */       } else {
/*  82 */         match = false;
/*     */       } 
/*  84 */       if ((!isAnd() && match) || (isAnd() && !match)) {
/*     */         break;
/*     */       }
/*     */     } 
/*  88 */     return match ? this.onMatch : this.onMismatch;
/*     */   }
/*     */   
/*     */   private StringBuilder getValue(StructuredDataMessage data, String key) {
/*  92 */     StringBuilder sb = getStringBuilder();
/*  93 */     if (key.equalsIgnoreCase("id")) {
/*  94 */       data.getId().formatTo(sb);
/*  95 */       return sb;
/*  96 */     }  if (key.equalsIgnoreCase("id.name"))
/*  97 */       return appendOrNull(data.getId().getName(), sb); 
/*  98 */     if (key.equalsIgnoreCase("type"))
/*  99 */       return appendOrNull(data.getType(), sb); 
/* 100 */     if (key.equalsIgnoreCase("message")) {
/* 101 */       data.formatTo(sb);
/* 102 */       return sb;
/*     */     } 
/* 104 */     return appendOrNull(data.get(key), sb);
/*     */   }
/*     */ 
/*     */   
/*     */   private StringBuilder getStringBuilder() {
/* 109 */     StringBuilder result = threadLocalStringBuilder.get();
/* 110 */     if (result == null) {
/* 111 */       result = new StringBuilder();
/* 112 */       threadLocalStringBuilder.set(result);
/*     */     } 
/* 114 */     if (result.length() > 2048) {
/* 115 */       result.setLength(2048);
/* 116 */       result.trimToSize();
/*     */     } 
/* 118 */     result.setLength(0);
/* 119 */     return result;
/*     */   }
/*     */   
/*     */   private StringBuilder appendOrNull(String value, StringBuilder sb) {
/* 123 */     if (value == null) {
/* 124 */       return null;
/*     */     }
/* 126 */     sb.append(value);
/* 127 */     return sb;
/*     */   }
/*     */   
/*     */   private boolean listContainsValue(List<String> candidates, StringBuilder toMatch) {
/* 131 */     if (toMatch == null) {
/* 132 */       for (int i = 0; i < candidates.size(); i++) {
/* 133 */         String candidate = candidates.get(i);
/* 134 */         if (candidate == null) {
/* 135 */           return true;
/*     */         }
/*     */       } 
/*     */     } else {
/* 139 */       for (int i = 0; i < candidates.size(); i++) {
/* 140 */         String candidate = candidates.get(i);
/* 141 */         if (candidate == null) {
/* 142 */           return false;
/*     */         }
/* 144 */         if (StringBuilders.equals(candidate, 0, candidate.length(), toMatch, 0, toMatch.length())) {
/* 145 */           return true;
/*     */         }
/*     */       } 
/*     */     } 
/* 149 */     return false;
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @PluginFactory
/*     */   public static StructuredDataFilter createFilter(@PluginElement("Pairs") KeyValuePair[] pairs, @PluginAttribute("operator") String oper, @PluginAttribute("onMatch") Filter.Result match, @PluginAttribute("onMismatch") Filter.Result mismatch) {
/* 166 */     if (pairs == null || pairs.length == 0) {
/* 167 */       LOGGER.error("keys and values must be specified for the StructuredDataFilter");
/* 168 */       return null;
/*     */     } 
/* 170 */     Map<String, List<String>> map = new HashMap<>();
/* 171 */     for (KeyValuePair pair : pairs) {
/* 172 */       String key = pair.getKey();
/* 173 */       if (key == null) {
/* 174 */         LOGGER.error("A null key is not valid in MapFilter");
/*     */       } else {
/*     */         
/* 177 */         String value = pair.getValue();
/* 178 */         if (value == null) {
/* 179 */           LOGGER.error("A null value for key " + key + " is not allowed in MapFilter");
/*     */         } else {
/*     */           
/* 182 */           List<String> list = map.get(pair.getKey());
/* 183 */           if (list != null)
/* 184 */           { list.add(value); }
/*     */           else
/* 186 */           { list = new ArrayList<>();
/* 187 */             list.add(value);
/* 188 */             map.put(pair.getKey(), list); } 
/*     */         } 
/*     */       } 
/* 191 */     }  if (map.isEmpty()) {
/* 192 */       LOGGER.error("StructuredDataFilter is not configured with any valid key value pairs");
/* 193 */       return null;
/*     */     } 
/* 195 */     boolean isAnd = (oper == null || !oper.equalsIgnoreCase("or"));
/* 196 */     return new StructuredDataFilter(map, isAnd, match, mismatch);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\filter\StructuredDataFilter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */