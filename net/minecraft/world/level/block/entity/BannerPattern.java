/*     */ package net.minecraft.world.level.block.entity;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.ListTag;
/*     */ import net.minecraft.world.item.DyeColor;
/*     */ import org.apache.commons.lang3.tuple.Pair;
/*     */ 
/*     */ 
/*     */ 
/*     */ public enum BannerPattern
/*     */ {
/*  15 */   BASE("base", "b", false),
/*  16 */   SQUARE_BOTTOM_LEFT("square_bottom_left", "bl"),
/*  17 */   SQUARE_BOTTOM_RIGHT("square_bottom_right", "br"),
/*  18 */   SQUARE_TOP_LEFT("square_top_left", "tl"),
/*  19 */   SQUARE_TOP_RIGHT("square_top_right", "tr"),
/*  20 */   STRIPE_BOTTOM("stripe_bottom", "bs"),
/*  21 */   STRIPE_TOP("stripe_top", "ts"),
/*  22 */   STRIPE_LEFT("stripe_left", "ls"),
/*  23 */   STRIPE_RIGHT("stripe_right", "rs"),
/*  24 */   STRIPE_CENTER("stripe_center", "cs"),
/*  25 */   STRIPE_MIDDLE("stripe_middle", "ms"),
/*  26 */   STRIPE_DOWNRIGHT("stripe_downright", "drs"),
/*  27 */   STRIPE_DOWNLEFT("stripe_downleft", "dls"),
/*  28 */   STRIPE_SMALL("small_stripes", "ss"),
/*  29 */   CROSS("cross", "cr"),
/*  30 */   STRAIGHT_CROSS("straight_cross", "sc"),
/*  31 */   TRIANGLE_BOTTOM("triangle_bottom", "bt"),
/*  32 */   TRIANGLE_TOP("triangle_top", "tt"),
/*  33 */   TRIANGLES_BOTTOM("triangles_bottom", "bts"),
/*  34 */   TRIANGLES_TOP("triangles_top", "tts"),
/*  35 */   DIAGONAL_LEFT("diagonal_left", "ld"),
/*  36 */   DIAGONAL_RIGHT("diagonal_up_right", "rd"),
/*  37 */   DIAGONAL_LEFT_MIRROR("diagonal_up_left", "lud"),
/*  38 */   DIAGONAL_RIGHT_MIRROR("diagonal_right", "rud"),
/*  39 */   CIRCLE_MIDDLE("circle", "mc"),
/*  40 */   RHOMBUS_MIDDLE("rhombus", "mr"),
/*  41 */   HALF_VERTICAL("half_vertical", "vh"),
/*  42 */   HALF_HORIZONTAL("half_horizontal", "hh"),
/*  43 */   HALF_VERTICAL_MIRROR("half_vertical_right", "vhr"),
/*  44 */   HALF_HORIZONTAL_MIRROR("half_horizontal_bottom", "hhb"),
/*  45 */   BORDER("border", "bo"),
/*  46 */   CURLY_BORDER("curly_border", "cbo"),
/*  47 */   GRADIENT("gradient", "gra"),
/*  48 */   GRADIENT_UP("gradient_up", "gru"),
/*  49 */   BRICKS("bricks", "bri"),
/*     */ 
/*     */ 
/*     */   
/*  53 */   GLOBE("globe", "glb", true),
/*  54 */   CREEPER("creeper", "cre", true),
/*  55 */   SKULL("skull", "sku", true),
/*  56 */   FLOWER("flower", "flo", true),
/*  57 */   MOJANG("mojang", "moj", true),
/*  58 */   PIGLIN("piglin", "pig", true);
/*     */   
/*     */   static {
/*  61 */     VALUES = values();
/*  62 */     COUNT = VALUES.length;
/*  63 */     PATTERN_ITEM_COUNT = (int)Arrays.<BannerPattern>stream(VALUES).filter(debug0 -> debug0.hasPatternItem).count();
/*  64 */     AVAILABLE_PATTERNS = COUNT - PATTERN_ITEM_COUNT - 1;
/*     */   }
/*     */   private static final BannerPattern[] VALUES;
/*     */   public static final int COUNT;
/*     */   public static final int PATTERN_ITEM_COUNT;
/*     */   public static final int AVAILABLE_PATTERNS;
/*     */   private final boolean hasPatternItem;
/*     */   private final String filename;
/*     */   private final String hashname;
/*     */   
/*     */   BannerPattern(String debug3, String debug4, boolean debug5) {
/*  75 */     this.filename = debug3;
/*  76 */     this.hashname = debug4;
/*  77 */     this.hasPatternItem = debug5;
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
/*     */   public String getHashname() {
/*  90 */     return this.hashname;
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
/*     */   public static class Builder
/*     */   {
/* 104 */     private final List<Pair<BannerPattern, DyeColor>> patterns = Lists.newArrayList();
/*     */     
/*     */     public Builder addPattern(BannerPattern debug1, DyeColor debug2) {
/* 107 */       this.patterns.add(Pair.of(debug1, debug2));
/* 108 */       return this;
/*     */     }
/*     */     
/*     */     public ListTag toListTag() {
/* 112 */       ListTag debug1 = new ListTag();
/*     */       
/* 114 */       for (Pair<BannerPattern, DyeColor> debug3 : this.patterns) {
/* 115 */         CompoundTag debug4 = new CompoundTag();
/* 116 */         debug4.putString("Pattern", ((BannerPattern)debug3.getLeft()).hashname);
/* 117 */         debug4.putInt("Color", ((DyeColor)debug3.getRight()).getId());
/* 118 */         debug1.add(debug4);
/*     */       } 
/*     */       
/* 121 */       return debug1;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\entity\BannerPattern.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */