/*     */ package it.unimi.dsi.fastutil.floats;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.SafeMath;
/*     */ import it.unimi.dsi.fastutil.chars.CharCollection;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectIterator;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectSet;
/*     */ import java.util.Collection;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import java.util.function.BiFunction;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.DoubleFunction;
/*     */ import java.util.function.DoubleToIntFunction;
/*     */ import java.util.function.Function;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public interface Float2CharMap
/*     */   extends Float2CharFunction, Map<Float, Character>
/*     */ {
/*     */   public static interface FastEntrySet
/*     */     extends ObjectSet<Entry>
/*     */   {
/*     */     ObjectIterator<Float2CharMap.Entry> fastIterator();
/*     */     
/*     */     default void fastForEach(Consumer<? super Float2CharMap.Entry> consumer) {
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
/*     */   default ObjectSet<Map.Entry<Float, Character>> entrySet() {
/* 152 */     return (ObjectSet)float2CharEntrySet();
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
/*     */   default Character put(Float key, Character value) {
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
/*     */   default Character get(Object key) {
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
/*     */   default Character remove(Object key) {
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
/* 257 */     return (value == null) ? false : containsValue(((Character)value).charValue());
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
/*     */   default char getOrDefault(float key, char defaultValue) {
/*     */     char v;
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
/*     */   default char putIfAbsent(float key, char value) {
/* 297 */     char v = get(key), drv = defaultReturnValue();
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
/*     */   default boolean remove(float key, char value) {
/* 318 */     char curValue = get(key);
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
/*     */   default boolean replace(float key, char oldValue, char newValue) {
/* 341 */     char curValue = get(key);
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
/*     */   default char replace(float key, char value) {
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
/*     */   default char computeIfAbsent(float key, DoubleToIntFunction mappingFunction) {
/* 390 */     Objects.requireNonNull(mappingFunction);
/* 391 */     char v = get(key);
/* 392 */     if (v != defaultReturnValue() || containsKey(key))
/* 393 */       return v; 
/* 394 */     char newValue = SafeMath.safeIntToChar(mappingFunction.applyAsInt(key));
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
/*     */   default char computeIfAbsentNullable(float key, DoubleFunction<? extends Character> mappingFunction) {
/* 423 */     Objects.requireNonNull(mappingFunction);
/* 424 */     char v = get(key), drv = defaultReturnValue();
/* 425 */     if (v != drv || containsKey(key))
/* 426 */       return v; 
/* 427 */     Character mappedValue = mappingFunction.apply(key);
/* 428 */     if (mappedValue == null)
/* 429 */       return drv; 
/* 430 */     char newValue = mappedValue.charValue();
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
/*     */   default char computeIfAbsentPartial(float key, Float2CharFunction mappingFunction) {
/* 461 */     Objects.requireNonNull(mappingFunction);
/* 462 */     char v = get(key), drv = defaultReturnValue();
/* 463 */     if (v != drv || containsKey(key))
/* 464 */       return v; 
/* 465 */     if (!mappingFunction.containsKey(key))
/* 466 */       return drv; 
/* 467 */     char newValue = mappingFunction.get(key);
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
/*     */   default char computeIfPresent(float key, BiFunction<? super Float, ? super Character, ? extends Character> remappingFunction) {
/* 488 */     Objects.requireNonNull(remappingFunction);
/* 489 */     char oldValue = get(key), drv = defaultReturnValue();
/* 490 */     if (oldValue == drv && !containsKey(key))
/* 491 */       return drv; 
/* 492 */     Character newValue = remappingFunction.apply(Float.valueOf(key), Character.valueOf(oldValue));
/* 493 */     if (newValue == null) {
/* 494 */       remove(key);
/* 495 */       return drv;
/*     */     } 
/* 497 */     char newVal = newValue.charValue();
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
/*     */   default char compute(float key, BiFunction<? super Float, ? super Character, ? extends Character> remappingFunction) {
/* 524 */     Objects.requireNonNull(remappingFunction);
/* 525 */     char oldValue = get(key), drv = defaultReturnValue();
/* 526 */     boolean contained = (oldValue != drv || containsKey(key));
/* 527 */     Character newValue = remappingFunction.apply(Float.valueOf(key), 
/* 528 */         contained ? Character.valueOf(oldValue) : null);
/* 529 */     if (newValue == null) {
/* 530 */       if (contained)
/* 531 */         remove(key); 
/* 532 */       return drv;
/*     */     } 
/* 534 */     char newVal = newValue.charValue();
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
/*     */   default char merge(float key, char value, BiFunction<? super Character, ? super Character, ? extends Character> remappingFunction) {
/*     */     char newValue;
/* 562 */     Objects.requireNonNull(remappingFunction);
/* 563 */     char oldValue = get(key), drv = defaultReturnValue();
/*     */     
/* 565 */     if (oldValue != drv || containsKey(key)) {
/* 566 */       Character mergedValue = remappingFunction.apply(Character.valueOf(oldValue), 
/* 567 */           Character.valueOf(value));
/* 568 */       if (mergedValue == null) {
/* 569 */         remove(key);
/* 570 */         return drv;
/*     */       } 
/* 572 */       newValue = mergedValue.charValue();
/*     */     } else {
/* 574 */       newValue = value;
/*     */     } 
/* 576 */     put(key, newValue);
/* 577 */     return newValue;
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
/*     */   default Character getOrDefault(Object key, Character defaultValue) {
/* 590 */     return super.getOrDefault(key, defaultValue);
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
/*     */   default Character putIfAbsent(Float key, Character value) {
/* 603 */     return super.putIfAbsent(key, value);
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
/* 616 */     return super.remove(key, value);
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
/*     */   default boolean replace(Float key, Character oldValue, Character newValue) {
/* 629 */     return super.replace(key, oldValue, newValue);
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
/*     */   default Character replace(Float key, Character value) {
/* 642 */     return super.replace(key, value);
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
/*     */   default Character computeIfAbsent(Float key, Function<? super Float, ? extends Character> mappingFunction) {
/* 656 */     return super.computeIfAbsent(key, mappingFunction);
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
/*     */   default Character computeIfPresent(Float key, BiFunction<? super Float, ? super Character, ? extends Character> remappingFunction) {
/* 670 */     return super.computeIfPresent(key, remappingFunction);
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
/*     */   default Character compute(Float key, BiFunction<? super Float, ? super Character, ? extends Character> remappingFunction) {
/* 684 */     return super.compute(key, remappingFunction);
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
/*     */   default Character merge(Float key, Character value, BiFunction<? super Character, ? super Character, ? extends Character> remappingFunction) {
/* 698 */     return super.merge(key, value, remappingFunction);
/*     */   }
/*     */   
/*     */   int size();
/*     */   
/*     */   void defaultReturnValue(char paramChar);
/*     */   
/*     */   char defaultReturnValue();
/*     */   
/*     */   ObjectSet<Entry> float2CharEntrySet();
/*     */   
/*     */   FloatSet keySet();
/*     */   
/*     */   CharCollection values();
/*     */   
/*     */   boolean containsKey(float paramFloat);
/*     */   
/*     */   boolean containsValue(char paramChar);
/*     */   
/*     */   public static interface Entry
/*     */     extends Map.Entry<Float, Character> {
/*     */     @Deprecated
/*     */     default Float getKey() {
/* 721 */       return Float.valueOf(getFloatKey());
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
/*     */     default Character getValue() {
/* 744 */       return Character.valueOf(getCharValue());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Deprecated
/*     */     default Character setValue(Character value) {
/* 754 */       return Character.valueOf(setValue(value.charValue()));
/*     */     }
/*     */     
/*     */     float getFloatKey();
/*     */     
/*     */     char getCharValue();
/*     */     
/*     */     char setValue(char param1Char);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\i\\unimi\dsi\fastutil\floats\Float2CharMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */