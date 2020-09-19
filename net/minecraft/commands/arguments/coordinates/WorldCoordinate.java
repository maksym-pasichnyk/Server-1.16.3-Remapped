/*     */ package net.minecraft.commands.arguments.coordinates;
/*     */ 
/*     */ import com.mojang.brigadier.Message;
/*     */ import com.mojang.brigadier.StringReader;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*     */ import net.minecraft.network.chat.TranslatableComponent;
/*     */ 
/*     */ public class WorldCoordinate
/*     */ {
/*  11 */   public static final SimpleCommandExceptionType ERROR_EXPECTED_DOUBLE = new SimpleCommandExceptionType((Message)new TranslatableComponent("argument.pos.missing.double"));
/*  12 */   public static final SimpleCommandExceptionType ERROR_EXPECTED_INT = new SimpleCommandExceptionType((Message)new TranslatableComponent("argument.pos.missing.int"));
/*     */   
/*     */   private final boolean relative;
/*     */   private final double value;
/*     */   
/*     */   public WorldCoordinate(boolean debug1, double debug2) {
/*  18 */     this.relative = debug1;
/*  19 */     this.value = debug2;
/*     */   }
/*     */   
/*     */   public double get(double debug1) {
/*  23 */     if (this.relative) {
/*  24 */       return this.value + debug1;
/*     */     }
/*  26 */     return this.value;
/*     */   }
/*     */ 
/*     */   
/*     */   public static WorldCoordinate parseDouble(StringReader debug0, boolean debug1) throws CommandSyntaxException {
/*  31 */     if (debug0.canRead() && debug0.peek() == '^') {
/*  32 */       throw Vec3Argument.ERROR_MIXED_TYPE.createWithContext(debug0);
/*     */     }
/*     */     
/*  35 */     if (!debug0.canRead()) {
/*  36 */       throw ERROR_EXPECTED_DOUBLE.createWithContext(debug0);
/*     */     }
/*     */     
/*  39 */     boolean debug2 = isRelative(debug0);
/*  40 */     int debug3 = debug0.getCursor();
/*  41 */     double debug4 = (debug0.canRead() && debug0.peek() != ' ') ? debug0.readDouble() : 0.0D;
/*  42 */     String debug6 = debug0.getString().substring(debug3, debug0.getCursor());
/*     */     
/*  44 */     if (debug2 && debug6.isEmpty()) {
/*  45 */       return new WorldCoordinate(true, 0.0D);
/*     */     }
/*     */     
/*  48 */     if (!debug6.contains(".") && !debug2 && debug1) {
/*  49 */       debug4 += 0.5D;
/*     */     }
/*     */     
/*  52 */     return new WorldCoordinate(debug2, debug4);
/*     */   }
/*     */   public static WorldCoordinate parseInt(StringReader debug0) throws CommandSyntaxException {
/*     */     double debug2;
/*  56 */     if (debug0.canRead() && debug0.peek() == '^') {
/*  57 */       throw Vec3Argument.ERROR_MIXED_TYPE.createWithContext(debug0);
/*     */     }
/*     */     
/*  60 */     if (!debug0.canRead()) {
/*  61 */       throw ERROR_EXPECTED_INT.createWithContext(debug0);
/*     */     }
/*     */     
/*  64 */     boolean debug1 = isRelative(debug0);
/*     */     
/*  66 */     if (debug0.canRead() && debug0.peek() != ' ') {
/*  67 */       debug2 = debug1 ? debug0.readDouble() : debug0.readInt();
/*     */     } else {
/*  69 */       debug2 = 0.0D;
/*     */     } 
/*  71 */     return new WorldCoordinate(debug1, debug2);
/*     */   }
/*     */   
/*     */   public static boolean isRelative(StringReader debug0) {
/*     */     boolean debug1;
/*  76 */     if (debug0.peek() == '~') {
/*  77 */       debug1 = true;
/*  78 */       debug0.skip();
/*     */     } else {
/*  80 */       debug1 = false;
/*     */     } 
/*  82 */     return debug1;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object debug1) {
/*  87 */     if (this == debug1) {
/*  88 */       return true;
/*     */     }
/*  90 */     if (!(debug1 instanceof WorldCoordinate)) {
/*  91 */       return false;
/*     */     }
/*     */     
/*  94 */     WorldCoordinate debug2 = (WorldCoordinate)debug1;
/*     */     
/*  96 */     if (this.relative != debug2.relative) {
/*  97 */       return false;
/*     */     }
/*  99 */     return (Double.compare(debug2.value, this.value) == 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 106 */     int debug1 = this.relative ? 1 : 0;
/* 107 */     long debug2 = Double.doubleToLongBits(this.value);
/* 108 */     debug1 = 31 * debug1 + (int)(debug2 ^ debug2 >>> 32L);
/* 109 */     return debug1;
/*     */   }
/*     */   
/*     */   public boolean isRelative() {
/* 113 */     return this.relative;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\commands\arguments\coordinates\WorldCoordinate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */