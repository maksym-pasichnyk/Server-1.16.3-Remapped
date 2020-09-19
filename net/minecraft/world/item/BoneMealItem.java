/*     */ package net.minecraft.world.item;
/*     */ 
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.tags.Tag;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.item.context.UseOnContext;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ import net.minecraft.world.level.biome.Biomes;
/*     */ import net.minecraft.world.level.block.BaseCoralWallFanBlock;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.BonemealableBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BoneMealItem
/*     */   extends Item
/*     */ {
/*     */   public BoneMealItem(Item.Properties debug1) {
/*  33 */     super(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public InteractionResult useOn(UseOnContext debug1) {
/*  38 */     Level debug2 = debug1.getLevel();
/*  39 */     BlockPos debug3 = debug1.getClickedPos();
/*  40 */     BlockPos debug4 = debug3.relative(debug1.getClickedFace());
/*     */ 
/*     */     
/*  43 */     if (growCrop(debug1.getItemInHand(), debug2, debug3)) {
/*  44 */       if (!debug2.isClientSide) {
/*  45 */         debug2.levelEvent(2005, debug3, 0);
/*     */       }
/*  47 */       return InteractionResult.sidedSuccess(debug2.isClientSide);
/*     */     } 
/*     */ 
/*     */     
/*  51 */     BlockState debug5 = debug2.getBlockState(debug3);
/*  52 */     boolean debug6 = debug5.isFaceSturdy((BlockGetter)debug2, debug3, debug1.getClickedFace());
/*  53 */     if (debug6 && 
/*  54 */       growWaterPlant(debug1.getItemInHand(), debug2, debug4, debug1.getClickedFace())) {
/*  55 */       if (!debug2.isClientSide) {
/*  56 */         debug2.levelEvent(2005, debug4, 0);
/*     */       }
/*  58 */       return InteractionResult.sidedSuccess(debug2.isClientSide);
/*     */     } 
/*     */ 
/*     */     
/*  62 */     return InteractionResult.PASS;
/*     */   }
/*     */   
/*     */   public static boolean growCrop(ItemStack debug0, Level debug1, BlockPos debug2) {
/*  66 */     BlockState debug3 = debug1.getBlockState(debug2);
/*     */     
/*  68 */     if (debug3.getBlock() instanceof BonemealableBlock) {
/*  69 */       BonemealableBlock debug4 = (BonemealableBlock)debug3.getBlock();
/*     */       
/*  71 */       if (debug4.isValidBonemealTarget((BlockGetter)debug1, debug2, debug3, debug1.isClientSide)) {
/*  72 */         if (debug1 instanceof ServerLevel) {
/*  73 */           if (debug4.isBonemealSuccess(debug1, debug1.random, debug2, debug3)) {
/*  74 */             debug4.performBonemeal((ServerLevel)debug1, debug1.random, debug2, debug3);
/*     */           }
/*  76 */           debug0.shrink(1);
/*     */         } 
/*  78 */         return true;
/*     */       } 
/*     */     } 
/*  81 */     return false;
/*     */   }
/*     */   
/*     */   public static boolean growWaterPlant(ItemStack debug0, Level debug1, BlockPos debug2, @Nullable Direction debug3) {
/*  85 */     if (!debug1.getBlockState(debug2).is(Blocks.WATER) || debug1.getFluidState(debug2).getAmount() != 8) {
/*  86 */       return false;
/*     */     }
/*     */     
/*  89 */     if (!(debug1 instanceof ServerLevel)) {
/*  90 */       return true;
/*     */     }
/*     */     
/*     */     int debug4;
/*  94 */     label47: for (debug4 = 0; debug4 < 128; debug4++) {
/*  95 */       BlockPos debug5 = debug2;
/*  96 */       BlockState debug6 = Blocks.SEAGRASS.defaultBlockState();
/*     */       
/*  98 */       for (int i = 0; i < debug4 / 16; i++) {
/*  99 */         debug5 = debug5.offset(random.nextInt(3) - 1, (random.nextInt(3) - 1) * random.nextInt(3) / 2, random.nextInt(3) - 1);
/*     */         
/* 101 */         if (debug1.getBlockState(debug5).isCollisionShapeFullBlock((BlockGetter)debug1, debug5)) {
/*     */           continue label47;
/*     */         }
/*     */       } 
/*     */ 
/*     */       
/* 107 */       Optional<ResourceKey<Biome>> debug7 = debug1.getBiomeName(debug5);
/* 108 */       if (Objects.equals(debug7, Optional.of(Biomes.WARM_OCEAN)) || Objects.equals(debug7, Optional.of(Biomes.DEEP_WARM_OCEAN))) {
/* 109 */         if (debug4 == 0 && debug3 != null && debug3.getAxis().isHorizontal()) {
/*     */           
/* 111 */           debug6 = (BlockState)((Block)BlockTags.WALL_CORALS.getRandomElement(debug1.random)).defaultBlockState().setValue((Property)BaseCoralWallFanBlock.FACING, (Comparable)debug3);
/* 112 */         } else if (random.nextInt(4) == 0) {
/* 113 */           debug6 = ((Block)BlockTags.UNDERWATER_BONEMEALS.getRandomElement(random)).defaultBlockState();
/*     */         } 
/*     */       }
/*     */       
/* 117 */       if (debug6.getBlock().is((Tag)BlockTags.WALL_CORALS)) {
/* 118 */         int debug8 = 0;
/* 119 */         while (!debug6.canSurvive((LevelReader)debug1, debug5) && debug8 < 4) {
/* 120 */           debug6 = (BlockState)debug6.setValue((Property)BaseCoralWallFanBlock.FACING, (Comparable)Direction.Plane.HORIZONTAL.getRandomDirection(random));
/* 121 */           debug8++;
/*     */         } 
/*     */       } 
/*     */       
/* 125 */       if (debug6.canSurvive((LevelReader)debug1, debug5)) {
/*     */ 
/*     */ 
/*     */         
/* 129 */         BlockState debug8 = debug1.getBlockState(debug5);
/* 130 */         if (debug8.is(Blocks.WATER) && debug1.getFluidState(debug5).getAmount() == 8) {
/* 131 */           debug1.setBlock(debug5, debug6, 3);
/*     */         
/*     */         }
/* 134 */         else if (debug8.is(Blocks.SEAGRASS) && random.nextInt(10) == 0) {
/* 135 */           ((BonemealableBlock)Blocks.SEAGRASS).performBonemeal((ServerLevel)debug1, random, debug5, debug8);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 140 */     debug0.shrink(1);
/* 141 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\item\BoneMealItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */