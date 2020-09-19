/*     */ package io.netty.handler.ssl;
/*     */ 
/*     */ import io.netty.channel.ChannelHandler;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.handler.codec.DecoderException;
/*     */ import io.netty.util.AsyncMapping;
/*     */ import io.netty.util.DomainNameMapping;
/*     */ import io.netty.util.Mapping;
/*     */ import io.netty.util.ReferenceCountUtil;
/*     */ import io.netty.util.concurrent.Future;
/*     */ import io.netty.util.concurrent.Promise;
/*     */ import io.netty.util.internal.ObjectUtil;
/*     */ import io.netty.util.internal.PlatformDependent;
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
/*     */ public class SniHandler
/*     */   extends AbstractSniHandler<SslContext>
/*     */ {
/*  37 */   private static final Selection EMPTY_SELECTION = new Selection(null, null);
/*     */   
/*     */   protected final AsyncMapping<String, SslContext> mapping;
/*     */   
/*  41 */   private volatile Selection selection = EMPTY_SELECTION;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SniHandler(Mapping<? super String, ? extends SslContext> mapping) {
/*  50 */     this(new AsyncMappingAdapter(mapping, null));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SniHandler(DomainNameMapping<? extends SslContext> mapping) {
/*  60 */     this((Mapping)mapping);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SniHandler(AsyncMapping<? super String, ? extends SslContext> mapping) {
/*  71 */     this.mapping = (AsyncMapping<String, SslContext>)ObjectUtil.checkNotNull(mapping, "mapping");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String hostname() {
/*  78 */     return this.selection.hostname;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SslContext sslContext() {
/*  85 */     return this.selection.context;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Future<SslContext> lookup(ChannelHandlerContext ctx, String hostname) throws Exception {
/*  96 */     return this.mapping.map(hostname, ctx.executor().newPromise());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void onLookupComplete(ChannelHandlerContext ctx, String hostname, Future<SslContext> future) throws Exception {
/* 102 */     if (!future.isSuccess()) {
/* 103 */       Throwable cause = future.cause();
/* 104 */       if (cause instanceof Error) {
/* 105 */         throw (Error)cause;
/*     */       }
/* 107 */       throw new DecoderException("failed to get the SslContext for " + hostname, cause);
/*     */     } 
/*     */     
/* 110 */     SslContext sslContext = (SslContext)future.getNow();
/* 111 */     this.selection = new Selection(sslContext, hostname);
/*     */     try {
/* 113 */       replaceHandler(ctx, hostname, sslContext);
/* 114 */     } catch (Throwable cause) {
/* 115 */       this.selection = EMPTY_SELECTION;
/* 116 */       PlatformDependent.throwException(cause);
/*     */     } 
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
/*     */   protected void replaceHandler(ChannelHandlerContext ctx, String hostname, SslContext sslContext) throws Exception {
/* 130 */     SslHandler sslHandler = null;
/*     */     try {
/* 132 */       sslHandler = sslContext.newHandler(ctx.alloc());
/* 133 */       ctx.pipeline().replace((ChannelHandler)this, SslHandler.class.getName(), (ChannelHandler)sslHandler);
/* 134 */       sslHandler = null;
/*     */     
/*     */     }
/*     */     finally {
/*     */       
/* 139 */       if (sslHandler != null)
/* 140 */         ReferenceCountUtil.safeRelease(sslHandler.engine()); 
/*     */     } 
/*     */   }
/*     */   
/*     */   private static final class AsyncMappingAdapter
/*     */     implements AsyncMapping<String, SslContext> {
/*     */     private final Mapping<? super String, ? extends SslContext> mapping;
/*     */     
/*     */     private AsyncMappingAdapter(Mapping<? super String, ? extends SslContext> mapping) {
/* 149 */       this.mapping = (Mapping<? super String, ? extends SslContext>)ObjectUtil.checkNotNull(mapping, "mapping");
/*     */     }
/*     */ 
/*     */     
/*     */     public Future<SslContext> map(String input, Promise<SslContext> promise) {
/*     */       SslContext context;
/*     */       try {
/* 156 */         context = (SslContext)this.mapping.map(input);
/* 157 */       } catch (Throwable cause) {
/* 158 */         return (Future<SslContext>)promise.setFailure(cause);
/*     */       } 
/* 160 */       return (Future<SslContext>)promise.setSuccess(context);
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class Selection {
/*     */     final SslContext context;
/*     */     final String hostname;
/*     */     
/*     */     Selection(SslContext context, String hostname) {
/* 169 */       this.context = context;
/* 170 */       this.hostname = hostname;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\ssl\SniHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */