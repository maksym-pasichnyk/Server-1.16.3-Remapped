/*    */ package net.minecraft.world.entity.animal.horse;
/*    */ 
/*    */ import java.util.Arrays;
/*    */ import java.util.Comparator;
/*    */ 
/*    */ public enum Markings {
/*  7 */   NONE(0),
/*  8 */   WHITE(1),
/*  9 */   WHITE_FIELD(2),
/* 10 */   WHITE_DOTS(3),
/* 11 */   BLACK_DOTS(4); private static final Markings[] BY_ID;
/*    */   
/*    */   static {
/* 14 */     BY_ID = (Markings[])Arrays.<Markings>stream(values()).sorted(Comparator.comparingInt(Markings::getId)).toArray(debug0 -> new Markings[debug0]);
/*    */   }
/*    */   private final int id;
/*    */   Markings(int debug3) {
/* 18 */     this.id = debug3;
/*    */   }
/*    */   
/*    */   public int getId() {
/* 22 */     return this.id;
/*    */   }
/*    */   
/*    */   public static Markings byId(int debug0) {
/* 26 */     return BY_ID[debug0 % BY_ID.length];
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\animal\horse\Markings.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */