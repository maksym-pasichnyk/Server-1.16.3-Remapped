/*     */ package net.minecraft.server.commands;
/*     */ 
/*     */ import com.mojang.brigadier.CommandDispatcher;
/*     */ import com.mojang.brigadier.Message;
/*     */ import com.mojang.brigadier.arguments.ArgumentType;
/*     */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*     */ import com.mojang.brigadier.builder.RequiredArgumentBuilder;
/*     */ import com.mojang.brigadier.context.CommandContext;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*     */ import com.mojang.brigadier.tree.CommandNode;
/*     */ import com.mojang.brigadier.tree.LiteralCommandNode;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.EnumSet;
/*     */ import java.util.Set;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.commands.Commands;
/*     */ import net.minecraft.commands.arguments.EntityAnchorArgument;
/*     */ import net.minecraft.commands.arguments.EntityArgument;
/*     */ import net.minecraft.commands.arguments.coordinates.Coordinates;
/*     */ import net.minecraft.commands.arguments.coordinates.RotationArgument;
/*     */ import net.minecraft.commands.arguments.coordinates.Vec3Argument;
/*     */ import net.minecraft.commands.arguments.coordinates.WorldCoordinates;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.TranslatableComponent;
/*     */ import net.minecraft.network.protocol.game.ClientboundPlayerPositionPacket;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.server.level.TicketType;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.phys.Vec2;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TeleportCommand
/*     */ {
/*  49 */   private static final SimpleCommandExceptionType INVALID_POSITION = new SimpleCommandExceptionType((Message)new TranslatableComponent("commands.teleport.invalidPosition"));
/*     */   
/*     */   public static void register(CommandDispatcher<CommandSourceStack> debug0) {
/*  52 */     LiteralCommandNode<CommandSourceStack> debug1 = debug0.register(
/*  53 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("teleport")
/*  54 */         .requires(debug0 -> debug0.hasPermission(2)))
/*  55 */         .then((
/*  56 */           (RequiredArgumentBuilder)Commands.argument("targets", (ArgumentType)EntityArgument.entities())
/*  57 */           .then((
/*  58 */             (RequiredArgumentBuilder)((RequiredArgumentBuilder)Commands.argument("location", (ArgumentType)Vec3Argument.vec3())
/*  59 */             .executes(debug0 -> teleportToPos((CommandSourceStack)debug0.getSource(), EntityArgument.getEntities(debug0, "targets"), ((CommandSourceStack)debug0.getSource()).getLevel(), Vec3Argument.getCoordinates(debug0, "location"), null, null)))
/*  60 */             .then(
/*  61 */               Commands.argument("rotation", (ArgumentType)RotationArgument.rotation())
/*  62 */               .executes(debug0 -> teleportToPos((CommandSourceStack)debug0.getSource(), EntityArgument.getEntities(debug0, "targets"), ((CommandSourceStack)debug0.getSource()).getLevel(), Vec3Argument.getCoordinates(debug0, "location"), RotationArgument.getRotation(debug0, "rotation"), null))))
/*     */             
/*  64 */             .then((
/*  65 */               (LiteralArgumentBuilder)Commands.literal("facing")
/*  66 */               .then(
/*  67 */                 Commands.literal("entity")
/*  68 */                 .then((
/*  69 */                   (RequiredArgumentBuilder)Commands.argument("facingEntity", (ArgumentType)EntityArgument.entity())
/*  70 */                   .executes(debug0 -> teleportToPos((CommandSourceStack)debug0.getSource(), EntityArgument.getEntities(debug0, "targets"), ((CommandSourceStack)debug0.getSource()).getLevel(), Vec3Argument.getCoordinates(debug0, "location"), null, new LookAt(EntityArgument.getEntity(debug0, "facingEntity"), EntityAnchorArgument.Anchor.FEET))))
/*  71 */                   .then(
/*  72 */                     Commands.argument("facingAnchor", (ArgumentType)EntityAnchorArgument.anchor())
/*  73 */                     .executes(debug0 -> teleportToPos((CommandSourceStack)debug0.getSource(), EntityArgument.getEntities(debug0, "targets"), ((CommandSourceStack)debug0.getSource()).getLevel(), Vec3Argument.getCoordinates(debug0, "location"), null, new LookAt(EntityArgument.getEntity(debug0, "facingEntity"), EntityAnchorArgument.getAnchor(debug0, "facingAnchor"))))))))
/*     */ 
/*     */ 
/*     */               
/*  77 */               .then(
/*  78 */                 Commands.argument("facingLocation", (ArgumentType)Vec3Argument.vec3())
/*  79 */                 .executes(debug0 -> teleportToPos((CommandSourceStack)debug0.getSource(), EntityArgument.getEntities(debug0, "targets"), ((CommandSourceStack)debug0.getSource()).getLevel(), Vec3Argument.getCoordinates(debug0, "location"), null, new LookAt(Vec3Argument.getVec3(debug0, "facingLocation"))))))))
/*     */ 
/*     */ 
/*     */           
/*  83 */           .then(
/*  84 */             Commands.argument("destination", (ArgumentType)EntityArgument.entity())
/*  85 */             .executes(debug0 -> teleportToEntity((CommandSourceStack)debug0.getSource(), EntityArgument.getEntities(debug0, "targets"), EntityArgument.getEntity(debug0, "destination"))))))
/*     */ 
/*     */         
/*  88 */         .then(
/*  89 */           Commands.argument("location", (ArgumentType)Vec3Argument.vec3())
/*  90 */           .executes(debug0 -> teleportToPos((CommandSourceStack)debug0.getSource(), Collections.singleton(((CommandSourceStack)debug0.getSource()).getEntityOrException()), ((CommandSourceStack)debug0.getSource()).getLevel(), Vec3Argument.getCoordinates(debug0, "location"), (Coordinates)WorldCoordinates.current(), null))))
/*     */         
/*  92 */         .then(
/*  93 */           Commands.argument("destination", (ArgumentType)EntityArgument.entity())
/*  94 */           .executes(debug0 -> teleportToEntity((CommandSourceStack)debug0.getSource(), Collections.singleton(((CommandSourceStack)debug0.getSource()).getEntityOrException()), EntityArgument.getEntity(debug0, "destination")))));
/*     */ 
/*     */     
/*  97 */     debug0.register(
/*  98 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("tp")
/*  99 */         .requires(debug0 -> debug0.hasPermission(2)))
/* 100 */         .redirect((CommandNode)debug1));
/*     */   }
/*     */ 
/*     */   
/*     */   private static int teleportToEntity(CommandSourceStack debug0, Collection<? extends Entity> debug1, Entity debug2) throws CommandSyntaxException {
/* 105 */     for (Entity debug4 : debug1) {
/* 106 */       performTeleport(debug0, debug4, (ServerLevel)debug2.level, debug2.getX(), debug2.getY(), debug2.getZ(), EnumSet.noneOf(ClientboundPlayerPositionPacket.RelativeArgument.class), debug2.yRot, debug2.xRot, null);
/*     */     }
/*     */     
/* 109 */     if (debug1.size() == 1) {
/* 110 */       debug0.sendSuccess((Component)new TranslatableComponent("commands.teleport.success.entity.single", new Object[] { ((Entity)debug1.iterator().next()).getDisplayName(), debug2.getDisplayName() }), true);
/*     */     } else {
/* 112 */       debug0.sendSuccess((Component)new TranslatableComponent("commands.teleport.success.entity.multiple", new Object[] { Integer.valueOf(debug1.size()), debug2.getDisplayName() }), true);
/*     */     } 
/*     */     
/* 115 */     return debug1.size();
/*     */   }
/*     */   
/*     */   private static int teleportToPos(CommandSourceStack debug0, Collection<? extends Entity> debug1, ServerLevel debug2, Coordinates debug3, @Nullable Coordinates debug4, @Nullable LookAt debug5) throws CommandSyntaxException {
/* 119 */     Vec3 debug6 = debug3.getPosition(debug0);
/* 120 */     Vec2 debug7 = (debug4 == null) ? null : debug4.getRotation(debug0);
/* 121 */     Set<ClientboundPlayerPositionPacket.RelativeArgument> debug8 = EnumSet.noneOf(ClientboundPlayerPositionPacket.RelativeArgument.class);
/*     */     
/* 123 */     if (debug3.isXRelative()) {
/* 124 */       debug8.add(ClientboundPlayerPositionPacket.RelativeArgument.X);
/*     */     }
/* 126 */     if (debug3.isYRelative()) {
/* 127 */       debug8.add(ClientboundPlayerPositionPacket.RelativeArgument.Y);
/*     */     }
/* 129 */     if (debug3.isZRelative()) {
/* 130 */       debug8.add(ClientboundPlayerPositionPacket.RelativeArgument.Z);
/*     */     }
/* 132 */     if (debug4 == null) {
/* 133 */       debug8.add(ClientboundPlayerPositionPacket.RelativeArgument.X_ROT);
/* 134 */       debug8.add(ClientboundPlayerPositionPacket.RelativeArgument.Y_ROT);
/*     */     } else {
/* 136 */       if (debug4.isXRelative()) {
/* 137 */         debug8.add(ClientboundPlayerPositionPacket.RelativeArgument.X_ROT);
/*     */       }
/* 139 */       if (debug4.isYRelative()) {
/* 140 */         debug8.add(ClientboundPlayerPositionPacket.RelativeArgument.Y_ROT);
/*     */       }
/*     */     } 
/*     */     
/* 144 */     for (Entity debug10 : debug1) {
/* 145 */       if (debug4 == null) {
/* 146 */         performTeleport(debug0, debug10, debug2, debug6.x, debug6.y, debug6.z, debug8, debug10.yRot, debug10.xRot, debug5); continue;
/*     */       } 
/* 148 */       performTeleport(debug0, debug10, debug2, debug6.x, debug6.y, debug6.z, debug8, debug7.y, debug7.x, debug5);
/*     */     } 
/*     */ 
/*     */     
/* 152 */     if (debug1.size() == 1) {
/* 153 */       debug0.sendSuccess((Component)new TranslatableComponent("commands.teleport.success.location.single", new Object[] { ((Entity)debug1.iterator().next()).getDisplayName(), Double.valueOf(debug6.x), Double.valueOf(debug6.y), Double.valueOf(debug6.z) }), true);
/*     */     } else {
/* 155 */       debug0.sendSuccess((Component)new TranslatableComponent("commands.teleport.success.location.multiple", new Object[] { Integer.valueOf(debug1.size()), Double.valueOf(debug6.x), Double.valueOf(debug6.y), Double.valueOf(debug6.z) }), true);
/*     */     } 
/*     */     
/* 158 */     return debug1.size();
/*     */   }
/*     */   
/*     */   private static void performTeleport(CommandSourceStack debug0, Entity debug1, ServerLevel debug2, double debug3, double debug5, double debug7, Set<ClientboundPlayerPositionPacket.RelativeArgument> debug9, float debug10, float debug11, @Nullable LookAt debug12) throws CommandSyntaxException {
/* 162 */     BlockPos debug13 = new BlockPos(debug3, debug5, debug7);
/* 163 */     if (!Level.isInSpawnableBounds(debug13)) {
/* 164 */       throw INVALID_POSITION.create();
/*     */     }
/*     */     
/* 167 */     if (debug1 instanceof ServerPlayer) {
/* 168 */       ChunkPos debug14 = new ChunkPos(new BlockPos(debug3, debug5, debug7));
/* 169 */       debug2.getChunkSource().addRegionTicket(TicketType.POST_TELEPORT, debug14, 1, Integer.valueOf(debug1.getId()));
/* 170 */       debug1.stopRiding();
/*     */       
/* 172 */       if (((ServerPlayer)debug1).isSleeping()) {
/* 173 */         ((ServerPlayer)debug1).stopSleepInBed(true, true);
/*     */       }
/*     */       
/* 176 */       if (debug2 == debug1.level) {
/* 177 */         ((ServerPlayer)debug1).connection.teleport(debug3, debug5, debug7, debug10, debug11, debug9);
/*     */       } else {
/* 179 */         ((ServerPlayer)debug1).teleportTo(debug2, debug3, debug5, debug7, debug10, debug11);
/*     */       } 
/* 181 */       debug1.setYHeadRot(debug10);
/*     */     } else {
/* 183 */       float debug14 = Mth.wrapDegrees(debug10);
/* 184 */       float debug15 = Mth.wrapDegrees(debug11);
/*     */       
/* 186 */       debug15 = Mth.clamp(debug15, -90.0F, 90.0F);
/*     */       
/* 188 */       if (debug2 == debug1.level) {
/* 189 */         debug1.moveTo(debug3, debug5, debug7, debug14, debug15);
/* 190 */         debug1.setYHeadRot(debug14);
/*     */       } else {
/* 192 */         debug1.unRide();
/* 193 */         Entity debug16 = debug1;
/* 194 */         debug1 = debug16.getType().create((Level)debug2);
/* 195 */         if (debug1 != null) {
/* 196 */           debug1.restoreFrom(debug16);
/* 197 */           debug1.moveTo(debug3, debug5, debug7, debug14, debug15);
/* 198 */           debug1.setYHeadRot(debug14);
/* 199 */           debug2.addFromAnotherDimension(debug1);
/*     */           
/* 201 */           debug16.removed = true;
/*     */         } else {
/*     */           return;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 208 */     if (debug12 != null) {
/* 209 */       debug12.perform(debug0, debug1);
/*     */     }
/*     */     
/* 212 */     if (!(debug1 instanceof LivingEntity) || !((LivingEntity)debug1).isFallFlying()) {
/* 213 */       debug1.setDeltaMovement(debug1.getDeltaMovement().multiply(1.0D, 0.0D, 1.0D));
/* 214 */       debug1.setOnGround(true);
/*     */     } 
/*     */     
/* 217 */     if (debug1 instanceof PathfinderMob)
/* 218 */       ((PathfinderMob)debug1).getNavigation().stop(); 
/*     */   }
/*     */   
/*     */   static class LookAt
/*     */   {
/*     */     private final Vec3 position;
/*     */     private final Entity entity;
/*     */     private final EntityAnchorArgument.Anchor anchor;
/*     */     
/*     */     public LookAt(Entity debug1, EntityAnchorArgument.Anchor debug2) {
/* 228 */       this.entity = debug1;
/* 229 */       this.anchor = debug2;
/* 230 */       this.position = debug2.apply(debug1);
/*     */     }
/*     */     
/*     */     public LookAt(Vec3 debug1) {
/* 234 */       this.entity = null;
/* 235 */       this.position = debug1;
/* 236 */       this.anchor = null;
/*     */     }
/*     */     
/*     */     public void perform(CommandSourceStack debug1, Entity debug2) {
/* 240 */       if (this.entity != null) {
/* 241 */         if (debug2 instanceof ServerPlayer) {
/* 242 */           ((ServerPlayer)debug2).lookAt(debug1.getAnchor(), this.entity, this.anchor);
/*     */         } else {
/* 244 */           debug2.lookAt(debug1.getAnchor(), this.position);
/*     */         } 
/*     */       } else {
/* 247 */         debug2.lookAt(debug1.getAnchor(), this.position);
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\commands\TeleportCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */