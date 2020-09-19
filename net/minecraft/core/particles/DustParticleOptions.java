/*    */ package net.minecraft.core.particles;
/*    */ import com.mojang.brigadier.StringReader;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function4;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.util.Mth;
/*    */ 
/*    */ public class DustParticleOptions implements ParticleOptions {
/* 14 */   public static final DustParticleOptions REDSTONE = new DustParticleOptions(1.0F, 0.0F, 0.0F, 1.0F); public static final Codec<DustParticleOptions> CODEC;
/*    */   static {
/* 16 */     CODEC = RecordCodecBuilder.create(debug0 -> debug0.group((App)Codec.FLOAT.fieldOf("r").forGetter(()), (App)Codec.FLOAT.fieldOf("g").forGetter(()), (App)Codec.FLOAT.fieldOf("b").forGetter(()), (App)Codec.FLOAT.fieldOf("scale").forGetter(())).apply((Applicative)debug0, DustParticleOptions::new));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 23 */   public static final ParticleOptions.Deserializer<DustParticleOptions> DESERIALIZER = new ParticleOptions.Deserializer<DustParticleOptions>()
/*    */     {
/*    */       public DustParticleOptions fromCommand(ParticleType<DustParticleOptions> debug1, StringReader debug2) throws CommandSyntaxException
/*    */       {
/* 27 */         debug2.expect(' ');
/* 28 */         float debug3 = (float)debug2.readDouble();
/* 29 */         debug2.expect(' ');
/* 30 */         float debug4 = (float)debug2.readDouble();
/* 31 */         debug2.expect(' ');
/* 32 */         float debug5 = (float)debug2.readDouble();
/* 33 */         debug2.expect(' ');
/* 34 */         float debug6 = (float)debug2.readDouble();
/* 35 */         return new DustParticleOptions(debug3, debug4, debug5, debug6);
/*    */       }
/*    */ 
/*    */       
/*    */       public DustParticleOptions fromNetwork(ParticleType<DustParticleOptions> debug1, FriendlyByteBuf debug2) {
/* 40 */         return new DustParticleOptions(debug2.readFloat(), debug2.readFloat(), debug2.readFloat(), debug2.readFloat());
/*    */       }
/*    */     };
/*    */   
/*    */   private final float r;
/*    */   private final float g;
/*    */   private final float b;
/*    */   private final float scale;
/*    */   
/*    */   public DustParticleOptions(float debug1, float debug2, float debug3, float debug4) {
/* 50 */     this.r = debug1;
/* 51 */     this.g = debug2;
/* 52 */     this.b = debug3;
/*    */     
/* 54 */     this.scale = Mth.clamp(debug4, 0.01F, 4.0F);
/*    */   }
/*    */ 
/*    */   
/*    */   public void writeToNetwork(FriendlyByteBuf debug1) {
/* 59 */     debug1.writeFloat(this.r);
/* 60 */     debug1.writeFloat(this.g);
/* 61 */     debug1.writeFloat(this.b);
/* 62 */     debug1.writeFloat(this.scale);
/*    */   }
/*    */ 
/*    */   
/*    */   public String writeToString() {
/* 67 */     return String.format(Locale.ROOT, "%s %.2f %.2f %.2f %.2f", new Object[] { Registry.PARTICLE_TYPE.getKey(getType()), Float.valueOf(this.r), Float.valueOf(this.g), Float.valueOf(this.b), Float.valueOf(this.scale) });
/*    */   }
/*    */ 
/*    */   
/*    */   public ParticleType<DustParticleOptions> getType() {
/* 72 */     return ParticleTypes.DUST;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\core\particles\DustParticleOptions.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */