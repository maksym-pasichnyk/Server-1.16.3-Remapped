/*    */ package io.netty.resolver.dns;
/*    */ 
/*    */ import io.netty.handler.codec.dns.DnsQuestion;
/*    */ import io.netty.util.internal.ObjectUtil;
/*    */ import io.netty.util.internal.logging.InternalLogLevel;
/*    */ import io.netty.util.internal.logging.InternalLogger;
/*    */ import io.netty.util.internal.logging.InternalLoggerFactory;
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
/*    */ final class TraceDnsQueryLifeCycleObserverFactory
/*    */   implements DnsQueryLifecycleObserverFactory
/*    */ {
/* 27 */   private static final InternalLogger DEFAULT_LOGGER = InternalLoggerFactory.getInstance(TraceDnsQueryLifeCycleObserverFactory.class);
/* 28 */   private static final InternalLogLevel DEFAULT_LEVEL = InternalLogLevel.DEBUG;
/*    */   private final InternalLogger logger;
/*    */   private final InternalLogLevel level;
/*    */   
/*    */   TraceDnsQueryLifeCycleObserverFactory() {
/* 33 */     this(DEFAULT_LOGGER, DEFAULT_LEVEL);
/*    */   }
/*    */   
/*    */   TraceDnsQueryLifeCycleObserverFactory(InternalLogger logger, InternalLogLevel level) {
/* 37 */     this.logger = (InternalLogger)ObjectUtil.checkNotNull(logger, "logger");
/* 38 */     this.level = (InternalLogLevel)ObjectUtil.checkNotNull(level, "level");
/*    */   }
/*    */ 
/*    */   
/*    */   public DnsQueryLifecycleObserver newDnsQueryLifecycleObserver(DnsQuestion question) {
/* 43 */     return new TraceDnsQueryLifecycleObserver(question, this.logger, this.level);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\resolver\dns\TraceDnsQueryLifeCycleObserverFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */