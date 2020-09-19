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
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class SocksCmdResponse
/*     */   extends SocksResponse
/*     */ {
/*     */   private final SocksCmdStatus cmdStatus;
/*     */   private final SocksAddressType addressType;
/*     */   private final String host;
/*     */   private final int port;
/*  38 */   private static final byte[] DOMAIN_ZEROED = new byte[] { 0 };
/*  39 */   private static final byte[] IPv4_HOSTNAME_ZEROED = new byte[] { 0, 0, 0, 0 };
/*  40 */   private static final byte[] IPv6_HOSTNAME_ZEROED = new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SocksCmdResponse(SocksCmdStatus cmdStatus, SocksAddressType addressType) {
/*  46 */     this(cmdStatus, addressType, null, 0);
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
/*     */   public SocksCmdResponse(SocksCmdStatus cmdStatus, SocksAddressType addressType, String host, int port) {
/*  63 */     super(SocksResponseType.CMD);
/*  64 */     if (cmdStatus == null) {
/*  65 */       throw new NullPointerException("cmdStatus");
/*     */     }
/*  67 */     if (addressType == null) {
/*  68 */       throw new NullPointerException("addressType");
/*     */     }
/*  70 */     if (host != null) {
/*  71 */       String asciiHost; switch (addressType) {
/*     */         case IPv4:
/*  73 */           if (!NetUtil.isValidIpV4Address(host)) {
/*  74 */             throw new IllegalArgumentException(host + " is not a valid IPv4 address");
/*     */           }
/*     */           break;
/*     */         case DOMAIN:
/*  78 */           asciiHost = IDN.toASCII(host);
/*  79 */           if (asciiHost.length() > 255) {
/*  80 */             throw new IllegalArgumentException(host + " IDN: " + asciiHost + " exceeds 255 char limit");
/*     */           }
/*  82 */           host = asciiHost;
/*     */           break;
/*     */         case IPv6:
/*  85 */           if (!NetUtil.isValidIpV6Address(host)) {
/*  86 */             throw new IllegalArgumentException(host + " is not a valid IPv6 address");
/*     */           }
/*     */           break;
/*     */       } 
/*     */ 
/*     */     
/*     */     } 
/*  93 */     if (port < 0 || port > 65535) {
/*  94 */       throw new IllegalArgumentException(port + " is not in bounds 0 <= x <= 65535");
/*     */     }
/*  96 */     this.cmdStatus = cmdStatus;
/*  97 */     this.addressType = addressType;
/*  98 */     this.host = host;
/*  99 */     this.port = port;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SocksCmdStatus cmdStatus() {
/* 108 */     return this.cmdStatus;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SocksAddressType addressType() {
/* 117 */     return this.addressType;
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
/*     */   public String host() {
/* 129 */     return (this.host != null && this.addressType == SocksAddressType.DOMAIN) ? IDN.toUnicode(this.host) : this.host;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int port() {
/* 139 */     return this.port;
/*     */   }
/*     */   
/*     */   public void encodeAsByteBuf(ByteBuf byteBuf) {
/*     */     byte[] hostContent;
/* 144 */     byteBuf.writeByte(protocolVersion().byteValue());
/* 145 */     byteBuf.writeByte(this.cmdStatus.byteValue());
/* 146 */     byteBuf.writeByte(0);
/* 147 */     byteBuf.writeByte(this.addressType.byteValue());
/* 148 */     switch (this.addressType) {
/*     */       
/*     */       case IPv4:
/* 151 */         hostContent = (this.host == null) ? IPv4_HOSTNAME_ZEROED : NetUtil.createByteArrayFromIpAddressString(this.host);
/* 152 */         byteBuf.writeBytes(hostContent);
/* 153 */         byteBuf.writeShort(this.port);
/*     */         break;
/*     */       
/*     */       case DOMAIN:
/* 157 */         if (this.host != null) {
/* 158 */           byteBuf.writeByte(this.host.length());
/* 159 */           byteBuf.writeCharSequence(this.host, CharsetUtil.US_ASCII);
/*     */         } else {
/* 161 */           byteBuf.writeByte(DOMAIN_ZEROED.length);
/* 162 */           byteBuf.writeBytes(DOMAIN_ZEROED);
/*     */         } 
/* 164 */         byteBuf.writeShort(this.port);
/*     */         break;
/*     */ 
/*     */       
/*     */       case IPv6:
/* 169 */         hostContent = (this.host == null) ? IPv6_HOSTNAME_ZEROED : NetUtil.createByteArrayFromIpAddressString(this.host);
/* 170 */         byteBuf.writeBytes(hostContent);
/* 171 */         byteBuf.writeShort(this.port);
/*     */         break;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\socks\SocksCmdResponse.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */