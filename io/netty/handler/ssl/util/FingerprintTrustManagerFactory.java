/*     */ package io.netty.handler.ssl.util;
/*     */ 
/*     */ import io.netty.buffer.ByteBufUtil;
/*     */ import io.netty.buffer.Unpooled;
/*     */ import io.netty.util.concurrent.FastThreadLocal;
/*     */ import io.netty.util.internal.EmptyArrays;
/*     */ import io.netty.util.internal.StringUtil;
/*     */ import java.security.KeyStore;
/*     */ import java.security.MessageDigest;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.security.cert.CertificateEncodingException;
/*     */ import java.security.cert.CertificateException;
/*     */ import java.security.cert.X509Certificate;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.regex.Pattern;
/*     */ import javax.net.ssl.ManagerFactoryParameters;
/*     */ import javax.net.ssl.TrustManager;
/*     */ import javax.net.ssl.X509TrustManager;
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
/*     */ public final class FingerprintTrustManagerFactory
/*     */   extends SimpleTrustManagerFactory
/*     */ {
/*  75 */   private static final Pattern FINGERPRINT_PATTERN = Pattern.compile("^[0-9a-fA-F:]+$");
/*  76 */   private static final Pattern FINGERPRINT_STRIP_PATTERN = Pattern.compile(":");
/*     */   private static final int SHA1_BYTE_LEN = 20;
/*     */   private static final int SHA1_HEX_LEN = 40;
/*     */   
/*  80 */   private static final FastThreadLocal<MessageDigest> tlmd = new FastThreadLocal<MessageDigest>()
/*     */     {
/*     */       protected MessageDigest initialValue() {
/*     */         try {
/*  84 */           return MessageDigest.getInstance("SHA1");
/*  85 */         } catch (NoSuchAlgorithmException e) {
/*     */           
/*  87 */           throw new Error(e);
/*     */         } 
/*     */       }
/*     */     };
/*     */   
/*  92 */   private final TrustManager tm = new X509TrustManager()
/*     */     {
/*     */       public void checkClientTrusted(X509Certificate[] chain, String s) throws CertificateException
/*     */       {
/*  96 */         checkTrusted("client", chain);
/*     */       }
/*     */ 
/*     */       
/*     */       public void checkServerTrusted(X509Certificate[] chain, String s) throws CertificateException {
/* 101 */         checkTrusted("server", chain);
/*     */       }
/*     */       
/*     */       private void checkTrusted(String type, X509Certificate[] chain) throws CertificateException {
/* 105 */         X509Certificate cert = chain[0];
/* 106 */         byte[] fingerprint = fingerprint(cert);
/* 107 */         boolean found = false;
/* 108 */         for (byte[] allowedFingerprint : FingerprintTrustManagerFactory.this.fingerprints) {
/* 109 */           if (Arrays.equals(fingerprint, allowedFingerprint)) {
/* 110 */             found = true;
/*     */             
/*     */             break;
/*     */           } 
/*     */         } 
/* 115 */         if (!found) {
/* 116 */           throw new CertificateException(type + " certificate with unknown fingerprint: " + cert
/* 117 */               .getSubjectDN());
/*     */         }
/*     */       }
/*     */       
/*     */       private byte[] fingerprint(X509Certificate cert) throws CertificateEncodingException {
/* 122 */         MessageDigest md = (MessageDigest)FingerprintTrustManagerFactory.tlmd.get();
/* 123 */         md.reset();
/* 124 */         return md.digest(cert.getEncoded());
/*     */       }
/*     */ 
/*     */       
/*     */       public X509Certificate[] getAcceptedIssuers() {
/* 129 */         return EmptyArrays.EMPTY_X509_CERTIFICATES;
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final byte[][] fingerprints;
/*     */ 
/*     */ 
/*     */   
/*     */   public FingerprintTrustManagerFactory(Iterable<String> fingerprints) {
/* 141 */     this(toFingerprintArray(fingerprints));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FingerprintTrustManagerFactory(String... fingerprints) {
/* 150 */     this(toFingerprintArray(Arrays.asList(fingerprints)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FingerprintTrustManagerFactory(byte[]... fingerprints) {
/* 159 */     if (fingerprints == null) {
/* 160 */       throw new NullPointerException("fingerprints");
/*     */     }
/*     */     
/* 163 */     List<byte[]> list = (List)new ArrayList<byte>(fingerprints.length);
/* 164 */     for (byte[] f : fingerprints) {
/* 165 */       if (f == null) {
/*     */         break;
/*     */       }
/* 168 */       if (f.length != 20) {
/* 169 */         throw new IllegalArgumentException("malformed fingerprint: " + 
/* 170 */             ByteBufUtil.hexDump(Unpooled.wrappedBuffer(f)) + " (expected: SHA1)");
/*     */       }
/* 172 */       list.add(f.clone());
/*     */     } 
/*     */     
/* 175 */     this.fingerprints = list.<byte[]>toArray(new byte[list.size()][]);
/*     */   }
/*     */   
/*     */   private static byte[][] toFingerprintArray(Iterable<String> fingerprints) {
/* 179 */     if (fingerprints == null) {
/* 180 */       throw new NullPointerException("fingerprints");
/*     */     }
/*     */     
/* 183 */     List<byte[]> list = (List)new ArrayList<byte>();
/* 184 */     for (String f : fingerprints) {
/* 185 */       if (f == null) {
/*     */         break;
/*     */       }
/*     */       
/* 189 */       if (!FINGERPRINT_PATTERN.matcher(f).matches()) {
/* 190 */         throw new IllegalArgumentException("malformed fingerprint: " + f);
/*     */       }
/* 192 */       f = FINGERPRINT_STRIP_PATTERN.matcher(f).replaceAll("");
/* 193 */       if (f.length() != 40) {
/* 194 */         throw new IllegalArgumentException("malformed fingerprint: " + f + " (expected: SHA1)");
/*     */       }
/*     */       
/* 197 */       list.add(StringUtil.decodeHexDump(f));
/*     */     } 
/*     */     
/* 200 */     return list.<byte[]>toArray(new byte[list.size()][]);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void engineInit(KeyStore keyStore) throws Exception {}
/*     */ 
/*     */   
/*     */   protected void engineInit(ManagerFactoryParameters managerFactoryParameters) throws Exception {}
/*     */ 
/*     */   
/*     */   protected TrustManager[] engineGetTrustManagers() {
/* 211 */     return new TrustManager[] { this.tm };
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\ss\\util\FingerprintTrustManagerFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */