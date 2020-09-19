/*     */ package net.minecraft.commands.arguments.coordinates;
/*     */ 
/*     */ import com.mojang.brigadier.StringReader;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import java.util.Objects;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.phys.Vec2;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ 
/*     */ public class LocalCoordinates
/*     */   implements Coordinates
/*     */ {
/*     */   private final double left;
/*     */   private final double up;
/*     */   private final double forwards;
/*     */   
/*     */   public LocalCoordinates(double debug1, double debug3, double debug5) {
/*  20 */     this.left = debug1;
/*  21 */     this.up = debug3;
/*  22 */     this.forwards = debug5;
/*     */   }
/*     */ 
/*     */   
/*     */   public Vec3 getPosition(CommandSourceStack debug1) {
/*  27 */     Vec2 debug2 = debug1.getRotation();
/*  28 */     Vec3 debug3 = debug1.getAnchor().apply(debug1);
/*  29 */     float debug4 = Mth.cos((debug2.y + 90.0F) * 0.017453292F);
/*  30 */     float debug5 = Mth.sin((debug2.y + 90.0F) * 0.017453292F);
/*  31 */     float debug6 = Mth.cos(-debug2.x * 0.017453292F);
/*  32 */     float debug7 = Mth.sin(-debug2.x * 0.017453292F);
/*  33 */     float debug8 = Mth.cos((-debug2.x + 90.0F) * 0.017453292F);
/*  34 */     float debug9 = Mth.sin((-debug2.x + 90.0F) * 0.017453292F);
/*  35 */     Vec3 debug10 = new Vec3((debug4 * debug6), debug7, (debug5 * debug6));
/*  36 */     Vec3 debug11 = new Vec3((debug4 * debug8), debug9, (debug5 * debug8));
/*  37 */     Vec3 debug12 = debug10.cross(debug11).scale(-1.0D);
/*  38 */     double debug13 = debug10.x * this.forwards + debug11.x * this.up + debug12.x * this.left;
/*  39 */     double debug15 = debug10.y * this.forwards + debug11.y * this.up + debug12.y * this.left;
/*  40 */     double debug17 = debug10.z * this.forwards + debug11.z * this.up + debug12.z * this.left;
/*  41 */     return new Vec3(debug3.x + debug13, debug3.y + debug15, debug3.z + debug17);
/*     */   }
/*     */ 
/*     */   
/*     */   public Vec2 getRotation(CommandSourceStack debug1) {
/*  46 */     return Vec2.ZERO;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isXRelative() {
/*  51 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isYRelative() {
/*  56 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isZRelative() {
/*  61 */     return true;
/*     */   }
/*     */   
/*     */   public static LocalCoordinates parse(StringReader debug0) throws CommandSyntaxException {
/*  65 */     int debug1 = debug0.getCursor();
/*  66 */     double debug2 = readDouble(debug0, debug1);
/*  67 */     if (!debug0.canRead() || debug0.peek() != ' ') {
/*  68 */       debug0.setCursor(debug1);
/*  69 */       throw Vec3Argument.ERROR_NOT_COMPLETE.createWithContext(debug0);
/*     */     } 
/*  71 */     debug0.skip();
/*  72 */     double debug4 = readDouble(debug0, debug1);
/*  73 */     if (!debug0.canRead() || debug0.peek() != ' ') {
/*  74 */       debug0.setCursor(debug1);
/*  75 */       throw Vec3Argument.ERROR_NOT_COMPLETE.createWithContext(debug0);
/*     */     } 
/*  77 */     debug0.skip();
/*  78 */     double debug6 = readDouble(debug0, debug1);
/*  79 */     return new LocalCoordinates(debug2, debug4, debug6);
/*     */   }
/*     */   
/*     */   private static double readDouble(StringReader debug0, int debug1) throws CommandSyntaxException {
/*  83 */     if (!debug0.canRead()) {
/*  84 */       throw WorldCoordinate.ERROR_EXPECTED_DOUBLE.createWithContext(debug0);
/*     */     }
/*     */     
/*  87 */     if (debug0.peek() != '^') {
/*  88 */       debug0.setCursor(debug1);
/*  89 */       throw Vec3Argument.ERROR_MIXED_TYPE.createWithContext(debug0);
/*     */     } 
/*  91 */     debug0.skip();
/*     */     
/*  93 */     return (debug0.canRead() && debug0.peek() != ' ') ? debug0.readDouble() : 0.0D;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object debug1) {
/*  98 */     if (this == debug1) {
/*  99 */       return true;
/*     */     }
/* 101 */     if (!(debug1 instanceof LocalCoordinates)) {
/* 102 */       return false;
/*     */     }
/*     */     
/* 105 */     LocalCoordinates debug2 = (LocalCoordinates)debug1;
/*     */     
/* 107 */     return (this.left == debug2.left && this.up == debug2.up && this.forwards == debug2.forwards);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 112 */     return Objects.hash(new Object[] { Double.valueOf(this.left), Double.valueOf(this.up), Double.valueOf(this.forwards) });
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\commands\arguments\coordinates\LocalCoordinates.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */