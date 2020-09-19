/*     */ package org.apache.logging.log4j.core.appender;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import org.apache.logging.log4j.core.Filter;
/*     */ import org.apache.logging.log4j.core.Layout;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginBuilderAttribute;
/*     */ import org.apache.logging.log4j.core.layout.ByteBufferDestination;
/*     */ import org.apache.logging.log4j.core.util.Constants;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractOutputStreamAppender<M extends OutputStreamManager>
/*     */   extends AbstractAppender
/*     */ {
/*     */   private final boolean immediateFlush;
/*     */   private final M manager;
/*     */   
/*     */   public static abstract class Builder<B extends Builder<B>>
/*     */     extends AbstractAppender.Builder<B>
/*     */   {
/*     */     @PluginBuilderAttribute
/*     */     private boolean bufferedIo = true;
/*     */     @PluginBuilderAttribute
/*  45 */     private int bufferSize = Constants.ENCODER_BYTE_BUFFER_SIZE;
/*     */     
/*     */     @PluginBuilderAttribute
/*     */     private boolean immediateFlush = true;
/*     */ 
/*     */     
/*     */     public int getBufferSize() {
/*  52 */       return this.bufferSize;
/*     */     }
/*     */     
/*     */     public boolean isBufferedIo() {
/*  56 */       return this.bufferedIo;
/*     */     }
/*     */     
/*     */     public boolean isImmediateFlush() {
/*  60 */       return this.immediateFlush;
/*     */     }
/*     */     
/*     */     public B withImmediateFlush(boolean immediateFlush) {
/*  64 */       this.immediateFlush = immediateFlush;
/*  65 */       return (B)asBuilder();
/*     */     }
/*     */     
/*     */     public B withBufferedIo(boolean bufferedIo) {
/*  69 */       this.bufferedIo = bufferedIo;
/*  70 */       return (B)asBuilder();
/*     */     }
/*     */     
/*     */     public B withBufferSize(int bufferSize) {
/*  74 */       this.bufferSize = bufferSize;
/*  75 */       return (B)asBuilder();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected AbstractOutputStreamAppender(String name, Layout<? extends Serializable> layout, Filter filter, boolean ignoreExceptions, boolean immediateFlush, M manager) {
/* 100 */     super(name, filter, layout, ignoreExceptions);
/* 101 */     this.manager = manager;
/* 102 */     this.immediateFlush = immediateFlush;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getImmediateFlush() {
/* 111 */     return this.immediateFlush;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public M getManager() {
/* 120 */     return this.manager;
/*     */   }
/*     */ 
/*     */   
/*     */   public void start() {
/* 125 */     if (getLayout() == null) {
/* 126 */       LOGGER.error("No layout set for the appender named [" + getName() + "].");
/*     */     }
/* 128 */     if (this.manager == null) {
/* 129 */       LOGGER.error("No OutputStreamManager set for the appender named [" + getName() + "].");
/*     */     }
/* 131 */     super.start();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean stop(long timeout, TimeUnit timeUnit) {
/* 136 */     return stop(timeout, timeUnit, true);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean stop(long timeout, TimeUnit timeUnit, boolean changeLifeCycleState) {
/* 141 */     boolean stopped = super.stop(timeout, timeUnit, changeLifeCycleState);
/* 142 */     stopped &= this.manager.stop(timeout, timeUnit);
/* 143 */     if (changeLifeCycleState) {
/* 144 */       setStopped();
/*     */     }
/* 146 */     LOGGER.debug("Appender {} stopped with status {}", getName(), Boolean.valueOf(stopped));
/* 147 */     return stopped;
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
/*     */   public void append(LogEvent event) {
/*     */     try {
/* 161 */       tryAppend(event);
/* 162 */     } catch (AppenderLoggingException ex) {
/* 163 */       error("Unable to write to stream " + this.manager.getName() + " for appender " + getName() + ": " + ex);
/* 164 */       throw ex;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void tryAppend(LogEvent event) {
/* 169 */     if (Constants.ENABLE_DIRECT_ENCODERS) {
/* 170 */       directEncodeEvent(event);
/*     */     } else {
/* 172 */       writeByteArrayToManager(event);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void directEncodeEvent(LogEvent event) {
/* 177 */     getLayout().encode(event, (ByteBufferDestination)this.manager);
/* 178 */     if (this.immediateFlush || event.isEndOfBatch()) {
/* 179 */       this.manager.flush();
/*     */     }
/*     */   }
/*     */   
/*     */   protected void writeByteArrayToManager(LogEvent event) {
/* 184 */     byte[] bytes = getLayout().toByteArray(event);
/* 185 */     if (bytes != null && bytes.length > 0)
/* 186 */       this.manager.write(bytes, (this.immediateFlush || event.isEndOfBatch())); 
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\appender\AbstractOutputStreamAppender.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */