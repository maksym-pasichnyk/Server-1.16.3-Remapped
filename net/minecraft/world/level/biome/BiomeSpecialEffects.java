/*     */ package net.minecraft.world.level.biome;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function12;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.OptionalInt;
/*     */ import net.minecraft.sounds.Music;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ 
/*     */ public class BiomeSpecialEffects {
/*     */   static {
/*  16 */     CODEC = RecordCodecBuilder.create(debug0 -> debug0.group((App)Codec.INT.fieldOf("fog_color").forGetter(()), (App)Codec.INT.fieldOf("water_color").forGetter(()), (App)Codec.INT.fieldOf("water_fog_color").forGetter(()), (App)Codec.INT.fieldOf("sky_color").forGetter(()), (App)Codec.INT.optionalFieldOf("foliage_color").forGetter(()), (App)Codec.INT.optionalFieldOf("grass_color").forGetter(()), (App)GrassColorModifier.CODEC.optionalFieldOf("grass_color_modifier", GrassColorModifier.NONE).forGetter(()), (App)AmbientParticleSettings.CODEC.optionalFieldOf("particle").forGetter(()), (App)SoundEvent.CODEC.optionalFieldOf("ambient_sound").forGetter(()), (App)AmbientMoodSettings.CODEC.optionalFieldOf("mood_sound").forGetter(()), (App)AmbientAdditionsSettings.CODEC.optionalFieldOf("additions_sound").forGetter(()), (App)Music.CODEC.optionalFieldOf("music").forGetter(())).apply((Applicative)debug0, BiomeSpecialEffects::new));
/*     */   }
/*     */ 
/*     */   
/*     */   public static final Codec<BiomeSpecialEffects> CODEC;
/*     */   
/*     */   private final int fogColor;
/*     */   
/*     */   private final int waterColor;
/*     */   
/*     */   private final int waterFogColor;
/*     */   
/*     */   private final int skyColor;
/*     */   
/*     */   private final Optional<Integer> foliageColorOverride;
/*     */   
/*     */   private final Optional<Integer> grassColorOverride;
/*     */   
/*     */   private final GrassColorModifier grassColorModifier;
/*     */   
/*     */   private final Optional<AmbientParticleSettings> ambientParticleSettings;
/*     */   
/*     */   private final Optional<SoundEvent> ambientLoopSoundEvent;
/*     */   
/*     */   private final Optional<AmbientMoodSettings> ambientMoodSettings;
/*     */   private final Optional<AmbientAdditionsSettings> ambientAdditionsSettings;
/*     */   private final Optional<Music> backgroundMusic;
/*     */   
/*     */   private BiomeSpecialEffects(int debug1, int debug2, int debug3, int debug4, Optional<Integer> debug5, Optional<Integer> debug6, GrassColorModifier debug7, Optional<AmbientParticleSettings> debug8, Optional<SoundEvent> debug9, Optional<AmbientMoodSettings> debug10, Optional<AmbientAdditionsSettings> debug11, Optional<Music> debug12) {
/*  45 */     this.fogColor = debug1;
/*  46 */     this.waterColor = debug2;
/*  47 */     this.waterFogColor = debug3;
/*  48 */     this.skyColor = debug4;
/*  49 */     this.foliageColorOverride = debug5;
/*  50 */     this.grassColorOverride = debug6;
/*  51 */     this.grassColorModifier = debug7;
/*  52 */     this.ambientParticleSettings = debug8;
/*  53 */     this.ambientLoopSoundEvent = debug9;
/*  54 */     this.ambientMoodSettings = debug10;
/*  55 */     this.ambientAdditionsSettings = debug11;
/*  56 */     this.backgroundMusic = debug12;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class Builder
/*     */   {
/* 108 */     private OptionalInt fogColor = OptionalInt.empty();
/* 109 */     private OptionalInt waterColor = OptionalInt.empty();
/* 110 */     private OptionalInt waterFogColor = OptionalInt.empty();
/* 111 */     private OptionalInt skyColor = OptionalInt.empty();
/* 112 */     private Optional<Integer> foliageColorOverride = Optional.empty();
/* 113 */     private Optional<Integer> grassColorOverride = Optional.empty();
/* 114 */     private BiomeSpecialEffects.GrassColorModifier grassColorModifier = BiomeSpecialEffects.GrassColorModifier.NONE;
/* 115 */     private Optional<AmbientParticleSettings> ambientParticle = Optional.empty();
/* 116 */     private Optional<SoundEvent> ambientLoopSoundEvent = Optional.empty();
/* 117 */     private Optional<AmbientMoodSettings> ambientMoodSettings = Optional.empty();
/* 118 */     private Optional<AmbientAdditionsSettings> ambientAdditionsSettings = Optional.empty();
/* 119 */     private Optional<Music> backgroundMusic = Optional.empty();
/*     */     
/*     */     public Builder fogColor(int debug1) {
/* 122 */       this.fogColor = OptionalInt.of(debug1);
/* 123 */       return this;
/*     */     }
/*     */     
/*     */     public Builder waterColor(int debug1) {
/* 127 */       this.waterColor = OptionalInt.of(debug1);
/* 128 */       return this;
/*     */     }
/*     */     
/*     */     public Builder waterFogColor(int debug1) {
/* 132 */       this.waterFogColor = OptionalInt.of(debug1);
/* 133 */       return this;
/*     */     }
/*     */     
/*     */     public Builder skyColor(int debug1) {
/* 137 */       this.skyColor = OptionalInt.of(debug1);
/* 138 */       return this;
/*     */     }
/*     */     
/*     */     public Builder foliageColorOverride(int debug1) {
/* 142 */       this.foliageColorOverride = Optional.of(Integer.valueOf(debug1));
/* 143 */       return this;
/*     */     }
/*     */     
/*     */     public Builder grassColorOverride(int debug1) {
/* 147 */       this.grassColorOverride = Optional.of(Integer.valueOf(debug1));
/* 148 */       return this;
/*     */     }
/*     */     
/*     */     public Builder grassColorModifier(BiomeSpecialEffects.GrassColorModifier debug1) {
/* 152 */       this.grassColorModifier = debug1;
/* 153 */       return this;
/*     */     }
/*     */     
/*     */     public Builder ambientParticle(AmbientParticleSettings debug1) {
/* 157 */       this.ambientParticle = Optional.of(debug1);
/* 158 */       return this;
/*     */     }
/*     */     
/*     */     public Builder ambientLoopSound(SoundEvent debug1) {
/* 162 */       this.ambientLoopSoundEvent = Optional.of(debug1);
/* 163 */       return this;
/*     */     }
/*     */     
/*     */     public Builder ambientMoodSound(AmbientMoodSettings debug1) {
/* 167 */       this.ambientMoodSettings = Optional.of(debug1);
/* 168 */       return this;
/*     */     }
/*     */     
/*     */     public Builder ambientAdditionsSound(AmbientAdditionsSettings debug1) {
/* 172 */       this.ambientAdditionsSettings = Optional.of(debug1);
/* 173 */       return this;
/*     */     }
/*     */     
/*     */     public Builder backgroundMusic(Music debug1) {
/* 177 */       this.backgroundMusic = Optional.of(debug1);
/* 178 */       return this;
/*     */     }
/*     */     
/*     */     public BiomeSpecialEffects build() {
/* 182 */       return new BiomeSpecialEffects(this.fogColor
/* 183 */           .orElseThrow(() -> new IllegalStateException("Missing 'fog' color.")), this.waterColor
/* 184 */           .orElseThrow(() -> new IllegalStateException("Missing 'water' color.")), this.waterFogColor
/* 185 */           .orElseThrow(() -> new IllegalStateException("Missing 'water fog' color.")), this.skyColor
/* 186 */           .orElseThrow(() -> new IllegalStateException("Missing 'sky' color.")), this.foliageColorOverride, this.grassColorOverride, this.grassColorModifier, this.ambientParticle, this.ambientLoopSoundEvent, this.ambientMoodSettings, this.ambientAdditionsSettings, this.backgroundMusic);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public enum GrassColorModifier
/*     */     implements StringRepresentable
/*     */   {
/* 198 */     NONE("none")
/*     */     {
/*     */ 
/*     */ 
/*     */     
/*     */     },
/* 204 */     DARK_FOREST("dark_forest")
/*     */     {
/*     */ 
/*     */ 
/*     */     
/*     */     },
/* 210 */     SWAMP("swamp")
/*     */     {
/*     */     
/*     */     };
/*     */ 
/*     */     
/*     */     private final String name;
/*     */ 
/*     */     
/*     */     private static final Map<String, GrassColorModifier> BY_NAME;
/*     */ 
/*     */ 
/*     */     
/*     */     GrassColorModifier(String debug3) {
/*     */       this.name = debug3;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 229 */     public static final Codec<GrassColorModifier> CODEC = StringRepresentable.fromEnum(GrassColorModifier::values, GrassColorModifier::byName);
/*     */     static {
/* 231 */       BY_NAME = (Map<String, GrassColorModifier>)Arrays.<GrassColorModifier>stream(values()).collect(Collectors.toMap(GrassColorModifier::getName, debug0 -> debug0));
/*     */     }
/*     */     public String getName() {
/* 234 */       return this.name;
/*     */     }
/*     */ 
/*     */     
/*     */     public String getSerializedName() {
/* 239 */       return this.name;
/*     */     }
/*     */     
/*     */     public static GrassColorModifier byName(String debug0) {
/* 243 */       return BY_NAME.get(debug0);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\biome\BiomeSpecialEffects.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */