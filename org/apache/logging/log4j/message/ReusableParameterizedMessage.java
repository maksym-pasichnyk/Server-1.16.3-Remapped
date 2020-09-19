/*     */ package org.apache.logging.log4j.message;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import org.apache.logging.log4j.util.PerformanceSensitive;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @PerformanceSensitive({"allocation"})
/*     */ public class ReusableParameterizedMessage
/*     */   implements ReusableMessage
/*     */ {
/*     */   private static final int MIN_BUILDER_SIZE = 512;
/*     */   private static final int MAX_PARMS = 10;
/*     */   private static final long serialVersionUID = 7800075879295123856L;
/*     */   private transient ThreadLocal<StringBuilder> buffer;
/*     */   private String messagePattern;
/*     */   private int argCount;
/*     */   private int usedCount;
/*  41 */   private final int[] indices = new int[256];
/*     */   private transient Object[] varargs;
/*  43 */   private transient Object[] params = new Object[10];
/*     */ 
/*     */   
/*     */   private transient Throwable throwable;
/*     */ 
/*     */   
/*     */   transient boolean reserved = false;
/*     */ 
/*     */ 
/*     */   
/*     */   private Object[] getTrimmedParams() {
/*  54 */     return (this.varargs == null) ? Arrays.<Object>copyOf(this.params, this.argCount) : this.varargs;
/*     */   }
/*     */   
/*     */   private Object[] getParams() {
/*  58 */     return (this.varargs == null) ? this.params : this.varargs;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Object[] swapParameters(Object[] emptyReplacement) {
/*     */     Object[] result;
/*  65 */     if (this.varargs == null) {
/*  66 */       result = this.params;
/*  67 */       if (emptyReplacement.length >= 10) {
/*  68 */         this.params = emptyReplacement;
/*     */       
/*     */       }
/*  71 */       else if (this.argCount <= emptyReplacement.length) {
/*     */         
/*  73 */         System.arraycopy(this.params, 0, emptyReplacement, 0, this.argCount);
/*  74 */         result = emptyReplacement;
/*     */       } else {
/*     */         
/*  77 */         this.params = new Object[10];
/*     */ 
/*     */       
/*     */       }
/*     */ 
/*     */ 
/*     */     
/*     */     }
/*     */     else {
/*     */ 
/*     */       
/*  88 */       if (this.argCount <= emptyReplacement.length) {
/*  89 */         result = emptyReplacement;
/*     */       } else {
/*  91 */         result = new Object[this.argCount];
/*     */       } 
/*     */       
/*  94 */       System.arraycopy(this.varargs, 0, result, 0, this.argCount);
/*     */     } 
/*  96 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public short getParameterCount() {
/* 102 */     return (short)this.argCount;
/*     */   }
/*     */ 
/*     */   
/*     */   public Message memento() {
/* 107 */     return new ParameterizedMessage(this.messagePattern, getTrimmedParams());
/*     */   }
/*     */   
/*     */   private void init(String messagePattern, int argCount, Object[] paramArray) {
/* 111 */     this.varargs = null;
/* 112 */     this.messagePattern = messagePattern;
/* 113 */     this.argCount = argCount;
/* 114 */     int placeholderCount = count(messagePattern, this.indices);
/* 115 */     initThrowable(paramArray, argCount, placeholderCount);
/* 116 */     this.usedCount = Math.min(placeholderCount, argCount);
/*     */   }
/*     */ 
/*     */   
/*     */   private static int count(String messagePattern, int[] indices) {
/*     */     try {
/* 122 */       return ParameterFormatter.countArgumentPlaceholders2(messagePattern, indices);
/* 123 */     } catch (Exception ex) {
/* 124 */       return ParameterFormatter.countArgumentPlaceholders(messagePattern);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void initThrowable(Object[] params, int argCount, int usedParams) {
/* 129 */     if (usedParams < argCount && params[argCount - 1] instanceof Throwable) {
/* 130 */       this.throwable = (Throwable)params[argCount - 1];
/*     */     } else {
/* 132 */       this.throwable = null;
/*     */     } 
/*     */   }
/*     */   
/*     */   ReusableParameterizedMessage set(String messagePattern, Object... arguments) {
/* 137 */     init(messagePattern, (arguments == null) ? 0 : arguments.length, arguments);
/* 138 */     this.varargs = arguments;
/* 139 */     return this;
/*     */   }
/*     */   
/*     */   ReusableParameterizedMessage set(String messagePattern, Object p0) {
/* 143 */     this.params[0] = p0;
/* 144 */     init(messagePattern, 1, this.params);
/* 145 */     return this;
/*     */   }
/*     */   
/*     */   ReusableParameterizedMessage set(String messagePattern, Object p0, Object p1) {
/* 149 */     this.params[0] = p0;
/* 150 */     this.params[1] = p1;
/* 151 */     init(messagePattern, 2, this.params);
/* 152 */     return this;
/*     */   }
/*     */   
/*     */   ReusableParameterizedMessage set(String messagePattern, Object p0, Object p1, Object p2) {
/* 156 */     this.params[0] = p0;
/* 157 */     this.params[1] = p1;
/* 158 */     this.params[2] = p2;
/* 159 */     init(messagePattern, 3, this.params);
/* 160 */     return this;
/*     */   }
/*     */   
/*     */   ReusableParameterizedMessage set(String messagePattern, Object p0, Object p1, Object p2, Object p3) {
/* 164 */     this.params[0] = p0;
/* 165 */     this.params[1] = p1;
/* 166 */     this.params[2] = p2;
/* 167 */     this.params[3] = p3;
/* 168 */     init(messagePattern, 4, this.params);
/* 169 */     return this;
/*     */   }
/*     */   
/*     */   ReusableParameterizedMessage set(String messagePattern, Object p0, Object p1, Object p2, Object p3, Object p4) {
/* 173 */     this.params[0] = p0;
/* 174 */     this.params[1] = p1;
/* 175 */     this.params[2] = p2;
/* 176 */     this.params[3] = p3;
/* 177 */     this.params[4] = p4;
/* 178 */     init(messagePattern, 5, this.params);
/* 179 */     return this;
/*     */   }
/*     */   
/*     */   ReusableParameterizedMessage set(String messagePattern, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
/* 183 */     this.params[0] = p0;
/* 184 */     this.params[1] = p1;
/* 185 */     this.params[2] = p2;
/* 186 */     this.params[3] = p3;
/* 187 */     this.params[4] = p4;
/* 188 */     this.params[5] = p5;
/* 189 */     init(messagePattern, 6, this.params);
/* 190 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   ReusableParameterizedMessage set(String messagePattern, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) {
/* 195 */     this.params[0] = p0;
/* 196 */     this.params[1] = p1;
/* 197 */     this.params[2] = p2;
/* 198 */     this.params[3] = p3;
/* 199 */     this.params[4] = p4;
/* 200 */     this.params[5] = p5;
/* 201 */     this.params[6] = p6;
/* 202 */     init(messagePattern, 7, this.params);
/* 203 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   ReusableParameterizedMessage set(String messagePattern, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7) {
/* 208 */     this.params[0] = p0;
/* 209 */     this.params[1] = p1;
/* 210 */     this.params[2] = p2;
/* 211 */     this.params[3] = p3;
/* 212 */     this.params[4] = p4;
/* 213 */     this.params[5] = p5;
/* 214 */     this.params[6] = p6;
/* 215 */     this.params[7] = p7;
/* 216 */     init(messagePattern, 8, this.params);
/* 217 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   ReusableParameterizedMessage set(String messagePattern, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8) {
/* 222 */     this.params[0] = p0;
/* 223 */     this.params[1] = p1;
/* 224 */     this.params[2] = p2;
/* 225 */     this.params[3] = p3;
/* 226 */     this.params[4] = p4;
/* 227 */     this.params[5] = p5;
/* 228 */     this.params[6] = p6;
/* 229 */     this.params[7] = p7;
/* 230 */     this.params[8] = p8;
/* 231 */     init(messagePattern, 9, this.params);
/* 232 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   ReusableParameterizedMessage set(String messagePattern, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8, Object p9) {
/* 237 */     this.params[0] = p0;
/* 238 */     this.params[1] = p1;
/* 239 */     this.params[2] = p2;
/* 240 */     this.params[3] = p3;
/* 241 */     this.params[4] = p4;
/* 242 */     this.params[5] = p5;
/* 243 */     this.params[6] = p6;
/* 244 */     this.params[7] = p7;
/* 245 */     this.params[8] = p8;
/* 246 */     this.params[9] = p9;
/* 247 */     init(messagePattern, 10, this.params);
/* 248 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFormat() {
/* 257 */     return this.messagePattern;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object[] getParameters() {
/* 266 */     return getTrimmedParams();
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
/* 280 */     return this.throwable;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFormattedMessage() {
/* 289 */     StringBuilder sb = getBuffer();
/* 290 */     formatTo(sb);
/* 291 */     return sb.toString();
/*     */   }
/*     */   
/*     */   private StringBuilder getBuffer() {
/* 295 */     if (this.buffer == null) {
/* 296 */       this.buffer = new ThreadLocal<>();
/*     */     }
/* 298 */     StringBuilder result = this.buffer.get();
/* 299 */     if (result == null) {
/* 300 */       int currentPatternLength = (this.messagePattern == null) ? 0 : this.messagePattern.length();
/* 301 */       result = new StringBuilder(Math.min(512, currentPatternLength * 2));
/* 302 */       this.buffer.set(result);
/*     */     } 
/* 304 */     result.setLength(0);
/* 305 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public void formatTo(StringBuilder builder) {
/* 310 */     if (this.indices[0] < 0) {
/* 311 */       ParameterFormatter.formatMessage(builder, this.messagePattern, getParams(), this.argCount);
/*     */     } else {
/* 313 */       ParameterFormatter.formatMessage2(builder, this.messagePattern, getParams(), this.usedCount, this.indices);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   ReusableParameterizedMessage reserve() {
/* 323 */     this.reserved = true;
/* 324 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 329 */     return "ReusableParameterizedMessage[messagePattern=" + getFormat() + ", stringArgs=" + Arrays.toString(getParameters()) + ", throwable=" + getThrowable() + ']';
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\message\ReusableParameterizedMessage.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */