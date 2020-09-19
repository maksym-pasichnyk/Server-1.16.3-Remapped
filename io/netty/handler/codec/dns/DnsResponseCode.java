/*     */ package io.netty.handler.codec.dns;
/*     */ 
/*     */ import io.netty.util.internal.ObjectUtil;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DnsResponseCode
/*     */   implements Comparable<DnsResponseCode>
/*     */ {
/*  31 */   public static final DnsResponseCode NOERROR = new DnsResponseCode(0, "NoError");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  36 */   public static final DnsResponseCode FORMERR = new DnsResponseCode(1, "FormErr");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  41 */   public static final DnsResponseCode SERVFAIL = new DnsResponseCode(2, "ServFail");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  46 */   public static final DnsResponseCode NXDOMAIN = new DnsResponseCode(3, "NXDomain");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  51 */   public static final DnsResponseCode NOTIMP = new DnsResponseCode(4, "NotImp");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  56 */   public static final DnsResponseCode REFUSED = new DnsResponseCode(5, "Refused");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  61 */   public static final DnsResponseCode YXDOMAIN = new DnsResponseCode(6, "YXDomain");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  66 */   public static final DnsResponseCode YXRRSET = new DnsResponseCode(7, "YXRRSet");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  71 */   public static final DnsResponseCode NXRRSET = new DnsResponseCode(8, "NXRRSet");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  76 */   public static final DnsResponseCode NOTAUTH = new DnsResponseCode(9, "NotAuth");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  81 */   public static final DnsResponseCode NOTZONE = new DnsResponseCode(10, "NotZone");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  87 */   public static final DnsResponseCode BADVERS_OR_BADSIG = new DnsResponseCode(16, "BADVERS_OR_BADSIG");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  92 */   public static final DnsResponseCode BADKEY = new DnsResponseCode(17, "BADKEY");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  97 */   public static final DnsResponseCode BADTIME = new DnsResponseCode(18, "BADTIME");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 102 */   public static final DnsResponseCode BADMODE = new DnsResponseCode(19, "BADMODE");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 107 */   public static final DnsResponseCode BADNAME = new DnsResponseCode(20, "BADNAME");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 112 */   public static final DnsResponseCode BADALG = new DnsResponseCode(21, "BADALG");
/*     */   
/*     */   private final int code;
/*     */   
/*     */   private final String name;
/*     */   
/*     */   private String text;
/*     */ 
/*     */   
/*     */   public static DnsResponseCode valueOf(int responseCode) {
/* 122 */     switch (responseCode) {
/*     */       case 0:
/* 124 */         return NOERROR;
/*     */       case 1:
/* 126 */         return FORMERR;
/*     */       case 2:
/* 128 */         return SERVFAIL;
/*     */       case 3:
/* 130 */         return NXDOMAIN;
/*     */       case 4:
/* 132 */         return NOTIMP;
/*     */       case 5:
/* 134 */         return REFUSED;
/*     */       case 6:
/* 136 */         return YXDOMAIN;
/*     */       case 7:
/* 138 */         return YXRRSET;
/*     */       case 8:
/* 140 */         return NXRRSET;
/*     */       case 9:
/* 142 */         return NOTAUTH;
/*     */       case 10:
/* 144 */         return NOTZONE;
/*     */       case 16:
/* 146 */         return BADVERS_OR_BADSIG;
/*     */       case 17:
/* 148 */         return BADKEY;
/*     */       case 18:
/* 150 */         return BADTIME;
/*     */       case 19:
/* 152 */         return BADMODE;
/*     */       case 20:
/* 154 */         return BADNAME;
/*     */       case 21:
/* 156 */         return BADALG;
/*     */     } 
/* 158 */     return new DnsResponseCode(responseCode);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private DnsResponseCode(int code) {
/* 167 */     this(code, "UNKNOWN");
/*     */   }
/*     */   
/*     */   public DnsResponseCode(int code, String name) {
/* 171 */     if (code < 0 || code > 65535) {
/* 172 */       throw new IllegalArgumentException("code: " + code + " (expected: 0 ~ 65535)");
/*     */     }
/*     */     
/* 175 */     this.code = code;
/* 176 */     this.name = (String)ObjectUtil.checkNotNull(name, "name");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int intValue() {
/* 183 */     return this.code;
/*     */   }
/*     */ 
/*     */   
/*     */   public int compareTo(DnsResponseCode o) {
/* 188 */     return intValue() - o.intValue();
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 193 */     return intValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 201 */     if (!(o instanceof DnsResponseCode)) {
/* 202 */       return false;
/*     */     }
/*     */     
/* 205 */     return (intValue() == ((DnsResponseCode)o).intValue());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 213 */     String text = this.text;
/* 214 */     if (text == null) {
/* 215 */       this.text = text = this.name + '(' + intValue() + ')';
/*     */     }
/* 217 */     return text;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\dns\DnsResponseCode.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */