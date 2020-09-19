/*     */ package io.netty.handler.codec.dns;
/*     */ 
/*     */ import io.netty.util.collection.IntObjectHashMap;
/*     */ import java.util.HashMap;
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
/*     */ public class DnsRecordType
/*     */   implements Comparable<DnsRecordType>
/*     */ {
/*  35 */   public static final DnsRecordType A = new DnsRecordType(1, "A");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  41 */   public static final DnsRecordType NS = new DnsRecordType(2, "NS");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  47 */   public static final DnsRecordType CNAME = new DnsRecordType(5, "CNAME");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  55 */   public static final DnsRecordType SOA = new DnsRecordType(6, "SOA");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  63 */   public static final DnsRecordType PTR = new DnsRecordType(12, "PTR");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  69 */   public static final DnsRecordType MX = new DnsRecordType(15, "MX");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  78 */   public static final DnsRecordType TXT = new DnsRecordType(16, "TXT");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  85 */   public static final DnsRecordType RP = new DnsRecordType(17, "RP");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  93 */   public static final DnsRecordType AFSDB = new DnsRecordType(18, "AFSDB");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 100 */   public static final DnsRecordType SIG = new DnsRecordType(24, "SIG");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 109 */   public static final DnsRecordType KEY = new DnsRecordType(25, "KEY");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 115 */   public static final DnsRecordType AAAA = new DnsRecordType(28, "AAAA");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 121 */   public static final DnsRecordType LOC = new DnsRecordType(29, "LOC");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 127 */   public static final DnsRecordType SRV = new DnsRecordType(33, "SRV");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 134 */   public static final DnsRecordType NAPTR = new DnsRecordType(35, "NAPTR");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 143 */   public static final DnsRecordType KX = new DnsRecordType(36, "KX");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 148 */   public static final DnsRecordType CERT = new DnsRecordType(37, "CERT");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 156 */   public static final DnsRecordType DNAME = new DnsRecordType(39, "DNAME");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 162 */   public static final DnsRecordType OPT = new DnsRecordType(41, "OPT");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 168 */   public static final DnsRecordType APL = new DnsRecordType(42, "APL");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 174 */   public static final DnsRecordType DS = new DnsRecordType(43, "DS");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 182 */   public static final DnsRecordType SSHFP = new DnsRecordType(44, "SSHFP");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 187 */   public static final DnsRecordType IPSECKEY = new DnsRecordType(45, "IPSECKEY");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 193 */   public static final DnsRecordType RRSIG = new DnsRecordType(46, "RRSIG");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 199 */   public static final DnsRecordType NSEC = new DnsRecordType(47, "NSEC");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 205 */   public static final DnsRecordType DNSKEY = new DnsRecordType(48, "DNSKEY");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 211 */   public static final DnsRecordType DHCID = new DnsRecordType(49, "DHCID");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 217 */   public static final DnsRecordType NSEC3 = new DnsRecordType(50, "NSEC3");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 222 */   public static final DnsRecordType NSEC3PARAM = new DnsRecordType(51, "NSEC3PARAM");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 231 */   public static final DnsRecordType TLSA = new DnsRecordType(52, "TLSA");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 237 */   public static final DnsRecordType HIP = new DnsRecordType(55, "HIP");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 244 */   public static final DnsRecordType SPF = new DnsRecordType(99, "SPF");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 251 */   public static final DnsRecordType TKEY = new DnsRecordType(249, "TKEY");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 258 */   public static final DnsRecordType TSIG = new DnsRecordType(250, "TSIG");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 267 */   public static final DnsRecordType IXFR = new DnsRecordType(251, "IXFR");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 273 */   public static final DnsRecordType AXFR = new DnsRecordType(252, "AXFR");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 284 */   public static final DnsRecordType ANY = new DnsRecordType(255, "ANY");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 290 */   public static final DnsRecordType CAA = new DnsRecordType(257, "CAA");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 297 */   public static final DnsRecordType TA = new DnsRecordType(32768, "TA");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 304 */   public static final DnsRecordType DLV = new DnsRecordType(32769, "DLV");
/*     */   
/* 306 */   private static final Map<String, DnsRecordType> BY_NAME = new HashMap<String, DnsRecordType>();
/* 307 */   private static final IntObjectHashMap<DnsRecordType> BY_TYPE = new IntObjectHashMap(); private static final String EXPECTED;
/*     */   private final int intValue;
/*     */   
/*     */   static {
/* 311 */     DnsRecordType[] all = { A, NS, CNAME, SOA, PTR, MX, TXT, RP, AFSDB, SIG, KEY, AAAA, LOC, SRV, NAPTR, KX, CERT, DNAME, OPT, APL, DS, SSHFP, IPSECKEY, RRSIG, NSEC, DNSKEY, DHCID, NSEC3, NSEC3PARAM, TLSA, HIP, SPF, TKEY, TSIG, IXFR, AXFR, ANY, CAA, TA, DLV };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 317 */     StringBuilder expected = new StringBuilder(512);
/*     */     
/* 319 */     expected.append(" (expected: ");
/* 320 */     for (DnsRecordType type : all) {
/* 321 */       BY_NAME.put(type.name(), type);
/* 322 */       BY_TYPE.put(type.intValue(), type);
/*     */       
/* 324 */       expected.append(type.name())
/* 325 */         .append('(')
/* 326 */         .append(type.intValue())
/* 327 */         .append("), ");
/*     */     } 
/*     */     
/* 330 */     expected.setLength(expected.length() - 2);
/* 331 */     expected.append(')');
/* 332 */     EXPECTED = expected.toString();
/*     */   }
/*     */   private final String name; private String text;
/*     */   public static DnsRecordType valueOf(int intValue) {
/* 336 */     DnsRecordType result = (DnsRecordType)BY_TYPE.get(intValue);
/* 337 */     if (result == null) {
/* 338 */       return new DnsRecordType(intValue);
/*     */     }
/* 340 */     return result;
/*     */   }
/*     */   
/*     */   public static DnsRecordType valueOf(String name) {
/* 344 */     DnsRecordType result = BY_NAME.get(name);
/* 345 */     if (result == null) {
/* 346 */       throw new IllegalArgumentException("name: " + name + EXPECTED);
/*     */     }
/* 348 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private DnsRecordType(int intValue) {
/* 356 */     this(intValue, "UNKNOWN");
/*     */   }
/*     */   
/*     */   public DnsRecordType(int intValue, String name) {
/* 360 */     if ((intValue & 0xFFFF) != intValue) {
/* 361 */       throw new IllegalArgumentException("intValue: " + intValue + " (expected: 0 ~ 65535)");
/*     */     }
/* 363 */     this.intValue = intValue;
/* 364 */     this.name = name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String name() {
/* 371 */     return this.name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int intValue() {
/* 378 */     return this.intValue;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 383 */     return this.intValue;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 388 */     return (o instanceof DnsRecordType && ((DnsRecordType)o).intValue == this.intValue);
/*     */   }
/*     */ 
/*     */   
/*     */   public int compareTo(DnsRecordType o) {
/* 393 */     return intValue() - o.intValue();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 398 */     String text = this.text;
/* 399 */     if (text == null) {
/* 400 */       this.text = text = this.name + '(' + intValue() + ')';
/*     */     }
/* 402 */     return text;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\dns\DnsRecordType.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */