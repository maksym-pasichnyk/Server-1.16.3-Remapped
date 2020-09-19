/*     */ package io.netty.handler.codec.http2;
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
/*     */ public final class Http2Flags
/*     */ {
/*     */   public static final short END_STREAM = 1;
/*     */   public static final short END_HEADERS = 4;
/*     */   public static final short ACK = 1;
/*     */   public static final short PADDED = 8;
/*     */   public static final short PRIORITY = 32;
/*     */   private short value;
/*     */   
/*     */   public Http2Flags() {}
/*     */   
/*     */   public Http2Flags(short value) {
/*  37 */     this.value = value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public short value() {
/*  44 */     return this.value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean endOfStream() {
/*  52 */     return isFlagSet((short)1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean endOfHeaders() {
/*  60 */     return isFlagSet((short)4);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean priorityPresent() {
/*  68 */     return isFlagSet((short)32);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean ack() {
/*  76 */     return isFlagSet((short)1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean paddingPresent() {
/*  84 */     return isFlagSet((short)8);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getNumPriorityBytes() {
/*  92 */     return priorityPresent() ? 5 : 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getPaddingPresenceFieldLength() {
/* 100 */     return paddingPresent() ? 1 : 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Http2Flags endOfStream(boolean endOfStream) {
/* 107 */     return setFlag(endOfStream, (short)1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Http2Flags endOfHeaders(boolean endOfHeaders) {
/* 114 */     return setFlag(endOfHeaders, (short)4);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Http2Flags priorityPresent(boolean priorityPresent) {
/* 121 */     return setFlag(priorityPresent, (short)32);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Http2Flags paddingPresent(boolean paddingPresent) {
/* 128 */     return setFlag(paddingPresent, (short)8);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Http2Flags ack(boolean ack) {
/* 135 */     return setFlag(ack, (short)1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Http2Flags setFlag(boolean on, short mask) {
/* 145 */     if (on) {
/* 146 */       this.value = (short)(this.value | mask);
/*     */     } else {
/* 148 */       this.value = (short)(this.value & (mask ^ 0xFFFFFFFF));
/*     */     } 
/* 150 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isFlagSet(short mask) {
/* 159 */     return ((this.value & mask) != 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 164 */     int prime = 31;
/* 165 */     int result = 1;
/* 166 */     result = 31 * result + this.value;
/* 167 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 172 */     if (this == obj) {
/* 173 */       return true;
/*     */     }
/* 175 */     if (obj == null) {
/* 176 */       return false;
/*     */     }
/* 178 */     if (getClass() != obj.getClass()) {
/* 179 */       return false;
/*     */     }
/*     */     
/* 182 */     return (this.value == ((Http2Flags)obj).value);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 187 */     StringBuilder builder = new StringBuilder();
/* 188 */     builder.append("value = ").append(this.value).append(" (");
/* 189 */     if (ack()) {
/* 190 */       builder.append("ACK,");
/*     */     }
/* 192 */     if (endOfHeaders()) {
/* 193 */       builder.append("END_OF_HEADERS,");
/*     */     }
/* 195 */     if (endOfStream()) {
/* 196 */       builder.append("END_OF_STREAM,");
/*     */     }
/* 198 */     if (priorityPresent()) {
/* 199 */       builder.append("PRIORITY_PRESENT,");
/*     */     }
/* 201 */     if (paddingPresent()) {
/* 202 */       builder.append("PADDING_PRESENT,");
/*     */     }
/* 204 */     builder.append(')');
/* 205 */     return builder.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\http2\Http2Flags.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */