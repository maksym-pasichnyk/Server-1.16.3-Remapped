/*     */ package org.apache.logging.log4j.message;
/*     */ 
/*     */ import org.apache.logging.log4j.util.PerformanceSensitive;
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
/*     */ @PerformanceSensitive({"allocation"})
/*     */ public class ReusableObjectMessage
/*     */   implements ReusableMessage
/*     */ {
/*     */   private static final long serialVersionUID = 6922476812535519960L;
/*     */   private transient Object obj;
/*     */   private transient String objectString;
/*     */   
/*     */   public void set(Object object) {
/*  34 */     this.obj = object;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFormattedMessage() {
/*  44 */     return String.valueOf(this.obj);
/*     */   }
/*     */ 
/*     */   
/*     */   public void formatTo(StringBuilder buffer) {
/*  49 */     if (this.obj == null || this.obj instanceof String) {
/*  50 */       buffer.append((String)this.obj);
/*  51 */     } else if (this.obj instanceof StringBuilderFormattable) {
/*  52 */       ((StringBuilderFormattable)this.obj).formatTo(buffer);
/*  53 */     } else if (this.obj instanceof CharSequence) {
/*  54 */       buffer.append((CharSequence)this.obj);
/*  55 */     } else if (this.obj instanceof Integer) {
/*  56 */       buffer.append(((Integer)this.obj).intValue());
/*  57 */     } else if (this.obj instanceof Long) {
/*  58 */       buffer.append(((Long)this.obj).longValue());
/*  59 */     } else if (this.obj instanceof Double) {
/*  60 */       buffer.append(((Double)this.obj).doubleValue());
/*  61 */     } else if (this.obj instanceof Boolean) {
/*  62 */       buffer.append(((Boolean)this.obj).booleanValue());
/*  63 */     } else if (this.obj instanceof Character) {
/*  64 */       buffer.append(((Character)this.obj).charValue());
/*  65 */     } else if (this.obj instanceof Short) {
/*  66 */       buffer.append(((Short)this.obj).shortValue());
/*  67 */     } else if (this.obj instanceof Float) {
/*  68 */       buffer.append(((Float)this.obj).floatValue());
/*     */     } else {
/*  70 */       buffer.append(this.obj);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFormat() {
/*  81 */     return getFormattedMessage();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getParameter() {
/*  91 */     return this.obj;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object[] getParameters() {
/* 101 */     return new Object[] { this.obj };
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 106 */     return getFormattedMessage();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Throwable getThrowable() {
/* 116 */     return (this.obj instanceof Throwable) ? (Throwable)this.obj : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object[] swapParameters(Object[] emptyReplacement) {
/* 126 */     return emptyReplacement;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public short getParameterCount() {
/* 135 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public Message memento() {
/* 140 */     return new ObjectMessage(this.obj);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\message\ReusableObjectMessage.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */