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
/*    */ public abstract class AbstractDnsOptPseudoRrRecord
/*    */   extends AbstractDnsRecord
/*    */   implements DnsOptPseudoRecord
/*    */ {
/*    */   protected AbstractDnsOptPseudoRrRecord(int maxPayloadSize, int extendedRcode, int version) {
/* 31 */     super("", DnsRecordType.OPT, maxPayloadSize, packIntoLong(extendedRcode, version));
/*    */   }
/*    */   
/*    */   protected AbstractDnsOptPseudoRrRecord(int maxPayloadSize) {
/* 35 */     super("", DnsRecordType.OPT, maxPayloadSize, 0L);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private static long packIntoLong(int val, int val2) {
/* 41 */     return ((val & 0xFF) << 24 | (val2 & 0xFF) << 16 | 0x0 | 0x0) & 0xFFFFFFFFL;
/*    */   }
/*    */ 
/*    */   
/*    */   public int extendedRcode() {
/* 46 */     return (short)((int)timeToLive() >> 24 & 0xFF);
/*    */   }
/*    */ 
/*    */   
/*    */   public int version() {
/* 51 */     return (short)((int)timeToLive() >> 16 & 0xFF);
/*    */   }
/*    */ 
/*    */   
/*    */   public int flags() {
/* 56 */     return (short)((short)(int)timeToLive() & 0xFF);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 61 */     return toStringBuilder().toString();
/*    */   }
/*    */   
/*    */   final StringBuilder toStringBuilder() {
/* 65 */     return (new StringBuilder(64))
/* 66 */       .append(StringUtil.simpleClassName(this))
/* 67 */       .append('(')
/* 68 */       .append("OPT flags:")
/* 69 */       .append(flags())
/* 70 */       .append(" version:")
/* 71 */       .append(version())
/* 72 */       .append(" extendedRecode:")
/* 73 */       .append(extendedRcode())
/* 74 */       .append(" udp:")
/* 75 */       .append(dnsClass())
/* 76 */       .append(')');
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\dns\AbstractDnsOptPseudoRrRecord.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */