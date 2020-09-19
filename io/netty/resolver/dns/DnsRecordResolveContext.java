/*    */ package io.netty.resolver.dns;
/*    */ 
/*    */ import io.netty.channel.EventLoop;
/*    */ import io.netty.handler.codec.dns.DnsQuestion;
/*    */ import io.netty.handler.codec.dns.DnsRecord;
/*    */ import io.netty.handler.codec.dns.DnsRecordType;
/*    */ import io.netty.util.ReferenceCountUtil;
/*    */ import java.net.UnknownHostException;
/*    */ import java.util.List;
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
/*    */ final class DnsRecordResolveContext
/*    */   extends DnsResolveContext<DnsRecord>
/*    */ {
/*    */   DnsRecordResolveContext(DnsNameResolver parent, DnsQuestion question, DnsRecord[] additionals, DnsServerAddressStream nameServerAddrs) {
/* 31 */     this(parent, question.name(), question.dnsClass(), new DnsRecordType[] { question
/* 32 */           .type() }, additionals, nameServerAddrs);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private DnsRecordResolveContext(DnsNameResolver parent, String hostname, int dnsClass, DnsRecordType[] expectedTypes, DnsRecord[] additionals, DnsServerAddressStream nameServerAddrs) {
/* 40 */     super(parent, hostname, dnsClass, expectedTypes, additionals, nameServerAddrs);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   DnsResolveContext<DnsRecord> newResolverContext(DnsNameResolver parent, String hostname, int dnsClass, DnsRecordType[] expectedTypes, DnsRecord[] additionals, DnsServerAddressStream nameServerAddrs) {
/* 48 */     return new DnsRecordResolveContext(parent, hostname, dnsClass, expectedTypes, additionals, nameServerAddrs);
/*    */   }
/*    */ 
/*    */   
/*    */   DnsRecord convertRecord(DnsRecord record, String hostname, DnsRecord[] additionals, EventLoop eventLoop) {
/* 53 */     return (DnsRecord)ReferenceCountUtil.retain(record);
/*    */   }
/*    */ 
/*    */   
/*    */   List<DnsRecord> filterResults(List<DnsRecord> unfiltered) {
/* 58 */     return unfiltered;
/*    */   }
/*    */   
/*    */   void cache(String hostname, DnsRecord[] additionals, DnsRecord result, DnsRecord convertedResult) {}
/*    */   
/*    */   void cache(String hostname, DnsRecord[] additionals, UnknownHostException cause) {}
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\resolver\dns\DnsRecordResolveContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */