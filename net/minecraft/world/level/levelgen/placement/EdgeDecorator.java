/*   */ package net.minecraft.world.level.levelgen.placement;
/*   */ 
/*   */ import com.mojang.serialization.Codec;
/*   */ import net.minecraft.world.level.levelgen.Heightmap;
/*   */ import net.minecraft.world.level.levelgen.feature.configurations.DecoratorConfiguration;
/*   */ 
/*   */ public abstract class EdgeDecorator<DC extends DecoratorConfiguration> extends FeatureDecorator<DC> {
/*   */   public EdgeDecorator(Codec<DC> debug1) {
/* 9 */     super(debug1);
/*   */   }
/*   */   
/*   */   protected abstract Heightmap.Types type(DC paramDC);
/*   */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\placement\EdgeDecorator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */