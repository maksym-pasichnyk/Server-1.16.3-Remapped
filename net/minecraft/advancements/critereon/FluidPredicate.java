/*    */ package net.minecraft.advancements.critereon;
/*    */ 
/*    */ import com.google.gson.JsonElement;
/*    */ import com.google.gson.JsonNull;
/*    */ import com.google.gson.JsonObject;
/*    */ import com.google.gson.JsonSyntaxException;
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.resources.ResourceLocation;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.tags.SerializationTags;
/*    */ import net.minecraft.tags.Tag;
/*    */ import net.minecraft.util.GsonHelper;
/*    */ import net.minecraft.world.level.material.Fluid;
/*    */ import net.minecraft.world.level.material.FluidState;
/*    */ 
/*    */ public class FluidPredicate
/*    */ {
/* 20 */   public static final FluidPredicate ANY = new FluidPredicate(null, null, StatePropertiesPredicate.ANY);
/*    */   
/*    */   @Nullable
/*    */   private final Tag<Fluid> tag;
/*    */   @Nullable
/*    */   private final Fluid fluid;
/*    */   private final StatePropertiesPredicate properties;
/*    */   
/*    */   public FluidPredicate(@Nullable Tag<Fluid> debug1, @Nullable Fluid debug2, StatePropertiesPredicate debug3) {
/* 29 */     this.tag = debug1;
/* 30 */     this.fluid = debug2;
/* 31 */     this.properties = debug3;
/*    */   }
/*    */   
/*    */   public boolean matches(ServerLevel debug1, BlockPos debug2) {
/* 35 */     if (this == ANY) {
/* 36 */       return true;
/*    */     }
/* 38 */     if (!debug1.isLoaded(debug2)) {
/* 39 */       return false;
/*    */     }
/* 41 */     FluidState debug3 = debug1.getFluidState(debug2);
/*    */     
/* 43 */     Fluid debug4 = debug3.getType();
/* 44 */     if (this.tag != null && !this.tag.contains(debug4)) {
/* 45 */       return false;
/*    */     }
/* 47 */     if (this.fluid != null && debug4 != this.fluid) {
/* 48 */       return false;
/*    */     }
/* 50 */     if (!this.properties.matches(debug3)) {
/* 51 */       return false;
/*    */     }
/* 53 */     return true;
/*    */   }
/*    */   
/*    */   public static FluidPredicate fromJson(@Nullable JsonElement debug0) {
/* 57 */     if (debug0 == null || debug0.isJsonNull()) {
/* 58 */       return ANY;
/*    */     }
/* 60 */     JsonObject debug1 = GsonHelper.convertToJsonObject(debug0, "fluid");
/*    */     
/* 62 */     Fluid debug2 = null;
/* 63 */     if (debug1.has("fluid")) {
/* 64 */       ResourceLocation resourceLocation = new ResourceLocation(GsonHelper.getAsString(debug1, "fluid"));
/* 65 */       debug2 = (Fluid)Registry.FLUID.get(resourceLocation);
/*    */     } 
/*    */     
/* 68 */     Tag<Fluid> debug3 = null;
/* 69 */     if (debug1.has("tag")) {
/* 70 */       ResourceLocation resourceLocation = new ResourceLocation(GsonHelper.getAsString(debug1, "tag"));
/* 71 */       debug3 = SerializationTags.getInstance().getFluids().getTag(resourceLocation);
/* 72 */       if (debug3 == null) {
/* 73 */         throw new JsonSyntaxException("Unknown fluid tag '" + resourceLocation + "'");
/*    */       }
/*    */     } 
/* 76 */     StatePropertiesPredicate debug4 = StatePropertiesPredicate.fromJson(debug1.get("state"));
/* 77 */     return new FluidPredicate(debug3, debug2, debug4);
/*    */   }
/*    */   
/*    */   public JsonElement serializeToJson() {
/* 81 */     if (this == ANY) {
/* 82 */       return (JsonElement)JsonNull.INSTANCE;
/*    */     }
/*    */     
/* 85 */     JsonObject debug1 = new JsonObject();
/* 86 */     if (this.fluid != null) {
/* 87 */       debug1.addProperty("fluid", Registry.FLUID.getKey(this.fluid).toString());
/*    */     }
/* 89 */     if (this.tag != null) {
/* 90 */       debug1.addProperty("tag", SerializationTags.getInstance().getFluids().getIdOrThrow(this.tag).toString());
/*    */     }
/* 92 */     debug1.add("state", this.properties.serializeToJson());
/*    */     
/* 94 */     return (JsonElement)debug1;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\advancements\critereon\FluidPredicate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */