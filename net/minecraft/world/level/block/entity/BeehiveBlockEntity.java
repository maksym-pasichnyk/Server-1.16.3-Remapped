/*     */ package net.minecraft.world.level.block.entity;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.ListTag;
/*     */ import net.minecraft.nbt.NbtUtils;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.network.protocol.game.DebugPackets;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.tags.EntityTypeTags;
/*     */ import net.minecraft.tags.Tag;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.animal.Bee;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.block.BeehiveBlock;
/*     */ import net.minecraft.world.level.block.CampfireBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
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
/*     */ public class BeehiveBlockEntity
/*     */   extends BlockEntity
/*     */   implements TickableBlockEntity
/*     */ {
/*  50 */   private final List<BeeData> stored = Lists.newArrayList();
/*     */   @Nullable
/*  52 */   private BlockPos savedFlowerPos = null;
/*     */   
/*     */   public enum BeeReleaseStatus
/*     */   {
/*  56 */     HONEY_DELIVERED,
/*  57 */     BEE_RELEASED,
/*  58 */     EMERGENCY;
/*     */   }
/*     */   
/*     */   public BeehiveBlockEntity() {
/*  62 */     super(BlockEntityType.BEEHIVE);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setChanged() {
/*  67 */     if (isFireNearby())
/*     */     {
/*  69 */       emptyAllLivingFromHive((Player)null, this.level.getBlockState(getBlockPos()), BeeReleaseStatus.EMERGENCY);
/*     */     }
/*  71 */     super.setChanged();
/*     */   }
/*     */   
/*     */   public boolean isFireNearby() {
/*  75 */     if (this.level == null) {
/*  76 */       return false;
/*     */     }
/*     */     
/*  79 */     for (BlockPos debug2 : BlockPos.betweenClosed(this.worldPosition.offset(-1, -1, -1), this.worldPosition.offset(1, 1, 1))) {
/*  80 */       if (this.level.getBlockState(debug2).getBlock() instanceof net.minecraft.world.level.block.FireBlock) {
/*  81 */         return true;
/*     */       }
/*     */     } 
/*     */     
/*  85 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isEmpty() {
/*  89 */     return this.stored.isEmpty();
/*     */   }
/*     */   
/*     */   public boolean isFull() {
/*  93 */     return (this.stored.size() == 3);
/*     */   }
/*     */   
/*     */   public void emptyAllLivingFromHive(@Nullable Player debug1, BlockState debug2, BeeReleaseStatus debug3) {
/*  97 */     List<Entity> debug4 = releaseAllOccupants(debug2, debug3);
/*     */     
/*  99 */     if (debug1 != null) {
/* 100 */       for (Entity debug6 : debug4) {
/* 101 */         if (debug6 instanceof Bee) {
/* 102 */           Bee debug7 = (Bee)debug6;
/* 103 */           if (debug1.position().distanceToSqr(debug6.position()) <= 16.0D) {
/* 104 */             if (!isSedated()) {
/* 105 */               debug7.setTarget((LivingEntity)debug1); continue;
/*     */             } 
/* 107 */             debug7.setStayOutOfHiveCountdown(400);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private List<Entity> releaseAllOccupants(BlockState debug1, BeeReleaseStatus debug2) {
/* 116 */     List<Entity> debug3 = Lists.newArrayList();
/* 117 */     this.stored.removeIf(debug4 -> releaseOccupant(debug1, debug4, debug2, debug3));
/* 118 */     return debug3;
/*     */   }
/*     */   
/*     */   public void addOccupant(Entity debug1, boolean debug2) {
/* 122 */     addOccupantWithPresetTicks(debug1, debug2, 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getOccupantCount() {
/* 127 */     return this.stored.size();
/*     */   }
/*     */   
/*     */   public static int getHoneyLevel(BlockState debug0) {
/* 131 */     return ((Integer)debug0.getValue((Property)BeehiveBlock.HONEY_LEVEL)).intValue();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isSedated() {
/* 136 */     return CampfireBlock.isSmokeyPos(this.level, getBlockPos());
/*     */   }
/*     */   
/*     */   protected void sendDebugPackets() {
/* 140 */     DebugPackets.sendHiveInfo(this);
/*     */   }
/*     */   
/*     */   public void addOccupantWithPresetTicks(Entity debug1, boolean debug2, int debug3) {
/* 144 */     if (this.stored.size() >= 3) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 149 */     debug1.stopRiding();
/* 150 */     debug1.ejectPassengers();
/*     */     
/* 152 */     CompoundTag debug4 = new CompoundTag();
/* 153 */     debug1.save(debug4);
/* 154 */     this.stored.add(new BeeData(debug4, debug3, debug2 ? 2400 : 600));
/*     */     
/* 156 */     if (this.level != null) {
/* 157 */       if (debug1 instanceof Bee) {
/* 158 */         Bee bee = (Bee)debug1;
/*     */         
/* 160 */         if (bee.hasSavedFlowerPos() && (!hasSavedFlowerPos() || this.level.random.nextBoolean())) {
/* 161 */           this.savedFlowerPos = bee.getSavedFlowerPos();
/*     */         }
/*     */       } 
/*     */       
/* 165 */       BlockPos debug5 = getBlockPos();
/* 166 */       this.level.playSound(null, debug5.getX(), debug5.getY(), debug5.getZ(), SoundEvents.BEEHIVE_ENTER, SoundSource.BLOCKS, 1.0F, 1.0F);
/*     */     } 
/*     */     
/* 169 */     debug1.remove();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean releaseOccupant(BlockState debug1, BeeData debug2, @Nullable List<Entity> debug3, BeeReleaseStatus debug4) {
/* 176 */     if ((this.level.isNight() || this.level.isRaining()) && debug4 != BeeReleaseStatus.EMERGENCY) {
/* 177 */       return false;
/*     */     }
/*     */     
/* 180 */     BlockPos debug5 = getBlockPos();
/* 181 */     CompoundTag debug6 = debug2.entityData;
/*     */ 
/*     */     
/* 184 */     debug6.remove("Passengers");
/*     */     
/* 186 */     debug6.remove("Leash");
/*     */     
/* 188 */     debug6.remove("UUID");
/*     */     
/* 190 */     Direction debug7 = (Direction)debug1.getValue((Property)BeehiveBlock.FACING);
/* 191 */     BlockPos debug8 = debug5.relative(debug7);
/* 192 */     boolean debug9 = !this.level.getBlockState(debug8).getCollisionShape((BlockGetter)this.level, debug8).isEmpty();
/*     */     
/* 194 */     if (debug9 && debug4 != BeeReleaseStatus.EMERGENCY) {
/* 195 */       return false;
/*     */     }
/*     */     
/* 198 */     Entity debug10 = EntityType.loadEntityRecursive(debug6, this.level, debug0 -> debug0);
/*     */     
/* 200 */     if (debug10 != null) {
/*     */       
/* 202 */       if (!debug10.getType().is((Tag)EntityTypeTags.BEEHIVE_INHABITORS)) {
/* 203 */         return false;
/*     */       }
/*     */       
/* 206 */       if (debug10 instanceof Bee) {
/* 207 */         Bee debug11 = (Bee)debug10;
/*     */ 
/*     */ 
/*     */         
/* 211 */         if (hasSavedFlowerPos() && !debug11.hasSavedFlowerPos() && this.level.random.nextFloat() < 0.9F) {
/* 212 */           debug11.setSavedFlowerPos(this.savedFlowerPos);
/*     */         }
/*     */         
/* 215 */         if (debug4 == BeeReleaseStatus.HONEY_DELIVERED) {
/* 216 */           debug11.dropOffNectar();
/*     */           
/* 218 */           if (debug1.getBlock().is((Tag)BlockTags.BEEHIVES)) {
/* 219 */             int i = getHoneyLevel(debug1);
/* 220 */             if (i < 5) {
/* 221 */               int j = (this.level.random.nextInt(100) == 0) ? 2 : 1;
/* 222 */               if (i + j > 5) {
/* 223 */                 j--;
/*     */               }
/* 225 */               this.level.setBlockAndUpdate(getBlockPos(), (BlockState)debug1.setValue((Property)BeehiveBlock.HONEY_LEVEL, Integer.valueOf(i + j)));
/*     */             } 
/*     */           } 
/*     */         } 
/*     */         
/* 230 */         setBeeReleaseData(debug2.ticksInHive, debug11);
/*     */         
/* 232 */         if (debug3 != null) {
/* 233 */           debug3.add(debug11);
/*     */         }
/*     */         
/* 236 */         float debug12 = debug10.getBbWidth();
/* 237 */         double debug13 = debug9 ? 0.0D : (0.55D + (debug12 / 2.0F));
/* 238 */         double debug15 = debug5.getX() + 0.5D + debug13 * debug7.getStepX();
/* 239 */         double debug17 = debug5.getY() + 0.5D - (debug10.getBbHeight() / 2.0F);
/* 240 */         double debug19 = debug5.getZ() + 0.5D + debug13 * debug7.getStepZ();
/* 241 */         debug10.moveTo(debug15, debug17, debug19, debug10.yRot, debug10.xRot);
/*     */       } 
/*     */       
/* 244 */       this.level.playSound(null, debug5, SoundEvents.BEEHIVE_EXIT, SoundSource.BLOCKS, 1.0F, 1.0F);
/*     */       
/* 246 */       return this.level.addFreshEntity(debug10);
/*     */     } 
/*     */     
/* 249 */     return false;
/*     */   }
/*     */   
/*     */   private void setBeeReleaseData(int debug1, Bee debug2) {
/* 253 */     int debug3 = debug2.getAge();
/* 254 */     if (debug3 < 0) {
/* 255 */       debug2.setAge(Math.min(0, debug3 + debug1));
/* 256 */     } else if (debug3 > 0) {
/* 257 */       debug2.setAge(Math.max(0, debug3 - debug1));
/*     */     } 
/* 259 */     debug2.setInLoveTime(Math.max(0, debug2.getInLoveTime() - debug1));
/* 260 */     debug2.resetTicksWithoutNectarSinceExitingHive();
/*     */   }
/*     */   
/*     */   private boolean hasSavedFlowerPos() {
/* 264 */     return (this.savedFlowerPos != null);
/*     */   }
/*     */   
/*     */   private void tickOccupants() {
/* 268 */     Iterator<BeeData> debug1 = this.stored.iterator();
/* 269 */     BlockState debug2 = getBlockState();
/* 270 */     while (debug1.hasNext()) {
/* 271 */       BeeData debug3 = debug1.next();
/* 272 */       if (debug3.ticksInHive > debug3.minOccupationTicks) {
/*     */         
/* 274 */         BeeReleaseStatus debug4 = debug3.entityData.getBoolean("HasNectar") ? BeeReleaseStatus.HONEY_DELIVERED : BeeReleaseStatus.BEE_RELEASED;
/* 275 */         if (releaseOccupant(debug2, debug3, (List<Entity>)null, debug4)) {
/* 276 */           debug1.remove();
/*     */         }
/*     */       } 
/* 279 */       debug3.ticksInHive++;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/* 285 */     if (this.level.isClientSide) {
/*     */       return;
/*     */     }
/*     */     
/* 289 */     tickOccupants();
/*     */     
/* 291 */     BlockPos debug1 = getBlockPos();
/*     */     
/* 293 */     if (this.stored.size() > 0 && this.level.getRandom().nextDouble() < 0.005D) {
/* 294 */       double debug2 = debug1.getX() + 0.5D;
/* 295 */       double debug4 = debug1.getY();
/* 296 */       double debug6 = debug1.getZ() + 0.5D;
/* 297 */       this.level.playSound(null, debug2, debug4, debug6, SoundEvents.BEEHIVE_WORK, SoundSource.BLOCKS, 1.0F, 1.0F);
/*     */     } 
/*     */     
/* 300 */     sendDebugPackets();
/*     */   }
/*     */ 
/*     */   
/*     */   public void load(BlockState debug1, CompoundTag debug2) {
/* 305 */     super.load(debug1, debug2);
/*     */     
/* 307 */     this.stored.clear();
/*     */     
/* 309 */     ListTag debug3 = debug2.getList("Bees", 10);
/* 310 */     for (int debug4 = 0; debug4 < debug3.size(); debug4++) {
/* 311 */       CompoundTag debug5 = debug3.getCompound(debug4);
/* 312 */       BeeData debug6 = new BeeData(debug5.getCompound("EntityData"), debug5.getInt("TicksInHive"), debug5.getInt("MinOccupationTicks"));
/* 313 */       this.stored.add(debug6);
/*     */     } 
/*     */     
/* 316 */     this.savedFlowerPos = null;
/* 317 */     if (debug2.contains("FlowerPos")) {
/* 318 */       this.savedFlowerPos = NbtUtils.readBlockPos(debug2.getCompound("FlowerPos"));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public CompoundTag save(CompoundTag debug1) {
/* 324 */     super.save(debug1);
/*     */     
/* 326 */     debug1.put("Bees", (Tag)writeBees());
/* 327 */     if (hasSavedFlowerPos()) {
/* 328 */       debug1.put("FlowerPos", (Tag)NbtUtils.writeBlockPos(this.savedFlowerPos));
/*     */     }
/*     */     
/* 331 */     return debug1;
/*     */   }
/*     */   
/*     */   public ListTag writeBees() {
/* 335 */     ListTag debug1 = new ListTag();
/* 336 */     for (BeeData debug3 : this.stored) {
/* 337 */       debug3.entityData.remove("UUID");
/* 338 */       CompoundTag debug4 = new CompoundTag();
/* 339 */       debug4.put("EntityData", (Tag)debug3.entityData);
/* 340 */       debug4.putInt("TicksInHive", debug3.ticksInHive);
/* 341 */       debug4.putInt("MinOccupationTicks", debug3.minOccupationTicks);
/* 342 */       debug1.add(debug4);
/*     */     } 
/* 344 */     return debug1;
/*     */   }
/*     */   
/*     */   static class BeeData {
/*     */     private final CompoundTag entityData;
/*     */     private int ticksInHive;
/*     */     private final int minOccupationTicks;
/*     */     
/*     */     private BeeData(CompoundTag debug1, int debug2, int debug3) {
/* 353 */       debug1.remove("UUID");
/* 354 */       this.entityData = debug1;
/* 355 */       this.ticksInHive = debug2;
/* 356 */       this.minOccupationTicks = debug3;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\entity\BeehiveBlockEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */