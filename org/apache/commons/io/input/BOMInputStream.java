/*     */ package org.apache.commons.io.input;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.util.Arrays;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import org.apache.commons.io.ByteOrderMark;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BOMInputStream
/*     */   extends ProxyInputStream
/*     */ {
/*     */   private final boolean include;
/*     */   private final List<ByteOrderMark> boms;
/*     */   private ByteOrderMark byteOrderMark;
/*     */   private int[] firstBytes;
/*     */   private int fbLength;
/*     */   private int fbIndex;
/*     */   private int markFbIndex;
/*     */   private boolean markedAtStart;
/*     */   
/*     */   public BOMInputStream(InputStream delegate) {
/* 109 */     this(delegate, false, new ByteOrderMark[] { ByteOrderMark.UTF_8 });
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
/*     */   public BOMInputStream(InputStream delegate, boolean include) {
/* 121 */     this(delegate, include, new ByteOrderMark[] { ByteOrderMark.UTF_8 });
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
/*     */   public BOMInputStream(InputStream delegate, ByteOrderMark... boms) {
/* 133 */     this(delegate, false, boms);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 139 */   private static final Comparator<ByteOrderMark> ByteOrderMarkLengthComparator = new Comparator<ByteOrderMark>()
/*     */     {
/*     */       public int compare(ByteOrderMark bom1, ByteOrderMark bom2) {
/* 142 */         int len1 = bom1.length();
/* 143 */         int len2 = bom2.length();
/* 144 */         if (len1 > len2) {
/* 145 */           return -1;
/*     */         }
/* 147 */         if (len2 > len1) {
/* 148 */           return 1;
/*     */         }
/* 150 */         return 0;
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BOMInputStream(InputStream delegate, boolean include, ByteOrderMark... boms) {
/* 165 */     super(delegate);
/* 166 */     if (boms == null || boms.length == 0) {
/* 167 */       throw new IllegalArgumentException("No BOMs specified");
/*     */     }
/* 169 */     this.include = include;
/*     */     
/* 171 */     Arrays.sort(boms, ByteOrderMarkLengthComparator);
/* 172 */     this.boms = Arrays.asList(boms);
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
/*     */   public boolean hasBOM() throws IOException {
/* 184 */     return (getBOM() != null);
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
/*     */   public boolean hasBOM(ByteOrderMark bom) throws IOException {
/* 199 */     if (!this.boms.contains(bom)) {
/* 200 */       throw new IllegalArgumentException("Stream not configure to detect " + bom);
/*     */     }
/* 202 */     return (this.byteOrderMark != null && getBOM().equals(bom));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ByteOrderMark getBOM() throws IOException {
/* 213 */     if (this.firstBytes == null) {
/* 214 */       this.fbLength = 0;
/*     */       
/* 216 */       int maxBomSize = ((ByteOrderMark)this.boms.get(0)).length();
/* 217 */       this.firstBytes = new int[maxBomSize];
/*     */       
/* 219 */       for (int i = 0; i < this.firstBytes.length; i++) {
/* 220 */         this.firstBytes[i] = this.in.read();
/* 221 */         this.fbLength++;
/* 222 */         if (this.firstBytes[i] < 0) {
/*     */           break;
/*     */         }
/*     */       } 
/*     */       
/* 227 */       this.byteOrderMark = find();
/* 228 */       if (this.byteOrderMark != null && 
/* 229 */         !this.include) {
/* 230 */         if (this.byteOrderMark.length() < this.firstBytes.length) {
/* 231 */           this.fbIndex = this.byteOrderMark.length();
/*     */         } else {
/* 233 */           this.fbLength = 0;
/*     */         } 
/*     */       }
/*     */     } 
/*     */     
/* 238 */     return this.byteOrderMark;
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
/*     */   public String getBOMCharsetName() throws IOException {
/* 250 */     getBOM();
/* 251 */     return (this.byteOrderMark == null) ? null : this.byteOrderMark.getCharsetName();
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
/*     */   private int readFirstBytes() throws IOException {
/* 264 */     getBOM();
/* 265 */     return (this.fbIndex < this.fbLength) ? this.firstBytes[this.fbIndex++] : -1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ByteOrderMark find() {
/* 274 */     for (ByteOrderMark bom : this.boms) {
/* 275 */       if (matches(bom)) {
/* 276 */         return bom;
/*     */       }
/*     */     } 
/* 279 */     return null;
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
/*     */   private boolean matches(ByteOrderMark bom) {
/* 294 */     for (int i = 0; i < bom.length(); i++) {
/* 295 */       if (bom.get(i) != this.firstBytes[i]) {
/* 296 */         return false;
/*     */       }
/*     */     } 
/* 299 */     return true;
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
/*     */   public int read() throws IOException {
/* 315 */     int b = readFirstBytes();
/* 316 */     return (b >= 0) ? b : this.in.read();
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
/*     */   public int read(byte[] buf, int off, int len) throws IOException {
/* 334 */     int firstCount = 0;
/* 335 */     int b = 0;
/* 336 */     while (len > 0 && b >= 0) {
/* 337 */       b = readFirstBytes();
/* 338 */       if (b >= 0) {
/* 339 */         buf[off++] = (byte)(b & 0xFF);
/* 340 */         len--;
/* 341 */         firstCount++;
/*     */       } 
/*     */     } 
/* 344 */     int secondCount = this.in.read(buf, off, len);
/* 345 */     return (secondCount < 0) ? ((firstCount > 0) ? firstCount : -1) : (firstCount + secondCount);
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
/*     */   public int read(byte[] buf) throws IOException {
/* 359 */     return read(buf, 0, buf.length);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void mark(int readlimit) {
/* 370 */     this.markFbIndex = this.fbIndex;
/* 371 */     this.markedAtStart = (this.firstBytes == null);
/* 372 */     this.in.mark(readlimit);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void reset() throws IOException {
/* 383 */     this.fbIndex = this.markFbIndex;
/* 384 */     if (this.markedAtStart) {
/* 385 */       this.firstBytes = null;
/*     */     }
/*     */     
/* 388 */     this.in.reset();
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
/*     */   public long skip(long n) throws IOException {
/* 402 */     int skipped = 0;
/* 403 */     while (n > skipped && readFirstBytes() >= 0) {
/* 404 */       skipped++;
/*     */     }
/* 406 */     return this.in.skip(n - skipped) + skipped;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\commons\io\input\BOMInputStream.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */