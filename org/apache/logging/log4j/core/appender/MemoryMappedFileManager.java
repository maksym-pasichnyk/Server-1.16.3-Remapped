/*     */ package org.apache.logging.log4j.core.appender;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.io.RandomAccessFile;
/*     */ import java.io.Serializable;
/*     */ import java.lang.reflect.Method;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.ByteOrder;
/*     */ import java.nio.MappedByteBuffer;
/*     */ import java.nio.channels.FileChannel;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedActionException;
/*     */ import java.security.PrivilegedExceptionAction;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import org.apache.logging.log4j.core.Layout;
/*     */ import org.apache.logging.log4j.core.util.Closer;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MemoryMappedFileManager
/*     */   extends OutputStreamManager
/*     */ {
/*     */   static final int DEFAULT_REGION_LENGTH = 33554432;
/*     */   private static final int MAX_REMAP_COUNT = 10;
/*  65 */   private static final MemoryMappedFileManagerFactory FACTORY = new MemoryMappedFileManagerFactory();
/*     */   
/*     */   private static final double NANOS_PER_MILLISEC = 1000000.0D;
/*     */   private final boolean immediateFlush;
/*     */   private final int regionLength;
/*     */   private final String advertiseURI;
/*     */   private final RandomAccessFile randomAccessFile;
/*  72 */   private final ThreadLocal<Boolean> isEndOfBatch = new ThreadLocal<>();
/*     */   
/*     */   private MappedByteBuffer mappedBuffer;
/*     */   
/*     */   private long mappingOffset;
/*     */   
/*     */   protected MemoryMappedFileManager(RandomAccessFile file, String fileName, OutputStream os, boolean immediateFlush, long position, int regionLength, String advertiseURI, Layout<? extends Serializable> layout, boolean writeHeader) throws IOException {
/*  79 */     super(os, fileName, layout, writeHeader, ByteBuffer.wrap(new byte[0]));
/*  80 */     this.immediateFlush = immediateFlush;
/*  81 */     this.randomAccessFile = Objects.<RandomAccessFile>requireNonNull(file, "RandomAccessFile");
/*  82 */     this.regionLength = regionLength;
/*  83 */     this.advertiseURI = advertiseURI;
/*  84 */     this.isEndOfBatch.set(Boolean.FALSE);
/*  85 */     this.mappedBuffer = mmap(this.randomAccessFile.getChannel(), getFileName(), position, regionLength);
/*  86 */     this.byteBuffer = this.mappedBuffer;
/*  87 */     this.mappingOffset = position;
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
/*     */   public static MemoryMappedFileManager getFileManager(String fileName, boolean append, boolean immediateFlush, int regionLength, String advertiseURI, Layout<? extends Serializable> layout) {
/* 104 */     return (MemoryMappedFileManager)getManager(fileName, new FactoryData(append, immediateFlush, regionLength, advertiseURI, layout), FACTORY);
/*     */   }
/*     */ 
/*     */   
/*     */   public Boolean isEndOfBatch() {
/* 109 */     return this.isEndOfBatch.get();
/*     */   }
/*     */   
/*     */   public void setEndOfBatch(boolean endOfBatch) {
/* 113 */     this.isEndOfBatch.set(Boolean.valueOf(endOfBatch));
/*     */   }
/*     */ 
/*     */   
/*     */   protected synchronized void write(byte[] bytes, int offset, int length, boolean immediateFlush) {
/* 118 */     while (length > this.mappedBuffer.remaining()) {
/* 119 */       int chunk = this.mappedBuffer.remaining();
/* 120 */       this.mappedBuffer.put(bytes, offset, chunk);
/* 121 */       offset += chunk;
/* 122 */       length -= chunk;
/* 123 */       remap();
/*     */     } 
/* 125 */     this.mappedBuffer.put(bytes, offset, length);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private synchronized void remap() {
/* 132 */     long offset = this.mappingOffset + this.mappedBuffer.position();
/* 133 */     int length = this.mappedBuffer.remaining() + this.regionLength;
/*     */     try {
/* 135 */       unsafeUnmap(this.mappedBuffer);
/* 136 */       long fileLength = this.randomAccessFile.length() + this.regionLength;
/* 137 */       LOGGER.debug("{} {} extending {} by {} bytes to {}", getClass().getSimpleName(), getName(), getFileName(), Integer.valueOf(this.regionLength), Long.valueOf(fileLength));
/*     */ 
/*     */       
/* 140 */       long startNanos = System.nanoTime();
/* 141 */       this.randomAccessFile.setLength(fileLength);
/* 142 */       float millis = (float)((System.nanoTime() - startNanos) / 1000000.0D);
/* 143 */       LOGGER.debug("{} {} extended {} OK in {} millis", getClass().getSimpleName(), getName(), getFileName(), Float.valueOf(millis));
/*     */ 
/*     */       
/* 146 */       this.mappedBuffer = mmap(this.randomAccessFile.getChannel(), getFileName(), offset, length);
/* 147 */       this.byteBuffer = this.mappedBuffer;
/* 148 */       this.mappingOffset = offset;
/* 149 */     } catch (Exception ex) {
/* 150 */       logError("Unable to remap", ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void flush() {
/* 156 */     this.mappedBuffer.force();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized boolean closeOutputStream() {
/* 161 */     long position = this.mappedBuffer.position();
/* 162 */     long length = this.mappingOffset + position;
/*     */     try {
/* 164 */       unsafeUnmap(this.mappedBuffer);
/* 165 */     } catch (Exception ex) {
/* 166 */       logError("Unable to unmap MappedBuffer", ex);
/*     */     } 
/*     */     try {
/* 169 */       LOGGER.debug("MMapAppender closing. Setting {} length to {} (offset {} + position {})", getFileName(), Long.valueOf(length), Long.valueOf(this.mappingOffset), Long.valueOf(position));
/*     */       
/* 171 */       this.randomAccessFile.setLength(length);
/* 172 */       this.randomAccessFile.close();
/* 173 */       return true;
/* 174 */     } catch (IOException ex) {
/* 175 */       logError("Unable to close MemoryMappedFile", ex);
/* 176 */       return false;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static MappedByteBuffer mmap(FileChannel fileChannel, String fileName, long start, int size) throws IOException {
/* 182 */     for (int i = 1;; i++) {
/*     */       try {
/* 184 */         LOGGER.debug("MMapAppender remapping {} start={}, size={}", fileName, Long.valueOf(start), Integer.valueOf(size));
/*     */         
/* 186 */         long startNanos = System.nanoTime();
/* 187 */         MappedByteBuffer map = fileChannel.map(FileChannel.MapMode.READ_WRITE, start, size);
/* 188 */         map.order(ByteOrder.nativeOrder());
/*     */         
/* 190 */         float millis = (float)((System.nanoTime() - startNanos) / 1000000.0D);
/* 191 */         LOGGER.debug("MMapAppender remapped {} OK in {} millis", fileName, Float.valueOf(millis));
/*     */         
/* 193 */         return map;
/* 194 */       } catch (IOException e) {
/* 195 */         if (e.getMessage() == null || !e.getMessage().endsWith("user-mapped section open")) {
/* 196 */           throw e;
/*     */         }
/* 198 */         LOGGER.debug("Remap attempt {}/{} failed. Retrying...", Integer.valueOf(i), Integer.valueOf(10), e);
/* 199 */         if (i < 10) {
/* 200 */           Thread.yield();
/*     */         } else {
/*     */           try {
/* 203 */             Thread.sleep(1L);
/* 204 */           } catch (InterruptedException ignored) {
/* 205 */             Thread.currentThread().interrupt();
/* 206 */             throw e;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void unsafeUnmap(final MappedByteBuffer mbb) throws PrivilegedActionException {
/* 214 */     LOGGER.debug("MMapAppender unmapping old buffer...");
/* 215 */     long startNanos = System.nanoTime();
/* 216 */     AccessController.doPrivileged(new PrivilegedExceptionAction()
/*     */         {
/*     */           public Object run() throws Exception {
/* 219 */             Method getCleanerMethod = mbb.getClass().getMethod("cleaner", new Class[0]);
/* 220 */             getCleanerMethod.setAccessible(true);
/* 221 */             Object cleaner = getCleanerMethod.invoke(mbb, new Object[0]);
/* 222 */             Method cleanMethod = cleaner.getClass().getMethod("clean", new Class[0]);
/* 223 */             cleanMethod.invoke(cleaner, new Object[0]);
/* 224 */             return null;
/*     */           }
/*     */         });
/* 227 */     float millis = (float)((System.nanoTime() - startNanos) / 1000000.0D);
/* 228 */     LOGGER.debug("MMapAppender unmapped buffer OK in {} millis", Float.valueOf(millis));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFileName() {
/* 237 */     return getName();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getRegionLength() {
/* 246 */     return this.regionLength;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isImmediateFlush() {
/* 256 */     return this.immediateFlush;
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
/* 269 */     Map<String, String> result = new HashMap<>(super.getContentFormat());
/* 270 */     result.put("fileURI", this.advertiseURI);
/* 271 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void flushBuffer(ByteBuffer buffer) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public ByteBuffer getByteBuffer() {
/* 281 */     return this.mappedBuffer;
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuffer drain(ByteBuffer buf) {
/* 286 */     remap();
/* 287 */     return this.mappedBuffer;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class FactoryData
/*     */   {
/*     */     private final boolean append;
/*     */ 
/*     */     
/*     */     private final boolean immediateFlush;
/*     */ 
/*     */     
/*     */     private final int regionLength;
/*     */ 
/*     */     
/*     */     private final String advertiseURI;
/*     */ 
/*     */     
/*     */     private final Layout<? extends Serializable> layout;
/*     */ 
/*     */ 
/*     */     
/*     */     public FactoryData(boolean append, boolean immediateFlush, int regionLength, String advertiseURI, Layout<? extends Serializable> layout) {
/* 311 */       this.append = append;
/* 312 */       this.immediateFlush = immediateFlush;
/* 313 */       this.regionLength = regionLength;
/* 314 */       this.advertiseURI = advertiseURI;
/* 315 */       this.layout = layout;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class MemoryMappedFileManagerFactory
/*     */     implements ManagerFactory<MemoryMappedFileManager, FactoryData>
/*     */   {
/*     */     private MemoryMappedFileManagerFactory() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public MemoryMappedFileManager createManager(String name, MemoryMappedFileManager.FactoryData data) {
/* 335 */       File file = new File(name);
/* 336 */       if (!data.append) {
/* 337 */         file.delete();
/*     */       }
/*     */       
/* 340 */       boolean writeHeader = (!data.append || !file.exists());
/* 341 */       NullOutputStream nullOutputStream = NullOutputStream.getInstance();
/* 342 */       RandomAccessFile raf = null;
/*     */       try {
/* 344 */         FileUtils.makeParentDirs(file);
/* 345 */         raf = new RandomAccessFile(name, "rw");
/* 346 */         long position = data.append ? raf.length() : 0L;
/* 347 */         raf.setLength(position + data.regionLength);
/* 348 */         return new MemoryMappedFileManager(raf, name, (OutputStream)nullOutputStream, data.immediateFlush, position, data.regionLength, data.advertiseURI, data.layout, writeHeader);
/*     */       }
/* 350 */       catch (Exception ex) {
/* 351 */         AbstractManager.LOGGER.error("MemoryMappedFileManager (" + name + ") " + ex, ex);
/* 352 */         Closer.closeSilently(raf);
/*     */         
/* 354 */         return null;
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\appender\MemoryMappedFileManager.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */