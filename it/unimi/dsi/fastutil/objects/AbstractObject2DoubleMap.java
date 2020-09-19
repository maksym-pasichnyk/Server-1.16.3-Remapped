/*     */ package it.unimi.dsi.fastutil.objects;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.HashCommon;
/*     */ import it.unimi.dsi.fastutil.doubles.AbstractDoubleCollection;
/*     */ import it.unimi.dsi.fastutil.doubles.DoubleCollection;
/*     */ import it.unimi.dsi.fastutil.doubles.DoubleIterator;
/*     */ import java.io.Serializable;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
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
/*     */ public abstract class AbstractObject2DoubleMap<K>
/*     */   extends AbstractObject2DoubleFunction<K>
/*     */   implements Object2DoubleMap<K>, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -4940583368468432370L;
/*     */   
/*     */   public boolean containsValue(double v) {
/*  48 */     return values().contains(v);
/*     */   }
/*     */   
/*     */   public boolean containsKey(Object k) {
/*  52 */     ObjectIterator<Object2DoubleMap.Entry<K>> i = object2DoubleEntrySet().iterator();
/*  53 */     while (i.hasNext()) {
/*  54 */       if (((Object2DoubleMap.Entry)i.next()).getKey() == k)
/*  55 */         return true; 
/*  56 */     }  return false;
/*     */   }
/*     */   
/*     */   public boolean isEmpty() {
/*  60 */     return (size() == 0);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static class BasicEntry<K>
/*     */     implements Object2DoubleMap.Entry<K>
/*     */   {
/*     */     protected K key;
/*     */     
/*     */     protected double value;
/*     */ 
/*     */     
/*     */     public BasicEntry() {}
/*     */ 
/*     */     
/*     */     public BasicEntry(K key, Double value) {
/*  77 */       this.key = key;
/*  78 */       this.value = value.doubleValue();
/*     */     }
/*     */     public BasicEntry(K key, double value) {
/*  81 */       this.key = key;
/*  82 */       this.value = value;
/*     */     }
/*     */     
/*     */     public K getKey() {
/*  86 */       return this.key;
/*     */     }
/*     */     
/*     */     public double getDoubleValue() {
/*  90 */       return this.value;
/*     */     }
/*     */     
/*     */     public double setValue(double value) {
/*  94 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object o) {
/*  99 */       if (!(o instanceof Map.Entry))
/* 100 */         return false; 
/* 101 */       if (o instanceof Object2DoubleMap.Entry) {
/* 102 */         Object2DoubleMap.Entry<K> entry = (Object2DoubleMap.Entry<K>)o;
/* 103 */         return (Objects.equals(this.key, entry.getKey()) && 
/* 104 */           Double.doubleToLongBits(this.value) == Double.doubleToLongBits(entry.getDoubleValue()));
/*     */       } 
/* 106 */       Map.Entry<?, ?> e = (Map.Entry<?, ?>)o;
/* 107 */       Object key = e.getKey();
/* 108 */       Object value = e.getValue();
/* 109 */       if (value == null || !(value instanceof Double))
/* 110 */         return false; 
/* 111 */       return (Objects.equals(this.key, key) && Double.doubleToLongBits(this.value) == 
/* 112 */         Double.doubleToLongBits(((Double)value).doubleValue()));
/*     */     }
/*     */     
/*     */     public int hashCode() {
/* 116 */       return ((this.key == null) ? 0 : this.key.hashCode()) ^ HashCommon.double2int(this.value);
/*     */     }
/*     */     
/*     */     public String toString() {
/* 120 */       return (new StringBuilder()).append(this.key).append("->").append(this.value).toString();
/*     */     }
/*     */   }
/*     */   
/*     */   public static abstract class BasicEntrySet<K>
/*     */     extends AbstractObjectSet<Object2DoubleMap.Entry<K>>
/*     */   {
/*     */     protected final Object2DoubleMap<K> map;
/*     */     
/*     */     public BasicEntrySet(Object2DoubleMap<K> map) {
/* 130 */       this.map = map;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean contains(Object o) {
/* 135 */       if (!(o instanceof Map.Entry))
/* 136 */         return false; 
/* 137 */       if (o instanceof Object2DoubleMap.Entry) {
/* 138 */         Object2DoubleMap.Entry<K> entry = (Object2DoubleMap.Entry<K>)o;
/* 139 */         K k1 = entry.getKey();
/* 140 */         return (this.map.containsKey(k1) && 
/* 141 */           Double.doubleToLongBits(this.map.getDouble(k1)) == Double.doubleToLongBits(entry.getDoubleValue()));
/*     */       } 
/* 143 */       Map.Entry<?, ?> e = (Map.Entry<?, ?>)o;
/* 144 */       Object k = e.getKey();
/* 145 */       Object value = e.getValue();
/* 146 */       if (value == null || !(value instanceof Double))
/* 147 */         return false; 
/* 148 */       return (this.map.containsKey(k) && Double.doubleToLongBits(this.map.getDouble(k)) == 
/* 149 */         Double.doubleToLongBits(((Double)value).doubleValue()));
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean remove(Object o) {
/* 154 */       if (!(o instanceof Map.Entry))
/* 155 */         return false; 
/* 156 */       if (o instanceof Object2DoubleMap.Entry) {
/* 157 */         Object2DoubleMap.Entry<K> entry = (Object2DoubleMap.Entry<K>)o;
/* 158 */         return this.map.remove(entry.getKey(), entry.getDoubleValue());
/*     */       } 
/* 160 */       Map.Entry<?, ?> e = (Map.Entry<?, ?>)o;
/* 161 */       Object k = e.getKey();
/* 162 */       Object value = e.getValue();
/* 163 */       if (value == null || !(value instanceof Double))
/* 164 */         return false; 
/* 165 */       double v = ((Double)value).doubleValue();
/* 166 */       return this.map.remove(k, v);
/*     */     }
/*     */     
/*     */     public int size() {
/* 170 */       return this.map.size();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ObjectSet<K> keySet() {
/* 189 */     return new AbstractObjectSet<K>()
/*     */       {
/*     */         public boolean contains(Object k) {
/* 192 */           return AbstractObject2DoubleMap.this.containsKey(k);
/*     */         }
/*     */         
/*     */         public int size() {
/* 196 */           return AbstractObject2DoubleMap.this.size();
/*     */         }
/*     */         
/*     */         public void clear() {
/* 200 */           AbstractObject2DoubleMap.this.clear();
/*     */         }
/*     */         
/*     */         public ObjectIterator<K> iterator() {
/* 204 */           return new ObjectIterator<K>()
/*     */             {
/* 206 */               private final ObjectIterator<Object2DoubleMap.Entry<K>> i = Object2DoubleMaps.fastIterator(AbstractObject2DoubleMap.this);
/*     */               
/*     */               public K next() {
/* 209 */                 return ((Object2DoubleMap.Entry<K>)this.i.next()).getKey();
/*     */               }
/*     */               
/*     */               public boolean hasNext() {
/* 213 */                 return this.i.hasNext();
/*     */               }
/*     */               
/*     */               public void remove() {
/* 217 */                 this.i.remove();
/*     */               }
/*     */             };
/*     */         }
/*     */       };
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
/*     */ 
/*     */   
/*     */   public DoubleCollection values() {
/* 239 */     return (DoubleCollection)new AbstractDoubleCollection()
/*     */       {
/*     */         public boolean contains(double k) {
/* 242 */           return AbstractObject2DoubleMap.this.containsValue(k);
/*     */         }
/*     */         
/*     */         public int size() {
/* 246 */           return AbstractObject2DoubleMap.this.size();
/*     */         }
/*     */         
/*     */         public void clear() {
/* 250 */           AbstractObject2DoubleMap.this.clear();
/*     */         }
/*     */         
/*     */         public DoubleIterator iterator() {
/* 254 */           return new DoubleIterator()
/*     */             {
/* 256 */               private final ObjectIterator<Object2DoubleMap.Entry<K>> i = Object2DoubleMaps.fastIterator(AbstractObject2DoubleMap.this);
/*     */               
/*     */               public double nextDouble() {
/* 259 */                 return ((Object2DoubleMap.Entry)this.i.next()).getDoubleValue();
/*     */               }
/*     */               
/*     */               public boolean hasNext() {
/* 263 */                 return this.i.hasNext();
/*     */               }
/*     */             };
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void putAll(Map<? extends K, ? extends Double> m) {
/* 273 */     if (m instanceof Object2DoubleMap) {
/* 274 */       ObjectIterator<Object2DoubleMap.Entry<K>> i = Object2DoubleMaps.fastIterator((Object2DoubleMap)m);
/* 275 */       while (i.hasNext()) {
/* 276 */         Object2DoubleMap.Entry<? extends K> e = i.next();
/* 277 */         put(e.getKey(), e.getDoubleValue());
/*     */       } 
/*     */     } else {
/* 280 */       int n = m.size();
/* 281 */       Iterator<? extends Map.Entry<? extends K, ? extends Double>> i = m.entrySet().iterator();
/*     */       
/* 283 */       while (n-- != 0) {
/* 284 */         Map.Entry<? extends K, ? extends Double> e = i.next();
/* 285 */         put(e.getKey(), e.getValue());
/*     */       } 
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
/*     */   public int hashCode() {
/* 298 */     int h = 0, n = size();
/* 299 */     ObjectIterator<Object2DoubleMap.Entry<K>> i = Object2DoubleMaps.fastIterator(this);
/* 300 */     while (n-- != 0)
/* 301 */       h += ((Object2DoubleMap.Entry)i.next()).hashCode(); 
/* 302 */     return h;
/*     */   }
/*     */   
/*     */   public boolean equals(Object o) {
/* 306 */     if (o == this)
/* 307 */       return true; 
/* 308 */     if (!(o instanceof Map))
/* 309 */       return false; 
/* 310 */     Map<?, ?> m = (Map<?, ?>)o;
/* 311 */     if (m.size() != size())
/* 312 */       return false; 
/* 313 */     return object2DoubleEntrySet().containsAll(m.entrySet());
/*     */   }
/*     */   
/*     */   public String toString() {
/* 317 */     StringBuilder s = new StringBuilder();
/* 318 */     ObjectIterator<Object2DoubleMap.Entry<K>> i = Object2DoubleMaps.fastIterator(this);
/* 319 */     int n = size();
/*     */     
/* 321 */     boolean first = true;
/* 322 */     s.append("{");
/* 323 */     while (n-- != 0) {
/* 324 */       if (first) {
/* 325 */         first = false;
/*     */       } else {
/* 327 */         s.append(", ");
/* 328 */       }  Object2DoubleMap.Entry<K> e = i.next();
/* 329 */       if (this == e.getKey()) {
/* 330 */         s.append("(this map)");
/*     */       } else {
/* 332 */         s.append(String.valueOf(e.getKey()));
/* 333 */       }  s.append("=>");
/* 334 */       s.append(String.valueOf(e.getDoubleValue()));
/*     */     } 
/* 336 */     s.append("}");
/* 337 */     return s.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\i\\unimi\dsi\fastutil\objects\AbstractObject2DoubleMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */