/*    */ package net.minecraft.world.entity.animal.horse;
/*    */ 
/*    */ import java.util.Arrays;
/*    */ import java.util.Comparator;
/*    */ 
/*    */ public enum Variant {
/*  7 */   WHITE(0),
/*  8 */   CREAMY(1),
/*  9 */   CHESTNUT(2),
/* 10 */   BROWN(3),
/* 11 */   BLACK(4),
/* 12 */   GRAY(5),
/* 13 */   DARKBROWN(6); private static final Variant[] BY_ID;
/*    */   
/*    */   static {
/* 16 */     BY_ID = (Variant[])Arrays.<Variant>stream(values()).sorted(Comparator.comparingInt(Variant::getId)).toArray(debug0 -> new Variant[debug0]);
/*    */   }
/*    */   private final int id;
/*    */   Variant(int debug3) {
/* 20 */     this.id = debug3;
/*    */   }
/*    */   
/*    */   public int getId() {
/* 24 */     return this.id;
/*    */   }
/*    */   
/*    */   public static Variant byId(int debug0) {
/* 28 */     return BY_ID[debug0 % BY_ID.length];
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\animal\horse\Variant.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */