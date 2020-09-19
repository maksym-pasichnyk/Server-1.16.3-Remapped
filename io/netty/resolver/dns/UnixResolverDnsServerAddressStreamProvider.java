/*     */ package io.netty.resolver.dns;
/*     */ 
/*     */ import io.netty.util.internal.ObjectUtil;
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.io.FileReader;
/*     */ import java.io.IOException;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
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
/*     */ public final class UnixResolverDnsServerAddressStreamProvider
/*     */   implements DnsServerAddressStreamProvider
/*     */ {
/*  47 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(UnixResolverDnsServerAddressStreamProvider.class);
/*     */   
/*     */   private static final String ETC_RESOLV_CONF_FILE = "/etc/resolv.conf";
/*     */   
/*     */   private static final String ETC_RESOLVER_DIR = "/etc/resolver";
/*     */   
/*     */   private static final String NAMESERVER_ROW_LABEL = "nameserver";
/*     */   
/*     */   private static final String SORTLIST_ROW_LABEL = "sortlist";
/*     */   private static final String OPTIONS_ROW_LABEL = "options";
/*     */   private static final String DOMAIN_ROW_LABEL = "domain";
/*     */   private static final String PORT_ROW_LABEL = "port";
/*     */   private static final String NDOTS_LABEL = "ndots:";
/*     */   static final int DEFAULT_NDOTS = 1;
/*     */   private final DnsServerAddresses defaultNameServerAddresses;
/*     */   private final Map<String, DnsServerAddresses> domainToNameServerStreamMap;
/*     */   
/*     */   static DnsServerAddressStreamProvider parseSilently() {
/*     */     try {
/*  66 */       UnixResolverDnsServerAddressStreamProvider nameServerCache = new UnixResolverDnsServerAddressStreamProvider("/etc/resolv.conf", "/etc/resolver");
/*     */       
/*  68 */       return nameServerCache.mayOverrideNameServers() ? nameServerCache : DefaultDnsServerAddressStreamProvider.INSTANCE;
/*     */     }
/*  70 */     catch (Exception e) {
/*  71 */       logger.debug("failed to parse {} and/or {}", new Object[] { "/etc/resolv.conf", "/etc/resolver", e });
/*  72 */       return DefaultDnsServerAddressStreamProvider.INSTANCE;
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
/*     */   
/*     */   public UnixResolverDnsServerAddressStreamProvider(File etcResolvConf, File... etcResolverFiles) throws IOException {
/*  90 */     Map<String, DnsServerAddresses> etcResolvConfMap = parse(new File[] { (File)ObjectUtil.checkNotNull(etcResolvConf, "etcResolvConf") });
/*  91 */     boolean useEtcResolverFiles = (etcResolverFiles != null && etcResolverFiles.length != 0);
/*  92 */     this.domainToNameServerStreamMap = useEtcResolverFiles ? parse(etcResolverFiles) : etcResolvConfMap;
/*     */     
/*  94 */     DnsServerAddresses defaultNameServerAddresses = etcResolvConfMap.get(etcResolvConf.getName());
/*  95 */     if (defaultNameServerAddresses == null) {
/*  96 */       Collection<DnsServerAddresses> values = etcResolvConfMap.values();
/*  97 */       if (values.isEmpty()) {
/*  98 */         throw new IllegalArgumentException(etcResolvConf + " didn't provide any name servers");
/*     */       }
/* 100 */       this.defaultNameServerAddresses = values.iterator().next();
/*     */     } else {
/* 102 */       this.defaultNameServerAddresses = defaultNameServerAddresses;
/*     */     } 
/*     */     
/* 105 */     if (useEtcResolverFiles) {
/* 106 */       this.domainToNameServerStreamMap.putAll(etcResolvConfMap);
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
/*     */   
/*     */   public UnixResolverDnsServerAddressStreamProvider(String etcResolvConf, String etcResolverDir) throws IOException {
/* 124 */     this((etcResolvConf == null) ? null : new File(etcResolvConf), (etcResolverDir == null) ? null : (new File(etcResolverDir))
/* 125 */         .listFiles());
/*     */   }
/*     */ 
/*     */   
/*     */   public DnsServerAddressStream nameServerAddressStream(String hostname) {
/*     */     while (true) {
/* 131 */       int i = hostname.indexOf('.', 1);
/* 132 */       if (i < 0 || i == hostname.length() - 1) {
/* 133 */         return this.defaultNameServerAddresses.stream();
/*     */       }
/*     */       
/* 136 */       DnsServerAddresses addresses = this.domainToNameServerStreamMap.get(hostname);
/* 137 */       if (addresses != null) {
/* 138 */         return addresses.stream();
/*     */       }
/*     */       
/* 141 */       hostname = hostname.substring(i + 1);
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean mayOverrideNameServers() {
/* 146 */     return (!this.domainToNameServerStreamMap.isEmpty() || this.defaultNameServerAddresses.stream().next() != null);
/*     */   }
/*     */   
/*     */   private static Map<String, DnsServerAddresses> parse(File... etcResolverFiles) throws IOException {
/* 150 */     Map<String, DnsServerAddresses> domainToNameServerStreamMap = new HashMap<String, DnsServerAddresses>(etcResolverFiles.length << 1);
/*     */     
/* 152 */     for (File etcResolverFile : etcResolverFiles) {
/* 153 */       if (etcResolverFile.isFile()) {
/*     */ 
/*     */         
/* 156 */         FileReader fr = new FileReader(etcResolverFile);
/* 157 */         BufferedReader br = null;
/*     */       } 
/*     */     } 
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
/* 221 */     return domainToNameServerStreamMap;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void putIfAbsent(Map<String, DnsServerAddresses> domainToNameServerStreamMap, String domainName, List<InetSocketAddress> addresses) {
/* 228 */     putIfAbsent(domainToNameServerStreamMap, domainName, DnsServerAddresses.sequential(addresses));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static void putIfAbsent(Map<String, DnsServerAddresses> domainToNameServerStreamMap, String domainName, DnsServerAddresses addresses) {
/* 234 */     DnsServerAddresses existingAddresses = domainToNameServerStreamMap.put(domainName, addresses);
/* 235 */     if (existingAddresses != null) {
/* 236 */       domainToNameServerStreamMap.put(domainName, existingAddresses);
/* 237 */       logger.debug("Domain name {} already maps to addresses {} so new addresses {} will be discarded", new Object[] { domainName, existingAddresses, addresses });
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
/*     */   static int parseEtcResolverFirstNdots() throws IOException {
/* 250 */     return parseEtcResolverFirstNdots(new File("/etc/resolv.conf"));
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
/*     */   static int parseEtcResolverFirstNdots(File etcResolvConf) throws IOException {
/* 262 */     FileReader fr = new FileReader(etcResolvConf);
/* 263 */     BufferedReader br = null;
/*     */     try {
/* 265 */       br = new BufferedReader(fr);
/*     */       String line;
/* 267 */       while ((line = br.readLine()) != null) {
/* 268 */         if (line.startsWith("options")) {
/* 269 */           int i = line.indexOf("ndots:");
/* 270 */           if (i >= 0) {
/* 271 */             i += "ndots:".length();
/* 272 */             int j = line.indexOf(' ', i);
/* 273 */             return Integer.parseInt(line.substring(i, (j < 0) ? line.length() : j));
/*     */           } 
/*     */           break;
/*     */         } 
/*     */       } 
/*     */     } finally {
/* 279 */       if (br == null) {
/* 280 */         fr.close();
/*     */       } else {
/* 282 */         br.close();
/*     */       } 
/*     */     } 
/* 285 */     return 1;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\resolver\dns\UnixResolverDnsServerAddressStreamProvider.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */