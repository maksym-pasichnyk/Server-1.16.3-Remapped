/*    */ package io.netty.handler.codec.dns;
/*    */ 
/*    */ import io.netty.util.internal.StringUtil;
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
/*    */ 
/*    */ 
/*    */ public class DefaultDnsQuestion
/*    */   extends AbstractDnsRecord
/*    */   implements DnsQuestion
/*    */ {
/*    */   public DefaultDnsQuestion(String name, DnsRecordType type) {
/* 34 */     super(name, type, 0L);
/*    */   }
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
/*    */   public DefaultDnsQuestion(String name, DnsRecordType type, int dnsClass) {
/* 53 */     super(name, type, dnsClass, 0L);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 58 */     StringBuilder buf = new StringBuilder(64);
/*    */     
/* 60 */     buf.append(StringUtil.simpleClassName(this))
/* 61 */       .append('(')
/* 62 */       .append(name())
/* 63 */       .append(' ');
/*    */     
/* 65 */     DnsMessageUtil.appendRecordClass(buf, dnsClass())
/* 66 */       .append(' ')
/* 67 */       .append(type().name())
/* 68 */       .append(')');
/*    */     
/* 70 */     return buf.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\dns\DefaultDnsQuestion.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */