/*     */ package org.apache.commons.lang3;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.Arrays;
/*     */ import org.apache.commons.lang3.builder.ToStringBuilder;
/*     */ import org.apache.commons.lang3.builder.ToStringStyle;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AnnotationUtils
/*     */ {
/*  50 */   private static final ToStringStyle TO_STRING_STYLE = new ToStringStyle()
/*     */     {
/*     */       private static final long serialVersionUID = 1L;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       protected String getShortClassName(Class<?> cls) {
/*  72 */         Class<? extends Annotation> annotationType = null;
/*  73 */         for (Class<?> iface : ClassUtils.getAllInterfaces(cls)) {
/*  74 */           if (Annotation.class.isAssignableFrom(iface)) {
/*     */ 
/*     */             
/*  77 */             Class<? extends Annotation> found = (Class)iface;
/*  78 */             annotationType = found;
/*     */             break;
/*     */           } 
/*     */         } 
/*  82 */         return (new StringBuilder((annotationType == null) ? "" : annotationType.getName()))
/*  83 */           .insert(0, '@').toString();
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       protected void appendDetail(StringBuffer buffer, String fieldName, Object value) {
/*  91 */         if (value instanceof Annotation) {
/*  92 */           value = AnnotationUtils.toString((Annotation)value);
/*     */         }
/*  94 */         super.appendDetail(buffer, fieldName, value);
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean equals(Annotation a1, Annotation a2) {
/* 122 */     if (a1 == a2) {
/* 123 */       return true;
/*     */     }
/* 125 */     if (a1 == null || a2 == null) {
/* 126 */       return false;
/*     */     }
/* 128 */     Class<? extends Annotation> type = a1.annotationType();
/* 129 */     Class<? extends Annotation> type2 = a2.annotationType();
/* 130 */     Validate.notNull(type, "Annotation %s with null annotationType()", new Object[] { a1 });
/* 131 */     Validate.notNull(type2, "Annotation %s with null annotationType()", new Object[] { a2 });
/* 132 */     if (!type.equals(type2)) {
/* 133 */       return false;
/*     */     }
/*     */     try {
/* 136 */       for (Method m : type.getDeclaredMethods()) {
/* 137 */         if ((m.getParameterTypes()).length == 0 && 
/* 138 */           isValidAnnotationMemberType(m.getReturnType())) {
/* 139 */           Object v1 = m.invoke(a1, new Object[0]);
/* 140 */           Object v2 = m.invoke(a2, new Object[0]);
/* 141 */           if (!memberEquals(m.getReturnType(), v1, v2)) {
/* 142 */             return false;
/*     */           }
/*     */         } 
/*     */       } 
/* 146 */     } catch (IllegalAccessException ex) {
/* 147 */       return false;
/* 148 */     } catch (InvocationTargetException ex) {
/* 149 */       return false;
/*     */     } 
/* 151 */     return true;
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
/*     */   public static int hashCode(Annotation a) {
/* 167 */     int result = 0;
/* 168 */     Class<? extends Annotation> type = a.annotationType();
/* 169 */     for (Method m : type.getDeclaredMethods()) {
/*     */       try {
/* 171 */         Object value = m.invoke(a, new Object[0]);
/* 172 */         if (value == null) {
/* 173 */           throw new IllegalStateException(
/* 174 */               String.format("Annotation method %s returned null", new Object[] { m }));
/*     */         }
/* 176 */         result += hashMember(m.getName(), value);
/* 177 */       } catch (RuntimeException ex) {
/* 178 */         throw ex;
/* 179 */       } catch (Exception ex) {
/* 180 */         throw new RuntimeException(ex);
/*     */       } 
/*     */     } 
/* 183 */     return result;
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
/*     */   public static String toString(Annotation a) {
/* 195 */     ToStringBuilder builder = new ToStringBuilder(a, TO_STRING_STYLE);
/* 196 */     for (Method m : a.annotationType().getDeclaredMethods()) {
/* 197 */       if ((m.getParameterTypes()).length <= 0)
/*     */         
/*     */         try {
/*     */           
/* 201 */           builder.append(m.getName(), m.invoke(a, new Object[0]));
/* 202 */         } catch (RuntimeException ex) {
/* 203 */           throw ex;
/* 204 */         } catch (Exception ex) {
/* 205 */           throw new RuntimeException(ex);
/*     */         }  
/*     */     } 
/* 208 */     return builder.build();
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
/*     */   public static boolean isValidAnnotationMemberType(Class<?> type) {
/* 223 */     if (type == null) {
/* 224 */       return false;
/*     */     }
/* 226 */     if (type.isArray()) {
/* 227 */       type = type.getComponentType();
/*     */     }
/* 229 */     return (type.isPrimitive() || type.isEnum() || type.isAnnotation() || String.class
/* 230 */       .equals(type) || Class.class.equals(type));
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
/*     */   private static int hashMember(String name, Object value) {
/* 242 */     int part1 = name.hashCode() * 127;
/* 243 */     if (value.getClass().isArray()) {
/* 244 */       return part1 ^ arrayMemberHash(value.getClass().getComponentType(), value);
/*     */     }
/* 246 */     if (value instanceof Annotation) {
/* 247 */       return part1 ^ hashCode((Annotation)value);
/*     */     }
/* 249 */     return part1 ^ value.hashCode();
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
/*     */   private static boolean memberEquals(Class<?> type, Object o1, Object o2) {
/* 263 */     if (o1 == o2) {
/* 264 */       return true;
/*     */     }
/* 266 */     if (o1 == null || o2 == null) {
/* 267 */       return false;
/*     */     }
/* 269 */     if (type.isArray()) {
/* 270 */       return arrayMemberEquals(type.getComponentType(), o1, o2);
/*     */     }
/* 272 */     if (type.isAnnotation()) {
/* 273 */       return equals((Annotation)o1, (Annotation)o2);
/*     */     }
/* 275 */     return o1.equals(o2);
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
/*     */   private static boolean arrayMemberEquals(Class<?> componentType, Object o1, Object o2) {
/* 287 */     if (componentType.isAnnotation()) {
/* 288 */       return annotationArrayMemberEquals((Annotation[])o1, (Annotation[])o2);
/*     */     }
/* 290 */     if (componentType.equals(byte.class)) {
/* 291 */       return Arrays.equals((byte[])o1, (byte[])o2);
/*     */     }
/* 293 */     if (componentType.equals(short.class)) {
/* 294 */       return Arrays.equals((short[])o1, (short[])o2);
/*     */     }
/* 296 */     if (componentType.equals(int.class)) {
/* 297 */       return Arrays.equals((int[])o1, (int[])o2);
/*     */     }
/* 299 */     if (componentType.equals(char.class)) {
/* 300 */       return Arrays.equals((char[])o1, (char[])o2);
/*     */     }
/* 302 */     if (componentType.equals(long.class)) {
/* 303 */       return Arrays.equals((long[])o1, (long[])o2);
/*     */     }
/* 305 */     if (componentType.equals(float.class)) {
/* 306 */       return Arrays.equals((float[])o1, (float[])o2);
/*     */     }
/* 308 */     if (componentType.equals(double.class)) {
/* 309 */       return Arrays.equals((double[])o1, (double[])o2);
/*     */     }
/* 311 */     if (componentType.equals(boolean.class)) {
/* 312 */       return Arrays.equals((boolean[])o1, (boolean[])o2);
/*     */     }
/* 314 */     return Arrays.equals((Object[])o1, (Object[])o2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean annotationArrayMemberEquals(Annotation[] a1, Annotation[] a2) {
/* 325 */     if (a1.length != a2.length) {
/* 326 */       return false;
/*     */     }
/* 328 */     for (int i = 0; i < a1.length; i++) {
/* 329 */       if (!equals(a1[i], a2[i])) {
/* 330 */         return false;
/*     */       }
/*     */     } 
/* 333 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int arrayMemberHash(Class<?> componentType, Object o) {
/* 344 */     if (componentType.equals(byte.class)) {
/* 345 */       return Arrays.hashCode((byte[])o);
/*     */     }
/* 347 */     if (componentType.equals(short.class)) {
/* 348 */       return Arrays.hashCode((short[])o);
/*     */     }
/* 350 */     if (componentType.equals(int.class)) {
/* 351 */       return Arrays.hashCode((int[])o);
/*     */     }
/* 353 */     if (componentType.equals(char.class)) {
/* 354 */       return Arrays.hashCode((char[])o);
/*     */     }
/* 356 */     if (componentType.equals(long.class)) {
/* 357 */       return Arrays.hashCode((long[])o);
/*     */     }
/* 359 */     if (componentType.equals(float.class)) {
/* 360 */       return Arrays.hashCode((float[])o);
/*     */     }
/* 362 */     if (componentType.equals(double.class)) {
/* 363 */       return Arrays.hashCode((double[])o);
/*     */     }
/* 365 */     if (componentType.equals(boolean.class)) {
/* 366 */       return Arrays.hashCode((boolean[])o);
/*     */     }
/* 368 */     return Arrays.hashCode((Object[])o);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\commons\lang3\AnnotationUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */