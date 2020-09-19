/*     */ package io.netty.handler.codec.haproxy;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public enum HAProxyProxiedProtocol
/*     */ {
/*  27 */   UNKNOWN((byte)0, AddressFamily.AF_UNSPEC, TransportProtocol.UNSPEC),
/*     */ 
/*     */ 
/*     */   
/*  31 */   TCP4((byte)17, AddressFamily.AF_IPv4, TransportProtocol.STREAM),
/*     */ 
/*     */ 
/*     */   
/*  35 */   TCP6((byte)33, AddressFamily.AF_IPv6, TransportProtocol.STREAM),
/*     */ 
/*     */ 
/*     */   
/*  39 */   UDP4((byte)18, AddressFamily.AF_IPv4, TransportProtocol.DGRAM),
/*     */ 
/*     */ 
/*     */   
/*  43 */   UDP6((byte)34, AddressFamily.AF_IPv6, TransportProtocol.DGRAM),
/*     */ 
/*     */ 
/*     */   
/*  47 */   UNIX_STREAM((byte)49, AddressFamily.AF_UNIX, TransportProtocol.STREAM),
/*     */ 
/*     */ 
/*     */   
/*  51 */   UNIX_DGRAM((byte)50, AddressFamily.AF_UNIX, TransportProtocol.DGRAM);
/*     */ 
/*     */   
/*     */   private final byte byteValue;
/*     */ 
/*     */   
/*     */   private final AddressFamily addressFamily;
/*     */ 
/*     */   
/*     */   private final TransportProtocol transportProtocol;
/*     */ 
/*     */ 
/*     */   
/*     */   HAProxyProxiedProtocol(byte byteValue, AddressFamily addressFamily, TransportProtocol transportProtocol) {
/*  65 */     this.byteValue = byteValue;
/*  66 */     this.addressFamily = addressFamily;
/*  67 */     this.transportProtocol = transportProtocol;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte byteValue() {
/* 101 */     return this.byteValue;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AddressFamily addressFamily() {
/* 108 */     return this.addressFamily;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TransportProtocol transportProtocol() {
/* 115 */     return this.transportProtocol;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public enum AddressFamily
/*     */   {
/* 125 */     AF_UNSPEC((byte)0),
/*     */ 
/*     */ 
/*     */     
/* 129 */     AF_IPv4((byte)16),
/*     */ 
/*     */ 
/*     */     
/* 133 */     AF_IPv6((byte)32),
/*     */ 
/*     */ 
/*     */     
/* 137 */     AF_UNIX((byte)48);
/*     */ 
/*     */ 
/*     */     
/*     */     private static final byte FAMILY_MASK = -16;
/*     */ 
/*     */ 
/*     */     
/*     */     private final byte byteValue;
/*     */ 
/*     */ 
/*     */     
/*     */     AddressFamily(byte byteValue) {
/* 150 */       this.byteValue = byteValue;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public byte byteValue() {
/* 178 */       return this.byteValue;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public enum TransportProtocol
/*     */   {
/* 189 */     UNSPEC((byte)0),
/*     */ 
/*     */ 
/*     */     
/* 193 */     STREAM((byte)1),
/*     */ 
/*     */ 
/*     */     
/* 197 */     DGRAM((byte)2);
/*     */ 
/*     */ 
/*     */     
/*     */     private static final byte TRANSPORT_MASK = 15;
/*     */ 
/*     */ 
/*     */     
/*     */     private final byte transportByte;
/*     */ 
/*     */ 
/*     */     
/*     */     TransportProtocol(byte transportByte) {
/* 210 */       this.transportByte = transportByte;
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
/*     */ 
/*     */ 
/*     */     
/*     */     public byte byteValue() {
/* 236 */       return this.transportByte;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\haproxy\HAProxyProxiedProtocol.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */