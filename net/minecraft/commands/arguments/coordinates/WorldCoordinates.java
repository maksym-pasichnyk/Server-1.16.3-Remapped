/*     */ package net.minecraft.commands.arguments.coordinates;
/*     */ 
/*     */ import com.mojang.brigadier.StringReader;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.world.phys.Vec2;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class WorldCoordinates implements Coordinates {
/*     */   private final WorldCoordinate x;
/*     */   private final WorldCoordinate y;
/*     */   private final WorldCoordinate z;
/*     */   
/*     */   public WorldCoordinates(WorldCoordinate debug1, WorldCoordinate debug2, WorldCoordinate debug3) {
/*  15 */     this.x = debug1;
/*  16 */     this.y = debug2;
/*  17 */     this.z = debug3;
/*     */   }
/*     */ 
/*     */   
/*     */   public Vec3 getPosition(CommandSourceStack debug1) {
/*  22 */     Vec3 debug2 = debug1.getPosition();
/*  23 */     return new Vec3(this.x.get(debug2.x), this.y.get(debug2.y), this.z.get(debug2.z));
/*     */   }
/*     */ 
/*     */   
/*     */   public Vec2 getRotation(CommandSourceStack debug1) {
/*  28 */     Vec2 debug2 = debug1.getRotation();
/*  29 */     return new Vec2((float)this.x.get(debug2.x), (float)this.y.get(debug2.y));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isXRelative() {
/*  34 */     return this.x.isRelative();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isYRelative() {
/*  39 */     return this.y.isRelative();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isZRelative() {
/*  44 */     return this.z.isRelative();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object debug1) {
/*  49 */     if (this == debug1) {
/*  50 */       return true;
/*     */     }
/*  52 */     if (!(debug1 instanceof WorldCoordinates)) {
/*  53 */       return false;
/*     */     }
/*     */     
/*  56 */     WorldCoordinates debug2 = (WorldCoordinates)debug1;
/*     */     
/*  58 */     if (!this.x.equals(debug2.x)) {
/*  59 */       return false;
/*     */     }
/*  61 */     if (!this.y.equals(debug2.y)) {
/*  62 */       return false;
/*     */     }
/*  64 */     return this.z.equals(debug2.z);
/*     */   }
/*     */   
/*     */   public static WorldCoordinates parseInt(StringReader debug0) throws CommandSyntaxException {
/*  68 */     int debug1 = debug0.getCursor();
/*  69 */     WorldCoordinate debug2 = WorldCoordinate.parseInt(debug0);
/*  70 */     if (!debug0.canRead() || debug0.peek() != ' ') {
/*  71 */       debug0.setCursor(debug1);
/*  72 */       throw Vec3Argument.ERROR_NOT_COMPLETE.createWithContext(debug0);
/*     */     } 
/*  74 */     debug0.skip();
/*  75 */     WorldCoordinate debug3 = WorldCoordinate.parseInt(debug0);
/*  76 */     if (!debug0.canRead() || debug0.peek() != ' ') {
/*  77 */       debug0.setCursor(debug1);
/*  78 */       throw Vec3Argument.ERROR_NOT_COMPLETE.createWithContext(debug0);
/*     */     } 
/*  80 */     debug0.skip();
/*  81 */     WorldCoordinate debug4 = WorldCoordinate.parseInt(debug0);
/*  82 */     return new WorldCoordinates(debug2, debug3, debug4);
/*     */   }
/*     */   
/*     */   public static WorldCoordinates parseDouble(StringReader debug0, boolean debug1) throws CommandSyntaxException {
/*  86 */     int debug2 = debug0.getCursor();
/*  87 */     WorldCoordinate debug3 = WorldCoordinate.parseDouble(debug0, debug1);
/*  88 */     if (!debug0.canRead() || debug0.peek() != ' ') {
/*  89 */       debug0.setCursor(debug2);
/*  90 */       throw Vec3Argument.ERROR_NOT_COMPLETE.createWithContext(debug0);
/*     */     } 
/*  92 */     debug0.skip();
/*  93 */     WorldCoordinate debug4 = WorldCoordinate.parseDouble(debug0, false);
/*  94 */     if (!debug0.canRead() || debug0.peek() != ' ') {
/*  95 */       debug0.setCursor(debug2);
/*  96 */       throw Vec3Argument.ERROR_NOT_COMPLETE.createWithContext(debug0);
/*     */     } 
/*  98 */     debug0.skip();
/*  99 */     WorldCoordinate debug5 = WorldCoordinate.parseDouble(debug0, debug1);
/* 100 */     return new WorldCoordinates(debug3, debug4, debug5);
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
/*     */   public static WorldCoordinates current() {
/* 112 */     return new WorldCoordinates(new WorldCoordinate(true, 0.0D), new WorldCoordinate(true, 0.0D), new WorldCoordinate(true, 0.0D));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 117 */     int debug1 = this.x.hashCode();
/* 118 */     debug1 = 31 * debug1 + this.y.hashCode();
/* 119 */     debug1 = 31 * debug1 + this.z.hashCode();
/* 120 */     return debug1;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\commands\arguments\coordinates\WorldCoordinates.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */