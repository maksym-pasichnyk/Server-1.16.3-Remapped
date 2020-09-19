/*     */ package io.netty.handler.codec.dns;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.handler.codec.CorruptedFrameException;
/*     */ import io.netty.util.CharsetUtil;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DefaultDnsRecordDecoder
/*     */   implements DnsRecordDecoder
/*     */ {
/*     */   static final String ROOT = ".";
/*     */   
/*     */   public final DnsQuestion decodeQuestion(ByteBuf in) throws Exception {
/*  40 */     String name = decodeName(in);
/*  41 */     DnsRecordType type = DnsRecordType.valueOf(in.readUnsignedShort());
/*  42 */     int qClass = in.readUnsignedShort();
/*  43 */     return new DefaultDnsQuestion(name, type, qClass);
/*     */   }
/*     */ 
/*     */   
/*     */   public final <T extends DnsRecord> T decodeRecord(ByteBuf in) throws Exception {
/*  48 */     int startOffset = in.readerIndex();
/*  49 */     String name = decodeName(in);
/*     */     
/*  51 */     int endOffset = in.writerIndex();
/*  52 */     if (endOffset - startOffset < 10) {
/*     */       
/*  54 */       in.readerIndex(startOffset);
/*  55 */       return null;
/*     */     } 
/*     */     
/*  58 */     DnsRecordType type = DnsRecordType.valueOf(in.readUnsignedShort());
/*  59 */     int aClass = in.readUnsignedShort();
/*  60 */     long ttl = in.readUnsignedInt();
/*  61 */     int length = in.readUnsignedShort();
/*  62 */     int offset = in.readerIndex();
/*     */     
/*  64 */     if (endOffset - offset < length) {
/*     */       
/*  66 */       in.readerIndex(startOffset);
/*  67 */       return null;
/*     */     } 
/*     */ 
/*     */     
/*  71 */     DnsRecord dnsRecord = decodeRecord(name, type, aClass, ttl, in, offset, length);
/*  72 */     in.readerIndex(offset + length);
/*  73 */     return (T)dnsRecord;
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
/*     */   protected DnsRecord decodeRecord(String name, DnsRecordType type, int dnsClass, long timeToLive, ByteBuf in, int offset, int length) throws Exception {
/*  97 */     if (type == DnsRecordType.PTR) {
/*  98 */       return new DefaultDnsPtrRecord(name, dnsClass, timeToLive, 
/*  99 */           decodeName0(in.duplicate().setIndex(offset, offset + length)));
/*     */     }
/* 101 */     return new DefaultDnsRawRecord(name, type, dnsClass, timeToLive, in
/* 102 */         .retainedDuplicate().setIndex(offset, offset + length));
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
/*     */   protected String decodeName0(ByteBuf in) {
/* 114 */     return decodeName(in);
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
/*     */   public static String decodeName(ByteBuf in) {
/* 126 */     int position = -1;
/* 127 */     int checked = 0;
/* 128 */     int end = in.writerIndex();
/* 129 */     int readable = in.readableBytes();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 138 */     if (readable == 0) {
/* 139 */       return ".";
/*     */     }
/*     */     
/* 142 */     StringBuilder name = new StringBuilder(readable << 1);
/* 143 */     while (in.isReadable()) {
/* 144 */       int len = in.readUnsignedByte();
/* 145 */       boolean pointer = ((len & 0xC0) == 192);
/* 146 */       if (pointer) {
/* 147 */         if (position == -1) {
/* 148 */           position = in.readerIndex() + 1;
/*     */         }
/*     */         
/* 151 */         if (!in.isReadable()) {
/* 152 */           throw new CorruptedFrameException("truncated pointer in a name");
/*     */         }
/*     */         
/* 155 */         int next = (len & 0x3F) << 8 | in.readUnsignedByte();
/* 156 */         if (next >= end) {
/* 157 */           throw new CorruptedFrameException("name has an out-of-range pointer");
/*     */         }
/* 159 */         in.readerIndex(next);
/*     */ 
/*     */         
/* 162 */         checked += 2;
/* 163 */         if (checked >= end)
/* 164 */           throw new CorruptedFrameException("name contains a loop.");  continue;
/*     */       } 
/* 166 */       if (len != 0) {
/* 167 */         if (!in.isReadable(len)) {
/* 168 */           throw new CorruptedFrameException("truncated label in a name");
/*     */         }
/* 170 */         name.append(in.toString(in.readerIndex(), len, CharsetUtil.UTF_8)).append('.');
/* 171 */         in.skipBytes(len);
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 177 */     if (position != -1) {
/* 178 */       in.readerIndex(position);
/*     */     }
/*     */     
/* 181 */     if (name.length() == 0) {
/* 182 */       return ".";
/*     */     }
/*     */     
/* 185 */     if (name.charAt(name.length() - 1) != '.') {
/* 186 */       name.append('.');
/*     */     }
/*     */     
/* 189 */     return name.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\dns\DefaultDnsRecordDecoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */