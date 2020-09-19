/*     */ package org.apache.logging.log4j.core.filter;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import org.apache.logging.log4j.Level;
/*     */ import org.apache.logging.log4j.Marker;
/*     */ import org.apache.logging.log4j.core.ContextDataInjector;
/*     */ import org.apache.logging.log4j.core.Filter;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.Logger;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginElement;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginFactory;
/*     */ import org.apache.logging.log4j.core.impl.ContextDataInjectorFactory;
/*     */ import org.apache.logging.log4j.core.util.KeyValuePair;
/*     */ import org.apache.logging.log4j.message.Message;
/*     */ import org.apache.logging.log4j.util.PerformanceSensitive;
/*     */ import org.apache.logging.log4j.util.ReadOnlyStringMap;
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
/*     */ @Plugin(name = "DynamicThresholdFilter", category = "Core", elementType = "filter", printObject = true)
/*     */ @PerformanceSensitive({"allocation"})
/*     */ public final class DynamicThresholdFilter
/*     */   extends AbstractFilter
/*     */ {
/*     */   @PluginFactory
/*     */   public static DynamicThresholdFilter createFilter(@PluginAttribute("key") String key, @PluginElement("Pairs") KeyValuePair[] pairs, @PluginAttribute("defaultThreshold") Level defaultThreshold, @PluginAttribute("onMatch") Filter.Result onMatch, @PluginAttribute("onMismatch") Filter.Result onMismatch) {
/*  66 */     Map<String, Level> map = new HashMap<>();
/*  67 */     for (KeyValuePair pair : pairs) {
/*  68 */       map.put(pair.getKey(), Level.toLevel(pair.getValue()));
/*     */     }
/*  70 */     Level level = (defaultThreshold == null) ? Level.ERROR : defaultThreshold;
/*  71 */     return new DynamicThresholdFilter(key, map, level, onMatch, onMismatch);
/*     */   }
/*     */   
/*  74 */   private Level defaultThreshold = Level.ERROR;
/*     */   private final String key;
/*  76 */   private final ContextDataInjector injector = ContextDataInjectorFactory.createInjector();
/*  77 */   private Map<String, Level> levelMap = new HashMap<>();
/*     */ 
/*     */   
/*     */   private DynamicThresholdFilter(String key, Map<String, Level> pairs, Level defaultLevel, Filter.Result onMatch, Filter.Result onMismatch) {
/*  81 */     super(onMatch, onMismatch);
/*  82 */     Objects.requireNonNull(key, "key cannot be null");
/*  83 */     this.key = key;
/*  84 */     this.levelMap = pairs;
/*  85 */     this.defaultThreshold = defaultLevel;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/*  90 */     if (this == obj) {
/*  91 */       return true;
/*     */     }
/*  93 */     if (!equalsImpl(obj)) {
/*  94 */       return false;
/*     */     }
/*  96 */     if (getClass() != obj.getClass()) {
/*  97 */       return false;
/*     */     }
/*  99 */     DynamicThresholdFilter other = (DynamicThresholdFilter)obj;
/* 100 */     if (this.defaultThreshold == null) {
/* 101 */       if (other.defaultThreshold != null) {
/* 102 */         return false;
/*     */       }
/* 104 */     } else if (!this.defaultThreshold.equals(other.defaultThreshold)) {
/* 105 */       return false;
/*     */     } 
/* 107 */     if (this.key == null) {
/* 108 */       if (other.key != null) {
/* 109 */         return false;
/*     */       }
/* 111 */     } else if (!this.key.equals(other.key)) {
/* 112 */       return false;
/*     */     } 
/* 114 */     if (this.levelMap == null) {
/* 115 */       if (other.levelMap != null) {
/* 116 */         return false;
/*     */       }
/* 118 */     } else if (!this.levelMap.equals(other.levelMap)) {
/* 119 */       return false;
/*     */     } 
/* 121 */     return true;
/*     */   }
/*     */   
/*     */   private Filter.Result filter(Level level, ReadOnlyStringMap contextMap) {
/* 125 */     String value = (String)contextMap.getValue(this.key);
/* 126 */     if (value != null) {
/* 127 */       Level ctxLevel = this.levelMap.get(value);
/* 128 */       if (ctxLevel == null) {
/* 129 */         ctxLevel = this.defaultThreshold;
/*     */       }
/* 131 */       return level.isMoreSpecificThan(ctxLevel) ? this.onMatch : this.onMismatch;
/*     */     } 
/* 133 */     return Filter.Result.NEUTRAL;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(LogEvent event) {
/* 139 */     return filter(event.getLevel(), event.getContextData());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, Message msg, Throwable t) {
/* 145 */     return filter(level, currentContextData());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, Object msg, Throwable t) {
/* 151 */     return filter(level, currentContextData());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object... params) {
/* 157 */     return filter(level, currentContextData());
/*     */   }
/*     */   
/*     */   private ReadOnlyStringMap currentContextData() {
/* 161 */     return this.injector.rawContextData();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0) {
/* 167 */     return filter(level, currentContextData());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1) {
/* 173 */     return filter(level, currentContextData());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2) {
/* 179 */     return filter(level, currentContextData());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3) {
/* 185 */     return filter(level, currentContextData());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3, Object p4) {
/* 192 */     return filter(level, currentContextData());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
/* 199 */     return filter(level, currentContextData());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) {
/* 206 */     return filter(level, currentContextData());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7) {
/* 214 */     return filter(level, currentContextData());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8) {
/* 222 */     return filter(level, currentContextData());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8, Object p9) {
/* 230 */     return filter(level, currentContextData());
/*     */   }
/*     */   
/*     */   public String getKey() {
/* 234 */     return this.key;
/*     */   }
/*     */   
/*     */   public Map<String, Level> getLevelMap() {
/* 238 */     return this.levelMap;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 243 */     int prime = 31;
/* 244 */     int result = hashCodeImpl();
/* 245 */     result = 31 * result + ((this.defaultThreshold == null) ? 0 : this.defaultThreshold.hashCode());
/* 246 */     result = 31 * result + ((this.key == null) ? 0 : this.key.hashCode());
/* 247 */     result = 31 * result + ((this.levelMap == null) ? 0 : this.levelMap.hashCode());
/* 248 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 253 */     StringBuilder sb = new StringBuilder();
/* 254 */     sb.append("key=").append(this.key);
/* 255 */     sb.append(", default=").append(this.defaultThreshold);
/* 256 */     if (this.levelMap.size() > 0) {
/* 257 */       sb.append('{');
/* 258 */       boolean first = true;
/* 259 */       for (Map.Entry<String, Level> entry : this.levelMap.entrySet()) {
/* 260 */         if (!first) {
/* 261 */           sb.append(", ");
/* 262 */           first = false;
/*     */         } 
/* 264 */         sb.append(entry.getKey()).append('=').append(entry.getValue());
/*     */       } 
/* 266 */       sb.append('}');
/*     */     } 
/* 268 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\filter\DynamicThresholdFilter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */