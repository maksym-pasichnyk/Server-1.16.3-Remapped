/*    */ package io.netty.resolver.dns;
/*    */ 
/*    */ import io.netty.handler.codec.dns.DnsQuestion;
/*    */ import io.netty.util.internal.EmptyArrays;
/*    */ import io.netty.util.internal.ObjectUtil;
/*    */ import java.net.InetSocketAddress;
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
/*    */ public class DnsNameResolverException
/*    */   extends RuntimeException
/*    */ {
/*    */   private static final long serialVersionUID = -8826717909627131850L;
/*    */   private final InetSocketAddress remoteAddress;
/*    */   private final DnsQuestion question;
/*    */   
/*    */   public DnsNameResolverException(InetSocketAddress remoteAddress, DnsQuestion question, String message) {
/* 37 */     super(message);
/* 38 */     this.remoteAddress = validateRemoteAddress(remoteAddress);
/* 39 */     this.question = validateQuestion(question);
/*    */   }
/*    */ 
/*    */   
/*    */   public DnsNameResolverException(InetSocketAddress remoteAddress, DnsQuestion question, String message, Throwable cause) {
/* 44 */     super(message, cause);
/* 45 */     this.remoteAddress = validateRemoteAddress(remoteAddress);
/* 46 */     this.question = validateQuestion(question);
/*    */   }
/*    */   
/*    */   private static InetSocketAddress validateRemoteAddress(InetSocketAddress remoteAddress) {
/* 50 */     return (InetSocketAddress)ObjectUtil.checkNotNull(remoteAddress, "remoteAddress");
/*    */   }
/*    */   
/*    */   private static DnsQuestion validateQuestion(DnsQuestion question) {
/* 54 */     return (DnsQuestion)ObjectUtil.checkNotNull(question, "question");
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public InetSocketAddress remoteAddress() {
/* 61 */     return this.remoteAddress;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public DnsQuestion question() {
/* 68 */     return this.question;
/*    */   }
/*    */ 
/*    */   
/*    */   public Throwable fillInStackTrace() {
/* 73 */     setStackTrace(EmptyArrays.EMPTY_STACK_TRACE);
/* 74 */     return this;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\resolver\dns\DnsNameResolverException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */