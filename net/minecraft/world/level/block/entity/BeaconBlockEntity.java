/*     */ package net.minecraft.world.level.block.entity;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.stream.Collectors;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.advancements.CriteriaTriggers;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.TranslatableComponent;
/*     */ import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.tags.Tag;
/*     */ import net.minecraft.world.Container;
/*     */ import net.minecraft.world.LockCode;
/*     */ import net.minecraft.world.MenuProvider;
/*     */ import net.minecraft.world.effect.MobEffect;
/*     */ import net.minecraft.world.effect.MobEffectInstance;
/*     */ import net.minecraft.world.effect.MobEffects;
/*     */ import net.minecraft.world.entity.player.Inventory;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.inventory.AbstractContainerMenu;
/*     */ import net.minecraft.world.inventory.BeaconMenu;
/*     */ import net.minecraft.world.inventory.ContainerData;
/*     */ import net.minecraft.world.inventory.ContainerLevelAccess;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.block.BeaconBeamBlock;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ 
/*     */ 
/*     */ public class BeaconBlockEntity
/*     */   extends BlockEntity
/*     */   implements MenuProvider, TickableBlockEntity
/*     */ {
/*  46 */   public static final MobEffect[][] BEACON_EFFECTS = new MobEffect[][] { { MobEffects.MOVEMENT_SPEED, MobEffects.DIG_SPEED }, { MobEffects.DAMAGE_RESISTANCE, MobEffects.JUMP }, { MobEffects.DAMAGE_BOOST }, { MobEffects.REGENERATION } };
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  51 */   private static final Set<MobEffect> VALID_EFFECTS = (Set<MobEffect>)Arrays.<MobEffect[]>stream(BEACON_EFFECTS).flatMap(Arrays::stream).collect(Collectors.toSet());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  60 */   private List<BeaconBeamSection> beamSections = Lists.newArrayList();
/*  61 */   private List<BeaconBeamSection> checkingBeamSections = Lists.newArrayList();
/*     */   
/*     */   private int levels;
/*  64 */   private int lastCheckY = -1;
/*     */   
/*     */   @Nullable
/*     */   private MobEffect primaryPower;
/*     */   
/*     */   @Nullable
/*     */   private MobEffect secondaryPower;
/*     */   
/*     */   @Nullable
/*     */   private Component name;
/*  74 */   private LockCode lockKey = LockCode.NO_LOCK;
/*     */   
/*  76 */   private final ContainerData dataAccess = new ContainerData()
/*     */     {
/*     */       public int get(int debug1) {
/*  79 */         switch (debug1) {
/*     */           case 0:
/*  81 */             return BeaconBlockEntity.this.levels;
/*     */           case 1:
/*  83 */             return MobEffect.getId(BeaconBlockEntity.this.primaryPower);
/*     */           case 2:
/*  85 */             return MobEffect.getId(BeaconBlockEntity.this.secondaryPower);
/*     */         } 
/*  87 */         return 0;
/*     */       }
/*     */ 
/*     */       
/*     */       public void set(int debug1, int debug2) {
/*  92 */         switch (debug1) {
/*     */           case 0:
/*  94 */             BeaconBlockEntity.this.levels = debug2;
/*     */             break;
/*     */           case 1:
/*  97 */             if (!BeaconBlockEntity.this.level.isClientSide && !BeaconBlockEntity.this.beamSections.isEmpty()) {
/*  98 */               BeaconBlockEntity.this.playSound(SoundEvents.BEACON_POWER_SELECT);
/*     */             }
/* 100 */             BeaconBlockEntity.this.primaryPower = BeaconBlockEntity.getValidEffectById(debug2);
/*     */             break;
/*     */           case 2:
/* 103 */             BeaconBlockEntity.this.secondaryPower = BeaconBlockEntity.getValidEffectById(debug2);
/*     */             break;
/*     */         } 
/*     */       }
/*     */ 
/*     */       
/*     */       public int getCount() {
/* 110 */         return 3;
/*     */       }
/*     */     };
/*     */   
/*     */   public BeaconBlockEntity() {
/* 115 */     super(BlockEntityType.BEACON);
/*     */   }
/*     */   
/*     */   public void tick() {
/*     */     BlockPos debug4;
/* 120 */     int debug1 = this.worldPosition.getX();
/* 121 */     int debug2 = this.worldPosition.getY();
/* 122 */     int debug3 = this.worldPosition.getZ();
/*     */ 
/*     */     
/* 125 */     if (this.lastCheckY < debug2) {
/* 126 */       debug4 = this.worldPosition;
/* 127 */       this.checkingBeamSections = Lists.newArrayList();
/* 128 */       this.lastCheckY = debug4.getY() - 1;
/*     */     } else {
/* 130 */       debug4 = new BlockPos(debug1, this.lastCheckY + 1, debug3);
/*     */     } 
/*     */     
/* 133 */     BeaconBeamSection debug5 = this.checkingBeamSections.isEmpty() ? null : this.checkingBeamSections.get(this.checkingBeamSections.size() - 1);
/*     */     
/* 135 */     int debug6 = this.level.getHeight(Heightmap.Types.WORLD_SURFACE, debug1, debug3);
/*     */     
/*     */     int debug7;
/* 138 */     for (debug7 = 0; debug7 < 10 && 
/* 139 */       debug4.getY() <= debug6; debug7++) {
/*     */ 
/*     */       
/* 142 */       BlockState debug8 = this.level.getBlockState(debug4);
/* 143 */       Block debug9 = debug8.getBlock();
/* 144 */       if (debug9 instanceof BeaconBeamBlock) {
/* 145 */         float[] debug10 = ((BeaconBeamBlock)debug9).getColor().getTextureDiffuseColors();
/*     */         
/* 147 */         if (this.checkingBeamSections.size() <= 1) {
/* 148 */           debug5 = new BeaconBeamSection(debug10);
/* 149 */           this.checkingBeamSections.add(debug5);
/* 150 */         } else if (debug5 != null) {
/*     */           
/* 152 */           if (Arrays.equals(debug10, debug5.color)) {
/* 153 */             debug5.increaseHeight();
/*     */           
/*     */           }
/*     */           else {
/*     */             
/* 158 */             debug5 = new BeaconBeamSection(new float[] { (BeaconBeamSection.access$500(debug5)[0] + debug10[0]) / 2.0F, (BeaconBeamSection.access$500(debug5)[1] + debug10[1]) / 2.0F, (BeaconBeamSection.access$500(debug5)[2] + debug10[2]) / 2.0F });
/*     */             
/* 160 */             this.checkingBeamSections.add(debug5);
/*     */           } 
/*     */         } 
/* 163 */       } else if (debug5 != null && (debug8.getLightBlock((BlockGetter)this.level, debug4) < 15 || debug9 == Blocks.BEDROCK)) {
/* 164 */         debug5.increaseHeight();
/*     */       } else {
/* 166 */         this.checkingBeamSections.clear();
/* 167 */         this.lastCheckY = debug6;
/*     */         break;
/*     */       } 
/* 170 */       debug4 = debug4.above();
/* 171 */       this.lastCheckY++;
/*     */     } 
/*     */     
/* 174 */     debug7 = this.levels;
/*     */     
/* 176 */     if (this.level.getGameTime() % 80L == 0L) {
/* 177 */       if (!this.beamSections.isEmpty()) {
/* 178 */         updateBase(debug1, debug2, debug3);
/*     */       }
/*     */       
/* 181 */       if (this.levels > 0 && !this.beamSections.isEmpty()) {
/* 182 */         applyEffects();
/*     */         
/* 184 */         playSound(SoundEvents.BEACON_AMBIENT);
/*     */       } 
/*     */     } 
/*     */     
/* 188 */     if (this.lastCheckY >= debug6) {
/* 189 */       this.lastCheckY = -1;
/* 190 */       boolean debug8 = (debug7 > 0);
/*     */       
/* 192 */       this.beamSections = this.checkingBeamSections;
/*     */       
/* 194 */       if (!this.level.isClientSide) {
/* 195 */         boolean debug9 = (this.levels > 0);
/*     */         
/* 197 */         if (!debug8 && debug9) {
/* 198 */           playSound(SoundEvents.BEACON_ACTIVATE);
/*     */           
/* 200 */           for (ServerPlayer debug11 : this.level.getEntitiesOfClass(ServerPlayer.class, (new AABB(debug1, debug2, debug3, debug1, (debug2 - 4), debug3)).inflate(10.0D, 5.0D, 10.0D))) {
/* 201 */             CriteriaTriggers.CONSTRUCT_BEACON.trigger(debug11, this);
/*     */           }
/* 203 */         } else if (debug8 && !debug9) {
/* 204 */           playSound(SoundEvents.BEACON_DEACTIVATE);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void updateBase(int debug1, int debug2, int debug3) {
/* 211 */     this.levels = 0;
/* 212 */     for (int debug4 = 1; debug4 <= 4; ) {
/* 213 */       int debug5 = debug2 - debug4;
/* 214 */       if (debug5 < 0) {
/*     */         break;
/*     */       }
/*     */       
/* 218 */       boolean debug6 = true;
/* 219 */       for (int debug7 = debug1 - debug4; debug7 <= debug1 + debug4 && debug6; debug7++) {
/* 220 */         for (int debug8 = debug3 - debug4; debug8 <= debug3 + debug4; debug8++) {
/* 221 */           if (!this.level.getBlockState(new BlockPos(debug7, debug5, debug8)).is((Tag)BlockTags.BEACON_BASE_BLOCKS)) {
/* 222 */             debug6 = false;
/*     */             
/*     */             break;
/*     */           } 
/*     */         } 
/*     */       } 
/* 228 */       if (debug6) {
/* 229 */         this.levels = debug4;
/*     */         debug4++;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRemoved() {
/* 238 */     playSound(SoundEvents.BEACON_DEACTIVATE);
/* 239 */     super.setRemoved();
/*     */   }
/*     */   
/*     */   private void applyEffects() {
/* 243 */     if (this.level.isClientSide || this.primaryPower == null) {
/*     */       return;
/*     */     }
/*     */     
/* 247 */     double debug1 = (this.levels * 10 + 10);
/* 248 */     int debug3 = 0;
/* 249 */     if (this.levels >= 4 && this.primaryPower == this.secondaryPower) {
/* 250 */       debug3 = 1;
/*     */     }
/* 252 */     int debug4 = (9 + this.levels * 2) * 20;
/*     */     
/* 254 */     AABB debug5 = (new AABB(this.worldPosition)).inflate(debug1).expandTowards(0.0D, this.level.getMaxBuildHeight(), 0.0D);
/* 255 */     List<Player> debug6 = this.level.getEntitiesOfClass(Player.class, debug5);
/* 256 */     for (Player debug8 : debug6) {
/* 257 */       debug8.addEffect(new MobEffectInstance(this.primaryPower, debug4, debug3, true, true));
/*     */     }
/*     */     
/* 260 */     if (this.levels >= 4 && this.primaryPower != this.secondaryPower && this.secondaryPower != null) {
/* 261 */       for (Player debug8 : debug6) {
/* 262 */         debug8.addEffect(new MobEffectInstance(this.secondaryPower, debug4, 0, true, true));
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void playSound(SoundEvent debug1) {
/* 268 */     this.level.playSound(null, this.worldPosition, debug1, SoundSource.BLOCKS, 1.0F, 1.0F);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getLevels() {
/* 276 */     return this.levels;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public ClientboundBlockEntityDataPacket getUpdatePacket() {
/* 282 */     return new ClientboundBlockEntityDataPacket(this.worldPosition, 3, getUpdateTag());
/*     */   }
/*     */ 
/*     */   
/*     */   public CompoundTag getUpdateTag() {
/* 287 */     return save(new CompoundTag());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private static MobEffect getValidEffectById(int debug0) {
/* 297 */     MobEffect debug1 = MobEffect.byId(debug0);
/*     */     
/* 299 */     return VALID_EFFECTS.contains(debug1) ? debug1 : null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void load(BlockState debug1, CompoundTag debug2) {
/* 304 */     super.load(debug1, debug2);
/*     */     
/* 306 */     this.primaryPower = getValidEffectById(debug2.getInt("Primary"));
/* 307 */     this.secondaryPower = getValidEffectById(debug2.getInt("Secondary"));
/*     */     
/* 309 */     if (debug2.contains("CustomName", 8)) {
/* 310 */       this.name = (Component)Component.Serializer.fromJson(debug2.getString("CustomName"));
/*     */     }
/*     */     
/* 313 */     this.lockKey = LockCode.fromTag(debug2);
/*     */   }
/*     */ 
/*     */   
/*     */   public CompoundTag save(CompoundTag debug1) {
/* 318 */     super.save(debug1);
/*     */     
/* 320 */     debug1.putInt("Primary", MobEffect.getId(this.primaryPower));
/* 321 */     debug1.putInt("Secondary", MobEffect.getId(this.secondaryPower));
/* 322 */     debug1.putInt("Levels", this.levels);
/*     */     
/* 324 */     if (this.name != null) {
/* 325 */       debug1.putString("CustomName", Component.Serializer.toJson(this.name));
/*     */     }
/*     */     
/* 328 */     this.lockKey.addToTag(debug1);
/*     */     
/* 330 */     return debug1;
/*     */   }
/*     */   
/*     */   public void setCustomName(@Nullable Component debug1) {
/* 334 */     this.name = debug1;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public AbstractContainerMenu createMenu(int debug1, Inventory debug2, Player debug3) {
/* 340 */     if (BaseContainerBlockEntity.canUnlock(debug3, this.lockKey, getDisplayName())) {
/* 341 */       return (AbstractContainerMenu)new BeaconMenu(debug1, (Container)debug2, this.dataAccess, ContainerLevelAccess.create(this.level, getBlockPos()));
/*     */     }
/* 343 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public Component getDisplayName() {
/* 348 */     return (this.name != null) ? this.name : (Component)new TranslatableComponent("container.beacon");
/*     */   }
/*     */   
/*     */   public static class BeaconBeamSection {
/*     */     private final float[] color;
/*     */     private int height;
/*     */     
/*     */     public BeaconBeamSection(float[] debug1) {
/* 356 */       this.color = debug1;
/* 357 */       this.height = 1;
/*     */     }
/*     */     
/*     */     protected void increaseHeight() {
/* 361 */       this.height++;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\entity\BeaconBlockEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */