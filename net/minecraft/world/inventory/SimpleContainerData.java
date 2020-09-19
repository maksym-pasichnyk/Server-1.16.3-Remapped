/*    */ package net.minecraft.world.inventory;
/*    */ 
/*    */ public class SimpleContainerData implements ContainerData {
/*    */   private final int[] ints;
/*    */   
/*    */   public SimpleContainerData(int debug1) {
/*  7 */     this.ints = new int[debug1];
/*    */   }
/*    */ 
/*    */   
/*    */   public int get(int debug1) {
/* 12 */     return this.ints[debug1];
/*    */   }
/*    */ 
/*    */   
/*    */   public void set(int debug1, int debug2) {
/* 17 */     this.ints[debug1] = debug2;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getCount() {
/* 22 */     return this.ints.length;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\inventory\SimpleContainerData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */