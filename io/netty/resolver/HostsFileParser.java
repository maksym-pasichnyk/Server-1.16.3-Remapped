/*     */ package io.netty.resolver;
/*     */ 
/*     */ import io.netty.util.NetUtil;
/*     */ import io.netty.util.internal.ObjectUtil;
/*     */ import io.netty.util.internal.PlatformDependent;
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.io.FileReader;
/*     */ import java.io.IOException;
/*     */ import java.io.Reader;
/*     */ import java.net.Inet4Address;
/*     */ import java.net.Inet6Address;
/*     */ import java.net.InetAddress;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
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
/*     */ 
/*     */ public final class HostsFileParser
/*     */ {
/*     */   private static final String WINDOWS_DEFAULT_SYSTEM_ROOT = "C:\\Windows";
/*     */   private static final String WINDOWS_HOSTS_FILE_RELATIVE_PATH = "\\system32\\drivers\\etc\\hosts";
/*     */   private static final String X_PLATFORMS_HOSTS_FILE_PATH = "/etc/hosts";
/*  51 */   private static final Pattern WHITESPACES = Pattern.compile("[ \t]+");
/*     */   
/*  53 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(HostsFileParser.class);
/*     */   
/*     */   private static File locateHostsFile() {
/*     */     File hostsFile;
/*  57 */     if (PlatformDependent.isWindows()) {
/*  58 */       hostsFile = new File(System.getenv("SystemRoot") + "\\system32\\drivers\\etc\\hosts");
/*  59 */       if (!hostsFile.exists()) {
/*  60 */         hostsFile = new File("C:\\Windows\\system32\\drivers\\etc\\hosts");
/*     */       }
/*     */     } else {
/*  63 */       hostsFile = new File("/etc/hosts");
/*     */     } 
/*  65 */     return hostsFile;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static HostsFileEntries parseSilently() {
/*  74 */     File hostsFile = locateHostsFile();
/*     */     try {
/*  76 */       return parse(hostsFile);
/*  77 */     } catch (IOException e) {
/*  78 */       logger.warn("Failed to load and parse hosts file at " + hostsFile.getPath(), e);
/*  79 */       return HostsFileEntries.EMPTY;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static HostsFileEntries parse() throws IOException {
/*  90 */     return parse(locateHostsFile());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static HostsFileEntries parse(File file) throws IOException {
/* 101 */     ObjectUtil.checkNotNull(file, "file");
/* 102 */     if (file.exists() && file.isFile()) {
/* 103 */       return parse(new BufferedReader(new FileReader(file)));
/*     */     }
/* 105 */     return HostsFileEntries.EMPTY;
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
/*     */   public static HostsFileEntries parse(Reader reader) throws IOException {
/* 117 */     ObjectUtil.checkNotNull(reader, "reader");
/* 118 */     BufferedReader buff = new BufferedReader(reader);
/*     */     try {
/* 120 */       Map<String, Inet4Address> ipv4Entries = new HashMap<String, Inet4Address>();
/* 121 */       Map<String, Inet6Address> ipv6Entries = new HashMap<String, Inet6Address>();
/*     */       String line;
/* 123 */       while ((line = buff.readLine()) != null) {
/*     */         
/* 125 */         int commentPosition = line.indexOf('#');
/* 126 */         if (commentPosition != -1) {
/* 127 */           line = line.substring(0, commentPosition);
/*     */         }
/*     */         
/* 130 */         line = line.trim();
/* 131 */         if (line.isEmpty()) {
/*     */           continue;
/*     */         }
/*     */ 
/*     */         
/* 136 */         List<String> lineParts = new ArrayList<String>();
/* 137 */         for (String s : WHITESPACES.split(line)) {
/* 138 */           if (!s.isEmpty()) {
/* 139 */             lineParts.add(s);
/*     */           }
/*     */         } 
/*     */ 
/*     */         
/* 144 */         if (lineParts.size() < 2) {
/*     */           continue;
/*     */         }
/*     */ 
/*     */         
/* 149 */         byte[] ipBytes = NetUtil.createByteArrayFromIpAddressString(lineParts.get(0));
/*     */         
/* 151 */         if (ipBytes == null) {
/*     */           continue;
/*     */         }
/*     */ 
/*     */ 
/*     */         
/* 157 */         for (int i = 1; i < lineParts.size(); i++) {
/* 158 */           String hostname = lineParts.get(i);
/* 159 */           String hostnameLower = hostname.toLowerCase(Locale.ENGLISH);
/* 160 */           InetAddress address = InetAddress.getByAddress(hostname, ipBytes);
/* 161 */           if (address instanceof Inet4Address) {
/* 162 */             Inet4Address previous = ipv4Entries.put(hostnameLower, (Inet4Address)address);
/* 163 */             if (previous != null)
/*     */             {
/* 165 */               ipv4Entries.put(hostnameLower, previous);
/*     */             }
/*     */           } else {
/* 168 */             Inet6Address previous = ipv6Entries.put(hostnameLower, (Inet6Address)address);
/* 169 */             if (previous != null)
/*     */             {
/* 171 */               ipv6Entries.put(hostnameLower, previous);
/*     */             }
/*     */           } 
/*     */         } 
/*     */       } 
/* 176 */       return (ipv4Entries.isEmpty() && ipv6Entries.isEmpty()) ? HostsFileEntries.EMPTY : new HostsFileEntries(ipv4Entries, ipv6Entries);
/*     */     } finally {
/*     */ 
/*     */       
/*     */       try {
/* 181 */         buff.close();
/* 182 */       } catch (IOException e) {
/* 183 */         logger.warn("Failed to close a reader", e);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\resolver\HostsFileParser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */