/*    */ package net.minecraft.world.level.levelgen;
/*    */ 
/*    */ import net.minecraft.util.UniformInt;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.CountConfiguration;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.DecoratorConfiguration;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.NoneDecoratorConfiguration;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.RangeDecoratorConfiguration;
/*    */ import net.minecraft.world.level.levelgen.placement.ChanceDecoratorConfiguration;
/*    */ import net.minecraft.world.level.levelgen.placement.ConfiguredDecorator;
/*    */ import net.minecraft.world.level.levelgen.placement.FeatureDecorator;
/*    */ 
/*    */ public interface Decoratable<R>
/*    */ {
/*    */   default R chance(int debug1) {
/* 15 */     return decorated(FeatureDecorator.CHANCE.configured((DecoratorConfiguration)new ChanceDecoratorConfiguration(debug1)));
/*    */   }
/*    */   R decorated(ConfiguredDecorator<?> paramConfiguredDecorator);
/*    */   default R count(UniformInt debug1) {
/* 19 */     return decorated(FeatureDecorator.COUNT.configured((DecoratorConfiguration)new CountConfiguration(debug1)));
/*    */   }
/*    */   
/*    */   default R count(int debug1) {
/* 23 */     return count(UniformInt.fixed(debug1));
/*    */   }
/*    */   
/*    */   default R countRandom(int debug1) {
/* 27 */     return count(UniformInt.of(0, debug1));
/*    */   }
/*    */   
/*    */   default R range(int debug1) {
/* 31 */     return decorated(FeatureDecorator.RANGE.configured((DecoratorConfiguration)new RangeDecoratorConfiguration(0, 0, debug1)));
/*    */   }
/*    */   
/*    */   default R squared() {
/* 35 */     return decorated(FeatureDecorator.SQUARE.configured((DecoratorConfiguration)NoneDecoratorConfiguration.INSTANCE));
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\Decoratable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */