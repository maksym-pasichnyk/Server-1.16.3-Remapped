/*    */ package net.minecraft.world.inventory;
/*    */ 
/*    */ public abstract class DataSlot {
/*    */   public static DataSlot forContainer(final ContainerData container, final int dataId) {
/*  5 */     return new DataSlot()
/*    */       {
/*    */         public int get() {
/*  8 */           return container.get(dataId);
/*    */         }
/*    */ 
/*    */         
/*    */         public void set(int debug1) {
/* 13 */           container.set(dataId, debug1);
/*    */         }
/*    */       };
/*    */   }
/*    */   private int prevValue;
/*    */   public static DataSlot shared(final int[] storage, final int index) {
/* 19 */     return new DataSlot()
/*    */       {
/*    */         public int get() {
/* 22 */           return storage[index];
/*    */         }
/*    */ 
/*    */         
/*    */         public void set(int debug1) {
/* 27 */           storage[index] = debug1;
/*    */         }
/*    */       };
/*    */   }
/*    */   
/*    */   public static DataSlot standalone() {
/* 33 */     return new DataSlot()
/*    */       {
/*    */         private int value;
/*    */         
/*    */         public int get() {
/* 38 */           return this.value;
/*    */         }
/*    */ 
/*    */         
/*    */         public void set(int debug1) {
/* 43 */           this.value = debug1;
/*    */         }
/*    */       };
/*    */   }
/*    */ 
/*    */   
/*    */   public abstract int get();
/*    */ 
/*    */   
/*    */   public abstract void set(int paramInt);
/*    */   
/*    */   public boolean checkAndClearUpdateFlag() {
/* 55 */     int debug1 = get();
/* 56 */     boolean debug2 = (debug1 != this.prevValue);
/* 57 */     this.prevValue = debug1;
/* 58 */     return debug2;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\inventory\DataSlot.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */