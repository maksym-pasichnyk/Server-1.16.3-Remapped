/*     */ package org.apache.commons.lang3.exception;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.io.PrintWriter;
/*     */ import java.io.StringWriter;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.UndeclaredThrowableException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.StringTokenizer;
/*     */ import org.apache.commons.lang3.ArrayUtils;
/*     */ import org.apache.commons.lang3.ClassUtils;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.apache.commons.lang3.SystemUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ExceptionUtils
/*     */ {
/*     */   static final String WRAPPED_MARKER = " [wrapped] ";
/*  54 */   private static final String[] CAUSE_METHOD_NAMES = new String[] { "getCause", "getNextException", "getTargetException", "getException", "getSourceException", "getRootCause", "getCausedByException", "getNested", "getLinkedException", "getNestedException", "getLinkedCause", "getThrowable" };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */   public static String[] getDefaultCauseMethodNames() {
/*  91 */     return (String[])ArrayUtils.clone((Object[])CAUSE_METHOD_NAMES);
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
/*     */   @Deprecated
/*     */   public static Throwable getCause(Throwable throwable) {
/* 124 */     return getCause(throwable, null);
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
/*     */   @Deprecated
/*     */   public static Throwable getCause(Throwable throwable, String[] methodNames) {
/* 142 */     if (throwable == null) {
/* 143 */       return null;
/*     */     }
/*     */     
/* 146 */     if (methodNames == null) {
/* 147 */       Throwable cause = throwable.getCause();
/* 148 */       if (cause != null) {
/* 149 */         return cause;
/*     */       }
/*     */       
/* 152 */       methodNames = CAUSE_METHOD_NAMES;
/*     */     } 
/*     */     
/* 155 */     for (String methodName : methodNames) {
/* 156 */       if (methodName != null) {
/* 157 */         Throwable legacyCause = getCauseUsingMethodName(throwable, methodName);
/* 158 */         if (legacyCause != null) {
/* 159 */           return legacyCause;
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 164 */     return null;
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
/*     */   public static Throwable getRootCause(Throwable throwable) {
/* 185 */     List<Throwable> list = getThrowableList(throwable);
/* 186 */     return (list.size() < 2) ? null : list.get(list.size() - 1);
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
/*     */   private static Throwable getCauseUsingMethodName(Throwable throwable, String methodName) {
/* 198 */     Method method = null;
/*     */     try {
/* 200 */       method = throwable.getClass().getMethod(methodName, new Class[0]);
/* 201 */     } catch (NoSuchMethodException noSuchMethodException) {
/*     */     
/* 203 */     } catch (SecurityException securityException) {}
/*     */ 
/*     */ 
/*     */     
/* 207 */     if (method != null && Throwable.class.isAssignableFrom(method.getReturnType())) {
/*     */       try {
/* 209 */         return (Throwable)method.invoke(throwable, new Object[0]);
/* 210 */       } catch (IllegalAccessException illegalAccessException) {
/*     */       
/* 212 */       } catch (IllegalArgumentException illegalArgumentException) {
/*     */       
/* 214 */       } catch (InvocationTargetException invocationTargetException) {}
/*     */     }
/*     */ 
/*     */     
/* 218 */     return null;
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
/*     */   public static int getThrowableCount(Throwable throwable) {
/* 239 */     return getThrowableList(throwable).size();
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
/*     */   public static Throwable[] getThrowables(Throwable throwable) {
/* 262 */     List<Throwable> list = getThrowableList(throwable);
/* 263 */     return list.<Throwable>toArray(new Throwable[list.size()]);
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
/*     */   public static List<Throwable> getThrowableList(Throwable throwable) {
/* 286 */     List<Throwable> list = new ArrayList<Throwable>();
/* 287 */     while (throwable != null && !list.contains(throwable)) {
/* 288 */       list.add(throwable);
/* 289 */       throwable = getCause(throwable);
/*     */     } 
/* 291 */     return list;
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
/*     */   public static int indexOfThrowable(Throwable throwable, Class<?> clazz) {
/* 310 */     return indexOf(throwable, clazz, 0, false);
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
/*     */   public static int indexOfThrowable(Throwable throwable, Class<?> clazz, int fromIndex) {
/* 333 */     return indexOf(throwable, clazz, fromIndex, false);
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
/*     */   public static int indexOfType(Throwable throwable, Class<?> type) {
/* 353 */     return indexOf(throwable, type, 0, true);
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
/*     */   public static int indexOfType(Throwable throwable, Class<?> type, int fromIndex) {
/* 377 */     return indexOf(throwable, type, fromIndex, true);
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
/*     */   private static int indexOf(Throwable throwable, Class<?> type, int fromIndex, boolean subclass) {
/* 392 */     if (throwable == null || type == null) {
/* 393 */       return -1;
/*     */     }
/* 395 */     if (fromIndex < 0) {
/* 396 */       fromIndex = 0;
/*     */     }
/* 398 */     Throwable[] throwables = getThrowables(throwable);
/* 399 */     if (fromIndex >= throwables.length) {
/* 400 */       return -1;
/*     */     }
/* 402 */     if (subclass) {
/* 403 */       for (int i = fromIndex; i < throwables.length; i++) {
/* 404 */         if (type.isAssignableFrom(throwables[i].getClass())) {
/* 405 */           return i;
/*     */         }
/*     */       } 
/*     */     } else {
/* 409 */       for (int i = fromIndex; i < throwables.length; i++) {
/* 410 */         if (type.equals(throwables[i].getClass())) {
/* 411 */           return i;
/*     */         }
/*     */       } 
/*     */     } 
/* 415 */     return -1;
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
/*     */   public static void printRootCauseStackTrace(Throwable throwable) {
/* 438 */     printRootCauseStackTrace(throwable, System.err);
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
/*     */   public static void printRootCauseStackTrace(Throwable throwable, PrintStream stream) {
/* 461 */     if (throwable == null) {
/*     */       return;
/*     */     }
/* 464 */     if (stream == null) {
/* 465 */       throw new IllegalArgumentException("The PrintStream must not be null");
/*     */     }
/* 467 */     String[] trace = getRootCauseStackTrace(throwable);
/* 468 */     for (String element : trace) {
/* 469 */       stream.println(element);
/*     */     }
/* 471 */     stream.flush();
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
/*     */   public static void printRootCauseStackTrace(Throwable throwable, PrintWriter writer) {
/* 494 */     if (throwable == null) {
/*     */       return;
/*     */     }
/* 497 */     if (writer == null) {
/* 498 */       throw new IllegalArgumentException("The PrintWriter must not be null");
/*     */     }
/* 500 */     String[] trace = getRootCauseStackTrace(throwable);
/* 501 */     for (String element : trace) {
/* 502 */       writer.println(element);
/*     */     }
/* 504 */     writer.flush();
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
/*     */   public static String[] getRootCauseStackTrace(Throwable throwable) {
/* 522 */     if (throwable == null) {
/* 523 */       return ArrayUtils.EMPTY_STRING_ARRAY;
/*     */     }
/* 525 */     Throwable[] throwables = getThrowables(throwable);
/* 526 */     int count = throwables.length;
/* 527 */     List<String> frames = new ArrayList<String>();
/* 528 */     List<String> nextTrace = getStackFrameList(throwables[count - 1]);
/* 529 */     for (int i = count; --i >= 0; ) {
/* 530 */       List<String> trace = nextTrace;
/* 531 */       if (i != 0) {
/* 532 */         nextTrace = getStackFrameList(throwables[i - 1]);
/* 533 */         removeCommonFrames(trace, nextTrace);
/*     */       } 
/* 535 */       if (i == count - 1) {
/* 536 */         frames.add(throwables[i].toString());
/*     */       } else {
/* 538 */         frames.add(" [wrapped] " + throwables[i].toString());
/*     */       } 
/* 540 */       for (int j = 0; j < trace.size(); j++) {
/* 541 */         frames.add(trace.get(j));
/*     */       }
/*     */     } 
/* 544 */     return frames.<String>toArray(new String[frames.size()]);
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
/*     */   public static void removeCommonFrames(List<String> causeFrames, List<String> wrapperFrames) {
/* 556 */     if (causeFrames == null || wrapperFrames == null) {
/* 557 */       throw new IllegalArgumentException("The List must not be null");
/*     */     }
/* 559 */     int causeFrameIndex = causeFrames.size() - 1;
/* 560 */     int wrapperFrameIndex = wrapperFrames.size() - 1;
/* 561 */     while (causeFrameIndex >= 0 && wrapperFrameIndex >= 0) {
/*     */ 
/*     */       
/* 564 */       String causeFrame = causeFrames.get(causeFrameIndex);
/* 565 */       String wrapperFrame = wrapperFrames.get(wrapperFrameIndex);
/* 566 */       if (causeFrame.equals(wrapperFrame)) {
/* 567 */         causeFrames.remove(causeFrameIndex);
/*     */       }
/* 569 */       causeFrameIndex--;
/* 570 */       wrapperFrameIndex--;
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
/*     */   public static String getStackTrace(Throwable throwable) {
/* 588 */     StringWriter sw = new StringWriter();
/* 589 */     PrintWriter pw = new PrintWriter(sw, true);
/* 590 */     throwable.printStackTrace(pw);
/* 591 */     return sw.getBuffer().toString();
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
/*     */   public static String[] getStackFrames(Throwable throwable) {
/* 608 */     if (throwable == null) {
/* 609 */       return ArrayUtils.EMPTY_STRING_ARRAY;
/*     */     }
/* 611 */     return getStackFrames(getStackTrace(throwable));
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
/*     */   static String[] getStackFrames(String stackTrace) {
/* 624 */     String linebreak = SystemUtils.LINE_SEPARATOR;
/* 625 */     StringTokenizer frames = new StringTokenizer(stackTrace, linebreak);
/* 626 */     List<String> list = new ArrayList<String>();
/* 627 */     while (frames.hasMoreTokens()) {
/* 628 */       list.add(frames.nextToken());
/*     */     }
/* 630 */     return list.<String>toArray(new String[list.size()]);
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
/*     */   static List<String> getStackFrameList(Throwable t) {
/* 646 */     String stackTrace = getStackTrace(t);
/* 647 */     String linebreak = SystemUtils.LINE_SEPARATOR;
/* 648 */     StringTokenizer frames = new StringTokenizer(stackTrace, linebreak);
/* 649 */     List<String> list = new ArrayList<String>();
/* 650 */     boolean traceStarted = false;
/* 651 */     while (frames.hasMoreTokens()) {
/* 652 */       String token = frames.nextToken();
/*     */       
/* 654 */       int at = token.indexOf("at");
/* 655 */       if (at != -1 && token.substring(0, at).trim().isEmpty()) {
/* 656 */         traceStarted = true;
/* 657 */         list.add(token); continue;
/* 658 */       }  if (traceStarted) {
/*     */         break;
/*     */       }
/*     */     } 
/* 662 */     return list;
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
/*     */   public static String getMessage(Throwable th) {
/* 677 */     if (th == null) {
/* 678 */       return "";
/*     */     }
/* 680 */     String clsName = ClassUtils.getShortClassName(th, null);
/* 681 */     String msg = th.getMessage();
/* 682 */     return clsName + ": " + StringUtils.defaultString(msg);
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
/*     */   public static String getRootCauseMessage(Throwable th) {
/* 697 */     Throwable root = getRootCause(th);
/* 698 */     root = (root == null) ? th : root;
/* 699 */     return getMessage(root);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <R> R rethrow(Throwable throwable) {
/* 759 */     return typeErasure(throwable);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static <R, T extends Throwable> R typeErasure(Throwable throwable) throws T {
/* 770 */     throw (T)throwable;
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
/*     */   public static <R> R wrapAndThrow(Throwable throwable) {
/* 795 */     if (throwable instanceof RuntimeException) {
/* 796 */       throw (RuntimeException)throwable;
/*     */     }
/* 798 */     if (throwable instanceof Error) {
/* 799 */       throw (Error)throwable;
/*     */     }
/* 801 */     throw new UndeclaredThrowableException(throwable);
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
/*     */   public static boolean hasCause(Throwable chain, Class<? extends Throwable> type) {
/* 819 */     if (chain instanceof UndeclaredThrowableException) {
/* 820 */       chain = chain.getCause();
/*     */     }
/* 822 */     return type.isInstance(chain);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\commons\lang3\exception\ExceptionUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */