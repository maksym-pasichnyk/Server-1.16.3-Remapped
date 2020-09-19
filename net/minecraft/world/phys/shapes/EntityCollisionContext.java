/*    */ package net.minecraft.world.phys.shapes;
/*    */ 
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.item.Items;
/*    */ import net.minecraft.world.level.material.FlowingFluid;
/*    */ import net.minecraft.world.level.material.Fluid;
/*    */ import net.minecraft.world.level.material.FluidState;
/*    */ 
/*    */ public class EntityCollisionContext
/*    */   implements CollisionContext
/*    */ {
/* 17 */   protected static final CollisionContext EMPTY = new EntityCollisionContext(false, -1.7976931348623157E308D, Items.AIR, debug0 -> false)
/*    */     {
/*    */       public boolean isAbove(VoxelShape debug1, BlockPos debug2, boolean debug3) {
/* 20 */         return debug3;
/*    */       }
/*    */     };
/*    */   
/*    */   private final boolean descending;
/*    */   private final double entityBottom;
/*    */   private final Item heldItem;
/*    */   private final Predicate<Fluid> canStandOnFluid;
/*    */   
/*    */   protected EntityCollisionContext(boolean debug1, double debug2, Item debug4, Predicate<Fluid> debug5) {
/* 30 */     this.descending = debug1;
/* 31 */     this.entityBottom = debug2;
/* 32 */     this.heldItem = debug4;
/* 33 */     this.canStandOnFluid = debug5;
/*    */   }
/*    */ 
/*    */   
/*    */   @Deprecated
/*    */   protected EntityCollisionContext(Entity debug1) {
/* 39 */     this(debug1
/* 40 */         .isDescending(), debug1
/* 41 */         .getY(), (debug1 instanceof LivingEntity) ? ((LivingEntity)debug1)
/* 42 */         .getMainHandItem().getItem() : Items.AIR, (debug1 instanceof LivingEntity) ? (LivingEntity)debug1::canStandOnFluid : (debug0 -> false));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isHoldingItem(Item debug1) {
/* 49 */     return (this.heldItem == debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canStandOnFluid(FluidState debug1, FlowingFluid debug2) {
/* 54 */     return (this.canStandOnFluid.test(debug2) && !debug1.getType().isSame((Fluid)debug2));
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isDescending() {
/* 59 */     return this.descending;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isAbove(VoxelShape debug1, BlockPos debug2, boolean debug3) {
/* 64 */     return (this.entityBottom > debug2.getY() + debug1.max(Direction.Axis.Y) - 9.999999747378752E-6D);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\phys\shapes\EntityCollisionContext.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */