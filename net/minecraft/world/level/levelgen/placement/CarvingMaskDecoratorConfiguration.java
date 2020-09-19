/*    */ package net.minecraft.world.level.levelgen.placement;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.world.level.levelgen.GenerationStep;
/*    */ 
/*    */ public class CarvingMaskDecoratorConfiguration implements DecoratorConfiguration {
/*    */   static {
/*  9 */     CODEC = RecordCodecBuilder.create(debug0 -> debug0.group((App)GenerationStep.Carving.CODEC.fieldOf("step").forGetter(()), (App)Codec.FLOAT.fieldOf("probability").forGetter(())).apply((Applicative)debug0, CarvingMaskDecoratorConfiguration::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final Codec<CarvingMaskDecoratorConfiguration> CODEC;
/*    */   protected final GenerationStep.Carving step;
/*    */   protected final float probability;
/*    */   
/*    */   public CarvingMaskDecoratorConfiguration(GenerationStep.Carving debug1, float debug2) {
/* 18 */     this.step = debug1;
/* 19 */     this.probability = debug2;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\placement\CarvingMaskDecoratorConfiguration.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */