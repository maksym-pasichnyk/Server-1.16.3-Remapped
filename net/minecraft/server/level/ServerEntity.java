/*     */ package net.minecraft.server.level;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ClientboundMoveEntityPacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundRotateHeadPacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundSetEntityLinkPacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundSetEquipmentPacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundSetPassengersPacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundTeleportEntityPacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundUpdateAttributesPacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundUpdateMobEffectPacket;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.effect.MobEffectInstance;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EquipmentSlot;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeInstance;
/*     */ import net.minecraft.world.entity.decoration.ItemFrame;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.MapItem;
/*     */ import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ServerEntity
/*     */ {
/*  43 */   private static final Logger LOGGER = LogManager.getLogger();
/*     */   
/*     */   private final ServerLevel level;
/*     */   
/*     */   private final Entity entity;
/*     */   
/*     */   private final int updateInterval;
/*     */   
/*     */   private final boolean trackDelta;
/*     */   
/*     */   private final Consumer<Packet<?>> broadcast;
/*     */   
/*     */   private long xp;
/*     */   private long yp;
/*     */   private long zp;
/*     */   private int yRotp;
/*     */   private int xRotp;
/*     */   private int yHeadRotp;
/*  61 */   private Vec3 ap = Vec3.ZERO;
/*     */   private int tickCount;
/*     */   private int teleportDelay;
/*  64 */   private List<Entity> lastPassengers = Collections.emptyList();
/*     */   private boolean wasRiding;
/*     */   private boolean wasOnGround;
/*     */   
/*     */   public ServerEntity(ServerLevel debug1, Entity debug2, int debug3, boolean debug4, Consumer<Packet<?>> debug5) {
/*  69 */     this.level = debug1;
/*  70 */     this.broadcast = debug5;
/*  71 */     this.entity = debug2;
/*  72 */     this.updateInterval = debug3;
/*  73 */     this.trackDelta = debug4;
/*     */     
/*  75 */     updateSentPos();
/*     */     
/*  77 */     this.yRotp = Mth.floor(debug2.yRot * 256.0F / 360.0F);
/*  78 */     this.xRotp = Mth.floor(debug2.xRot * 256.0F / 360.0F);
/*     */     
/*  80 */     this.yHeadRotp = Mth.floor(debug2.getYHeadRot() * 256.0F / 360.0F);
/*  81 */     this.wasOnGround = debug2.isOnGround();
/*     */   }
/*     */   
/*     */   public void sendChanges() {
/*  85 */     List<Entity> debug1 = this.entity.getPassengers();
/*  86 */     if (!debug1.equals(this.lastPassengers)) {
/*  87 */       this.lastPassengers = debug1;
/*  88 */       this.broadcast.accept(new ClientboundSetPassengersPacket(this.entity));
/*     */     } 
/*     */     
/*  91 */     if (this.entity instanceof ItemFrame && this.tickCount % 10 == 0) {
/*  92 */       ItemFrame debug2 = (ItemFrame)this.entity;
/*  93 */       ItemStack debug3 = debug2.getItem();
/*     */       
/*  95 */       if (debug3.getItem() instanceof MapItem) {
/*  96 */         MapItemSavedData debug4 = MapItem.getOrCreateSavedData(debug3, this.level);
/*  97 */         for (ServerPlayer debug6 : this.level.players()) {
/*  98 */           debug4.tickCarriedBy(debug6, debug3);
/*     */           
/* 100 */           Packet<?> debug7 = ((MapItem)debug3.getItem()).getUpdatePacket(debug3, this.level, debug6);
/* 101 */           if (debug7 != null) {
/* 102 */             debug6.connection.send(debug7);
/*     */           }
/*     */         } 
/*     */       } 
/*     */       
/* 107 */       sendDirtyEntityData();
/*     */     } 
/*     */     
/* 110 */     if (this.tickCount % this.updateInterval == 0 || this.entity.hasImpulse || this.entity.getEntityData().isDirty()) {
/* 111 */       if (this.entity.isPassenger()) {
/*     */         
/* 113 */         int i = Mth.floor(this.entity.yRot * 256.0F / 360.0F);
/* 114 */         int debug3 = Mth.floor(this.entity.xRot * 256.0F / 360.0F);
/* 115 */         boolean debug4 = (Math.abs(i - this.yRotp) >= 1 || Math.abs(debug3 - this.xRotp) >= 1);
/* 116 */         if (debug4) {
/* 117 */           this.broadcast.accept(new ClientboundMoveEntityPacket.Rot(this.entity.getId(), (byte)i, (byte)debug3, this.entity.isOnGround()));
/* 118 */           this.yRotp = i;
/* 119 */           this.xRotp = debug3;
/*     */         } 
/*     */         
/* 122 */         updateSentPos();
/*     */         
/* 124 */         sendDirtyEntityData();
/*     */         
/* 126 */         this.wasRiding = true;
/*     */       } else {
/* 128 */         ClientboundMoveEntityPacket.Rot rot; this.teleportDelay++;
/* 129 */         int i = Mth.floor(this.entity.yRot * 256.0F / 360.0F);
/* 130 */         int debug3 = Mth.floor(this.entity.xRot * 256.0F / 360.0F);
/*     */         
/* 132 */         Vec3 debug4 = this.entity.position().subtract(ClientboundMoveEntityPacket.packetToEntity(this.xp, this.yp, this.zp));
/*     */ 
/*     */         
/* 135 */         boolean debug5 = (debug4.lengthSqr() >= 7.62939453125E-6D);
/*     */         
/* 137 */         Packet<?> debug6 = null;
/*     */         
/* 139 */         boolean debug7 = (debug5 || this.tickCount % 60 == 0);
/* 140 */         boolean debug8 = (Math.abs(i - this.yRotp) >= 1 || Math.abs(debug3 - this.xRotp) >= 1);
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 145 */         if (this.tickCount > 0 || this.entity instanceof net.minecraft.world.entity.projectile.AbstractArrow) {
/* 146 */           long debug9 = ClientboundMoveEntityPacket.entityToPacket(debug4.x);
/* 147 */           long debug11 = ClientboundMoveEntityPacket.entityToPacket(debug4.y);
/* 148 */           long debug13 = ClientboundMoveEntityPacket.entityToPacket(debug4.z);
/* 149 */           boolean debug15 = (debug9 < -32768L || debug9 > 32767L || debug11 < -32768L || debug11 > 32767L || debug13 < -32768L || debug13 > 32767L);
/* 150 */           if (debug15 || this.teleportDelay > 400 || this.wasRiding || this.wasOnGround != this.entity.isOnGround()) {
/* 151 */             this.wasOnGround = this.entity.isOnGround();
/* 152 */             this.teleportDelay = 0;
/* 153 */             ClientboundTeleportEntityPacket clientboundTeleportEntityPacket = new ClientboundTeleportEntityPacket(this.entity);
/*     */           }
/* 155 */           else if ((debug7 && debug8) || this.entity instanceof net.minecraft.world.entity.projectile.AbstractArrow) {
/* 156 */             ClientboundMoveEntityPacket.PosRot posRot = new ClientboundMoveEntityPacket.PosRot(this.entity.getId(), (short)(int)debug9, (short)(int)debug11, (short)(int)debug13, (byte)i, (byte)debug3, this.entity.isOnGround());
/* 157 */           } else if (debug7) {
/* 158 */             ClientboundMoveEntityPacket.Pos pos = new ClientboundMoveEntityPacket.Pos(this.entity.getId(), (short)(int)debug9, (short)(int)debug11, (short)(int)debug13, this.entity.isOnGround());
/* 159 */           } else if (debug8) {
/* 160 */             rot = new ClientboundMoveEntityPacket.Rot(this.entity.getId(), (byte)i, (byte)debug3, this.entity.isOnGround());
/*     */           } 
/*     */         } 
/*     */ 
/*     */         
/* 165 */         if ((this.trackDelta || this.entity.hasImpulse || (this.entity instanceof LivingEntity && ((LivingEntity)this.entity).isFallFlying())) && this.tickCount > 0) {
/* 166 */           Vec3 debug9 = this.entity.getDeltaMovement();
/* 167 */           double debug10 = debug9.distanceToSqr(this.ap);
/*     */           
/* 169 */           if (debug10 > 1.0E-7D || (debug10 > 0.0D && debug9.lengthSqr() == 0.0D)) {
/* 170 */             this.ap = debug9;
/* 171 */             this.broadcast.accept(new ClientboundSetEntityMotionPacket(this.entity.getId(), this.ap));
/*     */           } 
/*     */         } 
/*     */         
/* 175 */         if (rot != null) {
/* 176 */           this.broadcast.accept(rot);
/*     */         }
/*     */         
/* 179 */         sendDirtyEntityData();
/*     */         
/* 181 */         if (debug7) {
/* 182 */           updateSentPos();
/*     */         }
/* 184 */         if (debug8) {
/* 185 */           this.yRotp = i;
/* 186 */           this.xRotp = debug3;
/*     */         } 
/*     */         
/* 189 */         this.wasRiding = false;
/*     */       } 
/*     */       
/* 192 */       int debug2 = Mth.floor(this.entity.getYHeadRot() * 256.0F / 360.0F);
/* 193 */       if (Math.abs(debug2 - this.yHeadRotp) >= 1) {
/* 194 */         this.broadcast.accept(new ClientboundRotateHeadPacket(this.entity, (byte)debug2));
/* 195 */         this.yHeadRotp = debug2;
/*     */       } 
/* 197 */       this.entity.hasImpulse = false;
/*     */     } 
/*     */     
/* 200 */     this.tickCount++;
/*     */     
/* 202 */     if (this.entity.hurtMarked) {
/* 203 */       broadcastAndSend((Packet<?>)new ClientboundSetEntityMotionPacket(this.entity));
/* 204 */       this.entity.hurtMarked = false;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void removePairing(ServerPlayer debug1) {
/* 209 */     this.entity.stopSeenByPlayer(debug1);
/* 210 */     debug1.sendRemoveEntity(this.entity);
/*     */   }
/*     */   
/*     */   public void addPairing(ServerPlayer debug1) {
/* 214 */     sendPairingData(debug1.connection::send);
/*     */     
/* 216 */     this.entity.startSeenByPlayer(debug1);
/* 217 */     debug1.cancelRemoveEntity(this.entity);
/*     */   }
/*     */   
/*     */   public void sendPairingData(Consumer<Packet<?>> debug1) {
/* 221 */     if (this.entity.removed) {
/* 222 */       LOGGER.warn("Fetching packet for removed entity " + this.entity);
/*     */     }
/* 224 */     Packet<?> debug2 = this.entity.getAddEntityPacket();
/* 225 */     this.yHeadRotp = Mth.floor(this.entity.getYHeadRot() * 256.0F / 360.0F);
/* 226 */     debug1.accept(debug2);
/*     */     
/* 228 */     if (!this.entity.getEntityData().isEmpty()) {
/* 229 */       debug1.accept(new ClientboundSetEntityDataPacket(this.entity.getId(), this.entity.getEntityData(), true));
/*     */     }
/*     */     
/* 232 */     boolean debug3 = this.trackDelta;
/* 233 */     if (this.entity instanceof LivingEntity) {
/* 234 */       Collection<AttributeInstance> debug4 = ((LivingEntity)this.entity).getAttributes().getSyncableAttributes();
/*     */       
/* 236 */       if (!debug4.isEmpty()) {
/* 237 */         debug1.accept(new ClientboundUpdateAttributesPacket(this.entity.getId(), debug4));
/*     */       }
/* 239 */       if (((LivingEntity)this.entity).isFallFlying()) {
/* 240 */         debug3 = true;
/*     */       }
/*     */     } 
/*     */     
/* 244 */     this.ap = this.entity.getDeltaMovement();
/*     */     
/* 246 */     if (debug3 && !(debug2 instanceof net.minecraft.network.protocol.game.ClientboundAddMobPacket)) {
/* 247 */       debug1.accept(new ClientboundSetEntityMotionPacket(this.entity.getId(), this.ap));
/*     */     }
/*     */     
/* 250 */     if (this.entity instanceof LivingEntity) {
/* 251 */       List<Pair<EquipmentSlot, ItemStack>> debug4 = Lists.newArrayList();
/* 252 */       for (EquipmentSlot debug8 : EquipmentSlot.values()) {
/* 253 */         ItemStack debug9 = ((LivingEntity)this.entity).getItemBySlot(debug8);
/* 254 */         if (!debug9.isEmpty()) {
/* 255 */           debug4.add(Pair.of(debug8, debug9.copy()));
/*     */         }
/*     */       } 
/* 258 */       if (!debug4.isEmpty()) {
/* 259 */         debug1.accept(new ClientboundSetEquipmentPacket(this.entity.getId(), debug4));
/*     */       }
/*     */     } 
/*     */     
/* 263 */     if (this.entity instanceof LivingEntity) {
/* 264 */       LivingEntity debug4 = (LivingEntity)this.entity;
/* 265 */       for (MobEffectInstance debug6 : debug4.getActiveEffects()) {
/* 266 */         debug1.accept(new ClientboundUpdateMobEffectPacket(this.entity.getId(), debug6));
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 272 */     if (!this.entity.getPassengers().isEmpty()) {
/* 273 */       debug1.accept(new ClientboundSetPassengersPacket(this.entity));
/*     */     }
/* 275 */     if (this.entity.isPassenger()) {
/* 276 */       debug1.accept(new ClientboundSetPassengersPacket(this.entity.getVehicle()));
/*     */     }
/*     */ 
/*     */     
/* 280 */     if (this.entity instanceof Mob) {
/* 281 */       Mob debug4 = (Mob)this.entity;
/* 282 */       if (debug4.isLeashed()) {
/* 283 */         debug1.accept(new ClientboundSetEntityLinkPacket((Entity)debug4, debug4.getLeashHolder()));
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private void sendDirtyEntityData() {
/* 289 */     SynchedEntityData debug1 = this.entity.getEntityData();
/* 290 */     if (debug1.isDirty()) {
/* 291 */       broadcastAndSend((Packet<?>)new ClientboundSetEntityDataPacket(this.entity.getId(), debug1, false));
/*     */     }
/*     */     
/* 294 */     if (this.entity instanceof LivingEntity) {
/* 295 */       Set<AttributeInstance> debug2 = ((LivingEntity)this.entity).getAttributes().getDirtyAttributes();
/*     */       
/* 297 */       if (!debug2.isEmpty()) {
/* 298 */         broadcastAndSend((Packet<?>)new ClientboundUpdateAttributesPacket(this.entity.getId(), debug2));
/*     */       }
/*     */       
/* 301 */       debug2.clear();
/*     */     } 
/*     */   }
/*     */   
/*     */   private void updateSentPos() {
/* 306 */     this.xp = ClientboundMoveEntityPacket.entityToPacket(this.entity.getX());
/* 307 */     this.yp = ClientboundMoveEntityPacket.entityToPacket(this.entity.getY());
/* 308 */     this.zp = ClientboundMoveEntityPacket.entityToPacket(this.entity.getZ());
/*     */   }
/*     */   
/*     */   public Vec3 sentPos() {
/* 312 */     return ClientboundMoveEntityPacket.packetToEntity(this.xp, this.yp, this.zp);
/*     */   }
/*     */   
/*     */   private void broadcastAndSend(Packet<?> debug1) {
/* 316 */     this.broadcast.accept(debug1);
/* 317 */     if (this.entity instanceof ServerPlayer)
/* 318 */       ((ServerPlayer)this.entity).connection.send(debug1); 
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\level\ServerEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */