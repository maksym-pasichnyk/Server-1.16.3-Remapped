/*    */ package io.netty.handler.codec.dns;
/*    */ 
/*    */ import io.netty.util.internal.ObjectUtil;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DefaultDnsPtrRecord
/*    */   extends AbstractDnsRecord
/*    */   implements DnsPtrRecord
/*    */ {
/*    */   private final String hostname;
/*    */   
/*    */   public DefaultDnsPtrRecord(String name, int dnsClass, long timeToLive, String hostname) {
/* 46 */     super(name, DnsRecordType.PTR, dnsClass, timeToLive);
/* 47 */     this.hostname = (String)ObjectUtil.checkNotNull(hostname, "hostname");
/*    */   }
/*    */ 
/*    */   
/*    */   public String hostname() {
/* 52 */     return this.hostname;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 57 */     StringBuilder buf = (new StringBuilder(64)).append(StringUtil.simpleClassName(this)).append('(');
/* 58 */     DnsRecordType type = type();
/* 59 */     buf.append(name().isEmpty() ? "<root>" : name())
/* 60 */       .append(' ')
/* 61 */       .append(timeToLive())
/* 62 */       .append(' ');
/*    */     
/* 64 */     DnsMessageUtil.appendRecordClass(buf, dnsClass())
/* 65 */       .append(' ')
/* 66 */       .append(type.name());
/*    */     
/* 68 */     buf.append(' ')
/* 69 */       .append(this.hostname);
/*    */     
/* 71 */     return buf.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\dns\DefaultDnsPtrRecord.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */