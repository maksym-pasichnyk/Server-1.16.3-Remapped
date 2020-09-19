/*     */ package io.netty.handler.ssl;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufAllocator;
/*     */ import io.netty.buffer.ByteBufUtil;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.handler.codec.base64.Base64;
/*     */ import io.netty.handler.codec.base64.Base64Dialect;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.ByteOrder;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import javax.net.ssl.SSLHandshakeException;
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
/*     */ final class SslUtils
/*     */ {
/*     */   static final String PROTOCOL_SSL_V2_HELLO = "SSLv2Hello";
/*     */   static final String PROTOCOL_SSL_V2 = "SSLv2";
/*     */   static final String PROTOCOL_SSL_V3 = "SSLv3";
/*     */   static final String PROTOCOL_TLS_V1 = "TLSv1";
/*     */   static final String PROTOCOL_TLS_V1_1 = "TLSv1.1";
/*     */   static final String PROTOCOL_TLS_V1_2 = "TLSv1.2";
/*     */   static final int SSL_CONTENT_TYPE_CHANGE_CIPHER_SPEC = 20;
/*     */   static final int SSL_CONTENT_TYPE_ALERT = 21;
/*     */   static final int SSL_CONTENT_TYPE_HANDSHAKE = 22;
/*     */   static final int SSL_CONTENT_TYPE_APPLICATION_DATA = 23;
/*     */   static final int SSL_CONTENT_TYPE_EXTENSION_HEARTBEAT = 24;
/*     */   static final int SSL_RECORD_HEADER_LENGTH = 5;
/*     */   static final int NOT_ENOUGH_DATA = -1;
/*     */   static final int NOT_ENCRYPTED = -2;
/*  87 */   static final String[] DEFAULT_CIPHER_SUITES = new String[] { "TLS_ECDHE_ECDSA_WITH_AES_256_GCM_SHA384", "TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256", "TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256", "TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA", "TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA", "TLS_RSA_WITH_AES_128_GCM_SHA256", "TLS_RSA_WITH_AES_128_CBC_SHA", "TLS_RSA_WITH_AES_256_CBC_SHA" };
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
/*     */   static void addIfSupported(Set<String> supported, List<String> enabled, String... names) {
/* 106 */     for (String n : names) {
/* 107 */       if (supported.contains(n)) {
/* 108 */         enabled.add(n);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   static void useFallbackCiphersIfDefaultIsEmpty(List<String> defaultCiphers, Iterable<String> fallbackCiphers) {
/* 114 */     if (defaultCiphers.isEmpty()) {
/* 115 */       for (String cipher : fallbackCiphers) {
/* 116 */         if (cipher.startsWith("SSL_") || cipher.contains("_RC4_")) {
/*     */           continue;
/*     */         }
/* 119 */         defaultCiphers.add(cipher);
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   static void useFallbackCiphersIfDefaultIsEmpty(List<String> defaultCiphers, String... fallbackCiphers) {
/* 125 */     useFallbackCiphersIfDefaultIsEmpty(defaultCiphers, Arrays.asList(fallbackCiphers));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static SSLHandshakeException toSSLHandshakeException(Throwable e) {
/* 132 */     if (e instanceof SSLHandshakeException) {
/* 133 */       return (SSLHandshakeException)e;
/*     */     }
/*     */     
/* 136 */     return (SSLHandshakeException)(new SSLHandshakeException(e.getMessage())).initCause(e);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static int getEncryptedPacketLength(ByteBuf buffer, int offset) {
/*     */     boolean tls;
/* 157 */     int packetLength = 0;
/*     */ 
/*     */ 
/*     */     
/* 161 */     switch (buffer.getUnsignedByte(offset)) {
/*     */       case 20:
/*     */       case 21:
/*     */       case 22:
/*     */       case 23:
/*     */       case 24:
/* 167 */         tls = true;
/*     */         break;
/*     */       
/*     */       default:
/* 171 */         tls = false;
/*     */         break;
/*     */     } 
/* 174 */     if (tls) {
/*     */       
/* 176 */       int majorVersion = buffer.getUnsignedByte(offset + 1);
/* 177 */       if (majorVersion == 3) {
/*     */         
/* 179 */         packetLength = unsignedShortBE(buffer, offset + 3) + 5;
/* 180 */         if (packetLength <= 5)
/*     */         {
/* 182 */           tls = false;
/*     */         }
/*     */       } else {
/*     */         
/* 186 */         tls = false;
/*     */       } 
/*     */     } 
/*     */     
/* 190 */     if (!tls) {
/*     */       
/* 192 */       int headerLength = ((buffer.getUnsignedByte(offset) & 0x80) != 0) ? 2 : 3;
/* 193 */       int majorVersion = buffer.getUnsignedByte(offset + headerLength + 1);
/* 194 */       if (majorVersion == 2 || majorVersion == 3) {
/*     */ 
/*     */         
/* 197 */         packetLength = (headerLength == 2) ? ((shortBE(buffer, offset) & Short.MAX_VALUE) + 2) : ((shortBE(buffer, offset) & 0x3FFF) + 3);
/* 198 */         if (packetLength <= headerLength) {
/* 199 */           return -1;
/*     */         }
/*     */       } else {
/* 202 */         return -2;
/*     */       } 
/*     */     } 
/* 205 */     return packetLength;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static int unsignedShortBE(ByteBuf buffer, int offset) {
/* 211 */     return (buffer.order() == ByteOrder.BIG_ENDIAN) ? buffer
/* 212 */       .getUnsignedShort(offset) : buffer.getUnsignedShortLE(offset);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static short shortBE(ByteBuf buffer, int offset) {
/* 218 */     return (buffer.order() == ByteOrder.BIG_ENDIAN) ? buffer
/* 219 */       .getShort(offset) : buffer.getShortLE(offset);
/*     */   }
/*     */   
/*     */   private static short unsignedByte(byte b) {
/* 223 */     return (short)(b & 0xFF);
/*     */   }
/*     */ 
/*     */   
/*     */   private static int unsignedShortBE(ByteBuffer buffer, int offset) {
/* 228 */     return shortBE(buffer, offset) & 0xFFFF;
/*     */   }
/*     */ 
/*     */   
/*     */   private static short shortBE(ByteBuffer buffer, int offset) {
/* 233 */     return (buffer.order() == ByteOrder.BIG_ENDIAN) ? buffer
/* 234 */       .getShort(offset) : ByteBufUtil.swapShort(buffer.getShort(offset));
/*     */   }
/*     */   
/*     */   static int getEncryptedPacketLength(ByteBuffer[] buffers, int offset) {
/* 238 */     ByteBuffer buffer = buffers[offset];
/*     */ 
/*     */     
/* 241 */     if (buffer.remaining() >= 5) {
/* 242 */       return getEncryptedPacketLength(buffer);
/*     */     }
/*     */ 
/*     */     
/* 246 */     ByteBuffer tmp = ByteBuffer.allocate(5);
/*     */     
/*     */     do {
/* 249 */       buffer = buffers[offset++].duplicate();
/* 250 */       if (buffer.remaining() > tmp.remaining()) {
/* 251 */         buffer.limit(buffer.position() + tmp.remaining());
/*     */       }
/* 253 */       tmp.put(buffer);
/* 254 */     } while (tmp.hasRemaining());
/*     */ 
/*     */     
/* 257 */     tmp.flip();
/* 258 */     return getEncryptedPacketLength(tmp);
/*     */   }
/*     */   private static int getEncryptedPacketLength(ByteBuffer buffer) {
/*     */     boolean tls;
/* 262 */     int packetLength = 0;
/* 263 */     int pos = buffer.position();
/*     */ 
/*     */     
/* 266 */     switch (unsignedByte(buffer.get(pos))) {
/*     */       case 20:
/*     */       case 21:
/*     */       case 22:
/*     */       case 23:
/*     */       case 24:
/* 272 */         tls = true;
/*     */         break;
/*     */       
/*     */       default:
/* 276 */         tls = false;
/*     */         break;
/*     */     } 
/* 279 */     if (tls) {
/*     */       
/* 281 */       int majorVersion = unsignedByte(buffer.get(pos + 1));
/* 282 */       if (majorVersion == 3) {
/*     */         
/* 284 */         packetLength = unsignedShortBE(buffer, pos + 3) + 5;
/* 285 */         if (packetLength <= 5)
/*     */         {
/* 287 */           tls = false;
/*     */         }
/*     */       } else {
/*     */         
/* 291 */         tls = false;
/*     */       } 
/*     */     } 
/*     */     
/* 295 */     if (!tls) {
/*     */       
/* 297 */       int headerLength = ((unsignedByte(buffer.get(pos)) & 0x80) != 0) ? 2 : 3;
/* 298 */       int majorVersion = unsignedByte(buffer.get(pos + headerLength + 1));
/* 299 */       if (majorVersion == 2 || majorVersion == 3) {
/*     */ 
/*     */         
/* 302 */         packetLength = (headerLength == 2) ? ((shortBE(buffer, pos) & Short.MAX_VALUE) + 2) : ((shortBE(buffer, pos) & 0x3FFF) + 3);
/* 303 */         if (packetLength <= headerLength) {
/* 304 */           return -1;
/*     */         }
/*     */       } else {
/* 307 */         return -2;
/*     */       } 
/*     */     } 
/* 310 */     return packetLength;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static void handleHandshakeFailure(ChannelHandlerContext ctx, Throwable cause, boolean notify) {
/* 316 */     ctx.flush();
/* 317 */     if (notify) {
/* 318 */       ctx.fireUserEventTriggered(new SslHandshakeCompletionEvent(cause));
/*     */     }
/* 320 */     ctx.close();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static void zeroout(ByteBuf buffer) {
/* 327 */     if (!buffer.isReadOnly()) {
/* 328 */       buffer.setZero(0, buffer.capacity());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static void zerooutAndRelease(ByteBuf buffer) {
/* 336 */     zeroout(buffer);
/* 337 */     buffer.release();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static ByteBuf toBase64(ByteBufAllocator allocator, ByteBuf src) {
/* 346 */     ByteBuf dst = Base64.encode(src, src.readerIndex(), src
/* 347 */         .readableBytes(), true, Base64Dialect.STANDARD, allocator);
/* 348 */     src.readerIndex(src.writerIndex());
/* 349 */     return dst;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\ssl\SslUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */