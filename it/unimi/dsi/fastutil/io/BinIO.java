/*      */ package it.unimi.dsi.fastutil.io;
/*      */ 
/*      */ import it.unimi.dsi.fastutil.BigArrays;
/*      */ import it.unimi.dsi.fastutil.booleans.BooleanArrays;
/*      */ import it.unimi.dsi.fastutil.booleans.BooleanBigArrays;
/*      */ import it.unimi.dsi.fastutil.booleans.BooleanIterable;
/*      */ import it.unimi.dsi.fastutil.booleans.BooleanIterator;
/*      */ import it.unimi.dsi.fastutil.bytes.ByteArrays;
/*      */ import it.unimi.dsi.fastutil.bytes.ByteBigArrays;
/*      */ import it.unimi.dsi.fastutil.bytes.ByteIterable;
/*      */ import it.unimi.dsi.fastutil.bytes.ByteIterator;
/*      */ import it.unimi.dsi.fastutil.chars.CharArrays;
/*      */ import it.unimi.dsi.fastutil.chars.CharBigArrays;
/*      */ import it.unimi.dsi.fastutil.chars.CharIterable;
/*      */ import it.unimi.dsi.fastutil.chars.CharIterator;
/*      */ import it.unimi.dsi.fastutil.doubles.DoubleArrays;
/*      */ import it.unimi.dsi.fastutil.doubles.DoubleBigArrays;
/*      */ import it.unimi.dsi.fastutil.doubles.DoubleIterable;
/*      */ import it.unimi.dsi.fastutil.doubles.DoubleIterator;
/*      */ import it.unimi.dsi.fastutil.floats.FloatArrays;
/*      */ import it.unimi.dsi.fastutil.floats.FloatBigArrays;
/*      */ import it.unimi.dsi.fastutil.floats.FloatIterable;
/*      */ import it.unimi.dsi.fastutil.floats.FloatIterator;
/*      */ import it.unimi.dsi.fastutil.ints.IntArrays;
/*      */ import it.unimi.dsi.fastutil.ints.IntBigArrays;
/*      */ import it.unimi.dsi.fastutil.ints.IntIterable;
/*      */ import it.unimi.dsi.fastutil.ints.IntIterator;
/*      */ import it.unimi.dsi.fastutil.longs.LongArrays;
/*      */ import it.unimi.dsi.fastutil.longs.LongBigArrays;
/*      */ import it.unimi.dsi.fastutil.longs.LongIterable;
/*      */ import it.unimi.dsi.fastutil.longs.LongIterator;
/*      */ import it.unimi.dsi.fastutil.shorts.ShortArrays;
/*      */ import it.unimi.dsi.fastutil.shorts.ShortBigArrays;
/*      */ import it.unimi.dsi.fastutil.shorts.ShortIterable;
/*      */ import it.unimi.dsi.fastutil.shorts.ShortIterator;
/*      */ import java.io.DataInput;
/*      */ import java.io.DataInputStream;
/*      */ import java.io.DataOutput;
/*      */ import java.io.DataOutputStream;
/*      */ import java.io.EOFException;
/*      */ import java.io.File;
/*      */ import java.io.FileInputStream;
/*      */ import java.io.FileOutputStream;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.io.ObjectOutputStream;
/*      */ import java.io.OutputStream;
/*      */ import java.util.NoSuchElementException;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class BinIO
/*      */ {
/*      */   private static final int MAX_IO_LENGTH = 1048576;
/*      */   
/*      */   public static void storeObject(Object o, File file) throws IOException {
/*   72 */     ObjectOutputStream oos = new ObjectOutputStream(new FastBufferedOutputStream(new FileOutputStream(file)));
/*   73 */     oos.writeObject(o);
/*   74 */     oos.close();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeObject(Object o, CharSequence filename) throws IOException {
/*   83 */     storeObject(o, new File(filename.toString()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Object loadObject(File file) throws IOException, ClassNotFoundException {
/*   92 */     ObjectInputStream ois = new ObjectInputStream(new FastBufferedInputStream(new FileInputStream(file)));
/*   93 */     Object result = ois.readObject();
/*   94 */     ois.close();
/*   95 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Object loadObject(CharSequence filename) throws IOException, ClassNotFoundException {
/*  104 */     return loadObject(new File(filename.toString()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeObject(Object o, OutputStream s) throws IOException {
/*  117 */     ObjectOutputStream oos = new ObjectOutputStream(new FastBufferedOutputStream(s));
/*  118 */     oos.writeObject(o);
/*  119 */     oos.flush();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Object loadObject(InputStream s) throws IOException, ClassNotFoundException {
/*  135 */     ObjectInputStream ois = new ObjectInputStream(new FastBufferedInputStream(s));
/*  136 */     Object result = ois.readObject();
/*  137 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static int read(InputStream is, byte[] a, int offset, int length) throws IOException {
/*  180 */     if (length == 0) return 0; 
/*  181 */     int read = 0;
/*      */     while (true) {
/*  183 */       int result = is.read(a, offset + read, Math.min(length - read, 1048576));
/*  184 */       if (result < 0) return read; 
/*  185 */       read += result;
/*  186 */       if (read >= length)
/*  187 */         return read; 
/*      */     } 
/*      */   } private static void write(OutputStream outputStream, byte[] a, int offset, int length) throws IOException {
/*  190 */     int written = 0;
/*  191 */     while (written < length) {
/*  192 */       outputStream.write(a, offset + written, Math.min(length - written, 1048576));
/*  193 */       written += Math.min(length - written, 1048576);
/*      */     } 
/*      */   }
/*      */   private static void write(DataOutput dataOutput, byte[] a, int offset, int length) throws IOException {
/*  197 */     int written = 0;
/*  198 */     while (written < length) {
/*  199 */       dataOutput.write(a, offset + written, Math.min(length - written, 1048576));
/*  200 */       written += Math.min(length - written, 1048576);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int loadBytes(InputStream inputStream, byte[] array, int offset, int length) throws IOException {
/*  216 */     return read(inputStream, array, offset, length);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int loadBytes(InputStream inputStream, byte[] array) throws IOException {
/*  228 */     return read(inputStream, array, 0, array.length);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeBytes(byte[] array, int offset, int length, OutputStream outputStream) throws IOException {
/*  241 */     write(outputStream, array, offset, length);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeBytes(byte[] array, OutputStream outputStream) throws IOException {
/*  252 */     write(outputStream, array, 0, array.length);
/*      */   }
/*      */   private static long read(InputStream is, byte[][] a, long offset, long length) throws IOException {
/*  255 */     if (length == 0L) return 0L; 
/*  256 */     long read = 0L;
/*  257 */     int segment = BigArrays.segment(offset);
/*  258 */     int displacement = BigArrays.displacement(offset);
/*      */     
/*      */     while (true) {
/*  261 */       int result = is.read(a[segment], displacement, (int)Math.min(((a[segment]).length - displacement), Math.min(length - read, 1048576L)));
/*  262 */       if (result < 0) return read; 
/*  263 */       read += result;
/*  264 */       displacement += result;
/*  265 */       if (displacement == (a[segment]).length) {
/*  266 */         segment++;
/*  267 */         displacement = 0;
/*      */       } 
/*  269 */       if (read >= length)
/*  270 */         return read; 
/*      */     } 
/*      */   } private static void write(OutputStream outputStream, byte[][] a, long offset, long length) throws IOException {
/*  273 */     if (length == 0L)
/*  274 */       return;  long written = 0L;
/*      */     
/*  276 */     int segment = BigArrays.segment(offset);
/*  277 */     int displacement = BigArrays.displacement(offset);
/*      */     do {
/*  279 */       int toWrite = (int)Math.min(((a[segment]).length - displacement), Math.min(length - written, 1048576L));
/*  280 */       outputStream.write(a[segment], displacement, toWrite);
/*  281 */       written += toWrite;
/*  282 */       displacement += toWrite;
/*  283 */       if (displacement != (a[segment]).length)
/*  284 */         continue;  segment++;
/*  285 */       displacement = 0;
/*      */     }
/*  287 */     while (written < length);
/*      */   }
/*      */   private static void write(DataOutput dataOutput, byte[][] a, long offset, long length) throws IOException {
/*  290 */     if (length == 0L)
/*  291 */       return;  long written = 0L;
/*      */     
/*  293 */     int segment = BigArrays.segment(offset);
/*  294 */     int displacement = BigArrays.displacement(offset);
/*      */     do {
/*  296 */       int toWrite = (int)Math.min(((a[segment]).length - displacement), Math.min(length - written, 1048576L));
/*  297 */       dataOutput.write(a[segment], displacement, toWrite);
/*  298 */       written += toWrite;
/*  299 */       displacement += toWrite;
/*  300 */       if (displacement != (a[segment]).length)
/*  301 */         continue;  segment++;
/*  302 */       displacement = 0;
/*      */     }
/*  304 */     while (written < length);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long loadBytes(InputStream inputStream, byte[][] array, long offset, long length) throws IOException {
/*  319 */     return read(inputStream, array, offset, length);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long loadBytes(InputStream inputStream, byte[][] array) throws IOException {
/*  331 */     return read(inputStream, array, 0L, ByteBigArrays.length(array));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeBytes(byte[][] array, long offset, long length, OutputStream outputStream) throws IOException {
/*  344 */     write(outputStream, array, offset, length);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeBytes(byte[][] array, OutputStream outputStream) throws IOException {
/*  355 */     write(outputStream, array, 0L, ByteBigArrays.length(array));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int loadBytes(DataInput dataInput, byte[] array, int offset, int length) throws IOException {
/*  366 */     ByteArrays.ensureOffsetLength(array, offset, length);
/*  367 */     int i = 0;
/*      */     try {
/*  369 */       for (i = 0; i < length; ) { array[i + offset] = dataInput.readByte(); i++; }
/*      */     
/*  371 */     } catch (EOFException eOFException) {}
/*  372 */     return i;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int loadBytes(DataInput dataInput, byte[] array) throws IOException {
/*  381 */     int i = 0;
/*      */     try {
/*  383 */       int length = array.length;
/*  384 */       for (i = 0; i < length; ) { array[i] = dataInput.readByte(); i++; }
/*      */     
/*  386 */     } catch (EOFException eOFException) {}
/*  387 */     return i;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int loadBytes(File file, byte[] array, int offset, int length) throws IOException {
/*  398 */     ByteArrays.ensureOffsetLength(array, offset, length);
/*  399 */     FileInputStream fis = new FileInputStream(file);
/*  400 */     int result = read(fis, array, offset, length);
/*  401 */     fis.close();
/*  402 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int loadBytes(CharSequence filename, byte[] array, int offset, int length) throws IOException {
/*  413 */     return loadBytes(new File(filename.toString()), array, offset, length);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int loadBytes(File file, byte[] array) throws IOException {
/*  422 */     FileInputStream fis = new FileInputStream(file);
/*  423 */     int result = read(fis, array, 0, array.length);
/*  424 */     fis.close();
/*  425 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int loadBytes(CharSequence filename, byte[] array) throws IOException {
/*  434 */     return loadBytes(new File(filename.toString()), array);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static byte[] loadBytes(File file) throws IOException {
/*  446 */     FileInputStream fis = new FileInputStream(file);
/*  447 */     long length = fis.getChannel().size() / 1L;
/*  448 */     if (length > 2147483647L) {
/*  449 */       fis.close();
/*  450 */       throw new IllegalArgumentException("File too long: " + fis.getChannel().size() + " bytes (" + length + " elements)");
/*      */     } 
/*  452 */     byte[] array = new byte[(int)length];
/*  453 */     if (read(fis, array, 0, (int)length) < length) throw new EOFException(); 
/*  454 */     fis.close();
/*  455 */     return array;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static byte[] loadBytes(CharSequence filename) throws IOException {
/*  467 */     return loadBytes(new File(filename.toString()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeBytes(byte[] array, int offset, int length, DataOutput dataOutput) throws IOException {
/*  477 */     ByteArrays.ensureOffsetLength(array, offset, length);
/*  478 */     write(dataOutput, array, offset, length);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeBytes(byte[] array, DataOutput dataOutput) throws IOException {
/*  486 */     write(dataOutput, array, 0, array.length);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeBytes(byte[] array, int offset, int length, File file) throws IOException {
/*  496 */     ByteArrays.ensureOffsetLength(array, offset, length);
/*  497 */     OutputStream os = new FastBufferedOutputStream(new FileOutputStream(file));
/*  498 */     write(os, array, offset, length);
/*  499 */     os.close();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeBytes(byte[] array, int offset, int length, CharSequence filename) throws IOException {
/*  509 */     storeBytes(array, offset, length, new File(filename.toString()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeBytes(byte[] array, File file) throws IOException {
/*  517 */     OutputStream os = new FastBufferedOutputStream(new FileOutputStream(file));
/*  518 */     write(os, array, 0, array.length);
/*  519 */     os.close();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeBytes(byte[] array, CharSequence filename) throws IOException {
/*  527 */     storeBytes(array, new File(filename.toString()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long loadBytes(DataInput dataInput, byte[][] array, long offset, long length) throws IOException {
/*  538 */     ByteBigArrays.ensureOffsetLength(array, offset, length);
/*  539 */     long c = 0L;
/*      */     try {
/*  541 */       for (int i = BigArrays.segment(offset); i < BigArrays.segment(offset + length + 134217727L); i++) {
/*  542 */         byte[] t = array[i];
/*  543 */         int l = (int)Math.min(t.length, offset + length - BigArrays.start(i));
/*  544 */         for (int d = (int)Math.max(0L, offset - BigArrays.start(i)); d < l; d++) {
/*  545 */           t[d] = dataInput.readByte();
/*  546 */           c++;
/*      */         }
/*      */       
/*      */       } 
/*  550 */     } catch (EOFException eOFException) {}
/*  551 */     return c;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long loadBytes(DataInput dataInput, byte[][] array) throws IOException {
/*  560 */     long c = 0L;
/*      */     try {
/*  562 */       for (int i = 0; i < array.length; i++) {
/*  563 */         byte[] t = array[i];
/*  564 */         int l = t.length;
/*  565 */         for (int d = 0; d < l; d++) {
/*  566 */           t[d] = dataInput.readByte();
/*  567 */           c++;
/*      */         }
/*      */       
/*      */       } 
/*  571 */     } catch (EOFException eOFException) {}
/*  572 */     return c;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long loadBytes(File file, byte[][] array, long offset, long length) throws IOException {
/*  583 */     ByteBigArrays.ensureOffsetLength(array, offset, length);
/*  584 */     FileInputStream fis = new FileInputStream(file);
/*  585 */     long result = read(fis, array, offset, length);
/*  586 */     fis.close();
/*  587 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long loadBytes(CharSequence filename, byte[][] array, long offset, long length) throws IOException {
/*  598 */     return loadBytes(new File(filename.toString()), array, offset, length);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long loadBytes(File file, byte[][] array) throws IOException {
/*  607 */     FileInputStream fis = new FileInputStream(file);
/*  608 */     long result = read(fis, array, 0L, ByteBigArrays.length(array));
/*  609 */     fis.close();
/*  610 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long loadBytes(CharSequence filename, byte[][] array) throws IOException {
/*  619 */     return loadBytes(new File(filename.toString()), array);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static byte[][] loadBytesBig(File file) throws IOException {
/*  631 */     FileInputStream fis = new FileInputStream(file);
/*  632 */     long length = fis.getChannel().size() / 1L;
/*  633 */     byte[][] array = ByteBigArrays.newBigArray(length);
/*  634 */     if (read(fis, array, 0L, length) < length) throw new EOFException(); 
/*  635 */     fis.close();
/*  636 */     return array;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static byte[][] loadBytesBig(CharSequence filename) throws IOException {
/*  648 */     return loadBytesBig(new File(filename.toString()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeBytes(byte[][] array, long offset, long length, DataOutput dataOutput) throws IOException {
/*  658 */     ByteBigArrays.ensureOffsetLength(array, offset, length);
/*  659 */     write(dataOutput, array, offset, length);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeBytes(byte[][] array, DataOutput dataOutput) throws IOException {
/*  667 */     write(dataOutput, array, 0L, ByteBigArrays.length(array));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeBytes(byte[][] array, long offset, long length, File file) throws IOException {
/*  677 */     ByteBigArrays.ensureOffsetLength(array, offset, length);
/*  678 */     OutputStream os = new FastBufferedOutputStream(new FileOutputStream(file));
/*  679 */     write(os, array, offset, length);
/*  680 */     os.close();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeBytes(byte[][] array, long offset, long length, CharSequence filename) throws IOException {
/*  690 */     storeBytes(array, offset, length, new File(filename.toString()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeBytes(byte[][] array, File file) throws IOException {
/*  698 */     OutputStream os = new FastBufferedOutputStream(new FileOutputStream(file));
/*  699 */     write(os, array, 0L, ByteBigArrays.length(array));
/*  700 */     os.close();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeBytes(byte[][] array, CharSequence filename) throws IOException {
/*  708 */     storeBytes(array, new File(filename.toString()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeBytes(ByteIterator i, DataOutput dataOutput) throws IOException {
/*  716 */     for (; i.hasNext(); dataOutput.writeByte(i.nextByte()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeBytes(ByteIterator i, File file) throws IOException {
/*  724 */     DataOutputStream dos = new DataOutputStream(new FastBufferedOutputStream(new FileOutputStream(file)));
/*  725 */     for (; i.hasNext(); dos.writeByte(i.nextByte()));
/*  726 */     dos.close();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeBytes(ByteIterator i, CharSequence filename) throws IOException {
/*  734 */     storeBytes(i, new File(filename.toString()));
/*      */   }
/*      */   
/*      */   private static final class ByteDataInputWrapper implements ByteIterator { private final DataInput dataInput;
/*      */     private boolean toAdvance = true;
/*      */     private boolean endOfProcess = false;
/*      */     private byte next;
/*      */     
/*      */     public ByteDataInputWrapper(DataInput dataInput) {
/*  743 */       this.dataInput = dataInput;
/*      */     }
/*      */     
/*      */     public boolean hasNext() {
/*  747 */       if (!this.toAdvance) return !this.endOfProcess; 
/*  748 */       this.toAdvance = false; 
/*  749 */       try { this.next = this.dataInput.readByte(); }
/*  750 */       catch (EOFException eof) { this.endOfProcess = true; }
/*  751 */       catch (IOException rethrow) { throw new RuntimeException(rethrow); }
/*  752 */        return !this.endOfProcess;
/*      */     }
/*      */     
/*      */     public byte nextByte() {
/*  756 */       if (!hasNext()) throw new NoSuchElementException(); 
/*  757 */       this.toAdvance = true;
/*  758 */       return this.next;
/*      */     } }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static ByteIterator asByteIterator(DataInput dataInput) {
/*  766 */     return new ByteDataInputWrapper(dataInput);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static ByteIterator asByteIterator(File file) throws IOException {
/*  773 */     return new ByteDataInputWrapper(new DataInputStream(new FastBufferedInputStream(new FileInputStream(file))));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static ByteIterator asByteIterator(CharSequence filename) throws IOException {
/*  780 */     return asByteIterator(new File(filename.toString()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static ByteIterable asByteIterable(File file) {
/*  787 */     return () -> { try {
/*      */           return asByteIterator(file);
/*  789 */         } catch (IOException e) {
/*      */           throw new RuntimeException(e);
/*      */         } 
/*      */       };
/*      */   }
/*      */ 
/*      */   
/*      */   public static ByteIterable asByteIterable(CharSequence filename) {
/*  797 */     return () -> { try {
/*      */           return asByteIterator(filename);
/*  799 */         } catch (IOException e) {
/*      */           throw new RuntimeException(e);
/*      */         } 
/*      */       };
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int loadInts(DataInput dataInput, int[] array, int offset, int length) throws IOException {
/*  849 */     IntArrays.ensureOffsetLength(array, offset, length);
/*  850 */     int i = 0;
/*      */     try {
/*  852 */       for (i = 0; i < length; ) { array[i + offset] = dataInput.readInt(); i++; }
/*      */     
/*  854 */     } catch (EOFException eOFException) {}
/*  855 */     return i;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int loadInts(DataInput dataInput, int[] array) throws IOException {
/*  864 */     int i = 0;
/*      */     try {
/*  866 */       int length = array.length;
/*  867 */       for (i = 0; i < length; ) { array[i] = dataInput.readInt(); i++; }
/*      */     
/*  869 */     } catch (EOFException eOFException) {}
/*  870 */     return i;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int loadInts(File file, int[] array, int offset, int length) throws IOException {
/*  881 */     IntArrays.ensureOffsetLength(array, offset, length);
/*  882 */     FileInputStream fis = new FileInputStream(file);
/*  883 */     DataInputStream dis = new DataInputStream(new FastBufferedInputStream(fis));
/*  884 */     int i = 0;
/*      */     try {
/*  886 */       for (i = 0; i < length; ) { array[i + offset] = dis.readInt(); i++; }
/*      */     
/*  888 */     } catch (EOFException eOFException) {}
/*  889 */     dis.close();
/*  890 */     return i;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int loadInts(CharSequence filename, int[] array, int offset, int length) throws IOException {
/*  901 */     return loadInts(new File(filename.toString()), array, offset, length);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int loadInts(File file, int[] array) throws IOException {
/*  910 */     FileInputStream fis = new FileInputStream(file);
/*  911 */     DataInputStream dis = new DataInputStream(new FastBufferedInputStream(fis));
/*  912 */     int i = 0;
/*      */     try {
/*  914 */       int length = array.length;
/*  915 */       for (i = 0; i < length; ) { array[i] = dis.readInt(); i++; }
/*      */     
/*  917 */     } catch (EOFException eOFException) {}
/*  918 */     dis.close();
/*  919 */     return i;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int loadInts(CharSequence filename, int[] array) throws IOException {
/*  928 */     return loadInts(new File(filename.toString()), array);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int[] loadInts(File file) throws IOException {
/*  940 */     FileInputStream fis = new FileInputStream(file);
/*  941 */     long length = fis.getChannel().size() / 4L;
/*  942 */     if (length > 2147483647L) {
/*  943 */       fis.close();
/*  944 */       throw new IllegalArgumentException("File too long: " + fis.getChannel().size() + " bytes (" + length + " elements)");
/*      */     } 
/*  946 */     int[] array = new int[(int)length];
/*  947 */     DataInputStream dis = new DataInputStream(new FastBufferedInputStream(fis));
/*  948 */     for (int i = 0; i < length; ) { array[i] = dis.readInt(); i++; }
/*  949 */      dis.close();
/*  950 */     return array;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int[] loadInts(CharSequence filename) throws IOException {
/*  962 */     return loadInts(new File(filename.toString()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeInts(int[] array, int offset, int length, DataOutput dataOutput) throws IOException {
/*  972 */     IntArrays.ensureOffsetLength(array, offset, length);
/*  973 */     for (int i = 0; i < length; ) { dataOutput.writeInt(array[offset + i]); i++; }
/*      */   
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeInts(int[] array, DataOutput dataOutput) throws IOException {
/*  981 */     int length = array.length;
/*  982 */     for (int i = 0; i < length; ) { dataOutput.writeInt(array[i]); i++; }
/*      */   
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeInts(int[] array, int offset, int length, File file) throws IOException {
/*  992 */     IntArrays.ensureOffsetLength(array, offset, length);
/*  993 */     DataOutputStream dos = new DataOutputStream(new FastBufferedOutputStream(new FileOutputStream(file)));
/*  994 */     for (int i = 0; i < length; ) { dos.writeInt(array[offset + i]); i++; }
/*  995 */      dos.close();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeInts(int[] array, int offset, int length, CharSequence filename) throws IOException {
/* 1005 */     storeInts(array, offset, length, new File(filename.toString()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeInts(int[] array, File file) throws IOException {
/* 1013 */     int length = array.length;
/* 1014 */     DataOutputStream dos = new DataOutputStream(new FastBufferedOutputStream(new FileOutputStream(file)));
/* 1015 */     for (int i = 0; i < length; ) { dos.writeInt(array[i]); i++; }
/* 1016 */      dos.close();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeInts(int[] array, CharSequence filename) throws IOException {
/* 1024 */     storeInts(array, new File(filename.toString()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long loadInts(DataInput dataInput, int[][] array, long offset, long length) throws IOException {
/* 1035 */     IntBigArrays.ensureOffsetLength(array, offset, length);
/* 1036 */     long c = 0L;
/*      */     try {
/* 1038 */       for (int i = BigArrays.segment(offset); i < BigArrays.segment(offset + length + 134217727L); i++) {
/* 1039 */         int[] t = array[i];
/* 1040 */         int l = (int)Math.min(t.length, offset + length - BigArrays.start(i));
/* 1041 */         for (int d = (int)Math.max(0L, offset - BigArrays.start(i)); d < l; d++) {
/* 1042 */           t[d] = dataInput.readInt();
/* 1043 */           c++;
/*      */         }
/*      */       
/*      */       } 
/* 1047 */     } catch (EOFException eOFException) {}
/* 1048 */     return c;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long loadInts(DataInput dataInput, int[][] array) throws IOException {
/* 1057 */     long c = 0L;
/*      */     try {
/* 1059 */       for (int i = 0; i < array.length; i++) {
/* 1060 */         int[] t = array[i];
/* 1061 */         int l = t.length;
/* 1062 */         for (int d = 0; d < l; d++) {
/* 1063 */           t[d] = dataInput.readInt();
/* 1064 */           c++;
/*      */         }
/*      */       
/*      */       } 
/* 1068 */     } catch (EOFException eOFException) {}
/* 1069 */     return c;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long loadInts(File file, int[][] array, long offset, long length) throws IOException {
/* 1080 */     IntBigArrays.ensureOffsetLength(array, offset, length);
/* 1081 */     FileInputStream fis = new FileInputStream(file);
/* 1082 */     DataInputStream dis = new DataInputStream(new FastBufferedInputStream(fis));
/* 1083 */     long c = 0L;
/*      */     try {
/* 1085 */       for (int i = BigArrays.segment(offset); i < BigArrays.segment(offset + length + 134217727L); i++) {
/* 1086 */         int[] t = array[i];
/* 1087 */         int l = (int)Math.min(t.length, offset + length - BigArrays.start(i));
/* 1088 */         for (int d = (int)Math.max(0L, offset - BigArrays.start(i)); d < l; d++) {
/* 1089 */           t[d] = dis.readInt();
/* 1090 */           c++;
/*      */         }
/*      */       
/*      */       } 
/* 1094 */     } catch (EOFException eOFException) {}
/* 1095 */     dis.close();
/* 1096 */     return c;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long loadInts(CharSequence filename, int[][] array, long offset, long length) throws IOException {
/* 1107 */     return loadInts(new File(filename.toString()), array, offset, length);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long loadInts(File file, int[][] array) throws IOException {
/* 1116 */     FileInputStream fis = new FileInputStream(file);
/* 1117 */     DataInputStream dis = new DataInputStream(new FastBufferedInputStream(fis));
/* 1118 */     long c = 0L;
/*      */     try {
/* 1120 */       for (int i = 0; i < array.length; i++) {
/* 1121 */         int[] t = array[i];
/* 1122 */         int l = t.length;
/* 1123 */         for (int d = 0; d < l; d++) {
/* 1124 */           t[d] = dis.readInt();
/* 1125 */           c++;
/*      */         }
/*      */       
/*      */       } 
/* 1129 */     } catch (EOFException eOFException) {}
/* 1130 */     dis.close();
/* 1131 */     return c;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long loadInts(CharSequence filename, int[][] array) throws IOException {
/* 1140 */     return loadInts(new File(filename.toString()), array);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int[][] loadIntsBig(File file) throws IOException {
/* 1152 */     FileInputStream fis = new FileInputStream(file);
/* 1153 */     long length = fis.getChannel().size() / 4L;
/* 1154 */     int[][] array = IntBigArrays.newBigArray(length);
/* 1155 */     DataInputStream dis = new DataInputStream(new FastBufferedInputStream(fis));
/* 1156 */     for (int i = 0; i < array.length; i++) {
/* 1157 */       int[] t = array[i];
/* 1158 */       int l = t.length;
/* 1159 */       for (int d = 0; d < l; ) { t[d] = dis.readInt(); d++; }
/*      */     
/* 1161 */     }  dis.close();
/* 1162 */     return array;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int[][] loadIntsBig(CharSequence filename) throws IOException {
/* 1174 */     return loadIntsBig(new File(filename.toString()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeInts(int[][] array, long offset, long length, DataOutput dataOutput) throws IOException {
/* 1184 */     IntBigArrays.ensureOffsetLength(array, offset, length);
/* 1185 */     for (int i = BigArrays.segment(offset); i < BigArrays.segment(offset + length + 134217727L); i++) {
/* 1186 */       int[] t = array[i];
/* 1187 */       int l = (int)Math.min(t.length, offset + length - BigArrays.start(i));
/* 1188 */       for (int d = (int)Math.max(0L, offset - BigArrays.start(i)); d < l; ) { dataOutput.writeInt(t[d]); d++; }
/*      */     
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeInts(int[][] array, DataOutput dataOutput) throws IOException {
/* 1197 */     for (int i = 0; i < array.length; i++) {
/* 1198 */       int[] t = array[i];
/* 1199 */       int l = t.length;
/* 1200 */       for (int d = 0; d < l; ) { dataOutput.writeInt(t[d]); d++; }
/*      */     
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeInts(int[][] array, long offset, long length, File file) throws IOException {
/* 1211 */     IntBigArrays.ensureOffsetLength(array, offset, length);
/* 1212 */     DataOutputStream dos = new DataOutputStream(new FastBufferedOutputStream(new FileOutputStream(file)));
/* 1213 */     for (int i = BigArrays.segment(offset); i < BigArrays.segment(offset + length + 134217727L); i++) {
/* 1214 */       int[] t = array[i];
/* 1215 */       int l = (int)Math.min(t.length, offset + length - BigArrays.start(i));
/* 1216 */       for (int d = (int)Math.max(0L, offset - BigArrays.start(i)); d < l; ) { dos.writeInt(t[d]); d++; }
/*      */     
/* 1218 */     }  dos.close();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeInts(int[][] array, long offset, long length, CharSequence filename) throws IOException {
/* 1228 */     storeInts(array, offset, length, new File(filename.toString()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeInts(int[][] array, File file) throws IOException {
/* 1236 */     DataOutputStream dos = new DataOutputStream(new FastBufferedOutputStream(new FileOutputStream(file)));
/* 1237 */     for (int i = 0; i < array.length; i++) {
/* 1238 */       int[] t = array[i];
/* 1239 */       int l = t.length;
/* 1240 */       for (int d = 0; d < l; ) { dos.writeInt(t[d]); d++; }
/*      */     
/* 1242 */     }  dos.close();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeInts(int[][] array, CharSequence filename) throws IOException {
/* 1250 */     storeInts(array, new File(filename.toString()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeInts(IntIterator i, DataOutput dataOutput) throws IOException {
/* 1258 */     for (; i.hasNext(); dataOutput.writeInt(i.nextInt()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeInts(IntIterator i, File file) throws IOException {
/* 1266 */     DataOutputStream dos = new DataOutputStream(new FastBufferedOutputStream(new FileOutputStream(file)));
/* 1267 */     for (; i.hasNext(); dos.writeInt(i.nextInt()));
/* 1268 */     dos.close();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeInts(IntIterator i, CharSequence filename) throws IOException {
/* 1276 */     storeInts(i, new File(filename.toString()));
/*      */   }
/*      */   
/*      */   private static final class IntDataInputWrapper implements IntIterator { private final DataInput dataInput;
/*      */     private boolean toAdvance = true;
/*      */     private boolean endOfProcess = false;
/*      */     private int next;
/*      */     
/*      */     public IntDataInputWrapper(DataInput dataInput) {
/* 1285 */       this.dataInput = dataInput;
/*      */     }
/*      */     
/*      */     public boolean hasNext() {
/* 1289 */       if (!this.toAdvance) return !this.endOfProcess; 
/* 1290 */       this.toAdvance = false; 
/* 1291 */       try { this.next = this.dataInput.readInt(); }
/* 1292 */       catch (EOFException eof) { this.endOfProcess = true; }
/* 1293 */       catch (IOException rethrow) { throw new RuntimeException(rethrow); }
/* 1294 */        return !this.endOfProcess;
/*      */     }
/*      */     
/*      */     public int nextInt() {
/* 1298 */       if (!hasNext()) throw new NoSuchElementException(); 
/* 1299 */       this.toAdvance = true;
/* 1300 */       return this.next;
/*      */     } }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static IntIterator asIntIterator(DataInput dataInput) {
/* 1308 */     return new IntDataInputWrapper(dataInput);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static IntIterator asIntIterator(File file) throws IOException {
/* 1315 */     return new IntDataInputWrapper(new DataInputStream(new FastBufferedInputStream(new FileInputStream(file))));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static IntIterator asIntIterator(CharSequence filename) throws IOException {
/* 1322 */     return asIntIterator(new File(filename.toString()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static IntIterable asIntIterable(File file) {
/* 1329 */     return () -> { try {
/*      */           return asIntIterator(file);
/* 1331 */         } catch (IOException e) {
/*      */           throw new RuntimeException(e);
/*      */         } 
/*      */       };
/*      */   }
/*      */ 
/*      */   
/*      */   public static IntIterable asIntIterable(CharSequence filename) {
/* 1339 */     return () -> { try {
/*      */           return asIntIterator(filename);
/* 1341 */         } catch (IOException e) {
/*      */           throw new RuntimeException(e);
/*      */         } 
/*      */       };
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int loadLongs(DataInput dataInput, long[] array, int offset, int length) throws IOException {
/* 1391 */     LongArrays.ensureOffsetLength(array, offset, length);
/* 1392 */     int i = 0;
/*      */     try {
/* 1394 */       for (i = 0; i < length; ) { array[i + offset] = dataInput.readLong(); i++; }
/*      */     
/* 1396 */     } catch (EOFException eOFException) {}
/* 1397 */     return i;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int loadLongs(DataInput dataInput, long[] array) throws IOException {
/* 1406 */     int i = 0;
/*      */     try {
/* 1408 */       int length = array.length;
/* 1409 */       for (i = 0; i < length; ) { array[i] = dataInput.readLong(); i++; }
/*      */     
/* 1411 */     } catch (EOFException eOFException) {}
/* 1412 */     return i;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int loadLongs(File file, long[] array, int offset, int length) throws IOException {
/* 1423 */     LongArrays.ensureOffsetLength(array, offset, length);
/* 1424 */     FileInputStream fis = new FileInputStream(file);
/* 1425 */     DataInputStream dis = new DataInputStream(new FastBufferedInputStream(fis));
/* 1426 */     int i = 0;
/*      */     try {
/* 1428 */       for (i = 0; i < length; ) { array[i + offset] = dis.readLong(); i++; }
/*      */     
/* 1430 */     } catch (EOFException eOFException) {}
/* 1431 */     dis.close();
/* 1432 */     return i;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int loadLongs(CharSequence filename, long[] array, int offset, int length) throws IOException {
/* 1443 */     return loadLongs(new File(filename.toString()), array, offset, length);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int loadLongs(File file, long[] array) throws IOException {
/* 1452 */     FileInputStream fis = new FileInputStream(file);
/* 1453 */     DataInputStream dis = new DataInputStream(new FastBufferedInputStream(fis));
/* 1454 */     int i = 0;
/*      */     try {
/* 1456 */       int length = array.length;
/* 1457 */       for (i = 0; i < length; ) { array[i] = dis.readLong(); i++; }
/*      */     
/* 1459 */     } catch (EOFException eOFException) {}
/* 1460 */     dis.close();
/* 1461 */     return i;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int loadLongs(CharSequence filename, long[] array) throws IOException {
/* 1470 */     return loadLongs(new File(filename.toString()), array);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long[] loadLongs(File file) throws IOException {
/* 1482 */     FileInputStream fis = new FileInputStream(file);
/* 1483 */     long length = fis.getChannel().size() / 8L;
/* 1484 */     if (length > 2147483647L) {
/* 1485 */       fis.close();
/* 1486 */       throw new IllegalArgumentException("File too long: " + fis.getChannel().size() + " bytes (" + length + " elements)");
/*      */     } 
/* 1488 */     long[] array = new long[(int)length];
/* 1489 */     DataInputStream dis = new DataInputStream(new FastBufferedInputStream(fis));
/* 1490 */     for (int i = 0; i < length; ) { array[i] = dis.readLong(); i++; }
/* 1491 */      dis.close();
/* 1492 */     return array;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long[] loadLongs(CharSequence filename) throws IOException {
/* 1504 */     return loadLongs(new File(filename.toString()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeLongs(long[] array, int offset, int length, DataOutput dataOutput) throws IOException {
/* 1514 */     LongArrays.ensureOffsetLength(array, offset, length);
/* 1515 */     for (int i = 0; i < length; ) { dataOutput.writeLong(array[offset + i]); i++; }
/*      */   
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeLongs(long[] array, DataOutput dataOutput) throws IOException {
/* 1523 */     int length = array.length;
/* 1524 */     for (int i = 0; i < length; ) { dataOutput.writeLong(array[i]); i++; }
/*      */   
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeLongs(long[] array, int offset, int length, File file) throws IOException {
/* 1534 */     LongArrays.ensureOffsetLength(array, offset, length);
/* 1535 */     DataOutputStream dos = new DataOutputStream(new FastBufferedOutputStream(new FileOutputStream(file)));
/* 1536 */     for (int i = 0; i < length; ) { dos.writeLong(array[offset + i]); i++; }
/* 1537 */      dos.close();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeLongs(long[] array, int offset, int length, CharSequence filename) throws IOException {
/* 1547 */     storeLongs(array, offset, length, new File(filename.toString()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeLongs(long[] array, File file) throws IOException {
/* 1555 */     int length = array.length;
/* 1556 */     DataOutputStream dos = new DataOutputStream(new FastBufferedOutputStream(new FileOutputStream(file)));
/* 1557 */     for (int i = 0; i < length; ) { dos.writeLong(array[i]); i++; }
/* 1558 */      dos.close();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeLongs(long[] array, CharSequence filename) throws IOException {
/* 1566 */     storeLongs(array, new File(filename.toString()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long loadLongs(DataInput dataInput, long[][] array, long offset, long length) throws IOException {
/* 1577 */     LongBigArrays.ensureOffsetLength(array, offset, length);
/* 1578 */     long c = 0L;
/*      */     try {
/* 1580 */       for (int i = BigArrays.segment(offset); i < BigArrays.segment(offset + length + 134217727L); i++) {
/* 1581 */         long[] t = array[i];
/* 1582 */         int l = (int)Math.min(t.length, offset + length - BigArrays.start(i));
/* 1583 */         for (int d = (int)Math.max(0L, offset - BigArrays.start(i)); d < l; d++) {
/* 1584 */           t[d] = dataInput.readLong();
/* 1585 */           c++;
/*      */         }
/*      */       
/*      */       } 
/* 1589 */     } catch (EOFException eOFException) {}
/* 1590 */     return c;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long loadLongs(DataInput dataInput, long[][] array) throws IOException {
/* 1599 */     long c = 0L;
/*      */     try {
/* 1601 */       for (int i = 0; i < array.length; i++) {
/* 1602 */         long[] t = array[i];
/* 1603 */         int l = t.length;
/* 1604 */         for (int d = 0; d < l; d++) {
/* 1605 */           t[d] = dataInput.readLong();
/* 1606 */           c++;
/*      */         }
/*      */       
/*      */       } 
/* 1610 */     } catch (EOFException eOFException) {}
/* 1611 */     return c;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long loadLongs(File file, long[][] array, long offset, long length) throws IOException {
/* 1622 */     LongBigArrays.ensureOffsetLength(array, offset, length);
/* 1623 */     FileInputStream fis = new FileInputStream(file);
/* 1624 */     DataInputStream dis = new DataInputStream(new FastBufferedInputStream(fis));
/* 1625 */     long c = 0L;
/*      */     try {
/* 1627 */       for (int i = BigArrays.segment(offset); i < BigArrays.segment(offset + length + 134217727L); i++) {
/* 1628 */         long[] t = array[i];
/* 1629 */         int l = (int)Math.min(t.length, offset + length - BigArrays.start(i));
/* 1630 */         for (int d = (int)Math.max(0L, offset - BigArrays.start(i)); d < l; d++) {
/* 1631 */           t[d] = dis.readLong();
/* 1632 */           c++;
/*      */         }
/*      */       
/*      */       } 
/* 1636 */     } catch (EOFException eOFException) {}
/* 1637 */     dis.close();
/* 1638 */     return c;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long loadLongs(CharSequence filename, long[][] array, long offset, long length) throws IOException {
/* 1649 */     return loadLongs(new File(filename.toString()), array, offset, length);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long loadLongs(File file, long[][] array) throws IOException {
/* 1658 */     FileInputStream fis = new FileInputStream(file);
/* 1659 */     DataInputStream dis = new DataInputStream(new FastBufferedInputStream(fis));
/* 1660 */     long c = 0L;
/*      */     try {
/* 1662 */       for (int i = 0; i < array.length; i++) {
/* 1663 */         long[] t = array[i];
/* 1664 */         int l = t.length;
/* 1665 */         for (int d = 0; d < l; d++) {
/* 1666 */           t[d] = dis.readLong();
/* 1667 */           c++;
/*      */         }
/*      */       
/*      */       } 
/* 1671 */     } catch (EOFException eOFException) {}
/* 1672 */     dis.close();
/* 1673 */     return c;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long loadLongs(CharSequence filename, long[][] array) throws IOException {
/* 1682 */     return loadLongs(new File(filename.toString()), array);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long[][] loadLongsBig(File file) throws IOException {
/* 1694 */     FileInputStream fis = new FileInputStream(file);
/* 1695 */     long length = fis.getChannel().size() / 8L;
/* 1696 */     long[][] array = LongBigArrays.newBigArray(length);
/* 1697 */     DataInputStream dis = new DataInputStream(new FastBufferedInputStream(fis));
/* 1698 */     for (int i = 0; i < array.length; i++) {
/* 1699 */       long[] t = array[i];
/* 1700 */       int l = t.length;
/* 1701 */       for (int d = 0; d < l; ) { t[d] = dis.readLong(); d++; }
/*      */     
/* 1703 */     }  dis.close();
/* 1704 */     return array;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long[][] loadLongsBig(CharSequence filename) throws IOException {
/* 1716 */     return loadLongsBig(new File(filename.toString()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeLongs(long[][] array, long offset, long length, DataOutput dataOutput) throws IOException {
/* 1726 */     LongBigArrays.ensureOffsetLength(array, offset, length);
/* 1727 */     for (int i = BigArrays.segment(offset); i < BigArrays.segment(offset + length + 134217727L); i++) {
/* 1728 */       long[] t = array[i];
/* 1729 */       int l = (int)Math.min(t.length, offset + length - BigArrays.start(i));
/* 1730 */       for (int d = (int)Math.max(0L, offset - BigArrays.start(i)); d < l; ) { dataOutput.writeLong(t[d]); d++; }
/*      */     
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeLongs(long[][] array, DataOutput dataOutput) throws IOException {
/* 1739 */     for (int i = 0; i < array.length; i++) {
/* 1740 */       long[] t = array[i];
/* 1741 */       int l = t.length;
/* 1742 */       for (int d = 0; d < l; ) { dataOutput.writeLong(t[d]); d++; }
/*      */     
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeLongs(long[][] array, long offset, long length, File file) throws IOException {
/* 1753 */     LongBigArrays.ensureOffsetLength(array, offset, length);
/* 1754 */     DataOutputStream dos = new DataOutputStream(new FastBufferedOutputStream(new FileOutputStream(file)));
/* 1755 */     for (int i = BigArrays.segment(offset); i < BigArrays.segment(offset + length + 134217727L); i++) {
/* 1756 */       long[] t = array[i];
/* 1757 */       int l = (int)Math.min(t.length, offset + length - BigArrays.start(i));
/* 1758 */       for (int d = (int)Math.max(0L, offset - BigArrays.start(i)); d < l; ) { dos.writeLong(t[d]); d++; }
/*      */     
/* 1760 */     }  dos.close();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeLongs(long[][] array, long offset, long length, CharSequence filename) throws IOException {
/* 1770 */     storeLongs(array, offset, length, new File(filename.toString()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeLongs(long[][] array, File file) throws IOException {
/* 1778 */     DataOutputStream dos = new DataOutputStream(new FastBufferedOutputStream(new FileOutputStream(file)));
/* 1779 */     for (int i = 0; i < array.length; i++) {
/* 1780 */       long[] t = array[i];
/* 1781 */       int l = t.length;
/* 1782 */       for (int d = 0; d < l; ) { dos.writeLong(t[d]); d++; }
/*      */     
/* 1784 */     }  dos.close();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeLongs(long[][] array, CharSequence filename) throws IOException {
/* 1792 */     storeLongs(array, new File(filename.toString()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeLongs(LongIterator i, DataOutput dataOutput) throws IOException {
/* 1800 */     for (; i.hasNext(); dataOutput.writeLong(i.nextLong()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeLongs(LongIterator i, File file) throws IOException {
/* 1808 */     DataOutputStream dos = new DataOutputStream(new FastBufferedOutputStream(new FileOutputStream(file)));
/* 1809 */     for (; i.hasNext(); dos.writeLong(i.nextLong()));
/* 1810 */     dos.close();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeLongs(LongIterator i, CharSequence filename) throws IOException {
/* 1818 */     storeLongs(i, new File(filename.toString()));
/*      */   }
/*      */   
/*      */   private static final class LongDataInputWrapper implements LongIterator { private final DataInput dataInput;
/*      */     private boolean toAdvance = true;
/*      */     private boolean endOfProcess = false;
/*      */     private long next;
/*      */     
/*      */     public LongDataInputWrapper(DataInput dataInput) {
/* 1827 */       this.dataInput = dataInput;
/*      */     }
/*      */     
/*      */     public boolean hasNext() {
/* 1831 */       if (!this.toAdvance) return !this.endOfProcess; 
/* 1832 */       this.toAdvance = false; 
/* 1833 */       try { this.next = this.dataInput.readLong(); }
/* 1834 */       catch (EOFException eof) { this.endOfProcess = true; }
/* 1835 */       catch (IOException rethrow) { throw new RuntimeException(rethrow); }
/* 1836 */        return !this.endOfProcess;
/*      */     }
/*      */     
/*      */     public long nextLong() {
/* 1840 */       if (!hasNext()) throw new NoSuchElementException(); 
/* 1841 */       this.toAdvance = true;
/* 1842 */       return this.next;
/*      */     } }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static LongIterator asLongIterator(DataInput dataInput) {
/* 1850 */     return new LongDataInputWrapper(dataInput);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static LongIterator asLongIterator(File file) throws IOException {
/* 1857 */     return new LongDataInputWrapper(new DataInputStream(new FastBufferedInputStream(new FileInputStream(file))));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static LongIterator asLongIterator(CharSequence filename) throws IOException {
/* 1864 */     return asLongIterator(new File(filename.toString()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static LongIterable asLongIterable(File file) {
/* 1871 */     return () -> { try {
/*      */           return asLongIterator(file);
/* 1873 */         } catch (IOException e) {
/*      */           throw new RuntimeException(e);
/*      */         } 
/*      */       };
/*      */   }
/*      */ 
/*      */   
/*      */   public static LongIterable asLongIterable(CharSequence filename) {
/* 1881 */     return () -> { try {
/*      */           return asLongIterator(filename);
/* 1883 */         } catch (IOException e) {
/*      */           throw new RuntimeException(e);
/*      */         } 
/*      */       };
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int loadDoubles(DataInput dataInput, double[] array, int offset, int length) throws IOException {
/* 1933 */     DoubleArrays.ensureOffsetLength(array, offset, length);
/* 1934 */     int i = 0;
/*      */     try {
/* 1936 */       for (i = 0; i < length; ) { array[i + offset] = dataInput.readDouble(); i++; }
/*      */     
/* 1938 */     } catch (EOFException eOFException) {}
/* 1939 */     return i;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int loadDoubles(DataInput dataInput, double[] array) throws IOException {
/* 1948 */     int i = 0;
/*      */     try {
/* 1950 */       int length = array.length;
/* 1951 */       for (i = 0; i < length; ) { array[i] = dataInput.readDouble(); i++; }
/*      */     
/* 1953 */     } catch (EOFException eOFException) {}
/* 1954 */     return i;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int loadDoubles(File file, double[] array, int offset, int length) throws IOException {
/* 1965 */     DoubleArrays.ensureOffsetLength(array, offset, length);
/* 1966 */     FileInputStream fis = new FileInputStream(file);
/* 1967 */     DataInputStream dis = new DataInputStream(new FastBufferedInputStream(fis));
/* 1968 */     int i = 0;
/*      */     try {
/* 1970 */       for (i = 0; i < length; ) { array[i + offset] = dis.readDouble(); i++; }
/*      */     
/* 1972 */     } catch (EOFException eOFException) {}
/* 1973 */     dis.close();
/* 1974 */     return i;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int loadDoubles(CharSequence filename, double[] array, int offset, int length) throws IOException {
/* 1985 */     return loadDoubles(new File(filename.toString()), array, offset, length);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int loadDoubles(File file, double[] array) throws IOException {
/* 1994 */     FileInputStream fis = new FileInputStream(file);
/* 1995 */     DataInputStream dis = new DataInputStream(new FastBufferedInputStream(fis));
/* 1996 */     int i = 0;
/*      */     try {
/* 1998 */       int length = array.length;
/* 1999 */       for (i = 0; i < length; ) { array[i] = dis.readDouble(); i++; }
/*      */     
/* 2001 */     } catch (EOFException eOFException) {}
/* 2002 */     dis.close();
/* 2003 */     return i;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int loadDoubles(CharSequence filename, double[] array) throws IOException {
/* 2012 */     return loadDoubles(new File(filename.toString()), array);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static double[] loadDoubles(File file) throws IOException {
/* 2024 */     FileInputStream fis = new FileInputStream(file);
/* 2025 */     long length = fis.getChannel().size() / 8L;
/* 2026 */     if (length > 2147483647L) {
/* 2027 */       fis.close();
/* 2028 */       throw new IllegalArgumentException("File too long: " + fis.getChannel().size() + " bytes (" + length + " elements)");
/*      */     } 
/* 2030 */     double[] array = new double[(int)length];
/* 2031 */     DataInputStream dis = new DataInputStream(new FastBufferedInputStream(fis));
/* 2032 */     for (int i = 0; i < length; ) { array[i] = dis.readDouble(); i++; }
/* 2033 */      dis.close();
/* 2034 */     return array;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static double[] loadDoubles(CharSequence filename) throws IOException {
/* 2046 */     return loadDoubles(new File(filename.toString()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeDoubles(double[] array, int offset, int length, DataOutput dataOutput) throws IOException {
/* 2056 */     DoubleArrays.ensureOffsetLength(array, offset, length);
/* 2057 */     for (int i = 0; i < length; ) { dataOutput.writeDouble(array[offset + i]); i++; }
/*      */   
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeDoubles(double[] array, DataOutput dataOutput) throws IOException {
/* 2065 */     int length = array.length;
/* 2066 */     for (int i = 0; i < length; ) { dataOutput.writeDouble(array[i]); i++; }
/*      */   
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeDoubles(double[] array, int offset, int length, File file) throws IOException {
/* 2076 */     DoubleArrays.ensureOffsetLength(array, offset, length);
/* 2077 */     DataOutputStream dos = new DataOutputStream(new FastBufferedOutputStream(new FileOutputStream(file)));
/* 2078 */     for (int i = 0; i < length; ) { dos.writeDouble(array[offset + i]); i++; }
/* 2079 */      dos.close();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeDoubles(double[] array, int offset, int length, CharSequence filename) throws IOException {
/* 2089 */     storeDoubles(array, offset, length, new File(filename.toString()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeDoubles(double[] array, File file) throws IOException {
/* 2097 */     int length = array.length;
/* 2098 */     DataOutputStream dos = new DataOutputStream(new FastBufferedOutputStream(new FileOutputStream(file)));
/* 2099 */     for (int i = 0; i < length; ) { dos.writeDouble(array[i]); i++; }
/* 2100 */      dos.close();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeDoubles(double[] array, CharSequence filename) throws IOException {
/* 2108 */     storeDoubles(array, new File(filename.toString()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long loadDoubles(DataInput dataInput, double[][] array, long offset, long length) throws IOException {
/* 2119 */     DoubleBigArrays.ensureOffsetLength(array, offset, length);
/* 2120 */     long c = 0L;
/*      */     try {
/* 2122 */       for (int i = BigArrays.segment(offset); i < BigArrays.segment(offset + length + 134217727L); i++) {
/* 2123 */         double[] t = array[i];
/* 2124 */         int l = (int)Math.min(t.length, offset + length - BigArrays.start(i));
/* 2125 */         for (int d = (int)Math.max(0L, offset - BigArrays.start(i)); d < l; d++) {
/* 2126 */           t[d] = dataInput.readDouble();
/* 2127 */           c++;
/*      */         }
/*      */       
/*      */       } 
/* 2131 */     } catch (EOFException eOFException) {}
/* 2132 */     return c;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long loadDoubles(DataInput dataInput, double[][] array) throws IOException {
/* 2141 */     long c = 0L;
/*      */     try {
/* 2143 */       for (int i = 0; i < array.length; i++) {
/* 2144 */         double[] t = array[i];
/* 2145 */         int l = t.length;
/* 2146 */         for (int d = 0; d < l; d++) {
/* 2147 */           t[d] = dataInput.readDouble();
/* 2148 */           c++;
/*      */         }
/*      */       
/*      */       } 
/* 2152 */     } catch (EOFException eOFException) {}
/* 2153 */     return c;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long loadDoubles(File file, double[][] array, long offset, long length) throws IOException {
/* 2164 */     DoubleBigArrays.ensureOffsetLength(array, offset, length);
/* 2165 */     FileInputStream fis = new FileInputStream(file);
/* 2166 */     DataInputStream dis = new DataInputStream(new FastBufferedInputStream(fis));
/* 2167 */     long c = 0L;
/*      */     try {
/* 2169 */       for (int i = BigArrays.segment(offset); i < BigArrays.segment(offset + length + 134217727L); i++) {
/* 2170 */         double[] t = array[i];
/* 2171 */         int l = (int)Math.min(t.length, offset + length - BigArrays.start(i));
/* 2172 */         for (int d = (int)Math.max(0L, offset - BigArrays.start(i)); d < l; d++) {
/* 2173 */           t[d] = dis.readDouble();
/* 2174 */           c++;
/*      */         }
/*      */       
/*      */       } 
/* 2178 */     } catch (EOFException eOFException) {}
/* 2179 */     dis.close();
/* 2180 */     return c;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long loadDoubles(CharSequence filename, double[][] array, long offset, long length) throws IOException {
/* 2191 */     return loadDoubles(new File(filename.toString()), array, offset, length);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long loadDoubles(File file, double[][] array) throws IOException {
/* 2200 */     FileInputStream fis = new FileInputStream(file);
/* 2201 */     DataInputStream dis = new DataInputStream(new FastBufferedInputStream(fis));
/* 2202 */     long c = 0L;
/*      */     try {
/* 2204 */       for (int i = 0; i < array.length; i++) {
/* 2205 */         double[] t = array[i];
/* 2206 */         int l = t.length;
/* 2207 */         for (int d = 0; d < l; d++) {
/* 2208 */           t[d] = dis.readDouble();
/* 2209 */           c++;
/*      */         }
/*      */       
/*      */       } 
/* 2213 */     } catch (EOFException eOFException) {}
/* 2214 */     dis.close();
/* 2215 */     return c;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long loadDoubles(CharSequence filename, double[][] array) throws IOException {
/* 2224 */     return loadDoubles(new File(filename.toString()), array);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static double[][] loadDoublesBig(File file) throws IOException {
/* 2236 */     FileInputStream fis = new FileInputStream(file);
/* 2237 */     long length = fis.getChannel().size() / 8L;
/* 2238 */     double[][] array = DoubleBigArrays.newBigArray(length);
/* 2239 */     DataInputStream dis = new DataInputStream(new FastBufferedInputStream(fis));
/* 2240 */     for (int i = 0; i < array.length; i++) {
/* 2241 */       double[] t = array[i];
/* 2242 */       int l = t.length;
/* 2243 */       for (int d = 0; d < l; ) { t[d] = dis.readDouble(); d++; }
/*      */     
/* 2245 */     }  dis.close();
/* 2246 */     return array;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static double[][] loadDoublesBig(CharSequence filename) throws IOException {
/* 2258 */     return loadDoublesBig(new File(filename.toString()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeDoubles(double[][] array, long offset, long length, DataOutput dataOutput) throws IOException {
/* 2268 */     DoubleBigArrays.ensureOffsetLength(array, offset, length);
/* 2269 */     for (int i = BigArrays.segment(offset); i < BigArrays.segment(offset + length + 134217727L); i++) {
/* 2270 */       double[] t = array[i];
/* 2271 */       int l = (int)Math.min(t.length, offset + length - BigArrays.start(i));
/* 2272 */       for (int d = (int)Math.max(0L, offset - BigArrays.start(i)); d < l; ) { dataOutput.writeDouble(t[d]); d++; }
/*      */     
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeDoubles(double[][] array, DataOutput dataOutput) throws IOException {
/* 2281 */     for (int i = 0; i < array.length; i++) {
/* 2282 */       double[] t = array[i];
/* 2283 */       int l = t.length;
/* 2284 */       for (int d = 0; d < l; ) { dataOutput.writeDouble(t[d]); d++; }
/*      */     
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeDoubles(double[][] array, long offset, long length, File file) throws IOException {
/* 2295 */     DoubleBigArrays.ensureOffsetLength(array, offset, length);
/* 2296 */     DataOutputStream dos = new DataOutputStream(new FastBufferedOutputStream(new FileOutputStream(file)));
/* 2297 */     for (int i = BigArrays.segment(offset); i < BigArrays.segment(offset + length + 134217727L); i++) {
/* 2298 */       double[] t = array[i];
/* 2299 */       int l = (int)Math.min(t.length, offset + length - BigArrays.start(i));
/* 2300 */       for (int d = (int)Math.max(0L, offset - BigArrays.start(i)); d < l; ) { dos.writeDouble(t[d]); d++; }
/*      */     
/* 2302 */     }  dos.close();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeDoubles(double[][] array, long offset, long length, CharSequence filename) throws IOException {
/* 2312 */     storeDoubles(array, offset, length, new File(filename.toString()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeDoubles(double[][] array, File file) throws IOException {
/* 2320 */     DataOutputStream dos = new DataOutputStream(new FastBufferedOutputStream(new FileOutputStream(file)));
/* 2321 */     for (int i = 0; i < array.length; i++) {
/* 2322 */       double[] t = array[i];
/* 2323 */       int l = t.length;
/* 2324 */       for (int d = 0; d < l; ) { dos.writeDouble(t[d]); d++; }
/*      */     
/* 2326 */     }  dos.close();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeDoubles(double[][] array, CharSequence filename) throws IOException {
/* 2334 */     storeDoubles(array, new File(filename.toString()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeDoubles(DoubleIterator i, DataOutput dataOutput) throws IOException {
/* 2342 */     for (; i.hasNext(); dataOutput.writeDouble(i.nextDouble()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeDoubles(DoubleIterator i, File file) throws IOException {
/* 2350 */     DataOutputStream dos = new DataOutputStream(new FastBufferedOutputStream(new FileOutputStream(file)));
/* 2351 */     for (; i.hasNext(); dos.writeDouble(i.nextDouble()));
/* 2352 */     dos.close();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeDoubles(DoubleIterator i, CharSequence filename) throws IOException {
/* 2360 */     storeDoubles(i, new File(filename.toString()));
/*      */   }
/*      */   
/*      */   private static final class DoubleDataInputWrapper implements DoubleIterator { private final DataInput dataInput;
/*      */     private boolean toAdvance = true;
/*      */     private boolean endOfProcess = false;
/*      */     private double next;
/*      */     
/*      */     public DoubleDataInputWrapper(DataInput dataInput) {
/* 2369 */       this.dataInput = dataInput;
/*      */     }
/*      */     
/*      */     public boolean hasNext() {
/* 2373 */       if (!this.toAdvance) return !this.endOfProcess; 
/* 2374 */       this.toAdvance = false; 
/* 2375 */       try { this.next = this.dataInput.readDouble(); }
/* 2376 */       catch (EOFException eof) { this.endOfProcess = true; }
/* 2377 */       catch (IOException rethrow) { throw new RuntimeException(rethrow); }
/* 2378 */        return !this.endOfProcess;
/*      */     }
/*      */     
/*      */     public double nextDouble() {
/* 2382 */       if (!hasNext()) throw new NoSuchElementException(); 
/* 2383 */       this.toAdvance = true;
/* 2384 */       return this.next;
/*      */     } }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static DoubleIterator asDoubleIterator(DataInput dataInput) {
/* 2392 */     return new DoubleDataInputWrapper(dataInput);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static DoubleIterator asDoubleIterator(File file) throws IOException {
/* 2399 */     return new DoubleDataInputWrapper(new DataInputStream(new FastBufferedInputStream(new FileInputStream(file))));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static DoubleIterator asDoubleIterator(CharSequence filename) throws IOException {
/* 2406 */     return asDoubleIterator(new File(filename.toString()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static DoubleIterable asDoubleIterable(File file) {
/* 2413 */     return () -> { try {
/*      */           return asDoubleIterator(file);
/* 2415 */         } catch (IOException e) {
/*      */           throw new RuntimeException(e);
/*      */         } 
/*      */       };
/*      */   }
/*      */ 
/*      */   
/*      */   public static DoubleIterable asDoubleIterable(CharSequence filename) {
/* 2423 */     return () -> { try {
/*      */           return asDoubleIterator(filename);
/* 2425 */         } catch (IOException e) {
/*      */           throw new RuntimeException(e);
/*      */         } 
/*      */       };
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int loadBooleans(DataInput dataInput, boolean[] array, int offset, int length) throws IOException {
/* 2475 */     BooleanArrays.ensureOffsetLength(array, offset, length);
/* 2476 */     int i = 0;
/*      */     try {
/* 2478 */       for (i = 0; i < length; ) { array[i + offset] = dataInput.readBoolean(); i++; }
/*      */     
/* 2480 */     } catch (EOFException eOFException) {}
/* 2481 */     return i;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int loadBooleans(DataInput dataInput, boolean[] array) throws IOException {
/* 2490 */     int i = 0;
/*      */     try {
/* 2492 */       int length = array.length;
/* 2493 */       for (i = 0; i < length; ) { array[i] = dataInput.readBoolean(); i++; }
/*      */     
/* 2495 */     } catch (EOFException eOFException) {}
/* 2496 */     return i;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int loadBooleans(File file, boolean[] array, int offset, int length) throws IOException {
/* 2507 */     BooleanArrays.ensureOffsetLength(array, offset, length);
/* 2508 */     FileInputStream fis = new FileInputStream(file);
/* 2509 */     DataInputStream dis = new DataInputStream(new FastBufferedInputStream(fis));
/* 2510 */     int i = 0;
/*      */     try {
/* 2512 */       for (i = 0; i < length; ) { array[i + offset] = dis.readBoolean(); i++; }
/*      */     
/* 2514 */     } catch (EOFException eOFException) {}
/* 2515 */     dis.close();
/* 2516 */     return i;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int loadBooleans(CharSequence filename, boolean[] array, int offset, int length) throws IOException {
/* 2527 */     return loadBooleans(new File(filename.toString()), array, offset, length);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int loadBooleans(File file, boolean[] array) throws IOException {
/* 2536 */     FileInputStream fis = new FileInputStream(file);
/* 2537 */     DataInputStream dis = new DataInputStream(new FastBufferedInputStream(fis));
/* 2538 */     int i = 0;
/*      */     try {
/* 2540 */       int length = array.length;
/* 2541 */       for (i = 0; i < length; ) { array[i] = dis.readBoolean(); i++; }
/*      */     
/* 2543 */     } catch (EOFException eOFException) {}
/* 2544 */     dis.close();
/* 2545 */     return i;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int loadBooleans(CharSequence filename, boolean[] array) throws IOException {
/* 2554 */     return loadBooleans(new File(filename.toString()), array);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean[] loadBooleans(File file) throws IOException {
/* 2566 */     FileInputStream fis = new FileInputStream(file);
/* 2567 */     long length = fis.getChannel().size();
/* 2568 */     if (length > 2147483647L) {
/* 2569 */       fis.close();
/* 2570 */       throw new IllegalArgumentException("File too long: " + fis.getChannel().size() + " bytes (" + length + " elements)");
/*      */     } 
/* 2572 */     boolean[] array = new boolean[(int)length];
/* 2573 */     DataInputStream dis = new DataInputStream(new FastBufferedInputStream(fis));
/* 2574 */     for (int i = 0; i < length; ) { array[i] = dis.readBoolean(); i++; }
/* 2575 */      dis.close();
/* 2576 */     return array;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean[] loadBooleans(CharSequence filename) throws IOException {
/* 2588 */     return loadBooleans(new File(filename.toString()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeBooleans(boolean[] array, int offset, int length, DataOutput dataOutput) throws IOException {
/* 2598 */     BooleanArrays.ensureOffsetLength(array, offset, length);
/* 2599 */     for (int i = 0; i < length; ) { dataOutput.writeBoolean(array[offset + i]); i++; }
/*      */   
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeBooleans(boolean[] array, DataOutput dataOutput) throws IOException {
/* 2607 */     int length = array.length;
/* 2608 */     for (int i = 0; i < length; ) { dataOutput.writeBoolean(array[i]); i++; }
/*      */   
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeBooleans(boolean[] array, int offset, int length, File file) throws IOException {
/* 2618 */     BooleanArrays.ensureOffsetLength(array, offset, length);
/* 2619 */     DataOutputStream dos = new DataOutputStream(new FastBufferedOutputStream(new FileOutputStream(file)));
/* 2620 */     for (int i = 0; i < length; ) { dos.writeBoolean(array[offset + i]); i++; }
/* 2621 */      dos.close();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeBooleans(boolean[] array, int offset, int length, CharSequence filename) throws IOException {
/* 2631 */     storeBooleans(array, offset, length, new File(filename.toString()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeBooleans(boolean[] array, File file) throws IOException {
/* 2639 */     int length = array.length;
/* 2640 */     DataOutputStream dos = new DataOutputStream(new FastBufferedOutputStream(new FileOutputStream(file)));
/* 2641 */     for (int i = 0; i < length; ) { dos.writeBoolean(array[i]); i++; }
/* 2642 */      dos.close();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeBooleans(boolean[] array, CharSequence filename) throws IOException {
/* 2650 */     storeBooleans(array, new File(filename.toString()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long loadBooleans(DataInput dataInput, boolean[][] array, long offset, long length) throws IOException {
/* 2661 */     BooleanBigArrays.ensureOffsetLength(array, offset, length);
/* 2662 */     long c = 0L;
/*      */     try {
/* 2664 */       for (int i = BigArrays.segment(offset); i < BigArrays.segment(offset + length + 134217727L); i++) {
/* 2665 */         boolean[] t = array[i];
/* 2666 */         int l = (int)Math.min(t.length, offset + length - BigArrays.start(i));
/* 2667 */         for (int d = (int)Math.max(0L, offset - BigArrays.start(i)); d < l; d++) {
/* 2668 */           t[d] = dataInput.readBoolean();
/* 2669 */           c++;
/*      */         }
/*      */       
/*      */       } 
/* 2673 */     } catch (EOFException eOFException) {}
/* 2674 */     return c;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long loadBooleans(DataInput dataInput, boolean[][] array) throws IOException {
/* 2683 */     long c = 0L;
/*      */     try {
/* 2685 */       for (int i = 0; i < array.length; i++) {
/* 2686 */         boolean[] t = array[i];
/* 2687 */         int l = t.length;
/* 2688 */         for (int d = 0; d < l; d++) {
/* 2689 */           t[d] = dataInput.readBoolean();
/* 2690 */           c++;
/*      */         }
/*      */       
/*      */       } 
/* 2694 */     } catch (EOFException eOFException) {}
/* 2695 */     return c;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long loadBooleans(File file, boolean[][] array, long offset, long length) throws IOException {
/* 2706 */     BooleanBigArrays.ensureOffsetLength(array, offset, length);
/* 2707 */     FileInputStream fis = new FileInputStream(file);
/* 2708 */     DataInputStream dis = new DataInputStream(new FastBufferedInputStream(fis));
/* 2709 */     long c = 0L;
/*      */     try {
/* 2711 */       for (int i = BigArrays.segment(offset); i < BigArrays.segment(offset + length + 134217727L); i++) {
/* 2712 */         boolean[] t = array[i];
/* 2713 */         int l = (int)Math.min(t.length, offset + length - BigArrays.start(i));
/* 2714 */         for (int d = (int)Math.max(0L, offset - BigArrays.start(i)); d < l; d++) {
/* 2715 */           t[d] = dis.readBoolean();
/* 2716 */           c++;
/*      */         }
/*      */       
/*      */       } 
/* 2720 */     } catch (EOFException eOFException) {}
/* 2721 */     dis.close();
/* 2722 */     return c;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long loadBooleans(CharSequence filename, boolean[][] array, long offset, long length) throws IOException {
/* 2733 */     return loadBooleans(new File(filename.toString()), array, offset, length);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long loadBooleans(File file, boolean[][] array) throws IOException {
/* 2742 */     FileInputStream fis = new FileInputStream(file);
/* 2743 */     DataInputStream dis = new DataInputStream(new FastBufferedInputStream(fis));
/* 2744 */     long c = 0L;
/*      */     try {
/* 2746 */       for (int i = 0; i < array.length; i++) {
/* 2747 */         boolean[] t = array[i];
/* 2748 */         int l = t.length;
/* 2749 */         for (int d = 0; d < l; d++) {
/* 2750 */           t[d] = dis.readBoolean();
/* 2751 */           c++;
/*      */         }
/*      */       
/*      */       } 
/* 2755 */     } catch (EOFException eOFException) {}
/* 2756 */     dis.close();
/* 2757 */     return c;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long loadBooleans(CharSequence filename, boolean[][] array) throws IOException {
/* 2766 */     return loadBooleans(new File(filename.toString()), array);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean[][] loadBooleansBig(File file) throws IOException {
/* 2778 */     FileInputStream fis = new FileInputStream(file);
/* 2779 */     long length = fis.getChannel().size();
/* 2780 */     boolean[][] array = BooleanBigArrays.newBigArray(length);
/* 2781 */     DataInputStream dis = new DataInputStream(new FastBufferedInputStream(fis));
/* 2782 */     for (int i = 0; i < array.length; i++) {
/* 2783 */       boolean[] t = array[i];
/* 2784 */       int l = t.length;
/* 2785 */       for (int d = 0; d < l; ) { t[d] = dis.readBoolean(); d++; }
/*      */     
/* 2787 */     }  dis.close();
/* 2788 */     return array;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean[][] loadBooleansBig(CharSequence filename) throws IOException {
/* 2800 */     return loadBooleansBig(new File(filename.toString()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeBooleans(boolean[][] array, long offset, long length, DataOutput dataOutput) throws IOException {
/* 2810 */     BooleanBigArrays.ensureOffsetLength(array, offset, length);
/* 2811 */     for (int i = BigArrays.segment(offset); i < BigArrays.segment(offset + length + 134217727L); i++) {
/* 2812 */       boolean[] t = array[i];
/* 2813 */       int l = (int)Math.min(t.length, offset + length - BigArrays.start(i));
/* 2814 */       for (int d = (int)Math.max(0L, offset - BigArrays.start(i)); d < l; ) { dataOutput.writeBoolean(t[d]); d++; }
/*      */     
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeBooleans(boolean[][] array, DataOutput dataOutput) throws IOException {
/* 2823 */     for (int i = 0; i < array.length; i++) {
/* 2824 */       boolean[] t = array[i];
/* 2825 */       int l = t.length;
/* 2826 */       for (int d = 0; d < l; ) { dataOutput.writeBoolean(t[d]); d++; }
/*      */     
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeBooleans(boolean[][] array, long offset, long length, File file) throws IOException {
/* 2837 */     BooleanBigArrays.ensureOffsetLength(array, offset, length);
/* 2838 */     DataOutputStream dos = new DataOutputStream(new FastBufferedOutputStream(new FileOutputStream(file)));
/* 2839 */     for (int i = BigArrays.segment(offset); i < BigArrays.segment(offset + length + 134217727L); i++) {
/* 2840 */       boolean[] t = array[i];
/* 2841 */       int l = (int)Math.min(t.length, offset + length - BigArrays.start(i));
/* 2842 */       for (int d = (int)Math.max(0L, offset - BigArrays.start(i)); d < l; ) { dos.writeBoolean(t[d]); d++; }
/*      */     
/* 2844 */     }  dos.close();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeBooleans(boolean[][] array, long offset, long length, CharSequence filename) throws IOException {
/* 2854 */     storeBooleans(array, offset, length, new File(filename.toString()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeBooleans(boolean[][] array, File file) throws IOException {
/* 2862 */     DataOutputStream dos = new DataOutputStream(new FastBufferedOutputStream(new FileOutputStream(file)));
/* 2863 */     for (int i = 0; i < array.length; i++) {
/* 2864 */       boolean[] t = array[i];
/* 2865 */       int l = t.length;
/* 2866 */       for (int d = 0; d < l; ) { dos.writeBoolean(t[d]); d++; }
/*      */     
/* 2868 */     }  dos.close();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeBooleans(boolean[][] array, CharSequence filename) throws IOException {
/* 2876 */     storeBooleans(array, new File(filename.toString()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeBooleans(BooleanIterator i, DataOutput dataOutput) throws IOException {
/* 2884 */     for (; i.hasNext(); dataOutput.writeBoolean(i.nextBoolean()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeBooleans(BooleanIterator i, File file) throws IOException {
/* 2892 */     DataOutputStream dos = new DataOutputStream(new FastBufferedOutputStream(new FileOutputStream(file)));
/* 2893 */     for (; i.hasNext(); dos.writeBoolean(i.nextBoolean()));
/* 2894 */     dos.close();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeBooleans(BooleanIterator i, CharSequence filename) throws IOException {
/* 2902 */     storeBooleans(i, new File(filename.toString()));
/*      */   }
/*      */   
/*      */   private static final class BooleanDataInputWrapper implements BooleanIterator { private final DataInput dataInput;
/*      */     private boolean toAdvance = true;
/*      */     private boolean endOfProcess = false;
/*      */     private boolean next;
/*      */     
/*      */     public BooleanDataInputWrapper(DataInput dataInput) {
/* 2911 */       this.dataInput = dataInput;
/*      */     }
/*      */     
/*      */     public boolean hasNext() {
/* 2915 */       if (!this.toAdvance) return !this.endOfProcess; 
/* 2916 */       this.toAdvance = false; 
/* 2917 */       try { this.next = this.dataInput.readBoolean(); }
/* 2918 */       catch (EOFException eof) { this.endOfProcess = true; }
/* 2919 */       catch (IOException rethrow) { throw new RuntimeException(rethrow); }
/* 2920 */        return !this.endOfProcess;
/*      */     }
/*      */     
/*      */     public boolean nextBoolean() {
/* 2924 */       if (!hasNext()) throw new NoSuchElementException(); 
/* 2925 */       this.toAdvance = true;
/* 2926 */       return this.next;
/*      */     } }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static BooleanIterator asBooleanIterator(DataInput dataInput) {
/* 2934 */     return new BooleanDataInputWrapper(dataInput);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static BooleanIterator asBooleanIterator(File file) throws IOException {
/* 2941 */     return new BooleanDataInputWrapper(new DataInputStream(new FastBufferedInputStream(new FileInputStream(file))));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static BooleanIterator asBooleanIterator(CharSequence filename) throws IOException {
/* 2948 */     return asBooleanIterator(new File(filename.toString()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static BooleanIterable asBooleanIterable(File file) {
/* 2955 */     return () -> { try {
/*      */           return asBooleanIterator(file);
/* 2957 */         } catch (IOException e) {
/*      */           throw new RuntimeException(e);
/*      */         } 
/*      */       };
/*      */   }
/*      */ 
/*      */   
/*      */   public static BooleanIterable asBooleanIterable(CharSequence filename) {
/* 2965 */     return () -> { try {
/*      */           return asBooleanIterator(filename);
/* 2967 */         } catch (IOException e) {
/*      */           throw new RuntimeException(e);
/*      */         } 
/*      */       };
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int loadShorts(DataInput dataInput, short[] array, int offset, int length) throws IOException {
/* 3017 */     ShortArrays.ensureOffsetLength(array, offset, length);
/* 3018 */     int i = 0;
/*      */     try {
/* 3020 */       for (i = 0; i < length; ) { array[i + offset] = dataInput.readShort(); i++; }
/*      */     
/* 3022 */     } catch (EOFException eOFException) {}
/* 3023 */     return i;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int loadShorts(DataInput dataInput, short[] array) throws IOException {
/* 3032 */     int i = 0;
/*      */     try {
/* 3034 */       int length = array.length;
/* 3035 */       for (i = 0; i < length; ) { array[i] = dataInput.readShort(); i++; }
/*      */     
/* 3037 */     } catch (EOFException eOFException) {}
/* 3038 */     return i;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int loadShorts(File file, short[] array, int offset, int length) throws IOException {
/* 3049 */     ShortArrays.ensureOffsetLength(array, offset, length);
/* 3050 */     FileInputStream fis = new FileInputStream(file);
/* 3051 */     DataInputStream dis = new DataInputStream(new FastBufferedInputStream(fis));
/* 3052 */     int i = 0;
/*      */     try {
/* 3054 */       for (i = 0; i < length; ) { array[i + offset] = dis.readShort(); i++; }
/*      */     
/* 3056 */     } catch (EOFException eOFException) {}
/* 3057 */     dis.close();
/* 3058 */     return i;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int loadShorts(CharSequence filename, short[] array, int offset, int length) throws IOException {
/* 3069 */     return loadShorts(new File(filename.toString()), array, offset, length);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int loadShorts(File file, short[] array) throws IOException {
/* 3078 */     FileInputStream fis = new FileInputStream(file);
/* 3079 */     DataInputStream dis = new DataInputStream(new FastBufferedInputStream(fis));
/* 3080 */     int i = 0;
/*      */     try {
/* 3082 */       int length = array.length;
/* 3083 */       for (i = 0; i < length; ) { array[i] = dis.readShort(); i++; }
/*      */     
/* 3085 */     } catch (EOFException eOFException) {}
/* 3086 */     dis.close();
/* 3087 */     return i;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int loadShorts(CharSequence filename, short[] array) throws IOException {
/* 3096 */     return loadShorts(new File(filename.toString()), array);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static short[] loadShorts(File file) throws IOException {
/* 3108 */     FileInputStream fis = new FileInputStream(file);
/* 3109 */     long length = fis.getChannel().size() / 2L;
/* 3110 */     if (length > 2147483647L) {
/* 3111 */       fis.close();
/* 3112 */       throw new IllegalArgumentException("File too long: " + fis.getChannel().size() + " bytes (" + length + " elements)");
/*      */     } 
/* 3114 */     short[] array = new short[(int)length];
/* 3115 */     DataInputStream dis = new DataInputStream(new FastBufferedInputStream(fis));
/* 3116 */     for (int i = 0; i < length; ) { array[i] = dis.readShort(); i++; }
/* 3117 */      dis.close();
/* 3118 */     return array;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static short[] loadShorts(CharSequence filename) throws IOException {
/* 3130 */     return loadShorts(new File(filename.toString()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeShorts(short[] array, int offset, int length, DataOutput dataOutput) throws IOException {
/* 3140 */     ShortArrays.ensureOffsetLength(array, offset, length);
/* 3141 */     for (int i = 0; i < length; ) { dataOutput.writeShort(array[offset + i]); i++; }
/*      */   
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeShorts(short[] array, DataOutput dataOutput) throws IOException {
/* 3149 */     int length = array.length;
/* 3150 */     for (int i = 0; i < length; ) { dataOutput.writeShort(array[i]); i++; }
/*      */   
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeShorts(short[] array, int offset, int length, File file) throws IOException {
/* 3160 */     ShortArrays.ensureOffsetLength(array, offset, length);
/* 3161 */     DataOutputStream dos = new DataOutputStream(new FastBufferedOutputStream(new FileOutputStream(file)));
/* 3162 */     for (int i = 0; i < length; ) { dos.writeShort(array[offset + i]); i++; }
/* 3163 */      dos.close();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeShorts(short[] array, int offset, int length, CharSequence filename) throws IOException {
/* 3173 */     storeShorts(array, offset, length, new File(filename.toString()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeShorts(short[] array, File file) throws IOException {
/* 3181 */     int length = array.length;
/* 3182 */     DataOutputStream dos = new DataOutputStream(new FastBufferedOutputStream(new FileOutputStream(file)));
/* 3183 */     for (int i = 0; i < length; ) { dos.writeShort(array[i]); i++; }
/* 3184 */      dos.close();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeShorts(short[] array, CharSequence filename) throws IOException {
/* 3192 */     storeShorts(array, new File(filename.toString()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long loadShorts(DataInput dataInput, short[][] array, long offset, long length) throws IOException {
/* 3203 */     ShortBigArrays.ensureOffsetLength(array, offset, length);
/* 3204 */     long c = 0L;
/*      */     try {
/* 3206 */       for (int i = BigArrays.segment(offset); i < BigArrays.segment(offset + length + 134217727L); i++) {
/* 3207 */         short[] t = array[i];
/* 3208 */         int l = (int)Math.min(t.length, offset + length - BigArrays.start(i));
/* 3209 */         for (int d = (int)Math.max(0L, offset - BigArrays.start(i)); d < l; d++) {
/* 3210 */           t[d] = dataInput.readShort();
/* 3211 */           c++;
/*      */         }
/*      */       
/*      */       } 
/* 3215 */     } catch (EOFException eOFException) {}
/* 3216 */     return c;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long loadShorts(DataInput dataInput, short[][] array) throws IOException {
/* 3225 */     long c = 0L;
/*      */     try {
/* 3227 */       for (int i = 0; i < array.length; i++) {
/* 3228 */         short[] t = array[i];
/* 3229 */         int l = t.length;
/* 3230 */         for (int d = 0; d < l; d++) {
/* 3231 */           t[d] = dataInput.readShort();
/* 3232 */           c++;
/*      */         }
/*      */       
/*      */       } 
/* 3236 */     } catch (EOFException eOFException) {}
/* 3237 */     return c;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long loadShorts(File file, short[][] array, long offset, long length) throws IOException {
/* 3248 */     ShortBigArrays.ensureOffsetLength(array, offset, length);
/* 3249 */     FileInputStream fis = new FileInputStream(file);
/* 3250 */     DataInputStream dis = new DataInputStream(new FastBufferedInputStream(fis));
/* 3251 */     long c = 0L;
/*      */     try {
/* 3253 */       for (int i = BigArrays.segment(offset); i < BigArrays.segment(offset + length + 134217727L); i++) {
/* 3254 */         short[] t = array[i];
/* 3255 */         int l = (int)Math.min(t.length, offset + length - BigArrays.start(i));
/* 3256 */         for (int d = (int)Math.max(0L, offset - BigArrays.start(i)); d < l; d++) {
/* 3257 */           t[d] = dis.readShort();
/* 3258 */           c++;
/*      */         }
/*      */       
/*      */       } 
/* 3262 */     } catch (EOFException eOFException) {}
/* 3263 */     dis.close();
/* 3264 */     return c;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long loadShorts(CharSequence filename, short[][] array, long offset, long length) throws IOException {
/* 3275 */     return loadShorts(new File(filename.toString()), array, offset, length);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long loadShorts(File file, short[][] array) throws IOException {
/* 3284 */     FileInputStream fis = new FileInputStream(file);
/* 3285 */     DataInputStream dis = new DataInputStream(new FastBufferedInputStream(fis));
/* 3286 */     long c = 0L;
/*      */     try {
/* 3288 */       for (int i = 0; i < array.length; i++) {
/* 3289 */         short[] t = array[i];
/* 3290 */         int l = t.length;
/* 3291 */         for (int d = 0; d < l; d++) {
/* 3292 */           t[d] = dis.readShort();
/* 3293 */           c++;
/*      */         }
/*      */       
/*      */       } 
/* 3297 */     } catch (EOFException eOFException) {}
/* 3298 */     dis.close();
/* 3299 */     return c;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long loadShorts(CharSequence filename, short[][] array) throws IOException {
/* 3308 */     return loadShorts(new File(filename.toString()), array);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static short[][] loadShortsBig(File file) throws IOException {
/* 3320 */     FileInputStream fis = new FileInputStream(file);
/* 3321 */     long length = fis.getChannel().size() / 2L;
/* 3322 */     short[][] array = ShortBigArrays.newBigArray(length);
/* 3323 */     DataInputStream dis = new DataInputStream(new FastBufferedInputStream(fis));
/* 3324 */     for (int i = 0; i < array.length; i++) {
/* 3325 */       short[] t = array[i];
/* 3326 */       int l = t.length;
/* 3327 */       for (int d = 0; d < l; ) { t[d] = dis.readShort(); d++; }
/*      */     
/* 3329 */     }  dis.close();
/* 3330 */     return array;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static short[][] loadShortsBig(CharSequence filename) throws IOException {
/* 3342 */     return loadShortsBig(new File(filename.toString()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeShorts(short[][] array, long offset, long length, DataOutput dataOutput) throws IOException {
/* 3352 */     ShortBigArrays.ensureOffsetLength(array, offset, length);
/* 3353 */     for (int i = BigArrays.segment(offset); i < BigArrays.segment(offset + length + 134217727L); i++) {
/* 3354 */       short[] t = array[i];
/* 3355 */       int l = (int)Math.min(t.length, offset + length - BigArrays.start(i));
/* 3356 */       for (int d = (int)Math.max(0L, offset - BigArrays.start(i)); d < l; ) { dataOutput.writeShort(t[d]); d++; }
/*      */     
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeShorts(short[][] array, DataOutput dataOutput) throws IOException {
/* 3365 */     for (int i = 0; i < array.length; i++) {
/* 3366 */       short[] t = array[i];
/* 3367 */       int l = t.length;
/* 3368 */       for (int d = 0; d < l; ) { dataOutput.writeShort(t[d]); d++; }
/*      */     
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeShorts(short[][] array, long offset, long length, File file) throws IOException {
/* 3379 */     ShortBigArrays.ensureOffsetLength(array, offset, length);
/* 3380 */     DataOutputStream dos = new DataOutputStream(new FastBufferedOutputStream(new FileOutputStream(file)));
/* 3381 */     for (int i = BigArrays.segment(offset); i < BigArrays.segment(offset + length + 134217727L); i++) {
/* 3382 */       short[] t = array[i];
/* 3383 */       int l = (int)Math.min(t.length, offset + length - BigArrays.start(i));
/* 3384 */       for (int d = (int)Math.max(0L, offset - BigArrays.start(i)); d < l; ) { dos.writeShort(t[d]); d++; }
/*      */     
/* 3386 */     }  dos.close();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeShorts(short[][] array, long offset, long length, CharSequence filename) throws IOException {
/* 3396 */     storeShorts(array, offset, length, new File(filename.toString()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeShorts(short[][] array, File file) throws IOException {
/* 3404 */     DataOutputStream dos = new DataOutputStream(new FastBufferedOutputStream(new FileOutputStream(file)));
/* 3405 */     for (int i = 0; i < array.length; i++) {
/* 3406 */       short[] t = array[i];
/* 3407 */       int l = t.length;
/* 3408 */       for (int d = 0; d < l; ) { dos.writeShort(t[d]); d++; }
/*      */     
/* 3410 */     }  dos.close();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeShorts(short[][] array, CharSequence filename) throws IOException {
/* 3418 */     storeShorts(array, new File(filename.toString()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeShorts(ShortIterator i, DataOutput dataOutput) throws IOException {
/* 3426 */     for (; i.hasNext(); dataOutput.writeShort(i.nextShort()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeShorts(ShortIterator i, File file) throws IOException {
/* 3434 */     DataOutputStream dos = new DataOutputStream(new FastBufferedOutputStream(new FileOutputStream(file)));
/* 3435 */     for (; i.hasNext(); dos.writeShort(i.nextShort()));
/* 3436 */     dos.close();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeShorts(ShortIterator i, CharSequence filename) throws IOException {
/* 3444 */     storeShorts(i, new File(filename.toString()));
/*      */   }
/*      */   
/*      */   private static final class ShortDataInputWrapper implements ShortIterator { private final DataInput dataInput;
/*      */     private boolean toAdvance = true;
/*      */     private boolean endOfProcess = false;
/*      */     private short next;
/*      */     
/*      */     public ShortDataInputWrapper(DataInput dataInput) {
/* 3453 */       this.dataInput = dataInput;
/*      */     }
/*      */     
/*      */     public boolean hasNext() {
/* 3457 */       if (!this.toAdvance) return !this.endOfProcess; 
/* 3458 */       this.toAdvance = false; 
/* 3459 */       try { this.next = this.dataInput.readShort(); }
/* 3460 */       catch (EOFException eof) { this.endOfProcess = true; }
/* 3461 */       catch (IOException rethrow) { throw new RuntimeException(rethrow); }
/* 3462 */        return !this.endOfProcess;
/*      */     }
/*      */     
/*      */     public short nextShort() {
/* 3466 */       if (!hasNext()) throw new NoSuchElementException(); 
/* 3467 */       this.toAdvance = true;
/* 3468 */       return this.next;
/*      */     } }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static ShortIterator asShortIterator(DataInput dataInput) {
/* 3476 */     return new ShortDataInputWrapper(dataInput);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static ShortIterator asShortIterator(File file) throws IOException {
/* 3483 */     return new ShortDataInputWrapper(new DataInputStream(new FastBufferedInputStream(new FileInputStream(file))));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static ShortIterator asShortIterator(CharSequence filename) throws IOException {
/* 3490 */     return asShortIterator(new File(filename.toString()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static ShortIterable asShortIterable(File file) {
/* 3497 */     return () -> { try {
/*      */           return asShortIterator(file);
/* 3499 */         } catch (IOException e) {
/*      */           throw new RuntimeException(e);
/*      */         } 
/*      */       };
/*      */   }
/*      */ 
/*      */   
/*      */   public static ShortIterable asShortIterable(CharSequence filename) {
/* 3507 */     return () -> { try {
/*      */           return asShortIterator(filename);
/* 3509 */         } catch (IOException e) {
/*      */           throw new RuntimeException(e);
/*      */         } 
/*      */       };
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int loadChars(DataInput dataInput, char[] array, int offset, int length) throws IOException {
/* 3559 */     CharArrays.ensureOffsetLength(array, offset, length);
/* 3560 */     int i = 0;
/*      */     try {
/* 3562 */       for (i = 0; i < length; ) { array[i + offset] = dataInput.readChar(); i++; }
/*      */     
/* 3564 */     } catch (EOFException eOFException) {}
/* 3565 */     return i;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int loadChars(DataInput dataInput, char[] array) throws IOException {
/* 3574 */     int i = 0;
/*      */     try {
/* 3576 */       int length = array.length;
/* 3577 */       for (i = 0; i < length; ) { array[i] = dataInput.readChar(); i++; }
/*      */     
/* 3579 */     } catch (EOFException eOFException) {}
/* 3580 */     return i;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int loadChars(File file, char[] array, int offset, int length) throws IOException {
/* 3591 */     CharArrays.ensureOffsetLength(array, offset, length);
/* 3592 */     FileInputStream fis = new FileInputStream(file);
/* 3593 */     DataInputStream dis = new DataInputStream(new FastBufferedInputStream(fis));
/* 3594 */     int i = 0;
/*      */     try {
/* 3596 */       for (i = 0; i < length; ) { array[i + offset] = dis.readChar(); i++; }
/*      */     
/* 3598 */     } catch (EOFException eOFException) {}
/* 3599 */     dis.close();
/* 3600 */     return i;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int loadChars(CharSequence filename, char[] array, int offset, int length) throws IOException {
/* 3611 */     return loadChars(new File(filename.toString()), array, offset, length);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int loadChars(File file, char[] array) throws IOException {
/* 3620 */     FileInputStream fis = new FileInputStream(file);
/* 3621 */     DataInputStream dis = new DataInputStream(new FastBufferedInputStream(fis));
/* 3622 */     int i = 0;
/*      */     try {
/* 3624 */       int length = array.length;
/* 3625 */       for (i = 0; i < length; ) { array[i] = dis.readChar(); i++; }
/*      */     
/* 3627 */     } catch (EOFException eOFException) {}
/* 3628 */     dis.close();
/* 3629 */     return i;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int loadChars(CharSequence filename, char[] array) throws IOException {
/* 3638 */     return loadChars(new File(filename.toString()), array);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static char[] loadChars(File file) throws IOException {
/* 3650 */     FileInputStream fis = new FileInputStream(file);
/* 3651 */     long length = fis.getChannel().size() / 2L;
/* 3652 */     if (length > 2147483647L) {
/* 3653 */       fis.close();
/* 3654 */       throw new IllegalArgumentException("File too long: " + fis.getChannel().size() + " bytes (" + length + " elements)");
/*      */     } 
/* 3656 */     char[] array = new char[(int)length];
/* 3657 */     DataInputStream dis = new DataInputStream(new FastBufferedInputStream(fis));
/* 3658 */     for (int i = 0; i < length; ) { array[i] = dis.readChar(); i++; }
/* 3659 */      dis.close();
/* 3660 */     return array;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static char[] loadChars(CharSequence filename) throws IOException {
/* 3672 */     return loadChars(new File(filename.toString()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeChars(char[] array, int offset, int length, DataOutput dataOutput) throws IOException {
/* 3682 */     CharArrays.ensureOffsetLength(array, offset, length);
/* 3683 */     for (int i = 0; i < length; ) { dataOutput.writeChar(array[offset + i]); i++; }
/*      */   
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeChars(char[] array, DataOutput dataOutput) throws IOException {
/* 3691 */     int length = array.length;
/* 3692 */     for (int i = 0; i < length; ) { dataOutput.writeChar(array[i]); i++; }
/*      */   
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeChars(char[] array, int offset, int length, File file) throws IOException {
/* 3702 */     CharArrays.ensureOffsetLength(array, offset, length);
/* 3703 */     DataOutputStream dos = new DataOutputStream(new FastBufferedOutputStream(new FileOutputStream(file)));
/* 3704 */     for (int i = 0; i < length; ) { dos.writeChar(array[offset + i]); i++; }
/* 3705 */      dos.close();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeChars(char[] array, int offset, int length, CharSequence filename) throws IOException {
/* 3715 */     storeChars(array, offset, length, new File(filename.toString()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeChars(char[] array, File file) throws IOException {
/* 3723 */     int length = array.length;
/* 3724 */     DataOutputStream dos = new DataOutputStream(new FastBufferedOutputStream(new FileOutputStream(file)));
/* 3725 */     for (int i = 0; i < length; ) { dos.writeChar(array[i]); i++; }
/* 3726 */      dos.close();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeChars(char[] array, CharSequence filename) throws IOException {
/* 3734 */     storeChars(array, new File(filename.toString()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long loadChars(DataInput dataInput, char[][] array, long offset, long length) throws IOException {
/* 3745 */     CharBigArrays.ensureOffsetLength(array, offset, length);
/* 3746 */     long c = 0L;
/*      */     try {
/* 3748 */       for (int i = BigArrays.segment(offset); i < BigArrays.segment(offset + length + 134217727L); i++) {
/* 3749 */         char[] t = array[i];
/* 3750 */         int l = (int)Math.min(t.length, offset + length - BigArrays.start(i));
/* 3751 */         for (int d = (int)Math.max(0L, offset - BigArrays.start(i)); d < l; d++) {
/* 3752 */           t[d] = dataInput.readChar();
/* 3753 */           c++;
/*      */         }
/*      */       
/*      */       } 
/* 3757 */     } catch (EOFException eOFException) {}
/* 3758 */     return c;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long loadChars(DataInput dataInput, char[][] array) throws IOException {
/* 3767 */     long c = 0L;
/*      */     try {
/* 3769 */       for (int i = 0; i < array.length; i++) {
/* 3770 */         char[] t = array[i];
/* 3771 */         int l = t.length;
/* 3772 */         for (int d = 0; d < l; d++) {
/* 3773 */           t[d] = dataInput.readChar();
/* 3774 */           c++;
/*      */         }
/*      */       
/*      */       } 
/* 3778 */     } catch (EOFException eOFException) {}
/* 3779 */     return c;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long loadChars(File file, char[][] array, long offset, long length) throws IOException {
/* 3790 */     CharBigArrays.ensureOffsetLength(array, offset, length);
/* 3791 */     FileInputStream fis = new FileInputStream(file);
/* 3792 */     DataInputStream dis = new DataInputStream(new FastBufferedInputStream(fis));
/* 3793 */     long c = 0L;
/*      */     try {
/* 3795 */       for (int i = BigArrays.segment(offset); i < BigArrays.segment(offset + length + 134217727L); i++) {
/* 3796 */         char[] t = array[i];
/* 3797 */         int l = (int)Math.min(t.length, offset + length - BigArrays.start(i));
/* 3798 */         for (int d = (int)Math.max(0L, offset - BigArrays.start(i)); d < l; d++) {
/* 3799 */           t[d] = dis.readChar();
/* 3800 */           c++;
/*      */         }
/*      */       
/*      */       } 
/* 3804 */     } catch (EOFException eOFException) {}
/* 3805 */     dis.close();
/* 3806 */     return c;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long loadChars(CharSequence filename, char[][] array, long offset, long length) throws IOException {
/* 3817 */     return loadChars(new File(filename.toString()), array, offset, length);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long loadChars(File file, char[][] array) throws IOException {
/* 3826 */     FileInputStream fis = new FileInputStream(file);
/* 3827 */     DataInputStream dis = new DataInputStream(new FastBufferedInputStream(fis));
/* 3828 */     long c = 0L;
/*      */     try {
/* 3830 */       for (int i = 0; i < array.length; i++) {
/* 3831 */         char[] t = array[i];
/* 3832 */         int l = t.length;
/* 3833 */         for (int d = 0; d < l; d++) {
/* 3834 */           t[d] = dis.readChar();
/* 3835 */           c++;
/*      */         }
/*      */       
/*      */       } 
/* 3839 */     } catch (EOFException eOFException) {}
/* 3840 */     dis.close();
/* 3841 */     return c;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long loadChars(CharSequence filename, char[][] array) throws IOException {
/* 3850 */     return loadChars(new File(filename.toString()), array);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static char[][] loadCharsBig(File file) throws IOException {
/* 3862 */     FileInputStream fis = new FileInputStream(file);
/* 3863 */     long length = fis.getChannel().size() / 2L;
/* 3864 */     char[][] array = CharBigArrays.newBigArray(length);
/* 3865 */     DataInputStream dis = new DataInputStream(new FastBufferedInputStream(fis));
/* 3866 */     for (int i = 0; i < array.length; i++) {
/* 3867 */       char[] t = array[i];
/* 3868 */       int l = t.length;
/* 3869 */       for (int d = 0; d < l; ) { t[d] = dis.readChar(); d++; }
/*      */     
/* 3871 */     }  dis.close();
/* 3872 */     return array;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static char[][] loadCharsBig(CharSequence filename) throws IOException {
/* 3884 */     return loadCharsBig(new File(filename.toString()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeChars(char[][] array, long offset, long length, DataOutput dataOutput) throws IOException {
/* 3894 */     CharBigArrays.ensureOffsetLength(array, offset, length);
/* 3895 */     for (int i = BigArrays.segment(offset); i < BigArrays.segment(offset + length + 134217727L); i++) {
/* 3896 */       char[] t = array[i];
/* 3897 */       int l = (int)Math.min(t.length, offset + length - BigArrays.start(i));
/* 3898 */       for (int d = (int)Math.max(0L, offset - BigArrays.start(i)); d < l; ) { dataOutput.writeChar(t[d]); d++; }
/*      */     
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeChars(char[][] array, DataOutput dataOutput) throws IOException {
/* 3907 */     for (int i = 0; i < array.length; i++) {
/* 3908 */       char[] t = array[i];
/* 3909 */       int l = t.length;
/* 3910 */       for (int d = 0; d < l; ) { dataOutput.writeChar(t[d]); d++; }
/*      */     
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeChars(char[][] array, long offset, long length, File file) throws IOException {
/* 3921 */     CharBigArrays.ensureOffsetLength(array, offset, length);
/* 3922 */     DataOutputStream dos = new DataOutputStream(new FastBufferedOutputStream(new FileOutputStream(file)));
/* 3923 */     for (int i = BigArrays.segment(offset); i < BigArrays.segment(offset + length + 134217727L); i++) {
/* 3924 */       char[] t = array[i];
/* 3925 */       int l = (int)Math.min(t.length, offset + length - BigArrays.start(i));
/* 3926 */       for (int d = (int)Math.max(0L, offset - BigArrays.start(i)); d < l; ) { dos.writeChar(t[d]); d++; }
/*      */     
/* 3928 */     }  dos.close();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeChars(char[][] array, long offset, long length, CharSequence filename) throws IOException {
/* 3938 */     storeChars(array, offset, length, new File(filename.toString()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeChars(char[][] array, File file) throws IOException {
/* 3946 */     DataOutputStream dos = new DataOutputStream(new FastBufferedOutputStream(new FileOutputStream(file)));
/* 3947 */     for (int i = 0; i < array.length; i++) {
/* 3948 */       char[] t = array[i];
/* 3949 */       int l = t.length;
/* 3950 */       for (int d = 0; d < l; ) { dos.writeChar(t[d]); d++; }
/*      */     
/* 3952 */     }  dos.close();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeChars(char[][] array, CharSequence filename) throws IOException {
/* 3960 */     storeChars(array, new File(filename.toString()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeChars(CharIterator i, DataOutput dataOutput) throws IOException {
/* 3968 */     for (; i.hasNext(); dataOutput.writeChar(i.nextChar()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeChars(CharIterator i, File file) throws IOException {
/* 3976 */     DataOutputStream dos = new DataOutputStream(new FastBufferedOutputStream(new FileOutputStream(file)));
/* 3977 */     for (; i.hasNext(); dos.writeChar(i.nextChar()));
/* 3978 */     dos.close();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeChars(CharIterator i, CharSequence filename) throws IOException {
/* 3986 */     storeChars(i, new File(filename.toString()));
/*      */   }
/*      */   
/*      */   private static final class CharDataInputWrapper implements CharIterator { private final DataInput dataInput;
/*      */     private boolean toAdvance = true;
/*      */     private boolean endOfProcess = false;
/*      */     private char next;
/*      */     
/*      */     public CharDataInputWrapper(DataInput dataInput) {
/* 3995 */       this.dataInput = dataInput;
/*      */     }
/*      */     
/*      */     public boolean hasNext() {
/* 3999 */       if (!this.toAdvance) return !this.endOfProcess; 
/* 4000 */       this.toAdvance = false; 
/* 4001 */       try { this.next = this.dataInput.readChar(); }
/* 4002 */       catch (EOFException eof) { this.endOfProcess = true; }
/* 4003 */       catch (IOException rethrow) { throw new RuntimeException(rethrow); }
/* 4004 */        return !this.endOfProcess;
/*      */     }
/*      */     
/*      */     public char nextChar() {
/* 4008 */       if (!hasNext()) throw new NoSuchElementException(); 
/* 4009 */       this.toAdvance = true;
/* 4010 */       return this.next;
/*      */     } }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static CharIterator asCharIterator(DataInput dataInput) {
/* 4018 */     return new CharDataInputWrapper(dataInput);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static CharIterator asCharIterator(File file) throws IOException {
/* 4025 */     return new CharDataInputWrapper(new DataInputStream(new FastBufferedInputStream(new FileInputStream(file))));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static CharIterator asCharIterator(CharSequence filename) throws IOException {
/* 4032 */     return asCharIterator(new File(filename.toString()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static CharIterable asCharIterable(File file) {
/* 4039 */     return () -> { try {
/*      */           return asCharIterator(file);
/* 4041 */         } catch (IOException e) {
/*      */           throw new RuntimeException(e);
/*      */         } 
/*      */       };
/*      */   }
/*      */ 
/*      */   
/*      */   public static CharIterable asCharIterable(CharSequence filename) {
/* 4049 */     return () -> { try {
/*      */           return asCharIterator(filename);
/* 4051 */         } catch (IOException e) {
/*      */           throw new RuntimeException(e);
/*      */         } 
/*      */       };
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int loadFloats(DataInput dataInput, float[] array, int offset, int length) throws IOException {
/* 4101 */     FloatArrays.ensureOffsetLength(array, offset, length);
/* 4102 */     int i = 0;
/*      */     try {
/* 4104 */       for (i = 0; i < length; ) { array[i + offset] = dataInput.readFloat(); i++; }
/*      */     
/* 4106 */     } catch (EOFException eOFException) {}
/* 4107 */     return i;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int loadFloats(DataInput dataInput, float[] array) throws IOException {
/* 4116 */     int i = 0;
/*      */     try {
/* 4118 */       int length = array.length;
/* 4119 */       for (i = 0; i < length; ) { array[i] = dataInput.readFloat(); i++; }
/*      */     
/* 4121 */     } catch (EOFException eOFException) {}
/* 4122 */     return i;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int loadFloats(File file, float[] array, int offset, int length) throws IOException {
/* 4133 */     FloatArrays.ensureOffsetLength(array, offset, length);
/* 4134 */     FileInputStream fis = new FileInputStream(file);
/* 4135 */     DataInputStream dis = new DataInputStream(new FastBufferedInputStream(fis));
/* 4136 */     int i = 0;
/*      */     try {
/* 4138 */       for (i = 0; i < length; ) { array[i + offset] = dis.readFloat(); i++; }
/*      */     
/* 4140 */     } catch (EOFException eOFException) {}
/* 4141 */     dis.close();
/* 4142 */     return i;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int loadFloats(CharSequence filename, float[] array, int offset, int length) throws IOException {
/* 4153 */     return loadFloats(new File(filename.toString()), array, offset, length);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int loadFloats(File file, float[] array) throws IOException {
/* 4162 */     FileInputStream fis = new FileInputStream(file);
/* 4163 */     DataInputStream dis = new DataInputStream(new FastBufferedInputStream(fis));
/* 4164 */     int i = 0;
/*      */     try {
/* 4166 */       int length = array.length;
/* 4167 */       for (i = 0; i < length; ) { array[i] = dis.readFloat(); i++; }
/*      */     
/* 4169 */     } catch (EOFException eOFException) {}
/* 4170 */     dis.close();
/* 4171 */     return i;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int loadFloats(CharSequence filename, float[] array) throws IOException {
/* 4180 */     return loadFloats(new File(filename.toString()), array);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static float[] loadFloats(File file) throws IOException {
/* 4192 */     FileInputStream fis = new FileInputStream(file);
/* 4193 */     long length = fis.getChannel().size() / 4L;
/* 4194 */     if (length > 2147483647L) {
/* 4195 */       fis.close();
/* 4196 */       throw new IllegalArgumentException("File too long: " + fis.getChannel().size() + " bytes (" + length + " elements)");
/*      */     } 
/* 4198 */     float[] array = new float[(int)length];
/* 4199 */     DataInputStream dis = new DataInputStream(new FastBufferedInputStream(fis));
/* 4200 */     for (int i = 0; i < length; ) { array[i] = dis.readFloat(); i++; }
/* 4201 */      dis.close();
/* 4202 */     return array;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static float[] loadFloats(CharSequence filename) throws IOException {
/* 4214 */     return loadFloats(new File(filename.toString()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeFloats(float[] array, int offset, int length, DataOutput dataOutput) throws IOException {
/* 4224 */     FloatArrays.ensureOffsetLength(array, offset, length);
/* 4225 */     for (int i = 0; i < length; ) { dataOutput.writeFloat(array[offset + i]); i++; }
/*      */   
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeFloats(float[] array, DataOutput dataOutput) throws IOException {
/* 4233 */     int length = array.length;
/* 4234 */     for (int i = 0; i < length; ) { dataOutput.writeFloat(array[i]); i++; }
/*      */   
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeFloats(float[] array, int offset, int length, File file) throws IOException {
/* 4244 */     FloatArrays.ensureOffsetLength(array, offset, length);
/* 4245 */     DataOutputStream dos = new DataOutputStream(new FastBufferedOutputStream(new FileOutputStream(file)));
/* 4246 */     for (int i = 0; i < length; ) { dos.writeFloat(array[offset + i]); i++; }
/* 4247 */      dos.close();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeFloats(float[] array, int offset, int length, CharSequence filename) throws IOException {
/* 4257 */     storeFloats(array, offset, length, new File(filename.toString()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeFloats(float[] array, File file) throws IOException {
/* 4265 */     int length = array.length;
/* 4266 */     DataOutputStream dos = new DataOutputStream(new FastBufferedOutputStream(new FileOutputStream(file)));
/* 4267 */     for (int i = 0; i < length; ) { dos.writeFloat(array[i]); i++; }
/* 4268 */      dos.close();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeFloats(float[] array, CharSequence filename) throws IOException {
/* 4276 */     storeFloats(array, new File(filename.toString()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long loadFloats(DataInput dataInput, float[][] array, long offset, long length) throws IOException {
/* 4287 */     FloatBigArrays.ensureOffsetLength(array, offset, length);
/* 4288 */     long c = 0L;
/*      */     try {
/* 4290 */       for (int i = BigArrays.segment(offset); i < BigArrays.segment(offset + length + 134217727L); i++) {
/* 4291 */         float[] t = array[i];
/* 4292 */         int l = (int)Math.min(t.length, offset + length - BigArrays.start(i));
/* 4293 */         for (int d = (int)Math.max(0L, offset - BigArrays.start(i)); d < l; d++) {
/* 4294 */           t[d] = dataInput.readFloat();
/* 4295 */           c++;
/*      */         }
/*      */       
/*      */       } 
/* 4299 */     } catch (EOFException eOFException) {}
/* 4300 */     return c;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long loadFloats(DataInput dataInput, float[][] array) throws IOException {
/* 4309 */     long c = 0L;
/*      */     try {
/* 4311 */       for (int i = 0; i < array.length; i++) {
/* 4312 */         float[] t = array[i];
/* 4313 */         int l = t.length;
/* 4314 */         for (int d = 0; d < l; d++) {
/* 4315 */           t[d] = dataInput.readFloat();
/* 4316 */           c++;
/*      */         }
/*      */       
/*      */       } 
/* 4320 */     } catch (EOFException eOFException) {}
/* 4321 */     return c;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long loadFloats(File file, float[][] array, long offset, long length) throws IOException {
/* 4332 */     FloatBigArrays.ensureOffsetLength(array, offset, length);
/* 4333 */     FileInputStream fis = new FileInputStream(file);
/* 4334 */     DataInputStream dis = new DataInputStream(new FastBufferedInputStream(fis));
/* 4335 */     long c = 0L;
/*      */     try {
/* 4337 */       for (int i = BigArrays.segment(offset); i < BigArrays.segment(offset + length + 134217727L); i++) {
/* 4338 */         float[] t = array[i];
/* 4339 */         int l = (int)Math.min(t.length, offset + length - BigArrays.start(i));
/* 4340 */         for (int d = (int)Math.max(0L, offset - BigArrays.start(i)); d < l; d++) {
/* 4341 */           t[d] = dis.readFloat();
/* 4342 */           c++;
/*      */         }
/*      */       
/*      */       } 
/* 4346 */     } catch (EOFException eOFException) {}
/* 4347 */     dis.close();
/* 4348 */     return c;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long loadFloats(CharSequence filename, float[][] array, long offset, long length) throws IOException {
/* 4359 */     return loadFloats(new File(filename.toString()), array, offset, length);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long loadFloats(File file, float[][] array) throws IOException {
/* 4368 */     FileInputStream fis = new FileInputStream(file);
/* 4369 */     DataInputStream dis = new DataInputStream(new FastBufferedInputStream(fis));
/* 4370 */     long c = 0L;
/*      */     try {
/* 4372 */       for (int i = 0; i < array.length; i++) {
/* 4373 */         float[] t = array[i];
/* 4374 */         int l = t.length;
/* 4375 */         for (int d = 0; d < l; d++) {
/* 4376 */           t[d] = dis.readFloat();
/* 4377 */           c++;
/*      */         }
/*      */       
/*      */       } 
/* 4381 */     } catch (EOFException eOFException) {}
/* 4382 */     dis.close();
/* 4383 */     return c;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long loadFloats(CharSequence filename, float[][] array) throws IOException {
/* 4392 */     return loadFloats(new File(filename.toString()), array);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static float[][] loadFloatsBig(File file) throws IOException {
/* 4404 */     FileInputStream fis = new FileInputStream(file);
/* 4405 */     long length = fis.getChannel().size() / 4L;
/* 4406 */     float[][] array = FloatBigArrays.newBigArray(length);
/* 4407 */     DataInputStream dis = new DataInputStream(new FastBufferedInputStream(fis));
/* 4408 */     for (int i = 0; i < array.length; i++) {
/* 4409 */       float[] t = array[i];
/* 4410 */       int l = t.length;
/* 4411 */       for (int d = 0; d < l; ) { t[d] = dis.readFloat(); d++; }
/*      */     
/* 4413 */     }  dis.close();
/* 4414 */     return array;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static float[][] loadFloatsBig(CharSequence filename) throws IOException {
/* 4426 */     return loadFloatsBig(new File(filename.toString()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeFloats(float[][] array, long offset, long length, DataOutput dataOutput) throws IOException {
/* 4436 */     FloatBigArrays.ensureOffsetLength(array, offset, length);
/* 4437 */     for (int i = BigArrays.segment(offset); i < BigArrays.segment(offset + length + 134217727L); i++) {
/* 4438 */       float[] t = array[i];
/* 4439 */       int l = (int)Math.min(t.length, offset + length - BigArrays.start(i));
/* 4440 */       for (int d = (int)Math.max(0L, offset - BigArrays.start(i)); d < l; ) { dataOutput.writeFloat(t[d]); d++; }
/*      */     
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeFloats(float[][] array, DataOutput dataOutput) throws IOException {
/* 4449 */     for (int i = 0; i < array.length; i++) {
/* 4450 */       float[] t = array[i];
/* 4451 */       int l = t.length;
/* 4452 */       for (int d = 0; d < l; ) { dataOutput.writeFloat(t[d]); d++; }
/*      */     
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeFloats(float[][] array, long offset, long length, File file) throws IOException {
/* 4463 */     FloatBigArrays.ensureOffsetLength(array, offset, length);
/* 4464 */     DataOutputStream dos = new DataOutputStream(new FastBufferedOutputStream(new FileOutputStream(file)));
/* 4465 */     for (int i = BigArrays.segment(offset); i < BigArrays.segment(offset + length + 134217727L); i++) {
/* 4466 */       float[] t = array[i];
/* 4467 */       int l = (int)Math.min(t.length, offset + length - BigArrays.start(i));
/* 4468 */       for (int d = (int)Math.max(0L, offset - BigArrays.start(i)); d < l; ) { dos.writeFloat(t[d]); d++; }
/*      */     
/* 4470 */     }  dos.close();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeFloats(float[][] array, long offset, long length, CharSequence filename) throws IOException {
/* 4480 */     storeFloats(array, offset, length, new File(filename.toString()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeFloats(float[][] array, File file) throws IOException {
/* 4488 */     DataOutputStream dos = new DataOutputStream(new FastBufferedOutputStream(new FileOutputStream(file)));
/* 4489 */     for (int i = 0; i < array.length; i++) {
/* 4490 */       float[] t = array[i];
/* 4491 */       int l = t.length;
/* 4492 */       for (int d = 0; d < l; ) { dos.writeFloat(t[d]); d++; }
/*      */     
/* 4494 */     }  dos.close();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeFloats(float[][] array, CharSequence filename) throws IOException {
/* 4502 */     storeFloats(array, new File(filename.toString()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeFloats(FloatIterator i, DataOutput dataOutput) throws IOException {
/* 4510 */     for (; i.hasNext(); dataOutput.writeFloat(i.nextFloat()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeFloats(FloatIterator i, File file) throws IOException {
/* 4518 */     DataOutputStream dos = new DataOutputStream(new FastBufferedOutputStream(new FileOutputStream(file)));
/* 4519 */     for (; i.hasNext(); dos.writeFloat(i.nextFloat()));
/* 4520 */     dos.close();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeFloats(FloatIterator i, CharSequence filename) throws IOException {
/* 4528 */     storeFloats(i, new File(filename.toString()));
/*      */   }
/*      */   
/*      */   private static final class FloatDataInputWrapper implements FloatIterator { private final DataInput dataInput;
/*      */     private boolean toAdvance = true;
/*      */     private boolean endOfProcess = false;
/*      */     private float next;
/*      */     
/*      */     public FloatDataInputWrapper(DataInput dataInput) {
/* 4537 */       this.dataInput = dataInput;
/*      */     }
/*      */     
/*      */     public boolean hasNext() {
/* 4541 */       if (!this.toAdvance) return !this.endOfProcess; 
/* 4542 */       this.toAdvance = false; 
/* 4543 */       try { this.next = this.dataInput.readFloat(); }
/* 4544 */       catch (EOFException eof) { this.endOfProcess = true; }
/* 4545 */       catch (IOException rethrow) { throw new RuntimeException(rethrow); }
/* 4546 */        return !this.endOfProcess;
/*      */     }
/*      */     
/*      */     public float nextFloat() {
/* 4550 */       if (!hasNext()) throw new NoSuchElementException(); 
/* 4551 */       this.toAdvance = true;
/* 4552 */       return this.next;
/*      */     } }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static FloatIterator asFloatIterator(DataInput dataInput) {
/* 4560 */     return new FloatDataInputWrapper(dataInput);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static FloatIterator asFloatIterator(File file) throws IOException {
/* 4567 */     return new FloatDataInputWrapper(new DataInputStream(new FastBufferedInputStream(new FileInputStream(file))));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static FloatIterator asFloatIterator(CharSequence filename) throws IOException {
/* 4574 */     return asFloatIterator(new File(filename.toString()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static FloatIterable asFloatIterable(File file) {
/* 4581 */     return () -> { try {
/*      */           return asFloatIterator(file);
/* 4583 */         } catch (IOException e) {
/*      */           throw new RuntimeException(e);
/*      */         } 
/*      */       };
/*      */   }
/*      */ 
/*      */   
/*      */   public static FloatIterable asFloatIterable(CharSequence filename) {
/* 4591 */     return () -> { try {
/*      */           return asFloatIterator(filename);
/* 4593 */         } catch (IOException e) {
/*      */           throw new RuntimeException(e);
/*      */         } 
/*      */       };
/*      */   }
/*      */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\i\\unimi\dsi\fastutil\io\BinIO.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */