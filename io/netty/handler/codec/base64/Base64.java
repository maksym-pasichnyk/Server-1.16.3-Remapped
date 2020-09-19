/*     */ package io.netty.handler.codec.base64;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufAllocator;
/*     */ import io.netty.util.ByteProcessor;
/*     */ import io.netty.util.internal.PlatformDependent;
/*     */ import java.nio.ByteOrder;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Base64
/*     */ {
/*     */   private static final int MAX_LINE_LENGTH = 76;
/*     */   private static final byte EQUALS_SIGN = 61;
/*     */   private static final byte NEW_LINE = 10;
/*     */   private static final byte WHITE_SPACE_ENC = -5;
/*     */   private static final byte EQUALS_SIGN_ENC = -1;
/*     */   
/*     */   private static byte[] alphabet(Base64Dialect dialect) {
/*  53 */     if (dialect == null) {
/*  54 */       throw new NullPointerException("dialect");
/*     */     }
/*  56 */     return dialect.alphabet;
/*     */   }
/*     */   
/*     */   private static byte[] decodabet(Base64Dialect dialect) {
/*  60 */     if (dialect == null) {
/*  61 */       throw new NullPointerException("dialect");
/*     */     }
/*  63 */     return dialect.decodabet;
/*     */   }
/*     */   
/*     */   private static boolean breakLines(Base64Dialect dialect) {
/*  67 */     if (dialect == null) {
/*  68 */       throw new NullPointerException("dialect");
/*     */     }
/*  70 */     return dialect.breakLinesByDefault;
/*     */   }
/*     */   
/*     */   public static ByteBuf encode(ByteBuf src) {
/*  74 */     return encode(src, Base64Dialect.STANDARD);
/*     */   }
/*     */   
/*     */   public static ByteBuf encode(ByteBuf src, Base64Dialect dialect) {
/*  78 */     return encode(src, breakLines(dialect), dialect);
/*     */   }
/*     */   
/*     */   public static ByteBuf encode(ByteBuf src, boolean breakLines) {
/*  82 */     return encode(src, breakLines, Base64Dialect.STANDARD);
/*     */   }
/*     */ 
/*     */   
/*     */   public static ByteBuf encode(ByteBuf src, boolean breakLines, Base64Dialect dialect) {
/*  87 */     if (src == null) {
/*  88 */       throw new NullPointerException("src");
/*     */     }
/*     */     
/*  91 */     ByteBuf dest = encode(src, src.readerIndex(), src.readableBytes(), breakLines, dialect);
/*  92 */     src.readerIndex(src.writerIndex());
/*  93 */     return dest;
/*     */   }
/*     */   
/*     */   public static ByteBuf encode(ByteBuf src, int off, int len) {
/*  97 */     return encode(src, off, len, Base64Dialect.STANDARD);
/*     */   }
/*     */   
/*     */   public static ByteBuf encode(ByteBuf src, int off, int len, Base64Dialect dialect) {
/* 101 */     return encode(src, off, len, breakLines(dialect), dialect);
/*     */   }
/*     */ 
/*     */   
/*     */   public static ByteBuf encode(ByteBuf src, int off, int len, boolean breakLines) {
/* 106 */     return encode(src, off, len, breakLines, Base64Dialect.STANDARD);
/*     */   }
/*     */ 
/*     */   
/*     */   public static ByteBuf encode(ByteBuf src, int off, int len, boolean breakLines, Base64Dialect dialect) {
/* 111 */     return encode(src, off, len, breakLines, dialect, src.alloc());
/*     */   }
/*     */ 
/*     */   
/*     */   public static ByteBuf encode(ByteBuf src, int off, int len, boolean breakLines, Base64Dialect dialect, ByteBufAllocator allocator) {
/* 116 */     if (src == null) {
/* 117 */       throw new NullPointerException("src");
/*     */     }
/* 119 */     if (dialect == null) {
/* 120 */       throw new NullPointerException("dialect");
/*     */     }
/*     */     
/* 123 */     ByteBuf dest = allocator.buffer(encodedBufferSize(len, breakLines)).order(src.order());
/* 124 */     byte[] alphabet = alphabet(dialect);
/* 125 */     int d = 0;
/* 126 */     int e = 0;
/* 127 */     int len2 = len - 2;
/* 128 */     int lineLength = 0;
/* 129 */     for (; d < len2; d += 3, e += 4) {
/* 130 */       encode3to4(src, d + off, 3, dest, e, alphabet);
/*     */       
/* 132 */       lineLength += 4;
/*     */       
/* 134 */       if (breakLines && lineLength == 76) {
/* 135 */         dest.setByte(e + 4, 10);
/* 136 */         e++;
/* 137 */         lineLength = 0;
/*     */       } 
/*     */     } 
/*     */     
/* 141 */     if (d < len) {
/* 142 */       encode3to4(src, d + off, len - d, dest, e, alphabet);
/* 143 */       e += 4;
/*     */     } 
/*     */ 
/*     */     
/* 147 */     if (e > 1 && dest.getByte(e - 1) == 10) {
/* 148 */       e--;
/*     */     }
/*     */     
/* 151 */     return dest.slice(0, e);
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
/*     */   private static void encode3to4(ByteBuf src, int srcOffset, int numSigBytes, ByteBuf dest, int destOffset, byte[] alphabet) {
/* 167 */     if (src.order() == ByteOrder.BIG_ENDIAN) {
/*     */       int inBuff;
/* 169 */       switch (numSigBytes) {
/*     */         case 1:
/* 171 */           inBuff = toInt(src.getByte(srcOffset));
/*     */           break;
/*     */         case 2:
/* 174 */           inBuff = toIntBE(src.getShort(srcOffset));
/*     */           break;
/*     */         default:
/* 177 */           inBuff = (numSigBytes <= 0) ? 0 : toIntBE(src.getMedium(srcOffset));
/*     */           break;
/*     */       } 
/* 180 */       encode3to4BigEndian(inBuff, numSigBytes, dest, destOffset, alphabet);
/*     */     } else {
/*     */       int inBuff;
/* 183 */       switch (numSigBytes) {
/*     */         case 1:
/* 185 */           inBuff = toInt(src.getByte(srcOffset));
/*     */           break;
/*     */         case 2:
/* 188 */           inBuff = toIntLE(src.getShort(srcOffset));
/*     */           break;
/*     */         default:
/* 191 */           inBuff = (numSigBytes <= 0) ? 0 : toIntLE(src.getMedium(srcOffset));
/*     */           break;
/*     */       } 
/* 194 */       encode3to4LittleEndian(inBuff, numSigBytes, dest, destOffset, alphabet);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static int encodedBufferSize(int len, boolean breakLines) {
/* 201 */     long len43 = (len << 2L) / 3L;
/*     */ 
/*     */     
/* 204 */     long ret = len43 + 3L & 0xFFFFFFFFFFFFFFFCL;
/*     */     
/* 206 */     if (breakLines) {
/* 207 */       ret += len43 / 76L;
/*     */     }
/*     */     
/* 210 */     return (ret < 2147483647L) ? (int)ret : Integer.MAX_VALUE;
/*     */   }
/*     */   
/*     */   private static int toInt(byte value) {
/* 214 */     return (value & 0xFF) << 16;
/*     */   }
/*     */   
/*     */   private static int toIntBE(short value) {
/* 218 */     return (value & 0xFF00) << 8 | (value & 0xFF) << 8;
/*     */   }
/*     */   
/*     */   private static int toIntLE(short value) {
/* 222 */     return (value & 0xFF) << 16 | value & 0xFF00;
/*     */   }
/*     */   
/*     */   private static int toIntBE(int mediumValue) {
/* 226 */     return mediumValue & 0xFF0000 | mediumValue & 0xFF00 | mediumValue & 0xFF;
/*     */   }
/*     */   
/*     */   private static int toIntLE(int mediumValue) {
/* 230 */     return (mediumValue & 0xFF) << 16 | mediumValue & 0xFF00 | (mediumValue & 0xFF0000) >>> 16;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static void encode3to4BigEndian(int inBuff, int numSigBytes, ByteBuf dest, int destOffset, byte[] alphabet) {
/* 236 */     switch (numSigBytes) {
/*     */       case 3:
/* 238 */         dest.setInt(destOffset, alphabet[inBuff >>> 18] << 24 | alphabet[inBuff >>> 12 & 0x3F] << 16 | alphabet[inBuff >>> 6 & 0x3F] << 8 | alphabet[inBuff & 0x3F]);
/*     */         break;
/*     */ 
/*     */ 
/*     */       
/*     */       case 2:
/* 244 */         dest.setInt(destOffset, alphabet[inBuff >>> 18] << 24 | alphabet[inBuff >>> 12 & 0x3F] << 16 | alphabet[inBuff >>> 6 & 0x3F] << 8 | 0x3D);
/*     */         break;
/*     */ 
/*     */ 
/*     */       
/*     */       case 1:
/* 250 */         dest.setInt(destOffset, alphabet[inBuff >>> 18] << 24 | alphabet[inBuff >>> 12 & 0x3F] << 16 | 0x3D00 | 0x3D);
/*     */         break;
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
/*     */   
/*     */   private static void encode3to4LittleEndian(int inBuff, int numSigBytes, ByteBuf dest, int destOffset, byte[] alphabet) {
/* 264 */     switch (numSigBytes) {
/*     */       case 3:
/* 266 */         dest.setInt(destOffset, alphabet[inBuff >>> 18] | alphabet[inBuff >>> 12 & 0x3F] << 8 | alphabet[inBuff >>> 6 & 0x3F] << 16 | alphabet[inBuff & 0x3F] << 24);
/*     */         break;
/*     */ 
/*     */ 
/*     */       
/*     */       case 2:
/* 272 */         dest.setInt(destOffset, alphabet[inBuff >>> 18] | alphabet[inBuff >>> 12 & 0x3F] << 8 | alphabet[inBuff >>> 6 & 0x3F] << 16 | 0x3D000000);
/*     */         break;
/*     */ 
/*     */ 
/*     */       
/*     */       case 1:
/* 278 */         dest.setInt(destOffset, alphabet[inBuff >>> 18] | alphabet[inBuff >>> 12 & 0x3F] << 8 | 0x3D0000 | 0x3D000000);
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ByteBuf decode(ByteBuf src) {
/* 290 */     return decode(src, Base64Dialect.STANDARD);
/*     */   }
/*     */   
/*     */   public static ByteBuf decode(ByteBuf src, Base64Dialect dialect) {
/* 294 */     if (src == null) {
/* 295 */       throw new NullPointerException("src");
/*     */     }
/*     */     
/* 298 */     ByteBuf dest = decode(src, src.readerIndex(), src.readableBytes(), dialect);
/* 299 */     src.readerIndex(src.writerIndex());
/* 300 */     return dest;
/*     */   }
/*     */ 
/*     */   
/*     */   public static ByteBuf decode(ByteBuf src, int off, int len) {
/* 305 */     return decode(src, off, len, Base64Dialect.STANDARD);
/*     */   }
/*     */ 
/*     */   
/*     */   public static ByteBuf decode(ByteBuf src, int off, int len, Base64Dialect dialect) {
/* 310 */     return decode(src, off, len, dialect, src.alloc());
/*     */   }
/*     */ 
/*     */   
/*     */   public static ByteBuf decode(ByteBuf src, int off, int len, Base64Dialect dialect, ByteBufAllocator allocator) {
/* 315 */     if (src == null) {
/* 316 */       throw new NullPointerException("src");
/*     */     }
/* 318 */     if (dialect == null) {
/* 319 */       throw new NullPointerException("dialect");
/*     */     }
/*     */ 
/*     */     
/* 323 */     return (new Decoder()).decode(src, off, len, allocator, dialect);
/*     */   }
/*     */ 
/*     */   
/*     */   static int decodedBufferSize(int len) {
/* 328 */     return len - (len >>> 2);
/*     */   }
/*     */   
/*     */   private static final class Decoder implements ByteProcessor {
/* 332 */     private final byte[] b4 = new byte[4];
/*     */     private int b4Posn;
/*     */     private byte sbiCrop;
/*     */     private byte sbiDecode;
/*     */     private byte[] decodabet;
/*     */     private int outBuffPosn;
/*     */     private ByteBuf dest;
/*     */     
/*     */     ByteBuf decode(ByteBuf src, int off, int len, ByteBufAllocator allocator, Base64Dialect dialect) {
/* 341 */       this.dest = allocator.buffer(Base64.decodedBufferSize(len)).order(src.order());
/*     */       
/* 343 */       this.decodabet = Base64.decodabet(dialect);
/*     */       try {
/* 345 */         src.forEachByte(off, len, this);
/* 346 */         return this.dest.slice(0, this.outBuffPosn);
/* 347 */       } catch (Throwable cause) {
/* 348 */         this.dest.release();
/* 349 */         PlatformDependent.throwException(cause);
/* 350 */         return null;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean process(byte value) throws Exception {
/* 356 */       this.sbiCrop = (byte)(value & Byte.MAX_VALUE);
/* 357 */       this.sbiDecode = this.decodabet[this.sbiCrop];
/*     */       
/* 359 */       if (this.sbiDecode >= -5) {
/* 360 */         if (this.sbiDecode >= -1) {
/* 361 */           this.b4[this.b4Posn++] = this.sbiCrop;
/* 362 */           if (this.b4Posn > 3) {
/* 363 */             this.outBuffPosn += decode4to3(this.b4, this.dest, this.outBuffPosn, this.decodabet);
/* 364 */             this.b4Posn = 0;
/*     */ 
/*     */             
/* 367 */             if (this.sbiCrop == 61) {
/* 368 */               return false;
/*     */             }
/*     */           } 
/*     */         } 
/* 372 */         return true;
/*     */       } 
/* 374 */       throw new IllegalArgumentException("invalid bad Base64 input character: " + (short)(value & 0xFF) + " (decimal)");
/*     */     }
/*     */     
/*     */     private static int decode4to3(byte[] src, ByteBuf dest, int destOffset, byte[] decodabet) {
/*     */       int decodedValue;
/* 379 */       byte src0 = src[0];
/* 380 */       byte src1 = src[1];
/* 381 */       byte src2 = src[2];
/*     */       
/* 383 */       if (src2 == 61) {
/*     */         
/*     */         try {
/* 386 */           decodedValue = (decodabet[src0] & 0xFF) << 2 | (decodabet[src1] & 0xFF) >>> 4;
/* 387 */         } catch (IndexOutOfBoundsException ignored) {
/* 388 */           throw new IllegalArgumentException("not encoded in Base64");
/*     */         } 
/* 390 */         dest.setByte(destOffset, decodedValue);
/* 391 */         return 1;
/*     */       } 
/*     */       
/* 394 */       byte src3 = src[3];
/* 395 */       if (src3 == 61) {
/*     */         
/* 397 */         byte b1 = decodabet[src1];
/*     */         
/*     */         try {
/* 400 */           if (dest.order() == ByteOrder.BIG_ENDIAN)
/*     */           {
/*     */             
/* 403 */             decodedValue = ((decodabet[src0] & 0x3F) << 2 | (b1 & 0xF0) >> 4) << 8 | (b1 & 0xF) << 4 | (decodabet[src2] & 0xFC) >>> 2;
/*     */           }
/*     */           else
/*     */           {
/* 407 */             decodedValue = (decodabet[src0] & 0x3F) << 2 | (b1 & 0xF0) >> 4 | ((b1 & 0xF) << 4 | (decodabet[src2] & 0xFC) >>> 2) << 8;
/*     */           }
/*     */         
/* 410 */         } catch (IndexOutOfBoundsException ignored) {
/* 411 */           throw new IllegalArgumentException("not encoded in Base64");
/*     */         } 
/* 413 */         dest.setShort(destOffset, decodedValue);
/* 414 */         return 2;
/*     */       } 
/*     */ 
/*     */       
/*     */       try {
/* 419 */         if (dest.order() == ByteOrder.BIG_ENDIAN) {
/* 420 */           decodedValue = (decodabet[src0] & 0x3F) << 18 | (decodabet[src1] & 0xFF) << 12 | (decodabet[src2] & 0xFF) << 6 | decodabet[src3] & 0xFF;
/*     */         
/*     */         }
/*     */         else {
/*     */           
/* 425 */           byte b1 = decodabet[src1];
/* 426 */           byte b2 = decodabet[src2];
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 432 */           decodedValue = (decodabet[src0] & 0x3F) << 2 | (b1 & 0xF) << 12 | (b1 & 0xF0) >>> 4 | (b2 & 0x3) << 22 | (b2 & 0xFC) << 6 | (decodabet[src3] & 0xFF) << 16;
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       }
/* 443 */       catch (IndexOutOfBoundsException ignored) {
/* 444 */         throw new IllegalArgumentException("not encoded in Base64");
/*     */       } 
/* 446 */       dest.setMedium(destOffset, decodedValue);
/* 447 */       return 3;
/*     */     }
/*     */     
/*     */     private Decoder() {}
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\base64\Base64.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */