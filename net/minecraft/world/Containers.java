/*    */ package net.minecraft.world;
/*    */ 
/*    */ import java.util.Random;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.NonNullList;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.item.ItemEntity;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public class Containers
/*    */ {
/* 14 */   private static final Random RANDOM = new Random();
/*    */   
/*    */   public static void dropContents(Level debug0, BlockPos debug1, Container debug2) {
/* 17 */     dropContents(debug0, debug1.getX(), debug1.getY(), debug1.getZ(), debug2);
/*    */   }
/*    */   
/*    */   public static void dropContents(Level debug0, Entity debug1, Container debug2) {
/* 21 */     dropContents(debug0, debug1.getX(), debug1.getY(), debug1.getZ(), debug2);
/*    */   }
/*    */   
/*    */   private static void dropContents(Level debug0, double debug1, double debug3, double debug5, Container debug7) {
/* 25 */     for (int debug8 = 0; debug8 < debug7.getContainerSize(); debug8++) {
/* 26 */       dropItemStack(debug0, debug1, debug3, debug5, debug7.getItem(debug8));
/*    */     }
/*    */   }
/*    */   
/*    */   public static void dropContents(Level debug0, BlockPos debug1, NonNullList<ItemStack> debug2) {
/* 31 */     debug2.forEach(debug2 -> dropItemStack(debug0, debug1.getX(), debug1.getY(), debug1.getZ(), debug2));
/*    */   }
/*    */   
/*    */   public static void dropItemStack(Level debug0, double debug1, double debug3, double debug5, ItemStack debug7) {
/* 35 */     double debug8 = EntityType.ITEM.getWidth();
/* 36 */     double debug10 = 1.0D - debug8;
/* 37 */     double debug12 = debug8 / 2.0D;
/* 38 */     double debug14 = Math.floor(debug1) + RANDOM.nextDouble() * debug10 + debug12;
/* 39 */     double debug16 = Math.floor(debug3) + RANDOM.nextDouble() * debug10;
/* 40 */     double debug18 = Math.floor(debug5) + RANDOM.nextDouble() * debug10 + debug12;
/*    */     
/* 42 */     while (!debug7.isEmpty()) {
/* 43 */       ItemEntity debug20 = new ItemEntity(debug0, debug14, debug16, debug18, debug7.split(RANDOM.nextInt(21) + 10));
/*    */       
/* 45 */       float debug21 = 0.05F;
/* 46 */       debug20.setDeltaMovement(RANDOM
/* 47 */           .nextGaussian() * 0.05000000074505806D, RANDOM
/* 48 */           .nextGaussian() * 0.05000000074505806D + 0.20000000298023224D, RANDOM
/* 49 */           .nextGaussian() * 0.05000000074505806D);
/*    */ 
/*    */       
/* 52 */       debug0.addFreshEntity((Entity)debug20);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\Containers.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */