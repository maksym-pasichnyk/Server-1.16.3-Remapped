/*    */ package io.netty.resolver.dns;
/*    */ 
/*    */ import io.netty.channel.ChannelFuture;
/*    */ import io.netty.handler.codec.dns.DnsQuestion;
/*    */ import io.netty.handler.codec.dns.DnsResponseCode;
/*    */ import io.netty.util.internal.ObjectUtil;
/*    */ import io.netty.util.internal.logging.InternalLogLevel;
/*    */ import io.netty.util.internal.logging.InternalLogger;
/*    */ import java.net.InetSocketAddress;
/*    */ import java.util.List;
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
/*    */ final class TraceDnsQueryLifecycleObserver
/*    */   implements DnsQueryLifecycleObserver
/*    */ {
/*    */   private final InternalLogger logger;
/*    */   private final InternalLogLevel level;
/*    */   private final DnsQuestion question;
/*    */   private InetSocketAddress dnsServerAddress;
/*    */   
/*    */   TraceDnsQueryLifecycleObserver(DnsQuestion question, InternalLogger logger, InternalLogLevel level) {
/* 36 */     this.question = (DnsQuestion)ObjectUtil.checkNotNull(question, "question");
/* 37 */     this.logger = (InternalLogger)ObjectUtil.checkNotNull(logger, "logger");
/* 38 */     this.level = (InternalLogLevel)ObjectUtil.checkNotNull(level, "level");
/*    */   }
/*    */ 
/*    */   
/*    */   public void queryWritten(InetSocketAddress dnsServerAddress, ChannelFuture future) {
/* 43 */     this.dnsServerAddress = dnsServerAddress;
/*    */   }
/*    */ 
/*    */   
/*    */   public void queryCancelled(int queriesRemaining) {
/* 48 */     if (this.dnsServerAddress != null) {
/* 49 */       this.logger.log(this.level, "from {} : {} cancelled with {} queries remaining", new Object[] { this.dnsServerAddress, this.question, 
/* 50 */             Integer.valueOf(queriesRemaining) });
/*    */     } else {
/* 52 */       this.logger.log(this.level, "{} query never written and cancelled with {} queries remaining", this.question, 
/* 53 */           Integer.valueOf(queriesRemaining));
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public DnsQueryLifecycleObserver queryRedirected(List<InetSocketAddress> nameServers) {
/* 59 */     this.logger.log(this.level, "from {} : {} redirected", this.dnsServerAddress, this.question);
/* 60 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public DnsQueryLifecycleObserver queryCNAMEd(DnsQuestion cnameQuestion) {
/* 65 */     this.logger.log(this.level, "from {} : {} CNAME question {}", new Object[] { this.dnsServerAddress, this.question, cnameQuestion });
/* 66 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public DnsQueryLifecycleObserver queryNoAnswer(DnsResponseCode code) {
/* 71 */     this.logger.log(this.level, "from {} : {} no answer {}", new Object[] { this.dnsServerAddress, this.question, code });
/* 72 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public void queryFailed(Throwable cause) {
/* 77 */     if (this.dnsServerAddress != null) {
/* 78 */       this.logger.log(this.level, "from {} : {} failure", new Object[] { this.dnsServerAddress, this.question, cause });
/*    */     } else {
/* 80 */       this.logger.log(this.level, "{} query never written and failed", this.question, cause);
/*    */     } 
/*    */   }
/*    */   
/*    */   public void querySucceed() {}
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\resolver\dns\TraceDnsQueryLifecycleObserver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */