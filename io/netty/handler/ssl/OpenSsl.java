/*     */ package io.netty.handler.ssl;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.handler.ssl.util.SelfSignedCertificate;
/*     */ import io.netty.internal.tcnative.Buffer;
/*     */ import io.netty.internal.tcnative.Library;
/*     */ import io.netty.internal.tcnative.SSL;
/*     */ import io.netty.internal.tcnative.SSLContext;
/*     */ import io.netty.util.ReferenceCountUtil;
/*     */ import io.netty.util.ReferenceCounted;
/*     */ import io.netty.util.internal.NativeLibraryLoader;
/*     */ import io.netty.util.internal.PlatformDependent;
/*     */ import io.netty.util.internal.SystemPropertyUtil;
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.security.cert.X509Certificate;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
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
/*     */ public final class OpenSsl
/*     */ {
/*  58 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(OpenSsl.class);
/*     */   
/*     */   private static final Throwable UNAVAILABILITY_CAUSE;
/*     */   
/*     */   static final List<String> DEFAULT_CIPHERS;
/*     */   static final Set<String> AVAILABLE_CIPHER_SUITES;
/*     */   private static final Set<String> AVAILABLE_OPENSSL_CIPHER_SUITES;
/*     */   private static final Set<String> AVAILABLE_JAVA_CIPHER_SUITES;
/*     */   private static final boolean SUPPORTS_KEYMANAGER_FACTORY;
/*     */   private static final boolean SUPPORTS_HOSTNAME_VALIDATION;
/*     */   private static final boolean USE_KEYMANAGER_FACTORY;
/*     */   private static final boolean SUPPORTS_OCSP;
/*     */   static final Set<String> SUPPORTED_PROTOCOLS_SET;
/*     */   
/*     */   static {
/*  73 */     Throwable cause = null;
/*     */     
/*  75 */     if (SystemPropertyUtil.getBoolean("io.netty.handler.ssl.noOpenSsl", false)) {
/*  76 */       cause = new UnsupportedOperationException("OpenSSL was explicit disabled with -Dio.netty.handler.ssl.noOpenSsl=true");
/*     */ 
/*     */       
/*  79 */       logger.debug("netty-tcnative explicit disabled; " + OpenSslEngine.class
/*     */           
/*  81 */           .getSimpleName() + " will be unavailable.", cause);
/*     */     } else {
/*     */       
/*     */       try {
/*  85 */         Class.forName("io.netty.internal.tcnative.SSL", false, OpenSsl.class.getClassLoader());
/*  86 */       } catch (ClassNotFoundException t) {
/*  87 */         cause = t;
/*  88 */         logger.debug("netty-tcnative not in the classpath; " + OpenSslEngine.class
/*     */             
/*  90 */             .getSimpleName() + " will be unavailable.");
/*     */       } 
/*     */ 
/*     */       
/*  94 */       if (cause == null) {
/*     */         
/*     */         try {
/*  97 */           loadTcNative();
/*  98 */         } catch (Throwable t) {
/*  99 */           cause = t;
/* 100 */           logger.debug("Failed to load netty-tcnative; " + OpenSslEngine.class
/*     */               
/* 102 */               .getSimpleName() + " will be unavailable, unless the application has already loaded the symbols by some other means. See http://netty.io/wiki/forked-tomcat-native.html for more information.", t);
/*     */         } 
/*     */ 
/*     */ 
/*     */         
/*     */         try {
/* 108 */           initializeTcNative();
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 113 */           cause = null;
/* 114 */         } catch (Throwable t) {
/* 115 */           if (cause == null) {
/* 116 */             cause = t;
/*     */           }
/* 118 */           logger.debug("Failed to initialize netty-tcnative; " + OpenSslEngine.class
/*     */               
/* 120 */               .getSimpleName() + " will be unavailable. See http://netty.io/wiki/forked-tomcat-native.html for more information.", t);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 126 */     UNAVAILABILITY_CAUSE = cause;
/*     */     
/* 128 */     if (cause == null) {
/* 129 */       logger.debug("netty-tcnative using native library: {}", SSL.versionString());
/*     */       
/* 131 */       List<String> defaultCiphers = new ArrayList<String>();
/* 132 */       Set<String> availableOpenSslCipherSuites = new LinkedHashSet<String>(128);
/* 133 */       boolean supportsKeyManagerFactory = false;
/* 134 */       boolean useKeyManagerFactory = false;
/* 135 */       boolean supportsHostNameValidation = false;
/*     */       try {
/* 137 */         long sslCtx = SSLContext.make(31, 1);
/* 138 */         long certBio = 0L;
/* 139 */         SelfSignedCertificate cert = null;
/*     */         try {
/* 141 */           SSLContext.setCipherSuite(sslCtx, "ALL");
/* 142 */           long ssl = SSL.newSSL(sslCtx, true);
/*     */           try {
/* 144 */             for (String c : SSL.getCiphers(ssl)) {
/*     */               
/* 146 */               if (c != null && !c.isEmpty() && !availableOpenSslCipherSuites.contains(c))
/*     */               {
/*     */                 
/* 149 */                 availableOpenSslCipherSuites.add(c);
/*     */               }
/*     */             } 
/*     */             try {
/* 153 */               SSL.setHostNameValidation(ssl, 0, "netty.io");
/* 154 */               supportsHostNameValidation = true;
/* 155 */             } catch (Throwable ignore) {
/* 156 */               logger.debug("Hostname Verification not supported.");
/*     */             } 
/*     */             try {
/* 159 */               cert = new SelfSignedCertificate();
/* 160 */               certBio = ReferenceCountedOpenSslContext.toBIO(new X509Certificate[] { cert.cert() });
/* 161 */               SSL.setCertificateChainBio(ssl, certBio, false);
/* 162 */               supportsKeyManagerFactory = true;
/*     */               try {
/* 164 */                 useKeyManagerFactory = ((Boolean)AccessController.<Boolean>doPrivileged(new PrivilegedAction<Boolean>()
/*     */                     {
/*     */                       public Boolean run() {
/* 167 */                         return Boolean.valueOf(SystemPropertyUtil.getBoolean("io.netty.handler.ssl.openssl.useKeyManagerFactory", true));
/*     */                       }
/*     */                     })).booleanValue();
/*     */               }
/* 171 */               catch (Throwable ignore) {
/* 172 */                 logger.debug("Failed to get useKeyManagerFactory system property.");
/*     */               } 
/* 174 */             } catch (Throwable ignore) {
/* 175 */               logger.debug("KeyManagerFactory not supported.");
/*     */             } 
/*     */           } finally {
/* 178 */             SSL.freeSSL(ssl);
/* 179 */             if (certBio != 0L) {
/* 180 */               SSL.freeBIO(certBio);
/*     */             }
/* 182 */             if (cert != null) {
/* 183 */               cert.delete();
/*     */             }
/*     */           } 
/*     */         } finally {
/* 187 */           SSLContext.free(sslCtx);
/*     */         } 
/* 189 */       } catch (Exception e) {
/* 190 */         logger.warn("Failed to get the list of available OpenSSL cipher suites.", e);
/*     */       } 
/* 192 */       AVAILABLE_OPENSSL_CIPHER_SUITES = Collections.unmodifiableSet(availableOpenSslCipherSuites);
/*     */       
/* 194 */       Set<String> availableJavaCipherSuites = new LinkedHashSet<String>(AVAILABLE_OPENSSL_CIPHER_SUITES.size() * 2);
/* 195 */       for (String cipher : AVAILABLE_OPENSSL_CIPHER_SUITES) {
/*     */         
/* 197 */         availableJavaCipherSuites.add(CipherSuiteConverter.toJava(cipher, "TLS"));
/* 198 */         availableJavaCipherSuites.add(CipherSuiteConverter.toJava(cipher, "SSL"));
/*     */       } 
/*     */       
/* 201 */       SslUtils.addIfSupported(availableJavaCipherSuites, defaultCiphers, SslUtils.DEFAULT_CIPHER_SUITES);
/* 202 */       SslUtils.useFallbackCiphersIfDefaultIsEmpty(defaultCiphers, availableJavaCipherSuites);
/* 203 */       DEFAULT_CIPHERS = Collections.unmodifiableList(defaultCiphers);
/*     */       
/* 205 */       AVAILABLE_JAVA_CIPHER_SUITES = Collections.unmodifiableSet(availableJavaCipherSuites);
/*     */ 
/*     */       
/* 208 */       Set<String> availableCipherSuites = new LinkedHashSet<String>(AVAILABLE_OPENSSL_CIPHER_SUITES.size() + AVAILABLE_JAVA_CIPHER_SUITES.size());
/* 209 */       availableCipherSuites.addAll(AVAILABLE_OPENSSL_CIPHER_SUITES);
/* 210 */       availableCipherSuites.addAll(AVAILABLE_JAVA_CIPHER_SUITES);
/*     */       
/* 212 */       AVAILABLE_CIPHER_SUITES = availableCipherSuites;
/* 213 */       SUPPORTS_KEYMANAGER_FACTORY = supportsKeyManagerFactory;
/* 214 */       SUPPORTS_HOSTNAME_VALIDATION = supportsHostNameValidation;
/* 215 */       USE_KEYMANAGER_FACTORY = useKeyManagerFactory;
/*     */       
/* 217 */       Set<String> protocols = new LinkedHashSet<String>(6);
/*     */       
/* 219 */       protocols.add("SSLv2Hello");
/* 220 */       if (doesSupportProtocol(1)) {
/* 221 */         protocols.add("SSLv2");
/*     */       }
/* 223 */       if (doesSupportProtocol(2)) {
/* 224 */         protocols.add("SSLv3");
/*     */       }
/* 226 */       if (doesSupportProtocol(4)) {
/* 227 */         protocols.add("TLSv1");
/*     */       }
/* 229 */       if (doesSupportProtocol(8)) {
/* 230 */         protocols.add("TLSv1.1");
/*     */       }
/* 232 */       if (doesSupportProtocol(16)) {
/* 233 */         protocols.add("TLSv1.2");
/*     */       }
/*     */       
/* 236 */       SUPPORTED_PROTOCOLS_SET = Collections.unmodifiableSet(protocols);
/* 237 */       SUPPORTS_OCSP = doesSupportOcsp();
/*     */       
/* 239 */       if (logger.isDebugEnabled()) {
/* 240 */         logger.debug("Supported protocols (OpenSSL): {} ", Arrays.asList(new Set[] { SUPPORTED_PROTOCOLS_SET }));
/* 241 */         logger.debug("Default cipher suites (OpenSSL): {}", DEFAULT_CIPHERS);
/*     */       } 
/*     */     } else {
/* 244 */       DEFAULT_CIPHERS = Collections.emptyList();
/* 245 */       AVAILABLE_OPENSSL_CIPHER_SUITES = Collections.emptySet();
/* 246 */       AVAILABLE_JAVA_CIPHER_SUITES = Collections.emptySet();
/* 247 */       AVAILABLE_CIPHER_SUITES = Collections.emptySet();
/* 248 */       SUPPORTS_KEYMANAGER_FACTORY = false;
/* 249 */       SUPPORTS_HOSTNAME_VALIDATION = false;
/* 250 */       USE_KEYMANAGER_FACTORY = false;
/* 251 */       SUPPORTED_PROTOCOLS_SET = Collections.emptySet();
/* 252 */       SUPPORTS_OCSP = false;
/*     */     } 
/*     */   }
/*     */   
/*     */   private static boolean doesSupportOcsp() {
/* 257 */     boolean supportsOcsp = false;
/* 258 */     if (version() >= 268443648L) {
/* 259 */       long sslCtx = -1L;
/*     */       try {
/* 261 */         sslCtx = SSLContext.make(16, 1);
/* 262 */         SSLContext.enableOcsp(sslCtx, false);
/* 263 */         supportsOcsp = true;
/* 264 */       } catch (Exception exception) {
/*     */       
/*     */       } finally {
/* 267 */         if (sslCtx != -1L) {
/* 268 */           SSLContext.free(sslCtx);
/*     */         }
/*     */       } 
/*     */     } 
/* 272 */     return supportsOcsp;
/*     */   }
/*     */   private static boolean doesSupportProtocol(int protocol) {
/* 275 */     long sslCtx = -1L;
/*     */     try {
/* 277 */       sslCtx = SSLContext.make(protocol, 2);
/* 278 */       return true;
/* 279 */     } catch (Exception ignore) {
/* 280 */       return false;
/*     */     } finally {
/* 282 */       if (sslCtx != -1L) {
/* 283 */         SSLContext.free(sslCtx);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isAvailable() {
/* 294 */     return (UNAVAILABILITY_CAUSE == null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isAlpnSupported() {
/* 302 */     return (version() >= 268443648L);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isOcspSupported() {
/* 309 */     return SUPPORTS_OCSP;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int version() {
/* 317 */     return isAvailable() ? SSL.version() : -1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String versionString() {
/* 325 */     return isAvailable() ? SSL.versionString() : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void ensureAvailability() {
/* 335 */     if (UNAVAILABILITY_CAUSE != null) {
/* 336 */       throw (Error)(new UnsatisfiedLinkError("failed to load the required native library"))
/* 337 */         .initCause(UNAVAILABILITY_CAUSE);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Throwable unavailabilityCause() {
/* 348 */     return UNAVAILABILITY_CAUSE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public static Set<String> availableCipherSuites() {
/* 356 */     return availableOpenSslCipherSuites();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Set<String> availableOpenSslCipherSuites() {
/* 364 */     return AVAILABLE_OPENSSL_CIPHER_SUITES;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Set<String> availableJavaCipherSuites() {
/* 372 */     return AVAILABLE_JAVA_CIPHER_SUITES;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isCipherSuiteAvailable(String cipherSuite) {
/* 380 */     String converted = CipherSuiteConverter.toOpenSsl(cipherSuite);
/* 381 */     if (converted != null) {
/* 382 */       cipherSuite = converted;
/*     */     }
/* 384 */     return AVAILABLE_OPENSSL_CIPHER_SUITES.contains(cipherSuite);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean supportsKeyManagerFactory() {
/* 391 */     return SUPPORTS_KEYMANAGER_FACTORY;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean supportsHostnameValidation() {
/* 399 */     return SUPPORTS_HOSTNAME_VALIDATION;
/*     */   }
/*     */   
/*     */   static boolean useKeyManagerFactory() {
/* 403 */     return USE_KEYMANAGER_FACTORY;
/*     */   }
/*     */   
/*     */   static long memoryAddress(ByteBuf buf) {
/* 407 */     assert buf.isDirect();
/* 408 */     return buf.hasMemoryAddress() ? buf.memoryAddress() : Buffer.address(buf.nioBuffer());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static void loadTcNative() throws Exception {
/* 414 */     String os = PlatformDependent.normalizedOs();
/* 415 */     String arch = PlatformDependent.normalizedArch();
/*     */     
/* 417 */     Set<String> libNames = new LinkedHashSet<String>(4);
/* 418 */     String staticLibName = "netty_tcnative";
/*     */ 
/*     */ 
/*     */     
/* 422 */     libNames.add(staticLibName + "_" + os + '_' + arch);
/* 423 */     if ("linux".equalsIgnoreCase(os))
/*     */     {
/* 425 */       libNames.add(staticLibName + "_" + os + '_' + arch + "_fedora");
/*     */     }
/* 427 */     libNames.add(staticLibName + "_" + arch);
/* 428 */     libNames.add(staticLibName);
/*     */     
/* 430 */     NativeLibraryLoader.loadFirstAvailable(SSL.class.getClassLoader(), libNames
/* 431 */         .<String>toArray(new String[libNames.size()]));
/*     */   }
/*     */   
/*     */   private static boolean initializeTcNative() throws Exception {
/* 435 */     return Library.initialize();
/*     */   }
/*     */   
/*     */   static void releaseIfNeeded(ReferenceCounted counted) {
/* 439 */     if (counted.refCnt() > 0)
/* 440 */       ReferenceCountUtil.safeRelease(counted); 
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\ssl\OpenSsl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */