/*    */ package com.mojang.serialization;
/*    */ 
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import java.util.Map;
/*    */ import java.util.stream.Stream;
/*    */ import javax.annotation.Nullable;
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface MapLike<T>
/*    */ {
/*    */   @Nullable
/*    */   T get(T paramT);
/*    */   
/*    */   @Nullable
/*    */   T get(String paramString);
/*    */   
/*    */   Stream<Pair<T, T>> entries();
/*    */   
/*    */   static <T> MapLike<T> forMap(final Map<T, T> map, final DynamicOps<T> ops) {
/* 21 */     return new MapLike<T>()
/*    */       {
/*    */         @Nullable
/*    */         public T get(T key) {
/* 25 */           return (T)map.get(key);
/*    */         }
/*    */ 
/*    */         
/*    */         @Nullable
/*    */         public T get(String key) {
/* 31 */           return get(ops.createString(key));
/*    */         }
/*    */ 
/*    */         
/*    */         public Stream<Pair<T, T>> entries() {
/* 36 */           return map.entrySet().stream().map(e -> Pair.of(e.getKey(), e.getValue()));
/*    */         }
/*    */ 
/*    */         
/*    */         public String toString() {
/* 41 */           return "MapLike[" + map + "]";
/*    */         }
/*    */       };
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\serialization\MapLike.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */