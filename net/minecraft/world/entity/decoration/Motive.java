/*    */ package net.minecraft.world.entity.decoration;
/*    */ 
/*    */ import net.minecraft.core.Registry;
/*    */ 
/*    */ public class Motive {
/*  6 */   public static final Motive KEBAB = register("kebab", 16, 16);
/*  7 */   public static final Motive AZTEC = register("aztec", 16, 16);
/*  8 */   public static final Motive ALBAN = register("alban", 16, 16);
/*  9 */   public static final Motive AZTEC2 = register("aztec2", 16, 16);
/* 10 */   public static final Motive BOMB = register("bomb", 16, 16);
/* 11 */   public static final Motive PLANT = register("plant", 16, 16);
/* 12 */   public static final Motive WASTELAND = register("wasteland", 16, 16);
/* 13 */   public static final Motive POOL = register("pool", 32, 16);
/* 14 */   public static final Motive COURBET = register("courbet", 32, 16);
/* 15 */   public static final Motive SEA = register("sea", 32, 16);
/* 16 */   public static final Motive SUNSET = register("sunset", 32, 16);
/* 17 */   public static final Motive CREEBET = register("creebet", 32, 16);
/* 18 */   public static final Motive WANDERER = register("wanderer", 16, 32);
/* 19 */   public static final Motive GRAHAM = register("graham", 16, 32);
/* 20 */   public static final Motive MATCH = register("match", 32, 32);
/* 21 */   public static final Motive BUST = register("bust", 32, 32);
/* 22 */   public static final Motive STAGE = register("stage", 32, 32);
/* 23 */   public static final Motive VOID = register("void", 32, 32);
/* 24 */   public static final Motive SKULL_AND_ROSES = register("skull_and_roses", 32, 32);
/* 25 */   public static final Motive WITHER = register("wither", 32, 32);
/* 26 */   public static final Motive FIGHTERS = register("fighters", 64, 32);
/* 27 */   public static final Motive POINTER = register("pointer", 64, 64);
/* 28 */   public static final Motive PIGSCENE = register("pigscene", 64, 64);
/* 29 */   public static final Motive BURNING_SKULL = register("burning_skull", 64, 64);
/* 30 */   public static final Motive SKELETON = register("skeleton", 64, 48);
/* 31 */   public static final Motive DONKEY_KONG = register("donkey_kong", 64, 48);
/*    */   
/*    */   private static Motive register(String debug0, int debug1, int debug2) {
/* 34 */     return (Motive)Registry.register((Registry)Registry.MOTIVE, debug0, new Motive(debug1, debug2));
/*    */   }
/*    */   
/*    */   private final int width;
/*    */   private final int height;
/*    */   
/*    */   public Motive(int debug1, int debug2) {
/* 41 */     this.width = debug1;
/* 42 */     this.height = debug2;
/*    */   }
/*    */   
/*    */   public int getWidth() {
/* 46 */     return this.width;
/*    */   }
/*    */   
/*    */   public int getHeight() {
/* 50 */     return this.height;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\decoration\Motive.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */