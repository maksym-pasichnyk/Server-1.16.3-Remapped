/*     */ package net.minecraft.world.item;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.BlockSource;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.tags.Tag;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.vehicle.AbstractMinecart;
/*     */ import net.minecraft.world.item.context.UseOnContext;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.BaseRailBlock;
/*     */ import net.minecraft.world.level.block.DispenserBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.RailShape;
/*     */ 
/*     */ public class MinecartItem extends Item {
/*  20 */   private static final DispenseItemBehavior DISPENSE_ITEM_BEHAVIOR = (DispenseItemBehavior)new DefaultDispenseItemBehavior() {
/*  21 */       private final DefaultDispenseItemBehavior defaultDispenseItemBehavior = new DefaultDispenseItemBehavior();
/*     */       
/*     */       public ItemStack execute(BlockSource debug1, ItemStack debug2) {
/*     */         double debug14;
/*  25 */         Direction debug3 = (Direction)debug1.getBlockState().getValue((Property)DispenserBlock.FACING);
/*  26 */         ServerLevel serverLevel = debug1.getLevel();
/*     */ 
/*     */ 
/*     */         
/*  30 */         double debug5 = debug1.x() + debug3.getStepX() * 1.125D;
/*  31 */         double debug7 = Math.floor(debug1.y()) + debug3.getStepY();
/*  32 */         double debug9 = debug1.z() + debug3.getStepZ() * 1.125D;
/*     */         
/*  34 */         BlockPos debug11 = debug1.getPos().relative(debug3);
/*  35 */         BlockState debug12 = serverLevel.getBlockState(debug11);
/*  36 */         RailShape debug13 = (debug12.getBlock() instanceof BaseRailBlock) ? (RailShape)debug12.getValue(((BaseRailBlock)debug12.getBlock()).getShapeProperty()) : RailShape.NORTH_SOUTH;
/*     */ 
/*     */         
/*  39 */         if (debug12.is((Tag)BlockTags.RAILS)) {
/*  40 */           if (debug13.isAscending()) {
/*  41 */             debug14 = 0.6D;
/*     */           } else {
/*  43 */             debug14 = 0.1D;
/*     */           } 
/*  45 */         } else if (debug12.isAir() && serverLevel.getBlockState(debug11.below()).is((Tag)BlockTags.RAILS)) {
/*  46 */           BlockState blockState = serverLevel.getBlockState(debug11.below());
/*  47 */           RailShape debug17 = (blockState.getBlock() instanceof BaseRailBlock) ? (RailShape)blockState.getValue(((BaseRailBlock)blockState.getBlock()).getShapeProperty()) : RailShape.NORTH_SOUTH;
/*  48 */           if (debug3 == Direction.DOWN || !debug17.isAscending()) {
/*  49 */             debug14 = -0.9D;
/*     */           } else {
/*  51 */             debug14 = -0.4D;
/*     */           } 
/*     */         } else {
/*  54 */           return this.defaultDispenseItemBehavior.dispense(debug1, debug2);
/*     */         } 
/*     */         
/*  57 */         AbstractMinecart debug16 = AbstractMinecart.createMinecart((Level)serverLevel, debug5, debug7 + debug14, debug9, ((MinecartItem)debug2.getItem()).type);
/*  58 */         if (debug2.hasCustomHoverName()) {
/*  59 */           debug16.setCustomName(debug2.getHoverName());
/*     */         }
/*  61 */         serverLevel.addFreshEntity((Entity)debug16);
/*     */         
/*  63 */         debug2.shrink(1);
/*  64 */         return debug2;
/*     */       }
/*     */ 
/*     */       
/*     */       protected void playSound(BlockSource debug1) {
/*  69 */         debug1.getLevel().levelEvent(1000, debug1.getPos(), 0);
/*     */       }
/*     */     };
/*     */   
/*     */   private final AbstractMinecart.Type type;
/*     */   
/*     */   public MinecartItem(AbstractMinecart.Type debug1, Item.Properties debug2) {
/*  76 */     super(debug2);
/*  77 */     this.type = debug1;
/*  78 */     DispenserBlock.registerBehavior(this, DISPENSE_ITEM_BEHAVIOR);
/*     */   }
/*     */ 
/*     */   
/*     */   public InteractionResult useOn(UseOnContext debug1) {
/*  83 */     Level debug2 = debug1.getLevel();
/*  84 */     BlockPos debug3 = debug1.getClickedPos();
/*     */     
/*  86 */     BlockState debug4 = debug2.getBlockState(debug3);
/*  87 */     if (!debug4.is((Tag)BlockTags.RAILS)) {
/*  88 */       return InteractionResult.FAIL;
/*     */     }
/*     */     
/*  91 */     ItemStack debug5 = debug1.getItemInHand();
/*  92 */     if (!debug2.isClientSide) {
/*  93 */       RailShape debug6 = (debug4.getBlock() instanceof BaseRailBlock) ? (RailShape)debug4.getValue(((BaseRailBlock)debug4.getBlock()).getShapeProperty()) : RailShape.NORTH_SOUTH;
/*  94 */       double debug7 = 0.0D;
/*  95 */       if (debug6.isAscending()) {
/*  96 */         debug7 = 0.5D;
/*     */       }
/*  98 */       AbstractMinecart debug9 = AbstractMinecart.createMinecart(debug2, debug3.getX() + 0.5D, debug3.getY() + 0.0625D + debug7, debug3.getZ() + 0.5D, this.type);
/*  99 */       if (debug5.hasCustomHoverName()) {
/* 100 */         debug9.setCustomName(debug5.getHoverName());
/*     */       }
/* 102 */       debug2.addFreshEntity((Entity)debug9);
/*     */     } 
/* 104 */     debug5.shrink(1);
/* 105 */     return InteractionResult.sidedSuccess(debug2.isClientSide);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\item\MinecartItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */