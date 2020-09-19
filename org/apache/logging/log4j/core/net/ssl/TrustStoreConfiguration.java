/*     */ package org.apache.logging.log4j.core.net.ssl;
/*     */ 
/*     */ import java.security.KeyStoreException;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import javax.net.ssl.TrustManagerFactory;
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
/*     */ @Plugin(name = "TrustStore", category = "Core", printObject = true)
/*     */ public class TrustStoreConfiguration
/*     */   extends AbstractKeyStoreConfiguration
/*     */ {
/*     */   private final String trustManagerFactoryAlgorithm;
/*     */   
/*     */   public TrustStoreConfiguration(String location, String password, String keyStoreType, String trustManagerFactoryAlgorithm) throws StoreConfigurationException {
/*  39 */     super(location, password, keyStoreType);
/*  40 */     this.trustManagerFactoryAlgorithm = (trustManagerFactoryAlgorithm == null) ? TrustManagerFactory.getDefaultAlgorithm() : trustManagerFactoryAlgorithm;
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
/*     */   public static TrustStoreConfiguration createKeyStoreConfiguration(@PluginAttribute("location") String location, @PluginAttribute(value = "password", sensitive = true) String password, @PluginAttribute("type") String keyStoreType, @PluginAttribute("trustManagerFactoryAlgorithm") String trustManagerFactoryAlgorithm) throws StoreConfigurationException {
/*  66 */     return new TrustStoreConfiguration(location, password, keyStoreType, trustManagerFactoryAlgorithm);
/*     */   }
/*     */   
/*     */   public TrustManagerFactory initTrustManagerFactory() throws NoSuchAlgorithmException, KeyStoreException {
/*  70 */     TrustManagerFactory tmFactory = TrustManagerFactory.getInstance(this.trustManagerFactoryAlgorithm);
/*  71 */     tmFactory.init(getKeyStore());
/*  72 */     return tmFactory;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  77 */     int prime = 31;
/*  78 */     int result = super.hashCode();
/*  79 */     result = 31 * result + ((this.trustManagerFactoryAlgorithm == null) ? 0 : this.trustManagerFactoryAlgorithm.hashCode());
/*     */     
/*  81 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/*  86 */     if (this == obj) {
/*  87 */       return true;
/*     */     }
/*  89 */     if (!super.equals(obj)) {
/*  90 */       return false;
/*     */     }
/*  92 */     if (getClass() != obj.getClass()) {
/*  93 */       return false;
/*     */     }
/*  95 */     TrustStoreConfiguration other = (TrustStoreConfiguration)obj;
/*  96 */     if (this.trustManagerFactoryAlgorithm == null) {
/*  97 */       if (other.trustManagerFactoryAlgorithm != null) {
/*  98 */         return false;
/*     */       }
/* 100 */     } else if (!this.trustManagerFactoryAlgorithm.equals(other.trustManagerFactoryAlgorithm)) {
/* 101 */       return false;
/*     */     } 
/* 103 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\net\ssl\TrustStoreConfiguration.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */