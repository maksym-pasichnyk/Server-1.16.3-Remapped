/*     */ package com.google.common.io;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.primitives.Ints;
/*     */ import com.google.common.primitives.Longs;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.io.DataInput;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.EOFException;
/*     */ import java.io.FilterInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Beta
/*     */ @GwtIncompatible
/*     */ public final class LittleEndianDataInputStream
/*     */   extends FilterInputStream
/*     */   implements DataInput
/*     */ {
/*     */   public LittleEndianDataInputStream(InputStream in) {
/*  51 */     super((InputStream)Preconditions.checkNotNull(in));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public String readLine() {
/*  60 */     throw new UnsupportedOperationException("readLine is not supported");
/*     */   }
/*     */ 
/*     */   
/*     */   public void readFully(byte[] b) throws IOException {
/*  65 */     ByteStreams.readFully(this, b);
/*     */   }
/*     */ 
/*     */   
/*     */   public void readFully(byte[] b, int off, int len) throws IOException {
/*  70 */     ByteStreams.readFully(this, b, off, len);
/*     */   }
/*     */ 
/*     */   
/*     */   public int skipBytes(int n) throws IOException {
/*  75 */     return (int)this.in.skip(n);
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public int readUnsignedByte() throws IOException {
/*  81 */     int b1 = this.in.read();
/*  82 */     if (0 > b1) {
/*  83 */       throw new EOFException();
/*     */     }
/*     */     
/*  86 */     return b1;
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
/*     */   @CanIgnoreReturnValue
/*     */   public int readUnsignedShort() throws IOException {
/* 100 */     byte b1 = readAndCheckByte();
/* 101 */     byte b2 = readAndCheckByte();
/*     */     
/* 103 */     return Ints.fromBytes((byte)0, (byte)0, b2, b1);
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
/*     */   @CanIgnoreReturnValue
/*     */   public int readInt() throws IOException {
/* 117 */     byte b1 = readAndCheckByte();
/* 118 */     byte b2 = readAndCheckByte();
/* 119 */     byte b3 = readAndCheckByte();
/* 120 */     byte b4 = readAndCheckByte();
/*     */     
/* 122 */     return Ints.fromBytes(b4, b3, b2, b1);
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
/*     */   @CanIgnoreReturnValue
/*     */   public long readLong() throws IOException {
/* 136 */     byte b1 = readAndCheckByte();
/* 137 */     byte b2 = readAndCheckByte();
/* 138 */     byte b3 = readAndCheckByte();
/* 139 */     byte b4 = readAndCheckByte();
/* 140 */     byte b5 = readAndCheckByte();
/* 141 */     byte b6 = readAndCheckByte();
/* 142 */     byte b7 = readAndCheckByte();
/* 143 */     byte b8 = readAndCheckByte();
/*     */     
/* 145 */     return Longs.fromBytes(b8, b7, b6, b5, b4, b3, b2, b1);
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
/*     */   @CanIgnoreReturnValue
/*     */   public float readFloat() throws IOException {
/* 159 */     return Float.intBitsToFloat(readInt());
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
/*     */   @CanIgnoreReturnValue
/*     */   public double readDouble() throws IOException {
/* 173 */     return Double.longBitsToDouble(readLong());
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public String readUTF() throws IOException {
/* 179 */     return (new DataInputStream(this.in)).readUTF();
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
/*     */   @CanIgnoreReturnValue
/*     */   public short readShort() throws IOException {
/* 193 */     return (short)readUnsignedShort();
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
/*     */   @CanIgnoreReturnValue
/*     */   public char readChar() throws IOException {
/* 207 */     return (char)readUnsignedShort();
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public byte readByte() throws IOException {
/* 213 */     return (byte)readUnsignedByte();
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public boolean readBoolean() throws IOException {
/* 219 */     return (readUnsignedByte() != 0);
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
/*     */   private byte readAndCheckByte() throws IOException, EOFException {
/* 231 */     int b1 = this.in.read();
/*     */     
/* 233 */     if (-1 == b1) {
/* 234 */       throw new EOFException();
/*     */     }
/*     */     
/* 237 */     return (byte)b1;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\io\LittleEndianDataInputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */