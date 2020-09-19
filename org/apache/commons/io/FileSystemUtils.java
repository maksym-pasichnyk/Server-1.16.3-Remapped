/*     */ package org.apache.commons.io;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.OutputStream;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.StringTokenizer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FileSystemUtils
/*     */ {
/*  49 */   private static final FileSystemUtils INSTANCE = new FileSystemUtils();
/*     */ 
/*     */   
/*     */   private static final int INIT_PROBLEM = -1;
/*     */ 
/*     */   
/*     */   private static final int OTHER = 0;
/*     */   
/*     */   private static final int WINDOWS = 1;
/*     */   
/*     */   private static final int UNIX = 2;
/*     */   
/*     */   private static final int POSIX_UNIX = 3;
/*     */   
/*     */   private static final int OS;
/*     */   
/*     */   private static final String DF;
/*     */ 
/*     */   
/*     */   static {
/*  69 */     int os = 0;
/*  70 */     String dfPath = "df";
/*     */     try {
/*  72 */       String osName = System.getProperty("os.name");
/*  73 */       if (osName == null) {
/*  74 */         throw new IOException("os.name not found");
/*     */       }
/*  76 */       osName = osName.toLowerCase(Locale.ENGLISH);
/*     */       
/*  78 */       if (osName.contains("windows")) {
/*  79 */         os = 1;
/*  80 */       } else if (osName.contains("linux") || osName.contains("mpe/ix") || osName.contains("freebsd") || osName.contains("irix") || osName.contains("digital unix") || osName.contains("unix") || osName.contains("mac os x")) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*  87 */         os = 2;
/*  88 */       } else if (osName.contains("sun os") || osName.contains("sunos") || osName.contains("solaris")) {
/*     */ 
/*     */         
/*  91 */         os = 3;
/*  92 */         dfPath = "/usr/xpg4/bin/df";
/*  93 */       } else if (osName.contains("hp-ux") || osName.contains("aix")) {
/*     */         
/*  95 */         os = 3;
/*     */       } else {
/*  97 */         os = 0;
/*     */       }
/*     */     
/* 100 */     } catch (Exception ex) {
/* 101 */       os = -1;
/*     */     } 
/* 103 */     OS = os;
/* 104 */     DF = dfPath;
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
/*     */   @Deprecated
/*     */   public static long freeSpace(String path) throws IOException {
/* 143 */     return INSTANCE.freeSpaceOS(path, OS, false, -1L);
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
/*     */   public static long freeSpaceKb(String path) throws IOException {
/* 172 */     return freeSpaceKb(path, -1L);
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
/*     */   public static long freeSpaceKb(String path, long timeout) throws IOException {
/* 201 */     return INSTANCE.freeSpaceOS(path, OS, true, timeout);
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
/*     */   public static long freeSpaceKb() throws IOException {
/* 217 */     return freeSpaceKb(-1L);
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
/*     */   public static long freeSpaceKb(long timeout) throws IOException {
/* 235 */     return freeSpaceKb((new File(".")).getAbsolutePath(), timeout);
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
/*     */   long freeSpaceOS(String path, int os, boolean kb, long timeout) throws IOException {
/* 260 */     if (path == null) {
/* 261 */       throw new IllegalArgumentException("Path must not be null");
/*     */     }
/* 263 */     switch (os) {
/*     */       case 1:
/* 265 */         return kb ? (freeSpaceWindows(path, timeout) / 1024L) : freeSpaceWindows(path, timeout);
/*     */       case 2:
/* 267 */         return freeSpaceUnix(path, kb, false, timeout);
/*     */       case 3:
/* 269 */         return freeSpaceUnix(path, kb, true, timeout);
/*     */       case 0:
/* 271 */         throw new IllegalStateException("Unsupported operating system");
/*     */     } 
/* 273 */     throw new IllegalStateException("Exception caught when determining operating system");
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
/*     */   long freeSpaceWindows(String path, long timeout) throws IOException {
/* 289 */     path = FilenameUtils.normalize(path, false);
/* 290 */     if (path.length() > 0 && path.charAt(0) != '"') {
/* 291 */       path = "\"" + path + "\"";
/*     */     }
/*     */ 
/*     */     
/* 295 */     String[] cmdAttribs = { "cmd.exe", "/C", "dir /a /-c " + path };
/*     */ 
/*     */     
/* 298 */     List<String> lines = performCommand(cmdAttribs, 2147483647, timeout);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 304 */     for (int i = lines.size() - 1; i >= 0; i--) {
/* 305 */       String line = lines.get(i);
/* 306 */       if (line.length() > 0) {
/* 307 */         return parseDir(line, path);
/*     */       }
/*     */     } 
/*     */     
/* 311 */     throw new IOException("Command line 'dir /-c' did not return any info for path '" + path + "'");
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
/*     */   long parseDir(String line, String path) throws IOException {
/* 329 */     int bytesStart = 0;
/* 330 */     int bytesEnd = 0;
/* 331 */     int j = line.length() - 1;
/* 332 */     while (j >= 0) {
/* 333 */       char c = line.charAt(j);
/* 334 */       if (Character.isDigit(c)) {
/*     */ 
/*     */         
/* 337 */         bytesEnd = j + 1;
/*     */         break;
/*     */       } 
/* 340 */       j--;
/*     */     } 
/* 342 */     while (j >= 0) {
/* 343 */       char c = line.charAt(j);
/* 344 */       if (!Character.isDigit(c) && c != ',' && c != '.') {
/*     */ 
/*     */         
/* 347 */         bytesStart = j + 1;
/*     */         break;
/*     */       } 
/* 350 */       j--;
/*     */     } 
/* 352 */     if (j < 0) {
/* 353 */       throw new IOException("Command line 'dir /-c' did not return valid info for path '" + path + "'");
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 359 */     StringBuilder buf = new StringBuilder(line.substring(bytesStart, bytesEnd));
/* 360 */     for (int k = 0; k < buf.length(); k++) {
/* 361 */       if (buf.charAt(k) == ',' || buf.charAt(k) == '.') {
/* 362 */         buf.deleteCharAt(k--);
/*     */       }
/*     */     } 
/* 365 */     return parseBytes(buf.toString(), path);
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
/*     */   long freeSpaceUnix(String path, boolean kb, boolean posix, long timeout) throws IOException {
/* 382 */     if (path.isEmpty()) {
/* 383 */       throw new IllegalArgumentException("Path must not be empty");
/*     */     }
/*     */ 
/*     */     
/* 387 */     String flags = "-";
/* 388 */     if (kb) {
/* 389 */       flags = flags + "k";
/*     */     }
/* 391 */     if (posix) {
/* 392 */       flags = flags + "P";
/*     */     }
/* 394 */     (new String[3])[0] = DF; (new String[3])[1] = flags; (new String[3])[2] = path; (new String[2])[0] = DF; (new String[2])[1] = path; String[] cmdAttribs = (flags.length() > 1) ? new String[3] : new String[2];
/*     */ 
/*     */ 
/*     */     
/* 398 */     List<String> lines = performCommand(cmdAttribs, 3, timeout);
/* 399 */     if (lines.size() < 2)
/*     */     {
/* 401 */       throw new IOException("Command line '" + DF + "' did not return info as expected " + "for path '" + path + "'- response was " + lines);
/*     */     }
/*     */ 
/*     */     
/* 405 */     String line2 = lines.get(1);
/*     */ 
/*     */     
/* 408 */     StringTokenizer tok = new StringTokenizer(line2, " ");
/* 409 */     if (tok.countTokens() < 4) {
/*     */       
/* 411 */       if (tok.countTokens() == 1 && lines.size() >= 3) {
/* 412 */         String line3 = lines.get(2);
/* 413 */         tok = new StringTokenizer(line3, " ");
/*     */       } else {
/* 415 */         throw new IOException("Command line '" + DF + "' did not return data as expected " + "for path '" + path + "'- check path is valid");
/*     */       }
/*     */     
/*     */     } else {
/*     */       
/* 420 */       tok.nextToken();
/*     */     } 
/* 422 */     tok.nextToken();
/* 423 */     tok.nextToken();
/* 424 */     String freeSpace = tok.nextToken();
/* 425 */     return parseBytes(freeSpace, path);
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
/*     */   long parseBytes(String freeSpace, String path) throws IOException {
/*     */     try {
/* 439 */       long bytes = Long.parseLong(freeSpace);
/* 440 */       if (bytes < 0L) {
/* 441 */         throw new IOException("Command line '" + DF + "' did not find free space in response " + "for path '" + path + "'- check path is valid");
/*     */       }
/*     */ 
/*     */       
/* 445 */       return bytes;
/*     */     }
/* 447 */     catch (NumberFormatException ex) {
/* 448 */       throw new IOException("Command line '" + DF + "' did not return numeric data as expected " + "for path '" + path + "'- check path is valid", ex);
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
/*     */   
/*     */   List<String> performCommand(String[] cmdAttribs, int max, long timeout) throws IOException {
/* 474 */     List<String> lines = new ArrayList<String>(20);
/* 475 */     Process proc = null;
/* 476 */     InputStream in = null;
/* 477 */     OutputStream out = null;
/* 478 */     InputStream err = null;
/* 479 */     BufferedReader inr = null;
/*     */     
/*     */     try {
/* 482 */       Thread monitor = ThreadMonitor.start(timeout);
/*     */       
/* 484 */       proc = openProcess(cmdAttribs);
/* 485 */       in = proc.getInputStream();
/* 486 */       out = proc.getOutputStream();
/* 487 */       err = proc.getErrorStream();
/*     */       
/* 489 */       inr = new BufferedReader(new InputStreamReader(in, Charset.defaultCharset()));
/* 490 */       String line = inr.readLine();
/* 491 */       while (line != null && lines.size() < max) {
/* 492 */         line = line.toLowerCase(Locale.ENGLISH).trim();
/* 493 */         lines.add(line);
/* 494 */         line = inr.readLine();
/*     */       } 
/*     */       
/* 497 */       proc.waitFor();
/*     */       
/* 499 */       ThreadMonitor.stop(monitor);
/*     */       
/* 501 */       if (proc.exitValue() != 0)
/*     */       {
/* 503 */         throw new IOException("Command line returned OS error code '" + proc.exitValue() + "' for command " + Arrays.asList(cmdAttribs));
/*     */       }
/*     */ 
/*     */       
/* 507 */       if (lines.isEmpty())
/*     */       {
/* 509 */         throw new IOException("Command line did not return any info for command " + Arrays.asList(cmdAttribs));
/*     */       }
/*     */ 
/*     */       
/* 513 */       return lines;
/*     */     }
/* 515 */     catch (InterruptedException ex) {
/* 516 */       throw new IOException("Command line threw an InterruptedException for command " + Arrays.asList(cmdAttribs) + " timeout=" + timeout, ex);
/*     */     }
/*     */     finally {
/*     */       
/* 520 */       IOUtils.closeQuietly(in);
/* 521 */       IOUtils.closeQuietly(out);
/* 522 */       IOUtils.closeQuietly(err);
/* 523 */       IOUtils.closeQuietly(inr);
/* 524 */       if (proc != null) {
/* 525 */         proc.destroy();
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
/*     */   Process openProcess(String[] cmdAttribs) throws IOException {
/* 538 */     return Runtime.getRuntime().exec(cmdAttribs);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\commons\io\FileSystemUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */