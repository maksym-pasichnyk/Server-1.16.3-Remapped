/*     */ package io.netty.handler.codec.compression;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.handler.codec.ByteToMessageDecoder;
/*     */ import java.util.List;
/*     */ import java.util.zip.Checksum;
/*     */ import net.jpountz.lz4.LZ4Factory;
/*     */ import net.jpountz.lz4.LZ4FastDecompressor;
/*     */ import net.jpountz.xxhash.XXHashFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Lz4FrameDecoder
/*     */   extends ByteToMessageDecoder
/*     */ {
/*     */   private enum State
/*     */   {
/*  52 */     INIT_BLOCK,
/*  53 */     DECOMPRESS_DATA,
/*  54 */     FINISHED,
/*  55 */     CORRUPTED;
/*     */   }
/*     */   
/*  58 */   private State currentState = State.INIT_BLOCK;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private LZ4FastDecompressor decompressor;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ByteBufChecksum checksum;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int blockType;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int compressedLength;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int decompressedLength;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int currentChecksum;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Lz4FrameDecoder() {
/* 100 */     this(false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Lz4FrameDecoder(boolean validateChecksums) {
/* 111 */     this(LZ4Factory.fastestInstance(), validateChecksums);
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
/*     */   public Lz4FrameDecoder(LZ4Factory factory, boolean validateChecksums) {
/* 127 */     this(factory, validateChecksums ? 
/* 128 */         XXHashFactory.fastestInstance().newStreamingHash32(-1756908916).asChecksum() : null);
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
/*     */   public Lz4FrameDecoder(LZ4Factory factory, Checksum checksum) {
/* 142 */     if (factory == null) {
/* 143 */       throw new NullPointerException("factory");
/*     */     }
/* 145 */     this.decompressor = factory.fastDecompressor();
/* 146 */     this.checksum = (checksum == null) ? null : ByteBufChecksum.wrapChecksum(checksum); } protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
/*     */     try {
/*     */       int blockType;
/*     */       int compressedLength;
/*     */       int decompressedLength;
/*     */       int currentChecksum;
/* 152 */       switch (this.currentState) {
/*     */         case INIT_BLOCK:
/* 154 */           if (in.readableBytes() >= 21) {
/*     */ 
/*     */             
/* 157 */             long magic = in.readLong();
/* 158 */             if (magic != 5501767354678207339L) {
/* 159 */               throw new DecompressionException("unexpected block identifier");
/*     */             }
/*     */             
/* 162 */             int token = in.readByte();
/* 163 */             int compressionLevel = (token & 0xF) + 10;
/* 164 */             int i = token & 0xF0;
/*     */             
/* 166 */             int j = Integer.reverseBytes(in.readInt());
/* 167 */             if (j < 0 || j > 33554432) {
/* 168 */               throw new DecompressionException(String.format("invalid compressedLength: %d (expected: 0-%d)", new Object[] {
/*     */                       
/* 170 */                       Integer.valueOf(j), Integer.valueOf(33554432)
/*     */                     }));
/*     */             }
/* 173 */             int k = Integer.reverseBytes(in.readInt());
/* 174 */             int maxDecompressedLength = 1 << compressionLevel;
/* 175 */             if (k < 0 || k > maxDecompressedLength)
/* 176 */               throw new DecompressionException(String.format("invalid decompressedLength: %d (expected: 0-%d)", new Object[] {
/*     */                       
/* 178 */                       Integer.valueOf(k), Integer.valueOf(maxDecompressedLength)
/*     */                     })); 
/* 180 */             if ((k == 0 && j != 0) || (k != 0 && j == 0) || (i == 16 && k != j))
/*     */             {
/*     */               
/* 183 */               throw new DecompressionException(String.format("stream corrupted: compressedLength(%d) and decompressedLength(%d) mismatch", new Object[] {
/*     */                       
/* 185 */                       Integer.valueOf(j), Integer.valueOf(k)
/*     */                     }));
/*     */             }
/* 188 */             int m = Integer.reverseBytes(in.readInt());
/* 189 */             if (k == 0 && j == 0)
/* 190 */             { if (m != 0) {
/* 191 */                 throw new DecompressionException("stream corrupted: checksum error");
/*     */               }
/* 193 */               this.currentState = State.FINISHED;
/* 194 */               this.decompressor = null;
/* 195 */               this.checksum = null; }
/*     */             
/*     */             else
/*     */             
/* 199 */             { this.blockType = i;
/* 200 */               this.compressedLength = j;
/* 201 */               this.decompressedLength = k;
/* 202 */               this.currentChecksum = m;
/*     */               
/* 204 */               this.currentState = State.DECOMPRESS_DATA; } 
/*     */           }  return;
/*     */         case DECOMPRESS_DATA:
/* 207 */           blockType = this.blockType;
/* 208 */           compressedLength = this.compressedLength;
/* 209 */           decompressedLength = this.decompressedLength;
/* 210 */           currentChecksum = this.currentChecksum;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         case FINISHED:
/*     */         case CORRUPTED:
/* 258 */           in.skipBytes(in.readableBytes());
/*     */           return;
/*     */       } 
/* 261 */       throw new IllegalStateException();
/*     */     }
/* 263 */     catch (Exception e) {
/* 264 */       this.currentState = State.CORRUPTED;
/* 265 */       throw e;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isClosed() {
/* 274 */     return (this.currentState == State.FINISHED);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\compression\Lz4FrameDecoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */