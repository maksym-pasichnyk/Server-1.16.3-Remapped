/*     */ package org.apache.commons.io;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.lang.ref.PhantomReference;
/*     */ import java.lang.ref.ReferenceQueue;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FileCleaningTracker
/*     */ {
/*  51 */   ReferenceQueue<Object> q = new ReferenceQueue();
/*     */ 
/*     */ 
/*     */   
/*  55 */   final Collection<Tracker> trackers = Collections.synchronizedSet(new HashSet<Tracker>());
/*     */ 
/*     */ 
/*     */   
/*  59 */   final List<String> deleteFailures = Collections.synchronizedList(new ArrayList<String>());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   volatile boolean exitWhenFinished = false;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Thread reaper;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void track(File file, Object marker) {
/*  80 */     track(file, marker, (FileDeleteStrategy)null);
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
/*     */   public void track(File file, Object marker, FileDeleteStrategy deleteStrategy) {
/*  94 */     if (file == null) {
/*  95 */       throw new NullPointerException("The file must not be null");
/*     */     }
/*  97 */     addTracker(file.getPath(), marker, deleteStrategy);
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
/*     */   public void track(String path, Object marker) {
/* 110 */     track(path, marker, (FileDeleteStrategy)null);
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
/*     */   public void track(String path, Object marker, FileDeleteStrategy deleteStrategy) {
/* 124 */     if (path == null) {
/* 125 */       throw new NullPointerException("The path must not be null");
/*     */     }
/* 127 */     addTracker(path, marker, deleteStrategy);
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
/*     */   private synchronized void addTracker(String path, Object marker, FileDeleteStrategy deleteStrategy) {
/* 140 */     if (this.exitWhenFinished) {
/* 141 */       throw new IllegalStateException("No new trackers can be added once exitWhenFinished() is called");
/*     */     }
/* 143 */     if (this.reaper == null) {
/* 144 */       this.reaper = new Reaper();
/* 145 */       this.reaper.start();
/*     */     } 
/* 147 */     this.trackers.add(new Tracker(path, deleteStrategy, marker, this.q));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getTrackCount() {
/* 158 */     return this.trackers.size();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<String> getDeleteFailures() {
/* 168 */     return this.deleteFailures;
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
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void exitWhenFinished() {
/* 195 */     this.exitWhenFinished = true;
/* 196 */     if (this.reaper != null) {
/* 197 */       synchronized (this.reaper) {
/* 198 */         this.reaper.interrupt();
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final class Reaper
/*     */     extends Thread
/*     */   {
/*     */     Reaper() {
/* 210 */       super("File Reaper");
/* 211 */       setPriority(10);
/* 212 */       setDaemon(true);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void run() {
/* 222 */       while (!FileCleaningTracker.this.exitWhenFinished || FileCleaningTracker.this.trackers.size() > 0) {
/*     */         
/*     */         try {
/* 225 */           FileCleaningTracker.Tracker tracker = (FileCleaningTracker.Tracker)FileCleaningTracker.this.q.remove();
/* 226 */           FileCleaningTracker.this.trackers.remove(tracker);
/* 227 */           if (!tracker.delete()) {
/* 228 */             FileCleaningTracker.this.deleteFailures.add(tracker.getPath());
/*     */           }
/* 230 */           tracker.clear();
/* 231 */         } catch (InterruptedException e) {}
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
/*     */   private static final class Tracker
/*     */     extends PhantomReference<Object>
/*     */   {
/*     */     private final String path;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final FileDeleteStrategy deleteStrategy;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     Tracker(String path, FileDeleteStrategy deleteStrategy, Object marker, ReferenceQueue<? super Object> queue) {
/* 263 */       super(marker, queue);
/* 264 */       this.path = path;
/* 265 */       this.deleteStrategy = (deleteStrategy == null) ? FileDeleteStrategy.NORMAL : deleteStrategy;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String getPath() {
/* 274 */       return this.path;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean delete() {
/* 284 */       return this.deleteStrategy.deleteQuietly(new File(this.path));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\commons\io\FileCleaningTracker.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */