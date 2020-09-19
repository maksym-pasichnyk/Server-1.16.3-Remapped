/*     */ package org.apache.logging.log4j.core.net.ssl;
/*     */ 
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.security.KeyStore;
/*     */ import java.security.KeyStoreException;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.security.cert.CertificateException;
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
/*     */ public class AbstractKeyStoreConfiguration
/*     */   extends StoreConfiguration<KeyStore>
/*     */ {
/*     */   private final KeyStore keyStore;
/*     */   private final String keyStoreType;
/*     */   
/*     */   public AbstractKeyStoreConfiguration(String location, String password, String keyStoreType) throws StoreConfigurationException {
/*  36 */     super(location, password);
/*  37 */     this.keyStoreType = (keyStoreType == null) ? "JKS" : keyStoreType;
/*  38 */     this.keyStore = load();
/*     */   }
/*     */ 
/*     */   
/*     */   protected KeyStore load() throws StoreConfigurationException {
/*  43 */     LOGGER.debug("Loading keystore from file with params(location={})", getLocation());
/*     */     try {
/*  45 */       if (getLocation() == null) {
/*  46 */         throw new IOException("The location is null");
/*     */       }
/*  48 */       try (FileInputStream fin = new FileInputStream(getLocation())) {
/*  49 */         KeyStore ks = KeyStore.getInstance(this.keyStoreType);
/*  50 */         ks.load(fin, getPasswordAsCharArray());
/*  51 */         LOGGER.debug("Keystore successfully loaded with params(location={})", getLocation());
/*  52 */         return ks;
/*     */       } 
/*  54 */     } catch (CertificateException e) {
/*  55 */       LOGGER.error("No Provider supports a KeyStoreSpi implementation for the specified type" + this.keyStoreType, e);
/*  56 */       throw new StoreConfigurationException(e);
/*  57 */     } catch (NoSuchAlgorithmException e) {
/*  58 */       LOGGER.error("The algorithm used to check the integrity of the keystore cannot be found", e);
/*  59 */       throw new StoreConfigurationException(e);
/*  60 */     } catch (KeyStoreException e) {
/*  61 */       LOGGER.error(e);
/*  62 */       throw new StoreConfigurationException(e);
/*  63 */     } catch (FileNotFoundException e) {
/*  64 */       LOGGER.error("The keystore file(" + getLocation() + ") is not found", e);
/*  65 */       throw new StoreConfigurationException(e);
/*  66 */     } catch (IOException e) {
/*  67 */       LOGGER.error("Something is wrong with the format of the keystore or the given password", e);
/*  68 */       throw new StoreConfigurationException(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public KeyStore getKeyStore() {
/*  73 */     return this.keyStore;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  78 */     int prime = 31;
/*  79 */     int result = super.hashCode();
/*  80 */     result = 31 * result + ((this.keyStore == null) ? 0 : this.keyStore.hashCode());
/*  81 */     result = 31 * result + ((this.keyStoreType == null) ? 0 : this.keyStoreType.hashCode());
/*  82 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/*  87 */     if (this == obj) {
/*  88 */       return true;
/*     */     }
/*  90 */     if (!super.equals(obj)) {
/*  91 */       return false;
/*     */     }
/*  93 */     if (getClass() != obj.getClass()) {
/*  94 */       return false;
/*     */     }
/*  96 */     AbstractKeyStoreConfiguration other = (AbstractKeyStoreConfiguration)obj;
/*  97 */     if (this.keyStore == null) {
/*  98 */       if (other.keyStore != null) {
/*  99 */         return false;
/*     */       }
/* 101 */     } else if (!this.keyStore.equals(other.keyStore)) {
/* 102 */       return false;
/*     */     } 
/* 104 */     if (this.keyStoreType == null) {
/* 105 */       if (other.keyStoreType != null) {
/* 106 */         return false;
/*     */       }
/* 108 */     } else if (!this.keyStoreType.equals(other.keyStoreType)) {
/* 109 */       return false;
/*     */     } 
/* 111 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\net\ssl\AbstractKeyStoreConfiguration.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */