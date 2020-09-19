/*      */ package org.apache.commons.lang3;
/*      */ 
/*      */ import java.lang.reflect.Array;
/*      */ import java.util.Arrays;
/*      */ import java.util.BitSet;
/*      */ import java.util.Comparator;
/*      */ import java.util.HashMap;
/*      */ import java.util.Map;
/*      */ import org.apache.commons.lang3.builder.EqualsBuilder;
/*      */ import org.apache.commons.lang3.builder.HashCodeBuilder;
/*      */ import org.apache.commons.lang3.builder.ToStringBuilder;
/*      */ import org.apache.commons.lang3.builder.ToStringStyle;
/*      */ import org.apache.commons.lang3.math.NumberUtils;
/*      */ import org.apache.commons.lang3.mutable.MutableInt;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class ArrayUtils
/*      */ {
/*   50 */   public static final Object[] EMPTY_OBJECT_ARRAY = new Object[0];
/*      */ 
/*      */ 
/*      */   
/*   54 */   public static final Class<?>[] EMPTY_CLASS_ARRAY = new Class[0];
/*      */ 
/*      */ 
/*      */   
/*   58 */   public static final String[] EMPTY_STRING_ARRAY = new String[0];
/*      */ 
/*      */ 
/*      */   
/*   62 */   public static final long[] EMPTY_LONG_ARRAY = new long[0];
/*      */ 
/*      */ 
/*      */   
/*   66 */   public static final Long[] EMPTY_LONG_OBJECT_ARRAY = new Long[0];
/*      */ 
/*      */ 
/*      */   
/*   70 */   public static final int[] EMPTY_INT_ARRAY = new int[0];
/*      */ 
/*      */ 
/*      */   
/*   74 */   public static final Integer[] EMPTY_INTEGER_OBJECT_ARRAY = new Integer[0];
/*      */ 
/*      */ 
/*      */   
/*   78 */   public static final short[] EMPTY_SHORT_ARRAY = new short[0];
/*      */ 
/*      */ 
/*      */   
/*   82 */   public static final Short[] EMPTY_SHORT_OBJECT_ARRAY = new Short[0];
/*      */ 
/*      */ 
/*      */   
/*   86 */   public static final byte[] EMPTY_BYTE_ARRAY = new byte[0];
/*      */ 
/*      */ 
/*      */   
/*   90 */   public static final Byte[] EMPTY_BYTE_OBJECT_ARRAY = new Byte[0];
/*      */ 
/*      */ 
/*      */   
/*   94 */   public static final double[] EMPTY_DOUBLE_ARRAY = new double[0];
/*      */ 
/*      */ 
/*      */   
/*   98 */   public static final Double[] EMPTY_DOUBLE_OBJECT_ARRAY = new Double[0];
/*      */ 
/*      */ 
/*      */   
/*  102 */   public static final float[] EMPTY_FLOAT_ARRAY = new float[0];
/*      */ 
/*      */ 
/*      */   
/*  106 */   public static final Float[] EMPTY_FLOAT_OBJECT_ARRAY = new Float[0];
/*      */ 
/*      */ 
/*      */   
/*  110 */   public static final boolean[] EMPTY_BOOLEAN_ARRAY = new boolean[0];
/*      */ 
/*      */ 
/*      */   
/*  114 */   public static final Boolean[] EMPTY_BOOLEAN_OBJECT_ARRAY = new Boolean[0];
/*      */ 
/*      */ 
/*      */   
/*  118 */   public static final char[] EMPTY_CHAR_ARRAY = new char[0];
/*      */ 
/*      */ 
/*      */   
/*  122 */   public static final Character[] EMPTY_CHARACTER_OBJECT_ARRAY = new Character[0];
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final int INDEX_NOT_FOUND = -1;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String toString(Object array) {
/*  160 */     return toString(array, "{}");
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
/*      */   public static String toString(Object array, String stringIfNull) {
/*  176 */     if (array == null) {
/*  177 */       return stringIfNull;
/*      */     }
/*  179 */     return (new ToStringBuilder(array, ToStringStyle.SIMPLE_STYLE)).append(array).toString();
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
/*      */   public static int hashCode(Object array) {
/*  191 */     return (new HashCodeBuilder()).append(array).toHashCode();
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
/*      */   @Deprecated
/*      */   public static boolean isEquals(Object array1, Object array2) {
/*  208 */     return (new EqualsBuilder()).append(array1, array2).isEquals();
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
/*      */   public static Map<Object, Object> toMap(Object[] array) {
/*  239 */     if (array == null) {
/*  240 */       return null;
/*      */     }
/*  242 */     Map<Object, Object> map = new HashMap<Object, Object>((int)(array.length * 1.5D));
/*  243 */     for (int i = 0; i < array.length; i++) {
/*  244 */       Object object = array[i];
/*  245 */       if (object instanceof Map.Entry) {
/*  246 */         Map.Entry<?, ?> entry = (Map.Entry<?, ?>)object;
/*  247 */         map.put(entry.getKey(), entry.getValue());
/*  248 */       } else if (object instanceof Object[]) {
/*  249 */         Object[] entry = (Object[])object;
/*  250 */         if (entry.length < 2) {
/*  251 */           throw new IllegalArgumentException("Array element " + i + ", '" + object + "', has a length less than 2");
/*      */         }
/*      */ 
/*      */         
/*  255 */         map.put(entry[0], entry[1]);
/*      */       } else {
/*  257 */         throw new IllegalArgumentException("Array element " + i + ", '" + object + "', is neither of type Map.Entry nor an Array");
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/*  262 */     return map;
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
/*      */   public static <T> T[] toArray(T... items) {
/*  305 */     return items;
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
/*      */   public static <T> T[] clone(T[] array) {
/*  324 */     if (array == null) {
/*  325 */       return null;
/*      */     }
/*  327 */     return (T[])array.clone();
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
/*      */   public static long[] clone(long[] array) {
/*  340 */     if (array == null) {
/*  341 */       return null;
/*      */     }
/*  343 */     return (long[])array.clone();
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
/*      */   public static int[] clone(int[] array) {
/*  356 */     if (array == null) {
/*  357 */       return null;
/*      */     }
/*  359 */     return (int[])array.clone();
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
/*      */   public static short[] clone(short[] array) {
/*  372 */     if (array == null) {
/*  373 */       return null;
/*      */     }
/*  375 */     return (short[])array.clone();
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
/*      */   public static char[] clone(char[] array) {
/*  388 */     if (array == null) {
/*  389 */       return null;
/*      */     }
/*  391 */     return (char[])array.clone();
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
/*      */   public static byte[] clone(byte[] array) {
/*  404 */     if (array == null) {
/*  405 */       return null;
/*      */     }
/*  407 */     return (byte[])array.clone();
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
/*      */   public static double[] clone(double[] array) {
/*  420 */     if (array == null) {
/*  421 */       return null;
/*      */     }
/*  423 */     return (double[])array.clone();
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
/*      */   public static float[] clone(float[] array) {
/*  436 */     if (array == null) {
/*  437 */       return null;
/*      */     }
/*  439 */     return (float[])array.clone();
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
/*      */   public static boolean[] clone(boolean[] array) {
/*  452 */     if (array == null) {
/*  453 */       return null;
/*      */     }
/*  455 */     return (boolean[])array.clone();
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
/*      */   public static <T> T[] nullToEmpty(T[] array, Class<T[]> type) {
/*  474 */     if (type == null) {
/*  475 */       throw new IllegalArgumentException("The type must not be null");
/*      */     }
/*      */     
/*  478 */     if (array == null) {
/*  479 */       return type.cast(Array.newInstance(type.getComponentType(), 0));
/*      */     }
/*  481 */     return array;
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
/*      */   public static Object[] nullToEmpty(Object[] array) {
/*  499 */     if (isEmpty(array)) {
/*  500 */       return EMPTY_OBJECT_ARRAY;
/*      */     }
/*  502 */     return array;
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
/*      */   public static Class<?>[] nullToEmpty(Class<?>[] array) {
/*  519 */     if (isEmpty((Object[])array)) {
/*  520 */       return EMPTY_CLASS_ARRAY;
/*      */     }
/*  522 */     return array;
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
/*      */   public static String[] nullToEmpty(String[] array) {
/*  539 */     if (isEmpty((Object[])array)) {
/*  540 */       return EMPTY_STRING_ARRAY;
/*      */     }
/*  542 */     return array;
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
/*      */   public static long[] nullToEmpty(long[] array) {
/*  559 */     if (isEmpty(array)) {
/*  560 */       return EMPTY_LONG_ARRAY;
/*      */     }
/*  562 */     return array;
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
/*      */   public static int[] nullToEmpty(int[] array) {
/*  579 */     if (isEmpty(array)) {
/*  580 */       return EMPTY_INT_ARRAY;
/*      */     }
/*  582 */     return array;
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
/*      */   public static short[] nullToEmpty(short[] array) {
/*  599 */     if (isEmpty(array)) {
/*  600 */       return EMPTY_SHORT_ARRAY;
/*      */     }
/*  602 */     return array;
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
/*      */   public static char[] nullToEmpty(char[] array) {
/*  619 */     if (isEmpty(array)) {
/*  620 */       return EMPTY_CHAR_ARRAY;
/*      */     }
/*  622 */     return array;
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
/*      */   public static byte[] nullToEmpty(byte[] array) {
/*  639 */     if (isEmpty(array)) {
/*  640 */       return EMPTY_BYTE_ARRAY;
/*      */     }
/*  642 */     return array;
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
/*      */   public static double[] nullToEmpty(double[] array) {
/*  659 */     if (isEmpty(array)) {
/*  660 */       return EMPTY_DOUBLE_ARRAY;
/*      */     }
/*  662 */     return array;
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
/*      */   public static float[] nullToEmpty(float[] array) {
/*  679 */     if (isEmpty(array)) {
/*  680 */       return EMPTY_FLOAT_ARRAY;
/*      */     }
/*  682 */     return array;
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
/*      */   public static boolean[] nullToEmpty(boolean[] array) {
/*  699 */     if (isEmpty(array)) {
/*  700 */       return EMPTY_BOOLEAN_ARRAY;
/*      */     }
/*  702 */     return array;
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
/*      */   public static Long[] nullToEmpty(Long[] array) {
/*  719 */     if (isEmpty((Object[])array)) {
/*  720 */       return EMPTY_LONG_OBJECT_ARRAY;
/*      */     }
/*  722 */     return array;
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
/*      */   public static Integer[] nullToEmpty(Integer[] array) {
/*  739 */     if (isEmpty((Object[])array)) {
/*  740 */       return EMPTY_INTEGER_OBJECT_ARRAY;
/*      */     }
/*  742 */     return array;
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
/*      */   public static Short[] nullToEmpty(Short[] array) {
/*  759 */     if (isEmpty((Object[])array)) {
/*  760 */       return EMPTY_SHORT_OBJECT_ARRAY;
/*      */     }
/*  762 */     return array;
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
/*      */   public static Character[] nullToEmpty(Character[] array) {
/*  779 */     if (isEmpty((Object[])array)) {
/*  780 */       return EMPTY_CHARACTER_OBJECT_ARRAY;
/*      */     }
/*  782 */     return array;
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
/*      */   public static Byte[] nullToEmpty(Byte[] array) {
/*  799 */     if (isEmpty((Object[])array)) {
/*  800 */       return EMPTY_BYTE_OBJECT_ARRAY;
/*      */     }
/*  802 */     return array;
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
/*      */   public static Double[] nullToEmpty(Double[] array) {
/*  819 */     if (isEmpty((Object[])array)) {
/*  820 */       return EMPTY_DOUBLE_OBJECT_ARRAY;
/*      */     }
/*  822 */     return array;
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
/*      */   public static Float[] nullToEmpty(Float[] array) {
/*  839 */     if (isEmpty((Object[])array)) {
/*  840 */       return EMPTY_FLOAT_OBJECT_ARRAY;
/*      */     }
/*  842 */     return array;
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
/*      */   public static Boolean[] nullToEmpty(Boolean[] array) {
/*  859 */     if (isEmpty((Object[])array)) {
/*  860 */       return EMPTY_BOOLEAN_OBJECT_ARRAY;
/*      */     }
/*  862 */     return array;
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
/*      */   public static <T> T[] subarray(T[] array, int startIndexInclusive, int endIndexExclusive) {
/*  897 */     if (array == null) {
/*  898 */       return null;
/*      */     }
/*  900 */     if (startIndexInclusive < 0) {
/*  901 */       startIndexInclusive = 0;
/*      */     }
/*  903 */     if (endIndexExclusive > array.length) {
/*  904 */       endIndexExclusive = array.length;
/*      */     }
/*  906 */     int newSize = endIndexExclusive - startIndexInclusive;
/*  907 */     Class<?> type = array.getClass().getComponentType();
/*  908 */     if (newSize <= 0) {
/*      */       
/*  910 */       T[] emptyArray = (T[])Array.newInstance(type, 0);
/*  911 */       return emptyArray;
/*      */     } 
/*      */ 
/*      */     
/*  915 */     T[] subarray = (T[])Array.newInstance(type, newSize);
/*  916 */     System.arraycopy(array, startIndexInclusive, subarray, 0, newSize);
/*  917 */     return subarray;
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
/*      */   public static long[] subarray(long[] array, int startIndexInclusive, int endIndexExclusive) {
/*  941 */     if (array == null) {
/*  942 */       return null;
/*      */     }
/*  944 */     if (startIndexInclusive < 0) {
/*  945 */       startIndexInclusive = 0;
/*      */     }
/*  947 */     if (endIndexExclusive > array.length) {
/*  948 */       endIndexExclusive = array.length;
/*      */     }
/*  950 */     int newSize = endIndexExclusive - startIndexInclusive;
/*  951 */     if (newSize <= 0) {
/*  952 */       return EMPTY_LONG_ARRAY;
/*      */     }
/*      */     
/*  955 */     long[] subarray = new long[newSize];
/*  956 */     System.arraycopy(array, startIndexInclusive, subarray, 0, newSize);
/*  957 */     return subarray;
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
/*      */   public static int[] subarray(int[] array, int startIndexInclusive, int endIndexExclusive) {
/*  981 */     if (array == null) {
/*  982 */       return null;
/*      */     }
/*  984 */     if (startIndexInclusive < 0) {
/*  985 */       startIndexInclusive = 0;
/*      */     }
/*  987 */     if (endIndexExclusive > array.length) {
/*  988 */       endIndexExclusive = array.length;
/*      */     }
/*  990 */     int newSize = endIndexExclusive - startIndexInclusive;
/*  991 */     if (newSize <= 0) {
/*  992 */       return EMPTY_INT_ARRAY;
/*      */     }
/*      */     
/*  995 */     int[] subarray = new int[newSize];
/*  996 */     System.arraycopy(array, startIndexInclusive, subarray, 0, newSize);
/*  997 */     return subarray;
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
/*      */   public static short[] subarray(short[] array, int startIndexInclusive, int endIndexExclusive) {
/* 1021 */     if (array == null) {
/* 1022 */       return null;
/*      */     }
/* 1024 */     if (startIndexInclusive < 0) {
/* 1025 */       startIndexInclusive = 0;
/*      */     }
/* 1027 */     if (endIndexExclusive > array.length) {
/* 1028 */       endIndexExclusive = array.length;
/*      */     }
/* 1030 */     int newSize = endIndexExclusive - startIndexInclusive;
/* 1031 */     if (newSize <= 0) {
/* 1032 */       return EMPTY_SHORT_ARRAY;
/*      */     }
/*      */     
/* 1035 */     short[] subarray = new short[newSize];
/* 1036 */     System.arraycopy(array, startIndexInclusive, subarray, 0, newSize);
/* 1037 */     return subarray;
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
/*      */   public static char[] subarray(char[] array, int startIndexInclusive, int endIndexExclusive) {
/* 1061 */     if (array == null) {
/* 1062 */       return null;
/*      */     }
/* 1064 */     if (startIndexInclusive < 0) {
/* 1065 */       startIndexInclusive = 0;
/*      */     }
/* 1067 */     if (endIndexExclusive > array.length) {
/* 1068 */       endIndexExclusive = array.length;
/*      */     }
/* 1070 */     int newSize = endIndexExclusive - startIndexInclusive;
/* 1071 */     if (newSize <= 0) {
/* 1072 */       return EMPTY_CHAR_ARRAY;
/*      */     }
/*      */     
/* 1075 */     char[] subarray = new char[newSize];
/* 1076 */     System.arraycopy(array, startIndexInclusive, subarray, 0, newSize);
/* 1077 */     return subarray;
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
/*      */   public static byte[] subarray(byte[] array, int startIndexInclusive, int endIndexExclusive) {
/* 1101 */     if (array == null) {
/* 1102 */       return null;
/*      */     }
/* 1104 */     if (startIndexInclusive < 0) {
/* 1105 */       startIndexInclusive = 0;
/*      */     }
/* 1107 */     if (endIndexExclusive > array.length) {
/* 1108 */       endIndexExclusive = array.length;
/*      */     }
/* 1110 */     int newSize = endIndexExclusive - startIndexInclusive;
/* 1111 */     if (newSize <= 0) {
/* 1112 */       return EMPTY_BYTE_ARRAY;
/*      */     }
/*      */     
/* 1115 */     byte[] subarray = new byte[newSize];
/* 1116 */     System.arraycopy(array, startIndexInclusive, subarray, 0, newSize);
/* 1117 */     return subarray;
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
/*      */   public static double[] subarray(double[] array, int startIndexInclusive, int endIndexExclusive) {
/* 1141 */     if (array == null) {
/* 1142 */       return null;
/*      */     }
/* 1144 */     if (startIndexInclusive < 0) {
/* 1145 */       startIndexInclusive = 0;
/*      */     }
/* 1147 */     if (endIndexExclusive > array.length) {
/* 1148 */       endIndexExclusive = array.length;
/*      */     }
/* 1150 */     int newSize = endIndexExclusive - startIndexInclusive;
/* 1151 */     if (newSize <= 0) {
/* 1152 */       return EMPTY_DOUBLE_ARRAY;
/*      */     }
/*      */     
/* 1155 */     double[] subarray = new double[newSize];
/* 1156 */     System.arraycopy(array, startIndexInclusive, subarray, 0, newSize);
/* 1157 */     return subarray;
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
/*      */   public static float[] subarray(float[] array, int startIndexInclusive, int endIndexExclusive) {
/* 1181 */     if (array == null) {
/* 1182 */       return null;
/*      */     }
/* 1184 */     if (startIndexInclusive < 0) {
/* 1185 */       startIndexInclusive = 0;
/*      */     }
/* 1187 */     if (endIndexExclusive > array.length) {
/* 1188 */       endIndexExclusive = array.length;
/*      */     }
/* 1190 */     int newSize = endIndexExclusive - startIndexInclusive;
/* 1191 */     if (newSize <= 0) {
/* 1192 */       return EMPTY_FLOAT_ARRAY;
/*      */     }
/*      */     
/* 1195 */     float[] subarray = new float[newSize];
/* 1196 */     System.arraycopy(array, startIndexInclusive, subarray, 0, newSize);
/* 1197 */     return subarray;
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
/*      */   public static boolean[] subarray(boolean[] array, int startIndexInclusive, int endIndexExclusive) {
/* 1221 */     if (array == null) {
/* 1222 */       return null;
/*      */     }
/* 1224 */     if (startIndexInclusive < 0) {
/* 1225 */       startIndexInclusive = 0;
/*      */     }
/* 1227 */     if (endIndexExclusive > array.length) {
/* 1228 */       endIndexExclusive = array.length;
/*      */     }
/* 1230 */     int newSize = endIndexExclusive - startIndexInclusive;
/* 1231 */     if (newSize <= 0) {
/* 1232 */       return EMPTY_BOOLEAN_ARRAY;
/*      */     }
/*      */     
/* 1235 */     boolean[] subarray = new boolean[newSize];
/* 1236 */     System.arraycopy(array, startIndexInclusive, subarray, 0, newSize);
/* 1237 */     return subarray;
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
/*      */   public static boolean isSameLength(Object[] array1, Object[] array2) {
/* 1254 */     return (getLength(array1) == getLength(array2));
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
/*      */   public static boolean isSameLength(long[] array1, long[] array2) {
/* 1267 */     return (getLength(array1) == getLength(array2));
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
/*      */   public static boolean isSameLength(int[] array1, int[] array2) {
/* 1280 */     return (getLength(array1) == getLength(array2));
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
/*      */   public static boolean isSameLength(short[] array1, short[] array2) {
/* 1293 */     return (getLength(array1) == getLength(array2));
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
/*      */   public static boolean isSameLength(char[] array1, char[] array2) {
/* 1306 */     return (getLength(array1) == getLength(array2));
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
/*      */   public static boolean isSameLength(byte[] array1, byte[] array2) {
/* 1319 */     return (getLength(array1) == getLength(array2));
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
/*      */   public static boolean isSameLength(double[] array1, double[] array2) {
/* 1332 */     return (getLength(array1) == getLength(array2));
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
/*      */   public static boolean isSameLength(float[] array1, float[] array2) {
/* 1345 */     return (getLength(array1) == getLength(array2));
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
/*      */   public static boolean isSameLength(boolean[] array1, boolean[] array2) {
/* 1358 */     return (getLength(array1) == getLength(array2));
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
/*      */   public static int getLength(Object array) {
/* 1383 */     if (array == null) {
/* 1384 */       return 0;
/*      */     }
/* 1386 */     return Array.getLength(array);
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
/*      */   public static boolean isSameType(Object array1, Object array2) {
/* 1399 */     if (array1 == null || array2 == null) {
/* 1400 */       throw new IllegalArgumentException("The Array must not be null");
/*      */     }
/* 1402 */     return array1.getClass().getName().equals(array2.getClass().getName());
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
/*      */   public static void reverse(Object[] array) {
/* 1417 */     if (array == null) {
/*      */       return;
/*      */     }
/* 1420 */     reverse(array, 0, array.length);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void reverse(long[] array) {
/* 1431 */     if (array == null) {
/*      */       return;
/*      */     }
/* 1434 */     reverse(array, 0, array.length);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void reverse(int[] array) {
/* 1445 */     if (array == null) {
/*      */       return;
/*      */     }
/* 1448 */     reverse(array, 0, array.length);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void reverse(short[] array) {
/* 1459 */     if (array == null) {
/*      */       return;
/*      */     }
/* 1462 */     reverse(array, 0, array.length);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void reverse(char[] array) {
/* 1473 */     if (array == null) {
/*      */       return;
/*      */     }
/* 1476 */     reverse(array, 0, array.length);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void reverse(byte[] array) {
/* 1487 */     if (array == null) {
/*      */       return;
/*      */     }
/* 1490 */     reverse(array, 0, array.length);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void reverse(double[] array) {
/* 1501 */     if (array == null) {
/*      */       return;
/*      */     }
/* 1504 */     reverse(array, 0, array.length);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void reverse(float[] array) {
/* 1515 */     if (array == null) {
/*      */       return;
/*      */     }
/* 1518 */     reverse(array, 0, array.length);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void reverse(boolean[] array) {
/* 1529 */     if (array == null) {
/*      */       return;
/*      */     }
/* 1532 */     reverse(array, 0, array.length);
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
/*      */   public static void reverse(boolean[] array, int startIndexInclusive, int endIndexExclusive) {
/* 1553 */     if (array == null) {
/*      */       return;
/*      */     }
/* 1556 */     int i = (startIndexInclusive < 0) ? 0 : startIndexInclusive;
/* 1557 */     int j = Math.min(array.length, endIndexExclusive) - 1;
/*      */     
/* 1559 */     while (j > i) {
/* 1560 */       boolean tmp = array[j];
/* 1561 */       array[j] = array[i];
/* 1562 */       array[i] = tmp;
/* 1563 */       j--;
/* 1564 */       i++;
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
/*      */   public static void reverse(byte[] array, int startIndexInclusive, int endIndexExclusive) {
/* 1586 */     if (array == null) {
/*      */       return;
/*      */     }
/* 1589 */     int i = (startIndexInclusive < 0) ? 0 : startIndexInclusive;
/* 1590 */     int j = Math.min(array.length, endIndexExclusive) - 1;
/*      */     
/* 1592 */     while (j > i) {
/* 1593 */       byte tmp = array[j];
/* 1594 */       array[j] = array[i];
/* 1595 */       array[i] = tmp;
/* 1596 */       j--;
/* 1597 */       i++;
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
/*      */   public static void reverse(char[] array, int startIndexInclusive, int endIndexExclusive) {
/* 1619 */     if (array == null) {
/*      */       return;
/*      */     }
/* 1622 */     int i = (startIndexInclusive < 0) ? 0 : startIndexInclusive;
/* 1623 */     int j = Math.min(array.length, endIndexExclusive) - 1;
/*      */     
/* 1625 */     while (j > i) {
/* 1626 */       char tmp = array[j];
/* 1627 */       array[j] = array[i];
/* 1628 */       array[i] = tmp;
/* 1629 */       j--;
/* 1630 */       i++;
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
/*      */   public static void reverse(double[] array, int startIndexInclusive, int endIndexExclusive) {
/* 1652 */     if (array == null) {
/*      */       return;
/*      */     }
/* 1655 */     int i = (startIndexInclusive < 0) ? 0 : startIndexInclusive;
/* 1656 */     int j = Math.min(array.length, endIndexExclusive) - 1;
/*      */     
/* 1658 */     while (j > i) {
/* 1659 */       double tmp = array[j];
/* 1660 */       array[j] = array[i];
/* 1661 */       array[i] = tmp;
/* 1662 */       j--;
/* 1663 */       i++;
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
/*      */   public static void reverse(float[] array, int startIndexInclusive, int endIndexExclusive) {
/* 1685 */     if (array == null) {
/*      */       return;
/*      */     }
/* 1688 */     int i = (startIndexInclusive < 0) ? 0 : startIndexInclusive;
/* 1689 */     int j = Math.min(array.length, endIndexExclusive) - 1;
/*      */     
/* 1691 */     while (j > i) {
/* 1692 */       float tmp = array[j];
/* 1693 */       array[j] = array[i];
/* 1694 */       array[i] = tmp;
/* 1695 */       j--;
/* 1696 */       i++;
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
/*      */   public static void reverse(int[] array, int startIndexInclusive, int endIndexExclusive) {
/* 1718 */     if (array == null) {
/*      */       return;
/*      */     }
/* 1721 */     int i = (startIndexInclusive < 0) ? 0 : startIndexInclusive;
/* 1722 */     int j = Math.min(array.length, endIndexExclusive) - 1;
/*      */     
/* 1724 */     while (j > i) {
/* 1725 */       int tmp = array[j];
/* 1726 */       array[j] = array[i];
/* 1727 */       array[i] = tmp;
/* 1728 */       j--;
/* 1729 */       i++;
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
/*      */   public static void reverse(long[] array, int startIndexInclusive, int endIndexExclusive) {
/* 1751 */     if (array == null) {
/*      */       return;
/*      */     }
/* 1754 */     int i = (startIndexInclusive < 0) ? 0 : startIndexInclusive;
/* 1755 */     int j = Math.min(array.length, endIndexExclusive) - 1;
/*      */     
/* 1757 */     while (j > i) {
/* 1758 */       long tmp = array[j];
/* 1759 */       array[j] = array[i];
/* 1760 */       array[i] = tmp;
/* 1761 */       j--;
/* 1762 */       i++;
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
/*      */   public static void reverse(Object[] array, int startIndexInclusive, int endIndexExclusive) {
/* 1784 */     if (array == null) {
/*      */       return;
/*      */     }
/* 1787 */     int i = (startIndexInclusive < 0) ? 0 : startIndexInclusive;
/* 1788 */     int j = Math.min(array.length, endIndexExclusive) - 1;
/*      */     
/* 1790 */     while (j > i) {
/* 1791 */       Object tmp = array[j];
/* 1792 */       array[j] = array[i];
/* 1793 */       array[i] = tmp;
/* 1794 */       j--;
/* 1795 */       i++;
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
/*      */   public static void reverse(short[] array, int startIndexInclusive, int endIndexExclusive) {
/* 1817 */     if (array == null) {
/*      */       return;
/*      */     }
/* 1820 */     int i = (startIndexInclusive < 0) ? 0 : startIndexInclusive;
/* 1821 */     int j = Math.min(array.length, endIndexExclusive) - 1;
/*      */     
/* 1823 */     while (j > i) {
/* 1824 */       short tmp = array[j];
/* 1825 */       array[j] = array[i];
/* 1826 */       array[i] = tmp;
/* 1827 */       j--;
/* 1828 */       i++;
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
/*      */   public static void swap(Object[] array, int offset1, int offset2) {
/* 1856 */     if (array == null || array.length == 0) {
/*      */       return;
/*      */     }
/* 1859 */     swap(array, offset1, offset2, 1);
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
/*      */   public static void swap(long[] array, int offset1, int offset2) {
/* 1885 */     if (array == null || array.length == 0) {
/*      */       return;
/*      */     }
/* 1888 */     swap(array, offset1, offset2, 1);
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
/*      */   public static void swap(int[] array, int offset1, int offset2) {
/* 1913 */     if (array == null || array.length == 0) {
/*      */       return;
/*      */     }
/* 1916 */     swap(array, offset1, offset2, 1);
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
/*      */   public static void swap(short[] array, int offset1, int offset2) {
/* 1941 */     if (array == null || array.length == 0) {
/*      */       return;
/*      */     }
/* 1944 */     swap(array, offset1, offset2, 1);
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
/*      */   public static void swap(char[] array, int offset1, int offset2) {
/* 1969 */     if (array == null || array.length == 0) {
/*      */       return;
/*      */     }
/* 1972 */     swap(array, offset1, offset2, 1);
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
/*      */   public static void swap(byte[] array, int offset1, int offset2) {
/* 1997 */     if (array == null || array.length == 0) {
/*      */       return;
/*      */     }
/* 2000 */     swap(array, offset1, offset2, 1);
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
/*      */   public static void swap(double[] array, int offset1, int offset2) {
/* 2025 */     if (array == null || array.length == 0) {
/*      */       return;
/*      */     }
/* 2028 */     swap(array, offset1, offset2, 1);
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
/*      */   public static void swap(float[] array, int offset1, int offset2) {
/* 2053 */     if (array == null || array.length == 0) {
/*      */       return;
/*      */     }
/* 2056 */     swap(array, offset1, offset2, 1);
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
/*      */   public static void swap(boolean[] array, int offset1, int offset2) {
/* 2081 */     if (array == null || array.length == 0) {
/*      */       return;
/*      */     }
/* 2084 */     swap(array, offset1, offset2, 1);
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
/*      */   public static void swap(boolean[] array, int offset1, int offset2, int len) {
/* 2112 */     if (array == null || array.length == 0 || offset1 >= array.length || offset2 >= array.length) {
/*      */       return;
/*      */     }
/* 2115 */     if (offset1 < 0) {
/* 2116 */       offset1 = 0;
/*      */     }
/* 2118 */     if (offset2 < 0) {
/* 2119 */       offset2 = 0;
/*      */     }
/* 2121 */     len = Math.min(Math.min(len, array.length - offset1), array.length - offset2);
/* 2122 */     for (int i = 0; i < len; i++, offset1++, offset2++) {
/* 2123 */       boolean aux = array[offset1];
/* 2124 */       array[offset1] = array[offset2];
/* 2125 */       array[offset2] = aux;
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
/*      */   public static void swap(byte[] array, int offset1, int offset2, int len) {
/* 2154 */     if (array == null || array.length == 0 || offset1 >= array.length || offset2 >= array.length) {
/*      */       return;
/*      */     }
/* 2157 */     if (offset1 < 0) {
/* 2158 */       offset1 = 0;
/*      */     }
/* 2160 */     if (offset2 < 0) {
/* 2161 */       offset2 = 0;
/*      */     }
/* 2163 */     len = Math.min(Math.min(len, array.length - offset1), array.length - offset2);
/* 2164 */     for (int i = 0; i < len; i++, offset1++, offset2++) {
/* 2165 */       byte aux = array[offset1];
/* 2166 */       array[offset1] = array[offset2];
/* 2167 */       array[offset2] = aux;
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
/*      */   public static void swap(char[] array, int offset1, int offset2, int len) {
/* 2196 */     if (array == null || array.length == 0 || offset1 >= array.length || offset2 >= array.length) {
/*      */       return;
/*      */     }
/* 2199 */     if (offset1 < 0) {
/* 2200 */       offset1 = 0;
/*      */     }
/* 2202 */     if (offset2 < 0) {
/* 2203 */       offset2 = 0;
/*      */     }
/* 2205 */     len = Math.min(Math.min(len, array.length - offset1), array.length - offset2);
/* 2206 */     for (int i = 0; i < len; i++, offset1++, offset2++) {
/* 2207 */       char aux = array[offset1];
/* 2208 */       array[offset1] = array[offset2];
/* 2209 */       array[offset2] = aux;
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
/*      */   public static void swap(double[] array, int offset1, int offset2, int len) {
/* 2238 */     if (array == null || array.length == 0 || offset1 >= array.length || offset2 >= array.length) {
/*      */       return;
/*      */     }
/* 2241 */     if (offset1 < 0) {
/* 2242 */       offset1 = 0;
/*      */     }
/* 2244 */     if (offset2 < 0) {
/* 2245 */       offset2 = 0;
/*      */     }
/* 2247 */     len = Math.min(Math.min(len, array.length - offset1), array.length - offset2);
/* 2248 */     for (int i = 0; i < len; i++, offset1++, offset2++) {
/* 2249 */       double aux = array[offset1];
/* 2250 */       array[offset1] = array[offset2];
/* 2251 */       array[offset2] = aux;
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
/*      */   public static void swap(float[] array, int offset1, int offset2, int len) {
/* 2280 */     if (array == null || array.length == 0 || offset1 >= array.length || offset2 >= array.length) {
/*      */       return;
/*      */     }
/* 2283 */     if (offset1 < 0) {
/* 2284 */       offset1 = 0;
/*      */     }
/* 2286 */     if (offset2 < 0) {
/* 2287 */       offset2 = 0;
/*      */     }
/* 2289 */     len = Math.min(Math.min(len, array.length - offset1), array.length - offset2);
/* 2290 */     for (int i = 0; i < len; i++, offset1++, offset2++) {
/* 2291 */       float aux = array[offset1];
/* 2292 */       array[offset1] = array[offset2];
/* 2293 */       array[offset2] = aux;
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
/*      */   public static void swap(int[] array, int offset1, int offset2, int len) {
/* 2323 */     if (array == null || array.length == 0 || offset1 >= array.length || offset2 >= array.length) {
/*      */       return;
/*      */     }
/* 2326 */     if (offset1 < 0) {
/* 2327 */       offset1 = 0;
/*      */     }
/* 2329 */     if (offset2 < 0) {
/* 2330 */       offset2 = 0;
/*      */     }
/* 2332 */     len = Math.min(Math.min(len, array.length - offset1), array.length - offset2);
/* 2333 */     for (int i = 0; i < len; i++, offset1++, offset2++) {
/* 2334 */       int aux = array[offset1];
/* 2335 */       array[offset1] = array[offset2];
/* 2336 */       array[offset2] = aux;
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
/*      */   public static void swap(long[] array, int offset1, int offset2, int len) {
/* 2365 */     if (array == null || array.length == 0 || offset1 >= array.length || offset2 >= array.length) {
/*      */       return;
/*      */     }
/* 2368 */     if (offset1 < 0) {
/* 2369 */       offset1 = 0;
/*      */     }
/* 2371 */     if (offset2 < 0) {
/* 2372 */       offset2 = 0;
/*      */     }
/* 2374 */     len = Math.min(Math.min(len, array.length - offset1), array.length - offset2);
/* 2375 */     for (int i = 0; i < len; i++, offset1++, offset2++) {
/* 2376 */       long aux = array[offset1];
/* 2377 */       array[offset1] = array[offset2];
/* 2378 */       array[offset2] = aux;
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
/*      */   public static void swap(Object[] array, int offset1, int offset2, int len) {
/* 2407 */     if (array == null || array.length == 0 || offset1 >= array.length || offset2 >= array.length) {
/*      */       return;
/*      */     }
/* 2410 */     if (offset1 < 0) {
/* 2411 */       offset1 = 0;
/*      */     }
/* 2413 */     if (offset2 < 0) {
/* 2414 */       offset2 = 0;
/*      */     }
/* 2416 */     len = Math.min(Math.min(len, array.length - offset1), array.length - offset2);
/* 2417 */     for (int i = 0; i < len; i++, offset1++, offset2++) {
/* 2418 */       Object aux = array[offset1];
/* 2419 */       array[offset1] = array[offset2];
/* 2420 */       array[offset2] = aux;
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
/*      */   public static void swap(short[] array, int offset1, int offset2, int len) {
/* 2449 */     if (array == null || array.length == 0 || offset1 >= array.length || offset2 >= array.length) {
/*      */       return;
/*      */     }
/* 2452 */     if (offset1 < 0) {
/* 2453 */       offset1 = 0;
/*      */     }
/* 2455 */     if (offset2 < 0) {
/* 2456 */       offset2 = 0;
/*      */     }
/* 2458 */     if (offset1 == offset2) {
/*      */       return;
/*      */     }
/* 2461 */     len = Math.min(Math.min(len, array.length - offset1), array.length - offset2);
/* 2462 */     for (int i = 0; i < len; i++, offset1++, offset2++) {
/* 2463 */       short aux = array[offset1];
/* 2464 */       array[offset1] = array[offset2];
/* 2465 */       array[offset2] = aux;
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
/*      */   public static void shift(Object[] array, int offset) {
/* 2484 */     if (array == null) {
/*      */       return;
/*      */     }
/* 2487 */     shift(array, 0, array.length, offset);
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
/*      */   public static void shift(long[] array, int offset) {
/* 2503 */     if (array == null) {
/*      */       return;
/*      */     }
/* 2506 */     shift(array, 0, array.length, offset);
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
/*      */   public static void shift(int[] array, int offset) {
/* 2522 */     if (array == null) {
/*      */       return;
/*      */     }
/* 2525 */     shift(array, 0, array.length, offset);
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
/*      */   public static void shift(short[] array, int offset) {
/* 2541 */     if (array == null) {
/*      */       return;
/*      */     }
/* 2544 */     shift(array, 0, array.length, offset);
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
/*      */   public static void shift(char[] array, int offset) {
/* 2560 */     if (array == null) {
/*      */       return;
/*      */     }
/* 2563 */     shift(array, 0, array.length, offset);
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
/*      */   public static void shift(byte[] array, int offset) {
/* 2579 */     if (array == null) {
/*      */       return;
/*      */     }
/* 2582 */     shift(array, 0, array.length, offset);
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
/*      */   public static void shift(double[] array, int offset) {
/* 2598 */     if (array == null) {
/*      */       return;
/*      */     }
/* 2601 */     shift(array, 0, array.length, offset);
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
/*      */   public static void shift(float[] array, int offset) {
/* 2617 */     if (array == null) {
/*      */       return;
/*      */     }
/* 2620 */     shift(array, 0, array.length, offset);
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
/*      */   public static void shift(boolean[] array, int offset) {
/* 2636 */     if (array == null) {
/*      */       return;
/*      */     }
/* 2639 */     shift(array, 0, array.length, offset);
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
/*      */   public static void shift(boolean[] array, int startIndexInclusive, int endIndexExclusive, int offset) {
/* 2662 */     if (array == null) {
/*      */       return;
/*      */     }
/* 2665 */     if (startIndexInclusive >= array.length - 1 || endIndexExclusive <= 0) {
/*      */       return;
/*      */     }
/* 2668 */     if (startIndexInclusive < 0) {
/* 2669 */       startIndexInclusive = 0;
/*      */     }
/* 2671 */     if (endIndexExclusive >= array.length) {
/* 2672 */       endIndexExclusive = array.length;
/*      */     }
/* 2674 */     int n = endIndexExclusive - startIndexInclusive;
/* 2675 */     if (n <= 1) {
/*      */       return;
/*      */     }
/* 2678 */     offset %= n;
/* 2679 */     if (offset < 0) {
/* 2680 */       offset += n;
/*      */     }
/*      */ 
/*      */     
/* 2684 */     while (n > 1 && offset > 0) {
/* 2685 */       int n_offset = n - offset;
/*      */       
/* 2687 */       if (offset > n_offset) {
/* 2688 */         swap(array, startIndexInclusive, startIndexInclusive + n - n_offset, n_offset);
/* 2689 */         n = offset;
/* 2690 */         offset -= n_offset; continue;
/* 2691 */       }  if (offset < n_offset) {
/* 2692 */         swap(array, startIndexInclusive, startIndexInclusive + n_offset, offset);
/* 2693 */         startIndexInclusive += offset;
/* 2694 */         n = n_offset; continue;
/*      */       } 
/* 2696 */       swap(array, startIndexInclusive, startIndexInclusive + n_offset, offset);
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
/*      */   public static void shift(byte[] array, int startIndexInclusive, int endIndexExclusive, int offset) {
/* 2722 */     if (array == null) {
/*      */       return;
/*      */     }
/* 2725 */     if (startIndexInclusive >= array.length - 1 || endIndexExclusive <= 0) {
/*      */       return;
/*      */     }
/* 2728 */     if (startIndexInclusive < 0) {
/* 2729 */       startIndexInclusive = 0;
/*      */     }
/* 2731 */     if (endIndexExclusive >= array.length) {
/* 2732 */       endIndexExclusive = array.length;
/*      */     }
/* 2734 */     int n = endIndexExclusive - startIndexInclusive;
/* 2735 */     if (n <= 1) {
/*      */       return;
/*      */     }
/* 2738 */     offset %= n;
/* 2739 */     if (offset < 0) {
/* 2740 */       offset += n;
/*      */     }
/*      */ 
/*      */     
/* 2744 */     while (n > 1 && offset > 0) {
/* 2745 */       int n_offset = n - offset;
/*      */       
/* 2747 */       if (offset > n_offset) {
/* 2748 */         swap(array, startIndexInclusive, startIndexInclusive + n - n_offset, n_offset);
/* 2749 */         n = offset;
/* 2750 */         offset -= n_offset; continue;
/* 2751 */       }  if (offset < n_offset) {
/* 2752 */         swap(array, startIndexInclusive, startIndexInclusive + n_offset, offset);
/* 2753 */         startIndexInclusive += offset;
/* 2754 */         n = n_offset; continue;
/*      */       } 
/* 2756 */       swap(array, startIndexInclusive, startIndexInclusive + n_offset, offset);
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
/*      */   public static void shift(char[] array, int startIndexInclusive, int endIndexExclusive, int offset) {
/* 2782 */     if (array == null) {
/*      */       return;
/*      */     }
/* 2785 */     if (startIndexInclusive >= array.length - 1 || endIndexExclusive <= 0) {
/*      */       return;
/*      */     }
/* 2788 */     if (startIndexInclusive < 0) {
/* 2789 */       startIndexInclusive = 0;
/*      */     }
/* 2791 */     if (endIndexExclusive >= array.length) {
/* 2792 */       endIndexExclusive = array.length;
/*      */     }
/* 2794 */     int n = endIndexExclusive - startIndexInclusive;
/* 2795 */     if (n <= 1) {
/*      */       return;
/*      */     }
/* 2798 */     offset %= n;
/* 2799 */     if (offset < 0) {
/* 2800 */       offset += n;
/*      */     }
/*      */ 
/*      */     
/* 2804 */     while (n > 1 && offset > 0) {
/* 2805 */       int n_offset = n - offset;
/*      */       
/* 2807 */       if (offset > n_offset) {
/* 2808 */         swap(array, startIndexInclusive, startIndexInclusive + n - n_offset, n_offset);
/* 2809 */         n = offset;
/* 2810 */         offset -= n_offset; continue;
/* 2811 */       }  if (offset < n_offset) {
/* 2812 */         swap(array, startIndexInclusive, startIndexInclusive + n_offset, offset);
/* 2813 */         startIndexInclusive += offset;
/* 2814 */         n = n_offset; continue;
/*      */       } 
/* 2816 */       swap(array, startIndexInclusive, startIndexInclusive + n_offset, offset);
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
/*      */   public static void shift(double[] array, int startIndexInclusive, int endIndexExclusive, int offset) {
/* 2842 */     if (array == null) {
/*      */       return;
/*      */     }
/* 2845 */     if (startIndexInclusive >= array.length - 1 || endIndexExclusive <= 0) {
/*      */       return;
/*      */     }
/* 2848 */     if (startIndexInclusive < 0) {
/* 2849 */       startIndexInclusive = 0;
/*      */     }
/* 2851 */     if (endIndexExclusive >= array.length) {
/* 2852 */       endIndexExclusive = array.length;
/*      */     }
/* 2854 */     int n = endIndexExclusive - startIndexInclusive;
/* 2855 */     if (n <= 1) {
/*      */       return;
/*      */     }
/* 2858 */     offset %= n;
/* 2859 */     if (offset < 0) {
/* 2860 */       offset += n;
/*      */     }
/*      */ 
/*      */     
/* 2864 */     while (n > 1 && offset > 0) {
/* 2865 */       int n_offset = n - offset;
/*      */       
/* 2867 */       if (offset > n_offset) {
/* 2868 */         swap(array, startIndexInclusive, startIndexInclusive + n - n_offset, n_offset);
/* 2869 */         n = offset;
/* 2870 */         offset -= n_offset; continue;
/* 2871 */       }  if (offset < n_offset) {
/* 2872 */         swap(array, startIndexInclusive, startIndexInclusive + n_offset, offset);
/* 2873 */         startIndexInclusive += offset;
/* 2874 */         n = n_offset; continue;
/*      */       } 
/* 2876 */       swap(array, startIndexInclusive, startIndexInclusive + n_offset, offset);
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
/*      */   public static void shift(float[] array, int startIndexInclusive, int endIndexExclusive, int offset) {
/* 2902 */     if (array == null) {
/*      */       return;
/*      */     }
/* 2905 */     if (startIndexInclusive >= array.length - 1 || endIndexExclusive <= 0) {
/*      */       return;
/*      */     }
/* 2908 */     if (startIndexInclusive < 0) {
/* 2909 */       startIndexInclusive = 0;
/*      */     }
/* 2911 */     if (endIndexExclusive >= array.length) {
/* 2912 */       endIndexExclusive = array.length;
/*      */     }
/* 2914 */     int n = endIndexExclusive - startIndexInclusive;
/* 2915 */     if (n <= 1) {
/*      */       return;
/*      */     }
/* 2918 */     offset %= n;
/* 2919 */     if (offset < 0) {
/* 2920 */       offset += n;
/*      */     }
/*      */ 
/*      */     
/* 2924 */     while (n > 1 && offset > 0) {
/* 2925 */       int n_offset = n - offset;
/*      */       
/* 2927 */       if (offset > n_offset) {
/* 2928 */         swap(array, startIndexInclusive, startIndexInclusive + n - n_offset, n_offset);
/* 2929 */         n = offset;
/* 2930 */         offset -= n_offset; continue;
/* 2931 */       }  if (offset < n_offset) {
/* 2932 */         swap(array, startIndexInclusive, startIndexInclusive + n_offset, offset);
/* 2933 */         startIndexInclusive += offset;
/* 2934 */         n = n_offset; continue;
/*      */       } 
/* 2936 */       swap(array, startIndexInclusive, startIndexInclusive + n_offset, offset);
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
/*      */   public static void shift(int[] array, int startIndexInclusive, int endIndexExclusive, int offset) {
/* 2962 */     if (array == null) {
/*      */       return;
/*      */     }
/* 2965 */     if (startIndexInclusive >= array.length - 1 || endIndexExclusive <= 0) {
/*      */       return;
/*      */     }
/* 2968 */     if (startIndexInclusive < 0) {
/* 2969 */       startIndexInclusive = 0;
/*      */     }
/* 2971 */     if (endIndexExclusive >= array.length) {
/* 2972 */       endIndexExclusive = array.length;
/*      */     }
/* 2974 */     int n = endIndexExclusive - startIndexInclusive;
/* 2975 */     if (n <= 1) {
/*      */       return;
/*      */     }
/* 2978 */     offset %= n;
/* 2979 */     if (offset < 0) {
/* 2980 */       offset += n;
/*      */     }
/*      */ 
/*      */     
/* 2984 */     while (n > 1 && offset > 0) {
/* 2985 */       int n_offset = n - offset;
/*      */       
/* 2987 */       if (offset > n_offset) {
/* 2988 */         swap(array, startIndexInclusive, startIndexInclusive + n - n_offset, n_offset);
/* 2989 */         n = offset;
/* 2990 */         offset -= n_offset; continue;
/* 2991 */       }  if (offset < n_offset) {
/* 2992 */         swap(array, startIndexInclusive, startIndexInclusive + n_offset, offset);
/* 2993 */         startIndexInclusive += offset;
/* 2994 */         n = n_offset; continue;
/*      */       } 
/* 2996 */       swap(array, startIndexInclusive, startIndexInclusive + n_offset, offset);
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
/*      */   public static void shift(long[] array, int startIndexInclusive, int endIndexExclusive, int offset) {
/* 3022 */     if (array == null) {
/*      */       return;
/*      */     }
/* 3025 */     if (startIndexInclusive >= array.length - 1 || endIndexExclusive <= 0) {
/*      */       return;
/*      */     }
/* 3028 */     if (startIndexInclusive < 0) {
/* 3029 */       startIndexInclusive = 0;
/*      */     }
/* 3031 */     if (endIndexExclusive >= array.length) {
/* 3032 */       endIndexExclusive = array.length;
/*      */     }
/* 3034 */     int n = endIndexExclusive - startIndexInclusive;
/* 3035 */     if (n <= 1) {
/*      */       return;
/*      */     }
/* 3038 */     offset %= n;
/* 3039 */     if (offset < 0) {
/* 3040 */       offset += n;
/*      */     }
/*      */ 
/*      */     
/* 3044 */     while (n > 1 && offset > 0) {
/* 3045 */       int n_offset = n - offset;
/*      */       
/* 3047 */       if (offset > n_offset) {
/* 3048 */         swap(array, startIndexInclusive, startIndexInclusive + n - n_offset, n_offset);
/* 3049 */         n = offset;
/* 3050 */         offset -= n_offset; continue;
/* 3051 */       }  if (offset < n_offset) {
/* 3052 */         swap(array, startIndexInclusive, startIndexInclusive + n_offset, offset);
/* 3053 */         startIndexInclusive += offset;
/* 3054 */         n = n_offset; continue;
/*      */       } 
/* 3056 */       swap(array, startIndexInclusive, startIndexInclusive + n_offset, offset);
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
/*      */   public static void shift(Object[] array, int startIndexInclusive, int endIndexExclusive, int offset) {
/* 3082 */     if (array == null) {
/*      */       return;
/*      */     }
/* 3085 */     if (startIndexInclusive >= array.length - 1 || endIndexExclusive <= 0) {
/*      */       return;
/*      */     }
/* 3088 */     if (startIndexInclusive < 0) {
/* 3089 */       startIndexInclusive = 0;
/*      */     }
/* 3091 */     if (endIndexExclusive >= array.length) {
/* 3092 */       endIndexExclusive = array.length;
/*      */     }
/* 3094 */     int n = endIndexExclusive - startIndexInclusive;
/* 3095 */     if (n <= 1) {
/*      */       return;
/*      */     }
/* 3098 */     offset %= n;
/* 3099 */     if (offset < 0) {
/* 3100 */       offset += n;
/*      */     }
/*      */ 
/*      */     
/* 3104 */     while (n > 1 && offset > 0) {
/* 3105 */       int n_offset = n - offset;
/*      */       
/* 3107 */       if (offset > n_offset) {
/* 3108 */         swap(array, startIndexInclusive, startIndexInclusive + n - n_offset, n_offset);
/* 3109 */         n = offset;
/* 3110 */         offset -= n_offset; continue;
/* 3111 */       }  if (offset < n_offset) {
/* 3112 */         swap(array, startIndexInclusive, startIndexInclusive + n_offset, offset);
/* 3113 */         startIndexInclusive += offset;
/* 3114 */         n = n_offset; continue;
/*      */       } 
/* 3116 */       swap(array, startIndexInclusive, startIndexInclusive + n_offset, offset);
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
/*      */   public static void shift(short[] array, int startIndexInclusive, int endIndexExclusive, int offset) {
/* 3142 */     if (array == null) {
/*      */       return;
/*      */     }
/* 3145 */     if (startIndexInclusive >= array.length - 1 || endIndexExclusive <= 0) {
/*      */       return;
/*      */     }
/* 3148 */     if (startIndexInclusive < 0) {
/* 3149 */       startIndexInclusive = 0;
/*      */     }
/* 3151 */     if (endIndexExclusive >= array.length) {
/* 3152 */       endIndexExclusive = array.length;
/*      */     }
/* 3154 */     int n = endIndexExclusive - startIndexInclusive;
/* 3155 */     if (n <= 1) {
/*      */       return;
/*      */     }
/* 3158 */     offset %= n;
/* 3159 */     if (offset < 0) {
/* 3160 */       offset += n;
/*      */     }
/*      */ 
/*      */     
/* 3164 */     while (n > 1 && offset > 0) {
/* 3165 */       int n_offset = n - offset;
/*      */       
/* 3167 */       if (offset > n_offset) {
/* 3168 */         swap(array, startIndexInclusive, startIndexInclusive + n - n_offset, n_offset);
/* 3169 */         n = offset;
/* 3170 */         offset -= n_offset; continue;
/* 3171 */       }  if (offset < n_offset) {
/* 3172 */         swap(array, startIndexInclusive, startIndexInclusive + n_offset, offset);
/* 3173 */         startIndexInclusive += offset;
/* 3174 */         n = n_offset; continue;
/*      */       } 
/* 3176 */       swap(array, startIndexInclusive, startIndexInclusive + n_offset, offset);
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
/*      */   public static int indexOf(Object[] array, Object objectToFind) {
/* 3198 */     return indexOf(array, objectToFind, 0);
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
/*      */   public static int indexOf(Object[] array, Object objectToFind, int startIndex) {
/* 3216 */     if (array == null) {
/* 3217 */       return -1;
/*      */     }
/* 3219 */     if (startIndex < 0) {
/* 3220 */       startIndex = 0;
/*      */     }
/* 3222 */     if (objectToFind == null) {
/* 3223 */       for (int i = startIndex; i < array.length; i++) {
/* 3224 */         if (array[i] == null) {
/* 3225 */           return i;
/*      */         }
/*      */       } 
/*      */     } else {
/* 3229 */       for (int i = startIndex; i < array.length; i++) {
/* 3230 */         if (objectToFind.equals(array[i])) {
/* 3231 */           return i;
/*      */         }
/*      */       } 
/*      */     } 
/* 3235 */     return -1;
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
/*      */   public static int lastIndexOf(Object[] array, Object objectToFind) {
/* 3249 */     return lastIndexOf(array, objectToFind, 2147483647);
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
/*      */   public static int lastIndexOf(Object[] array, Object objectToFind, int startIndex) {
/* 3267 */     if (array == null) {
/* 3268 */       return -1;
/*      */     }
/* 3270 */     if (startIndex < 0)
/* 3271 */       return -1; 
/* 3272 */     if (startIndex >= array.length) {
/* 3273 */       startIndex = array.length - 1;
/*      */     }
/* 3275 */     if (objectToFind == null) {
/* 3276 */       for (int i = startIndex; i >= 0; i--) {
/* 3277 */         if (array[i] == null) {
/* 3278 */           return i;
/*      */         }
/*      */       } 
/* 3281 */     } else if (array.getClass().getComponentType().isInstance(objectToFind)) {
/* 3282 */       for (int i = startIndex; i >= 0; i--) {
/* 3283 */         if (objectToFind.equals(array[i])) {
/* 3284 */           return i;
/*      */         }
/*      */       } 
/*      */     } 
/* 3288 */     return -1;
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
/*      */   public static boolean contains(Object[] array, Object objectToFind) {
/* 3301 */     return (indexOf(array, objectToFind) != -1);
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
/*      */   public static int indexOf(long[] array, long valueToFind) {
/* 3317 */     return indexOf(array, valueToFind, 0);
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
/*      */   public static int indexOf(long[] array, long valueToFind, int startIndex) {
/* 3335 */     if (array == null) {
/* 3336 */       return -1;
/*      */     }
/* 3338 */     if (startIndex < 0) {
/* 3339 */       startIndex = 0;
/*      */     }
/* 3341 */     for (int i = startIndex; i < array.length; i++) {
/* 3342 */       if (valueToFind == array[i]) {
/* 3343 */         return i;
/*      */       }
/*      */     } 
/* 3346 */     return -1;
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
/*      */   public static int lastIndexOf(long[] array, long valueToFind) {
/* 3360 */     return lastIndexOf(array, valueToFind, 2147483647);
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
/*      */   public static int lastIndexOf(long[] array, long valueToFind, int startIndex) {
/* 3378 */     if (array == null) {
/* 3379 */       return -1;
/*      */     }
/* 3381 */     if (startIndex < 0)
/* 3382 */       return -1; 
/* 3383 */     if (startIndex >= array.length) {
/* 3384 */       startIndex = array.length - 1;
/*      */     }
/* 3386 */     for (int i = startIndex; i >= 0; i--) {
/* 3387 */       if (valueToFind == array[i]) {
/* 3388 */         return i;
/*      */       }
/*      */     } 
/* 3391 */     return -1;
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
/*      */   public static boolean contains(long[] array, long valueToFind) {
/* 3404 */     return (indexOf(array, valueToFind) != -1);
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
/*      */   public static int indexOf(int[] array, int valueToFind) {
/* 3420 */     return indexOf(array, valueToFind, 0);
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
/*      */   public static int indexOf(int[] array, int valueToFind, int startIndex) {
/* 3438 */     if (array == null) {
/* 3439 */       return -1;
/*      */     }
/* 3441 */     if (startIndex < 0) {
/* 3442 */       startIndex = 0;
/*      */     }
/* 3444 */     for (int i = startIndex; i < array.length; i++) {
/* 3445 */       if (valueToFind == array[i]) {
/* 3446 */         return i;
/*      */       }
/*      */     } 
/* 3449 */     return -1;
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
/*      */   public static int lastIndexOf(int[] array, int valueToFind) {
/* 3463 */     return lastIndexOf(array, valueToFind, 2147483647);
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
/*      */   public static int lastIndexOf(int[] array, int valueToFind, int startIndex) {
/* 3481 */     if (array == null) {
/* 3482 */       return -1;
/*      */     }
/* 3484 */     if (startIndex < 0)
/* 3485 */       return -1; 
/* 3486 */     if (startIndex >= array.length) {
/* 3487 */       startIndex = array.length - 1;
/*      */     }
/* 3489 */     for (int i = startIndex; i >= 0; i--) {
/* 3490 */       if (valueToFind == array[i]) {
/* 3491 */         return i;
/*      */       }
/*      */     } 
/* 3494 */     return -1;
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
/*      */   public static boolean contains(int[] array, int valueToFind) {
/* 3507 */     return (indexOf(array, valueToFind) != -1);
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
/*      */   public static int indexOf(short[] array, short valueToFind) {
/* 3523 */     return indexOf(array, valueToFind, 0);
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
/*      */   public static int indexOf(short[] array, short valueToFind, int startIndex) {
/* 3541 */     if (array == null) {
/* 3542 */       return -1;
/*      */     }
/* 3544 */     if (startIndex < 0) {
/* 3545 */       startIndex = 0;
/*      */     }
/* 3547 */     for (int i = startIndex; i < array.length; i++) {
/* 3548 */       if (valueToFind == array[i]) {
/* 3549 */         return i;
/*      */       }
/*      */     } 
/* 3552 */     return -1;
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
/*      */   public static int lastIndexOf(short[] array, short valueToFind) {
/* 3566 */     return lastIndexOf(array, valueToFind, 2147483647);
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
/*      */   public static int lastIndexOf(short[] array, short valueToFind, int startIndex) {
/* 3584 */     if (array == null) {
/* 3585 */       return -1;
/*      */     }
/* 3587 */     if (startIndex < 0)
/* 3588 */       return -1; 
/* 3589 */     if (startIndex >= array.length) {
/* 3590 */       startIndex = array.length - 1;
/*      */     }
/* 3592 */     for (int i = startIndex; i >= 0; i--) {
/* 3593 */       if (valueToFind == array[i]) {
/* 3594 */         return i;
/*      */       }
/*      */     } 
/* 3597 */     return -1;
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
/*      */   public static boolean contains(short[] array, short valueToFind) {
/* 3610 */     return (indexOf(array, valueToFind) != -1);
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
/*      */   public static int indexOf(char[] array, char valueToFind) {
/* 3627 */     return indexOf(array, valueToFind, 0);
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
/*      */   public static int indexOf(char[] array, char valueToFind, int startIndex) {
/* 3646 */     if (array == null) {
/* 3647 */       return -1;
/*      */     }
/* 3649 */     if (startIndex < 0) {
/* 3650 */       startIndex = 0;
/*      */     }
/* 3652 */     for (int i = startIndex; i < array.length; i++) {
/* 3653 */       if (valueToFind == array[i]) {
/* 3654 */         return i;
/*      */       }
/*      */     } 
/* 3657 */     return -1;
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
/*      */   public static int lastIndexOf(char[] array, char valueToFind) {
/* 3672 */     return lastIndexOf(array, valueToFind, 2147483647);
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
/*      */   public static int lastIndexOf(char[] array, char valueToFind, int startIndex) {
/* 3691 */     if (array == null) {
/* 3692 */       return -1;
/*      */     }
/* 3694 */     if (startIndex < 0)
/* 3695 */       return -1; 
/* 3696 */     if (startIndex >= array.length) {
/* 3697 */       startIndex = array.length - 1;
/*      */     }
/* 3699 */     for (int i = startIndex; i >= 0; i--) {
/* 3700 */       if (valueToFind == array[i]) {
/* 3701 */         return i;
/*      */       }
/*      */     } 
/* 3704 */     return -1;
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
/*      */   public static boolean contains(char[] array, char valueToFind) {
/* 3718 */     return (indexOf(array, valueToFind) != -1);
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
/*      */   public static int indexOf(byte[] array, byte valueToFind) {
/* 3734 */     return indexOf(array, valueToFind, 0);
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
/*      */   public static int indexOf(byte[] array, byte valueToFind, int startIndex) {
/* 3752 */     if (array == null) {
/* 3753 */       return -1;
/*      */     }
/* 3755 */     if (startIndex < 0) {
/* 3756 */       startIndex = 0;
/*      */     }
/* 3758 */     for (int i = startIndex; i < array.length; i++) {
/* 3759 */       if (valueToFind == array[i]) {
/* 3760 */         return i;
/*      */       }
/*      */     } 
/* 3763 */     return -1;
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
/*      */   public static int lastIndexOf(byte[] array, byte valueToFind) {
/* 3777 */     return lastIndexOf(array, valueToFind, 2147483647);
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
/*      */   public static int lastIndexOf(byte[] array, byte valueToFind, int startIndex) {
/* 3795 */     if (array == null) {
/* 3796 */       return -1;
/*      */     }
/* 3798 */     if (startIndex < 0)
/* 3799 */       return -1; 
/* 3800 */     if (startIndex >= array.length) {
/* 3801 */       startIndex = array.length - 1;
/*      */     }
/* 3803 */     for (int i = startIndex; i >= 0; i--) {
/* 3804 */       if (valueToFind == array[i]) {
/* 3805 */         return i;
/*      */       }
/*      */     } 
/* 3808 */     return -1;
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
/*      */   public static boolean contains(byte[] array, byte valueToFind) {
/* 3821 */     return (indexOf(array, valueToFind) != -1);
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
/*      */   public static int indexOf(double[] array, double valueToFind) {
/* 3837 */     return indexOf(array, valueToFind, 0);
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
/*      */   public static int indexOf(double[] array, double valueToFind, double tolerance) {
/* 3854 */     return indexOf(array, valueToFind, 0, tolerance);
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
/*      */   public static int indexOf(double[] array, double valueToFind, int startIndex) {
/* 3872 */     if (isEmpty(array)) {
/* 3873 */       return -1;
/*      */     }
/* 3875 */     if (startIndex < 0) {
/* 3876 */       startIndex = 0;
/*      */     }
/* 3878 */     for (int i = startIndex; i < array.length; i++) {
/* 3879 */       if (valueToFind == array[i]) {
/* 3880 */         return i;
/*      */       }
/*      */     } 
/* 3883 */     return -1;
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
/*      */   public static int indexOf(double[] array, double valueToFind, int startIndex, double tolerance) {
/* 3904 */     if (isEmpty(array)) {
/* 3905 */       return -1;
/*      */     }
/* 3907 */     if (startIndex < 0) {
/* 3908 */       startIndex = 0;
/*      */     }
/* 3910 */     double min = valueToFind - tolerance;
/* 3911 */     double max = valueToFind + tolerance;
/* 3912 */     for (int i = startIndex; i < array.length; i++) {
/* 3913 */       if (array[i] >= min && array[i] <= max) {
/* 3914 */         return i;
/*      */       }
/*      */     } 
/* 3917 */     return -1;
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
/*      */   public static int lastIndexOf(double[] array, double valueToFind) {
/* 3931 */     return lastIndexOf(array, valueToFind, 2147483647);
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
/*      */   public static int lastIndexOf(double[] array, double valueToFind, double tolerance) {
/* 3948 */     return lastIndexOf(array, valueToFind, 2147483647, tolerance);
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
/*      */   public static int lastIndexOf(double[] array, double valueToFind, int startIndex) {
/* 3966 */     if (isEmpty(array)) {
/* 3967 */       return -1;
/*      */     }
/* 3969 */     if (startIndex < 0)
/* 3970 */       return -1; 
/* 3971 */     if (startIndex >= array.length) {
/* 3972 */       startIndex = array.length - 1;
/*      */     }
/* 3974 */     for (int i = startIndex; i >= 0; i--) {
/* 3975 */       if (valueToFind == array[i]) {
/* 3976 */         return i;
/*      */       }
/*      */     } 
/* 3979 */     return -1;
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
/*      */   public static int lastIndexOf(double[] array, double valueToFind, int startIndex, double tolerance) {
/* 4000 */     if (isEmpty(array)) {
/* 4001 */       return -1;
/*      */     }
/* 4003 */     if (startIndex < 0)
/* 4004 */       return -1; 
/* 4005 */     if (startIndex >= array.length) {
/* 4006 */       startIndex = array.length - 1;
/*      */     }
/* 4008 */     double min = valueToFind - tolerance;
/* 4009 */     double max = valueToFind + tolerance;
/* 4010 */     for (int i = startIndex; i >= 0; i--) {
/* 4011 */       if (array[i] >= min && array[i] <= max) {
/* 4012 */         return i;
/*      */       }
/*      */     } 
/* 4015 */     return -1;
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
/*      */   public static boolean contains(double[] array, double valueToFind) {
/* 4028 */     return (indexOf(array, valueToFind) != -1);
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
/*      */   public static boolean contains(double[] array, double valueToFind, double tolerance) {
/* 4045 */     return (indexOf(array, valueToFind, 0, tolerance) != -1);
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
/*      */   public static int indexOf(float[] array, float valueToFind) {
/* 4061 */     return indexOf(array, valueToFind, 0);
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
/*      */   public static int indexOf(float[] array, float valueToFind, int startIndex) {
/* 4079 */     if (isEmpty(array)) {
/* 4080 */       return -1;
/*      */     }
/* 4082 */     if (startIndex < 0) {
/* 4083 */       startIndex = 0;
/*      */     }
/* 4085 */     for (int i = startIndex; i < array.length; i++) {
/* 4086 */       if (valueToFind == array[i]) {
/* 4087 */         return i;
/*      */       }
/*      */     } 
/* 4090 */     return -1;
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
/*      */   public static int lastIndexOf(float[] array, float valueToFind) {
/* 4104 */     return lastIndexOf(array, valueToFind, 2147483647);
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
/*      */   public static int lastIndexOf(float[] array, float valueToFind, int startIndex) {
/* 4122 */     if (isEmpty(array)) {
/* 4123 */       return -1;
/*      */     }
/* 4125 */     if (startIndex < 0)
/* 4126 */       return -1; 
/* 4127 */     if (startIndex >= array.length) {
/* 4128 */       startIndex = array.length - 1;
/*      */     }
/* 4130 */     for (int i = startIndex; i >= 0; i--) {
/* 4131 */       if (valueToFind == array[i]) {
/* 4132 */         return i;
/*      */       }
/*      */     } 
/* 4135 */     return -1;
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
/*      */   public static boolean contains(float[] array, float valueToFind) {
/* 4148 */     return (indexOf(array, valueToFind) != -1);
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
/*      */   public static int indexOf(boolean[] array, boolean valueToFind) {
/* 4164 */     return indexOf(array, valueToFind, 0);
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
/*      */   public static int indexOf(boolean[] array, boolean valueToFind, int startIndex) {
/* 4183 */     if (isEmpty(array)) {
/* 4184 */       return -1;
/*      */     }
/* 4186 */     if (startIndex < 0) {
/* 4187 */       startIndex = 0;
/*      */     }
/* 4189 */     for (int i = startIndex; i < array.length; i++) {
/* 4190 */       if (valueToFind == array[i]) {
/* 4191 */         return i;
/*      */       }
/*      */     } 
/* 4194 */     return -1;
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
/*      */   public static int lastIndexOf(boolean[] array, boolean valueToFind) {
/* 4209 */     return lastIndexOf(array, valueToFind, 2147483647);
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
/*      */   public static int lastIndexOf(boolean[] array, boolean valueToFind, int startIndex) {
/* 4227 */     if (isEmpty(array)) {
/* 4228 */       return -1;
/*      */     }
/* 4230 */     if (startIndex < 0)
/* 4231 */       return -1; 
/* 4232 */     if (startIndex >= array.length) {
/* 4233 */       startIndex = array.length - 1;
/*      */     }
/* 4235 */     for (int i = startIndex; i >= 0; i--) {
/* 4236 */       if (valueToFind == array[i]) {
/* 4237 */         return i;
/*      */       }
/*      */     } 
/* 4240 */     return -1;
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
/*      */   public static boolean contains(boolean[] array, boolean valueToFind) {
/* 4253 */     return (indexOf(array, valueToFind) != -1);
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
/*      */   public static char[] toPrimitive(Character[] array) {
/* 4271 */     if (array == null)
/* 4272 */       return null; 
/* 4273 */     if (array.length == 0) {
/* 4274 */       return EMPTY_CHAR_ARRAY;
/*      */     }
/* 4276 */     char[] result = new char[array.length];
/* 4277 */     for (int i = 0; i < array.length; i++) {
/* 4278 */       result[i] = array[i].charValue();
/*      */     }
/* 4280 */     return result;
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
/*      */   public static char[] toPrimitive(Character[] array, char valueForNull) {
/* 4293 */     if (array == null)
/* 4294 */       return null; 
/* 4295 */     if (array.length == 0) {
/* 4296 */       return EMPTY_CHAR_ARRAY;
/*      */     }
/* 4298 */     char[] result = new char[array.length];
/* 4299 */     for (int i = 0; i < array.length; i++) {
/* 4300 */       Character b = array[i];
/* 4301 */       result[i] = (b == null) ? valueForNull : b.charValue();
/*      */     } 
/* 4303 */     return result;
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
/*      */   public static Character[] toObject(char[] array) {
/* 4315 */     if (array == null)
/* 4316 */       return null; 
/* 4317 */     if (array.length == 0) {
/* 4318 */       return EMPTY_CHARACTER_OBJECT_ARRAY;
/*      */     }
/* 4320 */     Character[] result = new Character[array.length];
/* 4321 */     for (int i = 0; i < array.length; i++) {
/* 4322 */       result[i] = Character.valueOf(array[i]);
/*      */     }
/* 4324 */     return result;
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
/*      */   public static long[] toPrimitive(Long[] array) {
/* 4339 */     if (array == null)
/* 4340 */       return null; 
/* 4341 */     if (array.length == 0) {
/* 4342 */       return EMPTY_LONG_ARRAY;
/*      */     }
/* 4344 */     long[] result = new long[array.length];
/* 4345 */     for (int i = 0; i < array.length; i++) {
/* 4346 */       result[i] = array[i].longValue();
/*      */     }
/* 4348 */     return result;
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
/*      */   public static long[] toPrimitive(Long[] array, long valueForNull) {
/* 4361 */     if (array == null)
/* 4362 */       return null; 
/* 4363 */     if (array.length == 0) {
/* 4364 */       return EMPTY_LONG_ARRAY;
/*      */     }
/* 4366 */     long[] result = new long[array.length];
/* 4367 */     for (int i = 0; i < array.length; i++) {
/* 4368 */       Long b = array[i];
/* 4369 */       result[i] = (b == null) ? valueForNull : b.longValue();
/*      */     } 
/* 4371 */     return result;
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
/*      */   public static Long[] toObject(long[] array) {
/* 4383 */     if (array == null)
/* 4384 */       return null; 
/* 4385 */     if (array.length == 0) {
/* 4386 */       return EMPTY_LONG_OBJECT_ARRAY;
/*      */     }
/* 4388 */     Long[] result = new Long[array.length];
/* 4389 */     for (int i = 0; i < array.length; i++) {
/* 4390 */       result[i] = Long.valueOf(array[i]);
/*      */     }
/* 4392 */     return result;
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
/*      */   public static int[] toPrimitive(Integer[] array) {
/* 4407 */     if (array == null)
/* 4408 */       return null; 
/* 4409 */     if (array.length == 0) {
/* 4410 */       return EMPTY_INT_ARRAY;
/*      */     }
/* 4412 */     int[] result = new int[array.length];
/* 4413 */     for (int i = 0; i < array.length; i++) {
/* 4414 */       result[i] = array[i].intValue();
/*      */     }
/* 4416 */     return result;
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
/*      */   public static int[] toPrimitive(Integer[] array, int valueForNull) {
/* 4429 */     if (array == null)
/* 4430 */       return null; 
/* 4431 */     if (array.length == 0) {
/* 4432 */       return EMPTY_INT_ARRAY;
/*      */     }
/* 4434 */     int[] result = new int[array.length];
/* 4435 */     for (int i = 0; i < array.length; i++) {
/* 4436 */       Integer b = array[i];
/* 4437 */       result[i] = (b == null) ? valueForNull : b.intValue();
/*      */     } 
/* 4439 */     return result;
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
/*      */   public static Integer[] toObject(int[] array) {
/* 4451 */     if (array == null)
/* 4452 */       return null; 
/* 4453 */     if (array.length == 0) {
/* 4454 */       return EMPTY_INTEGER_OBJECT_ARRAY;
/*      */     }
/* 4456 */     Integer[] result = new Integer[array.length];
/* 4457 */     for (int i = 0; i < array.length; i++) {
/* 4458 */       result[i] = Integer.valueOf(array[i]);
/*      */     }
/* 4460 */     return result;
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
/*      */   public static short[] toPrimitive(Short[] array) {
/* 4475 */     if (array == null)
/* 4476 */       return null; 
/* 4477 */     if (array.length == 0) {
/* 4478 */       return EMPTY_SHORT_ARRAY;
/*      */     }
/* 4480 */     short[] result = new short[array.length];
/* 4481 */     for (int i = 0; i < array.length; i++) {
/* 4482 */       result[i] = array[i].shortValue();
/*      */     }
/* 4484 */     return result;
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
/*      */   public static short[] toPrimitive(Short[] array, short valueForNull) {
/* 4497 */     if (array == null)
/* 4498 */       return null; 
/* 4499 */     if (array.length == 0) {
/* 4500 */       return EMPTY_SHORT_ARRAY;
/*      */     }
/* 4502 */     short[] result = new short[array.length];
/* 4503 */     for (int i = 0; i < array.length; i++) {
/* 4504 */       Short b = array[i];
/* 4505 */       result[i] = (b == null) ? valueForNull : b.shortValue();
/*      */     } 
/* 4507 */     return result;
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
/*      */   public static Short[] toObject(short[] array) {
/* 4519 */     if (array == null)
/* 4520 */       return null; 
/* 4521 */     if (array.length == 0) {
/* 4522 */       return EMPTY_SHORT_OBJECT_ARRAY;
/*      */     }
/* 4524 */     Short[] result = new Short[array.length];
/* 4525 */     for (int i = 0; i < array.length; i++) {
/* 4526 */       result[i] = Short.valueOf(array[i]);
/*      */     }
/* 4528 */     return result;
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
/*      */   public static byte[] toPrimitive(Byte[] array) {
/* 4543 */     if (array == null)
/* 4544 */       return null; 
/* 4545 */     if (array.length == 0) {
/* 4546 */       return EMPTY_BYTE_ARRAY;
/*      */     }
/* 4548 */     byte[] result = new byte[array.length];
/* 4549 */     for (int i = 0; i < array.length; i++) {
/* 4550 */       result[i] = array[i].byteValue();
/*      */     }
/* 4552 */     return result;
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
/*      */   public static byte[] toPrimitive(Byte[] array, byte valueForNull) {
/* 4565 */     if (array == null)
/* 4566 */       return null; 
/* 4567 */     if (array.length == 0) {
/* 4568 */       return EMPTY_BYTE_ARRAY;
/*      */     }
/* 4570 */     byte[] result = new byte[array.length];
/* 4571 */     for (int i = 0; i < array.length; i++) {
/* 4572 */       Byte b = array[i];
/* 4573 */       result[i] = (b == null) ? valueForNull : b.byteValue();
/*      */     } 
/* 4575 */     return result;
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
/*      */   public static Byte[] toObject(byte[] array) {
/* 4587 */     if (array == null)
/* 4588 */       return null; 
/* 4589 */     if (array.length == 0) {
/* 4590 */       return EMPTY_BYTE_OBJECT_ARRAY;
/*      */     }
/* 4592 */     Byte[] result = new Byte[array.length];
/* 4593 */     for (int i = 0; i < array.length; i++) {
/* 4594 */       result[i] = Byte.valueOf(array[i]);
/*      */     }
/* 4596 */     return result;
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
/*      */   public static double[] toPrimitive(Double[] array) {
/* 4611 */     if (array == null)
/* 4612 */       return null; 
/* 4613 */     if (array.length == 0) {
/* 4614 */       return EMPTY_DOUBLE_ARRAY;
/*      */     }
/* 4616 */     double[] result = new double[array.length];
/* 4617 */     for (int i = 0; i < array.length; i++) {
/* 4618 */       result[i] = array[i].doubleValue();
/*      */     }
/* 4620 */     return result;
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
/*      */   public static double[] toPrimitive(Double[] array, double valueForNull) {
/* 4633 */     if (array == null)
/* 4634 */       return null; 
/* 4635 */     if (array.length == 0) {
/* 4636 */       return EMPTY_DOUBLE_ARRAY;
/*      */     }
/* 4638 */     double[] result = new double[array.length];
/* 4639 */     for (int i = 0; i < array.length; i++) {
/* 4640 */       Double b = array[i];
/* 4641 */       result[i] = (b == null) ? valueForNull : b.doubleValue();
/*      */     } 
/* 4643 */     return result;
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
/*      */   public static Double[] toObject(double[] array) {
/* 4655 */     if (array == null)
/* 4656 */       return null; 
/* 4657 */     if (array.length == 0) {
/* 4658 */       return EMPTY_DOUBLE_OBJECT_ARRAY;
/*      */     }
/* 4660 */     Double[] result = new Double[array.length];
/* 4661 */     for (int i = 0; i < array.length; i++) {
/* 4662 */       result[i] = Double.valueOf(array[i]);
/*      */     }
/* 4664 */     return result;
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
/*      */   public static float[] toPrimitive(Float[] array) {
/* 4679 */     if (array == null)
/* 4680 */       return null; 
/* 4681 */     if (array.length == 0) {
/* 4682 */       return EMPTY_FLOAT_ARRAY;
/*      */     }
/* 4684 */     float[] result = new float[array.length];
/* 4685 */     for (int i = 0; i < array.length; i++) {
/* 4686 */       result[i] = array[i].floatValue();
/*      */     }
/* 4688 */     return result;
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
/*      */   public static float[] toPrimitive(Float[] array, float valueForNull) {
/* 4701 */     if (array == null)
/* 4702 */       return null; 
/* 4703 */     if (array.length == 0) {
/* 4704 */       return EMPTY_FLOAT_ARRAY;
/*      */     }
/* 4706 */     float[] result = new float[array.length];
/* 4707 */     for (int i = 0; i < array.length; i++) {
/* 4708 */       Float b = array[i];
/* 4709 */       result[i] = (b == null) ? valueForNull : b.floatValue();
/*      */     } 
/* 4711 */     return result;
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
/*      */   public static Float[] toObject(float[] array) {
/* 4723 */     if (array == null)
/* 4724 */       return null; 
/* 4725 */     if (array.length == 0) {
/* 4726 */       return EMPTY_FLOAT_OBJECT_ARRAY;
/*      */     }
/* 4728 */     Float[] result = new Float[array.length];
/* 4729 */     for (int i = 0; i < array.length; i++) {
/* 4730 */       result[i] = Float.valueOf(array[i]);
/*      */     }
/* 4732 */     return result;
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
/*      */   public static Object toPrimitive(Object array) {
/* 4745 */     if (array == null) {
/* 4746 */       return null;
/*      */     }
/* 4748 */     Class<?> ct = array.getClass().getComponentType();
/* 4749 */     Class<?> pt = ClassUtils.wrapperToPrimitive(ct);
/* 4750 */     if (int.class.equals(pt)) {
/* 4751 */       return toPrimitive((Integer[])array);
/*      */     }
/* 4753 */     if (long.class.equals(pt)) {
/* 4754 */       return toPrimitive((Long[])array);
/*      */     }
/* 4756 */     if (short.class.equals(pt)) {
/* 4757 */       return toPrimitive((Short[])array);
/*      */     }
/* 4759 */     if (double.class.equals(pt)) {
/* 4760 */       return toPrimitive((Double[])array);
/*      */     }
/* 4762 */     if (float.class.equals(pt)) {
/* 4763 */       return toPrimitive((Float[])array);
/*      */     }
/* 4765 */     return array;
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
/*      */   public static boolean[] toPrimitive(Boolean[] array) {
/* 4780 */     if (array == null)
/* 4781 */       return null; 
/* 4782 */     if (array.length == 0) {
/* 4783 */       return EMPTY_BOOLEAN_ARRAY;
/*      */     }
/* 4785 */     boolean[] result = new boolean[array.length];
/* 4786 */     for (int i = 0; i < array.length; i++) {
/* 4787 */       result[i] = array[i].booleanValue();
/*      */     }
/* 4789 */     return result;
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
/*      */   public static boolean[] toPrimitive(Boolean[] array, boolean valueForNull) {
/* 4802 */     if (array == null)
/* 4803 */       return null; 
/* 4804 */     if (array.length == 0) {
/* 4805 */       return EMPTY_BOOLEAN_ARRAY;
/*      */     }
/* 4807 */     boolean[] result = new boolean[array.length];
/* 4808 */     for (int i = 0; i < array.length; i++) {
/* 4809 */       Boolean b = array[i];
/* 4810 */       result[i] = (b == null) ? valueForNull : b.booleanValue();
/*      */     } 
/* 4812 */     return result;
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
/*      */   public static Boolean[] toObject(boolean[] array) {
/* 4824 */     if (array == null)
/* 4825 */       return null; 
/* 4826 */     if (array.length == 0) {
/* 4827 */       return EMPTY_BOOLEAN_OBJECT_ARRAY;
/*      */     }
/* 4829 */     Boolean[] result = new Boolean[array.length];
/* 4830 */     for (int i = 0; i < array.length; i++) {
/* 4831 */       result[i] = array[i] ? Boolean.TRUE : Boolean.FALSE;
/*      */     }
/* 4833 */     return result;
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
/*      */   public static boolean isEmpty(Object[] array) {
/* 4845 */     return (getLength(array) == 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isEmpty(long[] array) {
/* 4856 */     return (getLength(array) == 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isEmpty(int[] array) {
/* 4867 */     return (getLength(array) == 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isEmpty(short[] array) {
/* 4878 */     return (getLength(array) == 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isEmpty(char[] array) {
/* 4889 */     return (getLength(array) == 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isEmpty(byte[] array) {
/* 4900 */     return (getLength(array) == 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isEmpty(double[] array) {
/* 4911 */     return (getLength(array) == 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isEmpty(float[] array) {
/* 4922 */     return (getLength(array) == 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isEmpty(boolean[] array) {
/* 4933 */     return (getLength(array) == 0);
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
/*      */   public static <T> boolean isNotEmpty(T[] array) {
/* 4946 */     return !isEmpty((Object[])array);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isNotEmpty(long[] array) {
/* 4957 */     return !isEmpty(array);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isNotEmpty(int[] array) {
/* 4968 */     return !isEmpty(array);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isNotEmpty(short[] array) {
/* 4979 */     return !isEmpty(array);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isNotEmpty(char[] array) {
/* 4990 */     return !isEmpty(array);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isNotEmpty(byte[] array) {
/* 5001 */     return !isEmpty(array);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isNotEmpty(double[] array) {
/* 5012 */     return !isEmpty(array);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isNotEmpty(float[] array) {
/* 5023 */     return !isEmpty(array);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isNotEmpty(boolean[] array) {
/* 5034 */     return !isEmpty(array);
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
/*      */   public static <T> T[] addAll(T[] array1, T... array2) {
/* 5062 */     if (array1 == null)
/* 5063 */       return clone(array2); 
/* 5064 */     if (array2 == null) {
/* 5065 */       return clone(array1);
/*      */     }
/* 5067 */     Class<?> type1 = array1.getClass().getComponentType();
/*      */ 
/*      */     
/* 5070 */     T[] joinedArray = (T[])Array.newInstance(type1, array1.length + array2.length);
/* 5071 */     System.arraycopy(array1, 0, joinedArray, 0, array1.length);
/*      */     try {
/* 5073 */       System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
/* 5074 */     } catch (ArrayStoreException ase) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 5081 */       Class<?> type2 = array2.getClass().getComponentType();
/* 5082 */       if (!type1.isAssignableFrom(type2)) {
/* 5083 */         throw new IllegalArgumentException("Cannot store " + type2.getName() + " in an array of " + type1
/* 5084 */             .getName(), ase);
/*      */       }
/* 5086 */       throw ase;
/*      */     } 
/* 5088 */     return joinedArray;
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
/*      */   public static boolean[] addAll(boolean[] array1, boolean... array2) {
/* 5109 */     if (array1 == null)
/* 5110 */       return clone(array2); 
/* 5111 */     if (array2 == null) {
/* 5112 */       return clone(array1);
/*      */     }
/* 5114 */     boolean[] joinedArray = new boolean[array1.length + array2.length];
/* 5115 */     System.arraycopy(array1, 0, joinedArray, 0, array1.length);
/* 5116 */     System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
/* 5117 */     return joinedArray;
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
/*      */   public static char[] addAll(char[] array1, char... array2) {
/* 5138 */     if (array1 == null)
/* 5139 */       return clone(array2); 
/* 5140 */     if (array2 == null) {
/* 5141 */       return clone(array1);
/*      */     }
/* 5143 */     char[] joinedArray = new char[array1.length + array2.length];
/* 5144 */     System.arraycopy(array1, 0, joinedArray, 0, array1.length);
/* 5145 */     System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
/* 5146 */     return joinedArray;
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
/*      */   public static byte[] addAll(byte[] array1, byte... array2) {
/* 5167 */     if (array1 == null)
/* 5168 */       return clone(array2); 
/* 5169 */     if (array2 == null) {
/* 5170 */       return clone(array1);
/*      */     }
/* 5172 */     byte[] joinedArray = new byte[array1.length + array2.length];
/* 5173 */     System.arraycopy(array1, 0, joinedArray, 0, array1.length);
/* 5174 */     System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
/* 5175 */     return joinedArray;
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
/*      */   public static short[] addAll(short[] array1, short... array2) {
/* 5196 */     if (array1 == null)
/* 5197 */       return clone(array2); 
/* 5198 */     if (array2 == null) {
/* 5199 */       return clone(array1);
/*      */     }
/* 5201 */     short[] joinedArray = new short[array1.length + array2.length];
/* 5202 */     System.arraycopy(array1, 0, joinedArray, 0, array1.length);
/* 5203 */     System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
/* 5204 */     return joinedArray;
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
/*      */   public static int[] addAll(int[] array1, int... array2) {
/* 5225 */     if (array1 == null)
/* 5226 */       return clone(array2); 
/* 5227 */     if (array2 == null) {
/* 5228 */       return clone(array1);
/*      */     }
/* 5230 */     int[] joinedArray = new int[array1.length + array2.length];
/* 5231 */     System.arraycopy(array1, 0, joinedArray, 0, array1.length);
/* 5232 */     System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
/* 5233 */     return joinedArray;
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
/*      */   public static long[] addAll(long[] array1, long... array2) {
/* 5254 */     if (array1 == null)
/* 5255 */       return clone(array2); 
/* 5256 */     if (array2 == null) {
/* 5257 */       return clone(array1);
/*      */     }
/* 5259 */     long[] joinedArray = new long[array1.length + array2.length];
/* 5260 */     System.arraycopy(array1, 0, joinedArray, 0, array1.length);
/* 5261 */     System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
/* 5262 */     return joinedArray;
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
/*      */   public static float[] addAll(float[] array1, float... array2) {
/* 5283 */     if (array1 == null)
/* 5284 */       return clone(array2); 
/* 5285 */     if (array2 == null) {
/* 5286 */       return clone(array1);
/*      */     }
/* 5288 */     float[] joinedArray = new float[array1.length + array2.length];
/* 5289 */     System.arraycopy(array1, 0, joinedArray, 0, array1.length);
/* 5290 */     System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
/* 5291 */     return joinedArray;
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
/*      */   public static double[] addAll(double[] array1, double... array2) {
/* 5312 */     if (array1 == null)
/* 5313 */       return clone(array2); 
/* 5314 */     if (array2 == null) {
/* 5315 */       return clone(array1);
/*      */     }
/* 5317 */     double[] joinedArray = new double[array1.length + array2.length];
/* 5318 */     System.arraycopy(array1, 0, joinedArray, 0, array1.length);
/* 5319 */     System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
/* 5320 */     return joinedArray;
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
/*      */   public static <T> T[] add(T[] array, T element) {
/*      */     Class<?> type;
/* 5354 */     if (array != null) {
/* 5355 */       type = array.getClass().getComponentType();
/* 5356 */     } else if (element != null) {
/* 5357 */       type = element.getClass();
/*      */     } else {
/* 5359 */       throw new IllegalArgumentException("Arguments cannot both be null");
/*      */     } 
/*      */ 
/*      */     
/* 5363 */     T[] newArray = (T[])copyArrayGrow1(array, type);
/* 5364 */     newArray[newArray.length - 1] = element;
/* 5365 */     return newArray;
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
/*      */   public static boolean[] add(boolean[] array, boolean element) {
/* 5390 */     boolean[] newArray = (boolean[])copyArrayGrow1(array, boolean.class);
/* 5391 */     newArray[newArray.length - 1] = element;
/* 5392 */     return newArray;
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
/*      */   public static byte[] add(byte[] array, byte element) {
/* 5417 */     byte[] newArray = (byte[])copyArrayGrow1(array, byte.class);
/* 5418 */     newArray[newArray.length - 1] = element;
/* 5419 */     return newArray;
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
/*      */   public static char[] add(char[] array, char element) {
/* 5444 */     char[] newArray = (char[])copyArrayGrow1(array, char.class);
/* 5445 */     newArray[newArray.length - 1] = element;
/* 5446 */     return newArray;
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
/*      */   public static double[] add(double[] array, double element) {
/* 5471 */     double[] newArray = (double[])copyArrayGrow1(array, double.class);
/* 5472 */     newArray[newArray.length - 1] = element;
/* 5473 */     return newArray;
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
/*      */   public static float[] add(float[] array, float element) {
/* 5498 */     float[] newArray = (float[])copyArrayGrow1(array, float.class);
/* 5499 */     newArray[newArray.length - 1] = element;
/* 5500 */     return newArray;
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
/*      */   public static int[] add(int[] array, int element) {
/* 5525 */     int[] newArray = (int[])copyArrayGrow1(array, int.class);
/* 5526 */     newArray[newArray.length - 1] = element;
/* 5527 */     return newArray;
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
/*      */   public static long[] add(long[] array, long element) {
/* 5552 */     long[] newArray = (long[])copyArrayGrow1(array, long.class);
/* 5553 */     newArray[newArray.length - 1] = element;
/* 5554 */     return newArray;
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
/*      */   public static short[] add(short[] array, short element) {
/* 5579 */     short[] newArray = (short[])copyArrayGrow1(array, short.class);
/* 5580 */     newArray[newArray.length - 1] = element;
/* 5581 */     return newArray;
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
/*      */   private static Object copyArrayGrow1(Object array, Class<?> newArrayComponentType) {
/* 5594 */     if (array != null) {
/* 5595 */       int arrayLength = Array.getLength(array);
/* 5596 */       Object newArray = Array.newInstance(array.getClass().getComponentType(), arrayLength + 1);
/* 5597 */       System.arraycopy(array, 0, newArray, 0, arrayLength);
/* 5598 */       return newArray;
/*      */     } 
/* 5600 */     return Array.newInstance(newArrayComponentType, 1);
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
/*      */   public static <T> T[] add(T[] array, int index, T element) {
/* 5633 */     Class<?> clss = null;
/* 5634 */     if (array != null) {
/* 5635 */       clss = array.getClass().getComponentType();
/* 5636 */     } else if (element != null) {
/* 5637 */       clss = element.getClass();
/*      */     } else {
/* 5639 */       throw new IllegalArgumentException("Array and element cannot both be null");
/*      */     } 
/*      */     
/* 5642 */     T[] newArray = (T[])add(array, index, element, clss);
/* 5643 */     return newArray;
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
/*      */   public static boolean[] add(boolean[] array, int index, boolean element) {
/* 5673 */     return (boolean[])add(array, index, Boolean.valueOf(element), boolean.class);
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
/*      */   public static char[] add(char[] array, int index, char element) {
/* 5705 */     return (char[])add(array, index, Character.valueOf(element), char.class);
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
/*      */   public static byte[] add(byte[] array, int index, byte element) {
/* 5736 */     return (byte[])add(array, index, Byte.valueOf(element), byte.class);
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
/*      */   public static short[] add(short[] array, int index, short element) {
/* 5767 */     return (short[])add(array, index, Short.valueOf(element), short.class);
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
/*      */   public static int[] add(int[] array, int index, int element) {
/* 5798 */     return (int[])add(array, index, Integer.valueOf(element), int.class);
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
/*      */   public static long[] add(long[] array, int index, long element) {
/* 5829 */     return (long[])add(array, index, Long.valueOf(element), long.class);
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
/*      */   public static float[] add(float[] array, int index, float element) {
/* 5860 */     return (float[])add(array, index, Float.valueOf(element), float.class);
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
/*      */   public static double[] add(double[] array, int index, double element) {
/* 5891 */     return (double[])add(array, index, Double.valueOf(element), double.class);
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
/*      */   private static Object add(Object array, int index, Object element, Class<?> clss) {
/* 5906 */     if (array == null) {
/* 5907 */       if (index != 0) {
/* 5908 */         throw new IndexOutOfBoundsException("Index: " + index + ", Length: 0");
/*      */       }
/* 5910 */       Object joinedArray = Array.newInstance(clss, 1);
/* 5911 */       Array.set(joinedArray, 0, element);
/* 5912 */       return joinedArray;
/*      */     } 
/* 5914 */     int length = Array.getLength(array);
/* 5915 */     if (index > length || index < 0) {
/* 5916 */       throw new IndexOutOfBoundsException("Index: " + index + ", Length: " + length);
/*      */     }
/* 5918 */     Object result = Array.newInstance(clss, length + 1);
/* 5919 */     System.arraycopy(array, 0, result, 0, index);
/* 5920 */     Array.set(result, index, element);
/* 5921 */     if (index < length) {
/* 5922 */       System.arraycopy(array, index, result, index + 1, length - index);
/*      */     }
/* 5924 */     return result;
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
/*      */   public static <T> T[] remove(T[] array, int index) {
/* 5958 */     return (T[])remove(array, index);
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
/*      */   public static <T> T[] removeElement(T[] array, Object element) {
/* 5988 */     int index = indexOf((Object[])array, element);
/* 5989 */     if (index == -1) {
/* 5990 */       return clone(array);
/*      */     }
/* 5992 */     return remove(array, index);
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
/*      */   public static boolean[] remove(boolean[] array, int index) {
/* 6024 */     return (boolean[])remove(array, index);
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
/*      */   public static boolean[] removeElement(boolean[] array, boolean element) {
/* 6053 */     int index = indexOf(array, element);
/* 6054 */     if (index == -1) {
/* 6055 */       return clone(array);
/*      */     }
/* 6057 */     return remove(array, index);
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
/*      */   public static byte[] remove(byte[] array, int index) {
/* 6089 */     return (byte[])remove(array, index);
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
/*      */   public static byte[] removeElement(byte[] array, byte element) {
/* 6118 */     int index = indexOf(array, element);
/* 6119 */     if (index == -1) {
/* 6120 */       return clone(array);
/*      */     }
/* 6122 */     return remove(array, index);
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
/*      */   public static char[] remove(char[] array, int index) {
/* 6154 */     return (char[])remove(array, index);
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
/*      */   public static char[] removeElement(char[] array, char element) {
/* 6183 */     int index = indexOf(array, element);
/* 6184 */     if (index == -1) {
/* 6185 */       return clone(array);
/*      */     }
/* 6187 */     return remove(array, index);
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
/*      */   public static double[] remove(double[] array, int index) {
/* 6219 */     return (double[])remove(array, index);
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
/*      */   public static double[] removeElement(double[] array, double element) {
/* 6248 */     int index = indexOf(array, element);
/* 6249 */     if (index == -1) {
/* 6250 */       return clone(array);
/*      */     }
/* 6252 */     return remove(array, index);
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
/*      */   public static float[] remove(float[] array, int index) {
/* 6284 */     return (float[])remove(array, index);
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
/*      */   public static float[] removeElement(float[] array, float element) {
/* 6313 */     int index = indexOf(array, element);
/* 6314 */     if (index == -1) {
/* 6315 */       return clone(array);
/*      */     }
/* 6317 */     return remove(array, index);
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
/*      */   public static int[] remove(int[] array, int index) {
/* 6349 */     return (int[])remove(array, index);
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
/*      */   public static int[] removeElement(int[] array, int element) {
/* 6378 */     int index = indexOf(array, element);
/* 6379 */     if (index == -1) {
/* 6380 */       return clone(array);
/*      */     }
/* 6382 */     return remove(array, index);
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
/*      */   public static long[] remove(long[] array, int index) {
/* 6414 */     return (long[])remove(array, index);
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
/*      */   public static long[] removeElement(long[] array, long element) {
/* 6443 */     int index = indexOf(array, element);
/* 6444 */     if (index == -1) {
/* 6445 */       return clone(array);
/*      */     }
/* 6447 */     return remove(array, index);
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
/*      */   public static short[] remove(short[] array, int index) {
/* 6479 */     return (short[])remove(array, index);
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
/*      */   public static short[] removeElement(short[] array, short element) {
/* 6508 */     int index = indexOf(array, element);
/* 6509 */     if (index == -1) {
/* 6510 */       return clone(array);
/*      */     }
/* 6512 */     return remove(array, index);
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
/*      */   private static Object remove(Object array, int index) {
/* 6537 */     int length = getLength(array);
/* 6538 */     if (index < 0 || index >= length) {
/* 6539 */       throw new IndexOutOfBoundsException("Index: " + index + ", Length: " + length);
/*      */     }
/*      */     
/* 6542 */     Object result = Array.newInstance(array.getClass().getComponentType(), length - 1);
/* 6543 */     System.arraycopy(array, 0, result, 0, index);
/* 6544 */     if (index < length - 1) {
/* 6545 */       System.arraycopy(array, index + 1, result, index, length - index - 1);
/*      */     }
/*      */     
/* 6548 */     return result;
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
/*      */   public static <T> T[] removeAll(T[] array, int... indices) {
/* 6579 */     return (T[])removeAll(array, indices);
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
/*      */   public static <T> T[] removeElements(T[] array, T... values) {
/* 6611 */     if (isEmpty((Object[])array) || isEmpty((Object[])values)) {
/* 6612 */       return clone(array);
/*      */     }
/* 6614 */     HashMap<T, MutableInt> occurrences = new HashMap<T, MutableInt>(values.length);
/* 6615 */     for (T v : values) {
/* 6616 */       MutableInt count = occurrences.get(v);
/* 6617 */       if (count == null) {
/* 6618 */         occurrences.put(v, new MutableInt(1));
/*      */       } else {
/* 6620 */         count.increment();
/*      */       } 
/*      */     } 
/* 6623 */     BitSet toRemove = new BitSet();
/* 6624 */     for (int i = 0; i < array.length; i++) {
/* 6625 */       T key = array[i];
/* 6626 */       MutableInt count = occurrences.get(key);
/* 6627 */       if (count != null) {
/* 6628 */         if (count.decrementAndGet() == 0) {
/* 6629 */           occurrences.remove(key);
/*      */         }
/* 6631 */         toRemove.set(i);
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/* 6636 */     T[] result = (T[])removeAll(array, toRemove);
/* 6637 */     return result;
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
/*      */   public static byte[] removeAll(byte[] array, int... indices) {
/* 6670 */     return (byte[])removeAll(array, indices);
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
/*      */   public static byte[] removeElements(byte[] array, byte... values) {
/* 6701 */     if (isEmpty(array) || isEmpty(values)) {
/* 6702 */       return clone(array);
/*      */     }
/* 6704 */     Map<Byte, MutableInt> occurrences = new HashMap<Byte, MutableInt>(values.length);
/* 6705 */     for (byte v : values) {
/* 6706 */       Byte boxed = Byte.valueOf(v);
/* 6707 */       MutableInt count = occurrences.get(boxed);
/* 6708 */       if (count == null) {
/* 6709 */         occurrences.put(boxed, new MutableInt(1));
/*      */       } else {
/* 6711 */         count.increment();
/*      */       } 
/*      */     } 
/* 6714 */     BitSet toRemove = new BitSet();
/* 6715 */     for (int i = 0; i < array.length; i++) {
/* 6716 */       byte key = array[i];
/* 6717 */       MutableInt count = occurrences.get(Byte.valueOf(key));
/* 6718 */       if (count != null) {
/* 6719 */         if (count.decrementAndGet() == 0) {
/* 6720 */           occurrences.remove(Byte.valueOf(key));
/*      */         }
/* 6722 */         toRemove.set(i);
/*      */       } 
/*      */     } 
/* 6725 */     return (byte[])removeAll(array, toRemove);
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
/*      */   public static short[] removeAll(short[] array, int... indices) {
/* 6758 */     return (short[])removeAll(array, indices);
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
/*      */   public static short[] removeElements(short[] array, short... values) {
/* 6789 */     if (isEmpty(array) || isEmpty(values)) {
/* 6790 */       return clone(array);
/*      */     }
/* 6792 */     HashMap<Short, MutableInt> occurrences = new HashMap<Short, MutableInt>(values.length);
/* 6793 */     for (short v : values) {
/* 6794 */       Short boxed = Short.valueOf(v);
/* 6795 */       MutableInt count = occurrences.get(boxed);
/* 6796 */       if (count == null) {
/* 6797 */         occurrences.put(boxed, new MutableInt(1));
/*      */       } else {
/* 6799 */         count.increment();
/*      */       } 
/*      */     } 
/* 6802 */     BitSet toRemove = new BitSet();
/* 6803 */     for (int i = 0; i < array.length; i++) {
/* 6804 */       short key = array[i];
/* 6805 */       MutableInt count = occurrences.get(Short.valueOf(key));
/* 6806 */       if (count != null) {
/* 6807 */         if (count.decrementAndGet() == 0) {
/* 6808 */           occurrences.remove(Short.valueOf(key));
/*      */         }
/* 6810 */         toRemove.set(i);
/*      */       } 
/*      */     } 
/* 6813 */     return (short[])removeAll(array, toRemove);
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
/*      */   public static int[] removeAll(int[] array, int... indices) {
/* 6846 */     return (int[])removeAll(array, indices);
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
/*      */   public static int[] removeElements(int[] array, int... values) {
/* 6877 */     if (isEmpty(array) || isEmpty(values)) {
/* 6878 */       return clone(array);
/*      */     }
/* 6880 */     HashMap<Integer, MutableInt> occurrences = new HashMap<Integer, MutableInt>(values.length);
/* 6881 */     for (int v : values) {
/* 6882 */       Integer boxed = Integer.valueOf(v);
/* 6883 */       MutableInt count = occurrences.get(boxed);
/* 6884 */       if (count == null) {
/* 6885 */         occurrences.put(boxed, new MutableInt(1));
/*      */       } else {
/* 6887 */         count.increment();
/*      */       } 
/*      */     } 
/* 6890 */     BitSet toRemove = new BitSet();
/* 6891 */     for (int i = 0; i < array.length; i++) {
/* 6892 */       int key = array[i];
/* 6893 */       MutableInt count = occurrences.get(Integer.valueOf(key));
/* 6894 */       if (count != null) {
/* 6895 */         if (count.decrementAndGet() == 0) {
/* 6896 */           occurrences.remove(Integer.valueOf(key));
/*      */         }
/* 6898 */         toRemove.set(i);
/*      */       } 
/*      */     } 
/* 6901 */     return (int[])removeAll(array, toRemove);
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
/*      */   public static char[] removeAll(char[] array, int... indices) {
/* 6934 */     return (char[])removeAll(array, indices);
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
/*      */   public static char[] removeElements(char[] array, char... values) {
/* 6965 */     if (isEmpty(array) || isEmpty(values)) {
/* 6966 */       return clone(array);
/*      */     }
/* 6968 */     HashMap<Character, MutableInt> occurrences = new HashMap<Character, MutableInt>(values.length);
/* 6969 */     for (char v : values) {
/* 6970 */       Character boxed = Character.valueOf(v);
/* 6971 */       MutableInt count = occurrences.get(boxed);
/* 6972 */       if (count == null) {
/* 6973 */         occurrences.put(boxed, new MutableInt(1));
/*      */       } else {
/* 6975 */         count.increment();
/*      */       } 
/*      */     } 
/* 6978 */     BitSet toRemove = new BitSet();
/* 6979 */     for (int i = 0; i < array.length; i++) {
/* 6980 */       char key = array[i];
/* 6981 */       MutableInt count = occurrences.get(Character.valueOf(key));
/* 6982 */       if (count != null) {
/* 6983 */         if (count.decrementAndGet() == 0) {
/* 6984 */           occurrences.remove(Character.valueOf(key));
/*      */         }
/* 6986 */         toRemove.set(i);
/*      */       } 
/*      */     } 
/* 6989 */     return (char[])removeAll(array, toRemove);
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
/*      */   public static long[] removeAll(long[] array, int... indices) {
/* 7022 */     return (long[])removeAll(array, indices);
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
/*      */   public static long[] removeElements(long[] array, long... values) {
/* 7053 */     if (isEmpty(array) || isEmpty(values)) {
/* 7054 */       return clone(array);
/*      */     }
/* 7056 */     HashMap<Long, MutableInt> occurrences = new HashMap<Long, MutableInt>(values.length);
/* 7057 */     for (long v : values) {
/* 7058 */       Long boxed = Long.valueOf(v);
/* 7059 */       MutableInt count = occurrences.get(boxed);
/* 7060 */       if (count == null) {
/* 7061 */         occurrences.put(boxed, new MutableInt(1));
/*      */       } else {
/* 7063 */         count.increment();
/*      */       } 
/*      */     } 
/* 7066 */     BitSet toRemove = new BitSet();
/* 7067 */     for (int i = 0; i < array.length; i++) {
/* 7068 */       long key = array[i];
/* 7069 */       MutableInt count = occurrences.get(Long.valueOf(key));
/* 7070 */       if (count != null) {
/* 7071 */         if (count.decrementAndGet() == 0) {
/* 7072 */           occurrences.remove(Long.valueOf(key));
/*      */         }
/* 7074 */         toRemove.set(i);
/*      */       } 
/*      */     } 
/* 7077 */     return (long[])removeAll(array, toRemove);
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
/*      */   public static float[] removeAll(float[] array, int... indices) {
/* 7110 */     return (float[])removeAll(array, indices);
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
/*      */   public static float[] removeElements(float[] array, float... values) {
/* 7141 */     if (isEmpty(array) || isEmpty(values)) {
/* 7142 */       return clone(array);
/*      */     }
/* 7144 */     HashMap<Float, MutableInt> occurrences = new HashMap<Float, MutableInt>(values.length);
/* 7145 */     for (float v : values) {
/* 7146 */       Float boxed = Float.valueOf(v);
/* 7147 */       MutableInt count = occurrences.get(boxed);
/* 7148 */       if (count == null) {
/* 7149 */         occurrences.put(boxed, new MutableInt(1));
/*      */       } else {
/* 7151 */         count.increment();
/*      */       } 
/*      */     } 
/* 7154 */     BitSet toRemove = new BitSet();
/* 7155 */     for (int i = 0; i < array.length; i++) {
/* 7156 */       float key = array[i];
/* 7157 */       MutableInt count = occurrences.get(Float.valueOf(key));
/* 7158 */       if (count != null) {
/* 7159 */         if (count.decrementAndGet() == 0) {
/* 7160 */           occurrences.remove(Float.valueOf(key));
/*      */         }
/* 7162 */         toRemove.set(i);
/*      */       } 
/*      */     } 
/* 7165 */     return (float[])removeAll(array, toRemove);
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
/*      */   public static double[] removeAll(double[] array, int... indices) {
/* 7198 */     return (double[])removeAll(array, indices);
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
/*      */   public static double[] removeElements(double[] array, double... values) {
/* 7229 */     if (isEmpty(array) || isEmpty(values)) {
/* 7230 */       return clone(array);
/*      */     }
/* 7232 */     HashMap<Double, MutableInt> occurrences = new HashMap<Double, MutableInt>(values.length);
/* 7233 */     for (double v : values) {
/* 7234 */       Double boxed = Double.valueOf(v);
/* 7235 */       MutableInt count = occurrences.get(boxed);
/* 7236 */       if (count == null) {
/* 7237 */         occurrences.put(boxed, new MutableInt(1));
/*      */       } else {
/* 7239 */         count.increment();
/*      */       } 
/*      */     } 
/* 7242 */     BitSet toRemove = new BitSet();
/* 7243 */     for (int i = 0; i < array.length; i++) {
/* 7244 */       double key = array[i];
/* 7245 */       MutableInt count = occurrences.get(Double.valueOf(key));
/* 7246 */       if (count != null) {
/* 7247 */         if (count.decrementAndGet() == 0) {
/* 7248 */           occurrences.remove(Double.valueOf(key));
/*      */         }
/* 7250 */         toRemove.set(i);
/*      */       } 
/*      */     } 
/* 7253 */     return (double[])removeAll(array, toRemove);
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
/*      */   public static boolean[] removeAll(boolean[] array, int... indices) {
/* 7282 */     return (boolean[])removeAll(array, indices);
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
/*      */   public static boolean[] removeElements(boolean[] array, boolean... values) {
/* 7313 */     if (isEmpty(array) || isEmpty(values)) {
/* 7314 */       return clone(array);
/*      */     }
/* 7316 */     HashMap<Boolean, MutableInt> occurrences = new HashMap<Boolean, MutableInt>(2);
/* 7317 */     for (boolean v : values) {
/* 7318 */       Boolean boxed = Boolean.valueOf(v);
/* 7319 */       MutableInt count = occurrences.get(boxed);
/* 7320 */       if (count == null) {
/* 7321 */         occurrences.put(boxed, new MutableInt(1));
/*      */       } else {
/* 7323 */         count.increment();
/*      */       } 
/*      */     } 
/* 7326 */     BitSet toRemove = new BitSet();
/* 7327 */     for (int i = 0; i < array.length; i++) {
/* 7328 */       boolean key = array[i];
/* 7329 */       MutableInt count = occurrences.get(Boolean.valueOf(key));
/* 7330 */       if (count != null) {
/* 7331 */         if (count.decrementAndGet() == 0) {
/* 7332 */           occurrences.remove(Boolean.valueOf(key));
/*      */         }
/* 7334 */         toRemove.set(i);
/*      */       } 
/*      */     } 
/* 7337 */     return (boolean[])removeAll(array, toRemove);
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
/*      */   static Object removeAll(Object array, int... indices) {
/* 7349 */     int length = getLength(array);
/* 7350 */     int diff = 0;
/* 7351 */     int[] clonedIndices = clone(indices);
/* 7352 */     Arrays.sort(clonedIndices);
/*      */ 
/*      */     
/* 7355 */     if (isNotEmpty(clonedIndices)) {
/* 7356 */       int i = clonedIndices.length;
/* 7357 */       int prevIndex = length;
/* 7358 */       while (--i >= 0) {
/* 7359 */         int index = clonedIndices[i];
/* 7360 */         if (index < 0 || index >= length) {
/* 7361 */           throw new IndexOutOfBoundsException("Index: " + index + ", Length: " + length);
/*      */         }
/* 7363 */         if (index >= prevIndex) {
/*      */           continue;
/*      */         }
/* 7366 */         diff++;
/* 7367 */         prevIndex = index;
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/* 7372 */     Object result = Array.newInstance(array.getClass().getComponentType(), length - diff);
/* 7373 */     if (diff < length) {
/* 7374 */       int end = length;
/* 7375 */       int dest = length - diff;
/* 7376 */       for (int i = clonedIndices.length - 1; i >= 0; i--) {
/* 7377 */         int index = clonedIndices[i];
/* 7378 */         if (end - index > 1) {
/* 7379 */           int cp = end - index - 1;
/* 7380 */           dest -= cp;
/* 7381 */           System.arraycopy(array, index + 1, result, dest, cp);
/*      */         } 
/*      */         
/* 7384 */         end = index;
/*      */       } 
/* 7386 */       if (end > 0) {
/* 7387 */         System.arraycopy(array, 0, result, 0, end);
/*      */       }
/*      */     } 
/* 7390 */     return result;
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
/*      */   static Object removeAll(Object array, BitSet indices) {
/* 7403 */     int srcLength = getLength(array);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 7410 */     int removals = indices.cardinality();
/* 7411 */     Object result = Array.newInstance(array.getClass().getComponentType(), srcLength - removals);
/* 7412 */     int srcIndex = 0;
/* 7413 */     int destIndex = 0;
/*      */     
/*      */     int set;
/* 7416 */     while ((set = indices.nextSetBit(srcIndex)) != -1) {
/* 7417 */       int i = set - srcIndex;
/* 7418 */       if (i > 0) {
/* 7419 */         System.arraycopy(array, srcIndex, result, destIndex, i);
/* 7420 */         destIndex += i;
/*      */       } 
/* 7422 */       srcIndex = indices.nextClearBit(set);
/*      */     } 
/* 7424 */     int count = srcLength - srcIndex;
/* 7425 */     if (count > 0) {
/* 7426 */       System.arraycopy(array, srcIndex, result, destIndex, count);
/*      */     }
/* 7428 */     return result;
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
/*      */   public static <T extends Comparable<? super T>> boolean isSorted(T[] array) {
/* 7441 */     return isSorted(array, new Comparator<T>()
/*      */         {
/*      */           public int compare(T o1, T o2) {
/* 7444 */             return o1.compareTo(o2);
/*      */           }
/*      */         });
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
/*      */   public static <T> boolean isSorted(T[] array, Comparator<T> comparator) {
/* 7460 */     if (comparator == null) {
/* 7461 */       throw new IllegalArgumentException("Comparator should not be null.");
/*      */     }
/*      */     
/* 7464 */     if (array == null || array.length < 2) {
/* 7465 */       return true;
/*      */     }
/*      */     
/* 7468 */     T previous = array[0];
/* 7469 */     int n = array.length;
/* 7470 */     for (int i = 1; i < n; i++) {
/* 7471 */       T current = array[i];
/* 7472 */       if (comparator.compare(previous, current) > 0) {
/* 7473 */         return false;
/*      */       }
/*      */       
/* 7476 */       previous = current;
/*      */     } 
/* 7478 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isSorted(int[] array) {
/* 7489 */     if (array == null || array.length < 2) {
/* 7490 */       return true;
/*      */     }
/*      */     
/* 7493 */     int previous = array[0];
/* 7494 */     int n = array.length;
/* 7495 */     for (int i = 1; i < n; i++) {
/* 7496 */       int current = array[i];
/* 7497 */       if (NumberUtils.compare(previous, current) > 0) {
/* 7498 */         return false;
/*      */       }
/*      */       
/* 7501 */       previous = current;
/*      */     } 
/* 7503 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isSorted(long[] array) {
/* 7514 */     if (array == null || array.length < 2) {
/* 7515 */       return true;
/*      */     }
/*      */     
/* 7518 */     long previous = array[0];
/* 7519 */     int n = array.length;
/* 7520 */     for (int i = 1; i < n; i++) {
/* 7521 */       long current = array[i];
/* 7522 */       if (NumberUtils.compare(previous, current) > 0) {
/* 7523 */         return false;
/*      */       }
/*      */       
/* 7526 */       previous = current;
/*      */     } 
/* 7528 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isSorted(short[] array) {
/* 7539 */     if (array == null || array.length < 2) {
/* 7540 */       return true;
/*      */     }
/*      */     
/* 7543 */     short previous = array[0];
/* 7544 */     int n = array.length;
/* 7545 */     for (int i = 1; i < n; i++) {
/* 7546 */       short current = array[i];
/* 7547 */       if (NumberUtils.compare(previous, current) > 0) {
/* 7548 */         return false;
/*      */       }
/*      */       
/* 7551 */       previous = current;
/*      */     } 
/* 7553 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isSorted(double[] array) {
/* 7564 */     if (array == null || array.length < 2) {
/* 7565 */       return true;
/*      */     }
/*      */     
/* 7568 */     double previous = array[0];
/* 7569 */     int n = array.length;
/* 7570 */     for (int i = 1; i < n; i++) {
/* 7571 */       double current = array[i];
/* 7572 */       if (Double.compare(previous, current) > 0) {
/* 7573 */         return false;
/*      */       }
/*      */       
/* 7576 */       previous = current;
/*      */     } 
/* 7578 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isSorted(float[] array) {
/* 7589 */     if (array == null || array.length < 2) {
/* 7590 */       return true;
/*      */     }
/*      */     
/* 7593 */     float previous = array[0];
/* 7594 */     int n = array.length;
/* 7595 */     for (int i = 1; i < n; i++) {
/* 7596 */       float current = array[i];
/* 7597 */       if (Float.compare(previous, current) > 0) {
/* 7598 */         return false;
/*      */       }
/*      */       
/* 7601 */       previous = current;
/*      */     } 
/* 7603 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isSorted(byte[] array) {
/* 7614 */     if (array == null || array.length < 2) {
/* 7615 */       return true;
/*      */     }
/*      */     
/* 7618 */     byte previous = array[0];
/* 7619 */     int n = array.length;
/* 7620 */     for (int i = 1; i < n; i++) {
/* 7621 */       byte current = array[i];
/* 7622 */       if (NumberUtils.compare(previous, current) > 0) {
/* 7623 */         return false;
/*      */       }
/*      */       
/* 7626 */       previous = current;
/*      */     } 
/* 7628 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isSorted(char[] array) {
/* 7639 */     if (array == null || array.length < 2) {
/* 7640 */       return true;
/*      */     }
/*      */     
/* 7643 */     char previous = array[0];
/* 7644 */     int n = array.length;
/* 7645 */     for (int i = 1; i < n; i++) {
/* 7646 */       char current = array[i];
/* 7647 */       if (CharUtils.compare(previous, current) > 0) {
/* 7648 */         return false;
/*      */       }
/*      */       
/* 7651 */       previous = current;
/*      */     } 
/* 7653 */     return true;
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
/*      */   public static boolean isSorted(boolean[] array) {
/* 7665 */     if (array == null || array.length < 2) {
/* 7666 */       return true;
/*      */     }
/*      */     
/* 7669 */     boolean previous = array[0];
/* 7670 */     int n = array.length;
/* 7671 */     for (int i = 1; i < n; i++) {
/* 7672 */       boolean current = array[i];
/* 7673 */       if (BooleanUtils.compare(previous, current) > 0) {
/* 7674 */         return false;
/*      */       }
/*      */       
/* 7677 */       previous = current;
/*      */     } 
/* 7679 */     return true;
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
/*      */   public static boolean[] removeAllOccurences(boolean[] array, boolean element) {
/* 7698 */     int index = indexOf(array, element);
/* 7699 */     if (index == -1) {
/* 7700 */       return clone(array);
/*      */     }
/*      */     
/* 7703 */     int[] indices = new int[array.length - index];
/* 7704 */     indices[0] = index;
/* 7705 */     int count = 1;
/*      */     
/* 7707 */     while ((index = indexOf(array, element, indices[count - 1] + 1)) != -1) {
/* 7708 */       indices[count++] = index;
/*      */     }
/*      */     
/* 7711 */     return removeAll(array, Arrays.copyOf(indices, count));
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
/*      */   public static char[] removeAllOccurences(char[] array, char element) {
/* 7730 */     int index = indexOf(array, element);
/* 7731 */     if (index == -1) {
/* 7732 */       return clone(array);
/*      */     }
/*      */     
/* 7735 */     int[] indices = new int[array.length - index];
/* 7736 */     indices[0] = index;
/* 7737 */     int count = 1;
/*      */     
/* 7739 */     while ((index = indexOf(array, element, indices[count - 1] + 1)) != -1) {
/* 7740 */       indices[count++] = index;
/*      */     }
/*      */     
/* 7743 */     return removeAll(array, Arrays.copyOf(indices, count));
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
/*      */   public static byte[] removeAllOccurences(byte[] array, byte element) {
/* 7762 */     int index = indexOf(array, element);
/* 7763 */     if (index == -1) {
/* 7764 */       return clone(array);
/*      */     }
/*      */     
/* 7767 */     int[] indices = new int[array.length - index];
/* 7768 */     indices[0] = index;
/* 7769 */     int count = 1;
/*      */     
/* 7771 */     while ((index = indexOf(array, element, indices[count - 1] + 1)) != -1) {
/* 7772 */       indices[count++] = index;
/*      */     }
/*      */     
/* 7775 */     return removeAll(array, Arrays.copyOf(indices, count));
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
/*      */   public static short[] removeAllOccurences(short[] array, short element) {
/* 7794 */     int index = indexOf(array, element);
/* 7795 */     if (index == -1) {
/* 7796 */       return clone(array);
/*      */     }
/*      */     
/* 7799 */     int[] indices = new int[array.length - index];
/* 7800 */     indices[0] = index;
/* 7801 */     int count = 1;
/*      */     
/* 7803 */     while ((index = indexOf(array, element, indices[count - 1] + 1)) != -1) {
/* 7804 */       indices[count++] = index;
/*      */     }
/*      */     
/* 7807 */     return removeAll(array, Arrays.copyOf(indices, count));
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
/*      */   public static int[] removeAllOccurences(int[] array, int element) {
/* 7826 */     int index = indexOf(array, element);
/* 7827 */     if (index == -1) {
/* 7828 */       return clone(array);
/*      */     }
/*      */     
/* 7831 */     int[] indices = new int[array.length - index];
/* 7832 */     indices[0] = index;
/* 7833 */     int count = 1;
/*      */     
/* 7835 */     while ((index = indexOf(array, element, indices[count - 1] + 1)) != -1) {
/* 7836 */       indices[count++] = index;
/*      */     }
/*      */     
/* 7839 */     return removeAll(array, Arrays.copyOf(indices, count));
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
/*      */   public static long[] removeAllOccurences(long[] array, long element) {
/* 7858 */     int index = indexOf(array, element);
/* 7859 */     if (index == -1) {
/* 7860 */       return clone(array);
/*      */     }
/*      */     
/* 7863 */     int[] indices = new int[array.length - index];
/* 7864 */     indices[0] = index;
/* 7865 */     int count = 1;
/*      */     
/* 7867 */     while ((index = indexOf(array, element, indices[count - 1] + 1)) != -1) {
/* 7868 */       indices[count++] = index;
/*      */     }
/*      */     
/* 7871 */     return removeAll(array, Arrays.copyOf(indices, count));
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
/*      */   public static float[] removeAllOccurences(float[] array, float element) {
/* 7890 */     int index = indexOf(array, element);
/* 7891 */     if (index == -1) {
/* 7892 */       return clone(array);
/*      */     }
/*      */     
/* 7895 */     int[] indices = new int[array.length - index];
/* 7896 */     indices[0] = index;
/* 7897 */     int count = 1;
/*      */     
/* 7899 */     while ((index = indexOf(array, element, indices[count - 1] + 1)) != -1) {
/* 7900 */       indices[count++] = index;
/*      */     }
/*      */     
/* 7903 */     return removeAll(array, Arrays.copyOf(indices, count));
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
/*      */   public static double[] removeAllOccurences(double[] array, double element) {
/* 7922 */     int index = indexOf(array, element);
/* 7923 */     if (index == -1) {
/* 7924 */       return clone(array);
/*      */     }
/*      */     
/* 7927 */     int[] indices = new int[array.length - index];
/* 7928 */     indices[0] = index;
/* 7929 */     int count = 1;
/*      */     
/* 7931 */     while ((index = indexOf(array, element, indices[count - 1] + 1)) != -1) {
/* 7932 */       indices[count++] = index;
/*      */     }
/*      */     
/* 7935 */     return removeAll(array, Arrays.copyOf(indices, count));
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
/*      */   public static <T> T[] removeAllOccurences(T[] array, T element) {
/* 7955 */     int index = indexOf((Object[])array, element);
/* 7956 */     if (index == -1) {
/* 7957 */       return clone(array);
/*      */     }
/*      */     
/* 7960 */     int[] indices = new int[array.length - index];
/* 7961 */     indices[0] = index;
/* 7962 */     int count = 1;
/*      */     
/* 7964 */     while ((index = indexOf((Object[])array, element, indices[count - 1] + 1)) != -1) {
/* 7965 */       indices[count++] = index;
/*      */     }
/*      */     
/* 7968 */     return removeAll(array, Arrays.copyOf(indices, count));
/*      */   }
/*      */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\commons\lang3\ArrayUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */