/*     */ package net.minecraft.world.level.material;
/*     */ 
/*     */ public final class Material {
/*   4 */   public static final Material AIR = (new Builder(MaterialColor.NONE)).noCollider().notSolidBlocking().nonSolid().replaceable().build();
/*   5 */   public static final Material STRUCTURAL_AIR = (new Builder(MaterialColor.NONE)).noCollider().notSolidBlocking().nonSolid().replaceable().build();
/*   6 */   public static final Material PORTAL = (new Builder(MaterialColor.NONE)).noCollider().notSolidBlocking().nonSolid().notPushable().build();
/*   7 */   public static final Material CLOTH_DECORATION = (new Builder(MaterialColor.WOOL)).noCollider().notSolidBlocking().nonSolid().flammable().build();
/*   8 */   public static final Material PLANT = (new Builder(MaterialColor.PLANT)).noCollider().notSolidBlocking().nonSolid().destroyOnPush().build();
/*   9 */   public static final Material WATER_PLANT = (new Builder(MaterialColor.WATER)).noCollider().notSolidBlocking().nonSolid().destroyOnPush().build();
/*  10 */   public static final Material REPLACEABLE_PLANT = (new Builder(MaterialColor.PLANT)).noCollider().notSolidBlocking().nonSolid().destroyOnPush().replaceable().flammable().build();
/*  11 */   public static final Material REPLACEABLE_FIREPROOF_PLANT = (new Builder(MaterialColor.PLANT)).noCollider().notSolidBlocking().nonSolid().destroyOnPush().replaceable().build();
/*  12 */   public static final Material REPLACEABLE_WATER_PLANT = (new Builder(MaterialColor.WATER)).noCollider().notSolidBlocking().nonSolid().destroyOnPush().replaceable().build();
/*  13 */   public static final Material WATER = (new Builder(MaterialColor.WATER)).noCollider().notSolidBlocking().nonSolid().destroyOnPush().replaceable().liquid().build();
/*  14 */   public static final Material BUBBLE_COLUMN = (new Builder(MaterialColor.WATER)).noCollider().notSolidBlocking().nonSolid().destroyOnPush().replaceable().liquid().build();
/*  15 */   public static final Material LAVA = (new Builder(MaterialColor.FIRE)).noCollider().notSolidBlocking().nonSolid().destroyOnPush().replaceable().liquid().build();
/*  16 */   public static final Material TOP_SNOW = (new Builder(MaterialColor.SNOW)).noCollider().notSolidBlocking().nonSolid().destroyOnPush().replaceable().build();
/*  17 */   public static final Material FIRE = (new Builder(MaterialColor.NONE)).noCollider().notSolidBlocking().nonSolid().destroyOnPush().replaceable().build();
/*  18 */   public static final Material DECORATION = (new Builder(MaterialColor.NONE)).noCollider().notSolidBlocking().nonSolid().destroyOnPush().build();
/*  19 */   public static final Material WEB = (new Builder(MaterialColor.WOOL)).noCollider().notSolidBlocking().destroyOnPush().build();
/*     */   
/*  21 */   public static final Material BUILDABLE_GLASS = (new Builder(MaterialColor.NONE)).build();
/*  22 */   public static final Material CLAY = (new Builder(MaterialColor.CLAY)).build();
/*  23 */   public static final Material DIRT = (new Builder(MaterialColor.DIRT)).build();
/*  24 */   public static final Material GRASS = (new Builder(MaterialColor.GRASS)).build();
/*  25 */   public static final Material ICE_SOLID = (new Builder(MaterialColor.ICE)).build();
/*  26 */   public static final Material SAND = (new Builder(MaterialColor.SAND)).build();
/*  27 */   public static final Material SPONGE = (new Builder(MaterialColor.COLOR_YELLOW)).build();
/*  28 */   public static final Material SHULKER_SHELL = (new Builder(MaterialColor.COLOR_PURPLE)).build();
/*     */   
/*  30 */   public static final Material WOOD = (new Builder(MaterialColor.WOOD)).flammable().build();
/*  31 */   public static final Material NETHER_WOOD = (new Builder(MaterialColor.WOOD)).build();
/*  32 */   public static final Material BAMBOO_SAPLING = (new Builder(MaterialColor.WOOD)).flammable().destroyOnPush().noCollider().build();
/*  33 */   public static final Material BAMBOO = (new Builder(MaterialColor.WOOD)).flammable().destroyOnPush().build();
/*  34 */   public static final Material WOOL = (new Builder(MaterialColor.WOOL)).flammable().build();
/*  35 */   public static final Material EXPLOSIVE = (new Builder(MaterialColor.FIRE)).flammable().notSolidBlocking().build();
/*  36 */   public static final Material LEAVES = (new Builder(MaterialColor.PLANT)).flammable().notSolidBlocking().destroyOnPush().build();
/*  37 */   public static final Material GLASS = (new Builder(MaterialColor.NONE)).notSolidBlocking().build();
/*  38 */   public static final Material ICE = (new Builder(MaterialColor.ICE)).notSolidBlocking().build();
/*  39 */   public static final Material CACTUS = (new Builder(MaterialColor.PLANT)).notSolidBlocking().destroyOnPush().build();
/*     */   
/*  41 */   public static final Material STONE = (new Builder(MaterialColor.STONE)).build();
/*  42 */   public static final Material METAL = (new Builder(MaterialColor.METAL)).build();
/*  43 */   public static final Material SNOW = (new Builder(MaterialColor.SNOW)).build();
/*     */   
/*  45 */   public static final Material HEAVY_METAL = (new Builder(MaterialColor.METAL)).notPushable().build();
/*  46 */   public static final Material BARRIER = (new Builder(MaterialColor.NONE)).notPushable().build();
/*     */   
/*  48 */   public static final Material PISTON = (new Builder(MaterialColor.STONE)).notPushable().build();
/*     */   
/*  50 */   public static final Material CORAL = (new Builder(MaterialColor.PLANT)).destroyOnPush().build();
/*  51 */   public static final Material VEGETABLE = (new Builder(MaterialColor.PLANT)).destroyOnPush().build();
/*  52 */   public static final Material EGG = (new Builder(MaterialColor.PLANT)).destroyOnPush().build();
/*  53 */   public static final Material CAKE = (new Builder(MaterialColor.NONE)).destroyOnPush().build();
/*     */   
/*     */   private final MaterialColor color;
/*     */   private final PushReaction pushReaction;
/*     */   private final boolean blocksMotion;
/*     */   private final boolean flammable;
/*     */   private final boolean liquid;
/*     */   private final boolean solidBlocking;
/*     */   private final boolean replaceable;
/*     */   private final boolean solid;
/*     */   
/*     */   public Material(MaterialColor debug1, boolean debug2, boolean debug3, boolean debug4, boolean debug5, boolean debug6, boolean debug7, PushReaction debug8) {
/*  65 */     this.color = debug1;
/*  66 */     this.liquid = debug2;
/*  67 */     this.solid = debug3;
/*  68 */     this.blocksMotion = debug4;
/*  69 */     this.solidBlocking = debug5;
/*  70 */     this.flammable = debug6;
/*  71 */     this.replaceable = debug7;
/*  72 */     this.pushReaction = debug8;
/*     */   }
/*     */   
/*     */   public boolean isLiquid() {
/*  76 */     return this.liquid;
/*     */   }
/*     */   
/*     */   public boolean isSolid() {
/*  80 */     return this.solid;
/*     */   }
/*     */   
/*     */   public boolean blocksMotion() {
/*  84 */     return this.blocksMotion;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isFlammable() {
/*  89 */     return this.flammable;
/*     */   }
/*     */   
/*     */   public boolean isReplaceable() {
/*  93 */     return this.replaceable;
/*     */   }
/*     */   
/*     */   public boolean isSolidBlocking() {
/*  97 */     return this.solidBlocking;
/*     */   }
/*     */   
/*     */   public PushReaction getPushReaction() {
/* 101 */     return this.pushReaction;
/*     */   }
/*     */   
/*     */   public MaterialColor getColor() {
/* 105 */     return this.color;
/*     */   }
/*     */   
/*     */   public static class Builder {
/* 109 */     private PushReaction pushReaction = PushReaction.NORMAL;
/*     */     private boolean blocksMotion = true;
/*     */     private boolean flammable;
/*     */     private boolean liquid;
/*     */     private boolean replaceable;
/*     */     private boolean solid = true;
/*     */     private final MaterialColor color;
/*     */     private boolean solidBlocking = true;
/*     */     
/*     */     public Builder(MaterialColor debug1) {
/* 119 */       this.color = debug1;
/*     */     }
/*     */     
/*     */     public Builder liquid() {
/* 123 */       this.liquid = true;
/* 124 */       return this;
/*     */     }
/*     */     
/*     */     public Builder nonSolid() {
/* 128 */       this.solid = false;
/* 129 */       return this;
/*     */     }
/*     */     
/*     */     public Builder noCollider() {
/* 133 */       this.blocksMotion = false;
/* 134 */       return this;
/*     */     }
/*     */     
/*     */     private Builder notSolidBlocking() {
/* 138 */       this.solidBlocking = false;
/* 139 */       return this;
/*     */     }
/*     */     
/*     */     protected Builder flammable() {
/* 143 */       this.flammable = true;
/* 144 */       return this;
/*     */     }
/*     */     
/*     */     public Builder replaceable() {
/* 148 */       this.replaceable = true;
/* 149 */       return this;
/*     */     }
/*     */     
/*     */     protected Builder destroyOnPush() {
/* 153 */       this.pushReaction = PushReaction.DESTROY;
/* 154 */       return this;
/*     */     }
/*     */     
/*     */     protected Builder notPushable() {
/* 158 */       this.pushReaction = PushReaction.BLOCK;
/* 159 */       return this;
/*     */     }
/*     */     
/*     */     public Material build() {
/* 163 */       return new Material(this.color, this.liquid, this.solid, this.blocksMotion, this.solidBlocking, this.flammable, this.replaceable, this.pushReaction);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\material\Material.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */