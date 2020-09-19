/*     */ package net.minecraft.advancements.critereon;
/*     */ 
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonNull;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.google.gson.JsonSyntaxException;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.resources.ResourceLocation;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.tags.SerializationTags;
/*     */ import net.minecraft.tags.Tag;
/*     */ import net.minecraft.util.GsonHelper;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ 
/*     */ public class BlockPredicate {
/*  22 */   public static final BlockPredicate ANY = new BlockPredicate(null, null, StatePropertiesPredicate.ANY, NbtPredicate.ANY);
/*     */   
/*     */   @Nullable
/*     */   private final Tag<Block> tag;
/*     */   @Nullable
/*     */   private final Block block;
/*     */   private final StatePropertiesPredicate properties;
/*     */   private final NbtPredicate nbt;
/*     */   
/*     */   public BlockPredicate(@Nullable Tag<Block> debug1, @Nullable Block debug2, StatePropertiesPredicate debug3, NbtPredicate debug4) {
/*  32 */     this.tag = debug1;
/*  33 */     this.block = debug2;
/*  34 */     this.properties = debug3;
/*  35 */     this.nbt = debug4;
/*     */   }
/*     */   
/*     */   public boolean matches(ServerLevel debug1, BlockPos debug2) {
/*  39 */     if (this == ANY) {
/*  40 */       return true;
/*     */     }
/*  42 */     if (!debug1.isLoaded(debug2)) {
/*  43 */       return false;
/*     */     }
/*  45 */     BlockState debug3 = debug1.getBlockState(debug2);
/*     */     
/*  47 */     Block debug4 = debug3.getBlock();
/*  48 */     if (this.tag != null && !this.tag.contains(debug4)) {
/*  49 */       return false;
/*     */     }
/*  51 */     if (this.block != null && debug4 != this.block) {
/*  52 */       return false;
/*     */     }
/*  54 */     if (!this.properties.matches(debug3)) {
/*  55 */       return false;
/*     */     }
/*  57 */     if (this.nbt != NbtPredicate.ANY) {
/*  58 */       BlockEntity debug5 = debug1.getBlockEntity(debug2);
/*  59 */       if (debug5 == null || !this.nbt.matches((Tag)debug5.save(new CompoundTag()))) {
/*  60 */         return false;
/*     */       }
/*     */     } 
/*  63 */     return true;
/*     */   }
/*     */   
/*     */   public static BlockPredicate fromJson(@Nullable JsonElement debug0) {
/*  67 */     if (debug0 == null || debug0.isJsonNull()) {
/*  68 */       return ANY;
/*     */     }
/*  70 */     JsonObject debug1 = GsonHelper.convertToJsonObject(debug0, "block");
/*     */     
/*  72 */     NbtPredicate debug2 = NbtPredicate.fromJson(debug1.get("nbt"));
/*  73 */     Block debug3 = null;
/*  74 */     if (debug1.has("block")) {
/*  75 */       ResourceLocation resourceLocation = new ResourceLocation(GsonHelper.getAsString(debug1, "block"));
/*  76 */       debug3 = (Block)Registry.BLOCK.get(resourceLocation);
/*     */     } 
/*     */     
/*  79 */     Tag<Block> debug4 = null;
/*  80 */     if (debug1.has("tag")) {
/*  81 */       ResourceLocation resourceLocation = new ResourceLocation(GsonHelper.getAsString(debug1, "tag"));
/*  82 */       debug4 = SerializationTags.getInstance().getBlocks().getTag(resourceLocation);
/*  83 */       if (debug4 == null) {
/*  84 */         throw new JsonSyntaxException("Unknown block tag '" + resourceLocation + "'");
/*     */       }
/*     */     } 
/*  87 */     StatePropertiesPredicate debug5 = StatePropertiesPredicate.fromJson(debug1.get("state"));
/*  88 */     return new BlockPredicate(debug4, debug3, debug5, debug2);
/*     */   }
/*     */   
/*     */   public JsonElement serializeToJson() {
/*  92 */     if (this == ANY) {
/*  93 */       return (JsonElement)JsonNull.INSTANCE;
/*     */     }
/*     */     
/*  96 */     JsonObject debug1 = new JsonObject();
/*  97 */     if (this.block != null) {
/*  98 */       debug1.addProperty("block", Registry.BLOCK.getKey(this.block).toString());
/*     */     }
/* 100 */     if (this.tag != null) {
/* 101 */       debug1.addProperty("tag", SerializationTags.getInstance().getBlocks().getIdOrThrow(this.tag).toString());
/*     */     }
/* 103 */     debug1.add("nbt", this.nbt.serializeToJson());
/* 104 */     debug1.add("state", this.properties.serializeToJson());
/*     */     
/* 106 */     return (JsonElement)debug1;
/*     */   }
/*     */   
/*     */   public static class Builder {
/*     */     @Nullable
/*     */     private Block block;
/*     */     @Nullable
/*     */     private Tag<Block> blocks;
/* 114 */     private StatePropertiesPredicate properties = StatePropertiesPredicate.ANY;
/* 115 */     private NbtPredicate nbt = NbtPredicate.ANY;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public static Builder block() {
/* 121 */       return new Builder();
/*     */     }
/*     */     
/*     */     public Builder of(Block debug1) {
/* 125 */       this.block = debug1;
/* 126 */       return this;
/*     */     }
/*     */     
/*     */     public Builder of(Tag<Block> debug1) {
/* 130 */       this.blocks = debug1;
/* 131 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder setProperties(StatePropertiesPredicate debug1) {
/* 140 */       this.properties = debug1;
/* 141 */       return this;
/*     */     }
/*     */     
/*     */     public BlockPredicate build() {
/* 145 */       return new BlockPredicate(this.blocks, this.block, this.properties, this.nbt);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\advancements\critereon\BlockPredicate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */