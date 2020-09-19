/*     */ package net.minecraft.util;
/*     */ 
/*     */ import com.google.common.base.Predicates;
/*     */ import com.google.common.collect.Iterators;
/*     */ import java.util.Arrays;
/*     */ import java.util.Iterator;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.core.IdMap;
/*     */ 
/*     */ public class CrudeIncrementalIntIdentityHashBiMap<K>
/*     */   implements IdMap<K>
/*     */ {
/*  13 */   private static final Object EMPTY_SLOT = null;
/*     */   
/*     */   private K[] keys;
/*     */   
/*     */   private int[] values;
/*     */   
/*     */   private K[] byId;
/*     */   
/*     */   private int nextId;
/*     */   private int size;
/*     */   
/*     */   public CrudeIncrementalIntIdentityHashBiMap(int debug1) {
/*  25 */     debug1 = (int)(debug1 / 0.8F);
/*  26 */     this.keys = (K[])new Object[debug1];
/*  27 */     this.values = new int[debug1];
/*  28 */     this.byId = (K[])new Object[debug1];
/*     */   }
/*     */ 
/*     */   
/*     */   public int getId(@Nullable K debug1) {
/*  33 */     return getValue(indexOf(debug1, hash(debug1)));
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public K byId(int debug1) {
/*  39 */     if (debug1 < 0 || debug1 >= this.byId.length) {
/*  40 */       return null;
/*     */     }
/*     */     
/*  43 */     return this.byId[debug1];
/*     */   }
/*     */   
/*     */   private int getValue(int debug1) {
/*  47 */     if (debug1 == -1) {
/*  48 */       return -1;
/*     */     }
/*  50 */     return this.values[debug1];
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
/*     */   public int add(K debug1) {
/*  62 */     int debug2 = nextId();
/*     */     
/*  64 */     addMapping(debug1, debug2);
/*     */     
/*  66 */     return debug2;
/*     */   }
/*     */   
/*     */   private int nextId() {
/*  70 */     while (this.nextId < this.byId.length && this.byId[this.nextId] != null) {
/*  71 */       this.nextId++;
/*     */     }
/*  73 */     return this.nextId;
/*     */   }
/*     */ 
/*     */   
/*     */   private void grow(int debug1) {
/*  78 */     K[] debug2 = this.keys;
/*  79 */     int[] debug3 = this.values;
/*     */     
/*  81 */     this.keys = (K[])new Object[debug1];
/*  82 */     this.values = new int[debug1];
/*  83 */     this.byId = (K[])new Object[debug1];
/*  84 */     this.nextId = 0;
/*  85 */     this.size = 0;
/*     */     
/*  87 */     for (int debug4 = 0; debug4 < debug2.length; debug4++) {
/*  88 */       if (debug2[debug4] != null) {
/*  89 */         addMapping(debug2[debug4], debug3[debug4]);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public void addMapping(K debug1, int debug2) {
/*  95 */     int debug3 = Math.max(debug2, this.size + 1);
/*  96 */     if (debug3 >= this.keys.length * 0.8F) {
/*  97 */       int i = this.keys.length << 1;
/*  98 */       while (i < debug2) {
/*  99 */         i <<= 1;
/*     */       }
/* 101 */       grow(i);
/*     */     } 
/*     */     
/* 104 */     int debug4 = findEmpty(hash(debug1));
/* 105 */     this.keys[debug4] = debug1;
/* 106 */     this.values[debug4] = debug2;
/* 107 */     this.byId[debug2] = debug1;
/* 108 */     this.size++;
/*     */     
/* 110 */     if (debug2 == this.nextId) {
/* 111 */       this.nextId++;
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
/*     */   private int hash(@Nullable K debug1) {
/* 127 */     return (Mth.murmurHash3Mixer(System.identityHashCode(debug1)) & Integer.MAX_VALUE) % this.keys.length;
/*     */   }
/*     */   private int indexOf(@Nullable K debug1, int debug2) {
/*     */     int debug3;
/* 131 */     for (debug3 = debug2; debug3 < this.keys.length; debug3++) {
/* 132 */       if (this.keys[debug3] == debug1) {
/* 133 */         return debug3;
/*     */       }
/* 135 */       if (this.keys[debug3] == EMPTY_SLOT) {
/* 136 */         return -1;
/*     */       }
/*     */     } 
/*     */     
/* 140 */     for (debug3 = 0; debug3 < debug2; debug3++) {
/* 141 */       if (this.keys[debug3] == debug1) {
/* 142 */         return debug3;
/*     */       }
/* 144 */       if (this.keys[debug3] == EMPTY_SLOT) {
/* 145 */         return -1;
/*     */       }
/*     */     } 
/*     */     
/* 149 */     return -1;
/*     */   }
/*     */   private int findEmpty(int debug1) {
/*     */     int debug2;
/* 153 */     for (debug2 = debug1; debug2 < this.keys.length; debug2++) {
/* 154 */       if (this.keys[debug2] == EMPTY_SLOT) {
/* 155 */         return debug2;
/*     */       }
/*     */     } 
/*     */     
/* 159 */     for (debug2 = 0; debug2 < debug1; debug2++) {
/* 160 */       if (this.keys[debug2] == EMPTY_SLOT) {
/* 161 */         return debug2;
/*     */       }
/*     */     } 
/*     */     
/* 165 */     throw new RuntimeException("Overflowed :(");
/*     */   }
/*     */ 
/*     */   
/*     */   public Iterator<K> iterator() {
/* 170 */     return (Iterator<K>)Iterators.filter((Iterator)Iterators.forArray((Object[])this.byId), Predicates.notNull());
/*     */   }
/*     */   
/*     */   public void clear() {
/* 174 */     Arrays.fill((Object[])this.keys, (Object)null);
/* 175 */     Arrays.fill((Object[])this.byId, (Object)null);
/* 176 */     this.nextId = 0;
/* 177 */     this.size = 0;
/*     */   }
/*     */   
/*     */   public int size() {
/* 181 */     return this.size;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\CrudeIncrementalIntIdentityHashBiMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */