/*     */ package net.minecraft.server.commands;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.brigadier.CommandDispatcher;
/*     */ import com.mojang.brigadier.Message;
/*     */ import com.mojang.brigadier.arguments.ArgumentType;
/*     */ import com.mojang.brigadier.arguments.BoolArgumentType;
/*     */ import com.mojang.brigadier.arguments.StringArgumentType;
/*     */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*     */ import com.mojang.brigadier.builder.RequiredArgumentBuilder;
/*     */ import com.mojang.brigadier.context.CommandContext;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
/*     */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.commands.Commands;
/*     */ import net.minecraft.commands.arguments.ColorArgument;
/*     */ import net.minecraft.commands.arguments.ComponentArgument;
/*     */ import net.minecraft.commands.arguments.ScoreHolderArgument;
/*     */ import net.minecraft.commands.arguments.TeamArgument;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.ComponentUtils;
/*     */ import net.minecraft.network.chat.TextComponent;
/*     */ import net.minecraft.network.chat.TranslatableComponent;
/*     */ import net.minecraft.server.ServerScoreboard;
/*     */ import net.minecraft.world.scores.PlayerTeam;
/*     */ import net.minecraft.world.scores.Team;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TeamCommand
/*     */ {
/*     */   private static final DynamicCommandExceptionType ERROR_TEAM_NAME_TOO_LONG;
/*  39 */   private static final SimpleCommandExceptionType ERROR_TEAM_ALREADY_EXISTS = new SimpleCommandExceptionType((Message)new TranslatableComponent("commands.team.add.duplicate")); static {
/*  40 */     ERROR_TEAM_NAME_TOO_LONG = new DynamicCommandExceptionType(debug0 -> new TranslatableComponent("commands.team.add.longName", new Object[] { debug0 }));
/*  41 */   } private static final SimpleCommandExceptionType ERROR_TEAM_ALREADY_EMPTY = new SimpleCommandExceptionType((Message)new TranslatableComponent("commands.team.empty.unchanged"));
/*  42 */   private static final SimpleCommandExceptionType ERROR_TEAM_ALREADY_NAME = new SimpleCommandExceptionType((Message)new TranslatableComponent("commands.team.option.name.unchanged"));
/*  43 */   private static final SimpleCommandExceptionType ERROR_TEAM_ALREADY_COLOR = new SimpleCommandExceptionType((Message)new TranslatableComponent("commands.team.option.color.unchanged"));
/*  44 */   private static final SimpleCommandExceptionType ERROR_TEAM_ALREADY_FRIENDLYFIRE_ENABLED = new SimpleCommandExceptionType((Message)new TranslatableComponent("commands.team.option.friendlyfire.alreadyEnabled"));
/*  45 */   private static final SimpleCommandExceptionType ERROR_TEAM_ALREADY_FRIENDLYFIRE_DISABLED = new SimpleCommandExceptionType((Message)new TranslatableComponent("commands.team.option.friendlyfire.alreadyDisabled"));
/*  46 */   private static final SimpleCommandExceptionType ERROR_TEAM_ALREADY_FRIENDLYINVISIBLES_ENABLED = new SimpleCommandExceptionType((Message)new TranslatableComponent("commands.team.option.seeFriendlyInvisibles.alreadyEnabled"));
/*  47 */   private static final SimpleCommandExceptionType ERROR_TEAM_ALREADY_FRIENDLYINVISIBLES_DISABLED = new SimpleCommandExceptionType((Message)new TranslatableComponent("commands.team.option.seeFriendlyInvisibles.alreadyDisabled"));
/*  48 */   private static final SimpleCommandExceptionType ERROR_TEAM_NAMETAG_VISIBLITY_UNCHANGED = new SimpleCommandExceptionType((Message)new TranslatableComponent("commands.team.option.nametagVisibility.unchanged"));
/*  49 */   private static final SimpleCommandExceptionType ERROR_TEAM_DEATH_MESSAGE_VISIBLITY_UNCHANGED = new SimpleCommandExceptionType((Message)new TranslatableComponent("commands.team.option.deathMessageVisibility.unchanged"));
/*  50 */   private static final SimpleCommandExceptionType ERROR_TEAM_COLLISION_UNCHANGED = new SimpleCommandExceptionType((Message)new TranslatableComponent("commands.team.option.collisionRule.unchanged"));
/*     */   
/*     */   public static void register(CommandDispatcher<CommandSourceStack> debug0) {
/*  53 */     debug0.register(
/*  54 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("team")
/*  55 */         .requires(debug0 -> debug0.hasPermission(2)))
/*  56 */         .then((
/*  57 */           (LiteralArgumentBuilder)Commands.literal("list")
/*  58 */           .executes(debug0 -> listTeams((CommandSourceStack)debug0.getSource())))
/*  59 */           .then(
/*  60 */             Commands.argument("team", (ArgumentType)TeamArgument.team())
/*  61 */             .executes(debug0 -> listMembers((CommandSourceStack)debug0.getSource(), TeamArgument.getTeam(debug0, "team"))))))
/*     */ 
/*     */         
/*  64 */         .then(
/*  65 */           Commands.literal("add")
/*  66 */           .then((
/*  67 */             (RequiredArgumentBuilder)Commands.argument("team", (ArgumentType)StringArgumentType.word())
/*  68 */             .executes(debug0 -> createTeam((CommandSourceStack)debug0.getSource(), StringArgumentType.getString(debug0, "team"))))
/*  69 */             .then(
/*  70 */               Commands.argument("displayName", (ArgumentType)ComponentArgument.textComponent())
/*  71 */               .executes(debug0 -> createTeam((CommandSourceStack)debug0.getSource(), StringArgumentType.getString(debug0, "team"), ComponentArgument.getComponent(debug0, "displayName")))))))
/*     */ 
/*     */ 
/*     */         
/*  75 */         .then(
/*  76 */           Commands.literal("remove")
/*  77 */           .then(
/*  78 */             Commands.argument("team", (ArgumentType)TeamArgument.team())
/*  79 */             .executes(debug0 -> deleteTeam((CommandSourceStack)debug0.getSource(), TeamArgument.getTeam(debug0, "team"))))))
/*     */ 
/*     */         
/*  82 */         .then(
/*  83 */           Commands.literal("empty")
/*  84 */           .then(
/*  85 */             Commands.argument("team", (ArgumentType)TeamArgument.team())
/*  86 */             .executes(debug0 -> emptyTeam((CommandSourceStack)debug0.getSource(), TeamArgument.getTeam(debug0, "team"))))))
/*     */ 
/*     */         
/*  89 */         .then(
/*  90 */           Commands.literal("join")
/*  91 */           .then((
/*  92 */             (RequiredArgumentBuilder)Commands.argument("team", (ArgumentType)TeamArgument.team())
/*  93 */             .executes(debug0 -> joinTeam((CommandSourceStack)debug0.getSource(), TeamArgument.getTeam(debug0, "team"), Collections.singleton(((CommandSourceStack)debug0.getSource()).getEntityOrException().getScoreboardName()))))
/*  94 */             .then(
/*  95 */               Commands.argument("members", (ArgumentType)ScoreHolderArgument.scoreHolders())
/*  96 */               .suggests(ScoreHolderArgument.SUGGEST_SCORE_HOLDERS)
/*  97 */               .executes(debug0 -> joinTeam((CommandSourceStack)debug0.getSource(), TeamArgument.getTeam(debug0, "team"), ScoreHolderArgument.getNamesWithDefaultWildcard(debug0, "members")))))))
/*     */ 
/*     */ 
/*     */         
/* 101 */         .then(
/* 102 */           Commands.literal("leave")
/* 103 */           .then(
/* 104 */             Commands.argument("members", (ArgumentType)ScoreHolderArgument.scoreHolders())
/* 105 */             .suggests(ScoreHolderArgument.SUGGEST_SCORE_HOLDERS)
/* 106 */             .executes(debug0 -> leaveTeam((CommandSourceStack)debug0.getSource(), ScoreHolderArgument.getNamesWithDefaultWildcard(debug0, "members"))))))
/*     */ 
/*     */         
/* 109 */         .then(
/* 110 */           Commands.literal("modify")
/* 111 */           .then((
/* 112 */             (RequiredArgumentBuilder)((RequiredArgumentBuilder)((RequiredArgumentBuilder)((RequiredArgumentBuilder)((RequiredArgumentBuilder)((RequiredArgumentBuilder)((RequiredArgumentBuilder)((RequiredArgumentBuilder)Commands.argument("team", (ArgumentType)TeamArgument.team())
/* 113 */             .then(
/* 114 */               Commands.literal("displayName")
/* 115 */               .then(
/* 116 */                 Commands.argument("displayName", (ArgumentType)ComponentArgument.textComponent())
/* 117 */                 .executes(debug0 -> setDisplayName((CommandSourceStack)debug0.getSource(), TeamArgument.getTeam(debug0, "team"), ComponentArgument.getComponent(debug0, "displayName"))))))
/*     */ 
/*     */             
/* 120 */             .then(
/* 121 */               Commands.literal("color")
/* 122 */               .then(
/* 123 */                 Commands.argument("value", (ArgumentType)ColorArgument.color())
/* 124 */                 .executes(debug0 -> setColor((CommandSourceStack)debug0.getSource(), TeamArgument.getTeam(debug0, "team"), ColorArgument.getColor(debug0, "value"))))))
/*     */ 
/*     */             
/* 127 */             .then(
/* 128 */               Commands.literal("friendlyFire")
/* 129 */               .then(
/* 130 */                 Commands.argument("allowed", (ArgumentType)BoolArgumentType.bool())
/* 131 */                 .executes(debug0 -> setFriendlyFire((CommandSourceStack)debug0.getSource(), TeamArgument.getTeam(debug0, "team"), BoolArgumentType.getBool(debug0, "allowed"))))))
/*     */ 
/*     */             
/* 134 */             .then(
/* 135 */               Commands.literal("seeFriendlyInvisibles")
/* 136 */               .then(
/* 137 */                 Commands.argument("allowed", (ArgumentType)BoolArgumentType.bool())
/* 138 */                 .executes(debug0 -> setFriendlySight((CommandSourceStack)debug0.getSource(), TeamArgument.getTeam(debug0, "team"), BoolArgumentType.getBool(debug0, "allowed"))))))
/*     */ 
/*     */             
/* 141 */             .then((
/* 142 */               (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("nametagVisibility")
/* 143 */               .then(Commands.literal("never").executes(debug0 -> setNametagVisibility((CommandSourceStack)debug0.getSource(), TeamArgument.getTeam(debug0, "team"), Team.Visibility.NEVER))))
/* 144 */               .then(Commands.literal("hideForOtherTeams").executes(debug0 -> setNametagVisibility((CommandSourceStack)debug0.getSource(), TeamArgument.getTeam(debug0, "team"), Team.Visibility.HIDE_FOR_OTHER_TEAMS))))
/* 145 */               .then(Commands.literal("hideForOwnTeam").executes(debug0 -> setNametagVisibility((CommandSourceStack)debug0.getSource(), TeamArgument.getTeam(debug0, "team"), Team.Visibility.HIDE_FOR_OWN_TEAM))))
/* 146 */               .then(Commands.literal("always").executes(debug0 -> setNametagVisibility((CommandSourceStack)debug0.getSource(), TeamArgument.getTeam(debug0, "team"), Team.Visibility.ALWAYS)))))
/*     */             
/* 148 */             .then((
/* 149 */               (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("deathMessageVisibility")
/* 150 */               .then(Commands.literal("never").executes(debug0 -> setDeathMessageVisibility((CommandSourceStack)debug0.getSource(), TeamArgument.getTeam(debug0, "team"), Team.Visibility.NEVER))))
/* 151 */               .then(Commands.literal("hideForOtherTeams").executes(debug0 -> setDeathMessageVisibility((CommandSourceStack)debug0.getSource(), TeamArgument.getTeam(debug0, "team"), Team.Visibility.HIDE_FOR_OTHER_TEAMS))))
/* 152 */               .then(Commands.literal("hideForOwnTeam").executes(debug0 -> setDeathMessageVisibility((CommandSourceStack)debug0.getSource(), TeamArgument.getTeam(debug0, "team"), Team.Visibility.HIDE_FOR_OWN_TEAM))))
/* 153 */               .then(Commands.literal("always").executes(debug0 -> setDeathMessageVisibility((CommandSourceStack)debug0.getSource(), TeamArgument.getTeam(debug0, "team"), Team.Visibility.ALWAYS)))))
/*     */             
/* 155 */             .then((
/* 156 */               (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("collisionRule")
/* 157 */               .then(Commands.literal("never").executes(debug0 -> setCollision((CommandSourceStack)debug0.getSource(), TeamArgument.getTeam(debug0, "team"), Team.CollisionRule.NEVER))))
/* 158 */               .then(Commands.literal("pushOwnTeam").executes(debug0 -> setCollision((CommandSourceStack)debug0.getSource(), TeamArgument.getTeam(debug0, "team"), Team.CollisionRule.PUSH_OWN_TEAM))))
/* 159 */               .then(Commands.literal("pushOtherTeams").executes(debug0 -> setCollision((CommandSourceStack)debug0.getSource(), TeamArgument.getTeam(debug0, "team"), Team.CollisionRule.PUSH_OTHER_TEAMS))))
/* 160 */               .then(Commands.literal("always").executes(debug0 -> setCollision((CommandSourceStack)debug0.getSource(), TeamArgument.getTeam(debug0, "team"), Team.CollisionRule.ALWAYS)))))
/*     */             
/* 162 */             .then(
/* 163 */               Commands.literal("prefix")
/* 164 */               .then(
/* 165 */                 Commands.argument("prefix", (ArgumentType)ComponentArgument.textComponent())
/* 166 */                 .executes(debug0 -> setPrefix((CommandSourceStack)debug0.getSource(), TeamArgument.getTeam(debug0, "team"), ComponentArgument.getComponent(debug0, "prefix"))))))
/*     */ 
/*     */             
/* 169 */             .then(
/* 170 */               Commands.literal("suffix")
/* 171 */               .then(
/* 172 */                 Commands.argument("suffix", (ArgumentType)ComponentArgument.textComponent())
/* 173 */                 .executes(debug0 -> setSuffix((CommandSourceStack)debug0.getSource(), TeamArgument.getTeam(debug0, "team"), ComponentArgument.getComponent(debug0, "suffix"))))))));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int leaveTeam(CommandSourceStack debug0, Collection<String> debug1) {
/* 182 */     ServerScoreboard serverScoreboard = debug0.getServer().getScoreboard();
/*     */     
/* 184 */     for (String debug4 : debug1) {
/* 185 */       serverScoreboard.removePlayerFromTeam(debug4);
/*     */     }
/*     */     
/* 188 */     if (debug1.size() == 1) {
/* 189 */       debug0.sendSuccess((Component)new TranslatableComponent("commands.team.leave.success.single", new Object[] { debug1.iterator().next() }), true);
/*     */     } else {
/* 191 */       debug0.sendSuccess((Component)new TranslatableComponent("commands.team.leave.success.multiple", new Object[] { Integer.valueOf(debug1.size()) }), true);
/*     */     } 
/*     */     
/* 194 */     return debug1.size();
/*     */   }
/*     */   
/*     */   private static int joinTeam(CommandSourceStack debug0, PlayerTeam debug1, Collection<String> debug2) {
/* 198 */     ServerScoreboard serverScoreboard = debug0.getServer().getScoreboard();
/*     */     
/* 200 */     for (String debug5 : debug2) {
/* 201 */       serverScoreboard.addPlayerToTeam(debug5, debug1);
/*     */     }
/*     */     
/* 204 */     if (debug2.size() == 1) {
/* 205 */       debug0.sendSuccess((Component)new TranslatableComponent("commands.team.join.success.single", new Object[] { debug2.iterator().next(), debug1.getFormattedDisplayName() }), true);
/*     */     } else {
/* 207 */       debug0.sendSuccess((Component)new TranslatableComponent("commands.team.join.success.multiple", new Object[] { Integer.valueOf(debug2.size()), debug1.getFormattedDisplayName() }), true);
/*     */     } 
/*     */     
/* 210 */     return debug2.size();
/*     */   }
/*     */   
/*     */   private static int setNametagVisibility(CommandSourceStack debug0, PlayerTeam debug1, Team.Visibility debug2) throws CommandSyntaxException {
/* 214 */     if (debug1.getNameTagVisibility() == debug2) {
/* 215 */       throw ERROR_TEAM_NAMETAG_VISIBLITY_UNCHANGED.create();
/*     */     }
/* 217 */     debug1.setNameTagVisibility(debug2);
/* 218 */     debug0.sendSuccess((Component)new TranslatableComponent("commands.team.option.nametagVisibility.success", new Object[] { debug1.getFormattedDisplayName(), debug2.getDisplayName() }), true);
/* 219 */     return 0;
/*     */   }
/*     */   
/*     */   private static int setDeathMessageVisibility(CommandSourceStack debug0, PlayerTeam debug1, Team.Visibility debug2) throws CommandSyntaxException {
/* 223 */     if (debug1.getDeathMessageVisibility() == debug2) {
/* 224 */       throw ERROR_TEAM_DEATH_MESSAGE_VISIBLITY_UNCHANGED.create();
/*     */     }
/* 226 */     debug1.setDeathMessageVisibility(debug2);
/* 227 */     debug0.sendSuccess((Component)new TranslatableComponent("commands.team.option.deathMessageVisibility.success", new Object[] { debug1.getFormattedDisplayName(), debug2.getDisplayName() }), true);
/* 228 */     return 0;
/*     */   }
/*     */   
/*     */   private static int setCollision(CommandSourceStack debug0, PlayerTeam debug1, Team.CollisionRule debug2) throws CommandSyntaxException {
/* 232 */     if (debug1.getCollisionRule() == debug2) {
/* 233 */       throw ERROR_TEAM_COLLISION_UNCHANGED.create();
/*     */     }
/* 235 */     debug1.setCollisionRule(debug2);
/* 236 */     debug0.sendSuccess((Component)new TranslatableComponent("commands.team.option.collisionRule.success", new Object[] { debug1.getFormattedDisplayName(), debug2.getDisplayName() }), true);
/* 237 */     return 0;
/*     */   }
/*     */   
/*     */   private static int setFriendlySight(CommandSourceStack debug0, PlayerTeam debug1, boolean debug2) throws CommandSyntaxException {
/* 241 */     if (debug1.canSeeFriendlyInvisibles() == debug2) {
/* 242 */       if (debug2) {
/* 243 */         throw ERROR_TEAM_ALREADY_FRIENDLYINVISIBLES_ENABLED.create();
/*     */       }
/* 245 */       throw ERROR_TEAM_ALREADY_FRIENDLYINVISIBLES_DISABLED.create();
/*     */     } 
/*     */ 
/*     */     
/* 249 */     debug1.setSeeFriendlyInvisibles(debug2);
/* 250 */     debug0.sendSuccess((Component)new TranslatableComponent("commands.team.option.seeFriendlyInvisibles." + (debug2 ? "enabled" : "disabled"), new Object[] { debug1.getFormattedDisplayName() }), true);
/*     */     
/* 252 */     return 0;
/*     */   }
/*     */   
/*     */   private static int setFriendlyFire(CommandSourceStack debug0, PlayerTeam debug1, boolean debug2) throws CommandSyntaxException {
/* 256 */     if (debug1.isAllowFriendlyFire() == debug2) {
/* 257 */       if (debug2) {
/* 258 */         throw ERROR_TEAM_ALREADY_FRIENDLYFIRE_ENABLED.create();
/*     */       }
/* 260 */       throw ERROR_TEAM_ALREADY_FRIENDLYFIRE_DISABLED.create();
/*     */     } 
/*     */ 
/*     */     
/* 264 */     debug1.setAllowFriendlyFire(debug2);
/* 265 */     debug0.sendSuccess((Component)new TranslatableComponent("commands.team.option.friendlyfire." + (debug2 ? "enabled" : "disabled"), new Object[] { debug1.getFormattedDisplayName() }), true);
/*     */     
/* 267 */     return 0;
/*     */   }
/*     */   
/*     */   private static int setDisplayName(CommandSourceStack debug0, PlayerTeam debug1, Component debug2) throws CommandSyntaxException {
/* 271 */     if (debug1.getDisplayName().equals(debug2)) {
/* 272 */       throw ERROR_TEAM_ALREADY_NAME.create();
/*     */     }
/*     */     
/* 275 */     debug1.setDisplayName(debug2);
/* 276 */     debug0.sendSuccess((Component)new TranslatableComponent("commands.team.option.name.success", new Object[] { debug1.getFormattedDisplayName() }), true);
/* 277 */     return 0;
/*     */   }
/*     */   
/*     */   private static int setColor(CommandSourceStack debug0, PlayerTeam debug1, ChatFormatting debug2) throws CommandSyntaxException {
/* 281 */     if (debug1.getColor() == debug2) {
/* 282 */       throw ERROR_TEAM_ALREADY_COLOR.create();
/*     */     }
/* 284 */     debug1.setColor(debug2);
/* 285 */     debug0.sendSuccess((Component)new TranslatableComponent("commands.team.option.color.success", new Object[] { debug1.getFormattedDisplayName(), debug2.getName() }), true);
/* 286 */     return 0;
/*     */   }
/*     */   
/*     */   private static int emptyTeam(CommandSourceStack debug0, PlayerTeam debug1) throws CommandSyntaxException {
/* 290 */     ServerScoreboard serverScoreboard = debug0.getServer().getScoreboard();
/* 291 */     Collection<String> debug3 = Lists.newArrayList(debug1.getPlayers());
/*     */     
/* 293 */     if (debug3.isEmpty()) {
/* 294 */       throw ERROR_TEAM_ALREADY_EMPTY.create();
/*     */     }
/*     */     
/* 297 */     for (String debug5 : debug3) {
/* 298 */       serverScoreboard.removePlayerFromTeam(debug5, debug1);
/*     */     }
/*     */     
/* 301 */     debug0.sendSuccess((Component)new TranslatableComponent("commands.team.empty.success", new Object[] { Integer.valueOf(debug3.size()), debug1.getFormattedDisplayName() }), true);
/*     */     
/* 303 */     return debug3.size();
/*     */   }
/*     */   
/*     */   private static int deleteTeam(CommandSourceStack debug0, PlayerTeam debug1) {
/* 307 */     ServerScoreboard serverScoreboard = debug0.getServer().getScoreboard();
/* 308 */     serverScoreboard.removePlayerTeam(debug1);
/* 309 */     debug0.sendSuccess((Component)new TranslatableComponent("commands.team.remove.success", new Object[] { debug1.getFormattedDisplayName() }), true);
/* 310 */     return serverScoreboard.getPlayerTeams().size();
/*     */   }
/*     */   
/*     */   private static int createTeam(CommandSourceStack debug0, String debug1) throws CommandSyntaxException {
/* 314 */     return createTeam(debug0, debug1, (Component)new TextComponent(debug1));
/*     */   }
/*     */   
/*     */   private static int createTeam(CommandSourceStack debug0, String debug1, Component debug2) throws CommandSyntaxException {
/* 318 */     ServerScoreboard serverScoreboard = debug0.getServer().getScoreboard();
/* 319 */     if (serverScoreboard.getPlayerTeam(debug1) != null) {
/* 320 */       throw ERROR_TEAM_ALREADY_EXISTS.create();
/*     */     }
/* 322 */     if (debug1.length() > 16) {
/* 323 */       throw ERROR_TEAM_NAME_TOO_LONG.create(Integer.valueOf(16));
/*     */     }
/*     */     
/* 326 */     PlayerTeam debug4 = serverScoreboard.addPlayerTeam(debug1);
/* 327 */     debug4.setDisplayName(debug2);
/*     */     
/* 329 */     debug0.sendSuccess((Component)new TranslatableComponent("commands.team.add.success", new Object[] { debug4.getFormattedDisplayName() }), true);
/*     */     
/* 331 */     return serverScoreboard.getPlayerTeams().size();
/*     */   }
/*     */   
/*     */   private static int listMembers(CommandSourceStack debug0, PlayerTeam debug1) {
/* 335 */     Collection<String> debug2 = debug1.getPlayers();
/* 336 */     if (debug2.isEmpty()) {
/* 337 */       debug0.sendSuccess((Component)new TranslatableComponent("commands.team.list.members.empty", new Object[] { debug1.getFormattedDisplayName() }), false);
/*     */     } else {
/* 339 */       debug0.sendSuccess((Component)new TranslatableComponent("commands.team.list.members.success", new Object[] { debug1.getFormattedDisplayName(), Integer.valueOf(debug2.size()), ComponentUtils.formatList(debug2) }), false);
/*     */     } 
/* 341 */     return debug2.size();
/*     */   }
/*     */   
/*     */   private static int listTeams(CommandSourceStack debug0) {
/* 345 */     Collection<PlayerTeam> debug1 = debug0.getServer().getScoreboard().getPlayerTeams();
/* 346 */     if (debug1.isEmpty()) {
/* 347 */       debug0.sendSuccess((Component)new TranslatableComponent("commands.team.list.teams.empty"), false);
/*     */     } else {
/* 349 */       debug0.sendSuccess((Component)new TranslatableComponent("commands.team.list.teams.success", new Object[] { Integer.valueOf(debug1.size()), ComponentUtils.formatList(debug1, PlayerTeam::getFormattedDisplayName) }), false);
/*     */     } 
/* 351 */     return debug1.size();
/*     */   }
/*     */   
/*     */   private static int setPrefix(CommandSourceStack debug0, PlayerTeam debug1, Component debug2) {
/* 355 */     debug1.setPlayerPrefix(debug2);
/* 356 */     debug0.sendSuccess((Component)new TranslatableComponent("commands.team.option.prefix.success", new Object[] { debug2 }), false);
/* 357 */     return 1;
/*     */   }
/*     */   
/*     */   private static int setSuffix(CommandSourceStack debug0, PlayerTeam debug1, Component debug2) {
/* 361 */     debug1.setPlayerSuffix(debug2);
/* 362 */     debug0.sendSuccess((Component)new TranslatableComponent("commands.team.option.suffix.success", new Object[] { debug2 }), false);
/* 363 */     return 1;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\commands\TeamCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */