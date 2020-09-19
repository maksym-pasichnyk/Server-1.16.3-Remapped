/*    */ package net.minecraft.world.level.levelgen.placement;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.Random;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.world.level.levelgen.Decoratable;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.DecoratorConfiguration;
/*    */ 
/*    */ public class ConfiguredDecorator<DC extends DecoratorConfiguration> implements Decoratable<ConfiguredDecorator<?>> {
/*    */   static {
/* 13 */     CODEC = Registry.DECORATOR.dispatch("type", debug0 -> debug0.decorator, FeatureDecorator::configuredCodec);
/*    */   }
/*    */   
/*    */   public static final Codec<ConfiguredDecorator<?>> CODEC;
/*    */   
/*    */   public ConfiguredDecorator(FeatureDecorator<DC> debug1, DC debug2) {
/* 19 */     this.decorator = debug1;
/* 20 */     this.config = debug2;
/*    */   }
/*    */   private final FeatureDecorator<DC> decorator; private final DC config;
/*    */   public Stream<BlockPos> getPositions(DecorationContext debug1, Random debug2, BlockPos debug3) {
/* 24 */     return this.decorator.getPositions(debug1, debug2, this.config, debug3);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 29 */     return String.format("[%s %s]", new Object[] { Registry.DECORATOR.getKey(this.decorator), this.config });
/*    */   }
/*    */ 
/*    */   
/*    */   public ConfiguredDecorator<?> decorated(ConfiguredDecorator<?> debug1) {
/* 34 */     return new ConfiguredDecorator((FeatureDecorator)FeatureDecorator.DECORATED, (DC)new DecoratedDecoratorConfiguration(debug1, this));
/*    */   }
/*    */   
/*    */   public DC config() {
/* 38 */     return this.config;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\placement\ConfiguredDecorator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */