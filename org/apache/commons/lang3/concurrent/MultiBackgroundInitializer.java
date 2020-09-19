/*     */ package org.apache.commons.lang3.concurrent;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MultiBackgroundInitializer
/*     */   extends BackgroundInitializer<MultiBackgroundInitializer.MultiBackgroundInitializerResults>
/*     */ {
/* 100 */   private final Map<String, BackgroundInitializer<?>> childInitializers = new HashMap<String, BackgroundInitializer<?>>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MultiBackgroundInitializer(ExecutorService exec) {
/* 118 */     super(exec);
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
/*     */   public void addInitializer(String name, BackgroundInitializer<?> init) {
/* 134 */     if (name == null) {
/* 135 */       throw new IllegalArgumentException("Name of child initializer must not be null!");
/*     */     }
/*     */     
/* 138 */     if (init == null) {
/* 139 */       throw new IllegalArgumentException("Child initializer must not be null!");
/*     */     }
/*     */ 
/*     */     
/* 143 */     synchronized (this) {
/* 144 */       if (isStarted()) {
/* 145 */         throw new IllegalStateException("addInitializer() must not be called after start()!");
/*     */       }
/*     */       
/* 148 */       this.childInitializers.put(name, init);
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
/*     */   protected int getTaskCount() {
/* 164 */     int result = 1;
/*     */     
/* 166 */     for (BackgroundInitializer<?> bi : this.childInitializers.values()) {
/* 167 */       result += bi.getTaskCount();
/*     */     }
/*     */     
/* 170 */     return result;
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
/*     */   protected MultiBackgroundInitializerResults initialize() throws Exception {
/*     */     Map<String, BackgroundInitializer<?>> inits;
/* 186 */     synchronized (this) {
/*     */       
/* 188 */       inits = new HashMap<String, BackgroundInitializer<?>>(this.childInitializers);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 193 */     ExecutorService exec = getActiveExecutor();
/* 194 */     for (BackgroundInitializer<?> bi : inits.values()) {
/* 195 */       if (bi.getExternalExecutor() == null)
/*     */       {
/* 197 */         bi.setExternalExecutor(exec);
/*     */       }
/* 199 */       bi.start();
/*     */     } 
/*     */ 
/*     */     
/* 203 */     Map<String, Object> results = new HashMap<String, Object>();
/* 204 */     Map<String, ConcurrentException> excepts = new HashMap<String, ConcurrentException>();
/* 205 */     for (Map.Entry<String, BackgroundInitializer<?>> e : inits.entrySet()) {
/*     */       try {
/* 207 */         results.put(e.getKey(), ((BackgroundInitializer)e.getValue()).get());
/* 208 */       } catch (ConcurrentException cex) {
/* 209 */         excepts.put(e.getKey(), cex);
/*     */       } 
/*     */     } 
/*     */     
/* 213 */     return new MultiBackgroundInitializerResults(inits, results, excepts);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MultiBackgroundInitializer() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class MultiBackgroundInitializerResults
/*     */   {
/*     */     private final Map<String, BackgroundInitializer<?>> initializers;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final Map<String, Object> resultObjects;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final Map<String, ConcurrentException> exceptions;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private MultiBackgroundInitializerResults(Map<String, BackgroundInitializer<?>> inits, Map<String, Object> results, Map<String, ConcurrentException> excepts) {
/* 249 */       this.initializers = inits;
/* 250 */       this.resultObjects = results;
/* 251 */       this.exceptions = excepts;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public BackgroundInitializer<?> getInitializer(String name) {
/* 263 */       return checkName(name);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Object getResultObject(String name) {
/* 279 */       checkName(name);
/* 280 */       return this.resultObjects.get(name);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean isException(String name) {
/* 292 */       checkName(name);
/* 293 */       return this.exceptions.containsKey(name);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ConcurrentException getException(String name) {
/* 307 */       checkName(name);
/* 308 */       return this.exceptions.get(name);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Set<String> initializerNames() {
/* 319 */       return Collections.unmodifiableSet(this.initializers.keySet());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean isSuccessful() {
/* 329 */       return this.exceptions.isEmpty();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private BackgroundInitializer<?> checkName(String name) {
/* 342 */       BackgroundInitializer<?> init = this.initializers.get(name);
/* 343 */       if (init == null) {
/* 344 */         throw new NoSuchElementException("No child initializer with name " + name);
/*     */       }
/*     */ 
/*     */       
/* 348 */       return init;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\commons\lang3\concurrent\MultiBackgroundInitializer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */