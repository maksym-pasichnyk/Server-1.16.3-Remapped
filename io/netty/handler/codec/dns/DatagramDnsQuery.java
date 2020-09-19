/*     */ package io.netty.handler.codec.dns;
/*     */ 
/*     */ import io.netty.channel.AddressedEnvelope;
/*     */ import io.netty.util.ReferenceCounted;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.SocketAddress;
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
/*     */ public class DatagramDnsQuery
/*     */   extends DefaultDnsQuery
/*     */   implements AddressedEnvelope<DatagramDnsQuery, InetSocketAddress>
/*     */ {
/*     */   private final InetSocketAddress sender;
/*     */   private final InetSocketAddress recipient;
/*     */   
/*     */   public DatagramDnsQuery(InetSocketAddress sender, InetSocketAddress recipient, int id) {
/*  43 */     this(sender, recipient, id, DnsOpCode.QUERY);
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
/*     */   public DatagramDnsQuery(InetSocketAddress sender, InetSocketAddress recipient, int id, DnsOpCode opCode) {
/*  56 */     super(id, opCode);
/*     */     
/*  58 */     if (recipient == null && sender == null) {
/*  59 */       throw new NullPointerException("recipient and sender");
/*     */     }
/*     */     
/*  62 */     this.sender = sender;
/*  63 */     this.recipient = recipient;
/*     */   }
/*     */ 
/*     */   
/*     */   public DatagramDnsQuery content() {
/*  68 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public InetSocketAddress sender() {
/*  73 */     return this.sender;
/*     */   }
/*     */ 
/*     */   
/*     */   public InetSocketAddress recipient() {
/*  78 */     return this.recipient;
/*     */   }
/*     */ 
/*     */   
/*     */   public DatagramDnsQuery setId(int id) {
/*  83 */     return (DatagramDnsQuery)super.setId(id);
/*     */   }
/*     */ 
/*     */   
/*     */   public DatagramDnsQuery setOpCode(DnsOpCode opCode) {
/*  88 */     return (DatagramDnsQuery)super.setOpCode(opCode);
/*     */   }
/*     */ 
/*     */   
/*     */   public DatagramDnsQuery setRecursionDesired(boolean recursionDesired) {
/*  93 */     return (DatagramDnsQuery)super.setRecursionDesired(recursionDesired);
/*     */   }
/*     */ 
/*     */   
/*     */   public DatagramDnsQuery setZ(int z) {
/*  98 */     return (DatagramDnsQuery)super.setZ(z);
/*     */   }
/*     */ 
/*     */   
/*     */   public DatagramDnsQuery setRecord(DnsSection section, DnsRecord record) {
/* 103 */     return (DatagramDnsQuery)super.setRecord(section, record);
/*     */   }
/*     */ 
/*     */   
/*     */   public DatagramDnsQuery addRecord(DnsSection section, DnsRecord record) {
/* 108 */     return (DatagramDnsQuery)super.addRecord(section, record);
/*     */   }
/*     */ 
/*     */   
/*     */   public DatagramDnsQuery addRecord(DnsSection section, int index, DnsRecord record) {
/* 113 */     return (DatagramDnsQuery)super.addRecord(section, index, record);
/*     */   }
/*     */ 
/*     */   
/*     */   public DatagramDnsQuery clear(DnsSection section) {
/* 118 */     return (DatagramDnsQuery)super.clear(section);
/*     */   }
/*     */ 
/*     */   
/*     */   public DatagramDnsQuery clear() {
/* 123 */     return (DatagramDnsQuery)super.clear();
/*     */   }
/*     */ 
/*     */   
/*     */   public DatagramDnsQuery touch() {
/* 128 */     return (DatagramDnsQuery)super.touch();
/*     */   }
/*     */ 
/*     */   
/*     */   public DatagramDnsQuery touch(Object hint) {
/* 133 */     return (DatagramDnsQuery)super.touch(hint);
/*     */   }
/*     */ 
/*     */   
/*     */   public DatagramDnsQuery retain() {
/* 138 */     return (DatagramDnsQuery)super.retain();
/*     */   }
/*     */ 
/*     */   
/*     */   public DatagramDnsQuery retain(int increment) {
/* 143 */     return (DatagramDnsQuery)super.retain(increment);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 148 */     if (this == obj) {
/* 149 */       return true;
/*     */     }
/*     */     
/* 152 */     if (!super.equals(obj)) {
/* 153 */       return false;
/*     */     }
/*     */     
/* 156 */     if (!(obj instanceof AddressedEnvelope)) {
/* 157 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 161 */     AddressedEnvelope<?, SocketAddress> that = (AddressedEnvelope<?, SocketAddress>)obj;
/* 162 */     if (sender() == null) {
/* 163 */       if (that.sender() != null) {
/* 164 */         return false;
/*     */       }
/* 166 */     } else if (!sender().equals(that.sender())) {
/* 167 */       return false;
/*     */     } 
/*     */     
/* 170 */     if (recipient() == null) {
/* 171 */       if (that.recipient() != null) {
/* 172 */         return false;
/*     */       }
/* 174 */     } else if (!recipient().equals(that.recipient())) {
/* 175 */       return false;
/*     */     } 
/*     */     
/* 178 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 183 */     int hashCode = super.hashCode();
/* 184 */     if (sender() != null) {
/* 185 */       hashCode = hashCode * 31 + sender().hashCode();
/*     */     }
/* 187 */     if (recipient() != null) {
/* 188 */       hashCode = hashCode * 31 + recipient().hashCode();
/*     */     }
/* 190 */     return hashCode;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\dns\DatagramDnsQuery.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */