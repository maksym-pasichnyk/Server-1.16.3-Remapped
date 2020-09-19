/*     */ package org.apache.logging.log4j.core.appender.db;
/*     */ 
/*     */ import java.io.Flushable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.appender.AbstractManager;
/*     */ import org.apache.logging.log4j.core.appender.ManagerFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractDatabaseManager
/*     */   extends AbstractManager
/*     */   implements Flushable
/*     */ {
/*     */   private final ArrayList<LogEvent> buffer;
/*     */   private final int bufferSize;
/*     */   private boolean running = false;
/*     */   
/*     */   protected AbstractDatabaseManager(String name, int bufferSize) {
/*  45 */     super(null, name);
/*  46 */     this.bufferSize = bufferSize;
/*  47 */     this.buffer = new ArrayList<>(bufferSize + 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract void startupInternal() throws Exception;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final synchronized void startup() {
/*  63 */     if (!isRunning()) {
/*     */       try {
/*  65 */         startupInternal();
/*  66 */         this.running = true;
/*  67 */       } catch (Exception e) {
/*  68 */         logError("Could not perform database startup operations", e);
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
/*     */   protected abstract boolean shutdownInternal() throws Exception;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final synchronized boolean shutdown() {
/*  89 */     boolean closed = true;
/*  90 */     flush();
/*  91 */     if (isRunning()) {
/*     */       try {
/*  93 */         closed &= shutdownInternal();
/*  94 */       } catch (Exception e) {
/*  95 */         logWarn("Caught exception while performing database shutdown operations", e);
/*  96 */         closed = false;
/*     */       } finally {
/*  98 */         this.running = false;
/*     */       } 
/*     */     }
/* 101 */     return closed;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean isRunning() {
/* 111 */     return this.running;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract void connectAndStart();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract void writeInternal(LogEvent paramLogEvent);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract boolean commitAndClose();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final synchronized void flush() {
/* 144 */     if (isRunning() && this.buffer.size() > 0) {
/* 145 */       connectAndStart();
/*     */       try {
/* 147 */         for (LogEvent event : this.buffer) {
/* 148 */           writeInternal(event);
/*     */         }
/*     */       } finally {
/* 151 */         commitAndClose();
/*     */         
/* 153 */         this.buffer.clear();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final synchronized void write(LogEvent event) {
/* 164 */     if (this.bufferSize > 0) {
/* 165 */       this.buffer.add(event);
/* 166 */       if (this.buffer.size() >= this.bufferSize || event.isEndOfBatch()) {
/* 167 */         flush();
/*     */       }
/*     */     } else {
/* 170 */       connectAndStart();
/*     */       try {
/* 172 */         writeInternal(event);
/*     */       } finally {
/* 174 */         commitAndClose();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean releaseSub(long timeout, TimeUnit timeUnit) {
/* 181 */     return shutdown();
/*     */   }
/*     */ 
/*     */   
/*     */   public final String toString() {
/* 186 */     return getName();
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
/*     */   protected static <M extends AbstractDatabaseManager, T extends AbstractFactoryData> M getManager(String name, T data, ManagerFactory<M, T> factory) {
/* 204 */     return (M)AbstractManager.getManager(name, factory, data);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static abstract class AbstractFactoryData
/*     */   {
/*     */     private final int bufferSize;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected AbstractFactoryData(int bufferSize) {
/* 220 */       this.bufferSize = bufferSize;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int getBufferSize() {
/* 229 */       return this.bufferSize;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\appender\db\AbstractDatabaseManager.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */