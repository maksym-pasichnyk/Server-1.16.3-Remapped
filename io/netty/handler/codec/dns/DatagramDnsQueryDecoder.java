/*     */ package io.netty.handler.codec.dns;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.channel.ChannelHandler.Sharable;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.channel.socket.DatagramPacket;
/*     */ import io.netty.handler.codec.CorruptedFrameException;
/*     */ import io.netty.handler.codec.MessageToMessageDecoder;
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
/*     */ @Sharable
/*     */ public class DatagramDnsQueryDecoder
/*     */   extends MessageToMessageDecoder<DatagramPacket>
/*     */ {
/*     */   private final DnsRecordDecoder recordDecoder;
/*     */   
/*     */   public DatagramDnsQueryDecoder() {
/*  43 */     this(DnsRecordDecoder.DEFAULT);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DatagramDnsQueryDecoder(DnsRecordDecoder recordDecoder) {
/*  50 */     this.recordDecoder = (DnsRecordDecoder)ObjectUtil.checkNotNull(recordDecoder, "recordDecoder");
/*     */   }
/*     */ 
/*     */   
/*     */   protected void decode(ChannelHandlerContext ctx, DatagramPacket packet, List<Object> out) throws Exception {
/*  55 */     ByteBuf buf = (ByteBuf)packet.content();
/*     */     
/*  57 */     DnsQuery query = newQuery(packet, buf);
/*  58 */     boolean success = false;
/*     */     try {
/*  60 */       int questionCount = buf.readUnsignedShort();
/*  61 */       int answerCount = buf.readUnsignedShort();
/*  62 */       int authorityRecordCount = buf.readUnsignedShort();
/*  63 */       int additionalRecordCount = buf.readUnsignedShort();
/*     */       
/*  65 */       decodeQuestions(query, buf, questionCount);
/*  66 */       decodeRecords(query, DnsSection.ANSWER, buf, answerCount);
/*  67 */       decodeRecords(query, DnsSection.AUTHORITY, buf, authorityRecordCount);
/*  68 */       decodeRecords(query, DnsSection.ADDITIONAL, buf, additionalRecordCount);
/*     */       
/*  70 */       out.add(query);
/*  71 */       success = true;
/*     */     } finally {
/*  73 */       if (!success) {
/*  74 */         query.release();
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private static DnsQuery newQuery(DatagramPacket packet, ByteBuf buf) {
/*  80 */     int id = buf.readUnsignedShort();
/*     */     
/*  82 */     int flags = buf.readUnsignedShort();
/*  83 */     if (flags >> 15 == 1) {
/*  84 */       throw new CorruptedFrameException("not a query");
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  91 */     DnsQuery query = new DatagramDnsQuery((InetSocketAddress)packet.sender(), (InetSocketAddress)packet.recipient(), id, DnsOpCode.valueOf((byte)(flags >> 11 & 0xF)));
/*  92 */     query.setRecursionDesired(((flags >> 8 & 0x1) == 1));
/*  93 */     query.setZ(flags >> 4 & 0x7);
/*  94 */     return query;
/*     */   }
/*     */   
/*     */   private void decodeQuestions(DnsQuery query, ByteBuf buf, int questionCount) throws Exception {
/*  98 */     for (int i = questionCount; i > 0; i--) {
/*  99 */       query.addRecord(DnsSection.QUESTION, this.recordDecoder.decodeQuestion(buf));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private void decodeRecords(DnsQuery query, DnsSection section, ByteBuf buf, int count) throws Exception {
/* 105 */     for (int i = count; i > 0; i--) {
/* 106 */       DnsRecord r = this.recordDecoder.decodeRecord(buf);
/* 107 */       if (r == null) {
/*     */         break;
/*     */       }
/*     */ 
/*     */       
/* 112 */       query.addRecord(section, r);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\dns\DatagramDnsQueryDecoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */