/*     */ package io.netty.handler.ssl.util;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.Unpooled;
/*     */ import io.netty.handler.codec.base64.Base64;
/*     */ import io.netty.util.CharsetUtil;
/*     */ import io.netty.util.internal.SystemPropertyUtil;
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.security.KeyPair;
/*     */ import java.security.KeyPairGenerator;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.security.PrivateKey;
/*     */ import java.security.SecureRandom;
/*     */ import java.security.cert.CertificateEncodingException;
/*     */ import java.security.cert.CertificateException;
/*     */ import java.security.cert.CertificateFactory;
/*     */ import java.security.cert.X509Certificate;
/*     */ import java.util.Date;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class SelfSignedCertificate
/*     */ {
/*  61 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(SelfSignedCertificate.class);
/*     */ 
/*     */   
/*  64 */   private static final Date DEFAULT_NOT_BEFORE = new Date(SystemPropertyUtil.getLong("io.netty.selfSignedCertificate.defaultNotBefore", 
/*  65 */         System.currentTimeMillis() - 31536000000L));
/*     */   
/*  67 */   private static final Date DEFAULT_NOT_AFTER = new Date(SystemPropertyUtil.getLong("io.netty.selfSignedCertificate.defaultNotAfter", 253402300799000L));
/*     */   
/*     */   private final File certificate;
/*     */   
/*     */   private final File privateKey;
/*     */   
/*     */   private final X509Certificate cert;
/*     */   
/*     */   private final PrivateKey key;
/*     */ 
/*     */   
/*     */   public SelfSignedCertificate() throws CertificateException {
/*  79 */     this(DEFAULT_NOT_BEFORE, DEFAULT_NOT_AFTER);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SelfSignedCertificate(Date notBefore, Date notAfter) throws CertificateException {
/*  88 */     this("example.com", notBefore, notAfter);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SelfSignedCertificate(String fqdn) throws CertificateException {
/*  97 */     this(fqdn, DEFAULT_NOT_BEFORE, DEFAULT_NOT_AFTER);
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
/*     */   public SelfSignedCertificate(String fqdn, Date notBefore, Date notAfter) throws CertificateException {
/* 110 */     this(fqdn, ThreadLocalInsecureRandom.current(), 1024, notBefore, notAfter);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SelfSignedCertificate(String fqdn, SecureRandom random, int bits) throws CertificateException {
/* 121 */     this(fqdn, random, bits, DEFAULT_NOT_BEFORE, DEFAULT_NOT_AFTER);
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
/*     */   public SelfSignedCertificate(String fqdn, SecureRandom random, int bits, Date notBefore, Date notAfter) throws CertificateException {
/*     */     KeyPair keypair;
/*     */     String[] paths;
/*     */     try {
/* 138 */       KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
/* 139 */       keyGen.initialize(bits, random);
/* 140 */       keypair = keyGen.generateKeyPair();
/* 141 */     } catch (NoSuchAlgorithmException e) {
/*     */       
/* 143 */       throw new Error(e);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 149 */       paths = OpenJdkSelfSignedCertGenerator.generate(fqdn, keypair, random, notBefore, notAfter);
/* 150 */     } catch (Throwable t) {
/* 151 */       logger.debug("Failed to generate a self-signed X.509 certificate using sun.security.x509:", t);
/*     */       
/*     */       try {
/* 154 */         paths = BouncyCastleSelfSignedCertGenerator.generate(fqdn, keypair, random, notBefore, notAfter);
/* 155 */       } catch (Throwable t2) {
/* 156 */         logger.debug("Failed to generate a self-signed X.509 certificate using Bouncy Castle:", t2);
/* 157 */         throw new CertificateException("No provider succeeded to generate a self-signed certificate. See debug log for the root cause.", t2);
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 164 */     this.certificate = new File(paths[0]);
/* 165 */     this.privateKey = new File(paths[1]);
/* 166 */     this.key = keypair.getPrivate();
/* 167 */     FileInputStream certificateInput = null;
/*     */     try {
/* 169 */       certificateInput = new FileInputStream(this.certificate);
/* 170 */       this.cert = (X509Certificate)CertificateFactory.getInstance("X509").generateCertificate(certificateInput);
/* 171 */     } catch (Exception e) {
/* 172 */       throw new CertificateEncodingException(e);
/*     */     } finally {
/* 174 */       if (certificateInput != null) {
/*     */         try {
/* 176 */           certificateInput.close();
/* 177 */         } catch (IOException e) {
/* 178 */           logger.warn("Failed to close a file: " + this.certificate, e);
/*     */         } 
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public File certificate() {
/* 188 */     return this.certificate;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public File privateKey() {
/* 195 */     return this.privateKey;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public X509Certificate cert() {
/* 202 */     return this.cert;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PrivateKey key() {
/* 209 */     return this.key;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void delete() {
/* 216 */     safeDelete(this.certificate);
/* 217 */     safeDelete(this.privateKey);
/*     */   }
/*     */ 
/*     */   
/*     */   static String[] newSelfSignedCertificate(String fqdn, PrivateKey key, X509Certificate cert) throws IOException, CertificateEncodingException {
/*     */     String keyText, certText;
/* 223 */     ByteBuf wrappedBuf = Unpooled.wrappedBuffer(key.getEncoded());
/*     */ 
/*     */     
/*     */     try {
/* 227 */       ByteBuf encodedBuf = Base64.encode(wrappedBuf, true);
/*     */       
/*     */       try {
/* 230 */         keyText = "-----BEGIN PRIVATE KEY-----\n" + encodedBuf.toString(CharsetUtil.US_ASCII) + "\n-----END PRIVATE KEY-----\n";
/*     */       } finally {
/*     */         
/* 233 */         encodedBuf.release();
/*     */       } 
/*     */     } finally {
/* 236 */       wrappedBuf.release();
/*     */     } 
/*     */     
/* 239 */     File keyFile = File.createTempFile("keyutil_" + fqdn + '_', ".key");
/* 240 */     keyFile.deleteOnExit();
/*     */     
/* 242 */     OutputStream keyOut = new FileOutputStream(keyFile);
/*     */     try {
/* 244 */       keyOut.write(keyText.getBytes(CharsetUtil.US_ASCII));
/* 245 */       keyOut.close();
/* 246 */       keyOut = null;
/*     */     } finally {
/* 248 */       if (keyOut != null) {
/* 249 */         safeClose(keyFile, keyOut);
/* 250 */         safeDelete(keyFile);
/*     */       } 
/*     */     } 
/*     */     
/* 254 */     wrappedBuf = Unpooled.wrappedBuffer(cert.getEncoded());
/*     */     
/*     */     try {
/* 257 */       ByteBuf encodedBuf = Base64.encode(wrappedBuf, true);
/*     */ 
/*     */       
/*     */       try {
/* 261 */         certText = "-----BEGIN CERTIFICATE-----\n" + encodedBuf.toString(CharsetUtil.US_ASCII) + "\n-----END CERTIFICATE-----\n";
/*     */       } finally {
/*     */         
/* 264 */         encodedBuf.release();
/*     */       } 
/*     */     } finally {
/* 267 */       wrappedBuf.release();
/*     */     } 
/*     */     
/* 270 */     File certFile = File.createTempFile("keyutil_" + fqdn + '_', ".crt");
/* 271 */     certFile.deleteOnExit();
/*     */     
/* 273 */     OutputStream certOut = new FileOutputStream(certFile);
/*     */     try {
/* 275 */       certOut.write(certText.getBytes(CharsetUtil.US_ASCII));
/* 276 */       certOut.close();
/* 277 */       certOut = null;
/*     */     } finally {
/* 279 */       if (certOut != null) {
/* 280 */         safeClose(certFile, certOut);
/* 281 */         safeDelete(certFile);
/* 282 */         safeDelete(keyFile);
/*     */       } 
/*     */     } 
/*     */     
/* 286 */     return new String[] { certFile.getPath(), keyFile.getPath() };
/*     */   }
/*     */   
/*     */   private static void safeDelete(File certFile) {
/* 290 */     if (!certFile.delete()) {
/* 291 */       logger.warn("Failed to delete a file: " + certFile);
/*     */     }
/*     */   }
/*     */   
/*     */   private static void safeClose(File keyFile, OutputStream keyOut) {
/*     */     try {
/* 297 */       keyOut.close();
/* 298 */     } catch (IOException e) {
/* 299 */       logger.warn("Failed to close a file: " + keyFile, e);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\ss\\util\SelfSignedCertificate.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */