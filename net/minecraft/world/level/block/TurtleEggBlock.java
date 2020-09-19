/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import java.util.Random;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.tags.Tag;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.animal.Turtle;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.GameRules;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.IntegerProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TurtleEggBlock
/*     */   extends Block
/*     */ {
/*  36 */   private static final VoxelShape ONE_EGG_AABB = Block.box(3.0D, 0.0D, 3.0D, 12.0D, 7.0D, 12.0D);
/*  37 */   private static final VoxelShape MULTIPLE_EGGS_AABB = Block.box(1.0D, 0.0D, 1.0D, 15.0D, 7.0D, 15.0D);
/*     */   
/*  39 */   public static final IntegerProperty HATCH = BlockStateProperties.HATCH;
/*  40 */   public static final IntegerProperty EGGS = BlockStateProperties.EGGS;
/*     */   
/*     */   public TurtleEggBlock(BlockBehaviour.Properties debug1) {
/*  43 */     super(debug1);
/*  44 */     registerDefaultState((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)HATCH, Integer.valueOf(0))).setValue((Property)EGGS, Integer.valueOf(1)));
/*     */   }
/*     */ 
/*     */   
/*     */   public void stepOn(Level debug1, BlockPos debug2, Entity debug3) {
/*  49 */     destroyEgg(debug1, debug2, debug3, 100);
/*  50 */     super.stepOn(debug1, debug2, debug3);
/*     */   }
/*     */ 
/*     */   
/*     */   public void fallOn(Level debug1, BlockPos debug2, Entity debug3, float debug4) {
/*  55 */     if (!(debug3 instanceof net.minecraft.world.entity.monster.Zombie)) {
/*  56 */       destroyEgg(debug1, debug2, debug3, 3);
/*     */     }
/*     */     
/*  59 */     super.fallOn(debug1, debug2, debug3, debug4);
/*     */   }
/*     */   
/*     */   private void destroyEgg(Level debug1, BlockPos debug2, Entity debug3, int debug4) {
/*  63 */     if (!canDestroyEgg(debug1, debug3)) {
/*     */       return;
/*     */     }
/*     */     
/*  67 */     if (!debug1.isClientSide && debug1.random.nextInt(debug4) == 0) {
/*  68 */       BlockState debug5 = debug1.getBlockState(debug2);
/*  69 */       if (debug5.is(Blocks.TURTLE_EGG)) {
/*  70 */         decreaseEggs(debug1, debug2, debug5);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private void decreaseEggs(Level debug1, BlockPos debug2, BlockState debug3) {
/*  76 */     debug1.playSound(null, debug2, SoundEvents.TURTLE_EGG_BREAK, SoundSource.BLOCKS, 0.7F, 0.9F + debug1.random.nextFloat() * 0.2F);
/*  77 */     int debug4 = ((Integer)debug3.getValue((Property)EGGS)).intValue();
/*  78 */     if (debug4 <= 1) {
/*     */       
/*  80 */       debug1.destroyBlock(debug2, false);
/*     */     } else {
/*     */       
/*  83 */       debug1.setBlock(debug2, (BlockState)debug3.setValue((Property)EGGS, Integer.valueOf(debug4 - 1)), 2);
/*  84 */       debug1.levelEvent(2001, debug2, Block.getId(debug3));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void randomTick(BlockState debug1, ServerLevel debug2, BlockPos debug3, Random debug4) {
/*  90 */     if (shouldUpdateHatchLevel((Level)debug2) && 
/*  91 */       onSand((BlockGetter)debug2, debug3)) {
/*  92 */       int debug5 = ((Integer)debug1.getValue((Property)HATCH)).intValue();
/*  93 */       if (debug5 < 2) {
/*  94 */         debug2.playSound(null, debug3, SoundEvents.TURTLE_EGG_CRACK, SoundSource.BLOCKS, 0.7F, 0.9F + debug4.nextFloat() * 0.2F);
/*  95 */         debug2.setBlock(debug3, (BlockState)debug1.setValue((Property)HATCH, Integer.valueOf(debug5 + 1)), 2);
/*     */       } else {
/*     */         
/*  98 */         debug2.playSound(null, debug3, SoundEvents.TURTLE_EGG_HATCH, SoundSource.BLOCKS, 0.7F, 0.9F + debug4.nextFloat() * 0.2F);
/*  99 */         debug2.removeBlock(debug3, false);
/*     */         
/* 101 */         for (int debug6 = 0; debug6 < ((Integer)debug1.getValue((Property)EGGS)).intValue(); debug6++) {
/* 102 */           debug2.levelEvent(2001, debug3, Block.getId(debug1));
/* 103 */           Turtle debug7 = (Turtle)EntityType.TURTLE.create((Level)debug2);
/* 104 */           debug7.setAge(-24000);
/* 105 */           debug7.setHomePos(debug3);
/* 106 */           debug7.moveTo(debug3.getX() + 0.3D + debug6 * 0.2D, debug3.getY(), debug3.getZ() + 0.3D, 0.0F, 0.0F);
/* 107 */           debug2.addFreshEntity((Entity)debug7);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean onSand(BlockGetter debug0, BlockPos debug1) {
/* 115 */     return isSand(debug0, debug1.below());
/*     */   }
/*     */   
/*     */   public static boolean isSand(BlockGetter debug0, BlockPos debug1) {
/* 119 */     return debug0.getBlockState(debug1).is((Tag)BlockTags.SAND);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onPlace(BlockState debug1, Level debug2, BlockPos debug3, BlockState debug4, boolean debug5) {
/* 124 */     if (onSand((BlockGetter)debug2, debug3) && !debug2.isClientSide) {
/* 125 */       debug2.levelEvent(2005, debug3, 0);
/*     */     }
/*     */   }
/*     */   
/*     */   private boolean shouldUpdateHatchLevel(Level debug1) {
/* 130 */     float debug2 = debug1.getTimeOfDay(1.0F);
/*     */     
/* 132 */     if (debug2 < 0.69D && debug2 > 0.65D) {
/* 133 */       return true;
/*     */     }
/* 135 */     return (debug1.random.nextInt(500) == 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public void playerDestroy(Level debug1, Player debug2, BlockPos debug3, BlockState debug4, @Nullable BlockEntity debug5, ItemStack debug6) {
/* 140 */     super.playerDestroy(debug1, debug2, debug3, debug4, debug5, debug6);
/*     */     
/* 142 */     decreaseEggs(debug1, debug3, debug4);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canBeReplaced(BlockState debug1, BlockPlaceContext debug2) {
/* 147 */     if (debug2.getItemInHand().getItem() == asItem() && ((Integer)debug1.getValue((Property)EGGS)).intValue() < 4) {
/* 148 */       return true;
/*     */     }
/* 150 */     return super.canBeReplaced(debug1, debug2);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public BlockState getStateForPlacement(BlockPlaceContext debug1) {
/* 156 */     BlockState debug2 = debug1.getLevel().getBlockState(debug1.getClickedPos());
/* 157 */     if (debug2.is(this)) {
/* 158 */       return (BlockState)debug2.setValue((Property)EGGS, Integer.valueOf(Math.min(4, ((Integer)debug2.getValue((Property)EGGS)).intValue() + 1)));
/*     */     }
/*     */     
/* 161 */     return super.getStateForPlacement(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public VoxelShape getShape(BlockState debug1, BlockGetter debug2, BlockPos debug3, CollisionContext debug4) {
/* 166 */     if (((Integer)debug1.getValue((Property)EGGS)).intValue() > 1) {
/* 167 */       return MULTIPLE_EGGS_AABB;
/*     */     }
/*     */     
/* 170 */     return ONE_EGG_AABB;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> debug1) {
/* 175 */     debug1.add(new Property[] { (Property)HATCH, (Property)EGGS });
/*     */   }
/*     */   
/*     */   private boolean canDestroyEgg(Level debug1, Entity debug2) {
/* 179 */     if (debug2 instanceof Turtle || debug2 instanceof net.minecraft.world.entity.ambient.Bat) {
/* 180 */       return false;
/*     */     }
/*     */     
/* 183 */     if (debug2 instanceof net.minecraft.world.entity.LivingEntity) {
/* 184 */       return (debug2 instanceof Player || debug1.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING));
/*     */     }
/*     */     
/* 187 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\TurtleEggBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */