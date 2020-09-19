/*     */ package net.minecraft.server.commands;
/*     */ 
/*     */ import com.mojang.brigadier.CommandDispatcher;
/*     */ import com.mojang.brigadier.Message;
/*     */ import com.mojang.brigadier.arguments.ArgumentType;
/*     */ import com.mojang.brigadier.arguments.IntegerArgumentType;
/*     */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*     */ import com.mojang.brigadier.builder.RequiredArgumentBuilder;
/*     */ import com.mojang.brigadier.context.CommandContext;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*     */ import com.mojang.brigadier.tree.CommandNode;
/*     */ import com.mojang.brigadier.tree.LiteralCommandNode;
/*     */ import java.util.Collection;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.BiPredicate;
/*     */ import java.util.function.ToIntFunction;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.commands.Commands;
/*     */ import net.minecraft.commands.arguments.EntityArgument;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.TranslatableComponent;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ 
/*     */ 
/*     */ public class ExperienceCommand
/*     */ {
/*  30 */   private static final SimpleCommandExceptionType ERROR_SET_POINTS_INVALID = new SimpleCommandExceptionType((Message)new TranslatableComponent("commands.experience.set.points.invalid"));
/*     */   
/*     */   public static void register(CommandDispatcher<CommandSourceStack> debug0) {
/*  33 */     LiteralCommandNode<CommandSourceStack> debug1 = debug0.register(
/*  34 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("experience")
/*  35 */         .requires(debug0 -> debug0.hasPermission(2)))
/*  36 */         .then(
/*  37 */           Commands.literal("add")
/*  38 */           .then(
/*  39 */             Commands.argument("targets", (ArgumentType)EntityArgument.players())
/*  40 */             .then((
/*  41 */               (RequiredArgumentBuilder)((RequiredArgumentBuilder)Commands.argument("amount", (ArgumentType)IntegerArgumentType.integer())
/*  42 */               .executes(debug0 -> addExperience((CommandSourceStack)debug0.getSource(), EntityArgument.getPlayers(debug0, "targets"), IntegerArgumentType.getInteger(debug0, "amount"), Type.POINTS)))
/*  43 */               .then(
/*  44 */                 Commands.literal("points")
/*  45 */                 .executes(debug0 -> addExperience((CommandSourceStack)debug0.getSource(), EntityArgument.getPlayers(debug0, "targets"), IntegerArgumentType.getInteger(debug0, "amount"), Type.POINTS))))
/*     */               
/*  47 */               .then(
/*  48 */                 Commands.literal("levels")
/*  49 */                 .executes(debug0 -> addExperience((CommandSourceStack)debug0.getSource(), EntityArgument.getPlayers(debug0, "targets"), IntegerArgumentType.getInteger(debug0, "amount"), Type.LEVELS)))))))
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*  54 */         .then(
/*  55 */           Commands.literal("set")
/*  56 */           .then(
/*  57 */             Commands.argument("targets", (ArgumentType)EntityArgument.players())
/*  58 */             .then((
/*  59 */               (RequiredArgumentBuilder)((RequiredArgumentBuilder)Commands.argument("amount", (ArgumentType)IntegerArgumentType.integer(0))
/*  60 */               .executes(debug0 -> setExperience((CommandSourceStack)debug0.getSource(), EntityArgument.getPlayers(debug0, "targets"), IntegerArgumentType.getInteger(debug0, "amount"), Type.POINTS)))
/*  61 */               .then(
/*  62 */                 Commands.literal("points")
/*  63 */                 .executes(debug0 -> setExperience((CommandSourceStack)debug0.getSource(), EntityArgument.getPlayers(debug0, "targets"), IntegerArgumentType.getInteger(debug0, "amount"), Type.POINTS))))
/*     */               
/*  65 */               .then(
/*  66 */                 Commands.literal("levels")
/*  67 */                 .executes(debug0 -> setExperience((CommandSourceStack)debug0.getSource(), EntityArgument.getPlayers(debug0, "targets"), IntegerArgumentType.getInteger(debug0, "amount"), Type.LEVELS)))))))
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*  72 */         .then(
/*  73 */           Commands.literal("query")
/*  74 */           .then((
/*  75 */             (RequiredArgumentBuilder)Commands.argument("targets", (ArgumentType)EntityArgument.player())
/*  76 */             .then(
/*  77 */               Commands.literal("points")
/*  78 */               .executes(debug0 -> queryExperience((CommandSourceStack)debug0.getSource(), EntityArgument.getPlayer(debug0, "targets"), Type.POINTS))))
/*     */             
/*  80 */             .then(
/*  81 */               Commands.literal("levels")
/*  82 */               .executes(debug0 -> queryExperience((CommandSourceStack)debug0.getSource(), EntityArgument.getPlayer(debug0, "targets"), Type.LEVELS))))));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  88 */     debug0.register(
/*  89 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("xp")
/*  90 */         .requires(debug0 -> debug0.hasPermission(2)))
/*  91 */         .redirect((CommandNode)debug1));
/*     */   }
/*     */ 
/*     */   
/*     */   private static int queryExperience(CommandSourceStack debug0, ServerPlayer debug1, Type debug2) {
/*  96 */     int debug3 = debug2.query.applyAsInt(debug1);
/*  97 */     debug0.sendSuccess((Component)new TranslatableComponent("commands.experience.query." + debug2.name, new Object[] { debug1.getDisplayName(), Integer.valueOf(debug3) }), false);
/*  98 */     return debug3;
/*     */   }
/*     */   
/*     */   private static int addExperience(CommandSourceStack debug0, Collection<? extends ServerPlayer> debug1, int debug2, Type debug3) {
/* 102 */     for (ServerPlayer debug5 : debug1) {
/* 103 */       debug3.add.accept(debug5, Integer.valueOf(debug2));
/*     */     }
/*     */     
/* 106 */     if (debug1.size() == 1) {
/* 107 */       debug0.sendSuccess((Component)new TranslatableComponent("commands.experience.add." + debug3.name + ".success.single", new Object[] { Integer.valueOf(debug2), ((ServerPlayer)debug1.iterator().next()).getDisplayName() }), true);
/*     */     } else {
/* 109 */       debug0.sendSuccess((Component)new TranslatableComponent("commands.experience.add." + debug3.name + ".success.multiple", new Object[] { Integer.valueOf(debug2), Integer.valueOf(debug1.size()) }), true);
/*     */     } 
/*     */     
/* 112 */     return debug1.size();
/*     */   }
/*     */   
/*     */   private static int setExperience(CommandSourceStack debug0, Collection<? extends ServerPlayer> debug1, int debug2, Type debug3) throws CommandSyntaxException {
/* 116 */     int debug4 = 0;
/*     */     
/* 118 */     for (ServerPlayer debug6 : debug1) {
/* 119 */       if (debug3.set.test(debug6, Integer.valueOf(debug2))) {
/* 120 */         debug4++;
/*     */       }
/*     */     } 
/*     */     
/* 124 */     if (debug4 == 0) {
/* 125 */       throw ERROR_SET_POINTS_INVALID.create();
/*     */     }
/*     */     
/* 128 */     if (debug1.size() == 1) {
/* 129 */       debug0.sendSuccess((Component)new TranslatableComponent("commands.experience.set." + debug3.name + ".success.single", new Object[] { Integer.valueOf(debug2), ((ServerPlayer)debug1.iterator().next()).getDisplayName() }), true);
/*     */     } else {
/* 131 */       debug0.sendSuccess((Component)new TranslatableComponent("commands.experience.set." + debug3.name + ".success.multiple", new Object[] { Integer.valueOf(debug2), Integer.valueOf(debug1.size()) }), true);
/*     */     } 
/*     */     
/* 134 */     return debug1.size();
/*     */   }
/*     */   enum Type { POINTS, LEVELS;
/*     */     static {
/* 138 */       POINTS = new Type("POINTS", 0, "points", Player::giveExperiencePoints, (debug0, debug1) -> {
/*     */             if (debug1.intValue() >= debug0.getXpNeededForNextLevel()) {
/*     */               return false;
/*     */             }
/*     */             debug0.setExperiencePoints(debug1.intValue());
/*     */             return true;
/*     */           }debug0 -> Mth.floor(debug0.experienceProgress * debug0.getXpNeededForNextLevel()));
/* 145 */       LEVELS = new Type("LEVELS", 1, "levels", ServerPlayer::giveExperienceLevels, (debug0, debug1) -> {
/*     */             debug0.setExperienceLevels(debug1.intValue());
/*     */             return true;
/*     */           }debug0 -> debug0.experienceLevel);
/*     */     }
/*     */     public final BiConsumer<ServerPlayer, Integer> add;
/*     */     public final BiPredicate<ServerPlayer, Integer> set;
/*     */     public final String name;
/*     */     private final ToIntFunction<ServerPlayer> query;
/*     */     
/*     */     Type(String debug3, BiConsumer<ServerPlayer, Integer> debug4, BiPredicate<ServerPlayer, Integer> debug5, ToIntFunction<ServerPlayer> debug6) {
/* 156 */       this.add = debug4;
/* 157 */       this.name = debug3;
/* 158 */       this.set = debug5;
/* 159 */       this.query = debug6;
/*     */     } }
/*     */ 
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\commands\ExperienceCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */