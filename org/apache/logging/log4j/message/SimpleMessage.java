/*     */ package org.apache.logging.log4j.message;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
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
/*     */ public class SimpleMessage
/*     */   implements Message, StringBuilderFormattable, CharSequence
/*     */ {
/*     */   private static final long serialVersionUID = -8398002534962715992L;
/*     */   private String message;
/*     */   private transient CharSequence charSequence;
/*     */   
/*     */   public SimpleMessage() {
/*  37 */     this((String)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SimpleMessage(String message) {
/*  45 */     this.message = message;
/*  46 */     this.charSequence = message;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SimpleMessage(CharSequence charSequence) {
/*  55 */     this.charSequence = charSequence;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFormattedMessage() {
/*  64 */     if (this.message == null) {
/*  65 */       this.message = String.valueOf(this.charSequence);
/*     */     }
/*  67 */     return this.message;
/*     */   }
/*     */ 
/*     */   
/*     */   public void formatTo(StringBuilder buffer) {
/*  72 */     if (this.message != null) {
/*  73 */       buffer.append(this.message);
/*     */     } else {
/*  75 */       buffer.append(this.charSequence);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFormat() {
/*  85 */     return getFormattedMessage();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object[] getParameters() {
/*  94 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/*  99 */     if (this == o) {
/* 100 */       return true;
/*     */     }
/* 102 */     if (o == null || getClass() != o.getClass()) {
/* 103 */       return false;
/*     */     }
/*     */     
/* 106 */     SimpleMessage that = (SimpleMessage)o;
/*     */     
/* 108 */     if ((this.charSequence != null) ? !this.charSequence.equals(that.charSequence) : (that.charSequence != null)) return false;
/*     */   
/*     */   }
/*     */   
/*     */   public int hashCode() {
/* 113 */     return (this.charSequence != null) ? this.charSequence.hashCode() : 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 118 */     return getFormattedMessage();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Throwable getThrowable() {
/* 128 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int length() {
/* 136 */     return (this.charSequence == null) ? 0 : this.charSequence.length();
/*     */   }
/*     */ 
/*     */   
/*     */   public char charAt(int index) {
/* 141 */     return this.charSequence.charAt(index);
/*     */   }
/*     */ 
/*     */   
/*     */   public CharSequence subSequence(int start, int end) {
/* 146 */     return this.charSequence.subSequence(start, end);
/*     */   }
/*     */ 
/*     */   
/*     */   private void writeObject(ObjectOutputStream out) throws IOException {
/* 151 */     getFormattedMessage();
/* 152 */     out.defaultWriteObject();
/*     */   }
/*     */   
/*     */   private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
/* 156 */     in.defaultReadObject();
/* 157 */     this.charSequence = this.message;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\message\SimpleMessage.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */