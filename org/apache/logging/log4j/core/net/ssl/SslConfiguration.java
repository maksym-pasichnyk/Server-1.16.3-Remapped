/*     */ package org.apache.logging.log4j.core.net.ssl;
/*     */ 
/*     */ import java.security.KeyManagementException;
/*     */ import java.security.KeyStoreException;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.security.UnrecoverableKeyException;
/*     */ import javax.net.ssl.KeyManager;
/*     */ import javax.net.ssl.KeyManagerFactory;
/*     */ import javax.net.ssl.SSLContext;
/*     */ import javax.net.ssl.SSLServerSocketFactory;
/*     */ import javax.net.ssl.SSLSocketFactory;
/*     */ import javax.net.ssl.TrustManager;
/*     */ import javax.net.ssl.TrustManagerFactory;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginElement;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginFactory;
/*     */ import org.apache.logging.log4j.status.StatusLogger;
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
/*     */ @Plugin(name = "Ssl", category = "Core", printObject = true)
/*     */ public class SslConfiguration
/*     */ {
/*  44 */   private static final StatusLogger LOGGER = StatusLogger.getLogger();
/*     */   
/*     */   private final KeyStoreConfiguration keyStoreConfig;
/*     */   private final TrustStoreConfiguration trustStoreConfig;
/*     */   private final SSLContext sslContext;
/*     */   private final String protocol;
/*     */   
/*     */   private SslConfiguration(String protocol, KeyStoreConfiguration keyStoreConfig, TrustStoreConfiguration trustStoreConfig) {
/*  52 */     this.keyStoreConfig = keyStoreConfig;
/*  53 */     this.trustStoreConfig = trustStoreConfig;
/*  54 */     this.protocol = (protocol == null) ? "SSL" : protocol;
/*  55 */     this.sslContext = createSslContext();
/*     */   }
/*     */   
/*     */   public SSLSocketFactory getSslSocketFactory() {
/*  59 */     return this.sslContext.getSocketFactory();
/*     */   }
/*     */   
/*     */   public SSLServerSocketFactory getSslServerSocketFactory() {
/*  63 */     return this.sslContext.getServerSocketFactory();
/*     */   }
/*     */   
/*     */   private SSLContext createSslContext() {
/*  67 */     SSLContext context = null;
/*     */     
/*     */     try {
/*  70 */       context = createSslContextBasedOnConfiguration();
/*  71 */       LOGGER.debug("Creating SSLContext with the given parameters");
/*     */     }
/*  73 */     catch (TrustStoreConfigurationException e) {
/*  74 */       context = createSslContextWithTrustStoreFailure();
/*     */     }
/*  76 */     catch (KeyStoreConfigurationException e) {
/*  77 */       context = createSslContextWithKeyStoreFailure();
/*     */     } 
/*  79 */     return context;
/*     */   }
/*     */ 
/*     */   
/*     */   private SSLContext createSslContextWithTrustStoreFailure() {
/*     */     SSLContext context;
/*     */     try {
/*  86 */       context = createSslContextWithDefaultTrustManagerFactory();
/*  87 */       LOGGER.debug("Creating SSLContext with default truststore");
/*     */     }
/*  89 */     catch (KeyStoreConfigurationException e) {
/*  90 */       context = createDefaultSslContext();
/*  91 */       LOGGER.debug("Creating SSLContext with default configuration");
/*     */     } 
/*  93 */     return context;
/*     */   }
/*     */ 
/*     */   
/*     */   private SSLContext createSslContextWithKeyStoreFailure() {
/*     */     SSLContext context;
/*     */     try {
/* 100 */       context = createSslContextWithDefaultKeyManagerFactory();
/* 101 */       LOGGER.debug("Creating SSLContext with default keystore");
/*     */     }
/* 103 */     catch (TrustStoreConfigurationException e) {
/* 104 */       context = createDefaultSslContext();
/* 105 */       LOGGER.debug("Creating SSLContext with default configuration");
/*     */     } 
/* 107 */     return context;
/*     */   }
/*     */   
/*     */   private SSLContext createSslContextBasedOnConfiguration() throws KeyStoreConfigurationException, TrustStoreConfigurationException {
/* 111 */     return createSslContext(false, false);
/*     */   }
/*     */   
/*     */   private SSLContext createSslContextWithDefaultKeyManagerFactory() throws TrustStoreConfigurationException {
/*     */     try {
/* 116 */       return createSslContext(true, false);
/* 117 */     } catch (KeyStoreConfigurationException dummy) {
/* 118 */       LOGGER.debug("Exception occured while using default keystore. This should be a BUG");
/* 119 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   private SSLContext createSslContextWithDefaultTrustManagerFactory() throws KeyStoreConfigurationException {
/*     */     try {
/* 125 */       return createSslContext(false, true);
/*     */     }
/* 127 */     catch (TrustStoreConfigurationException dummy) {
/* 128 */       LOGGER.debug("Exception occured while using default truststore. This should be a BUG");
/* 129 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   private SSLContext createDefaultSslContext() {
/*     */     try {
/* 135 */       return SSLContext.getDefault();
/* 136 */     } catch (NoSuchAlgorithmException e) {
/* 137 */       LOGGER.error("Failed to create an SSLContext with default configuration", e);
/* 138 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private SSLContext createSslContext(boolean loadDefaultKeyManagerFactory, boolean loadDefaultTrustManagerFactory) throws KeyStoreConfigurationException, TrustStoreConfigurationException {
/*     */     try {
/* 145 */       KeyManager[] kManagers = null;
/* 146 */       TrustManager[] tManagers = null;
/*     */       
/* 148 */       SSLContext newSslContext = SSLContext.getInstance(this.protocol);
/* 149 */       if (!loadDefaultKeyManagerFactory) {
/* 150 */         KeyManagerFactory kmFactory = loadKeyManagerFactory();
/* 151 */         kManagers = kmFactory.getKeyManagers();
/*     */       } 
/* 153 */       if (!loadDefaultTrustManagerFactory) {
/* 154 */         TrustManagerFactory tmFactory = loadTrustManagerFactory();
/* 155 */         tManagers = tmFactory.getTrustManagers();
/*     */       } 
/*     */       
/* 158 */       newSslContext.init(kManagers, tManagers, null);
/* 159 */       return newSslContext;
/*     */     }
/* 161 */     catch (NoSuchAlgorithmException e) {
/* 162 */       LOGGER.error("No Provider supports a TrustManagerFactorySpi implementation for the specified protocol", e);
/* 163 */       throw new TrustStoreConfigurationException(e);
/*     */     }
/* 165 */     catch (KeyManagementException e) {
/* 166 */       LOGGER.error("Failed to initialize the SSLContext", e);
/* 167 */       throw new KeyStoreConfigurationException(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   private TrustManagerFactory loadTrustManagerFactory() throws TrustStoreConfigurationException {
/* 172 */     if (this.trustStoreConfig == null) {
/* 173 */       throw new TrustStoreConfigurationException(new Exception("The trustStoreConfiguration is null"));
/*     */     }
/*     */     
/*     */     try {
/* 177 */       return this.trustStoreConfig.initTrustManagerFactory();
/*     */     }
/* 179 */     catch (NoSuchAlgorithmException e) {
/* 180 */       LOGGER.error("The specified algorithm is not available from the specified provider", e);
/* 181 */       throw new TrustStoreConfigurationException(e);
/* 182 */     } catch (KeyStoreException e) {
/* 183 */       LOGGER.error("Failed to initialize the TrustManagerFactory", e);
/* 184 */       throw new TrustStoreConfigurationException(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   private KeyManagerFactory loadKeyManagerFactory() throws KeyStoreConfigurationException {
/* 189 */     if (this.keyStoreConfig == null) {
/* 190 */       throw new KeyStoreConfigurationException(new Exception("The keyStoreConfiguration is null"));
/*     */     }
/*     */     
/*     */     try {
/* 194 */       return this.keyStoreConfig.initKeyManagerFactory();
/*     */     }
/* 196 */     catch (NoSuchAlgorithmException e) {
/* 197 */       LOGGER.error("The specified algorithm is not available from the specified provider", e);
/* 198 */       throw new KeyStoreConfigurationException(e);
/* 199 */     } catch (KeyStoreException e) {
/* 200 */       LOGGER.error("Failed to initialize the TrustManagerFactory", e);
/* 201 */       throw new KeyStoreConfigurationException(e);
/* 202 */     } catch (UnrecoverableKeyException e) {
/* 203 */       LOGGER.error("The key cannot be recovered (e.g. the given password is wrong)", e);
/* 204 */       throw new KeyStoreConfigurationException(e);
/*     */     } 
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
/*     */   @PluginFactory
/*     */   public static SslConfiguration createSSLConfiguration(@PluginAttribute("protocol") String protocol, @PluginElement("KeyStore") KeyStoreConfiguration keyStoreConfig, @PluginElement("TrustStore") TrustStoreConfiguration trustStoreConfig) {
/* 222 */     return new SslConfiguration(protocol, keyStoreConfig, trustStoreConfig);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 227 */     int prime = 31;
/* 228 */     int result = 1;
/* 229 */     result = 31 * result + ((this.keyStoreConfig == null) ? 0 : this.keyStoreConfig.hashCode());
/* 230 */     result = 31 * result + ((this.protocol == null) ? 0 : this.protocol.hashCode());
/* 231 */     result = 31 * result + ((this.sslContext == null) ? 0 : this.sslContext.hashCode());
/* 232 */     result = 31 * result + ((this.trustStoreConfig == null) ? 0 : this.trustStoreConfig.hashCode());
/* 233 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 238 */     if (this == obj) {
/* 239 */       return true;
/*     */     }
/* 241 */     if (obj == null) {
/* 242 */       return false;
/*     */     }
/* 244 */     if (getClass() != obj.getClass()) {
/* 245 */       return false;
/*     */     }
/* 247 */     SslConfiguration other = (SslConfiguration)obj;
/* 248 */     if (this.keyStoreConfig == null) {
/* 249 */       if (other.keyStoreConfig != null) {
/* 250 */         return false;
/*     */       }
/* 252 */     } else if (!this.keyStoreConfig.equals(other.keyStoreConfig)) {
/* 253 */       return false;
/*     */     } 
/* 255 */     if (this.protocol == null) {
/* 256 */       if (other.protocol != null) {
/* 257 */         return false;
/*     */       }
/* 259 */     } else if (!this.protocol.equals(other.protocol)) {
/* 260 */       return false;
/*     */     } 
/* 262 */     if (this.sslContext == null) {
/* 263 */       if (other.sslContext != null) {
/* 264 */         return false;
/*     */       }
/* 266 */     } else if (!this.sslContext.equals(other.sslContext)) {
/* 267 */       return false;
/*     */     } 
/* 269 */     if (this.trustStoreConfig == null) {
/* 270 */       if (other.trustStoreConfig != null) {
/* 271 */         return false;
/*     */       }
/* 273 */     } else if (!this.trustStoreConfig.equals(other.trustStoreConfig)) {
/* 274 */       return false;
/*     */     } 
/* 276 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\net\ssl\SslConfiguration.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */