/*     */ package org.apache.logging.log4j.core.impl;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.net.URL;
/*     */ import java.security.CodeSource;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.Stack;
/*     */ import org.apache.logging.log4j.core.pattern.PlainTextRenderer;
/*     */ import org.apache.logging.log4j.core.pattern.TextRenderer;
/*     */ import org.apache.logging.log4j.core.util.Loader;
/*     */ import org.apache.logging.log4j.status.StatusLogger;
/*     */ import org.apache.logging.log4j.util.LoaderUtil;
/*     */ import org.apache.logging.log4j.util.ReflectionUtil;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ThrowableProxy
/*     */   implements Serializable
/*     */ {
/*     */   private static final String TAB = "\t";
/*     */   private static final String CAUSED_BY_LABEL = "Caused by: ";
/*     */   private static final String SUPPRESSED_LABEL = "Suppressed: ";
/*     */   private static final String WRAPPED_BY_LABEL = "Wrapped by: ";
/*     */   
/*     */   static class CacheEntry
/*     */   {
/*     */     private final ExtendedClassInfo element;
/*     */     private final ClassLoader loader;
/*     */     
/*     */     public CacheEntry(ExtendedClassInfo element, ClassLoader loader) {
/*  73 */       this.element = element;
/*  74 */       this.loader = loader;
/*     */     }
/*     */   }
/*     */   
/*  78 */   private static final ThrowableProxy[] EMPTY_THROWABLE_PROXY_ARRAY = new ThrowableProxy[0];
/*     */   
/*     */   private static final char EOL = '\n';
/*     */   
/*  82 */   private static final String EOL_STR = String.valueOf('\n');
/*     */ 
/*     */   
/*     */   private static final long serialVersionUID = -2752771578252251910L;
/*     */ 
/*     */   
/*     */   private final ThrowableProxy causeProxy;
/*     */ 
/*     */   
/*     */   private int commonElementCount;
/*     */   
/*     */   private final ExtendedStackTraceElement[] extendedStackTrace;
/*     */   
/*     */   private final String localizedMessage;
/*     */   
/*     */   private final String message;
/*     */   
/*     */   private final String name;
/*     */   
/*     */   private final ThrowableProxy[] suppressedProxies;
/*     */   
/*     */   private final transient Throwable throwable;
/*     */ 
/*     */   
/*     */   private ThrowableProxy() {
/* 107 */     this.throwable = null;
/* 108 */     this.name = null;
/* 109 */     this.extendedStackTrace = null;
/* 110 */     this.causeProxy = null;
/* 111 */     this.message = null;
/* 112 */     this.localizedMessage = null;
/* 113 */     this.suppressedProxies = EMPTY_THROWABLE_PROXY_ARRAY;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ThrowableProxy(Throwable throwable) {
/* 122 */     this(throwable, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ThrowableProxy(Throwable throwable, Set<Throwable> visited) {
/* 132 */     this.throwable = throwable;
/* 133 */     this.name = throwable.getClass().getName();
/* 134 */     this.message = throwable.getMessage();
/* 135 */     this.localizedMessage = throwable.getLocalizedMessage();
/* 136 */     Map<String, CacheEntry> map = new HashMap<>();
/* 137 */     Stack<Class<?>> stack = ReflectionUtil.getCurrentStackTrace();
/* 138 */     this.extendedStackTrace = toExtendedStackTrace(stack, map, null, throwable.getStackTrace());
/* 139 */     Throwable throwableCause = throwable.getCause();
/* 140 */     Set<Throwable> causeVisited = new HashSet<>(1);
/* 141 */     this.causeProxy = (throwableCause == null) ? null : new ThrowableProxy(throwable, stack, map, throwableCause, visited, causeVisited);
/*     */     
/* 143 */     this.suppressedProxies = toSuppressedProxies(throwable, visited);
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
/*     */   private ThrowableProxy(Throwable parent, Stack<Class<?>> stack, Map<String, CacheEntry> map, Throwable cause, Set<Throwable> suppressedVisited, Set<Throwable> causeVisited) {
/* 159 */     causeVisited.add(cause);
/* 160 */     this.throwable = cause;
/* 161 */     this.name = cause.getClass().getName();
/* 162 */     this.message = this.throwable.getMessage();
/* 163 */     this.localizedMessage = this.throwable.getLocalizedMessage();
/* 164 */     this.extendedStackTrace = toExtendedStackTrace(stack, map, parent.getStackTrace(), cause.getStackTrace());
/* 165 */     Throwable causeCause = cause.getCause();
/* 166 */     this.causeProxy = (causeCause == null || causeVisited.contains(causeCause)) ? null : new ThrowableProxy(parent, stack, map, causeCause, suppressedVisited, causeVisited);
/*     */     
/* 168 */     this.suppressedProxies = toSuppressedProxies(cause, suppressedVisited);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 173 */     if (this == obj) {
/* 174 */       return true;
/*     */     }
/* 176 */     if (obj == null) {
/* 177 */       return false;
/*     */     }
/* 179 */     if (getClass() != obj.getClass()) {
/* 180 */       return false;
/*     */     }
/* 182 */     ThrowableProxy other = (ThrowableProxy)obj;
/* 183 */     if (this.causeProxy == null) {
/* 184 */       if (other.causeProxy != null) {
/* 185 */         return false;
/*     */       }
/* 187 */     } else if (!this.causeProxy.equals(other.causeProxy)) {
/* 188 */       return false;
/*     */     } 
/* 190 */     if (this.commonElementCount != other.commonElementCount) {
/* 191 */       return false;
/*     */     }
/* 193 */     if (this.name == null) {
/* 194 */       if (other.name != null) {
/* 195 */         return false;
/*     */       }
/* 197 */     } else if (!this.name.equals(other.name)) {
/* 198 */       return false;
/*     */     } 
/* 200 */     if (!Arrays.equals((Object[])this.extendedStackTrace, (Object[])other.extendedStackTrace)) {
/* 201 */       return false;
/*     */     }
/* 203 */     if (!Arrays.equals((Object[])this.suppressedProxies, (Object[])other.suppressedProxies)) {
/* 204 */       return false;
/*     */     }
/* 206 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   private void formatCause(StringBuilder sb, String prefix, ThrowableProxy cause, List<String> ignorePackages, TextRenderer textRenderer) {
/* 211 */     formatThrowableProxy(sb, prefix, "Caused by: ", cause, ignorePackages, textRenderer);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void formatThrowableProxy(StringBuilder sb, String prefix, String causeLabel, ThrowableProxy throwableProxy, List<String> ignorePackages, TextRenderer textRenderer) {
/* 217 */     if (throwableProxy == null) {
/*     */       return;
/*     */     }
/* 220 */     textRenderer.render(prefix, sb, "Prefix");
/* 221 */     textRenderer.render(causeLabel, sb, "CauseLabel");
/* 222 */     throwableProxy.renderOn(sb, textRenderer);
/* 223 */     textRenderer.render(EOL_STR, sb, "Text");
/* 224 */     formatElements(sb, prefix, throwableProxy.commonElementCount, throwableProxy.getStackTrace(), throwableProxy.extendedStackTrace, ignorePackages, textRenderer);
/*     */     
/* 226 */     formatSuppressed(sb, prefix + "\t", throwableProxy.suppressedProxies, ignorePackages, textRenderer);
/* 227 */     formatCause(sb, prefix, throwableProxy.causeProxy, ignorePackages, textRenderer);
/*     */   }
/*     */   
/*     */   void renderOn(StringBuilder output, TextRenderer textRenderer) {
/* 231 */     String msg = this.message;
/* 232 */     textRenderer.render(this.name, output, "Name");
/* 233 */     if (msg != null) {
/* 234 */       textRenderer.render(": ", output, "NameMessageSeparator");
/* 235 */       textRenderer.render(msg, output, "Message");
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void formatSuppressed(StringBuilder sb, String prefix, ThrowableProxy[] suppressedProxies, List<String> ignorePackages, TextRenderer textRenderer) {
/* 241 */     if (suppressedProxies == null) {
/*     */       return;
/*     */     }
/* 244 */     for (ThrowableProxy suppressedProxy : suppressedProxies) {
/* 245 */       formatThrowableProxy(sb, prefix, "Suppressed: ", suppressedProxy, ignorePackages, textRenderer);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void formatElements(StringBuilder sb, String prefix, int commonCount, StackTraceElement[] causedTrace, ExtendedStackTraceElement[] extStackTrace, List<String> ignorePackages, TextRenderer textRenderer) {
/* 252 */     if (ignorePackages == null || ignorePackages.isEmpty()) {
/* 253 */       for (ExtendedStackTraceElement element : extStackTrace) {
/* 254 */         formatEntry(element, sb, prefix, textRenderer);
/*     */       }
/*     */     } else {
/* 257 */       int count = 0;
/* 258 */       for (int i = 0; i < extStackTrace.length; i++) {
/* 259 */         if (!ignoreElement(causedTrace[i], ignorePackages)) {
/* 260 */           if (count > 0) {
/* 261 */             appendSuppressedCount(sb, prefix, count, textRenderer);
/* 262 */             count = 0;
/*     */           } 
/* 264 */           formatEntry(extStackTrace[i], sb, prefix, textRenderer);
/*     */         } else {
/* 266 */           count++;
/*     */         } 
/*     */       } 
/* 269 */       if (count > 0) {
/* 270 */         appendSuppressedCount(sb, prefix, count, textRenderer);
/*     */       }
/*     */     } 
/* 273 */     if (commonCount != 0) {
/* 274 */       textRenderer.render(prefix, sb, "Prefix");
/* 275 */       textRenderer.render("\t... ", sb, "More");
/* 276 */       textRenderer.render(Integer.toString(commonCount), sb, "More");
/* 277 */       textRenderer.render(" more", sb, "More");
/* 278 */       textRenderer.render(EOL_STR, sb, "Text");
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void appendSuppressedCount(StringBuilder sb, String prefix, int count, TextRenderer textRenderer) {
/* 284 */     textRenderer.render(prefix, sb, "Prefix");
/* 285 */     if (count == 1) {
/* 286 */       textRenderer.render("\t... ", sb, "Suppressed");
/*     */     } else {
/* 288 */       textRenderer.render("\t... suppressed ", sb, "Suppressed");
/* 289 */       textRenderer.render(Integer.toString(count), sb, "Suppressed");
/* 290 */       textRenderer.render(" lines", sb, "Suppressed");
/*     */     } 
/* 292 */     textRenderer.render(EOL_STR, sb, "Text");
/*     */   }
/*     */ 
/*     */   
/*     */   private void formatEntry(ExtendedStackTraceElement extStackTraceElement, StringBuilder sb, String prefix, TextRenderer textRenderer) {
/* 297 */     textRenderer.render(prefix, sb, "Prefix");
/* 298 */     textRenderer.render("\tat ", sb, "At");
/* 299 */     extStackTraceElement.renderOn(sb, textRenderer);
/* 300 */     textRenderer.render(EOL_STR, sb, "Text");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void formatWrapper(StringBuilder sb, ThrowableProxy cause) {
/* 310 */     formatWrapper(sb, cause, null, (TextRenderer)PlainTextRenderer.getInstance());
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
/*     */   public void formatWrapper(StringBuilder sb, ThrowableProxy cause, List<String> ignorePackages) {
/* 322 */     formatWrapper(sb, cause, ignorePackages, (TextRenderer)PlainTextRenderer.getInstance());
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
/*     */   public void formatWrapper(StringBuilder sb, ThrowableProxy cause, List<String> ignorePackages, TextRenderer textRenderer) {
/* 336 */     Throwable caused = (cause.getCauseProxy() != null) ? cause.getCauseProxy().getThrowable() : null;
/* 337 */     if (caused != null) {
/* 338 */       formatWrapper(sb, cause.causeProxy, ignorePackages, textRenderer);
/* 339 */       sb.append("Wrapped by: ");
/*     */     } 
/* 341 */     cause.renderOn(sb, textRenderer);
/* 342 */     textRenderer.render(EOL_STR, sb, "Text");
/* 343 */     formatElements(sb, "", cause.commonElementCount, cause.getThrowable().getStackTrace(), cause.extendedStackTrace, ignorePackages, textRenderer);
/*     */   }
/*     */ 
/*     */   
/*     */   public ThrowableProxy getCauseProxy() {
/* 348 */     return this.causeProxy;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getCauseStackTraceAsString() {
/* 357 */     return getCauseStackTraceAsString(null, (TextRenderer)PlainTextRenderer.getInstance());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getCauseStackTraceAsString(List<String> packages) {
/* 367 */     return getCauseStackTraceAsString(packages, (TextRenderer)PlainTextRenderer.getInstance());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getCauseStackTraceAsString(List<String> ignorePackages, TextRenderer textRenderer) {
/* 378 */     StringBuilder sb = new StringBuilder();
/* 379 */     if (this.causeProxy != null) {
/* 380 */       formatWrapper(sb, this.causeProxy, ignorePackages, textRenderer);
/* 381 */       sb.append("Wrapped by: ");
/*     */     } 
/* 383 */     renderOn(sb, textRenderer);
/* 384 */     textRenderer.render(EOL_STR, sb, "Text");
/* 385 */     formatElements(sb, "", 0, this.throwable.getStackTrace(), this.extendedStackTrace, ignorePackages, textRenderer);
/*     */     
/* 387 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getCommonElementCount() {
/* 397 */     return this.commonElementCount;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ExtendedStackTraceElement[] getExtendedStackTrace() {
/* 406 */     return this.extendedStackTrace;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getExtendedStackTraceAsString() {
/* 415 */     return getExtendedStackTraceAsString(null, (TextRenderer)PlainTextRenderer.getInstance());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getExtendedStackTraceAsString(List<String> ignorePackages) {
/* 425 */     return getExtendedStackTraceAsString(ignorePackages, (TextRenderer)PlainTextRenderer.getInstance());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getExtendedStackTraceAsString(List<String> ignorePackages, TextRenderer textRenderer) {
/* 436 */     StringBuilder sb = new StringBuilder(1024);
/* 437 */     textRenderer.render(this.name, sb, "Name");
/* 438 */     textRenderer.render(": ", sb, "NameMessageSeparator");
/* 439 */     textRenderer.render(this.message, sb, "Message");
/* 440 */     textRenderer.render(EOL_STR, sb, "Text");
/* 441 */     StackTraceElement[] causedTrace = (this.throwable != null) ? this.throwable.getStackTrace() : null;
/* 442 */     formatElements(sb, "", 0, causedTrace, this.extendedStackTrace, ignorePackages, textRenderer);
/* 443 */     formatSuppressed(sb, "\t", this.suppressedProxies, ignorePackages, textRenderer);
/* 444 */     formatCause(sb, "", this.causeProxy, ignorePackages, textRenderer);
/* 445 */     return sb.toString();
/*     */   }
/*     */   
/*     */   public String getLocalizedMessage() {
/* 449 */     return this.localizedMessage;
/*     */   }
/*     */   
/*     */   public String getMessage() {
/* 453 */     return this.message;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getName() {
/* 462 */     return this.name;
/*     */   }
/*     */   
/*     */   public StackTraceElement[] getStackTrace() {
/* 466 */     return (this.throwable == null) ? null : this.throwable.getStackTrace();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ThrowableProxy[] getSuppressedProxies() {
/* 475 */     return this.suppressedProxies;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSuppressedStackTrace() {
/* 484 */     ThrowableProxy[] suppressed = getSuppressedProxies();
/* 485 */     if (suppressed == null || suppressed.length == 0) {
/* 486 */       return "";
/*     */     }
/* 488 */     StringBuilder sb = (new StringBuilder("Suppressed Stack Trace Elements:")).append('\n');
/* 489 */     for (ThrowableProxy proxy : suppressed) {
/* 490 */       sb.append(proxy.getExtendedStackTraceAsString());
/*     */     }
/* 492 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Throwable getThrowable() {
/* 501 */     return this.throwable;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 506 */     int prime = 31;
/* 507 */     int result = 1;
/* 508 */     result = 31 * result + ((this.causeProxy == null) ? 0 : this.causeProxy.hashCode());
/* 509 */     result = 31 * result + this.commonElementCount;
/* 510 */     result = 31 * result + ((this.extendedStackTrace == null) ? 0 : Arrays.hashCode((Object[])this.extendedStackTrace));
/* 511 */     result = 31 * result + ((this.suppressedProxies == null) ? 0 : Arrays.hashCode((Object[])this.suppressedProxies));
/* 512 */     result = 31 * result + ((this.name == null) ? 0 : this.name.hashCode());
/* 513 */     return result;
/*     */   }
/*     */   
/*     */   private boolean ignoreElement(StackTraceElement element, List<String> ignorePackages) {
/* 517 */     if (ignorePackages != null) {
/* 518 */       String className = element.getClassName();
/* 519 */       for (String pkg : ignorePackages) {
/* 520 */         if (className.startsWith(pkg)) {
/* 521 */           return true;
/*     */         }
/*     */       } 
/*     */     } 
/* 525 */     return false;
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
/*     */   private Class<?> loadClass(ClassLoader lastLoader, String className) {
/*     */     Class<?> clazz;
/* 538 */     if (lastLoader != null) {
/*     */       try {
/* 540 */         clazz = lastLoader.loadClass(className);
/* 541 */         if (clazz != null) {
/* 542 */           return clazz;
/*     */         }
/* 544 */       } catch (Throwable throwable) {}
/*     */     }
/*     */ 
/*     */     
/*     */     try {
/* 549 */       clazz = LoaderUtil.loadClass(className);
/* 550 */     } catch (ClassNotFoundException|NoClassDefFoundError e) {
/* 551 */       return loadClass(className);
/* 552 */     } catch (SecurityException e) {
/* 553 */       return null;
/*     */     } 
/* 555 */     return clazz;
/*     */   }
/*     */   
/*     */   private Class<?> loadClass(String className) {
/*     */     try {
/* 560 */       return Loader.loadClass(className, getClass().getClassLoader());
/* 561 */     } catch (ClassNotFoundException|NoClassDefFoundError|SecurityException e) {
/* 562 */       return null;
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
/*     */   private CacheEntry toCacheEntry(StackTraceElement stackTraceElement, Class<?> callerClass, boolean exact) {
/* 576 */     String location = "?";
/* 577 */     String version = "?";
/* 578 */     ClassLoader lastLoader = null;
/* 579 */     if (callerClass != null) {
/*     */       try {
/* 581 */         CodeSource source = callerClass.getProtectionDomain().getCodeSource();
/* 582 */         if (source != null) {
/* 583 */           URL locationURL = source.getLocation();
/* 584 */           if (locationURL != null) {
/* 585 */             String str = locationURL.toString().replace('\\', '/');
/* 586 */             int index = str.lastIndexOf("/");
/* 587 */             if (index >= 0 && index == str.length() - 1) {
/* 588 */               index = str.lastIndexOf("/", index - 1);
/* 589 */               location = str.substring(index + 1);
/*     */             } else {
/* 591 */               location = str.substring(index + 1);
/*     */             } 
/*     */           } 
/*     */         } 
/* 595 */       } catch (Exception exception) {}
/*     */ 
/*     */       
/* 598 */       Package pkg = callerClass.getPackage();
/* 599 */       if (pkg != null) {
/* 600 */         String ver = pkg.getImplementationVersion();
/* 601 */         if (ver != null) {
/* 602 */           version = ver;
/*     */         }
/*     */       } 
/* 605 */       lastLoader = callerClass.getClassLoader();
/*     */     } 
/* 607 */     return new CacheEntry(new ExtendedClassInfo(exact, location, version), lastLoader);
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
/*     */   ExtendedStackTraceElement[] toExtendedStackTrace(Stack<Class<?>> stack, Map<String, CacheEntry> map, StackTraceElement[] rootTrace, StackTraceElement[] stackTrace) {
/*     */     int stackLength;
/* 623 */     if (rootTrace != null) {
/* 624 */       int rootIndex = rootTrace.length - 1;
/* 625 */       int stackIndex = stackTrace.length - 1;
/* 626 */       while (rootIndex >= 0 && stackIndex >= 0 && rootTrace[rootIndex].equals(stackTrace[stackIndex])) {
/* 627 */         rootIndex--;
/* 628 */         stackIndex--;
/*     */       } 
/* 630 */       this.commonElementCount = stackTrace.length - 1 - stackIndex;
/* 631 */       stackLength = stackIndex + 1;
/*     */     } else {
/* 633 */       this.commonElementCount = 0;
/* 634 */       stackLength = stackTrace.length;
/*     */     } 
/* 636 */     ExtendedStackTraceElement[] extStackTrace = new ExtendedStackTraceElement[stackLength];
/* 637 */     Class<?> clazz = stack.isEmpty() ? null : stack.peek();
/* 638 */     ClassLoader lastLoader = null;
/* 639 */     for (int i = stackLength - 1; i >= 0; i--) {
/* 640 */       ExtendedClassInfo extClassInfo; StackTraceElement stackTraceElement = stackTrace[i];
/* 641 */       String className = stackTraceElement.getClassName();
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 646 */       if (clazz != null && className.equals(clazz.getName())) {
/* 647 */         CacheEntry entry = toCacheEntry(stackTraceElement, clazz, true);
/* 648 */         extClassInfo = entry.element;
/* 649 */         lastLoader = entry.loader;
/* 650 */         stack.pop();
/* 651 */         clazz = stack.isEmpty() ? null : stack.peek();
/*     */       } else {
/* 653 */         CacheEntry cacheEntry = map.get(className);
/* 654 */         if (cacheEntry != null) {
/* 655 */           CacheEntry entry = cacheEntry;
/* 656 */           extClassInfo = entry.element;
/* 657 */           if (entry.loader != null) {
/* 658 */             lastLoader = entry.loader;
/*     */           }
/*     */         } else {
/* 661 */           CacheEntry entry = toCacheEntry(stackTraceElement, loadClass(lastLoader, className), false);
/*     */           
/* 663 */           extClassInfo = entry.element;
/* 664 */           map.put(stackTraceElement.toString(), entry);
/* 665 */           if (entry.loader != null) {
/* 666 */             lastLoader = entry.loader;
/*     */           }
/*     */         } 
/*     */       } 
/* 670 */       extStackTrace[i] = new ExtendedStackTraceElement(stackTraceElement, extClassInfo);
/*     */     } 
/* 672 */     return extStackTrace;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 677 */     String msg = this.message;
/* 678 */     return (msg != null) ? (this.name + ": " + msg) : this.name;
/*     */   }
/*     */   
/*     */   private ThrowableProxy[] toSuppressedProxies(Throwable thrown, Set<Throwable> suppressedVisited) {
/*     */     try {
/* 683 */       Throwable[] suppressed = thrown.getSuppressed();
/* 684 */       if (suppressed == null) {
/* 685 */         return EMPTY_THROWABLE_PROXY_ARRAY;
/*     */       }
/* 687 */       List<ThrowableProxy> proxies = new ArrayList<>(suppressed.length);
/* 688 */       if (suppressedVisited == null) {
/* 689 */         suppressedVisited = new HashSet<>(proxies.size());
/*     */       }
/* 691 */       for (int i = 0; i < suppressed.length; i++) {
/* 692 */         Throwable candidate = suppressed[i];
/* 693 */         if (!suppressedVisited.contains(candidate)) {
/* 694 */           suppressedVisited.add(candidate);
/* 695 */           proxies.add(new ThrowableProxy(candidate, suppressedVisited));
/*     */         } 
/*     */       } 
/* 698 */       return proxies.<ThrowableProxy>toArray(new ThrowableProxy[proxies.size()]);
/* 699 */     } catch (Exception e) {
/* 700 */       StatusLogger.getLogger().error(e);
/*     */       
/* 702 */       return null;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\impl\ThrowableProxy.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */