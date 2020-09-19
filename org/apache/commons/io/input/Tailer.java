/*     */ package org.apache.commons.io.input;
/*     */ 
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.File;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.io.RandomAccessFile;
/*     */ import java.nio.charset.Charset;
/*     */ import org.apache.commons.io.FileUtils;
/*     */ import org.apache.commons.io.IOUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Tailer
/*     */   implements Runnable
/*     */ {
/*     */   private static final int DEFAULT_DELAY_MILLIS = 1000;
/*     */   private static final String RAF_MODE = "r";
/*     */   private static final int DEFAULT_BUFSIZE = 4096;
/* 130 */   private static final Charset DEFAULT_CHARSET = Charset.defaultCharset();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final byte[] inbuf;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final File file;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final Charset cset;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final long delayMillis;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final boolean end;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final TailerListener listener;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final boolean reOpen;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private volatile boolean run = true;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Tailer(File file, TailerListener listener) {
/* 178 */     this(file, listener, 1000L);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Tailer(File file, TailerListener listener, long delayMillis) {
/* 188 */     this(file, listener, delayMillis, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Tailer(File file, TailerListener listener, long delayMillis, boolean end) {
/* 199 */     this(file, listener, delayMillis, end, 4096);
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
/*     */   public Tailer(File file, TailerListener listener, long delayMillis, boolean end, boolean reOpen) {
/* 212 */     this(file, listener, delayMillis, end, reOpen, 4096);
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
/*     */   public Tailer(File file, TailerListener listener, long delayMillis, boolean end, int bufSize) {
/* 225 */     this(file, listener, delayMillis, end, false, bufSize);
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
/*     */   public Tailer(File file, TailerListener listener, long delayMillis, boolean end, boolean reOpen, int bufSize) {
/* 239 */     this(file, DEFAULT_CHARSET, listener, delayMillis, end, reOpen, bufSize);
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
/*     */   public Tailer(File file, Charset cset, TailerListener listener, long delayMillis, boolean end, boolean reOpen, int bufSize) {
/* 255 */     this.file = file;
/* 256 */     this.delayMillis = delayMillis;
/* 257 */     this.end = end;
/*     */     
/* 259 */     this.inbuf = new byte[bufSize];
/*     */ 
/*     */     
/* 262 */     this.listener = listener;
/* 263 */     listener.init(this);
/* 264 */     this.reOpen = reOpen;
/* 265 */     this.cset = cset;
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
/*     */   public static Tailer create(File file, TailerListener listener, long delayMillis, boolean end, int bufSize) {
/* 280 */     return create(file, listener, delayMillis, end, false, bufSize);
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
/*     */   public static Tailer create(File file, TailerListener listener, long delayMillis, boolean end, boolean reOpen, int bufSize) {
/* 297 */     return create(file, DEFAULT_CHARSET, listener, delayMillis, end, reOpen, bufSize);
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
/*     */   public static Tailer create(File file, Charset charset, TailerListener listener, long delayMillis, boolean end, boolean reOpen, int bufSize) {
/* 315 */     Tailer tailer = new Tailer(file, charset, listener, delayMillis, end, reOpen, bufSize);
/* 316 */     Thread thread = new Thread(tailer);
/* 317 */     thread.setDaemon(true);
/* 318 */     thread.start();
/* 319 */     return tailer;
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
/*     */   public static Tailer create(File file, TailerListener listener, long delayMillis, boolean end) {
/* 333 */     return create(file, listener, delayMillis, end, 4096);
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
/*     */   public static Tailer create(File file, TailerListener listener, long delayMillis, boolean end, boolean reOpen) {
/* 348 */     return create(file, listener, delayMillis, end, reOpen, 4096);
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
/*     */   public static Tailer create(File file, TailerListener listener, long delayMillis) {
/* 360 */     return create(file, listener, delayMillis, false);
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
/*     */   public static Tailer create(File file, TailerListener listener) {
/* 372 */     return create(file, listener, 1000L, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public File getFile() {
/* 381 */     return this.file;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean getRun() {
/* 391 */     return this.run;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getDelay() {
/* 400 */     return this.delayMillis;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void run() {
/* 407 */     RandomAccessFile reader = null;
/*     */     try {
/* 409 */       long last = 0L;
/* 410 */       long position = 0L;
/*     */       
/* 412 */       while (getRun() && reader == null) {
/*     */         try {
/* 414 */           reader = new RandomAccessFile(this.file, "r");
/* 415 */         } catch (FileNotFoundException e) {
/* 416 */           this.listener.fileNotFound();
/*     */         } 
/* 418 */         if (reader == null) {
/* 419 */           Thread.sleep(this.delayMillis);
/*     */           continue;
/*     */         } 
/* 422 */         position = this.end ? this.file.length() : 0L;
/* 423 */         last = this.file.lastModified();
/* 424 */         reader.seek(position);
/*     */       } 
/*     */       
/* 427 */       while (getRun()) {
/* 428 */         boolean newer = FileUtils.isFileNewer(this.file, last);
/*     */         
/* 430 */         long length = this.file.length();
/* 431 */         if (length < position) {
/*     */           
/* 433 */           this.listener.fileRotated();
/*     */ 
/*     */           
/*     */           try {
/* 437 */             RandomAccessFile save = reader;
/* 438 */             reader = new RandomAccessFile(this.file, "r");
/*     */ 
/*     */             
/*     */             try {
/* 442 */               readLines(save);
/* 443 */             } catch (IOException ioe) {
/* 444 */               this.listener.handle(ioe);
/*     */             } 
/* 446 */             position = 0L;
/*     */             
/* 448 */             IOUtils.closeQuietly(save);
/* 449 */           } catch (FileNotFoundException e) {
/*     */             
/* 451 */             this.listener.fileNotFound();
/*     */           } 
/*     */           
/*     */           continue;
/*     */         } 
/*     */         
/* 457 */         if (length > position) {
/*     */           
/* 459 */           position = readLines(reader);
/* 460 */           last = this.file.lastModified();
/* 461 */         } else if (newer) {
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 466 */           position = 0L;
/* 467 */           reader.seek(position);
/*     */ 
/*     */           
/* 470 */           position = readLines(reader);
/* 471 */           last = this.file.lastModified();
/*     */         } 
/*     */         
/* 474 */         if (this.reOpen) {
/* 475 */           IOUtils.closeQuietly(reader);
/*     */         }
/* 477 */         Thread.sleep(this.delayMillis);
/* 478 */         if (getRun() && this.reOpen) {
/* 479 */           reader = new RandomAccessFile(this.file, "r");
/* 480 */           reader.seek(position);
/*     */         } 
/*     */       } 
/* 483 */     } catch (InterruptedException e) {
/* 484 */       Thread.currentThread().interrupt();
/* 485 */       stop(e);
/* 486 */     } catch (Exception e) {
/* 487 */       stop(e);
/*     */     } finally {
/* 489 */       IOUtils.closeQuietly(reader);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void stop(Exception e) {
/* 498 */     this.listener.handle(e);
/* 499 */     stop();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void stop() {
/* 506 */     this.run = false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private long readLines(RandomAccessFile reader) throws IOException {
/* 517 */     ByteArrayOutputStream lineBuf = new ByteArrayOutputStream(64);
/* 518 */     long pos = reader.getFilePointer();
/* 519 */     long rePos = pos;
/*     */     
/* 521 */     boolean seenCR = false; int num;
/* 522 */     while (getRun() && (num = reader.read(this.inbuf)) != -1) {
/* 523 */       for (int i = 0; i < num; i++) {
/* 524 */         byte ch = this.inbuf[i];
/* 525 */         switch (ch) {
/*     */           case 10:
/* 527 */             seenCR = false;
/* 528 */             this.listener.handle(new String(lineBuf.toByteArray(), this.cset));
/* 529 */             lineBuf.reset();
/* 530 */             rePos = pos + i + 1L;
/*     */             break;
/*     */           case 13:
/* 533 */             if (seenCR) {
/* 534 */               lineBuf.write(13);
/*     */             }
/* 536 */             seenCR = true;
/*     */             break;
/*     */           default:
/* 539 */             if (seenCR) {
/* 540 */               seenCR = false;
/* 541 */               this.listener.handle(new String(lineBuf.toByteArray(), this.cset));
/* 542 */               lineBuf.reset();
/* 543 */               rePos = pos + i + 1L;
/*     */             } 
/* 545 */             lineBuf.write(ch); break;
/*     */         } 
/*     */       } 
/* 548 */       pos = reader.getFilePointer();
/*     */     } 
/* 550 */     IOUtils.closeQuietly(lineBuf);
/* 551 */     reader.seek(rePos);
/*     */     
/* 553 */     if (this.listener instanceof TailerListenerAdapter) {
/* 554 */       ((TailerListenerAdapter)this.listener).endOfFileReached();
/*     */     }
/*     */     
/* 557 */     return rePos;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\commons\io\input\Tailer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */