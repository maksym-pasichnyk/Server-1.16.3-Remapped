/*    */ package net.minecraft.world.level.levelgen.placement;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.world.level.levelgen.Heightmap;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.DecoratorConfiguration;
/*    */ 
/*    */ public class HeightmapDecorator<DC extends DecoratorConfiguration> extends BaseHeightmapDecorator<DC> {
/*    */   public HeightmapDecorator(Codec<DC> debug1) {
/*  9 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   protected Heightmap.Types type(DC debug1) {
/* 14 */     return Heightmap.Types.MOTION_BLOCKING;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\placement\HeightmapDecorator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */