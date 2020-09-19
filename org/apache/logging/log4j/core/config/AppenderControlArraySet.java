/*     */ package org.apache.logging.log4j.core.config;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.concurrent.atomic.AtomicReference;
/*     */ import org.apache.logging.log4j.core.Appender;
/*     */ import org.apache.logging.log4j.util.PerformanceSensitive;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @PerformanceSensitive
/*     */ public class AppenderControlArraySet
/*     */ {
/*  35 */   private final AtomicReference<AppenderControl[]> appenderArray = (AtomicReference)new AtomicReference<>(new AppenderControl[0]);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean add(AppenderControl control) {
/*     */     while (true) {
/*  47 */       AppenderControl[] original = this.appenderArray.get();
/*  48 */       for (AppenderControl existing : original) {
/*  49 */         if (existing.equals(control)) {
/*  50 */           return false;
/*     */         }
/*     */       } 
/*  53 */       AppenderControl[] copy = Arrays.<AppenderControl>copyOf(original, original.length + 1);
/*  54 */       copy[copy.length - 1] = control;
/*  55 */       boolean success = this.appenderArray.compareAndSet(original, copy);
/*  56 */       if (success) {
/*  57 */         return true;
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AppenderControl remove(String name) {
/*     */     while (true) {
/*  69 */       boolean success = true;
/*  70 */       AppenderControl[] original = this.appenderArray.get();
/*  71 */       for (int i = 0; i < original.length; i++) {
/*  72 */         AppenderControl appenderControl = original[i];
/*  73 */         if (Objects.equals(name, appenderControl.getAppenderName())) {
/*  74 */           AppenderControl[] copy = removeElementAt(i, original);
/*  75 */           if (this.appenderArray.compareAndSet(original, copy)) {
/*  76 */             return appenderControl;
/*     */           }
/*  78 */           success = false;
/*     */           break;
/*     */         } 
/*     */       } 
/*  82 */       if (success)
/*  83 */         return null; 
/*     */     } 
/*     */   }
/*     */   private AppenderControl[] removeElementAt(int i, AppenderControl[] array) {
/*  87 */     AppenderControl[] result = Arrays.<AppenderControl>copyOf(array, array.length - 1);
/*  88 */     System.arraycopy(array, i + 1, result, i, result.length - i);
/*  89 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<String, Appender> asMap() {
/*  98 */     Map<String, Appender> result = new HashMap<>();
/*  99 */     for (AppenderControl appenderControl : (AppenderControl[])this.appenderArray.get()) {
/* 100 */       result.put(appenderControl.getAppenderName(), appenderControl.getAppender());
/*     */     }
/* 102 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AppenderControl[] clear() {
/* 111 */     return this.appenderArray.getAndSet(new AppenderControl[0]);
/*     */   }
/*     */   
/*     */   public boolean isEmpty() {
/* 115 */     return (((AppenderControl[])this.appenderArray.get()).length == 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AppenderControl[] get() {
/* 124 */     return this.appenderArray.get();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\config\AppenderControlArraySet.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */