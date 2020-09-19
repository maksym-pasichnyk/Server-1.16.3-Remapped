/*     */ package io.netty.handler.ssl;
/*     */ 
/*     */ import java.nio.ByteBuffer;
/*     */ import javax.net.ssl.SSLEngine;
/*     */ import javax.net.ssl.SSLEngineResult;
/*     */ import javax.net.ssl.SSLException;
/*     */ import javax.net.ssl.SSLParameters;
/*     */ import javax.net.ssl.SSLSession;
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
/*     */ class JdkSslEngine
/*     */   extends SSLEngine
/*     */   implements ApplicationProtocolAccessor
/*     */ {
/*     */   private final SSLEngine engine;
/*     */   private volatile String applicationProtocol;
/*     */   
/*     */   JdkSslEngine(SSLEngine engine) {
/*  32 */     this.engine = engine;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getNegotiatedApplicationProtocol() {
/*  37 */     return this.applicationProtocol;
/*     */   }
/*     */   
/*     */   void setNegotiatedApplicationProtocol(String applicationProtocol) {
/*  41 */     this.applicationProtocol = applicationProtocol;
/*     */   }
/*     */ 
/*     */   
/*     */   public SSLSession getSession() {
/*  46 */     return this.engine.getSession();
/*     */   }
/*     */   
/*     */   public SSLEngine getWrappedEngine() {
/*  50 */     return this.engine;
/*     */   }
/*     */ 
/*     */   
/*     */   public void closeInbound() throws SSLException {
/*  55 */     this.engine.closeInbound();
/*     */   }
/*     */ 
/*     */   
/*     */   public void closeOutbound() {
/*  60 */     this.engine.closeOutbound();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getPeerHost() {
/*  65 */     return this.engine.getPeerHost();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getPeerPort() {
/*  70 */     return this.engine.getPeerPort();
/*     */   }
/*     */ 
/*     */   
/*     */   public SSLEngineResult wrap(ByteBuffer byteBuffer, ByteBuffer byteBuffer2) throws SSLException {
/*  75 */     return this.engine.wrap(byteBuffer, byteBuffer2);
/*     */   }
/*     */ 
/*     */   
/*     */   public SSLEngineResult wrap(ByteBuffer[] byteBuffers, ByteBuffer byteBuffer) throws SSLException {
/*  80 */     return this.engine.wrap(byteBuffers, byteBuffer);
/*     */   }
/*     */ 
/*     */   
/*     */   public SSLEngineResult wrap(ByteBuffer[] byteBuffers, int i, int i2, ByteBuffer byteBuffer) throws SSLException {
/*  85 */     return this.engine.wrap(byteBuffers, i, i2, byteBuffer);
/*     */   }
/*     */ 
/*     */   
/*     */   public SSLEngineResult unwrap(ByteBuffer byteBuffer, ByteBuffer byteBuffer2) throws SSLException {
/*  90 */     return this.engine.unwrap(byteBuffer, byteBuffer2);
/*     */   }
/*     */ 
/*     */   
/*     */   public SSLEngineResult unwrap(ByteBuffer byteBuffer, ByteBuffer[] byteBuffers) throws SSLException {
/*  95 */     return this.engine.unwrap(byteBuffer, byteBuffers);
/*     */   }
/*     */ 
/*     */   
/*     */   public SSLEngineResult unwrap(ByteBuffer byteBuffer, ByteBuffer[] byteBuffers, int i, int i2) throws SSLException {
/* 100 */     return this.engine.unwrap(byteBuffer, byteBuffers, i, i2);
/*     */   }
/*     */ 
/*     */   
/*     */   public Runnable getDelegatedTask() {
/* 105 */     return this.engine.getDelegatedTask();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isInboundDone() {
/* 110 */     return this.engine.isInboundDone();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isOutboundDone() {
/* 115 */     return this.engine.isOutboundDone();
/*     */   }
/*     */ 
/*     */   
/*     */   public String[] getSupportedCipherSuites() {
/* 120 */     return this.engine.getSupportedCipherSuites();
/*     */   }
/*     */ 
/*     */   
/*     */   public String[] getEnabledCipherSuites() {
/* 125 */     return this.engine.getEnabledCipherSuites();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setEnabledCipherSuites(String[] strings) {
/* 130 */     this.engine.setEnabledCipherSuites(strings);
/*     */   }
/*     */ 
/*     */   
/*     */   public String[] getSupportedProtocols() {
/* 135 */     return this.engine.getSupportedProtocols();
/*     */   }
/*     */ 
/*     */   
/*     */   public String[] getEnabledProtocols() {
/* 140 */     return this.engine.getEnabledProtocols();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setEnabledProtocols(String[] strings) {
/* 145 */     this.engine.setEnabledProtocols(strings);
/*     */   }
/*     */ 
/*     */   
/*     */   public SSLSession getHandshakeSession() {
/* 150 */     return this.engine.getHandshakeSession();
/*     */   }
/*     */ 
/*     */   
/*     */   public void beginHandshake() throws SSLException {
/* 155 */     this.engine.beginHandshake();
/*     */   }
/*     */ 
/*     */   
/*     */   public SSLEngineResult.HandshakeStatus getHandshakeStatus() {
/* 160 */     return this.engine.getHandshakeStatus();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setUseClientMode(boolean b) {
/* 165 */     this.engine.setUseClientMode(b);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean getUseClientMode() {
/* 170 */     return this.engine.getUseClientMode();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setNeedClientAuth(boolean b) {
/* 175 */     this.engine.setNeedClientAuth(b);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean getNeedClientAuth() {
/* 180 */     return this.engine.getNeedClientAuth();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setWantClientAuth(boolean b) {
/* 185 */     this.engine.setWantClientAuth(b);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean getWantClientAuth() {
/* 190 */     return this.engine.getWantClientAuth();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setEnableSessionCreation(boolean b) {
/* 195 */     this.engine.setEnableSessionCreation(b);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean getEnableSessionCreation() {
/* 200 */     return this.engine.getEnableSessionCreation();
/*     */   }
/*     */ 
/*     */   
/*     */   public SSLParameters getSSLParameters() {
/* 205 */     return this.engine.getSSLParameters();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setSSLParameters(SSLParameters sslParameters) {
/* 210 */     this.engine.setSSLParameters(sslParameters);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\ssl\JdkSslEngine.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */