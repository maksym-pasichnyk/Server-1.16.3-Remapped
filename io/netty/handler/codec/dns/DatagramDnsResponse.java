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
/*     */ public class DatagramDnsResponse
/*     */   extends DefaultDnsResponse
/*     */   implements AddressedEnvelope<DatagramDnsResponse, InetSocketAddress>
/*     */ {
/*     */   private final InetSocketAddress sender;
/*     */   private final InetSocketAddress recipient;
/*     */   
/*     */   public DatagramDnsResponse(InetSocketAddress sender, InetSocketAddress recipient, int id) {
/*  43 */     this(sender, recipient, id, DnsOpCode.QUERY, DnsResponseCode.NOERROR);
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
/*     */   public DatagramDnsResponse(InetSocketAddress sender, InetSocketAddress recipient, int id, DnsOpCode opCode) {
/*  55 */     this(sender, recipient, id, opCode, DnsResponseCode.NOERROR);
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
/*     */   public DatagramDnsResponse(InetSocketAddress sender, InetSocketAddress recipient, int id, DnsOpCode opCode, DnsResponseCode responseCode) {
/*  70 */     super(id, opCode, responseCode);
/*     */     
/*  72 */     if (recipient == null && sender == null) {
/*  73 */       throw new NullPointerException("recipient and sender");
/*     */     }
/*     */     
/*  76 */     this.sender = sender;
/*  77 */     this.recipient = recipient;
/*     */   }
/*     */ 
/*     */   
/*     */   public DatagramDnsResponse content() {
/*  82 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public InetSocketAddress sender() {
/*  87 */     return this.sender;
/*     */   }
/*     */ 
/*     */   
/*     */   public InetSocketAddress recipient() {
/*  92 */     return this.recipient;
/*     */   }
/*     */ 
/*     */   
/*     */   public DatagramDnsResponse setAuthoritativeAnswer(boolean authoritativeAnswer) {
/*  97 */     return (DatagramDnsResponse)super.setAuthoritativeAnswer(authoritativeAnswer);
/*     */   }
/*     */ 
/*     */   
/*     */   public DatagramDnsResponse setTruncated(boolean truncated) {
/* 102 */     return (DatagramDnsResponse)super.setTruncated(truncated);
/*     */   }
/*     */ 
/*     */   
/*     */   public DatagramDnsResponse setRecursionAvailable(boolean recursionAvailable) {
/* 107 */     return (DatagramDnsResponse)super.setRecursionAvailable(recursionAvailable);
/*     */   }
/*     */ 
/*     */   
/*     */   public DatagramDnsResponse setCode(DnsResponseCode code) {
/* 112 */     return (DatagramDnsResponse)super.setCode(code);
/*     */   }
/*     */ 
/*     */   
/*     */   public DatagramDnsResponse setId(int id) {
/* 117 */     return (DatagramDnsResponse)super.setId(id);
/*     */   }
/*     */ 
/*     */   
/*     */   public DatagramDnsResponse setOpCode(DnsOpCode opCode) {
/* 122 */     return (DatagramDnsResponse)super.setOpCode(opCode);
/*     */   }
/*     */ 
/*     */   
/*     */   public DatagramDnsResponse setRecursionDesired(boolean recursionDesired) {
/* 127 */     return (DatagramDnsResponse)super.setRecursionDesired(recursionDesired);
/*     */   }
/*     */ 
/*     */   
/*     */   public DatagramDnsResponse setZ(int z) {
/* 132 */     return (DatagramDnsResponse)super.setZ(z);
/*     */   }
/*     */ 
/*     */   
/*     */   public DatagramDnsResponse setRecord(DnsSection section, DnsRecord record) {
/* 137 */     return (DatagramDnsResponse)super.setRecord(section, record);
/*     */   }
/*     */ 
/*     */   
/*     */   public DatagramDnsResponse addRecord(DnsSection section, DnsRecord record) {
/* 142 */     return (DatagramDnsResponse)super.addRecord(section, record);
/*     */   }
/*     */ 
/*     */   
/*     */   public DatagramDnsResponse addRecord(DnsSection section, int index, DnsRecord record) {
/* 147 */     return (DatagramDnsResponse)super.addRecord(section, index, record);
/*     */   }
/*     */ 
/*     */   
/*     */   public DatagramDnsResponse clear(DnsSection section) {
/* 152 */     return (DatagramDnsResponse)super.clear(section);
/*     */   }
/*     */ 
/*     */   
/*     */   public DatagramDnsResponse clear() {
/* 157 */     return (DatagramDnsResponse)super.clear();
/*     */   }
/*     */ 
/*     */   
/*     */   public DatagramDnsResponse touch() {
/* 162 */     return (DatagramDnsResponse)super.touch();
/*     */   }
/*     */ 
/*     */   
/*     */   public DatagramDnsResponse touch(Object hint) {
/* 167 */     return (DatagramDnsResponse)super.touch(hint);
/*     */   }
/*     */ 
/*     */   
/*     */   public DatagramDnsResponse retain() {
/* 172 */     return (DatagramDnsResponse)super.retain();
/*     */   }
/*     */ 
/*     */   
/*     */   public DatagramDnsResponse retain(int increment) {
/* 177 */     return (DatagramDnsResponse)super.retain(increment);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 182 */     if (this == obj) {
/* 183 */       return true;
/*     */     }
/*     */     
/* 186 */     if (!super.equals(obj)) {
/* 187 */       return false;
/*     */     }
/*     */     
/* 190 */     if (!(obj instanceof AddressedEnvelope)) {
/* 191 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 195 */     AddressedEnvelope<?, SocketAddress> that = (AddressedEnvelope<?, SocketAddress>)obj;
/* 196 */     if (sender() == null) {
/* 197 */       if (that.sender() != null) {
/* 198 */         return false;
/*     */       }
/* 200 */     } else if (!sender().equals(that.sender())) {
/* 201 */       return false;
/*     */     } 
/*     */     
/* 204 */     if (recipient() == null) {
/* 205 */       if (that.recipient() != null) {
/* 206 */         return false;
/*     */       }
/* 208 */     } else if (!recipient().equals(that.recipient())) {
/* 209 */       return false;
/*     */     } 
/*     */     
/* 212 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 217 */     int hashCode = super.hashCode();
/* 218 */     if (sender() != null) {
/* 219 */       hashCode = hashCode * 31 + sender().hashCode();
/*     */     }
/* 221 */     if (recipient() != null) {
/* 222 */       hashCode = hashCode * 31 + recipient().hashCode();
/*     */     }
/* 224 */     return hashCode;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\dns\DatagramDnsResponse.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */