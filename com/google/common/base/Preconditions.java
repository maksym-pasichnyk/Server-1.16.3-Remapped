/*      */ package com.google.common.base;
/*      */ 
/*      */ import com.google.common.annotations.GwtCompatible;
/*      */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*      */ import javax.annotation.Nullable;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ @GwtCompatible
/*      */ public final class Preconditions
/*      */ {
/*      */   public static void checkArgument(boolean expression) {
/*  107 */     if (!expression) {
/*  108 */       throw new IllegalArgumentException();
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
/*      */   public static void checkArgument(boolean expression, @Nullable Object errorMessage) {
/*  121 */     if (!expression) {
/*  122 */       throw new IllegalArgumentException(String.valueOf(errorMessage));
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
/*      */   public static void checkArgument(boolean expression, @Nullable String errorMessageTemplate, @Nullable Object... errorMessageArgs) {
/*  145 */     if (!expression) {
/*  146 */       throw new IllegalArgumentException(format(errorMessageTemplate, errorMessageArgs));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void checkArgument(boolean b, @Nullable String errorMessageTemplate, char p1) {
/*  156 */     if (!b) {
/*  157 */       throw new IllegalArgumentException(format(errorMessageTemplate, new Object[] { Character.valueOf(p1) }));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void checkArgument(boolean b, @Nullable String errorMessageTemplate, int p1) {
/*  167 */     if (!b) {
/*  168 */       throw new IllegalArgumentException(format(errorMessageTemplate, new Object[] { Integer.valueOf(p1) }));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void checkArgument(boolean b, @Nullable String errorMessageTemplate, long p1) {
/*  178 */     if (!b) {
/*  179 */       throw new IllegalArgumentException(format(errorMessageTemplate, new Object[] { Long.valueOf(p1) }));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void checkArgument(boolean b, @Nullable String errorMessageTemplate, @Nullable Object p1) {
/*  190 */     if (!b) {
/*  191 */       throw new IllegalArgumentException(format(errorMessageTemplate, new Object[] { p1 }));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void checkArgument(boolean b, @Nullable String errorMessageTemplate, char p1, char p2) {
/*  202 */     if (!b) {
/*  203 */       throw new IllegalArgumentException(format(errorMessageTemplate, new Object[] { Character.valueOf(p1), Character.valueOf(p2) }));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void checkArgument(boolean b, @Nullable String errorMessageTemplate, char p1, int p2) {
/*  214 */     if (!b) {
/*  215 */       throw new IllegalArgumentException(format(errorMessageTemplate, new Object[] { Character.valueOf(p1), Integer.valueOf(p2) }));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void checkArgument(boolean b, @Nullable String errorMessageTemplate, char p1, long p2) {
/*  226 */     if (!b) {
/*  227 */       throw new IllegalArgumentException(format(errorMessageTemplate, new Object[] { Character.valueOf(p1), Long.valueOf(p2) }));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void checkArgument(boolean b, @Nullable String errorMessageTemplate, char p1, @Nullable Object p2) {
/*  238 */     if (!b) {
/*  239 */       throw new IllegalArgumentException(format(errorMessageTemplate, new Object[] { Character.valueOf(p1), p2 }));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void checkArgument(boolean b, @Nullable String errorMessageTemplate, int p1, char p2) {
/*  250 */     if (!b) {
/*  251 */       throw new IllegalArgumentException(format(errorMessageTemplate, new Object[] { Integer.valueOf(p1), Character.valueOf(p2) }));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void checkArgument(boolean b, @Nullable String errorMessageTemplate, int p1, int p2) {
/*  262 */     if (!b) {
/*  263 */       throw new IllegalArgumentException(format(errorMessageTemplate, new Object[] { Integer.valueOf(p1), Integer.valueOf(p2) }));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void checkArgument(boolean b, @Nullable String errorMessageTemplate, int p1, long p2) {
/*  274 */     if (!b) {
/*  275 */       throw new IllegalArgumentException(format(errorMessageTemplate, new Object[] { Integer.valueOf(p1), Long.valueOf(p2) }));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void checkArgument(boolean b, @Nullable String errorMessageTemplate, int p1, @Nullable Object p2) {
/*  286 */     if (!b) {
/*  287 */       throw new IllegalArgumentException(format(errorMessageTemplate, new Object[] { Integer.valueOf(p1), p2 }));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void checkArgument(boolean b, @Nullable String errorMessageTemplate, long p1, char p2) {
/*  298 */     if (!b) {
/*  299 */       throw new IllegalArgumentException(format(errorMessageTemplate, new Object[] { Long.valueOf(p1), Character.valueOf(p2) }));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void checkArgument(boolean b, @Nullable String errorMessageTemplate, long p1, int p2) {
/*  310 */     if (!b) {
/*  311 */       throw new IllegalArgumentException(format(errorMessageTemplate, new Object[] { Long.valueOf(p1), Integer.valueOf(p2) }));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void checkArgument(boolean b, @Nullable String errorMessageTemplate, long p1, long p2) {
/*  322 */     if (!b) {
/*  323 */       throw new IllegalArgumentException(format(errorMessageTemplate, new Object[] { Long.valueOf(p1), Long.valueOf(p2) }));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void checkArgument(boolean b, @Nullable String errorMessageTemplate, long p1, @Nullable Object p2) {
/*  334 */     if (!b) {
/*  335 */       throw new IllegalArgumentException(format(errorMessageTemplate, new Object[] { Long.valueOf(p1), p2 }));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void checkArgument(boolean b, @Nullable String errorMessageTemplate, @Nullable Object p1, char p2) {
/*  346 */     if (!b) {
/*  347 */       throw new IllegalArgumentException(format(errorMessageTemplate, new Object[] { p1, Character.valueOf(p2) }));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void checkArgument(boolean b, @Nullable String errorMessageTemplate, @Nullable Object p1, int p2) {
/*  358 */     if (!b) {
/*  359 */       throw new IllegalArgumentException(format(errorMessageTemplate, new Object[] { p1, Integer.valueOf(p2) }));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void checkArgument(boolean b, @Nullable String errorMessageTemplate, @Nullable Object p1, long p2) {
/*  370 */     if (!b) {
/*  371 */       throw new IllegalArgumentException(format(errorMessageTemplate, new Object[] { p1, Long.valueOf(p2) }));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void checkArgument(boolean b, @Nullable String errorMessageTemplate, @Nullable Object p1, @Nullable Object p2) {
/*  382 */     if (!b) {
/*  383 */       throw new IllegalArgumentException(format(errorMessageTemplate, new Object[] { p1, p2 }));
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
/*      */   public static void checkArgument(boolean b, @Nullable String errorMessageTemplate, @Nullable Object p1, @Nullable Object p2, @Nullable Object p3) {
/*  398 */     if (!b) {
/*  399 */       throw new IllegalArgumentException(format(errorMessageTemplate, new Object[] { p1, p2, p3 }));
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
/*      */   public static void checkArgument(boolean b, @Nullable String errorMessageTemplate, @Nullable Object p1, @Nullable Object p2, @Nullable Object p3, @Nullable Object p4) {
/*  415 */     if (!b) {
/*  416 */       throw new IllegalArgumentException(format(errorMessageTemplate, new Object[] { p1, p2, p3, p4 }));
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
/*      */   public static void checkState(boolean expression) {
/*  428 */     if (!expression) {
/*  429 */       throw new IllegalStateException();
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
/*      */   public static void checkState(boolean expression, @Nullable Object errorMessage) {
/*  443 */     if (!expression) {
/*  444 */       throw new IllegalStateException(String.valueOf(errorMessage));
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
/*      */   public static void checkState(boolean expression, @Nullable String errorMessageTemplate, @Nullable Object... errorMessageArgs) {
/*  468 */     if (!expression) {
/*  469 */       throw new IllegalStateException(format(errorMessageTemplate, errorMessageArgs));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void checkState(boolean b, @Nullable String errorMessageTemplate, char p1) {
/*  480 */     if (!b) {
/*  481 */       throw new IllegalStateException(format(errorMessageTemplate, new Object[] { Character.valueOf(p1) }));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void checkState(boolean b, @Nullable String errorMessageTemplate, int p1) {
/*  492 */     if (!b) {
/*  493 */       throw new IllegalStateException(format(errorMessageTemplate, new Object[] { Integer.valueOf(p1) }));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void checkState(boolean b, @Nullable String errorMessageTemplate, long p1) {
/*  504 */     if (!b) {
/*  505 */       throw new IllegalStateException(format(errorMessageTemplate, new Object[] { Long.valueOf(p1) }));
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
/*      */   public static void checkState(boolean b, @Nullable String errorMessageTemplate, @Nullable Object p1) {
/*  517 */     if (!b) {
/*  518 */       throw new IllegalStateException(format(errorMessageTemplate, new Object[] { p1 }));
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
/*      */   public static void checkState(boolean b, @Nullable String errorMessageTemplate, char p1, char p2) {
/*  530 */     if (!b) {
/*  531 */       throw new IllegalStateException(format(errorMessageTemplate, new Object[] { Character.valueOf(p1), Character.valueOf(p2) }));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void checkState(boolean b, @Nullable String errorMessageTemplate, char p1, int p2) {
/*  542 */     if (!b) {
/*  543 */       throw new IllegalStateException(format(errorMessageTemplate, new Object[] { Character.valueOf(p1), Integer.valueOf(p2) }));
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
/*      */   public static void checkState(boolean b, @Nullable String errorMessageTemplate, char p1, long p2) {
/*  555 */     if (!b) {
/*  556 */       throw new IllegalStateException(format(errorMessageTemplate, new Object[] { Character.valueOf(p1), Long.valueOf(p2) }));
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
/*      */   public static void checkState(boolean b, @Nullable String errorMessageTemplate, char p1, @Nullable Object p2) {
/*  568 */     if (!b) {
/*  569 */       throw new IllegalStateException(format(errorMessageTemplate, new Object[] { Character.valueOf(p1), p2 }));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void checkState(boolean b, @Nullable String errorMessageTemplate, int p1, char p2) {
/*  580 */     if (!b) {
/*  581 */       throw new IllegalStateException(format(errorMessageTemplate, new Object[] { Integer.valueOf(p1), Character.valueOf(p2) }));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void checkState(boolean b, @Nullable String errorMessageTemplate, int p1, int p2) {
/*  592 */     if (!b) {
/*  593 */       throw new IllegalStateException(format(errorMessageTemplate, new Object[] { Integer.valueOf(p1), Integer.valueOf(p2) }));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void checkState(boolean b, @Nullable String errorMessageTemplate, int p1, long p2) {
/*  604 */     if (!b) {
/*  605 */       throw new IllegalStateException(format(errorMessageTemplate, new Object[] { Integer.valueOf(p1), Long.valueOf(p2) }));
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
/*      */   public static void checkState(boolean b, @Nullable String errorMessageTemplate, int p1, @Nullable Object p2) {
/*  617 */     if (!b) {
/*  618 */       throw new IllegalStateException(format(errorMessageTemplate, new Object[] { Integer.valueOf(p1), p2 }));
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
/*      */   public static void checkState(boolean b, @Nullable String errorMessageTemplate, long p1, char p2) {
/*  630 */     if (!b) {
/*  631 */       throw new IllegalStateException(format(errorMessageTemplate, new Object[] { Long.valueOf(p1), Character.valueOf(p2) }));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void checkState(boolean b, @Nullable String errorMessageTemplate, long p1, int p2) {
/*  642 */     if (!b) {
/*  643 */       throw new IllegalStateException(format(errorMessageTemplate, new Object[] { Long.valueOf(p1), Integer.valueOf(p2) }));
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
/*      */   public static void checkState(boolean b, @Nullable String errorMessageTemplate, long p1, long p2) {
/*  655 */     if (!b) {
/*  656 */       throw new IllegalStateException(format(errorMessageTemplate, new Object[] { Long.valueOf(p1), Long.valueOf(p2) }));
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
/*      */   public static void checkState(boolean b, @Nullable String errorMessageTemplate, long p1, @Nullable Object p2) {
/*  668 */     if (!b) {
/*  669 */       throw new IllegalStateException(format(errorMessageTemplate, new Object[] { Long.valueOf(p1), p2 }));
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
/*      */   public static void checkState(boolean b, @Nullable String errorMessageTemplate, @Nullable Object p1, char p2) {
/*  681 */     if (!b) {
/*  682 */       throw new IllegalStateException(format(errorMessageTemplate, new Object[] { p1, Character.valueOf(p2) }));
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
/*      */   public static void checkState(boolean b, @Nullable String errorMessageTemplate, @Nullable Object p1, int p2) {
/*  694 */     if (!b) {
/*  695 */       throw new IllegalStateException(format(errorMessageTemplate, new Object[] { p1, Integer.valueOf(p2) }));
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
/*      */   public static void checkState(boolean b, @Nullable String errorMessageTemplate, @Nullable Object p1, long p2) {
/*  707 */     if (!b) {
/*  708 */       throw new IllegalStateException(format(errorMessageTemplate, new Object[] { p1, Long.valueOf(p2) }));
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
/*      */   public static void checkState(boolean b, @Nullable String errorMessageTemplate, @Nullable Object p1, @Nullable Object p2) {
/*  720 */     if (!b) {
/*  721 */       throw new IllegalStateException(format(errorMessageTemplate, new Object[] { p1, p2 }));
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
/*      */   public static void checkState(boolean b, @Nullable String errorMessageTemplate, @Nullable Object p1, @Nullable Object p2, @Nullable Object p3) {
/*  737 */     if (!b) {
/*  738 */       throw new IllegalStateException(format(errorMessageTemplate, new Object[] { p1, p2, p3 }));
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
/*      */   public static void checkState(boolean b, @Nullable String errorMessageTemplate, @Nullable Object p1, @Nullable Object p2, @Nullable Object p3, @Nullable Object p4) {
/*  755 */     if (!b) {
/*  756 */       throw new IllegalStateException(format(errorMessageTemplate, new Object[] { p1, p2, p3, p4 }));
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
/*      */   @CanIgnoreReturnValue
/*      */   public static <T> T checkNotNull(T reference) {
/*  769 */     if (reference == null) {
/*  770 */       throw new NullPointerException();
/*      */     }
/*  772 */     return reference;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @CanIgnoreReturnValue
/*      */   public static <T> T checkNotNull(T reference, @Nullable Object errorMessage) {
/*  786 */     if (reference == null) {
/*  787 */       throw new NullPointerException(String.valueOf(errorMessage));
/*      */     }
/*  789 */     return reference;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @CanIgnoreReturnValue
/*      */   public static <T> T checkNotNull(T reference, @Nullable String errorMessageTemplate, @Nullable Object... errorMessageArgs) {
/*  809 */     if (reference == null)
/*      */     {
/*  811 */       throw new NullPointerException(format(errorMessageTemplate, errorMessageArgs));
/*      */     }
/*  813 */     return reference;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @CanIgnoreReturnValue
/*      */   public static <T> T checkNotNull(T obj, @Nullable String errorMessageTemplate, char p1) {
/*  823 */     if (obj == null) {
/*  824 */       throw new NullPointerException(format(errorMessageTemplate, new Object[] { Character.valueOf(p1) }));
/*      */     }
/*  826 */     return obj;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @CanIgnoreReturnValue
/*      */   public static <T> T checkNotNull(T obj, @Nullable String errorMessageTemplate, int p1) {
/*  836 */     if (obj == null) {
/*  837 */       throw new NullPointerException(format(errorMessageTemplate, new Object[] { Integer.valueOf(p1) }));
/*      */     }
/*  839 */     return obj;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @CanIgnoreReturnValue
/*      */   public static <T> T checkNotNull(T obj, @Nullable String errorMessageTemplate, long p1) {
/*  849 */     if (obj == null) {
/*  850 */       throw new NullPointerException(format(errorMessageTemplate, new Object[] { Long.valueOf(p1) }));
/*      */     }
/*  852 */     return obj;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @CanIgnoreReturnValue
/*      */   public static <T> T checkNotNull(T obj, @Nullable String errorMessageTemplate, @Nullable Object p1) {
/*  863 */     if (obj == null) {
/*  864 */       throw new NullPointerException(format(errorMessageTemplate, new Object[] { p1 }));
/*      */     }
/*  866 */     return obj;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @CanIgnoreReturnValue
/*      */   public static <T> T checkNotNull(T obj, @Nullable String errorMessageTemplate, char p1, char p2) {
/*  876 */     if (obj == null) {
/*  877 */       throw new NullPointerException(format(errorMessageTemplate, new Object[] { Character.valueOf(p1), Character.valueOf(p2) }));
/*      */     }
/*  879 */     return obj;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @CanIgnoreReturnValue
/*      */   public static <T> T checkNotNull(T obj, @Nullable String errorMessageTemplate, char p1, int p2) {
/*  889 */     if (obj == null) {
/*  890 */       throw new NullPointerException(format(errorMessageTemplate, new Object[] { Character.valueOf(p1), Integer.valueOf(p2) }));
/*      */     }
/*  892 */     return obj;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @CanIgnoreReturnValue
/*      */   public static <T> T checkNotNull(T obj, @Nullable String errorMessageTemplate, char p1, long p2) {
/*  902 */     if (obj == null) {
/*  903 */       throw new NullPointerException(format(errorMessageTemplate, new Object[] { Character.valueOf(p1), Long.valueOf(p2) }));
/*      */     }
/*  905 */     return obj;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @CanIgnoreReturnValue
/*      */   public static <T> T checkNotNull(T obj, @Nullable String errorMessageTemplate, char p1, @Nullable Object p2) {
/*  916 */     if (obj == null) {
/*  917 */       throw new NullPointerException(format(errorMessageTemplate, new Object[] { Character.valueOf(p1), p2 }));
/*      */     }
/*  919 */     return obj;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @CanIgnoreReturnValue
/*      */   public static <T> T checkNotNull(T obj, @Nullable String errorMessageTemplate, int p1, char p2) {
/*  929 */     if (obj == null) {
/*  930 */       throw new NullPointerException(format(errorMessageTemplate, new Object[] { Integer.valueOf(p1), Character.valueOf(p2) }));
/*      */     }
/*  932 */     return obj;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @CanIgnoreReturnValue
/*      */   public static <T> T checkNotNull(T obj, @Nullable String errorMessageTemplate, int p1, int p2) {
/*  942 */     if (obj == null) {
/*  943 */       throw new NullPointerException(format(errorMessageTemplate, new Object[] { Integer.valueOf(p1), Integer.valueOf(p2) }));
/*      */     }
/*  945 */     return obj;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @CanIgnoreReturnValue
/*      */   public static <T> T checkNotNull(T obj, @Nullable String errorMessageTemplate, int p1, long p2) {
/*  955 */     if (obj == null) {
/*  956 */       throw new NullPointerException(format(errorMessageTemplate, new Object[] { Integer.valueOf(p1), Long.valueOf(p2) }));
/*      */     }
/*  958 */     return obj;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @CanIgnoreReturnValue
/*      */   public static <T> T checkNotNull(T obj, @Nullable String errorMessageTemplate, int p1, @Nullable Object p2) {
/*  969 */     if (obj == null) {
/*  970 */       throw new NullPointerException(format(errorMessageTemplate, new Object[] { Integer.valueOf(p1), p2 }));
/*      */     }
/*  972 */     return obj;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @CanIgnoreReturnValue
/*      */   public static <T> T checkNotNull(T obj, @Nullable String errorMessageTemplate, long p1, char p2) {
/*  982 */     if (obj == null) {
/*  983 */       throw new NullPointerException(format(errorMessageTemplate, new Object[] { Long.valueOf(p1), Character.valueOf(p2) }));
/*      */     }
/*  985 */     return obj;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @CanIgnoreReturnValue
/*      */   public static <T> T checkNotNull(T obj, @Nullable String errorMessageTemplate, long p1, int p2) {
/*  995 */     if (obj == null) {
/*  996 */       throw new NullPointerException(format(errorMessageTemplate, new Object[] { Long.valueOf(p1), Integer.valueOf(p2) }));
/*      */     }
/*  998 */     return obj;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @CanIgnoreReturnValue
/*      */   public static <T> T checkNotNull(T obj, @Nullable String errorMessageTemplate, long p1, long p2) {
/* 1008 */     if (obj == null) {
/* 1009 */       throw new NullPointerException(format(errorMessageTemplate, new Object[] { Long.valueOf(p1), Long.valueOf(p2) }));
/*      */     }
/* 1011 */     return obj;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @CanIgnoreReturnValue
/*      */   public static <T> T checkNotNull(T obj, @Nullable String errorMessageTemplate, long p1, @Nullable Object p2) {
/* 1022 */     if (obj == null) {
/* 1023 */       throw new NullPointerException(format(errorMessageTemplate, new Object[] { Long.valueOf(p1), p2 }));
/*      */     }
/* 1025 */     return obj;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @CanIgnoreReturnValue
/*      */   public static <T> T checkNotNull(T obj, @Nullable String errorMessageTemplate, @Nullable Object p1, char p2) {
/* 1036 */     if (obj == null) {
/* 1037 */       throw new NullPointerException(format(errorMessageTemplate, new Object[] { p1, Character.valueOf(p2) }));
/*      */     }
/* 1039 */     return obj;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @CanIgnoreReturnValue
/*      */   public static <T> T checkNotNull(T obj, @Nullable String errorMessageTemplate, @Nullable Object p1, int p2) {
/* 1050 */     if (obj == null) {
/* 1051 */       throw new NullPointerException(format(errorMessageTemplate, new Object[] { p1, Integer.valueOf(p2) }));
/*      */     }
/* 1053 */     return obj;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @CanIgnoreReturnValue
/*      */   public static <T> T checkNotNull(T obj, @Nullable String errorMessageTemplate, @Nullable Object p1, long p2) {
/* 1064 */     if (obj == null) {
/* 1065 */       throw new NullPointerException(format(errorMessageTemplate, new Object[] { p1, Long.valueOf(p2) }));
/*      */     }
/* 1067 */     return obj;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @CanIgnoreReturnValue
/*      */   public static <T> T checkNotNull(T obj, @Nullable String errorMessageTemplate, @Nullable Object p1, @Nullable Object p2) {
/* 1078 */     if (obj == null) {
/* 1079 */       throw new NullPointerException(format(errorMessageTemplate, new Object[] { p1, p2 }));
/*      */     }
/* 1081 */     return obj;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @CanIgnoreReturnValue
/*      */   public static <T> T checkNotNull(T obj, @Nullable String errorMessageTemplate, @Nullable Object p1, @Nullable Object p2, @Nullable Object p3) {
/* 1096 */     if (obj == null) {
/* 1097 */       throw new NullPointerException(format(errorMessageTemplate, new Object[] { p1, p2, p3 }));
/*      */     }
/* 1099 */     return obj;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @CanIgnoreReturnValue
/*      */   public static <T> T checkNotNull(T obj, @Nullable String errorMessageTemplate, @Nullable Object p1, @Nullable Object p2, @Nullable Object p3, @Nullable Object p4) {
/* 1115 */     if (obj == null) {
/* 1116 */       throw new NullPointerException(format(errorMessageTemplate, new Object[] { p1, p2, p3, p4 }));
/*      */     }
/* 1118 */     return obj;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @CanIgnoreReturnValue
/*      */   public static int checkElementIndex(int index, int size) {
/* 1159 */     return checkElementIndex(index, size, "index");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @CanIgnoreReturnValue
/*      */   public static int checkElementIndex(int index, int size, @Nullable String desc) {
/* 1176 */     if (index < 0 || index >= size) {
/* 1177 */       throw new IndexOutOfBoundsException(badElementIndex(index, size, desc));
/*      */     }
/* 1179 */     return index;
/*      */   }
/*      */   
/*      */   private static String badElementIndex(int index, int size, String desc) {
/* 1183 */     if (index < 0)
/* 1184 */       return format("%s (%s) must not be negative", new Object[] { desc, Integer.valueOf(index) }); 
/* 1185 */     if (size < 0) {
/* 1186 */       throw new IllegalArgumentException("negative size: " + size);
/*      */     }
/* 1188 */     return format("%s (%s) must be less than size (%s)", new Object[] { desc, Integer.valueOf(index), Integer.valueOf(size) });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @CanIgnoreReturnValue
/*      */   public static int checkPositionIndex(int index, int size) {
/* 1204 */     return checkPositionIndex(index, size, "index");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @CanIgnoreReturnValue
/*      */   public static int checkPositionIndex(int index, int size, @Nullable String desc) {
/* 1221 */     if (index < 0 || index > size) {
/* 1222 */       throw new IndexOutOfBoundsException(badPositionIndex(index, size, desc));
/*      */     }
/* 1224 */     return index;
/*      */   }
/*      */   
/*      */   private static String badPositionIndex(int index, int size, String desc) {
/* 1228 */     if (index < 0)
/* 1229 */       return format("%s (%s) must not be negative", new Object[] { desc, Integer.valueOf(index) }); 
/* 1230 */     if (size < 0) {
/* 1231 */       throw new IllegalArgumentException("negative size: " + size);
/*      */     }
/* 1233 */     return format("%s (%s) must not be greater than size (%s)", new Object[] { desc, Integer.valueOf(index), Integer.valueOf(size) });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void checkPositionIndexes(int start, int end, int size) {
/* 1251 */     if (start < 0 || end < start || end > size) {
/* 1252 */       throw new IndexOutOfBoundsException(badPositionIndexes(start, end, size));
/*      */     }
/*      */   }
/*      */   
/*      */   private static String badPositionIndexes(int start, int end, int size) {
/* 1257 */     if (start < 0 || start > size) {
/* 1258 */       return badPositionIndex(start, size, "start index");
/*      */     }
/* 1260 */     if (end < 0 || end > size) {
/* 1261 */       return badPositionIndex(end, size, "end index");
/*      */     }
/*      */     
/* 1264 */     return format("end index (%s) must not be less than start index (%s)", new Object[] { Integer.valueOf(end), Integer.valueOf(start) });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static String format(String template, @Nullable Object... args) {
/* 1279 */     template = String.valueOf(template);
/*      */ 
/*      */     
/* 1282 */     StringBuilder builder = new StringBuilder(template.length() + 16 * args.length);
/* 1283 */     int templateStart = 0;
/* 1284 */     int i = 0;
/* 1285 */     while (i < args.length) {
/* 1286 */       int placeholderStart = template.indexOf("%s", templateStart);
/* 1287 */       if (placeholderStart == -1) {
/*      */         break;
/*      */       }
/* 1290 */       builder.append(template, templateStart, placeholderStart);
/* 1291 */       builder.append(args[i++]);
/* 1292 */       templateStart = placeholderStart + 2;
/*      */     } 
/* 1294 */     builder.append(template, templateStart, template.length());
/*      */ 
/*      */     
/* 1297 */     if (i < args.length) {
/* 1298 */       builder.append(" [");
/* 1299 */       builder.append(args[i++]);
/* 1300 */       while (i < args.length) {
/* 1301 */         builder.append(", ");
/* 1302 */         builder.append(args[i++]);
/*      */       } 
/* 1304 */       builder.append(']');
/*      */     } 
/*      */     
/* 1307 */     return builder.toString();
/*      */   }
/*      */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\base\Preconditions.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */