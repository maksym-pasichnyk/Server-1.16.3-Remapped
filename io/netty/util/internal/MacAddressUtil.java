/*     */ package io.netty.util.internal;
/*     */ 
/*     */ import io.netty.util.NetUtil;
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*     */ import java.net.InetAddress;
/*     */ import java.net.NetworkInterface;
/*     */ import java.net.SocketException;
/*     */ import java.util.Arrays;
/*     */ import java.util.Enumeration;
/*     */ import java.util.LinkedHashMap;
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
/*     */ public final class MacAddressUtil
/*     */ {
/*  35 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(MacAddressUtil.class);
/*     */ 
/*     */ 
/*     */   
/*     */   private static final int EUI64_MAC_ADDRESS_LENGTH = 8;
/*     */ 
/*     */ 
/*     */   
/*     */   private static final int EUI48_MAC_ADDRESS_LENGTH = 6;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte[] bestAvailableMac() {
/*  49 */     byte[] newAddr, bestMacAddr = EmptyArrays.EMPTY_BYTES;
/*  50 */     InetAddress bestInetAddr = NetUtil.LOCALHOST4;
/*     */ 
/*     */     
/*  53 */     Map<NetworkInterface, InetAddress> ifaces = new LinkedHashMap<NetworkInterface, InetAddress>();
/*     */     try {
/*  55 */       Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
/*  56 */       if (interfaces != null) {
/*  57 */         while (interfaces.hasMoreElements()) {
/*  58 */           NetworkInterface iface = interfaces.nextElement();
/*     */           
/*  60 */           Enumeration<InetAddress> addrs = SocketUtils.addressesFromNetworkInterface(iface);
/*  61 */           if (addrs.hasMoreElements()) {
/*  62 */             InetAddress a = addrs.nextElement();
/*  63 */             if (!a.isLoopbackAddress()) {
/*  64 */               ifaces.put(iface, a);
/*     */             }
/*     */           } 
/*     */         } 
/*     */       }
/*  69 */     } catch (SocketException e) {
/*  70 */       logger.warn("Failed to retrieve the list of available network interfaces", e);
/*     */     } 
/*     */     
/*  73 */     for (Map.Entry<NetworkInterface, InetAddress> entry : ifaces.entrySet()) {
/*  74 */       byte[] macAddr; NetworkInterface iface = entry.getKey();
/*  75 */       InetAddress inetAddr = entry.getValue();
/*  76 */       if (iface.isVirtual()) {
/*     */         continue;
/*     */       }
/*     */ 
/*     */       
/*     */       try {
/*  82 */         macAddr = SocketUtils.hardwareAddressFromNetworkInterface(iface);
/*  83 */       } catch (SocketException e) {
/*  84 */         logger.debug("Failed to get the hardware address of a network interface: {}", iface, e);
/*     */         
/*     */         continue;
/*     */       } 
/*  88 */       boolean replace = false;
/*  89 */       int res = compareAddresses(bestMacAddr, macAddr);
/*  90 */       if (res < 0) {
/*     */         
/*  92 */         replace = true;
/*  93 */       } else if (res == 0) {
/*     */         
/*  95 */         res = compareAddresses(bestInetAddr, inetAddr);
/*  96 */         if (res < 0) {
/*     */           
/*  98 */           replace = true;
/*  99 */         } else if (res == 0) {
/*     */           
/* 101 */           if (bestMacAddr.length < macAddr.length) {
/* 102 */             replace = true;
/*     */           }
/*     */         } 
/*     */       } 
/*     */       
/* 107 */       if (replace) {
/* 108 */         bestMacAddr = macAddr;
/* 109 */         bestInetAddr = inetAddr;
/*     */       } 
/*     */     } 
/*     */     
/* 113 */     if (bestMacAddr == EmptyArrays.EMPTY_BYTES) {
/* 114 */       return null;
/*     */     }
/*     */     
/* 117 */     switch (bestMacAddr.length)
/*     */     { case 6:
/* 119 */         newAddr = new byte[8];
/* 120 */         System.arraycopy(bestMacAddr, 0, newAddr, 0, 3);
/* 121 */         newAddr[3] = -1;
/* 122 */         newAddr[4] = -2;
/* 123 */         System.arraycopy(bestMacAddr, 3, newAddr, 5, 3);
/* 124 */         bestMacAddr = newAddr;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 130 */         return bestMacAddr; }  bestMacAddr = Arrays.copyOf(bestMacAddr, 8); return bestMacAddr;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte[] defaultMachineId() {
/* 138 */     byte[] bestMacAddr = bestAvailableMac();
/* 139 */     if (bestMacAddr == null) {
/* 140 */       bestMacAddr = new byte[8];
/* 141 */       PlatformDependent.threadLocalRandom().nextBytes(bestMacAddr);
/* 142 */       logger.warn("Failed to find a usable hardware address from the network interfaces; using random bytes: {}", 
/*     */           
/* 144 */           formatAddress(bestMacAddr));
/*     */     } 
/* 146 */     return bestMacAddr;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte[] parseMAC(String value) {
/*     */     byte[] machineId;
/*     */     char separator;
/* 157 */     switch (value.length()) {
/*     */       case 17:
/* 159 */         separator = value.charAt(2);
/* 160 */         validateMacSeparator(separator);
/* 161 */         machineId = new byte[6];
/*     */         break;
/*     */       case 23:
/* 164 */         separator = value.charAt(2);
/* 165 */         validateMacSeparator(separator);
/* 166 */         machineId = new byte[8];
/*     */         break;
/*     */       default:
/* 169 */         throw new IllegalArgumentException("value is not supported [MAC-48, EUI-48, EUI-64]");
/*     */     } 
/*     */     
/* 172 */     int end = machineId.length - 1;
/* 173 */     int j = 0;
/* 174 */     for (int i = 0; i < end; i++, j += 3) {
/* 175 */       int sIndex = j + 2;
/* 176 */       machineId[i] = StringUtil.decodeHexByte(value, j);
/* 177 */       if (value.charAt(sIndex) != separator) {
/* 178 */         throw new IllegalArgumentException("expected separator '" + separator + " but got '" + value
/* 179 */             .charAt(sIndex) + "' at index: " + sIndex);
/*     */       }
/*     */     } 
/*     */     
/* 183 */     machineId[end] = StringUtil.decodeHexByte(value, j);
/*     */     
/* 185 */     return machineId;
/*     */   }
/*     */   
/*     */   private static void validateMacSeparator(char separator) {
/* 189 */     if (separator != ':' && separator != '-') {
/* 190 */       throw new IllegalArgumentException("unsupported separator: " + separator + " (expected: [:-])");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String formatAddress(byte[] addr) {
/* 199 */     StringBuilder buf = new StringBuilder(24);
/* 200 */     for (byte b : addr) {
/* 201 */       buf.append(String.format("%02x:", new Object[] { Integer.valueOf(b & 0xFF) }));
/*     */     } 
/* 203 */     return buf.substring(0, buf.length() - 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static int compareAddresses(byte[] current, byte[] candidate) {
/* 211 */     if (candidate == null || candidate.length < 6) {
/* 212 */       return 1;
/*     */     }
/*     */ 
/*     */     
/* 216 */     boolean onlyZeroAndOne = true;
/* 217 */     for (byte b : candidate) {
/* 218 */       if (b != 0 && b != 1) {
/* 219 */         onlyZeroAndOne = false;
/*     */         
/*     */         break;
/*     */       } 
/*     */     } 
/* 224 */     if (onlyZeroAndOne) {
/* 225 */       return 1;
/*     */     }
/*     */ 
/*     */     
/* 229 */     if ((candidate[0] & 0x1) != 0) {
/* 230 */       return 1;
/*     */     }
/*     */ 
/*     */     
/* 234 */     if ((candidate[0] & 0x2) == 0) {
/* 235 */       if (current.length != 0 && (current[0] & 0x2) == 0)
/*     */       {
/* 237 */         return 0;
/*     */       }
/*     */       
/* 240 */       return -1;
/*     */     } 
/*     */     
/* 243 */     if (current.length != 0 && (current[0] & 0x2) == 0)
/*     */     {
/* 245 */       return 1;
/*     */     }
/*     */     
/* 248 */     return 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int compareAddresses(InetAddress current, InetAddress candidate) {
/* 257 */     return scoreAddress(current) - scoreAddress(candidate);
/*     */   }
/*     */   
/*     */   private static int scoreAddress(InetAddress addr) {
/* 261 */     if (addr.isAnyLocalAddress() || addr.isLoopbackAddress()) {
/* 262 */       return 0;
/*     */     }
/* 264 */     if (addr.isMulticastAddress()) {
/* 265 */       return 1;
/*     */     }
/* 267 */     if (addr.isLinkLocalAddress()) {
/* 268 */       return 2;
/*     */     }
/* 270 */     if (addr.isSiteLocalAddress()) {
/* 271 */       return 3;
/*     */     }
/*     */     
/* 274 */     return 4;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\nett\\util\internal\MacAddressUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */