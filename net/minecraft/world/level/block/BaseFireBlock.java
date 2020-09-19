/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.portal.PortalShape;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class BaseFireBlock
/*     */   extends Block
/*     */ {
/*     */   private final float fireDamage;
/*  28 */   protected static final VoxelShape DOWN_AABB = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 1.0D, 16.0D);
/*     */   
/*     */   public BaseFireBlock(BlockBehaviour.Properties debug1, float debug2) {
/*  31 */     super(debug1);
/*  32 */     this.fireDamage = debug2;
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext debug1) {
/*  37 */     return getState((BlockGetter)debug1.getLevel(), debug1.getClickedPos());
/*     */   }
/*     */   
/*     */   public static BlockState getState(BlockGetter debug0, BlockPos debug1) {
/*  41 */     BlockPos debug2 = debug1.below();
/*  42 */     BlockState debug3 = debug0.getBlockState(debug2);
/*     */     
/*  44 */     if (SoulFireBlock.canSurviveOnBlock(debug3.getBlock())) {
/*  45 */       return Blocks.SOUL_FIRE.defaultBlockState();
/*     */     }
/*     */     
/*  48 */     return ((FireBlock)Blocks.FIRE).getStateForPlacement(debug0, debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public VoxelShape getShape(BlockState debug1, BlockGetter debug2, BlockPos debug3, CollisionContext debug4) {
/*  53 */     return DOWN_AABB;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract boolean canBurn(BlockState paramBlockState);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void entityInside(BlockState debug1, Level debug2, BlockPos debug3, Entity debug4) {
/* 119 */     if (!debug4.fireImmune()) {
/* 120 */       debug4.setRemainingFireTicks(debug4.getRemainingFireTicks() + 1);
/* 121 */       if (debug4.getRemainingFireTicks() == 0) {
/* 122 */         debug4.setSecondsOnFire(8);
/*     */       }
/*     */       
/* 125 */       debug4.hurt(DamageSource.IN_FIRE, this.fireDamage);
/*     */     } 
/*     */     
/* 128 */     super.entityInside(debug1, debug2, debug3, debug4);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onPlace(BlockState debug1, Level debug2, BlockPos debug3, BlockState debug4, boolean debug5) {
/* 133 */     if (debug4.is(debug1.getBlock())) {
/*     */       return;
/*     */     }
/* 136 */     if (inPortalDimension(debug2)) {
/* 137 */       Optional<PortalShape> debug6 = PortalShape.findEmptyPortalShape((LevelAccessor)debug2, debug3, Direction.Axis.X);
/*     */       
/* 139 */       if (debug6.isPresent()) {
/* 140 */         ((PortalShape)debug6.get()).createPortalBlocks();
/*     */         
/*     */         return;
/*     */       } 
/*     */     } 
/*     */     
/* 146 */     if (!debug1.canSurvive((LevelReader)debug2, debug3)) {
/* 147 */       debug2.removeBlock(debug3, false);
/*     */     }
/*     */   }
/*     */   
/*     */   private static boolean inPortalDimension(Level debug0) {
/* 152 */     return (debug0.dimension() == Level.OVERWORLD || debug0.dimension() == Level.NETHER);
/*     */   }
/*     */ 
/*     */   
/*     */   public void playerWillDestroy(Level debug1, BlockPos debug2, BlockState debug3, Player debug4) {
/* 157 */     if (!debug1.isClientSide()) {
/* 158 */       debug1.levelEvent(null, 1009, debug2, 0);
/*     */     }
/*     */   }
/*     */   
/*     */   public static boolean canBePlacedAt(Level debug0, BlockPos debug1, Direction debug2) {
/* 163 */     BlockState debug3 = debug0.getBlockState(debug1);
/*     */     
/* 165 */     if (!debug3.isAir()) {
/* 166 */       return false;
/*     */     }
/*     */     
/* 169 */     return (getState((BlockGetter)debug0, debug1).canSurvive((LevelReader)debug0, debug1) || isPortal(debug0, debug1, debug2));
/*     */   }
/*     */   
/*     */   private static boolean isPortal(Level debug0, BlockPos debug1, Direction debug2) {
/* 173 */     if (!inPortalDimension(debug0)) {
/* 174 */       return false;
/*     */     }
/* 176 */     BlockPos.MutableBlockPos debug3 = debug1.mutable();
/* 177 */     boolean debug4 = false;
/* 178 */     for (Direction debug8 : Direction.values()) {
/* 179 */       if (debug0.getBlockState((BlockPos)debug3.set((Vec3i)debug1).move(debug8)).is(Blocks.OBSIDIAN)) {
/* 180 */         debug4 = true;
/*     */         break;
/*     */       } 
/*     */     } 
/* 184 */     return (debug4 && PortalShape.findEmptyPortalShape((LevelAccessor)debug0, debug1, debug2.getCounterClockWise().getAxis()).isPresent());
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\BaseFireBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */