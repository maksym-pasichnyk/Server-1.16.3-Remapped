/*     */ package io.netty.handler.codec.dns;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufUtil;
/*     */ import io.netty.channel.socket.InternetProtocolFamily;
/*     */ import io.netty.handler.codec.UnsupportedMessageTypeException;
/*     */ import io.netty.util.internal.StringUtil;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DefaultDnsRecordEncoder
/*     */   implements DnsRecordEncoder
/*     */ {
/*     */   private static final int PREFIX_MASK = 7;
/*     */   
/*     */   public final void encodeQuestion(DnsQuestion question, ByteBuf out) throws Exception {
/*  43 */     encodeName(question.name(), out);
/*  44 */     out.writeShort(question.type().intValue());
/*  45 */     out.writeShort(question.dnsClass());
/*     */   }
/*     */ 
/*     */   
/*     */   public void encodeRecord(DnsRecord record, ByteBuf out) throws Exception {
/*  50 */     if (record instanceof DnsQuestion) {
/*  51 */       encodeQuestion((DnsQuestion)record, out);
/*  52 */     } else if (record instanceof DnsPtrRecord) {
/*  53 */       encodePtrRecord((DnsPtrRecord)record, out);
/*  54 */     } else if (record instanceof DnsOptEcsRecord) {
/*  55 */       encodeOptEcsRecord((DnsOptEcsRecord)record, out);
/*  56 */     } else if (record instanceof DnsOptPseudoRecord) {
/*  57 */       encodeOptPseudoRecord((DnsOptPseudoRecord)record, out);
/*  58 */     } else if (record instanceof DnsRawRecord) {
/*  59 */       encodeRawRecord((DnsRawRecord)record, out);
/*     */     } else {
/*  61 */       throw new UnsupportedMessageTypeException(StringUtil.simpleClassName(record));
/*     */     } 
/*     */   }
/*     */   
/*     */   private void encodeRecord0(DnsRecord record, ByteBuf out) throws Exception {
/*  66 */     encodeName(record.name(), out);
/*  67 */     out.writeShort(record.type().intValue());
/*  68 */     out.writeShort(record.dnsClass());
/*  69 */     out.writeInt((int)record.timeToLive());
/*     */   }
/*     */   
/*     */   private void encodePtrRecord(DnsPtrRecord record, ByteBuf out) throws Exception {
/*  73 */     encodeRecord0(record, out);
/*  74 */     encodeName(record.hostname(), out);
/*     */   }
/*     */   
/*     */   private void encodeOptPseudoRecord(DnsOptPseudoRecord record, ByteBuf out) throws Exception {
/*  78 */     encodeRecord0(record, out);
/*  79 */     out.writeShort(0);
/*     */   }
/*     */   
/*     */   private void encodeOptEcsRecord(DnsOptEcsRecord record, ByteBuf out) throws Exception {
/*  83 */     encodeRecord0(record, out);
/*     */     
/*  85 */     int sourcePrefixLength = record.sourcePrefixLength();
/*  86 */     int scopePrefixLength = record.scopePrefixLength();
/*  87 */     int lowOrderBitsToPreserve = sourcePrefixLength & 0x7;
/*     */     
/*  89 */     byte[] bytes = record.address();
/*  90 */     int addressBits = bytes.length << 3;
/*  91 */     if (addressBits < sourcePrefixLength || sourcePrefixLength < 0) {
/*  92 */       throw new IllegalArgumentException(sourcePrefixLength + ": " + sourcePrefixLength + " (expected: 0 >= " + addressBits + ')');
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  98 */     short addressNumber = (short)((bytes.length == 4) ? InternetProtocolFamily.IPv4.addressNumber() : InternetProtocolFamily.IPv6.addressNumber());
/*  99 */     int payloadLength = calculateEcsAddressLength(sourcePrefixLength, lowOrderBitsToPreserve);
/*     */     
/* 101 */     int fullPayloadLength = 8 + payloadLength;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 108 */     out.writeShort(fullPayloadLength);
/* 109 */     out.writeShort(8);
/*     */     
/* 111 */     out.writeShort(fullPayloadLength - 4);
/* 112 */     out.writeShort(addressNumber);
/* 113 */     out.writeByte(sourcePrefixLength);
/* 114 */     out.writeByte(scopePrefixLength);
/*     */     
/* 116 */     if (lowOrderBitsToPreserve > 0) {
/* 117 */       int bytesLength = payloadLength - 1;
/* 118 */       out.writeBytes(bytes, 0, bytesLength);
/*     */ 
/*     */       
/* 121 */       out.writeByte(padWithZeros(bytes[bytesLength], lowOrderBitsToPreserve));
/*     */     } else {
/*     */       
/* 124 */       out.writeBytes(bytes, 0, payloadLength);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   static int calculateEcsAddressLength(int sourcePrefixLength, int lowOrderBitsToPreserve) {
/* 130 */     return (sourcePrefixLength >>> 3) + ((lowOrderBitsToPreserve != 0) ? 1 : 0);
/*     */   }
/*     */   
/*     */   private void encodeRawRecord(DnsRawRecord record, ByteBuf out) throws Exception {
/* 134 */     encodeRecord0(record, out);
/*     */     
/* 136 */     ByteBuf content = record.content();
/* 137 */     int contentLen = content.readableBytes();
/*     */     
/* 139 */     out.writeShort(contentLen);
/* 140 */     out.writeBytes(content, content.readerIndex(), contentLen);
/*     */   }
/*     */   
/*     */   protected void encodeName(String name, ByteBuf buf) throws Exception {
/* 144 */     if (".".equals(name)) {
/*     */       
/* 146 */       buf.writeByte(0);
/*     */       
/*     */       return;
/*     */     } 
/* 150 */     String[] labels = name.split("\\.");
/* 151 */     for (String label : labels) {
/* 152 */       int labelLen = label.length();
/* 153 */       if (labelLen == 0) {
/*     */         break;
/*     */       }
/*     */ 
/*     */       
/* 158 */       buf.writeByte(labelLen);
/* 159 */       ByteBufUtil.writeAscii(buf, label);
/*     */     } 
/*     */     
/* 162 */     buf.writeByte(0);
/*     */   }
/*     */   
/*     */   private static byte padWithZeros(byte b, int lowOrderBitsToPreserve) {
/* 166 */     switch (lowOrderBitsToPreserve) {
/*     */       case 0:
/* 168 */         return 0;
/*     */       case 1:
/* 170 */         return (byte)(0x80 & b);
/*     */       case 2:
/* 172 */         return (byte)(0xC0 & b);
/*     */       case 3:
/* 174 */         return (byte)(0xE0 & b);
/*     */       case 4:
/* 176 */         return (byte)(0xF0 & b);
/*     */       case 5:
/* 178 */         return (byte)(0xF8 & b);
/*     */       case 6:
/* 180 */         return (byte)(0xFC & b);
/*     */       case 7:
/* 182 */         return (byte)(0xFE & b);
/*     */       case 8:
/* 184 */         return b;
/*     */     } 
/* 186 */     throw new IllegalArgumentException("lowOrderBitsToPreserve: " + lowOrderBitsToPreserve);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\dns\DefaultDnsRecordEncoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */