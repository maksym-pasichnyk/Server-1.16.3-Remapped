/*     */ package org.apache.logging.log4j.core.filter;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
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
/*     */ import org.apache.logging.log4j.message.MapMessage;
/*     */ import org.apache.logging.log4j.message.Message;
/*     */ import org.apache.logging.log4j.util.BiConsumer;
/*     */ import org.apache.logging.log4j.util.IndexedReadOnlyStringMap;
/*     */ import org.apache.logging.log4j.util.IndexedStringMap;
/*     */ import org.apache.logging.log4j.util.PerformanceSensitive;
/*     */ import org.apache.logging.log4j.util.ReadOnlyStringMap;
/*     */ import org.apache.logging.log4j.util.SortedArrayStringMap;
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
/*     */ @Plugin(name = "MapFilter", category = "Core", elementType = "filter", printObject = true)
/*     */ @PerformanceSensitive({"allocation"})
/*     */ public class MapFilter
/*     */   extends AbstractFilter
/*     */ {
/*     */   private final IndexedStringMap map;
/*     */   private final boolean isAnd;
/*     */   
/*     */   protected MapFilter(Map<String, List<String>> map, boolean oper, Filter.Result onMatch, Filter.Result onMismatch) {
/*  56 */     super(onMatch, onMismatch);
/*  57 */     this.isAnd = oper;
/*  58 */     Objects.requireNonNull(map, "map cannot be null");
/*     */     
/*  60 */     this.map = (IndexedStringMap)new SortedArrayStringMap(map.size());
/*  61 */     for (Map.Entry<String, List<String>> entry : map.entrySet()) {
/*  62 */       this.map.putValue(entry.getKey(), entry.getValue());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, Message msg, Throwable t) {
/*  69 */     if (msg instanceof MapMessage) {
/*  70 */       return filter((MapMessage)msg) ? this.onMatch : this.onMismatch;
/*     */     }
/*  72 */     return Filter.Result.NEUTRAL;
/*     */   }
/*     */ 
/*     */   
/*     */   public Filter.Result filter(LogEvent event) {
/*  77 */     Message msg = event.getMessage();
/*  78 */     if (msg instanceof MapMessage) {
/*  79 */       return filter((MapMessage)msg) ? this.onMatch : this.onMismatch;
/*     */     }
/*  81 */     return Filter.Result.NEUTRAL;
/*     */   }
/*     */   
/*     */   protected boolean filter(MapMessage mapMessage) {
/*  85 */     boolean match = false;
/*  86 */     for (int i = 0; i < this.map.size(); i++) {
/*  87 */       String toMatch = mapMessage.get(this.map.getKeyAt(i));
/*  88 */       match = (toMatch != null && ((List)this.map.getValueAt(i)).contains(toMatch));
/*     */       
/*  90 */       if ((!this.isAnd && match) || (this.isAnd && !match)) {
/*     */         break;
/*     */       }
/*     */     } 
/*  94 */     return match;
/*     */   }
/*     */   
/*     */   protected boolean filter(Map<String, String> data) {
/*  98 */     boolean match = false;
/*  99 */     for (int i = 0; i < this.map.size(); i++) {
/* 100 */       String toMatch = data.get(this.map.getKeyAt(i));
/* 101 */       match = (toMatch != null && ((List)this.map.getValueAt(i)).contains(toMatch));
/*     */       
/* 103 */       if ((!this.isAnd && match) || (this.isAnd && !match)) {
/*     */         break;
/*     */       }
/*     */     } 
/* 107 */     return match;
/*     */   }
/*     */   
/*     */   protected boolean filter(ReadOnlyStringMap data) {
/* 111 */     boolean match = false;
/* 112 */     for (int i = 0; i < this.map.size(); i++) {
/* 113 */       String toMatch = (String)data.getValue(this.map.getKeyAt(i));
/* 114 */       match = (toMatch != null && ((List)this.map.getValueAt(i)).contains(toMatch));
/*     */       
/* 116 */       if ((!this.isAnd && match) || (this.isAnd && !match)) {
/*     */         break;
/*     */       }
/*     */     } 
/* 120 */     return match;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0) {
/* 126 */     return Filter.Result.NEUTRAL;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1) {
/* 132 */     return Filter.Result.NEUTRAL;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2) {
/* 138 */     return Filter.Result.NEUTRAL;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3) {
/* 144 */     return Filter.Result.NEUTRAL;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3, Object p4) {
/* 151 */     return Filter.Result.NEUTRAL;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
/* 158 */     return Filter.Result.NEUTRAL;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) {
/* 165 */     return Filter.Result.NEUTRAL;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7) {
/* 173 */     return Filter.Result.NEUTRAL;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8) {
/* 181 */     return Filter.Result.NEUTRAL;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8, Object p9) {
/* 189 */     return Filter.Result.NEUTRAL;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 194 */     StringBuilder sb = new StringBuilder();
/* 195 */     sb.append("isAnd=").append(this.isAnd);
/* 196 */     if (this.map.size() > 0) {
/* 197 */       sb.append(", {");
/* 198 */       for (int i = 0; i < this.map.size(); i++) {
/* 199 */         if (i > 0) {
/* 200 */           sb.append(", ");
/*     */         }
/* 202 */         List<String> list = (List<String>)this.map.getValueAt(i);
/* 203 */         String value = (list.size() > 1) ? list.get(0) : list.toString();
/* 204 */         sb.append(this.map.getKeyAt(i)).append('=').append(value);
/*     */       } 
/* 206 */       sb.append('}');
/*     */     } 
/* 208 */     return sb.toString();
/*     */   }
/*     */   
/*     */   protected boolean isAnd() {
/* 212 */     return this.isAnd;
/*     */   }
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   protected Map<String, List<String>> getMap() {
/* 218 */     final Map<String, List<String>> result = new HashMap<>(this.map.size());
/* 219 */     this.map.forEach(new BiConsumer<String, List<String>>()
/*     */         {
/*     */           public void accept(String key, List<String> value) {
/* 222 */             result.put(key, value);
/*     */           }
/*     */         });
/* 225 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected IndexedReadOnlyStringMap getStringMap() {
/* 234 */     return (IndexedReadOnlyStringMap)this.map;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @PluginFactory
/*     */   public static MapFilter createFilter(@PluginElement("Pairs") KeyValuePair[] pairs, @PluginAttribute("operator") String oper, @PluginAttribute("onMatch") Filter.Result match, @PluginAttribute("onMismatch") Filter.Result mismatch) {
/* 243 */     if (pairs == null || pairs.length == 0) {
/* 244 */       LOGGER.error("keys and values must be specified for the MapFilter");
/* 245 */       return null;
/*     */     } 
/* 247 */     Map<String, List<String>> map = new HashMap<>();
/* 248 */     for (KeyValuePair pair : pairs) {
/* 249 */       String key = pair.getKey();
/* 250 */       if (key == null) {
/* 251 */         LOGGER.error("A null key is not valid in MapFilter");
/*     */       } else {
/*     */         
/* 254 */         String value = pair.getValue();
/* 255 */         if (value == null) {
/* 256 */           LOGGER.error("A null value for key " + key + " is not allowed in MapFilter");
/*     */         } else {
/*     */           
/* 259 */           List<String> list = map.get(pair.getKey());
/* 260 */           if (list != null)
/* 261 */           { list.add(value); }
/*     */           else
/* 263 */           { list = new ArrayList<>();
/* 264 */             list.add(value);
/* 265 */             map.put(pair.getKey(), list); } 
/*     */         } 
/*     */       } 
/* 268 */     }  if (map.isEmpty()) {
/* 269 */       LOGGER.error("MapFilter is not configured with any valid key value pairs");
/* 270 */       return null;
/*     */     } 
/* 272 */     boolean isAnd = (oper == null || !oper.equalsIgnoreCase("or"));
/* 273 */     return new MapFilter(map, isAnd, match, mismatch);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\filter\MapFilter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */