/*    */ package net.minecraft.data.models.model;
/*    */ 
/*    */ import com.google.gson.JsonElement;
/*    */ import java.util.function.BiConsumer;
/*    */ import java.util.function.Consumer;
/*    */ import java.util.function.Function;
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.resources.ResourceLocation;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ 
/*    */ public class TexturedModel
/*    */ {
/* 13 */   public static final Provider CUBE = createDefault(TextureMapping::cube, ModelTemplates.CUBE_ALL);
/* 14 */   public static final Provider CUBE_MIRRORED = createDefault(TextureMapping::cube, ModelTemplates.CUBE_MIRRORED_ALL);
/* 15 */   public static final Provider COLUMN = createDefault(TextureMapping::column, ModelTemplates.CUBE_COLUMN);
/* 16 */   public static final Provider COLUMN_HORIZONTAL = createDefault(TextureMapping::column, ModelTemplates.CUBE_COLUMN_HORIZONTAL);
/* 17 */   public static final Provider CUBE_TOP_BOTTOM = createDefault(TextureMapping::cubeBottomTop, ModelTemplates.CUBE_BOTTOM_TOP);
/* 18 */   public static final Provider CUBE_TOP = createDefault(TextureMapping::cubeTop, ModelTemplates.CUBE_TOP);
/*    */   
/* 20 */   public static final Provider ORIENTABLE_ONLY_TOP = createDefault(TextureMapping::orientableCubeOnlyTop, ModelTemplates.CUBE_ORIENTABLE);
/* 21 */   public static final Provider ORIENTABLE = createDefault(TextureMapping::orientableCube, ModelTemplates.CUBE_ORIENTABLE_TOP_BOTTOM);
/*    */   
/* 23 */   public static final Provider CARPET = createDefault(TextureMapping::wool, ModelTemplates.CARPET);
/* 24 */   public static final Provider GLAZED_TERRACOTTA = createDefault(TextureMapping::pattern, ModelTemplates.GLAZED_TERRACOTTA);
/* 25 */   public static final Provider CORAL_FAN = createDefault(TextureMapping::fan, ModelTemplates.CORAL_FAN);
/* 26 */   public static final Provider PARTICLE_ONLY = createDefault(TextureMapping::particle, ModelTemplates.PARTICLE_ONLY);
/* 27 */   public static final Provider ANVIL = createDefault(TextureMapping::top, ModelTemplates.ANVIL);
/* 28 */   public static final Provider LEAVES = createDefault(TextureMapping::cube, ModelTemplates.LEAVES);
/* 29 */   public static final Provider LANTERN = createDefault(TextureMapping::lantern, ModelTemplates.LANTERN);
/* 30 */   public static final Provider HANGING_LANTERN = createDefault(TextureMapping::lantern, ModelTemplates.HANGING_LANTERN);
/* 31 */   public static final Provider SEAGRASS = createDefault(TextureMapping::defaultTexture, ModelTemplates.SEAGRASS);
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 36 */   public static final Provider COLUMN_ALT = createDefault(TextureMapping::logColumn, ModelTemplates.CUBE_COLUMN);
/* 37 */   public static final Provider COLUMN_HORIZONTAL_ALT = createDefault(TextureMapping::logColumn, ModelTemplates.CUBE_COLUMN_HORIZONTAL);
/*    */ 
/*    */   
/* 40 */   public static final Provider TOP_BOTTOM_WITH_WALL = createDefault(TextureMapping::cubeBottomTopWithWall, ModelTemplates.CUBE_BOTTOM_TOP);
/*    */ 
/*    */   
/* 43 */   public static final Provider COLUMN_WITH_WALL = createDefault(TextureMapping::columnWithWall, ModelTemplates.CUBE_COLUMN);
/*    */   
/*    */   private final TextureMapping mapping;
/*    */   private final ModelTemplate template;
/*    */   
/*    */   private TexturedModel(TextureMapping debug1, ModelTemplate debug2) {
/* 49 */     this.mapping = debug1;
/* 50 */     this.template = debug2;
/*    */   }
/*    */   
/*    */   public ModelTemplate getTemplate() {
/* 54 */     return this.template;
/*    */   }
/*    */   
/*    */   public TextureMapping getMapping() {
/* 58 */     return this.mapping;
/*    */   }
/*    */   
/*    */   public TexturedModel updateTextures(Consumer<TextureMapping> debug1) {
/* 62 */     debug1.accept(this.mapping);
/* 63 */     return this;
/*    */   }
/*    */   
/*    */   public ResourceLocation create(Block debug1, BiConsumer<ResourceLocation, Supplier<JsonElement>> debug2) {
/* 67 */     return this.template.create(debug1, this.mapping, debug2);
/*    */   }
/*    */   
/*    */   public ResourceLocation createWithSuffix(Block debug1, String debug2, BiConsumer<ResourceLocation, Supplier<JsonElement>> debug3) {
/* 71 */     return this.template.createWithSuffix(debug1, debug2, this.mapping, debug3);
/*    */   }
/*    */   
/*    */   private static Provider createDefault(Function<Block, TextureMapping> debug0, ModelTemplate debug1) {
/* 75 */     return debug2 -> new TexturedModel(debug0.apply(debug2), debug1);
/*    */   }
/*    */   
/*    */   public static TexturedModel createAllSame(ResourceLocation debug0) {
/* 79 */     return new TexturedModel(TextureMapping.cube(debug0), ModelTemplates.CUBE_ALL);
/*    */   }
/*    */   
/*    */   @FunctionalInterface
/*    */   public static interface Provider {
/*    */     TexturedModel get(Block param1Block);
/*    */     
/*    */     default ResourceLocation create(Block debug1, BiConsumer<ResourceLocation, Supplier<JsonElement>> debug2) {
/* 87 */       return get(debug1).create(debug1, debug2);
/*    */     }
/*    */     
/*    */     default ResourceLocation createWithSuffix(Block debug1, String debug2, BiConsumer<ResourceLocation, Supplier<JsonElement>> debug3) {
/* 91 */       return get(debug1).createWithSuffix(debug1, debug2, debug3);
/*    */     }
/*    */     
/*    */     default Provider updateTexture(Consumer<TextureMapping> debug1) {
/* 95 */       return debug2 -> get(debug2).updateTextures(debug1);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\data\models\model\TexturedModel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */