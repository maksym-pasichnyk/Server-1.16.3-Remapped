/*    */ package net.minecraft.core.particles;
/*    */ 
/*    */ import com.mojang.brigadier.StringReader;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ 
/*    */ public class SimpleParticleType extends ParticleType<SimpleParticleType> implements ParticleOptions {
/* 10 */   private static final ParticleOptions.Deserializer<SimpleParticleType> DESERIALIZER = new ParticleOptions.Deserializer<SimpleParticleType>()
/*    */     {
/*    */       public SimpleParticleType fromCommand(ParticleType<SimpleParticleType> debug1, StringReader debug2) throws CommandSyntaxException {
/* 13 */         return (SimpleParticleType)debug1;
/*    */       }
/*    */ 
/*    */       
/*    */       public SimpleParticleType fromNetwork(ParticleType<SimpleParticleType> debug1, FriendlyByteBuf debug2) {
/* 18 */         return (SimpleParticleType)debug1;
/*    */       }
/*    */     };
/*    */   
/* 22 */   private final Codec<SimpleParticleType> codec = Codec.unit(this::getType);
/*    */   
/*    */   protected SimpleParticleType(boolean debug1) {
/* 25 */     super(debug1, DESERIALIZER);
/*    */   }
/*    */ 
/*    */   
/*    */   public SimpleParticleType getType() {
/* 30 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public Codec<SimpleParticleType> codec() {
/* 35 */     return this.codec;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void writeToNetwork(FriendlyByteBuf debug1) {}
/*    */ 
/*    */   
/*    */   public String writeToString() {
/* 44 */     return Registry.PARTICLE_TYPE.getKey(this).toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\core\particles\SimpleParticleType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */