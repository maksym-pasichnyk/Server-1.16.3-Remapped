/*    */ package net.minecraft.world;
/*    */ 
/*    */ import java.util.Arrays;
/*    */ import java.util.Comparator;
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.TranslatableComponent;
/*    */ 
/*    */ public enum Difficulty {
/*    */   private static final Difficulty[] BY_ID;
/* 11 */   PEACEFUL(0, "peaceful"),
/* 12 */   EASY(1, "easy"),
/* 13 */   NORMAL(2, "normal"),
/* 14 */   HARD(3, "hard");
/*    */   
/*    */   static {
/* 17 */     BY_ID = (Difficulty[])Arrays.<Difficulty>stream(values()).sorted(Comparator.comparingInt(Difficulty::getId)).toArray(debug0 -> new Difficulty[debug0]);
/*    */   }
/*    */   
/*    */   private final int id;
/*    */   
/*    */   Difficulty(int debug3, String debug4) {
/* 23 */     this.id = debug3;
/* 24 */     this.key = debug4;
/*    */   }
/*    */   private final String key;
/*    */   public int getId() {
/* 28 */     return this.id;
/*    */   }
/*    */   
/*    */   public Component getDisplayName() {
/* 32 */     return (Component)new TranslatableComponent("options.difficulty." + this.key);
/*    */   }
/*    */   
/*    */   public static Difficulty byId(int debug0) {
/* 36 */     return BY_ID[debug0 % BY_ID.length];
/*    */   }
/*    */   
/*    */   @Nullable
/*    */   public static Difficulty byName(String debug0) {
/* 41 */     for (Difficulty debug4 : values()) {
/* 42 */       if (debug4.key.equals(debug0)) {
/* 43 */         return debug4;
/*    */       }
/*    */     } 
/* 46 */     return null;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getKey() {
/* 53 */     return this.key;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\Difficulty.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */