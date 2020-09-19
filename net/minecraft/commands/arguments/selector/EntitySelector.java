/*     */ package net.minecraft.commands.arguments.selector;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.UUID;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Predicate;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.advancements.critereon.MinMaxBounds;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.commands.arguments.EntityArgument;
/*     */ import net.minecraft.network.chat.ComponentUtils;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class EntitySelector
/*     */ {
/*     */   private final int maxResults;
/*     */   private final boolean includesEntities;
/*     */   private final boolean worldLimited;
/*     */   private final Predicate<Entity> predicate;
/*     */   private final MinMaxBounds.Floats range;
/*     */   private final Function<Vec3, Vec3> position;
/*     */   @Nullable
/*     */   private final AABB aabb;
/*     */   private final BiConsumer<Vec3, List<? extends Entity>> order;
/*     */   private final boolean currentEntity;
/*     */   @Nullable
/*     */   private final String playerName;
/*     */   @Nullable
/*     */   private final UUID entityUUID;
/*     */   @Nullable
/*     */   private final EntityType<?> type;
/*     */   private final boolean usesSelector;
/*     */   
/*     */   public EntitySelector(int debug1, boolean debug2, boolean debug3, Predicate<Entity> debug4, MinMaxBounds.Floats debug5, Function<Vec3, Vec3> debug6, @Nullable AABB debug7, BiConsumer<Vec3, List<? extends Entity>> debug8, boolean debug9, @Nullable String debug10, @Nullable UUID debug11, @Nullable EntityType<?> debug12, boolean debug13) {
/*  48 */     this.maxResults = debug1;
/*  49 */     this.includesEntities = debug2;
/*  50 */     this.worldLimited = debug3;
/*  51 */     this.predicate = debug4;
/*  52 */     this.range = debug5;
/*  53 */     this.position = debug6;
/*  54 */     this.aabb = debug7;
/*  55 */     this.order = debug8;
/*  56 */     this.currentEntity = debug9;
/*  57 */     this.playerName = debug10;
/*  58 */     this.entityUUID = debug11;
/*  59 */     this.type = debug12;
/*  60 */     this.usesSelector = debug13;
/*     */   }
/*     */   
/*     */   public int getMaxResults() {
/*  64 */     return this.maxResults;
/*     */   }
/*     */   
/*     */   public boolean includesEntities() {
/*  68 */     return this.includesEntities;
/*     */   }
/*     */   
/*     */   public boolean isSelfSelector() {
/*  72 */     return this.currentEntity;
/*     */   }
/*     */   
/*     */   public boolean isWorldLimited() {
/*  76 */     return this.worldLimited;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void checkPermissions(CommandSourceStack debug1) throws CommandSyntaxException {
/*  84 */     if (this.usesSelector && !debug1.hasPermission(2)) {
/*  85 */       throw EntityArgument.ERROR_SELECTORS_NOT_ALLOWED.create();
/*     */     }
/*     */   }
/*     */   
/*     */   public Entity findSingleEntity(CommandSourceStack debug1) throws CommandSyntaxException {
/*  90 */     checkPermissions(debug1);
/*     */     
/*  92 */     List<? extends Entity> debug2 = findEntities(debug1);
/*  93 */     if (debug2.isEmpty()) {
/*  94 */       throw EntityArgument.NO_ENTITIES_FOUND.create();
/*     */     }
/*  96 */     if (debug2.size() > 1) {
/*  97 */       throw EntityArgument.ERROR_NOT_SINGLE_ENTITY.create();
/*     */     }
/*  99 */     return debug2.get(0);
/*     */   }
/*     */   
/*     */   public List<? extends Entity> findEntities(CommandSourceStack debug1) throws CommandSyntaxException {
/* 103 */     checkPermissions(debug1);
/*     */     
/* 105 */     if (!this.includesEntities) {
/* 106 */       return (List)findPlayers(debug1);
/*     */     }
/* 108 */     if (this.playerName != null) {
/* 109 */       ServerPlayer serverPlayer = debug1.getServer().getPlayerList().getPlayerByName(this.playerName);
/* 110 */       if (serverPlayer == null) {
/* 111 */         return Collections.emptyList();
/*     */       }
/* 113 */       return Lists.newArrayList((Object[])new ServerPlayer[] { serverPlayer });
/*     */     } 
/* 115 */     if (this.entityUUID != null) {
/* 116 */       for (ServerLevel serverLevel : debug1.getServer().getAllLevels()) {
/* 117 */         Entity entity = serverLevel.getEntity(this.entityUUID);
/* 118 */         if (entity != null) {
/* 119 */           return Lists.newArrayList((Object[])new Entity[] { entity });
/*     */         }
/*     */       } 
/* 122 */       return Collections.emptyList();
/*     */     } 
/* 124 */     Vec3 debug2 = this.position.apply(debug1.getPosition());
/* 125 */     Predicate<Entity> debug3 = getPredicate(debug2);
/*     */     
/* 127 */     if (this.currentEntity) {
/* 128 */       if (debug1.getEntity() != null && debug3.test(debug1.getEntity())) {
/* 129 */         return Lists.newArrayList((Object[])new Entity[] { debug1.getEntity() });
/*     */       }
/* 131 */       return Collections.emptyList();
/*     */     } 
/*     */ 
/*     */     
/* 135 */     List<Entity> debug4 = Lists.newArrayList();
/*     */     
/* 137 */     if (isWorldLimited()) {
/* 138 */       addEntities(debug4, debug1.getLevel(), debug2, debug3);
/*     */     } else {
/* 140 */       for (ServerLevel debug6 : debug1.getServer().getAllLevels()) {
/* 141 */         addEntities(debug4, debug6, debug2, debug3);
/*     */       }
/*     */     } 
/*     */     
/* 145 */     return sortAndLimit(debug2, debug4);
/*     */   }
/*     */   
/*     */   private void addEntities(List<Entity> debug1, ServerLevel debug2, Vec3 debug3, Predicate<Entity> debug4) {
/* 149 */     if (this.aabb != null) {
/* 150 */       debug1.addAll(debug2.getEntities(this.type, this.aabb.move(debug3), debug4));
/*     */     } else {
/* 152 */       debug1.addAll(debug2.getEntities(this.type, debug4));
/*     */     } 
/*     */   }
/*     */   
/*     */   public ServerPlayer findSinglePlayer(CommandSourceStack debug1) throws CommandSyntaxException {
/* 157 */     checkPermissions(debug1);
/*     */     
/* 159 */     List<ServerPlayer> debug2 = findPlayers(debug1);
/* 160 */     if (debug2.size() != 1) {
/* 161 */       throw EntityArgument.NO_PLAYERS_FOUND.create();
/*     */     }
/* 163 */     return debug2.get(0);
/*     */   }
/*     */   public List<ServerPlayer> findPlayers(CommandSourceStack debug1) throws CommandSyntaxException {
/*     */     List<ServerPlayer> debug4;
/* 167 */     checkPermissions(debug1);
/*     */     
/* 169 */     if (this.playerName != null) {
/* 170 */       ServerPlayer serverPlayer = debug1.getServer().getPlayerList().getPlayerByName(this.playerName);
/* 171 */       if (serverPlayer == null) {
/* 172 */         return Collections.emptyList();
/*     */       }
/* 174 */       return Lists.newArrayList((Object[])new ServerPlayer[] { serverPlayer });
/*     */     } 
/* 176 */     if (this.entityUUID != null) {
/* 177 */       ServerPlayer serverPlayer = debug1.getServer().getPlayerList().getPlayer(this.entityUUID);
/* 178 */       if (serverPlayer == null) {
/* 179 */         return Collections.emptyList();
/*     */       }
/* 181 */       return Lists.newArrayList((Object[])new ServerPlayer[] { serverPlayer });
/*     */     } 
/*     */ 
/*     */     
/* 185 */     Vec3 debug2 = this.position.apply(debug1.getPosition());
/* 186 */     Predicate<Entity> debug3 = getPredicate(debug2);
/*     */     
/* 188 */     if (this.currentEntity) {
/* 189 */       if (debug1.getEntity() instanceof ServerPlayer) {
/* 190 */         ServerPlayer serverPlayer = (ServerPlayer)debug1.getEntity();
/* 191 */         if (debug3.test(serverPlayer)) {
/* 192 */           return Lists.newArrayList((Object[])new ServerPlayer[] { serverPlayer });
/*     */         }
/*     */       } 
/* 195 */       return Collections.emptyList();
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 200 */     if (isWorldLimited()) {
/* 201 */       debug4 = debug1.getLevel().getPlayers(debug3::test);
/*     */     } else {
/* 203 */       debug4 = Lists.newArrayList();
/* 204 */       for (ServerPlayer debug6 : debug1.getServer().getPlayerList().getPlayers()) {
/* 205 */         if (debug3.test(debug6)) {
/* 206 */           debug4.add(debug6);
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 211 */     return sortAndLimit(debug2, debug4);
/*     */   }
/*     */   
/*     */   private Predicate<Entity> getPredicate(Vec3 debug1) {
/* 215 */     Predicate<Entity> debug2 = this.predicate;
/* 216 */     if (this.aabb != null) {
/* 217 */       AABB debug3 = this.aabb.move(debug1);
/* 218 */       debug2 = debug2.and(debug1 -> debug0.intersects(debug1.getBoundingBox()));
/*     */     } 
/*     */     
/* 221 */     if (!this.range.isAny()) {
/* 222 */       debug2 = debug2.and(debug2 -> this.range.matchesSqr(debug2.distanceToSqr(debug1)));
/*     */     }
/* 224 */     return debug2;
/*     */   }
/*     */   
/*     */   private <T extends Entity> List<T> sortAndLimit(Vec3 debug1, List<T> debug2) {
/* 228 */     if (debug2.size() > 1) {
/* 229 */       this.order.accept(debug1, debug2);
/*     */     }
/*     */     
/* 232 */     return debug2.subList(0, Math.min(this.maxResults, debug2.size()));
/*     */   }
/*     */   
/*     */   public static MutableComponent joinNames(List<? extends Entity> debug0) {
/* 236 */     return ComponentUtils.formatList(debug0, Entity::getDisplayName);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\commands\arguments\selector\EntitySelector.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */