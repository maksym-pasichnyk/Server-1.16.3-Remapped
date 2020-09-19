/*     */ package org.apache.logging.log4j.core.util;
/*     */ 
/*     */ import java.net.InetAddress;
/*     */ import java.net.NetworkInterface;
/*     */ import java.net.SocketException;
/*     */ import java.net.UnknownHostException;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.security.SecureRandom;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Random;
/*     */ import java.util.UUID;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.apache.logging.log4j.status.StatusLogger;
/*     */ import org.apache.logging.log4j.util.PropertiesUtil;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class UuidUtil
/*     */ {
/*     */   public static final String UUID_SEQUENCE = "org.apache.logging.log4j.uuidSequence";
/*  44 */   private static final Logger LOGGER = (Logger)StatusLogger.getLogger();
/*     */   
/*     */   private static final String ASSIGNED_SEQUENCES = "org.apache.logging.log4j.assignedSequences";
/*     */   
/*  48 */   private static final AtomicInteger COUNT = new AtomicInteger(0);
/*     */   private static final long TYPE1 = 4096L;
/*     */   private static final byte VARIANT = -128;
/*     */   private static final int SEQUENCE_MASK = 16383;
/*     */   private static final long NUM_100NS_INTERVALS_SINCE_UUID_EPOCH = 122192928000000000L;
/*  53 */   private static final long INITIAL_UUID_SEQNO = PropertiesUtil.getProperties().getLongProperty("org.apache.logging.log4j.uuidSequence", 0L);
/*     */   
/*     */   private static final long LEAST;
/*     */   
/*     */   private static final long LOW_MASK = 4294967295L;
/*     */   
/*     */   private static final long MID_MASK = 281470681743360L;
/*     */   
/*     */   private static final long HIGH_MASK = 1152640029630136320L;
/*     */   
/*     */   private static final int NODE_SIZE = 8;
/*     */ 
/*     */   
/*     */   static {
/*  67 */     byte[] mac = getLocalMacAddress();
/*  68 */     Random randomGenerator = new SecureRandom();
/*  69 */     if (mac == null || mac.length == 0) {
/*  70 */       mac = new byte[6];
/*  71 */       randomGenerator.nextBytes(mac);
/*     */     } 
/*  73 */     int length = (mac.length >= 6) ? 6 : mac.length;
/*  74 */     int index = (mac.length >= 6) ? (mac.length - 6) : 0;
/*  75 */     byte[] node = new byte[8];
/*  76 */     node[0] = Byte.MIN_VALUE;
/*  77 */     node[1] = 0;
/*  78 */     for (int i = 2; i < 8; i++) {
/*  79 */       node[i] = 0;
/*     */     }
/*  81 */     System.arraycopy(mac, index, node, index + 2, length);
/*  82 */     ByteBuffer buf = ByteBuffer.wrap(node);
/*  83 */     long rand = INITIAL_UUID_SEQNO;
/*  84 */     String assigned = PropertiesUtil.getProperties().getStringProperty("org.apache.logging.log4j.assignedSequences");
/*     */     
/*  86 */     if (assigned == null) {
/*  87 */       sequences = new long[0];
/*     */     } else {
/*  89 */       String[] array = assigned.split(Patterns.COMMA_SEPARATOR);
/*  90 */       sequences = new long[array.length];
/*  91 */       int j = 0;
/*  92 */       for (String value : array) {
/*  93 */         sequences[j] = Long.parseLong(value);
/*  94 */         j++;
/*     */       } 
/*     */     } 
/*  97 */     if (rand == 0L) {
/*  98 */       rand = randomGenerator.nextLong();
/*     */     }
/* 100 */     rand &= 0x3FFFL;
/*     */     
/*     */     while (true) {
/* 103 */       boolean duplicate = false;
/* 104 */       for (long sequence : sequences) {
/* 105 */         if (sequence == rand) {
/* 106 */           duplicate = true;
/*     */           break;
/*     */         } 
/*     */       } 
/* 110 */       if (duplicate) {
/* 111 */         rand = rand + 1L & 0x3FFFL;
/*     */       }
/* 113 */       if (!duplicate) {
/* 114 */         assigned = (assigned == null) ? Long.toString(rand) : (assigned + ',' + Long.toString(rand));
/* 115 */         System.setProperty("org.apache.logging.log4j.assignedSequences", assigned);
/*     */         
/* 117 */         LEAST = buf.getLong() | rand << 48L;
/*     */         return;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static final int SHIFT_2 = 16;
/*     */ 
/*     */   
/*     */   private static final int SHIFT_4 = 32;
/*     */ 
/*     */   
/*     */   private static final int SHIFT_6 = 48;
/*     */ 
/*     */   
/*     */   private static final int HUNDRED_NANOS_PER_MILLI = 10000;
/*     */ 
/*     */   
/*     */   static {
/*     */     long[] sequences;
/*     */   }
/*     */ 
/*     */   
/*     */   public static UUID getTimeBasedUuid() {
/* 143 */     long time = System.currentTimeMillis() * 10000L + 122192928000000000L + (COUNT.incrementAndGet() % 10000);
/*     */     
/* 145 */     long timeLow = (time & 0xFFFFFFFFL) << 32L;
/* 146 */     long timeMid = (time & 0xFFFF00000000L) >> 16L;
/* 147 */     long timeHi = (time & 0xFFF000000000000L) >> 48L;
/* 148 */     long most = timeLow | timeMid | 0x1000L | timeHi;
/* 149 */     return new UUID(most, LEAST);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static byte[] getLocalMacAddress() {
/* 160 */     byte[] mac = null;
/*     */     try {
/* 162 */       InetAddress localHost = InetAddress.getLocalHost();
/*     */       try {
/* 164 */         NetworkInterface localInterface = NetworkInterface.getByInetAddress(localHost);
/* 165 */         if (isUpAndNotLoopback(localInterface)) {
/* 166 */           mac = localInterface.getHardwareAddress();
/*     */         }
/* 168 */         if (mac == null) {
/* 169 */           Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
/* 170 */           while (networkInterfaces.hasMoreElements() && mac == null) {
/* 171 */             NetworkInterface nic = networkInterfaces.nextElement();
/* 172 */             if (isUpAndNotLoopback(nic)) {
/* 173 */               mac = nic.getHardwareAddress();
/*     */             }
/*     */           } 
/*     */         } 
/* 177 */       } catch (SocketException e) {
/* 178 */         LOGGER.catching(e);
/*     */       } 
/* 180 */       if (mac == null || mac.length == 0) {
/* 181 */         mac = localHost.getAddress();
/*     */       }
/* 183 */     } catch (UnknownHostException unknownHostException) {}
/*     */ 
/*     */     
/* 186 */     return mac;
/*     */   }
/*     */   
/*     */   private static boolean isUpAndNotLoopback(NetworkInterface ni) throws SocketException {
/* 190 */     return (ni != null && !ni.isLoopback() && ni.isUp());
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\cor\\util\UuidUtil.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */