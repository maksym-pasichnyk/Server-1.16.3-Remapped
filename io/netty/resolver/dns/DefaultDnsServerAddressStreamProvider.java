/*     */ package io.netty.resolver.dns;
/*     */ 
/*     */ import io.netty.util.NetUtil;
/*     */ import io.netty.util.internal.SocketUtils;
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*     */ import java.lang.reflect.Method;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Hashtable;
/*     */ import java.util.List;
/*     */ import javax.naming.NamingException;
/*     */ import javax.naming.directory.DirContext;
/*     */ import javax.naming.directory.InitialDirContext;
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
/*     */ public final class DefaultDnsServerAddressStreamProvider
/*     */   implements DnsServerAddressStreamProvider
/*     */ {
/*  49 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(DefaultDnsServerAddressStreamProvider.class);
/*  50 */   public static final DefaultDnsServerAddressStreamProvider INSTANCE = new DefaultDnsServerAddressStreamProvider();
/*     */   
/*     */   private static final List<InetSocketAddress> DEFAULT_NAME_SERVER_LIST;
/*     */   
/*     */   private static final InetSocketAddress[] DEFAULT_NAME_SERVER_ARRAY;
/*     */ 
/*     */   
/*     */   static {
/*  58 */     List<InetSocketAddress> defaultNameServers = new ArrayList<InetSocketAddress>(2);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  65 */     Hashtable<String, String> env = new Hashtable<String, String>();
/*  66 */     env.put("java.naming.factory.initial", "com.sun.jndi.dns.DnsContextFactory");
/*  67 */     env.put("java.naming.provider.url", "dns://");
/*     */     try {
/*  69 */       DirContext ctx = new InitialDirContext(env);
/*  70 */       String dnsUrls = (String)ctx.getEnvironment().get("java.naming.provider.url");
/*     */       
/*  72 */       if (dnsUrls != null && !dnsUrls.isEmpty()) {
/*  73 */         String[] servers = dnsUrls.split(" ");
/*  74 */         for (String server : servers) {
/*     */           try {
/*  76 */             URI uri = new URI(server);
/*  77 */             String host = (new URI(server)).getHost();
/*     */             
/*  79 */             if (host == null || host.isEmpty())
/*  80 */             { logger.debug("Skipping a nameserver URI as host portion could not be extracted: {}", server);
/*     */                }
/*     */             
/*     */             else
/*     */             
/*  85 */             { int port = uri.getPort();
/*  86 */               defaultNameServers.add(SocketUtils.socketAddress(uri.getHost(), (port == -1) ? 53 : port)); } 
/*  87 */           } catch (URISyntaxException e) {
/*  88 */             logger.debug("Skipping a malformed nameserver URI: {}", server, e);
/*     */           } 
/*     */         } 
/*     */       } 
/*  92 */     } catch (NamingException namingException) {}
/*     */ 
/*     */ 
/*     */     
/*  96 */     if (defaultNameServers.isEmpty()) {
/*     */       try {
/*  98 */         Class<?> configClass = Class.forName("sun.net.dns.ResolverConfiguration");
/*  99 */         Method open = configClass.getMethod("open", new Class[0]);
/* 100 */         Method nameservers = configClass.getMethod("nameservers", new Class[0]);
/* 101 */         Object instance = open.invoke(null, new Object[0]);
/*     */ 
/*     */         
/* 104 */         List<String> list = (List<String>)nameservers.invoke(instance, new Object[0]);
/* 105 */         for (String a : list) {
/* 106 */           if (a != null) {
/* 107 */             defaultNameServers.add(new InetSocketAddress(SocketUtils.addressByName(a), 53));
/*     */           }
/*     */         } 
/* 110 */       } catch (Exception exception) {}
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 116 */     if (!defaultNameServers.isEmpty()) {
/* 117 */       if (logger.isDebugEnabled()) {
/* 118 */         logger.debug("Default DNS servers: {} (sun.net.dns.ResolverConfiguration)", defaultNameServers);
/*     */       
/*     */       }
/*     */     
/*     */     }
/*     */     else {
/*     */       
/* 125 */       if (NetUtil.isIpV6AddressesPreferred() || (NetUtil.LOCALHOST instanceof java.net.Inet6Address && 
/* 126 */         !NetUtil.isIpV4StackPreferred())) {
/* 127 */         Collections.addAll(defaultNameServers, new InetSocketAddress[] {
/*     */               
/* 129 */               SocketUtils.socketAddress("2001:4860:4860::8888", 53), 
/* 130 */               SocketUtils.socketAddress("2001:4860:4860::8844", 53) });
/*     */       } else {
/* 132 */         Collections.addAll(defaultNameServers, new InetSocketAddress[] {
/*     */               
/* 134 */               SocketUtils.socketAddress("8.8.8.8", 53), 
/* 135 */               SocketUtils.socketAddress("8.8.4.4", 53)
/*     */             });
/*     */       } 
/* 138 */       if (logger.isWarnEnabled()) {
/* 139 */         logger.warn("Default DNS servers: {} (Google Public DNS as a fallback)", defaultNameServers);
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 144 */     DEFAULT_NAME_SERVER_LIST = Collections.unmodifiableList(defaultNameServers);
/* 145 */     DEFAULT_NAME_SERVER_ARRAY = defaultNameServers.<InetSocketAddress>toArray(new InetSocketAddress[defaultNameServers.size()]);
/* 146 */   } private static final DnsServerAddresses DEFAULT_NAME_SERVERS = DnsServerAddresses.sequential(DEFAULT_NAME_SERVER_ARRAY);
/*     */ 
/*     */   
/*     */   static final int DNS_PORT = 53;
/*     */ 
/*     */ 
/*     */   
/*     */   public DnsServerAddressStream nameServerAddressStream(String hostname) {
/* 154 */     return DEFAULT_NAME_SERVERS.stream();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static List<InetSocketAddress> defaultAddressList() {
/* 163 */     return DEFAULT_NAME_SERVER_LIST;
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
/*     */   public static DnsServerAddresses defaultAddresses() {
/* 178 */     return DEFAULT_NAME_SERVERS;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static InetSocketAddress[] defaultAddressArray() {
/* 186 */     return (InetSocketAddress[])DEFAULT_NAME_SERVER_ARRAY.clone();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\resolver\dns\DefaultDnsServerAddressStreamProvider.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */