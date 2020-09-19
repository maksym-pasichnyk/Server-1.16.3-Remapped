/*     */ package io.netty.handler.codec.http2;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.util.AsciiString;
/*     */ import io.netty.util.ByteProcessor;
/*     */ import io.netty.util.internal.ObjectUtil;
/*     */ import io.netty.util.internal.PlatformDependent;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class HpackHuffmanEncoder
/*     */ {
/*     */   private final int[] codes;
/*     */   private final byte[] lengths;
/*  44 */   private final EncodedLengthProcessor encodedLengthProcessor = new EncodedLengthProcessor();
/*  45 */   private final EncodeProcessor encodeProcessor = new EncodeProcessor();
/*     */   
/*     */   HpackHuffmanEncoder() {
/*  48 */     this(HpackUtil.HUFFMAN_CODES, HpackUtil.HUFFMAN_CODE_LENGTHS);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private HpackHuffmanEncoder(int[] codes, byte[] lengths) {
/*  58 */     this.codes = codes;
/*  59 */     this.lengths = lengths;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void encode(ByteBuf out, CharSequence data) {
/*  69 */     ObjectUtil.checkNotNull(out, "out");
/*  70 */     if (data instanceof AsciiString) {
/*  71 */       AsciiString string = (AsciiString)data;
/*     */       try {
/*  73 */         this.encodeProcessor.out = out;
/*  74 */         string.forEachByte(this.encodeProcessor);
/*  75 */       } catch (Exception e) {
/*  76 */         PlatformDependent.throwException(e);
/*     */       } finally {
/*  78 */         this.encodeProcessor.end();
/*     */       } 
/*     */     } else {
/*  81 */       encodeSlowPath(out, data);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void encodeSlowPath(ByteBuf out, CharSequence data) {
/*  86 */     long current = 0L;
/*  87 */     int n = 0;
/*     */     
/*  89 */     for (int i = 0; i < data.length(); i++) {
/*  90 */       int b = data.charAt(i) & 0xFF;
/*  91 */       int code = this.codes[b];
/*  92 */       int nbits = this.lengths[b];
/*     */       
/*  94 */       current <<= nbits;
/*  95 */       current |= code;
/*  96 */       n += nbits;
/*     */       
/*  98 */       while (n >= 8) {
/*  99 */         n -= 8;
/* 100 */         out.writeByte((int)(current >> n));
/*     */       } 
/*     */     } 
/*     */     
/* 104 */     if (n > 0) {
/* 105 */       current <<= 8 - n;
/* 106 */       current |= (255 >>> n);
/* 107 */       out.writeByte((int)current);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   int getEncodedLength(CharSequence data) {
/* 118 */     if (data instanceof AsciiString) {
/* 119 */       AsciiString string = (AsciiString)data;
/*     */       try {
/* 121 */         this.encodedLengthProcessor.reset();
/* 122 */         string.forEachByte(this.encodedLengthProcessor);
/* 123 */         return this.encodedLengthProcessor.length();
/* 124 */       } catch (Exception e) {
/* 125 */         PlatformDependent.throwException(e);
/* 126 */         return -1;
/*     */       } 
/*     */     } 
/* 129 */     return getEncodedLengthSlowPath(data);
/*     */   }
/*     */ 
/*     */   
/*     */   private int getEncodedLengthSlowPath(CharSequence data) {
/* 134 */     long len = 0L;
/* 135 */     for (int i = 0; i < data.length(); i++) {
/* 136 */       len += this.lengths[data.charAt(i) & 0xFF];
/*     */     }
/* 138 */     return (int)(len + 7L >> 3L);
/*     */   }
/*     */   
/*     */   private final class EncodeProcessor
/*     */     implements ByteProcessor
/*     */   {
/*     */     ByteBuf out;
/*     */     private long current;
/*     */     
/*     */     public boolean process(byte value) {
/* 148 */       int b = value & 0xFF;
/* 149 */       int nbits = HpackHuffmanEncoder.this.lengths[b];
/*     */       
/* 151 */       this.current <<= nbits;
/* 152 */       this.current |= HpackHuffmanEncoder.this.codes[b];
/* 153 */       this.n += nbits;
/*     */       
/* 155 */       while (this.n >= 8) {
/* 156 */         this.n -= 8;
/* 157 */         this.out.writeByte((int)(this.current >> this.n));
/*     */       } 
/* 159 */       return true;
/*     */     } private int n;
/*     */     private EncodeProcessor() {}
/*     */     void end() {
/*     */       try {
/* 164 */         if (this.n > 0) {
/* 165 */           this.current <<= 8 - this.n;
/* 166 */           this.current |= (255 >>> this.n);
/* 167 */           this.out.writeByte((int)this.current);
/*     */         } 
/*     */       } finally {
/* 170 */         this.out = null;
/* 171 */         this.current = 0L;
/* 172 */         this.n = 0;
/*     */       } 
/*     */     } }
/*     */   
/*     */   private final class EncodedLengthProcessor implements ByteProcessor {
/*     */     private long len;
/*     */     
/*     */     private EncodedLengthProcessor() {}
/*     */     
/*     */     public boolean process(byte value) {
/* 182 */       this.len += HpackHuffmanEncoder.this.lengths[value & 0xFF];
/* 183 */       return true;
/*     */     }
/*     */     
/*     */     void reset() {
/* 187 */       this.len = 0L;
/*     */     }
/*     */     
/*     */     int length() {
/* 191 */       return (int)(this.len + 7L >> 3L);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\http2\HpackHuffmanEncoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */