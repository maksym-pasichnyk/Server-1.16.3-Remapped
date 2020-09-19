/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import java.util.Random;
/*     */ import net.minecraft.advancements.CriteriaTriggers;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.stats.Stats;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.projectile.Projectile;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.IntegerProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class TargetBlock
/*     */   extends Block
/*     */ {
/*  28 */   private static final IntegerProperty OUTPUT_POWER = BlockStateProperties.POWER;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TargetBlock(BlockBehaviour.Properties debug1) {
/*  34 */     super(debug1);
/*  35 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)OUTPUT_POWER, Integer.valueOf(0)));
/*     */   }
/*     */ 
/*     */   
/*     */   public void onProjectileHit(Level debug1, BlockState debug2, BlockHitResult debug3, Projectile debug4) {
/*  40 */     int debug5 = updateRedstoneOutput((LevelAccessor)debug1, debug2, debug3, (Entity)debug4);
/*     */     
/*  42 */     Entity debug6 = debug4.getOwner();
/*  43 */     if (debug6 instanceof ServerPlayer) {
/*  44 */       ServerPlayer debug7 = (ServerPlayer)debug6;
/*  45 */       debug7.awardStat(Stats.TARGET_HIT);
/*  46 */       CriteriaTriggers.TARGET_BLOCK_HIT.trigger(debug7, (Entity)debug4, debug3.getLocation(), debug5);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static int updateRedstoneOutput(LevelAccessor debug0, BlockState debug1, BlockHitResult debug2, Entity debug3) {
/*  51 */     int debug4 = getRedstoneStrength(debug2, debug2.getLocation());
/*  52 */     int debug5 = (debug3 instanceof net.minecraft.world.entity.projectile.AbstractArrow) ? 20 : 8;
/*     */     
/*  54 */     if (!debug0.getBlockTicks().hasScheduledTick(debug2.getBlockPos(), debug1.getBlock())) {
/*  55 */       setOutputPower(debug0, debug1, debug4, debug2.getBlockPos(), debug5);
/*     */     }
/*     */     
/*  58 */     return debug4;
/*     */   }
/*     */   private static int getRedstoneStrength(BlockHitResult debug0, Vec3 debug1) {
/*     */     double debug9;
/*  62 */     Direction debug2 = debug0.getDirection();
/*  63 */     double debug3 = Math.abs(Mth.frac(debug1.x) - 0.5D);
/*  64 */     double debug5 = Math.abs(Mth.frac(debug1.y) - 0.5D);
/*  65 */     double debug7 = Math.abs(Mth.frac(debug1.z) - 0.5D);
/*     */ 
/*     */     
/*  68 */     Direction.Axis debug11 = debug2.getAxis();
/*  69 */     if (debug11 == Direction.Axis.Y) {
/*  70 */       debug9 = Math.max(debug3, debug7);
/*  71 */     } else if (debug11 == Direction.Axis.Z) {
/*  72 */       debug9 = Math.max(debug3, debug5);
/*     */     } else {
/*  74 */       debug9 = Math.max(debug5, debug7);
/*     */     } 
/*     */     
/*  77 */     return Math.max(1, Mth.ceil(15.0D * Mth.clamp((0.5D - debug9) / 0.5D, 0.0D, 1.0D)));
/*     */   }
/*     */   
/*     */   private static void setOutputPower(LevelAccessor debug0, BlockState debug1, int debug2, BlockPos debug3, int debug4) {
/*  81 */     debug0.setBlock(debug3, (BlockState)debug1.setValue((Property)OUTPUT_POWER, Integer.valueOf(debug2)), 3);
/*  82 */     debug0.getBlockTicks().scheduleTick(debug3, debug1.getBlock(), debug4);
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick(BlockState debug1, ServerLevel debug2, BlockPos debug3, Random debug4) {
/*  87 */     if (((Integer)debug1.getValue((Property)OUTPUT_POWER)).intValue() != 0) {
/*  88 */       debug2.setBlock(debug3, (BlockState)debug1.setValue((Property)OUTPUT_POWER, Integer.valueOf(0)), 3);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public int getSignal(BlockState debug1, BlockGetter debug2, BlockPos debug3, Direction debug4) {
/*  94 */     return ((Integer)debug1.getValue((Property)OUTPUT_POWER)).intValue();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isSignalSource(BlockState debug1) {
/*  99 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> debug1) {
/* 104 */     debug1.add(new Property[] { (Property)OUTPUT_POWER });
/*     */   }
/*     */ 
/*     */   
/*     */   public void onPlace(BlockState debug1, Level debug2, BlockPos debug3, BlockState debug4, boolean debug5) {
/* 109 */     if (debug2.isClientSide() || debug1.is(debug4.getBlock())) {
/*     */       return;
/*     */     }
/*     */     
/* 113 */     if (((Integer)debug1.getValue((Property)OUTPUT_POWER)).intValue() > 0 && !debug2.getBlockTicks().hasScheduledTick(debug3, this))
/* 114 */       debug2.setBlock(debug3, (BlockState)debug1.setValue((Property)OUTPUT_POWER, Integer.valueOf(0)), 18); 
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\TargetBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */