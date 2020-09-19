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
/*     */ public class DatagramDnsResponseDecoder
/*     */   extends MessageToMessageDecoder<DatagramPacket>
/*     */ {
/*     */   private final DnsRecordDecoder recordDecoder;
/*     */   
/*     */   public DatagramDnsResponseDecoder() {
/*  43 */     this(DnsRecordDecoder.DEFAULT);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DatagramDnsResponseDecoder(DnsRecordDecoder recordDecoder) {
/*  50 */     this.recordDecoder = (DnsRecordDecoder)ObjectUtil.checkNotNull(recordDecoder, "recordDecoder");
/*     */   }
/*     */ 
/*     */   
/*     */   protected void decode(ChannelHandlerContext ctx, DatagramPacket packet, List<Object> out) throws Exception {
/*  55 */     ByteBuf buf = (ByteBuf)packet.content();
/*     */     
/*  57 */     DnsResponse response = newResponse(packet, buf);
/*  58 */     boolean success = false;
/*     */     try {
/*  60 */       int questionCount = buf.readUnsignedShort();
/*  61 */       int answerCount = buf.readUnsignedShort();
/*  62 */       int authorityRecordCount = buf.readUnsignedShort();
/*  63 */       int additionalRecordCount = buf.readUnsignedShort();
/*     */       
/*  65 */       decodeQuestions(response, buf, questionCount);
/*  66 */       decodeRecords(response, DnsSection.ANSWER, buf, answerCount);
/*  67 */       decodeRecords(response, DnsSection.AUTHORITY, buf, authorityRecordCount);
/*  68 */       decodeRecords(response, DnsSection.ADDITIONAL, buf, additionalRecordCount);
/*     */       
/*  70 */       out.add(response);
/*  71 */       success = true;
/*     */     } finally {
/*  73 */       if (!success) {
/*  74 */         response.release();
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private static DnsResponse newResponse(DatagramPacket packet, ByteBuf buf) {
/*  80 */     int id = buf.readUnsignedShort();
/*     */     
/*  82 */     int flags = buf.readUnsignedShort();
/*  83 */     if (flags >> 15 == 0) {
/*  84 */       throw new CorruptedFrameException("not a response");
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  91 */     DnsResponse response = new DatagramDnsResponse((InetSocketAddress)packet.sender(), (InetSocketAddress)packet.recipient(), id, DnsOpCode.valueOf((byte)(flags >> 11 & 0xF)), DnsResponseCode.valueOf((byte)(flags & 0xF)));
/*     */     
/*  93 */     response.setRecursionDesired(((flags >> 8 & 0x1) == 1));
/*  94 */     response.setAuthoritativeAnswer(((flags >> 10 & 0x1) == 1));
/*  95 */     response.setTruncated(((flags >> 9 & 0x1) == 1));
/*  96 */     response.setRecursionAvailable(((flags >> 7 & 0x1) == 1));
/*  97 */     response.setZ(flags >> 4 & 0x7);
/*  98 */     return response;
/*     */   }
/*     */   
/*     */   private void decodeQuestions(DnsResponse response, ByteBuf buf, int questionCount) throws Exception {
/* 102 */     for (int i = questionCount; i > 0; i--) {
/* 103 */       response.addRecord(DnsSection.QUESTION, this.recordDecoder.decodeQuestion(buf));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private void decodeRecords(DnsResponse response, DnsSection section, ByteBuf buf, int count) throws Exception {
/* 109 */     for (int i = count; i > 0; i--) {
/* 110 */       DnsRecord r = this.recordDecoder.decodeRecord(buf);
/* 111 */       if (r == null) {
/*     */         break;
/*     */       }
/*     */ 
/*     */       
/* 116 */       response.addRecord(section, r);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\dns\DatagramDnsResponseDecoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */