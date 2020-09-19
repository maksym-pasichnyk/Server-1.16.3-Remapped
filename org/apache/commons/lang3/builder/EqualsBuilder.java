/*     */ package org.apache.commons.lang3.builder;
/*     */ 
/*     */ import java.lang.reflect.AccessibleObject;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.util.Collection;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import org.apache.commons.lang3.ArrayUtils;
/*     */ import org.apache.commons.lang3.tuple.Pair;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class EqualsBuilder
/*     */   implements Builder<Boolean>
/*     */ {
/*  95 */   private static final ThreadLocal<Set<Pair<IDKey, IDKey>>> REGISTRY = new ThreadLocal<Set<Pair<IDKey, IDKey>>>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static Set<Pair<IDKey, IDKey>> getRegistry() {
/* 124 */     return REGISTRY.get();
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
/*     */   static Pair<IDKey, IDKey> getRegisterPair(Object lhs, Object rhs) {
/* 138 */     IDKey left = new IDKey(lhs);
/* 139 */     IDKey right = new IDKey(rhs);
/* 140 */     return Pair.of(left, right);
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
/*     */   static boolean isRegistered(Object lhs, Object rhs) {
/* 157 */     Set<Pair<IDKey, IDKey>> registry = getRegistry();
/* 158 */     Pair<IDKey, IDKey> pair = getRegisterPair(lhs, rhs);
/* 159 */     Pair<IDKey, IDKey> swappedPair = Pair.of(pair.getLeft(), pair.getRight());
/*     */     
/* 161 */     return (registry != null && (registry
/* 162 */       .contains(pair) || registry.contains(swappedPair)));
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
/*     */   private static void register(Object lhs, Object rhs) {
/* 175 */     Set<Pair<IDKey, IDKey>> registry = getRegistry();
/* 176 */     if (registry == null) {
/* 177 */       registry = new HashSet<Pair<IDKey, IDKey>>();
/* 178 */       REGISTRY.set(registry);
/*     */     } 
/* 180 */     Pair<IDKey, IDKey> pair = getRegisterPair(lhs, rhs);
/* 181 */     registry.add(pair);
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
/*     */   private static void unregister(Object lhs, Object rhs) {
/* 197 */     Set<Pair<IDKey, IDKey>> registry = getRegistry();
/* 198 */     if (registry != null) {
/* 199 */       Pair<IDKey, IDKey> pair = getRegisterPair(lhs, rhs);
/* 200 */       registry.remove(pair);
/* 201 */       if (registry.isEmpty()) {
/* 202 */         REGISTRY.remove();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean isEquals = true;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean reflectionEquals(Object lhs, Object rhs, Collection<String> excludeFields) {
/* 248 */     return reflectionEquals(lhs, rhs, ReflectionToStringBuilder.toNoNullStringArray(excludeFields));
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
/*     */   public static boolean reflectionEquals(Object lhs, Object rhs, String... excludeFields) {
/* 274 */     return reflectionEquals(lhs, rhs, false, null, excludeFields);
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
/*     */   public static boolean reflectionEquals(Object lhs, Object rhs, boolean testTransients) {
/* 301 */     return reflectionEquals(lhs, rhs, testTransients, null, new String[0]);
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
/*     */   public static boolean reflectionEquals(Object lhs, Object rhs, boolean testTransients, Class<?> reflectUpToClass, String... excludeFields) {
/*     */     Class<?> testClass;
/* 335 */     if (lhs == rhs) {
/* 336 */       return true;
/*     */     }
/* 338 */     if (lhs == null || rhs == null) {
/* 339 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 345 */     Class<?> lhsClass = lhs.getClass();
/* 346 */     Class<?> rhsClass = rhs.getClass();
/*     */     
/* 348 */     if (lhsClass.isInstance(rhs)) {
/* 349 */       testClass = lhsClass;
/* 350 */       if (!rhsClass.isInstance(lhs))
/*     */       {
/* 352 */         testClass = rhsClass;
/*     */       }
/* 354 */     } else if (rhsClass.isInstance(lhs)) {
/* 355 */       testClass = rhsClass;
/* 356 */       if (!lhsClass.isInstance(rhs))
/*     */       {
/* 358 */         testClass = lhsClass;
/*     */       }
/*     */     } else {
/*     */       
/* 362 */       return false;
/*     */     } 
/* 364 */     EqualsBuilder equalsBuilder = new EqualsBuilder();
/*     */     try {
/* 366 */       if (testClass.isArray()) {
/* 367 */         equalsBuilder.append(lhs, rhs);
/*     */       } else {
/* 369 */         reflectionAppend(lhs, rhs, testClass, equalsBuilder, testTransients, excludeFields);
/* 370 */         while (testClass.getSuperclass() != null && testClass != reflectUpToClass) {
/* 371 */           testClass = testClass.getSuperclass();
/* 372 */           reflectionAppend(lhs, rhs, testClass, equalsBuilder, testTransients, excludeFields);
/*     */         } 
/*     */       } 
/* 375 */     } catch (IllegalArgumentException e) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 381 */       return false;
/*     */     } 
/* 383 */     return equalsBuilder.isEquals();
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
/*     */   private static void reflectionAppend(Object lhs, Object rhs, Class<?> clazz, EqualsBuilder builder, boolean useTransients, String[] excludeFields) {
/* 405 */     if (isRegistered(lhs, rhs)) {
/*     */       return;
/*     */     }
/*     */     
/*     */     try {
/* 410 */       register(lhs, rhs);
/* 411 */       Field[] fields = clazz.getDeclaredFields();
/* 412 */       AccessibleObject.setAccessible((AccessibleObject[])fields, true);
/* 413 */       for (int i = 0; i < fields.length && builder.isEquals; i++) {
/* 414 */         Field f = fields[i];
/* 415 */         if (!ArrayUtils.contains((Object[])excludeFields, f.getName()) && 
/* 416 */           !f.getName().contains("$") && (useTransients || 
/* 417 */           !Modifier.isTransient(f.getModifiers())) && 
/* 418 */           !Modifier.isStatic(f.getModifiers()) && 
/* 419 */           !f.isAnnotationPresent((Class)EqualsExclude.class)) {
/*     */           try {
/* 421 */             builder.append(f.get(lhs), f.get(rhs));
/* 422 */           } catch (IllegalAccessException e) {
/*     */ 
/*     */             
/* 425 */             throw new InternalError("Unexpected IllegalAccessException");
/*     */           } 
/*     */         }
/*     */       } 
/*     */     } finally {
/* 430 */       unregister(lhs, rhs);
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
/*     */   public EqualsBuilder appendSuper(boolean superEquals) {
/* 444 */     if (!this.isEquals) {
/* 445 */       return this;
/*     */     }
/* 447 */     this.isEquals = superEquals;
/* 448 */     return this;
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
/*     */   public EqualsBuilder append(Object lhs, Object rhs) {
/* 462 */     if (!this.isEquals) {
/* 463 */       return this;
/*     */     }
/* 465 */     if (lhs == rhs) {
/* 466 */       return this;
/*     */     }
/* 468 */     if (lhs == null || rhs == null) {
/* 469 */       setEquals(false);
/* 470 */       return this;
/*     */     } 
/* 472 */     Class<?> lhsClass = lhs.getClass();
/* 473 */     if (!lhsClass.isArray()) {
/*     */       
/* 475 */       this.isEquals = lhs.equals(rhs);
/*     */     }
/*     */     else {
/*     */       
/* 479 */       appendArray(lhs, rhs);
/*     */     } 
/* 481 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void appendArray(Object lhs, Object rhs) {
/* 491 */     if (lhs.getClass() != rhs.getClass()) {
/*     */       
/* 493 */       setEquals(false);
/*     */ 
/*     */     
/*     */     }
/* 497 */     else if (lhs instanceof long[]) {
/* 498 */       append((long[])lhs, (long[])rhs);
/* 499 */     } else if (lhs instanceof int[]) {
/* 500 */       append((int[])lhs, (int[])rhs);
/* 501 */     } else if (lhs instanceof short[]) {
/* 502 */       append((short[])lhs, (short[])rhs);
/* 503 */     } else if (lhs instanceof char[]) {
/* 504 */       append((char[])lhs, (char[])rhs);
/* 505 */     } else if (lhs instanceof byte[]) {
/* 506 */       append((byte[])lhs, (byte[])rhs);
/* 507 */     } else if (lhs instanceof double[]) {
/* 508 */       append((double[])lhs, (double[])rhs);
/* 509 */     } else if (lhs instanceof float[]) {
/* 510 */       append((float[])lhs, (float[])rhs);
/* 511 */     } else if (lhs instanceof boolean[]) {
/* 512 */       append((boolean[])lhs, (boolean[])rhs);
/*     */     } else {
/*     */       
/* 515 */       append((Object[])lhs, (Object[])rhs);
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
/*     */   public EqualsBuilder append(long lhs, long rhs) {
/* 531 */     if (!this.isEquals) {
/* 532 */       return this;
/*     */     }
/* 534 */     this.isEquals = (lhs == rhs);
/* 535 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public EqualsBuilder append(int lhs, int rhs) {
/* 546 */     if (!this.isEquals) {
/* 547 */       return this;
/*     */     }
/* 549 */     this.isEquals = (lhs == rhs);
/* 550 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public EqualsBuilder append(short lhs, short rhs) {
/* 561 */     if (!this.isEquals) {
/* 562 */       return this;
/*     */     }
/* 564 */     this.isEquals = (lhs == rhs);
/* 565 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public EqualsBuilder append(char lhs, char rhs) {
/* 576 */     if (!this.isEquals) {
/* 577 */       return this;
/*     */     }
/* 579 */     this.isEquals = (lhs == rhs);
/* 580 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public EqualsBuilder append(byte lhs, byte rhs) {
/* 591 */     if (!this.isEquals) {
/* 592 */       return this;
/*     */     }
/* 594 */     this.isEquals = (lhs == rhs);
/* 595 */     return this;
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
/*     */   public EqualsBuilder append(double lhs, double rhs) {
/* 612 */     if (!this.isEquals) {
/* 613 */       return this;
/*     */     }
/* 615 */     return append(Double.doubleToLongBits(lhs), Double.doubleToLongBits(rhs));
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
/*     */   public EqualsBuilder append(float lhs, float rhs) {
/* 632 */     if (!this.isEquals) {
/* 633 */       return this;
/*     */     }
/* 635 */     return append(Float.floatToIntBits(lhs), Float.floatToIntBits(rhs));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public EqualsBuilder append(boolean lhs, boolean rhs) {
/* 646 */     if (!this.isEquals) {
/* 647 */       return this;
/*     */     }
/* 649 */     this.isEquals = (lhs == rhs);
/* 650 */     return this;
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
/*     */   public EqualsBuilder append(Object[] lhs, Object[] rhs) {
/* 664 */     if (!this.isEquals) {
/* 665 */       return this;
/*     */     }
/* 667 */     if (lhs == rhs) {
/* 668 */       return this;
/*     */     }
/* 670 */     if (lhs == null || rhs == null) {
/* 671 */       setEquals(false);
/* 672 */       return this;
/*     */     } 
/* 674 */     if (lhs.length != rhs.length) {
/* 675 */       setEquals(false);
/* 676 */       return this;
/*     */     } 
/* 678 */     for (int i = 0; i < lhs.length && this.isEquals; i++) {
/* 679 */       append(lhs[i], rhs[i]);
/*     */     }
/* 681 */     return this;
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
/*     */   public EqualsBuilder append(long[] lhs, long[] rhs) {
/* 695 */     if (!this.isEquals) {
/* 696 */       return this;
/*     */     }
/* 698 */     if (lhs == rhs) {
/* 699 */       return this;
/*     */     }
/* 701 */     if (lhs == null || rhs == null) {
/* 702 */       setEquals(false);
/* 703 */       return this;
/*     */     } 
/* 705 */     if (lhs.length != rhs.length) {
/* 706 */       setEquals(false);
/* 707 */       return this;
/*     */     } 
/* 709 */     for (int i = 0; i < lhs.length && this.isEquals; i++) {
/* 710 */       append(lhs[i], rhs[i]);
/*     */     }
/* 712 */     return this;
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
/*     */   public EqualsBuilder append(int[] lhs, int[] rhs) {
/* 726 */     if (!this.isEquals) {
/* 727 */       return this;
/*     */     }
/* 729 */     if (lhs == rhs) {
/* 730 */       return this;
/*     */     }
/* 732 */     if (lhs == null || rhs == null) {
/* 733 */       setEquals(false);
/* 734 */       return this;
/*     */     } 
/* 736 */     if (lhs.length != rhs.length) {
/* 737 */       setEquals(false);
/* 738 */       return this;
/*     */     } 
/* 740 */     for (int i = 0; i < lhs.length && this.isEquals; i++) {
/* 741 */       append(lhs[i], rhs[i]);
/*     */     }
/* 743 */     return this;
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
/*     */   public EqualsBuilder append(short[] lhs, short[] rhs) {
/* 757 */     if (!this.isEquals) {
/* 758 */       return this;
/*     */     }
/* 760 */     if (lhs == rhs) {
/* 761 */       return this;
/*     */     }
/* 763 */     if (lhs == null || rhs == null) {
/* 764 */       setEquals(false);
/* 765 */       return this;
/*     */     } 
/* 767 */     if (lhs.length != rhs.length) {
/* 768 */       setEquals(false);
/* 769 */       return this;
/*     */     } 
/* 771 */     for (int i = 0; i < lhs.length && this.isEquals; i++) {
/* 772 */       append(lhs[i], rhs[i]);
/*     */     }
/* 774 */     return this;
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
/*     */   public EqualsBuilder append(char[] lhs, char[] rhs) {
/* 788 */     if (!this.isEquals) {
/* 789 */       return this;
/*     */     }
/* 791 */     if (lhs == rhs) {
/* 792 */       return this;
/*     */     }
/* 794 */     if (lhs == null || rhs == null) {
/* 795 */       setEquals(false);
/* 796 */       return this;
/*     */     } 
/* 798 */     if (lhs.length != rhs.length) {
/* 799 */       setEquals(false);
/* 800 */       return this;
/*     */     } 
/* 802 */     for (int i = 0; i < lhs.length && this.isEquals; i++) {
/* 803 */       append(lhs[i], rhs[i]);
/*     */     }
/* 805 */     return this;
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
/*     */   public EqualsBuilder append(byte[] lhs, byte[] rhs) {
/* 819 */     if (!this.isEquals) {
/* 820 */       return this;
/*     */     }
/* 822 */     if (lhs == rhs) {
/* 823 */       return this;
/*     */     }
/* 825 */     if (lhs == null || rhs == null) {
/* 826 */       setEquals(false);
/* 827 */       return this;
/*     */     } 
/* 829 */     if (lhs.length != rhs.length) {
/* 830 */       setEquals(false);
/* 831 */       return this;
/*     */     } 
/* 833 */     for (int i = 0; i < lhs.length && this.isEquals; i++) {
/* 834 */       append(lhs[i], rhs[i]);
/*     */     }
/* 836 */     return this;
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
/*     */   public EqualsBuilder append(double[] lhs, double[] rhs) {
/* 850 */     if (!this.isEquals) {
/* 851 */       return this;
/*     */     }
/* 853 */     if (lhs == rhs) {
/* 854 */       return this;
/*     */     }
/* 856 */     if (lhs == null || rhs == null) {
/* 857 */       setEquals(false);
/* 858 */       return this;
/*     */     } 
/* 860 */     if (lhs.length != rhs.length) {
/* 861 */       setEquals(false);
/* 862 */       return this;
/*     */     } 
/* 864 */     for (int i = 0; i < lhs.length && this.isEquals; i++) {
/* 865 */       append(lhs[i], rhs[i]);
/*     */     }
/* 867 */     return this;
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
/*     */   public EqualsBuilder append(float[] lhs, float[] rhs) {
/* 881 */     if (!this.isEquals) {
/* 882 */       return this;
/*     */     }
/* 884 */     if (lhs == rhs) {
/* 885 */       return this;
/*     */     }
/* 887 */     if (lhs == null || rhs == null) {
/* 888 */       setEquals(false);
/* 889 */       return this;
/*     */     } 
/* 891 */     if (lhs.length != rhs.length) {
/* 892 */       setEquals(false);
/* 893 */       return this;
/*     */     } 
/* 895 */     for (int i = 0; i < lhs.length && this.isEquals; i++) {
/* 896 */       append(lhs[i], rhs[i]);
/*     */     }
/* 898 */     return this;
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
/*     */   public EqualsBuilder append(boolean[] lhs, boolean[] rhs) {
/* 912 */     if (!this.isEquals) {
/* 913 */       return this;
/*     */     }
/* 915 */     if (lhs == rhs) {
/* 916 */       return this;
/*     */     }
/* 918 */     if (lhs == null || rhs == null) {
/* 919 */       setEquals(false);
/* 920 */       return this;
/*     */     } 
/* 922 */     if (lhs.length != rhs.length) {
/* 923 */       setEquals(false);
/* 924 */       return this;
/*     */     } 
/* 926 */     for (int i = 0; i < lhs.length && this.isEquals; i++) {
/* 927 */       append(lhs[i], rhs[i]);
/*     */     }
/* 929 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEquals() {
/* 939 */     return this.isEquals;
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
/*     */   public Boolean build() {
/* 953 */     return Boolean.valueOf(isEquals());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void setEquals(boolean isEquals) {
/* 963 */     this.isEquals = isEquals;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void reset() {
/* 971 */     this.isEquals = true;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\commons\lang3\builder\EqualsBuilder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */