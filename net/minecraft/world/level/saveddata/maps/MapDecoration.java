/*     */ package net.minecraft.world.level.saveddata.maps;
/*     */ 
/*     */ public class MapDecoration {
/*     */   private final Type type;
/*     */   private byte x;
/*     */   private byte y;
/*     */   private byte rot;
/*     */   private final Component name;
/*     */   
/*     */   public enum Type {
/*  11 */     PLAYER(false),
/*  12 */     FRAME(true),
/*  13 */     RED_MARKER(false),
/*  14 */     BLUE_MARKER(false),
/*  15 */     TARGET_X(true),
/*  16 */     TARGET_POINT(true),
/*  17 */     PLAYER_OFF_MAP(false),
/*  18 */     PLAYER_OFF_LIMITS(false),
/*  19 */     MANSION(true, 5393476),
/*  20 */     MONUMENT(true, 3830373),
/*  21 */     BANNER_WHITE(true),
/*  22 */     BANNER_ORANGE(true),
/*  23 */     BANNER_MAGENTA(true),
/*  24 */     BANNER_LIGHT_BLUE(true),
/*  25 */     BANNER_YELLOW(true),
/*  26 */     BANNER_LIME(true),
/*  27 */     BANNER_PINK(true),
/*  28 */     BANNER_GRAY(true),
/*  29 */     BANNER_LIGHT_GRAY(true),
/*  30 */     BANNER_CYAN(true),
/*  31 */     BANNER_PURPLE(true),
/*  32 */     BANNER_BLUE(true),
/*  33 */     BANNER_BROWN(true),
/*  34 */     BANNER_GREEN(true),
/*  35 */     BANNER_RED(true),
/*  36 */     BANNER_BLACK(true),
/*  37 */     RED_X(true);
/*     */ 
/*     */     
/*     */     private final byte icon;
/*     */ 
/*     */     
/*     */     private final boolean renderedOnFrame;
/*     */     
/*     */     private final int mapColor;
/*     */ 
/*     */     
/*     */     Type(boolean debug3, int debug4) {
/*  49 */       this.icon = (byte)ordinal();
/*  50 */       this.renderedOnFrame = debug3;
/*  51 */       this.mapColor = debug4;
/*     */     }
/*     */     
/*     */     public byte getIcon() {
/*  55 */       return this.icon;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean hasMapColor() {
/*  63 */       return (this.mapColor >= 0);
/*     */     }
/*     */     
/*     */     public int getMapColor() {
/*  67 */       return this.mapColor;
/*     */     }
/*     */     
/*     */     public static Type byIcon(byte debug0) {
/*  71 */       return values()[Mth.clamp(debug0, 0, (values()).length - 1)];
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MapDecoration(Type debug1, byte debug2, byte debug3, byte debug4, @Nullable Component debug5) {
/*  82 */     this.type = debug1;
/*  83 */     this.x = debug2;
/*  84 */     this.y = debug3;
/*  85 */     this.rot = debug4;
/*  86 */     this.name = debug5;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Type getType() {
/*  94 */     return this.type;
/*     */   }
/*     */   
/*     */   public byte getX() {
/*  98 */     return this.x;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte getY() {
/* 106 */     return this.y;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte getRot() {
/* 114 */     return this.rot;
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
/*     */   @Nullable
/*     */   public Component getName() {
/* 127 */     return this.name;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object debug1) {
/* 132 */     if (this == debug1) {
/* 133 */       return true;
/*     */     }
/* 135 */     if (!(debug1 instanceof MapDecoration)) {
/* 136 */       return false;
/*     */     }
/*     */     
/* 139 */     MapDecoration debug2 = (MapDecoration)debug1;
/*     */     
/* 141 */     if (this.type != debug2.type) {
/* 142 */       return false;
/*     */     }
/* 144 */     if (this.rot != debug2.rot) {
/* 145 */       return false;
/*     */     }
/* 147 */     if (this.x != debug2.x) {
/* 148 */       return false;
/*     */     }
/* 150 */     if (this.y != debug2.y) {
/* 151 */       return false;
/*     */     }
/* 153 */     if (!Objects.equals(this.name, debug2.name)) {
/* 154 */       return false;
/*     */     }
/*     */     
/* 157 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 162 */     int debug1 = this.type.getIcon();
/* 163 */     debug1 = 31 * debug1 + this.x;
/* 164 */     debug1 = 31 * debug1 + this.y;
/* 165 */     debug1 = 31 * debug1 + this.rot;
/* 166 */     debug1 = 31 * debug1 + Objects.hashCode(this.name);
/* 167 */     return debug1;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\saveddata\maps\MapDecoration.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */