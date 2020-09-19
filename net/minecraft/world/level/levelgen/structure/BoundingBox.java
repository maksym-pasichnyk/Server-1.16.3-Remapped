/*     */ package net.minecraft.world.level.levelgen.structure;
/*     */ 
/*     */ import com.google.common.base.MoreObjects;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.nbt.IntArrayTag;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BoundingBox
/*     */ {
/*     */   public int x0;
/*     */   public int y0;
/*     */   public int z0;
/*     */   public int x1;
/*     */   public int y1;
/*     */   public int z1;
/*     */   
/*     */   public BoundingBox() {}
/*     */   
/*     */   public BoundingBox(int[] debug1) {
/*  23 */     if (debug1.length == 6) {
/*  24 */       this.x0 = debug1[0];
/*  25 */       this.y0 = debug1[1];
/*  26 */       this.z0 = debug1[2];
/*  27 */       this.x1 = debug1[3];
/*  28 */       this.y1 = debug1[4];
/*  29 */       this.z1 = debug1[5];
/*     */     } 
/*     */   }
/*     */   
/*     */   public static BoundingBox getUnknownBox() {
/*  34 */     return new BoundingBox(2147483647, 2147483647, 2147483647, -2147483648, -2147483648, -2147483648);
/*     */   }
/*     */   
/*     */   public static BoundingBox infinite() {
/*  38 */     return new BoundingBox(-2147483648, -2147483648, -2147483648, 2147483647, 2147483647, 2147483647);
/*     */   }
/*     */   
/*     */   public static BoundingBox orientBox(int debug0, int debug1, int debug2, int debug3, int debug4, int debug5, int debug6, int debug7, int debug8, Direction debug9) {
/*  42 */     switch (debug9) {
/*     */       default:
/*  44 */         return new BoundingBox(debug0 + debug3, debug1 + debug4, debug2 + debug5, debug0 + debug6 - 1 + debug3, debug1 + debug7 - 1 + debug4, debug2 + debug8 - 1 + debug5);
/*     */       
/*     */       case NORTH:
/*  47 */         return new BoundingBox(debug0 + debug3, debug1 + debug4, debug2 - debug8 + 1 + debug5, debug0 + debug6 - 1 + debug3, debug1 + debug7 - 1 + debug4, debug2 + debug5);
/*     */       
/*     */       case SOUTH:
/*  50 */         return new BoundingBox(debug0 + debug3, debug1 + debug4, debug2 + debug5, debug0 + debug6 - 1 + debug3, debug1 + debug7 - 1 + debug4, debug2 + debug8 - 1 + debug5);
/*     */       
/*     */       case WEST:
/*  53 */         return new BoundingBox(debug0 - debug8 + 1 + debug5, debug1 + debug4, debug2 + debug3, debug0 + debug5, debug1 + debug7 - 1 + debug4, debug2 + debug6 - 1 + debug3);
/*     */       case EAST:
/*     */         break;
/*  56 */     }  return new BoundingBox(debug0 + debug5, debug1 + debug4, debug2 + debug3, debug0 + debug8 - 1 + debug5, debug1 + debug7 - 1 + debug4, debug2 + debug6 - 1 + debug3);
/*     */   }
/*     */ 
/*     */   
/*     */   public static BoundingBox createProper(int debug0, int debug1, int debug2, int debug3, int debug4, int debug5) {
/*  61 */     return new BoundingBox(Math.min(debug0, debug3), Math.min(debug1, debug4), Math.min(debug2, debug5), Math.max(debug0, debug3), Math.max(debug1, debug4), Math.max(debug2, debug5));
/*     */   }
/*     */   
/*     */   public BoundingBox(BoundingBox debug1) {
/*  65 */     this.x0 = debug1.x0;
/*  66 */     this.y0 = debug1.y0;
/*  67 */     this.z0 = debug1.z0;
/*  68 */     this.x1 = debug1.x1;
/*  69 */     this.y1 = debug1.y1;
/*  70 */     this.z1 = debug1.z1;
/*     */   }
/*     */   
/*     */   public BoundingBox(int debug1, int debug2, int debug3, int debug4, int debug5, int debug6) {
/*  74 */     this.x0 = debug1;
/*  75 */     this.y0 = debug2;
/*  76 */     this.z0 = debug3;
/*  77 */     this.x1 = debug4;
/*  78 */     this.y1 = debug5;
/*  79 */     this.z1 = debug6;
/*     */   }
/*     */   
/*     */   public BoundingBox(Vec3i debug1, Vec3i debug2) {
/*  83 */     this.x0 = Math.min(debug1.getX(), debug2.getX());
/*  84 */     this.y0 = Math.min(debug1.getY(), debug2.getY());
/*  85 */     this.z0 = Math.min(debug1.getZ(), debug2.getZ());
/*  86 */     this.x1 = Math.max(debug1.getX(), debug2.getX());
/*  87 */     this.y1 = Math.max(debug1.getY(), debug2.getY());
/*  88 */     this.z1 = Math.max(debug1.getZ(), debug2.getZ());
/*     */   }
/*     */   
/*     */   public BoundingBox(int debug1, int debug2, int debug3, int debug4) {
/*  92 */     this.x0 = debug1;
/*  93 */     this.z0 = debug2;
/*  94 */     this.x1 = debug3;
/*  95 */     this.z1 = debug4;
/*     */ 
/*     */     
/*  98 */     this.y0 = 1;
/*  99 */     this.y1 = 512;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean intersects(BoundingBox debug1) {
/* 107 */     return (this.x1 >= debug1.x0 && this.x0 <= debug1.x1 && this.z1 >= debug1.z0 && this.z0 <= debug1.z1 && this.y1 >= debug1.y0 && this.y0 <= debug1.y1);
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
/*     */   public boolean intersects(int debug1, int debug2, int debug3, int debug4) {
/* 119 */     return (this.x1 >= debug1 && this.x0 <= debug3 && this.z1 >= debug2 && this.z0 <= debug4);
/*     */   }
/*     */   
/*     */   public void expand(BoundingBox debug1) {
/* 123 */     this.x0 = Math.min(this.x0, debug1.x0);
/* 124 */     this.y0 = Math.min(this.y0, debug1.y0);
/* 125 */     this.z0 = Math.min(this.z0, debug1.z0);
/* 126 */     this.x1 = Math.max(this.x1, debug1.x1);
/* 127 */     this.y1 = Math.max(this.y1, debug1.y1);
/* 128 */     this.z1 = Math.max(this.z1, debug1.z1);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void move(int debug1, int debug2, int debug3) {
/* 165 */     this.x0 += debug1;
/* 166 */     this.y0 += debug2;
/* 167 */     this.z0 += debug3;
/* 168 */     this.x1 += debug1;
/* 169 */     this.y1 += debug2;
/* 170 */     this.z1 += debug3;
/*     */   }
/*     */   
/*     */   public BoundingBox moved(int debug1, int debug2, int debug3) {
/* 174 */     return new BoundingBox(this.x0 + debug1, this.y0 + debug2, this.z0 + debug3, this.x1 + debug1, this.y1 + debug2, this.z1 + debug3);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void move(Vec3i debug1) {
/* 185 */     move(debug1.getX(), debug1.getY(), debug1.getZ());
/*     */   }
/*     */   
/*     */   public boolean isInside(Vec3i debug1) {
/* 189 */     return (debug1.getX() >= this.x0 && debug1.getX() <= this.x1 && debug1.getZ() >= this.z0 && debug1.getZ() <= this.z1 && debug1.getY() >= this.y0 && debug1.getY() <= this.y1);
/*     */   }
/*     */   
/*     */   public Vec3i getLength() {
/* 193 */     return new Vec3i(this.x1 - this.x0, this.y1 - this.y0, this.z1 - this.z0);
/*     */   }
/*     */   
/*     */   public int getXSpan() {
/* 197 */     return this.x1 - this.x0 + 1;
/*     */   }
/*     */   
/*     */   public int getYSpan() {
/* 201 */     return this.y1 - this.y0 + 1;
/*     */   }
/*     */   
/*     */   public int getZSpan() {
/* 205 */     return this.z1 - this.z0 + 1;
/*     */   }
/*     */   
/*     */   public Vec3i getCenter() {
/* 209 */     return (Vec3i)new BlockPos(this.x0 + (this.x1 - this.x0 + 1) / 2, this.y0 + (this.y1 - this.y0 + 1) / 2, this.z0 + (this.z1 - this.z0 + 1) / 2);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 214 */     return MoreObjects.toStringHelper(this)
/* 215 */       .add("x0", this.x0)
/* 216 */       .add("y0", this.y0)
/* 217 */       .add("z0", this.z0)
/* 218 */       .add("x1", this.x1)
/* 219 */       .add("y1", this.y1)
/* 220 */       .add("z1", this.z1)
/* 221 */       .toString();
/*     */   }
/*     */   
/*     */   public IntArrayTag createTag() {
/* 225 */     return new IntArrayTag(new int[] { this.x0, this.y0, this.z0, this.x1, this.y1, this.z1 });
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\structure\BoundingBox.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */