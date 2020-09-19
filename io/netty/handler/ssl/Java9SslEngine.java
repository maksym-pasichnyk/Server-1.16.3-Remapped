/*     */ package io.netty.handler.ssl;
/*     */ 
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.function.BiFunction;
/*     */ import javax.net.ssl.SSLEngine;
/*     */ import javax.net.ssl.SSLEngineResult;
/*     */ import javax.net.ssl.SSLException;
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
/*     */ final class Java9SslEngine
/*     */   extends JdkSslEngine
/*     */ {
/*     */   private final JdkApplicationProtocolNegotiator.ProtocolSelectionListener selectionListener;
/*     */   private final AlpnSelector alpnSelector;
/*     */   
/*     */   private final class AlpnSelector
/*     */     implements BiFunction<SSLEngine, List<String>, String>
/*     */   {
/*     */     private final JdkApplicationProtocolNegotiator.ProtocolSelector selector;
/*     */     private boolean called;
/*     */     
/*     */     AlpnSelector(JdkApplicationProtocolNegotiator.ProtocolSelector selector) {
/*  42 */       this.selector = selector;
/*     */     }
/*     */ 
/*     */     
/*     */     public String apply(SSLEngine sslEngine, List<String> strings) {
/*  47 */       assert !this.called;
/*  48 */       this.called = true;
/*     */       
/*     */       try {
/*  51 */         String selected = this.selector.select(strings);
/*  52 */         return (selected == null) ? "" : selected;
/*  53 */       } catch (Exception cause) {
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*  58 */         return null;
/*     */       } 
/*     */     }
/*     */     
/*     */     void checkUnsupported() {
/*  63 */       if (this.called) {
/*     */         return;
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  70 */       String protocol = Java9SslEngine.this.getApplicationProtocol();
/*  71 */       assert protocol != null;
/*     */       
/*  73 */       if (protocol.isEmpty())
/*     */       {
/*  75 */         this.selector.unsupported();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   Java9SslEngine(SSLEngine engine, JdkApplicationProtocolNegotiator applicationNegotiator, boolean isServer) {
/*  81 */     super(engine);
/*  82 */     if (isServer) {
/*  83 */       this.selectionListener = null;
/*  84 */       this
/*  85 */         .alpnSelector = new AlpnSelector(applicationNegotiator.protocolSelectorFactory().newSelector(this, new LinkedHashSet<String>(applicationNegotiator.protocols())));
/*  86 */       Java9SslUtils.setHandshakeApplicationProtocolSelector(engine, this.alpnSelector);
/*     */     } else {
/*  88 */       this
/*  89 */         .selectionListener = applicationNegotiator.protocolListenerFactory().newListener(this, applicationNegotiator.protocols());
/*  90 */       this.alpnSelector = null;
/*  91 */       Java9SslUtils.setApplicationProtocols(engine, applicationNegotiator.protocols());
/*     */     } 
/*     */   }
/*     */   
/*     */   private SSLEngineResult verifyProtocolSelection(SSLEngineResult result) throws SSLException {
/*  96 */     if (result.getHandshakeStatus() == SSLEngineResult.HandshakeStatus.FINISHED) {
/*  97 */       if (this.alpnSelector == null) {
/*     */         
/*     */         try {
/* 100 */           String protocol = getApplicationProtocol();
/* 101 */           assert protocol != null;
/* 102 */           if (protocol.isEmpty()) {
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 107 */             this.selectionListener.unsupported();
/*     */           } else {
/* 109 */             this.selectionListener.selected(protocol);
/*     */           } 
/* 111 */         } catch (Throwable e) {
/* 112 */           throw SslUtils.toSSLHandshakeException(e);
/*     */         } 
/*     */       } else {
/* 115 */         assert this.selectionListener == null;
/* 116 */         this.alpnSelector.checkUnsupported();
/*     */       } 
/*     */     }
/* 119 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public SSLEngineResult wrap(ByteBuffer src, ByteBuffer dst) throws SSLException {
/* 124 */     return verifyProtocolSelection(super.wrap(src, dst));
/*     */   }
/*     */ 
/*     */   
/*     */   public SSLEngineResult wrap(ByteBuffer[] srcs, ByteBuffer dst) throws SSLException {
/* 129 */     return verifyProtocolSelection(super.wrap(srcs, dst));
/*     */   }
/*     */ 
/*     */   
/*     */   public SSLEngineResult wrap(ByteBuffer[] srcs, int offset, int len, ByteBuffer dst) throws SSLException {
/* 134 */     return verifyProtocolSelection(super.wrap(srcs, offset, len, dst));
/*     */   }
/*     */ 
/*     */   
/*     */   public SSLEngineResult unwrap(ByteBuffer src, ByteBuffer dst) throws SSLException {
/* 139 */     return verifyProtocolSelection(super.unwrap(src, dst));
/*     */   }
/*     */ 
/*     */   
/*     */   public SSLEngineResult unwrap(ByteBuffer src, ByteBuffer[] dsts) throws SSLException {
/* 144 */     return verifyProtocolSelection(super.unwrap(src, dsts));
/*     */   }
/*     */ 
/*     */   
/*     */   public SSLEngineResult unwrap(ByteBuffer src, ByteBuffer[] dst, int offset, int len) throws SSLException {
/* 149 */     return verifyProtocolSelection(super.unwrap(src, dst, offset, len));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   void setNegotiatedApplicationProtocol(String applicationProtocol) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public String getNegotiatedApplicationProtocol() {
/* 159 */     String protocol = getApplicationProtocol();
/* 160 */     if (protocol != null) {
/* 161 */       return protocol.isEmpty() ? null : protocol;
/*     */     }
/* 163 */     return protocol;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getApplicationProtocol() {
/* 169 */     return Java9SslUtils.getApplicationProtocol(getWrappedEngine());
/*     */   }
/*     */   
/*     */   public String getHandshakeApplicationProtocol() {
/* 173 */     return Java9SslUtils.getHandshakeApplicationProtocol(getWrappedEngine());
/*     */   }
/*     */   
/*     */   public void setHandshakeApplicationProtocolSelector(BiFunction<SSLEngine, List<String>, String> selector) {
/* 177 */     Java9SslUtils.setHandshakeApplicationProtocolSelector(getWrappedEngine(), selector);
/*     */   }
/*     */   
/*     */   public BiFunction<SSLEngine, List<String>, String> getHandshakeApplicationProtocolSelector() {
/* 181 */     return Java9SslUtils.getHandshakeApplicationProtocolSelector(getWrappedEngine());
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\ssl\Java9SslEngine.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */