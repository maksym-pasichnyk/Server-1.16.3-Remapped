/*     */ package io.netty.handler.codec.dns;
/*     */ 
/*     */ import io.netty.util.internal.ObjectUtil;
/*     */ import io.netty.util.internal.StringUtil;
/*     */ import java.net.IDN;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractDnsRecord
/*     */   implements DnsRecord
/*     */ {
/*     */   private final String name;
/*     */   private final DnsRecordType type;
/*     */   private final short dnsClass;
/*     */   private final long timeToLive;
/*     */   private int hashCode;
/*     */   
/*     */   protected AbstractDnsRecord(String name, DnsRecordType type, long timeToLive) {
/*  45 */     this(name, type, 1, timeToLive);
/*     */   }
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
/*     */   protected AbstractDnsRecord(String name, DnsRecordType type, int dnsClass, long timeToLive) {
/*  65 */     if (timeToLive < 0L) {
/*  66 */       throw new IllegalArgumentException("timeToLive: " + timeToLive + " (expected: >= 0)");
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  72 */     this.name = appendTrailingDot(IDN.toASCII((String)ObjectUtil.checkNotNull(name, "name")));
/*  73 */     this.type = (DnsRecordType)ObjectUtil.checkNotNull(type, "type");
/*  74 */     this.dnsClass = (short)dnsClass;
/*  75 */     this.timeToLive = timeToLive;
/*     */   }
/*     */   
/*     */   private static String appendTrailingDot(String name) {
/*  79 */     if (name.length() > 0 && name.charAt(name.length() - 1) != '.') {
/*  80 */       return name + '.';
/*     */     }
/*  82 */     return name;
/*     */   }
/*     */ 
/*     */   
/*     */   public String name() {
/*  87 */     return this.name;
/*     */   }
/*     */ 
/*     */   
/*     */   public DnsRecordType type() {
/*  92 */     return this.type;
/*     */   }
/*     */ 
/*     */   
/*     */   public int dnsClass() {
/*  97 */     return this.dnsClass & 0xFFFF;
/*     */   }
/*     */ 
/*     */   
/*     */   public long timeToLive() {
/* 102 */     return this.timeToLive;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 107 */     if (this == obj) {
/* 108 */       return true;
/*     */     }
/*     */     
/* 111 */     if (!(obj instanceof DnsRecord)) {
/* 112 */       return false;
/*     */     }
/*     */     
/* 115 */     DnsRecord that = (DnsRecord)obj;
/* 116 */     int hashCode = this.hashCode;
/* 117 */     if (hashCode != 0 && hashCode != that.hashCode()) {
/* 118 */       return false;
/*     */     }
/*     */     
/* 121 */     return (type().intValue() == that.type().intValue() && 
/* 122 */       dnsClass() == that.dnsClass() && 
/* 123 */       name().equals(that.name()));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 128 */     int hashCode = this.hashCode;
/* 129 */     if (hashCode != 0) {
/* 130 */       return hashCode;
/*     */     }
/*     */     
/* 133 */     return this.hashCode = this.name.hashCode() * 31 + type().intValue() * 31 + dnsClass();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 138 */     StringBuilder buf = new StringBuilder(64);
/*     */     
/* 140 */     buf.append(StringUtil.simpleClassName(this))
/* 141 */       .append('(')
/* 142 */       .append(name())
/* 143 */       .append(' ')
/* 144 */       .append(timeToLive())
/* 145 */       .append(' ');
/*     */     
/* 147 */     DnsMessageUtil.appendRecordClass(buf, dnsClass())
/* 148 */       .append(' ')
/* 149 */       .append(type().name())
/* 150 */       .append(')');
/*     */     
/* 152 */     return buf.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\dns\AbstractDnsRecord.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */