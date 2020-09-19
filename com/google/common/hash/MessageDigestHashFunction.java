/*     */ package com.google.common.hash;
/*     */ 
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.io.Serializable;
/*     */ import java.security.MessageDigest;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.util.Arrays;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class MessageDigestHashFunction
/*     */   extends AbstractStreamingHashFunction
/*     */   implements Serializable
/*     */ {
/*     */   private final MessageDigest prototype;
/*     */   private final int bytes;
/*     */   private final boolean supportsClone;
/*     */   private final String toString;
/*     */   
/*     */   MessageDigestHashFunction(String algorithmName, String toString) {
/*  40 */     this.prototype = getMessageDigest(algorithmName);
/*  41 */     this.bytes = this.prototype.getDigestLength();
/*  42 */     this.toString = (String)Preconditions.checkNotNull(toString);
/*  43 */     this.supportsClone = supportsClone(this.prototype);
/*     */   }
/*     */   
/*     */   MessageDigestHashFunction(String algorithmName, int bytes, String toString) {
/*  47 */     this.toString = (String)Preconditions.checkNotNull(toString);
/*  48 */     this.prototype = getMessageDigest(algorithmName);
/*  49 */     int maxLength = this.prototype.getDigestLength();
/*  50 */     Preconditions.checkArgument((bytes >= 4 && bytes <= maxLength), "bytes (%s) must be >= 4 and < %s", bytes, maxLength);
/*     */     
/*  52 */     this.bytes = bytes;
/*  53 */     this.supportsClone = supportsClone(this.prototype);
/*     */   }
/*     */   
/*     */   private static boolean supportsClone(MessageDigest digest) {
/*     */     try {
/*  58 */       digest.clone();
/*  59 */       return true;
/*  60 */     } catch (CloneNotSupportedException e) {
/*  61 */       return false;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int bits() {
/*  67 */     return this.bytes * 8;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  72 */     return this.toString;
/*     */   }
/*     */   
/*     */   private static MessageDigest getMessageDigest(String algorithmName) {
/*     */     try {
/*  77 */       return MessageDigest.getInstance(algorithmName);
/*  78 */     } catch (NoSuchAlgorithmException e) {
/*  79 */       throw new AssertionError(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Hasher newHasher() {
/*  85 */     if (this.supportsClone) {
/*     */       try {
/*  87 */         return new MessageDigestHasher((MessageDigest)this.prototype.clone(), this.bytes);
/*  88 */       } catch (CloneNotSupportedException cloneNotSupportedException) {}
/*     */     }
/*     */ 
/*     */     
/*  92 */     return new MessageDigestHasher(getMessageDigest(this.prototype.getAlgorithm()), this.bytes);
/*     */   }
/*     */   
/*     */   private static final class SerializedForm
/*     */     implements Serializable {
/*     */     private final String algorithmName;
/*     */     private final int bytes;
/*     */     
/*     */     private SerializedForm(String algorithmName, int bytes, String toString) {
/* 101 */       this.algorithmName = algorithmName;
/* 102 */       this.bytes = bytes;
/* 103 */       this.toString = toString;
/*     */     }
/*     */     private final String toString; private static final long serialVersionUID = 0L;
/*     */     private Object readResolve() {
/* 107 */       return new MessageDigestHashFunction(this.algorithmName, this.bytes, this.toString);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   Object writeReplace() {
/* 114 */     return new SerializedForm(this.prototype.getAlgorithm(), this.bytes, this.toString);
/*     */   }
/*     */ 
/*     */   
/*     */   private static final class MessageDigestHasher
/*     */     extends AbstractByteHasher
/*     */   {
/*     */     private final MessageDigest digest;
/*     */     private final int bytes;
/*     */     private boolean done;
/*     */     
/*     */     private MessageDigestHasher(MessageDigest digest, int bytes) {
/* 126 */       this.digest = digest;
/* 127 */       this.bytes = bytes;
/*     */     }
/*     */ 
/*     */     
/*     */     protected void update(byte b) {
/* 132 */       checkNotDone();
/* 133 */       this.digest.update(b);
/*     */     }
/*     */ 
/*     */     
/*     */     protected void update(byte[] b) {
/* 138 */       checkNotDone();
/* 139 */       this.digest.update(b);
/*     */     }
/*     */ 
/*     */     
/*     */     protected void update(byte[] b, int off, int len) {
/* 144 */       checkNotDone();
/* 145 */       this.digest.update(b, off, len);
/*     */     }
/*     */     
/*     */     private void checkNotDone() {
/* 149 */       Preconditions.checkState(!this.done, "Cannot re-use a Hasher after calling hash() on it");
/*     */     }
/*     */ 
/*     */     
/*     */     public HashCode hash() {
/* 154 */       checkNotDone();
/* 155 */       this.done = true;
/* 156 */       return (this.bytes == this.digest.getDigestLength()) ? 
/* 157 */         HashCode.fromBytesNoCopy(this.digest.digest()) : 
/* 158 */         HashCode.fromBytesNoCopy(Arrays.copyOf(this.digest.digest(), this.bytes));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\hash\MessageDigestHashFunction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */