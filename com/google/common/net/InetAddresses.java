/*     */ package com.google.common.net;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.MoreObjects;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Splitter;
/*     */ import com.google.common.hash.Hashing;
/*     */ import com.google.common.io.ByteStreams;
/*     */ import com.google.common.primitives.Ints;
/*     */ import java.net.Inet4Address;
/*     */ import java.net.Inet6Address;
/*     */ import java.net.InetAddress;
/*     */ import java.net.UnknownHostException;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.Arrays;
/*     */ import java.util.Locale;
/*     */ import javax.annotation.Nullable;
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
/*     */ @Beta
/*     */ @GwtIncompatible
/*     */ public final class InetAddresses
/*     */ {
/*     */   private static final int IPV4_PART_COUNT = 4;
/*     */   private static final int IPV6_PART_COUNT = 8;
/* 106 */   private static final Splitter IPV4_SPLITTER = Splitter.on('.').limit(4);
/* 107 */   private static final Inet4Address LOOPBACK4 = (Inet4Address)forString("127.0.0.1");
/* 108 */   private static final Inet4Address ANY4 = (Inet4Address)forString("0.0.0.0");
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
/*     */   private static Inet4Address getInet4Address(byte[] bytes) {
/* 120 */     Preconditions.checkArgument((bytes.length == 4), "Byte array has invalid length for an IPv4 address: %s != 4.", bytes.length);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 126 */     return (Inet4Address)bytesToInetAddress(bytes);
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
/*     */   public static InetAddress forString(String ipString) {
/* 140 */     byte[] addr = ipStringToBytes(ipString);
/*     */ 
/*     */     
/* 143 */     if (addr == null) {
/* 144 */       throw formatIllegalArgumentException("'%s' is not an IP string literal.", new Object[] { ipString });
/*     */     }
/*     */     
/* 147 */     return bytesToInetAddress(addr);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isInetAddress(String ipString) {
/* 158 */     return (ipStringToBytes(ipString) != null);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private static byte[] ipStringToBytes(String ipString) {
/* 164 */     boolean hasColon = false;
/* 165 */     boolean hasDot = false;
/* 166 */     for (int i = 0; i < ipString.length(); i++) {
/* 167 */       char c = ipString.charAt(i);
/* 168 */       if (c == '.') {
/* 169 */         hasDot = true;
/* 170 */       } else if (c == ':') {
/* 171 */         if (hasDot) {
/* 172 */           return null;
/*     */         }
/* 174 */         hasColon = true;
/* 175 */       } else if (Character.digit(c, 16) == -1) {
/* 176 */         return null;
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 181 */     if (hasColon) {
/* 182 */       if (hasDot) {
/* 183 */         ipString = convertDottedQuadToHex(ipString);
/* 184 */         if (ipString == null) {
/* 185 */           return null;
/*     */         }
/*     */       } 
/* 188 */       return textToNumericFormatV6(ipString);
/* 189 */     }  if (hasDot) {
/* 190 */       return textToNumericFormatV4(ipString);
/*     */     }
/* 192 */     return null;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private static byte[] textToNumericFormatV4(String ipString) {
/* 197 */     byte[] bytes = new byte[4];
/* 198 */     int i = 0;
/*     */     try {
/* 200 */       for (String octet : IPV4_SPLITTER.split(ipString)) {
/* 201 */         bytes[i++] = parseOctet(octet);
/*     */       }
/* 203 */     } catch (NumberFormatException ex) {
/* 204 */       return null;
/*     */     } 
/*     */     
/* 207 */     return (i == 4) ? bytes : null;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private static byte[] textToNumericFormatV6(String ipString) {
/*     */     int partsHi, partsLo;
/* 213 */     String[] parts = ipString.split(":", 10);
/* 214 */     if (parts.length < 3 || parts.length > 9) {
/* 215 */       return null;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 220 */     int skipIndex = -1;
/* 221 */     for (int i = 1; i < parts.length - 1; i++) {
/* 222 */       if (parts[i].length() == 0) {
/* 223 */         if (skipIndex >= 0) {
/* 224 */           return null;
/*     */         }
/* 226 */         skipIndex = i;
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 232 */     if (skipIndex >= 0) {
/*     */       
/* 234 */       partsHi = skipIndex;
/* 235 */       partsLo = parts.length - skipIndex - 1;
/* 236 */       if (parts[0].length() == 0 && --partsHi != 0) {
/* 237 */         return null;
/*     */       }
/* 239 */       if (parts[parts.length - 1].length() == 0 && --partsLo != 0) {
/* 240 */         return null;
/*     */       }
/*     */     }
/*     */     else {
/*     */       
/* 245 */       partsHi = parts.length;
/* 246 */       partsLo = 0;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 251 */     int partsSkipped = 8 - partsHi + partsLo;
/* 252 */     if ((skipIndex >= 0) ? (partsSkipped >= 1) : (partsSkipped == 0)) {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 257 */       ByteBuffer rawBytes = ByteBuffer.allocate(16); try {
/*     */         int j;
/* 259 */         for (j = 0; j < partsHi; j++) {
/* 260 */           rawBytes.putShort(parseHextet(parts[j]));
/*     */         }
/* 262 */         for (j = 0; j < partsSkipped; j++) {
/* 263 */           rawBytes.putShort((short)0);
/*     */         }
/* 265 */         for (j = partsLo; j > 0; j--) {
/* 266 */           rawBytes.putShort(parseHextet(parts[parts.length - j]));
/*     */         }
/* 268 */       } catch (NumberFormatException ex) {
/* 269 */         return null;
/*     */       } 
/* 271 */       return rawBytes.array();
/*     */     } 
/*     */     return null;
/*     */   } @Nullable
/*     */   private static String convertDottedQuadToHex(String ipString) {
/* 276 */     int lastColon = ipString.lastIndexOf(':');
/* 277 */     String initialPart = ipString.substring(0, lastColon + 1);
/* 278 */     String dottedQuad = ipString.substring(lastColon + 1);
/* 279 */     byte[] quad = textToNumericFormatV4(dottedQuad);
/* 280 */     if (quad == null) {
/* 281 */       return null;
/*     */     }
/* 283 */     String penultimate = Integer.toHexString((quad[0] & 0xFF) << 8 | quad[1] & 0xFF);
/* 284 */     String ultimate = Integer.toHexString((quad[2] & 0xFF) << 8 | quad[3] & 0xFF);
/* 285 */     return initialPart + penultimate + ":" + ultimate;
/*     */   }
/*     */ 
/*     */   
/*     */   private static byte parseOctet(String ipPart) {
/* 290 */     int octet = Integer.parseInt(ipPart);
/*     */ 
/*     */     
/* 293 */     if (octet > 255 || (ipPart.startsWith("0") && ipPart.length() > 1)) {
/* 294 */       throw new NumberFormatException();
/*     */     }
/* 296 */     return (byte)octet;
/*     */   }
/*     */ 
/*     */   
/*     */   private static short parseHextet(String ipPart) {
/* 301 */     int hextet = Integer.parseInt(ipPart, 16);
/* 302 */     if (hextet > 65535) {
/* 303 */       throw new NumberFormatException();
/*     */     }
/* 305 */     return (short)hextet;
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
/*     */   private static InetAddress bytesToInetAddress(byte[] addr) {
/*     */     try {
/* 320 */       return InetAddress.getByAddress(addr);
/* 321 */     } catch (UnknownHostException e) {
/* 322 */       throw new AssertionError(e);
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
/*     */ 
/*     */   
/*     */   public static String toAddrString(InetAddress ip) {
/* 342 */     Preconditions.checkNotNull(ip);
/* 343 */     if (ip instanceof Inet4Address)
/*     */     {
/* 345 */       return ip.getHostAddress();
/*     */     }
/* 347 */     Preconditions.checkArgument(ip instanceof Inet6Address);
/* 348 */     byte[] bytes = ip.getAddress();
/* 349 */     int[] hextets = new int[8];
/* 350 */     for (int i = 0; i < hextets.length; i++) {
/* 351 */       hextets[i] = Ints.fromBytes((byte)0, (byte)0, bytes[2 * i], bytes[2 * i + 1]);
/*     */     }
/* 353 */     compressLongestRunOfZeroes(hextets);
/* 354 */     return hextetsToIPv6String(hextets);
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
/*     */   private static void compressLongestRunOfZeroes(int[] hextets) {
/* 366 */     int bestRunStart = -1;
/* 367 */     int bestRunLength = -1;
/* 368 */     int runStart = -1;
/* 369 */     for (int i = 0; i < hextets.length + 1; i++) {
/* 370 */       if (i < hextets.length && hextets[i] == 0) {
/* 371 */         if (runStart < 0) {
/* 372 */           runStart = i;
/*     */         }
/* 374 */       } else if (runStart >= 0) {
/* 375 */         int runLength = i - runStart;
/* 376 */         if (runLength > bestRunLength) {
/* 377 */           bestRunStart = runStart;
/* 378 */           bestRunLength = runLength;
/*     */         } 
/* 380 */         runStart = -1;
/*     */       } 
/*     */     } 
/* 383 */     if (bestRunLength >= 2) {
/* 384 */       Arrays.fill(hextets, bestRunStart, bestRunStart + bestRunLength, -1);
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
/*     */   private static String hextetsToIPv6String(int[] hextets) {
/* 401 */     StringBuilder buf = new StringBuilder(39);
/* 402 */     boolean lastWasNumber = false;
/* 403 */     for (int i = 0; i < hextets.length; i++) {
/* 404 */       boolean thisIsNumber = (hextets[i] >= 0);
/* 405 */       if (thisIsNumber) {
/* 406 */         if (lastWasNumber) {
/* 407 */           buf.append(':');
/*     */         }
/* 409 */         buf.append(Integer.toHexString(hextets[i]));
/*     */       }
/* 411 */       else if (i == 0 || lastWasNumber) {
/* 412 */         buf.append("::");
/*     */       } 
/*     */       
/* 415 */       lastWasNumber = thisIsNumber;
/*     */     } 
/* 417 */     return buf.toString();
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
/*     */   public static String toUriString(InetAddress ip) {
/* 441 */     if (ip instanceof Inet6Address) {
/* 442 */       return "[" + toAddrString(ip) + "]";
/*     */     }
/* 444 */     return toAddrString(ip);
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
/*     */   public static InetAddress forUriString(String hostAddr) {
/* 462 */     InetAddress addr = forUriStringNoThrow(hostAddr);
/* 463 */     if (addr == null) {
/* 464 */       throw formatIllegalArgumentException("Not a valid URI IP literal: '%s'", new Object[] { hostAddr });
/*     */     }
/*     */     
/* 467 */     return addr;
/*     */   } @Nullable
/*     */   private static InetAddress forUriStringNoThrow(String hostAddr) {
/*     */     String ipString;
/*     */     int expectBytes;
/* 472 */     Preconditions.checkNotNull(hostAddr);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 477 */     if (hostAddr.startsWith("[") && hostAddr.endsWith("]")) {
/* 478 */       ipString = hostAddr.substring(1, hostAddr.length() - 1);
/* 479 */       expectBytes = 16;
/*     */     } else {
/* 481 */       ipString = hostAddr;
/* 482 */       expectBytes = 4;
/*     */     } 
/*     */ 
/*     */     
/* 486 */     byte[] addr = ipStringToBytes(ipString);
/* 487 */     if (addr == null || addr.length != expectBytes) {
/* 488 */       return null;
/*     */     }
/*     */     
/* 491 */     return bytesToInetAddress(addr);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isUriInetAddress(String ipString) {
/* 502 */     return (forUriStringNoThrow(ipString) != null);
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
/*     */   public static boolean isCompatIPv4Address(Inet6Address ip) {
/* 524 */     if (!ip.isIPv4CompatibleAddress()) {
/* 525 */       return false;
/*     */     }
/*     */     
/* 528 */     byte[] bytes = ip.getAddress();
/* 529 */     if (bytes[12] == 0 && bytes[13] == 0 && bytes[14] == 0 && (bytes[15] == 0 || bytes[15] == 1))
/*     */     {
/*     */ 
/*     */       
/* 533 */       return false;
/*     */     }
/*     */     
/* 536 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Inet4Address getCompatIPv4Address(Inet6Address ip) {
/* 547 */     Preconditions.checkArgument(
/* 548 */         isCompatIPv4Address(ip), "Address '%s' is not IPv4-compatible.", toAddrString(ip));
/*     */     
/* 550 */     return getInet4Address(Arrays.copyOfRange(ip.getAddress(), 12, 16));
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
/*     */   public static boolean is6to4Address(Inet6Address ip) {
/* 566 */     byte[] bytes = ip.getAddress();
/* 567 */     return (bytes[0] == 32 && bytes[1] == 2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Inet4Address get6to4IPv4Address(Inet6Address ip) {
/* 578 */     Preconditions.checkArgument(is6to4Address(ip), "Address '%s' is not a 6to4 address.", toAddrString(ip));
/*     */     
/* 580 */     return getInet4Address(Arrays.copyOfRange(ip.getAddress(), 2, 6));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Beta
/*     */   public static final class TeredoInfo
/*     */   {
/*     */     private final Inet4Address server;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final Inet4Address client;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final int port;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final int flags;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public TeredoInfo(@Nullable Inet4Address server, @Nullable Inet4Address client, int port, int flags) {
/* 615 */       Preconditions.checkArgument((port >= 0 && port <= 65535), "port '%s' is out of range (0 <= port <= 0xffff)", port);
/*     */       
/* 617 */       Preconditions.checkArgument((flags >= 0 && flags <= 65535), "flags '%s' is out of range (0 <= flags <= 0xffff)", flags);
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 622 */       this.server = (Inet4Address)MoreObjects.firstNonNull(server, InetAddresses.ANY4);
/* 623 */       this.client = (Inet4Address)MoreObjects.firstNonNull(client, InetAddresses.ANY4);
/* 624 */       this.port = port;
/* 625 */       this.flags = flags;
/*     */     }
/*     */     
/*     */     public Inet4Address getServer() {
/* 629 */       return this.server;
/*     */     }
/*     */     
/*     */     public Inet4Address getClient() {
/* 633 */       return this.client;
/*     */     }
/*     */     
/*     */     public int getPort() {
/* 637 */       return this.port;
/*     */     }
/*     */     
/*     */     public int getFlags() {
/* 641 */       return this.flags;
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
/*     */   public static boolean isTeredoAddress(Inet6Address ip) {
/* 654 */     byte[] bytes = ip.getAddress();
/* 655 */     return (bytes[0] == 32 && bytes[1] == 1 && bytes[2] == 0 && bytes[3] == 0);
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
/*     */   public static TeredoInfo getTeredoInfo(Inet6Address ip) {
/* 669 */     Preconditions.checkArgument(isTeredoAddress(ip), "Address '%s' is not a Teredo address.", toAddrString(ip));
/*     */     
/* 671 */     byte[] bytes = ip.getAddress();
/* 672 */     Inet4Address server = getInet4Address(Arrays.copyOfRange(bytes, 4, 8));
/*     */     
/* 674 */     int flags = ByteStreams.newDataInput(bytes, 8).readShort() & 0xFFFF;
/*     */ 
/*     */     
/* 677 */     int port = (ByteStreams.newDataInput(bytes, 10).readShort() ^ 0xFFFFFFFF) & 0xFFFF;
/*     */     
/* 679 */     byte[] clientBytes = Arrays.copyOfRange(bytes, 12, 16);
/* 680 */     for (int i = 0; i < clientBytes.length; i++)
/*     */     {
/* 682 */       clientBytes[i] = (byte)(clientBytes[i] ^ 0xFFFFFFFF);
/*     */     }
/* 684 */     Inet4Address client = getInet4Address(clientBytes);
/*     */     
/* 686 */     return new TeredoInfo(server, client, port, flags);
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
/*     */   public static boolean isIsatapAddress(Inet6Address ip) {
/* 706 */     if (isTeredoAddress(ip)) {
/* 707 */       return false;
/*     */     }
/*     */     
/* 710 */     byte[] bytes = ip.getAddress();
/*     */     
/* 712 */     if ((bytes[8] | 0x3) != 3)
/*     */     {
/*     */ 
/*     */       
/* 716 */       return false;
/*     */     }
/*     */     
/* 719 */     return (bytes[9] == 0 && bytes[10] == 94 && bytes[11] == -2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Inet4Address getIsatapIPv4Address(Inet6Address ip) {
/* 730 */     Preconditions.checkArgument(isIsatapAddress(ip), "Address '%s' is not an ISATAP address.", toAddrString(ip));
/*     */     
/* 732 */     return getInet4Address(Arrays.copyOfRange(ip.getAddress(), 12, 16));
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
/*     */   public static boolean hasEmbeddedIPv4ClientAddress(Inet6Address ip) {
/* 748 */     return (isCompatIPv4Address(ip) || is6to4Address(ip) || isTeredoAddress(ip));
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
/*     */   public static Inet4Address getEmbeddedIPv4ClientAddress(Inet6Address ip) {
/* 764 */     if (isCompatIPv4Address(ip)) {
/* 765 */       return getCompatIPv4Address(ip);
/*     */     }
/*     */     
/* 768 */     if (is6to4Address(ip)) {
/* 769 */       return get6to4IPv4Address(ip);
/*     */     }
/*     */     
/* 772 */     if (isTeredoAddress(ip)) {
/* 773 */       return getTeredoInfo(ip).getClient();
/*     */     }
/*     */     
/* 776 */     throw formatIllegalArgumentException("'%s' has no embedded IPv4 address.", new Object[] { toAddrString(ip) });
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
/*     */   public static boolean isMappedIPv4Address(String ipString) {
/* 798 */     byte[] bytes = ipStringToBytes(ipString);
/* 799 */     if (bytes != null && bytes.length == 16) {
/* 800 */       int i; for (i = 0; i < 10; i++) {
/* 801 */         if (bytes[i] != 0) {
/* 802 */           return false;
/*     */         }
/*     */       } 
/* 805 */       for (i = 10; i < 12; i++) {
/* 806 */         if (bytes[i] != -1) {
/* 807 */           return false;
/*     */         }
/*     */       } 
/* 810 */       return true;
/*     */     } 
/* 812 */     return false;
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
/*     */   public static Inet4Address getCoercedIPv4Address(InetAddress ip) {
/* 834 */     if (ip instanceof Inet4Address) {
/* 835 */       return (Inet4Address)ip;
/*     */     }
/*     */ 
/*     */     
/* 839 */     byte[] bytes = ip.getAddress();
/* 840 */     boolean leadingBytesOfZero = true;
/* 841 */     for (int i = 0; i < 15; i++) {
/* 842 */       if (bytes[i] != 0) {
/* 843 */         leadingBytesOfZero = false;
/*     */         break;
/*     */       } 
/*     */     } 
/* 847 */     if (leadingBytesOfZero && bytes[15] == 1)
/* 848 */       return LOOPBACK4; 
/* 849 */     if (leadingBytesOfZero && bytes[15] == 0) {
/* 850 */       return ANY4;
/*     */     }
/*     */     
/* 853 */     Inet6Address ip6 = (Inet6Address)ip;
/* 854 */     long addressAsLong = 0L;
/* 855 */     if (hasEmbeddedIPv4ClientAddress(ip6)) {
/* 856 */       addressAsLong = getEmbeddedIPv4ClientAddress(ip6).hashCode();
/*     */     }
/*     */     else {
/*     */       
/* 860 */       addressAsLong = ByteBuffer.wrap(ip6.getAddress(), 0, 8).getLong();
/*     */     } 
/*     */ 
/*     */     
/* 864 */     int coercedHash = Hashing.murmur3_32().hashLong(addressAsLong).asInt();
/*     */ 
/*     */     
/* 867 */     coercedHash |= 0xE0000000;
/*     */ 
/*     */ 
/*     */     
/* 871 */     if (coercedHash == -1) {
/* 872 */       coercedHash = -2;
/*     */     }
/*     */     
/* 875 */     return getInet4Address(Ints.toByteArray(coercedHash));
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
/*     */   public static int coerceToInteger(InetAddress ip) {
/* 897 */     return ByteStreams.newDataInput(getCoercedIPv4Address(ip).getAddress()).readInt();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Inet4Address fromInteger(int address) {
/* 907 */     return getInet4Address(Ints.toByteArray(address));
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
/*     */   public static InetAddress fromLittleEndianByteArray(byte[] addr) throws UnknownHostException {
/* 921 */     byte[] reversed = new byte[addr.length];
/* 922 */     for (int i = 0; i < addr.length; i++) {
/* 923 */       reversed[i] = addr[addr.length - i - 1];
/*     */     }
/* 925 */     return InetAddress.getByAddress(reversed);
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
/*     */   public static InetAddress decrement(InetAddress address) {
/* 938 */     byte[] addr = address.getAddress();
/* 939 */     int i = addr.length - 1;
/* 940 */     while (i >= 0 && addr[i] == 0) {
/* 941 */       addr[i] = -1;
/* 942 */       i--;
/*     */     } 
/*     */     
/* 945 */     Preconditions.checkArgument((i >= 0), "Decrementing %s would wrap.", address);
/*     */     
/* 947 */     addr[i] = (byte)(addr[i] - 1);
/* 948 */     return bytesToInetAddress(addr);
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
/*     */   public static InetAddress increment(InetAddress address) {
/* 961 */     byte[] addr = address.getAddress();
/* 962 */     int i = addr.length - 1;
/* 963 */     while (i >= 0 && addr[i] == -1) {
/* 964 */       addr[i] = 0;
/* 965 */       i--;
/*     */     } 
/*     */     
/* 968 */     Preconditions.checkArgument((i >= 0), "Incrementing %s would wrap.", address);
/*     */     
/* 970 */     addr[i] = (byte)(addr[i] + 1);
/* 971 */     return bytesToInetAddress(addr);
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
/*     */   public static boolean isMaximum(InetAddress address) {
/* 983 */     byte[] addr = address.getAddress();
/* 984 */     for (int i = 0; i < addr.length; i++) {
/* 985 */       if (addr[i] != -1) {
/* 986 */         return false;
/*     */       }
/*     */     } 
/* 989 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   private static IllegalArgumentException formatIllegalArgumentException(String format, Object... args) {
/* 994 */     return new IllegalArgumentException(String.format(Locale.ROOT, format, args));
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\net\InetAddresses.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */