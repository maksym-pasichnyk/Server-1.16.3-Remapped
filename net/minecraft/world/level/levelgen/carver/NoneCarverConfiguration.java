/*   */ package net.minecraft.world.level.levelgen.carver;
/*   */ 
/*   */ import com.mojang.serialization.Codec;
/*   */ 
/*   */ public class NoneCarverConfiguration implements CarverConfiguration {
/* 6 */   public static final Codec<NoneCarverConfiguration> CODEC = Codec.unit(() -> INSTANCE);
/*   */   
/* 8 */   public static final NoneCarverConfiguration INSTANCE = new NoneCarverConfiguration();
/*   */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\carver\NoneCarverConfiguration.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */