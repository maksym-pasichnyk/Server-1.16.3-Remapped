/*    */ package com.google.common.hash;
/*    */ 
/*    */ import com.google.common.base.Preconditions;
/*    */ import com.google.common.base.Supplier;
/*    */ import java.io.Serializable;
/*    */ import java.util.zip.Checksum;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ final class ChecksumHashFunction
/*    */   extends AbstractStreamingHashFunction
/*    */   implements Serializable
/*    */ {
/*    */   private final Supplier<? extends Checksum> checksumSupplier;
/*    */   private final int bits;
/*    */   private final String toString;
/*    */   private static final long serialVersionUID = 0L;
/*    */   
/*    */   ChecksumHashFunction(Supplier<? extends Checksum> checksumSupplier, int bits, String toString) {
/* 35 */     this.checksumSupplier = (Supplier<? extends Checksum>)Preconditions.checkNotNull(checksumSupplier);
/* 36 */     Preconditions.checkArgument((bits == 32 || bits == 64), "bits (%s) must be either 32 or 64", bits);
/* 37 */     this.bits = bits;
/* 38 */     this.toString = (String)Preconditions.checkNotNull(toString);
/*    */   }
/*    */ 
/*    */   
/*    */   public int bits() {
/* 43 */     return this.bits;
/*    */   }
/*    */ 
/*    */   
/*    */   public Hasher newHasher() {
/* 48 */     return new ChecksumHasher((Checksum)this.checksumSupplier.get());
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 53 */     return this.toString;
/*    */   }
/*    */ 
/*    */   
/*    */   private final class ChecksumHasher
/*    */     extends AbstractByteHasher
/*    */   {
/*    */     private final Checksum checksum;
/*    */     
/*    */     private ChecksumHasher(Checksum checksum) {
/* 63 */       this.checksum = (Checksum)Preconditions.checkNotNull(checksum);
/*    */     }
/*    */ 
/*    */     
/*    */     protected void update(byte b) {
/* 68 */       this.checksum.update(b);
/*    */     }
/*    */ 
/*    */     
/*    */     protected void update(byte[] bytes, int off, int len) {
/* 73 */       this.checksum.update(bytes, off, len);
/*    */     }
/*    */ 
/*    */     
/*    */     public HashCode hash() {
/* 78 */       long value = this.checksum.getValue();
/* 79 */       if (ChecksumHashFunction.this.bits == 32)
/*    */       {
/*    */ 
/*    */ 
/*    */ 
/*    */         
/* 85 */         return HashCode.fromInt((int)value);
/*    */       }
/* 87 */       return HashCode.fromLong(value);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\hash\ChecksumHashFunction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */