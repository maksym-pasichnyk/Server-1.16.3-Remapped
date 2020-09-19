/*    */ package net.minecraft.world.phys.shapes;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.doubles.AbstractDoubleList;
/*    */ import it.unimi.dsi.fastutil.doubles.DoubleList;
/*    */ 
/*    */ public class OffsetDoubleList extends AbstractDoubleList {
/*    */   private final DoubleList delegate;
/*    */   private final double offset;
/*    */   
/*    */   public OffsetDoubleList(DoubleList debug1, double debug2) {
/* 11 */     this.delegate = debug1;
/* 12 */     this.offset = debug2;
/*    */   }
/*    */ 
/*    */   
/*    */   public double getDouble(int debug1) {
/* 17 */     return this.delegate.getDouble(debug1) + this.offset;
/*    */   }
/*    */ 
/*    */   
/*    */   public int size() {
/* 22 */     return this.delegate.size();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\phys\shapes\OffsetDoubleList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */