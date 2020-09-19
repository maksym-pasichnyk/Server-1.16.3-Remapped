/*    */ package net.minecraft.core.particles;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ 
/*    */ public abstract class ParticleType<T extends ParticleOptions> {
/*    */   private final boolean overrideLimiter;
/*    */   private final ParticleOptions.Deserializer<T> deserializer;
/*    */   
/*    */   protected ParticleType(boolean debug1, ParticleOptions.Deserializer<T> debug2) {
/* 10 */     this.overrideLimiter = debug1;
/* 11 */     this.deserializer = debug2;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ParticleOptions.Deserializer<T> getDeserializer() {
/* 19 */     return this.deserializer;
/*    */   }
/*    */   
/*    */   public abstract Codec<T> codec();
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\core\particles\ParticleType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */