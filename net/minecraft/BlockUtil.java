/*     */ package net.minecraft;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import it.unimi.dsi.fastutil.ints.IntArrayList;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Vec3i;
/*     */ 
/*     */ public class BlockUtil
/*     */ {
/*     */   public static class IntBounds {
/*     */     public final int min;
/*     */     public final int max;
/*     */     
/*     */     public IntBounds(int debug1, int debug2) {
/*  18 */       this.min = debug1;
/*  19 */       this.max = debug2;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/*  24 */       return "IntBounds{min=" + this.min + ", max=" + this.max + '}';
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static class FoundRectangle
/*     */   {
/*     */     public final BlockPos minCorner;
/*     */     
/*     */     public final int axis1Size;
/*     */     public final int axis2Size;
/*     */     
/*     */     public FoundRectangle(BlockPos debug1, int debug2, int debug3) {
/*  37 */       this.minCorner = debug1;
/*  38 */       this.axis1Size = debug2;
/*  39 */       this.axis2Size = debug3;
/*     */     }
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
/*     */   public static FoundRectangle getLargestRectangleAround(BlockPos debug0, Direction.Axis debug1, int debug2, Direction.Axis debug3, int debug4, Predicate<BlockPos> debug5) {
/*  57 */     BlockPos.MutableBlockPos debug6 = debug0.mutable();
/*     */     
/*  59 */     Direction debug7 = Direction.get(Direction.AxisDirection.NEGATIVE, debug1);
/*  60 */     Direction debug8 = debug7.getOpposite();
/*     */     
/*  62 */     Direction debug9 = Direction.get(Direction.AxisDirection.NEGATIVE, debug3);
/*  63 */     Direction debug10 = debug9.getOpposite();
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
/*  79 */     int debug11 = getLimit(debug5, debug6.set((Vec3i)debug0), debug7, debug2);
/*  80 */     int debug12 = getLimit(debug5, debug6.set((Vec3i)debug0), debug8, debug2);
/*     */     
/*  82 */     int debug13 = debug11;
/*  83 */     IntBounds[] debug14 = new IntBounds[debug13 + 1 + debug12];
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
/*  99 */     debug14[debug13] = new IntBounds(
/* 100 */         getLimit(debug5, debug6.set((Vec3i)debug0), debug9, debug4), 
/* 101 */         getLimit(debug5, debug6.set((Vec3i)debug0), debug10, debug4));
/*     */ 
/*     */     
/* 104 */     int debug15 = (debug14[debug13]).min;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     int debug16;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 124 */     for (debug16 = 1; debug16 <= debug11; debug16++) {
/* 125 */       IntBounds intBounds = debug14[debug13 - debug16 - 1];
/* 126 */       debug14[debug13 - debug16] = new IntBounds(
/* 127 */           getLimit(debug5, debug6.set((Vec3i)debug0).move(debug7, debug16), debug9, intBounds.min), 
/* 128 */           getLimit(debug5, debug6.set((Vec3i)debug0).move(debug7, debug16), debug10, intBounds.max));
/*     */     } 
/*     */ 
/*     */     
/* 132 */     for (debug16 = 1; debug16 <= debug12; debug16++) {
/* 133 */       IntBounds intBounds = debug14[debug13 + debug16 - 1];
/* 134 */       debug14[debug13 + debug16] = new IntBounds(
/* 135 */           getLimit(debug5, debug6.set((Vec3i)debug0).move(debug8, debug16), debug9, intBounds.min), 
/* 136 */           getLimit(debug5, debug6.set((Vec3i)debug0).move(debug8, debug16), debug10, intBounds.max));
/*     */     } 
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
/* 154 */     debug16 = 0;
/* 155 */     int debug17 = 0;
/* 156 */     int debug18 = 0;
/* 157 */     int debug19 = 0;
/*     */     
/* 159 */     int[] debug20 = new int[debug14.length];
/*     */     
/* 161 */     for (int debug21 = debug15; debug21 >= 0; debug21--) {
/* 162 */       for (int i = 0; i < debug14.length; i++) {
/* 163 */         IntBounds intBounds = debug14[i];
/* 164 */         int j = debug15 - intBounds.min;
/* 165 */         int k = debug15 + intBounds.max;
/*     */         
/* 167 */         debug20[i] = (debug21 >= j && debug21 <= k) ? (k + 1 - debug21) : 0;
/*     */       } 
/*     */       
/* 170 */       Pair<IntBounds, Integer> debug22 = getMaxRectangleLocation(debug20);
/* 171 */       IntBounds debug23 = (IntBounds)debug22.getFirst();
/* 172 */       int debug24 = 1 + debug23.max - debug23.min;
/* 173 */       int debug25 = ((Integer)debug22.getSecond()).intValue();
/*     */       
/* 175 */       if (debug24 * debug25 > debug18 * debug19) {
/* 176 */         debug16 = debug23.min;
/* 177 */         debug17 = debug21;
/* 178 */         debug18 = debug24;
/* 179 */         debug19 = debug25;
/*     */       } 
/*     */     } 
/*     */     
/* 183 */     return new FoundRectangle(debug0
/* 184 */         .relative(debug1, debug16 - debug13).relative(debug3, debug17 - debug15), debug18, debug19);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int getLimit(Predicate<BlockPos> debug0, BlockPos.MutableBlockPos debug1, Direction debug2, int debug3) {
/* 191 */     int debug4 = 0;
/* 192 */     while (debug4 < debug3 && debug0.test(debug1.move(debug2))) {
/* 193 */       debug4++;
/*     */     }
/* 195 */     return debug4;
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   static Pair<IntBounds, Integer> getMaxRectangleLocation(int[] debug0) {
/* 200 */     int debug1 = 0;
/* 201 */     int debug2 = 0;
/* 202 */     int debug3 = 0;
/*     */     
/* 204 */     IntArrayList intArrayList = new IntArrayList();
/* 205 */     intArrayList.push(0);
/* 206 */     for (int debug5 = 1; debug5 <= debug0.length; debug5++) {
/* 207 */       int debug6 = (debug5 == debug0.length) ? 0 : debug0[debug5];
/* 208 */       while (!intArrayList.isEmpty()) {
/* 209 */         int debug7 = debug0[intArrayList.topInt()];
/* 210 */         if (debug6 >= debug7) {
/* 211 */           intArrayList.push(debug5);
/*     */           
/*     */           break;
/*     */         } 
/* 215 */         intArrayList.popInt();
/* 216 */         int debug8 = intArrayList.isEmpty() ? 0 : (intArrayList.topInt() + 1);
/*     */         
/* 218 */         if (debug7 * (debug5 - debug8) > debug3 * (debug2 - debug1)) {
/* 219 */           debug2 = debug5;
/* 220 */           debug1 = debug8;
/* 221 */           debug3 = debug7;
/*     */         } 
/*     */       } 
/*     */       
/* 225 */       if (intArrayList.isEmpty()) {
/* 226 */         intArrayList.push(debug5);
/*     */       }
/*     */     } 
/*     */     
/* 230 */     return new Pair(new IntBounds(debug1, debug2 - 1), Integer.valueOf(debug3));
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\BlockUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */