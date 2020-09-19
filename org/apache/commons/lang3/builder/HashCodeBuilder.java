/*     */ package org.apache.commons.lang3.builder;
/*     */ 
/*     */ import java.lang.reflect.AccessibleObject;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.util.Collection;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import org.apache.commons.lang3.ArrayUtils;
/*     */ import org.apache.commons.lang3.Validate;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class HashCodeBuilder
/*     */   implements Builder<Integer>
/*     */ {
/*     */   private static final int DEFAULT_INITIAL_VALUE = 17;
/*     */   private static final int DEFAULT_MULTIPLIER_VALUE = 37;
/* 121 */   private static final ThreadLocal<Set<IDKey>> REGISTRY = new ThreadLocal<Set<IDKey>>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final int iConstant;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static Set<IDKey> getRegistry() {
/* 149 */     return REGISTRY.get();
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
/*     */   static boolean isRegistered(Object value) {
/* 164 */     Set<IDKey> registry = getRegistry();
/* 165 */     return (registry != null && registry.contains(new IDKey(value)));
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
/*     */   private static void reflectionAppend(Object object, Class<?> clazz, HashCodeBuilder builder, boolean useTransients, String[] excludeFields) {
/* 186 */     if (isRegistered(object)) {
/*     */       return;
/*     */     }
/*     */     try {
/* 190 */       register(object);
/* 191 */       Field[] fields = clazz.getDeclaredFields();
/* 192 */       AccessibleObject.setAccessible((AccessibleObject[])fields, true);
/* 193 */       for (Field field : fields) {
/* 194 */         if (!ArrayUtils.contains((Object[])excludeFields, field.getName()) && 
/* 195 */           !field.getName().contains("$") && (useTransients || 
/* 196 */           !Modifier.isTransient(field.getModifiers())) && 
/* 197 */           !Modifier.isStatic(field.getModifiers()) && 
/* 198 */           !field.isAnnotationPresent((Class)HashCodeExclude.class)) {
/*     */           try {
/* 200 */             Object fieldValue = field.get(object);
/* 201 */             builder.append(fieldValue);
/* 202 */           } catch (IllegalAccessException e) {
/*     */ 
/*     */             
/* 205 */             throw new InternalError("Unexpected IllegalAccessException");
/*     */           } 
/*     */         }
/*     */       } 
/*     */     } finally {
/* 210 */       unregister(object);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int reflectionHashCode(int initialNonZeroOddNumber, int multiplierNonZeroOddNumber, Object object) {
/* 255 */     return reflectionHashCode(initialNonZeroOddNumber, multiplierNonZeroOddNumber, object, false, null, new String[0]);
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
/*     */   public static int reflectionHashCode(int initialNonZeroOddNumber, int multiplierNonZeroOddNumber, Object object, boolean testTransients) {
/* 302 */     return reflectionHashCode(initialNonZeroOddNumber, multiplierNonZeroOddNumber, object, testTransients, null, new String[0]);
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
/*     */   public static <T> int reflectionHashCode(int initialNonZeroOddNumber, int multiplierNonZeroOddNumber, T object, boolean testTransients, Class<? super T> reflectUpToClass, String... excludeFields) {
/* 358 */     if (object == null) {
/* 359 */       throw new IllegalArgumentException("The object to build a hash code for must not be null");
/*     */     }
/* 361 */     HashCodeBuilder builder = new HashCodeBuilder(initialNonZeroOddNumber, multiplierNonZeroOddNumber);
/* 362 */     Class<?> clazz = object.getClass();
/* 363 */     reflectionAppend(object, clazz, builder, testTransients, excludeFields);
/* 364 */     while (clazz.getSuperclass() != null && clazz != reflectUpToClass) {
/* 365 */       clazz = clazz.getSuperclass();
/* 366 */       reflectionAppend(object, clazz, builder, testTransients, excludeFields);
/*     */     } 
/* 368 */     return builder.toHashCode();
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
/*     */   public static int reflectionHashCode(Object object, boolean testTransients) {
/* 407 */     return reflectionHashCode(17, 37, object, testTransients, null, new String[0]);
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
/*     */   public static int reflectionHashCode(Object object, Collection<String> excludeFields) {
/* 447 */     return reflectionHashCode(object, ReflectionToStringBuilder.toNoNullStringArray(excludeFields));
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
/*     */   public static int reflectionHashCode(Object object, String... excludeFields) {
/* 488 */     return reflectionHashCode(17, 37, object, false, null, excludeFields);
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
/*     */   private static void register(Object value) {
/* 501 */     Set<IDKey> registry = getRegistry();
/* 502 */     if (registry == null) {
/* 503 */       registry = new HashSet<IDKey>();
/* 504 */       REGISTRY.set(registry);
/*     */     } 
/* 506 */     registry.add(new IDKey(value));
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
/*     */   private static void unregister(Object value) {
/* 522 */     Set<IDKey> registry = getRegistry();
/* 523 */     if (registry != null) {
/* 524 */       registry.remove(new IDKey(value));
/* 525 */       if (registry.isEmpty()) {
/* 526 */         REGISTRY.remove();
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
/* 539 */   private int iTotal = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HashCodeBuilder() {
/* 547 */     this.iConstant = 37;
/* 548 */     this.iTotal = 17;
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
/*     */   public HashCodeBuilder(int initialOddNumber, int multiplierOddNumber) {
/* 569 */     Validate.isTrue((initialOddNumber % 2 != 0), "HashCodeBuilder requires an odd initial value", new Object[0]);
/* 570 */     Validate.isTrue((multiplierOddNumber % 2 != 0), "HashCodeBuilder requires an odd multiplier", new Object[0]);
/* 571 */     this.iConstant = multiplierOddNumber;
/* 572 */     this.iTotal = initialOddNumber;
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
/*     */   public HashCodeBuilder append(boolean value) {
/* 597 */     this.iTotal = this.iTotal * this.iConstant + (value ? 0 : 1);
/* 598 */     return this;
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
/*     */   public HashCodeBuilder append(boolean[] array) {
/* 611 */     if (array == null) {
/* 612 */       this.iTotal *= this.iConstant;
/*     */     } else {
/* 614 */       for (boolean element : array) {
/* 615 */         append(element);
/*     */       }
/*     */     } 
/* 618 */     return this;
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
/*     */   public HashCodeBuilder append(byte value) {
/* 633 */     this.iTotal = this.iTotal * this.iConstant + value;
/* 634 */     return this;
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
/*     */   public HashCodeBuilder append(byte[] array) {
/* 649 */     if (array == null) {
/* 650 */       this.iTotal *= this.iConstant;
/*     */     } else {
/* 652 */       for (byte element : array) {
/* 653 */         append(element);
/*     */       }
/*     */     } 
/* 656 */     return this;
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
/*     */   public HashCodeBuilder append(char value) {
/* 669 */     this.iTotal = this.iTotal * this.iConstant + value;
/* 670 */     return this;
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
/*     */   public HashCodeBuilder append(char[] array) {
/* 683 */     if (array == null) {
/* 684 */       this.iTotal *= this.iConstant;
/*     */     } else {
/* 686 */       for (char element : array) {
/* 687 */         append(element);
/*     */       }
/*     */     } 
/* 690 */     return this;
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
/*     */   public HashCodeBuilder append(double value) {
/* 703 */     return append(Double.doubleToLongBits(value));
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
/*     */   public HashCodeBuilder append(double[] array) {
/* 716 */     if (array == null) {
/* 717 */       this.iTotal *= this.iConstant;
/*     */     } else {
/* 719 */       for (double element : array) {
/* 720 */         append(element);
/*     */       }
/*     */     } 
/* 723 */     return this;
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
/*     */   public HashCodeBuilder append(float value) {
/* 736 */     this.iTotal = this.iTotal * this.iConstant + Float.floatToIntBits(value);
/* 737 */     return this;
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
/*     */   public HashCodeBuilder append(float[] array) {
/* 750 */     if (array == null) {
/* 751 */       this.iTotal *= this.iConstant;
/*     */     } else {
/* 753 */       for (float element : array) {
/* 754 */         append(element);
/*     */       }
/*     */     } 
/* 757 */     return this;
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
/*     */   public HashCodeBuilder append(int value) {
/* 770 */     this.iTotal = this.iTotal * this.iConstant + value;
/* 771 */     return this;
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
/*     */   public HashCodeBuilder append(int[] array) {
/* 784 */     if (array == null) {
/* 785 */       this.iTotal *= this.iConstant;
/*     */     } else {
/* 787 */       for (int element : array) {
/* 788 */         append(element);
/*     */       }
/*     */     } 
/* 791 */     return this;
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
/*     */   public HashCodeBuilder append(long value) {
/* 808 */     this.iTotal = this.iTotal * this.iConstant + (int)(value ^ value >> 32L);
/* 809 */     return this;
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
/*     */   public HashCodeBuilder append(long[] array) {
/* 822 */     if (array == null) {
/* 823 */       this.iTotal *= this.iConstant;
/*     */     } else {
/* 825 */       for (long element : array) {
/* 826 */         append(element);
/*     */       }
/*     */     } 
/* 829 */     return this;
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
/*     */   public HashCodeBuilder append(Object object) {
/* 842 */     if (object == null) {
/* 843 */       this.iTotal *= this.iConstant;
/*     */     
/*     */     }
/* 846 */     else if (object.getClass().isArray()) {
/*     */ 
/*     */       
/* 849 */       appendArray(object);
/*     */     } else {
/* 851 */       this.iTotal = this.iTotal * this.iConstant + object.hashCode();
/*     */     } 
/*     */     
/* 854 */     return this;
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
/*     */   private void appendArray(Object object) {
/* 868 */     if (object instanceof long[]) {
/* 869 */       append((long[])object);
/* 870 */     } else if (object instanceof int[]) {
/* 871 */       append((int[])object);
/* 872 */     } else if (object instanceof short[]) {
/* 873 */       append((short[])object);
/* 874 */     } else if (object instanceof char[]) {
/* 875 */       append((char[])object);
/* 876 */     } else if (object instanceof byte[]) {
/* 877 */       append((byte[])object);
/* 878 */     } else if (object instanceof double[]) {
/* 879 */       append((double[])object);
/* 880 */     } else if (object instanceof float[]) {
/* 881 */       append((float[])object);
/* 882 */     } else if (object instanceof boolean[]) {
/* 883 */       append((boolean[])object);
/*     */     } else {
/*     */       
/* 886 */       append((Object[])object);
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
/*     */   public HashCodeBuilder append(Object[] array) {
/* 900 */     if (array == null) {
/* 901 */       this.iTotal *= this.iConstant;
/*     */     } else {
/* 903 */       for (Object element : array) {
/* 904 */         append(element);
/*     */       }
/*     */     } 
/* 907 */     return this;
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
/*     */   public HashCodeBuilder append(short value) {
/* 920 */     this.iTotal = this.iTotal * this.iConstant + value;
/* 921 */     return this;
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
/*     */   public HashCodeBuilder append(short[] array) {
/* 934 */     if (array == null) {
/* 935 */       this.iTotal *= this.iConstant;
/*     */     } else {
/* 937 */       for (short element : array) {
/* 938 */         append(element);
/*     */       }
/*     */     } 
/* 941 */     return this;
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
/*     */   public HashCodeBuilder appendSuper(int superHashCode) {
/* 955 */     this.iTotal = this.iTotal * this.iConstant + superHashCode;
/* 956 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int toHashCode() {
/* 967 */     return this.iTotal;
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
/*     */   public Integer build() {
/* 979 */     return Integer.valueOf(toHashCode());
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
/*     */   public int hashCode() {
/* 993 */     return toHashCode();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\commons\lang3\builder\HashCodeBuilder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */