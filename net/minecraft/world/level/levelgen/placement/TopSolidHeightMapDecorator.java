/*    */ package net.minecraft.world.level.levelgen.placement;
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.world.level.levelgen.Heightmap;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.DecoratorConfiguration;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.NoneDecoratorConfiguration;
/*    */ 
/*    */ public class TopSolidHeightMapDecorator extends BaseHeightmapDecorator<NoneDecoratorConfiguration> {
/*    */   public TopSolidHeightMapDecorator(Codec<NoneDecoratorConfiguration> debug1) {
/*  9 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   protected Heightmap.Types type(NoneDecoratorConfiguration debug1) {
/* 14 */     return Heightmap.Types.OCEAN_FLOOR_WG;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\placement\TopSolidHeightMapDecorator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */