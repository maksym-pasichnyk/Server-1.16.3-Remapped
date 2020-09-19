/*    */ package net.minecraft.world.level.levelgen.feature.configurations;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.Function;
/*    */ 
/*    */ public class StructureFeatureConfiguration {
/*    */   public static final Codec<StructureFeatureConfiguration> CODEC;
/*    */   
/*    */   static {
/* 14 */     CODEC = RecordCodecBuilder.create(debug0 -> debug0.group((App)Codec.intRange(0, 4096).fieldOf("spacing").forGetter(()), (App)Codec.intRange(0, 4096).fieldOf("separation").forGetter(()), (App)Codec.intRange(0, 2147483647).fieldOf("salt").forGetter(())).apply((Applicative)debug0, StructureFeatureConfiguration::new)).comapFlatMap(debug0 -> (debug0.spacing <= debug0.separation) ? DataResult.error("Spacing has to be smaller than separation") : DataResult.success(debug0), 
/*    */ 
/*    */ 
/*    */ 
/*    */         
/* 19 */         Function.identity());
/*    */   }
/*    */   private final int spacing;
/*    */   private final int separation;
/*    */   private final int salt;
/*    */   
/*    */   public StructureFeatureConfiguration(int debug1, int debug2, int debug3) {
/* 26 */     this.spacing = debug1;
/* 27 */     this.separation = debug2;
/* 28 */     this.salt = debug3;
/*    */   }
/*    */   
/*    */   public int spacing() {
/* 32 */     return this.spacing;
/*    */   }
/*    */   
/*    */   public int separation() {
/* 36 */     return this.separation;
/*    */   }
/*    */   
/*    */   public int salt() {
/* 40 */     return this.salt;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\feature\configurations\StructureFeatureConfiguration.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */