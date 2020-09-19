/*     */ package org.apache.logging.log4j.core.appender;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.locks.Lock;
/*     */ import java.util.concurrent.locks.ReadWriteLock;
/*     */ import java.util.concurrent.locks.ReentrantReadWriteLock;
/*     */ import org.apache.logging.log4j.core.Filter;
/*     */ import org.apache.logging.log4j.core.Layout;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.StringLayout;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractWriterAppender<M extends WriterManager>
/*     */   extends AbstractAppender
/*     */ {
/*     */   protected final boolean immediateFlush;
/*     */   private final M manager;
/*  46 */   private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
/*  47 */   private final Lock readLock = this.readWriteLock.readLock();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected AbstractWriterAppender(String name, StringLayout layout, Filter filter, boolean ignoreExceptions, boolean immediateFlush, M manager) {
/*  61 */     super(name, filter, (Layout<? extends Serializable>)layout, ignoreExceptions);
/*  62 */     this.manager = manager;
/*  63 */     this.immediateFlush = immediateFlush;
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
/*     */   public void append(LogEvent event) {
/*  77 */     this.readLock.lock();
/*     */     try {
/*  79 */       String str = (String)getStringLayout().toSerializable(event);
/*  80 */       if (str.length() > 0) {
/*  81 */         this.manager.write(str);
/*  82 */         if (this.immediateFlush || event.isEndOfBatch()) {
/*  83 */           this.manager.flush();
/*     */         }
/*     */       } 
/*  86 */     } catch (AppenderLoggingException ex) {
/*  87 */       error("Unable to write " + this.manager.getName() + " for appender " + getName() + ": " + ex);
/*  88 */       throw ex;
/*     */     } finally {
/*  90 */       this.readLock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public M getManager() {
/* 100 */     return this.manager;
/*     */   }
/*     */   
/*     */   public StringLayout getStringLayout() {
/* 104 */     return (StringLayout)getLayout();
/*     */   }
/*     */ 
/*     */   
/*     */   public void start() {
/* 109 */     if (getLayout() == null) {
/* 110 */       LOGGER.error("No layout set for the appender named [{}].", getName());
/*     */     }
/* 112 */     if (this.manager == null) {
/* 113 */       LOGGER.error("No OutputStreamManager set for the appender named [{}].", getName());
/*     */     }
/* 115 */     super.start();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean stop(long timeout, TimeUnit timeUnit) {
/* 120 */     setStopping();
/* 121 */     boolean stopped = stop(timeout, timeUnit, false);
/* 122 */     stopped &= this.manager.stop(timeout, timeUnit);
/* 123 */     setStopped();
/* 124 */     return stopped;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\appender\AbstractWriterAppender.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */