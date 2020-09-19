/*     */ package org.apache.logging.log4j.core.config.plugins.convert;
/*     */ 
/*     */ import java.lang.reflect.ParameterizedType;
/*     */ import java.lang.reflect.Type;
/*     */ import java.util.Collection;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.UnknownFormatConversionException;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.apache.logging.log4j.core.config.plugins.util.PluginManager;
/*     */ import org.apache.logging.log4j.core.config.plugins.util.PluginType;
/*     */ import org.apache.logging.log4j.core.util.ReflectionUtil;
/*     */ import org.apache.logging.log4j.core.util.TypeUtil;
/*     */ import org.apache.logging.log4j.status.StatusLogger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TypeConverterRegistry
/*     */ {
/*  42 */   private static final Logger LOGGER = (Logger)StatusLogger.getLogger();
/*     */   private static volatile TypeConverterRegistry INSTANCE;
/*  44 */   private static final Object INSTANCE_LOCK = new Object();
/*     */   
/*  46 */   private final ConcurrentMap<Type, TypeConverter<?>> registry = new ConcurrentHashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static TypeConverterRegistry getInstance() {
/*  54 */     TypeConverterRegistry result = INSTANCE;
/*  55 */     if (result == null) {
/*  56 */       synchronized (INSTANCE_LOCK) {
/*  57 */         result = INSTANCE;
/*  58 */         if (result == null) {
/*  59 */           INSTANCE = result = new TypeConverterRegistry();
/*     */         }
/*     */       } 
/*     */     }
/*  63 */     return result;
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
/*     */   public TypeConverter<?> findCompatibleConverter(Type type) {
/*  77 */     Objects.requireNonNull(type, "No type was provided");
/*  78 */     TypeConverter<?> primary = this.registry.get(type);
/*     */     
/*  80 */     if (primary != null) {
/*  81 */       return primary;
/*     */     }
/*     */     
/*  84 */     if (type instanceof Class) {
/*  85 */       Class<?> clazz = (Class)type;
/*  86 */       if (clazz.isEnum()) {
/*     */         
/*  88 */         EnumConverter<? extends Enum> converter = new EnumConverter<>(clazz.asSubclass(Enum.class));
/*  89 */         this.registry.putIfAbsent(type, converter);
/*  90 */         return converter;
/*     */       } 
/*     */     } 
/*     */     
/*  94 */     for (Map.Entry<Type, TypeConverter<?>> entry : this.registry.entrySet()) {
/*  95 */       Type key = entry.getKey();
/*  96 */       if (TypeUtil.isAssignable(type, key)) {
/*  97 */         LOGGER.debug("Found compatible TypeConverter<{}> for type [{}].", key, type);
/*  98 */         TypeConverter<?> value = entry.getValue();
/*  99 */         this.registry.putIfAbsent(type, value);
/* 100 */         return value;
/*     */       } 
/*     */     } 
/* 103 */     throw new UnknownFormatConversionException(type.toString());
/*     */   }
/*     */   
/*     */   private TypeConverterRegistry() {
/* 107 */     LOGGER.trace("TypeConverterRegistry initializing.");
/* 108 */     PluginManager manager = new PluginManager("TypeConverter");
/* 109 */     manager.collectPlugins();
/* 110 */     loadKnownTypeConverters(manager.getPlugins().values());
/* 111 */     registerPrimitiveTypes();
/*     */   }
/*     */   
/*     */   private void loadKnownTypeConverters(Collection<PluginType<?>> knownTypes) {
/* 115 */     for (PluginType<?> knownType : knownTypes) {
/* 116 */       Class<?> clazz = knownType.getPluginClass();
/* 117 */       if (TypeConverter.class.isAssignableFrom(clazz)) {
/*     */         
/* 119 */         Class<? extends TypeConverter> pluginClass = clazz.asSubclass(TypeConverter.class);
/* 120 */         Type conversionType = getTypeConverterSupportedType(pluginClass);
/* 121 */         TypeConverter<?> converter = (TypeConverter)ReflectionUtil.instantiate(pluginClass);
/* 122 */         if (this.registry.putIfAbsent(conversionType, converter) != null) {
/* 123 */           LOGGER.warn("Found a TypeConverter [{}] for type [{}] that already exists.", converter, conversionType);
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static Type getTypeConverterSupportedType(Class<? extends TypeConverter> typeConverterClass) {
/* 131 */     for (Type type : typeConverterClass.getGenericInterfaces()) {
/* 132 */       if (type instanceof ParameterizedType) {
/* 133 */         ParameterizedType pType = (ParameterizedType)type;
/* 134 */         if (TypeConverter.class.equals(pType.getRawType()))
/*     */         {
/* 136 */           return pType.getActualTypeArguments()[0];
/*     */         }
/*     */       } 
/*     */     } 
/* 140 */     return void.class;
/*     */   }
/*     */   
/*     */   private void registerPrimitiveTypes() {
/* 144 */     registerTypeAlias(Boolean.class, boolean.class);
/* 145 */     registerTypeAlias(Byte.class, byte.class);
/* 146 */     registerTypeAlias(Character.class, char.class);
/* 147 */     registerTypeAlias(Double.class, double.class);
/* 148 */     registerTypeAlias(Float.class, float.class);
/* 149 */     registerTypeAlias(Integer.class, int.class);
/* 150 */     registerTypeAlias(Long.class, long.class);
/* 151 */     registerTypeAlias(Short.class, short.class);
/*     */   }
/*     */   
/*     */   private void registerTypeAlias(Type knownType, Type aliasType) {
/* 155 */     this.registry.putIfAbsent(aliasType, this.registry.get(knownType));
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\config\plugins\convert\TypeConverterRegistry.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */