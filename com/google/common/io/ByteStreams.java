/*     */ package com.google.common.io;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.DataInput;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.DataOutput;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.EOFException;
/*     */ import java.io.FilterInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.channels.FileChannel;
/*     */ import java.nio.channels.ReadableByteChannel;
/*     */ import java.nio.channels.WritableByteChannel;
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
/*     */ @Beta
/*     */ @GwtIncompatible
/*     */ public final class ByteStreams
/*     */ {
/*     */   private static final int ZERO_COPY_CHUNK_SIZE = 524288;
/*     */   
/*     */   static byte[] createBuffer() {
/*  56 */     return new byte[8192];
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */   public static long copy(InputStream from, OutputStream to) throws IOException {
/* 101 */     Preconditions.checkNotNull(from);
/* 102 */     Preconditions.checkNotNull(to);
/* 103 */     byte[] buf = createBuffer();
/* 104 */     long total = 0L;
/*     */     while (true) {
/* 106 */       int r = from.read(buf);
/* 107 */       if (r == -1) {
/*     */         break;
/*     */       }
/* 110 */       to.write(buf, 0, r);
/* 111 */       total += r;
/*     */     } 
/* 113 */     return total;
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
/*     */   public static long copy(ReadableByteChannel from, WritableByteChannel to) throws IOException {
/* 127 */     Preconditions.checkNotNull(from);
/* 128 */     Preconditions.checkNotNull(to);
/* 129 */     if (from instanceof FileChannel) {
/* 130 */       FileChannel sourceChannel = (FileChannel)from;
/* 131 */       long oldPosition = sourceChannel.position();
/* 132 */       long position = oldPosition;
/*     */       
/*     */       while (true) {
/* 135 */         long copied = sourceChannel.transferTo(position, 524288L, to);
/* 136 */         position += copied;
/* 137 */         sourceChannel.position(position);
/* 138 */         if (copied <= 0L && position >= sourceChannel.size())
/* 139 */           return position - oldPosition; 
/*     */       } 
/*     */     } 
/* 142 */     ByteBuffer buf = ByteBuffer.wrap(createBuffer());
/* 143 */     long total = 0L;
/* 144 */     while (from.read(buf) != -1) {
/* 145 */       buf.flip();
/* 146 */       while (buf.hasRemaining()) {
/* 147 */         total += to.write(buf);
/*     */       }
/* 149 */       buf.clear();
/*     */     } 
/* 151 */     return total;
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
/*     */   public static byte[] toByteArray(InputStream in) throws IOException {
/* 165 */     ByteArrayOutputStream out = new ByteArrayOutputStream(Math.max(32, in.available()));
/* 166 */     copy(in, out);
/* 167 */     return out.toByteArray();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static byte[] toByteArray(InputStream in, int expectedSize) throws IOException {
/* 176 */     byte[] bytes = new byte[expectedSize];
/* 177 */     int remaining = expectedSize;
/*     */     
/* 179 */     while (remaining > 0) {
/* 180 */       int off = expectedSize - remaining;
/* 181 */       int read = in.read(bytes, off, remaining);
/* 182 */       if (read == -1)
/*     */       {
/*     */         
/* 185 */         return Arrays.copyOf(bytes, off);
/*     */       }
/* 187 */       remaining -= read;
/*     */     } 
/*     */ 
/*     */     
/* 191 */     int b = in.read();
/* 192 */     if (b == -1) {
/* 193 */       return bytes;
/*     */     }
/*     */ 
/*     */     
/* 197 */     FastByteArrayOutputStream out = new FastByteArrayOutputStream();
/* 198 */     out.write(b);
/* 199 */     copy(in, out);
/*     */     
/* 201 */     byte[] result = new byte[bytes.length + out.size()];
/* 202 */     System.arraycopy(bytes, 0, result, 0, bytes.length);
/* 203 */     out.writeTo(result, bytes.length);
/* 204 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static final class FastByteArrayOutputStream
/*     */     extends ByteArrayOutputStream
/*     */   {
/*     */     private FastByteArrayOutputStream() {}
/*     */ 
/*     */     
/*     */     void writeTo(byte[] b, int off) {
/* 216 */       System.arraycopy(this.buf, 0, b, off, this.count);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public static long exhaust(InputStream in) throws IOException {
/* 228 */     long total = 0L;
/*     */     
/* 230 */     byte[] buf = createBuffer(); long read;
/* 231 */     while ((read = in.read(buf)) != -1L) {
/* 232 */       total += read;
/*     */     }
/* 234 */     return total;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ByteArrayDataInput newDataInput(byte[] bytes) {
/* 242 */     return newDataInput(new ByteArrayInputStream(bytes));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ByteArrayDataInput newDataInput(byte[] bytes, int start) {
/* 253 */     Preconditions.checkPositionIndex(start, bytes.length);
/* 254 */     return newDataInput(new ByteArrayInputStream(bytes, start, bytes.length - start));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ByteArrayDataInput newDataInput(ByteArrayInputStream byteArrayInputStream) {
/* 265 */     return new ByteArrayDataInputStream((ByteArrayInputStream)Preconditions.checkNotNull(byteArrayInputStream));
/*     */   }
/*     */   
/*     */   private static class ByteArrayDataInputStream implements ByteArrayDataInput {
/*     */     final DataInput input;
/*     */     
/*     */     ByteArrayDataInputStream(ByteArrayInputStream byteArrayInputStream) {
/* 272 */       this.input = new DataInputStream(byteArrayInputStream);
/*     */     }
/*     */ 
/*     */     
/*     */     public void readFully(byte[] b) {
/*     */       try {
/* 278 */         this.input.readFully(b);
/* 279 */       } catch (IOException e) {
/* 280 */         throw new IllegalStateException(e);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void readFully(byte[] b, int off, int len) {
/*     */       try {
/* 287 */         this.input.readFully(b, off, len);
/* 288 */       } catch (IOException e) {
/* 289 */         throw new IllegalStateException(e);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public int skipBytes(int n) {
/*     */       try {
/* 296 */         return this.input.skipBytes(n);
/* 297 */       } catch (IOException e) {
/* 298 */         throw new IllegalStateException(e);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean readBoolean() {
/*     */       try {
/* 305 */         return this.input.readBoolean();
/* 306 */       } catch (IOException e) {
/* 307 */         throw new IllegalStateException(e);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public byte readByte() {
/*     */       try {
/* 314 */         return this.input.readByte();
/* 315 */       } catch (EOFException e) {
/* 316 */         throw new IllegalStateException(e);
/* 317 */       } catch (IOException impossible) {
/* 318 */         throw new AssertionError(impossible);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public int readUnsignedByte() {
/*     */       try {
/* 325 */         return this.input.readUnsignedByte();
/* 326 */       } catch (IOException e) {
/* 327 */         throw new IllegalStateException(e);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public short readShort() {
/*     */       try {
/* 334 */         return this.input.readShort();
/* 335 */       } catch (IOException e) {
/* 336 */         throw new IllegalStateException(e);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public int readUnsignedShort() {
/*     */       try {
/* 343 */         return this.input.readUnsignedShort();
/* 344 */       } catch (IOException e) {
/* 345 */         throw new IllegalStateException(e);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public char readChar() {
/*     */       try {
/* 352 */         return this.input.readChar();
/* 353 */       } catch (IOException e) {
/* 354 */         throw new IllegalStateException(e);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public int readInt() {
/*     */       try {
/* 361 */         return this.input.readInt();
/* 362 */       } catch (IOException e) {
/* 363 */         throw new IllegalStateException(e);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public long readLong() {
/*     */       try {
/* 370 */         return this.input.readLong();
/* 371 */       } catch (IOException e) {
/* 372 */         throw new IllegalStateException(e);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public float readFloat() {
/*     */       try {
/* 379 */         return this.input.readFloat();
/* 380 */       } catch (IOException e) {
/* 381 */         throw new IllegalStateException(e);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public double readDouble() {
/*     */       try {
/* 388 */         return this.input.readDouble();
/* 389 */       } catch (IOException e) {
/* 390 */         throw new IllegalStateException(e);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public String readLine() {
/*     */       try {
/* 397 */         return this.input.readLine();
/* 398 */       } catch (IOException e) {
/* 399 */         throw new IllegalStateException(e);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public String readUTF() {
/*     */       try {
/* 406 */         return this.input.readUTF();
/* 407 */       } catch (IOException e) {
/* 408 */         throw new IllegalStateException(e);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ByteArrayDataOutput newDataOutput() {
/* 417 */     return newDataOutput(new ByteArrayOutputStream());
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
/*     */   public static ByteArrayDataOutput newDataOutput(int size) {
/* 429 */     if (size < 0) {
/* 430 */       throw new IllegalArgumentException(String.format("Invalid size: %s", new Object[] { Integer.valueOf(size) }));
/*     */     }
/* 432 */     return newDataOutput(new ByteArrayOutputStream(size));
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
/*     */   public static ByteArrayDataOutput newDataOutput(ByteArrayOutputStream byteArrayOutputSteam) {
/* 448 */     return new ByteArrayDataOutputStream((ByteArrayOutputStream)Preconditions.checkNotNull(byteArrayOutputSteam));
/*     */   }
/*     */   
/*     */   private static class ByteArrayDataOutputStream
/*     */     implements ByteArrayDataOutput
/*     */   {
/*     */     final DataOutput output;
/*     */     final ByteArrayOutputStream byteArrayOutputSteam;
/*     */     
/*     */     ByteArrayDataOutputStream(ByteArrayOutputStream byteArrayOutputSteam) {
/* 458 */       this.byteArrayOutputSteam = byteArrayOutputSteam;
/* 459 */       this.output = new DataOutputStream(byteArrayOutputSteam);
/*     */     }
/*     */ 
/*     */     
/*     */     public void write(int b) {
/*     */       try {
/* 465 */         this.output.write(b);
/* 466 */       } catch (IOException impossible) {
/* 467 */         throw new AssertionError(impossible);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void write(byte[] b) {
/*     */       try {
/* 474 */         this.output.write(b);
/* 475 */       } catch (IOException impossible) {
/* 476 */         throw new AssertionError(impossible);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void write(byte[] b, int off, int len) {
/*     */       try {
/* 483 */         this.output.write(b, off, len);
/* 484 */       } catch (IOException impossible) {
/* 485 */         throw new AssertionError(impossible);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void writeBoolean(boolean v) {
/*     */       try {
/* 492 */         this.output.writeBoolean(v);
/* 493 */       } catch (IOException impossible) {
/* 494 */         throw new AssertionError(impossible);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void writeByte(int v) {
/*     */       try {
/* 501 */         this.output.writeByte(v);
/* 502 */       } catch (IOException impossible) {
/* 503 */         throw new AssertionError(impossible);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void writeBytes(String s) {
/*     */       try {
/* 510 */         this.output.writeBytes(s);
/* 511 */       } catch (IOException impossible) {
/* 512 */         throw new AssertionError(impossible);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void writeChar(int v) {
/*     */       try {
/* 519 */         this.output.writeChar(v);
/* 520 */       } catch (IOException impossible) {
/* 521 */         throw new AssertionError(impossible);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void writeChars(String s) {
/*     */       try {
/* 528 */         this.output.writeChars(s);
/* 529 */       } catch (IOException impossible) {
/* 530 */         throw new AssertionError(impossible);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void writeDouble(double v) {
/*     */       try {
/* 537 */         this.output.writeDouble(v);
/* 538 */       } catch (IOException impossible) {
/* 539 */         throw new AssertionError(impossible);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void writeFloat(float v) {
/*     */       try {
/* 546 */         this.output.writeFloat(v);
/* 547 */       } catch (IOException impossible) {
/* 548 */         throw new AssertionError(impossible);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void writeInt(int v) {
/*     */       try {
/* 555 */         this.output.writeInt(v);
/* 556 */       } catch (IOException impossible) {
/* 557 */         throw new AssertionError(impossible);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void writeLong(long v) {
/*     */       try {
/* 564 */         this.output.writeLong(v);
/* 565 */       } catch (IOException impossible) {
/* 566 */         throw new AssertionError(impossible);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void writeShort(int v) {
/*     */       try {
/* 573 */         this.output.writeShort(v);
/* 574 */       } catch (IOException impossible) {
/* 575 */         throw new AssertionError(impossible);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void writeUTF(String s) {
/*     */       try {
/* 582 */         this.output.writeUTF(s);
/* 583 */       } catch (IOException impossible) {
/* 584 */         throw new AssertionError(impossible);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public byte[] toByteArray() {
/* 590 */       return this.byteArrayOutputSteam.toByteArray();
/*     */     }
/*     */   }
/*     */   
/* 594 */   private static final OutputStream NULL_OUTPUT_STREAM = new OutputStream()
/*     */     {
/*     */       public void write(int b) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       public void write(byte[] b) {
/* 603 */         Preconditions.checkNotNull(b);
/*     */       }
/*     */ 
/*     */ 
/*     */       
/*     */       public void write(byte[] b, int off, int len) {
/* 609 */         Preconditions.checkNotNull(b);
/*     */       }
/*     */ 
/*     */       
/*     */       public String toString() {
/* 614 */         return "ByteStreams.nullOutputStream()";
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static OutputStream nullOutputStream() {
/* 624 */     return NULL_OUTPUT_STREAM;
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
/*     */   public static InputStream limit(InputStream in, long limit) {
/* 636 */     return new LimitedInputStream(in, limit);
/*     */   }
/*     */   
/*     */   private static final class LimitedInputStream
/*     */     extends FilterInputStream {
/*     */     private long left;
/* 642 */     private long mark = -1L;
/*     */     
/*     */     LimitedInputStream(InputStream in, long limit) {
/* 645 */       super(in);
/* 646 */       Preconditions.checkNotNull(in);
/* 647 */       Preconditions.checkArgument((limit >= 0L), "limit must be non-negative");
/* 648 */       this.left = limit;
/*     */     }
/*     */ 
/*     */     
/*     */     public int available() throws IOException {
/* 653 */       return (int)Math.min(this.in.available(), this.left);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public synchronized void mark(int readLimit) {
/* 659 */       this.in.mark(readLimit);
/* 660 */       this.mark = this.left;
/*     */     }
/*     */ 
/*     */     
/*     */     public int read() throws IOException {
/* 665 */       if (this.left == 0L) {
/* 666 */         return -1;
/*     */       }
/*     */       
/* 669 */       int result = this.in.read();
/* 670 */       if (result != -1) {
/* 671 */         this.left--;
/*     */       }
/* 673 */       return result;
/*     */     }
/*     */ 
/*     */     
/*     */     public int read(byte[] b, int off, int len) throws IOException {
/* 678 */       if (this.left == 0L) {
/* 679 */         return -1;
/*     */       }
/*     */       
/* 682 */       len = (int)Math.min(len, this.left);
/* 683 */       int result = this.in.read(b, off, len);
/* 684 */       if (result != -1) {
/* 685 */         this.left -= result;
/*     */       }
/* 687 */       return result;
/*     */     }
/*     */ 
/*     */     
/*     */     public synchronized void reset() throws IOException {
/* 692 */       if (!this.in.markSupported()) {
/* 693 */         throw new IOException("Mark not supported");
/*     */       }
/* 695 */       if (this.mark == -1L) {
/* 696 */         throw new IOException("Mark not set");
/*     */       }
/*     */       
/* 699 */       this.in.reset();
/* 700 */       this.left = this.mark;
/*     */     }
/*     */ 
/*     */     
/*     */     public long skip(long n) throws IOException {
/* 705 */       n = Math.min(n, this.left);
/* 706 */       long skipped = this.in.skip(n);
/* 707 */       this.left -= skipped;
/* 708 */       return skipped;
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
/*     */   
/*     */   public static void readFully(InputStream in, byte[] b) throws IOException {
/* 722 */     readFully(in, b, 0, b.length);
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
/*     */   public static void readFully(InputStream in, byte[] b, int off, int len) throws IOException {
/* 738 */     int read = read(in, b, off, len);
/* 739 */     if (read != len) {
/* 740 */       throw new EOFException("reached end of stream after reading " + read + " bytes; " + len + " bytes expected");
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
/*     */ 
/*     */   
/*     */   public static void skipFully(InputStream in, long n) throws IOException {
/* 755 */     long skipped = skipUpTo(in, n);
/* 756 */     if (skipped < n) {
/* 757 */       throw new EOFException("reached end of stream after skipping " + skipped + " bytes; " + n + " bytes expected");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static long skipUpTo(InputStream in, long n) throws IOException {
/* 768 */     long totalSkipped = 0L;
/* 769 */     byte[] buf = createBuffer();
/*     */     
/* 771 */     while (totalSkipped < n) {
/* 772 */       long remaining = n - totalSkipped;
/* 773 */       long skipped = skipSafely(in, remaining);
/*     */       
/* 775 */       if (skipped == 0L) {
/*     */ 
/*     */         
/* 778 */         int skip = (int)Math.min(remaining, buf.length);
/* 779 */         if ((skipped = in.read(buf, 0, skip)) == -1L) {
/*     */           break;
/*     */         }
/*     */       } 
/*     */ 
/*     */       
/* 785 */       totalSkipped += skipped;
/*     */     } 
/*     */     
/* 788 */     return totalSkipped;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static long skipSafely(InputStream in, long n) throws IOException {
/* 799 */     int available = in.available();
/* 800 */     return (available == 0) ? 0L : in.skip(Math.min(available, n));
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
/*     */   @CanIgnoreReturnValue
/*     */   public static <T> T readBytes(InputStream input, ByteProcessor<T> processor) throws IOException {
/*     */     int read;
/* 814 */     Preconditions.checkNotNull(input);
/* 815 */     Preconditions.checkNotNull(processor);
/*     */     
/* 817 */     byte[] buf = createBuffer();
/*     */     
/*     */     do {
/* 820 */       read = input.read(buf);
/* 821 */     } while (read != -1 && processor.processBytes(buf, 0, read));
/* 822 */     return processor.getResult();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public static int read(InputStream in, byte[] b, int off, int len) throws IOException {
/* 852 */     Preconditions.checkNotNull(in);
/* 853 */     Preconditions.checkNotNull(b);
/* 854 */     if (len < 0) {
/* 855 */       throw new IndexOutOfBoundsException("len is negative");
/*     */     }
/* 857 */     int total = 0;
/* 858 */     while (total < len) {
/* 859 */       int result = in.read(b, off + total, len - total);
/* 860 */       if (result == -1) {
/*     */         break;
/*     */       }
/* 863 */       total += result;
/*     */     } 
/* 865 */     return total;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\io\ByteStreams.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */