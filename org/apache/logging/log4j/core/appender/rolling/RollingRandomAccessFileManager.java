/*     */ package org.apache.logging.log4j.core.appender.rolling;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.io.RandomAccessFile;
/*     */ import java.io.Serializable;
/*     */ import java.nio.ByteBuffer;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.apache.logging.log4j.core.Layout;
/*     */ import org.apache.logging.log4j.core.LoggerContext;
/*     */ import org.apache.logging.log4j.core.appender.AppenderLoggingException;
/*     */ import org.apache.logging.log4j.core.appender.ConfigurationFactoryData;
/*     */ import org.apache.logging.log4j.core.appender.ManagerFactory;
/*     */ import org.apache.logging.log4j.core.config.Configuration;
/*     */ import org.apache.logging.log4j.core.util.FileUtils;
/*     */ import org.apache.logging.log4j.core.util.NullOutputStream;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RollingRandomAccessFileManager
/*     */   extends RollingFileManager
/*     */ {
/*     */   public static final int DEFAULT_BUFFER_SIZE = 262144;
/*  45 */   private static final RollingRandomAccessFileManagerFactory FACTORY = new RollingRandomAccessFileManagerFactory();
/*     */   
/*     */   private RandomAccessFile randomAccessFile;
/*  48 */   private final ThreadLocal<Boolean> isEndOfBatch = new ThreadLocal<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RollingRandomAccessFileManager(LoggerContext loggerContext, RandomAccessFile raf, String fileName, String pattern, OutputStream os, boolean append, boolean immediateFlush, int bufferSize, long size, long time, TriggeringPolicy policy, RolloverStrategy strategy, String advertiseURI, Layout<? extends Serializable> layout, boolean writeHeader) {
/*  55 */     super(loggerContext, fileName, pattern, os, append, false, size, time, policy, strategy, advertiseURI, layout, writeHeader, ByteBuffer.wrap(new byte[bufferSize]));
/*     */     
/*  57 */     this.randomAccessFile = raf;
/*  58 */     this.isEndOfBatch.set(Boolean.FALSE);
/*  59 */     writeHeader();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void writeHeader() {
/*  66 */     if (this.layout == null) {
/*     */       return;
/*     */     }
/*  69 */     byte[] header = this.layout.getHeader();
/*  70 */     if (header == null) {
/*     */       return;
/*     */     }
/*     */     try {
/*  74 */       if (this.randomAccessFile.length() == 0L)
/*     */       {
/*  76 */         this.randomAccessFile.write(header, 0, header.length);
/*     */       }
/*  78 */     } catch (IOException e) {
/*  79 */       logError("Unable to write header", e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static RollingRandomAccessFileManager getRollingRandomAccessFileManager(String fileName, String filePattern, boolean isAppend, boolean immediateFlush, int bufferSize, TriggeringPolicy policy, RolloverStrategy strategy, String advertiseURI, Layout<? extends Serializable> layout, Configuration configuration) {
/*  87 */     return (RollingRandomAccessFileManager)getManager(fileName, new FactoryData(filePattern, isAppend, immediateFlush, bufferSize, policy, strategy, advertiseURI, layout, configuration), FACTORY);
/*     */   }
/*     */ 
/*     */   
/*     */   public Boolean isEndOfBatch() {
/*  92 */     return this.isEndOfBatch.get();
/*     */   }
/*     */   
/*     */   public void setEndOfBatch(boolean endOfBatch) {
/*  96 */     this.isEndOfBatch.set(Boolean.valueOf(endOfBatch));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected synchronized void write(byte[] bytes, int offset, int length, boolean immediateFlush) {
/* 103 */     super.write(bytes, offset, length, immediateFlush);
/*     */   }
/*     */ 
/*     */   
/*     */   protected synchronized void writeToDestination(byte[] bytes, int offset, int length) {
/*     */     try {
/* 109 */       this.randomAccessFile.write(bytes, offset, length);
/* 110 */       this.size += length;
/* 111 */     } catch (IOException ex) {
/* 112 */       String msg = "Error writing to RandomAccessFile " + getName();
/* 113 */       throw new AppenderLoggingException(msg, ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createFileAfterRollover() throws IOException {
/* 119 */     this.randomAccessFile = new RandomAccessFile(getFileName(), "rw");
/* 120 */     if (isAppend()) {
/* 121 */       this.randomAccessFile.seek(this.randomAccessFile.length());
/*     */     }
/* 123 */     writeHeader();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void flush() {
/* 128 */     flushBuffer(this.byteBuffer);
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized boolean closeOutputStream() {
/* 133 */     flush();
/*     */     try {
/* 135 */       this.randomAccessFile.close();
/* 136 */       return true;
/* 137 */     } catch (IOException e) {
/* 138 */       logError("Unable to close RandomAccessFile", e);
/* 139 */       return false;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getBufferSize() {
/* 150 */     return this.byteBuffer.capacity();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class RollingRandomAccessFileManagerFactory
/*     */     implements ManagerFactory<RollingRandomAccessFileManager, FactoryData>
/*     */   {
/*     */     private RollingRandomAccessFileManagerFactory() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public RollingRandomAccessFileManager createManager(String name, RollingRandomAccessFileManager.FactoryData data) {
/* 168 */       File file = new File(name);
/*     */       
/* 170 */       if (!data.append) {
/* 171 */         file.delete();
/*     */       }
/* 173 */       long size = data.append ? file.length() : 0L;
/* 174 */       long time = file.exists() ? file.lastModified() : System.currentTimeMillis();
/*     */       
/* 176 */       boolean writeHeader = (!data.append || !file.exists());
/* 177 */       RandomAccessFile raf = null;
/*     */       try {
/* 179 */         FileUtils.makeParentDirs(file);
/* 180 */         raf = new RandomAccessFile(name, "rw");
/* 181 */         if (data.append) {
/* 182 */           long length = raf.length();
/* 183 */           RollingRandomAccessFileManager.LOGGER.trace("RandomAccessFile {} seek to {}", name, Long.valueOf(length));
/* 184 */           raf.seek(length);
/*     */         } else {
/* 186 */           RollingRandomAccessFileManager.LOGGER.trace("RandomAccessFile {} set length to 0", name);
/* 187 */           raf.setLength(0L);
/*     */         } 
/* 189 */         return new RollingRandomAccessFileManager(data.getLoggerContext(), raf, name, data.pattern, (OutputStream)NullOutputStream.getInstance(), data.append, data.immediateFlush, data.bufferSize, size, time, data.policy, data.strategy, data.advertiseURI, data.layout, writeHeader);
/*     */       
/*     */       }
/* 192 */       catch (IOException ex) {
/* 193 */         RollingRandomAccessFileManager.LOGGER.error("Cannot access RandomAccessFile " + ex, ex);
/* 194 */         if (raf != null) {
/*     */           try {
/* 196 */             raf.close();
/* 197 */           } catch (IOException e) {
/* 198 */             RollingRandomAccessFileManager.LOGGER.error("Cannot close RandomAccessFile {}", name, e);
/*     */           } 
/*     */         }
/*     */         
/* 202 */         return null;
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class FactoryData
/*     */     extends ConfigurationFactoryData
/*     */   {
/*     */     private final String pattern;
/*     */ 
/*     */     
/*     */     private final boolean append;
/*     */ 
/*     */     
/*     */     private final boolean immediateFlush;
/*     */ 
/*     */     
/*     */     private final int bufferSize;
/*     */ 
/*     */     
/*     */     private final TriggeringPolicy policy;
/*     */ 
/*     */     
/*     */     private final RolloverStrategy strategy;
/*     */     
/*     */     private final String advertiseURI;
/*     */     
/*     */     private final Layout<? extends Serializable> layout;
/*     */ 
/*     */     
/*     */     public FactoryData(String pattern, boolean append, boolean immediateFlush, int bufferSize, TriggeringPolicy policy, RolloverStrategy strategy, String advertiseURI, Layout<? extends Serializable> layout, Configuration configuration) {
/* 235 */       super(configuration);
/* 236 */       this.pattern = pattern;
/* 237 */       this.append = append;
/* 238 */       this.immediateFlush = immediateFlush;
/* 239 */       this.bufferSize = bufferSize;
/* 240 */       this.policy = policy;
/* 241 */       this.strategy = strategy;
/* 242 */       this.advertiseURI = advertiseURI;
/* 243 */       this.layout = layout;
/*     */     }
/*     */ 
/*     */     
/*     */     public TriggeringPolicy getTriggeringPolicy() {
/* 248 */       return this.policy;
/*     */     }
/*     */ 
/*     */     
/*     */     public RolloverStrategy getRolloverStrategy() {
/* 253 */       return this.strategy;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateData(Object data) {
/* 259 */     FactoryData factoryData = (FactoryData)data;
/* 260 */     setRolloverStrategy(factoryData.getRolloverStrategy());
/* 261 */     setTriggeringPolicy(factoryData.getTriggeringPolicy());
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\appender\rolling\RollingRandomAccessFileManager.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */