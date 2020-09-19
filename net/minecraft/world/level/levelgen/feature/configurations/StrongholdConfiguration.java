/*    */ package net.minecraft.world.level.levelgen.feature.configurations;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.Codec;
/*    */ 
/*    */ public class StrongholdConfiguration {
/*    */   static {
/*  7 */     CODEC = RecordCodecBuilder.create(debug0 -> debug0.group((App)Codec.intRange(0, 1023).fieldOf("distance").forGetter(StrongholdConfiguration::distance), (App)Codec.intRange(0, 1023).fieldOf("spread").forGetter(StrongholdConfiguration::spread), (App)Codec.intRange(1, 4095).fieldOf("count").forGetter(StrongholdConfiguration::count)).apply((Applicative)debug0, StrongholdConfiguration::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final Codec<StrongholdConfiguration> CODEC;
/*    */   
/*    */   private final int distance;
/*    */   private final int spread;
/*    */   private final int count;
/*    */   
/*    */   public StrongholdConfiguration(int debug1, int debug2, int debug3) {
/* 18 */     this.distance = debug1;
/* 19 */     this.spread = debug2;
/* 20 */     this.count = debug3;
/*    */   }
/*    */   
/*    */   public int distance() {
/* 24 */     return this.distance;
/*    */   }
/*    */   
/*    */   public int spread() {
/* 28 */     return this.spread;
/*    */   }
/*    */   
/*    */   public int count() {
/* 32 */     return this.count;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\feature\configurations\StrongholdConfiguration.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */