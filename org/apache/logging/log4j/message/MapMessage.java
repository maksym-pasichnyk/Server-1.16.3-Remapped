/*     */ package org.apache.logging.log4j.message;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.Map;
/*     */ import java.util.TreeMap;
/*     */ import org.apache.logging.log4j.util.EnglishEnums;
/*     */ import org.apache.logging.log4j.util.IndexedReadOnlyStringMap;
/*     */ import org.apache.logging.log4j.util.IndexedStringMap;
/*     */ import org.apache.logging.log4j.util.PerformanceSensitive;
/*     */ import org.apache.logging.log4j.util.SortedArrayStringMap;
/*     */ import org.apache.logging.log4j.util.StringBuilderFormattable;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @AsynchronouslyFormattable
/*     */ @PerformanceSensitive({"allocation"})
/*     */ public class MapMessage
/*     */   implements MultiformatMessage, StringBuilderFormattable
/*     */ {
/*     */   private static final long serialVersionUID = -5031471831131487120L;
/*     */   private final IndexedStringMap data;
/*     */   
/*     */   public enum MapFormat
/*     */   {
/*  50 */     XML,
/*     */     
/*  52 */     JSON,
/*     */     
/*  54 */     JAVA;
/*     */     
/*     */     public static MapFormat lookupIgnoreCase(String format) {
/*  57 */       return XML.name().equalsIgnoreCase(format) ? XML : (JSON.name().equalsIgnoreCase(format) ? JSON : (JAVA.name().equalsIgnoreCase(format) ? JAVA : null));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public static String[] names() {
/*  64 */       return new String[] { XML.name(), JSON.name(), JAVA.name() };
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MapMessage() {
/*  76 */     this.data = (IndexedStringMap)new SortedArrayStringMap();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MapMessage(Map<String, String> map) {
/*  84 */     this.data = (IndexedStringMap)new SortedArrayStringMap(map);
/*     */   }
/*     */ 
/*     */   
/*     */   public String[] getFormats() {
/*  89 */     return MapFormat.names();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object[] getParameters() {
/*  98 */     Object[] result = new Object[this.data.size()];
/*  99 */     for (int i = 0; i < this.data.size(); i++) {
/* 100 */       result[i] = this.data.getValueAt(i);
/*     */     }
/* 102 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFormat() {
/* 111 */     return "";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<String, String> getData() {
/* 119 */     TreeMap<String, String> result = new TreeMap<>();
/* 120 */     for (int i = 0; i < this.data.size(); i++) {
/* 121 */       result.put(this.data.getKeyAt(i), (String)this.data.getValueAt(i));
/*     */     }
/* 123 */     return Collections.unmodifiableMap(result);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public IndexedReadOnlyStringMap getIndexedReadOnlyStringMap() {
/* 131 */     return (IndexedReadOnlyStringMap)this.data;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clear() {
/* 138 */     this.data.clear();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MapMessage with(String key, String value) {
/* 148 */     put(key, value);
/* 149 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void put(String key, String value) {
/* 158 */     if (value == null) {
/* 159 */       throw new IllegalArgumentException("No value provided for key " + key);
/*     */     }
/* 161 */     validate(key, value);
/* 162 */     this.data.putValue(key, value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void validate(String key, String value) {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void putAll(Map<String, String> map) {
/* 174 */     for (Map.Entry<String, ?> entry : map.entrySet()) {
/* 175 */       this.data.putValue(entry.getKey(), entry.getValue());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String get(String key) {
/* 185 */     return (String)this.data.getValue(key);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String remove(String key) {
/* 194 */     String result = (String)this.data.getValue(key);
/* 195 */     this.data.remove(key);
/* 196 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String asString() {
/* 205 */     return format((MapFormat)null, new StringBuilder()).toString();
/*     */   }
/*     */   
/*     */   public String asString(String format) {
/*     */     try {
/* 210 */       return format((MapFormat)EnglishEnums.valueOf(MapFormat.class, format), new StringBuilder()).toString();
/* 211 */     } catch (IllegalArgumentException ex) {
/* 212 */       return asString();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private StringBuilder format(MapFormat format, StringBuilder sb) {
/* 222 */     if (format == null)
/* 223 */     { appendMap(sb); }
/*     */     else
/* 225 */     { switch (format)
/*     */       { case XML:
/* 227 */           asXml(sb);
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
/* 243 */           return sb;case JSON: asJson(sb); return sb;case JAVA: asJava(sb); return sb; }  appendMap(sb); }  return sb;
/*     */   }
/*     */   
/*     */   public void asXml(StringBuilder sb) {
/* 247 */     sb.append("<Map>\n");
/* 248 */     for (int i = 0; i < this.data.size(); i++) {
/* 249 */       sb.append("  <Entry key=\"").append(this.data.getKeyAt(i)).append("\">").append(this.data.getValueAt(i)).append("</Entry>\n");
/*     */     }
/*     */     
/* 252 */     sb.append("</Map>");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFormattedMessage() {
/* 261 */     return asString();
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
/*     */   public String getFormattedMessage(String[] formats) {
/* 274 */     if (formats == null || formats.length == 0) {
/* 275 */       return asString();
/*     */     }
/* 277 */     for (int i = 0; i < formats.length; i++) {
/* 278 */       MapFormat mapFormat = MapFormat.lookupIgnoreCase(formats[i]);
/* 279 */       if (mapFormat != null) {
/* 280 */         return format(mapFormat, new StringBuilder()).toString();
/*     */       }
/*     */     } 
/* 283 */     return asString();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void appendMap(StringBuilder sb) {
/* 288 */     for (int i = 0; i < this.data.size(); i++) {
/* 289 */       if (i > 0) {
/* 290 */         sb.append(' ');
/*     */       }
/* 292 */       StringBuilders.appendKeyDqValue(sb, this.data.getKeyAt(i), this.data.getValueAt(i));
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void asJson(StringBuilder sb) {
/* 297 */     sb.append('{');
/* 298 */     for (int i = 0; i < this.data.size(); i++) {
/* 299 */       if (i > 0) {
/* 300 */         sb.append(", ");
/*     */       }
/* 302 */       StringBuilders.appendDqValue(sb, this.data.getKeyAt(i)).append(':');
/* 303 */       StringBuilders.appendDqValue(sb, this.data.getValueAt(i));
/*     */     } 
/* 305 */     sb.append('}');
/*     */   }
/*     */ 
/*     */   
/*     */   protected void asJava(StringBuilder sb) {
/* 310 */     sb.append('{');
/* 311 */     for (int i = 0; i < this.data.size(); i++) {
/* 312 */       if (i > 0) {
/* 313 */         sb.append(", ");
/*     */       }
/* 315 */       StringBuilders.appendKeyDqValue(sb, this.data.getKeyAt(i), this.data.getValueAt(i));
/*     */     } 
/* 317 */     sb.append('}');
/*     */   }
/*     */   
/*     */   public MapMessage newInstance(Map<String, String> map) {
/* 321 */     return new MapMessage(map);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 326 */     return asString();
/*     */   }
/*     */ 
/*     */   
/*     */   public void formatTo(StringBuilder buffer) {
/* 331 */     format((MapFormat)null, buffer);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 336 */     if (this == o) {
/* 337 */       return true;
/*     */     }
/* 339 */     if (o == null || getClass() != o.getClass()) {
/* 340 */       return false;
/*     */     }
/*     */     
/* 343 */     MapMessage that = (MapMessage)o;
/*     */     
/* 345 */     return this.data.equals(that.data);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 350 */     return this.data.hashCode();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Throwable getThrowable() {
/* 360 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\message\MapMessage.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */