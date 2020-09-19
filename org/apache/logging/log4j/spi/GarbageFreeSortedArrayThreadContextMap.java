/*     */ package org.apache.logging.log4j.spi;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.apache.logging.log4j.util.PropertiesUtil;
/*     */ import org.apache.logging.log4j.util.ReadOnlyStringMap;
/*     */ import org.apache.logging.log4j.util.SortedArrayStringMap;
/*     */ import org.apache.logging.log4j.util.StringMap;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class GarbageFreeSortedArrayThreadContextMap
/*     */   implements ReadOnlyThreadContextMap, ObjectThreadContextMap
/*     */ {
/*     */   public static final String INHERITABLE_MAP = "isThreadContextMapInheritable";
/*     */   protected static final int DEFAULT_INITIAL_CAPACITY = 16;
/*     */   protected static final String PROPERTY_NAME_INITIAL_CAPACITY = "log4j2.ThreadContext.initial.capacity";
/*  58 */   protected final ThreadLocal<StringMap> localMap = createThreadLocalMap();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ThreadLocal<StringMap> createThreadLocalMap() {
/*  64 */     PropertiesUtil managerProps = PropertiesUtil.getProperties();
/*  65 */     boolean inheritable = managerProps.getBooleanProperty("isThreadContextMapInheritable");
/*  66 */     if (inheritable) {
/*  67 */       return new InheritableThreadLocal<StringMap>()
/*     */         {
/*     */           protected StringMap childValue(StringMap parentValue) {
/*  70 */             return (parentValue != null) ? GarbageFreeSortedArrayThreadContextMap.this.createStringMap((ReadOnlyStringMap)parentValue) : null;
/*     */           }
/*     */         };
/*     */     }
/*     */     
/*  75 */     return new ThreadLocal<>();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected StringMap createStringMap() {
/*  86 */     return (StringMap)new SortedArrayStringMap(PropertiesUtil.getProperties().getIntegerProperty("log4j2.ThreadContext.initial.capacity", 16));
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
/*     */   protected StringMap createStringMap(ReadOnlyStringMap original) {
/* 100 */     return (StringMap)new SortedArrayStringMap(original);
/*     */   }
/*     */   
/*     */   private StringMap getThreadLocalMap() {
/* 104 */     StringMap map = this.localMap.get();
/* 105 */     if (map == null) {
/* 106 */       map = createStringMap();
/* 107 */       this.localMap.set(map);
/*     */     } 
/* 109 */     return map;
/*     */   }
/*     */ 
/*     */   
/*     */   public void put(String key, String value) {
/* 114 */     getThreadLocalMap().putValue(key, value);
/*     */   }
/*     */ 
/*     */   
/*     */   public void putValue(String key, Object value) {
/* 119 */     getThreadLocalMap().putValue(key, value);
/*     */   }
/*     */ 
/*     */   
/*     */   public void putAll(Map<String, String> values) {
/* 124 */     if (values == null || values.isEmpty()) {
/*     */       return;
/*     */     }
/* 127 */     StringMap map = getThreadLocalMap();
/* 128 */     for (Map.Entry<String, String> entry : values.entrySet()) {
/* 129 */       map.putValue(entry.getKey(), entry.getValue());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public <V> void putAllValues(Map<String, V> values) {
/* 135 */     if (values == null || values.isEmpty()) {
/*     */       return;
/*     */     }
/* 138 */     StringMap map = getThreadLocalMap();
/* 139 */     for (Map.Entry<String, V> entry : values.entrySet()) {
/* 140 */       map.putValue(entry.getKey(), entry.getValue());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public String get(String key) {
/* 146 */     return (String)getValue(key);
/*     */   }
/*     */ 
/*     */   
/*     */   public Object getValue(String key) {
/* 151 */     StringMap map = this.localMap.get();
/* 152 */     return (map == null) ? null : map.getValue(key);
/*     */   }
/*     */ 
/*     */   
/*     */   public void remove(String key) {
/* 157 */     StringMap map = this.localMap.get();
/* 158 */     if (map != null) {
/* 159 */       map.remove(key);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void removeAll(Iterable<String> keys) {
/* 165 */     StringMap map = this.localMap.get();
/* 166 */     if (map != null) {
/* 167 */       for (String key : keys) {
/* 168 */         map.remove(key);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/* 175 */     StringMap map = this.localMap.get();
/* 176 */     if (map != null) {
/* 177 */       map.clear();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsKey(String key) {
/* 183 */     StringMap map = this.localMap.get();
/* 184 */     return (map != null && map.containsKey(key));
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<String, String> getCopy() {
/* 189 */     StringMap map = this.localMap.get();
/* 190 */     return (map == null) ? new HashMap<>() : map.toMap();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StringMap getReadOnlyContextData() {
/* 198 */     StringMap map = this.localMap.get();
/* 199 */     if (map == null) {
/* 200 */       map = createStringMap();
/* 201 */       this.localMap.set(map);
/*     */     } 
/* 203 */     return map;
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<String, String> getImmutableMapOrNull() {
/* 208 */     StringMap map = this.localMap.get();
/* 209 */     return (map == null) ? null : Collections.<String, String>unmodifiableMap(map.toMap());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 214 */     StringMap map = this.localMap.get();
/* 215 */     return (map == null || map.size() == 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 220 */     StringMap map = this.localMap.get();
/* 221 */     return (map == null) ? "{}" : map.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 226 */     int prime = 31;
/* 227 */     int result = 1;
/* 228 */     StringMap map = this.localMap.get();
/* 229 */     result = 31 * result + ((map == null) ? 0 : map.hashCode());
/* 230 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 235 */     if (this == obj) {
/* 236 */       return true;
/*     */     }
/* 238 */     if (obj == null) {
/* 239 */       return false;
/*     */     }
/* 241 */     if (!(obj instanceof ThreadContextMap)) {
/* 242 */       return false;
/*     */     }
/* 244 */     ThreadContextMap other = (ThreadContextMap)obj;
/* 245 */     Map<String, String> map = getImmutableMapOrNull();
/* 246 */     Map<String, String> otherMap = other.getImmutableMapOrNull();
/* 247 */     if (map == null) {
/* 248 */       if (otherMap != null) {
/* 249 */         return false;
/*     */       }
/* 251 */     } else if (!map.equals(otherMap)) {
/* 252 */       return false;
/*     */     } 
/* 254 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\spi\GarbageFreeSortedArrayThreadContextMap.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */