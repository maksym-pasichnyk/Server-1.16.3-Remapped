/*     */ package org.apache.commons.io.filefilter;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.RandomAccessFile;
/*     */ import java.io.Serializable;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.Arrays;
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
/*     */ public class MagicNumberFileFilter
/*     */   extends AbstractFileFilter
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -547733176983104172L;
/*     */   private final byte[] magicNumbers;
/*     */   private final long byteOffset;
/*     */   
/*     */   public MagicNumberFileFilter(byte[] magicNumber) {
/* 114 */     this(magicNumber, 0L);
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
/*     */   public MagicNumberFileFilter(String magicNumber) {
/* 139 */     this(magicNumber, 0L);
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
/*     */   public MagicNumberFileFilter(String magicNumber, long offset) {
/* 163 */     if (magicNumber == null) {
/* 164 */       throw new IllegalArgumentException("The magic number cannot be null");
/*     */     }
/* 166 */     if (magicNumber.isEmpty()) {
/* 167 */       throw new IllegalArgumentException("The magic number must contain at least one byte");
/*     */     }
/* 169 */     if (offset < 0L) {
/* 170 */       throw new IllegalArgumentException("The offset cannot be negative");
/*     */     }
/*     */     
/* 173 */     this.magicNumbers = magicNumber.getBytes(Charset.defaultCharset());
/*     */     
/* 175 */     this.byteOffset = offset;
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
/*     */   
/*     */   public MagicNumberFileFilter(byte[] magicNumber, long offset) {
/* 203 */     if (magicNumber == null) {
/* 204 */       throw new IllegalArgumentException("The magic number cannot be null");
/*     */     }
/* 206 */     if (magicNumber.length == 0) {
/* 207 */       throw new IllegalArgumentException("The magic number must contain at least one byte");
/*     */     }
/* 209 */     if (offset < 0L) {
/* 210 */       throw new IllegalArgumentException("The offset cannot be negative");
/*     */     }
/*     */     
/* 213 */     this.magicNumbers = new byte[magicNumber.length];
/* 214 */     System.arraycopy(magicNumber, 0, this.magicNumbers, 0, magicNumber.length);
/* 215 */     this.byteOffset = offset;
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
/*     */   public boolean accept(File file) {
/* 236 */     if (file != null && file.isFile() && file.canRead()) {
/* 237 */       RandomAccessFile randomAccessFile = null;
/*     */       try {
/* 239 */         byte[] fileBytes = new byte[this.magicNumbers.length];
/* 240 */         randomAccessFile = new RandomAccessFile(file, "r");
/* 241 */         randomAccessFile.seek(this.byteOffset);
/* 242 */         int read = randomAccessFile.read(fileBytes);
/* 243 */         if (read != this.magicNumbers.length) {
/* 244 */           return false;
/*     */         }
/* 246 */         return Arrays.equals(this.magicNumbers, fileBytes);
/* 247 */       } catch (IOException iOException) {
/*     */       
/*     */       } finally {
/* 250 */         IOUtils.closeQuietly(randomAccessFile);
/*     */       } 
/*     */     } 
/*     */     
/* 254 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 265 */     StringBuilder builder = new StringBuilder(super.toString());
/* 266 */     builder.append("(");
/* 267 */     builder.append(new String(this.magicNumbers, Charset.defaultCharset()));
/*     */     
/* 269 */     builder.append(",");
/* 270 */     builder.append(this.byteOffset);
/* 271 */     builder.append(")");
/* 272 */     return builder.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\commons\io\filefilter\MagicNumberFileFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */