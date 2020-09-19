/*     */ package org.apache.logging.log4j.message;
/*     */ 
/*     */ import java.util.Map;
/*     */ import org.apache.logging.log4j.util.EnglishEnums;
/*     */ import org.apache.logging.log4j.util.StringBuilderFormattable;
/*     */ import org.apache.logging.log4j.util.StringBuilders;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @AsynchronouslyFormattable
/*     */ public class StructuredDataMessage
/*     */   extends MapMessage
/*     */   implements StringBuilderFormattable
/*     */ {
/*     */   private static final long serialVersionUID = 1703221292892071920L;
/*     */   private static final int MAX_LENGTH = 32;
/*     */   private static final int HASHVAL = 31;
/*     */   private StructuredDataId id;
/*     */   private String message;
/*     */   private String type;
/*     */   
/*     */   public enum Format
/*     */   {
/*  55 */     XML,
/*     */     
/*  57 */     FULL;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StructuredDataMessage(String id, String msg, String type) {
/*  67 */     this.id = new StructuredDataId(id, null, null);
/*  68 */     this.message = msg;
/*  69 */     this.type = type;
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
/*     */   public StructuredDataMessage(String id, String msg, String type, Map<String, String> data) {
/*  81 */     super(data);
/*  82 */     this.id = new StructuredDataId(id, null, null);
/*  83 */     this.message = msg;
/*  84 */     this.type = type;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StructuredDataMessage(StructuredDataId id, String msg, String type) {
/*  94 */     this.id = id;
/*  95 */     this.message = msg;
/*  96 */     this.type = type;
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
/*     */   public StructuredDataMessage(StructuredDataId id, String msg, String type, Map<String, String> data) {
/* 109 */     super(data);
/* 110 */     this.id = id;
/* 111 */     this.message = msg;
/* 112 */     this.type = type;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private StructuredDataMessage(StructuredDataMessage msg, Map<String, String> map) {
/* 122 */     super(map);
/* 123 */     this.id = msg.id;
/* 124 */     this.message = msg.message;
/* 125 */     this.type = msg.type;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected StructuredDataMessage() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StructuredDataMessage with(String key, String value) {
/* 144 */     put(key, value);
/* 145 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] getFormats() {
/* 154 */     String[] formats = new String[(Format.values()).length];
/* 155 */     int i = 0;
/* 156 */     for (Format format : Format.values()) {
/* 157 */       formats[i++] = format.name();
/*     */     }
/* 159 */     return formats;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StructuredDataId getId() {
/* 167 */     return this.id;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void setId(String id) {
/* 175 */     this.id = new StructuredDataId(id, null, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void setId(StructuredDataId id) {
/* 183 */     this.id = id;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getType() {
/* 191 */     return this.type;
/*     */   }
/*     */   
/*     */   protected void setType(String type) {
/* 195 */     if (type.length() > 32) {
/* 196 */       throw new IllegalArgumentException("structured data type exceeds maximum length of 32 characters: " + type);
/*     */     }
/* 198 */     this.type = type;
/*     */   }
/*     */ 
/*     */   
/*     */   public void formatTo(StringBuilder buffer) {
/* 203 */     asString(Format.FULL, (StructuredDataId)null, buffer);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFormat() {
/* 212 */     return this.message;
/*     */   }
/*     */   
/*     */   protected void setMessageFormat(String msg) {
/* 216 */     this.message = msg;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void validate(String key, String value) {
/* 222 */     validateKey(key);
/*     */   }
/*     */   
/*     */   private void validateKey(String key) {
/* 226 */     if (key.length() > 32) {
/* 227 */       throw new IllegalArgumentException("Structured data keys are limited to 32 characters. key: " + key);
/*     */     }
/* 229 */     for (int i = 0; i < key.length(); i++) {
/* 230 */       char c = key.charAt(i);
/* 231 */       if (c < '!' || c > '~' || c == '=' || c == ']' || c == '"') {
/* 232 */         throw new IllegalArgumentException("Structured data keys must contain printable US ASCII charactersand may not contain a space, =, ], or \"");
/*     */       }
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
/*     */   public String asString() {
/* 245 */     return asString(Format.FULL, (StructuredDataId)null);
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
/*     */   public String asString(String format) {
/*     */     try {
/* 258 */       return asString((Format)EnglishEnums.valueOf(Format.class, format), (StructuredDataId)null);
/* 259 */     } catch (IllegalArgumentException ex) {
/* 260 */       return asString();
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
/*     */   public final String asString(Format format, StructuredDataId structuredDataId) {
/* 274 */     StringBuilder sb = new StringBuilder();
/* 275 */     asString(format, structuredDataId, sb);
/* 276 */     return sb.toString();
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
/*     */   public final void asString(Format format, StructuredDataId structuredDataId, StringBuilder sb) {
/* 289 */     boolean full = Format.FULL.equals(format);
/* 290 */     if (full) {
/* 291 */       String myType = getType();
/* 292 */       if (myType == null) {
/*     */         return;
/*     */       }
/* 295 */       sb.append(getType()).append(' ');
/*     */     } 
/* 297 */     StructuredDataId sdId = getId();
/* 298 */     if (sdId != null) {
/* 299 */       sdId = sdId.makeId(structuredDataId);
/*     */     } else {
/* 301 */       sdId = structuredDataId;
/*     */     } 
/* 303 */     if (sdId == null || sdId.getName() == null) {
/*     */       return;
/*     */     }
/* 306 */     sb.append('[');
/* 307 */     StringBuilders.appendValue(sb, sdId);
/* 308 */     sb.append(' ');
/* 309 */     appendMap(sb);
/* 310 */     sb.append(']');
/* 311 */     if (full) {
/* 312 */       String msg = getFormat();
/* 313 */       if (msg != null) {
/* 314 */         sb.append(' ').append(msg);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFormattedMessage() {
/* 325 */     return asString(Format.FULL, (StructuredDataId)null);
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
/*     */   public String getFormattedMessage(String[] formats) {
/* 339 */     if (formats != null && formats.length > 0) {
/* 340 */       for (int i = 0; i < formats.length; i++) {
/* 341 */         String format = formats[i];
/* 342 */         if (Format.XML.name().equalsIgnoreCase(format))
/* 343 */           return asXml(); 
/* 344 */         if (Format.FULL.name().equalsIgnoreCase(format)) {
/* 345 */           return asString(Format.FULL, (StructuredDataId)null);
/*     */         }
/*     */       } 
/* 348 */       return asString((Format)null, (StructuredDataId)null);
/*     */     } 
/* 350 */     return asString(Format.FULL, (StructuredDataId)null);
/*     */   }
/*     */   
/*     */   private String asXml() {
/* 354 */     StringBuilder sb = new StringBuilder();
/* 355 */     StructuredDataId sdId = getId();
/* 356 */     if (sdId == null || sdId.getName() == null || this.type == null) {
/* 357 */       return sb.toString();
/*     */     }
/* 359 */     sb.append("<StructuredData>\n");
/* 360 */     sb.append("<type>").append(this.type).append("</type>\n");
/* 361 */     sb.append("<id>").append(sdId).append("</id>\n");
/* 362 */     asXml(sb);
/* 363 */     sb.append("</StructuredData>\n");
/* 364 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 369 */     return asString((Format)null, (StructuredDataId)null);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public MapMessage newInstance(Map<String, String> map) {
/* 375 */     return new StructuredDataMessage(this, map);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 380 */     if (this == o) {
/* 381 */       return true;
/*     */     }
/* 383 */     if (o == null || getClass() != o.getClass()) {
/* 384 */       return false;
/*     */     }
/*     */     
/* 387 */     StructuredDataMessage that = (StructuredDataMessage)o;
/*     */     
/* 389 */     if (!super.equals(o)) {
/* 390 */       return false;
/*     */     }
/* 392 */     if ((this.type != null) ? !this.type.equals(that.type) : (that.type != null)) {
/* 393 */       return false;
/*     */     }
/* 395 */     if ((this.id != null) ? !this.id.equals(that.id) : (that.id != null)) {
/* 396 */       return false;
/*     */     }
/* 398 */     if ((this.message != null) ? !this.message.equals(that.message) : (that.message != null)) {
/* 399 */       return false;
/*     */     }
/*     */     
/* 402 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 407 */     int result = super.hashCode();
/* 408 */     result = 31 * result + ((this.type != null) ? this.type.hashCode() : 0);
/* 409 */     result = 31 * result + ((this.id != null) ? this.id.hashCode() : 0);
/* 410 */     result = 31 * result + ((this.message != null) ? this.message.hashCode() : 0);
/* 411 */     return result;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\message\StructuredDataMessage.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */