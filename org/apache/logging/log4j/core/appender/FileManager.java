/*     */ package org.apache.logging.log4j.core.appender;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.channels.FileChannel;
/*     */ import java.nio.channels.FileLock;
/*     */ import java.util.Date;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.apache.logging.log4j.core.Layout;
/*     */ import org.apache.logging.log4j.core.LoggerContext;
/*     */ import org.apache.logging.log4j.core.config.Configuration;
/*     */ import org.apache.logging.log4j.core.util.Constants;
/*     */ import org.apache.logging.log4j.core.util.FileUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FileManager
/*     */   extends OutputStreamManager
/*     */ {
/*  44 */   private static final FileManagerFactory FACTORY = new FileManagerFactory();
/*     */   
/*     */   private final boolean isAppend;
/*     */   
/*     */   private final boolean createOnDemand;
/*     */   
/*     */   private final boolean isLocking;
/*     */   
/*     */   private final String advertiseURI;
/*     */   
/*     */   private final int bufferSize;
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   protected FileManager(String fileName, OutputStream os, boolean append, boolean locking, String advertiseURI, Layout<? extends Serializable> layout, int bufferSize, boolean writeHeader) {
/*  59 */     this(fileName, os, append, locking, advertiseURI, layout, writeHeader, ByteBuffer.wrap(new byte[bufferSize]));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   protected FileManager(String fileName, OutputStream os, boolean append, boolean locking, String advertiseURI, Layout<? extends Serializable> layout, boolean writeHeader, ByteBuffer buffer) {
/*  70 */     super(os, fileName, layout, writeHeader, buffer);
/*  71 */     this.isAppend = append;
/*  72 */     this.createOnDemand = false;
/*  73 */     this.isLocking = locking;
/*  74 */     this.advertiseURI = advertiseURI;
/*  75 */     this.bufferSize = buffer.capacity();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected FileManager(LoggerContext loggerContext, String fileName, OutputStream os, boolean append, boolean locking, boolean createOnDemand, String advertiseURI, Layout<? extends Serializable> layout, boolean writeHeader, ByteBuffer buffer) {
/*  84 */     super(loggerContext, os, fileName, createOnDemand, layout, writeHeader, buffer);
/*  85 */     this.isAppend = append;
/*  86 */     this.createOnDemand = createOnDemand;
/*  87 */     this.isLocking = locking;
/*  88 */     this.advertiseURI = advertiseURI;
/*  89 */     this.bufferSize = buffer.capacity();
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
/*     */   public static FileManager getFileManager(String fileName, boolean append, boolean locking, boolean bufferedIo, boolean createOnDemand, String advertiseUri, Layout<? extends Serializable> layout, int bufferSize, Configuration configuration) {
/* 109 */     if (locking && bufferedIo) {
/* 110 */       locking = false;
/*     */     }
/* 112 */     return (FileManager)getManager(fileName, new FactoryData(append, locking, bufferedIo, bufferSize, createOnDemand, advertiseUri, layout, configuration), FACTORY);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected OutputStream createOutputStream() throws FileNotFoundException {
/* 118 */     String filename = getFileName();
/* 119 */     LOGGER.debug("Now writing to {} at {}", filename, new Date());
/* 120 */     return new FileOutputStream(filename, this.isAppend);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected synchronized void write(byte[] bytes, int offset, int length, boolean immediateFlush) {
/* 126 */     if (this.isLocking) {
/*     */       
/*     */       try {
/* 129 */         FileChannel channel = ((FileOutputStream)getOutputStream()).getChannel();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 138 */         try (FileLock lock = channel.lock(0L, Long.MAX_VALUE, false)) {
/* 139 */           super.write(bytes, offset, length, immediateFlush);
/*     */         } 
/* 141 */       } catch (IOException ex) {
/* 142 */         throw new AppenderLoggingException("Unable to obtain lock on " + getName(), ex);
/*     */       } 
/*     */     } else {
/* 145 */       super.write(bytes, offset, length, immediateFlush);
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
/* 158 */     if (this.isLocking) {
/*     */       
/*     */       try {
/* 161 */         FileChannel channel = ((FileOutputStream)getOutputStream()).getChannel();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 170 */         try (FileLock lock = channel.lock(0L, Long.MAX_VALUE, false)) {
/* 171 */           super.writeToDestination(bytes, offset, length);
/*     */         } 
/* 173 */       } catch (IOException ex) {
/* 174 */         throw new AppenderLoggingException("Unable to obtain lock on " + getName(), ex);
/*     */       } 
/*     */     } else {
/* 177 */       super.writeToDestination(bytes, offset, length);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFileName() {
/* 186 */     return getName();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isAppend() {
/* 194 */     return this.isAppend;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isCreateOnDemand() {
/* 202 */     return this.createOnDemand;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isLocking() {
/* 210 */     return this.isLocking;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getBufferSize() {
/* 219 */     return this.bufferSize;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<String, String> getContentFormat() {
/* 229 */     Map<String, String> result = new HashMap<>(super.getContentFormat());
/* 230 */     result.put("fileURI", this.advertiseURI);
/* 231 */     return result;
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
/*     */     private final boolean locking;
/*     */ 
/*     */     
/*     */     private final boolean bufferedIo;
/*     */ 
/*     */     
/*     */     private final int bufferSize;
/*     */ 
/*     */     
/*     */     private final boolean createOnDemand;
/*     */ 
/*     */     
/*     */     private final String advertiseURI;
/*     */     
/*     */     private final Layout<? extends Serializable> layout;
/*     */ 
/*     */     
/*     */     public FactoryData(boolean append, boolean locking, boolean bufferedIo, int bufferSize, boolean createOnDemand, String advertiseURI, Layout<? extends Serializable> layout, Configuration configuration) {
/* 260 */       super(configuration);
/* 261 */       this.append = append;
/* 262 */       this.locking = locking;
/* 263 */       this.bufferedIo = bufferedIo;
/* 264 */       this.bufferSize = bufferSize;
/* 265 */       this.createOnDemand = createOnDemand;
/* 266 */       this.advertiseURI = advertiseURI;
/* 267 */       this.layout = layout;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class FileManagerFactory
/*     */     implements ManagerFactory<FileManager, FactoryData>
/*     */   {
/*     */     private FileManagerFactory() {}
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public FileManager createManager(String name, FileManager.FactoryData data) {
/* 284 */       File file = new File(name);
/*     */       try {
/* 286 */         FileUtils.makeParentDirs(file);
/* 287 */         boolean writeHeader = (!data.append || !file.exists());
/* 288 */         int actualSize = data.bufferedIo ? data.bufferSize : Constants.ENCODER_BYTE_BUFFER_SIZE;
/* 289 */         ByteBuffer byteBuffer = ByteBuffer.wrap(new byte[actualSize]);
/* 290 */         FileOutputStream fos = data.createOnDemand ? null : new FileOutputStream(file, data.append);
/* 291 */         return new FileManager(data.getLoggerContext(), name, fos, data.append, data.locking, data.createOnDemand, data.advertiseURI, data.layout, writeHeader, byteBuffer);
/*     */       }
/* 293 */       catch (IOException ex) {
/* 294 */         AbstractManager.LOGGER.error("FileManager (" + name + ") " + ex, ex);
/*     */         
/* 296 */         return null;
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\appender\FileManager.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */