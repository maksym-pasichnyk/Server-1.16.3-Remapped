/*    */ package net.minecraft.world.level.levelgen;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.Arrays;
/*    */ import java.util.Map;
/*    */ import java.util.stream.Collectors;
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public class GenerationStep
/*    */ {
/*    */   public enum Decoration {
/* 13 */     RAW_GENERATION,
/* 14 */     LAKES,
/* 15 */     LOCAL_MODIFICATIONS,
/* 16 */     UNDERGROUND_STRUCTURES,
/* 17 */     SURFACE_STRUCTURES,
/* 18 */     STRONGHOLDS,
/* 19 */     UNDERGROUND_ORES,
/* 20 */     UNDERGROUND_DECORATION,
/* 21 */     VEGETAL_DECORATION,
/* 22 */     TOP_LAYER_MODIFICATION;
/*    */   }
/*    */   
/*    */   public enum Carving
/*    */     implements StringRepresentable {
/* 27 */     AIR("air"),
/* 28 */     LIQUID("liquid");
/*    */ 
/*    */     
/* 31 */     public static final Codec<Carving> CODEC = StringRepresentable.fromEnum(Carving::values, Carving::byName); private static final Map<String, Carving> BY_NAME;
/*    */     static {
/* 33 */       BY_NAME = (Map<String, Carving>)Arrays.<Carving>stream(values()).collect(Collectors.toMap(Carving::getName, debug0 -> debug0));
/*    */     }
/*    */     private final String name;
/*    */     Carving(String debug3) {
/* 37 */       this.name = debug3;
/*    */     }
/*    */     
/*    */     public String getName() {
/* 41 */       return this.name;
/*    */     }
/*    */     
/*    */     @Nullable
/*    */     public static Carving byName(String debug0) {
/* 46 */       return BY_NAME.get(debug0);
/*    */     }
/*    */ 
/*    */     
/*    */     public String getSerializedName() {
/* 51 */       return this.name;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\GenerationStep.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */