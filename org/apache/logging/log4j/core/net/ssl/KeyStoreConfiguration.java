/*     */ package org.apache.logging.log4j.core.net.ssl;
/*     */ 
/*     */ import java.security.KeyStoreException;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.security.UnrecoverableKeyException;
/*     */ import javax.net.ssl.KeyManagerFactory;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginFactory;
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
/*     */ @Plugin(name = "KeyStore", category = "Core", printObject = true)
/*     */ public class KeyStoreConfiguration
/*     */   extends AbstractKeyStoreConfiguration
/*     */ {
/*     */   private final String keyManagerFactoryAlgorithm;
/*     */   
/*     */   public KeyStoreConfiguration(String location, String password, String keyStoreType, String keyManagerFactoryAlgorithm) throws StoreConfigurationException {
/*  44 */     super(location, password, keyStoreType);
/*  45 */     this.keyManagerFactoryAlgorithm = (keyManagerFactoryAlgorithm == null) ? KeyManagerFactory.getDefaultAlgorithm() : keyManagerFactoryAlgorithm;
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
/*     */   @PluginFactory
/*     */   public static KeyStoreConfiguration createKeyStoreConfiguration(@PluginAttribute("location") String location, @PluginAttribute(value = "password", sensitive = true) String password, @PluginAttribute("type") String keyStoreType, @PluginAttribute("keyManagerFactoryAlgorithm") String keyManagerFactoryAlgorithm) throws StoreConfigurationException {
/*  71 */     return new KeyStoreConfiguration(location, password, keyStoreType, keyManagerFactoryAlgorithm);
/*     */   }
/*     */ 
/*     */   
/*     */   public KeyManagerFactory initKeyManagerFactory() throws NoSuchAlgorithmException, UnrecoverableKeyException, KeyStoreException {
/*  76 */     KeyManagerFactory kmFactory = KeyManagerFactory.getInstance(this.keyManagerFactoryAlgorithm);
/*  77 */     kmFactory.init(getKeyStore(), getPasswordAsCharArray());
/*  78 */     return kmFactory;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  83 */     int prime = 31;
/*  84 */     int result = super.hashCode();
/*  85 */     result = 31 * result + ((this.keyManagerFactoryAlgorithm == null) ? 0 : this.keyManagerFactoryAlgorithm.hashCode());
/*  86 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/*  91 */     if (this == obj) {
/*  92 */       return true;
/*     */     }
/*  94 */     if (!super.equals(obj)) {
/*  95 */       return false;
/*     */     }
/*  97 */     if (getClass() != obj.getClass()) {
/*  98 */       return false;
/*     */     }
/* 100 */     KeyStoreConfiguration other = (KeyStoreConfiguration)obj;
/* 101 */     if (this.keyManagerFactoryAlgorithm == null) {
/* 102 */       if (other.keyManagerFactoryAlgorithm != null) {
/* 103 */         return false;
/*     */       }
/* 105 */     } else if (!this.keyManagerFactoryAlgorithm.equals(other.keyManagerFactoryAlgorithm)) {
/* 106 */       return false;
/*     */     } 
/* 108 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\net\ssl\KeyStoreConfiguration.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */