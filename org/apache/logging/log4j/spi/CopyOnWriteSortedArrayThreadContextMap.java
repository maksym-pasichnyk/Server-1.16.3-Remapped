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
/*     */ class CopyOnWriteSortedArrayThreadContextMap
/*     */   implements ReadOnlyThreadContextMap, ObjectThreadContextMap, CopyOnWrite
/*     */ {
/*     */   public static final String INHERITABLE_MAP = "isThreadContextMapInheritable";
/*     */   protected static final int DEFAULT_INITIAL_CAPACITY = 16;
/*     */   protected static final String PROPERTY_NAME_INITIAL_CAPACITY = "log4j2.ThreadContext.initial.capacity";
/*  55 */   private static final StringMap EMPTY_CONTEXT_DATA = (StringMap)new SortedArrayStringMap(1);
/*     */   static {
/*  57 */     EMPTY_CONTEXT_DATA.freeze();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  63 */   private final ThreadLocal<StringMap> localMap = createThreadLocalMap();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ThreadLocal<StringMap> createThreadLocalMap() {
/*  69 */     PropertiesUtil managerProps = PropertiesUtil.getProperties();
/*  70 */     boolean inheritable = managerProps.getBooleanProperty("isThreadContextMapInheritable");
/*  71 */     if (inheritable) {
/*  72 */       return new InheritableThreadLocal<StringMap>()
/*     */         {
/*     */           protected StringMap childValue(StringMap parentValue) {
/*  75 */             return (parentValue != null) ? CopyOnWriteSortedArrayThreadContextMap.this.createStringMap((ReadOnlyStringMap)parentValue) : null;
/*     */           }
/*     */         };
/*     */     }
/*     */     
/*  80 */     return new ThreadLocal<>();
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
/*  91 */     return (StringMap)new SortedArrayStringMap(PropertiesUtil.getProperties().getIntegerProperty("log4j2.ThreadContext.initial.capacity", 16));
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
/* 105 */     return (StringMap)new SortedArrayStringMap(original);
/*     */   }
/*     */ 
/*     */   
/*     */   public void put(String key, String value) {
/* 110 */     putValue(key, value);
/*     */   }
/*     */ 
/*     */   
/*     */   public void putValue(String key, Object value) {
/* 115 */     StringMap map = this.localMap.get();
/* 116 */     map = (map == null) ? createStringMap() : createStringMap((ReadOnlyStringMap)map);
/* 117 */     map.putValue(key, value);
/* 118 */     map.freeze();
/* 119 */     this.localMap.set(map);
/*     */   }
/*     */ 
/*     */   
/*     */   public void putAll(Map<String, String> values) {
/* 124 */     if (values == null || values.isEmpty()) {
/*     */       return;
/*     */     }
/* 127 */     StringMap map = this.localMap.get();
/* 128 */     map = (map == null) ? createStringMap() : createStringMap((ReadOnlyStringMap)map);
/* 129 */     for (Map.Entry<String, String> entry : values.entrySet()) {
/* 130 */       map.putValue(entry.getKey(), entry.getValue());
/*     */     }
/* 132 */     map.freeze();
/* 133 */     this.localMap.set(map);
/*     */   }
/*     */ 
/*     */   
/*     */   public <V> void putAllValues(Map<String, V> values) {
/* 138 */     if (values == null || values.isEmpty()) {
/*     */       return;
/*     */     }
/* 141 */     StringMap map = this.localMap.get();
/* 142 */     map = (map == null) ? createStringMap() : createStringMap((ReadOnlyStringMap)map);
/* 143 */     for (Map.Entry<String, V> entry : values.entrySet()) {
/* 144 */       map.putValue(entry.getKey(), entry.getValue());
/*     */     }
/* 146 */     map.freeze();
/* 147 */     this.localMap.set(map);
/*     */   }
/*     */ 
/*     */   
/*     */   public String get(String key) {
/* 152 */     return (String)getValue(key);
/*     */   }
/*     */ 
/*     */   
/*     */   public Object getValue(String key) {
/* 157 */     StringMap map = this.localMap.get();
/* 158 */     return (map == null) ? null : map.getValue(key);
/*     */   }
/*     */ 
/*     */   
/*     */   public void remove(String key) {
/* 163 */     StringMap map = this.localMap.get();
/* 164 */     if (map != null) {
/* 165 */       StringMap copy = createStringMap((ReadOnlyStringMap)map);
/* 166 */       copy.remove(key);
/* 167 */       copy.freeze();
/* 168 */       this.localMap.set(copy);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void removeAll(Iterable<String> keys) {
/* 174 */     StringMap map = this.localMap.get();
/* 175 */     if (map != null) {
/* 176 */       StringMap copy = createStringMap((ReadOnlyStringMap)map);
/* 177 */       for (String key : keys) {
/* 178 */         copy.remove(key);
/*     */       }
/* 180 */       copy.freeze();
/* 181 */       this.localMap.set(copy);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/* 187 */     this.localMap.remove();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsKey(String key) {
/* 192 */     StringMap map = this.localMap.get();
/* 193 */     return (map != null && map.containsKey(key));
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<String, String> getCopy() {
/* 198 */     StringMap map = this.localMap.get();
/* 199 */     return (map == null) ? new HashMap<>() : map.toMap();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StringMap getReadOnlyContextData() {
/* 207 */     StringMap map = this.localMap.get();
/* 208 */     return (map == null) ? EMPTY_CONTEXT_DATA : map;
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<String, String> getImmutableMapOrNull() {
/* 213 */     StringMap map = this.localMap.get();
/* 214 */     return (map == null) ? null : Collections.<String, String>unmodifiableMap(map.toMap());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 219 */     StringMap map = this.localMap.get();
/* 220 */     return (map == null || map.size() == 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 225 */     StringMap map = this.localMap.get();
/* 226 */     return (map == null) ? "{}" : map.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 231 */     int prime = 31;
/* 232 */     int result = 1;
/* 233 */     StringMap map = this.localMap.get();
/* 234 */     result = 31 * result + ((map == null) ? 0 : map.hashCode());
/* 235 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 240 */     if (this == obj) {
/* 241 */       return true;
/*     */     }
/* 243 */     if (obj == null) {
/* 244 */       return false;
/*     */     }
/* 246 */     if (!(obj instanceof ThreadContextMap)) {
/* 247 */       return false;
/*     */     }
/* 249 */     ThreadContextMap other = (ThreadContextMap)obj;
/* 250 */     Map<String, String> map = getImmutableMapOrNull();
/* 251 */     Map<String, String> otherMap = other.getImmutableMapOrNull();
/* 252 */     if (map == null) {
/* 253 */       if (otherMap != null) {
/* 254 */         return false;
/*     */       }
/* 256 */     } else if (!map.equals(otherMap)) {
/* 257 */       return false;
/*     */     } 
/* 259 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\spi\CopyOnWriteSortedArrayThreadContextMap.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */