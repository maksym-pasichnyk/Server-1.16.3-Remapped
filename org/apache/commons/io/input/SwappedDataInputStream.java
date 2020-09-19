/*     */ package org.apache.commons.io.input;
/*     */ 
/*     */ import java.io.DataInput;
/*     */ import java.io.EOFException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import org.apache.commons.io.EndianUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SwappedDataInputStream
/*     */   extends ProxyInputStream
/*     */   implements DataInput
/*     */ {
/*     */   public SwappedDataInputStream(InputStream input) {
/*  48 */     super(input);
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
/*     */   public boolean readBoolean() throws IOException, EOFException {
/*  60 */     return (0 != readByte());
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
/*     */   public byte readByte() throws IOException, EOFException {
/*  72 */     return (byte)this.in.read();
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
/*     */   public char readChar() throws IOException, EOFException {
/*  84 */     return (char)readShort();
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
/*     */   public double readDouble() throws IOException, EOFException {
/*  96 */     return EndianUtils.readSwappedDouble(this.in);
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
/*     */   public float readFloat() throws IOException, EOFException {
/* 108 */     return EndianUtils.readSwappedFloat(this.in);
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
/*     */   public void readFully(byte[] data) throws IOException, EOFException {
/* 121 */     readFully(data, 0, data.length);
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
/*     */   public void readFully(byte[] data, int offset, int length) throws IOException, EOFException {
/* 137 */     int remaining = length;
/*     */     
/* 139 */     while (remaining > 0) {
/*     */       
/* 141 */       int location = offset + length - remaining;
/* 142 */       int count = read(data, location, remaining);
/*     */       
/* 144 */       if (-1 == count)
/*     */       {
/* 146 */         throw new EOFException();
/*     */       }
/*     */       
/* 149 */       remaining -= count;
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
/*     */   public int readInt() throws IOException, EOFException {
/* 162 */     return EndianUtils.readSwappedInteger(this.in);
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
/*     */   public String readLine() throws IOException, EOFException {
/* 174 */     throw new UnsupportedOperationException("Operation not supported: readLine()");
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
/*     */   public long readLong() throws IOException, EOFException {
/* 187 */     return EndianUtils.readSwappedLong(this.in);
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
/*     */   public short readShort() throws IOException, EOFException {
/* 199 */     return EndianUtils.readSwappedShort(this.in);
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
/*     */   public int readUnsignedByte() throws IOException, EOFException {
/* 211 */     return this.in.read();
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
/*     */   public int readUnsignedShort() throws IOException, EOFException {
/* 223 */     return EndianUtils.readSwappedUnsignedShort(this.in);
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
/*     */   public String readUTF() throws IOException, EOFException {
/* 235 */     throw new UnsupportedOperationException("Operation not supported: readUTF()");
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
/*     */   public int skipBytes(int count) throws IOException, EOFException {
/* 249 */     return (int)this.in.skip(count);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\commons\io\input\SwappedDataInputStream.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */