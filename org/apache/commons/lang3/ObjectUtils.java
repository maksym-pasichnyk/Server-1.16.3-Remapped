/*      */ package org.apache.commons.lang3;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.io.Serializable;
/*      */ import java.lang.reflect.Array;
/*      */ import java.lang.reflect.InvocationTargetException;
/*      */ import java.lang.reflect.Method;
/*      */ import java.util.Collections;
/*      */ import java.util.Comparator;
/*      */ import java.util.HashMap;
/*      */ import java.util.Map;
/*      */ import java.util.TreeSet;
/*      */ import org.apache.commons.lang3.exception.CloneFailedException;
/*      */ import org.apache.commons.lang3.mutable.MutableInt;
/*      */ import org.apache.commons.lang3.text.StrBuilder;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class ObjectUtils
/*      */ {
/*   62 */   public static final Null NULL = new Null();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T> T defaultIfNull(T object, T defaultValue) {
/*   95 */     return (object != null) ? object : defaultValue;
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
/*      */   public static <T> T firstNonNull(T... values) {
/*  121 */     if (values != null) {
/*  122 */       for (T val : values) {
/*  123 */         if (val != null) {
/*  124 */           return val;
/*      */         }
/*      */       } 
/*      */     }
/*  128 */     return null;
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
/*      */   public static boolean anyNotNull(Object... values) {
/*  155 */     return (firstNonNull(values) != null);
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
/*      */   public static boolean allNotNull(Object... values) {
/*  184 */     if (values == null) {
/*  185 */       return false;
/*      */     }
/*      */     
/*  188 */     for (Object val : values) {
/*  189 */       if (val == null) {
/*  190 */         return false;
/*      */       }
/*      */     } 
/*      */     
/*  194 */     return true;
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
/*      */   @Deprecated
/*      */   public static boolean equals(Object object1, Object object2) {
/*  222 */     if (object1 == object2) {
/*  223 */       return true;
/*      */     }
/*  225 */     if (object1 == null || object2 == null) {
/*  226 */       return false;
/*      */     }
/*  228 */     return object1.equals(object2);
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
/*      */   public static boolean notEqual(Object object1, Object object2) {
/*  251 */     return !equals(object1, object2);
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
/*      */   @Deprecated
/*      */   public static int hashCode(Object obj) {
/*  272 */     return (obj == null) ? 0 : obj.hashCode();
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
/*      */   @Deprecated
/*      */   public static int hashCodeMulti(Object... objects) {
/*  299 */     int hash = 1;
/*  300 */     if (objects != null) {
/*  301 */       for (Object object : objects) {
/*  302 */         int tmpHash = hashCode(object);
/*  303 */         hash = hash * 31 + tmpHash;
/*      */       } 
/*      */     }
/*  306 */     return hash;
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
/*      */   public static String identityToString(Object object) {
/*  328 */     if (object == null) {
/*  329 */       return null;
/*      */     }
/*  331 */     StringBuilder builder = new StringBuilder();
/*  332 */     identityToString(builder, object);
/*  333 */     return builder.toString();
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
/*      */   public static void identityToString(Appendable appendable, Object object) throws IOException {
/*  353 */     if (object == null) {
/*  354 */       throw new NullPointerException("Cannot get the toString of a null identity");
/*      */     }
/*  356 */     appendable.append(object.getClass().getName())
/*  357 */       .append('@')
/*  358 */       .append(Integer.toHexString(System.identityHashCode(object)));
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
/*      */   public static void identityToString(StrBuilder builder, Object object) {
/*  377 */     if (object == null) {
/*  378 */       throw new NullPointerException("Cannot get the toString of a null identity");
/*      */     }
/*  380 */     builder.append(object.getClass().getName())
/*  381 */       .append('@')
/*  382 */       .append(Integer.toHexString(System.identityHashCode(object)));
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
/*      */   public static void identityToString(StringBuffer buffer, Object object) {
/*  401 */     if (object == null) {
/*  402 */       throw new NullPointerException("Cannot get the toString of a null identity");
/*      */     }
/*  404 */     buffer.append(object.getClass().getName())
/*  405 */       .append('@')
/*  406 */       .append(Integer.toHexString(System.identityHashCode(object)));
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
/*      */   public static void identityToString(StringBuilder builder, Object object) {
/*  425 */     if (object == null) {
/*  426 */       throw new NullPointerException("Cannot get the toString of a null identity");
/*      */     }
/*  428 */     builder.append(object.getClass().getName())
/*  429 */       .append('@')
/*  430 */       .append(Integer.toHexString(System.identityHashCode(object)));
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
/*      */   @Deprecated
/*      */   public static String toString(Object obj) {
/*  457 */     return (obj == null) ? "" : obj.toString();
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
/*      */   @Deprecated
/*      */   public static String toString(Object obj, String nullStr) {
/*  483 */     return (obj == null) ? nullStr : obj.toString();
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
/*      */   public static <T extends Comparable<? super T>> T min(T... values) {
/*  502 */     T result = null;
/*  503 */     if (values != null) {
/*  504 */       for (T value : values) {
/*  505 */         if (compare(value, result, true) < 0) {
/*  506 */           result = value;
/*      */         }
/*      */       } 
/*      */     }
/*  510 */     return result;
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
/*      */   public static <T extends Comparable<? super T>> T max(T... values) {
/*  527 */     T result = null;
/*  528 */     if (values != null) {
/*  529 */       for (T value : values) {
/*  530 */         if (compare(value, result, false) > 0) {
/*  531 */           result = value;
/*      */         }
/*      */       } 
/*      */     }
/*  535 */     return result;
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
/*      */   public static <T extends Comparable<? super T>> int compare(T c1, T c2) {
/*  549 */     return compare(c1, c2, false);
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
/*      */   public static <T extends Comparable<? super T>> int compare(T c1, T c2, boolean nullGreater) {
/*  566 */     if (c1 == c2)
/*  567 */       return 0; 
/*  568 */     if (c1 == null)
/*  569 */       return nullGreater ? 1 : -1; 
/*  570 */     if (c2 == null) {
/*  571 */       return nullGreater ? -1 : 1;
/*      */     }
/*  573 */     return c1.compareTo(c2);
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
/*      */   public static <T extends Comparable<? super T>> T median(T... items) {
/*  587 */     Validate.notEmpty(items);
/*  588 */     Validate.noNullElements(items);
/*  589 */     TreeSet<T> sort = new TreeSet<T>();
/*  590 */     Collections.addAll(sort, items);
/*      */ 
/*      */     
/*  593 */     return (T)sort.toArray()[(sort.size() - 1) / 2];
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
/*      */   public static <T> T median(Comparator<T> comparator, T... items) {
/*  609 */     Validate.notEmpty(items, "null/empty items", new Object[0]);
/*  610 */     Validate.noNullElements(items);
/*  611 */     Validate.notNull(comparator, "null comparator", new Object[0]);
/*  612 */     TreeSet<T> sort = new TreeSet<T>(comparator);
/*  613 */     Collections.addAll(sort, items);
/*      */ 
/*      */     
/*  616 */     T result = (T)sort.toArray()[(sort.size() - 1) / 2];
/*  617 */     return result;
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
/*      */   public static <T> T mode(T... items) {
/*  631 */     if (ArrayUtils.isNotEmpty(items)) {
/*  632 */       HashMap<T, MutableInt> occurrences = new HashMap<T, MutableInt>(items.length);
/*  633 */       for (T t : items) {
/*  634 */         MutableInt count = occurrences.get(t);
/*  635 */         if (count == null) {
/*  636 */           occurrences.put(t, new MutableInt(1));
/*      */         } else {
/*  638 */           count.increment();
/*      */         } 
/*      */       } 
/*  641 */       T result = null;
/*  642 */       int max = 0;
/*  643 */       for (Map.Entry<T, MutableInt> e : occurrences.entrySet()) {
/*  644 */         int cmp = ((MutableInt)e.getValue()).intValue();
/*  645 */         if (cmp == max) {
/*  646 */           result = null; continue;
/*  647 */         }  if (cmp > max) {
/*  648 */           max = cmp;
/*  649 */           result = e.getKey();
/*      */         } 
/*      */       } 
/*  652 */       return result;
/*      */     } 
/*  654 */     return null;
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
/*      */   public static <T> T clone(T obj) {
/*  669 */     if (obj instanceof Cloneable) {
/*      */       Object result;
/*  671 */       if (obj.getClass().isArray()) {
/*  672 */         Class<?> componentType = obj.getClass().getComponentType();
/*  673 */         if (!componentType.isPrimitive()) {
/*  674 */           result = ((Object[])obj).clone();
/*      */         } else {
/*  676 */           int length = Array.getLength(obj);
/*  677 */           result = Array.newInstance(componentType, length);
/*  678 */           while (length-- > 0) {
/*  679 */             Array.set(result, length, Array.get(obj, length));
/*      */           }
/*      */         } 
/*      */       } else {
/*      */         try {
/*  684 */           Method clone = obj.getClass().getMethod("clone", new Class[0]);
/*  685 */           result = clone.invoke(obj, new Object[0]);
/*  686 */         } catch (NoSuchMethodException e) {
/*  687 */           throw new CloneFailedException("Cloneable type " + obj
/*  688 */               .getClass().getName() + " has no clone method", e);
/*      */         }
/*  690 */         catch (IllegalAccessException e) {
/*  691 */           throw new CloneFailedException("Cannot clone Cloneable type " + obj
/*  692 */               .getClass().getName(), e);
/*  693 */         } catch (InvocationTargetException e) {
/*  694 */           throw new CloneFailedException("Exception cloning Cloneable type " + obj
/*  695 */               .getClass().getName(), e.getCause());
/*      */         } 
/*      */       } 
/*      */       
/*  699 */       T checked = (T)result;
/*  700 */       return checked;
/*      */     } 
/*      */     
/*  703 */     return null;
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
/*      */   public static <T> T cloneIfPossible(T obj) {
/*  723 */     T clone = clone(obj);
/*  724 */     return (clone == null) ? obj : clone;
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
/*      */   public static class Null
/*      */     implements Serializable
/*      */   {
/*      */     private static final long serialVersionUID = 7092611880189329093L;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private Object readResolve() {
/*  763 */       return ObjectUtils.NULL;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean CONST(boolean v) {
/*  806 */     return v;
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
/*      */   public static byte CONST(byte v) {
/*  825 */     return v;
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
/*      */   public static byte CONST_BYTE(int v) throws IllegalArgumentException {
/*  848 */     if (v < -128 || v > 127) {
/*  849 */       throw new IllegalArgumentException("Supplied value must be a valid byte literal between -128 and 127: [" + v + "]");
/*      */     }
/*  851 */     return (byte)v;
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
/*      */   public static char CONST(char v) {
/*  871 */     return v;
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
/*      */   public static short CONST(short v) {
/*  890 */     return v;
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
/*      */   public static short CONST_SHORT(int v) throws IllegalArgumentException {
/*  913 */     if (v < -32768 || v > 32767) {
/*  914 */       throw new IllegalArgumentException("Supplied value must be a valid byte literal between -32768 and 32767: [" + v + "]");
/*      */     }
/*  916 */     return (short)v;
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
/*      */   public static int CONST(int v) {
/*  937 */     return v;
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
/*      */   public static long CONST(long v) {
/*  956 */     return v;
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
/*      */   public static float CONST(float v) {
/*  975 */     return v;
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
/*      */   public static double CONST(double v) {
/*  994 */     return v;
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
/*      */   public static <T> T CONST(T v) {
/* 1014 */     return v;
/*      */   }
/*      */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\commons\lang3\ObjectUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */