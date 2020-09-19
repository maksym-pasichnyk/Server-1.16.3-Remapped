/*     */ package org.apache.logging.log4j.core.impl;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.Comparator;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import org.apache.logging.log4j.util.BiConsumer;
/*     */ import org.apache.logging.log4j.util.ReadOnlyStringMap;
/*     */ import org.apache.logging.log4j.util.StringMap;
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
/*     */ class JdkMapAdapterStringMap
/*     */   implements StringMap
/*     */ {
/*     */   private static final long serialVersionUID = -7348247784983193612L;
/*     */   private static final String FROZEN = "Frozen collection cannot be modified";
/*     */   
/*  36 */   private static final Comparator<? super String> NULL_FIRST_COMPARATOR = new Comparator<String>()
/*     */     {
/*     */       public int compare(String left, String right) {
/*  39 */         if (left == null) {
/*  40 */           return -1;
/*     */         }
/*  42 */         if (right == null) {
/*  43 */           return 1;
/*     */         }
/*  45 */         return left.compareTo(right);
/*     */       }
/*     */     };
/*     */   
/*     */   private final Map<String, String> map;
/*     */   private boolean immutable = false;
/*     */   private transient String[] sortedKeys;
/*     */   
/*     */   public JdkMapAdapterStringMap() {
/*  54 */     this(new HashMap<>());
/*     */   }
/*     */   
/*     */   public JdkMapAdapterStringMap(Map<String, String> map) {
/*  58 */     this.map = Objects.<Map<String, String>>requireNonNull(map, "map");
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<String, String> toMap() {
/*  63 */     return this.map;
/*     */   }
/*     */   
/*     */   private void assertNotFrozen() {
/*  67 */     if (this.immutable) {
/*  68 */       throw new UnsupportedOperationException("Frozen collection cannot be modified");
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsKey(String key) {
/*  74 */     return this.map.containsKey(key);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public <V> void forEach(BiConsumer<String, ? super V> action) {
/*  80 */     String[] keys = getSortedKeys();
/*  81 */     for (int i = 0; i < keys.length; i++) {
/*  82 */       action.accept(keys[i], this.map.get(keys[i]));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public <V, S> void forEach(TriConsumer<String, ? super V, S> action, S state) {
/*  89 */     String[] keys = getSortedKeys();
/*  90 */     for (int i = 0; i < keys.length; i++) {
/*  91 */       action.accept(keys[i], this.map.get(keys[i]), state);
/*     */     }
/*     */   }
/*     */   
/*     */   private String[] getSortedKeys() {
/*  96 */     if (this.sortedKeys == null) {
/*  97 */       this.sortedKeys = (String[])this.map.keySet().toArray((Object[])new String[this.map.size()]);
/*  98 */       Arrays.sort(this.sortedKeys, NULL_FIRST_COMPARATOR);
/*     */     } 
/* 100 */     return this.sortedKeys;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public <V> V getValue(String key) {
/* 106 */     return (V)this.map.get(key);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 111 */     return this.map.isEmpty();
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/* 116 */     return this.map.size();
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/* 121 */     if (this.map.isEmpty()) {
/*     */       return;
/*     */     }
/* 124 */     assertNotFrozen();
/* 125 */     this.map.clear();
/* 126 */     this.sortedKeys = null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void freeze() {
/* 131 */     this.immutable = true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isFrozen() {
/* 136 */     return this.immutable;
/*     */   }
/*     */ 
/*     */   
/*     */   public void putAll(ReadOnlyStringMap source) {
/* 141 */     assertNotFrozen();
/* 142 */     source.forEach(PUT_ALL, this.map);
/* 143 */     this.sortedKeys = null;
/*     */   }
/*     */   
/* 146 */   private static TriConsumer<String, String, Map<String, String>> PUT_ALL = new TriConsumer<String, String, Map<String, String>>()
/*     */     {
/*     */       public void accept(String key, String value, Map<String, String> stringStringMap) {
/* 149 */         stringStringMap.put(key, value);
/*     */       }
/*     */     };
/*     */ 
/*     */   
/*     */   public void putValue(String key, Object value) {
/* 155 */     assertNotFrozen();
/* 156 */     this.map.put(key, (value == null) ? null : String.valueOf(value));
/* 157 */     this.sortedKeys = null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void remove(String key) {
/* 162 */     if (!this.map.containsKey(key)) {
/*     */       return;
/*     */     }
/* 165 */     assertNotFrozen();
/* 166 */     this.map.remove(key);
/* 167 */     this.sortedKeys = null;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 172 */     StringBuilder result = new StringBuilder(this.map.size() * 13);
/* 173 */     result.append('{');
/* 174 */     String[] keys = getSortedKeys();
/* 175 */     for (int i = 0; i < keys.length; i++) {
/* 176 */       if (i > 0) {
/* 177 */         result.append(", ");
/*     */       }
/* 179 */       result.append(keys[i]).append('=').append(this.map.get(keys[i]));
/*     */     } 
/* 181 */     result.append('}');
/* 182 */     return result.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object object) {
/* 187 */     if (object == this) {
/* 188 */       return true;
/*     */     }
/* 190 */     if (!(object instanceof JdkMapAdapterStringMap)) {
/* 191 */       return false;
/*     */     }
/* 193 */     JdkMapAdapterStringMap other = (JdkMapAdapterStringMap)object;
/* 194 */     return (this.map.equals(other.map) && this.immutable == other.immutable);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 199 */     return this.map.hashCode() + (this.immutable ? 31 : 0);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\impl\JdkMapAdapterStringMap.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */