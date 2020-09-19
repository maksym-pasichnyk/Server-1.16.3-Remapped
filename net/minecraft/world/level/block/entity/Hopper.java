/*    */ package net.minecraft.world.level.block.entity;
/*    */ 
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.world.Container;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.phys.shapes.Shapes;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public interface Hopper
/*    */   extends Container {
/* 12 */   public static final VoxelShape INSIDE = Block.box(2.0D, 11.0D, 2.0D, 14.0D, 16.0D, 14.0D);
/* 13 */   public static final VoxelShape ABOVE = Block.box(0.0D, 16.0D, 0.0D, 16.0D, 32.0D, 16.0D);
/* 14 */   public static final VoxelShape SUCK = Shapes.or(INSIDE, ABOVE);
/*    */   
/*    */   default VoxelShape getSuckShape() {
/* 17 */     return SUCK;
/*    */   }
/*    */   
/*    */   @Nullable
/*    */   Level getLevel();
/*    */   
/*    */   double getLevelX();
/*    */   
/*    */   double getLevelY();
/*    */   
/*    */   double getLevelZ();
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\entity\Hopper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */