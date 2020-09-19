/*     */ package net.minecraft.world.level.saveddata.maps;
/*     */ 
/*     */ import java.util.Objects;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.NbtUtils;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.world.item.DyeColor;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.block.entity.BannerBlockEntity;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ 
/*     */ public class MapBanner
/*     */ {
/*     */   private final BlockPos pos;
/*     */   
/*     */   public MapBanner(BlockPos debug1, DyeColor debug2, @Nullable Component debug3) {
/*  22 */     this.pos = debug1;
/*  23 */     this.color = debug2;
/*  24 */     this.name = debug3;
/*     */   } private final DyeColor color; @Nullable
/*     */   private final Component name;
/*     */   public static MapBanner load(CompoundTag debug0) {
/*  28 */     BlockPos debug1 = NbtUtils.readBlockPos(debug0.getCompound("Pos"));
/*  29 */     DyeColor debug2 = DyeColor.byName(debug0.getString("Color"), DyeColor.WHITE);
/*  30 */     MutableComponent mutableComponent = debug0.contains("Name") ? Component.Serializer.fromJson(debug0.getString("Name")) : null;
/*  31 */     return new MapBanner(debug1, debug2, (Component)mutableComponent);
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public static MapBanner fromWorld(BlockGetter debug0, BlockPos debug1) {
/*  36 */     BlockEntity debug2 = debug0.getBlockEntity(debug1);
/*  37 */     if (debug2 instanceof BannerBlockEntity) {
/*  38 */       BannerBlockEntity debug3 = (BannerBlockEntity)debug2;
/*  39 */       DyeColor debug4 = debug3.getBaseColor(() -> debug0.getBlockState(debug1));
/*  40 */       Component debug5 = debug3.hasCustomName() ? debug3.getCustomName() : null;
/*  41 */       return new MapBanner(debug1, debug4, debug5);
/*     */     } 
/*  43 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockPos getPos() {
/*  48 */     return this.pos;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MapDecoration.Type getDecoration() {
/*  56 */     switch (this.color) {
/*     */       case WHITE:
/*  58 */         return MapDecoration.Type.BANNER_WHITE;
/*     */       case ORANGE:
/*  60 */         return MapDecoration.Type.BANNER_ORANGE;
/*     */       case MAGENTA:
/*  62 */         return MapDecoration.Type.BANNER_MAGENTA;
/*     */       case LIGHT_BLUE:
/*  64 */         return MapDecoration.Type.BANNER_LIGHT_BLUE;
/*     */       case YELLOW:
/*  66 */         return MapDecoration.Type.BANNER_YELLOW;
/*     */       case LIME:
/*  68 */         return MapDecoration.Type.BANNER_LIME;
/*     */       case PINK:
/*  70 */         return MapDecoration.Type.BANNER_PINK;
/*     */       case GRAY:
/*  72 */         return MapDecoration.Type.BANNER_GRAY;
/*     */       case LIGHT_GRAY:
/*  74 */         return MapDecoration.Type.BANNER_LIGHT_GRAY;
/*     */       case CYAN:
/*  76 */         return MapDecoration.Type.BANNER_CYAN;
/*     */       case PURPLE:
/*  78 */         return MapDecoration.Type.BANNER_PURPLE;
/*     */       case BLUE:
/*  80 */         return MapDecoration.Type.BANNER_BLUE;
/*     */       case BROWN:
/*  82 */         return MapDecoration.Type.BANNER_BROWN;
/*     */       case GREEN:
/*  84 */         return MapDecoration.Type.BANNER_GREEN;
/*     */       case RED:
/*  86 */         return MapDecoration.Type.BANNER_RED;
/*     */     } 
/*     */     
/*  89 */     return MapDecoration.Type.BANNER_BLACK;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Component getName() {
/*  95 */     return this.name;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object debug1) {
/* 100 */     if (this == debug1) {
/* 101 */       return true;
/*     */     }
/* 103 */     if (debug1 == null || getClass() != debug1.getClass()) {
/* 104 */       return false;
/*     */     }
/* 106 */     MapBanner debug2 = (MapBanner)debug1;
/* 107 */     return (Objects.equals(this.pos, debug2.pos) && this.color == debug2.color && Objects.equals(this.name, debug2.name));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 112 */     return Objects.hash(new Object[] { this.pos, this.color, this.name });
/*     */   }
/*     */   
/*     */   public CompoundTag save() {
/* 116 */     CompoundTag debug1 = new CompoundTag();
/*     */     
/* 118 */     debug1.put("Pos", (Tag)NbtUtils.writeBlockPos(this.pos));
/* 119 */     debug1.putString("Color", this.color.getName());
/*     */     
/* 121 */     if (this.name != null) {
/* 122 */       debug1.putString("Name", Component.Serializer.toJson(this.name));
/*     */     }
/*     */     
/* 125 */     return debug1;
/*     */   }
/*     */   
/*     */   public String getId() {
/* 129 */     return "banner-" + this.pos.getX() + "," + this.pos.getY() + "," + this.pos.getZ();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\saveddata\maps\MapBanner.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */