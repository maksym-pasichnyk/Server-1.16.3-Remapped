/*     */ package io.netty.handler.codec.socks;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.util.CharsetUtil;
/*     */ import io.netty.util.NetUtil;
/*     */ import java.net.IDN;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class SocksCmdRequest
/*     */   extends SocksRequest
/*     */ {
/*     */   private final SocksCmdType cmdType;
/*     */   private final SocksAddressType addressType;
/*     */   private final String host;
/*     */   private final int port;
/*     */   
/*     */   public SocksCmdRequest(SocksCmdType cmdType, SocksAddressType addressType, String host, int port) {
/*  37 */     super(SocksRequestType.CMD); String asciiHost;
/*  38 */     if (cmdType == null) {
/*  39 */       throw new NullPointerException("cmdType");
/*     */     }
/*  41 */     if (addressType == null) {
/*  42 */       throw new NullPointerException("addressType");
/*     */     }
/*  44 */     if (host == null) {
/*  45 */       throw new NullPointerException("host");
/*     */     }
/*  47 */     switch (addressType) {
/*     */       case IPv4:
/*  49 */         if (!NetUtil.isValidIpV4Address(host)) {
/*  50 */           throw new IllegalArgumentException(host + " is not a valid IPv4 address");
/*     */         }
/*     */         break;
/*     */       case DOMAIN:
/*  54 */         asciiHost = IDN.toASCII(host);
/*  55 */         if (asciiHost.length() > 255) {
/*  56 */           throw new IllegalArgumentException(host + " IDN: " + asciiHost + " exceeds 255 char limit");
/*     */         }
/*  58 */         host = asciiHost;
/*     */         break;
/*     */       case IPv6:
/*  61 */         if (!NetUtil.isValidIpV6Address(host)) {
/*  62 */           throw new IllegalArgumentException(host + " is not a valid IPv6 address");
/*     */         }
/*     */         break;
/*     */     } 
/*     */ 
/*     */     
/*  68 */     if (port <= 0 || port >= 65536) {
/*  69 */       throw new IllegalArgumentException(port + " is not in bounds 0 < x < 65536");
/*     */     }
/*  71 */     this.cmdType = cmdType;
/*  72 */     this.addressType = addressType;
/*  73 */     this.host = host;
/*  74 */     this.port = port;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SocksCmdType cmdType() {
/*  83 */     return this.cmdType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SocksAddressType addressType() {
/*  92 */     return this.addressType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String host() {
/* 101 */     return (this.addressType == SocksAddressType.DOMAIN) ? IDN.toUnicode(this.host) : this.host;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int port() {
/* 110 */     return this.port;
/*     */   }
/*     */ 
/*     */   
/*     */   public void encodeAsByteBuf(ByteBuf byteBuf) {
/* 115 */     byteBuf.writeByte(protocolVersion().byteValue());
/* 116 */     byteBuf.writeByte(this.cmdType.byteValue());
/* 117 */     byteBuf.writeByte(0);
/* 118 */     byteBuf.writeByte(this.addressType.byteValue());
/* 119 */     switch (this.addressType) {
/*     */       case IPv4:
/* 121 */         byteBuf.writeBytes(NetUtil.createByteArrayFromIpAddressString(this.host));
/* 122 */         byteBuf.writeShort(this.port);
/*     */         break;
/*     */ 
/*     */       
/*     */       case DOMAIN:
/* 127 */         byteBuf.writeByte(this.host.length());
/* 128 */         byteBuf.writeCharSequence(this.host, CharsetUtil.US_ASCII);
/* 129 */         byteBuf.writeShort(this.port);
/*     */         break;
/*     */ 
/*     */       
/*     */       case IPv6:
/* 134 */         byteBuf.writeBytes(NetUtil.createByteArrayFromIpAddressString(this.host));
/* 135 */         byteBuf.writeShort(this.port);
/*     */         break;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\socks\SocksCmdRequest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */