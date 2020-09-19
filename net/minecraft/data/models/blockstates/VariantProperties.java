/*    */ package net.minecraft.data.models.blockstates;
/*    */ 
/*    */ public class VariantProperties {
/*    */   public static final VariantProperty<Rotation> X_ROT;
/*    */   public static final VariantProperty<Rotation> Y_ROT;
/*    */   public static final VariantProperty<ResourceLocation> MODEL;
/*    */   
/*  8 */   public enum Rotation { R0(0),
/*  9 */     R90(90),
/* 10 */     R180(180),
/* 11 */     R270(270);
/*    */     
/*    */     private final int value;
/*    */     
/*    */     Rotation(int debug3) {
/* 16 */       this.value = debug3;
/*    */     } }
/*    */   
/*    */   static {
/* 20 */     X_ROT = new VariantProperty<>("x", debug0 -> new JsonPrimitive(Integer.valueOf(debug0.value)));
/* 21 */     Y_ROT = new VariantProperty<>("y", debug0 -> new JsonPrimitive(Integer.valueOf(debug0.value)));
/* 22 */     MODEL = new VariantProperty<>("model", debug0 -> new JsonPrimitive(debug0.toString()));
/* 23 */   } public static final VariantProperty<Boolean> UV_LOCK = new VariantProperty<>("uvlock", JsonPrimitive::new);
/* 24 */   public static final VariantProperty<Integer> WEIGHT = new VariantProperty<>("weight", JsonPrimitive::new);
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\data\models\blockstates\VariantProperties.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */