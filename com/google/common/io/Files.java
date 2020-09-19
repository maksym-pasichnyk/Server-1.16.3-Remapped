/*     */ package com.google.common.io;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Joiner;
/*     */ import com.google.common.base.Optional;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Predicate;
/*     */ import com.google.common.base.Splitter;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.TreeTraverser;
/*     */ import com.google.common.hash.HashCode;
/*     */ import com.google.common.hash.HashFunction;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.BufferedWriter;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.OutputStream;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.io.RandomAccessFile;
/*     */ import java.nio.MappedByteBuffer;
/*     */ import java.nio.channels.FileChannel;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public final class Files
/*     */ {
/*     */   private static final int TEMP_DIR_ATTEMPTS = 10000;
/*     */   
/*     */   public static BufferedReader newReader(File file, Charset charset) throws FileNotFoundException {
/*  82 */     Preconditions.checkNotNull(file);
/*  83 */     Preconditions.checkNotNull(charset);
/*  84 */     return new BufferedReader(new InputStreamReader(new FileInputStream(file), charset));
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
/*     */   public static BufferedWriter newWriter(File file, Charset charset) throws FileNotFoundException {
/*  96 */     Preconditions.checkNotNull(file);
/*  97 */     Preconditions.checkNotNull(charset);
/*  98 */     return new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), charset));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ByteSource asByteSource(File file) {
/* 107 */     return new FileByteSource(file);
/*     */   }
/*     */   
/*     */   private static final class FileByteSource
/*     */     extends ByteSource {
/*     */     private final File file;
/*     */     
/*     */     private FileByteSource(File file) {
/* 115 */       this.file = (File)Preconditions.checkNotNull(file);
/*     */     }
/*     */ 
/*     */     
/*     */     public FileInputStream openStream() throws IOException {
/* 120 */       return new FileInputStream(this.file);
/*     */     }
/*     */ 
/*     */     
/*     */     public Optional<Long> sizeIfKnown() {
/* 125 */       if (this.file.isFile()) {
/* 126 */         return Optional.of(Long.valueOf(this.file.length()));
/*     */       }
/* 128 */       return Optional.absent();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public long size() throws IOException {
/* 134 */       if (!this.file.isFile()) {
/* 135 */         throw new FileNotFoundException(this.file.toString());
/*     */       }
/* 137 */       return this.file.length();
/*     */     }
/*     */ 
/*     */     
/*     */     public byte[] read() throws IOException {
/* 142 */       Closer closer = Closer.create();
/*     */       try {
/* 144 */         FileInputStream in = closer.<FileInputStream>register(openStream());
/* 145 */         return Files.readFile(in, in.getChannel().size());
/* 146 */       } catch (Throwable e) {
/* 147 */         throw closer.rethrow(e);
/*     */       } finally {
/* 149 */         closer.close();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 155 */       return "Files.asByteSource(" + this.file + ")";
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static byte[] readFile(InputStream in, long expectedSize) throws IOException {
/* 165 */     if (expectedSize > 2147483647L) {
/* 166 */       throw new OutOfMemoryError("file is too large to fit in a byte array: " + expectedSize + " bytes");
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 172 */     return (expectedSize == 0L) ? 
/* 173 */       ByteStreams.toByteArray(in) : 
/* 174 */       ByteStreams.toByteArray(in, (int)expectedSize);
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
/*     */   public static ByteSink asByteSink(File file, FileWriteMode... modes) {
/* 186 */     return new FileByteSink(file, modes);
/*     */   }
/*     */   
/*     */   private static final class FileByteSink
/*     */     extends ByteSink {
/*     */     private final File file;
/*     */     private final ImmutableSet<FileWriteMode> modes;
/*     */     
/*     */     private FileByteSink(File file, FileWriteMode... modes) {
/* 195 */       this.file = (File)Preconditions.checkNotNull(file);
/* 196 */       this.modes = ImmutableSet.copyOf((Object[])modes);
/*     */     }
/*     */ 
/*     */     
/*     */     public FileOutputStream openStream() throws IOException {
/* 201 */       return new FileOutputStream(this.file, this.modes.contains(FileWriteMode.APPEND));
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 206 */       return "Files.asByteSink(" + this.file + ", " + this.modes + ")";
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static CharSource asCharSource(File file, Charset charset) {
/* 217 */     return asByteSource(file).asCharSource(charset);
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
/*     */   public static CharSink asCharSink(File file, Charset charset, FileWriteMode... modes) {
/* 229 */     return asByteSink(file, modes).asCharSink(charset);
/*     */   }
/*     */   
/*     */   private static FileWriteMode[] modes(boolean append) {
/* 233 */     (new FileWriteMode[1])[0] = FileWriteMode.APPEND; return append ? new FileWriteMode[1] : new FileWriteMode[0];
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
/*     */   public static byte[] toByteArray(File file) throws IOException {
/* 248 */     return asByteSource(file).read();
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
/*     */   public static String toString(File file, Charset charset) throws IOException {
/* 261 */     return asCharSource(file, charset).read();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void write(byte[] from, File to) throws IOException {
/* 272 */     asByteSink(to, new FileWriteMode[0]).write(from);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void copy(File from, OutputStream to) throws IOException {
/* 283 */     asByteSource(from).copyTo(to);
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
/*     */   public static void copy(File from, File to) throws IOException {
/* 303 */     Preconditions.checkArgument(!from.equals(to), "Source %s and destination %s must be different", from, to);
/* 304 */     asByteSource(from).copyTo(asByteSink(to, new FileWriteMode[0]));
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
/*     */   public static void write(CharSequence from, File to, Charset charset) throws IOException {
/* 317 */     asCharSink(to, charset, new FileWriteMode[0]).write(from);
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
/*     */   public static void append(CharSequence from, File to, Charset charset) throws IOException {
/* 330 */     write(from, to, charset, true);
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
/*     */   private static void write(CharSequence from, File to, Charset charset, boolean append) throws IOException {
/* 345 */     asCharSink(to, charset, modes(append)).write(from);
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
/*     */   public static void copy(File from, Charset charset, Appendable to) throws IOException {
/* 358 */     asCharSource(from, charset).copyTo(to);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean equal(File file1, File file2) throws IOException {
/* 367 */     Preconditions.checkNotNull(file1);
/* 368 */     Preconditions.checkNotNull(file2);
/* 369 */     if (file1 == file2 || file1.equals(file2)) {
/* 370 */       return true;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 378 */     long len1 = file1.length();
/* 379 */     long len2 = file2.length();
/* 380 */     if (len1 != 0L && len2 != 0L && len1 != len2) {
/* 381 */       return false;
/*     */     }
/* 383 */     return asByteSource(file1).contentEquals(asByteSource(file2));
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
/*     */   public static File createTempDir() {
/* 403 */     File baseDir = new File(System.getProperty("java.io.tmpdir"));
/* 404 */     String baseName = System.currentTimeMillis() + "-";
/*     */     
/* 406 */     for (int counter = 0; counter < 10000; counter++) {
/* 407 */       File tempDir = new File(baseDir, baseName + counter);
/* 408 */       if (tempDir.mkdir()) {
/* 409 */         return tempDir;
/*     */       }
/*     */     } 
/* 412 */     throw new IllegalStateException("Failed to create directory within 10000 attempts (tried " + baseName + "0 to " + baseName + '✏' + ')');
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
/*     */   public static void touch(File file) throws IOException {
/* 431 */     Preconditions.checkNotNull(file);
/* 432 */     if (!file.createNewFile() && !file.setLastModified(System.currentTimeMillis())) {
/* 433 */       throw new IOException("Unable to update modification time of " + file);
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
/*     */   public static void createParentDirs(File file) throws IOException {
/* 447 */     Preconditions.checkNotNull(file);
/* 448 */     File parent = file.getCanonicalFile().getParentFile();
/* 449 */     if (parent == null) {
/*     */       return;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 458 */     parent.mkdirs();
/* 459 */     if (!parent.isDirectory()) {
/* 460 */       throw new IOException("Unable to create parent directories of " + file);
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
/*     */   public static void move(File from, File to) throws IOException {
/* 475 */     Preconditions.checkNotNull(from);
/* 476 */     Preconditions.checkNotNull(to);
/* 477 */     Preconditions.checkArgument(!from.equals(to), "Source %s and destination %s must be different", from, to);
/*     */     
/* 479 */     if (!from.renameTo(to)) {
/* 480 */       copy(from, to);
/* 481 */       if (!from.delete()) {
/* 482 */         if (!to.delete()) {
/* 483 */           throw new IOException("Unable to delete " + to);
/*     */         }
/* 485 */         throw new IOException("Unable to delete " + from);
/*     */       } 
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
/*     */   public static String readFirstLine(File file, Charset charset) throws IOException {
/* 501 */     return asCharSource(file, charset).readFirstLine();
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
/*     */   public static List<String> readLines(File file, Charset charset) throws IOException {
/* 520 */     return readLines(file, charset, new LineProcessor<List<String>>()
/*     */         {
/*     */ 
/*     */           
/* 524 */           final List<String> result = Lists.newArrayList();
/*     */ 
/*     */           
/*     */           public boolean processLine(String line) {
/* 528 */             this.result.add(line);
/* 529 */             return true;
/*     */           }
/*     */ 
/*     */           
/*     */           public List<String> getResult() {
/* 534 */             return this.result;
/*     */           }
/*     */         });
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
/*     */   @CanIgnoreReturnValue
/*     */   public static <T> T readLines(File file, Charset charset, LineProcessor<T> callback) throws IOException {
/* 553 */     return asCharSource(file, charset).readLines(callback);
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
/*     */   @CanIgnoreReturnValue
/*     */   public static <T> T readBytes(File file, ByteProcessor<T> processor) throws IOException {
/* 568 */     return asByteSource(file).read(processor);
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
/*     */   public static HashCode hash(File file, HashFunction hashFunction) throws IOException {
/* 581 */     return asByteSource(file).hash(hashFunction);
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
/*     */   public static MappedByteBuffer map(File file) throws IOException {
/* 600 */     Preconditions.checkNotNull(file);
/* 601 */     return map(file, FileChannel.MapMode.READ_ONLY);
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
/*     */   public static MappedByteBuffer map(File file, FileChannel.MapMode mode) throws IOException {
/* 622 */     Preconditions.checkNotNull(file);
/* 623 */     Preconditions.checkNotNull(mode);
/* 624 */     if (!file.exists()) {
/* 625 */       throw new FileNotFoundException(file.toString());
/*     */     }
/* 627 */     return map(file, mode, file.length());
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
/*     */   public static MappedByteBuffer map(File file, FileChannel.MapMode mode, long size) throws FileNotFoundException, IOException {
/* 651 */     Preconditions.checkNotNull(file);
/* 652 */     Preconditions.checkNotNull(mode);
/*     */     
/* 654 */     Closer closer = Closer.create();
/*     */     
/*     */     try {
/* 657 */       RandomAccessFile raf = closer.<RandomAccessFile>register(new RandomAccessFile(file, (mode == FileChannel.MapMode.READ_ONLY) ? "r" : "rw"));
/* 658 */       return map(raf, mode, size);
/* 659 */     } catch (Throwable e) {
/* 660 */       throw closer.rethrow(e);
/*     */     } finally {
/* 662 */       closer.close();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static MappedByteBuffer map(RandomAccessFile raf, FileChannel.MapMode mode, long size) throws IOException {
/* 668 */     Closer closer = Closer.create();
/*     */     try {
/* 670 */       FileChannel channel = closer.<FileChannel>register(raf.getChannel());
/* 671 */       return channel.map(mode, 0L, size);
/* 672 */     } catch (Throwable e) {
/* 673 */       throw closer.rethrow(e);
/*     */     } finally {
/* 675 */       closer.close();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String simplifyPath(String pathname) {
/* 700 */     Preconditions.checkNotNull(pathname);
/* 701 */     if (pathname.length() == 0) {
/* 702 */       return ".";
/*     */     }
/*     */ 
/*     */     
/* 706 */     Iterable<String> components = Splitter.on('/').omitEmptyStrings().split(pathname);
/* 707 */     List<String> path = new ArrayList<>();
/*     */ 
/*     */     
/* 710 */     for (String component : components) {
/* 711 */       if (component.equals("."))
/*     */         continue; 
/* 713 */       if (component.equals("..")) {
/* 714 */         if (path.size() > 0 && !((String)path.get(path.size() - 1)).equals("..")) {
/* 715 */           path.remove(path.size() - 1); continue;
/*     */         } 
/* 717 */         path.add("..");
/*     */         continue;
/*     */       } 
/* 720 */       path.add(component);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 725 */     String result = Joiner.on('/').join(path);
/* 726 */     if (pathname.charAt(0) == '/') {
/* 727 */       result = "/" + result;
/*     */     }
/*     */     
/* 730 */     while (result.startsWith("/../")) {
/* 731 */       result = result.substring(3);
/*     */     }
/* 733 */     if (result.equals("/..")) {
/* 734 */       result = "/";
/* 735 */     } else if ("".equals(result)) {
/* 736 */       result = ".";
/*     */     } 
/*     */     
/* 739 */     return result;
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
/*     */   public static String getFileExtension(String fullName) {
/* 757 */     Preconditions.checkNotNull(fullName);
/* 758 */     String fileName = (new File(fullName)).getName();
/* 759 */     int dotIndex = fileName.lastIndexOf('.');
/* 760 */     return (dotIndex == -1) ? "" : fileName.substring(dotIndex + 1);
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
/*     */   public static String getNameWithoutExtension(String file) {
/* 774 */     Preconditions.checkNotNull(file);
/* 775 */     String fileName = (new File(file)).getName();
/* 776 */     int dotIndex = fileName.lastIndexOf('.');
/* 777 */     return (dotIndex == -1) ? fileName : fileName.substring(0, dotIndex);
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
/*     */   public static TreeTraverser<File> fileTreeTraverser() {
/* 791 */     return FILE_TREE_TRAVERSER;
/*     */   }
/*     */   
/* 794 */   private static final TreeTraverser<File> FILE_TREE_TRAVERSER = new TreeTraverser<File>()
/*     */     {
/*     */       
/*     */       public Iterable<File> children(File file)
/*     */       {
/* 799 */         if (file.isDirectory()) {
/* 800 */           File[] files = file.listFiles();
/* 801 */           if (files != null) {
/* 802 */             return Collections.unmodifiableList(Arrays.asList(files));
/*     */           }
/*     */         } 
/*     */         
/* 806 */         return Collections.emptyList();
/*     */       }
/*     */ 
/*     */       
/*     */       public String toString() {
/* 811 */         return "Files.fileTreeTraverser()";
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Predicate<File> isDirectory() {
/* 821 */     return FilePredicate.IS_DIRECTORY;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Predicate<File> isFile() {
/* 830 */     return FilePredicate.IS_FILE;
/*     */   }
/*     */   
/*     */   private enum FilePredicate implements Predicate<File> {
/* 834 */     IS_DIRECTORY
/*     */     {
/*     */       public boolean apply(File file) {
/* 837 */         return file.isDirectory();
/*     */       }
/*     */ 
/*     */       
/*     */       public String toString() {
/* 842 */         return "Files.isDirectory()";
/*     */       }
/*     */     },
/*     */     
/* 846 */     IS_FILE
/*     */     {
/*     */       public boolean apply(File file) {
/* 849 */         return file.isFile();
/*     */       }
/*     */ 
/*     */       
/*     */       public String toString() {
/* 854 */         return "Files.isFile()";
/*     */       }
/*     */     };
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\io\Files.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */