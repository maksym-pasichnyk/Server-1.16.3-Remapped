/*    */ package net.minecraft.world.level.levelgen;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ 
/*    */ public class NoiseSlideSettings {
/*    */   static {
/*  8 */     CODEC = RecordCodecBuilder.create(debug0 -> debug0.group((App)Codec.INT.fieldOf("target").forGetter(NoiseSlideSettings::target), (App)Codec.intRange(0, 256).fieldOf("size").forGetter(NoiseSlideSettings::size), (App)Codec.INT.fieldOf("offset").forGetter(NoiseSlideSettings::offset)).apply((Applicative)debug0, NoiseSlideSettings::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final Codec<NoiseSlideSettings> CODEC;
/*    */   
/*    */   private final int target;
/*    */   private final int size;
/*    */   private final int offset;
/*    */   
/*    */   public NoiseSlideSettings(int debug1, int debug2, int debug3) {
/* 19 */     this.target = debug1;
/* 20 */     this.size = debug2;
/* 21 */     this.offset = debug3;
/*    */   }
/*    */   
/*    */   public int target() {
/* 25 */     return this.target;
/*    */   }
/*    */   
/*    */   public int size() {
/* 29 */     return this.size;
/*    */   }
/*    */   
/*    */   public int offset() {
/* 33 */     return this.offset;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\NoiseSlideSettings.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */