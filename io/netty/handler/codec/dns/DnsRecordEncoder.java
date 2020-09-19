/*    */ package io.netty.handler.codec.dns;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface DnsRecordEncoder
/*    */ {
/* 29 */   public static final DnsRecordEncoder DEFAULT = new DefaultDnsRecordEncoder();
/*    */   
/*    */   void encodeQuestion(DnsQuestion paramDnsQuestion, ByteBuf paramByteBuf) throws Exception;
/*    */   
/*    */   void encodeRecord(DnsRecord paramDnsRecord, ByteBuf paramByteBuf) throws Exception;
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\dns\DnsRecordEncoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */