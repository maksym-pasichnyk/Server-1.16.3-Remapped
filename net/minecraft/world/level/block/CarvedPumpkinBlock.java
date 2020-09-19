/*     */ package net.minecraft.world.level.block;
/*     */ import java.util.function.Predicate;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.advancements.CriteriaTriggers;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.animal.IronGolem;
/*     */ import net.minecraft.world.entity.animal.SnowGolem;
/*     */ import net.minecraft.world.item.Wearable;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.pattern.BlockInWorld;
/*     */ import net.minecraft.world.level.block.state.pattern.BlockPattern;
/*     */ import net.minecraft.world.level.block.state.pattern.BlockPatternBuilder;
/*     */ import net.minecraft.world.level.block.state.predicate.BlockMaterialPredicate;
/*     */ import net.minecraft.world.level.block.state.predicate.BlockStatePredicate;
/*     */ import net.minecraft.world.level.block.state.properties.DirectionProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.material.Material;
/*     */ 
/*     */ public class CarvedPumpkinBlock extends HorizontalDirectionalBlock implements Wearable {
/*  28 */   public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
/*     */   
/*     */   @Nullable
/*     */   private BlockPattern snowGolemBase;
/*     */   
/*     */   @Nullable
/*     */   private BlockPattern snowGolemFull;
/*     */   
/*     */   @Nullable
/*     */   private BlockPattern ironGolemBase;
/*     */   @Nullable
/*     */   private BlockPattern ironGolemFull;
/*     */   private static final Predicate<BlockState> PUMPKINS_PREDICATE;
/*     */   
/*     */   protected CarvedPumpkinBlock(BlockBehaviour.Properties debug1) {
/*  43 */     super(debug1);
/*  44 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)FACING, (Comparable)Direction.NORTH));
/*     */   }
/*     */ 
/*     */   
/*     */   public void onPlace(BlockState debug1, Level debug2, BlockPos debug3, BlockState debug4, boolean debug5) {
/*  49 */     if (debug4.is(debug1.getBlock())) {
/*     */       return;
/*     */     }
/*  52 */     trySpawnGolem(debug2, debug3);
/*     */   }
/*     */   
/*     */   public boolean canSpawnGolem(LevelReader debug1, BlockPos debug2) {
/*  56 */     return (getOrCreateSnowGolemBase().find(debug1, debug2) != null || getOrCreateIronGolemBase().find(debug1, debug2) != null);
/*     */   }
/*     */   
/*     */   private void trySpawnGolem(Level debug1, BlockPos debug2) {
/*  60 */     BlockPattern.BlockPatternMatch debug3 = getOrCreateSnowGolemFull().find((LevelReader)debug1, debug2);
/*  61 */     if (debug3 != null) {
/*  62 */       for (int i = 0; i < getOrCreateSnowGolemFull().getHeight(); i++) {
/*  63 */         BlockInWorld blockInWorld = debug3.getBlock(0, i, 0);
/*  64 */         debug1.setBlock(blockInWorld.getPos(), Blocks.AIR.defaultBlockState(), 2);
/*  65 */         debug1.levelEvent(2001, blockInWorld.getPos(), Block.getId(blockInWorld.getState()));
/*     */       } 
/*     */       
/*  68 */       SnowGolem debug4 = (SnowGolem)EntityType.SNOW_GOLEM.create(debug1);
/*  69 */       BlockPos debug5 = debug3.getBlock(0, 2, 0).getPos();
/*  70 */       debug4.moveTo(debug5.getX() + 0.5D, debug5.getY() + 0.05D, debug5.getZ() + 0.5D, 0.0F, 0.0F);
/*  71 */       debug1.addFreshEntity((Entity)debug4);
/*     */       
/*  73 */       for (ServerPlayer debug7 : debug1.getEntitiesOfClass(ServerPlayer.class, debug4.getBoundingBox().inflate(5.0D))) {
/*  74 */         CriteriaTriggers.SUMMONED_ENTITY.trigger(debug7, (Entity)debug4);
/*     */       }
/*     */       
/*  77 */       for (int debug6 = 0; debug6 < getOrCreateSnowGolemFull().getHeight(); debug6++) {
/*  78 */         BlockInWorld debug7 = debug3.getBlock(0, debug6, 0);
/*  79 */         debug1.blockUpdated(debug7.getPos(), Blocks.AIR);
/*     */       } 
/*     */     } else {
/*  82 */       debug3 = getOrCreateIronGolemFull().find((LevelReader)debug1, debug2);
/*  83 */       if (debug3 != null) {
/*  84 */         for (int i = 0; i < getOrCreateIronGolemFull().getWidth(); i++) {
/*  85 */           for (int j = 0; j < getOrCreateIronGolemFull().getHeight(); j++) {
/*  86 */             BlockInWorld blockInWorld = debug3.getBlock(i, j, 0);
/*  87 */             debug1.setBlock(blockInWorld.getPos(), Blocks.AIR.defaultBlockState(), 2);
/*  88 */             debug1.levelEvent(2001, blockInWorld.getPos(), Block.getId(blockInWorld.getState()));
/*     */           } 
/*     */         } 
/*     */         
/*  92 */         BlockPos debug4 = debug3.getBlock(1, 2, 0).getPos();
/*     */         
/*  94 */         IronGolem debug5 = (IronGolem)EntityType.IRON_GOLEM.create(debug1);
/*  95 */         debug5.setPlayerCreated(true);
/*  96 */         debug5.moveTo(debug4.getX() + 0.5D, debug4.getY() + 0.05D, debug4.getZ() + 0.5D, 0.0F, 0.0F);
/*  97 */         debug1.addFreshEntity((Entity)debug5);
/*     */         
/*  99 */         for (ServerPlayer debug7 : debug1.getEntitiesOfClass(ServerPlayer.class, debug5.getBoundingBox().inflate(5.0D))) {
/* 100 */           CriteriaTriggers.SUMMONED_ENTITY.trigger(debug7, (Entity)debug5);
/*     */         }
/*     */         
/* 103 */         for (int debug6 = 0; debug6 < getOrCreateIronGolemFull().getWidth(); debug6++) {
/* 104 */           for (int debug7 = 0; debug7 < getOrCreateIronGolemFull().getHeight(); debug7++) {
/* 105 */             BlockInWorld debug8 = debug3.getBlock(debug6, debug7, 0);
/* 106 */             debug1.blockUpdated(debug8.getPos(), Blocks.AIR);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext debug1) {
/* 115 */     return (BlockState)defaultBlockState().setValue((Property)FACING, (Comparable)debug1.getHorizontalDirection().getOpposite());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> debug1) {
/* 120 */     debug1.add(new Property[] { (Property)FACING });
/*     */   }
/*     */   static {
/* 123 */     PUMPKINS_PREDICATE = (debug0 -> (debug0 != null && (debug0.is(Blocks.CARVED_PUMPKIN) || debug0.is(Blocks.JACK_O_LANTERN))));
/*     */   }
/*     */   private BlockPattern getOrCreateSnowGolemBase() {
/* 126 */     if (this.snowGolemBase == null) {
/* 127 */       this
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 134 */         .snowGolemBase = BlockPatternBuilder.start().aisle(new String[] { " ", "#", "#" }).where('#', BlockInWorld.hasState((Predicate)BlockStatePredicate.forBlock(Blocks.SNOW_BLOCK))).build();
/*     */     }
/*     */     
/* 137 */     return this.snowGolemBase;
/*     */   }
/*     */   
/*     */   private BlockPattern getOrCreateSnowGolemFull() {
/* 141 */     if (this.snowGolemFull == null) {
/* 142 */       this
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 150 */         .snowGolemFull = BlockPatternBuilder.start().aisle(new String[] { "^", "#", "#" }).where('^', BlockInWorld.hasState(PUMPKINS_PREDICATE)).where('#', BlockInWorld.hasState((Predicate)BlockStatePredicate.forBlock(Blocks.SNOW_BLOCK))).build();
/*     */     }
/*     */     
/* 153 */     return this.snowGolemFull;
/*     */   }
/*     */   
/*     */   private BlockPattern getOrCreateIronGolemBase() {
/* 157 */     if (this.ironGolemBase == null) {
/* 158 */       this
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 166 */         .ironGolemBase = BlockPatternBuilder.start().aisle(new String[] { "~ ~", "###", "~#~" }).where('#', BlockInWorld.hasState((Predicate)BlockStatePredicate.forBlock(Blocks.IRON_BLOCK))).where('~', BlockInWorld.hasState((Predicate)BlockMaterialPredicate.forMaterial(Material.AIR))).build();
/*     */     }
/*     */     
/* 169 */     return this.ironGolemBase;
/*     */   }
/*     */   
/*     */   private BlockPattern getOrCreateIronGolemFull() {
/* 173 */     if (this.ironGolemFull == null) {
/* 174 */       this
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 183 */         .ironGolemFull = BlockPatternBuilder.start().aisle(new String[] { "~^~", "###", "~#~" }).where('^', BlockInWorld.hasState(PUMPKINS_PREDICATE)).where('#', BlockInWorld.hasState((Predicate)BlockStatePredicate.forBlock(Blocks.IRON_BLOCK))).where('~', BlockInWorld.hasState((Predicate)BlockMaterialPredicate.forMaterial(Material.AIR))).build();
/*     */     }
/*     */     
/* 186 */     return this.ironGolemFull;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\CarvedPumpkinBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */