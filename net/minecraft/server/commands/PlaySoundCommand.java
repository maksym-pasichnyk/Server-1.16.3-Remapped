/*     */ package net.minecraft.server.commands;
/*     */ 
/*     */ import com.mojang.brigadier.CommandDispatcher;
/*     */ import com.mojang.brigadier.Message;
/*     */ import com.mojang.brigadier.arguments.ArgumentType;
/*     */ import com.mojang.brigadier.arguments.FloatArgumentType;
/*     */ import com.mojang.brigadier.builder.ArgumentBuilder;
/*     */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*     */ import com.mojang.brigadier.builder.RequiredArgumentBuilder;
/*     */ import com.mojang.brigadier.context.CommandContext;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*     */ import java.util.Collection;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.commands.Commands;
/*     */ import net.minecraft.commands.arguments.EntityArgument;
/*     */ import net.minecraft.commands.arguments.ResourceLocationArgument;
/*     */ import net.minecraft.commands.arguments.coordinates.Vec3Argument;
/*     */ import net.minecraft.commands.synchronization.SuggestionProviders;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.TranslatableComponent;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ClientboundCustomSoundPacket;
/*     */ import net.minecraft.resources.ResourceLocation;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class PlaySoundCommand
/*     */ {
/*  32 */   private static final SimpleCommandExceptionType ERROR_TOO_FAR = new SimpleCommandExceptionType((Message)new TranslatableComponent("commands.playsound.failed"));
/*     */   
/*     */   public static void register(CommandDispatcher<CommandSourceStack> debug0) {
/*  35 */     RequiredArgumentBuilder<CommandSourceStack, ResourceLocation> debug1 = Commands.argument("sound", (ArgumentType)ResourceLocationArgument.id()).suggests(SuggestionProviders.AVAILABLE_SOUNDS);
/*     */     
/*  37 */     for (SoundSource debug5 : SoundSource.values()) {
/*  38 */       debug1.then((ArgumentBuilder)source(debug5));
/*     */     }
/*     */     
/*  41 */     debug0.register(
/*  42 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("playsound")
/*  43 */         .requires(debug0 -> debug0.hasPermission(2)))
/*  44 */         .then((ArgumentBuilder)debug1));
/*     */   }
/*     */ 
/*     */   
/*     */   private static LiteralArgumentBuilder<CommandSourceStack> source(SoundSource debug0) {
/*  49 */     return (LiteralArgumentBuilder<CommandSourceStack>)Commands.literal(debug0.getName())
/*  50 */       .then((
/*  51 */         (RequiredArgumentBuilder)Commands.argument("targets", (ArgumentType)EntityArgument.players())
/*  52 */         .executes(debug1 -> playSound((CommandSourceStack)debug1.getSource(), EntityArgument.getPlayers(debug1, "targets"), ResourceLocationArgument.getId(debug1, "sound"), debug0, ((CommandSourceStack)debug1.getSource()).getPosition(), 1.0F, 1.0F, 0.0F)))
/*  53 */         .then((
/*  54 */           (RequiredArgumentBuilder)Commands.argument("pos", (ArgumentType)Vec3Argument.vec3())
/*  55 */           .executes(debug1 -> playSound((CommandSourceStack)debug1.getSource(), EntityArgument.getPlayers(debug1, "targets"), ResourceLocationArgument.getId(debug1, "sound"), debug0, Vec3Argument.getVec3(debug1, "pos"), 1.0F, 1.0F, 0.0F)))
/*  56 */           .then((
/*  57 */             (RequiredArgumentBuilder)Commands.argument("volume", (ArgumentType)FloatArgumentType.floatArg(0.0F))
/*  58 */             .executes(debug1 -> playSound((CommandSourceStack)debug1.getSource(), EntityArgument.getPlayers(debug1, "targets"), ResourceLocationArgument.getId(debug1, "sound"), debug0, Vec3Argument.getVec3(debug1, "pos"), ((Float)debug1.getArgument("volume", Float.class)).floatValue(), 1.0F, 0.0F)))
/*  59 */             .then((
/*  60 */               (RequiredArgumentBuilder)Commands.argument("pitch", (ArgumentType)FloatArgumentType.floatArg(0.0F, 2.0F))
/*  61 */               .executes(debug1 -> playSound((CommandSourceStack)debug1.getSource(), EntityArgument.getPlayers(debug1, "targets"), ResourceLocationArgument.getId(debug1, "sound"), debug0, Vec3Argument.getVec3(debug1, "pos"), ((Float)debug1.getArgument("volume", Float.class)).floatValue(), ((Float)debug1.getArgument("pitch", Float.class)).floatValue(), 0.0F)))
/*  62 */               .then(
/*  63 */                 Commands.argument("minVolume", (ArgumentType)FloatArgumentType.floatArg(0.0F, 1.0F))
/*  64 */                 .executes(debug1 -> playSound((CommandSourceStack)debug1.getSource(), EntityArgument.getPlayers(debug1, "targets"), ResourceLocationArgument.getId(debug1, "sound"), debug0, Vec3Argument.getVec3(debug1, "pos"), ((Float)debug1.getArgument("volume", Float.class)).floatValue(), ((Float)debug1.getArgument("pitch", Float.class)).floatValue(), ((Float)debug1.getArgument("minVolume", Float.class)).floatValue())))))));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int playSound(CommandSourceStack debug0, Collection<ServerPlayer> debug1, ResourceLocation debug2, SoundSource debug3, Vec3 debug4, float debug5, float debug6, float debug7) throws CommandSyntaxException {
/*  73 */     double debug8 = Math.pow((debug5 > 1.0F) ? (debug5 * 16.0F) : 16.0D, 2.0D);
/*  74 */     int debug10 = 0;
/*     */     
/*  76 */     for (ServerPlayer debug12 : debug1) {
/*  77 */       double debug13 = debug4.x - debug12.getX();
/*  78 */       double debug15 = debug4.y - debug12.getY();
/*  79 */       double debug17 = debug4.z - debug12.getZ();
/*  80 */       double debug19 = debug13 * debug13 + debug15 * debug15 + debug17 * debug17;
/*  81 */       Vec3 debug21 = debug4;
/*  82 */       float debug22 = debug5;
/*     */       
/*  84 */       if (debug19 > debug8) {
/*  85 */         if (debug7 <= 0.0F) {
/*     */           continue;
/*     */         }
/*     */         
/*  89 */         double debug23 = Mth.sqrt(debug19);
/*  90 */         debug21 = new Vec3(debug12.getX() + debug13 / debug23 * 2.0D, debug12.getY() + debug15 / debug23 * 2.0D, debug12.getZ() + debug17 / debug23 * 2.0D);
/*  91 */         debug22 = debug7;
/*     */       } 
/*     */       
/*  94 */       debug12.connection.send((Packet)new ClientboundCustomSoundPacket(debug2, debug3, debug21, debug22, debug6));
/*  95 */       debug10++;
/*     */     } 
/*     */     
/*  98 */     if (debug10 == 0) {
/*  99 */       throw ERROR_TOO_FAR.create();
/*     */     }
/*     */     
/* 102 */     if (debug1.size() == 1) {
/* 103 */       debug0.sendSuccess((Component)new TranslatableComponent("commands.playsound.success.single", new Object[] { debug2, ((ServerPlayer)debug1.iterator().next()).getDisplayName() }), true);
/*     */     } else {
/* 105 */       debug0.sendSuccess((Component)new TranslatableComponent("commands.playsound.success.multiple", new Object[] { debug2, Integer.valueOf(debug1.size()) }), true);
/*     */     } 
/*     */     
/* 108 */     return debug10;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\commands\PlaySoundCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */