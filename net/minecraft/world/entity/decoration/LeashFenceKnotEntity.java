/*     */ package net.minecraft.world.entity.decoration;
/*     */ 
/*     */ import java.util.List;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.tags.Tag;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityDimensions;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.Pose;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ 
/*     */ public class LeashFenceKnotEntity
/*     */   extends HangingEntity {
/*     */   public LeashFenceKnotEntity(EntityType<? extends LeashFenceKnotEntity> debug1, Level debug2) {
/*  28 */     super((EntityType)debug1, debug2);
/*     */   }
/*     */   
/*     */   public LeashFenceKnotEntity(Level debug1, BlockPos debug2) {
/*  32 */     super(EntityType.LEASH_KNOT, debug1, debug2);
/*     */     
/*  34 */     setPos(debug2.getX() + 0.5D, debug2.getY() + 0.5D, debug2.getZ() + 0.5D);
/*  35 */     float debug3 = 0.125F;
/*  36 */     float debug4 = 0.1875F;
/*  37 */     float debug5 = 0.25F;
/*  38 */     setBoundingBox(new AABB(getX() - 0.1875D, getY() - 0.25D + 0.125D, getZ() - 0.1875D, getX() + 0.1875D, getY() + 0.25D + 0.125D, getZ() + 0.1875D));
/*  39 */     this.forcedLoading = true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setPos(double debug1, double debug3, double debug5) {
/*  44 */     super.setPos(Mth.floor(debug1) + 0.5D, Mth.floor(debug3) + 0.5D, Mth.floor(debug5) + 0.5D);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void recalculateBoundingBox() {
/*  49 */     setPosRaw(this.pos.getX() + 0.5D, this.pos.getY() + 0.5D, this.pos.getZ() + 0.5D);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDirection(Direction debug1) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public int getWidth() {
/*  59 */     return 9;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getHeight() {
/*  64 */     return 9;
/*     */   }
/*     */ 
/*     */   
/*     */   protected float getEyeHeight(Pose debug1, EntityDimensions debug2) {
/*  69 */     return -0.0625F;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void dropItem(@Nullable Entity debug1) {
/*  79 */     playSound(SoundEvents.LEASH_KNOT_BREAK, 1.0F, 1.0F);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void addAdditionalSaveData(CompoundTag debug1) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void readAdditionalSaveData(CompoundTag debug1) {}
/*     */ 
/*     */   
/*     */   public InteractionResult interact(Player debug1, InteractionHand debug2) {
/*  92 */     if (this.level.isClientSide) {
/*  93 */       return InteractionResult.SUCCESS;
/*     */     }
/*     */     
/*  96 */     boolean debug3 = false;
/*  97 */     double debug4 = 7.0D;
/*  98 */     List<Mob> debug6 = this.level.getEntitiesOfClass(Mob.class, new AABB(getX() - 7.0D, getY() - 7.0D, getZ() - 7.0D, getX() + 7.0D, getY() + 7.0D, getZ() + 7.0D));
/*  99 */     for (Mob debug8 : debug6) {
/* 100 */       if (debug8.getLeashHolder() == debug1) {
/* 101 */         debug8.setLeashedTo(this, true);
/* 102 */         debug3 = true;
/*     */       } 
/*     */     } 
/*     */     
/* 106 */     if (!debug3) {
/* 107 */       remove();
/* 108 */       if (debug1.abilities.instabuild) {
/* 109 */         for (Mob debug8 : debug6) {
/* 110 */           if (debug8.isLeashed() && debug8.getLeashHolder() == this) {
/* 111 */             debug8.dropLeash(true, false);
/*     */           }
/*     */         } 
/*     */       }
/*     */     } 
/*     */     
/* 117 */     return InteractionResult.CONSUME;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean survives() {
/* 123 */     return this.level.getBlockState(this.pos).getBlock().is((Tag)BlockTags.FENCES);
/*     */   }
/*     */   
/*     */   public static LeashFenceKnotEntity getOrCreateKnot(Level debug0, BlockPos debug1) {
/* 127 */     int debug2 = debug1.getX();
/* 128 */     int debug3 = debug1.getY();
/* 129 */     int debug4 = debug1.getZ();
/*     */     
/* 131 */     List<LeashFenceKnotEntity> debug5 = debug0.getEntitiesOfClass(LeashFenceKnotEntity.class, new AABB(debug2 - 1.0D, debug3 - 1.0D, debug4 - 1.0D, debug2 + 1.0D, debug3 + 1.0D, debug4 + 1.0D));
/* 132 */     for (LeashFenceKnotEntity debug7 : debug5) {
/* 133 */       if (debug7.getPos().equals(debug1)) {
/* 134 */         return debug7;
/*     */       }
/*     */     } 
/*     */     
/* 138 */     LeashFenceKnotEntity debug6 = new LeashFenceKnotEntity(debug0, debug1);
/* 139 */     debug0.addFreshEntity(debug6);
/* 140 */     debug6.playPlacementSound();
/* 141 */     return debug6;
/*     */   }
/*     */ 
/*     */   
/*     */   public void playPlacementSound() {
/* 146 */     playSound(SoundEvents.LEASH_KNOT_PLACE, 1.0F, 1.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   public Packet<?> getAddEntityPacket() {
/* 151 */     return (Packet<?>)new ClientboundAddEntityPacket(this, getType(), 0, getPos());
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\decoration\LeashFenceKnotEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */