/*     */ package net.minecraft.util;
/*     */ 
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.security.GeneralSecurityException;
/*     */ import java.security.InvalidKeyException;
/*     */ import java.security.Key;
/*     */ import java.security.KeyFactory;
/*     */ import java.security.KeyPair;
/*     */ import java.security.KeyPairGenerator;
/*     */ import java.security.MessageDigest;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.security.PrivateKey;
/*     */ import java.security.PublicKey;
/*     */ import java.security.spec.EncodedKeySpec;
/*     */ import java.security.spec.InvalidKeySpecException;
/*     */ import java.security.spec.X509EncodedKeySpec;
/*     */ import javax.crypto.BadPaddingException;
/*     */ import javax.crypto.Cipher;
/*     */ import javax.crypto.IllegalBlockSizeException;
/*     */ import javax.crypto.NoSuchPaddingException;
/*     */ import javax.crypto.SecretKey;
/*     */ import javax.crypto.spec.IvParameterSpec;
/*     */ import javax.crypto.spec.SecretKeySpec;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ 
/*     */ public class Crypt
/*     */ {
/*  30 */   private static final Logger LOGGER = LogManager.getLogger();
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
/*     */   public static KeyPair generateKeyPair() {
/*     */     try {
/*  51 */       KeyPairGenerator debug0 = KeyPairGenerator.getInstance("RSA");
/*  52 */       debug0.initialize(1024);
/*     */       
/*  54 */       return debug0.generateKeyPair();
/*  55 */     } catch (NoSuchAlgorithmException debug0) {
/*  56 */       debug0.printStackTrace();
/*     */       
/*  58 */       LOGGER.error("Key pair generation failed!");
/*  59 */       return null;
/*     */     } 
/*     */   }
/*     */   public static byte[] digestData(String debug0, PublicKey debug1, SecretKey debug2) {
/*     */     try {
/*  64 */       return digestData("SHA-1", new byte[][] { debug0
/*     */             
/*  66 */             .getBytes("ISO_8859_1"), debug2
/*  67 */             .getEncoded(), debug1
/*  68 */             .getEncoded() });
/*     */     }
/*  70 */     catch (UnsupportedEncodingException debug3) {
/*  71 */       debug3.printStackTrace();
/*     */ 
/*     */       
/*  74 */       return null;
/*     */     } 
/*     */   }
/*     */   private static byte[] digestData(String debug0, byte[]... debug1) {
/*     */     try {
/*  79 */       MessageDigest debug2 = MessageDigest.getInstance(debug0);
/*  80 */       for (byte[] debug6 : debug1) {
/*  81 */         debug2.update(debug6);
/*     */       }
/*  83 */       return debug2.digest();
/*  84 */     } catch (NoSuchAlgorithmException debug2) {
/*  85 */       debug2.printStackTrace();
/*     */ 
/*     */       
/*  88 */       return null;
/*     */     } 
/*     */   }
/*     */   public static PublicKey byteToPublicKey(byte[] debug0) {
/*     */     
/*  93 */     try { EncodedKeySpec debug1 = new X509EncodedKeySpec(debug0);
/*  94 */       KeyFactory debug2 = KeyFactory.getInstance("RSA");
/*  95 */       return debug2.generatePublic(debug1); }
/*  96 */     catch (NoSuchAlgorithmException noSuchAlgorithmException) {  }
/*  97 */     catch (InvalidKeySpecException invalidKeySpecException) {}
/*     */     
/*  99 */     LOGGER.error("Public key reconstitute failed!");
/* 100 */     return null;
/*     */   }
/*     */   
/*     */   public static SecretKey decryptByteToSecretKey(PrivateKey debug0, byte[] debug1) {
/* 104 */     return new SecretKeySpec(decryptUsingKey(debug0, debug1), "AES");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte[] decryptUsingKey(Key debug0, byte[] debug1) {
/* 112 */     return cipherData(2, debug0, debug1);
/*     */   }
/*     */   
/*     */   private static byte[] cipherData(int debug0, Key debug1, byte[] debug2) {
/*     */     try {
/* 117 */       return setupCipher(debug0, debug1.getAlgorithm(), debug1).doFinal(debug2);
/* 118 */     } catch (IllegalBlockSizeException debug3) {
/* 119 */       debug3.printStackTrace();
/* 120 */     } catch (BadPaddingException debug3) {
/* 121 */       debug3.printStackTrace();
/*     */     } 
/* 123 */     LOGGER.error("Cipher data failed!");
/* 124 */     return null;
/*     */   }
/*     */   
/*     */   private static Cipher setupCipher(int debug0, String debug1, Key debug2) {
/*     */     try {
/* 129 */       Cipher debug3 = Cipher.getInstance(debug1);
/* 130 */       debug3.init(debug0, debug2);
/* 131 */       return debug3;
/* 132 */     } catch (InvalidKeyException debug3) {
/* 133 */       debug3.printStackTrace();
/* 134 */     } catch (NoSuchAlgorithmException debug3) {
/* 135 */       debug3.printStackTrace();
/* 136 */     } catch (NoSuchPaddingException debug3) {
/* 137 */       debug3.printStackTrace();
/*     */     } 
/* 139 */     LOGGER.error("Cipher creation failed!");
/* 140 */     return null;
/*     */   }
/*     */   
/*     */   public static Cipher getCipher(int debug0, Key debug1) {
/*     */     try {
/* 145 */       Cipher debug2 = Cipher.getInstance("AES/CFB8/NoPadding");
/* 146 */       debug2.init(debug0, debug1, new IvParameterSpec(debug1.getEncoded()));
/* 147 */       return debug2;
/* 148 */     } catch (GeneralSecurityException debug2) {
/* 149 */       throw new RuntimeException(debug2);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\Crypt.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */