/*    */ package net.minecraft.tags;
/*    */ 
/*    */ import com.google.common.collect.HashMultimap;
/*    */ import com.google.common.collect.Maps;
/*    */ import com.google.common.collect.Multimap;
/*    */ import java.util.Map;
/*    */ import java.util.function.Function;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.resources.ResourceLocation;
/*    */ 
/*    */ public class StaticTags
/*    */ {
/* 13 */   private static final Map<ResourceLocation, StaticTagHelper<?>> HELPERS = Maps.newHashMap();
/*    */   
/*    */   public static <T> StaticTagHelper<T> create(ResourceLocation debug0, Function<TagContainer, TagCollection<T>> debug1) {
/* 16 */     StaticTagHelper<T> debug2 = new StaticTagHelper<>(debug1);
/* 17 */     StaticTagHelper<?> debug3 = HELPERS.putIfAbsent(debug0, debug2);
/* 18 */     if (debug3 != null) {
/* 19 */       throw new IllegalStateException("Duplicate entry for static tag collection: " + debug0);
/*    */     }
/* 21 */     return debug2;
/*    */   }
/*    */   
/*    */   public static void resetAll(TagContainer debug0) {
/* 25 */     HELPERS.values().forEach(debug1 -> debug1.reset(debug0));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static Multimap<ResourceLocation, ResourceLocation> getAllMissingTags(TagContainer debug0) {
/* 33 */     HashMultimap hashMultimap = HashMultimap.create();
/* 34 */     HELPERS.forEach((debug2, debug3) -> debug0.putAll(debug2, debug3.getMissingTags(debug1)));
/* 35 */     return (Multimap<ResourceLocation, ResourceLocation>)hashMultimap;
/*    */   }
/*    */   
/*    */   public static void bootStrap() {
/* 39 */     StaticTagHelper[] debug0 = { BlockTags.HELPER, ItemTags.HELPER, FluidTags.HELPER, EntityTypeTags.HELPER };
/* 40 */     boolean debug1 = Stream.<StaticTagHelper>of(debug0).anyMatch(debug0 -> !HELPERS.containsValue(debug0));
/* 41 */     if (debug1)
/* 42 */       throw new IllegalStateException("Missing helper registrations"); 
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\tags\StaticTags.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */