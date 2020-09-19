/*     */ package org.apache.logging.log4j;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.AbstractCollection;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.NoSuchElementException;
/*     */ import org.apache.logging.log4j.message.ParameterizedMessage;
/*     */ import org.apache.logging.log4j.spi.CleanableThreadContextMap;
/*     */ import org.apache.logging.log4j.spi.DefaultThreadContextMap;
/*     */ import org.apache.logging.log4j.spi.DefaultThreadContextStack;
/*     */ import org.apache.logging.log4j.spi.NoOpThreadContextMap;
/*     */ import org.apache.logging.log4j.spi.ReadOnlyThreadContextMap;
/*     */ import org.apache.logging.log4j.spi.ThreadContextMap;
/*     */ import org.apache.logging.log4j.spi.ThreadContextMap2;
/*     */ import org.apache.logging.log4j.spi.ThreadContextMapFactory;
/*     */ import org.apache.logging.log4j.spi.ThreadContextStack;
/*     */ import org.apache.logging.log4j.util.PropertiesUtil;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ThreadContext
/*     */ {
/*     */   private static class EmptyThreadContextStack
/*     */     extends AbstractCollection<String>
/*     */     implements ThreadContextStack
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/*     */     
/*     */     private EmptyThreadContextStack() {}
/*     */     
/*  57 */     private static final Iterator<String> EMPTY_ITERATOR = new ThreadContext.EmptyIterator<>();
/*     */ 
/*     */     
/*     */     public String pop() {
/*  61 */       return null;
/*     */     }
/*     */ 
/*     */     
/*     */     public String peek() {
/*  66 */       return null;
/*     */     }
/*     */ 
/*     */     
/*     */     public void push(String message) {
/*  71 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */     
/*     */     public int getDepth() {
/*  76 */       return 0;
/*     */     }
/*     */ 
/*     */     
/*     */     public List<String> asList() {
/*  81 */       return Collections.emptyList();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void trim(int depth) {}
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean equals(Object o) {
/*  92 */       return (o instanceof Collection && ((Collection)o).isEmpty());
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public int hashCode() {
/*  98 */       return 1;
/*     */     }
/*     */ 
/*     */     
/*     */     public ThreadContext.ContextStack copy() {
/* 103 */       return (ThreadContext.ContextStack)this;
/*     */     }
/*     */ 
/*     */     
/*     */     public <T> T[] toArray(T[] a) {
/* 108 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean add(String e) {
/* 113 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean containsAll(Collection<?> c) {
/* 118 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean addAll(Collection<? extends String> c) {
/* 123 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean removeAll(Collection<?> c) {
/* 128 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean retainAll(Collection<?> c) {
/* 133 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */     
/*     */     public Iterator<String> iterator() {
/* 138 */       return EMPTY_ITERATOR;
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 143 */       return 0;
/*     */     }
/*     */ 
/*     */     
/*     */     public ThreadContext.ContextStack getImmutableStackOrNull() {
/* 148 */       return (ThreadContext.ContextStack)this;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class EmptyIterator<E>
/*     */     implements Iterator<E>
/*     */   {
/*     */     private EmptyIterator() {}
/*     */ 
/*     */     
/*     */     public boolean hasNext() {
/* 161 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public E next() {
/* 166 */       throw new NoSuchElementException("This is an empty iterator!");
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void remove() {}
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 181 */   public static final Map<String, String> EMPTY_MAP = Collections.emptyMap();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 188 */   public static final ThreadContextStack EMPTY_STACK = new EmptyThreadContextStack();
/*     */   
/*     */   private static final String DISABLE_MAP = "disableThreadContextMap";
/*     */   
/*     */   private static final String DISABLE_STACK = "disableThreadContextStack";
/*     */   private static final String DISABLE_ALL = "disableThreadContext";
/*     */   private static boolean disableAll;
/*     */   private static boolean useMap;
/*     */   private static boolean useStack;
/*     */   private static ThreadContextMap contextMap;
/*     */   private static ThreadContextStack contextStack;
/*     */   private static ReadOnlyThreadContextMap readOnlyContextMap;
/*     */   
/*     */   static {
/* 202 */     init();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static void init() {
/* 213 */     contextMap = null;
/* 214 */     PropertiesUtil managerProps = PropertiesUtil.getProperties();
/* 215 */     disableAll = managerProps.getBooleanProperty("disableThreadContext");
/* 216 */     useStack = (!managerProps.getBooleanProperty("disableThreadContextStack") && !disableAll);
/* 217 */     useMap = (!managerProps.getBooleanProperty("disableThreadContextMap") && !disableAll);
/*     */     
/* 219 */     contextStack = (ThreadContextStack)new DefaultThreadContextStack(useStack);
/* 220 */     if (!useMap) {
/* 221 */       contextMap = (ThreadContextMap)new NoOpThreadContextMap();
/*     */     } else {
/* 223 */       contextMap = ThreadContextMapFactory.createThreadContextMap();
/*     */     } 
/* 225 */     if (contextMap instanceof ReadOnlyThreadContextMap) {
/* 226 */       readOnlyContextMap = (ReadOnlyThreadContextMap)contextMap;
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
/*     */   public static void put(String key, String value) {
/* 242 */     contextMap.put(key, value);
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
/*     */   public static void putAll(Map<String, String> m) {
/* 255 */     if (contextMap instanceof ThreadContextMap2) {
/* 256 */       ((ThreadContextMap2)contextMap).putAll(m);
/* 257 */     } else if (contextMap instanceof DefaultThreadContextMap) {
/* 258 */       ((DefaultThreadContextMap)contextMap).putAll(m);
/*     */     } else {
/* 260 */       for (Map.Entry<String, String> entry : m.entrySet()) {
/* 261 */         contextMap.put(entry.getKey(), entry.getValue());
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
/*     */   public static String get(String key) {
/* 277 */     return contextMap.get(key);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void remove(String key) {
/* 286 */     contextMap.remove(key);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void removeAll(Iterable<String> keys) {
/* 297 */     if (contextMap instanceof CleanableThreadContextMap) {
/* 298 */       ((CleanableThreadContextMap)contextMap).removeAll(keys);
/* 299 */     } else if (contextMap instanceof DefaultThreadContextMap) {
/* 300 */       ((DefaultThreadContextMap)contextMap).removeAll(keys);
/*     */     } else {
/* 302 */       for (String key : keys) {
/* 303 */         contextMap.remove(key);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void clearMap() {
/* 312 */     contextMap.clear();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void clearAll() {
/* 319 */     clearMap();
/* 320 */     clearStack();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean containsKey(String key) {
/* 330 */     return contextMap.containsKey(key);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Map<String, String> getContext() {
/* 339 */     return contextMap.getCopy();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Map<String, String> getImmutableContext() {
/* 348 */     Map<String, String> map = contextMap.getImmutableMapOrNull();
/* 349 */     return (map == null) ? EMPTY_MAP : map;
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
/*     */   public static ReadOnlyThreadContextMap getThreadContextMap() {
/* 369 */     return readOnlyContextMap;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isEmpty() {
/* 378 */     return contextMap.isEmpty();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void clearStack() {
/* 385 */     contextStack.clear();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ContextStack cloneStack() {
/* 394 */     return contextStack.copy();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ContextStack getImmutableStack() {
/* 403 */     ContextStack result = contextStack.getImmutableStackOrNull();
/* 404 */     return (result == null) ? (ContextStack)EMPTY_STACK : result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void setStack(Collection<String> stack) {
/* 413 */     if (stack.isEmpty() || !useStack) {
/*     */       return;
/*     */     }
/* 416 */     contextStack.clear();
/* 417 */     contextStack.addAll(stack);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int getDepth() {
/* 428 */     return contextStack.getDepth();
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
/*     */   public static String pop() {
/* 442 */     return contextStack.pop();
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
/*     */   public static String peek() {
/* 456 */     return contextStack.peek();
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
/*     */   public static void push(String message) {
/* 469 */     contextStack.push(message);
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
/*     */   public static void push(String message, Object... args) {
/* 485 */     contextStack.push(ParameterizedMessage.format(message, args));
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
/*     */   public static void removeStack() {
/* 505 */     contextStack.clear();
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
/*     */   public static void trim(int depth) {
/* 541 */     contextStack.trim(depth);
/*     */   }
/*     */   
/*     */   public static interface ContextStack extends Serializable, Collection<String> {
/*     */     String pop();
/*     */     
/*     */     String peek();
/*     */     
/*     */     void push(String param1String);
/*     */     
/*     */     int getDepth();
/*     */     
/*     */     List<String> asList();
/*     */     
/*     */     void trim(int param1Int);
/*     */     
/*     */     ContextStack copy();
/*     */     
/*     */     ContextStack getImmutableStackOrNull();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\ThreadContext.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */