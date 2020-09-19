/*     */ package com.google.common.hash;
/*     */ 
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.ByteOrder;
/*     */ import java.nio.charset.Charset;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ abstract class AbstractStreamingHashFunction
/*     */   implements HashFunction
/*     */ {
/*     */   public <T> HashCode hashObject(T instance, Funnel<? super T> funnel) {
/*  37 */     return newHasher().<T>putObject(instance, funnel).hash();
/*     */   }
/*     */ 
/*     */   
/*     */   public HashCode hashUnencodedChars(CharSequence input) {
/*  42 */     return newHasher().putUnencodedChars(input).hash();
/*     */   }
/*     */ 
/*     */   
/*     */   public HashCode hashString(CharSequence input, Charset charset) {
/*  47 */     return newHasher().putString(input, charset).hash();
/*     */   }
/*     */ 
/*     */   
/*     */   public HashCode hashInt(int input) {
/*  52 */     return newHasher().putInt(input).hash();
/*     */   }
/*     */ 
/*     */   
/*     */   public HashCode hashLong(long input) {
/*  57 */     return newHasher().putLong(input).hash();
/*     */   }
/*     */ 
/*     */   
/*     */   public HashCode hashBytes(byte[] input) {
/*  62 */     return newHasher().putBytes(input).hash();
/*     */   }
/*     */ 
/*     */   
/*     */   public HashCode hashBytes(byte[] input, int off, int len) {
/*  67 */     return newHasher().putBytes(input, off, len).hash();
/*     */   }
/*     */ 
/*     */   
/*     */   public Hasher newHasher(int expectedInputSize) {
/*  72 */     Preconditions.checkArgument((expectedInputSize >= 0));
/*  73 */     return newHasher();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   protected static abstract class AbstractStreamingHasher
/*     */     extends AbstractHasher
/*     */   {
/*     */     private final ByteBuffer buffer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final int bufferSize;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final int chunkSize;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected AbstractStreamingHasher(int chunkSize) {
/* 103 */       this(chunkSize, chunkSize);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected AbstractStreamingHasher(int chunkSize, int bufferSize) {
/* 117 */       Preconditions.checkArgument((bufferSize % chunkSize == 0));
/*     */ 
/*     */ 
/*     */       
/* 121 */       this.buffer = ByteBuffer.allocate(bufferSize + 7).order(ByteOrder.LITTLE_ENDIAN);
/* 122 */       this.bufferSize = bufferSize;
/* 123 */       this.chunkSize = chunkSize;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected void processRemaining(ByteBuffer bb) {
/* 138 */       bb.position(bb.limit());
/* 139 */       bb.limit(this.chunkSize + 7);
/* 140 */       while (bb.position() < this.chunkSize) {
/* 141 */         bb.putLong(0L);
/*     */       }
/* 143 */       bb.limit(this.chunkSize);
/* 144 */       bb.flip();
/* 145 */       process(bb);
/*     */     }
/*     */ 
/*     */     
/*     */     public final Hasher putBytes(byte[] bytes) {
/* 150 */       return putBytes(bytes, 0, bytes.length);
/*     */     }
/*     */ 
/*     */     
/*     */     public final Hasher putBytes(byte[] bytes, int off, int len) {
/* 155 */       return putBytes(ByteBuffer.wrap(bytes, off, len).order(ByteOrder.LITTLE_ENDIAN));
/*     */     }
/*     */ 
/*     */     
/*     */     private Hasher putBytes(ByteBuffer readBuffer) {
/* 160 */       if (readBuffer.remaining() <= this.buffer.remaining()) {
/* 161 */         this.buffer.put(readBuffer);
/* 162 */         munchIfFull();
/* 163 */         return this;
/*     */       } 
/*     */ 
/*     */       
/* 167 */       int bytesToCopy = this.bufferSize - this.buffer.position();
/* 168 */       for (int i = 0; i < bytesToCopy; i++) {
/* 169 */         this.buffer.put(readBuffer.get());
/*     */       }
/* 171 */       munch();
/*     */ 
/*     */       
/* 174 */       while (readBuffer.remaining() >= this.chunkSize) {
/* 175 */         process(readBuffer);
/*     */       }
/*     */ 
/*     */       
/* 179 */       this.buffer.put(readBuffer);
/* 180 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public final Hasher putUnencodedChars(CharSequence charSequence) {
/* 185 */       for (int i = 0; i < charSequence.length(); i++) {
/* 186 */         putChar(charSequence.charAt(i));
/*     */       }
/* 188 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public final Hasher putByte(byte b) {
/* 203 */       this.buffer.put(b);
/* 204 */       munchIfFull();
/* 205 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public final Hasher putShort(short s) {
/* 210 */       this.buffer.putShort(s);
/* 211 */       munchIfFull();
/* 212 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public final Hasher putChar(char c) {
/* 217 */       this.buffer.putChar(c);
/* 218 */       munchIfFull();
/* 219 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public final Hasher putInt(int i) {
/* 224 */       this.buffer.putInt(i);
/* 225 */       munchIfFull();
/* 226 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public final Hasher putLong(long l) {
/* 231 */       this.buffer.putLong(l);
/* 232 */       munchIfFull();
/* 233 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public final <T> Hasher putObject(T instance, Funnel<? super T> funnel) {
/* 238 */       funnel.funnel(instance, this);
/* 239 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public final HashCode hash() {
/* 244 */       munch();
/* 245 */       this.buffer.flip();
/* 246 */       if (this.buffer.remaining() > 0) {
/* 247 */         processRemaining(this.buffer);
/*     */       }
/* 249 */       return makeHash();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private void munchIfFull() {
/* 256 */       if (this.buffer.remaining() < 8)
/*     */       {
/* 258 */         munch();
/*     */       }
/*     */     }
/*     */     
/*     */     private void munch() {
/* 263 */       this.buffer.flip();
/* 264 */       while (this.buffer.remaining() >= this.chunkSize)
/*     */       {
/*     */         
/* 267 */         process(this.buffer);
/*     */       }
/* 269 */       this.buffer.compact();
/*     */     }
/*     */     
/*     */     protected abstract void process(ByteBuffer param1ByteBuffer);
/*     */     
/*     */     abstract HashCode makeHash();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\hash\AbstractStreamingHashFunction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */