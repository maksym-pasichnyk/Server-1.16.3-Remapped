/*     */ package io.netty.handler.codec.dns;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufHolder;
/*     */ import io.netty.util.ReferenceCounted;
/*     */ import io.netty.util.internal.ObjectUtil;
/*     */ import io.netty.util.internal.StringUtil;
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
/*     */ public class DefaultDnsRawRecord
/*     */   extends AbstractDnsRecord
/*     */   implements DnsRawRecord
/*     */ {
/*     */   private final ByteBuf content;
/*     */   
/*     */   public DefaultDnsRawRecord(String name, DnsRecordType type, long timeToLive, ByteBuf content) {
/*  40 */     this(name, type, 1, timeToLive, content);
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
/*     */   
/*     */   public DefaultDnsRawRecord(String name, DnsRecordType type, int dnsClass, long timeToLive, ByteBuf content) {
/*  61 */     super(name, type, dnsClass, timeToLive);
/*  62 */     this.content = (ByteBuf)ObjectUtil.checkNotNull(content, "content");
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf content() {
/*  67 */     return this.content;
/*     */   }
/*     */ 
/*     */   
/*     */   public DnsRawRecord copy() {
/*  72 */     return replace(content().copy());
/*     */   }
/*     */ 
/*     */   
/*     */   public DnsRawRecord duplicate() {
/*  77 */     return replace(content().duplicate());
/*     */   }
/*     */ 
/*     */   
/*     */   public DnsRawRecord retainedDuplicate() {
/*  82 */     return replace(content().retainedDuplicate());
/*     */   }
/*     */ 
/*     */   
/*     */   public DnsRawRecord replace(ByteBuf content) {
/*  87 */     return new DefaultDnsRawRecord(name(), type(), dnsClass(), timeToLive(), content);
/*     */   }
/*     */ 
/*     */   
/*     */   public int refCnt() {
/*  92 */     return content().refCnt();
/*     */   }
/*     */ 
/*     */   
/*     */   public DnsRawRecord retain() {
/*  97 */     content().retain();
/*  98 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public DnsRawRecord retain(int increment) {
/* 103 */     content().retain(increment);
/* 104 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean release() {
/* 109 */     return content().release();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean release(int decrement) {
/* 114 */     return content().release(decrement);
/*     */   }
/*     */ 
/*     */   
/*     */   public DnsRawRecord touch() {
/* 119 */     content().touch();
/* 120 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public DnsRawRecord touch(Object hint) {
/* 125 */     content().touch(hint);
/* 126 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 131 */     StringBuilder buf = (new StringBuilder(64)).append(StringUtil.simpleClassName(this)).append('(');
/* 132 */     DnsRecordType type = type();
/* 133 */     if (type != DnsRecordType.OPT) {
/* 134 */       buf.append(name().isEmpty() ? "<root>" : name())
/* 135 */         .append(' ')
/* 136 */         .append(timeToLive())
/* 137 */         .append(' ');
/*     */       
/* 139 */       DnsMessageUtil.appendRecordClass(buf, dnsClass())
/* 140 */         .append(' ')
/* 141 */         .append(type.name());
/*     */     } else {
/* 143 */       buf.append("OPT flags:")
/* 144 */         .append(timeToLive())
/* 145 */         .append(" udp:")
/* 146 */         .append(dnsClass());
/*     */     } 
/*     */     
/* 149 */     buf.append(' ')
/* 150 */       .append(content().readableBytes())
/* 151 */       .append("B)");
/*     */     
/* 153 */     return buf.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\dns\DefaultDnsRawRecord.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */