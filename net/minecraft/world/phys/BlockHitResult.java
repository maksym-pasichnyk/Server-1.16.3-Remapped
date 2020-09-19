/*    */ package net.minecraft.world.phys;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ 
/*    */ public class BlockHitResult extends HitResult {
/*    */   private final Direction direction;
/*    */   private final BlockPos blockPos;
/*    */   private final boolean miss;
/*    */   private final boolean inside;
/*    */   
/*    */   public static BlockHitResult miss(Vec3 debug0, Direction debug1, BlockPos debug2) {
/* 13 */     return new BlockHitResult(true, debug0, debug1, debug2, false);
/*    */   }
/*    */   
/*    */   public BlockHitResult(Vec3 debug1, Direction debug2, BlockPos debug3, boolean debug4) {
/* 17 */     this(false, debug1, debug2, debug3, debug4);
/*    */   }
/*    */   
/*    */   private BlockHitResult(boolean debug1, Vec3 debug2, Direction debug3, BlockPos debug4, boolean debug5) {
/* 21 */     super(debug2);
/*    */     
/* 23 */     this.miss = debug1;
/* 24 */     this.direction = debug3;
/* 25 */     this.blockPos = debug4;
/* 26 */     this.inside = debug5;
/*    */   }
/*    */   
/*    */   public BlockHitResult withDirection(Direction debug1) {
/* 30 */     return new BlockHitResult(this.miss, this.location, debug1, this.blockPos, this.inside);
/*    */   }
/*    */   
/*    */   public BlockHitResult withPosition(BlockPos debug1) {
/* 34 */     return new BlockHitResult(this.miss, this.location, this.direction, debug1, this.inside);
/*    */   }
/*    */   
/*    */   public BlockPos getBlockPos() {
/* 38 */     return this.blockPos;
/*    */   }
/*    */   
/*    */   public Direction getDirection() {
/* 42 */     return this.direction;
/*    */   }
/*    */ 
/*    */   
/*    */   public HitResult.Type getType() {
/* 47 */     return this.miss ? HitResult.Type.MISS : HitResult.Type.BLOCK;
/*    */   }
/*    */   
/*    */   public boolean isInside() {
/* 51 */     return this.inside;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\phys\BlockHitResult.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */