/*     */ package org.apache.logging.log4j.message;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import org.apache.logging.log4j.util.StringBuilderFormattable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ParameterizedMessage
/*     */   implements Message, StringBuilderFormattable
/*     */ {
/*     */   private static final int DEFAULT_STRING_BUILDER_SIZE = 255;
/*     */   public static final String RECURSION_PREFIX = "[...";
/*     */   public static final String RECURSION_SUFFIX = "...]";
/*     */   public static final String ERROR_PREFIX = "[!!!";
/*     */   public static final String ERROR_SEPARATOR = "=>";
/*     */   public static final String ERROR_MSG_SEPARATOR = ":";
/*     */   public static final String ERROR_SUFFIX = "!!!]";
/*     */   private static final long serialVersionUID = -665975803997290697L;
/*     */   private static final int HASHVAL = 31;
/*  70 */   private static ThreadLocal<StringBuilder> threadLocalStringBuilder = new ThreadLocal<>();
/*     */ 
/*     */   
/*     */   private String messagePattern;
/*     */ 
/*     */   
/*     */   private transient Object[] argArray;
/*     */ 
/*     */   
/*     */   private String formattedMessage;
/*     */   
/*     */   private transient Throwable throwable;
/*     */   
/*     */   private int[] indices;
/*     */   
/*     */   private int usedCount;
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public ParameterizedMessage(String messagePattern, String[] arguments, Throwable throwable) {
/*  90 */     this.argArray = (Object[])arguments;
/*  91 */     this.throwable = throwable;
/*  92 */     init(messagePattern);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ParameterizedMessage(String messagePattern, Object[] arguments, Throwable throwable) {
/* 103 */     this.argArray = arguments;
/* 104 */     this.throwable = throwable;
/* 105 */     init(messagePattern);
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
/*     */   public ParameterizedMessage(String messagePattern, Object... arguments) {
/* 120 */     this.argArray = arguments;
/* 121 */     init(messagePattern);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ParameterizedMessage(String messagePattern, Object arg) {
/* 130 */     this(messagePattern, new Object[] { arg });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ParameterizedMessage(String messagePattern, Object arg0, Object arg1) {
/* 140 */     this(messagePattern, new Object[] { arg0, arg1 });
/*     */   }
/*     */   
/*     */   private void init(String messagePattern) {
/* 144 */     this.messagePattern = messagePattern;
/* 145 */     int len = Math.max(1, (messagePattern == null) ? 0 : (messagePattern.length() >> 1));
/* 146 */     this.indices = new int[len];
/* 147 */     int placeholders = ParameterFormatter.countArgumentPlaceholders2(messagePattern, this.indices);
/* 148 */     initThrowable(this.argArray, placeholders);
/* 149 */     this.usedCount = Math.min(placeholders, (this.argArray == null) ? 0 : this.argArray.length);
/*     */   }
/*     */   
/*     */   private void initThrowable(Object[] params, int usedParams) {
/* 153 */     if (params != null) {
/* 154 */       int argCount = params.length;
/* 155 */       if (usedParams < argCount && this.throwable == null && params[argCount - 1] instanceof Throwable) {
/* 156 */         this.throwable = (Throwable)params[argCount - 1];
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFormat() {
/* 167 */     return this.messagePattern;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object[] getParameters() {
/* 176 */     return this.argArray;
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
/*     */   public Throwable getThrowable() {
/* 190 */     return this.throwable;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFormattedMessage() {
/* 199 */     if (this.formattedMessage == null) {
/* 200 */       StringBuilder buffer = getThreadLocalStringBuilder();
/* 201 */       formatTo(buffer);
/* 202 */       this.formattedMessage = buffer.toString();
/*     */     } 
/* 204 */     return this.formattedMessage;
/*     */   }
/*     */   
/*     */   private static StringBuilder getThreadLocalStringBuilder() {
/* 208 */     StringBuilder buffer = threadLocalStringBuilder.get();
/* 209 */     if (buffer == null) {
/* 210 */       buffer = new StringBuilder(255);
/* 211 */       threadLocalStringBuilder.set(buffer);
/*     */     } 
/* 213 */     buffer.setLength(0);
/* 214 */     return buffer;
/*     */   }
/*     */ 
/*     */   
/*     */   public void formatTo(StringBuilder buffer) {
/* 219 */     if (this.formattedMessage != null) {
/* 220 */       buffer.append(this.formattedMessage);
/*     */     }
/* 222 */     else if (this.indices[0] < 0) {
/* 223 */       ParameterFormatter.formatMessage(buffer, this.messagePattern, this.argArray, this.usedCount);
/*     */     } else {
/* 225 */       ParameterFormatter.formatMessage2(buffer, this.messagePattern, this.argArray, this.usedCount, this.indices);
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
/*     */   public static String format(String messagePattern, Object[] arguments) {
/* 238 */     return ParameterFormatter.format(messagePattern, arguments);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 243 */     if (this == o) {
/* 244 */       return true;
/*     */     }
/* 246 */     if (o == null || getClass() != o.getClass()) {
/* 247 */       return false;
/*     */     }
/*     */     
/* 250 */     ParameterizedMessage that = (ParameterizedMessage)o;
/*     */     
/* 252 */     if ((this.messagePattern != null) ? !this.messagePattern.equals(that.messagePattern) : (that.messagePattern != null)) {
/* 253 */       return false;
/*     */     }
/* 255 */     if (!Arrays.equals(this.argArray, that.argArray)) {
/* 256 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 260 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 265 */     int result = (this.messagePattern != null) ? this.messagePattern.hashCode() : 0;
/* 266 */     result = 31 * result + ((this.argArray != null) ? Arrays.hashCode(this.argArray) : 0);
/* 267 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int countArgumentPlaceholders(String messagePattern) {
/* 277 */     return ParameterFormatter.countArgumentPlaceholders(messagePattern);
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
/*     */   public static String deepToString(Object o) {
/* 299 */     return ParameterFormatter.deepToString(o);
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
/*     */   public static String identityToString(Object obj) {
/* 323 */     return ParameterFormatter.identityToString(obj);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 328 */     return "ParameterizedMessage[messagePattern=" + this.messagePattern + ", stringArgs=" + Arrays.toString(this.argArray) + ", throwable=" + this.throwable + ']';
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\message\ParameterizedMessage.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */