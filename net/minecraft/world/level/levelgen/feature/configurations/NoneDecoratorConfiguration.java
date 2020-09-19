/*   */ package net.minecraft.world.level.levelgen.feature.configurations;
/*   */ 
/*   */ import com.mojang.serialization.Codec;
/*   */ 
/*   */ public class NoneDecoratorConfiguration implements DecoratorConfiguration {
/* 6 */   public static final Codec<NoneDecoratorConfiguration> CODEC = Codec.unit(() -> INSTANCE);
/*   */   
/* 8 */   public static final NoneDecoratorConfiguration INSTANCE = new NoneDecoratorConfiguration();
/*   */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\feature\configurations\NoneDecoratorConfiguration.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */