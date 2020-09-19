/*     */ package org.apache.commons.io;
/*     */ 
/*     */ import java.io.EOFException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class EndianUtils
/*     */ {
/*     */   public static short swapShort(short value) {
/*  58 */     return (short)(((value >> 0 & 0xFF) << 8) + ((value >> 8 & 0xFF) << 0));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int swapInteger(int value) {
/*  68 */     return ((value >> 0 & 0xFF) << 24) + ((value >> 8 & 0xFF) << 16) + ((value >> 16 & 0xFF) << 8) + ((value >> 24 & 0xFF) << 0);
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
/*     */   public static long swapLong(long value) {
/*  81 */     return ((value >> 0L & 0xFFL) << 56L) + ((value >> 8L & 0xFFL) << 48L) + ((value >> 16L & 0xFFL) << 40L) + ((value >> 24L & 0xFFL) << 32L) + ((value >> 32L & 0xFFL) << 24L) + ((value >> 40L & 0xFFL) << 16L) + ((value >> 48L & 0xFFL) << 8L) + ((value >> 56L & 0xFFL) << 0L);
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
/*     */   public static float swapFloat(float value) {
/*  98 */     return Float.intBitsToFloat(swapInteger(Float.floatToIntBits(value)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static double swapDouble(double value) {
/* 107 */     return Double.longBitsToDouble(swapLong(Double.doubleToLongBits(value)));
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
/*     */   public static void writeSwappedShort(byte[] data, int offset, short value) {
/* 120 */     data[offset + 0] = (byte)(value >> 0 & 0xFF);
/* 121 */     data[offset + 1] = (byte)(value >> 8 & 0xFF);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static short readSwappedShort(byte[] data, int offset) {
/* 132 */     return (short)(((data[offset + 0] & 0xFF) << 0) + ((data[offset + 1] & 0xFF) << 8));
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
/*     */   public static int readSwappedUnsignedShort(byte[] data, int offset) {
/* 145 */     return ((data[offset + 0] & 0xFF) << 0) + ((data[offset + 1] & 0xFF) << 8);
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
/*     */   public static void writeSwappedInteger(byte[] data, int offset, int value) {
/* 157 */     data[offset + 0] = (byte)(value >> 0 & 0xFF);
/* 158 */     data[offset + 1] = (byte)(value >> 8 & 0xFF);
/* 159 */     data[offset + 2] = (byte)(value >> 16 & 0xFF);
/* 160 */     data[offset + 3] = (byte)(value >> 24 & 0xFF);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int readSwappedInteger(byte[] data, int offset) {
/* 171 */     return ((data[offset + 0] & 0xFF) << 0) + ((data[offset + 1] & 0xFF) << 8) + ((data[offset + 2] & 0xFF) << 16) + ((data[offset + 3] & 0xFF) << 24);
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
/*     */   public static long readSwappedUnsignedInteger(byte[] data, int offset) {
/* 186 */     long low = (((data[offset + 0] & 0xFF) << 0) + ((data[offset + 1] & 0xFF) << 8) + ((data[offset + 2] & 0xFF) << 16));
/*     */ 
/*     */ 
/*     */     
/* 190 */     long high = (data[offset + 3] & 0xFF);
/*     */     
/* 192 */     return (high << 24L) + (0xFFFFFFFFL & low);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void writeSwappedLong(byte[] data, int offset, long value) {
/* 203 */     data[offset + 0] = (byte)(int)(value >> 0L & 0xFFL);
/* 204 */     data[offset + 1] = (byte)(int)(value >> 8L & 0xFFL);
/* 205 */     data[offset + 2] = (byte)(int)(value >> 16L & 0xFFL);
/* 206 */     data[offset + 3] = (byte)(int)(value >> 24L & 0xFFL);
/* 207 */     data[offset + 4] = (byte)(int)(value >> 32L & 0xFFL);
/* 208 */     data[offset + 5] = (byte)(int)(value >> 40L & 0xFFL);
/* 209 */     data[offset + 6] = (byte)(int)(value >> 48L & 0xFFL);
/* 210 */     data[offset + 7] = (byte)(int)(value >> 56L & 0xFFL);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static long readSwappedLong(byte[] data, int offset) {
/* 221 */     long low = readSwappedInteger(data, offset);
/* 222 */     long high = readSwappedInteger(data, offset + 4);
/* 223 */     return (high << 32L) + (0xFFFFFFFFL & low);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void writeSwappedFloat(byte[] data, int offset, float value) {
/* 234 */     writeSwappedInteger(data, offset, Float.floatToIntBits(value));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static float readSwappedFloat(byte[] data, int offset) {
/* 245 */     return Float.intBitsToFloat(readSwappedInteger(data, offset));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void writeSwappedDouble(byte[] data, int offset, double value) {
/* 256 */     writeSwappedLong(data, offset, Double.doubleToLongBits(value));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static double readSwappedDouble(byte[] data, int offset) {
/* 267 */     return Double.longBitsToDouble(readSwappedLong(data, offset));
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
/*     */   public static void writeSwappedShort(OutputStream output, short value) throws IOException {
/* 280 */     output.write((byte)(value >> 0 & 0xFF));
/* 281 */     output.write((byte)(value >> 8 & 0xFF));
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
/*     */   public static short readSwappedShort(InputStream input) throws IOException {
/* 294 */     return (short)(((read(input) & 0xFF) << 0) + ((read(input) & 0xFF) << 8));
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
/*     */   public static int readSwappedUnsignedShort(InputStream input) throws IOException {
/* 308 */     int value1 = read(input);
/* 309 */     int value2 = read(input);
/*     */     
/* 311 */     return ((value1 & 0xFF) << 0) + ((value2 & 0xFF) << 8);
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
/*     */   public static void writeSwappedInteger(OutputStream output, int value) throws IOException {
/* 325 */     output.write((byte)(value >> 0 & 0xFF));
/* 326 */     output.write((byte)(value >> 8 & 0xFF));
/* 327 */     output.write((byte)(value >> 16 & 0xFF));
/* 328 */     output.write((byte)(value >> 24 & 0xFF));
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
/*     */   public static int readSwappedInteger(InputStream input) throws IOException {
/* 341 */     int value1 = read(input);
/* 342 */     int value2 = read(input);
/* 343 */     int value3 = read(input);
/* 344 */     int value4 = read(input);
/*     */     
/* 346 */     return ((value1 & 0xFF) << 0) + ((value2 & 0xFF) << 8) + ((value3 & 0xFF) << 16) + ((value4 & 0xFF) << 24);
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
/*     */   public static long readSwappedUnsignedInteger(InputStream input) throws IOException {
/* 362 */     int value1 = read(input);
/* 363 */     int value2 = read(input);
/* 364 */     int value3 = read(input);
/* 365 */     int value4 = read(input);
/*     */     
/* 367 */     long low = (((value1 & 0xFF) << 0) + ((value2 & 0xFF) << 8) + ((value3 & 0xFF) << 16));
/*     */ 
/*     */ 
/*     */     
/* 371 */     long high = (value4 & 0xFF);
/*     */     
/* 373 */     return (high << 24L) + (0xFFFFFFFFL & low);
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
/*     */   public static void writeSwappedLong(OutputStream output, long value) throws IOException {
/* 386 */     output.write((byte)(int)(value >> 0L & 0xFFL));
/* 387 */     output.write((byte)(int)(value >> 8L & 0xFFL));
/* 388 */     output.write((byte)(int)(value >> 16L & 0xFFL));
/* 389 */     output.write((byte)(int)(value >> 24L & 0xFFL));
/* 390 */     output.write((byte)(int)(value >> 32L & 0xFFL));
/* 391 */     output.write((byte)(int)(value >> 40L & 0xFFL));
/* 392 */     output.write((byte)(int)(value >> 48L & 0xFFL));
/* 393 */     output.write((byte)(int)(value >> 56L & 0xFFL));
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
/*     */   public static long readSwappedLong(InputStream input) throws IOException {
/* 406 */     byte[] bytes = new byte[8];
/* 407 */     for (int i = 0; i < 8; i++) {
/* 408 */       bytes[i] = (byte)read(input);
/*     */     }
/* 410 */     return readSwappedLong(bytes, 0);
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
/*     */   public static void writeSwappedFloat(OutputStream output, float value) throws IOException {
/* 423 */     writeSwappedInteger(output, Float.floatToIntBits(value));
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
/*     */   public static float readSwappedFloat(InputStream input) throws IOException {
/* 436 */     return Float.intBitsToFloat(readSwappedInteger(input));
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
/*     */   public static void writeSwappedDouble(OutputStream output, double value) throws IOException {
/* 449 */     writeSwappedLong(output, Double.doubleToLongBits(value));
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
/*     */   public static double readSwappedDouble(InputStream input) throws IOException {
/* 462 */     return Double.longBitsToDouble(readSwappedLong(input));
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
/*     */   private static int read(InputStream input) throws IOException {
/* 474 */     int value = input.read();
/*     */     
/* 476 */     if (-1 == value) {
/* 477 */       throw new EOFException("Unexpected EOF reached");
/*     */     }
/*     */     
/* 480 */     return value;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\commons\io\EndianUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */