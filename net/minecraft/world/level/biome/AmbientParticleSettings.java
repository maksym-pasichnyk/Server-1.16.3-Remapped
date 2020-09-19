/*    */ package net.minecraft.world.level.biome;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.core.particles.ParticleTypes;
/*    */ 
/*    */ public class AmbientParticleSettings {
/*    */   static {
/* 11 */     CODEC = RecordCodecBuilder.create(debug0 -> debug0.group((App)ParticleTypes.CODEC.fieldOf("options").forGetter(()), (App)Codec.FLOAT.fieldOf("probability").forGetter(())).apply((Applicative)debug0, AmbientParticleSettings::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final Codec<AmbientParticleSettings> CODEC;
/*    */   private final ParticleOptions options;
/*    */   private final float probability;
/*    */   
/*    */   public AmbientParticleSettings(ParticleOptions debug1, float debug2) {
/* 20 */     this.options = debug1;
/* 21 */     this.probability = debug2;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\biome\AmbientParticleSettings.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */