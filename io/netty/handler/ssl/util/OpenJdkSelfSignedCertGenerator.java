/*    */ package io.netty.handler.ssl.util;
/*    */ 
/*    */ import java.math.BigInteger;
/*    */ import java.security.KeyPair;
/*    */ import java.security.PrivateKey;
/*    */ import java.security.SecureRandom;
/*    */ import java.security.cert.CertificateException;
/*    */ import java.util.Date;
/*    */ import sun.security.x509.AlgorithmId;
/*    */ import sun.security.x509.CertificateAlgorithmId;
/*    */ import sun.security.x509.CertificateIssuerName;
/*    */ import sun.security.x509.CertificateSerialNumber;
/*    */ import sun.security.x509.CertificateSubjectName;
/*    */ import sun.security.x509.CertificateValidity;
/*    */ import sun.security.x509.CertificateVersion;
/*    */ import sun.security.x509.CertificateX509Key;
/*    */ import sun.security.x509.X500Name;
/*    */ import sun.security.x509.X509CertImpl;
/*    */ import sun.security.x509.X509CertInfo;
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
/*    */ 
/*    */ final class OpenJdkSelfSignedCertGenerator
/*    */ {
/*    */   static String[] generate(String fqdn, KeyPair keypair, SecureRandom random, Date notBefore, Date notAfter) throws Exception {
/* 47 */     PrivateKey key = keypair.getPrivate();
/*    */ 
/*    */     
/* 50 */     X509CertInfo info = new X509CertInfo();
/* 51 */     X500Name owner = new X500Name("CN=" + fqdn);
/* 52 */     info.set("version", new CertificateVersion(2));
/* 53 */     info.set("serialNumber", new CertificateSerialNumber(new BigInteger(64, random)));
/*    */     try {
/* 55 */       info.set("subject", new CertificateSubjectName(owner));
/* 56 */     } catch (CertificateException ignore) {
/* 57 */       info.set("subject", owner);
/*    */     } 
/*    */     try {
/* 60 */       info.set("issuer", new CertificateIssuerName(owner));
/* 61 */     } catch (CertificateException ignore) {
/* 62 */       info.set("issuer", owner);
/*    */     } 
/* 64 */     info.set("validity", new CertificateValidity(notBefore, notAfter));
/* 65 */     info.set("key", new CertificateX509Key(keypair.getPublic()));
/* 66 */     info.set("algorithmID", new CertificateAlgorithmId(new AlgorithmId(AlgorithmId.sha1WithRSAEncryption_oid)));
/*    */ 
/*    */ 
/*    */     
/* 70 */     X509CertImpl cert = new X509CertImpl(info);
/* 71 */     cert.sign(key, "SHA1withRSA");
/*    */ 
/*    */     
/* 74 */     info.set("algorithmID.algorithm", cert.get("x509.algorithm"));
/* 75 */     cert = new X509CertImpl(info);
/* 76 */     cert.sign(key, "SHA1withRSA");
/* 77 */     cert.verify(keypair.getPublic());
/*    */     
/* 79 */     return SelfSignedCertificate.newSelfSignedCertificate(fqdn, key, cert);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\ss\\util\OpenJdkSelfSignedCertGenerator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */