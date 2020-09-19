/*    */ package io.netty.handler.ssl;
/*    */ 
/*    */ import io.netty.buffer.ByteBufAllocator;
/*    */ import java.security.cert.Certificate;
/*    */ import javax.net.ssl.SSLEngine;
/*    */ import javax.net.ssl.SSLException;
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
/*    */ 
/*    */ public abstract class OpenSslContext
/*    */   extends ReferenceCountedOpenSslContext
/*    */ {
/*    */   OpenSslContext(Iterable<String> ciphers, CipherSuiteFilter cipherFilter, ApplicationProtocolConfig apnCfg, long sessionCacheSize, long sessionTimeout, int mode, Certificate[] keyCertChain, ClientAuth clientAuth, String[] protocols, boolean startTls, boolean enableOcsp) throws SSLException {
/* 34 */     super(ciphers, cipherFilter, apnCfg, sessionCacheSize, sessionTimeout, mode, keyCertChain, clientAuth, protocols, startTls, enableOcsp, false);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   OpenSslContext(Iterable<String> ciphers, CipherSuiteFilter cipherFilter, OpenSslApplicationProtocolNegotiator apn, long sessionCacheSize, long sessionTimeout, int mode, Certificate[] keyCertChain, ClientAuth clientAuth, String[] protocols, boolean startTls, boolean enableOcsp) throws SSLException {
/* 43 */     super(ciphers, cipherFilter, apn, sessionCacheSize, sessionTimeout, mode, keyCertChain, clientAuth, protocols, startTls, enableOcsp, false);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   final SSLEngine newEngine0(ByteBufAllocator alloc, String peerHost, int peerPort, boolean jdkCompatibilityMode) {
/* 49 */     return new OpenSslEngine(this, alloc, peerHost, peerPort, jdkCompatibilityMode);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected final void finalize() throws Throwable {
/* 55 */     super.finalize();
/* 56 */     OpenSsl.releaseIfNeeded(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\ssl\OpenSslContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */