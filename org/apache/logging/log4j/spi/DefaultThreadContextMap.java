/*     */ package org.apache.logging.log4j.spi;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.apache.logging.log4j.util.BiConsumer;
/*     */ import org.apache.logging.log4j.util.PropertiesUtil;
/*     */ import org.apache.logging.log4j.util.ReadOnlyStringMap;
/*     */ import org.apache.logging.log4j.util.TriConsumer;
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
/*     */ public class DefaultThreadContextMap
/*     */   implements ThreadContextMap, ReadOnlyStringMap
/*     */ {
/*     */   public static final String INHERITABLE_MAP = "isThreadContextMapInheritable";
/*     */   private final boolean useMap;
/*     */   private final ThreadLocal<Map<String, String>> localMap;
/*     */   
/*     */   public DefaultThreadContextMap() {
/*  46 */     this(true);
/*     */   }
/*     */   
/*     */   public DefaultThreadContextMap(boolean useMap) {
/*  50 */     this.useMap = useMap;
/*  51 */     this.localMap = createThreadLocalMap(useMap);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static ThreadLocal<Map<String, String>> createThreadLocalMap(final boolean isMapEnabled) {
/*  57 */     PropertiesUtil managerProps = PropertiesUtil.getProperties();
/*  58 */     boolean inheritable = managerProps.getBooleanProperty("isThreadContextMapInheritable");
/*  59 */     if (inheritable) {
/*  60 */       return new InheritableThreadLocal<Map<String, String>>()
/*     */         {
/*     */           protected Map<String, String> childValue(Map<String, String> parentValue) {
/*  63 */             return (parentValue != null && isMapEnabled) ? Collections.<String, String>unmodifiableMap(new HashMap<>(parentValue)) : null;
/*     */           }
/*     */         };
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*  70 */     return new ThreadLocal<>();
/*     */   }
/*     */ 
/*     */   
/*     */   public void put(String key, String value) {
/*  75 */     if (!this.useMap) {
/*     */       return;
/*     */     }
/*  78 */     Map<String, String> map = this.localMap.get();
/*  79 */     map = (map == null) ? new HashMap<>(1) : new HashMap<>(map);
/*  80 */     map.put(key, value);
/*  81 */     this.localMap.set(Collections.unmodifiableMap(map));
/*     */   }
/*     */   
/*     */   public void putAll(Map<String, String> m) {
/*  85 */     if (!this.useMap) {
/*     */       return;
/*     */     }
/*  88 */     Map<String, String> map = this.localMap.get();
/*  89 */     map = (map == null) ? new HashMap<>(m.size()) : new HashMap<>(map);
/*  90 */     for (Map.Entry<String, String> e : m.entrySet()) {
/*  91 */       map.put(e.getKey(), e.getValue());
/*     */     }
/*  93 */     this.localMap.set(Collections.unmodifiableMap(map));
/*     */   }
/*     */ 
/*     */   
/*     */   public String get(String key) {
/*  98 */     Map<String, String> map = this.localMap.get();
/*  99 */     return (map == null) ? null : map.get(key);
/*     */   }
/*     */ 
/*     */   
/*     */   public void remove(String key) {
/* 104 */     Map<String, String> map = this.localMap.get();
/* 105 */     if (map != null) {
/* 106 */       Map<String, String> copy = new HashMap<>(map);
/* 107 */       copy.remove(key);
/* 108 */       this.localMap.set(Collections.unmodifiableMap(copy));
/*     */     } 
/*     */   }
/*     */   
/*     */   public void removeAll(Iterable<String> keys) {
/* 113 */     Map<String, String> map = this.localMap.get();
/* 114 */     if (map != null) {
/* 115 */       Map<String, String> copy = new HashMap<>(map);
/* 116 */       for (String key : keys) {
/* 117 */         copy.remove(key);
/*     */       }
/* 119 */       this.localMap.set(Collections.unmodifiableMap(copy));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/* 125 */     this.localMap.remove();
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<String, String> toMap() {
/* 130 */     return getCopy();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsKey(String key) {
/* 135 */     Map<String, String> map = this.localMap.get();
/* 136 */     return (map != null && map.containsKey(key));
/*     */   }
/*     */ 
/*     */   
/*     */   public <V> void forEach(BiConsumer<String, ? super V> action) {
/* 141 */     Map<String, String> map = this.localMap.get();
/* 142 */     if (map == null) {
/*     */       return;
/*     */     }
/* 145 */     for (Map.Entry<String, String> entry : map.entrySet()) {
/* 146 */       action.accept(entry.getKey(), entry.getValue());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public <V, S> void forEach(TriConsumer<String, ? super V, S> action, S state) {
/* 152 */     Map<String, String> map = this.localMap.get();
/* 153 */     if (map == null) {
/*     */       return;
/*     */     }
/* 156 */     for (Map.Entry<String, String> entry : map.entrySet()) {
/* 157 */       action.accept(entry.getKey(), entry.getValue(), state);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public <V> V getValue(String key) {
/* 164 */     Map<String, String> map = this.localMap.get();
/* 165 */     return (map == null) ? null : (V)map.get(key);
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<String, String> getCopy() {
/* 170 */     Map<String, String> map = this.localMap.get();
/* 171 */     return (map == null) ? new HashMap<>() : new HashMap<>(map);
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<String, String> getImmutableMapOrNull() {
/* 176 */     return this.localMap.get();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 181 */     Map<String, String> map = this.localMap.get();
/* 182 */     return (map == null || map.size() == 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/* 187 */     Map<String, String> map = this.localMap.get();
/* 188 */     return (map == null) ? 0 : map.size();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 193 */     Map<String, String> map = this.localMap.get();
/* 194 */     return (map == null) ? "{}" : map.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 199 */     int prime = 31;
/* 200 */     int result = 1;
/* 201 */     Map<String, String> map = this.localMap.get();
/* 202 */     result = 31 * result + ((map == null) ? 0 : map.hashCode());
/* 203 */     result = 31 * result + Boolean.valueOf(this.useMap).hashCode();
/* 204 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 209 */     if (this == obj) {
/* 210 */       return true;
/*     */     }
/* 212 */     if (obj == null) {
/* 213 */       return false;
/*     */     }
/* 215 */     if (obj instanceof DefaultThreadContextMap) {
/* 216 */       DefaultThreadContextMap defaultThreadContextMap = (DefaultThreadContextMap)obj;
/* 217 */       if (this.useMap != defaultThreadContextMap.useMap) {
/* 218 */         return false;
/*     */       }
/*     */     } 
/* 221 */     if (!(obj instanceof ThreadContextMap)) {
/* 222 */       return false;
/*     */     }
/* 224 */     ThreadContextMap other = (ThreadContextMap)obj;
/* 225 */     Map<String, String> map = this.localMap.get();
/* 226 */     Map<String, String> otherMap = other.getImmutableMapOrNull();
/* 227 */     if (map == null) {
/* 228 */       if (otherMap != null) {
/* 229 */         return false;
/*     */       }
/* 231 */     } else if (!map.equals(otherMap)) {
/* 232 */       return false;
/*     */     } 
/* 234 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\spi\DefaultThreadContextMap.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */