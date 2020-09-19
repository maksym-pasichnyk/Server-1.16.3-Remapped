/*    */ package net.minecraft.util;
/*    */ 
/*    */ import com.google.common.base.Charsets;
/*    */ import java.io.IOException;
/*    */ import java.nio.ByteBuffer;
/*    */ import java.nio.channels.FileChannel;
/*    */ import java.nio.channels.FileLock;
/*    */ import java.nio.file.Files;
/*    */ import java.nio.file.OpenOption;
/*    */ import java.nio.file.Path;
/*    */ import java.nio.file.StandardOpenOption;
/*    */ import java.nio.file.attribute.FileAttribute;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DirectoryLock
/*    */   implements AutoCloseable
/*    */ {
/*    */   private final FileChannel lockFile;
/*    */   private final FileLock lock;
/*    */   private static final ByteBuffer DUMMY;
/*    */   
/*    */   static {
/* 24 */     byte[] debug0 = "☃".getBytes(Charsets.UTF_8);
/* 25 */     DUMMY = ByteBuffer.allocateDirect(debug0.length);
/* 26 */     DUMMY.put(debug0);
/* 27 */     DUMMY.flip();
/*    */   }
/*    */   
/*    */   public static DirectoryLock create(Path debug0) throws IOException {
/* 31 */     Path debug1 = debug0.resolve("session.lock");
/*    */ 
/*    */     
/* 34 */     if (!Files.isDirectory(debug0, new java.nio.file.LinkOption[0])) {
/* 35 */       Files.createDirectories(debug0, (FileAttribute<?>[])new FileAttribute[0]);
/*    */     }
/* 37 */     FileChannel debug2 = FileChannel.open(debug1, new OpenOption[] { StandardOpenOption.CREATE, StandardOpenOption.WRITE });
/*    */     
/*    */     try {
/* 40 */       debug2.write(DUMMY.duplicate());
/* 41 */       debug2.force(true);
/* 42 */       FileLock debug3 = debug2.tryLock();
/* 43 */       if (debug3 == null) {
/* 44 */         throw LockException.alreadyLocked(debug1);
/*    */       }
/* 46 */       return new DirectoryLock(debug2, debug3);
/* 47 */     } catch (IOException debug3) {
/*    */       try {
/* 49 */         debug2.close();
/* 50 */       } catch (IOException debug4) {
/* 51 */         debug3.addSuppressed(debug4);
/*    */       } 
/* 53 */       throw debug3;
/*    */     } 
/*    */   }
/*    */   
/*    */   private DirectoryLock(FileChannel debug1, FileLock debug2) {
/* 58 */     this.lockFile = debug1;
/* 59 */     this.lock = debug2;
/*    */   }
/*    */ 
/*    */   
/*    */   public void close() throws IOException {
/*    */     try {
/* 65 */       if (this.lock.isValid()) {
/* 66 */         this.lock.release();
/*    */       }
/*    */     } finally {
/* 69 */       if (this.lockFile.isOpen()) {
/* 70 */         this.lockFile.close();
/*    */       }
/*    */     } 
/*    */   }
/*    */   
/*    */   public boolean isValid() {
/* 76 */     return this.lock.isValid();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static class LockException
/*    */     extends IOException
/*    */   {
/*    */     private LockException(Path debug1, String debug2) {
/* 94 */       super(debug1.toAbsolutePath() + ": " + debug2);
/*    */     }
/*    */     
/*    */     public static LockException alreadyLocked(Path debug0) {
/* 98 */       return new LockException(debug0, "already locked (possibly by other Minecraft instance?)");
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\DirectoryLock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */