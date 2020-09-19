/*     */ package org.apache.logging.log4j.core.appender;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.Objects;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import org.apache.logging.log4j.core.Layout;
/*     */ import org.apache.logging.log4j.core.LoggerContext;
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
/*     */ public class OutputStreamManager
/*     */   extends AbstractManager
/*     */   implements ByteBufferDestination
/*     */ {
/*     */   protected final Layout<?> layout;
/*     */   protected ByteBuffer byteBuffer;
/*     */   private volatile OutputStream os;
/*     */   private boolean skipFooter;
/*     */   
/*     */   protected OutputStreamManager(OutputStream os, String streamName, Layout<?> layout, boolean writeHeader) {
/*  44 */     this(os, streamName, layout, writeHeader, Constants.ENCODER_BYTE_BUFFER_SIZE);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected OutputStreamManager(OutputStream os, String streamName, Layout<?> layout, boolean writeHeader, int bufferSize) {
/*  50 */     this(os, streamName, layout, writeHeader, ByteBuffer.wrap(new byte[bufferSize]));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   protected OutputStreamManager(OutputStream os, String streamName, Layout<?> layout, boolean writeHeader, ByteBuffer byteBuffer) {
/*  60 */     super(null, streamName);
/*  61 */     this.os = os;
/*  62 */     this.layout = layout;
/*  63 */     if (writeHeader && layout != null) {
/*  64 */       byte[] header = layout.getHeader();
/*  65 */       if (header != null) {
/*     */         try {
/*  67 */           getOutputStream().write(header, 0, header.length);
/*  68 */         } catch (IOException e) {
/*  69 */           logError("Unable to write header", e);
/*     */         } 
/*     */       }
/*     */     } 
/*  73 */     this.byteBuffer = Objects.<ByteBuffer>requireNonNull(byteBuffer, "byteBuffer");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected OutputStreamManager(LoggerContext loggerContext, OutputStream os, String streamName, boolean createOnDemand, Layout<? extends Serializable> layout, boolean writeHeader, ByteBuffer byteBuffer) {
/*  82 */     super(loggerContext, streamName);
/*  83 */     if (createOnDemand && os != null) {
/*  84 */       LOGGER.error("Invalid OutputStreamManager configuration for '{}': You cannot both set the OutputStream and request on-demand.", streamName);
/*     */     }
/*     */ 
/*     */     
/*  88 */     this.layout = layout;
/*  89 */     this.byteBuffer = Objects.<ByteBuffer>requireNonNull(byteBuffer, "byteBuffer");
/*  90 */     this.os = os;
/*  91 */     if (writeHeader && layout != null) {
/*  92 */       byte[] header = layout.getHeader();
/*  93 */       if (header != null) {
/*     */         try {
/*  95 */           getOutputStream().write(header, 0, header.length);
/*  96 */         } catch (IOException e) {
/*  97 */           logError("Unable to write header for " + streamName, e);
/*     */         } 
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
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> OutputStreamManager getManager(String name, T data, ManagerFactory<? extends OutputStreamManager, T> factory) {
/* 114 */     return AbstractManager.<OutputStreamManager, T>getManager(name, (ManagerFactory)factory, data);
/*     */   }
/*     */ 
/*     */   
/*     */   protected OutputStream createOutputStream() throws IOException {
/* 119 */     throw new IllegalStateException(getClass().getCanonicalName() + " must implement createOutputStream()");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void skipFooter(boolean skipFooter) {
/* 127 */     this.skipFooter = skipFooter;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean releaseSub(long timeout, TimeUnit timeUnit) {
/* 135 */     writeFooter();
/* 136 */     return closeOutputStream();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void writeFooter() {
/* 143 */     if (this.layout == null || this.skipFooter) {
/*     */       return;
/*     */     }
/* 146 */     byte[] footer = this.layout.getFooter();
/* 147 */     if (footer != null) {
/* 148 */       write(footer);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isOpen() {
/* 157 */     return (getCount() > 0);
/*     */   }
/*     */   
/*     */   public boolean hasOutputStream() {
/* 161 */     return (this.os != null);
/*     */   }
/*     */   
/*     */   protected OutputStream getOutputStream() throws IOException {
/* 165 */     if (this.os == null) {
/* 166 */       this.os = createOutputStream();
/*     */     }
/* 168 */     return this.os;
/*     */   }
/*     */   
/*     */   protected void setOutputStream(OutputStream os) {
/* 172 */     byte[] header = this.layout.getHeader();
/* 173 */     if (header != null) {
/*     */       try {
/* 175 */         os.write(header, 0, header.length);
/* 176 */         this.os = os;
/* 177 */       } catch (IOException ioe) {
/* 178 */         logError("Unable to write header", ioe);
/*     */       } 
/*     */     } else {
/* 181 */       this.os = os;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void write(byte[] bytes) {
/* 191 */     write(bytes, 0, bytes.length, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void write(byte[] bytes, boolean immediateFlush) {
/* 201 */     write(bytes, 0, bytes.length, immediateFlush);
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
/*     */   protected void write(byte[] bytes, int offset, int length) {
/* 213 */     write(bytes, offset, length, false);
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
/*     */   protected synchronized void write(byte[] bytes, int offset, int length, boolean immediateFlush) {
/* 226 */     if (immediateFlush && this.byteBuffer.position() == 0) {
/* 227 */       writeToDestination(bytes, offset, length);
/* 228 */       flushDestination();
/*     */       return;
/*     */     } 
/* 231 */     if (length >= this.byteBuffer.capacity()) {
/*     */       
/* 233 */       flush();
/* 234 */       writeToDestination(bytes, offset, length);
/*     */     } else {
/* 236 */       if (length > this.byteBuffer.remaining()) {
/* 237 */         flush();
/*     */       }
/* 239 */       this.byteBuffer.put(bytes, offset, length);
/*     */     } 
/* 241 */     if (immediateFlush) {
/* 242 */       flush();
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
/*     */   protected synchronized void writeToDestination(byte[] bytes, int offset, int length) {
/*     */     try {
/* 256 */       getOutputStream().write(bytes, offset, length);
/* 257 */     } catch (IOException ex) {
/* 258 */       throw new AppenderLoggingException("Error writing to stream " + getName(), ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected synchronized void flushDestination() {
/* 267 */     OutputStream stream = this.os;
/* 268 */     if (stream != null) {
/*     */       try {
/* 270 */         stream.flush();
/* 271 */       } catch (IOException ex) {
/* 272 */         throw new AppenderLoggingException("Error flushing stream " + getName(), ex);
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
/*     */   
/*     */   protected synchronized void flushBuffer(ByteBuffer buf) {
/* 286 */     buf.flip();
/* 287 */     if (buf.limit() > 0) {
/* 288 */       writeToDestination(buf.array(), 0, buf.limit());
/*     */     }
/* 290 */     buf.clear();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void flush() {
/* 297 */     flushBuffer(this.byteBuffer);
/* 298 */     flushDestination();
/*     */   }
/*     */   
/*     */   protected synchronized boolean closeOutputStream() {
/* 302 */     flush();
/* 303 */     OutputStream stream = this.os;
/* 304 */     if (stream == null || stream == System.out || stream == System.err) {
/* 305 */       return true;
/*     */     }
/*     */     try {
/* 308 */       stream.close();
/* 309 */     } catch (IOException ex) {
/* 310 */       logError("Unable to close stream", ex);
/* 311 */       return false;
/*     */     } 
/* 313 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ByteBuffer getByteBuffer() {
/* 323 */     return this.byteBuffer;
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
/*     */   public ByteBuffer drain(ByteBuffer buf) {
/* 345 */     flushBuffer(buf);
/* 346 */     return buf;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\appender\OutputStreamManager.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */