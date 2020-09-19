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
/*     */ public class DnsOpCode
/*     */   implements Comparable<DnsOpCode>
/*     */ {
/*  31 */   public static final DnsOpCode QUERY = new DnsOpCode(0, "QUERY");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  36 */   public static final DnsOpCode IQUERY = new DnsOpCode(1, "IQUERY");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  41 */   public static final DnsOpCode STATUS = new DnsOpCode(2, "STATUS");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  46 */   public static final DnsOpCode NOTIFY = new DnsOpCode(4, "NOTIFY");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  51 */   public static final DnsOpCode UPDATE = new DnsOpCode(5, "UPDATE");
/*     */   private final byte byteValue;
/*     */   private final String name;
/*     */   private String text;
/*     */   
/*     */   public static DnsOpCode valueOf(int b) {
/*  57 */     switch (b) {
/*     */       case 0:
/*  59 */         return QUERY;
/*     */       case 1:
/*  61 */         return IQUERY;
/*     */       case 2:
/*  63 */         return STATUS;
/*     */       case 4:
/*  65 */         return NOTIFY;
/*     */       case 5:
/*  67 */         return UPDATE;
/*     */     } 
/*     */     
/*  70 */     return new DnsOpCode(b);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private DnsOpCode(int byteValue) {
/*  78 */     this(byteValue, "UNKNOWN");
/*     */   }
/*     */   
/*     */   public DnsOpCode(int byteValue, String name) {
/*  82 */     this.byteValue = (byte)byteValue;
/*  83 */     this.name = (String)ObjectUtil.checkNotNull(name, "name");
/*     */   }
/*     */   
/*     */   public byte byteValue() {
/*  87 */     return this.byteValue;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  92 */     return this.byteValue;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/*  97 */     if (this == obj) {
/*  98 */       return true;
/*     */     }
/*     */     
/* 101 */     if (!(obj instanceof DnsOpCode)) {
/* 102 */       return false;
/*     */     }
/*     */     
/* 105 */     return (this.byteValue == ((DnsOpCode)obj).byteValue);
/*     */   }
/*     */ 
/*     */   
/*     */   public int compareTo(DnsOpCode o) {
/* 110 */     return this.byteValue - o.byteValue;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 115 */     String text = this.text;
/* 116 */     if (text == null) {
/* 117 */       this.text = text = this.name + '(' + (this.byteValue & 0xFF) + ')';
/*     */     }
/* 119 */     return text;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\dns\DnsOpCode.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */