/*     */ package io.netty.handler.ssl.util;
/*     */ 
/*     */ import io.netty.util.concurrent.FastThreadLocal;
/*     */ import io.netty.util.internal.PlatformDependent;
/*     */ import java.security.InvalidAlgorithmParameterException;
/*     */ import java.security.KeyStore;
/*     */ import java.security.KeyStoreException;
/*     */ import java.security.Provider;
/*     */ import javax.net.ssl.ManagerFactoryParameters;
/*     */ import javax.net.ssl.TrustManager;
/*     */ import javax.net.ssl.TrustManagerFactory;
/*     */ import javax.net.ssl.TrustManagerFactorySpi;
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
/*     */ public abstract class SimpleTrustManagerFactory
/*     */   extends TrustManagerFactory
/*     */ {
/*  38 */   private static final Provider PROVIDER = new Provider("", 0.0D, "")
/*     */     {
/*     */       private static final long serialVersionUID = -2680540247105807895L;
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  50 */   private static final FastThreadLocal<SimpleTrustManagerFactorySpi> CURRENT_SPI = new FastThreadLocal<SimpleTrustManagerFactorySpi>()
/*     */     {
/*     */       protected SimpleTrustManagerFactory.SimpleTrustManagerFactorySpi initialValue()
/*     */       {
/*  54 */         return new SimpleTrustManagerFactory.SimpleTrustManagerFactorySpi();
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected SimpleTrustManagerFactory() {
/*  62 */     this("");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected SimpleTrustManagerFactory(String name) {
/*  71 */     super((TrustManagerFactorySpi)CURRENT_SPI.get(), PROVIDER, name);
/*  72 */     ((SimpleTrustManagerFactorySpi)CURRENT_SPI.get()).init(this);
/*  73 */     CURRENT_SPI.remove();
/*     */     
/*  75 */     if (name == null) {
/*  76 */       throw new NullPointerException("name");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract void engineInit(KeyStore paramKeyStore) throws Exception;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract void engineInit(ManagerFactoryParameters paramManagerFactoryParameters) throws Exception;
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract TrustManager[] engineGetTrustManagers();
/*     */ 
/*     */ 
/*     */   
/*     */   static final class SimpleTrustManagerFactorySpi
/*     */     extends TrustManagerFactorySpi
/*     */   {
/*     */     private SimpleTrustManagerFactory parent;
/*     */ 
/*     */     
/*     */     private volatile TrustManager[] trustManagers;
/*     */ 
/*     */ 
/*     */     
/*     */     void init(SimpleTrustManagerFactory parent) {
/* 107 */       this.parent = parent;
/*     */     }
/*     */ 
/*     */     
/*     */     protected void engineInit(KeyStore keyStore) throws KeyStoreException {
/*     */       try {
/* 113 */         this.parent.engineInit(keyStore);
/* 114 */       } catch (KeyStoreException e) {
/* 115 */         throw e;
/* 116 */       } catch (Exception e) {
/* 117 */         throw new KeyStoreException(e);
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     protected void engineInit(ManagerFactoryParameters managerFactoryParameters) throws InvalidAlgorithmParameterException {
/*     */       try {
/* 125 */         this.parent.engineInit(managerFactoryParameters);
/* 126 */       } catch (InvalidAlgorithmParameterException e) {
/* 127 */         throw e;
/* 128 */       } catch (Exception e) {
/* 129 */         throw new InvalidAlgorithmParameterException(e);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     protected TrustManager[] engineGetTrustManagers() {
/* 135 */       TrustManager[] trustManagers = this.trustManagers;
/* 136 */       if (trustManagers == null) {
/* 137 */         trustManagers = this.parent.engineGetTrustManagers();
/* 138 */         if (PlatformDependent.javaVersion() >= 7) {
/* 139 */           for (int i = 0; i < trustManagers.length; i++) {
/* 140 */             TrustManager tm = trustManagers[i];
/* 141 */             if (tm instanceof X509TrustManager && !(tm instanceof javax.net.ssl.X509ExtendedTrustManager)) {
/* 142 */               trustManagers[i] = new X509TrustManagerWrapper((X509TrustManager)tm);
/*     */             }
/*     */           } 
/*     */         }
/* 146 */         this.trustManagers = trustManagers;
/*     */       } 
/* 148 */       return (TrustManager[])trustManagers.clone();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\ss\\util\SimpleTrustManagerFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */