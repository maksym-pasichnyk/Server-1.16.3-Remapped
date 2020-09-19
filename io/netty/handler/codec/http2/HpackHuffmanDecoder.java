/*     */ package io.netty.handler.codec.http2;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.util.AsciiString;
/*     */ import io.netty.util.ByteProcessor;
/*     */ import io.netty.util.internal.ObjectUtil;
/*     */ import io.netty.util.internal.ThrowableUtil;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class HpackHuffmanDecoder
/*     */ {
/*  45 */   private static final Http2Exception EOS_DECODED = (Http2Exception)ThrowableUtil.unknownStackTrace(
/*  46 */       Http2Exception.connectionError(Http2Error.COMPRESSION_ERROR, "HPACK - EOS Decoded", new Object[0]), HpackHuffmanDecoder.class, "decode(..)");
/*  47 */   private static final Http2Exception INVALID_PADDING = (Http2Exception)ThrowableUtil.unknownStackTrace(
/*  48 */       Http2Exception.connectionError(Http2Error.COMPRESSION_ERROR, "HPACK - Invalid Padding", new Object[0]), HpackHuffmanDecoder.class, "decode(..)");
/*     */   
/*  50 */   private static final Node ROOT = buildTree(HpackUtil.HUFFMAN_CODES, HpackUtil.HUFFMAN_CODE_LENGTHS);
/*     */   
/*     */   private final DecoderProcessor processor;
/*     */   
/*     */   HpackHuffmanDecoder(int initialCapacity) {
/*  55 */     this.processor = new DecoderProcessor(initialCapacity);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AsciiString decode(ByteBuf buf, int length) throws Http2Exception {
/*  66 */     this.processor.reset();
/*  67 */     buf.forEachByte(buf.readerIndex(), length, this.processor);
/*  68 */     buf.skipBytes(length);
/*  69 */     return this.processor.end();
/*     */   }
/*     */ 
/*     */   
/*     */   private static final class Node
/*     */   {
/*     */     private final int symbol;
/*     */     
/*     */     private final int bits;
/*     */     
/*     */     private final Node[] children;
/*     */     
/*     */     Node() {
/*  82 */       this.symbol = 0;
/*  83 */       this.bits = 8;
/*  84 */       this.children = new Node[256];
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     Node(int symbol, int bits) {
/*  94 */       assert bits > 0 && bits <= 8;
/*  95 */       this.symbol = symbol;
/*  96 */       this.bits = bits;
/*  97 */       this.children = null;
/*     */     }
/*     */     
/*     */     private boolean isTerminal() {
/* 101 */       return (this.children == null);
/*     */     }
/*     */   }
/*     */   
/*     */   private static Node buildTree(int[] codes, byte[] lengths) {
/* 106 */     Node root = new Node();
/* 107 */     for (int i = 0; i < codes.length; i++) {
/* 108 */       insert(root, i, codes[i], lengths[i]);
/*     */     }
/* 110 */     return root;
/*     */   }
/*     */ 
/*     */   
/*     */   private static void insert(Node root, int symbol, int code, byte length) {
/* 115 */     Node current = root;
/* 116 */     while (length > 8) {
/* 117 */       if (current.isTerminal()) {
/* 118 */         throw new IllegalStateException("invalid Huffman code: prefix not unique");
/*     */       }
/* 120 */       length = (byte)(length - 8);
/* 121 */       int j = code >>> length & 0xFF;
/* 122 */       if (current.children[j] == null) {
/* 123 */         current.children[j] = new Node();
/*     */       }
/* 125 */       current = current.children[j];
/*     */     } 
/*     */     
/* 128 */     Node terminal = new Node(symbol, length);
/* 129 */     int shift = 8 - length;
/* 130 */     int start = code << shift & 0xFF;
/* 131 */     int end = 1 << shift;
/* 132 */     for (int i = start; i < start + end; i++)
/* 133 */       current.children[i] = terminal; 
/*     */   }
/*     */   
/*     */   private static final class DecoderProcessor
/*     */     implements ByteProcessor {
/*     */     private final int initialCapacity;
/*     */     private byte[] bytes;
/*     */     private int index;
/*     */     private HpackHuffmanDecoder.Node node;
/*     */     private int current;
/*     */     private int currentBits;
/*     */     private int symbolBits;
/*     */     
/*     */     DecoderProcessor(int initialCapacity) {
/* 147 */       this.initialCapacity = ObjectUtil.checkPositive(initialCapacity, "initialCapacity");
/*     */     }
/*     */     
/*     */     void reset() {
/* 151 */       this.node = HpackHuffmanDecoder.ROOT;
/* 152 */       this.current = 0;
/* 153 */       this.currentBits = 0;
/* 154 */       this.symbolBits = 0;
/* 155 */       this.bytes = new byte[this.initialCapacity];
/* 156 */       this.index = 0;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean process(byte value) throws Http2Exception {
/* 179 */       this.current = this.current << 8 | value & 0xFF;
/* 180 */       this.currentBits += 8;
/* 181 */       this.symbolBits += 8;
/*     */       
/*     */       while (true) {
/* 184 */         this.node = this.node.children[this.current >>> this.currentBits - 8 & 0xFF];
/* 185 */         this.currentBits -= this.node.bits;
/* 186 */         if (this.node.isTerminal()) {
/* 187 */           if (this.node.symbol == 256) {
/* 188 */             throw HpackHuffmanDecoder.EOS_DECODED;
/*     */           }
/* 190 */           append(this.node.symbol);
/* 191 */           this.node = HpackHuffmanDecoder.ROOT;
/*     */ 
/*     */           
/* 194 */           this.symbolBits = this.currentBits;
/*     */         } 
/* 196 */         if (this.currentBits < 8) {
/* 197 */           return true;
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     AsciiString end() throws Http2Exception {
/* 205 */       while (this.currentBits > 0) {
/* 206 */         this.node = this.node.children[this.current << 8 - this.currentBits & 0xFF];
/* 207 */         if (this.node.isTerminal() && this.node.bits <= this.currentBits) {
/* 208 */           if (this.node.symbol == 256) {
/* 209 */             throw HpackHuffmanDecoder.EOS_DECODED;
/*     */           }
/* 211 */           this.currentBits -= this.node.bits;
/* 212 */           append(this.node.symbol);
/* 213 */           this.node = HpackHuffmanDecoder.ROOT;
/* 214 */           this.symbolBits = this.currentBits;
/*     */         } 
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 224 */       int mask = (1 << this.symbolBits) - 1;
/* 225 */       if (this.symbolBits > 7 || (this.current & mask) != mask) {
/* 226 */         throw HpackHuffmanDecoder.INVALID_PADDING;
/*     */       }
/*     */       
/* 229 */       return new AsciiString(this.bytes, 0, this.index, false);
/*     */     }
/*     */     
/*     */     private void append(int i) {
/* 233 */       if (this.bytes.length == this.index) {
/*     */ 
/*     */ 
/*     */         
/* 237 */         int newLength = (this.bytes.length >= 1024) ? (this.bytes.length + this.initialCapacity) : (this.bytes.length << 1);
/* 238 */         byte[] newBytes = new byte[newLength];
/* 239 */         System.arraycopy(this.bytes, 0, newBytes, 0, this.bytes.length);
/* 240 */         this.bytes = newBytes;
/*     */       } 
/* 242 */       this.bytes[this.index++] = (byte)i;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\http2\HpackHuffmanDecoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */