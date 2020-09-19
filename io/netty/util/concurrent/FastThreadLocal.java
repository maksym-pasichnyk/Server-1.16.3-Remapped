/*     */ package io.netty.util.concurrent;
/*     */ 
/*     */ import io.netty.util.internal.InternalThreadLocalMap;
/*     */ import io.netty.util.internal.ObjectCleaner;
/*     */ import io.netty.util.internal.PlatformDependent;
/*     */ import java.util.Collections;
/*     */ import java.util.IdentityHashMap;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FastThreadLocal<V>
/*     */ {
/*  47 */   private static final int variablesToRemoveIndex = InternalThreadLocalMap.nextVariableIndex();
/*     */ 
/*     */   
/*     */   private final int index;
/*     */ 
/*     */ 
/*     */   
/*     */   public static void removeAll() {
/*  55 */     InternalThreadLocalMap threadLocalMap = InternalThreadLocalMap.getIfSet();
/*  56 */     if (threadLocalMap == null) {
/*     */       return;
/*     */     }
/*     */     
/*     */     try {
/*  61 */       Object v = threadLocalMap.indexedVariable(variablesToRemoveIndex);
/*  62 */       if (v != null && v != InternalThreadLocalMap.UNSET) {
/*     */         
/*  64 */         Set<FastThreadLocal<?>> variablesToRemove = (Set<FastThreadLocal<?>>)v;
/*     */         
/*  66 */         FastThreadLocal[] arrayOfFastThreadLocal = variablesToRemove.<FastThreadLocal>toArray(new FastThreadLocal[variablesToRemove.size()]);
/*  67 */         for (FastThreadLocal<?> tlv : arrayOfFastThreadLocal) {
/*  68 */           tlv.remove(threadLocalMap);
/*     */         }
/*     */       } 
/*     */     } finally {
/*  72 */       InternalThreadLocalMap.remove();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int size() {
/*  80 */     InternalThreadLocalMap threadLocalMap = InternalThreadLocalMap.getIfSet();
/*  81 */     if (threadLocalMap == null) {
/*  82 */       return 0;
/*     */     }
/*  84 */     return threadLocalMap.size();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void destroy() {
/*  95 */     InternalThreadLocalMap.destroy();
/*     */   }
/*     */   
/*     */   private static void addToVariablesToRemove(InternalThreadLocalMap threadLocalMap, FastThreadLocal<?> variable) {
/*     */     Set<FastThreadLocal<?>> variablesToRemove;
/* 100 */     Object v = threadLocalMap.indexedVariable(variablesToRemoveIndex);
/*     */     
/* 102 */     if (v == InternalThreadLocalMap.UNSET || v == null) {
/* 103 */       variablesToRemove = Collections.newSetFromMap(new IdentityHashMap<FastThreadLocal<?>, Boolean>());
/* 104 */       threadLocalMap.setIndexedVariable(variablesToRemoveIndex, variablesToRemove);
/*     */     } else {
/* 106 */       variablesToRemove = (Set<FastThreadLocal<?>>)v;
/*     */     } 
/*     */     
/* 109 */     variablesToRemove.add(variable);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static void removeFromVariablesToRemove(InternalThreadLocalMap threadLocalMap, FastThreadLocal<?> variable) {
/* 115 */     Object v = threadLocalMap.indexedVariable(variablesToRemoveIndex);
/*     */     
/* 117 */     if (v == InternalThreadLocalMap.UNSET || v == null) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 122 */     Set<FastThreadLocal<?>> variablesToRemove = (Set<FastThreadLocal<?>>)v;
/* 123 */     variablesToRemove.remove(variable);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public FastThreadLocal() {
/* 129 */     this.index = InternalThreadLocalMap.nextVariableIndex();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final V get() {
/* 137 */     InternalThreadLocalMap threadLocalMap = InternalThreadLocalMap.get();
/* 138 */     Object v = threadLocalMap.indexedVariable(this.index);
/* 139 */     if (v != InternalThreadLocalMap.UNSET) {
/* 140 */       return (V)v;
/*     */     }
/*     */     
/* 143 */     V value = initialize(threadLocalMap);
/* 144 */     registerCleaner(threadLocalMap);
/* 145 */     return value;
/*     */   }
/*     */   
/*     */   private void registerCleaner(final InternalThreadLocalMap threadLocalMap) {
/* 149 */     Thread current = Thread.currentThread();
/* 150 */     if (FastThreadLocalThread.willCleanupFastThreadLocals(current) || threadLocalMap.isCleanerFlagSet(this.index)) {
/*     */       return;
/*     */     }
/*     */     
/* 154 */     threadLocalMap.setCleanerFlag(this.index);
/*     */ 
/*     */ 
/*     */     
/* 158 */     ObjectCleaner.register(current, new Runnable()
/*     */         {
/*     */           public void run() {
/* 161 */             FastThreadLocal.this.remove(threadLocalMap);
/*     */           }
/*     */         });
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
/*     */   public final V get(InternalThreadLocalMap threadLocalMap) {
/* 175 */     Object v = threadLocalMap.indexedVariable(this.index);
/* 176 */     if (v != InternalThreadLocalMap.UNSET) {
/* 177 */       return (V)v;
/*     */     }
/*     */     
/* 180 */     return initialize(threadLocalMap);
/*     */   }
/*     */   
/*     */   private V initialize(InternalThreadLocalMap threadLocalMap) {
/* 184 */     V v = null;
/*     */     try {
/* 186 */       v = initialValue();
/* 187 */     } catch (Exception e) {
/* 188 */       PlatformDependent.throwException(e);
/*     */     } 
/*     */     
/* 191 */     threadLocalMap.setIndexedVariable(this.index, v);
/* 192 */     addToVariablesToRemove(threadLocalMap, this);
/* 193 */     return v;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void set(V value) {
/* 200 */     if (value != InternalThreadLocalMap.UNSET) {
/* 201 */       InternalThreadLocalMap threadLocalMap = InternalThreadLocalMap.get();
/* 202 */       if (setKnownNotUnset(threadLocalMap, value)) {
/* 203 */         registerCleaner(threadLocalMap);
/*     */       }
/*     */     } else {
/* 206 */       remove();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void set(InternalThreadLocalMap threadLocalMap, V value) {
/* 214 */     if (value != InternalThreadLocalMap.UNSET) {
/* 215 */       setKnownNotUnset(threadLocalMap, value);
/*     */     } else {
/* 217 */       remove(threadLocalMap);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean setKnownNotUnset(InternalThreadLocalMap threadLocalMap, V value) {
/* 225 */     if (threadLocalMap.setIndexedVariable(this.index, value)) {
/* 226 */       addToVariablesToRemove(threadLocalMap, this);
/* 227 */       return true;
/*     */     } 
/* 229 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean isSet() {
/* 236 */     return isSet(InternalThreadLocalMap.getIfSet());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean isSet(InternalThreadLocalMap threadLocalMap) {
/* 244 */     return (threadLocalMap != null && threadLocalMap.isIndexedVariableSet(this.index));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final void remove() {
/* 250 */     remove(InternalThreadLocalMap.getIfSet());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void remove(InternalThreadLocalMap threadLocalMap) {
/* 260 */     if (threadLocalMap == null) {
/*     */       return;
/*     */     }
/*     */     
/* 264 */     Object v = threadLocalMap.removeIndexedVariable(this.index);
/* 265 */     removeFromVariablesToRemove(threadLocalMap, this);
/*     */     
/* 267 */     if (v != InternalThreadLocalMap.UNSET) {
/*     */       try {
/* 269 */         onRemoval((V)v);
/* 270 */       } catch (Exception e) {
/* 271 */         PlatformDependent.throwException(e);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected V initialValue() throws Exception {
/* 280 */     return null;
/*     */   }
/*     */   
/*     */   protected void onRemoval(V value) throws Exception {}
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\nett\\util\concurrent\FastThreadLocal.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */