/*     */ package io.netty.resolver.dns;
/*     */ 
/*     */ import io.netty.channel.AddressedEnvelope;
/*     */ import io.netty.channel.Channel;
/*     */ import io.netty.channel.ChannelFuture;
/*     */ import io.netty.channel.ChannelFutureListener;
/*     */ import io.netty.channel.ChannelPromise;
/*     */ import io.netty.handler.codec.dns.AbstractDnsOptPseudoRrRecord;
/*     */ import io.netty.handler.codec.dns.DatagramDnsQuery;
/*     */ import io.netty.handler.codec.dns.DnsQuery;
/*     */ import io.netty.handler.codec.dns.DnsQuestion;
/*     */ import io.netty.handler.codec.dns.DnsRecord;
/*     */ import io.netty.handler.codec.dns.DnsResponse;
/*     */ import io.netty.handler.codec.dns.DnsSection;
/*     */ import io.netty.util.concurrent.Future;
/*     */ import io.netty.util.concurrent.GenericFutureListener;
/*     */ import io.netty.util.concurrent.Promise;
/*     */ import io.netty.util.concurrent.ScheduledFuture;
/*     */ import io.netty.util.internal.ObjectUtil;
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.util.concurrent.TimeUnit;
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
/*     */ final class DnsQueryContext
/*     */ {
/*  44 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(DnsQueryContext.class);
/*     */   
/*     */   private final DnsNameResolver parent;
/*     */   
/*     */   private final Promise<AddressedEnvelope<DnsResponse, InetSocketAddress>> promise;
/*     */   
/*     */   private final int id;
/*     */   
/*     */   private final DnsQuestion question;
/*     */   
/*     */   private final DnsRecord[] additionals;
/*     */   
/*     */   private final DnsRecord optResource;
/*     */   
/*     */   private final InetSocketAddress nameServerAddr;
/*     */   private final boolean recursionDesired;
/*     */   private volatile ScheduledFuture<?> timeoutFuture;
/*     */   
/*     */   DnsQueryContext(DnsNameResolver parent, InetSocketAddress nameServerAddr, DnsQuestion question, DnsRecord[] additionals, Promise<AddressedEnvelope<DnsResponse, InetSocketAddress>> promise) {
/*  63 */     this.parent = (DnsNameResolver)ObjectUtil.checkNotNull(parent, "parent");
/*  64 */     this.nameServerAddr = (InetSocketAddress)ObjectUtil.checkNotNull(nameServerAddr, "nameServerAddr");
/*  65 */     this.question = (DnsQuestion)ObjectUtil.checkNotNull(question, "question");
/*  66 */     this.additionals = (DnsRecord[])ObjectUtil.checkNotNull(additionals, "additionals");
/*  67 */     this.promise = (Promise<AddressedEnvelope<DnsResponse, InetSocketAddress>>)ObjectUtil.checkNotNull(promise, "promise");
/*  68 */     this.recursionDesired = parent.isRecursionDesired();
/*  69 */     this.id = parent.queryContextManager.add(this);
/*     */     
/*  71 */     if (parent.isOptResourceEnabled()) {
/*  72 */       this.optResource = (DnsRecord)new AbstractDnsOptPseudoRrRecord(parent.maxPayloadSize(), 0, 0) {
/*     */         
/*     */         };
/*     */     } else {
/*  76 */       this.optResource = null;
/*     */     } 
/*     */   }
/*     */   
/*     */   InetSocketAddress nameServerAddr() {
/*  81 */     return this.nameServerAddr;
/*     */   }
/*     */   
/*     */   DnsQuestion question() {
/*  85 */     return this.question;
/*     */   }
/*     */   
/*     */   void query(ChannelPromise writePromise) {
/*  89 */     DnsQuestion question = question();
/*  90 */     InetSocketAddress nameServerAddr = nameServerAddr();
/*  91 */     DatagramDnsQuery query = new DatagramDnsQuery(null, nameServerAddr, this.id);
/*     */     
/*  93 */     query.setRecursionDesired(this.recursionDesired);
/*     */     
/*  95 */     query.addRecord(DnsSection.QUESTION, (DnsRecord)question);
/*     */     
/*  97 */     for (DnsRecord record : this.additionals) {
/*  98 */       query.addRecord(DnsSection.ADDITIONAL, record);
/*     */     }
/*     */     
/* 101 */     if (this.optResource != null) {
/* 102 */       query.addRecord(DnsSection.ADDITIONAL, this.optResource);
/*     */     }
/*     */     
/* 105 */     if (logger.isDebugEnabled()) {
/* 106 */       logger.debug("{} WRITE: [{}: {}], {}", new Object[] { this.parent.ch, Integer.valueOf(this.id), nameServerAddr, question });
/*     */     }
/*     */     
/* 109 */     sendQuery((DnsQuery)query, writePromise);
/*     */   }
/*     */   
/*     */   private void sendQuery(final DnsQuery query, final ChannelPromise writePromise) {
/* 113 */     if (this.parent.channelFuture.isDone()) {
/* 114 */       writeQuery(query, writePromise);
/*     */     } else {
/* 116 */       this.parent.channelFuture.addListener(new GenericFutureListener<Future<? super Channel>>()
/*     */           {
/*     */             public void operationComplete(Future<? super Channel> future) throws Exception {
/* 119 */               if (future.isSuccess()) {
/* 120 */                 DnsQueryContext.this.writeQuery(query, writePromise);
/*     */               } else {
/* 122 */                 Throwable cause = future.cause();
/* 123 */                 DnsQueryContext.this.promise.tryFailure(cause);
/* 124 */                 writePromise.setFailure(cause);
/*     */               } 
/*     */             }
/*     */           });
/*     */     } 
/*     */   }
/*     */   
/*     */   private void writeQuery(DnsQuery query, ChannelPromise writePromise) {
/* 132 */     final ChannelFuture writeFuture = this.parent.ch.writeAndFlush(query, writePromise);
/* 133 */     if (writeFuture.isDone()) {
/* 134 */       onQueryWriteCompletion(writeFuture);
/*     */     } else {
/* 136 */       writeFuture.addListener((GenericFutureListener)new ChannelFutureListener()
/*     */           {
/*     */             public void operationComplete(ChannelFuture future) throws Exception {
/* 139 */               DnsQueryContext.this.onQueryWriteCompletion(writeFuture);
/*     */             }
/*     */           });
/*     */     } 
/*     */   }
/*     */   
/*     */   private void onQueryWriteCompletion(ChannelFuture writeFuture) {
/* 146 */     if (!writeFuture.isSuccess()) {
/* 147 */       setFailure("failed to send a query", writeFuture.cause());
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 152 */     final long queryTimeoutMillis = this.parent.queryTimeoutMillis();
/* 153 */     if (queryTimeoutMillis > 0L) {
/* 154 */       this.timeoutFuture = this.parent.ch.eventLoop().schedule(new Runnable()
/*     */           {
/*     */             public void run() {
/* 157 */               if (DnsQueryContext.this.promise.isDone()) {
/*     */                 return;
/*     */               }
/*     */ 
/*     */               
/* 162 */               DnsQueryContext.this.setFailure("query timed out after " + queryTimeoutMillis + " milliseconds", null);
/*     */             }
/*     */           }queryTimeoutMillis, TimeUnit.MILLISECONDS);
/*     */     }
/*     */   }
/*     */   
/*     */   void finish(AddressedEnvelope<? extends DnsResponse, InetSocketAddress> envelope) {
/* 169 */     DnsResponse res = (DnsResponse)envelope.content();
/* 170 */     if (res.count(DnsSection.QUESTION) != 1) {
/* 171 */       logger.warn("Received a DNS response with invalid number of questions: {}", envelope);
/*     */       
/*     */       return;
/*     */     } 
/* 175 */     if (!question().equals(res.recordAt(DnsSection.QUESTION))) {
/* 176 */       logger.warn("Received a mismatching DNS response: {}", envelope);
/*     */       
/*     */       return;
/*     */     } 
/* 180 */     setSuccess(envelope);
/*     */   }
/*     */   
/*     */   private void setSuccess(AddressedEnvelope<? extends DnsResponse, InetSocketAddress> envelope) {
/* 184 */     this.parent.queryContextManager.remove(nameServerAddr(), this.id);
/*     */ 
/*     */     
/* 187 */     ScheduledFuture<?> timeoutFuture = this.timeoutFuture;
/* 188 */     if (timeoutFuture != null) {
/* 189 */       timeoutFuture.cancel(false);
/*     */     }
/*     */     
/* 192 */     Promise<AddressedEnvelope<DnsResponse, InetSocketAddress>> promise = this.promise;
/* 193 */     if (promise.setUncancellable()) {
/*     */ 
/*     */       
/* 196 */       AddressedEnvelope<DnsResponse, InetSocketAddress> castResponse = envelope.retain();
/* 197 */       if (!promise.trySuccess(castResponse))
/*     */       {
/* 199 */         envelope.release(); } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void setFailure(String message, Throwable cause) {
/*     */     DnsNameResolverException e;
/* 205 */     InetSocketAddress nameServerAddr = nameServerAddr();
/* 206 */     this.parent.queryContextManager.remove(nameServerAddr, this.id);
/*     */     
/* 208 */     StringBuilder buf = new StringBuilder(message.length() + 64);
/* 209 */     buf.append('[')
/* 210 */       .append(nameServerAddr)
/* 211 */       .append("] ")
/* 212 */       .append(message)
/* 213 */       .append(" (no stack trace available)");
/*     */ 
/*     */     
/* 216 */     if (cause == null) {
/*     */ 
/*     */       
/* 219 */       e = new DnsNameResolverTimeoutException(nameServerAddr, question(), buf.toString());
/*     */     } else {
/* 221 */       e = new DnsNameResolverException(nameServerAddr, question(), buf.toString(), cause);
/*     */     } 
/* 223 */     this.promise.tryFailure(e);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\resolver\dns\DnsQueryContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */