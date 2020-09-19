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
/*      */ import java.io.BufferedReader;
/*      */ import java.io.EOFException;
/*      */ import java.io.File;
/*      */ import java.io.FileOutputStream;
/*      */ import java.io.FileReader;
/*      */ import java.io.IOException;
/*      */ import java.io.PrintStream;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class TextIO
/*      */ {
/*      */   public static final int BUFFER_SIZE = 8192;
/*      */   
/*      */   public static int loadInts(BufferedReader reader, int[] array, int offset, int length) throws IOException {
/*  105 */     IntArrays.ensureOffsetLength(array, offset, length);
/*  106 */     int i = 0;
/*      */     try {
/*      */       String s;
/*  109 */       for (i = 0; i < length && (
/*  110 */         s = reader.readLine()) != null; i++) array[i + offset] = Integer.parseInt(s.trim());
/*      */     
/*      */     }
/*  113 */     catch (EOFException eOFException) {}
/*  114 */     return i;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int loadInts(BufferedReader reader, int[] array) throws IOException {
/*  123 */     return loadInts(reader, array, 0, array.length);
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
/*  134 */     BufferedReader reader = new BufferedReader(new FileReader(file));
/*  135 */     int result = loadInts(reader, array, offset, length);
/*  136 */     reader.close();
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
/*      */   public static int loadInts(CharSequence filename, int[] array, int offset, int length) throws IOException {
/*  148 */     return loadInts(new File(filename.toString()), array, offset, length);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int loadInts(File file, int[] array) throws IOException {
/*  157 */     return loadInts(file, array, 0, array.length);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int loadInts(CharSequence filename, int[] array) throws IOException {
/*  166 */     return loadInts(filename, array, 0, array.length);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeInts(int[] array, int offset, int length, PrintStream stream) {
/*  176 */     IntArrays.ensureOffsetLength(array, offset, length);
/*  177 */     for (int i = 0; i < length; ) { stream.println(array[offset + i]); i++; }
/*      */   
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeInts(int[] array, PrintStream stream) {
/*  185 */     storeInts(array, 0, array.length, stream);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeInts(int[] array, int offset, int length, File file) throws IOException {
/*  195 */     PrintStream stream = new PrintStream(new FastBufferedOutputStream(new FileOutputStream(file)));
/*  196 */     storeInts(array, offset, length, stream);
/*  197 */     stream.close();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeInts(int[] array, int offset, int length, CharSequence filename) throws IOException {
/*  207 */     storeInts(array, offset, length, new File(filename.toString()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeInts(int[] array, File file) throws IOException {
/*  215 */     storeInts(array, 0, array.length, file);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeInts(int[] array, CharSequence filename) throws IOException {
/*  223 */     storeInts(array, 0, array.length, filename);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeInts(IntIterator i, PrintStream stream) {
/*  231 */     for (; i.hasNext(); stream.println(i.nextInt()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeInts(IntIterator i, File file) throws IOException {
/*  239 */     PrintStream stream = new PrintStream(new FastBufferedOutputStream(new FileOutputStream(file)));
/*  240 */     storeInts(i, stream);
/*  241 */     stream.close();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeInts(IntIterator i, CharSequence filename) throws IOException {
/*  249 */     storeInts(i, new File(filename.toString()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long loadInts(BufferedReader reader, int[][] array, long offset, long length) throws IOException {
/*  260 */     IntBigArrays.ensureOffsetLength(array, offset, length);
/*  261 */     long c = 0L;
/*      */     
/*      */     try {
/*  264 */       for (int i = BigArrays.segment(offset); i < BigArrays.segment(offset + length + 134217727L); i++) {
/*  265 */         int[] t = array[i];
/*  266 */         int l = (int)Math.min(t.length, offset + length - BigArrays.start(i));
/*  267 */         for (int d = (int)Math.max(0L, offset - BigArrays.start(i)); d < l; d++) {
/*  268 */           String s; if ((s = reader.readLine()) != null) { t[d] = Integer.parseInt(s.trim()); }
/*  269 */           else { return c; }
/*  270 */            c++;
/*      */         }
/*      */       
/*      */       } 
/*  274 */     } catch (EOFException eOFException) {}
/*  275 */     return c;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long loadInts(BufferedReader reader, int[][] array) throws IOException {
/*  284 */     return loadInts(reader, array, 0L, IntBigArrays.length(array));
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
/*  295 */     BufferedReader reader = new BufferedReader(new FileReader(file));
/*  296 */     long result = loadInts(reader, array, offset, length);
/*  297 */     reader.close();
/*  298 */     return result;
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
/*  309 */     return loadInts(new File(filename.toString()), array, offset, length);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long loadInts(File file, int[][] array) throws IOException {
/*  318 */     return loadInts(file, array, 0L, IntBigArrays.length(array));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long loadInts(CharSequence filename, int[][] array) throws IOException {
/*  327 */     return loadInts(filename, array, 0L, IntBigArrays.length(array));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeInts(int[][] array, long offset, long length, PrintStream stream) {
/*  337 */     IntBigArrays.ensureOffsetLength(array, offset, length);
/*  338 */     for (int i = BigArrays.segment(offset); i < BigArrays.segment(offset + length + 134217727L); i++) {
/*  339 */       int[] t = array[i];
/*  340 */       int l = (int)Math.min(t.length, offset + length - BigArrays.start(i));
/*  341 */       for (int d = (int)Math.max(0L, offset - BigArrays.start(i)); d < l; ) { stream.println(t[d]); d++; }
/*      */     
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeInts(int[][] array, PrintStream stream) {
/*  350 */     storeInts(array, 0L, IntBigArrays.length(array), stream);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeInts(int[][] array, long offset, long length, File file) throws IOException {
/*  360 */     PrintStream stream = new PrintStream(new FastBufferedOutputStream(new FileOutputStream(file)));
/*  361 */     storeInts(array, offset, length, stream);
/*  362 */     stream.close();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeInts(int[][] array, long offset, long length, CharSequence filename) throws IOException {
/*  372 */     storeInts(array, offset, length, new File(filename.toString()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeInts(int[][] array, File file) throws IOException {
/*  380 */     storeInts(array, 0L, IntBigArrays.length(array), file);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeInts(int[][] array, CharSequence filename) throws IOException {
/*  388 */     storeInts(array, 0L, IntBigArrays.length(array), filename);
/*      */   }
/*      */   
/*      */   private static final class IntReaderWrapper implements IntIterator { private final BufferedReader reader;
/*      */     private boolean toAdvance = true;
/*      */     private String s;
/*      */     private int next;
/*      */     
/*      */     public IntReaderWrapper(BufferedReader reader) {
/*  397 */       this.reader = reader;
/*      */     }
/*      */     
/*      */     public boolean hasNext() {
/*  401 */       if (!this.toAdvance) return (this.s != null); 
/*  402 */       this.toAdvance = false;
/*      */       
/*  404 */       try { this.s = this.reader.readLine(); }
/*      */       
/*  406 */       catch (EOFException eOFException) {  }
/*  407 */       catch (IOException rethrow) { throw new RuntimeException(rethrow); }
/*  408 */        if (this.s == null) return false; 
/*  409 */       this.next = Integer.parseInt(this.s.trim());
/*  410 */       return true;
/*      */     }
/*      */     
/*      */     public int nextInt() {
/*  414 */       if (!hasNext()) throw new NoSuchElementException(); 
/*  415 */       this.toAdvance = true;
/*  416 */       return this.next;
/*      */     } }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static IntIterator asIntIterator(BufferedReader reader) {
/*  424 */     return new IntReaderWrapper(reader);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static IntIterator asIntIterator(File file) throws IOException {
/*  431 */     return new IntReaderWrapper(new BufferedReader(new FileReader(file)));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static IntIterator asIntIterator(CharSequence filename) throws IOException {
/*  438 */     return asIntIterator(new File(filename.toString()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static IntIterable asIntIterable(File file) {
/*  445 */     return () -> { try {
/*      */           return asIntIterator(file);
/*  447 */         } catch (IOException e) {
/*      */           throw new RuntimeException(e);
/*      */         } 
/*      */       };
/*      */   }
/*      */ 
/*      */   
/*      */   public static IntIterable asIntIterable(CharSequence filename) {
/*  455 */     return () -> { try {
/*      */           return asIntIterator(filename);
/*  457 */         } catch (IOException e) {
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
/*      */   public static int loadLongs(BufferedReader reader, long[] array, int offset, int length) throws IOException {
/*  507 */     LongArrays.ensureOffsetLength(array, offset, length);
/*  508 */     int i = 0;
/*      */     try {
/*      */       String s;
/*  511 */       for (i = 0; i < length && (
/*  512 */         s = reader.readLine()) != null; i++) array[i + offset] = Long.parseLong(s.trim());
/*      */     
/*      */     }
/*  515 */     catch (EOFException eOFException) {}
/*  516 */     return i;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int loadLongs(BufferedReader reader, long[] array) throws IOException {
/*  525 */     return loadLongs(reader, array, 0, array.length);
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
/*  536 */     BufferedReader reader = new BufferedReader(new FileReader(file));
/*  537 */     int result = loadLongs(reader, array, offset, length);
/*  538 */     reader.close();
/*  539 */     return result;
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
/*  550 */     return loadLongs(new File(filename.toString()), array, offset, length);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int loadLongs(File file, long[] array) throws IOException {
/*  559 */     return loadLongs(file, array, 0, array.length);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int loadLongs(CharSequence filename, long[] array) throws IOException {
/*  568 */     return loadLongs(filename, array, 0, array.length);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeLongs(long[] array, int offset, int length, PrintStream stream) {
/*  578 */     LongArrays.ensureOffsetLength(array, offset, length);
/*  579 */     for (int i = 0; i < length; ) { stream.println(array[offset + i]); i++; }
/*      */   
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeLongs(long[] array, PrintStream stream) {
/*  587 */     storeLongs(array, 0, array.length, stream);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeLongs(long[] array, int offset, int length, File file) throws IOException {
/*  597 */     PrintStream stream = new PrintStream(new FastBufferedOutputStream(new FileOutputStream(file)));
/*  598 */     storeLongs(array, offset, length, stream);
/*  599 */     stream.close();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeLongs(long[] array, int offset, int length, CharSequence filename) throws IOException {
/*  609 */     storeLongs(array, offset, length, new File(filename.toString()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeLongs(long[] array, File file) throws IOException {
/*  617 */     storeLongs(array, 0, array.length, file);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeLongs(long[] array, CharSequence filename) throws IOException {
/*  625 */     storeLongs(array, 0, array.length, filename);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeLongs(LongIterator i, PrintStream stream) {
/*  633 */     for (; i.hasNext(); stream.println(i.nextLong()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeLongs(LongIterator i, File file) throws IOException {
/*  641 */     PrintStream stream = new PrintStream(new FastBufferedOutputStream(new FileOutputStream(file)));
/*  642 */     storeLongs(i, stream);
/*  643 */     stream.close();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeLongs(LongIterator i, CharSequence filename) throws IOException {
/*  651 */     storeLongs(i, new File(filename.toString()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long loadLongs(BufferedReader reader, long[][] array, long offset, long length) throws IOException {
/*  662 */     LongBigArrays.ensureOffsetLength(array, offset, length);
/*  663 */     long c = 0L;
/*      */     
/*      */     try {
/*  666 */       for (int i = BigArrays.segment(offset); i < BigArrays.segment(offset + length + 134217727L); i++) {
/*  667 */         long[] t = array[i];
/*  668 */         int l = (int)Math.min(t.length, offset + length - BigArrays.start(i));
/*  669 */         for (int d = (int)Math.max(0L, offset - BigArrays.start(i)); d < l; d++) {
/*  670 */           String s; if ((s = reader.readLine()) != null) { t[d] = Long.parseLong(s.trim()); }
/*  671 */           else { return c; }
/*  672 */            c++;
/*      */         }
/*      */       
/*      */       } 
/*  676 */     } catch (EOFException eOFException) {}
/*  677 */     return c;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long loadLongs(BufferedReader reader, long[][] array) throws IOException {
/*  686 */     return loadLongs(reader, array, 0L, LongBigArrays.length(array));
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
/*  697 */     BufferedReader reader = new BufferedReader(new FileReader(file));
/*  698 */     long result = loadLongs(reader, array, offset, length);
/*  699 */     reader.close();
/*  700 */     return result;
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
/*  711 */     return loadLongs(new File(filename.toString()), array, offset, length);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long loadLongs(File file, long[][] array) throws IOException {
/*  720 */     return loadLongs(file, array, 0L, LongBigArrays.length(array));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long loadLongs(CharSequence filename, long[][] array) throws IOException {
/*  729 */     return loadLongs(filename, array, 0L, LongBigArrays.length(array));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeLongs(long[][] array, long offset, long length, PrintStream stream) {
/*  739 */     LongBigArrays.ensureOffsetLength(array, offset, length);
/*  740 */     for (int i = BigArrays.segment(offset); i < BigArrays.segment(offset + length + 134217727L); i++) {
/*  741 */       long[] t = array[i];
/*  742 */       int l = (int)Math.min(t.length, offset + length - BigArrays.start(i));
/*  743 */       for (int d = (int)Math.max(0L, offset - BigArrays.start(i)); d < l; ) { stream.println(t[d]); d++; }
/*      */     
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeLongs(long[][] array, PrintStream stream) {
/*  752 */     storeLongs(array, 0L, LongBigArrays.length(array), stream);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeLongs(long[][] array, long offset, long length, File file) throws IOException {
/*  762 */     PrintStream stream = new PrintStream(new FastBufferedOutputStream(new FileOutputStream(file)));
/*  763 */     storeLongs(array, offset, length, stream);
/*  764 */     stream.close();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeLongs(long[][] array, long offset, long length, CharSequence filename) throws IOException {
/*  774 */     storeLongs(array, offset, length, new File(filename.toString()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeLongs(long[][] array, File file) throws IOException {
/*  782 */     storeLongs(array, 0L, LongBigArrays.length(array), file);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeLongs(long[][] array, CharSequence filename) throws IOException {
/*  790 */     storeLongs(array, 0L, LongBigArrays.length(array), filename);
/*      */   }
/*      */   
/*      */   private static final class LongReaderWrapper implements LongIterator { private final BufferedReader reader;
/*      */     private boolean toAdvance = true;
/*      */     private String s;
/*      */     private long next;
/*      */     
/*      */     public LongReaderWrapper(BufferedReader reader) {
/*  799 */       this.reader = reader;
/*      */     }
/*      */     
/*      */     public boolean hasNext() {
/*  803 */       if (!this.toAdvance) return (this.s != null); 
/*  804 */       this.toAdvance = false;
/*      */       
/*  806 */       try { this.s = this.reader.readLine(); }
/*      */       
/*  808 */       catch (EOFException eOFException) {  }
/*  809 */       catch (IOException rethrow) { throw new RuntimeException(rethrow); }
/*  810 */        if (this.s == null) return false; 
/*  811 */       this.next = Long.parseLong(this.s.trim());
/*  812 */       return true;
/*      */     }
/*      */     
/*      */     public long nextLong() {
/*  816 */       if (!hasNext()) throw new NoSuchElementException(); 
/*  817 */       this.toAdvance = true;
/*  818 */       return this.next;
/*      */     } }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static LongIterator asLongIterator(BufferedReader reader) {
/*  826 */     return new LongReaderWrapper(reader);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static LongIterator asLongIterator(File file) throws IOException {
/*  833 */     return new LongReaderWrapper(new BufferedReader(new FileReader(file)));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static LongIterator asLongIterator(CharSequence filename) throws IOException {
/*  840 */     return asLongIterator(new File(filename.toString()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static LongIterable asLongIterable(File file) {
/*  847 */     return () -> { try {
/*      */           return asLongIterator(file);
/*  849 */         } catch (IOException e) {
/*      */           throw new RuntimeException(e);
/*      */         } 
/*      */       };
/*      */   }
/*      */ 
/*      */   
/*      */   public static LongIterable asLongIterable(CharSequence filename) {
/*  857 */     return () -> { try {
/*      */           return asLongIterator(filename);
/*  859 */         } catch (IOException e) {
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
/*      */   public static int loadDoubles(BufferedReader reader, double[] array, int offset, int length) throws IOException {
/*  909 */     DoubleArrays.ensureOffsetLength(array, offset, length);
/*  910 */     int i = 0;
/*      */     try {
/*      */       String s;
/*  913 */       for (i = 0; i < length && (
/*  914 */         s = reader.readLine()) != null; i++) array[i + offset] = Double.parseDouble(s.trim());
/*      */     
/*      */     }
/*  917 */     catch (EOFException eOFException) {}
/*  918 */     return i;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int loadDoubles(BufferedReader reader, double[] array) throws IOException {
/*  927 */     return loadDoubles(reader, array, 0, array.length);
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
/*  938 */     BufferedReader reader = new BufferedReader(new FileReader(file));
/*  939 */     int result = loadDoubles(reader, array, offset, length);
/*  940 */     reader.close();
/*  941 */     return result;
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
/*  952 */     return loadDoubles(new File(filename.toString()), array, offset, length);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int loadDoubles(File file, double[] array) throws IOException {
/*  961 */     return loadDoubles(file, array, 0, array.length);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int loadDoubles(CharSequence filename, double[] array) throws IOException {
/*  970 */     return loadDoubles(filename, array, 0, array.length);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeDoubles(double[] array, int offset, int length, PrintStream stream) {
/*  980 */     DoubleArrays.ensureOffsetLength(array, offset, length);
/*  981 */     for (int i = 0; i < length; ) { stream.println(array[offset + i]); i++; }
/*      */   
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeDoubles(double[] array, PrintStream stream) {
/*  989 */     storeDoubles(array, 0, array.length, stream);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeDoubles(double[] array, int offset, int length, File file) throws IOException {
/*  999 */     PrintStream stream = new PrintStream(new FastBufferedOutputStream(new FileOutputStream(file)));
/* 1000 */     storeDoubles(array, offset, length, stream);
/* 1001 */     stream.close();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeDoubles(double[] array, int offset, int length, CharSequence filename) throws IOException {
/* 1011 */     storeDoubles(array, offset, length, new File(filename.toString()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeDoubles(double[] array, File file) throws IOException {
/* 1019 */     storeDoubles(array, 0, array.length, file);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeDoubles(double[] array, CharSequence filename) throws IOException {
/* 1027 */     storeDoubles(array, 0, array.length, filename);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeDoubles(DoubleIterator i, PrintStream stream) {
/* 1035 */     for (; i.hasNext(); stream.println(i.nextDouble()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeDoubles(DoubleIterator i, File file) throws IOException {
/* 1043 */     PrintStream stream = new PrintStream(new FastBufferedOutputStream(new FileOutputStream(file)));
/* 1044 */     storeDoubles(i, stream);
/* 1045 */     stream.close();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeDoubles(DoubleIterator i, CharSequence filename) throws IOException {
/* 1053 */     storeDoubles(i, new File(filename.toString()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long loadDoubles(BufferedReader reader, double[][] array, long offset, long length) throws IOException {
/* 1064 */     DoubleBigArrays.ensureOffsetLength(array, offset, length);
/* 1065 */     long c = 0L;
/*      */     
/*      */     try {
/* 1068 */       for (int i = BigArrays.segment(offset); i < BigArrays.segment(offset + length + 134217727L); i++) {
/* 1069 */         double[] t = array[i];
/* 1070 */         int l = (int)Math.min(t.length, offset + length - BigArrays.start(i));
/* 1071 */         for (int d = (int)Math.max(0L, offset - BigArrays.start(i)); d < l; d++) {
/* 1072 */           String s; if ((s = reader.readLine()) != null) { t[d] = Double.parseDouble(s.trim()); }
/* 1073 */           else { return c; }
/* 1074 */            c++;
/*      */         }
/*      */       
/*      */       } 
/* 1078 */     } catch (EOFException eOFException) {}
/* 1079 */     return c;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long loadDoubles(BufferedReader reader, double[][] array) throws IOException {
/* 1088 */     return loadDoubles(reader, array, 0L, DoubleBigArrays.length(array));
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
/* 1099 */     BufferedReader reader = new BufferedReader(new FileReader(file));
/* 1100 */     long result = loadDoubles(reader, array, offset, length);
/* 1101 */     reader.close();
/* 1102 */     return result;
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
/* 1113 */     return loadDoubles(new File(filename.toString()), array, offset, length);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long loadDoubles(File file, double[][] array) throws IOException {
/* 1122 */     return loadDoubles(file, array, 0L, DoubleBigArrays.length(array));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long loadDoubles(CharSequence filename, double[][] array) throws IOException {
/* 1131 */     return loadDoubles(filename, array, 0L, DoubleBigArrays.length(array));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeDoubles(double[][] array, long offset, long length, PrintStream stream) {
/* 1141 */     DoubleBigArrays.ensureOffsetLength(array, offset, length);
/* 1142 */     for (int i = BigArrays.segment(offset); i < BigArrays.segment(offset + length + 134217727L); i++) {
/* 1143 */       double[] t = array[i];
/* 1144 */       int l = (int)Math.min(t.length, offset + length - BigArrays.start(i));
/* 1145 */       for (int d = (int)Math.max(0L, offset - BigArrays.start(i)); d < l; ) { stream.println(t[d]); d++; }
/*      */     
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeDoubles(double[][] array, PrintStream stream) {
/* 1154 */     storeDoubles(array, 0L, DoubleBigArrays.length(array), stream);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeDoubles(double[][] array, long offset, long length, File file) throws IOException {
/* 1164 */     PrintStream stream = new PrintStream(new FastBufferedOutputStream(new FileOutputStream(file)));
/* 1165 */     storeDoubles(array, offset, length, stream);
/* 1166 */     stream.close();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeDoubles(double[][] array, long offset, long length, CharSequence filename) throws IOException {
/* 1176 */     storeDoubles(array, offset, length, new File(filename.toString()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeDoubles(double[][] array, File file) throws IOException {
/* 1184 */     storeDoubles(array, 0L, DoubleBigArrays.length(array), file);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeDoubles(double[][] array, CharSequence filename) throws IOException {
/* 1192 */     storeDoubles(array, 0L, DoubleBigArrays.length(array), filename);
/*      */   }
/*      */   
/*      */   private static final class DoubleReaderWrapper implements DoubleIterator { private final BufferedReader reader;
/*      */     private boolean toAdvance = true;
/*      */     private String s;
/*      */     private double next;
/*      */     
/*      */     public DoubleReaderWrapper(BufferedReader reader) {
/* 1201 */       this.reader = reader;
/*      */     }
/*      */     
/*      */     public boolean hasNext() {
/* 1205 */       if (!this.toAdvance) return (this.s != null); 
/* 1206 */       this.toAdvance = false;
/*      */       
/* 1208 */       try { this.s = this.reader.readLine(); }
/*      */       
/* 1210 */       catch (EOFException eOFException) {  }
/* 1211 */       catch (IOException rethrow) { throw new RuntimeException(rethrow); }
/* 1212 */        if (this.s == null) return false; 
/* 1213 */       this.next = Double.parseDouble(this.s.trim());
/* 1214 */       return true;
/*      */     }
/*      */     
/*      */     public double nextDouble() {
/* 1218 */       if (!hasNext()) throw new NoSuchElementException(); 
/* 1219 */       this.toAdvance = true;
/* 1220 */       return this.next;
/*      */     } }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static DoubleIterator asDoubleIterator(BufferedReader reader) {
/* 1228 */     return new DoubleReaderWrapper(reader);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static DoubleIterator asDoubleIterator(File file) throws IOException {
/* 1235 */     return new DoubleReaderWrapper(new BufferedReader(new FileReader(file)));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static DoubleIterator asDoubleIterator(CharSequence filename) throws IOException {
/* 1242 */     return asDoubleIterator(new File(filename.toString()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static DoubleIterable asDoubleIterable(File file) {
/* 1249 */     return () -> { try {
/*      */           return asDoubleIterator(file);
/* 1251 */         } catch (IOException e) {
/*      */           throw new RuntimeException(e);
/*      */         } 
/*      */       };
/*      */   }
/*      */ 
/*      */   
/*      */   public static DoubleIterable asDoubleIterable(CharSequence filename) {
/* 1259 */     return () -> { try {
/*      */           return asDoubleIterator(filename);
/* 1261 */         } catch (IOException e) {
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
/*      */   public static int loadBooleans(BufferedReader reader, boolean[] array, int offset, int length) throws IOException {
/* 1311 */     BooleanArrays.ensureOffsetLength(array, offset, length);
/* 1312 */     int i = 0;
/*      */     try {
/*      */       String s;
/* 1315 */       for (i = 0; i < length && (
/* 1316 */         s = reader.readLine()) != null; i++) array[i + offset] = Boolean.parseBoolean(s.trim());
/*      */     
/*      */     }
/* 1319 */     catch (EOFException eOFException) {}
/* 1320 */     return i;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int loadBooleans(BufferedReader reader, boolean[] array) throws IOException {
/* 1329 */     return loadBooleans(reader, array, 0, array.length);
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
/* 1340 */     BufferedReader reader = new BufferedReader(new FileReader(file));
/* 1341 */     int result = loadBooleans(reader, array, offset, length);
/* 1342 */     reader.close();
/* 1343 */     return result;
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
/* 1354 */     return loadBooleans(new File(filename.toString()), array, offset, length);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int loadBooleans(File file, boolean[] array) throws IOException {
/* 1363 */     return loadBooleans(file, array, 0, array.length);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int loadBooleans(CharSequence filename, boolean[] array) throws IOException {
/* 1372 */     return loadBooleans(filename, array, 0, array.length);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeBooleans(boolean[] array, int offset, int length, PrintStream stream) {
/* 1382 */     BooleanArrays.ensureOffsetLength(array, offset, length);
/* 1383 */     for (int i = 0; i < length; ) { stream.println(array[offset + i]); i++; }
/*      */   
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeBooleans(boolean[] array, PrintStream stream) {
/* 1391 */     storeBooleans(array, 0, array.length, stream);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeBooleans(boolean[] array, int offset, int length, File file) throws IOException {
/* 1401 */     PrintStream stream = new PrintStream(new FastBufferedOutputStream(new FileOutputStream(file)));
/* 1402 */     storeBooleans(array, offset, length, stream);
/* 1403 */     stream.close();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeBooleans(boolean[] array, int offset, int length, CharSequence filename) throws IOException {
/* 1413 */     storeBooleans(array, offset, length, new File(filename.toString()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeBooleans(boolean[] array, File file) throws IOException {
/* 1421 */     storeBooleans(array, 0, array.length, file);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeBooleans(boolean[] array, CharSequence filename) throws IOException {
/* 1429 */     storeBooleans(array, 0, array.length, filename);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeBooleans(BooleanIterator i, PrintStream stream) {
/* 1437 */     for (; i.hasNext(); stream.println(i.nextBoolean()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeBooleans(BooleanIterator i, File file) throws IOException {
/* 1445 */     PrintStream stream = new PrintStream(new FastBufferedOutputStream(new FileOutputStream(file)));
/* 1446 */     storeBooleans(i, stream);
/* 1447 */     stream.close();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeBooleans(BooleanIterator i, CharSequence filename) throws IOException {
/* 1455 */     storeBooleans(i, new File(filename.toString()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long loadBooleans(BufferedReader reader, boolean[][] array, long offset, long length) throws IOException {
/* 1466 */     BooleanBigArrays.ensureOffsetLength(array, offset, length);
/* 1467 */     long c = 0L;
/*      */     
/*      */     try {
/* 1470 */       for (int i = BigArrays.segment(offset); i < BigArrays.segment(offset + length + 134217727L); i++) {
/* 1471 */         boolean[] t = array[i];
/* 1472 */         int l = (int)Math.min(t.length, offset + length - BigArrays.start(i));
/* 1473 */         for (int d = (int)Math.max(0L, offset - BigArrays.start(i)); d < l; d++) {
/* 1474 */           String s; if ((s = reader.readLine()) != null) { t[d] = Boolean.parseBoolean(s.trim()); }
/* 1475 */           else { return c; }
/* 1476 */            c++;
/*      */         }
/*      */       
/*      */       } 
/* 1480 */     } catch (EOFException eOFException) {}
/* 1481 */     return c;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long loadBooleans(BufferedReader reader, boolean[][] array) throws IOException {
/* 1490 */     return loadBooleans(reader, array, 0L, BooleanBigArrays.length(array));
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
/* 1501 */     BufferedReader reader = new BufferedReader(new FileReader(file));
/* 1502 */     long result = loadBooleans(reader, array, offset, length);
/* 1503 */     reader.close();
/* 1504 */     return result;
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
/* 1515 */     return loadBooleans(new File(filename.toString()), array, offset, length);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long loadBooleans(File file, boolean[][] array) throws IOException {
/* 1524 */     return loadBooleans(file, array, 0L, BooleanBigArrays.length(array));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long loadBooleans(CharSequence filename, boolean[][] array) throws IOException {
/* 1533 */     return loadBooleans(filename, array, 0L, BooleanBigArrays.length(array));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeBooleans(boolean[][] array, long offset, long length, PrintStream stream) {
/* 1543 */     BooleanBigArrays.ensureOffsetLength(array, offset, length);
/* 1544 */     for (int i = BigArrays.segment(offset); i < BigArrays.segment(offset + length + 134217727L); i++) {
/* 1545 */       boolean[] t = array[i];
/* 1546 */       int l = (int)Math.min(t.length, offset + length - BigArrays.start(i));
/* 1547 */       for (int d = (int)Math.max(0L, offset - BigArrays.start(i)); d < l; ) { stream.println(t[d]); d++; }
/*      */     
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeBooleans(boolean[][] array, PrintStream stream) {
/* 1556 */     storeBooleans(array, 0L, BooleanBigArrays.length(array), stream);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeBooleans(boolean[][] array, long offset, long length, File file) throws IOException {
/* 1566 */     PrintStream stream = new PrintStream(new FastBufferedOutputStream(new FileOutputStream(file)));
/* 1567 */     storeBooleans(array, offset, length, stream);
/* 1568 */     stream.close();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeBooleans(boolean[][] array, long offset, long length, CharSequence filename) throws IOException {
/* 1578 */     storeBooleans(array, offset, length, new File(filename.toString()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeBooleans(boolean[][] array, File file) throws IOException {
/* 1586 */     storeBooleans(array, 0L, BooleanBigArrays.length(array), file);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeBooleans(boolean[][] array, CharSequence filename) throws IOException {
/* 1594 */     storeBooleans(array, 0L, BooleanBigArrays.length(array), filename);
/*      */   }
/*      */   
/*      */   private static final class BooleanReaderWrapper implements BooleanIterator { private final BufferedReader reader;
/*      */     private boolean toAdvance = true;
/*      */     private String s;
/*      */     private boolean next;
/*      */     
/*      */     public BooleanReaderWrapper(BufferedReader reader) {
/* 1603 */       this.reader = reader;
/*      */     }
/*      */     
/*      */     public boolean hasNext() {
/* 1607 */       if (!this.toAdvance) return (this.s != null); 
/* 1608 */       this.toAdvance = false;
/*      */       
/* 1610 */       try { this.s = this.reader.readLine(); }
/*      */       
/* 1612 */       catch (EOFException eOFException) {  }
/* 1613 */       catch (IOException rethrow) { throw new RuntimeException(rethrow); }
/* 1614 */        if (this.s == null) return false; 
/* 1615 */       this.next = Boolean.parseBoolean(this.s.trim());
/* 1616 */       return true;
/*      */     }
/*      */     
/*      */     public boolean nextBoolean() {
/* 1620 */       if (!hasNext()) throw new NoSuchElementException(); 
/* 1621 */       this.toAdvance = true;
/* 1622 */       return this.next;
/*      */     } }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static BooleanIterator asBooleanIterator(BufferedReader reader) {
/* 1630 */     return new BooleanReaderWrapper(reader);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static BooleanIterator asBooleanIterator(File file) throws IOException {
/* 1637 */     return new BooleanReaderWrapper(new BufferedReader(new FileReader(file)));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static BooleanIterator asBooleanIterator(CharSequence filename) throws IOException {
/* 1644 */     return asBooleanIterator(new File(filename.toString()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static BooleanIterable asBooleanIterable(File file) {
/* 1651 */     return () -> { try {
/*      */           return asBooleanIterator(file);
/* 1653 */         } catch (IOException e) {
/*      */           throw new RuntimeException(e);
/*      */         } 
/*      */       };
/*      */   }
/*      */ 
/*      */   
/*      */   public static BooleanIterable asBooleanIterable(CharSequence filename) {
/* 1661 */     return () -> { try {
/*      */           return asBooleanIterator(filename);
/* 1663 */         } catch (IOException e) {
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
/*      */   public static int loadBytes(BufferedReader reader, byte[] array, int offset, int length) throws IOException {
/* 1713 */     ByteArrays.ensureOffsetLength(array, offset, length);
/* 1714 */     int i = 0;
/*      */     try {
/*      */       String s;
/* 1717 */       for (i = 0; i < length && (
/* 1718 */         s = reader.readLine()) != null; i++) array[i + offset] = Byte.parseByte(s.trim());
/*      */     
/*      */     }
/* 1721 */     catch (EOFException eOFException) {}
/* 1722 */     return i;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int loadBytes(BufferedReader reader, byte[] array) throws IOException {
/* 1731 */     return loadBytes(reader, array, 0, array.length);
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
/* 1742 */     BufferedReader reader = new BufferedReader(new FileReader(file));
/* 1743 */     int result = loadBytes(reader, array, offset, length);
/* 1744 */     reader.close();
/* 1745 */     return result;
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
/* 1756 */     return loadBytes(new File(filename.toString()), array, offset, length);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int loadBytes(File file, byte[] array) throws IOException {
/* 1765 */     return loadBytes(file, array, 0, array.length);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int loadBytes(CharSequence filename, byte[] array) throws IOException {
/* 1774 */     return loadBytes(filename, array, 0, array.length);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeBytes(byte[] array, int offset, int length, PrintStream stream) {
/* 1784 */     ByteArrays.ensureOffsetLength(array, offset, length);
/* 1785 */     for (int i = 0; i < length; ) { stream.println(array[offset + i]); i++; }
/*      */   
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeBytes(byte[] array, PrintStream stream) {
/* 1793 */     storeBytes(array, 0, array.length, stream);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeBytes(byte[] array, int offset, int length, File file) throws IOException {
/* 1803 */     PrintStream stream = new PrintStream(new FastBufferedOutputStream(new FileOutputStream(file)));
/* 1804 */     storeBytes(array, offset, length, stream);
/* 1805 */     stream.close();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeBytes(byte[] array, int offset, int length, CharSequence filename) throws IOException {
/* 1815 */     storeBytes(array, offset, length, new File(filename.toString()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeBytes(byte[] array, File file) throws IOException {
/* 1823 */     storeBytes(array, 0, array.length, file);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeBytes(byte[] array, CharSequence filename) throws IOException {
/* 1831 */     storeBytes(array, 0, array.length, filename);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeBytes(ByteIterator i, PrintStream stream) {
/* 1839 */     for (; i.hasNext(); stream.println(i.nextByte()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeBytes(ByteIterator i, File file) throws IOException {
/* 1847 */     PrintStream stream = new PrintStream(new FastBufferedOutputStream(new FileOutputStream(file)));
/* 1848 */     storeBytes(i, stream);
/* 1849 */     stream.close();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeBytes(ByteIterator i, CharSequence filename) throws IOException {
/* 1857 */     storeBytes(i, new File(filename.toString()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long loadBytes(BufferedReader reader, byte[][] array, long offset, long length) throws IOException {
/* 1868 */     ByteBigArrays.ensureOffsetLength(array, offset, length);
/* 1869 */     long c = 0L;
/*      */     
/*      */     try {
/* 1872 */       for (int i = BigArrays.segment(offset); i < BigArrays.segment(offset + length + 134217727L); i++) {
/* 1873 */         byte[] t = array[i];
/* 1874 */         int l = (int)Math.min(t.length, offset + length - BigArrays.start(i));
/* 1875 */         for (int d = (int)Math.max(0L, offset - BigArrays.start(i)); d < l; d++) {
/* 1876 */           String s; if ((s = reader.readLine()) != null) { t[d] = Byte.parseByte(s.trim()); }
/* 1877 */           else { return c; }
/* 1878 */            c++;
/*      */         }
/*      */       
/*      */       } 
/* 1882 */     } catch (EOFException eOFException) {}
/* 1883 */     return c;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long loadBytes(BufferedReader reader, byte[][] array) throws IOException {
/* 1892 */     return loadBytes(reader, array, 0L, ByteBigArrays.length(array));
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
/* 1903 */     BufferedReader reader = new BufferedReader(new FileReader(file));
/* 1904 */     long result = loadBytes(reader, array, offset, length);
/* 1905 */     reader.close();
/* 1906 */     return result;
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
/* 1917 */     return loadBytes(new File(filename.toString()), array, offset, length);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long loadBytes(File file, byte[][] array) throws IOException {
/* 1926 */     return loadBytes(file, array, 0L, ByteBigArrays.length(array));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long loadBytes(CharSequence filename, byte[][] array) throws IOException {
/* 1935 */     return loadBytes(filename, array, 0L, ByteBigArrays.length(array));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeBytes(byte[][] array, long offset, long length, PrintStream stream) {
/* 1945 */     ByteBigArrays.ensureOffsetLength(array, offset, length);
/* 1946 */     for (int i = BigArrays.segment(offset); i < BigArrays.segment(offset + length + 134217727L); i++) {
/* 1947 */       byte[] t = array[i];
/* 1948 */       int l = (int)Math.min(t.length, offset + length - BigArrays.start(i));
/* 1949 */       for (int d = (int)Math.max(0L, offset - BigArrays.start(i)); d < l; ) { stream.println(t[d]); d++; }
/*      */     
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeBytes(byte[][] array, PrintStream stream) {
/* 1958 */     storeBytes(array, 0L, ByteBigArrays.length(array), stream);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeBytes(byte[][] array, long offset, long length, File file) throws IOException {
/* 1968 */     PrintStream stream = new PrintStream(new FastBufferedOutputStream(new FileOutputStream(file)));
/* 1969 */     storeBytes(array, offset, length, stream);
/* 1970 */     stream.close();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeBytes(byte[][] array, long offset, long length, CharSequence filename) throws IOException {
/* 1980 */     storeBytes(array, offset, length, new File(filename.toString()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeBytes(byte[][] array, File file) throws IOException {
/* 1988 */     storeBytes(array, 0L, ByteBigArrays.length(array), file);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeBytes(byte[][] array, CharSequence filename) throws IOException {
/* 1996 */     storeBytes(array, 0L, ByteBigArrays.length(array), filename);
/*      */   }
/*      */   
/*      */   private static final class ByteReaderWrapper implements ByteIterator { private final BufferedReader reader;
/*      */     private boolean toAdvance = true;
/*      */     private String s;
/*      */     private byte next;
/*      */     
/*      */     public ByteReaderWrapper(BufferedReader reader) {
/* 2005 */       this.reader = reader;
/*      */     }
/*      */     
/*      */     public boolean hasNext() {
/* 2009 */       if (!this.toAdvance) return (this.s != null); 
/* 2010 */       this.toAdvance = false;
/*      */       
/* 2012 */       try { this.s = this.reader.readLine(); }
/*      */       
/* 2014 */       catch (EOFException eOFException) {  }
/* 2015 */       catch (IOException rethrow) { throw new RuntimeException(rethrow); }
/* 2016 */        if (this.s == null) return false; 
/* 2017 */       this.next = Byte.parseByte(this.s.trim());
/* 2018 */       return true;
/*      */     }
/*      */     
/*      */     public byte nextByte() {
/* 2022 */       if (!hasNext()) throw new NoSuchElementException(); 
/* 2023 */       this.toAdvance = true;
/* 2024 */       return this.next;
/*      */     } }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static ByteIterator asByteIterator(BufferedReader reader) {
/* 2032 */     return new ByteReaderWrapper(reader);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static ByteIterator asByteIterator(File file) throws IOException {
/* 2039 */     return new ByteReaderWrapper(new BufferedReader(new FileReader(file)));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static ByteIterator asByteIterator(CharSequence filename) throws IOException {
/* 2046 */     return asByteIterator(new File(filename.toString()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static ByteIterable asByteIterable(File file) {
/* 2053 */     return () -> { try {
/*      */           return asByteIterator(file);
/* 2055 */         } catch (IOException e) {
/*      */           throw new RuntimeException(e);
/*      */         } 
/*      */       };
/*      */   }
/*      */ 
/*      */   
/*      */   public static ByteIterable asByteIterable(CharSequence filename) {
/* 2063 */     return () -> { try {
/*      */           return asByteIterator(filename);
/* 2065 */         } catch (IOException e) {
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
/*      */   public static int loadShorts(BufferedReader reader, short[] array, int offset, int length) throws IOException {
/* 2115 */     ShortArrays.ensureOffsetLength(array, offset, length);
/* 2116 */     int i = 0;
/*      */     try {
/*      */       String s;
/* 2119 */       for (i = 0; i < length && (
/* 2120 */         s = reader.readLine()) != null; i++) array[i + offset] = Short.parseShort(s.trim());
/*      */     
/*      */     }
/* 2123 */     catch (EOFException eOFException) {}
/* 2124 */     return i;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int loadShorts(BufferedReader reader, short[] array) throws IOException {
/* 2133 */     return loadShorts(reader, array, 0, array.length);
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
/* 2144 */     BufferedReader reader = new BufferedReader(new FileReader(file));
/* 2145 */     int result = loadShorts(reader, array, offset, length);
/* 2146 */     reader.close();
/* 2147 */     return result;
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
/* 2158 */     return loadShorts(new File(filename.toString()), array, offset, length);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int loadShorts(File file, short[] array) throws IOException {
/* 2167 */     return loadShorts(file, array, 0, array.length);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int loadShorts(CharSequence filename, short[] array) throws IOException {
/* 2176 */     return loadShorts(filename, array, 0, array.length);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeShorts(short[] array, int offset, int length, PrintStream stream) {
/* 2186 */     ShortArrays.ensureOffsetLength(array, offset, length);
/* 2187 */     for (int i = 0; i < length; ) { stream.println(array[offset + i]); i++; }
/*      */   
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeShorts(short[] array, PrintStream stream) {
/* 2195 */     storeShorts(array, 0, array.length, stream);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeShorts(short[] array, int offset, int length, File file) throws IOException {
/* 2205 */     PrintStream stream = new PrintStream(new FastBufferedOutputStream(new FileOutputStream(file)));
/* 2206 */     storeShorts(array, offset, length, stream);
/* 2207 */     stream.close();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeShorts(short[] array, int offset, int length, CharSequence filename) throws IOException {
/* 2217 */     storeShorts(array, offset, length, new File(filename.toString()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeShorts(short[] array, File file) throws IOException {
/* 2225 */     storeShorts(array, 0, array.length, file);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeShorts(short[] array, CharSequence filename) throws IOException {
/* 2233 */     storeShorts(array, 0, array.length, filename);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeShorts(ShortIterator i, PrintStream stream) {
/* 2241 */     for (; i.hasNext(); stream.println(i.nextShort()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeShorts(ShortIterator i, File file) throws IOException {
/* 2249 */     PrintStream stream = new PrintStream(new FastBufferedOutputStream(new FileOutputStream(file)));
/* 2250 */     storeShorts(i, stream);
/* 2251 */     stream.close();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeShorts(ShortIterator i, CharSequence filename) throws IOException {
/* 2259 */     storeShorts(i, new File(filename.toString()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long loadShorts(BufferedReader reader, short[][] array, long offset, long length) throws IOException {
/* 2270 */     ShortBigArrays.ensureOffsetLength(array, offset, length);
/* 2271 */     long c = 0L;
/*      */     
/*      */     try {
/* 2274 */       for (int i = BigArrays.segment(offset); i < BigArrays.segment(offset + length + 134217727L); i++) {
/* 2275 */         short[] t = array[i];
/* 2276 */         int l = (int)Math.min(t.length, offset + length - BigArrays.start(i));
/* 2277 */         for (int d = (int)Math.max(0L, offset - BigArrays.start(i)); d < l; d++) {
/* 2278 */           String s; if ((s = reader.readLine()) != null) { t[d] = Short.parseShort(s.trim()); }
/* 2279 */           else { return c; }
/* 2280 */            c++;
/*      */         }
/*      */       
/*      */       } 
/* 2284 */     } catch (EOFException eOFException) {}
/* 2285 */     return c;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long loadShorts(BufferedReader reader, short[][] array) throws IOException {
/* 2294 */     return loadShorts(reader, array, 0L, ShortBigArrays.length(array));
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
/* 2305 */     BufferedReader reader = new BufferedReader(new FileReader(file));
/* 2306 */     long result = loadShorts(reader, array, offset, length);
/* 2307 */     reader.close();
/* 2308 */     return result;
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
/* 2319 */     return loadShorts(new File(filename.toString()), array, offset, length);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long loadShorts(File file, short[][] array) throws IOException {
/* 2328 */     return loadShorts(file, array, 0L, ShortBigArrays.length(array));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long loadShorts(CharSequence filename, short[][] array) throws IOException {
/* 2337 */     return loadShorts(filename, array, 0L, ShortBigArrays.length(array));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeShorts(short[][] array, long offset, long length, PrintStream stream) {
/* 2347 */     ShortBigArrays.ensureOffsetLength(array, offset, length);
/* 2348 */     for (int i = BigArrays.segment(offset); i < BigArrays.segment(offset + length + 134217727L); i++) {
/* 2349 */       short[] t = array[i];
/* 2350 */       int l = (int)Math.min(t.length, offset + length - BigArrays.start(i));
/* 2351 */       for (int d = (int)Math.max(0L, offset - BigArrays.start(i)); d < l; ) { stream.println(t[d]); d++; }
/*      */     
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeShorts(short[][] array, PrintStream stream) {
/* 2360 */     storeShorts(array, 0L, ShortBigArrays.length(array), stream);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeShorts(short[][] array, long offset, long length, File file) throws IOException {
/* 2370 */     PrintStream stream = new PrintStream(new FastBufferedOutputStream(new FileOutputStream(file)));
/* 2371 */     storeShorts(array, offset, length, stream);
/* 2372 */     stream.close();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeShorts(short[][] array, long offset, long length, CharSequence filename) throws IOException {
/* 2382 */     storeShorts(array, offset, length, new File(filename.toString()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeShorts(short[][] array, File file) throws IOException {
/* 2390 */     storeShorts(array, 0L, ShortBigArrays.length(array), file);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeShorts(short[][] array, CharSequence filename) throws IOException {
/* 2398 */     storeShorts(array, 0L, ShortBigArrays.length(array), filename);
/*      */   }
/*      */   
/*      */   private static final class ShortReaderWrapper implements ShortIterator { private final BufferedReader reader;
/*      */     private boolean toAdvance = true;
/*      */     private String s;
/*      */     private short next;
/*      */     
/*      */     public ShortReaderWrapper(BufferedReader reader) {
/* 2407 */       this.reader = reader;
/*      */     }
/*      */     
/*      */     public boolean hasNext() {
/* 2411 */       if (!this.toAdvance) return (this.s != null); 
/* 2412 */       this.toAdvance = false;
/*      */       
/* 2414 */       try { this.s = this.reader.readLine(); }
/*      */       
/* 2416 */       catch (EOFException eOFException) {  }
/* 2417 */       catch (IOException rethrow) { throw new RuntimeException(rethrow); }
/* 2418 */        if (this.s == null) return false; 
/* 2419 */       this.next = Short.parseShort(this.s.trim());
/* 2420 */       return true;
/*      */     }
/*      */     
/*      */     public short nextShort() {
/* 2424 */       if (!hasNext()) throw new NoSuchElementException(); 
/* 2425 */       this.toAdvance = true;
/* 2426 */       return this.next;
/*      */     } }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static ShortIterator asShortIterator(BufferedReader reader) {
/* 2434 */     return new ShortReaderWrapper(reader);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static ShortIterator asShortIterator(File file) throws IOException {
/* 2441 */     return new ShortReaderWrapper(new BufferedReader(new FileReader(file)));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static ShortIterator asShortIterator(CharSequence filename) throws IOException {
/* 2448 */     return asShortIterator(new File(filename.toString()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static ShortIterable asShortIterable(File file) {
/* 2455 */     return () -> { try {
/*      */           return asShortIterator(file);
/* 2457 */         } catch (IOException e) {
/*      */           throw new RuntimeException(e);
/*      */         } 
/*      */       };
/*      */   }
/*      */ 
/*      */   
/*      */   public static ShortIterable asShortIterable(CharSequence filename) {
/* 2465 */     return () -> { try {
/*      */           return asShortIterator(filename);
/* 2467 */         } catch (IOException e) {
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
/*      */   public static int loadFloats(BufferedReader reader, float[] array, int offset, int length) throws IOException {
/* 2517 */     FloatArrays.ensureOffsetLength(array, offset, length);
/* 2518 */     int i = 0;
/*      */     try {
/*      */       String s;
/* 2521 */       for (i = 0; i < length && (
/* 2522 */         s = reader.readLine()) != null; i++) array[i + offset] = Float.parseFloat(s.trim());
/*      */     
/*      */     }
/* 2525 */     catch (EOFException eOFException) {}
/* 2526 */     return i;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int loadFloats(BufferedReader reader, float[] array) throws IOException {
/* 2535 */     return loadFloats(reader, array, 0, array.length);
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
/* 2546 */     BufferedReader reader = new BufferedReader(new FileReader(file));
/* 2547 */     int result = loadFloats(reader, array, offset, length);
/* 2548 */     reader.close();
/* 2549 */     return result;
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
/* 2560 */     return loadFloats(new File(filename.toString()), array, offset, length);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int loadFloats(File file, float[] array) throws IOException {
/* 2569 */     return loadFloats(file, array, 0, array.length);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int loadFloats(CharSequence filename, float[] array) throws IOException {
/* 2578 */     return loadFloats(filename, array, 0, array.length);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeFloats(float[] array, int offset, int length, PrintStream stream) {
/* 2588 */     FloatArrays.ensureOffsetLength(array, offset, length);
/* 2589 */     for (int i = 0; i < length; ) { stream.println(array[offset + i]); i++; }
/*      */   
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeFloats(float[] array, PrintStream stream) {
/* 2597 */     storeFloats(array, 0, array.length, stream);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeFloats(float[] array, int offset, int length, File file) throws IOException {
/* 2607 */     PrintStream stream = new PrintStream(new FastBufferedOutputStream(new FileOutputStream(file)));
/* 2608 */     storeFloats(array, offset, length, stream);
/* 2609 */     stream.close();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeFloats(float[] array, int offset, int length, CharSequence filename) throws IOException {
/* 2619 */     storeFloats(array, offset, length, new File(filename.toString()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeFloats(float[] array, File file) throws IOException {
/* 2627 */     storeFloats(array, 0, array.length, file);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeFloats(float[] array, CharSequence filename) throws IOException {
/* 2635 */     storeFloats(array, 0, array.length, filename);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeFloats(FloatIterator i, PrintStream stream) {
/* 2643 */     for (; i.hasNext(); stream.println(i.nextFloat()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeFloats(FloatIterator i, File file) throws IOException {
/* 2651 */     PrintStream stream = new PrintStream(new FastBufferedOutputStream(new FileOutputStream(file)));
/* 2652 */     storeFloats(i, stream);
/* 2653 */     stream.close();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeFloats(FloatIterator i, CharSequence filename) throws IOException {
/* 2661 */     storeFloats(i, new File(filename.toString()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long loadFloats(BufferedReader reader, float[][] array, long offset, long length) throws IOException {
/* 2672 */     FloatBigArrays.ensureOffsetLength(array, offset, length);
/* 2673 */     long c = 0L;
/*      */     
/*      */     try {
/* 2676 */       for (int i = BigArrays.segment(offset); i < BigArrays.segment(offset + length + 134217727L); i++) {
/* 2677 */         float[] t = array[i];
/* 2678 */         int l = (int)Math.min(t.length, offset + length - BigArrays.start(i));
/* 2679 */         for (int d = (int)Math.max(0L, offset - BigArrays.start(i)); d < l; d++) {
/* 2680 */           String s; if ((s = reader.readLine()) != null) { t[d] = Float.parseFloat(s.trim()); }
/* 2681 */           else { return c; }
/* 2682 */            c++;
/*      */         }
/*      */       
/*      */       } 
/* 2686 */     } catch (EOFException eOFException) {}
/* 2687 */     return c;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long loadFloats(BufferedReader reader, float[][] array) throws IOException {
/* 2696 */     return loadFloats(reader, array, 0L, FloatBigArrays.length(array));
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
/* 2707 */     BufferedReader reader = new BufferedReader(new FileReader(file));
/* 2708 */     long result = loadFloats(reader, array, offset, length);
/* 2709 */     reader.close();
/* 2710 */     return result;
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
/* 2721 */     return loadFloats(new File(filename.toString()), array, offset, length);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long loadFloats(File file, float[][] array) throws IOException {
/* 2730 */     return loadFloats(file, array, 0L, FloatBigArrays.length(array));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long loadFloats(CharSequence filename, float[][] array) throws IOException {
/* 2739 */     return loadFloats(filename, array, 0L, FloatBigArrays.length(array));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeFloats(float[][] array, long offset, long length, PrintStream stream) {
/* 2749 */     FloatBigArrays.ensureOffsetLength(array, offset, length);
/* 2750 */     for (int i = BigArrays.segment(offset); i < BigArrays.segment(offset + length + 134217727L); i++) {
/* 2751 */       float[] t = array[i];
/* 2752 */       int l = (int)Math.min(t.length, offset + length - BigArrays.start(i));
/* 2753 */       for (int d = (int)Math.max(0L, offset - BigArrays.start(i)); d < l; ) { stream.println(t[d]); d++; }
/*      */     
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeFloats(float[][] array, PrintStream stream) {
/* 2762 */     storeFloats(array, 0L, FloatBigArrays.length(array), stream);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeFloats(float[][] array, long offset, long length, File file) throws IOException {
/* 2772 */     PrintStream stream = new PrintStream(new FastBufferedOutputStream(new FileOutputStream(file)));
/* 2773 */     storeFloats(array, offset, length, stream);
/* 2774 */     stream.close();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeFloats(float[][] array, long offset, long length, CharSequence filename) throws IOException {
/* 2784 */     storeFloats(array, offset, length, new File(filename.toString()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeFloats(float[][] array, File file) throws IOException {
/* 2792 */     storeFloats(array, 0L, FloatBigArrays.length(array), file);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeFloats(float[][] array, CharSequence filename) throws IOException {
/* 2800 */     storeFloats(array, 0L, FloatBigArrays.length(array), filename);
/*      */   }
/*      */   
/*      */   private static final class FloatReaderWrapper implements FloatIterator { private final BufferedReader reader;
/*      */     private boolean toAdvance = true;
/*      */     private String s;
/*      */     private float next;
/*      */     
/*      */     public FloatReaderWrapper(BufferedReader reader) {
/* 2809 */       this.reader = reader;
/*      */     }
/*      */     
/*      */     public boolean hasNext() {
/* 2813 */       if (!this.toAdvance) return (this.s != null); 
/* 2814 */       this.toAdvance = false;
/*      */       
/* 2816 */       try { this.s = this.reader.readLine(); }
/*      */       
/* 2818 */       catch (EOFException eOFException) {  }
/* 2819 */       catch (IOException rethrow) { throw new RuntimeException(rethrow); }
/* 2820 */        if (this.s == null) return false; 
/* 2821 */       this.next = Float.parseFloat(this.s.trim());
/* 2822 */       return true;
/*      */     }
/*      */     
/*      */     public float nextFloat() {
/* 2826 */       if (!hasNext()) throw new NoSuchElementException(); 
/* 2827 */       this.toAdvance = true;
/* 2828 */       return this.next;
/*      */     } }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static FloatIterator asFloatIterator(BufferedReader reader) {
/* 2836 */     return new FloatReaderWrapper(reader);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static FloatIterator asFloatIterator(File file) throws IOException {
/* 2843 */     return new FloatReaderWrapper(new BufferedReader(new FileReader(file)));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static FloatIterator asFloatIterator(CharSequence filename) throws IOException {
/* 2850 */     return asFloatIterator(new File(filename.toString()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static FloatIterable asFloatIterable(File file) {
/* 2857 */     return () -> { try {
/*      */           return asFloatIterator(file);
/* 2859 */         } catch (IOException e) {
/*      */           throw new RuntimeException(e);
/*      */         } 
/*      */       };
/*      */   }
/*      */ 
/*      */   
/*      */   public static FloatIterable asFloatIterable(CharSequence filename) {
/* 2867 */     return () -> { try {
/*      */           return asFloatIterator(filename);
/* 2869 */         } catch (IOException e) {
/*      */           throw new RuntimeException(e);
/*      */         } 
/*      */       };
/*      */   }
/*      */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\i\\unimi\dsi\fastutil\io\TextIO.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */