/*     */ package org.apache.logging.log4j.message;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.util.Arrays;
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
/*     */ public final class ObjectArrayMessage
/*     */   implements Message
/*     */ {
/*  37 */   private static final Object[] EMPTY_OBJECT_ARRAY = new Object[0];
/*     */ 
/*     */   
/*     */   private static final long serialVersionUID = -5903272448334166185L;
/*     */ 
/*     */   
/*     */   private transient Object[] array;
/*     */ 
/*     */   
/*     */   private transient String arrayString;
/*     */ 
/*     */ 
/*     */   
/*     */   public ObjectArrayMessage(Object... obj) {
/*  51 */     this.array = (obj == null) ? EMPTY_OBJECT_ARRAY : obj;
/*     */   }
/*     */   
/*     */   private boolean equalObjectsOrStrings(Object[] left, Object[] right) {
/*  55 */     return (Arrays.equals(left, right) || Arrays.toString(left).equals(Arrays.toString(right)));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/*  60 */     if (this == o) {
/*  61 */       return true;
/*     */     }
/*  63 */     if (o == null || getClass() != o.getClass()) {
/*  64 */       return false;
/*     */     }
/*     */     
/*  67 */     ObjectArrayMessage that = (ObjectArrayMessage)o;
/*  68 */     return (this.array == null) ? ((that.array == null)) : equalObjectsOrStrings(this.array, that.array);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFormat() {
/*  78 */     return getFormattedMessage();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFormattedMessage() {
/*  89 */     if (this.arrayString == null) {
/*  90 */       this.arrayString = Arrays.toString(this.array);
/*     */     }
/*  92 */     return this.arrayString;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object[] getParameters() {
/* 102 */     return this.array;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Throwable getThrowable() {
/* 112 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 117 */     return Arrays.hashCode(this.array);
/*     */   }
/*     */   
/*     */   private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
/* 121 */     in.defaultReadObject();
/* 122 */     this.array = (Object[])in.readObject();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 127 */     return getFormattedMessage();
/*     */   }
/*     */   
/*     */   private void writeObject(ObjectOutputStream out) throws IOException {
/* 131 */     out.defaultWriteObject();
/* 132 */     out.writeObject(this.array);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\message\ObjectArrayMessage.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */