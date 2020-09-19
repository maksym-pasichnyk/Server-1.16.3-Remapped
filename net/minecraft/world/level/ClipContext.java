/*    */ package net.minecraft.world.level;
/*    */ 
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.material.FluidState;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ import net.minecraft.world.phys.shapes.CollisionContext;
/*    */ import net.minecraft.world.phys.shapes.Shapes;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class ClipContext {
/*    */   private final Vec3 from;
/*    */   private final Vec3 to;
/*    */   private final Block block;
/*    */   private final Fluid fluid;
/*    */   private final CollisionContext collisionContext;
/*    */   
/*    */   public ClipContext(Vec3 debug1, Vec3 debug2, Block debug3, Fluid debug4, Entity debug5) {
/* 22 */     this.from = debug1;
/* 23 */     this.to = debug2;
/* 24 */     this.block = debug3;
/* 25 */     this.fluid = debug4;
/* 26 */     this.collisionContext = CollisionContext.of(debug5);
/*    */   }
/*    */   
/*    */   public Vec3 getTo() {
/* 30 */     return this.to;
/*    */   }
/*    */   
/*    */   public Vec3 getFrom() {
/* 34 */     return this.from;
/*    */   }
/*    */   
/*    */   public VoxelShape getBlockShape(BlockState debug1, BlockGetter debug2, BlockPos debug3) {
/* 38 */     return this.block.get(debug1, debug2, debug3, this.collisionContext);
/*    */   }
/*    */   
/*    */   public VoxelShape getFluidShape(FluidState debug1, BlockGetter debug2, BlockPos debug3) {
/* 42 */     return this.fluid.canPick(debug1) ? debug1.getShape(debug2, debug3) : Shapes.empty();
/*    */   }
/*    */   
/*    */   public enum Block implements ShapeGetter {
/* 46 */     COLLIDER((String)BlockBehaviour.BlockStateBase::getCollisionShape),
/* 47 */     OUTLINE((String)BlockBehaviour.BlockStateBase::getShape),
/* 48 */     VISUAL((String)BlockBehaviour.BlockStateBase::getVisualShape);
/*    */     
/*    */     private final ClipContext.ShapeGetter shapeGetter;
/*    */ 
/*    */     
/*    */     Block(ClipContext.ShapeGetter debug3) {
/* 54 */       this.shapeGetter = debug3;
/*    */     }
/*    */ 
/*    */     
/*    */     public VoxelShape get(BlockState debug1, BlockGetter debug2, BlockPos debug3, CollisionContext debug4) {
/* 59 */       return this.shapeGetter.get(debug1, debug2, debug3, debug4);
/*    */     }
/*    */   }
/*    */   
/*    */   public static interface ShapeGetter {
/*    */     VoxelShape get(BlockState param1BlockState, BlockGetter param1BlockGetter, BlockPos param1BlockPos, CollisionContext param1CollisionContext);
/*    */   }
/*    */   
/*    */   public enum Fluid {
/* 68 */     NONE((String)(debug0 -> false)),
/* 69 */     SOURCE_ONLY((String)FluidState::isSource), ANY((String)FluidState::isSource); static {
/* 70 */       ANY = new Fluid("ANY", 2, debug0 -> !debug0.isEmpty());
/*    */     }
/*    */     
/*    */     private final Predicate<FluidState> canPick;
/*    */     
/*    */     Fluid(Predicate<FluidState> debug3) {
/* 76 */       this.canPick = debug3;
/*    */     }
/*    */     
/*    */     public boolean canPick(FluidState debug1) {
/* 80 */       return this.canPick.test(debug1);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\ClipContext.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */