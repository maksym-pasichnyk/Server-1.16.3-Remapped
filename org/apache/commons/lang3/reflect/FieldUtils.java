/*     */ package org.apache.commons.lang3.reflect;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.apache.commons.lang3.ClassUtils;
/*     */ import org.apache.commons.lang3.StringUtils;
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
/*     */ public class FieldUtils
/*     */ {
/*     */   public static Field getField(Class<?> cls, String fieldName) {
/*  62 */     Field field = getField(cls, fieldName, false);
/*  63 */     MemberUtils.setAccessibleWorkaround(field);
/*  64 */     return field;
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
/*     */   public static Field getField(Class<?> cls, String fieldName, boolean forceAccess) {
/*  85 */     Validate.isTrue((cls != null), "The class must not be null", new Object[0]);
/*  86 */     Validate.isTrue(StringUtils.isNotBlank(fieldName), "The field name must not be blank/empty", new Object[0]);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 102 */     for (Class<?> acls = cls; acls != null; acls = acls.getSuperclass()) {
/*     */       try {
/* 104 */         Field field = acls.getDeclaredField(fieldName);
/*     */ 
/*     */         
/* 107 */         if (!Modifier.isPublic(field.getModifiers()))
/* 108 */         { if (forceAccess)
/* 109 */           { field.setAccessible(true);
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 114 */             return field; }  } else { return field; } 
/* 115 */       } catch (NoSuchFieldException noSuchFieldException) {}
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 122 */     Field match = null;
/* 123 */     for (Class<?> class1 : (Iterable<Class<?>>)ClassUtils.getAllInterfaces(cls)) {
/*     */       try {
/* 125 */         Field test = class1.getField(fieldName);
/* 126 */         Validate.isTrue((match == null), "Reference to field %s is ambiguous relative to %s; a matching field exists on two or more implemented interfaces.", new Object[] { fieldName, cls });
/*     */         
/* 128 */         match = test;
/* 129 */       } catch (NoSuchFieldException noSuchFieldException) {}
/*     */     } 
/*     */ 
/*     */     
/* 133 */     return match;
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
/*     */   public static Field getDeclaredField(Class<?> cls, String fieldName) {
/* 148 */     return getDeclaredField(cls, fieldName, false);
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
/*     */   public static Field getDeclaredField(Class<?> cls, String fieldName, boolean forceAccess) {
/* 168 */     Validate.isTrue((cls != null), "The class must not be null", new Object[0]);
/* 169 */     Validate.isTrue(StringUtils.isNotBlank(fieldName), "The field name must not be blank/empty", new Object[0]);
/*     */     
/*     */     try {
/* 172 */       Field field = cls.getDeclaredField(fieldName);
/* 173 */       if (!MemberUtils.isAccessible(field)) {
/* 174 */         if (forceAccess) {
/* 175 */           field.setAccessible(true);
/*     */         } else {
/* 177 */           return null;
/*     */         } 
/*     */       }
/* 180 */       return field;
/* 181 */     } catch (NoSuchFieldException noSuchFieldException) {
/*     */ 
/*     */       
/* 184 */       return null;
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
/*     */   public static Field[] getAllFields(Class<?> cls) {
/* 198 */     List<Field> allFieldsList = getAllFieldsList(cls);
/* 199 */     return allFieldsList.<Field>toArray(new Field[allFieldsList.size()]);
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
/*     */   public static List<Field> getAllFieldsList(Class<?> cls) {
/* 213 */     Validate.isTrue((cls != null), "The class must not be null", new Object[0]);
/* 214 */     List<Field> allFields = new ArrayList<Field>();
/* 215 */     Class<?> currentClass = cls;
/* 216 */     while (currentClass != null) {
/* 217 */       Field[] declaredFields = currentClass.getDeclaredFields();
/* 218 */       for (Field field : declaredFields) {
/* 219 */         allFields.add(field);
/*     */       }
/* 221 */       currentClass = currentClass.getSuperclass();
/*     */     } 
/* 223 */     return allFields;
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
/*     */   public static Field[] getFieldsWithAnnotation(Class<?> cls, Class<? extends Annotation> annotationCls) {
/* 238 */     List<Field> annotatedFieldsList = getFieldsListWithAnnotation(cls, annotationCls);
/* 239 */     return annotatedFieldsList.<Field>toArray(new Field[annotatedFieldsList.size()]);
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
/*     */   public static List<Field> getFieldsListWithAnnotation(Class<?> cls, Class<? extends Annotation> annotationCls) {
/* 254 */     Validate.isTrue((annotationCls != null), "The annotation class must not be null", new Object[0]);
/* 255 */     List<Field> allFields = getAllFieldsList(cls);
/* 256 */     List<Field> annotatedFields = new ArrayList<Field>();
/* 257 */     for (Field field : allFields) {
/* 258 */       if (field.getAnnotation(annotationCls) != null) {
/* 259 */         annotatedFields.add(field);
/*     */       }
/*     */     } 
/* 262 */     return annotatedFields;
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
/*     */   public static Object readStaticField(Field field) throws IllegalAccessException {
/* 277 */     return readStaticField(field, false);
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
/*     */   public static Object readStaticField(Field field, boolean forceAccess) throws IllegalAccessException {
/* 295 */     Validate.isTrue((field != null), "The field must not be null", new Object[0]);
/* 296 */     Validate.isTrue(Modifier.isStatic(field.getModifiers()), "The field '%s' is not static", new Object[] { field.getName() });
/* 297 */     return readField(field, (Object)null, forceAccess);
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
/*     */   public static Object readStaticField(Class<?> cls, String fieldName) throws IllegalAccessException {
/* 315 */     return readStaticField(cls, fieldName, false);
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
/*     */   public static Object readStaticField(Class<?> cls, String fieldName, boolean forceAccess) throws IllegalAccessException {
/* 337 */     Field field = getField(cls, fieldName, forceAccess);
/* 338 */     Validate.isTrue((field != null), "Cannot locate field '%s' on %s", new Object[] { fieldName, cls });
/*     */     
/* 340 */     return readStaticField(field, false);
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
/*     */   public static Object readDeclaredStaticField(Class<?> cls, String fieldName) throws IllegalAccessException {
/* 359 */     return readDeclaredStaticField(cls, fieldName, false);
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
/*     */   public static Object readDeclaredStaticField(Class<?> cls, String fieldName, boolean forceAccess) throws IllegalAccessException {
/* 381 */     Field field = getDeclaredField(cls, fieldName, forceAccess);
/* 382 */     Validate.isTrue((field != null), "Cannot locate declared field %s.%s", new Object[] { cls.getName(), fieldName });
/*     */     
/* 384 */     return readStaticField(field, false);
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
/*     */   public static Object readField(Field field, Object target) throws IllegalAccessException {
/* 401 */     return readField(field, target, false);
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
/*     */   public static Object readField(Field field, Object target, boolean forceAccess) throws IllegalAccessException {
/* 421 */     Validate.isTrue((field != null), "The field must not be null", new Object[0]);
/* 422 */     if (forceAccess && !field.isAccessible()) {
/* 423 */       field.setAccessible(true);
/*     */     } else {
/* 425 */       MemberUtils.setAccessibleWorkaround(field);
/*     */     } 
/* 427 */     return field.get(target);
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
/*     */   public static Object readField(Object target, String fieldName) throws IllegalAccessException {
/* 444 */     return readField(target, fieldName, false);
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
/*     */   public static Object readField(Object target, String fieldName, boolean forceAccess) throws IllegalAccessException {
/* 465 */     Validate.isTrue((target != null), "target object must not be null", new Object[0]);
/* 466 */     Class<?> cls = target.getClass();
/* 467 */     Field field = getField(cls, fieldName, forceAccess);
/* 468 */     Validate.isTrue((field != null), "Cannot locate field %s on %s", new Object[] { fieldName, cls });
/*     */     
/* 470 */     return readField(field, target, false);
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
/*     */   public static Object readDeclaredField(Object target, String fieldName) throws IllegalAccessException {
/* 487 */     return readDeclaredField(target, fieldName, false);
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
/*     */   public static Object readDeclaredField(Object target, String fieldName, boolean forceAccess) throws IllegalAccessException {
/* 508 */     Validate.isTrue((target != null), "target object must not be null", new Object[0]);
/* 509 */     Class<?> cls = target.getClass();
/* 510 */     Field field = getDeclaredField(cls, fieldName, forceAccess);
/* 511 */     Validate.isTrue((field != null), "Cannot locate declared field %s.%s", new Object[] { cls, fieldName });
/*     */     
/* 513 */     return readField(field, target, false);
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
/*     */   public static void writeStaticField(Field field, Object value) throws IllegalAccessException {
/* 529 */     writeStaticField(field, value, false);
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
/*     */   public static void writeStaticField(Field field, Object value, boolean forceAccess) throws IllegalAccessException {
/* 549 */     Validate.isTrue((field != null), "The field must not be null", new Object[0]);
/* 550 */     Validate.isTrue(Modifier.isStatic(field.getModifiers()), "The field %s.%s is not static", new Object[] { field.getDeclaringClass().getName(), field
/* 551 */           .getName() });
/* 552 */     writeField(field, (Object)null, value, forceAccess);
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
/*     */   public static void writeStaticField(Class<?> cls, String fieldName, Object value) throws IllegalAccessException {
/* 571 */     writeStaticField(cls, fieldName, value, false);
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
/*     */   public static void writeStaticField(Class<?> cls, String fieldName, Object value, boolean forceAccess) throws IllegalAccessException {
/* 595 */     Field field = getField(cls, fieldName, forceAccess);
/* 596 */     Validate.isTrue((field != null), "Cannot locate field %s on %s", new Object[] { fieldName, cls });
/*     */     
/* 598 */     writeStaticField(field, value, false);
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
/*     */   public static void writeDeclaredStaticField(Class<?> cls, String fieldName, Object value) throws IllegalAccessException {
/* 617 */     writeDeclaredStaticField(cls, fieldName, value, false);
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
/*     */   public static void writeDeclaredStaticField(Class<?> cls, String fieldName, Object value, boolean forceAccess) throws IllegalAccessException {
/* 640 */     Field field = getDeclaredField(cls, fieldName, forceAccess);
/* 641 */     Validate.isTrue((field != null), "Cannot locate declared field %s.%s", new Object[] { cls.getName(), fieldName });
/*     */     
/* 643 */     writeField(field, (Object)null, value, false);
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
/*     */   public static void writeField(Field field, Object target, Object value) throws IllegalAccessException {
/* 660 */     writeField(field, target, value, false);
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
/*     */   public static void writeField(Field field, Object target, Object value, boolean forceAccess) throws IllegalAccessException {
/* 683 */     Validate.isTrue((field != null), "The field must not be null", new Object[0]);
/* 684 */     if (forceAccess && !field.isAccessible()) {
/* 685 */       field.setAccessible(true);
/*     */     } else {
/* 687 */       MemberUtils.setAccessibleWorkaround(field);
/*     */     } 
/* 689 */     field.set(target, value);
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
/*     */   public static void removeFinalModifier(Field field) {
/* 702 */     removeFinalModifier(field, true);
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
/*     */   public static void removeFinalModifier(Field field, boolean forceAccess) {
/* 719 */     Validate.isTrue((field != null), "The field must not be null", new Object[0]);
/*     */     
/*     */     try {
/* 722 */       if (Modifier.isFinal(field.getModifiers())) {
/*     */         
/* 724 */         Field modifiersField = Field.class.getDeclaredField("modifiers");
/* 725 */         boolean doForceAccess = (forceAccess && !modifiersField.isAccessible());
/* 726 */         if (doForceAccess) {
/* 727 */           modifiersField.setAccessible(true);
/*     */         }
/*     */         try {
/* 730 */           modifiersField.setInt(field, field.getModifiers() & 0xFFFFFFEF);
/*     */         } finally {
/* 732 */           if (doForceAccess) {
/* 733 */             modifiersField.setAccessible(false);
/*     */           }
/*     */         } 
/*     */       } 
/* 737 */     } catch (NoSuchFieldException noSuchFieldException) {
/*     */     
/* 739 */     } catch (IllegalAccessException illegalAccessException) {}
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
/*     */   public static void writeField(Object target, String fieldName, Object value) throws IllegalAccessException {
/* 760 */     writeField(target, fieldName, value, false);
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
/*     */   public static void writeField(Object target, String fieldName, Object value, boolean forceAccess) throws IllegalAccessException {
/* 784 */     Validate.isTrue((target != null), "target object must not be null", new Object[0]);
/* 785 */     Class<?> cls = target.getClass();
/* 786 */     Field field = getField(cls, fieldName, forceAccess);
/* 787 */     Validate.isTrue((field != null), "Cannot locate declared field %s.%s", new Object[] { cls.getName(), fieldName });
/*     */     
/* 789 */     writeField(field, target, value, false);
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
/*     */   public static void writeDeclaredField(Object target, String fieldName, Object value) throws IllegalAccessException {
/* 808 */     writeDeclaredField(target, fieldName, value, false);
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
/*     */   public static void writeDeclaredField(Object target, String fieldName, Object value, boolean forceAccess) throws IllegalAccessException {
/* 832 */     Validate.isTrue((target != null), "target object must not be null", new Object[0]);
/* 833 */     Class<?> cls = target.getClass();
/* 834 */     Field field = getDeclaredField(cls, fieldName, forceAccess);
/* 835 */     Validate.isTrue((field != null), "Cannot locate declared field %s.%s", new Object[] { cls.getName(), fieldName });
/*     */     
/* 837 */     writeField(field, target, value, false);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\commons\lang3\reflect\FieldUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */