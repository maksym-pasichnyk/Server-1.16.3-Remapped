/*    */ package net.minecraft.world.phys.shapes;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.doubles.DoubleArrayList;
/*    */ import it.unimi.dsi.fastutil.doubles.DoubleList;
/*    */ import it.unimi.dsi.fastutil.ints.IntArrayList;
/*    */ 
/*    */ public final class IndirectMerger implements IndexMerger {
/*    */   private final DoubleArrayList result;
/*    */   private final IntArrayList firstIndices;
/*    */   private final IntArrayList secondIndices;
/*    */   
/*    */   protected IndirectMerger(DoubleList debug1, DoubleList debug2, boolean debug3, boolean debug4) {
/* 13 */     int debug5 = 0;
/* 14 */     int debug6 = 0;
/* 15 */     double debug7 = Double.NaN;
/*    */     
/* 17 */     int debug9 = debug1.size();
/* 18 */     int debug10 = debug2.size();
/* 19 */     int debug11 = debug9 + debug10;
/* 20 */     this.result = new DoubleArrayList(debug11);
/* 21 */     this.firstIndices = new IntArrayList(debug11);
/* 22 */     this.secondIndices = new IntArrayList(debug11);
/*    */     
/*    */     while (true) {
/* 25 */       boolean debug12 = (debug5 < debug9);
/* 26 */       boolean debug13 = (debug6 < debug10);
/* 27 */       if (!debug12 && !debug13) {
/*    */         break;
/*    */       }
/*    */       
/* 31 */       boolean debug14 = (debug12 && (!debug13 || debug1.getDouble(debug5) < debug2.getDouble(debug6) + 1.0E-7D));
/* 32 */       double debug15 = debug14 ? debug1.getDouble(debug5++) : debug2.getDouble(debug6++);
/* 33 */       if ((debug5 == 0 || !debug12) && !debug14 && !debug4) {
/*    */         continue;
/*    */       }
/* 36 */       if ((debug6 == 0 || !debug13) && debug14 && !debug3) {
/*    */         continue;
/*    */       }
/* 39 */       if (debug7 < debug15 - 1.0E-7D) {
/* 40 */         this.firstIndices.add(debug5 - 1);
/* 41 */         this.secondIndices.add(debug6 - 1);
/* 42 */         this.result.add(debug15);
/* 43 */         debug7 = debug15; continue;
/* 44 */       }  if (!this.result.isEmpty()) {
/* 45 */         this.firstIndices.set(this.firstIndices.size() - 1, debug5 - 1);
/* 46 */         this.secondIndices.set(this.secondIndices.size() - 1, debug6 - 1);
/*    */       } 
/*    */     } 
/* 49 */     if (this.result.isEmpty()) {
/* 50 */       this.result.add(Math.min(debug1.getDouble(debug9 - 1), debug2.getDouble(debug10 - 1)));
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean forMergedIndexes(IndexMerger.IndexConsumer debug1) {
/* 56 */     for (int debug2 = 0; debug2 < this.result.size() - 1; debug2++) {
/* 57 */       if (!debug1.merge(this.firstIndices.getInt(debug2), this.secondIndices.getInt(debug2), debug2)) {
/* 58 */         return false;
/*    */       }
/*    */     } 
/* 61 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public DoubleList getList() {
/* 66 */     return (DoubleList)this.result;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\phys\shapes\IndirectMerger.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */