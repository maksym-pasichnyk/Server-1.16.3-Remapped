/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.Random;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.level.LevelAccessor;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
/*    */ 
/*    */ public class CoralMushroomFeature
/*    */   extends CoralFeature {
/*    */   public CoralMushroomFeature(Codec<NoneFeatureConfiguration> debug1) {
/* 14 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean placeFeature(LevelAccessor debug1, Random debug2, BlockPos debug3, BlockState debug4) {
/* 19 */     int debug5 = debug2.nextInt(3) + 3;
/* 20 */     int debug6 = debug2.nextInt(3) + 3;
/* 21 */     int debug7 = debug2.nextInt(3) + 3;
/*    */     
/* 23 */     int debug8 = debug2.nextInt(3) + 1;
/*    */     
/* 25 */     BlockPos.MutableBlockPos debug9 = debug3.mutable();
/*    */ 
/*    */ 
/*    */     
/* 29 */     for (int debug10 = 0; debug10 <= debug6; debug10++) {
/* 30 */       for (int debug11 = 0; debug11 <= debug5; debug11++) {
/* 31 */         for (int debug12 = 0; debug12 <= debug7; debug12++) {
/* 32 */           debug9.set(debug10 + debug3.getX(), debug11 + debug3.getY(), debug12 + debug3.getZ());
/* 33 */           debug9.move(Direction.DOWN, debug8);
/*    */ 
/*    */           
/* 36 */           if ((debug10 != 0 && debug10 != debug6) || (debug11 != 0 && debug11 != debug5))
/*    */           {
/*    */ 
/*    */             
/* 40 */             if ((debug12 != 0 && debug12 != debug7) || (debug11 != 0 && debug11 != debug5))
/*    */             {
/*    */ 
/*    */               
/* 44 */               if ((debug10 != 0 && debug10 != debug6) || (debug12 != 0 && debug12 != debug7))
/*    */               {
/*    */ 
/*    */ 
/*    */                 
/* 49 */                 if (debug10 == 0 || debug10 == debug6 || debug11 == 0 || debug11 == debug5 || debug12 == 0 || debug12 == debug7)
/*    */                 {
/*    */ 
/*    */ 
/*    */                   
/* 54 */                   if (debug2.nextFloat() >= 0.1F)
/*    */                   {
/*    */ 
/*    */                     
/* 58 */                     if (!placeCoralBlock(debug1, debug2, (BlockPos)debug9, debug4)); }  }  } 
/*    */             }
/*    */           }
/*    */         } 
/*    */       } 
/*    */     } 
/* 64 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\feature\CoralMushroomFeature.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */