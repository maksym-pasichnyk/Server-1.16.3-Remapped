/*    */ package net.minecraft.data.models.model;
/*    */ 
/*    */ import javax.annotation.Nullable;
/*    */ 
/*    */ public final class TextureSlot {
/*  6 */   public static final TextureSlot ALL = create("all");
/*  7 */   public static final TextureSlot TEXTURE = create("texture", ALL);
/*  8 */   public static final TextureSlot PARTICLE = create("particle", TEXTURE);
/*  9 */   public static final TextureSlot END = create("end", ALL);
/* 10 */   public static final TextureSlot BOTTOM = create("bottom", END);
/* 11 */   public static final TextureSlot TOP = create("top", END);
/* 12 */   public static final TextureSlot FRONT = create("front", ALL);
/* 13 */   public static final TextureSlot BACK = create("back", ALL);
/* 14 */   public static final TextureSlot SIDE = create("side", ALL);
/* 15 */   public static final TextureSlot NORTH = create("north", SIDE);
/* 16 */   public static final TextureSlot SOUTH = create("south", SIDE);
/* 17 */   public static final TextureSlot EAST = create("east", SIDE);
/* 18 */   public static final TextureSlot WEST = create("west", SIDE);
/* 19 */   public static final TextureSlot UP = create("up");
/* 20 */   public static final TextureSlot DOWN = create("down");
/* 21 */   public static final TextureSlot CROSS = create("cross");
/* 22 */   public static final TextureSlot PLANT = create("plant");
/* 23 */   public static final TextureSlot WALL = create("wall", ALL);
/* 24 */   public static final TextureSlot RAIL = create("rail");
/* 25 */   public static final TextureSlot WOOL = create("wool");
/* 26 */   public static final TextureSlot PATTERN = create("pattern");
/* 27 */   public static final TextureSlot PANE = create("pane");
/* 28 */   public static final TextureSlot EDGE = create("edge");
/* 29 */   public static final TextureSlot FAN = create("fan");
/* 30 */   public static final TextureSlot STEM = create("stem");
/* 31 */   public static final TextureSlot UPPER_STEM = create("upperstem");
/* 32 */   public static final TextureSlot CROP = create("crop");
/* 33 */   public static final TextureSlot DIRT = create("dirt");
/* 34 */   public static final TextureSlot FIRE = create("fire");
/* 35 */   public static final TextureSlot LANTERN = create("lantern");
/* 36 */   public static final TextureSlot PLATFORM = create("platform");
/* 37 */   public static final TextureSlot UNSTICKY = create("unsticky");
/* 38 */   public static final TextureSlot TORCH = create("torch");
/* 39 */   public static final TextureSlot LAYER0 = create("layer0");
/* 40 */   public static final TextureSlot LIT_LOG = create("lit_log");
/*    */   
/*    */   private final String id;
/*    */   
/*    */   @Nullable
/*    */   private final TextureSlot parent;
/*    */   
/*    */   private static TextureSlot create(String debug0) {
/* 48 */     return new TextureSlot(debug0, null);
/*    */   }
/*    */   
/*    */   private static TextureSlot create(String debug0, TextureSlot debug1) {
/* 52 */     return new TextureSlot(debug0, debug1);
/*    */   }
/*    */   
/*    */   private TextureSlot(String debug1, @Nullable TextureSlot debug2) {
/* 56 */     this.id = debug1;
/* 57 */     this.parent = debug2;
/*    */   }
/*    */   
/*    */   public String getId() {
/* 61 */     return this.id;
/*    */   }
/*    */   
/*    */   @Nullable
/*    */   public TextureSlot getParent() {
/* 66 */     return this.parent;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 71 */     return "#" + this.id;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\data\models\model\TextureSlot.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */