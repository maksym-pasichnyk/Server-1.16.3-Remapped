/*    */ package net.minecraft.world.level.biome;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.util.Function4;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.sounds.SoundEvent;
/*    */ 
/*    */ public class AmbientMoodSettings {
/*    */   static {
/* 10 */     CODEC = RecordCodecBuilder.create(debug0 -> debug0.group((App)SoundEvent.CODEC.fieldOf("sound").forGetter(()), (App)Codec.INT.fieldOf("tick_delay").forGetter(()), (App)Codec.INT.fieldOf("block_search_extent").forGetter(()), (App)Codec.DOUBLE.fieldOf("offset").forGetter(())).apply((Applicative)debug0, AmbientMoodSettings::new));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public static final Codec<AmbientMoodSettings> CODEC;
/*    */   
/* 17 */   public static final AmbientMoodSettings LEGACY_CAVE_SETTINGS = new AmbientMoodSettings(SoundEvents.AMBIENT_CAVE, 6000, 8, 2.0D);
/*    */ 
/*    */   
/*    */   private SoundEvent soundEvent;
/*    */   
/*    */   private int tickDelay;
/*    */   
/*    */   private int blockSearchExtent;
/*    */   
/*    */   private double soundPositionOffset;
/*    */ 
/*    */   
/*    */   public AmbientMoodSettings(SoundEvent debug1, int debug2, int debug3, double debug4) {
/* 30 */     this.soundEvent = debug1;
/* 31 */     this.tickDelay = debug2;
/* 32 */     this.blockSearchExtent = debug3;
/* 33 */     this.soundPositionOffset = debug4;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\biome\AmbientMoodSettings.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */