/*    */ package net.minecraft.network.syncher;
/*    */ 
/*    */ public class EntityDataAccessor<T> {
/*    */   private final int id;
/*    */   private final EntityDataSerializer<T> serializer;
/*    */   
/*    */   public EntityDataAccessor(int debug1, EntityDataSerializer<T> debug2) {
/*  8 */     this.id = debug1;
/*  9 */     this.serializer = debug2;
/*    */   }
/*    */   
/*    */   public int getId() {
/* 13 */     return this.id;
/*    */   }
/*    */   
/*    */   public EntityDataSerializer<T> getSerializer() {
/* 17 */     return this.serializer;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object debug1) {
/* 22 */     if (this == debug1) {
/* 23 */       return true;
/*    */     }
/* 25 */     if (debug1 == null || getClass() != debug1.getClass()) {
/* 26 */       return false;
/*    */     }
/*    */     
/* 29 */     EntityDataAccessor<?> debug2 = (EntityDataAccessor)debug1;
/*    */     
/* 31 */     return (this.id == debug2.id);
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 36 */     return this.id;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 41 */     return "<entity data: " + this.id + ">";
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\syncher\EntityDataAccessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */