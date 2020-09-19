/*     */ package io.netty.handler.ssl;
/*     */ 
/*     */ import io.netty.buffer.ByteBufAllocator;
/*     */ import io.netty.internal.tcnative.CertificateRequestedCallback;
/*     */ import io.netty.internal.tcnative.SSL;
/*     */ import java.security.Principal;
/*     */ import java.security.PrivateKey;
/*     */ import java.security.cert.X509Certificate;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import javax.net.ssl.SSLException;
/*     */ import javax.net.ssl.X509KeyManager;
/*     */ import javax.security.auth.x500.X500Principal;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ class OpenSslKeyMaterialManager
/*     */ {
/*     */   static final String KEY_TYPE_RSA = "RSA";
/*     */   static final String KEY_TYPE_DH_RSA = "DH_RSA";
/*     */   static final String KEY_TYPE_EC = "EC";
/*     */   static final String KEY_TYPE_EC_EC = "EC_EC";
/*     */   static final String KEY_TYPE_EC_RSA = "EC_RSA";
/*  54 */   private static final Map<String, String> KEY_TYPES = new HashMap<String, String>();
/*     */   static {
/*  56 */     KEY_TYPES.put("RSA", "RSA");
/*  57 */     KEY_TYPES.put("DHE_RSA", "RSA");
/*  58 */     KEY_TYPES.put("ECDHE_RSA", "RSA");
/*  59 */     KEY_TYPES.put("ECDHE_ECDSA", "EC");
/*  60 */     KEY_TYPES.put("ECDH_RSA", "EC_RSA");
/*  61 */     KEY_TYPES.put("ECDH_ECDSA", "EC_EC");
/*  62 */     KEY_TYPES.put("DH_RSA", "DH_RSA");
/*     */   }
/*     */   
/*     */   private final X509KeyManager keyManager;
/*     */   private final String password;
/*     */   
/*     */   OpenSslKeyMaterialManager(X509KeyManager keyManager, String password) {
/*  69 */     this.keyManager = keyManager;
/*  70 */     this.password = password;
/*     */   }
/*     */   
/*     */   void setKeyMaterial(ReferenceCountedOpenSslEngine engine) throws SSLException {
/*  74 */     long ssl = engine.sslPointer();
/*  75 */     String[] authMethods = SSL.authenticationMethods(ssl);
/*  76 */     Set<String> aliases = new HashSet<String>(authMethods.length);
/*  77 */     for (String authMethod : authMethods) {
/*  78 */       String type = KEY_TYPES.get(authMethod);
/*  79 */       if (type != null) {
/*  80 */         String alias = chooseServerAlias(engine, type);
/*  81 */         if (alias != null && aliases.add(alias)) {
/*  82 */           setKeyMaterial(ssl, alias);
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   CertificateRequestedCallback.KeyMaterial keyMaterial(ReferenceCountedOpenSslEngine engine, String[] keyTypes, X500Principal[] issuer) throws SSLException {
/*  90 */     String alias = chooseClientAlias(engine, keyTypes, issuer);
/*  91 */     long keyBio = 0L;
/*  92 */     long keyCertChainBio = 0L;
/*  93 */     long pkey = 0L;
/*  94 */     long certChain = 0L;
/*     */ 
/*     */     
/*     */     try {
/*  98 */       X509Certificate[] certificates = this.keyManager.getCertificateChain(alias);
/*  99 */       if (certificates == null || certificates.length == 0) {
/* 100 */         return null;
/*     */       }
/*     */       
/* 103 */       PrivateKey key = this.keyManager.getPrivateKey(alias);
/* 104 */       keyCertChainBio = ReferenceCountedOpenSslContext.toBIO(certificates);
/* 105 */       certChain = SSL.parseX509Chain(keyCertChainBio);
/* 106 */       if (key != null) {
/* 107 */         keyBio = ReferenceCountedOpenSslContext.toBIO(key);
/* 108 */         pkey = SSL.parsePrivateKey(keyBio, this.password);
/*     */       } 
/* 110 */       CertificateRequestedCallback.KeyMaterial material = new CertificateRequestedCallback.KeyMaterial(certChain, pkey);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 116 */       certChain = pkey = 0L;
/* 117 */       return material;
/* 118 */     } catch (SSLException e) {
/* 119 */       throw e;
/* 120 */     } catch (Exception e) {
/* 121 */       throw new SSLException(e);
/*     */     } finally {
/* 123 */       ReferenceCountedOpenSslContext.freeBio(keyBio);
/* 124 */       ReferenceCountedOpenSslContext.freeBio(keyCertChainBio);
/* 125 */       SSL.freePrivateKey(pkey);
/* 126 */       SSL.freeX509Chain(certChain);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void setKeyMaterial(long ssl, String alias) throws SSLException {
/* 131 */     long keyBio = 0L;
/* 132 */     long keyCertChainBio = 0L;
/* 133 */     long keyCertChainBio2 = 0L;
/*     */ 
/*     */     
/*     */     try {
/* 137 */       X509Certificate[] certificates = this.keyManager.getCertificateChain(alias);
/* 138 */       if (certificates == null || certificates.length == 0) {
/*     */         return;
/*     */       }
/*     */       
/* 142 */       PrivateKey key = this.keyManager.getPrivateKey(alias);
/*     */ 
/*     */       
/* 145 */       PemEncoded encoded = PemX509Certificate.toPEM(ByteBufAllocator.DEFAULT, true, certificates);
/*     */       try {
/* 147 */         keyCertChainBio = ReferenceCountedOpenSslContext.toBIO(ByteBufAllocator.DEFAULT, encoded.retain());
/* 148 */         keyCertChainBio2 = ReferenceCountedOpenSslContext.toBIO(ByteBufAllocator.DEFAULT, encoded.retain());
/*     */         
/* 150 */         if (key != null) {
/* 151 */           keyBio = ReferenceCountedOpenSslContext.toBIO(key);
/*     */         }
/* 153 */         SSL.setCertificateBio(ssl, keyCertChainBio, keyBio, this.password);
/*     */ 
/*     */         
/* 156 */         SSL.setCertificateChainBio(ssl, keyCertChainBio2, true);
/*     */       } finally {
/* 158 */         encoded.release();
/*     */       } 
/* 160 */     } catch (SSLException e) {
/* 161 */       throw e;
/* 162 */     } catch (Exception e) {
/* 163 */       throw new SSLException(e);
/*     */     } finally {
/* 165 */       ReferenceCountedOpenSslContext.freeBio(keyBio);
/* 166 */       ReferenceCountedOpenSslContext.freeBio(keyCertChainBio);
/* 167 */       ReferenceCountedOpenSslContext.freeBio(keyCertChainBio2);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected String chooseClientAlias(ReferenceCountedOpenSslEngine engine, String[] keyTypes, X500Principal[] issuer) {
/* 173 */     return this.keyManager.chooseClientAlias(keyTypes, (Principal[])issuer, null);
/*     */   }
/*     */   
/*     */   protected String chooseServerAlias(ReferenceCountedOpenSslEngine engine, String type) {
/* 177 */     return this.keyManager.chooseServerAlias(type, null, null);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\ssl\OpenSslKeyMaterialManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */