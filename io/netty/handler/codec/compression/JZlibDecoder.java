/*     */ package io.netty.handler.codec.compression;
/*     */ 
/*     */ import com.jcraft.jzlib.Inflater;
/*     */ import com.jcraft.jzlib.JZlib;
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JZlibDecoder
/*     */   extends ZlibDecoder
/*     */ {
/*  27 */   private final Inflater z = new Inflater();
/*     */ 
/*     */   
/*     */   private byte[] dictionary;
/*     */ 
/*     */   
/*     */   private volatile boolean finished;
/*     */ 
/*     */   
/*     */   public JZlibDecoder() {
/*  37 */     this(ZlibWrapper.ZLIB);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JZlibDecoder(ZlibWrapper wrapper) {
/*  46 */     if (wrapper == null) {
/*  47 */       throw new NullPointerException("wrapper");
/*     */     }
/*     */     
/*  50 */     int resultCode = this.z.init(ZlibUtil.convertWrapperType(wrapper));
/*  51 */     if (resultCode != 0) {
/*  52 */       ZlibUtil.fail(this.z, "initialization failure", resultCode);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JZlibDecoder(byte[] dictionary) {
/*  64 */     if (dictionary == null) {
/*  65 */       throw new NullPointerException("dictionary");
/*     */     }
/*  67 */     this.dictionary = dictionary;
/*     */ 
/*     */     
/*  70 */     int resultCode = this.z.inflateInit(JZlib.W_ZLIB);
/*  71 */     if (resultCode != 0) {
/*  72 */       ZlibUtil.fail(this.z, "initialization failure", resultCode);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isClosed() {
/*  82 */     return this.finished;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
/*  87 */     if (this.finished) {
/*     */       
/*  89 */       in.skipBytes(in.readableBytes());
/*     */       
/*     */       return;
/*     */     } 
/*  93 */     int inputLength = in.readableBytes();
/*  94 */     if (inputLength == 0) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/*     */     try {
/* 100 */       this.z.avail_in = inputLength;
/* 101 */       if (in.hasArray()) {
/* 102 */         this.z.next_in = in.array();
/* 103 */         this.z.next_in_index = in.arrayOffset() + in.readerIndex();
/*     */       } else {
/* 105 */         byte[] array = new byte[inputLength];
/* 106 */         in.getBytes(in.readerIndex(), array);
/* 107 */         this.z.next_in = array;
/* 108 */         this.z.next_in_index = 0;
/*     */       } 
/* 110 */       int oldNextInIndex = this.z.next_in_index;
/*     */ 
/*     */       
/* 113 */       ByteBuf decompressed = ctx.alloc().heapBuffer(inputLength << 1);
/*     */       
/*     */       try {
/*     */         while (true) {
/* 117 */           decompressed.ensureWritable(this.z.avail_in << 1);
/* 118 */           this.z.avail_out = decompressed.writableBytes();
/* 119 */           this.z.next_out = decompressed.array();
/* 120 */           this.z.next_out_index = decompressed.arrayOffset() + decompressed.writerIndex();
/* 121 */           int oldNextOutIndex = this.z.next_out_index;
/*     */ 
/*     */           
/* 124 */           int resultCode = this.z.inflate(2);
/* 125 */           int outputLength = this.z.next_out_index - oldNextOutIndex;
/* 126 */           if (outputLength > 0) {
/* 127 */             decompressed.writerIndex(decompressed.writerIndex() + outputLength);
/*     */           }
/*     */           
/* 130 */           switch (resultCode) {
/*     */             case 2:
/* 132 */               if (this.dictionary == null) {
/* 133 */                 ZlibUtil.fail(this.z, "decompression failure", resultCode); continue;
/*     */               } 
/* 135 */               resultCode = this.z.inflateSetDictionary(this.dictionary, this.dictionary.length);
/* 136 */               if (resultCode != 0) {
/* 137 */                 ZlibUtil.fail(this.z, "failed to set the dictionary", resultCode);
/*     */               }
/*     */               continue;
/*     */             
/*     */             case 1:
/* 142 */               this.finished = true;
/* 143 */               this.z.inflateEnd();
/*     */               break;
/*     */             case 0:
/*     */               continue;
/*     */             case -5:
/* 148 */               if (this.z.avail_in <= 0) {
/*     */                 break;
/*     */               }
/*     */               continue;
/*     */           } 
/* 153 */           ZlibUtil.fail(this.z, "decompression failure", resultCode);
/*     */         } 
/*     */       } finally {
/*     */         
/* 157 */         in.skipBytes(this.z.next_in_index - oldNextInIndex);
/* 158 */         if (decompressed.isReadable()) {
/* 159 */           out.add(decompressed);
/*     */         } else {
/* 161 */           decompressed.release();
/*     */         }
/*     */       
/*     */       }
/*     */     
/*     */     }
/*     */     finally {
/*     */       
/* 169 */       this.z.next_in = null;
/* 170 */       this.z.next_out = null;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\compression\JZlibDecoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */