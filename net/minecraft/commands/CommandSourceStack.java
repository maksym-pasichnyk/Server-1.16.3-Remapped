/*     */ package net.minecraft.commands;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.brigadier.Message;
/*     */ import com.mojang.brigadier.ResultConsumer;
/*     */ import com.mojang.brigadier.context.CommandContext;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*     */ import com.mojang.brigadier.suggestion.Suggestions;
/*     */ import com.mojang.brigadier.suggestion.SuggestionsBuilder;
/*     */ import java.util.Collection;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.function.BinaryOperator;
/*     */ import java.util.stream.Stream;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.Util;
/*     */ import net.minecraft.commands.arguments.EntityAnchorArgument;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.RegistryAccess;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.network.chat.TextComponent;
/*     */ import net.minecraft.network.chat.TranslatableComponent;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.resources.ResourceLocation;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.level.GameRules;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.dimension.DimensionType;
/*     */ import net.minecraft.world.phys.Vec2;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class CommandSourceStack implements SharedSuggestionProvider {
/*  39 */   public static final SimpleCommandExceptionType ERROR_NOT_PLAYER = new SimpleCommandExceptionType((Message)new TranslatableComponent("permissions.requires.player"));
/*  40 */   public static final SimpleCommandExceptionType ERROR_NOT_ENTITY = new SimpleCommandExceptionType((Message)new TranslatableComponent("permissions.requires.entity"));
/*     */   
/*     */   private final CommandSource source;
/*     */   private final Vec3 worldPosition;
/*     */   private final ServerLevel level;
/*     */   private final int permissionLevel;
/*     */   private final String textName;
/*     */   private final Component displayName;
/*     */   private final MinecraftServer server;
/*     */   private final boolean silent;
/*     */   @Nullable
/*     */   private final Entity entity;
/*     */   private final ResultConsumer<CommandSourceStack> consumer;
/*     */   private final EntityAnchorArgument.Anchor anchor;
/*     */   private final Vec2 rotation;
/*     */   
/*     */   public CommandSourceStack(CommandSource debug1, Vec3 debug2, Vec2 debug3, ServerLevel debug4, int debug5, String debug6, Component debug7, MinecraftServer debug8, @Nullable Entity debug9) {
/*  57 */     this(debug1, debug2, debug3, debug4, debug5, debug6, debug7, debug8, debug9, false, (debug0, debug1, debug2) -> {  }EntityAnchorArgument.Anchor.FEET);
/*     */   }
/*     */   
/*     */   protected CommandSourceStack(CommandSource debug1, Vec3 debug2, Vec2 debug3, ServerLevel debug4, int debug5, String debug6, Component debug7, MinecraftServer debug8, @Nullable Entity debug9, boolean debug10, ResultConsumer<CommandSourceStack> debug11, EntityAnchorArgument.Anchor debug12) {
/*  61 */     this.source = debug1;
/*  62 */     this.worldPosition = debug2;
/*  63 */     this.level = debug4;
/*  64 */     this.silent = debug10;
/*  65 */     this.entity = debug9;
/*  66 */     this.permissionLevel = debug5;
/*  67 */     this.textName = debug6;
/*  68 */     this.displayName = debug7;
/*  69 */     this.server = debug8;
/*  70 */     this.consumer = debug11;
/*  71 */     this.anchor = debug12;
/*  72 */     this.rotation = debug3;
/*     */   }
/*     */   
/*     */   public CommandSourceStack withEntity(Entity debug1) {
/*  76 */     if (this.entity == debug1) {
/*  77 */       return this;
/*     */     }
/*  79 */     return new CommandSourceStack(this.source, this.worldPosition, this.rotation, this.level, this.permissionLevel, debug1.getName().getString(), debug1.getDisplayName(), this.server, debug1, this.silent, this.consumer, this.anchor);
/*     */   }
/*     */   
/*     */   public CommandSourceStack withPosition(Vec3 debug1) {
/*  83 */     if (this.worldPosition.equals(debug1)) {
/*  84 */       return this;
/*     */     }
/*  86 */     return new CommandSourceStack(this.source, debug1, this.rotation, this.level, this.permissionLevel, this.textName, this.displayName, this.server, this.entity, this.silent, this.consumer, this.anchor);
/*     */   }
/*     */   
/*     */   public CommandSourceStack withRotation(Vec2 debug1) {
/*  90 */     if (this.rotation.equals(debug1)) {
/*  91 */       return this;
/*     */     }
/*  93 */     return new CommandSourceStack(this.source, this.worldPosition, debug1, this.level, this.permissionLevel, this.textName, this.displayName, this.server, this.entity, this.silent, this.consumer, this.anchor);
/*     */   }
/*     */   
/*     */   public CommandSourceStack withCallback(ResultConsumer<CommandSourceStack> debug1) {
/*  97 */     if (this.consumer.equals(debug1)) {
/*  98 */       return this;
/*     */     }
/* 100 */     return new CommandSourceStack(this.source, this.worldPosition, this.rotation, this.level, this.permissionLevel, this.textName, this.displayName, this.server, this.entity, this.silent, debug1, this.anchor);
/*     */   }
/*     */   
/*     */   public CommandSourceStack withCallback(ResultConsumer<CommandSourceStack> debug1, BinaryOperator<ResultConsumer<CommandSourceStack>> debug2) {
/* 104 */     ResultConsumer<CommandSourceStack> debug3 = debug2.apply(this.consumer, debug1);
/* 105 */     return withCallback(debug3);
/*     */   }
/*     */   
/*     */   public CommandSourceStack withSuppressedOutput() {
/* 109 */     if (this.silent) {
/* 110 */       return this;
/*     */     }
/* 112 */     return new CommandSourceStack(this.source, this.worldPosition, this.rotation, this.level, this.permissionLevel, this.textName, this.displayName, this.server, this.entity, true, this.consumer, this.anchor);
/*     */   }
/*     */   
/*     */   public CommandSourceStack withPermission(int debug1) {
/* 116 */     if (debug1 == this.permissionLevel) {
/* 117 */       return this;
/*     */     }
/* 119 */     return new CommandSourceStack(this.source, this.worldPosition, this.rotation, this.level, debug1, this.textName, this.displayName, this.server, this.entity, this.silent, this.consumer, this.anchor);
/*     */   }
/*     */   
/*     */   public CommandSourceStack withMaximumPermission(int debug1) {
/* 123 */     if (debug1 <= this.permissionLevel) {
/* 124 */       return this;
/*     */     }
/* 126 */     return new CommandSourceStack(this.source, this.worldPosition, this.rotation, this.level, debug1, this.textName, this.displayName, this.server, this.entity, this.silent, this.consumer, this.anchor);
/*     */   }
/*     */   
/*     */   public CommandSourceStack withAnchor(EntityAnchorArgument.Anchor debug1) {
/* 130 */     if (debug1 == this.anchor) {
/* 131 */       return this;
/*     */     }
/* 133 */     return new CommandSourceStack(this.source, this.worldPosition, this.rotation, this.level, this.permissionLevel, this.textName, this.displayName, this.server, this.entity, this.silent, this.consumer, debug1);
/*     */   }
/*     */   
/*     */   public CommandSourceStack withLevel(ServerLevel debug1) {
/* 137 */     if (debug1 == this.level) {
/* 138 */       return this;
/*     */     }
/* 140 */     double debug2 = DimensionType.getTeleportationScale(this.level.dimensionType(), debug1.dimensionType());
/* 141 */     Vec3 debug4 = new Vec3(this.worldPosition.x * debug2, this.worldPosition.y, this.worldPosition.z * debug2);
/* 142 */     return new CommandSourceStack(this.source, debug4, this.rotation, debug1, this.permissionLevel, this.textName, this.displayName, this.server, this.entity, this.silent, this.consumer, this.anchor);
/*     */   }
/*     */   
/*     */   public CommandSourceStack facing(Entity debug1, EntityAnchorArgument.Anchor debug2) throws CommandSyntaxException {
/* 146 */     return facing(debug2.apply(debug1));
/*     */   }
/*     */   
/*     */   public CommandSourceStack facing(Vec3 debug1) throws CommandSyntaxException {
/* 150 */     Vec3 debug2 = this.anchor.apply(this);
/* 151 */     double debug3 = debug1.x - debug2.x;
/* 152 */     double debug5 = debug1.y - debug2.y;
/* 153 */     double debug7 = debug1.z - debug2.z;
/* 154 */     double debug9 = Mth.sqrt(debug3 * debug3 + debug7 * debug7);
/*     */     
/* 156 */     float debug11 = Mth.wrapDegrees((float)-(Mth.atan2(debug5, debug9) * 57.2957763671875D));
/* 157 */     float debug12 = Mth.wrapDegrees((float)(Mth.atan2(debug7, debug3) * 57.2957763671875D) - 90.0F);
/* 158 */     return withRotation(new Vec2(debug11, debug12));
/*     */   }
/*     */   
/*     */   public Component getDisplayName() {
/* 162 */     return this.displayName;
/*     */   }
/*     */   
/*     */   public String getTextName() {
/* 166 */     return this.textName;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasPermission(int debug1) {
/* 171 */     return (this.permissionLevel >= debug1);
/*     */   }
/*     */   
/*     */   public Vec3 getPosition() {
/* 175 */     return this.worldPosition;
/*     */   }
/*     */   
/*     */   public ServerLevel getLevel() {
/* 179 */     return this.level;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public Entity getEntity() {
/* 184 */     return this.entity;
/*     */   }
/*     */   
/*     */   public Entity getEntityOrException() throws CommandSyntaxException {
/* 188 */     if (this.entity == null) {
/* 189 */       throw ERROR_NOT_ENTITY.create();
/*     */     }
/* 191 */     return this.entity;
/*     */   }
/*     */   
/*     */   public ServerPlayer getPlayerOrException() throws CommandSyntaxException {
/* 195 */     if (!(this.entity instanceof ServerPlayer)) {
/* 196 */       throw ERROR_NOT_PLAYER.create();
/*     */     }
/* 198 */     return (ServerPlayer)this.entity;
/*     */   }
/*     */   
/*     */   public Vec2 getRotation() {
/* 202 */     return this.rotation;
/*     */   }
/*     */   
/*     */   public MinecraftServer getServer() {
/* 206 */     return this.server;
/*     */   }
/*     */   
/*     */   public EntityAnchorArgument.Anchor getAnchor() {
/* 210 */     return this.anchor;
/*     */   }
/*     */   
/*     */   public void sendSuccess(Component debug1, boolean debug2) {
/* 214 */     if (this.source.acceptsSuccess() && !this.silent) {
/* 215 */       this.source.sendMessage(debug1, Util.NIL_UUID);
/*     */     }
/* 217 */     if (debug2 && this.source.shouldInformAdmins() && !this.silent) {
/* 218 */       broadcastToAdmins(debug1);
/*     */     }
/*     */   }
/*     */   
/*     */   private void broadcastToAdmins(Component debug1) {
/* 223 */     MutableComponent mutableComponent = (new TranslatableComponent("chat.type.admin", new Object[] { getDisplayName(), debug1 })).withStyle(new ChatFormatting[] { ChatFormatting.GRAY, ChatFormatting.ITALIC });
/*     */     
/* 225 */     if (this.server.getGameRules().getBoolean(GameRules.RULE_SENDCOMMANDFEEDBACK)) {
/* 226 */       for (ServerPlayer debug4 : this.server.getPlayerList().getPlayers()) {
/* 227 */         if (debug4 != this.source && this.server.getPlayerList().isOp(debug4.getGameProfile())) {
/* 228 */           debug4.sendMessage((Component)mutableComponent, Util.NIL_UUID);
/*     */         }
/*     */       } 
/*     */     }
/*     */     
/* 233 */     if (this.source != this.server && this.server.getGameRules().getBoolean(GameRules.RULE_LOGADMINCOMMANDS)) {
/* 234 */       this.server.sendMessage((Component)mutableComponent, Util.NIL_UUID);
/*     */     }
/*     */   }
/*     */   
/*     */   public void sendFailure(Component debug1) {
/* 239 */     if (this.source.acceptsFailure() && !this.silent) {
/* 240 */       this.source.sendMessage((Component)(new TextComponent("")).append(debug1).withStyle(ChatFormatting.RED), Util.NIL_UUID);
/*     */     }
/*     */   }
/*     */   
/*     */   public void onCommandComplete(CommandContext<CommandSourceStack> debug1, boolean debug2, int debug3) {
/* 245 */     if (this.consumer != null) {
/* 246 */       this.consumer.onCommandComplete(debug1, debug2, debug3);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public Collection<String> getOnlinePlayerNames() {
/* 252 */     return Lists.newArrayList((Object[])this.server.getPlayerNames());
/*     */   }
/*     */ 
/*     */   
/*     */   public Collection<String> getAllTeams() {
/* 257 */     return this.server.getScoreboard().getTeamNames();
/*     */   }
/*     */ 
/*     */   
/*     */   public Collection<ResourceLocation> getAvailableSoundEvents() {
/* 262 */     return Registry.SOUND_EVENT.keySet();
/*     */   }
/*     */ 
/*     */   
/*     */   public Stream<ResourceLocation> getRecipeNames() {
/* 267 */     return this.server.getRecipeManager().getRecipeIds();
/*     */   }
/*     */ 
/*     */   
/*     */   public CompletableFuture<Suggestions> customSuggestion(CommandContext<SharedSuggestionProvider> debug1, SuggestionsBuilder debug2) {
/* 272 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<ResourceKey<Level>> levels() {
/* 277 */     return this.server.levelKeys();
/*     */   }
/*     */ 
/*     */   
/*     */   public RegistryAccess registryAccess() {
/* 282 */     return this.server.registryAccess();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\commands\CommandSourceStack.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */