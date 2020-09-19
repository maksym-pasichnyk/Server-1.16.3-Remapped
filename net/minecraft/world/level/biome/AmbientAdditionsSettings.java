/*    */ package net.minecraft.world.level.biome;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.sounds.SoundEvent;
/*    */ 
/*    */ public class AmbientAdditionsSettings {
/*    */   static {
/*  8 */     CODEC = RecordCodecBuilder.create(debug0 -> debug0.group((App)SoundEvent.CODEC.fieldOf("sound").forGetter(()), (App)Codec.DOUBLE.fieldOf("tick_chance").forGetter(())).apply((Applicative)debug0, AmbientAdditionsSettings::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final Codec<AmbientAdditionsSettings> CODEC;
/*    */   private SoundEvent soundEvent;
/*    */   private double tickChance;
/*    */   
/*    */   public AmbientAdditionsSettings(SoundEvent debug1, double debug2) {
/* 17 */     this.soundEvent = debug1;
/* 18 */     this.tickChance = debug2;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\biome\AmbientAdditionsSettings.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */