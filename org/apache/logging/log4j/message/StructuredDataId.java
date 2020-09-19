/*     */ package org.apache.logging.log4j.message;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import org.apache.logging.log4j.util.StringBuilderFormattable;
/*     */ import org.apache.logging.log4j.util.Strings;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class StructuredDataId
/*     */   implements Serializable, StringBuilderFormattable
/*     */ {
/*  32 */   public static final StructuredDataId TIME_QUALITY = new StructuredDataId("timeQuality", null, new String[] { "tzKnown", "isSynced", "syncAccuracy" });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  38 */   public static final StructuredDataId ORIGIN = new StructuredDataId("origin", null, new String[] { "ip", "enterpriseId", "software", "swVersion" });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  44 */   public static final StructuredDataId META = new StructuredDataId("meta", null, new String[] { "sequenceId", "sysUpTime", "language" });
/*     */   
/*     */   public static final int RESERVED = -1;
/*     */   
/*     */   private static final long serialVersionUID = 9031746276396249990L;
/*     */   
/*     */   private static final int MAX_LENGTH = 32;
/*     */   
/*     */   private static final String AT_SIGN = "@";
/*     */   
/*     */   private final String name;
/*     */   
/*     */   private final int enterpriseNumber;
/*     */   
/*     */   private final String[] required;
/*     */   private final String[] optional;
/*     */   
/*     */   protected StructuredDataId(String name, String[] required, String[] optional) {
/*  62 */     int index = -1;
/*  63 */     if (name != null) {
/*  64 */       if (name.length() > 32) {
/*  65 */         throw new IllegalArgumentException(String.format("Length of id %s exceeds maximum of %d characters", new Object[] { name, Integer.valueOf(32) }));
/*     */       }
/*     */       
/*  68 */       index = name.indexOf("@");
/*     */     } 
/*     */     
/*  71 */     if (index > 0) {
/*  72 */       this.name = name.substring(0, index);
/*  73 */       this.enterpriseNumber = Integer.parseInt(name.substring(index + 1));
/*     */     } else {
/*  75 */       this.name = name;
/*  76 */       this.enterpriseNumber = -1;
/*     */     } 
/*  78 */     this.required = required;
/*  79 */     this.optional = optional;
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
/*     */   public StructuredDataId(String name, int enterpriseNumber, String[] required, String[] optional) {
/*  92 */     if (name == null) {
/*  93 */       throw new IllegalArgumentException("No structured id name was supplied");
/*     */     }
/*  95 */     if (name.contains("@")) {
/*  96 */       throw new IllegalArgumentException("Structured id name cannot contain an " + Strings.quote("@"));
/*     */     }
/*  98 */     if (enterpriseNumber <= 0) {
/*  99 */       throw new IllegalArgumentException("No enterprise number was supplied");
/*     */     }
/* 101 */     this.name = name;
/* 102 */     this.enterpriseNumber = enterpriseNumber;
/* 103 */     String id = name + "@" + enterpriseNumber;
/* 104 */     if (id.length() > 32) {
/* 105 */       throw new IllegalArgumentException("Length of id exceeds maximum of 32 characters: " + id);
/*     */     }
/* 107 */     this.required = required;
/* 108 */     this.optional = optional;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StructuredDataId makeId(StructuredDataId id) {
/* 118 */     if (id == null) {
/* 119 */       return this;
/*     */     }
/* 121 */     return makeId(id.getName(), id.getEnterpriseNumber());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StructuredDataId makeId(String defaultId, int anEnterpriseNumber) {
/*     */     String id;
/*     */     String[] req;
/*     */     String[] opt;
/* 135 */     if (anEnterpriseNumber <= 0) {
/* 136 */       return this;
/*     */     }
/* 138 */     if (this.name != null) {
/* 139 */       id = this.name;
/* 140 */       req = this.required;
/* 141 */       opt = this.optional;
/*     */     } else {
/* 143 */       id = defaultId;
/* 144 */       req = null;
/* 145 */       opt = null;
/*     */     } 
/*     */     
/* 148 */     return new StructuredDataId(id, anEnterpriseNumber, req, opt);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] getRequired() {
/* 157 */     return this.required;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] getOptional() {
/* 166 */     return this.optional;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getName() {
/* 175 */     return this.name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getEnterpriseNumber() {
/* 184 */     return this.enterpriseNumber;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isReserved() {
/* 193 */     return (this.enterpriseNumber <= 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 198 */     StringBuilder sb = new StringBuilder(this.name.length() + 10);
/* 199 */     formatTo(sb);
/* 200 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public void formatTo(StringBuilder buffer) {
/* 205 */     if (isReserved()) {
/* 206 */       buffer.append(this.name);
/*     */     } else {
/* 208 */       buffer.append(this.name).append("@").append(this.enterpriseNumber);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\message\StructuredDataId.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */