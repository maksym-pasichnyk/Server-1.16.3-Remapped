/*     */ package net.minecraft.world.item;
/*     */ import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
/*     */ import java.util.Arrays;
/*     */ import java.util.Comparator;
/*     */ import java.util.Map;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ import net.minecraft.world.level.material.MaterialColor;
/*     */ 
/*     */ public enum DyeColor implements StringRepresentable {
/*     */   private static final DyeColor[] BY_ID;
/*     */   private static final Int2ObjectOpenHashMap<DyeColor> BY_FIREWORK_COLOR;
/*  13 */   WHITE(0, "white", 16383998, MaterialColor.SNOW, 15790320, 16777215),
/*  14 */   ORANGE(1, "orange", 16351261, MaterialColor.COLOR_ORANGE, 15435844, 16738335),
/*  15 */   MAGENTA(2, "magenta", 13061821, MaterialColor.COLOR_MAGENTA, 12801229, 16711935),
/*  16 */   LIGHT_BLUE(3, "light_blue", 3847130, MaterialColor.COLOR_LIGHT_BLUE, 6719955, 10141901),
/*  17 */   YELLOW(4, "yellow", 16701501, MaterialColor.COLOR_YELLOW, 14602026, 16776960),
/*  18 */   LIME(5, "lime", 8439583, MaterialColor.COLOR_LIGHT_GREEN, 4312372, 12582656),
/*  19 */   PINK(6, "pink", 15961002, MaterialColor.COLOR_PINK, 14188952, 16738740),
/*  20 */   GRAY(7, "gray", 4673362, MaterialColor.COLOR_GRAY, 4408131, 8421504),
/*  21 */   LIGHT_GRAY(8, "light_gray", 10329495, MaterialColor.COLOR_LIGHT_GRAY, 11250603, 13882323),
/*  22 */   CYAN(9, "cyan", 1481884, MaterialColor.COLOR_CYAN, 2651799, 65535),
/*  23 */   PURPLE(10, "purple", 8991416, MaterialColor.COLOR_PURPLE, 8073150, 10494192),
/*  24 */   BLUE(11, "blue", 3949738, MaterialColor.COLOR_BLUE, 2437522, 255),
/*  25 */   BROWN(12, "brown", 8606770, MaterialColor.COLOR_BROWN, 5320730, 9127187),
/*  26 */   GREEN(13, "green", 6192150, MaterialColor.COLOR_GREEN, 3887386, 65280),
/*  27 */   RED(14, "red", 11546150, MaterialColor.COLOR_RED, 11743532, 16711680),
/*  28 */   BLACK(15, "black", 1908001, MaterialColor.COLOR_BLACK, 1973019, 0);
/*     */   
/*     */   static {
/*  31 */     BY_ID = (DyeColor[])Arrays.<DyeColor>stream(values()).sorted(Comparator.comparingInt(DyeColor::getId)).toArray(debug0 -> new DyeColor[debug0]);
/*  32 */     BY_FIREWORK_COLOR = new Int2ObjectOpenHashMap((Map)Arrays.<DyeColor>stream(values()).collect(Collectors.toMap(debug0 -> Integer.valueOf(debug0.fireworkColor), debug0 -> debug0)));
/*     */   }
/*     */   private final int id;
/*     */   private final String name;
/*     */   private final MaterialColor color;
/*     */   private final int textureDiffuseColor;
/*     */   private final int textureDiffuseColorBGR;
/*     */   private final float[] textureDiffuseColors;
/*     */   private final int fireworkColor;
/*     */   private final int textColor;
/*     */   
/*     */   DyeColor(int debug3, String debug4, int debug5, MaterialColor debug6, int debug7, int debug8) {
/*  44 */     this.id = debug3;
/*  45 */     this.name = debug4;
/*  46 */     this.textureDiffuseColor = debug5;
/*  47 */     this.color = debug6;
/*  48 */     this.textColor = debug8;
/*     */     
/*  50 */     int debug9 = (debug5 & 0xFF0000) >> 16;
/*  51 */     int debug10 = (debug5 & 0xFF00) >> 8;
/*  52 */     int debug11 = (debug5 & 0xFF) >> 0;
/*  53 */     this.textureDiffuseColorBGR = debug11 << 16 | debug10 << 8 | debug9 << 0;
/*  54 */     this.textureDiffuseColors = new float[] { debug9 / 255.0F, debug10 / 255.0F, debug11 / 255.0F };
/*  55 */     this.fireworkColor = debug7;
/*     */   }
/*     */   
/*     */   public int getId() {
/*  59 */     return this.id;
/*     */   }
/*     */   
/*     */   public String getName() {
/*  63 */     return this.name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float[] getTextureDiffuseColors() {
/*  71 */     return this.textureDiffuseColors;
/*     */   }
/*     */   
/*     */   public MaterialColor getMaterialColor() {
/*  75 */     return this.color;
/*     */   }
/*     */   
/*     */   public int getFireworkColor() {
/*  79 */     return this.fireworkColor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static DyeColor byId(int debug0) {
/*  87 */     if (debug0 < 0 || debug0 >= BY_ID.length) {
/*  88 */       debug0 = 0;
/*     */     }
/*  90 */     return BY_ID[debug0];
/*     */   }
/*     */   
/*     */   public static DyeColor byName(String debug0, DyeColor debug1) {
/*  94 */     for (DyeColor debug5 : values()) {
/*  95 */       if (debug5.name.equals(debug0)) {
/*  96 */         return debug5;
/*     */       }
/*     */     } 
/*     */     
/* 100 */     return debug1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 110 */     return this.name;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getSerializedName() {
/* 115 */     return this.name;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\item\DyeColor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */