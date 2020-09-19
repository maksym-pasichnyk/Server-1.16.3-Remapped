/*     */ package com.google.common.hash;
/*     */ 
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.security.InvalidKeyException;
/*     */ import java.security.Key;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import javax.crypto.Mac;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class MacHashFunction
/*     */   extends AbstractStreamingHashFunction
/*     */ {
/*     */   private final Mac prototype;
/*     */   private final Key key;
/*     */   private final String toString;
/*     */   private final int bits;
/*     */   private final boolean supportsClone;
/*     */   
/*     */   MacHashFunction(String algorithmName, Key key, String toString) {
/*  38 */     this.prototype = getMac(algorithmName, key);
/*  39 */     this.key = (Key)Preconditions.checkNotNull(key);
/*  40 */     this.toString = (String)Preconditions.checkNotNull(toString);
/*  41 */     this.bits = this.prototype.getMacLength() * 8;
/*  42 */     this.supportsClone = supportsClone(this.prototype);
/*     */   }
/*     */ 
/*     */   
/*     */   public int bits() {
/*  47 */     return this.bits;
/*     */   }
/*     */   
/*     */   private static boolean supportsClone(Mac mac) {
/*     */     try {
/*  52 */       mac.clone();
/*  53 */       return true;
/*  54 */     } catch (CloneNotSupportedException e) {
/*  55 */       return false;
/*     */     } 
/*     */   }
/*     */   
/*     */   private static Mac getMac(String algorithmName, Key key) {
/*     */     try {
/*  61 */       Mac mac = Mac.getInstance(algorithmName);
/*  62 */       mac.init(key);
/*  63 */       return mac;
/*  64 */     } catch (NoSuchAlgorithmException e) {
/*  65 */       throw new IllegalStateException(e);
/*  66 */     } catch (InvalidKeyException e) {
/*  67 */       throw new IllegalArgumentException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Hasher newHasher() {
/*  73 */     if (this.supportsClone) {
/*     */       try {
/*  75 */         return new MacHasher((Mac)this.prototype.clone());
/*  76 */       } catch (CloneNotSupportedException cloneNotSupportedException) {}
/*     */     }
/*     */ 
/*     */     
/*  80 */     return new MacHasher(getMac(this.prototype.getAlgorithm(), this.key));
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  85 */     return this.toString;
/*     */   }
/*     */ 
/*     */   
/*     */   private static final class MacHasher
/*     */     extends AbstractByteHasher
/*     */   {
/*     */     private final Mac mac;
/*     */     private boolean done;
/*     */     
/*     */     private MacHasher(Mac mac) {
/*  96 */       this.mac = mac;
/*     */     }
/*     */ 
/*     */     
/*     */     protected void update(byte b) {
/* 101 */       checkNotDone();
/* 102 */       this.mac.update(b);
/*     */     }
/*     */ 
/*     */     
/*     */     protected void update(byte[] b) {
/* 107 */       checkNotDone();
/* 108 */       this.mac.update(b);
/*     */     }
/*     */ 
/*     */     
/*     */     protected void update(byte[] b, int off, int len) {
/* 113 */       checkNotDone();
/* 114 */       this.mac.update(b, off, len);
/*     */     }
/*     */     
/*     */     private void checkNotDone() {
/* 118 */       Preconditions.checkState(!this.done, "Cannot re-use a Hasher after calling hash() on it");
/*     */     }
/*     */ 
/*     */     
/*     */     public HashCode hash() {
/* 123 */       checkNotDone();
/* 124 */       this.done = true;
/* 125 */       return HashCode.fromBytesNoCopy(this.mac.doFinal());
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\hash\MacHashFunction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */