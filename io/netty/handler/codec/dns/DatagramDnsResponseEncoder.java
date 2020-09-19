/*     */ package io.netty.handler.codec.dns;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.channel.AddressedEnvelope;
/*     */ import io.netty.channel.ChannelHandler.Sharable;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.channel.socket.DatagramPacket;
/*     */ import io.netty.handler.codec.MessageToMessageEncoder;
/*     */ import io.netty.util.internal.ObjectUtil;
/*     */ import java.net.InetSocketAddress;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Sharable
/*     */ public class DatagramDnsResponseEncoder
/*     */   extends MessageToMessageEncoder<AddressedEnvelope<DnsResponse, InetSocketAddress>>
/*     */ {
/*     */   private final DnsRecordEncoder recordEncoder;
/*     */   
/*     */   public DatagramDnsResponseEncoder() {
/*  46 */     this(DnsRecordEncoder.DEFAULT);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DatagramDnsResponseEncoder(DnsRecordEncoder recordEncoder) {
/*  53 */     this.recordEncoder = (DnsRecordEncoder)ObjectUtil.checkNotNull(recordEncoder, "recordEncoder");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void encode(ChannelHandlerContext ctx, AddressedEnvelope<DnsResponse, InetSocketAddress> in, List<Object> out) throws Exception {
/*  60 */     InetSocketAddress recipient = (InetSocketAddress)in.recipient();
/*  61 */     DnsResponse response = (DnsResponse)in.content();
/*  62 */     ByteBuf buf = allocateBuffer(ctx, in);
/*     */     
/*  64 */     boolean success = false;
/*     */     try {
/*  66 */       encodeHeader(response, buf);
/*  67 */       encodeQuestions(response, buf);
/*  68 */       encodeRecords(response, DnsSection.ANSWER, buf);
/*  69 */       encodeRecords(response, DnsSection.AUTHORITY, buf);
/*  70 */       encodeRecords(response, DnsSection.ADDITIONAL, buf);
/*  71 */       success = true;
/*     */     } finally {
/*  73 */       if (!success) {
/*  74 */         buf.release();
/*     */       }
/*     */     } 
/*     */     
/*  78 */     out.add(new DatagramPacket(buf, recipient, null));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ByteBuf allocateBuffer(ChannelHandlerContext ctx, AddressedEnvelope<DnsResponse, InetSocketAddress> msg) throws Exception {
/*  88 */     return ctx.alloc().ioBuffer(1024);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void encodeHeader(DnsResponse response, ByteBuf buf) {
/*  98 */     buf.writeShort(response.id());
/*  99 */     int flags = 32768;
/* 100 */     flags |= (response.opCode().byteValue() & 0xFF) << 11;
/* 101 */     if (response.isAuthoritativeAnswer()) {
/* 102 */       flags |= 0x400;
/*     */     }
/* 104 */     if (response.isTruncated()) {
/* 105 */       flags |= 0x200;
/*     */     }
/* 107 */     if (response.isRecursionDesired()) {
/* 108 */       flags |= 0x100;
/*     */     }
/* 110 */     if (response.isRecursionAvailable()) {
/* 111 */       flags |= 0x80;
/*     */     }
/* 113 */     flags |= response.z() << 4;
/* 114 */     flags |= response.code().intValue();
/* 115 */     buf.writeShort(flags);
/* 116 */     buf.writeShort(response.count(DnsSection.QUESTION));
/* 117 */     buf.writeShort(response.count(DnsSection.ANSWER));
/* 118 */     buf.writeShort(response.count(DnsSection.AUTHORITY));
/* 119 */     buf.writeShort(response.count(DnsSection.ADDITIONAL));
/*     */   }
/*     */   
/*     */   private void encodeQuestions(DnsResponse response, ByteBuf buf) throws Exception {
/* 123 */     int count = response.count(DnsSection.QUESTION);
/* 124 */     for (int i = 0; i < count; i++) {
/* 125 */       this.recordEncoder.encodeQuestion(response.<DnsQuestion>recordAt(DnsSection.QUESTION, i), buf);
/*     */     }
/*     */   }
/*     */   
/*     */   private void encodeRecords(DnsResponse response, DnsSection section, ByteBuf buf) throws Exception {
/* 130 */     int count = response.count(section);
/* 131 */     for (int i = 0; i < count; i++)
/* 132 */       this.recordEncoder.encodeRecord(response.recordAt(section, i), buf); 
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\dns\DatagramDnsResponseEncoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */