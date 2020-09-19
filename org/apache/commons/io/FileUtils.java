/*      */ package org.apache.commons.io;
/*      */ 
/*      */ import java.io.BufferedOutputStream;
/*      */ import java.io.Closeable;
/*      */ import java.io.File;
/*      */ import java.io.FileFilter;
/*      */ import java.io.FileInputStream;
/*      */ import java.io.FileNotFoundException;
/*      */ import java.io.FileOutputStream;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.InputStreamReader;
/*      */ import java.io.OutputStream;
/*      */ import java.io.Reader;
/*      */ import java.math.BigInteger;
/*      */ import java.net.URL;
/*      */ import java.net.URLConnection;
/*      */ import java.nio.ByteBuffer;
/*      */ import java.nio.channels.FileChannel;
/*      */ import java.nio.charset.Charset;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collection;
/*      */ import java.util.Date;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedList;
/*      */ import java.util.List;
/*      */ import java.util.zip.CRC32;
/*      */ import java.util.zip.CheckedInputStream;
/*      */ import java.util.zip.Checksum;
/*      */ import org.apache.commons.io.filefilter.DirectoryFileFilter;
/*      */ import org.apache.commons.io.filefilter.FalseFileFilter;
/*      */ import org.apache.commons.io.filefilter.FileFilterUtils;
/*      */ import org.apache.commons.io.filefilter.IOFileFilter;
/*      */ import org.apache.commons.io.filefilter.SuffixFileFilter;
/*      */ import org.apache.commons.io.filefilter.TrueFileFilter;
/*      */ import org.apache.commons.io.output.NullOutputStream;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class FileUtils
/*      */ {
/*      */   public static final long ONE_KB = 1024L;
/*   93 */   public static final BigInteger ONE_KB_BI = BigInteger.valueOf(1024L);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final long ONE_MB = 1048576L;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  105 */   public static final BigInteger ONE_MB_BI = ONE_KB_BI.multiply(ONE_KB_BI);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final long FILE_COPY_BUFFER_SIZE = 31457280L;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final long ONE_GB = 1073741824L;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  122 */   public static final BigInteger ONE_GB_BI = ONE_KB_BI.multiply(ONE_MB_BI);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final long ONE_TB = 1099511627776L;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  134 */   public static final BigInteger ONE_TB_BI = ONE_KB_BI.multiply(ONE_GB_BI);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final long ONE_PB = 1125899906842624L;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  146 */   public static final BigInteger ONE_PB_BI = ONE_KB_BI.multiply(ONE_TB_BI);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final long ONE_EB = 1152921504606846976L;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  158 */   public static final BigInteger ONE_EB_BI = ONE_KB_BI.multiply(ONE_PB_BI);
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  163 */   public static final BigInteger ONE_ZB = BigInteger.valueOf(1024L).multiply(BigInteger.valueOf(1152921504606846976L));
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  168 */   public static final BigInteger ONE_YB = ONE_KB_BI.multiply(ONE_ZB);
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  173 */   public static final File[] EMPTY_FILE_ARRAY = new File[0];
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static File getFile(File directory, String... names) {
/*  185 */     if (directory == null) {
/*  186 */       throw new NullPointerException("directory must not be null");
/*      */     }
/*  188 */     if (names == null) {
/*  189 */       throw new NullPointerException("names must not be null");
/*      */     }
/*  191 */     File file = directory;
/*  192 */     for (String name : names) {
/*  193 */       file = new File(file, name);
/*      */     }
/*  195 */     return file;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static File getFile(String... names) {
/*  206 */     if (names == null) {
/*  207 */       throw new NullPointerException("names must not be null");
/*      */     }
/*  209 */     File file = null;
/*  210 */     for (String name : names) {
/*  211 */       if (file == null) {
/*  212 */         file = new File(name);
/*      */       } else {
/*  214 */         file = new File(file, name);
/*      */       } 
/*      */     } 
/*  217 */     return file;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String getTempDirectoryPath() {
/*  228 */     return System.getProperty("java.io.tmpdir");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static File getTempDirectory() {
/*  239 */     return new File(getTempDirectoryPath());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String getUserDirectoryPath() {
/*  250 */     return System.getProperty("user.home");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static File getUserDirectory() {
/*  261 */     return new File(getUserDirectoryPath());
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
/*      */   public static FileInputStream openInputStream(File file) throws IOException {
/*  284 */     if (file.exists()) {
/*  285 */       if (file.isDirectory()) {
/*  286 */         throw new IOException("File '" + file + "' exists but is a directory");
/*      */       }
/*  288 */       if (!file.canRead()) {
/*  289 */         throw new IOException("File '" + file + "' cannot be read");
/*      */       }
/*      */     } else {
/*  292 */       throw new FileNotFoundException("File '" + file + "' does not exist");
/*      */     } 
/*  294 */     return new FileInputStream(file);
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
/*      */   public static FileOutputStream openOutputStream(File file) throws IOException {
/*  319 */     return openOutputStream(file, false);
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
/*      */   public static FileOutputStream openOutputStream(File file, boolean append) throws IOException {
/*  345 */     if (file.exists()) {
/*  346 */       if (file.isDirectory()) {
/*  347 */         throw new IOException("File '" + file + "' exists but is a directory");
/*      */       }
/*  349 */       if (!file.canWrite()) {
/*  350 */         throw new IOException("File '" + file + "' cannot be written to");
/*      */       }
/*      */     } else {
/*  353 */       File parent = file.getParentFile();
/*  354 */       if (parent != null && 
/*  355 */         !parent.mkdirs() && !parent.isDirectory()) {
/*  356 */         throw new IOException("Directory '" + parent + "' could not be created");
/*      */       }
/*      */     } 
/*      */     
/*  360 */     return new FileOutputStream(file, append);
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
/*      */   public static String byteCountToDisplaySize(BigInteger size) {
/*      */     String displaySize;
/*  383 */     if (size.divide(ONE_EB_BI).compareTo(BigInteger.ZERO) > 0) {
/*  384 */       displaySize = String.valueOf(size.divide(ONE_EB_BI)) + " EB";
/*  385 */     } else if (size.divide(ONE_PB_BI).compareTo(BigInteger.ZERO) > 0) {
/*  386 */       displaySize = String.valueOf(size.divide(ONE_PB_BI)) + " PB";
/*  387 */     } else if (size.divide(ONE_TB_BI).compareTo(BigInteger.ZERO) > 0) {
/*  388 */       displaySize = String.valueOf(size.divide(ONE_TB_BI)) + " TB";
/*  389 */     } else if (size.divide(ONE_GB_BI).compareTo(BigInteger.ZERO) > 0) {
/*  390 */       displaySize = String.valueOf(size.divide(ONE_GB_BI)) + " GB";
/*  391 */     } else if (size.divide(ONE_MB_BI).compareTo(BigInteger.ZERO) > 0) {
/*  392 */       displaySize = String.valueOf(size.divide(ONE_MB_BI)) + " MB";
/*  393 */     } else if (size.divide(ONE_KB_BI).compareTo(BigInteger.ZERO) > 0) {
/*  394 */       displaySize = String.valueOf(size.divide(ONE_KB_BI)) + " KB";
/*      */     } else {
/*  396 */       displaySize = String.valueOf(size) + " bytes";
/*      */     } 
/*  398 */     return displaySize;
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
/*      */   public static String byteCountToDisplaySize(long size) {
/*  417 */     return byteCountToDisplaySize(BigInteger.valueOf(size));
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
/*      */   public static void touch(File file) throws IOException {
/*  434 */     if (!file.exists()) {
/*  435 */       OutputStream out = openOutputStream(file);
/*  436 */       IOUtils.closeQuietly(out);
/*      */     } 
/*  438 */     boolean success = file.setLastModified(System.currentTimeMillis());
/*  439 */     if (!success) {
/*  440 */       throw new IOException("Unable to set the last modification time for " + file);
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
/*      */   public static File[] convertFileCollectionToFileArray(Collection<File> files) {
/*  454 */     return files.<File>toArray(new File[files.size()]);
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
/*      */   private static void innerListFiles(Collection<File> files, File directory, IOFileFilter filter, boolean includeSubDirectories) {
/*  469 */     File[] found = directory.listFiles((FileFilter)filter);
/*      */     
/*  471 */     if (found != null) {
/*  472 */       for (File file : found) {
/*  473 */         if (file.isDirectory()) {
/*  474 */           if (includeSubDirectories) {
/*  475 */             files.add(file);
/*      */           }
/*  477 */           innerListFiles(files, file, filter, includeSubDirectories);
/*      */         } else {
/*  479 */           files.add(file);
/*      */         } 
/*      */       } 
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Collection<File> listFiles(File directory, IOFileFilter fileFilter, IOFileFilter dirFilter) {
/*  512 */     validateListFilesParameters(directory, fileFilter);
/*      */     
/*  514 */     IOFileFilter effFileFilter = setUpEffectiveFileFilter(fileFilter);
/*  515 */     IOFileFilter effDirFilter = setUpEffectiveDirFilter(dirFilter);
/*      */ 
/*      */     
/*  518 */     Collection<File> files = new LinkedList<File>();
/*  519 */     innerListFiles(files, directory, FileFilterUtils.or(new IOFileFilter[] { effFileFilter, effDirFilter }, ), false);
/*      */     
/*  521 */     return files;
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
/*      */   private static void validateListFilesParameters(File directory, IOFileFilter fileFilter) {
/*  535 */     if (!directory.isDirectory()) {
/*  536 */       throw new IllegalArgumentException("Parameter 'directory' is not a directory: " + directory);
/*      */     }
/*  538 */     if (fileFilter == null) {
/*  539 */       throw new NullPointerException("Parameter 'fileFilter' is null");
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static IOFileFilter setUpEffectiveFileFilter(IOFileFilter fileFilter) {
/*  550 */     return FileFilterUtils.and(new IOFileFilter[] { fileFilter, FileFilterUtils.notFileFilter(DirectoryFileFilter.INSTANCE) });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static IOFileFilter setUpEffectiveDirFilter(IOFileFilter dirFilter) {
/*  560 */     return (dirFilter == null) ? FalseFileFilter.INSTANCE : FileFilterUtils.and(new IOFileFilter[] { dirFilter, DirectoryFileFilter.INSTANCE });
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
/*      */   public static Collection<File> listFilesAndDirs(File directory, IOFileFilter fileFilter, IOFileFilter dirFilter) {
/*  585 */     validateListFilesParameters(directory, fileFilter);
/*      */     
/*  587 */     IOFileFilter effFileFilter = setUpEffectiveFileFilter(fileFilter);
/*  588 */     IOFileFilter effDirFilter = setUpEffectiveDirFilter(dirFilter);
/*      */ 
/*      */     
/*  591 */     Collection<File> files = new LinkedList<File>();
/*  592 */     if (directory.isDirectory()) {
/*  593 */       files.add(directory);
/*      */     }
/*  595 */     innerListFiles(files, directory, FileFilterUtils.or(new IOFileFilter[] { effFileFilter, effDirFilter }, ), true);
/*      */     
/*  597 */     return files;
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
/*      */   public static Iterator<File> iterateFiles(File directory, IOFileFilter fileFilter, IOFileFilter dirFilter) {
/*  620 */     return listFiles(directory, fileFilter, dirFilter).iterator();
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
/*      */   public static Iterator<File> iterateFilesAndDirs(File directory, IOFileFilter fileFilter, IOFileFilter dirFilter) {
/*  645 */     return listFilesAndDirs(directory, fileFilter, dirFilter).iterator();
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
/*      */   private static String[] toSuffixes(String[] extensions) {
/*  657 */     String[] suffixes = new String[extensions.length];
/*  658 */     for (int i = 0; i < extensions.length; i++) {
/*  659 */       suffixes[i] = "." + extensions[i];
/*      */     }
/*  661 */     return suffixes;
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
/*      */   public static Collection<File> listFiles(File directory, String[] extensions, boolean recursive) {
/*      */     SuffixFileFilter suffixFileFilter;
/*  678 */     if (extensions == null) {
/*  679 */       IOFileFilter filter = TrueFileFilter.INSTANCE;
/*      */     } else {
/*  681 */       String[] suffixes = toSuffixes(extensions);
/*  682 */       suffixFileFilter = new SuffixFileFilter(suffixes);
/*      */     } 
/*  684 */     return listFiles(directory, (IOFileFilter)suffixFileFilter, recursive ? TrueFileFilter.INSTANCE : FalseFileFilter.INSTANCE);
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
/*      */   public static Iterator<File> iterateFiles(File directory, String[] extensions, boolean recursive) {
/*  703 */     return listFiles(directory, extensions, recursive).iterator();
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
/*      */   public static boolean contentEquals(File file1, File file2) throws IOException {
/*  723 */     boolean file1Exists = file1.exists();
/*  724 */     if (file1Exists != file2.exists()) {
/*  725 */       return false;
/*      */     }
/*      */     
/*  728 */     if (!file1Exists)
/*      */     {
/*  730 */       return true;
/*      */     }
/*      */     
/*  733 */     if (file1.isDirectory() || file2.isDirectory())
/*      */     {
/*  735 */       throw new IOException("Can't compare directories, only files");
/*      */     }
/*      */     
/*  738 */     if (file1.length() != file2.length())
/*      */     {
/*  740 */       return false;
/*      */     }
/*      */     
/*  743 */     if (file1.getCanonicalFile().equals(file2.getCanonicalFile()))
/*      */     {
/*  745 */       return true;
/*      */     }
/*      */     
/*  748 */     InputStream input1 = null;
/*  749 */     InputStream input2 = null;
/*      */     try {
/*  751 */       input1 = new FileInputStream(file1);
/*  752 */       input2 = new FileInputStream(file2);
/*  753 */       return IOUtils.contentEquals(input1, input2);
/*      */     } finally {
/*      */       
/*  756 */       IOUtils.closeQuietly(input1);
/*  757 */       IOUtils.closeQuietly(input2);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean contentEqualsIgnoreEOL(File file1, File file2, String charsetName) throws IOException {
/*  781 */     boolean file1Exists = file1.exists();
/*  782 */     if (file1Exists != file2.exists()) {
/*  783 */       return false;
/*      */     }
/*      */     
/*  786 */     if (!file1Exists)
/*      */     {
/*  788 */       return true;
/*      */     }
/*      */     
/*  791 */     if (file1.isDirectory() || file2.isDirectory())
/*      */     {
/*  793 */       throw new IOException("Can't compare directories, only files");
/*      */     }
/*      */     
/*  796 */     if (file1.getCanonicalFile().equals(file2.getCanonicalFile()))
/*      */     {
/*  798 */       return true;
/*      */     }
/*      */     
/*  801 */     Reader input1 = null;
/*  802 */     Reader input2 = null;
/*      */     try {
/*  804 */       if (charsetName == null) {
/*      */         
/*  806 */         input1 = new InputStreamReader(new FileInputStream(file1), Charset.defaultCharset());
/*  807 */         input2 = new InputStreamReader(new FileInputStream(file2), Charset.defaultCharset());
/*      */       } else {
/*  809 */         input1 = new InputStreamReader(new FileInputStream(file1), charsetName);
/*  810 */         input2 = new InputStreamReader(new FileInputStream(file2), charsetName);
/*      */       } 
/*  812 */       return IOUtils.contentEqualsIgnoreEOL(input1, input2);
/*      */     } finally {
/*      */       
/*  815 */       IOUtils.closeQuietly(input1);
/*  816 */       IOUtils.closeQuietly(input2);
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
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static File toFile(URL url) {
/*  836 */     if (url == null || !"file".equalsIgnoreCase(url.getProtocol())) {
/*  837 */       return null;
/*      */     }
/*  839 */     String filename = url.getFile().replace('/', File.separatorChar);
/*  840 */     filename = decodeUrl(filename);
/*  841 */     return new File(filename);
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
/*      */   static String decodeUrl(String url) {
/*  861 */     String decoded = url;
/*  862 */     if (url != null && url.indexOf('%') >= 0) {
/*  863 */       int n = url.length();
/*  864 */       StringBuilder buffer = new StringBuilder();
/*  865 */       ByteBuffer bytes = ByteBuffer.allocate(n);
/*  866 */       for (int i = 0; i < n; ) {
/*  867 */         if (url.charAt(i) == '%') {
/*      */           try {
/*      */             do {
/*  870 */               byte octet = (byte)Integer.parseInt(url.substring(i + 1, i + 3), 16);
/*  871 */               bytes.put(octet);
/*  872 */               i += 3;
/*  873 */             } while (i < n && url.charAt(i) == '%');
/*      */             continue;
/*  875 */           } catch (RuntimeException runtimeException) {
/*      */ 
/*      */           
/*      */           } finally {
/*  879 */             if (bytes.position() > 0) {
/*  880 */               bytes.flip();
/*  881 */               buffer.append(Charsets.UTF_8.decode(bytes).toString());
/*  882 */               bytes.clear();
/*      */             } 
/*      */           } 
/*      */         }
/*  886 */         buffer.append(url.charAt(i++));
/*      */       } 
/*  888 */       decoded = buffer.toString();
/*      */     } 
/*  890 */     return decoded;
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
/*      */   public static File[] toFiles(URL[] urls) {
/*  913 */     if (urls == null || urls.length == 0) {
/*  914 */       return EMPTY_FILE_ARRAY;
/*      */     }
/*  916 */     File[] files = new File[urls.length];
/*  917 */     for (int i = 0; i < urls.length; i++) {
/*  918 */       URL url = urls[i];
/*  919 */       if (url != null) {
/*  920 */         if (!url.getProtocol().equals("file")) {
/*  921 */           throw new IllegalArgumentException("URL could not be converted to a File: " + url);
/*      */         }
/*      */         
/*  924 */         files[i] = toFile(url);
/*      */       } 
/*      */     } 
/*  927 */     return files;
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
/*      */   public static URL[] toURLs(File[] files) throws IOException {
/*  941 */     URL[] urls = new URL[files.length];
/*      */     
/*  943 */     for (int i = 0; i < urls.length; i++) {
/*  944 */       urls[i] = files[i].toURI().toURL();
/*      */     }
/*      */     
/*  947 */     return urls;
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
/*      */   public static void copyFileToDirectory(File srcFile, File destDir) throws IOException {
/*  973 */     copyFileToDirectory(srcFile, destDir, true);
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
/*      */   public static void copyFileToDirectory(File srcFile, File destDir, boolean preserveFileDate) throws IOException {
/* 1005 */     if (destDir == null) {
/* 1006 */       throw new NullPointerException("Destination must not be null");
/*      */     }
/* 1008 */     if (destDir.exists() && !destDir.isDirectory()) {
/* 1009 */       throw new IllegalArgumentException("Destination '" + destDir + "' is not a directory");
/*      */     }
/* 1011 */     File destFile = new File(destDir, srcFile.getName());
/* 1012 */     copyFile(srcFile, destFile, preserveFileDate);
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
/*      */   public static void copyFile(File srcFile, File destFile) throws IOException {
/* 1040 */     copyFile(srcFile, destFile, true);
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
/*      */   public static void copyFile(File srcFile, File destFile, boolean preserveFileDate) throws IOException {
/* 1072 */     checkFileRequirements(srcFile, destFile);
/* 1073 */     if (srcFile.isDirectory()) {
/* 1074 */       throw new IOException("Source '" + srcFile + "' exists but is a directory");
/*      */     }
/* 1076 */     if (srcFile.getCanonicalPath().equals(destFile.getCanonicalPath())) {
/* 1077 */       throw new IOException("Source '" + srcFile + "' and destination '" + destFile + "' are the same");
/*      */     }
/* 1079 */     File parentFile = destFile.getParentFile();
/* 1080 */     if (parentFile != null && 
/* 1081 */       !parentFile.mkdirs() && !parentFile.isDirectory()) {
/* 1082 */       throw new IOException("Destination '" + parentFile + "' directory cannot be created");
/*      */     }
/*      */     
/* 1085 */     if (destFile.exists() && !destFile.canWrite()) {
/* 1086 */       throw new IOException("Destination '" + destFile + "' exists but is read-only");
/*      */     }
/* 1088 */     doCopyFile(srcFile, destFile, preserveFileDate);
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
/*      */   public static long copyFile(File input, OutputStream output) throws IOException {
/* 1105 */     FileInputStream fis = new FileInputStream(input);
/*      */     try {
/* 1107 */       return IOUtils.copyLarge(fis, output);
/*      */     } finally {
/* 1109 */       fis.close();
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void doCopyFile(File srcFile, File destFile, boolean preserveFileDate) throws IOException {
/* 1132 */     if (destFile.exists() && destFile.isDirectory()) {
/* 1133 */       throw new IOException("Destination '" + destFile + "' exists but is a directory");
/*      */     }
/*      */     
/* 1136 */     FileInputStream fis = null;
/* 1137 */     FileOutputStream fos = null;
/* 1138 */     FileChannel input = null;
/* 1139 */     FileChannel output = null;
/*      */     try {
/* 1141 */       fis = new FileInputStream(srcFile);
/* 1142 */       fos = new FileOutputStream(destFile);
/* 1143 */       input = fis.getChannel();
/* 1144 */       output = fos.getChannel();
/* 1145 */       long size = input.size();
/* 1146 */       long pos = 0L;
/* 1147 */       long count = 0L;
/* 1148 */       while (pos < size) {
/* 1149 */         long remain = size - pos;
/* 1150 */         count = (remain > 31457280L) ? 31457280L : remain;
/* 1151 */         long bytesCopied = output.transferFrom(input, pos, count);
/* 1152 */         if (bytesCopied == 0L) {
/*      */           break;
/*      */         }
/* 1155 */         pos += bytesCopied;
/*      */       } 
/*      */     } finally {
/* 1158 */       IOUtils.closeQuietly(new Closeable[] { output, fos, input, fis });
/*      */     } 
/*      */     
/* 1161 */     long srcLen = srcFile.length();
/* 1162 */     long dstLen = destFile.length();
/* 1163 */     if (srcLen != dstLen) {
/* 1164 */       throw new IOException("Failed to copy full contents from '" + srcFile + "' to '" + destFile + "' Expected length: " + srcLen + " Actual: " + dstLen);
/*      */     }
/*      */     
/* 1167 */     if (preserveFileDate) {
/* 1168 */       destFile.setLastModified(srcFile.lastModified());
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void copyDirectoryToDirectory(File srcDir, File destDir) throws IOException {
/* 1197 */     if (srcDir == null) {
/* 1198 */       throw new NullPointerException("Source must not be null");
/*      */     }
/* 1200 */     if (srcDir.exists() && !srcDir.isDirectory()) {
/* 1201 */       throw new IllegalArgumentException("Source '" + destDir + "' is not a directory");
/*      */     }
/* 1203 */     if (destDir == null) {
/* 1204 */       throw new NullPointerException("Destination must not be null");
/*      */     }
/* 1206 */     if (destDir.exists() && !destDir.isDirectory()) {
/* 1207 */       throw new IllegalArgumentException("Destination '" + destDir + "' is not a directory");
/*      */     }
/* 1209 */     copyDirectory(srcDir, new File(destDir, srcDir.getName()), true);
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
/*      */   public static void copyDirectory(File srcDir, File destDir) throws IOException {
/* 1237 */     copyDirectory(srcDir, destDir, true);
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
/*      */   public static void copyDirectory(File srcDir, File destDir, boolean preserveFileDate) throws IOException {
/* 1268 */     copyDirectory(srcDir, destDir, null, preserveFileDate);
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
/*      */ 
/*      */   
/*      */   public static void copyDirectory(File srcDir, File destDir, FileFilter filter) throws IOException {
/* 1317 */     copyDirectory(srcDir, destDir, filter, true);
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
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void copyDirectory(File srcDir, File destDir, FileFilter filter, boolean preserveFileDate) throws IOException {
/* 1368 */     checkFileRequirements(srcDir, destDir);
/* 1369 */     if (!srcDir.isDirectory()) {
/* 1370 */       throw new IOException("Source '" + srcDir + "' exists but is not a directory");
/*      */     }
/* 1372 */     if (srcDir.getCanonicalPath().equals(destDir.getCanonicalPath())) {
/* 1373 */       throw new IOException("Source '" + srcDir + "' and destination '" + destDir + "' are the same");
/*      */     }
/*      */ 
/*      */     
/* 1377 */     List<String> exclusionList = null;
/* 1378 */     if (destDir.getCanonicalPath().startsWith(srcDir.getCanonicalPath())) {
/* 1379 */       File[] srcFiles = (filter == null) ? srcDir.listFiles() : srcDir.listFiles(filter);
/* 1380 */       if (srcFiles != null && srcFiles.length > 0) {
/* 1381 */         exclusionList = new ArrayList<String>(srcFiles.length);
/* 1382 */         for (File srcFile : srcFiles) {
/* 1383 */           File copiedFile = new File(destDir, srcFile.getName());
/* 1384 */           exclusionList.add(copiedFile.getCanonicalPath());
/*      */         } 
/*      */       } 
/*      */     } 
/* 1388 */     doCopyDirectory(srcDir, destDir, filter, preserveFileDate, exclusionList);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void checkFileRequirements(File src, File dest) throws FileNotFoundException {
/* 1398 */     if (src == null) {
/* 1399 */       throw new NullPointerException("Source must not be null");
/*      */     }
/* 1401 */     if (dest == null) {
/* 1402 */       throw new NullPointerException("Destination must not be null");
/*      */     }
/* 1404 */     if (!src.exists()) {
/* 1405 */       throw new FileNotFoundException("Source '" + src + "' does not exist");
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
/*      */ 
/*      */ 
/*      */   
/*      */   private static void doCopyDirectory(File srcDir, File destDir, FileFilter filter, boolean preserveFileDate, List<String> exclusionList) throws IOException {
/* 1424 */     File[] srcFiles = (filter == null) ? srcDir.listFiles() : srcDir.listFiles(filter);
/* 1425 */     if (srcFiles == null) {
/* 1426 */       throw new IOException("Failed to list contents of " + srcDir);
/*      */     }
/* 1428 */     if (destDir.exists()) {
/* 1429 */       if (!destDir.isDirectory()) {
/* 1430 */         throw new IOException("Destination '" + destDir + "' exists but is not a directory");
/*      */       }
/*      */     }
/* 1433 */     else if (!destDir.mkdirs() && !destDir.isDirectory()) {
/* 1434 */       throw new IOException("Destination '" + destDir + "' directory cannot be created");
/*      */     } 
/*      */     
/* 1437 */     if (!destDir.canWrite()) {
/* 1438 */       throw new IOException("Destination '" + destDir + "' cannot be written to");
/*      */     }
/* 1440 */     for (File srcFile : srcFiles) {
/* 1441 */       File dstFile = new File(destDir, srcFile.getName());
/* 1442 */       if (exclusionList == null || !exclusionList.contains(srcFile.getCanonicalPath())) {
/* 1443 */         if (srcFile.isDirectory()) {
/* 1444 */           doCopyDirectory(srcFile, dstFile, filter, preserveFileDate, exclusionList);
/*      */         } else {
/* 1446 */           doCopyFile(srcFile, dstFile, preserveFileDate);
/*      */         } 
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/* 1452 */     if (preserveFileDate) {
/* 1453 */       destDir.setLastModified(srcDir.lastModified());
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void copyURLToFile(URL source, File destination) throws IOException {
/* 1478 */     copyInputStreamToFile(source.openStream(), destination);
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
/*      */   public static void copyURLToFile(URL source, File destination, int connectionTimeout, int readTimeout) throws IOException {
/* 1503 */     URLConnection connection = source.openConnection();
/* 1504 */     connection.setConnectTimeout(connectionTimeout);
/* 1505 */     connection.setReadTimeout(readTimeout);
/* 1506 */     copyInputStreamToFile(connection.getInputStream(), destination);
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
/*      */   public static void copyInputStreamToFile(InputStream source, File destination) throws IOException {
/*      */     try {
/* 1528 */       copyToFile(source, destination);
/*      */     } finally {
/* 1530 */       IOUtils.closeQuietly(source);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void copyToFile(InputStream source, File destination) throws IOException {
/* 1552 */     FileOutputStream output = openOutputStream(destination);
/*      */     try {
/* 1554 */       IOUtils.copy(source, output);
/* 1555 */       output.close();
/*      */     } finally {
/* 1557 */       IOUtils.closeQuietly(output);
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
/*      */   public static void deleteDirectory(File directory) throws IOException {
/* 1570 */     if (!directory.exists()) {
/*      */       return;
/*      */     }
/*      */     
/* 1574 */     if (!isSymlink(directory)) {
/* 1575 */       cleanDirectory(directory);
/*      */     }
/*      */     
/* 1578 */     if (!directory.delete()) {
/* 1579 */       String message = "Unable to delete directory " + directory + ".";
/*      */       
/* 1581 */       throw new IOException(message);
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
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean deleteQuietly(File file) {
/* 1601 */     if (file == null) {
/* 1602 */       return false;
/*      */     }
/*      */     try {
/* 1605 */       if (file.isDirectory()) {
/* 1606 */         cleanDirectory(file);
/*      */       }
/* 1608 */     } catch (Exception exception) {}
/*      */ 
/*      */     
/*      */     try {
/* 1612 */       return file.delete();
/* 1613 */     } catch (Exception ignored) {
/* 1614 */       return false;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean directoryContains(File directory, File child) throws IOException {
/* 1643 */     if (directory == null) {
/* 1644 */       throw new IllegalArgumentException("Directory must not be null");
/*      */     }
/*      */     
/* 1647 */     if (!directory.isDirectory()) {
/* 1648 */       throw new IllegalArgumentException("Not a directory: " + directory);
/*      */     }
/*      */     
/* 1651 */     if (child == null) {
/* 1652 */       return false;
/*      */     }
/*      */     
/* 1655 */     if (!directory.exists() || !child.exists()) {
/* 1656 */       return false;
/*      */     }
/*      */ 
/*      */     
/* 1660 */     String canonicalParent = directory.getCanonicalPath();
/* 1661 */     String canonicalChild = child.getCanonicalPath();
/*      */     
/* 1663 */     return FilenameUtils.directoryContains(canonicalParent, canonicalChild);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void cleanDirectory(File directory) throws IOException {
/* 1674 */     File[] files = verifiedListFiles(directory);
/*      */     
/* 1676 */     IOException exception = null;
/* 1677 */     for (File file : files) {
/*      */       try {
/* 1679 */         forceDelete(file);
/* 1680 */       } catch (IOException ioe) {
/* 1681 */         exception = ioe;
/*      */       } 
/*      */     } 
/*      */     
/* 1685 */     if (null != exception) {
/* 1686 */       throw exception;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static File[] verifiedListFiles(File directory) throws IOException {
/* 1697 */     if (!directory.exists()) {
/* 1698 */       String message = directory + " does not exist";
/* 1699 */       throw new IllegalArgumentException(message);
/*      */     } 
/*      */     
/* 1702 */     if (!directory.isDirectory()) {
/* 1703 */       String message = directory + " is not a directory";
/* 1704 */       throw new IllegalArgumentException(message);
/*      */     } 
/*      */     
/* 1707 */     File[] files = directory.listFiles();
/* 1708 */     if (files == null) {
/* 1709 */       throw new IOException("Failed to list contents of " + directory);
/*      */     }
/* 1711 */     return files;
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
/*      */   public static boolean waitFor(File file, int seconds) {
/* 1727 */     long finishAt = System.currentTimeMillis() + seconds * 1000L;
/* 1728 */     boolean wasInterrupted = false;
/*      */     try {
/* 1730 */       while (!file.exists()) {
/* 1731 */         long remaining = finishAt - System.currentTimeMillis();
/* 1732 */         if (remaining < 0L) {
/* 1733 */           return false;
/*      */         }
/*      */         try {
/* 1736 */           Thread.sleep(Math.min(100L, remaining));
/* 1737 */         } catch (InterruptedException ignore) {
/* 1738 */           wasInterrupted = true;
/* 1739 */         } catch (Exception ex) {
/*      */           break;
/*      */         } 
/*      */       } 
/*      */     } finally {
/* 1744 */       if (wasInterrupted) {
/* 1745 */         Thread.currentThread().interrupt();
/*      */       }
/*      */     } 
/* 1748 */     return true;
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
/*      */   public static String readFileToString(File file, Charset encoding) throws IOException {
/* 1763 */     InputStream in = null;
/*      */     try {
/* 1765 */       in = openInputStream(file);
/* 1766 */       return IOUtils.toString(in, Charsets.toCharset(encoding));
/*      */     } finally {
/* 1768 */       IOUtils.closeQuietly(in);
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
/*      */   public static String readFileToString(File file, String encoding) throws IOException {
/* 1784 */     return readFileToString(file, Charsets.toCharset(encoding));
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
/*      */   @Deprecated
/*      */   public static String readFileToString(File file) throws IOException {
/* 1800 */     return readFileToString(file, Charset.defaultCharset());
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
/*      */   public static byte[] readFileToByteArray(File file) throws IOException {
/* 1813 */     InputStream in = null;
/*      */     try {
/* 1815 */       in = openInputStream(file);
/* 1816 */       return IOUtils.toByteArray(in);
/*      */     } finally {
/* 1818 */       IOUtils.closeQuietly(in);
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
/*      */   public static List<String> readLines(File file, Charset encoding) throws IOException {
/* 1833 */     InputStream in = null;
/*      */     try {
/* 1835 */       in = openInputStream(file);
/* 1836 */       return IOUtils.readLines(in, Charsets.toCharset(encoding));
/*      */     } finally {
/* 1838 */       IOUtils.closeQuietly(in);
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
/*      */   public static List<String> readLines(File file, String encoding) throws IOException {
/* 1854 */     return readLines(file, Charsets.toCharset(encoding));
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
/*      */   @Deprecated
/*      */   public static List<String> readLines(File file) throws IOException {
/* 1869 */     return readLines(file, Charset.defaultCharset());
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
/*      */   public static LineIterator lineIterator(File file, String encoding) throws IOException {
/* 1904 */     InputStream in = null;
/*      */     try {
/* 1906 */       in = openInputStream(file);
/* 1907 */       return IOUtils.lineIterator(in, encoding);
/* 1908 */     } catch (IOException ex) {
/* 1909 */       IOUtils.closeQuietly(in);
/* 1910 */       throw ex;
/* 1911 */     } catch (RuntimeException ex) {
/* 1912 */       IOUtils.closeQuietly(in);
/* 1913 */       throw ex;
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
/*      */   public static LineIterator lineIterator(File file) throws IOException {
/* 1927 */     return lineIterator(file, null);
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
/*      */   public static void writeStringToFile(File file, String data, Charset encoding) throws IOException {
/* 1947 */     writeStringToFile(file, data, encoding, false);
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
/*      */   public static void writeStringToFile(File file, String data, String encoding) throws IOException {
/* 1963 */     writeStringToFile(file, data, encoding, false);
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
/*      */   public static void writeStringToFile(File file, String data, Charset encoding, boolean append) throws IOException {
/* 1979 */     OutputStream out = null;
/*      */     try {
/* 1981 */       out = openOutputStream(file, append);
/* 1982 */       IOUtils.write(data, out, encoding);
/* 1983 */       out.close();
/*      */     } finally {
/* 1985 */       IOUtils.closeQuietly(out);
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
/*      */ 
/*      */ 
/*      */   
/*      */   public static void writeStringToFile(File file, String data, String encoding, boolean append) throws IOException {
/* 2004 */     writeStringToFile(file, data, Charsets.toCharset(encoding), append);
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
/*      */   @Deprecated
/*      */   public static void writeStringToFile(File file, String data) throws IOException {
/* 2017 */     writeStringToFile(file, data, Charset.defaultCharset(), false);
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
/*      */   @Deprecated
/*      */   public static void writeStringToFile(File file, String data, boolean append) throws IOException {
/* 2033 */     writeStringToFile(file, data, Charset.defaultCharset(), append);
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
/*      */   @Deprecated
/*      */   public static void write(File file, CharSequence data) throws IOException {
/* 2047 */     write(file, data, Charset.defaultCharset(), false);
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
/*      */   @Deprecated
/*      */   public static void write(File file, CharSequence data, boolean append) throws IOException {
/* 2063 */     write(file, data, Charset.defaultCharset(), append);
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
/*      */   public static void write(File file, CharSequence data, Charset encoding) throws IOException {
/* 2076 */     write(file, data, encoding, false);
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
/*      */   public static void write(File file, CharSequence data, String encoding) throws IOException {
/* 2090 */     write(file, data, encoding, false);
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
/*      */   public static void write(File file, CharSequence data, Charset encoding, boolean append) throws IOException {
/* 2106 */     String str = (data == null) ? null : data.toString();
/* 2107 */     writeStringToFile(file, str, encoding, append);
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
/*      */   public static void write(File file, CharSequence data, String encoding, boolean append) throws IOException {
/* 2125 */     write(file, data, Charsets.toCharset(encoding), append);
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
/*      */   public static void writeByteArrayToFile(File file, byte[] data) throws IOException {
/* 2140 */     writeByteArrayToFile(file, data, false);
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
/*      */   public static void writeByteArrayToFile(File file, byte[] data, boolean append) throws IOException {
/* 2155 */     writeByteArrayToFile(file, data, 0, data.length, append);
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
/*      */   public static void writeByteArrayToFile(File file, byte[] data, int off, int len) throws IOException {
/* 2172 */     writeByteArrayToFile(file, data, off, len, false);
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
/*      */   public static void writeByteArrayToFile(File file, byte[] data, int off, int len, boolean append) throws IOException {
/* 2191 */     OutputStream out = null;
/*      */     try {
/* 2193 */       out = openOutputStream(file, append);
/* 2194 */       out.write(data, off, len);
/* 2195 */       out.close();
/*      */     } finally {
/* 2197 */       IOUtils.closeQuietly(out);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void writeLines(File file, String encoding, Collection<?> lines) throws IOException {
/* 2218 */     writeLines(file, encoding, lines, null, false);
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
/*      */   public static void writeLines(File file, String encoding, Collection<?> lines, boolean append) throws IOException {
/* 2237 */     writeLines(file, encoding, lines, null, append);
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
/*      */   public static void writeLines(File file, Collection<?> lines) throws IOException {
/* 2251 */     writeLines(file, null, lines, null, false);
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
/*      */   public static void writeLines(File file, Collection<?> lines, boolean append) throws IOException {
/* 2267 */     writeLines(file, null, lines, null, append);
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
/*      */   public static void writeLines(File file, String encoding, Collection<?> lines, String lineEnding) throws IOException {
/* 2288 */     writeLines(file, encoding, lines, lineEnding, false);
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
/*      */   public static void writeLines(File file, String encoding, Collection<?> lines, String lineEnding, boolean append) throws IOException {
/* 2308 */     FileOutputStream out = null;
/*      */     try {
/* 2310 */       out = openOutputStream(file, append);
/* 2311 */       BufferedOutputStream buffer = new BufferedOutputStream(out);
/* 2312 */       IOUtils.writeLines(lines, lineEnding, buffer, encoding);
/* 2313 */       buffer.flush();
/* 2314 */       out.close();
/*      */     } finally {
/* 2316 */       IOUtils.closeQuietly(out);
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
/*      */   
/*      */   public static void writeLines(File file, Collection<?> lines, String lineEnding) throws IOException {
/* 2333 */     writeLines(file, null, lines, lineEnding, false);
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
/*      */   public static void writeLines(File file, Collection<?> lines, String lineEnding, boolean append) throws IOException {
/* 2351 */     writeLines(file, null, lines, lineEnding, append);
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
/*      */   public static void forceDelete(File file) throws IOException {
/* 2371 */     if (file.isDirectory()) {
/* 2372 */       deleteDirectory(file);
/*      */     } else {
/* 2374 */       boolean filePresent = file.exists();
/* 2375 */       if (!file.delete()) {
/* 2376 */         if (!filePresent) {
/* 2377 */           throw new FileNotFoundException("File does not exist: " + file);
/*      */         }
/* 2379 */         String message = "Unable to delete file: " + file;
/*      */         
/* 2381 */         throw new IOException(message);
/*      */       } 
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
/*      */   public static void forceDeleteOnExit(File file) throws IOException {
/* 2395 */     if (file.isDirectory()) {
/* 2396 */       deleteDirectoryOnExit(file);
/*      */     } else {
/* 2398 */       file.deleteOnExit();
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
/*      */   private static void deleteDirectoryOnExit(File directory) throws IOException {
/* 2410 */     if (!directory.exists()) {
/*      */       return;
/*      */     }
/*      */     
/* 2414 */     directory.deleteOnExit();
/* 2415 */     if (!isSymlink(directory)) {
/* 2416 */       cleanDirectoryOnExit(directory);
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
/*      */   private static void cleanDirectoryOnExit(File directory) throws IOException {
/* 2428 */     File[] files = verifiedListFiles(directory);
/*      */     
/* 2430 */     IOException exception = null;
/* 2431 */     for (File file : files) {
/*      */       try {
/* 2433 */         forceDeleteOnExit(file);
/* 2434 */       } catch (IOException ioe) {
/* 2435 */         exception = ioe;
/*      */       } 
/*      */     } 
/*      */     
/* 2439 */     if (null != exception) {
/* 2440 */       throw exception;
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
/*      */   public static void forceMkdir(File directory) throws IOException {
/* 2456 */     if (directory.exists()) {
/* 2457 */       if (!directory.isDirectory()) {
/* 2458 */         String message = "File " + directory + " exists and is " + "not a directory. Unable to create directory.";
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 2463 */         throw new IOException(message);
/*      */       }
/*      */     
/* 2466 */     } else if (!directory.mkdirs()) {
/*      */ 
/*      */       
/* 2469 */       if (!directory.isDirectory()) {
/* 2470 */         String message = "Unable to create directory " + directory;
/*      */         
/* 2472 */         throw new IOException(message);
/*      */       } 
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
/*      */   public static void forceMkdirParent(File file) throws IOException {
/* 2488 */     File parent = file.getParentFile();
/* 2489 */     if (parent == null) {
/*      */       return;
/*      */     }
/* 2492 */     forceMkdir(parent);
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
/*      */   public static long sizeOf(File file) {
/* 2520 */     if (!file.exists()) {
/* 2521 */       String message = file + " does not exist";
/* 2522 */       throw new IllegalArgumentException(message);
/*      */     } 
/*      */     
/* 2525 */     if (file.isDirectory()) {
/* 2526 */       return sizeOfDirectory0(file);
/*      */     }
/* 2528 */     return file.length();
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
/*      */   public static BigInteger sizeOfAsBigInteger(File file) {
/* 2553 */     if (!file.exists()) {
/* 2554 */       String message = file + " does not exist";
/* 2555 */       throw new IllegalArgumentException(message);
/*      */     } 
/*      */     
/* 2558 */     if (file.isDirectory()) {
/* 2559 */       return sizeOfDirectoryBig0(file);
/*      */     }
/* 2561 */     return BigInteger.valueOf(file.length());
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
/*      */   public static long sizeOfDirectory(File directory) {
/* 2579 */     checkDirectory(directory);
/* 2580 */     return sizeOfDirectory0(directory);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static long sizeOfDirectory0(File directory) {
/* 2591 */     File[] files = directory.listFiles();
/* 2592 */     if (files == null) {
/* 2593 */       return 0L;
/*      */     }
/* 2595 */     long size = 0L;
/*      */     
/* 2597 */     for (File file : files) {
/*      */       try {
/* 2599 */         if (!isSymlink(file)) {
/* 2600 */           size += sizeOf0(file);
/* 2601 */           if (size < 0L) {
/*      */             break;
/*      */           }
/*      */         } 
/* 2605 */       } catch (IOException iOException) {}
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 2610 */     return size;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static long sizeOf0(File file) {
/* 2621 */     if (file.isDirectory()) {
/* 2622 */       return sizeOfDirectory0(file);
/*      */     }
/* 2624 */     return file.length();
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
/*      */   public static BigInteger sizeOfDirectoryAsBigInteger(File directory) {
/* 2637 */     checkDirectory(directory);
/* 2638 */     return sizeOfDirectoryBig0(directory);
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
/*      */   private static BigInteger sizeOfDirectoryBig0(File directory) {
/* 2650 */     File[] files = directory.listFiles();
/* 2651 */     if (files == null) {
/* 2652 */       return BigInteger.ZERO;
/*      */     }
/* 2654 */     BigInteger size = BigInteger.ZERO;
/*      */     
/* 2656 */     for (File file : files) {
/*      */       try {
/* 2658 */         if (!isSymlink(file)) {
/* 2659 */           size = size.add(sizeOfBig0(file));
/*      */         }
/* 2661 */       } catch (IOException iOException) {}
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 2666 */     return size;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static BigInteger sizeOfBig0(File fileOrDir) {
/* 2677 */     if (fileOrDir.isDirectory()) {
/* 2678 */       return sizeOfDirectoryBig0(fileOrDir);
/*      */     }
/* 2680 */     return BigInteger.valueOf(fileOrDir.length());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void checkDirectory(File directory) {
/* 2691 */     if (!directory.exists()) {
/* 2692 */       throw new IllegalArgumentException(directory + " does not exist");
/*      */     }
/* 2694 */     if (!directory.isDirectory()) {
/* 2695 */       throw new IllegalArgumentException(directory + " is not a directory");
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
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isFileNewer(File file, File reference) {
/* 2714 */     if (reference == null) {
/* 2715 */       throw new IllegalArgumentException("No specified reference file");
/*      */     }
/* 2717 */     if (!reference.exists()) {
/* 2718 */       throw new IllegalArgumentException("The reference file '" + reference + "' doesn't exist");
/*      */     }
/*      */     
/* 2721 */     return isFileNewer(file, reference.lastModified());
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
/*      */   public static boolean isFileNewer(File file, Date date) {
/* 2737 */     if (date == null) {
/* 2738 */       throw new IllegalArgumentException("No specified date");
/*      */     }
/* 2740 */     return isFileNewer(file, date.getTime());
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
/*      */   public static boolean isFileNewer(File file, long timeMillis) {
/* 2756 */     if (file == null) {
/* 2757 */       throw new IllegalArgumentException("No specified file");
/*      */     }
/* 2759 */     if (!file.exists()) {
/* 2760 */       return false;
/*      */     }
/* 2762 */     return (file.lastModified() > timeMillis);
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
/*      */   public static boolean isFileOlder(File file, File reference) {
/* 2781 */     if (reference == null) {
/* 2782 */       throw new IllegalArgumentException("No specified reference file");
/*      */     }
/* 2784 */     if (!reference.exists()) {
/* 2785 */       throw new IllegalArgumentException("The reference file '" + reference + "' doesn't exist");
/*      */     }
/*      */     
/* 2788 */     return isFileOlder(file, reference.lastModified());
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
/*      */   public static boolean isFileOlder(File file, Date date) {
/* 2804 */     if (date == null) {
/* 2805 */       throw new IllegalArgumentException("No specified date");
/*      */     }
/* 2807 */     return isFileOlder(file, date.getTime());
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
/*      */   public static boolean isFileOlder(File file, long timeMillis) {
/* 2823 */     if (file == null) {
/* 2824 */       throw new IllegalArgumentException("No specified file");
/*      */     }
/* 2826 */     if (!file.exists()) {
/* 2827 */       return false;
/*      */     }
/* 2829 */     return (file.lastModified() < timeMillis);
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
/*      */   public static long checksumCRC32(File file) throws IOException {
/* 2845 */     CRC32 crc = new CRC32();
/* 2846 */     checksum(file, crc);
/* 2847 */     return crc.getValue();
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
/*      */   public static Checksum checksum(File file, Checksum checksum) throws IOException {
/* 2868 */     if (file.isDirectory()) {
/* 2869 */       throw new IllegalArgumentException("Checksums can't be computed on directories");
/*      */     }
/* 2871 */     InputStream in = null;
/*      */     try {
/* 2873 */       in = new CheckedInputStream(new FileInputStream(file), checksum);
/* 2874 */       IOUtils.copy(in, (OutputStream)new NullOutputStream());
/*      */     } finally {
/* 2876 */       IOUtils.closeQuietly(in);
/*      */     } 
/* 2878 */     return checksum;
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
/*      */   public static void moveDirectory(File srcDir, File destDir) throws IOException {
/* 2895 */     if (srcDir == null) {
/* 2896 */       throw new NullPointerException("Source must not be null");
/*      */     }
/* 2898 */     if (destDir == null) {
/* 2899 */       throw new NullPointerException("Destination must not be null");
/*      */     }
/* 2901 */     if (!srcDir.exists()) {
/* 2902 */       throw new FileNotFoundException("Source '" + srcDir + "' does not exist");
/*      */     }
/* 2904 */     if (!srcDir.isDirectory()) {
/* 2905 */       throw new IOException("Source '" + srcDir + "' is not a directory");
/*      */     }
/* 2907 */     if (destDir.exists()) {
/* 2908 */       throw new FileExistsException("Destination '" + destDir + "' already exists");
/*      */     }
/* 2910 */     boolean rename = srcDir.renameTo(destDir);
/* 2911 */     if (!rename) {
/* 2912 */       if (destDir.getCanonicalPath().startsWith(srcDir.getCanonicalPath() + File.separator)) {
/* 2913 */         throw new IOException("Cannot move directory: " + srcDir + " to a subdirectory of itself: " + destDir);
/*      */       }
/* 2915 */       copyDirectory(srcDir, destDir);
/* 2916 */       deleteDirectory(srcDir);
/* 2917 */       if (srcDir.exists()) {
/* 2918 */         throw new IOException("Failed to delete original directory '" + srcDir + "' after copy to '" + destDir + "'");
/*      */       }
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
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void moveDirectoryToDirectory(File src, File destDir, boolean createDestDir) throws IOException {
/* 2939 */     if (src == null) {
/* 2940 */       throw new NullPointerException("Source must not be null");
/*      */     }
/* 2942 */     if (destDir == null) {
/* 2943 */       throw new NullPointerException("Destination directory must not be null");
/*      */     }
/* 2945 */     if (!destDir.exists() && createDestDir) {
/* 2946 */       destDir.mkdirs();
/*      */     }
/* 2948 */     if (!destDir.exists()) {
/* 2949 */       throw new FileNotFoundException("Destination directory '" + destDir + "' does not exist [createDestDir=" + createDestDir + "]");
/*      */     }
/*      */     
/* 2952 */     if (!destDir.isDirectory()) {
/* 2953 */       throw new IOException("Destination '" + destDir + "' is not a directory");
/*      */     }
/* 2955 */     moveDirectory(src, new File(destDir, src.getName()));
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
/*      */   public static void moveFile(File srcFile, File destFile) throws IOException {
/* 2973 */     if (srcFile == null) {
/* 2974 */       throw new NullPointerException("Source must not be null");
/*      */     }
/* 2976 */     if (destFile == null) {
/* 2977 */       throw new NullPointerException("Destination must not be null");
/*      */     }
/* 2979 */     if (!srcFile.exists()) {
/* 2980 */       throw new FileNotFoundException("Source '" + srcFile + "' does not exist");
/*      */     }
/* 2982 */     if (srcFile.isDirectory()) {
/* 2983 */       throw new IOException("Source '" + srcFile + "' is a directory");
/*      */     }
/* 2985 */     if (destFile.exists()) {
/* 2986 */       throw new FileExistsException("Destination '" + destFile + "' already exists");
/*      */     }
/* 2988 */     if (destFile.isDirectory()) {
/* 2989 */       throw new IOException("Destination '" + destFile + "' is a directory");
/*      */     }
/* 2991 */     boolean rename = srcFile.renameTo(destFile);
/* 2992 */     if (!rename) {
/* 2993 */       copyFile(srcFile, destFile);
/* 2994 */       if (!srcFile.delete()) {
/* 2995 */         deleteQuietly(destFile);
/* 2996 */         throw new IOException("Failed to delete original file '" + srcFile + "' after copy to '" + destFile + "'");
/*      */       } 
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
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void moveFileToDirectory(File srcFile, File destDir, boolean createDestDir) throws IOException {
/* 3017 */     if (srcFile == null) {
/* 3018 */       throw new NullPointerException("Source must not be null");
/*      */     }
/* 3020 */     if (destDir == null) {
/* 3021 */       throw new NullPointerException("Destination directory must not be null");
/*      */     }
/* 3023 */     if (!destDir.exists() && createDestDir) {
/* 3024 */       destDir.mkdirs();
/*      */     }
/* 3026 */     if (!destDir.exists()) {
/* 3027 */       throw new FileNotFoundException("Destination directory '" + destDir + "' does not exist [createDestDir=" + createDestDir + "]");
/*      */     }
/*      */     
/* 3030 */     if (!destDir.isDirectory()) {
/* 3031 */       throw new IOException("Destination '" + destDir + "' is not a directory");
/*      */     }
/* 3033 */     moveFile(srcFile, new File(destDir, srcFile.getName()));
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
/*      */   public static void moveToDirectory(File src, File destDir, boolean createDestDir) throws IOException {
/* 3053 */     if (src == null) {
/* 3054 */       throw new NullPointerException("Source must not be null");
/*      */     }
/* 3056 */     if (destDir == null) {
/* 3057 */       throw new NullPointerException("Destination must not be null");
/*      */     }
/* 3059 */     if (!src.exists()) {
/* 3060 */       throw new FileNotFoundException("Source '" + src + "' does not exist");
/*      */     }
/* 3062 */     if (src.isDirectory()) {
/* 3063 */       moveDirectoryToDirectory(src, destDir, createDestDir);
/*      */     } else {
/* 3065 */       moveFileToDirectory(src, destDir, createDestDir);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isSymlink(File file) throws IOException {
/* 3089 */     if (Java7Support.isAtLeastJava7())
/*      */     {
/* 3091 */       return Java7Support.isSymLink(file);
/*      */     }
/*      */     
/* 3094 */     if (file == null) {
/* 3095 */       throw new NullPointerException("File must not be null");
/*      */     }
/* 3097 */     if (FilenameUtils.isSystemWindows()) {
/* 3098 */       return false;
/*      */     }
/* 3100 */     File fileInCanonicalDir = null;
/* 3101 */     if (file.getParent() == null) {
/* 3102 */       fileInCanonicalDir = file;
/*      */     } else {
/* 3104 */       File canonicalDir = file.getParentFile().getCanonicalFile();
/* 3105 */       fileInCanonicalDir = new File(canonicalDir, file.getName());
/*      */     } 
/*      */     
/* 3108 */     if (fileInCanonicalDir.getCanonicalFile().equals(fileInCanonicalDir.getAbsoluteFile())) {
/* 3109 */       return isBrokenSymlink(file);
/*      */     }
/* 3111 */     return true;
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
/*      */   private static boolean isBrokenSymlink(File file) throws IOException {
/* 3125 */     if (file.exists()) {
/* 3126 */       return false;
/*      */     }
/*      */     
/* 3129 */     final File canon = file.getCanonicalFile();
/* 3130 */     File parentDir = canon.getParentFile();
/* 3131 */     if (parentDir == null || !parentDir.exists()) {
/* 3132 */       return false;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 3137 */     File[] fileInDir = parentDir.listFiles(new FileFilter()
/*      */         {
/*      */           public boolean accept(File aFile) {
/* 3140 */             return aFile.equals(canon);
/*      */           }
/*      */         });
/*      */     
/* 3144 */     return (fileInDir != null && fileInDir.length > 0);
/*      */   }
/*      */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\commons\io\FileUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */