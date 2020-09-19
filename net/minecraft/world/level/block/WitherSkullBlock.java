/*     */ package net.minecraft.world.level.block;
/*     */ import java.util.function.Predicate;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.advancements.CriteriaTriggers;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.tags.Tag;
/*     */ import net.minecraft.world.Difficulty;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.boss.wither.WitherBoss;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.SkullBlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.pattern.BlockInWorld;
/*     */ import net.minecraft.world.level.block.state.pattern.BlockPattern;
/*     */ import net.minecraft.world.level.block.state.pattern.BlockPatternBuilder;
/*     */ import net.minecraft.world.level.block.state.predicate.BlockMaterialPredicate;
/*     */ import net.minecraft.world.level.block.state.predicate.BlockStatePredicate;
/*     */ import net.minecraft.world.level.material.Material;
/*     */ 
/*     */ public class WitherSkullBlock extends SkullBlock {
/*     */   @Nullable
/*     */   private static BlockPattern witherPatternFull;
/*     */   
/*     */   protected WitherSkullBlock(BlockBehaviour.Properties debug1) {
/*  35 */     super(SkullBlock.Types.WITHER_SKELETON, debug1);
/*     */   }
/*     */   @Nullable
/*     */   private static BlockPattern witherPatternBase;
/*     */   public void setPlacedBy(Level debug1, BlockPos debug2, BlockState debug3, @Nullable LivingEntity debug4, ItemStack debug5) {
/*  40 */     super.setPlacedBy(debug1, debug2, debug3, debug4, debug5);
/*     */     
/*  42 */     BlockEntity debug6 = debug1.getBlockEntity(debug2);
/*  43 */     if (debug6 instanceof SkullBlockEntity) {
/*  44 */       checkSpawn(debug1, debug2, (SkullBlockEntity)debug6);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void checkSpawn(Level debug0, BlockPos debug1, SkullBlockEntity debug2) {
/*  49 */     if (debug0.isClientSide) {
/*     */       return;
/*     */     }
/*  52 */     BlockState debug3 = debug2.getBlockState();
/*  53 */     boolean debug4 = (debug3.is(Blocks.WITHER_SKELETON_SKULL) || debug3.is(Blocks.WITHER_SKELETON_WALL_SKULL));
/*  54 */     if (!debug4 || debug1.getY() < 0 || debug0.getDifficulty() == Difficulty.PEACEFUL) {
/*     */       return;
/*     */     }
/*     */     
/*  58 */     BlockPattern debug5 = getOrCreateWitherFull();
/*  59 */     BlockPattern.BlockPatternMatch debug6 = debug5.find((LevelReader)debug0, debug1);
/*  60 */     if (debug6 == null) {
/*     */       return;
/*     */     }
/*     */     
/*  64 */     for (int i = 0; i < debug5.getWidth(); i++) {
/*  65 */       for (int j = 0; j < debug5.getHeight(); j++) {
/*  66 */         BlockInWorld blockInWorld = debug6.getBlock(i, j, 0);
/*  67 */         debug0.setBlock(blockInWorld.getPos(), Blocks.AIR.defaultBlockState(), 2);
/*  68 */         debug0.levelEvent(2001, blockInWorld.getPos(), Block.getId(blockInWorld.getState()));
/*     */       } 
/*     */     } 
/*     */     
/*  72 */     WitherBoss debug7 = (WitherBoss)EntityType.WITHER.create(debug0);
/*  73 */     BlockPos debug8 = debug6.getBlock(1, 2, 0).getPos();
/*  74 */     debug7.moveTo(debug8.getX() + 0.5D, debug8.getY() + 0.55D, debug8.getZ() + 0.5D, (debug6.getForwards().getAxis() == Direction.Axis.X) ? 0.0F : 90.0F, 0.0F);
/*  75 */     debug7.yBodyRot = (debug6.getForwards().getAxis() == Direction.Axis.X) ? 0.0F : 90.0F;
/*  76 */     debug7.makeInvulnerable();
/*     */     
/*  78 */     for (ServerPlayer debug10 : debug0.getEntitiesOfClass(ServerPlayer.class, debug7.getBoundingBox().inflate(50.0D))) {
/*  79 */       CriteriaTriggers.SUMMONED_ENTITY.trigger(debug10, (Entity)debug7);
/*     */     }
/*     */     
/*  82 */     debug0.addFreshEntity((Entity)debug7);
/*     */     
/*  84 */     for (int debug9 = 0; debug9 < debug5.getWidth(); debug9++) {
/*  85 */       for (int debug10 = 0; debug10 < debug5.getHeight(); debug10++) {
/*  86 */         debug0.blockUpdated(debug6.getBlock(debug9, debug10, 0).getPos(), Blocks.AIR);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public static boolean canSpawnMob(Level debug0, BlockPos debug1, ItemStack debug2) {
/*  92 */     if (debug2.getItem() == Items.WITHER_SKELETON_SKULL && debug1.getY() >= 2 && debug0.getDifficulty() != Difficulty.PEACEFUL && !debug0.isClientSide) {
/*  93 */       return (getOrCreateWitherBase().find((LevelReader)debug0, debug1) != null);
/*     */     }
/*     */     
/*  96 */     return false;
/*     */   }
/*     */   
/*     */   private static BlockPattern getOrCreateWitherFull() {
/* 100 */     if (witherPatternFull == null)
/*     */     {
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
/* 112 */       witherPatternFull = BlockPatternBuilder.start().aisle(new String[] { "^^^", "###", "~#~" }).where('#', debug0 -> debug0.getState().is((Tag)BlockTags.WITHER_SUMMON_BASE_BLOCKS)).where('^', BlockInWorld.hasState(BlockStatePredicate.forBlock(Blocks.WITHER_SKELETON_SKULL).or((Predicate)BlockStatePredicate.forBlock(Blocks.WITHER_SKELETON_WALL_SKULL)))).where('~', BlockInWorld.hasState((Predicate)BlockMaterialPredicate.forMaterial(Material.AIR))).build();
/*     */     }
/*     */     
/* 115 */     return witherPatternFull;
/*     */   }
/*     */   
/*     */   private static BlockPattern getOrCreateWitherBase() {
/* 119 */     if (witherPatternBase == null)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 128 */       witherPatternBase = BlockPatternBuilder.start().aisle(new String[] { "   ", "###", "~#~" }).where('#', debug0 -> debug0.getState().is((Tag)BlockTags.WITHER_SUMMON_BASE_BLOCKS)).where('~', BlockInWorld.hasState((Predicate)BlockMaterialPredicate.forMaterial(Material.AIR))).build();
/*     */     }
/*     */     
/* 131 */     return witherPatternBase;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\WitherSkullBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */