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
/*    */ public interface DnsRecordDecoder
/*    */ {
/* 29 */   public static final DnsRecordDecoder DEFAULT = new DefaultDnsRecordDecoder();
/*    */   
/*    */   DnsQuestion decodeQuestion(ByteBuf paramByteBuf) throws Exception;
/*    */   
/*    */   <T extends DnsRecord> T decodeRecord(ByteBuf paramByteBuf) throws Exception;
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\dns\DnsRecordDecoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */