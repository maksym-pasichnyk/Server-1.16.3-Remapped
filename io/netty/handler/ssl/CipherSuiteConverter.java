/*     */ package io.netty.handler.ssl;
/*     */ 
/*     */ import io.netty.util.internal.PlatformDependent;
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
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
/*     */ final class CipherSuiteConverter
/*     */ {
/*  36 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(CipherSuiteConverter.class);
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
/*  52 */   private static final Pattern JAVA_CIPHERSUITE_PATTERN = Pattern.compile("^(?:TLS|SSL)_((?:(?!_WITH_).)+)_WITH_(.*)_(.*)$");
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
/*  68 */   private static final Pattern OPENSSL_CIPHERSUITE_PATTERN = Pattern.compile("^(?:((?:(?:EXP-)?(?:(?:DHE|EDH|ECDH|ECDHE|SRP|RSA)-(?:DSS|RSA|ECDSA|PSK)|(?:ADH|AECDH|KRB5|PSK|SRP)))|EXP)-)?(.*)-(.*)$");
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
/*  80 */   private static final Pattern JAVA_AES_CBC_PATTERN = Pattern.compile("^(AES)_([0-9]+)_CBC$");
/*  81 */   private static final Pattern JAVA_AES_PATTERN = Pattern.compile("^(AES)_([0-9]+)_(.*)$");
/*  82 */   private static final Pattern OPENSSL_AES_CBC_PATTERN = Pattern.compile("^(AES)([0-9]+)$");
/*  83 */   private static final Pattern OPENSSL_AES_PATTERN = Pattern.compile("^(AES)([0-9]+)-(.*)$");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  89 */   private static final ConcurrentMap<String, String> j2o = PlatformDependent.newConcurrentHashMap();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  96 */   private static final ConcurrentMap<String, Map<String, String>> o2j = PlatformDependent.newConcurrentHashMap();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static void clearCache() {
/* 102 */     j2o.clear();
/* 103 */     o2j.clear();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static boolean isJ2OCached(String key, String value) {
/* 110 */     return value.equals(j2o.get(key));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static boolean isO2JCached(String key, String protocol, String value) {
/* 117 */     Map<String, String> p2j = o2j.get(key);
/* 118 */     if (p2j == null) {
/* 119 */       return false;
/*     */     }
/* 121 */     return value.equals(p2j.get(protocol));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static String toOpenSsl(Iterable<String> javaCipherSuites) {
/* 129 */     StringBuilder buf = new StringBuilder();
/* 130 */     for (String c : javaCipherSuites) {
/* 131 */       if (c == null) {
/*     */         break;
/*     */       }
/*     */       
/* 135 */       String converted = toOpenSsl(c);
/* 136 */       if (converted != null) {
/* 137 */         c = converted;
/*     */       }
/*     */       
/* 140 */       buf.append(c);
/* 141 */       buf.append(':');
/*     */     } 
/*     */     
/* 144 */     if (buf.length() > 0) {
/* 145 */       buf.setLength(buf.length() - 1);
/* 146 */       return buf.toString();
/*     */     } 
/* 148 */     return "";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static String toOpenSsl(String javaCipherSuite) {
/* 158 */     String converted = j2o.get(javaCipherSuite);
/* 159 */     if (converted != null) {
/* 160 */       return converted;
/*     */     }
/* 162 */     return cacheFromJava(javaCipherSuite);
/*     */   }
/*     */ 
/*     */   
/*     */   private static String cacheFromJava(String javaCipherSuite) {
/* 167 */     String openSslCipherSuite = toOpenSslUncached(javaCipherSuite);
/* 168 */     if (openSslCipherSuite == null) {
/* 169 */       return null;
/*     */     }
/*     */ 
/*     */     
/* 173 */     j2o.putIfAbsent(javaCipherSuite, openSslCipherSuite);
/*     */ 
/*     */     
/* 176 */     String javaCipherSuiteSuffix = javaCipherSuite.substring(4);
/* 177 */     Map<String, String> p2j = new HashMap<String, String>(4);
/* 178 */     p2j.put("", javaCipherSuiteSuffix);
/* 179 */     p2j.put("SSL", "SSL_" + javaCipherSuiteSuffix);
/* 180 */     p2j.put("TLS", "TLS_" + javaCipherSuiteSuffix);
/* 181 */     o2j.put(openSslCipherSuite, p2j);
/*     */     
/* 183 */     logger.debug("Cipher suite mapping: {} => {}", javaCipherSuite, openSslCipherSuite);
/*     */     
/* 185 */     return openSslCipherSuite;
/*     */   }
/*     */   
/*     */   static String toOpenSslUncached(String javaCipherSuite) {
/* 189 */     Matcher m = JAVA_CIPHERSUITE_PATTERN.matcher(javaCipherSuite);
/* 190 */     if (!m.matches()) {
/* 191 */       return null;
/*     */     }
/*     */     
/* 194 */     String handshakeAlgo = toOpenSslHandshakeAlgo(m.group(1));
/* 195 */     String bulkCipher = toOpenSslBulkCipher(m.group(2));
/* 196 */     String hmacAlgo = toOpenSslHmacAlgo(m.group(3));
/* 197 */     if (handshakeAlgo.isEmpty())
/* 198 */       return bulkCipher + '-' + hmacAlgo; 
/* 199 */     if (bulkCipher.contains("CHACHA20")) {
/* 200 */       return handshakeAlgo + '-' + bulkCipher;
/*     */     }
/* 202 */     return handshakeAlgo + '-' + bulkCipher + '-' + hmacAlgo;
/*     */   }
/*     */ 
/*     */   
/*     */   private static String toOpenSslHandshakeAlgo(String handshakeAlgo) {
/* 207 */     boolean export = handshakeAlgo.endsWith("_EXPORT");
/* 208 */     if (export) {
/* 209 */       handshakeAlgo = handshakeAlgo.substring(0, handshakeAlgo.length() - 7);
/*     */     }
/*     */     
/* 212 */     if ("RSA".equals(handshakeAlgo)) {
/* 213 */       handshakeAlgo = "";
/* 214 */     } else if (handshakeAlgo.endsWith("_anon")) {
/* 215 */       handshakeAlgo = 'A' + handshakeAlgo.substring(0, handshakeAlgo.length() - 5);
/*     */     } 
/*     */     
/* 218 */     if (export) {
/* 219 */       if (handshakeAlgo.isEmpty()) {
/* 220 */         handshakeAlgo = "EXP";
/*     */       } else {
/* 222 */         handshakeAlgo = "EXP-" + handshakeAlgo;
/*     */       } 
/*     */     }
/*     */     
/* 226 */     return handshakeAlgo.replace('_', '-');
/*     */   }
/*     */   
/*     */   private static String toOpenSslBulkCipher(String bulkCipher) {
/* 230 */     if (bulkCipher.startsWith("AES_")) {
/* 231 */       Matcher m = JAVA_AES_CBC_PATTERN.matcher(bulkCipher);
/* 232 */       if (m.matches()) {
/* 233 */         return m.replaceFirst("$1$2");
/*     */       }
/*     */       
/* 236 */       m = JAVA_AES_PATTERN.matcher(bulkCipher);
/* 237 */       if (m.matches()) {
/* 238 */         return m.replaceFirst("$1$2-$3");
/*     */       }
/*     */     } 
/*     */     
/* 242 */     if ("3DES_EDE_CBC".equals(bulkCipher)) {
/* 243 */       return "DES-CBC3";
/*     */     }
/*     */     
/* 246 */     if ("RC4_128".equals(bulkCipher) || "RC4_40".equals(bulkCipher)) {
/* 247 */       return "RC4";
/*     */     }
/*     */     
/* 250 */     if ("DES40_CBC".equals(bulkCipher) || "DES_CBC_40".equals(bulkCipher)) {
/* 251 */       return "DES-CBC";
/*     */     }
/*     */     
/* 254 */     if ("RC2_CBC_40".equals(bulkCipher)) {
/* 255 */       return "RC2-CBC";
/*     */     }
/*     */     
/* 258 */     return bulkCipher.replace('_', '-');
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static String toOpenSslHmacAlgo(String hmacAlgo) {
/* 268 */     return hmacAlgo;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static String toJava(String openSslCipherSuite, String protocol) {
/* 278 */     Map<String, String> p2j = o2j.get(openSslCipherSuite);
/* 279 */     if (p2j == null) {
/* 280 */       p2j = cacheFromOpenSsl(openSslCipherSuite);
/*     */ 
/*     */       
/* 283 */       if (p2j == null) {
/* 284 */         return null;
/*     */       }
/*     */     } 
/*     */     
/* 288 */     String javaCipherSuite = p2j.get(protocol);
/* 289 */     if (javaCipherSuite == null) {
/* 290 */       javaCipherSuite = protocol + '_' + (String)p2j.get("");
/*     */     }
/*     */     
/* 293 */     return javaCipherSuite;
/*     */   }
/*     */   
/*     */   private static Map<String, String> cacheFromOpenSsl(String openSslCipherSuite) {
/* 297 */     String javaCipherSuiteSuffix = toJavaUncached(openSslCipherSuite);
/* 298 */     if (javaCipherSuiteSuffix == null) {
/* 299 */       return null;
/*     */     }
/*     */     
/* 302 */     String javaCipherSuiteSsl = "SSL_" + javaCipherSuiteSuffix;
/* 303 */     String javaCipherSuiteTls = "TLS_" + javaCipherSuiteSuffix;
/*     */ 
/*     */     
/* 306 */     Map<String, String> p2j = new HashMap<String, String>(4);
/* 307 */     p2j.put("", javaCipherSuiteSuffix);
/* 308 */     p2j.put("SSL", javaCipherSuiteSsl);
/* 309 */     p2j.put("TLS", javaCipherSuiteTls);
/* 310 */     o2j.putIfAbsent(openSslCipherSuite, p2j);
/*     */ 
/*     */     
/* 313 */     j2o.putIfAbsent(javaCipherSuiteTls, openSslCipherSuite);
/* 314 */     j2o.putIfAbsent(javaCipherSuiteSsl, openSslCipherSuite);
/*     */     
/* 316 */     logger.debug("Cipher suite mapping: {} => {}", javaCipherSuiteTls, openSslCipherSuite);
/* 317 */     logger.debug("Cipher suite mapping: {} => {}", javaCipherSuiteSsl, openSslCipherSuite);
/*     */     
/* 319 */     return p2j;
/*     */   }
/*     */   static String toJavaUncached(String openSslCipherSuite) {
/*     */     boolean export;
/* 323 */     Matcher m = OPENSSL_CIPHERSUITE_PATTERN.matcher(openSslCipherSuite);
/* 324 */     if (!m.matches()) {
/* 325 */       return null;
/*     */     }
/*     */     
/* 328 */     String handshakeAlgo = m.group(1);
/*     */     
/* 330 */     if (handshakeAlgo == null) {
/* 331 */       handshakeAlgo = "";
/* 332 */       export = false;
/* 333 */     } else if (handshakeAlgo.startsWith("EXP-")) {
/* 334 */       handshakeAlgo = handshakeAlgo.substring(4);
/* 335 */       export = true;
/* 336 */     } else if ("EXP".equals(handshakeAlgo)) {
/* 337 */       handshakeAlgo = "";
/* 338 */       export = true;
/*     */     } else {
/* 340 */       export = false;
/*     */     } 
/*     */     
/* 343 */     handshakeAlgo = toJavaHandshakeAlgo(handshakeAlgo, export);
/* 344 */     String bulkCipher = toJavaBulkCipher(m.group(2), export);
/* 345 */     String hmacAlgo = toJavaHmacAlgo(m.group(3));
/*     */     
/* 347 */     String javaCipherSuite = handshakeAlgo + "_WITH_" + bulkCipher + '_' + hmacAlgo;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 352 */     return bulkCipher.contains("CHACHA20") ? (javaCipherSuite + "_SHA256") : javaCipherSuite;
/*     */   }
/*     */   
/*     */   private static String toJavaHandshakeAlgo(String handshakeAlgo, boolean export) {
/* 356 */     if (handshakeAlgo.isEmpty()) {
/* 357 */       handshakeAlgo = "RSA";
/* 358 */     } else if ("ADH".equals(handshakeAlgo)) {
/* 359 */       handshakeAlgo = "DH_anon";
/* 360 */     } else if ("AECDH".equals(handshakeAlgo)) {
/* 361 */       handshakeAlgo = "ECDH_anon";
/*     */     } 
/*     */     
/* 364 */     handshakeAlgo = handshakeAlgo.replace('-', '_');
/* 365 */     if (export) {
/* 366 */       return handshakeAlgo + "_EXPORT";
/*     */     }
/* 368 */     return handshakeAlgo;
/*     */   }
/*     */ 
/*     */   
/*     */   private static String toJavaBulkCipher(String bulkCipher, boolean export) {
/* 373 */     if (bulkCipher.startsWith("AES")) {
/* 374 */       Matcher m = OPENSSL_AES_CBC_PATTERN.matcher(bulkCipher);
/* 375 */       if (m.matches()) {
/* 376 */         return m.replaceFirst("$1_$2_CBC");
/*     */       }
/*     */       
/* 379 */       m = OPENSSL_AES_PATTERN.matcher(bulkCipher);
/* 380 */       if (m.matches()) {
/* 381 */         return m.replaceFirst("$1_$2_$3");
/*     */       }
/*     */     } 
/*     */     
/* 385 */     if ("DES-CBC3".equals(bulkCipher)) {
/* 386 */       return "3DES_EDE_CBC";
/*     */     }
/*     */     
/* 389 */     if ("RC4".equals(bulkCipher)) {
/* 390 */       if (export) {
/* 391 */         return "RC4_40";
/*     */       }
/* 393 */       return "RC4_128";
/*     */     } 
/*     */ 
/*     */     
/* 397 */     if ("DES-CBC".equals(bulkCipher)) {
/* 398 */       if (export) {
/* 399 */         return "DES_CBC_40";
/*     */       }
/* 401 */       return "DES_CBC";
/*     */     } 
/*     */ 
/*     */     
/* 405 */     if ("RC2-CBC".equals(bulkCipher)) {
/* 406 */       if (export) {
/* 407 */         return "RC2_CBC_40";
/*     */       }
/* 409 */       return "RC2_CBC";
/*     */     } 
/*     */ 
/*     */     
/* 413 */     return bulkCipher.replace('-', '_');
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static String toJavaHmacAlgo(String hmacAlgo) {
/* 423 */     return hmacAlgo;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\ssl\CipherSuiteConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */