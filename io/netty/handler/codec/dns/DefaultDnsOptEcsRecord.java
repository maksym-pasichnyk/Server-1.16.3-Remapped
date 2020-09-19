/*     */ package io.netty.handler.codec.dns;
/*     */ 
/*     */ import io.netty.channel.socket.InternetProtocolFamily;
/*     */ import java.util.Arrays;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class DefaultDnsOptEcsRecord
/*     */   extends AbstractDnsOptPseudoRrRecord
/*     */   implements DnsOptEcsRecord
/*     */ {
/*     */   private final int srcPrefixLength;
/*     */   private final byte[] address;
/*     */   
/*     */   public DefaultDnsOptEcsRecord(int maxPayloadSize, int extendedRcode, int version, int srcPrefixLength, byte[] address) {
/*  43 */     super(maxPayloadSize, extendedRcode, version);
/*  44 */     this.srcPrefixLength = srcPrefixLength;
/*  45 */     this.address = (byte[])verifyAddress(address).clone();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultDnsOptEcsRecord(int maxPayloadSize, int srcPrefixLength, byte[] address) {
/*  56 */     this(maxPayloadSize, 0, 0, srcPrefixLength, address);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultDnsOptEcsRecord(int maxPayloadSize, InternetProtocolFamily protocolFamily) {
/*  67 */     this(maxPayloadSize, 0, 0, 0, protocolFamily.localhost().getAddress());
/*     */   }
/*     */   
/*     */   private static byte[] verifyAddress(byte[] bytes) {
/*  71 */     if (bytes.length == 4 || bytes.length == 16) {
/*  72 */       return bytes;
/*     */     }
/*  74 */     throw new IllegalArgumentException("bytes.length must either 4 or 16");
/*     */   }
/*     */ 
/*     */   
/*     */   public int sourcePrefixLength() {
/*  79 */     return this.srcPrefixLength;
/*     */   }
/*     */ 
/*     */   
/*     */   public int scopePrefixLength() {
/*  84 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public byte[] address() {
/*  89 */     return (byte[])this.address.clone();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  94 */     StringBuilder sb = toStringBuilder();
/*  95 */     sb.setLength(sb.length() - 1);
/*  96 */     return sb.append(" address:")
/*  97 */       .append(Arrays.toString(this.address))
/*  98 */       .append(" sourcePrefixLength:")
/*  99 */       .append(sourcePrefixLength())
/* 100 */       .append(" scopePrefixLength:")
/* 101 */       .append(scopePrefixLength())
/* 102 */       .append(')').toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\dns\DefaultDnsOptEcsRecord.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */