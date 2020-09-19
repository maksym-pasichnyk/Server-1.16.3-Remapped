/*    */ package net.minecraft.data.models.model;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import com.google.common.collect.ImmutableSet;
/*    */ import com.google.common.collect.Streams;
/*    */ import com.google.gson.JsonElement;
/*    */ import com.google.gson.JsonObject;
/*    */ import java.util.Map;
/*    */ import java.util.Optional;
/*    */ import java.util.Set;
/*    */ import java.util.function.BiConsumer;
/*    */ import java.util.function.Function;
/*    */ import java.util.function.Supplier;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.resources.ResourceLocation;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ModelTemplate
/*    */ {
/*    */   private final Optional<ResourceLocation> model;
/*    */   private final Set<TextureSlot> requiredSlots;
/*    */   private Optional<String> suffix;
/*    */   
/*    */   public ModelTemplate(Optional<ResourceLocation> debug1, Optional<String> debug2, TextureSlot... debug3) {
/* 27 */     this.model = debug1;
/* 28 */     this.suffix = debug2;
/* 29 */     this.requiredSlots = (Set<TextureSlot>)ImmutableSet.copyOf((Object[])debug3);
/*    */   }
/*    */   
/*    */   public ResourceLocation create(Block debug1, TextureMapping debug2, BiConsumer<ResourceLocation, Supplier<JsonElement>> debug3) {
/* 33 */     return create(ModelLocationUtils.getModelLocation(debug1, this.suffix.orElse("")), debug2, debug3);
/*    */   }
/*    */   
/*    */   public ResourceLocation createWithSuffix(Block debug1, String debug2, TextureMapping debug3, BiConsumer<ResourceLocation, Supplier<JsonElement>> debug4) {
/* 37 */     return create(ModelLocationUtils.getModelLocation(debug1, debug2 + (String)this.suffix.orElse("")), debug3, debug4);
/*    */   }
/*    */   
/*    */   public ResourceLocation createWithOverride(Block debug1, String debug2, TextureMapping debug3, BiConsumer<ResourceLocation, Supplier<JsonElement>> debug4) {
/* 41 */     return create(ModelLocationUtils.getModelLocation(debug1, debug2), debug3, debug4);
/*    */   }
/*    */   
/*    */   public ResourceLocation create(ResourceLocation debug1, TextureMapping debug2, BiConsumer<ResourceLocation, Supplier<JsonElement>> debug3) {
/* 45 */     Map<TextureSlot, ResourceLocation> debug4 = createMap(debug2);
/*    */     
/* 47 */     debug3.accept(debug1, () -> {
/*    */           JsonObject debug2 = new JsonObject();
/*    */           
/*    */           this.model.ifPresent(());
/*    */           if (!debug1.isEmpty()) {
/*    */             JsonObject debug3 = new JsonObject();
/*    */             debug1.forEach(());
/*    */             debug2.add("textures", (JsonElement)debug3);
/*    */           } 
/*    */           return (JsonElement)debug2;
/*    */         });
/* 58 */     return debug1;
/*    */   }
/*    */   
/*    */   private Map<TextureSlot, ResourceLocation> createMap(TextureMapping debug1) {
/* 62 */     return (Map<TextureSlot, ResourceLocation>)Streams.concat(new Stream[] { this.requiredSlots.stream(), debug1.getForced() }).collect(ImmutableMap.toImmutableMap(Function.identity(), debug1::get));
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\data\models\model\ModelTemplate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */