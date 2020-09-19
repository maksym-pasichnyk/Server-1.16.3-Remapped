/*     */ package org.apache.logging.log4j.core.appender;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.io.RandomAccessFile;
/*     */ import java.io.Serializable;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.apache.logging.log4j.core.Layout;
/*     */ import org.apache.logging.log4j.core.LoggerContext;
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
/*     */ public class RandomAccessFileManager
/*     */   extends OutputStreamManager
/*     */ {
/*     */   static final int DEFAULT_BUFFER_SIZE = 262144;
/*  42 */   private static final RandomAccessFileManagerFactory FACTORY = new RandomAccessFileManagerFactory();
/*     */   
/*     */   private final String advertiseURI;
/*     */   private final RandomAccessFile randomAccessFile;
/*  46 */   private final ThreadLocal<Boolean> isEndOfBatch = new ThreadLocal<>();
/*     */ 
/*     */ 
/*     */   
/*     */   protected RandomAccessFileManager(LoggerContext loggerContext, RandomAccessFile file, String fileName, OutputStream os, int bufferSize, String advertiseURI, Layout<? extends Serializable> layout, boolean writeHeader) {
/*  51 */     super(loggerContext, os, fileName, false, layout, writeHeader, ByteBuffer.wrap(new byte[bufferSize]));
/*  52 */     this.randomAccessFile = file;
/*  53 */     this.advertiseURI = advertiseURI;
/*  54 */     this.isEndOfBatch.set(Boolean.FALSE);
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
/*     */   public static RandomAccessFileManager getFileManager(String fileName, boolean append, boolean isFlush, int bufferSize, String advertiseURI, Layout<? extends Serializable> layout, Configuration configuration) {
/*  74 */     return (RandomAccessFileManager)getManager(fileName, new FactoryData(append, isFlush, bufferSize, advertiseURI, layout, configuration), FACTORY);
/*     */   }
/*     */ 
/*     */   
/*     */   public Boolean isEndOfBatch() {
/*  79 */     return this.isEndOfBatch.get();
/*     */   }
/*     */   
/*     */   public void setEndOfBatch(boolean endOfBatch) {
/*  83 */     this.isEndOfBatch.set(Boolean.valueOf(endOfBatch));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void writeToDestination(byte[] bytes, int offset, int length) {
/*     */     try {
/*  89 */       this.randomAccessFile.write(bytes, offset, length);
/*  90 */     } catch (IOException ex) {
/*  91 */       String msg = "Error writing to RandomAccessFile " + getName();
/*  92 */       throw new AppenderLoggingException(msg, ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void flush() {
/*  98 */     flushBuffer(this.byteBuffer);
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized boolean closeOutputStream() {
/* 103 */     flush();
/*     */     try {
/* 105 */       this.randomAccessFile.close();
/* 106 */       return true;
/* 107 */     } catch (IOException ex) {
/* 108 */       logError("Unable to close RandomAccessFile", ex);
/* 109 */       return false;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFileName() {
/* 119 */     return getName();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getBufferSize() {
/* 127 */     return this.byteBuffer.capacity();
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
/*     */   public Map<String, String> getContentFormat() {
/* 140 */     Map<String, String> result = new HashMap<>(super.getContentFormat());
/*     */     
/* 142 */     result.put("fileURI", this.advertiseURI);
/* 143 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class FactoryData
/*     */     extends ConfigurationFactoryData
/*     */   {
/*     */     private final boolean append;
/*     */ 
/*     */     
/*     */     private final boolean immediateFlush;
/*     */ 
/*     */     
/*     */     private final int bufferSize;
/*     */     
/*     */     private final String advertiseURI;
/*     */     
/*     */     private final Layout<? extends Serializable> layout;
/*     */ 
/*     */     
/*     */     public FactoryData(boolean append, boolean immediateFlush, int bufferSize, String advertiseURI, Layout<? extends Serializable> layout, Configuration configuration) {
/* 165 */       super(configuration);
/* 166 */       this.append = append;
/* 167 */       this.immediateFlush = immediateFlush;
/* 168 */       this.bufferSize = bufferSize;
/* 169 */       this.advertiseURI = advertiseURI;
/* 170 */       this.layout = layout;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class RandomAccessFileManagerFactory
/*     */     implements ManagerFactory<RandomAccessFileManager, FactoryData>
/*     */   {
/*     */     private RandomAccessFileManagerFactory() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public RandomAccessFileManager createManager(String name, RandomAccessFileManager.FactoryData data) {
/* 189 */       File file = new File(name);
/* 190 */       if (!data.append) {
/* 191 */         file.delete();
/*     */       }
/*     */       
/* 194 */       boolean writeHeader = (!data.append || !file.exists());
/* 195 */       NullOutputStream nullOutputStream = NullOutputStream.getInstance();
/*     */       
/*     */       try {
/* 198 */         FileUtils.makeParentDirs(file);
/* 199 */         RandomAccessFile raf = new RandomAccessFile(name, "rw");
/* 200 */         if (data.append) {
/* 201 */           raf.seek(raf.length());
/*     */         } else {
/* 203 */           raf.setLength(0L);
/*     */         } 
/* 205 */         return new RandomAccessFileManager(data.getLoggerContext(), raf, name, (OutputStream)nullOutputStream, data.bufferSize, data.advertiseURI, data.layout, writeHeader);
/*     */       }
/* 207 */       catch (Exception ex) {
/* 208 */         AbstractManager.LOGGER.error("RandomAccessFileManager (" + name + ") " + ex, ex);
/*     */         
/* 210 */         return null;
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\appender\RandomAccessFileManager.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */