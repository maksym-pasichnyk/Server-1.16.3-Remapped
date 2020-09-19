/*      */ package org.apache.commons.lang3.builder;
/*      */ 
/*      */ import org.apache.commons.lang3.ObjectUtils;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class ToStringBuilder
/*      */   implements Builder<String>
/*      */ {
/*   93 */   private static volatile ToStringStyle defaultStyle = ToStringStyle.DEFAULT_STYLE;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final StringBuffer buffer;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final Object object;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final ToStringStyle style;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static ToStringStyle getDefaultStyle() {
/*  116 */     return defaultStyle;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void setDefaultStyle(ToStringStyle style) {
/*  135 */     if (style == null) {
/*  136 */       throw new IllegalArgumentException("The style must not be null");
/*      */     }
/*  138 */     defaultStyle = style;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String reflectionToString(Object object) {
/*  151 */     return ReflectionToStringBuilder.toString(object);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String reflectionToString(Object object, ToStringStyle style) {
/*  164 */     return ReflectionToStringBuilder.toString(object, style);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String reflectionToString(Object object, ToStringStyle style, boolean outputTransients) {
/*  178 */     return ReflectionToStringBuilder.toString(object, style, outputTransients, false, null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T> String reflectionToString(T object, ToStringStyle style, boolean outputTransients, Class<? super T> reflectUpToClass) {
/*  199 */     return ReflectionToStringBuilder.toString(object, style, outputTransients, false, reflectUpToClass);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ToStringBuilder(Object object) {
/*  225 */     this(object, null, null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ToStringBuilder(Object object, ToStringStyle style) {
/*  237 */     this(object, style, null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ToStringBuilder(Object object, ToStringStyle style, StringBuffer buffer) {
/*  252 */     if (style == null) {
/*  253 */       style = getDefaultStyle();
/*      */     }
/*  255 */     if (buffer == null) {
/*  256 */       buffer = new StringBuffer(512);
/*      */     }
/*  258 */     this.buffer = buffer;
/*  259 */     this.style = style;
/*  260 */     this.object = object;
/*      */     
/*  262 */     style.appendStart(buffer, object);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ToStringBuilder append(boolean value) {
/*  275 */     this.style.append(this.buffer, (String)null, value);
/*  276 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ToStringBuilder append(boolean[] array) {
/*  289 */     this.style.append(this.buffer, (String)null, array, (Boolean)null);
/*  290 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ToStringBuilder append(byte value) {
/*  303 */     this.style.append(this.buffer, (String)null, value);
/*  304 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ToStringBuilder append(byte[] array) {
/*  317 */     this.style.append(this.buffer, (String)null, array, (Boolean)null);
/*  318 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ToStringBuilder append(char value) {
/*  331 */     this.style.append(this.buffer, (String)null, value);
/*  332 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ToStringBuilder append(char[] array) {
/*  345 */     this.style.append(this.buffer, (String)null, array, (Boolean)null);
/*  346 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ToStringBuilder append(double value) {
/*  359 */     this.style.append(this.buffer, (String)null, value);
/*  360 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ToStringBuilder append(double[] array) {
/*  373 */     this.style.append(this.buffer, (String)null, array, (Boolean)null);
/*  374 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ToStringBuilder append(float value) {
/*  387 */     this.style.append(this.buffer, (String)null, value);
/*  388 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ToStringBuilder append(float[] array) {
/*  401 */     this.style.append(this.buffer, (String)null, array, (Boolean)null);
/*  402 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ToStringBuilder append(int value) {
/*  415 */     this.style.append(this.buffer, (String)null, value);
/*  416 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ToStringBuilder append(int[] array) {
/*  429 */     this.style.append(this.buffer, (String)null, array, (Boolean)null);
/*  430 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ToStringBuilder append(long value) {
/*  443 */     this.style.append(this.buffer, (String)null, value);
/*  444 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ToStringBuilder append(long[] array) {
/*  457 */     this.style.append(this.buffer, (String)null, array, (Boolean)null);
/*  458 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ToStringBuilder append(Object obj) {
/*  471 */     this.style.append(this.buffer, (String)null, obj, (Boolean)null);
/*  472 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ToStringBuilder append(Object[] array) {
/*  485 */     this.style.append(this.buffer, (String)null, array, (Boolean)null);
/*  486 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ToStringBuilder append(short value) {
/*  499 */     this.style.append(this.buffer, (String)null, value);
/*  500 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ToStringBuilder append(short[] array) {
/*  513 */     this.style.append(this.buffer, (String)null, array, (Boolean)null);
/*  514 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ToStringBuilder append(String fieldName, boolean value) {
/*  526 */     this.style.append(this.buffer, fieldName, value);
/*  527 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ToStringBuilder append(String fieldName, boolean[] array) {
/*  539 */     this.style.append(this.buffer, fieldName, array, (Boolean)null);
/*  540 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ToStringBuilder append(String fieldName, boolean[] array, boolean fullDetail) {
/*  559 */     this.style.append(this.buffer, fieldName, array, Boolean.valueOf(fullDetail));
/*  560 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ToStringBuilder append(String fieldName, byte value) {
/*  572 */     this.style.append(this.buffer, fieldName, value);
/*  573 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ToStringBuilder append(String fieldName, byte[] array) {
/*  584 */     this.style.append(this.buffer, fieldName, array, (Boolean)null);
/*  585 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ToStringBuilder append(String fieldName, byte[] array, boolean fullDetail) {
/*  604 */     this.style.append(this.buffer, fieldName, array, Boolean.valueOf(fullDetail));
/*  605 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ToStringBuilder append(String fieldName, char value) {
/*  617 */     this.style.append(this.buffer, fieldName, value);
/*  618 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ToStringBuilder append(String fieldName, char[] array) {
/*  630 */     this.style.append(this.buffer, fieldName, array, (Boolean)null);
/*  631 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ToStringBuilder append(String fieldName, char[] array, boolean fullDetail) {
/*  650 */     this.style.append(this.buffer, fieldName, array, Boolean.valueOf(fullDetail));
/*  651 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ToStringBuilder append(String fieldName, double value) {
/*  663 */     this.style.append(this.buffer, fieldName, value);
/*  664 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ToStringBuilder append(String fieldName, double[] array) {
/*  676 */     this.style.append(this.buffer, fieldName, array, (Boolean)null);
/*  677 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ToStringBuilder append(String fieldName, double[] array, boolean fullDetail) {
/*  696 */     this.style.append(this.buffer, fieldName, array, Boolean.valueOf(fullDetail));
/*  697 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ToStringBuilder append(String fieldName, float value) {
/*  709 */     this.style.append(this.buffer, fieldName, value);
/*  710 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ToStringBuilder append(String fieldName, float[] array) {
/*  722 */     this.style.append(this.buffer, fieldName, array, (Boolean)null);
/*  723 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ToStringBuilder append(String fieldName, float[] array, boolean fullDetail) {
/*  742 */     this.style.append(this.buffer, fieldName, array, Boolean.valueOf(fullDetail));
/*  743 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ToStringBuilder append(String fieldName, int value) {
/*  755 */     this.style.append(this.buffer, fieldName, value);
/*  756 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ToStringBuilder append(String fieldName, int[] array) {
/*  768 */     this.style.append(this.buffer, fieldName, array, (Boolean)null);
/*  769 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ToStringBuilder append(String fieldName, int[] array, boolean fullDetail) {
/*  788 */     this.style.append(this.buffer, fieldName, array, Boolean.valueOf(fullDetail));
/*  789 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ToStringBuilder append(String fieldName, long value) {
/*  801 */     this.style.append(this.buffer, fieldName, value);
/*  802 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ToStringBuilder append(String fieldName, long[] array) {
/*  814 */     this.style.append(this.buffer, fieldName, array, (Boolean)null);
/*  815 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ToStringBuilder append(String fieldName, long[] array, boolean fullDetail) {
/*  834 */     this.style.append(this.buffer, fieldName, array, Boolean.valueOf(fullDetail));
/*  835 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ToStringBuilder append(String fieldName, Object obj) {
/*  847 */     this.style.append(this.buffer, fieldName, obj, (Boolean)null);
/*  848 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ToStringBuilder append(String fieldName, Object obj, boolean fullDetail) {
/*  862 */     this.style.append(this.buffer, fieldName, obj, Boolean.valueOf(fullDetail));
/*  863 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ToStringBuilder append(String fieldName, Object[] array) {
/*  875 */     this.style.append(this.buffer, fieldName, array, (Boolean)null);
/*  876 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ToStringBuilder append(String fieldName, Object[] array, boolean fullDetail) {
/*  895 */     this.style.append(this.buffer, fieldName, array, Boolean.valueOf(fullDetail));
/*  896 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ToStringBuilder append(String fieldName, short value) {
/*  908 */     this.style.append(this.buffer, fieldName, value);
/*  909 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ToStringBuilder append(String fieldName, short[] array) {
/*  921 */     this.style.append(this.buffer, fieldName, array, (Boolean)null);
/*  922 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ToStringBuilder append(String fieldName, short[] array, boolean fullDetail) {
/*  941 */     this.style.append(this.buffer, fieldName, array, Boolean.valueOf(fullDetail));
/*  942 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ToStringBuilder appendAsObjectToString(Object srcObject) {
/*  955 */     ObjectUtils.identityToString(getStringBuffer(), srcObject);
/*  956 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ToStringBuilder appendSuper(String superToString) {
/*  974 */     if (superToString != null) {
/*  975 */       this.style.appendSuper(this.buffer, superToString);
/*      */     }
/*  977 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ToStringBuilder appendToString(String toString) {
/* 1008 */     if (toString != null) {
/* 1009 */       this.style.appendToString(this.buffer, toString);
/*      */     }
/* 1011 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object getObject() {
/* 1021 */     return this.object;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StringBuffer getStringBuffer() {
/* 1030 */     return this.buffer;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ToStringStyle getStyle() {
/* 1042 */     return this.style;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String toString() {
/* 1057 */     if (getObject() == null) {
/* 1058 */       getStringBuffer().append(getStyle().getNullText());
/*      */     } else {
/* 1060 */       this.style.appendEnd(getStringBuffer(), getObject());
/*      */     } 
/* 1062 */     return getStringBuffer().toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String build() {
/* 1077 */     return toString();
/*      */   }
/*      */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\commons\lang3\builder\ToStringBuilder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */