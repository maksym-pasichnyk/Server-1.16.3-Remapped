/*     */ package net.minecraft.world.level.block.entity;
/*     */ 
/*     */ import java.util.Optional;
/*     */ import java.util.Random;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.NonNullList;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.Clearable;
/*     */ import net.minecraft.world.Container;
/*     */ import net.minecraft.world.ContainerHelper;
/*     */ import net.minecraft.world.Containers;
/*     */ import net.minecraft.world.SimpleContainer;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.crafting.CampfireCookingRecipe;
/*     */ import net.minecraft.world.item.crafting.RecipeType;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.CampfireBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ 
/*     */ 
/*     */ public class CampfireBlockEntity
/*     */   extends BlockEntity
/*     */   implements Clearable, TickableBlockEntity
/*     */ {
/*  32 */   private final NonNullList<ItemStack> items = NonNullList.withSize(4, ItemStack.EMPTY);
/*  33 */   private final int[] cookingProgress = new int[4];
/*  34 */   private final int[] cookingTime = new int[4];
/*     */   
/*     */   public CampfireBlockEntity() {
/*  37 */     super(BlockEntityType.CAMPFIRE);
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/*  42 */     boolean debug1 = ((Boolean)getBlockState().getValue((Property)CampfireBlock.LIT)).booleanValue();
/*  43 */     boolean debug2 = this.level.isClientSide;
/*     */     
/*  45 */     if (debug2) {
/*  46 */       if (debug1) {
/*  47 */         makeParticles();
/*     */       }
/*     */       
/*     */       return;
/*     */     } 
/*  52 */     if (debug1) {
/*  53 */       cook();
/*     */     } else {
/*  55 */       for (int debug3 = 0; debug3 < this.items.size(); debug3++) {
/*  56 */         if (this.cookingProgress[debug3] > 0) {
/*  57 */           this.cookingProgress[debug3] = Mth.clamp(this.cookingProgress[debug3] - 2, 0, this.cookingTime[debug3]);
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void cook() {
/*  64 */     for (int debug1 = 0; debug1 < this.items.size(); debug1++) {
/*  65 */       ItemStack debug2 = (ItemStack)this.items.get(debug1);
/*  66 */       if (!debug2.isEmpty()) {
/*     */ 
/*     */ 
/*     */         
/*  70 */         this.cookingProgress[debug1] = this.cookingProgress[debug1] + 1;
/*  71 */         if (this.cookingProgress[debug1] >= this.cookingTime[debug1]) {
/*  72 */           SimpleContainer simpleContainer = new SimpleContainer(new ItemStack[] { debug2 });
/*  73 */           ItemStack debug4 = this.level.getRecipeManager().getRecipeFor(RecipeType.CAMPFIRE_COOKING, (Container)simpleContainer, this.level).map(debug1 -> debug1.assemble(debug0)).orElse(debug2);
/*  74 */           BlockPos debug5 = getBlockPos();
/*  75 */           Containers.dropItemStack(this.level, debug5.getX(), debug5.getY(), debug5.getZ(), debug4);
/*  76 */           this.items.set(debug1, ItemStack.EMPTY);
/*  77 */           markUpdated();
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   private void makeParticles() {
/*  83 */     Level debug1 = getLevel();
/*  84 */     if (debug1 == null) {
/*     */       return;
/*     */     }
/*     */     
/*  88 */     BlockPos debug2 = getBlockPos();
/*  89 */     Random debug3 = debug1.random;
/*     */     
/*  91 */     if (debug3.nextFloat() < 0.11F) {
/*  92 */       for (int i = 0; i < debug3.nextInt(2) + 2; i++) {
/*  93 */         CampfireBlock.makeParticles(debug1, debug2, ((Boolean)getBlockState().getValue((Property)CampfireBlock.SIGNAL_FIRE)).booleanValue(), false);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*  98 */     int debug4 = ((Direction)getBlockState().getValue((Property)CampfireBlock.FACING)).get2DDataValue();
/*  99 */     for (int debug5 = 0; debug5 < this.items.size(); debug5++) {
/* 100 */       if (!((ItemStack)this.items.get(debug5)).isEmpty() && debug3.nextFloat() < 0.2F) {
/* 101 */         Direction debug6 = Direction.from2DDataValue(Math.floorMod(debug5 + debug4, 4));
/* 102 */         float debug7 = 0.3125F;
/*     */         
/* 104 */         double debug8 = debug2.getX() + 0.5D - (debug6.getStepX() * 0.3125F) + (debug6.getClockWise().getStepX() * 0.3125F);
/* 105 */         double debug10 = debug2.getY() + 0.5D;
/* 106 */         double debug12 = debug2.getZ() + 0.5D - (debug6.getStepZ() * 0.3125F) + (debug6.getClockWise().getStepZ() * 0.3125F);
/*     */         
/* 108 */         for (int debug14 = 0; debug14 < 4; debug14++) {
/* 109 */           debug1.addParticle((ParticleOptions)ParticleTypes.SMOKE, debug8, debug10, debug12, 0.0D, 5.0E-4D, 0.0D);
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public NonNullList<ItemStack> getItems() {
/* 116 */     return this.items;
/*     */   }
/*     */ 
/*     */   
/*     */   public void load(BlockState debug1, CompoundTag debug2) {
/* 121 */     super.load(debug1, debug2);
/*     */     
/* 123 */     this.items.clear();
/* 124 */     ContainerHelper.loadAllItems(debug2, this.items);
/*     */     
/* 126 */     if (debug2.contains("CookingTimes", 11)) {
/* 127 */       int[] debug3 = debug2.getIntArray("CookingTimes");
/* 128 */       System.arraycopy(debug3, 0, this.cookingProgress, 0, Math.min(this.cookingTime.length, debug3.length));
/*     */     } 
/*     */     
/* 131 */     if (debug2.contains("CookingTotalTimes", 11)) {
/* 132 */       int[] debug3 = debug2.getIntArray("CookingTotalTimes");
/* 133 */       System.arraycopy(debug3, 0, this.cookingTime, 0, Math.min(this.cookingTime.length, debug3.length));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public CompoundTag save(CompoundTag debug1) {
/* 139 */     saveMetadataAndItems(debug1);
/*     */     
/* 141 */     debug1.putIntArray("CookingTimes", this.cookingProgress);
/* 142 */     debug1.putIntArray("CookingTotalTimes", this.cookingTime);
/*     */     
/* 144 */     return debug1;
/*     */   }
/*     */   
/*     */   private CompoundTag saveMetadataAndItems(CompoundTag debug1) {
/* 148 */     super.save(debug1);
/*     */     
/* 150 */     ContainerHelper.saveAllItems(debug1, this.items, true);
/*     */     
/* 152 */     return debug1;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public ClientboundBlockEntityDataPacket getUpdatePacket() {
/* 158 */     return new ClientboundBlockEntityDataPacket(this.worldPosition, 13, getUpdateTag());
/*     */   }
/*     */ 
/*     */   
/*     */   public CompoundTag getUpdateTag() {
/* 163 */     return saveMetadataAndItems(new CompoundTag());
/*     */   }
/*     */   
/*     */   public Optional<CampfireCookingRecipe> getCookableRecipe(ItemStack debug1) {
/* 167 */     if (this.items.stream().noneMatch(ItemStack::isEmpty)) {
/* 168 */       return Optional.empty();
/*     */     }
/*     */     
/* 171 */     return this.level.getRecipeManager().getRecipeFor(RecipeType.CAMPFIRE_COOKING, (Container)new SimpleContainer(new ItemStack[] { debug1 }, ), this.level);
/*     */   }
/*     */   
/*     */   public boolean placeFood(ItemStack debug1, int debug2) {
/* 175 */     for (int debug3 = 0; debug3 < this.items.size(); debug3++) {
/* 176 */       ItemStack debug4 = (ItemStack)this.items.get(debug3);
/* 177 */       if (debug4.isEmpty()) {
/* 178 */         this.cookingTime[debug3] = debug2;
/* 179 */         this.cookingProgress[debug3] = 0;
/*     */         
/* 181 */         this.items.set(debug3, debug1.split(1));
/*     */         
/* 183 */         markUpdated();
/*     */         
/* 185 */         return true;
/*     */       } 
/*     */     } 
/* 188 */     return false;
/*     */   }
/*     */   
/*     */   private void markUpdated() {
/* 192 */     setChanged();
/* 193 */     getLevel().sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
/*     */   }
/*     */ 
/*     */   
/*     */   public void clearContent() {
/* 198 */     this.items.clear();
/*     */   }
/*     */ 
/*     */   
/*     */   public void dowse() {
/* 203 */     if (this.level != null) {
/* 204 */       if (!this.level.isClientSide) {
/* 205 */         Containers.dropContents(this.level, getBlockPos(), getItems());
/*     */       }
/*     */       
/* 208 */       markUpdated();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\entity\CampfireBlockEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */