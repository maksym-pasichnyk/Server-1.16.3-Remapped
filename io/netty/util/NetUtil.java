/*      */ package io.netty.util;
/*      */ 
/*      */ import io.netty.util.internal.PlatformDependent;
/*      */ import io.netty.util.internal.SocketUtils;
/*      */ import io.netty.util.internal.StringUtil;
/*      */ import io.netty.util.internal.SystemPropertyUtil;
/*      */ import io.netty.util.internal.logging.InternalLogger;
/*      */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*      */ import java.io.BufferedReader;
/*      */ import java.io.File;
/*      */ import java.io.FileReader;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.InputStreamReader;
/*      */ import java.net.Inet4Address;
/*      */ import java.net.Inet6Address;
/*      */ import java.net.InetAddress;
/*      */ import java.net.InetSocketAddress;
/*      */ import java.net.NetworkInterface;
/*      */ import java.net.SocketException;
/*      */ import java.net.UnknownHostException;
/*      */ import java.security.AccessController;
/*      */ import java.security.PrivilegedAction;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Enumeration;
/*      */ import java.util.List;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public final class NetUtil
/*      */ {
/*      */   public static final Inet4Address LOCALHOST4;
/*      */   public static final Inet6Address LOCALHOST6;
/*      */   public static final InetAddress LOCALHOST;
/*      */   public static final NetworkInterface LOOPBACK_IF;
/*      */   public static final int SOMAXCONN;
/*      */   private static final int IPV6_WORD_COUNT = 8;
/*      */   private static final int IPV6_MAX_CHAR_COUNT = 39;
/*      */   private static final int IPV6_BYTE_COUNT = 16;
/*      */   private static final int IPV6_MAX_CHAR_BETWEEN_SEPARATOR = 4;
/*      */   private static final int IPV6_MIN_SEPARATORS = 2;
/*      */   private static final int IPV6_MAX_SEPARATORS = 8;
/*      */   private static final int IPV4_MAX_CHAR_BETWEEN_SEPARATOR = 3;
/*      */   private static final int IPV4_SEPARATORS = 3;
/*  125 */   private static final boolean IPV4_PREFERRED = SystemPropertyUtil.getBoolean("java.net.preferIPv4Stack", false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  131 */   private static final boolean IPV6_ADDRESSES_PREFERRED = SystemPropertyUtil.getBoolean("java.net.preferIPv6Addresses", false);
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  136 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(NetUtil.class);
/*      */   
/*      */   static {
/*  139 */     logger.debug("-Djava.net.preferIPv4Stack: {}", Boolean.valueOf(IPV4_PREFERRED));
/*  140 */     logger.debug("-Djava.net.preferIPv6Addresses: {}", Boolean.valueOf(IPV6_ADDRESSES_PREFERRED));
/*      */     
/*  142 */     byte[] LOCALHOST4_BYTES = { Byte.MAX_VALUE, 0, 0, 1 };
/*  143 */     byte[] LOCALHOST6_BYTES = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 };
/*      */ 
/*      */     
/*  146 */     Inet4Address localhost4 = null;
/*      */     try {
/*  148 */       localhost4 = (Inet4Address)InetAddress.getByAddress("localhost", LOCALHOST4_BYTES);
/*  149 */     } catch (Exception e) {
/*      */       
/*  151 */       PlatformDependent.throwException(e);
/*      */     } 
/*  153 */     LOCALHOST4 = localhost4;
/*      */ 
/*      */     
/*  156 */     Inet6Address localhost6 = null;
/*      */     try {
/*  158 */       localhost6 = (Inet6Address)InetAddress.getByAddress("localhost", LOCALHOST6_BYTES);
/*  159 */     } catch (Exception e) {
/*      */       
/*  161 */       PlatformDependent.throwException(e);
/*      */     } 
/*  163 */     LOCALHOST6 = localhost6;
/*      */ 
/*      */     
/*  166 */     List<NetworkInterface> ifaces = new ArrayList<NetworkInterface>();
/*      */     try {
/*  168 */       Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
/*  169 */       if (interfaces != null) {
/*  170 */         while (interfaces.hasMoreElements()) {
/*  171 */           NetworkInterface iface = interfaces.nextElement();
/*      */           
/*  173 */           if (SocketUtils.addressesFromNetworkInterface(iface).hasMoreElements()) {
/*  174 */             ifaces.add(iface);
/*      */           }
/*      */         } 
/*      */       }
/*  178 */     } catch (SocketException e) {
/*  179 */       logger.warn("Failed to retrieve the list of available network interfaces", e);
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  185 */     NetworkInterface loopbackIface = null;
/*  186 */     InetAddress loopbackAddr = null;
/*  187 */     label71: for (NetworkInterface iface : ifaces) {
/*  188 */       for (Enumeration<InetAddress> i = SocketUtils.addressesFromNetworkInterface(iface); i.hasMoreElements(); ) {
/*  189 */         InetAddress addr = i.nextElement();
/*  190 */         if (addr.isLoopbackAddress()) {
/*      */           
/*  192 */           loopbackIface = iface;
/*  193 */           loopbackAddr = addr;
/*      */           
/*      */           break label71;
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/*  200 */     if (loopbackIface == null) {
/*      */       try {
/*  202 */         for (NetworkInterface iface : ifaces) {
/*  203 */           if (iface.isLoopback()) {
/*  204 */             Enumeration<InetAddress> i = SocketUtils.addressesFromNetworkInterface(iface);
/*  205 */             if (i.hasMoreElements()) {
/*      */               
/*  207 */               loopbackIface = iface;
/*  208 */               loopbackAddr = i.nextElement();
/*      */               
/*      */               break;
/*      */             } 
/*      */           } 
/*      */         } 
/*  214 */         if (loopbackIface == null) {
/*  215 */           logger.warn("Failed to find the loopback interface");
/*      */         }
/*  217 */       } catch (SocketException e) {
/*  218 */         logger.warn("Failed to find the loopback interface", e);
/*      */       } 
/*      */     }
/*      */     
/*  222 */     if (loopbackIface != null) {
/*      */       
/*  224 */       logger.debug("Loopback interface: {} ({}, {})", new Object[] { loopbackIface
/*      */             
/*  226 */             .getName(), loopbackIface.getDisplayName(), loopbackAddr.getHostAddress() });
/*      */ 
/*      */     
/*      */     }
/*  230 */     else if (loopbackAddr == null) {
/*      */       try {
/*  232 */         if (NetworkInterface.getByInetAddress(LOCALHOST6) != null) {
/*  233 */           logger.debug("Using hard-coded IPv6 localhost address: {}", localhost6);
/*  234 */           loopbackAddr = localhost6;
/*      */         } 
/*  236 */       } catch (Exception exception) {
/*      */       
/*      */       } finally {
/*  239 */         if (loopbackAddr == null) {
/*  240 */           logger.debug("Using hard-coded IPv4 localhost address: {}", localhost4);
/*  241 */           loopbackAddr = localhost4;
/*      */         } 
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/*  247 */     LOOPBACK_IF = loopbackIface;
/*  248 */     LOCALHOST = loopbackAddr;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  253 */     SOMAXCONN = ((Integer)AccessController.<Integer>doPrivileged(new PrivilegedAction<Integer>()
/*      */         {
/*      */ 
/*      */ 
/*      */           
/*      */           public Integer run()
/*      */           {
/*  260 */             int somaxconn = PlatformDependent.isWindows() ? 200 : 128;
/*  261 */             File file = new File("/proc/sys/net/core/somaxconn");
/*  262 */             BufferedReader in = null;
/*      */ 
/*      */ 
/*      */             
/*      */             try {
/*  267 */               if (file.exists()) {
/*  268 */                 in = new BufferedReader(new FileReader(file));
/*  269 */                 somaxconn = Integer.parseInt(in.readLine());
/*  270 */                 if (NetUtil.logger.isDebugEnabled()) {
/*  271 */                   NetUtil.logger.debug("{}: {}", file, Integer.valueOf(somaxconn));
/*      */                 }
/*      */               } else {
/*      */                 
/*  275 */                 Integer tmp = null;
/*  276 */                 if (SystemPropertyUtil.getBoolean("io.netty.net.somaxconn.trySysctl", false)) {
/*  277 */                   tmp = NetUtil.sysctlGetInt("kern.ipc.somaxconn");
/*  278 */                   if (tmp == null) {
/*  279 */                     tmp = NetUtil.sysctlGetInt("kern.ipc.soacceptqueue");
/*  280 */                     if (tmp != null) {
/*  281 */                       somaxconn = tmp.intValue();
/*      */                     }
/*      */                   } else {
/*  284 */                     somaxconn = tmp.intValue();
/*      */                   } 
/*      */                 } 
/*      */                 
/*  288 */                 if (tmp == null) {
/*  289 */                   NetUtil.logger.debug("Failed to get SOMAXCONN from sysctl and file {}. Default: {}", file, 
/*  290 */                       Integer.valueOf(somaxconn));
/*      */                 }
/*      */               } 
/*  293 */             } catch (Exception e) {
/*  294 */               NetUtil.logger.debug("Failed to get SOMAXCONN from sysctl and file {}. Default: {}", new Object[] { file, Integer.valueOf(somaxconn), e });
/*      */             } finally {
/*  296 */               if (in != null) {
/*      */                 try {
/*  298 */                   in.close();
/*  299 */                 } catch (Exception exception) {}
/*      */               }
/*      */             } 
/*      */ 
/*      */             
/*  304 */             return Integer.valueOf(somaxconn);
/*      */           }
/*      */         })).intValue();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static Integer sysctlGetInt(String sysctlKey) throws IOException {
/*  316 */     Process process = (new ProcessBuilder(new String[] { "sysctl", sysctlKey })).start();
/*      */     try {
/*  318 */       InputStream is = process.getInputStream();
/*  319 */       InputStreamReader isr = new InputStreamReader(is);
/*  320 */       BufferedReader br = new BufferedReader(isr);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     }
/*      */     finally {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  335 */       if (process != null) {
/*  336 */         process.destroy();
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isIpV4StackPreferred() {
/*  349 */     return IPV4_PREFERRED;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isIpV6AddressesPreferred() {
/*  360 */     return IPV6_ADDRESSES_PREFERRED;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static byte[] createByteArrayFromIpAddressString(String ipAddressString) {
/*  368 */     if (isValidIpV4Address(ipAddressString)) {
/*  369 */       return validIpV4ToBytes(ipAddressString);
/*      */     }
/*      */     
/*  372 */     if (isValidIpV6Address(ipAddressString)) {
/*  373 */       if (ipAddressString.charAt(0) == '[') {
/*  374 */         ipAddressString = ipAddressString.substring(1, ipAddressString.length() - 1);
/*      */       }
/*      */       
/*  377 */       int percentPos = ipAddressString.indexOf('%');
/*  378 */       if (percentPos >= 0) {
/*  379 */         ipAddressString = ipAddressString.substring(0, percentPos);
/*      */       }
/*      */       
/*  382 */       return getIPv6ByName(ipAddressString, true);
/*      */     } 
/*  384 */     return null;
/*      */   }
/*      */   
/*      */   private static int decimalDigit(String str, int pos) {
/*  388 */     return str.charAt(pos) - 48;
/*      */   }
/*      */   
/*      */   private static byte ipv4WordToByte(String ip, int from, int toExclusive) {
/*  392 */     int ret = decimalDigit(ip, from);
/*  393 */     from++;
/*  394 */     if (from == toExclusive) {
/*  395 */       return (byte)ret;
/*      */     }
/*  397 */     ret = ret * 10 + decimalDigit(ip, from);
/*  398 */     from++;
/*  399 */     if (from == toExclusive) {
/*  400 */       return (byte)ret;
/*      */     }
/*  402 */     return (byte)(ret * 10 + decimalDigit(ip, from));
/*      */   }
/*      */ 
/*      */   
/*      */   static byte[] validIpV4ToBytes(String ip) {
/*      */     int i;
/*  408 */     return new byte[] {
/*  409 */         ipv4WordToByte(ip, 0, i = ip.indexOf('.', 1)), 
/*  410 */         ipv4WordToByte(ip, i + 1, i = ip.indexOf('.', i + 2)), 
/*  411 */         ipv4WordToByte(ip, i + 1, i = ip.indexOf('.', i + 2)), 
/*  412 */         ipv4WordToByte(ip, i + 1, ip.length())
/*      */       };
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String intToIpAddress(int i) {
/*  420 */     StringBuilder buf = new StringBuilder(15);
/*  421 */     buf.append(i >> 24 & 0xFF);
/*  422 */     buf.append('.');
/*  423 */     buf.append(i >> 16 & 0xFF);
/*  424 */     buf.append('.');
/*  425 */     buf.append(i >> 8 & 0xFF);
/*  426 */     buf.append('.');
/*  427 */     buf.append(i & 0xFF);
/*  428 */     return buf.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String bytesToIpAddress(byte[] bytes) {
/*  438 */     return bytesToIpAddress(bytes, 0, bytes.length);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String bytesToIpAddress(byte[] bytes, int offset, int length) {
/*  448 */     switch (length) {
/*      */       case 4:
/*  450 */         return (new StringBuilder(15))
/*  451 */           .append(bytes[offset] & 0xFF)
/*  452 */           .append('.')
/*  453 */           .append(bytes[offset + 1] & 0xFF)
/*  454 */           .append('.')
/*  455 */           .append(bytes[offset + 2] & 0xFF)
/*  456 */           .append('.')
/*  457 */           .append(bytes[offset + 3] & 0xFF).toString();
/*      */       
/*      */       case 16:
/*  460 */         return toAddressString(bytes, offset, false);
/*      */     } 
/*  462 */     throw new IllegalArgumentException("length: " + length + " (expected: 4 or 16)");
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isValidIpV6Address(String ip) {
/*  467 */     return isValidIpV6Address(ip);
/*      */   }
/*      */   
/*      */   public static boolean isValidIpV6Address(CharSequence ip) {
/*  471 */     int start, colons, compressBegin, end = ip.length();
/*  472 */     if (end < 2) {
/*  473 */       return false;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*  478 */     char c = ip.charAt(0);
/*  479 */     if (c == '[') {
/*  480 */       end--;
/*  481 */       if (ip.charAt(end) != ']')
/*      */       {
/*  483 */         return false;
/*      */       }
/*  485 */       start = 1;
/*  486 */       c = ip.charAt(1);
/*      */     } else {
/*  488 */       start = 0;
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  493 */     if (c == ':') {
/*      */       
/*  495 */       if (ip.charAt(start + 1) != ':') {
/*  496 */         return false;
/*      */       }
/*  498 */       colons = 2;
/*  499 */       compressBegin = start;
/*  500 */       start += 2;
/*      */     } else {
/*  502 */       colons = 0;
/*  503 */       compressBegin = -1;
/*      */     } 
/*      */     
/*  506 */     int wordLen = 0;
/*      */     
/*  508 */     for (int i = start; i < end; i++) {
/*  509 */       c = ip.charAt(i);
/*  510 */       if (isValidHexChar(c)) {
/*  511 */         if (wordLen < 4) {
/*  512 */           wordLen++;
/*      */         } else {
/*      */           
/*  515 */           return false;
/*      */         } 
/*      */       } else {
/*  518 */         int ipv4Start; int j; int ipv4End; switch (c) {
/*      */           case ':':
/*  520 */             if (colons > 7) {
/*  521 */               return false;
/*      */             }
/*  523 */             if (ip.charAt(i - 1) == ':') {
/*  524 */               if (compressBegin >= 0) {
/*  525 */                 return false;
/*      */               }
/*  527 */               compressBegin = i - 1;
/*      */             } else {
/*  529 */               wordLen = 0;
/*      */             } 
/*  531 */             colons++;
/*      */             break;
/*      */ 
/*      */ 
/*      */           
/*      */           case '.':
/*  537 */             if ((compressBegin < 0 && colons != 6) || (colons == 7 && compressBegin >= start) || colons > 7)
/*      */             {
/*      */ 
/*      */               
/*  541 */               return false;
/*      */             }
/*      */ 
/*      */ 
/*      */ 
/*      */             
/*  547 */             ipv4Start = i - wordLen;
/*  548 */             j = ipv4Start - 2;
/*  549 */             if (isValidIPv4MappedChar(ip.charAt(j))) {
/*  550 */               if (!isValidIPv4MappedChar(ip.charAt(j - 1)) || 
/*  551 */                 !isValidIPv4MappedChar(ip.charAt(j - 2)) || 
/*  552 */                 !isValidIPv4MappedChar(ip.charAt(j - 3))) {
/*  553 */                 return false;
/*      */               }
/*  555 */               j -= 5;
/*      */             } 
/*      */             
/*  558 */             for (; j >= start; j--) {
/*  559 */               char tmpChar = ip.charAt(j);
/*  560 */               if (tmpChar != '0' && tmpChar != ':') {
/*  561 */                 return false;
/*      */               }
/*      */             } 
/*      */ 
/*      */             
/*  566 */             ipv4End = AsciiString.indexOf(ip, '%', ipv4Start + 7);
/*  567 */             if (ipv4End < 0) {
/*  568 */               ipv4End = end;
/*      */             }
/*  570 */             return isValidIpV4Address(ip, ipv4Start, ipv4End);
/*      */           
/*      */           case '%':
/*  573 */             end = i;
/*      */             break;
/*      */           default:
/*  576 */             return false;
/*      */         } 
/*      */       
/*      */       } 
/*      */     } 
/*  581 */     if (compressBegin < 0) {
/*  582 */       return (colons == 7 && wordLen > 0);
/*      */     }
/*      */     
/*  585 */     return (compressBegin + 2 == end || (wordLen > 0 && (colons < 8 || compressBegin <= start)));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static boolean isValidIpV4Word(CharSequence word, int from, int toExclusive) {
/*  591 */     int len = toExclusive - from;
/*      */     char c0;
/*  593 */     if (len < 1 || len > 3 || (c0 = word.charAt(from)) < '0') {
/*  594 */       return false;
/*      */     }
/*  596 */     if (len == 3) {
/*  597 */       char c1; char c2; return ((c1 = word.charAt(from + 1)) >= '0' && (
/*  598 */         c2 = word.charAt(from + 2)) >= '0' && ((c0 <= '1' && c1 <= '9' && c2 <= '9') || (c0 == '2' && c1 <= '5' && (c2 <= '5' || (c1 < '5' && c2 <= '9')))));
/*      */     } 
/*      */ 
/*      */     
/*  602 */     return (c0 <= '9' && (len == 1 || isValidNumericChar(word.charAt(from + 1))));
/*      */   }
/*      */   
/*      */   private static boolean isValidHexChar(char c) {
/*  606 */     return ((c >= '0' && c <= '9') || (c >= 'A' && c <= 'F') || (c >= 'a' && c <= 'f'));
/*      */   }
/*      */   
/*      */   private static boolean isValidNumericChar(char c) {
/*  610 */     return (c >= '0' && c <= '9');
/*      */   }
/*      */   
/*      */   private static boolean isValidIPv4MappedChar(char c) {
/*  614 */     return (c == 'f' || c == 'F');
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static boolean isValidIPv4MappedSeparators(byte b0, byte b1, boolean mustBeZero) {
/*  621 */     return (b0 == b1 && (b0 == 0 || (!mustBeZero && b1 == -1)));
/*      */   }
/*      */   
/*      */   private static boolean isValidIPv4Mapped(byte[] bytes, int currentIndex, int compressBegin, int compressLength) {
/*  625 */     boolean mustBeZero = (compressBegin + compressLength >= 14);
/*  626 */     return (currentIndex <= 12 && currentIndex >= 2 && (!mustBeZero || compressBegin < 12) && 
/*  627 */       isValidIPv4MappedSeparators(bytes[currentIndex - 1], bytes[currentIndex - 2], mustBeZero) && 
/*  628 */       PlatformDependent.isZero(bytes, 0, currentIndex - 3));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isValidIpV4Address(CharSequence ip) {
/*  638 */     return isValidIpV4Address(ip, 0, ip.length());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isValidIpV4Address(String ip) {
/*  648 */     return isValidIpV4Address(ip, 0, ip.length());
/*      */   }
/*      */   
/*      */   private static boolean isValidIpV4Address(CharSequence ip, int from, int toExcluded) {
/*  652 */     return (ip instanceof String) ? isValidIpV4Address((String)ip, from, toExcluded) : ((ip instanceof AsciiString) ? 
/*  653 */       isValidIpV4Address((AsciiString)ip, from, toExcluded) : 
/*  654 */       isValidIpV4Address0(ip, from, toExcluded));
/*      */   }
/*      */ 
/*      */   
/*      */   private static boolean isValidIpV4Address(String ip, int from, int toExcluded) {
/*  659 */     int len = toExcluded - from;
/*      */     int i;
/*  661 */     return (len <= 15 && len >= 7 && (
/*  662 */       i = ip.indexOf('.', from + 1)) > 0 && isValidIpV4Word(ip, from, i) && (
/*  663 */       i = ip.indexOf('.', from = i + 2)) > 0 && isValidIpV4Word(ip, from - 1, i) && (
/*  664 */       i = ip.indexOf('.', from = i + 2)) > 0 && isValidIpV4Word(ip, from - 1, i) && 
/*  665 */       isValidIpV4Word(ip, i + 1, toExcluded));
/*      */   }
/*      */ 
/*      */   
/*      */   private static boolean isValidIpV4Address(AsciiString ip, int from, int toExcluded) {
/*  670 */     int len = toExcluded - from;
/*      */     int i;
/*  672 */     return (len <= 15 && len >= 7 && (
/*  673 */       i = ip.indexOf('.', from + 1)) > 0 && isValidIpV4Word(ip, from, i) && (
/*  674 */       i = ip.indexOf('.', from = i + 2)) > 0 && isValidIpV4Word(ip, from - 1, i) && (
/*  675 */       i = ip.indexOf('.', from = i + 2)) > 0 && isValidIpV4Word(ip, from - 1, i) && 
/*  676 */       isValidIpV4Word(ip, i + 1, toExcluded));
/*      */   }
/*      */ 
/*      */   
/*      */   private static boolean isValidIpV4Address0(CharSequence ip, int from, int toExcluded) {
/*  681 */     int len = toExcluded - from;
/*      */     int i;
/*  683 */     return (len <= 15 && len >= 7 && (
/*  684 */       i = AsciiString.indexOf(ip, '.', from + 1)) > 0 && isValidIpV4Word(ip, from, i) && (
/*  685 */       i = AsciiString.indexOf(ip, '.', from = i + 2)) > 0 && isValidIpV4Word(ip, from - 1, i) && (
/*  686 */       i = AsciiString.indexOf(ip, '.', from = i + 2)) > 0 && isValidIpV4Word(ip, from - 1, i) && 
/*  687 */       isValidIpV4Word(ip, i + 1, toExcluded));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Inet6Address getByName(CharSequence ip) {
/*  698 */     return getByName(ip, true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Inet6Address getByName(CharSequence ip, boolean ipv4Mapped) {
/*  716 */     byte[] bytes = getIPv6ByName(ip, ipv4Mapped);
/*  717 */     if (bytes == null) {
/*  718 */       return null;
/*      */     }
/*      */     try {
/*  721 */       return Inet6Address.getByAddress((String)null, bytes, -1);
/*  722 */     } catch (UnknownHostException e) {
/*  723 */       throw new RuntimeException(e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static byte[] getIPv6ByName(CharSequence ip, boolean ipv4Mapped) {
/*  742 */     byte[] bytes = new byte[16];
/*  743 */     int ipLength = ip.length();
/*  744 */     int compressBegin = 0;
/*  745 */     int compressLength = 0;
/*  746 */     int currentIndex = 0;
/*  747 */     int value = 0;
/*  748 */     int begin = -1;
/*  749 */     int i = 0;
/*  750 */     int ipv6Separators = 0;
/*  751 */     int ipv4Separators = 0;
/*      */     
/*  753 */     boolean needsShift = false;
/*  754 */     for (; i < ipLength; i++) {
/*  755 */       int tmp; char c = ip.charAt(i);
/*  756 */       switch (c) {
/*      */         case ':':
/*  758 */           ipv6Separators++;
/*  759 */           if (i - begin > 4 || ipv4Separators > 0 || ipv6Separators > 8 || currentIndex + 1 >= bytes.length)
/*      */           {
/*      */             
/*  762 */             return null;
/*      */           }
/*  764 */           value <<= 4 - i - begin << 2;
/*      */           
/*  766 */           if (compressLength > 0) {
/*  767 */             compressLength -= 2;
/*      */           }
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*  773 */           bytes[currentIndex++] = (byte)((value & 0xF) << 4 | value >> 4 & 0xF);
/*  774 */           bytes[currentIndex++] = (byte)((value >> 8 & 0xF) << 4 | value >> 12 & 0xF);
/*  775 */           tmp = i + 1;
/*  776 */           if (tmp < ipLength && ip.charAt(tmp) == ':') {
/*  777 */             tmp++;
/*  778 */             if (compressBegin != 0 || (tmp < ipLength && ip.charAt(tmp) == ':')) {
/*  779 */               return null;
/*      */             }
/*  781 */             ipv6Separators++;
/*  782 */             needsShift = (ipv6Separators == 2 && value == 0);
/*  783 */             compressBegin = currentIndex;
/*  784 */             compressLength = bytes.length - compressBegin - 2;
/*  785 */             i++;
/*      */           } 
/*  787 */           value = 0;
/*  788 */           begin = -1;
/*      */           break;
/*      */         case '.':
/*  791 */           ipv4Separators++;
/*  792 */           tmp = i - begin;
/*  793 */           if (tmp > 3 || begin < 0 || ipv4Separators > 3 || (ipv6Separators > 0 && currentIndex + compressLength < 12) || i + 1 >= ipLength || currentIndex >= bytes.length || (ipv4Separators == 1 && (!ipv4Mapped || (currentIndex != 0 && 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */             
/*  801 */             !isValidIPv4Mapped(bytes, currentIndex, compressBegin, compressLength)) || (tmp == 3 && (
/*      */             
/*  803 */             !isValidNumericChar(ip.charAt(i - 1)) || 
/*  804 */             !isValidNumericChar(ip.charAt(i - 2)) || 
/*  805 */             !isValidNumericChar(ip.charAt(i - 3)))) || (tmp == 2 && (
/*  806 */             !isValidNumericChar(ip.charAt(i - 1)) || 
/*  807 */             !isValidNumericChar(ip.charAt(i - 2)))) || (tmp == 1 && 
/*  808 */             !isValidNumericChar(ip.charAt(i - 1)))))) {
/*  809 */             return null;
/*      */           }
/*  811 */           value <<= 3 - tmp << 2;
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*  816 */           begin = (value & 0xF) * 100 + (value >> 4 & 0xF) * 10 + (value >> 8 & 0xF);
/*  817 */           if (begin < 0 || begin > 255) {
/*  818 */             return null;
/*      */           }
/*  820 */           bytes[currentIndex++] = (byte)begin;
/*  821 */           value = 0;
/*  822 */           begin = -1;
/*      */           break;
/*      */         default:
/*  825 */           if (!isValidHexChar(c) || (ipv4Separators > 0 && !isValidNumericChar(c))) {
/*  826 */             return null;
/*      */           }
/*  828 */           if (begin < 0) {
/*  829 */             begin = i;
/*  830 */           } else if (i - begin > 4) {
/*  831 */             return null;
/*      */           } 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*  837 */           value += StringUtil.decodeHexNibble(c) << i - begin << 2;
/*      */           break;
/*      */       } 
/*      */     
/*      */     } 
/*  842 */     boolean isCompressed = (compressBegin > 0);
/*      */     
/*  844 */     if (ipv4Separators > 0) {
/*  845 */       if ((begin > 0 && i - begin > 3) || ipv4Separators != 3 || currentIndex >= bytes.length)
/*      */       {
/*      */         
/*  848 */         return null;
/*      */       }
/*  850 */       if (ipv6Separators == 0) {
/*  851 */         compressLength = 12;
/*  852 */       } else if (ipv6Separators >= 2 && ((!isCompressed && ipv6Separators == 6 && ip
/*  853 */         .charAt(0) != ':') || (isCompressed && ipv6Separators < 8 && (ip
/*      */         
/*  855 */         .charAt(0) != ':' || compressBegin <= 2)))) {
/*  856 */         compressLength -= 2;
/*      */       } else {
/*  858 */         return null;
/*      */       } 
/*  860 */       value <<= 3 - i - begin << 2;
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  865 */       begin = (value & 0xF) * 100 + (value >> 4 & 0xF) * 10 + (value >> 8 & 0xF);
/*  866 */       if (begin < 0 || begin > 255) {
/*  867 */         return null;
/*      */       }
/*  869 */       bytes[currentIndex++] = (byte)begin;
/*      */     } else {
/*  871 */       int tmp = ipLength - 1;
/*  872 */       if ((begin > 0 && i - begin > 4) || ipv6Separators < 2 || (!isCompressed && (ipv6Separators + 1 != 8 || ip
/*      */ 
/*      */         
/*  875 */         .charAt(0) == ':' || ip.charAt(tmp) == ':')) || (isCompressed && (ipv6Separators > 8 || (ipv6Separators == 8 && ((compressBegin <= 2 && ip
/*      */ 
/*      */         
/*  878 */         .charAt(0) != ':') || (compressBegin >= 14 && ip
/*  879 */         .charAt(tmp) != ':'))))) || currentIndex + 1 >= bytes.length || (begin < 0 && ip
/*      */         
/*  881 */         .charAt(tmp - 1) != ':') || (compressBegin > 2 && ip
/*  882 */         .charAt(0) == ':')) {
/*  883 */         return null;
/*      */       }
/*  885 */       if (begin >= 0 && i - begin <= 4) {
/*  886 */         value <<= 4 - i - begin << 2;
/*      */       }
/*      */ 
/*      */ 
/*      */       
/*  891 */       bytes[currentIndex++] = (byte)((value & 0xF) << 4 | value >> 4 & 0xF);
/*  892 */       bytes[currentIndex++] = (byte)((value >> 8 & 0xF) << 4 | value >> 12 & 0xF);
/*      */     } 
/*      */     
/*  895 */     i = currentIndex + compressLength;
/*  896 */     if (needsShift || i >= bytes.length) {
/*      */       
/*  898 */       if (i >= bytes.length) {
/*  899 */         compressBegin++;
/*      */       }
/*  901 */       for (i = currentIndex; i < bytes.length; i++) {
/*  902 */         for (begin = bytes.length - 1; begin >= compressBegin; begin--) {
/*  903 */           bytes[begin] = bytes[begin - 1];
/*      */         }
/*  905 */         bytes[begin] = 0;
/*  906 */         compressBegin++;
/*      */       } 
/*      */     } else {
/*      */       
/*  910 */       for (i = 0; i < compressLength; ) {
/*  911 */         begin = i + compressBegin;
/*  912 */         currentIndex = begin + compressLength;
/*  913 */         if (currentIndex < bytes.length) {
/*  914 */           bytes[currentIndex] = bytes[begin];
/*  915 */           bytes[begin] = 0;
/*      */           
/*      */           i++;
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/*  922 */     if (ipv4Separators > 0) {
/*      */ 
/*      */ 
/*      */       
/*  926 */       bytes[11] = -1; bytes[10] = -1;
/*      */     } 
/*      */     
/*  929 */     return bytes;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String toSocketAddressString(InetSocketAddress addr) {
/*      */     StringBuilder sb;
/*  940 */     String port = String.valueOf(addr.getPort());
/*      */ 
/*      */     
/*  943 */     if (addr.isUnresolved()) {
/*  944 */       String hostname = getHostname(addr);
/*  945 */       sb = newSocketAddressStringBuilder(hostname, port, !isValidIpV6Address(hostname));
/*      */     } else {
/*  947 */       InetAddress address = addr.getAddress();
/*  948 */       String hostString = toAddressString(address);
/*  949 */       sb = newSocketAddressStringBuilder(hostString, port, address instanceof Inet4Address);
/*      */     } 
/*  951 */     return sb.append(':').append(port).toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String toSocketAddressString(String host, int port) {
/*  958 */     String portStr = String.valueOf(port);
/*  959 */     return newSocketAddressStringBuilder(host, portStr, 
/*  960 */         !isValidIpV6Address(host)).append(':').append(portStr).toString();
/*      */   }
/*      */   
/*      */   private static StringBuilder newSocketAddressStringBuilder(String host, String port, boolean ipv4) {
/*  964 */     int hostLen = host.length();
/*  965 */     if (ipv4)
/*      */     {
/*  967 */       return (new StringBuilder(hostLen + 1 + port.length())).append(host);
/*      */     }
/*      */     
/*  970 */     StringBuilder stringBuilder = new StringBuilder(hostLen + 3 + port.length());
/*  971 */     if (hostLen > 1 && host.charAt(0) == '[' && host.charAt(hostLen - 1) == ']') {
/*  972 */       return stringBuilder.append(host);
/*      */     }
/*  974 */     return stringBuilder.append('[').append(host).append(']');
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String toAddressString(InetAddress ip) {
/*  990 */     return toAddressString(ip, false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String toAddressString(InetAddress ip, boolean ipv4Mapped) {
/* 1018 */     if (ip instanceof Inet4Address) {
/* 1019 */       return ip.getHostAddress();
/*      */     }
/* 1021 */     if (!(ip instanceof Inet6Address)) {
/* 1022 */       throw new IllegalArgumentException("Unhandled type: " + ip);
/*      */     }
/*      */     
/* 1025 */     return toAddressString(ip.getAddress(), 0, ipv4Mapped);
/*      */   }
/*      */   
/*      */   private static String toAddressString(byte[] bytes, int offset, boolean ipv4Mapped) {
/* 1029 */     int[] words = new int[8];
/*      */     
/* 1031 */     int end = offset + words.length; int i;
/* 1032 */     for (i = offset; i < end; i++) {
/* 1033 */       words[i] = (bytes[i << 1] & 0xFF) << 8 | bytes[(i << 1) + 1] & 0xFF;
/*      */     }
/*      */ 
/*      */     
/* 1037 */     int currentStart = -1;
/*      */     
/* 1039 */     int shortestStart = -1;
/* 1040 */     int shortestLength = 0;
/* 1041 */     for (i = 0; i < words.length; i++) {
/* 1042 */       if (words[i] == 0) {
/* 1043 */         if (currentStart < 0) {
/* 1044 */           currentStart = i;
/*      */         }
/* 1046 */       } else if (currentStart >= 0) {
/* 1047 */         int currentLength = i - currentStart;
/* 1048 */         if (currentLength > shortestLength) {
/* 1049 */           shortestStart = currentStart;
/* 1050 */           shortestLength = currentLength;
/*      */         } 
/* 1052 */         currentStart = -1;
/*      */       } 
/*      */     } 
/*      */     
/* 1056 */     if (currentStart >= 0) {
/* 1057 */       int currentLength = i - currentStart;
/* 1058 */       if (currentLength > shortestLength) {
/* 1059 */         shortestStart = currentStart;
/* 1060 */         shortestLength = currentLength;
/*      */       } 
/*      */     } 
/*      */     
/* 1064 */     if (shortestLength == 1) {
/* 1065 */       shortestLength = 0;
/* 1066 */       shortestStart = -1;
/*      */     } 
/*      */ 
/*      */     
/* 1070 */     int shortestEnd = shortestStart + shortestLength;
/* 1071 */     StringBuilder b = new StringBuilder(39);
/* 1072 */     if (shortestEnd < 0) {
/* 1073 */       b.append(Integer.toHexString(words[0]));
/* 1074 */       for (i = 1; i < words.length; i++) {
/* 1075 */         b.append(':');
/* 1076 */         b.append(Integer.toHexString(words[i]));
/*      */       } 
/*      */     } else {
/*      */       boolean isIpv4Mapped;
/*      */       
/* 1081 */       if (inRangeEndExclusive(0, shortestStart, shortestEnd)) {
/* 1082 */         b.append("::");
/* 1083 */         isIpv4Mapped = (ipv4Mapped && shortestEnd == 5 && words[5] == 65535);
/*      */       } else {
/* 1085 */         b.append(Integer.toHexString(words[0]));
/* 1086 */         isIpv4Mapped = false;
/*      */       } 
/* 1088 */       for (i = 1; i < words.length; i++) {
/* 1089 */         if (!inRangeEndExclusive(i, shortestStart, shortestEnd)) {
/* 1090 */           if (!inRangeEndExclusive(i - 1, shortestStart, shortestEnd))
/*      */           {
/* 1092 */             if (!isIpv4Mapped || i == 6) {
/* 1093 */               b.append(':');
/*      */             } else {
/* 1095 */               b.append('.');
/*      */             } 
/*      */           }
/* 1098 */           if (isIpv4Mapped && i > 5) {
/* 1099 */             b.append(words[i] >> 8);
/* 1100 */             b.append('.');
/* 1101 */             b.append(words[i] & 0xFF);
/*      */           } else {
/* 1103 */             b.append(Integer.toHexString(words[i]));
/*      */           } 
/* 1105 */         } else if (!inRangeEndExclusive(i - 1, shortestStart, shortestEnd)) {
/*      */           
/* 1107 */           b.append("::");
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/* 1112 */     return b.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String getHostname(InetSocketAddress addr) {
/* 1122 */     return (PlatformDependent.javaVersion() >= 7) ? addr.getHostString() : addr.getHostName();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static boolean inRangeEndExclusive(int value, int start, int end) {
/* 1137 */     return (value >= start && value < end);
/*      */   }
/*      */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\nett\\util\NetUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */