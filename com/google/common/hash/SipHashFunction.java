/*     */ package com.google.common.hash;
/*     */ 
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.io.Serializable;
/*     */ import java.nio.ByteBuffer;
/*     */ import javax.annotation.Nullable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class SipHashFunction
/*     */   extends AbstractStreamingHashFunction
/*     */   implements Serializable
/*     */ {
/*     */   private final int c;
/*     */   private final int d;
/*     */   private final long k0;
/*     */   private final long k1;
/*     */   private static final long serialVersionUID = 0L;
/*     */   
/*     */   SipHashFunction(int c, int d, long k0, long k1) {
/*  52 */     Preconditions.checkArgument((c > 0), "The number of SipRound iterations (c=%s) during Compression must be positive.", c);
/*     */     
/*  54 */     Preconditions.checkArgument((d > 0), "The number of SipRound iterations (d=%s) during Finalization must be positive.", d);
/*     */     
/*  56 */     this.c = c;
/*  57 */     this.d = d;
/*  58 */     this.k0 = k0;
/*  59 */     this.k1 = k1;
/*     */   }
/*     */ 
/*     */   
/*     */   public int bits() {
/*  64 */     return 64;
/*     */   }
/*     */ 
/*     */   
/*     */   public Hasher newHasher() {
/*  69 */     return new SipHasher(this.c, this.d, this.k0, this.k1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/*  76 */     return "Hashing.sipHash" + this.c + "" + this.d + "(" + this.k0 + ", " + this.k1 + ")";
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(@Nullable Object object) {
/*  81 */     if (object instanceof SipHashFunction) {
/*  82 */       SipHashFunction other = (SipHashFunction)object;
/*  83 */       return (this.c == other.c && this.d == other.d && this.k0 == other.k0 && this.k1 == other.k1);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/*  88 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  93 */     return (int)((getClass().hashCode() ^ this.c ^ this.d) ^ this.k0 ^ this.k1);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static final class SipHasher
/*     */     extends AbstractStreamingHashFunction.AbstractStreamingHasher
/*     */   {
/*     */     private static final int CHUNK_SIZE = 8;
/*     */ 
/*     */     
/*     */     private final int c;
/*     */     
/*     */     private final int d;
/*     */     
/* 108 */     private long v0 = 8317987319222330741L;
/* 109 */     private long v1 = 7237128888997146477L;
/* 110 */     private long v2 = 7816392313619706465L;
/* 111 */     private long v3 = 8387220255154660723L;
/*     */ 
/*     */     
/* 114 */     private long b = 0L;
/*     */ 
/*     */ 
/*     */     
/* 118 */     private long finalM = 0L;
/*     */     
/*     */     SipHasher(int c, int d, long k0, long k1) {
/* 121 */       super(8);
/* 122 */       this.c = c;
/* 123 */       this.d = d;
/* 124 */       this.v0 ^= k0;
/* 125 */       this.v1 ^= k1;
/* 126 */       this.v2 ^= k0;
/* 127 */       this.v3 ^= k1;
/*     */     }
/*     */ 
/*     */     
/*     */     protected void process(ByteBuffer buffer) {
/* 132 */       this.b += 8L;
/* 133 */       processM(buffer.getLong());
/*     */     }
/*     */ 
/*     */     
/*     */     protected void processRemaining(ByteBuffer buffer) {
/* 138 */       this.b += buffer.remaining();
/* 139 */       for (int i = 0; buffer.hasRemaining(); i += 8) {
/* 140 */         this.finalM ^= (buffer.get() & 0xFFL) << i;
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public HashCode makeHash() {
/* 147 */       this.finalM ^= this.b << 56L;
/* 148 */       processM(this.finalM);
/*     */ 
/*     */       
/* 151 */       this.v2 ^= 0xFFL;
/* 152 */       sipRound(this.d);
/* 153 */       return HashCode.fromLong(this.v0 ^ this.v1 ^ this.v2 ^ this.v3);
/*     */     }
/*     */     
/*     */     private void processM(long m) {
/* 157 */       this.v3 ^= m;
/* 158 */       sipRound(this.c);
/* 159 */       this.v0 ^= m;
/*     */     }
/*     */     
/*     */     private void sipRound(int iterations) {
/* 163 */       for (int i = 0; i < iterations; i++) {
/* 164 */         this.v0 += this.v1;
/* 165 */         this.v2 += this.v3;
/* 166 */         this.v1 = Long.rotateLeft(this.v1, 13);
/* 167 */         this.v3 = Long.rotateLeft(this.v3, 16);
/* 168 */         this.v1 ^= this.v0;
/* 169 */         this.v3 ^= this.v2;
/* 170 */         this.v0 = Long.rotateLeft(this.v0, 32);
/* 171 */         this.v2 += this.v1;
/* 172 */         this.v0 += this.v3;
/* 173 */         this.v1 = Long.rotateLeft(this.v1, 17);
/* 174 */         this.v3 = Long.rotateLeft(this.v3, 21);
/* 175 */         this.v1 ^= this.v2;
/* 176 */         this.v3 ^= this.v0;
/* 177 */         this.v2 = Long.rotateLeft(this.v2, 32);
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\hash\SipHashFunction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */