/*     */ package io.netty.handler.codec.dns;
/*     */ 
/*     */ import io.netty.util.ReferenceCounted;
/*     */ import io.netty.util.internal.ObjectUtil;
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
/*     */ public class DefaultDnsResponse
/*     */   extends AbstractDnsMessage
/*     */   implements DnsResponse
/*     */ {
/*     */   private boolean authoritativeAnswer;
/*     */   private boolean truncated;
/*     */   private boolean recursionAvailable;
/*     */   private DnsResponseCode code;
/*     */   
/*     */   public DefaultDnsResponse(int id) {
/*  40 */     this(id, DnsOpCode.QUERY, DnsResponseCode.NOERROR);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultDnsResponse(int id, DnsOpCode opCode) {
/*  50 */     this(id, opCode, DnsResponseCode.NOERROR);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultDnsResponse(int id, DnsOpCode opCode, DnsResponseCode code) {
/*  61 */     super(id, opCode);
/*  62 */     setCode(code);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isAuthoritativeAnswer() {
/*  67 */     return this.authoritativeAnswer;
/*     */   }
/*     */ 
/*     */   
/*     */   public DnsResponse setAuthoritativeAnswer(boolean authoritativeAnswer) {
/*  72 */     this.authoritativeAnswer = authoritativeAnswer;
/*  73 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isTruncated() {
/*  78 */     return this.truncated;
/*     */   }
/*     */ 
/*     */   
/*     */   public DnsResponse setTruncated(boolean truncated) {
/*  83 */     this.truncated = truncated;
/*  84 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isRecursionAvailable() {
/*  89 */     return this.recursionAvailable;
/*     */   }
/*     */ 
/*     */   
/*     */   public DnsResponse setRecursionAvailable(boolean recursionAvailable) {
/*  94 */     this.recursionAvailable = recursionAvailable;
/*  95 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public DnsResponseCode code() {
/* 100 */     return this.code;
/*     */   }
/*     */ 
/*     */   
/*     */   public DnsResponse setCode(DnsResponseCode code) {
/* 105 */     this.code = (DnsResponseCode)ObjectUtil.checkNotNull(code, "code");
/* 106 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public DnsResponse setId(int id) {
/* 111 */     return (DnsResponse)super.setId(id);
/*     */   }
/*     */ 
/*     */   
/*     */   public DnsResponse setOpCode(DnsOpCode opCode) {
/* 116 */     return (DnsResponse)super.setOpCode(opCode);
/*     */   }
/*     */ 
/*     */   
/*     */   public DnsResponse setRecursionDesired(boolean recursionDesired) {
/* 121 */     return (DnsResponse)super.setRecursionDesired(recursionDesired);
/*     */   }
/*     */ 
/*     */   
/*     */   public DnsResponse setZ(int z) {
/* 126 */     return (DnsResponse)super.setZ(z);
/*     */   }
/*     */ 
/*     */   
/*     */   public DnsResponse setRecord(DnsSection section, DnsRecord record) {
/* 131 */     return (DnsResponse)super.setRecord(section, record);
/*     */   }
/*     */ 
/*     */   
/*     */   public DnsResponse addRecord(DnsSection section, DnsRecord record) {
/* 136 */     return (DnsResponse)super.addRecord(section, record);
/*     */   }
/*     */ 
/*     */   
/*     */   public DnsResponse addRecord(DnsSection section, int index, DnsRecord record) {
/* 141 */     return (DnsResponse)super.addRecord(section, index, record);
/*     */   }
/*     */ 
/*     */   
/*     */   public DnsResponse clear(DnsSection section) {
/* 146 */     return (DnsResponse)super.clear(section);
/*     */   }
/*     */ 
/*     */   
/*     */   public DnsResponse clear() {
/* 151 */     return (DnsResponse)super.clear();
/*     */   }
/*     */ 
/*     */   
/*     */   public DnsResponse touch() {
/* 156 */     return (DnsResponse)super.touch();
/*     */   }
/*     */ 
/*     */   
/*     */   public DnsResponse touch(Object hint) {
/* 161 */     return (DnsResponse)super.touch(hint);
/*     */   }
/*     */ 
/*     */   
/*     */   public DnsResponse retain() {
/* 166 */     return (DnsResponse)super.retain();
/*     */   }
/*     */ 
/*     */   
/*     */   public DnsResponse retain(int increment) {
/* 171 */     return (DnsResponse)super.retain(increment);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 176 */     return DnsMessageUtil.appendResponse(new StringBuilder(128), this).toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\dns\DefaultDnsResponse.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */