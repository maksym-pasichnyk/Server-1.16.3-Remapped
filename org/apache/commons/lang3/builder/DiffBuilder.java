/*      */ package org.apache.commons.lang3.builder;
/*      */ 
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.List;
/*      */ import org.apache.commons.lang3.ArrayUtils;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class DiffBuilder
/*      */   implements Builder<DiffResult>
/*      */ {
/*      */   private final List<Diff<?>> diffs;
/*      */   private final boolean objectsTriviallyEqual;
/*      */   private final Object left;
/*      */   private final Object right;
/*      */   private final ToStringStyle style;
/*      */   
/*      */   public DiffBuilder(Object lhs, Object rhs, ToStringStyle style, boolean testTriviallyEqual) {
/*  105 */     if (lhs == null) {
/*  106 */       throw new IllegalArgumentException("lhs cannot be null");
/*      */     }
/*  108 */     if (rhs == null) {
/*  109 */       throw new IllegalArgumentException("rhs cannot be null");
/*      */     }
/*      */     
/*  112 */     this.diffs = new ArrayList<Diff<?>>();
/*  113 */     this.left = lhs;
/*  114 */     this.right = rhs;
/*  115 */     this.style = style;
/*      */ 
/*      */     
/*  118 */     this.objectsTriviallyEqual = (testTriviallyEqual && (lhs == rhs || lhs.equals(rhs)));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public DiffBuilder(Object lhs, Object rhs, ToStringStyle style) {
/*  150 */     this(lhs, rhs, style, true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public DiffBuilder append(String fieldName, final boolean lhs, final boolean rhs) {
/*  170 */     if (fieldName == null) {
/*  171 */       throw new IllegalArgumentException("Field name cannot be null");
/*      */     }
/*      */     
/*  174 */     if (this.objectsTriviallyEqual) {
/*  175 */       return this;
/*      */     }
/*  177 */     if (lhs != rhs) {
/*  178 */       this.diffs.add(new Diff<Boolean>(fieldName)
/*      */           {
/*      */             private static final long serialVersionUID = 1L;
/*      */             
/*      */             public Boolean getLeft() {
/*  183 */               return Boolean.valueOf(lhs);
/*      */             }
/*      */ 
/*      */             
/*      */             public Boolean getRight() {
/*  188 */               return Boolean.valueOf(rhs);
/*      */             }
/*      */           });
/*      */     }
/*  192 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public DiffBuilder append(String fieldName, final boolean[] lhs, final boolean[] rhs) {
/*  212 */     if (fieldName == null) {
/*  213 */       throw new IllegalArgumentException("Field name cannot be null");
/*      */     }
/*  215 */     if (this.objectsTriviallyEqual) {
/*  216 */       return this;
/*      */     }
/*  218 */     if (!Arrays.equals(lhs, rhs)) {
/*  219 */       this.diffs.add(new Diff<Boolean[]>(fieldName)
/*      */           {
/*      */             private static final long serialVersionUID = 1L;
/*      */             
/*      */             public Boolean[] getLeft() {
/*  224 */               return ArrayUtils.toObject(lhs);
/*      */             }
/*      */ 
/*      */             
/*      */             public Boolean[] getRight() {
/*  229 */               return ArrayUtils.toObject(rhs);
/*      */             }
/*      */           });
/*      */     }
/*  233 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public DiffBuilder append(String fieldName, final byte lhs, final byte rhs) {
/*  253 */     if (fieldName == null) {
/*  254 */       throw new IllegalArgumentException("Field name cannot be null");
/*      */     }
/*  256 */     if (this.objectsTriviallyEqual) {
/*  257 */       return this;
/*      */     }
/*  259 */     if (lhs != rhs) {
/*  260 */       this.diffs.add(new Diff<Byte>(fieldName)
/*      */           {
/*      */             private static final long serialVersionUID = 1L;
/*      */             
/*      */             public Byte getLeft() {
/*  265 */               return Byte.valueOf(lhs);
/*      */             }
/*      */ 
/*      */             
/*      */             public Byte getRight() {
/*  270 */               return Byte.valueOf(rhs);
/*      */             }
/*      */           });
/*      */     }
/*  274 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public DiffBuilder append(String fieldName, final byte[] lhs, final byte[] rhs) {
/*  294 */     if (fieldName == null) {
/*  295 */       throw new IllegalArgumentException("Field name cannot be null");
/*      */     }
/*      */     
/*  298 */     if (this.objectsTriviallyEqual) {
/*  299 */       return this;
/*      */     }
/*  301 */     if (!Arrays.equals(lhs, rhs)) {
/*  302 */       this.diffs.add(new Diff<Byte[]>(fieldName)
/*      */           {
/*      */             private static final long serialVersionUID = 1L;
/*      */             
/*      */             public Byte[] getLeft() {
/*  307 */               return ArrayUtils.toObject(lhs);
/*      */             }
/*      */ 
/*      */             
/*      */             public Byte[] getRight() {
/*  312 */               return ArrayUtils.toObject(rhs);
/*      */             }
/*      */           });
/*      */     }
/*  316 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public DiffBuilder append(String fieldName, final char lhs, final char rhs) {
/*  336 */     if (fieldName == null) {
/*  337 */       throw new IllegalArgumentException("Field name cannot be null");
/*      */     }
/*      */     
/*  340 */     if (this.objectsTriviallyEqual) {
/*  341 */       return this;
/*      */     }
/*  343 */     if (lhs != rhs) {
/*  344 */       this.diffs.add(new Diff<Character>(fieldName)
/*      */           {
/*      */             private static final long serialVersionUID = 1L;
/*      */             
/*      */             public Character getLeft() {
/*  349 */               return Character.valueOf(lhs);
/*      */             }
/*      */ 
/*      */             
/*      */             public Character getRight() {
/*  354 */               return Character.valueOf(rhs);
/*      */             }
/*      */           });
/*      */     }
/*  358 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public DiffBuilder append(String fieldName, final char[] lhs, final char[] rhs) {
/*  378 */     if (fieldName == null) {
/*  379 */       throw new IllegalArgumentException("Field name cannot be null");
/*      */     }
/*      */     
/*  382 */     if (this.objectsTriviallyEqual) {
/*  383 */       return this;
/*      */     }
/*  385 */     if (!Arrays.equals(lhs, rhs)) {
/*  386 */       this.diffs.add(new Diff<Character[]>(fieldName)
/*      */           {
/*      */             private static final long serialVersionUID = 1L;
/*      */             
/*      */             public Character[] getLeft() {
/*  391 */               return ArrayUtils.toObject(lhs);
/*      */             }
/*      */ 
/*      */             
/*      */             public Character[] getRight() {
/*  396 */               return ArrayUtils.toObject(rhs);
/*      */             }
/*      */           });
/*      */     }
/*  400 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public DiffBuilder append(String fieldName, final double lhs, final double rhs) {
/*  420 */     if (fieldName == null) {
/*  421 */       throw new IllegalArgumentException("Field name cannot be null");
/*      */     }
/*      */     
/*  424 */     if (this.objectsTriviallyEqual) {
/*  425 */       return this;
/*      */     }
/*  427 */     if (Double.doubleToLongBits(lhs) != Double.doubleToLongBits(rhs)) {
/*  428 */       this.diffs.add(new Diff<Double>(fieldName)
/*      */           {
/*      */             private static final long serialVersionUID = 1L;
/*      */             
/*      */             public Double getLeft() {
/*  433 */               return Double.valueOf(lhs);
/*      */             }
/*      */ 
/*      */             
/*      */             public Double getRight() {
/*  438 */               return Double.valueOf(rhs);
/*      */             }
/*      */           });
/*      */     }
/*  442 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public DiffBuilder append(String fieldName, final double[] lhs, final double[] rhs) {
/*  462 */     if (fieldName == null) {
/*  463 */       throw new IllegalArgumentException("Field name cannot be null");
/*      */     }
/*      */     
/*  466 */     if (this.objectsTriviallyEqual) {
/*  467 */       return this;
/*      */     }
/*  469 */     if (!Arrays.equals(lhs, rhs)) {
/*  470 */       this.diffs.add(new Diff<Double[]>(fieldName)
/*      */           {
/*      */             private static final long serialVersionUID = 1L;
/*      */             
/*      */             public Double[] getLeft() {
/*  475 */               return ArrayUtils.toObject(lhs);
/*      */             }
/*      */ 
/*      */             
/*      */             public Double[] getRight() {
/*  480 */               return ArrayUtils.toObject(rhs);
/*      */             }
/*      */           });
/*      */     }
/*  484 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public DiffBuilder append(String fieldName, final float lhs, final float rhs) {
/*  504 */     if (fieldName == null) {
/*  505 */       throw new IllegalArgumentException("Field name cannot be null");
/*      */     }
/*      */     
/*  508 */     if (this.objectsTriviallyEqual) {
/*  509 */       return this;
/*      */     }
/*  511 */     if (Float.floatToIntBits(lhs) != Float.floatToIntBits(rhs)) {
/*  512 */       this.diffs.add(new Diff<Float>(fieldName)
/*      */           {
/*      */             private static final long serialVersionUID = 1L;
/*      */             
/*      */             public Float getLeft() {
/*  517 */               return Float.valueOf(lhs);
/*      */             }
/*      */ 
/*      */             
/*      */             public Float getRight() {
/*  522 */               return Float.valueOf(rhs);
/*      */             }
/*      */           });
/*      */     }
/*  526 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public DiffBuilder append(String fieldName, final float[] lhs, final float[] rhs) {
/*  546 */     if (fieldName == null) {
/*  547 */       throw new IllegalArgumentException("Field name cannot be null");
/*      */     }
/*      */     
/*  550 */     if (this.objectsTriviallyEqual) {
/*  551 */       return this;
/*      */     }
/*  553 */     if (!Arrays.equals(lhs, rhs)) {
/*  554 */       this.diffs.add(new Diff<Float[]>(fieldName)
/*      */           {
/*      */             private static final long serialVersionUID = 1L;
/*      */             
/*      */             public Float[] getLeft() {
/*  559 */               return ArrayUtils.toObject(lhs);
/*      */             }
/*      */ 
/*      */             
/*      */             public Float[] getRight() {
/*  564 */               return ArrayUtils.toObject(rhs);
/*      */             }
/*      */           });
/*      */     }
/*  568 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public DiffBuilder append(String fieldName, final int lhs, final int rhs) {
/*  588 */     if (fieldName == null) {
/*  589 */       throw new IllegalArgumentException("Field name cannot be null");
/*      */     }
/*      */     
/*  592 */     if (this.objectsTriviallyEqual) {
/*  593 */       return this;
/*      */     }
/*  595 */     if (lhs != rhs) {
/*  596 */       this.diffs.add(new Diff<Integer>(fieldName)
/*      */           {
/*      */             private static final long serialVersionUID = 1L;
/*      */             
/*      */             public Integer getLeft() {
/*  601 */               return Integer.valueOf(lhs);
/*      */             }
/*      */ 
/*      */             
/*      */             public Integer getRight() {
/*  606 */               return Integer.valueOf(rhs);
/*      */             }
/*      */           });
/*      */     }
/*  610 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public DiffBuilder append(String fieldName, final int[] lhs, final int[] rhs) {
/*  630 */     if (fieldName == null) {
/*  631 */       throw new IllegalArgumentException("Field name cannot be null");
/*      */     }
/*      */     
/*  634 */     if (this.objectsTriviallyEqual) {
/*  635 */       return this;
/*      */     }
/*  637 */     if (!Arrays.equals(lhs, rhs)) {
/*  638 */       this.diffs.add(new Diff<Integer[]>(fieldName)
/*      */           {
/*      */             private static final long serialVersionUID = 1L;
/*      */             
/*      */             public Integer[] getLeft() {
/*  643 */               return ArrayUtils.toObject(lhs);
/*      */             }
/*      */ 
/*      */             
/*      */             public Integer[] getRight() {
/*  648 */               return ArrayUtils.toObject(rhs);
/*      */             }
/*      */           });
/*      */     }
/*  652 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public DiffBuilder append(String fieldName, final long lhs, final long rhs) {
/*  672 */     if (fieldName == null) {
/*  673 */       throw new IllegalArgumentException("Field name cannot be null");
/*      */     }
/*      */     
/*  676 */     if (this.objectsTriviallyEqual) {
/*  677 */       return this;
/*      */     }
/*  679 */     if (lhs != rhs) {
/*  680 */       this.diffs.add(new Diff<Long>(fieldName)
/*      */           {
/*      */             private static final long serialVersionUID = 1L;
/*      */             
/*      */             public Long getLeft() {
/*  685 */               return Long.valueOf(lhs);
/*      */             }
/*      */ 
/*      */             
/*      */             public Long getRight() {
/*  690 */               return Long.valueOf(rhs);
/*      */             }
/*      */           });
/*      */     }
/*  694 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public DiffBuilder append(String fieldName, final long[] lhs, final long[] rhs) {
/*  714 */     if (fieldName == null) {
/*  715 */       throw new IllegalArgumentException("Field name cannot be null");
/*      */     }
/*      */     
/*  718 */     if (this.objectsTriviallyEqual) {
/*  719 */       return this;
/*      */     }
/*  721 */     if (!Arrays.equals(lhs, rhs)) {
/*  722 */       this.diffs.add(new Diff<Long[]>(fieldName)
/*      */           {
/*      */             private static final long serialVersionUID = 1L;
/*      */             
/*      */             public Long[] getLeft() {
/*  727 */               return ArrayUtils.toObject(lhs);
/*      */             }
/*      */ 
/*      */             
/*      */             public Long[] getRight() {
/*  732 */               return ArrayUtils.toObject(rhs);
/*      */             }
/*      */           });
/*      */     }
/*  736 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public DiffBuilder append(String fieldName, final short lhs, final short rhs) {
/*  756 */     if (fieldName == null) {
/*  757 */       throw new IllegalArgumentException("Field name cannot be null");
/*      */     }
/*      */     
/*  760 */     if (this.objectsTriviallyEqual) {
/*  761 */       return this;
/*      */     }
/*  763 */     if (lhs != rhs) {
/*  764 */       this.diffs.add(new Diff<Short>(fieldName)
/*      */           {
/*      */             private static final long serialVersionUID = 1L;
/*      */             
/*      */             public Short getLeft() {
/*  769 */               return Short.valueOf(lhs);
/*      */             }
/*      */ 
/*      */             
/*      */             public Short getRight() {
/*  774 */               return Short.valueOf(rhs);
/*      */             }
/*      */           });
/*      */     }
/*  778 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public DiffBuilder append(String fieldName, final short[] lhs, final short[] rhs) {
/*  798 */     if (fieldName == null) {
/*  799 */       throw new IllegalArgumentException("Field name cannot be null");
/*      */     }
/*      */     
/*  802 */     if (this.objectsTriviallyEqual) {
/*  803 */       return this;
/*      */     }
/*  805 */     if (!Arrays.equals(lhs, rhs)) {
/*  806 */       this.diffs.add(new Diff<Short[]>(fieldName)
/*      */           {
/*      */             private static final long serialVersionUID = 1L;
/*      */             
/*      */             public Short[] getLeft() {
/*  811 */               return ArrayUtils.toObject(lhs);
/*      */             }
/*      */ 
/*      */             
/*      */             public Short[] getRight() {
/*  816 */               return ArrayUtils.toObject(rhs);
/*      */             }
/*      */           });
/*      */     }
/*  820 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public DiffBuilder append(String fieldName, final Object lhs, final Object rhs) {
/*      */     Object objectToTest;
/*  840 */     if (fieldName == null) {
/*  841 */       throw new IllegalArgumentException("Field name cannot be null");
/*      */     }
/*  843 */     if (this.objectsTriviallyEqual) {
/*  844 */       return this;
/*      */     }
/*  846 */     if (lhs == rhs) {
/*  847 */       return this;
/*      */     }
/*      */ 
/*      */     
/*  851 */     if (lhs != null) {
/*  852 */       objectToTest = lhs;
/*      */     } else {
/*      */       
/*  855 */       objectToTest = rhs;
/*      */     } 
/*      */     
/*  858 */     if (objectToTest.getClass().isArray()) {
/*  859 */       if (objectToTest instanceof boolean[]) {
/*  860 */         return append(fieldName, (boolean[])lhs, (boolean[])rhs);
/*      */       }
/*  862 */       if (objectToTest instanceof byte[]) {
/*  863 */         return append(fieldName, (byte[])lhs, (byte[])rhs);
/*      */       }
/*  865 */       if (objectToTest instanceof char[]) {
/*  866 */         return append(fieldName, (char[])lhs, (char[])rhs);
/*      */       }
/*  868 */       if (objectToTest instanceof double[]) {
/*  869 */         return append(fieldName, (double[])lhs, (double[])rhs);
/*      */       }
/*  871 */       if (objectToTest instanceof float[]) {
/*  872 */         return append(fieldName, (float[])lhs, (float[])rhs);
/*      */       }
/*  874 */       if (objectToTest instanceof int[]) {
/*  875 */         return append(fieldName, (int[])lhs, (int[])rhs);
/*      */       }
/*  877 */       if (objectToTest instanceof long[]) {
/*  878 */         return append(fieldName, (long[])lhs, (long[])rhs);
/*      */       }
/*  880 */       if (objectToTest instanceof short[]) {
/*  881 */         return append(fieldName, (short[])lhs, (short[])rhs);
/*      */       }
/*      */       
/*  884 */       return append(fieldName, (Object[])lhs, (Object[])rhs);
/*      */     } 
/*      */ 
/*      */     
/*  888 */     if (lhs != null && lhs.equals(rhs)) {
/*  889 */       return this;
/*      */     }
/*      */     
/*  892 */     this.diffs.add(new Diff(fieldName)
/*      */         {
/*      */           private static final long serialVersionUID = 1L;
/*      */           
/*      */           public Object getLeft() {
/*  897 */             return lhs;
/*      */           }
/*      */ 
/*      */           
/*      */           public Object getRight() {
/*  902 */             return rhs;
/*      */           }
/*      */         });
/*      */     
/*  906 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public DiffBuilder append(String fieldName, final Object[] lhs, final Object[] rhs) {
/*  926 */     if (fieldName == null) {
/*  927 */       throw new IllegalArgumentException("Field name cannot be null");
/*      */     }
/*  929 */     if (this.objectsTriviallyEqual) {
/*  930 */       return this;
/*      */     }
/*      */     
/*  933 */     if (!Arrays.equals(lhs, rhs)) {
/*  934 */       this.diffs.add(new Diff<Object[]>(fieldName)
/*      */           {
/*      */             private static final long serialVersionUID = 1L;
/*      */             
/*      */             public Object[] getLeft() {
/*  939 */               return lhs;
/*      */             }
/*      */ 
/*      */             
/*      */             public Object[] getRight() {
/*  944 */               return rhs;
/*      */             }
/*      */           });
/*      */     }
/*      */     
/*  949 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public DiffBuilder append(String fieldName, DiffResult diffResult) {
/*  990 */     if (fieldName == null) {
/*  991 */       throw new IllegalArgumentException("Field name cannot be null");
/*      */     }
/*  993 */     if (diffResult == null) {
/*  994 */       throw new IllegalArgumentException("Diff result cannot be null");
/*      */     }
/*  996 */     if (this.objectsTriviallyEqual) {
/*  997 */       return this;
/*      */     }
/*      */     
/* 1000 */     for (Diff<?> diff : diffResult.getDiffs()) {
/* 1001 */       append(fieldName + "." + diff.getFieldName(), diff
/* 1002 */           .getLeft(), diff.getRight());
/*      */     }
/*      */     
/* 1005 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public DiffResult build() {
/* 1019 */     return new DiffResult(this.left, this.right, this.diffs, this.style);
/*      */   }
/*      */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\commons\lang3\builder\DiffBuilder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */