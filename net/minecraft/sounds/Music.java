/*    */ package net.minecraft.sounds;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.Codec;
/*    */ 
/*    */ public class Music {
/*    */   static {
/*  7 */     CODEC = RecordCodecBuilder.create(debug0 -> debug0.group((App)SoundEvent.CODEC.fieldOf("sound").forGetter(()), (App)Codec.INT.fieldOf("min_delay").forGetter(()), (App)Codec.INT.fieldOf("max_delay").forGetter(()), (App)Codec.BOOL.fieldOf("replace_current_music").forGetter(())).apply((Applicative)debug0, Music::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final Codec<Music> CODEC;
/*    */   
/*    */   private final SoundEvent event;
/*    */   
/*    */   private final int minDelay;
/*    */   private final int maxDelay;
/*    */   private final boolean replaceCurrentMusic;
/*    */   
/*    */   public Music(SoundEvent debug1, int debug2, int debug3, boolean debug4) {
/* 20 */     this.event = debug1;
/* 21 */     this.minDelay = debug2;
/* 22 */     this.maxDelay = debug3;
/* 23 */     this.replaceCurrentMusic = debug4;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\sounds\Music.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */