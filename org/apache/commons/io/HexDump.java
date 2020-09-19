/*     */ package org.apache.commons.io;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.nio.charset.Charset;
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
/*     */ public class HexDump
/*     */ {
/*     */   public static void dump(byte[] data, long offset, OutputStream stream, int index) throws IOException, ArrayIndexOutOfBoundsException, IllegalArgumentException {
/*  77 */     if (index < 0 || index >= data.length) {
/*  78 */       throw new ArrayIndexOutOfBoundsException("illegal index: " + index + " into array of length " + data.length);
/*     */     }
/*     */ 
/*     */     
/*  82 */     if (stream == null) {
/*  83 */       throw new IllegalArgumentException("cannot write to nullstream");
/*     */     }
/*  85 */     long display_offset = offset + index;
/*  86 */     StringBuilder buffer = new StringBuilder(74);
/*     */     
/*  88 */     for (int j = index; j < data.length; j += 16) {
/*  89 */       int chars_read = data.length - j;
/*     */       
/*  91 */       if (chars_read > 16) {
/*  92 */         chars_read = 16;
/*     */       }
/*  94 */       dump(buffer, display_offset).append(' '); int k;
/*  95 */       for (k = 0; k < 16; k++) {
/*  96 */         if (k < chars_read) {
/*  97 */           dump(buffer, data[k + j]);
/*     */         } else {
/*  99 */           buffer.append("  ");
/*     */         } 
/* 101 */         buffer.append(' ');
/*     */       } 
/* 103 */       for (k = 0; k < chars_read; k++) {
/* 104 */         if (data[k + j] >= 32 && data[k + j] < Byte.MAX_VALUE) {
/* 105 */           buffer.append((char)data[k + j]);
/*     */         } else {
/* 107 */           buffer.append('.');
/*     */         } 
/*     */       } 
/* 110 */       buffer.append(EOL);
/*     */       
/* 112 */       stream.write(buffer.toString().getBytes(Charset.defaultCharset()));
/* 113 */       stream.flush();
/* 114 */       buffer.setLength(0);
/* 115 */       display_offset += chars_read;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 122 */   public static final String EOL = System.getProperty("line.separator");
/*     */   
/* 124 */   private static final char[] _hexcodes = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 129 */   private static final int[] _shifts = new int[] { 28, 24, 20, 16, 12, 8, 4, 0 };
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
/*     */   private static StringBuilder dump(StringBuilder _lbuffer, long value) {
/* 142 */     for (int j = 0; j < 8; j++) {
/* 143 */       _lbuffer.append(_hexcodes[(int)(value >> _shifts[j]) & 0xF]);
/*     */     }
/*     */     
/* 146 */     return _lbuffer;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static StringBuilder dump(StringBuilder _cbuffer, byte value) {
/* 157 */     for (int j = 0; j < 2; j++) {
/* 158 */       _cbuffer.append(_hexcodes[value >> _shifts[j + 6] & 0xF]);
/*     */     }
/* 160 */     return _cbuffer;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\commons\io\HexDump.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */