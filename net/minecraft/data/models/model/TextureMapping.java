/*     */ package net.minecraft.data.models.model;
/*     */ 
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.common.collect.Sets;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.resources.ResourceLocation;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ 
/*     */ public class TextureMapping
/*     */ {
/*  15 */   private final Map<TextureSlot, ResourceLocation> slots = Maps.newHashMap();
/*  16 */   private final Set<TextureSlot> forcedSlots = Sets.newHashSet();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TextureMapping put(TextureSlot debug1, ResourceLocation debug2) {
/*  22 */     this.slots.put(debug1, debug2);
/*  23 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Stream<TextureSlot> getForced() {
/*  33 */     return this.forcedSlots.stream();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TextureMapping copyForced(TextureSlot debug1, TextureSlot debug2) {
/*  42 */     this.slots.put(debug2, this.slots.get(debug1));
/*  43 */     this.forcedSlots.add(debug2);
/*  44 */     return this;
/*     */   }
/*     */   
/*     */   public ResourceLocation get(TextureSlot debug1) {
/*  48 */     TextureSlot debug2 = debug1;
/*  49 */     while (debug2 != null) {
/*  50 */       ResourceLocation debug3 = this.slots.get(debug2);
/*  51 */       if (debug3 != null) {
/*  52 */         return debug3;
/*     */       }
/*  54 */       debug2 = debug2.getParent();
/*     */     } 
/*  56 */     throw new IllegalStateException("Can't find texture for slot " + debug1);
/*     */   }
/*     */   
/*     */   public TextureMapping copyAndUpdate(TextureSlot debug1, ResourceLocation debug2) {
/*  60 */     TextureMapping debug3 = new TextureMapping();
/*  61 */     debug3.slots.putAll(this.slots);
/*  62 */     debug3.forcedSlots.addAll(this.forcedSlots);
/*  63 */     debug3.put(debug1, debug2);
/*  64 */     return debug3;
/*     */   }
/*     */   
/*     */   public static TextureMapping cube(Block debug0) {
/*  68 */     ResourceLocation debug1 = getBlockTexture(debug0);
/*  69 */     return cube(debug1);
/*     */   }
/*     */   
/*     */   public static TextureMapping defaultTexture(Block debug0) {
/*  73 */     ResourceLocation debug1 = getBlockTexture(debug0);
/*  74 */     return defaultTexture(debug1);
/*     */   }
/*     */   
/*     */   public static TextureMapping defaultTexture(ResourceLocation debug0) {
/*  78 */     return (new TextureMapping()).put(TextureSlot.TEXTURE, debug0);
/*     */   }
/*     */   
/*     */   public static TextureMapping cube(ResourceLocation debug0) {
/*  82 */     return (new TextureMapping()).put(TextureSlot.ALL, debug0);
/*     */   }
/*     */   
/*     */   public static TextureMapping cross(Block debug0) {
/*  86 */     return singleSlot(TextureSlot.CROSS, getBlockTexture(debug0));
/*     */   }
/*     */   
/*     */   public static TextureMapping cross(ResourceLocation debug0) {
/*  90 */     return singleSlot(TextureSlot.CROSS, debug0);
/*     */   }
/*     */   
/*     */   public static TextureMapping plant(Block debug0) {
/*  94 */     return singleSlot(TextureSlot.PLANT, getBlockTexture(debug0));
/*     */   }
/*     */   
/*     */   public static TextureMapping plant(ResourceLocation debug0) {
/*  98 */     return singleSlot(TextureSlot.PLANT, debug0);
/*     */   }
/*     */   
/*     */   public static TextureMapping rail(Block debug0) {
/* 102 */     return singleSlot(TextureSlot.RAIL, getBlockTexture(debug0));
/*     */   }
/*     */   
/*     */   public static TextureMapping rail(ResourceLocation debug0) {
/* 106 */     return singleSlot(TextureSlot.RAIL, debug0);
/*     */   }
/*     */   
/*     */   public static TextureMapping wool(Block debug0) {
/* 110 */     return singleSlot(TextureSlot.WOOL, getBlockTexture(debug0));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static TextureMapping stem(Block debug0) {
/* 118 */     return singleSlot(TextureSlot.STEM, getBlockTexture(debug0));
/*     */   }
/*     */   
/*     */   public static TextureMapping attachedStem(Block debug0, Block debug1) {
/* 122 */     return (new TextureMapping())
/* 123 */       .put(TextureSlot.STEM, getBlockTexture(debug0))
/* 124 */       .put(TextureSlot.UPPER_STEM, getBlockTexture(debug1));
/*     */   }
/*     */ 
/*     */   
/*     */   public static TextureMapping pattern(Block debug0) {
/* 129 */     return singleSlot(TextureSlot.PATTERN, getBlockTexture(debug0));
/*     */   }
/*     */   
/*     */   public static TextureMapping fan(Block debug0) {
/* 133 */     return singleSlot(TextureSlot.FAN, getBlockTexture(debug0));
/*     */   }
/*     */   
/*     */   public static TextureMapping crop(ResourceLocation debug0) {
/* 137 */     return singleSlot(TextureSlot.CROP, debug0);
/*     */   }
/*     */   
/*     */   public static TextureMapping pane(Block debug0, Block debug1) {
/* 141 */     return (new TextureMapping()).put(TextureSlot.PANE, getBlockTexture(debug0)).put(TextureSlot.EDGE, getBlockTexture(debug1, "_top"));
/*     */   }
/*     */   
/*     */   public static TextureMapping singleSlot(TextureSlot debug0, ResourceLocation debug1) {
/* 145 */     return (new TextureMapping()).put(debug0, debug1);
/*     */   }
/*     */   
/*     */   public static TextureMapping column(Block debug0) {
/* 149 */     return (new TextureMapping())
/* 150 */       .put(TextureSlot.SIDE, getBlockTexture(debug0, "_side"))
/* 151 */       .put(TextureSlot.END, getBlockTexture(debug0, "_top"));
/*     */   }
/*     */   
/*     */   public static TextureMapping cubeTop(Block debug0) {
/* 155 */     return (new TextureMapping())
/* 156 */       .put(TextureSlot.SIDE, getBlockTexture(debug0, "_side"))
/* 157 */       .put(TextureSlot.TOP, getBlockTexture(debug0, "_top"));
/*     */   }
/*     */ 
/*     */   
/*     */   public static TextureMapping logColumn(Block debug0) {
/* 162 */     return (new TextureMapping()).put(TextureSlot.SIDE, getBlockTexture(debug0)).put(TextureSlot.END, getBlockTexture(debug0, "_top"));
/*     */   }
/*     */   
/*     */   public static TextureMapping column(ResourceLocation debug0, ResourceLocation debug1) {
/* 166 */     return (new TextureMapping()).put(TextureSlot.SIDE, debug0).put(TextureSlot.END, debug1);
/*     */   }
/*     */   
/*     */   public static TextureMapping cubeBottomTop(Block debug0) {
/* 170 */     return (new TextureMapping())
/* 171 */       .put(TextureSlot.SIDE, getBlockTexture(debug0, "_side"))
/* 172 */       .put(TextureSlot.TOP, getBlockTexture(debug0, "_top"))
/* 173 */       .put(TextureSlot.BOTTOM, getBlockTexture(debug0, "_bottom"));
/*     */   }
/*     */   
/*     */   public static TextureMapping cubeBottomTopWithWall(Block debug0) {
/* 177 */     ResourceLocation debug1 = getBlockTexture(debug0);
/* 178 */     return (new TextureMapping())
/* 179 */       .put(TextureSlot.WALL, debug1)
/* 180 */       .put(TextureSlot.SIDE, debug1)
/* 181 */       .put(TextureSlot.TOP, getBlockTexture(debug0, "_top"))
/* 182 */       .put(TextureSlot.BOTTOM, getBlockTexture(debug0, "_bottom"));
/*     */   }
/*     */   
/*     */   public static TextureMapping columnWithWall(Block debug0) {
/* 186 */     ResourceLocation debug1 = getBlockTexture(debug0);
/* 187 */     return (new TextureMapping())
/* 188 */       .put(TextureSlot.WALL, debug1)
/* 189 */       .put(TextureSlot.SIDE, debug1)
/* 190 */       .put(TextureSlot.END, getBlockTexture(debug0, "_top"));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static TextureMapping door(Block debug0) {
/* 198 */     return (new TextureMapping()).put(TextureSlot.TOP, getBlockTexture(debug0, "_top")).put(TextureSlot.BOTTOM, getBlockTexture(debug0, "_bottom"));
/*     */   }
/*     */   
/*     */   public static TextureMapping particle(Block debug0) {
/* 202 */     return (new TextureMapping()).put(TextureSlot.PARTICLE, getBlockTexture(debug0));
/*     */   }
/*     */   
/*     */   public static TextureMapping particle(ResourceLocation debug0) {
/* 206 */     return (new TextureMapping()).put(TextureSlot.PARTICLE, debug0);
/*     */   }
/*     */   
/*     */   public static TextureMapping fire0(Block debug0) {
/* 210 */     return (new TextureMapping()).put(TextureSlot.FIRE, getBlockTexture(debug0, "_0"));
/*     */   }
/*     */   
/*     */   public static TextureMapping fire1(Block debug0) {
/* 214 */     return (new TextureMapping()).put(TextureSlot.FIRE, getBlockTexture(debug0, "_1"));
/*     */   }
/*     */   
/*     */   public static TextureMapping lantern(Block debug0) {
/* 218 */     return (new TextureMapping()).put(TextureSlot.LANTERN, getBlockTexture(debug0));
/*     */   }
/*     */   
/*     */   public static TextureMapping torch(Block debug0) {
/* 222 */     return (new TextureMapping()).put(TextureSlot.TORCH, getBlockTexture(debug0));
/*     */   }
/*     */   
/*     */   public static TextureMapping torch(ResourceLocation debug0) {
/* 226 */     return (new TextureMapping()).put(TextureSlot.TORCH, debug0);
/*     */   }
/*     */   
/*     */   public static TextureMapping particleFromItem(Item debug0) {
/* 230 */     return (new TextureMapping()).put(TextureSlot.PARTICLE, getItemTexture(debug0));
/*     */   }
/*     */   
/*     */   public static TextureMapping commandBlock(Block debug0) {
/* 234 */     return (new TextureMapping())
/* 235 */       .put(TextureSlot.SIDE, getBlockTexture(debug0, "_side"))
/* 236 */       .put(TextureSlot.FRONT, getBlockTexture(debug0, "_front"))
/* 237 */       .put(TextureSlot.BACK, getBlockTexture(debug0, "_back"));
/*     */   }
/*     */   
/*     */   public static TextureMapping orientableCube(Block debug0) {
/* 241 */     return (new TextureMapping())
/* 242 */       .put(TextureSlot.SIDE, getBlockTexture(debug0, "_side"))
/* 243 */       .put(TextureSlot.FRONT, getBlockTexture(debug0, "_front"))
/* 244 */       .put(TextureSlot.TOP, getBlockTexture(debug0, "_top"))
/* 245 */       .put(TextureSlot.BOTTOM, getBlockTexture(debug0, "_bottom"));
/*     */   }
/*     */   
/*     */   public static TextureMapping orientableCubeOnlyTop(Block debug0) {
/* 249 */     return (new TextureMapping())
/* 250 */       .put(TextureSlot.SIDE, getBlockTexture(debug0, "_side"))
/* 251 */       .put(TextureSlot.FRONT, getBlockTexture(debug0, "_front"))
/* 252 */       .put(TextureSlot.TOP, getBlockTexture(debug0, "_top"));
/*     */   }
/*     */   
/*     */   public static TextureMapping orientableCubeSameEnds(Block debug0) {
/* 256 */     return (new TextureMapping())
/* 257 */       .put(TextureSlot.SIDE, getBlockTexture(debug0, "_side"))
/* 258 */       .put(TextureSlot.FRONT, getBlockTexture(debug0, "_front"))
/* 259 */       .put(TextureSlot.END, getBlockTexture(debug0, "_end"));
/*     */   }
/*     */   
/*     */   public static TextureMapping top(Block debug0) {
/* 263 */     return (new TextureMapping()).put(TextureSlot.TOP, getBlockTexture(debug0, "_top"));
/*     */   }
/*     */   
/*     */   public static TextureMapping craftingTable(Block debug0, Block debug1) {
/* 267 */     return (new TextureMapping())
/* 268 */       .put(TextureSlot.PARTICLE, getBlockTexture(debug0, "_front"))
/* 269 */       .put(TextureSlot.DOWN, getBlockTexture(debug1))
/* 270 */       .put(TextureSlot.UP, getBlockTexture(debug0, "_top"))
/* 271 */       .put(TextureSlot.NORTH, getBlockTexture(debug0, "_front"))
/* 272 */       .put(TextureSlot.EAST, getBlockTexture(debug0, "_side"))
/* 273 */       .put(TextureSlot.SOUTH, getBlockTexture(debug0, "_side"))
/* 274 */       .put(TextureSlot.WEST, getBlockTexture(debug0, "_front"));
/*     */   }
/*     */ 
/*     */   
/*     */   public static TextureMapping fletchingTable(Block debug0, Block debug1) {
/* 279 */     return (new TextureMapping())
/* 280 */       .put(TextureSlot.PARTICLE, getBlockTexture(debug0, "_front"))
/* 281 */       .put(TextureSlot.DOWN, getBlockTexture(debug1))
/* 282 */       .put(TextureSlot.UP, getBlockTexture(debug0, "_top"))
/* 283 */       .put(TextureSlot.NORTH, getBlockTexture(debug0, "_front"))
/* 284 */       .put(TextureSlot.SOUTH, getBlockTexture(debug0, "_front"))
/* 285 */       .put(TextureSlot.EAST, getBlockTexture(debug0, "_side"))
/* 286 */       .put(TextureSlot.WEST, getBlockTexture(debug0, "_side"));
/*     */   }
/*     */   
/*     */   public static TextureMapping campfire(Block debug0) {
/* 290 */     return (new TextureMapping())
/* 291 */       .put(TextureSlot.LIT_LOG, getBlockTexture(debug0, "_log_lit"))
/* 292 */       .put(TextureSlot.FIRE, getBlockTexture(debug0, "_fire"));
/*     */   }
/*     */ 
/*     */   
/*     */   public static TextureMapping layer0(Item debug0) {
/* 297 */     return (new TextureMapping()).put(TextureSlot.LAYER0, getItemTexture(debug0));
/*     */   }
/*     */   
/*     */   public static TextureMapping layer0(Block debug0) {
/* 301 */     return (new TextureMapping()).put(TextureSlot.LAYER0, getBlockTexture(debug0));
/*     */   }
/*     */   
/*     */   public static TextureMapping layer0(ResourceLocation debug0) {
/* 305 */     return (new TextureMapping()).put(TextureSlot.LAYER0, debug0);
/*     */   }
/*     */   
/*     */   public static ResourceLocation getBlockTexture(Block debug0) {
/* 309 */     ResourceLocation debug1 = Registry.BLOCK.getKey(debug0);
/* 310 */     return new ResourceLocation(debug1.getNamespace(), "block/" + debug1.getPath());
/*     */   }
/*     */   
/*     */   public static ResourceLocation getBlockTexture(Block debug0, String debug1) {
/* 314 */     ResourceLocation debug2 = Registry.BLOCK.getKey(debug0);
/* 315 */     return new ResourceLocation(debug2.getNamespace(), "block/" + debug2.getPath() + debug1);
/*     */   }
/*     */   
/*     */   public static ResourceLocation getItemTexture(Item debug0) {
/* 319 */     ResourceLocation debug1 = Registry.ITEM.getKey(debug0);
/* 320 */     return new ResourceLocation(debug1.getNamespace(), "item/" + debug1.getPath());
/*     */   }
/*     */   
/*     */   public static ResourceLocation getItemTexture(Item debug0, String debug1) {
/* 324 */     ResourceLocation debug2 = Registry.ITEM.getKey(debug0);
/* 325 */     return new ResourceLocation(debug2.getNamespace(), "item/" + debug2.getPath() + debug1);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\data\models\model\TextureMapping.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */