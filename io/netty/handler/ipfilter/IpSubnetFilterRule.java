/*     */ package io.netty.handler.ipfilter;
/*     */ 
/*     */ import io.netty.util.internal.SocketUtils;
/*     */ import java.math.BigInteger;
/*     */ import java.net.Inet4Address;
/*     */ import java.net.Inet6Address;
/*     */ import java.net.InetAddress;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.UnknownHostException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class IpSubnetFilterRule
/*     */   implements IpFilterRule
/*     */ {
/*     */   private final IpFilterRule filterRule;
/*     */   
/*     */   public IpSubnetFilterRule(String ipAddress, int cidrPrefix, IpFilterRuleType ruleType) {
/*     */     try {
/*  37 */       this.filterRule = selectFilterRule(SocketUtils.addressByName(ipAddress), cidrPrefix, ruleType);
/*  38 */     } catch (UnknownHostException e) {
/*  39 */       throw new IllegalArgumentException("ipAddress", e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public IpSubnetFilterRule(InetAddress ipAddress, int cidrPrefix, IpFilterRuleType ruleType) {
/*  44 */     this.filterRule = selectFilterRule(ipAddress, cidrPrefix, ruleType);
/*     */   }
/*     */   
/*     */   private static IpFilterRule selectFilterRule(InetAddress ipAddress, int cidrPrefix, IpFilterRuleType ruleType) {
/*  48 */     if (ipAddress == null) {
/*  49 */       throw new NullPointerException("ipAddress");
/*     */     }
/*     */     
/*  52 */     if (ruleType == null) {
/*  53 */       throw new NullPointerException("ruleType");
/*     */     }
/*     */     
/*  56 */     if (ipAddress instanceof Inet4Address)
/*  57 */       return new Ip4SubnetFilterRule((Inet4Address)ipAddress, cidrPrefix, ruleType); 
/*  58 */     if (ipAddress instanceof Inet6Address) {
/*  59 */       return new Ip6SubnetFilterRule((Inet6Address)ipAddress, cidrPrefix, ruleType);
/*     */     }
/*  61 */     throw new IllegalArgumentException("Only IPv4 and IPv6 addresses are supported");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean matches(InetSocketAddress remoteAddress) {
/*  67 */     return this.filterRule.matches(remoteAddress);
/*     */   }
/*     */ 
/*     */   
/*     */   public IpFilterRuleType ruleType() {
/*  72 */     return this.filterRule.ruleType();
/*     */   }
/*     */   
/*     */   private static final class Ip4SubnetFilterRule
/*     */     implements IpFilterRule {
/*     */     private final int networkAddress;
/*     */     private final int subnetMask;
/*     */     private final IpFilterRuleType ruleType;
/*     */     
/*     */     private Ip4SubnetFilterRule(Inet4Address ipAddress, int cidrPrefix, IpFilterRuleType ruleType) {
/*  82 */       if (cidrPrefix < 0 || cidrPrefix > 32) {
/*  83 */         throw new IllegalArgumentException(String.format("IPv4 requires the subnet prefix to be in range of [0,32]. The prefix was: %d", new Object[] {
/*  84 */                 Integer.valueOf(cidrPrefix)
/*     */               }));
/*     */       }
/*  87 */       this.subnetMask = prefixToSubnetMask(cidrPrefix);
/*  88 */       this.networkAddress = ipToInt(ipAddress) & this.subnetMask;
/*  89 */       this.ruleType = ruleType;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean matches(InetSocketAddress remoteAddress) {
/*  94 */       InetAddress inetAddress = remoteAddress.getAddress();
/*  95 */       if (inetAddress instanceof Inet4Address) {
/*  96 */         int ipAddress = ipToInt((Inet4Address)inetAddress);
/*  97 */         return ((ipAddress & this.subnetMask) == this.networkAddress);
/*     */       } 
/*  99 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public IpFilterRuleType ruleType() {
/* 104 */       return this.ruleType;
/*     */     }
/*     */     
/*     */     private static int ipToInt(Inet4Address ipAddress) {
/* 108 */       byte[] octets = ipAddress.getAddress();
/* 109 */       assert octets.length == 4;
/*     */       
/* 111 */       return (octets[0] & 0xFF) << 24 | (octets[1] & 0xFF) << 16 | (octets[2] & 0xFF) << 8 | octets[3] & 0xFF;
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
/*     */     private static int prefixToSubnetMask(int cidrPrefix) {
/* 128 */       return (int)(-1L << 32 - cidrPrefix & 0xFFFFFFFFFFFFFFFFL);
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class Ip6SubnetFilterRule
/*     */     implements IpFilterRule {
/* 134 */     private static final BigInteger MINUS_ONE = BigInteger.valueOf(-1L);
/*     */     
/*     */     private final BigInteger networkAddress;
/*     */     private final BigInteger subnetMask;
/*     */     private final IpFilterRuleType ruleType;
/*     */     
/*     */     private Ip6SubnetFilterRule(Inet6Address ipAddress, int cidrPrefix, IpFilterRuleType ruleType) {
/* 141 */       if (cidrPrefix < 0 || cidrPrefix > 128) {
/* 142 */         throw new IllegalArgumentException(String.format("IPv6 requires the subnet prefix to be in range of [0,128]. The prefix was: %d", new Object[] {
/* 143 */                 Integer.valueOf(cidrPrefix)
/*     */               }));
/*     */       }
/* 146 */       this.subnetMask = prefixToSubnetMask(cidrPrefix);
/* 147 */       this.networkAddress = ipToInt(ipAddress).and(this.subnetMask);
/* 148 */       this.ruleType = ruleType;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean matches(InetSocketAddress remoteAddress) {
/* 153 */       InetAddress inetAddress = remoteAddress.getAddress();
/* 154 */       if (inetAddress instanceof Inet6Address) {
/* 155 */         BigInteger ipAddress = ipToInt((Inet6Address)inetAddress);
/* 156 */         return ipAddress.and(this.subnetMask).equals(this.networkAddress);
/*     */       } 
/* 158 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public IpFilterRuleType ruleType() {
/* 163 */       return this.ruleType;
/*     */     }
/*     */     
/*     */     private static BigInteger ipToInt(Inet6Address ipAddress) {
/* 167 */       byte[] octets = ipAddress.getAddress();
/* 168 */       assert octets.length == 16;
/*     */       
/* 170 */       return new BigInteger(octets);
/*     */     }
/*     */     
/*     */     private static BigInteger prefixToSubnetMask(int cidrPrefix) {
/* 174 */       return MINUS_ONE.shiftLeft(128 - cidrPrefix);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\ipfilter\IpSubnetFilterRule.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */