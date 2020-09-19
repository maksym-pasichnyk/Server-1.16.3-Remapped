/*     */ package com.google.common.cache;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Function;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Supplier;
/*     */ import com.google.common.util.concurrent.Futures;
/*     */ import com.google.common.util.concurrent.ListenableFuture;
/*     */ import com.google.common.util.concurrent.ListenableFutureTask;
/*     */ import java.io.Serializable;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.Callable;
/*     */ import java.util.concurrent.Executor;
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
/*     */ @GwtCompatible(emulated = true)
/*     */ public abstract class CacheLoader<K, V>
/*     */ {
/*     */   public abstract V load(K paramK) throws Exception;
/*     */   
/*     */   @GwtIncompatible
/*     */   public ListenableFuture<V> reload(K key, V oldValue) throws Exception {
/*  91 */     Preconditions.checkNotNull(key);
/*  92 */     Preconditions.checkNotNull(oldValue);
/*  93 */     return Futures.immediateFuture(load(key));
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
/*     */   public Map<K, V> loadAll(Iterable<? extends K> keys) throws Exception {
/* 121 */     throw new UnsupportedLoadingOperationException();
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
/*     */   public static <K, V> CacheLoader<K, V> from(Function<K, V> function) {
/* 133 */     return new FunctionToCacheLoader<>(function);
/*     */   }
/*     */   
/*     */   private static final class FunctionToCacheLoader<K, V> extends CacheLoader<K, V> implements Serializable {
/*     */     private final Function<K, V> computingFunction;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     public FunctionToCacheLoader(Function<K, V> computingFunction) {
/* 141 */       this.computingFunction = (Function<K, V>)Preconditions.checkNotNull(computingFunction);
/*     */     }
/*     */ 
/*     */     
/*     */     public V load(K key) {
/* 146 */       return (V)this.computingFunction.apply(Preconditions.checkNotNull(key));
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
/*     */   public static <V> CacheLoader<Object, V> from(Supplier<V> supplier) {
/* 162 */     return new SupplierToCacheLoader<>(supplier);
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
/*     */   @GwtIncompatible
/*     */   public static <K, V> CacheLoader<K, V> asyncReloading(final CacheLoader<K, V> loader, final Executor executor) {
/* 177 */     Preconditions.checkNotNull(loader);
/* 178 */     Preconditions.checkNotNull(executor);
/* 179 */     return new CacheLoader<K, V>()
/*     */       {
/*     */         public V load(K key) throws Exception {
/* 182 */           return loader.load(key);
/*     */         }
/*     */ 
/*     */ 
/*     */         
/*     */         public ListenableFuture<V> reload(final K key, final V oldValue) throws Exception {
/* 188 */           ListenableFutureTask<V> task = ListenableFutureTask.create(new Callable<V>()
/*     */               {
/*     */                 public V call() throws Exception
/*     */                 {
/* 192 */                   return (V)loader.reload(key, (V)oldValue).get();
/*     */                 }
/*     */               });
/* 195 */           executor.execute((Runnable)task);
/* 196 */           return (ListenableFuture<V>)task;
/*     */         }
/*     */ 
/*     */         
/*     */         public Map<K, V> loadAll(Iterable<? extends K> keys) throws Exception {
/* 201 */           return loader.loadAll(keys);
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   private static final class SupplierToCacheLoader<V> extends CacheLoader<Object, V> implements Serializable {
/*     */     private final Supplier<V> computingSupplier;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     public SupplierToCacheLoader(Supplier<V> computingSupplier) {
/* 211 */       this.computingSupplier = (Supplier<V>)Preconditions.checkNotNull(computingSupplier);
/*     */     }
/*     */ 
/*     */     
/*     */     public V load(Object key) {
/* 216 */       Preconditions.checkNotNull(key);
/* 217 */       return (V)this.computingSupplier.get();
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
/*     */   public static final class UnsupportedLoadingOperationException
/*     */     extends UnsupportedOperationException {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final class InvalidCacheLoadException
/*     */     extends RuntimeException
/*     */   {
/*     */     public InvalidCacheLoadException(String message) {
/* 242 */       super(message);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\cache\CacheLoader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */