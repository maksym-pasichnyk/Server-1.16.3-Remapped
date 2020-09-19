/*    */ package net.minecraft.world.entity;
/*    */ 
/*    */ public enum EquipmentSlot {
/*  4 */   MAINHAND(Type.HAND, 0, 0, "mainhand"),
/*  5 */   OFFHAND(Type.HAND, 1, 5, "offhand"),
/*  6 */   FEET(Type.ARMOR, 0, 1, "feet"),
/*  7 */   LEGS(Type.ARMOR, 1, 2, "legs"),
/*  8 */   CHEST(Type.ARMOR, 2, 3, "chest"),
/*  9 */   HEAD(Type.ARMOR, 3, 4, "head");
/*    */   
/*    */   private final Type type;
/*    */   private final int index;
/*    */   private final int filterFlag;
/*    */   private final String name;
/*    */   
/*    */   EquipmentSlot(Type debug3, int debug4, int debug5, String debug6) {
/* 17 */     this.type = debug3;
/* 18 */     this.index = debug4;
/* 19 */     this.filterFlag = debug5;
/* 20 */     this.name = debug6;
/*    */   }
/*    */   
/*    */   public Type getType() {
/* 24 */     return this.type;
/*    */   }
/*    */   
/*    */   public int getIndex() {
/* 28 */     return this.index;
/*    */   }
/*    */   
/*    */   public int getFilterFlag() {
/* 32 */     return this.filterFlag;
/*    */   }
/*    */   
/*    */   public String getName() {
/* 36 */     return this.name;
/*    */   }
/*    */   
/*    */   public enum Type {
/* 40 */     HAND,
/* 41 */     ARMOR;
/*    */   }
/*    */   
/*    */   public static EquipmentSlot byName(String debug0) {
/* 45 */     for (EquipmentSlot debug4 : values()) {
/* 46 */       if (debug4.getName().equals(debug0)) {
/* 47 */         return debug4;
/*    */       }
/*    */     } 
/*    */     
/* 51 */     throw new IllegalArgumentException("Invalid slot '" + debug0 + "'");
/*    */   }
/*    */   
/*    */   public static EquipmentSlot byTypeAndIndex(Type debug0, int debug1) {
/* 55 */     for (EquipmentSlot debug5 : values()) {
/* 56 */       if (debug5.getType() == debug0 && debug5.getIndex() == debug1) {
/* 57 */         return debug5;
/*    */       }
/*    */     } 
/*    */     
/* 61 */     throw new IllegalArgumentException("Invalid slot '" + debug0 + "': " + debug1);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\EquipmentSlot.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */