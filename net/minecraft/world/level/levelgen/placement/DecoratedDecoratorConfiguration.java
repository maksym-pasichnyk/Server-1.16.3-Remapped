/*    */ package net.minecraft.world.level.levelgen.placement;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ 
/*    */ public class DecoratedDecoratorConfiguration implements DecoratorConfiguration {
/*    */   static {
/*  8 */     CODEC = RecordCodecBuilder.create(debug0 -> debug0.group((App)ConfiguredDecorator.CODEC.fieldOf("outer").forGetter(DecoratedDecoratorConfiguration::outer), (App)ConfiguredDecorator.CODEC.fieldOf("inner").forGetter(DecoratedDecoratorConfiguration::inner)).apply((Applicative)debug0, DecoratedDecoratorConfiguration::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final Codec<DecoratedDecoratorConfiguration> CODEC;
/*    */   
/*    */   private final ConfiguredDecorator<?> outer;
/*    */   private final ConfiguredDecorator<?> inner;
/*    */   
/*    */   public DecoratedDecoratorConfiguration(ConfiguredDecorator<?> debug1, ConfiguredDecorator<?> debug2) {
/* 18 */     this.outer = debug1;
/* 19 */     this.inner = debug2;
/*    */   }
/*    */   
/*    */   public ConfiguredDecorator<?> outer() {
/* 23 */     return this.outer;
/*    */   }
/*    */   
/*    */   public ConfiguredDecorator<?> inner() {
/* 27 */     return this.inner;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\placement\DecoratedDecoratorConfiguration.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */