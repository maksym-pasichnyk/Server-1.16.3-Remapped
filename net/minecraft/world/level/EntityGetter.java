/*     */ package net.minecraft.world.level;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.List;
/*     */ import java.util.UUID;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.stream.Stream;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntitySelector;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.ai.targeting.TargetingConditions;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.shapes.BooleanOp;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public interface EntityGetter
/*     */ {
/*     */   default <T extends Entity> List<T> getLoadedEntitiesOfClass(Class<? extends T> debug1, AABB debug2, @Nullable Predicate<? super T> debug3) {
/*  26 */     return getEntitiesOfClass(debug1, debug2, debug3);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   default List<Entity> getEntities(@Nullable Entity debug1, AABB debug2) {
/*  32 */     return getEntities(debug1, debug2, EntitySelector.NO_SPECTATORS);
/*     */   }
/*     */   
/*     */   default boolean isUnobstructed(@Nullable Entity debug1, VoxelShape debug2) {
/*  36 */     if (debug2.isEmpty()) {
/*  37 */       return true;
/*     */     }
/*     */     
/*  40 */     for (Entity debug4 : getEntities(debug1, debug2.bounds())) {
/*  41 */       if (!debug4.removed && debug4.blocksBuilding && (debug1 == null || !debug4.isPassengerOfSameVehicle(debug1)) && 
/*  42 */         Shapes.joinIsNotEmpty(debug2, Shapes.create(debug4.getBoundingBox()), BooleanOp.AND)) {
/*  43 */         return false;
/*     */       }
/*     */     } 
/*     */     
/*  47 */     return true;
/*     */   }
/*     */   
/*     */   default <T extends Entity> List<T> getEntitiesOfClass(Class<? extends T> debug1, AABB debug2) {
/*  51 */     return getEntitiesOfClass(debug1, debug2, EntitySelector.NO_SPECTATORS);
/*     */   }
/*     */   
/*     */   default <T extends Entity> List<T> getLoadedEntitiesOfClass(Class<? extends T> debug1, AABB debug2) {
/*  55 */     return getLoadedEntitiesOfClass(debug1, debug2, EntitySelector.NO_SPECTATORS);
/*     */   }
/*     */ 
/*     */   
/*     */   default Stream<VoxelShape> getEntityCollisions(@Nullable Entity debug1, AABB debug2, Predicate<Entity> debug3) {
/*  60 */     if (debug2.getSize() < 1.0E-7D) {
/*  61 */       return Stream.empty();
/*     */     }
/*     */     
/*  64 */     AABB debug4 = debug2.inflate(1.0E-7D);
/*     */     
/*  66 */     return getEntities(debug1, debug4, debug3.and(debug2 -> (debug2.getBoundingBox().intersects(debug0) && ((debug1 == null) ? debug2.canBeCollidedWith() : debug1.canCollideWith(debug2)))))
/*  67 */       .stream()
/*  68 */       .map(Entity::getBoundingBox)
/*  69 */       .map(Shapes::create);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   default Player getNearestPlayer(double debug1, double debug3, double debug5, double debug7, @Nullable Predicate<Entity> debug9) {
/*  75 */     double debug10 = -1.0D;
/*  76 */     Player debug12 = null;
/*     */     
/*  78 */     for (Player debug14 : players()) {
/*  79 */       if (debug9 != null && !debug9.test(debug14)) {
/*     */         continue;
/*     */       }
/*     */       
/*  83 */       double debug15 = debug14.distanceToSqr(debug1, debug3, debug5);
/*  84 */       if ((debug7 < 0.0D || debug15 < debug7 * debug7) && (debug10 == -1.0D || debug15 < debug10)) {
/*  85 */         debug10 = debug15;
/*  86 */         debug12 = debug14;
/*     */       } 
/*     */     } 
/*  89 */     return debug12;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   default Player getNearestPlayer(Entity debug1, double debug2) {
/*  94 */     return getNearestPlayer(debug1.getX(), debug1.getY(), debug1.getZ(), debug2, false);
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   default Player getNearestPlayer(double debug1, double debug3, double debug5, double debug7, boolean debug9) {
/*  99 */     Predicate<Entity> debug10 = debug9 ? EntitySelector.NO_CREATIVE_OR_SPECTATOR : EntitySelector.NO_SPECTATORS;
/* 100 */     return getNearestPlayer(debug1, debug3, debug5, debug7, debug10);
/*     */   }
/*     */   
/*     */   default boolean hasNearbyAlivePlayer(double debug1, double debug3, double debug5, double debug7) {
/* 104 */     for (Player debug10 : players()) {
/* 105 */       if (!EntitySelector.NO_SPECTATORS.test(debug10) || !EntitySelector.LIVING_ENTITY_STILL_ALIVE.test(debug10)) {
/*     */         continue;
/*     */       }
/* 108 */       double debug11 = debug10.distanceToSqr(debug1, debug3, debug5);
/* 109 */       if (debug7 < 0.0D || debug11 < debug7 * debug7) {
/* 110 */         return true;
/*     */       }
/*     */     } 
/* 113 */     return false;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   default Player getNearestPlayer(TargetingConditions debug1, LivingEntity debug2) {
/* 118 */     return getNearestEntity(players(), debug1, debug2, debug2.getX(), debug2.getY(), debug2.getZ());
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   default Player getNearestPlayer(TargetingConditions debug1, LivingEntity debug2, double debug3, double debug5, double debug7) {
/* 123 */     return getNearestEntity(players(), debug1, debug2, debug3, debug5, debug7);
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   default Player getNearestPlayer(TargetingConditions debug1, double debug2, double debug4, double debug6) {
/* 128 */     return getNearestEntity(players(), debug1, null, debug2, debug4, debug6);
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   default <T extends LivingEntity> T getNearestEntity(Class<? extends T> debug1, TargetingConditions debug2, @Nullable LivingEntity debug3, double debug4, double debug6, double debug8, AABB debug10) {
/* 133 */     return getNearestEntity(getEntitiesOfClass(debug1, debug10, null), debug2, debug3, debug4, debug6, debug8);
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   default <T extends LivingEntity> T getNearestLoadedEntity(Class<? extends T> debug1, TargetingConditions debug2, @Nullable LivingEntity debug3, double debug4, double debug6, double debug8, AABB debug10) {
/* 138 */     return getNearestEntity(getLoadedEntitiesOfClass(debug1, debug10, null), debug2, debug3, debug4, debug6, debug8);
/*     */   }
/*     */   @Nullable
/*     */   default <T extends LivingEntity> T getNearestEntity(List<? extends T> debug1, TargetingConditions debug2, @Nullable LivingEntity debug3, double debug4, double debug6, double debug8) {
/*     */     LivingEntity livingEntity;
/* 143 */     double debug10 = -1.0D;
/* 144 */     T debug12 = null;
/* 145 */     for (LivingEntity livingEntity1 : debug1) {
/* 146 */       if (!debug2.test(debug3, livingEntity1)) {
/*     */         continue;
/*     */       }
/*     */       
/* 150 */       double debug15 = livingEntity1.distanceToSqr(debug4, debug6, debug8);
/* 151 */       if (debug10 == -1.0D || debug15 < debug10) {
/* 152 */         debug10 = debug15;
/* 153 */         livingEntity = livingEntity1;
/*     */       } 
/*     */     } 
/*     */     
/* 157 */     return (T)livingEntity;
/*     */   }
/*     */   
/*     */   default List<Player> getNearbyPlayers(TargetingConditions debug1, LivingEntity debug2, AABB debug3) {
/* 161 */     List<Player> debug4 = Lists.newArrayList();
/* 162 */     for (Player debug6 : players()) {
/* 163 */       if (debug3.contains(debug6.getX(), debug6.getY(), debug6.getZ()) && debug1.test(debug2, (LivingEntity)debug6)) {
/* 164 */         debug4.add(debug6);
/*     */       }
/*     */     } 
/*     */     
/* 168 */     return debug4;
/*     */   }
/*     */   
/*     */   default <T extends LivingEntity> List<T> getNearbyEntities(Class<? extends T> debug1, TargetingConditions debug2, LivingEntity debug3, AABB debug4) {
/* 172 */     List<T> debug5 = (List)getEntitiesOfClass((Class)debug1, debug4, null);
/* 173 */     List<T> debug6 = Lists.newArrayList();
/*     */     
/* 175 */     for (LivingEntity livingEntity : debug5) {
/* 176 */       if (debug2.test(debug3, livingEntity)) {
/* 177 */         debug6.add((T)livingEntity);
/*     */       }
/*     */     } 
/*     */     
/* 181 */     return debug6;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   default Player getPlayerByUUID(UUID debug1) {
/* 186 */     for (int debug2 = 0; debug2 < players().size(); debug2++) {
/* 187 */       Player debug3 = players().get(debug2);
/* 188 */       if (debug1.equals(debug3.getUUID())) {
/* 189 */         return debug3;
/*     */       }
/*     */     } 
/* 192 */     return null;
/*     */   }
/*     */   
/*     */   List<Entity> getEntities(@Nullable Entity paramEntity, AABB paramAABB, @Nullable Predicate<? super Entity> paramPredicate);
/*     */   
/*     */   <T extends Entity> List<T> getEntitiesOfClass(Class<? extends T> paramClass, AABB paramAABB, @Nullable Predicate<? super T> paramPredicate);
/*     */   
/*     */   List<? extends Player> players();
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\EntityGetter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */