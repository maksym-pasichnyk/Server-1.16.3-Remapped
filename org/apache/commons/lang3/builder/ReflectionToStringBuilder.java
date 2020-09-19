/*     */ package org.apache.commons.lang3.builder;
/*     */ 
/*     */ import java.lang.reflect.AccessibleObject;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import org.apache.commons.lang3.ArrayUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ReflectionToStringBuilder
/*     */   extends ToStringBuilder
/*     */ {
/*     */   public static String toString(Object object) {
/* 123 */     return toString(object, (ToStringStyle)null, false, false, (Class<? super Object>)null);
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
/*     */   public static String toString(Object object, ToStringStyle style) {
/* 157 */     return toString(object, style, false, false, (Class<? super Object>)null);
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
/*     */   public static String toString(Object object, ToStringStyle style, boolean outputTransients) {
/* 197 */     return toString(object, style, outputTransients, false, (Class<? super Object>)null);
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
/*     */   public static String toString(Object object, ToStringStyle style, boolean outputTransients, boolean outputStatics) {
/* 245 */     return toString(object, style, outputTransients, outputStatics, (Class<? super Object>)null);
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
/*     */   public static <T> String toString(T object, ToStringStyle style, boolean outputTransients, boolean outputStatics, Class<? super T> reflectUpToClass) {
/* 300 */     return (new ReflectionToStringBuilder(object, style, null, reflectUpToClass, outputTransients, outputStatics))
/* 301 */       .toString();
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
/*     */   public static String toStringExclude(Object object, Collection<String> excludeFieldNames) {
/* 314 */     return toStringExclude(object, toNoNullStringArray(excludeFieldNames));
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
/*     */   static String[] toNoNullStringArray(Collection<String> collection) {
/* 327 */     if (collection == null) {
/* 328 */       return ArrayUtils.EMPTY_STRING_ARRAY;
/*     */     }
/* 330 */     return toNoNullStringArray(collection.toArray());
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
/*     */   static String[] toNoNullStringArray(Object[] array) {
/* 343 */     List<String> list = new ArrayList<String>(array.length);
/* 344 */     for (Object e : array) {
/* 345 */       if (e != null) {
/* 346 */         list.add(e.toString());
/*     */       }
/*     */     } 
/* 349 */     return list.<String>toArray(ArrayUtils.EMPTY_STRING_ARRAY);
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
/*     */   public static String toStringExclude(Object object, String... excludeFieldNames) {
/* 363 */     return (new ReflectionToStringBuilder(object)).setExcludeFieldNames(excludeFieldNames).toString();
/*     */   }
/*     */   
/*     */   private static Object checkNotNull(Object obj) {
/* 367 */     if (obj == null) {
/* 368 */       throw new IllegalArgumentException("The Object passed in should not be null.");
/*     */     }
/* 370 */     return obj;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean appendStatics = false;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean appendTransients = false;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String[] excludeFieldNames;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 393 */   private Class<?> upToClass = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ReflectionToStringBuilder(Object object) {
/* 410 */     super(checkNotNull(object));
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
/*     */   public ReflectionToStringBuilder(Object object, ToStringStyle style) {
/* 430 */     super(checkNotNull(object), style);
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
/*     */   public ReflectionToStringBuilder(Object object, ToStringStyle style, StringBuffer buffer) {
/* 456 */     super(checkNotNull(object), style, buffer);
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
/*     */   public <T> ReflectionToStringBuilder(T object, ToStringStyle style, StringBuffer buffer, Class<? super T> reflectUpToClass, boolean outputTransients, boolean outputStatics) {
/* 481 */     super(checkNotNull(object), style, buffer);
/* 482 */     setUpToClass(reflectUpToClass);
/* 483 */     setAppendTransients(outputTransients);
/* 484 */     setAppendStatics(outputStatics);
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
/*     */   protected boolean accept(Field field) {
/* 500 */     if (field.getName().indexOf('$') != -1)
/*     */     {
/* 502 */       return false;
/*     */     }
/* 504 */     if (Modifier.isTransient(field.getModifiers()) && !isAppendTransients())
/*     */     {
/* 506 */       return false;
/*     */     }
/* 508 */     if (Modifier.isStatic(field.getModifiers()) && !isAppendStatics())
/*     */     {
/* 510 */       return false;
/*     */     }
/* 512 */     if (this.excludeFieldNames != null && 
/* 513 */       Arrays.binarySearch((Object[])this.excludeFieldNames, field.getName()) >= 0)
/*     */     {
/* 515 */       return false;
/*     */     }
/* 517 */     if (field.isAnnotationPresent((Class)ToStringExclude.class)) {
/* 518 */       return false;
/*     */     }
/* 520 */     return true;
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
/*     */   protected void appendFieldsIn(Class<?> clazz) {
/* 537 */     if (clazz.isArray()) {
/* 538 */       reflectionAppendArray(getObject());
/*     */       return;
/*     */     } 
/* 541 */     Field[] fields = clazz.getDeclaredFields();
/* 542 */     AccessibleObject.setAccessible((AccessibleObject[])fields, true);
/* 543 */     for (Field field : fields) {
/* 544 */       String fieldName = field.getName();
/* 545 */       if (accept(field)) {
/*     */         
/*     */         try {
/*     */           
/* 549 */           Object fieldValue = getValue(field);
/* 550 */           append(fieldName, fieldValue);
/* 551 */         } catch (IllegalAccessException ex) {
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 556 */           throw new InternalError("Unexpected IllegalAccessException: " + ex.getMessage());
/*     */         } 
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] getExcludeFieldNames() {
/* 566 */     return (String[])this.excludeFieldNames.clone();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Class<?> getUpToClass() {
/* 577 */     return this.upToClass;
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
/*     */   protected Object getValue(Field field) throws IllegalArgumentException, IllegalAccessException {
/* 597 */     return field.get(getObject());
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
/*     */   public boolean isAppendStatics() {
/* 609 */     return this.appendStatics;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isAppendTransients() {
/* 620 */     return this.appendTransients;
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
/*     */   public ReflectionToStringBuilder reflectionAppendArray(Object array) {
/* 633 */     getStyle().reflectionAppendArrayDetail(getStringBuffer(), null, array);
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
/*     */   public void setAppendStatics(boolean appendStatics) {
/* 647 */     this.appendStatics = appendStatics;
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
/*     */   public void setAppendTransients(boolean appendTransients) {
/* 659 */     this.appendTransients = appendTransients;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ReflectionToStringBuilder setExcludeFieldNames(String... excludeFieldNamesParam) {
/* 670 */     if (excludeFieldNamesParam == null) {
/* 671 */       this.excludeFieldNames = null;
/*     */     } else {
/*     */       
/* 674 */       this.excludeFieldNames = toNoNullStringArray((Object[])excludeFieldNamesParam);
/* 675 */       Arrays.sort((Object[])this.excludeFieldNames);
/*     */     } 
/* 677 */     return this;
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
/*     */   public void setUpToClass(Class<?> clazz) {
/* 689 */     if (clazz != null) {
/* 690 */       Object object = getObject();
/* 691 */       if (object != null && !clazz.isInstance(object)) {
/* 692 */         throw new IllegalArgumentException("Specified class is not a superclass of the object");
/*     */       }
/*     */     } 
/* 695 */     this.upToClass = clazz;
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
/*     */   public String toString() {
/* 707 */     if (getObject() == null) {
/* 708 */       return getStyle().getNullText();
/*     */     }
/* 710 */     Class<?> clazz = getObject().getClass();
/* 711 */     appendFieldsIn(clazz);
/* 712 */     while (clazz.getSuperclass() != null && clazz != getUpToClass()) {
/* 713 */       clazz = clazz.getSuperclass();
/* 714 */       appendFieldsIn(clazz);
/*     */     } 
/* 716 */     return super.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\commons\lang3\builder\ReflectionToStringBuilder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */