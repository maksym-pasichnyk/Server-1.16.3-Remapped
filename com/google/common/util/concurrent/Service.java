/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.TimeoutException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Beta
/*     */ @GwtIncompatible
/*     */ public interface Service
/*     */ {
/*     */   @CanIgnoreReturnValue
/*     */   Service startAsync();
/*     */   
/*     */   boolean isRunning();
/*     */   
/*     */   State state();
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   Service stopAsync();
/*     */   
/*     */   void awaitRunning();
/*     */   
/*     */   void awaitRunning(long paramLong, TimeUnit paramTimeUnit) throws TimeoutException;
/*     */   
/*     */   void awaitTerminated();
/*     */   
/*     */   void awaitTerminated(long paramLong, TimeUnit paramTimeUnit) throws TimeoutException;
/*     */   
/*     */   Throwable failureCause();
/*     */   
/*     */   void addListener(Listener paramListener, Executor paramExecutor);
/*     */   
/*     */   @Beta
/*     */   public enum State
/*     */   {
/* 189 */     NEW
/*     */     {
/*     */       boolean isTerminal() {
/* 192 */         return false;
/*     */       }
/*     */     },
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 199 */     STARTING
/*     */     {
/*     */       boolean isTerminal() {
/* 202 */         return false;
/*     */       }
/*     */     },
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 209 */     RUNNING
/*     */     {
/*     */       boolean isTerminal() {
/* 212 */         return false;
/*     */       }
/*     */     },
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 219 */     STOPPING
/*     */     {
/*     */       boolean isTerminal() {
/* 222 */         return false;
/*     */       }
/*     */     },
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 230 */     TERMINATED
/*     */     {
/*     */       boolean isTerminal() {
/* 233 */         return true;
/*     */       }
/*     */     },
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 241 */     FAILED
/*     */     {
/*     */       boolean isTerminal() {
/* 244 */         return true;
/*     */       }
/*     */     };
/*     */     
/*     */     abstract boolean isTerminal();
/*     */   }
/*     */   
/*     */   @Beta
/*     */   public static abstract class Listener {
/*     */     public void starting() {}
/*     */     
/*     */     public void running() {}
/*     */     
/*     */     public void stopping(Service.State from) {}
/*     */     
/*     */     public void terminated(Service.State from) {}
/*     */     
/*     */     public void failed(Service.State from, Throwable failure) {}
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\commo\\util\concurrent\Service.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */