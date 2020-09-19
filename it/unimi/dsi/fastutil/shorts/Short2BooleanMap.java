/*     */ package it.unimi.dsi.fastutil.shorts;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.booleans.BooleanCollection;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectIterator;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectSet;
/*     */ import java.util.Collection;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import java.util.function.BiFunction;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.IntFunction;
/*     */ import java.util.function.IntPredicate;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public interface Short2BooleanMap
/*     */   extends Short2BooleanFunction, Map<Short, Boolean>
/*     */ {
/*     */   public static interface FastEntrySet
/*     */     extends ObjectSet<Entry>
/*     */   {
/*     */     ObjectIterator<Short2BooleanMap.Entry> fastIterator();
/*     */     
/*     */     default void fastForEach(Consumer<? super Short2BooleanMap.Entry> consumer) {
/*  80 */       forEach(consumer);
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
/*     */   default void clear() {
/* 102 */     throw new UnsupportedOperationException();
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
/*     */   @Deprecated
/*     */   default ObjectSet<Map.Entry<Short, Boolean>> entrySet() {
/* 152 */     return (ObjectSet)short2BooleanEntrySet();
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
/*     */   @Deprecated
/*     */   default Boolean put(Short key, Boolean value) {
/* 166 */     return super.put(key, value);
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
/*     */   @Deprecated
/*     */   default Boolean get(Object key) {
/* 180 */     return super.get(key);
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
/*     */   @Deprecated
/*     */   default Boolean remove(Object key) {
/* 194 */     return super.remove(key);
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
/*     */   @Deprecated
/*     */   default boolean containsKey(Object key) {
/* 240 */     return super.containsKey(key);
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
/*     */   @Deprecated
/*     */   default boolean containsValue(Object value) {
/* 257 */     return (value == null) ? false : containsValue(((Boolean)value).booleanValue());
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
/*     */   default boolean getOrDefault(short key, boolean defaultValue) {
/*     */     boolean v;
/* 277 */     return ((v = get(key)) != defaultReturnValue() || containsKey(key)) ? v : defaultValue;
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
/*     */   default boolean putIfAbsent(short key, boolean value) {
/* 297 */     boolean v = get(key), drv = defaultReturnValue();
/* 298 */     if (v != drv || containsKey(key))
/* 299 */       return v; 
/* 300 */     put(key, value);
/* 301 */     return drv;
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
/*     */   default boolean remove(short key, boolean value) {
/* 318 */     boolean curValue = get(key);
/* 319 */     if (curValue != value || (curValue == defaultReturnValue() && !containsKey(key)))
/* 320 */       return false; 
/* 321 */     remove(key);
/* 322 */     return true;
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
/*     */   default boolean replace(short key, boolean oldValue, boolean newValue) {
/* 341 */     boolean curValue = get(key);
/* 342 */     if (curValue != oldValue || (curValue == defaultReturnValue() && !containsKey(key)))
/* 343 */       return false; 
/* 344 */     put(key, newValue);
/* 345 */     return true;
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
/*     */   default boolean replace(short key, boolean value) {
/* 364 */     return containsKey(key) ? put(key, value) : defaultReturnValue();
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
/*     */   default boolean computeIfAbsent(short key, IntPredicate mappingFunction) {
/* 390 */     Objects.requireNonNull(mappingFunction);
/* 391 */     boolean v = get(key);
/* 392 */     if (v != defaultReturnValue() || containsKey(key))
/* 393 */       return v; 
/* 394 */     boolean newValue = mappingFunction.test(key);
/* 395 */     put(key, newValue);
/* 396 */     return newValue;
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
/*     */   default boolean computeIfAbsentNullable(short key, IntFunction<? extends Boolean> mappingFunction) {
/* 423 */     Objects.requireNonNull(mappingFunction);
/* 424 */     boolean v = get(key), drv = defaultReturnValue();
/* 425 */     if (v != drv || containsKey(key))
/* 426 */       return v; 
/* 427 */     Boolean mappedValue = mappingFunction.apply(key);
/* 428 */     if (mappedValue == null)
/* 429 */       return drv; 
/* 430 */     boolean newValue = mappedValue.booleanValue();
/* 431 */     put(key, newValue);
/* 432 */     return newValue;
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
/*     */   default boolean computeIfAbsentPartial(short key, Short2BooleanFunction mappingFunction) {
/* 461 */     Objects.requireNonNull(mappingFunction);
/* 462 */     boolean v = get(key), drv = defaultReturnValue();
/* 463 */     if (v != drv || containsKey(key))
/* 464 */       return v; 
/* 465 */     if (!mappingFunction.containsKey(key))
/* 466 */       return drv; 
/* 467 */     boolean newValue = mappingFunction.get(key);
/* 468 */     put(key, newValue);
/* 469 */     return newValue;
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
/*     */   default boolean computeIfPresent(short key, BiFunction<? super Short, ? super Boolean, ? extends Boolean> remappingFunction) {
/* 488 */     Objects.requireNonNull(remappingFunction);
/* 489 */     boolean oldValue = get(key), drv = defaultReturnValue();
/* 490 */     if (oldValue == drv && !containsKey(key))
/* 491 */       return drv; 
/* 492 */     Boolean newValue = remappingFunction.apply(Short.valueOf(key), Boolean.valueOf(oldValue));
/* 493 */     if (newValue == null) {
/* 494 */       remove(key);
/* 495 */       return drv;
/*     */     } 
/* 497 */     boolean newVal = newValue.booleanValue();
/* 498 */     put(key, newVal);
/* 499 */     return newVal;
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
/*     */   default boolean compute(short key, BiFunction<? super Short, ? super Boolean, ? extends Boolean> remappingFunction) {
/* 524 */     Objects.requireNonNull(remappingFunction);
/* 525 */     boolean oldValue = get(key), drv = defaultReturnValue();
/* 526 */     boolean contained = (oldValue != drv || containsKey(key));
/* 527 */     Boolean newValue = remappingFunction.apply(Short.valueOf(key), 
/* 528 */         contained ? Boolean.valueOf(oldValue) : null);
/* 529 */     if (newValue == null) {
/* 530 */       if (contained)
/* 531 */         remove(key); 
/* 532 */       return drv;
/*     */     } 
/* 534 */     boolean newVal = newValue.booleanValue();
/* 535 */     put(key, newVal);
/* 536 */     return newVal;
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
/*     */   default boolean merge(short key, boolean value, BiFunction<? super Boolean, ? super Boolean, ? extends Boolean> remappingFunction) {
/*     */     boolean newValue;
/* 562 */     Objects.requireNonNull(remappingFunction);
/* 563 */     boolean oldValue = get(key), drv = defaultReturnValue();
/*     */     
/* 565 */     if (oldValue != drv || containsKey(key)) {
/* 566 */       Boolean mergedValue = remappingFunction.apply(Boolean.valueOf(oldValue), Boolean.valueOf(value));
/* 567 */       if (mergedValue == null) {
/* 568 */         remove(key);
/* 569 */         return drv;
/*     */       } 
/* 571 */       newValue = mergedValue.booleanValue();
/*     */     } else {
/* 573 */       newValue = value;
/*     */     } 
/* 575 */     put(key, newValue);
/* 576 */     return newValue;
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
/*     */   @Deprecated
/*     */   default Boolean getOrDefault(Object key, Boolean defaultValue) {
/* 589 */     return super.getOrDefault(key, defaultValue);
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
/*     */   @Deprecated
/*     */   default Boolean putIfAbsent(Short key, Boolean value) {
/* 602 */     return super.putIfAbsent(key, value);
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
/*     */   @Deprecated
/*     */   default boolean remove(Object key, Object value) {
/* 615 */     return super.remove(key, value);
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
/*     */   @Deprecated
/*     */   default boolean replace(Short key, Boolean oldValue, Boolean newValue) {
/* 628 */     return super.replace(key, oldValue, newValue);
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
/*     */   @Deprecated
/*     */   default Boolean replace(Short key, Boolean value) {
/* 641 */     return super.replace(key, value);
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
/*     */   @Deprecated
/*     */   default Boolean computeIfAbsent(Short key, Function<? super Short, ? extends Boolean> mappingFunction) {
/* 655 */     return super.computeIfAbsent(key, mappingFunction);
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
/*     */   @Deprecated
/*     */   default Boolean computeIfPresent(Short key, BiFunction<? super Short, ? super Boolean, ? extends Boolean> remappingFunction) {
/* 669 */     return super.computeIfPresent(key, remappingFunction);
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
/*     */   @Deprecated
/*     */   default Boolean compute(Short key, BiFunction<? super Short, ? super Boolean, ? extends Boolean> remappingFunction) {
/* 683 */     return super.compute(key, remappingFunction);
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
/*     */   @Deprecated
/*     */   default Boolean merge(Short key, Boolean value, BiFunction<? super Boolean, ? super Boolean, ? extends Boolean> remappingFunction) {
/* 697 */     return super.merge(key, value, remappingFunction);
/*     */   }
/*     */   
/*     */   int size();
/*     */   
/*     */   void defaultReturnValue(boolean paramBoolean);
/*     */   
/*     */   boolean defaultReturnValue();
/*     */   
/*     */   ObjectSet<Entry> short2BooleanEntrySet();
/*     */   
/*     */   ShortSet keySet();
/*     */   
/*     */   BooleanCollection values();
/*     */   
/*     */   boolean containsKey(short paramShort);
/*     */   
/*     */   boolean containsValue(boolean paramBoolean);
/*     */   
/*     */   public static interface Entry
/*     */     extends Map.Entry<Short, Boolean> {
/*     */     @Deprecated
/*     */     default Short getKey() {
/* 720 */       return Short.valueOf(getShortKey());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Deprecated
/*     */     default Boolean getValue() {
/* 743 */       return Boolean.valueOf(getBooleanValue());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Deprecated
/*     */     default Boolean setValue(Boolean value) {
/* 753 */       return Boolean.valueOf(setValue(value.booleanValue()));
/*     */     }
/*     */     
/*     */     short getShortKey();
/*     */     
/*     */     boolean getBooleanValue();
/*     */     
/*     */     boolean setValue(boolean param1Boolean);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\i\\unimi\dsi\fastutil\shorts\Short2BooleanMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */