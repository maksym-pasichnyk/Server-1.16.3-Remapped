/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import java.util.Random;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.MobSpawnType;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.GameRules;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.portal.PortalShape;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ 
/*     */ public class NetherPortalBlock
/*     */   extends Block
/*     */ {
/*  28 */   public static final EnumProperty<Direction.Axis> AXIS = BlockStateProperties.HORIZONTAL_AXIS;
/*     */ 
/*     */   
/*  31 */   protected static final VoxelShape X_AXIS_AABB = Block.box(0.0D, 0.0D, 6.0D, 16.0D, 16.0D, 10.0D);
/*  32 */   protected static final VoxelShape Z_AXIS_AABB = Block.box(6.0D, 0.0D, 0.0D, 10.0D, 16.0D, 16.0D);
/*     */   
/*     */   public NetherPortalBlock(BlockBehaviour.Properties debug1) {
/*  35 */     super(debug1);
/*  36 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)AXIS, (Comparable)Direction.Axis.X));
/*     */   }
/*     */ 
/*     */   
/*     */   public VoxelShape getShape(BlockState debug1, BlockGetter debug2, BlockPos debug3, CollisionContext debug4) {
/*  41 */     switch ((Direction.Axis)debug1.getValue((Property)AXIS)) {
/*     */       case COUNTERCLOCKWISE_90:
/*  43 */         return Z_AXIS_AABB;
/*     */     } 
/*     */     
/*  46 */     return X_AXIS_AABB;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void randomTick(BlockState debug1, ServerLevel debug2, BlockPos debug3, Random debug4) {
/*  52 */     if (debug2.dimensionType().natural() && debug2.getGameRules().getBoolean(GameRules.RULE_DOMOBSPAWNING) && debug4.nextInt(2000) < debug2.getDifficulty().getId()) {
/*     */       
/*  54 */       while (debug2.getBlockState(debug3).is(this)) {
/*  55 */         debug3 = debug3.below();
/*     */       }
/*  57 */       if (debug2.getBlockState(debug3).isValidSpawn((BlockGetter)debug2, debug3, EntityType.ZOMBIFIED_PIGLIN)) {
/*  58 */         Entity debug5 = EntityType.ZOMBIFIED_PIGLIN.spawn(debug2, null, null, null, debug3.above(), MobSpawnType.STRUCTURE, false, false);
/*  59 */         if (debug5 != null) {
/*  60 */           debug5.setPortalCooldown();
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState updateShape(BlockState debug1, Direction debug2, BlockState debug3, LevelAccessor debug4, BlockPos debug5, BlockPos debug6) {
/*  68 */     Direction.Axis debug7 = debug2.getAxis();
/*  69 */     Direction.Axis debug8 = (Direction.Axis)debug1.getValue((Property)AXIS);
/*     */     
/*  71 */     boolean debug9 = (debug8 != debug7 && debug7.isHorizontal());
/*  72 */     if (debug9 || debug3.is(this) || (new PortalShape(debug4, debug5, debug8)).isComplete()) {
/*  73 */       return super.updateShape(debug1, debug2, debug3, debug4, debug5, debug6);
/*     */     }
/*     */     
/*  76 */     return Blocks.AIR.defaultBlockState();
/*     */   }
/*     */ 
/*     */   
/*     */   public void entityInside(BlockState debug1, Level debug2, BlockPos debug3, Entity debug4) {
/*  81 */     if (!debug4.isPassenger() && !debug4.isVehicle() && debug4.canChangeDimensions()) {
/*  82 */       debug4.handleInsidePortal(debug3);
/*     */     }
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
/*     */ 
/*     */ 
/*     */   
/*     */   public BlockState rotate(BlockState debug1, Rotation debug2) {
/* 120 */     switch (debug2) {
/*     */       case COUNTERCLOCKWISE_90:
/*     */       case CLOCKWISE_90:
/* 123 */         switch ((Direction.Axis)debug1.getValue((Property)AXIS)) {
/*     */           case CLOCKWISE_90:
/* 125 */             return (BlockState)debug1.setValue((Property)AXIS, (Comparable)Direction.Axis.Z);
/*     */           case COUNTERCLOCKWISE_90:
/* 127 */             return (BlockState)debug1.setValue((Property)AXIS, (Comparable)Direction.Axis.X);
/*     */         } 
/* 129 */         return debug1;
/*     */     } 
/*     */     
/* 132 */     return debug1;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> debug1) {
/* 138 */     debug1.add(new Property[] { (Property)AXIS });
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\NetherPortalBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */