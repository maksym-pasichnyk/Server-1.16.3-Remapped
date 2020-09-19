/*      */ package org.apache.commons.io;
/*      */ 
/*      */ import java.io.File;
/*      */ import java.io.IOException;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collection;
/*      */ import java.util.Stack;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class FilenameUtils
/*      */ {
/*      */   private static final int NOT_FOUND = -1;
/*      */   public static final char EXTENSION_SEPARATOR = '.';
/*   97 */   public static final String EXTENSION_SEPARATOR_STR = Character.toString('.');
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final char UNIX_SEPARATOR = '/';
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final char WINDOWS_SEPARATOR = '\\';
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  112 */   private static final char SYSTEM_SEPARATOR = File.separatorChar;
/*      */ 
/*      */   
/*      */   private static final char OTHER_SEPARATOR;
/*      */ 
/*      */   
/*      */   static {
/*  119 */     if (isSystemWindows()) {
/*  120 */       OTHER_SEPARATOR = '/';
/*      */     } else {
/*  122 */       OTHER_SEPARATOR = '\\';
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
/*      */   static boolean isSystemWindows() {
/*  140 */     return (SYSTEM_SEPARATOR == '\\');
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static boolean isSeparator(char ch) {
/*  151 */     return (ch == '/' || ch == '\\');
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
/*      */   public static String normalize(String filename) {
/*  196 */     return doNormalize(filename, SYSTEM_SEPARATOR, true);
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
/*      */   public static String normalize(String filename, boolean unixSeparator) {
/*  243 */     char separator = unixSeparator ? '/' : '\\';
/*  244 */     return doNormalize(filename, separator, true);
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
/*      */   public static String normalizeNoEndSeparator(String filename) {
/*  290 */     return doNormalize(filename, SYSTEM_SEPARATOR, false);
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
/*      */   public static String normalizeNoEndSeparator(String filename, boolean unixSeparator) {
/*  337 */     char separator = unixSeparator ? '/' : '\\';
/*  338 */     return doNormalize(filename, separator, false);
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
/*      */   private static String doNormalize(String filename, char separator, boolean keepSeparator) {
/*  350 */     if (filename == null) {
/*  351 */       return null;
/*      */     }
/*      */     
/*  354 */     failIfNullBytePresent(filename);
/*      */     
/*  356 */     int size = filename.length();
/*  357 */     if (size == 0) {
/*  358 */       return filename;
/*      */     }
/*  360 */     int prefix = getPrefixLength(filename);
/*  361 */     if (prefix < 0) {
/*  362 */       return null;
/*      */     }
/*      */     
/*  365 */     char[] array = new char[size + 2];
/*  366 */     filename.getChars(0, filename.length(), array, 0);
/*      */ 
/*      */     
/*  369 */     char otherSeparator = (separator == SYSTEM_SEPARATOR) ? OTHER_SEPARATOR : SYSTEM_SEPARATOR;
/*  370 */     for (int i = 0; i < array.length; i++) {
/*  371 */       if (array[i] == otherSeparator) {
/*  372 */         array[i] = separator;
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/*  377 */     boolean lastIsDirectory = true;
/*  378 */     if (array[size - 1] != separator) {
/*  379 */       array[size++] = separator;
/*  380 */       lastIsDirectory = false;
/*      */     } 
/*      */     
/*      */     int j;
/*  384 */     for (j = prefix + 1; j < size; j++) {
/*  385 */       if (array[j] == separator && array[j - 1] == separator) {
/*  386 */         System.arraycopy(array, j, array, j - 1, size - j);
/*  387 */         size--;
/*  388 */         j--;
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/*  393 */     for (j = prefix + 1; j < size; j++) {
/*  394 */       if (array[j] == separator && array[j - 1] == '.' && (j == prefix + 1 || array[j - 2] == separator)) {
/*      */         
/*  396 */         if (j == size - 1) {
/*  397 */           lastIsDirectory = true;
/*      */         }
/*  399 */         System.arraycopy(array, j + 1, array, j - 1, size - j);
/*  400 */         size -= 2;
/*  401 */         j--;
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  407 */     for (j = prefix + 2; j < size; j++) {
/*  408 */       if (array[j] == separator && array[j - 1] == '.' && array[j - 2] == '.' && (j == prefix + 2 || array[j - 3] == separator)) {
/*      */         
/*  410 */         if (j == prefix + 2) {
/*  411 */           return null;
/*      */         }
/*  413 */         if (j == size - 1) {
/*  414 */           lastIsDirectory = true;
/*      */         }
/*      */         
/*  417 */         int k = j - 4; while (true) { if (k >= prefix) {
/*  418 */             if (array[k] == separator) {
/*      */               
/*  420 */               System.arraycopy(array, j + 1, array, k + 1, size - j);
/*  421 */               size -= j - k;
/*  422 */               j = k + 1; break;
/*      */             } 
/*      */             k--;
/*      */             continue;
/*      */           } 
/*  427 */           System.arraycopy(array, j + 1, array, prefix, size - j);
/*  428 */           size -= j + 1 - prefix;
/*  429 */           j = prefix + 1; break; }
/*      */       
/*      */       } 
/*      */     } 
/*  433 */     if (size <= 0) {
/*  434 */       return "";
/*      */     }
/*  436 */     if (size <= prefix) {
/*  437 */       return new String(array, 0, size);
/*      */     }
/*  439 */     if (lastIsDirectory && keepSeparator) {
/*  440 */       return new String(array, 0, size);
/*      */     }
/*  442 */     return new String(array, 0, size - 1);
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
/*      */   public static String concat(String basePath, String fullFilenameToAdd) {
/*  487 */     int prefix = getPrefixLength(fullFilenameToAdd);
/*  488 */     if (prefix < 0) {
/*  489 */       return null;
/*      */     }
/*  491 */     if (prefix > 0) {
/*  492 */       return normalize(fullFilenameToAdd);
/*      */     }
/*  494 */     if (basePath == null) {
/*  495 */       return null;
/*      */     }
/*  497 */     int len = basePath.length();
/*  498 */     if (len == 0) {
/*  499 */       return normalize(fullFilenameToAdd);
/*      */     }
/*  501 */     char ch = basePath.charAt(len - 1);
/*  502 */     if (isSeparator(ch)) {
/*  503 */       return normalize(basePath + fullFilenameToAdd);
/*      */     }
/*  505 */     return normalize(basePath + '/' + fullFilenameToAdd);
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
/*      */   public static boolean directoryContains(String canonicalParent, String canonicalChild) throws IOException {
/*  536 */     if (canonicalParent == null) {
/*  537 */       throw new IllegalArgumentException("Directory must not be null");
/*      */     }
/*      */     
/*  540 */     if (canonicalChild == null) {
/*  541 */       return false;
/*      */     }
/*      */     
/*  544 */     if (IOCase.SYSTEM.checkEquals(canonicalParent, canonicalChild)) {
/*  545 */       return false;
/*      */     }
/*      */     
/*  548 */     return IOCase.SYSTEM.checkStartsWith(canonicalChild, canonicalParent);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String separatorsToUnix(String path) {
/*  559 */     if (path == null || path.indexOf('\\') == -1) {
/*  560 */       return path;
/*      */     }
/*  562 */     return path.replace('\\', '/');
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String separatorsToWindows(String path) {
/*  572 */     if (path == null || path.indexOf('/') == -1) {
/*  573 */       return path;
/*      */     }
/*  575 */     return path.replace('/', '\\');
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String separatorsToSystem(String path) {
/*  585 */     if (path == null) {
/*  586 */       return null;
/*      */     }
/*  588 */     if (isSystemWindows()) {
/*  589 */       return separatorsToWindows(path);
/*      */     }
/*  591 */     return separatorsToUnix(path);
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
/*      */   public static int getPrefixLength(String filename) {
/*  635 */     if (filename == null) {
/*  636 */       return -1;
/*      */     }
/*  638 */     int len = filename.length();
/*  639 */     if (len == 0) {
/*  640 */       return 0;
/*      */     }
/*  642 */     char ch0 = filename.charAt(0);
/*  643 */     if (ch0 == ':') {
/*  644 */       return -1;
/*      */     }
/*  646 */     if (len == 1) {
/*  647 */       if (ch0 == '~') {
/*  648 */         return 2;
/*      */       }
/*  650 */       return isSeparator(ch0) ? 1 : 0;
/*      */     } 
/*  652 */     if (ch0 == '~') {
/*  653 */       int posUnix = filename.indexOf('/', 1);
/*  654 */       int posWin = filename.indexOf('\\', 1);
/*  655 */       if (posUnix == -1 && posWin == -1) {
/*  656 */         return len + 1;
/*      */       }
/*  658 */       posUnix = (posUnix == -1) ? posWin : posUnix;
/*  659 */       posWin = (posWin == -1) ? posUnix : posWin;
/*  660 */       return Math.min(posUnix, posWin) + 1;
/*      */     } 
/*  662 */     char ch1 = filename.charAt(1);
/*  663 */     if (ch1 == ':') {
/*  664 */       ch0 = Character.toUpperCase(ch0);
/*  665 */       if (ch0 >= 'A' && ch0 <= 'Z') {
/*  666 */         if (len == 2 || !isSeparator(filename.charAt(2))) {
/*  667 */           return 2;
/*      */         }
/*  669 */         return 3;
/*      */       } 
/*  671 */       return -1;
/*      */     } 
/*  673 */     if (isSeparator(ch0) && isSeparator(ch1)) {
/*  674 */       int posUnix = filename.indexOf('/', 2);
/*  675 */       int posWin = filename.indexOf('\\', 2);
/*  676 */       if ((posUnix == -1 && posWin == -1) || posUnix == 2 || posWin == 2) {
/*  677 */         return -1;
/*      */       }
/*  679 */       posUnix = (posUnix == -1) ? posWin : posUnix;
/*  680 */       posWin = (posWin == -1) ? posUnix : posWin;
/*  681 */       return Math.min(posUnix, posWin) + 1;
/*      */     } 
/*  683 */     return isSeparator(ch0) ? 1 : 0;
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
/*      */   public static int indexOfLastSeparator(String filename) {
/*  701 */     if (filename == null) {
/*  702 */       return -1;
/*      */     }
/*  704 */     int lastUnixPos = filename.lastIndexOf('/');
/*  705 */     int lastWindowsPos = filename.lastIndexOf('\\');
/*  706 */     return Math.max(lastUnixPos, lastWindowsPos);
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
/*      */   public static int indexOfExtension(String filename) {
/*  724 */     if (filename == null) {
/*  725 */       return -1;
/*      */     }
/*  727 */     int extensionPos = filename.lastIndexOf('.');
/*  728 */     int lastSeparator = indexOfLastSeparator(filename);
/*  729 */     return (lastSeparator > extensionPos) ? -1 : extensionPos;
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
/*      */   public static String getPrefix(String filename) {
/*  763 */     if (filename == null) {
/*  764 */       return null;
/*      */     }
/*  766 */     int len = getPrefixLength(filename);
/*  767 */     if (len < 0) {
/*  768 */       return null;
/*      */     }
/*  770 */     if (len > filename.length()) {
/*  771 */       failIfNullBytePresent(filename + '/');
/*  772 */       return filename + '/';
/*      */     } 
/*  774 */     String path = filename.substring(0, len);
/*  775 */     failIfNullBytePresent(path);
/*  776 */     return path;
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
/*      */   public static String getPath(String filename) {
/*  803 */     return doGetPath(filename, 1);
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
/*      */   public static String getPathNoEndSeparator(String filename) {
/*  831 */     return doGetPath(filename, 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static String doGetPath(String filename, int separatorAdd) {
/*  842 */     if (filename == null) {
/*  843 */       return null;
/*      */     }
/*  845 */     int prefix = getPrefixLength(filename);
/*  846 */     if (prefix < 0) {
/*  847 */       return null;
/*      */     }
/*  849 */     int index = indexOfLastSeparator(filename);
/*  850 */     int endIndex = index + separatorAdd;
/*  851 */     if (prefix >= filename.length() || index < 0 || prefix >= endIndex) {
/*  852 */       return "";
/*      */     }
/*  854 */     String path = filename.substring(prefix, endIndex);
/*  855 */     failIfNullBytePresent(path);
/*  856 */     return path;
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
/*      */   public static String getFullPath(String filename) {
/*  885 */     return doGetFullPath(filename, true);
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
/*      */   public static String getFullPathNoEndSeparator(String filename) {
/*  915 */     return doGetFullPath(filename, false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static String doGetFullPath(String filename, boolean includeSeparator) {
/*  926 */     if (filename == null) {
/*  927 */       return null;
/*      */     }
/*  929 */     int prefix = getPrefixLength(filename);
/*  930 */     if (prefix < 0) {
/*  931 */       return null;
/*      */     }
/*  933 */     if (prefix >= filename.length()) {
/*  934 */       if (includeSeparator) {
/*  935 */         return getPrefix(filename);
/*      */       }
/*  937 */       return filename;
/*      */     } 
/*      */     
/*  940 */     int index = indexOfLastSeparator(filename);
/*  941 */     if (index < 0) {
/*  942 */       return filename.substring(0, prefix);
/*      */     }
/*  944 */     int end = index + (includeSeparator ? 1 : 0);
/*  945 */     if (end == 0) {
/*  946 */       end++;
/*      */     }
/*  948 */     return filename.substring(0, end);
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
/*      */   public static String getName(String filename) {
/*  970 */     if (filename == null) {
/*  971 */       return null;
/*      */     }
/*  973 */     failIfNullBytePresent(filename);
/*  974 */     int index = indexOfLastSeparator(filename);
/*  975 */     return filename.substring(index + 1);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void failIfNullBytePresent(String path) {
/*  985 */     int len = path.length();
/*  986 */     for (int i = 0; i < len; i++) {
/*  987 */       if (path.charAt(i) == '\000') {
/*  988 */         throw new IllegalArgumentException("Null byte present in file/path name. There are no known legitimate use cases for such data, but several injection attacks may use it");
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
/*      */   public static String getBaseName(String filename) {
/* 1013 */     return removeExtension(getName(filename));
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
/*      */   public static String getExtension(String filename) {
/* 1035 */     if (filename == null) {
/* 1036 */       return null;
/*      */     }
/* 1038 */     int index = indexOfExtension(filename);
/* 1039 */     if (index == -1) {
/* 1040 */       return "";
/*      */     }
/* 1042 */     return filename.substring(index + 1);
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
/*      */   public static String removeExtension(String filename) {
/* 1065 */     if (filename == null) {
/* 1066 */       return null;
/*      */     }
/* 1068 */     failIfNullBytePresent(filename);
/*      */     
/* 1070 */     int index = indexOfExtension(filename);
/* 1071 */     if (index == -1) {
/* 1072 */       return filename;
/*      */     }
/* 1074 */     return filename.substring(0, index);
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
/*      */   public static boolean equals(String filename1, String filename2) {
/* 1091 */     return equals(filename1, filename2, false, IOCase.SENSITIVE);
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
/*      */   public static boolean equalsOnSystem(String filename1, String filename2) {
/* 1106 */     return equals(filename1, filename2, false, IOCase.SYSTEM);
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
/*      */   public static boolean equalsNormalized(String filename1, String filename2) {
/* 1122 */     return equals(filename1, filename2, true, IOCase.SENSITIVE);
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
/*      */   public static boolean equalsNormalizedOnSystem(String filename1, String filename2) {
/* 1139 */     return equals(filename1, filename2, true, IOCase.SYSTEM);
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
/*      */   public static boolean equals(String filename1, String filename2, boolean normalized, IOCase caseSensitivity) {
/* 1157 */     if (filename1 == null || filename2 == null) {
/* 1158 */       return (filename1 == null && filename2 == null);
/*      */     }
/* 1160 */     if (normalized) {
/* 1161 */       filename1 = normalize(filename1);
/* 1162 */       filename2 = normalize(filename2);
/* 1163 */       if (filename1 == null || filename2 == null) {
/* 1164 */         throw new NullPointerException("Error normalizing one or both of the file names");
/*      */       }
/*      */     } 
/*      */     
/* 1168 */     if (caseSensitivity == null) {
/* 1169 */       caseSensitivity = IOCase.SENSITIVE;
/*      */     }
/* 1171 */     return caseSensitivity.checkEquals(filename1, filename2);
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
/*      */   public static boolean isExtension(String filename, String extension) {
/* 1188 */     if (filename == null) {
/* 1189 */       return false;
/*      */     }
/* 1191 */     failIfNullBytePresent(filename);
/*      */     
/* 1193 */     if (extension == null || extension.isEmpty()) {
/* 1194 */       return (indexOfExtension(filename) == -1);
/*      */     }
/* 1196 */     String fileExt = getExtension(filename);
/* 1197 */     return fileExt.equals(extension);
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
/*      */   public static boolean isExtension(String filename, String[] extensions) {
/* 1213 */     if (filename == null) {
/* 1214 */       return false;
/*      */     }
/* 1216 */     failIfNullBytePresent(filename);
/*      */     
/* 1218 */     if (extensions == null || extensions.length == 0) {
/* 1219 */       return (indexOfExtension(filename) == -1);
/*      */     }
/* 1221 */     String fileExt = getExtension(filename);
/* 1222 */     for (String extension : extensions) {
/* 1223 */       if (fileExt.equals(extension)) {
/* 1224 */         return true;
/*      */       }
/*      */     } 
/* 1227 */     return false;
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
/*      */   public static boolean isExtension(String filename, Collection<String> extensions) {
/* 1243 */     if (filename == null) {
/* 1244 */       return false;
/*      */     }
/* 1246 */     failIfNullBytePresent(filename);
/*      */     
/* 1248 */     if (extensions == null || extensions.isEmpty()) {
/* 1249 */       return (indexOfExtension(filename) == -1);
/*      */     }
/* 1251 */     String fileExt = getExtension(filename);
/* 1252 */     for (String extension : extensions) {
/* 1253 */       if (fileExt.equals(extension)) {
/* 1254 */         return true;
/*      */       }
/*      */     } 
/* 1257 */     return false;
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
/*      */   public static boolean wildcardMatch(String filename, String wildcardMatcher) {
/* 1284 */     return wildcardMatch(filename, wildcardMatcher, IOCase.SENSITIVE);
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
/*      */   public static boolean wildcardMatchOnSystem(String filename, String wildcardMatcher) {
/* 1310 */     return wildcardMatch(filename, wildcardMatcher, IOCase.SYSTEM);
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
/*      */   public static boolean wildcardMatch(String filename, String wildcardMatcher, IOCase caseSensitivity) {
/* 1328 */     if (filename == null && wildcardMatcher == null) {
/* 1329 */       return true;
/*      */     }
/* 1331 */     if (filename == null || wildcardMatcher == null) {
/* 1332 */       return false;
/*      */     }
/* 1334 */     if (caseSensitivity == null) {
/* 1335 */       caseSensitivity = IOCase.SENSITIVE;
/*      */     }
/* 1337 */     String[] wcs = splitOnTokens(wildcardMatcher);
/* 1338 */     boolean anyChars = false;
/* 1339 */     int textIdx = 0;
/* 1340 */     int wcsIdx = 0;
/* 1341 */     Stack<int[]> backtrack = (Stack)new Stack<int>();
/*      */ 
/*      */     
/*      */     do {
/* 1345 */       if (backtrack.size() > 0) {
/* 1346 */         int[] array = backtrack.pop();
/* 1347 */         wcsIdx = array[0];
/* 1348 */         textIdx = array[1];
/* 1349 */         anyChars = true;
/*      */       } 
/*      */ 
/*      */       
/* 1353 */       while (wcsIdx < wcs.length) {
/*      */         
/* 1355 */         if (wcs[wcsIdx].equals("?")) {
/*      */           
/* 1357 */           textIdx++;
/* 1358 */           if (textIdx > filename.length()) {
/*      */             break;
/*      */           }
/* 1361 */           anyChars = false;
/*      */         }
/* 1363 */         else if (wcs[wcsIdx].equals("*")) {
/*      */           
/* 1365 */           anyChars = true;
/* 1366 */           if (wcsIdx == wcs.length - 1) {
/* 1367 */             textIdx = filename.length();
/*      */           }
/*      */         }
/*      */         else {
/*      */           
/* 1372 */           if (anyChars) {
/*      */             
/* 1374 */             textIdx = caseSensitivity.checkIndexOf(filename, textIdx, wcs[wcsIdx]);
/* 1375 */             if (textIdx == -1) {
/*      */               break;
/*      */             }
/*      */             
/* 1379 */             int repeat = caseSensitivity.checkIndexOf(filename, textIdx + 1, wcs[wcsIdx]);
/* 1380 */             if (repeat >= 0) {
/* 1381 */               backtrack.push(new int[] { wcsIdx, repeat });
/*      */             
/*      */             }
/*      */           }
/* 1385 */           else if (!caseSensitivity.checkRegionMatches(filename, textIdx, wcs[wcsIdx])) {
/*      */             break;
/*      */           } 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 1392 */           textIdx += wcs[wcsIdx].length();
/* 1393 */           anyChars = false;
/*      */         } 
/*      */         
/* 1396 */         wcsIdx++;
/*      */       } 
/*      */ 
/*      */       
/* 1400 */       if (wcsIdx == wcs.length && textIdx == filename.length()) {
/* 1401 */         return true;
/*      */       }
/*      */     }
/* 1404 */     while (backtrack.size() > 0);
/*      */     
/* 1406 */     return false;
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
/*      */   static String[] splitOnTokens(String text) {
/* 1421 */     if (text.indexOf('?') == -1 && text.indexOf('*') == -1) {
/* 1422 */       return new String[] { text };
/*      */     }
/*      */     
/* 1425 */     char[] array = text.toCharArray();
/* 1426 */     ArrayList<String> list = new ArrayList<String>();
/* 1427 */     StringBuilder buffer = new StringBuilder();
/* 1428 */     char prevChar = Character.MIN_VALUE;
/* 1429 */     for (char ch : array) {
/* 1430 */       if (ch == '?' || ch == '*') {
/* 1431 */         if (buffer.length() != 0) {
/* 1432 */           list.add(buffer.toString());
/* 1433 */           buffer.setLength(0);
/*      */         } 
/* 1435 */         if (ch == '?') {
/* 1436 */           list.add("?");
/* 1437 */         } else if (prevChar != '*') {
/* 1438 */           list.add("*");
/*      */         } 
/*      */       } else {
/* 1441 */         buffer.append(ch);
/*      */       } 
/* 1443 */       prevChar = ch;
/*      */     } 
/* 1445 */     if (buffer.length() != 0) {
/* 1446 */       list.add(buffer.toString());
/*      */     }
/*      */     
/* 1449 */     return list.<String>toArray(new String[list.size()]);
/*      */   }
/*      */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\commons\io\FilenameUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */