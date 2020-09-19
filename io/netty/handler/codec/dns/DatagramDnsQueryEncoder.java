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
/*     */ @Sharable
/*     */ public class DatagramDnsQueryEncoder
/*     */   extends MessageToMessageEncoder<AddressedEnvelope<DnsQuery, InetSocketAddress>>
/*     */ {
/*     */   private final DnsRecordEncoder recordEncoder;
/*     */   
/*     */   public DatagramDnsQueryEncoder() {
/*  45 */     this(DnsRecordEncoder.DEFAULT);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DatagramDnsQueryEncoder(DnsRecordEncoder recordEncoder) {
/*  52 */     this.recordEncoder = (DnsRecordEncoder)ObjectUtil.checkNotNull(recordEncoder, "recordEncoder");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void encode(ChannelHandlerContext ctx, AddressedEnvelope<DnsQuery, InetSocketAddress> in, List<Object> out) throws Exception {
/*  60 */     InetSocketAddress recipient = (InetSocketAddress)in.recipient();
/*  61 */     DnsQuery query = (DnsQuery)in.content();
/*  62 */     ByteBuf buf = allocateBuffer(ctx, in);
/*     */     
/*  64 */     boolean success = false;
/*     */     try {
/*  66 */       encodeHeader(query, buf);
/*  67 */       encodeQuestions(query, buf);
/*  68 */       encodeRecords(query, DnsSection.ADDITIONAL, buf);
/*  69 */       success = true;
/*     */     } finally {
/*  71 */       if (!success) {
/*  72 */         buf.release();
/*     */       }
/*     */     } 
/*     */     
/*  76 */     out.add(new DatagramPacket(buf, recipient, null));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ByteBuf allocateBuffer(ChannelHandlerContext ctx, AddressedEnvelope<DnsQuery, InetSocketAddress> msg) throws Exception {
/*  86 */     return ctx.alloc().ioBuffer(1024);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void encodeHeader(DnsQuery query, ByteBuf buf) {
/*  96 */     buf.writeShort(query.id());
/*  97 */     int flags = 0;
/*  98 */     flags |= (query.opCode().byteValue() & 0xFF) << 14;
/*  99 */     if (query.isRecursionDesired()) {
/* 100 */       flags |= 0x100;
/*     */     }
/* 102 */     buf.writeShort(flags);
/* 103 */     buf.writeShort(query.count(DnsSection.QUESTION));
/* 104 */     buf.writeShort(0);
/* 105 */     buf.writeShort(0);
/* 106 */     buf.writeShort(query.count(DnsSection.ADDITIONAL));
/*     */   }
/*     */   
/*     */   private void encodeQuestions(DnsQuery query, ByteBuf buf) throws Exception {
/* 110 */     int count = query.count(DnsSection.QUESTION);
/* 111 */     for (int i = 0; i < count; i++) {
/* 112 */       this.recordEncoder.encodeQuestion(query.<DnsQuestion>recordAt(DnsSection.QUESTION, i), buf);
/*     */     }
/*     */   }
/*     */   
/*     */   private void encodeRecords(DnsQuery query, DnsSection section, ByteBuf buf) throws Exception {
/* 117 */     int count = query.count(section);
/* 118 */     for (int i = 0; i < count; i++)
/* 119 */       this.recordEncoder.encodeRecord(query.recordAt(section, i), buf); 
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\dns\DatagramDnsQueryEncoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */