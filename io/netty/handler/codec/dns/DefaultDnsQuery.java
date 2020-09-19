/*     */ package io.netty.handler.codec.dns;
/*     */ 
/*     */ import io.netty.util.ReferenceCounted;
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
/*     */ public class DefaultDnsQuery
/*     */   extends AbstractDnsMessage
/*     */   implements DnsQuery
/*     */ {
/*     */   public DefaultDnsQuery(int id) {
/*  32 */     super(id);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultDnsQuery(int id, DnsOpCode opCode) {
/*  42 */     super(id, opCode);
/*     */   }
/*     */ 
/*     */   
/*     */   public DnsQuery setId(int id) {
/*  47 */     return (DnsQuery)super.setId(id);
/*     */   }
/*     */ 
/*     */   
/*     */   public DnsQuery setOpCode(DnsOpCode opCode) {
/*  52 */     return (DnsQuery)super.setOpCode(opCode);
/*     */   }
/*     */ 
/*     */   
/*     */   public DnsQuery setRecursionDesired(boolean recursionDesired) {
/*  57 */     return (DnsQuery)super.setRecursionDesired(recursionDesired);
/*     */   }
/*     */ 
/*     */   
/*     */   public DnsQuery setZ(int z) {
/*  62 */     return (DnsQuery)super.setZ(z);
/*     */   }
/*     */ 
/*     */   
/*     */   public DnsQuery setRecord(DnsSection section, DnsRecord record) {
/*  67 */     return (DnsQuery)super.setRecord(section, record);
/*     */   }
/*     */ 
/*     */   
/*     */   public DnsQuery addRecord(DnsSection section, DnsRecord record) {
/*  72 */     return (DnsQuery)super.addRecord(section, record);
/*     */   }
/*     */ 
/*     */   
/*     */   public DnsQuery addRecord(DnsSection section, int index, DnsRecord record) {
/*  77 */     return (DnsQuery)super.addRecord(section, index, record);
/*     */   }
/*     */ 
/*     */   
/*     */   public DnsQuery clear(DnsSection section) {
/*  82 */     return (DnsQuery)super.clear(section);
/*     */   }
/*     */ 
/*     */   
/*     */   public DnsQuery clear() {
/*  87 */     return (DnsQuery)super.clear();
/*     */   }
/*     */ 
/*     */   
/*     */   public DnsQuery touch() {
/*  92 */     return (DnsQuery)super.touch();
/*     */   }
/*     */ 
/*     */   
/*     */   public DnsQuery touch(Object hint) {
/*  97 */     return (DnsQuery)super.touch(hint);
/*     */   }
/*     */ 
/*     */   
/*     */   public DnsQuery retain() {
/* 102 */     return (DnsQuery)super.retain();
/*     */   }
/*     */ 
/*     */   
/*     */   public DnsQuery retain(int increment) {
/* 107 */     return (DnsQuery)super.retain(increment);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 112 */     return DnsMessageUtil.appendQuery(new StringBuilder(128), this).toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\dns\DefaultDnsQuery.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */