/*     */ package net.minecraft.server.commands;
/*     */ 
/*     */ import com.mojang.brigadier.CommandDispatcher;
/*     */ import com.mojang.brigadier.Message;
/*     */ import com.mojang.brigadier.arguments.ArgumentType;
/*     */ import com.mojang.brigadier.arguments.FloatArgumentType;
/*     */ import com.mojang.brigadier.arguments.IntegerArgumentType;
/*     */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*     */ import com.mojang.brigadier.builder.RequiredArgumentBuilder;
/*     */ import com.mojang.brigadier.context.CommandContext;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*     */ import java.util.Locale;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.commands.Commands;
/*     */ import net.minecraft.commands.arguments.coordinates.Vec2Argument;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.TranslatableComponent;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.level.border.WorldBorder;
/*     */ import net.minecraft.world.phys.Vec2;
/*     */ 
/*     */ public class WorldBorderCommand
/*     */ {
/*  25 */   private static final SimpleCommandExceptionType ERROR_SAME_CENTER = new SimpleCommandExceptionType((Message)new TranslatableComponent("commands.worldborder.center.failed"));
/*  26 */   private static final SimpleCommandExceptionType ERROR_SAME_SIZE = new SimpleCommandExceptionType((Message)new TranslatableComponent("commands.worldborder.set.failed.nochange"));
/*  27 */   private static final SimpleCommandExceptionType ERROR_TOO_SMALL = new SimpleCommandExceptionType((Message)new TranslatableComponent("commands.worldborder.set.failed.small."));
/*  28 */   private static final SimpleCommandExceptionType ERROR_TOO_BIG = new SimpleCommandExceptionType((Message)new TranslatableComponent("commands.worldborder.set.failed.big."));
/*  29 */   private static final SimpleCommandExceptionType ERROR_SAME_WARNING_TIME = new SimpleCommandExceptionType((Message)new TranslatableComponent("commands.worldborder.warning.time.failed"));
/*  30 */   private static final SimpleCommandExceptionType ERROR_SAME_WARNING_DISTANCE = new SimpleCommandExceptionType((Message)new TranslatableComponent("commands.worldborder.warning.distance.failed"));
/*  31 */   private static final SimpleCommandExceptionType ERROR_SAME_DAMAGE_BUFFER = new SimpleCommandExceptionType((Message)new TranslatableComponent("commands.worldborder.damage.buffer.failed"));
/*  32 */   private static final SimpleCommandExceptionType ERROR_SAME_DAMAGE_AMOUNT = new SimpleCommandExceptionType((Message)new TranslatableComponent("commands.worldborder.damage.amount.failed"));
/*     */   
/*     */   public static void register(CommandDispatcher<CommandSourceStack> debug0) {
/*  35 */     debug0.register(
/*  36 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("worldborder")
/*  37 */         .requires(debug0 -> debug0.hasPermission(2)))
/*  38 */         .then(
/*  39 */           Commands.literal("add")
/*  40 */           .then((
/*  41 */             (RequiredArgumentBuilder)Commands.argument("distance", (ArgumentType)FloatArgumentType.floatArg(-6.0E7F, 6.0E7F))
/*  42 */             .executes(debug0 -> setSize((CommandSourceStack)debug0.getSource(), ((CommandSourceStack)debug0.getSource()).getLevel().getWorldBorder().getSize() + FloatArgumentType.getFloat(debug0, "distance"), 0L)))
/*  43 */             .then(
/*  44 */               Commands.argument("time", (ArgumentType)IntegerArgumentType.integer(0))
/*  45 */               .executes(debug0 -> setSize((CommandSourceStack)debug0.getSource(), ((CommandSourceStack)debug0.getSource()).getLevel().getWorldBorder().getSize() + FloatArgumentType.getFloat(debug0, "distance"), ((CommandSourceStack)debug0.getSource()).getLevel().getWorldBorder().getLerpRemainingTime() + IntegerArgumentType.getInteger(debug0, "time") * 1000L))))))
/*     */ 
/*     */ 
/*     */         
/*  49 */         .then(
/*  50 */           Commands.literal("set")
/*  51 */           .then((
/*  52 */             (RequiredArgumentBuilder)Commands.argument("distance", (ArgumentType)FloatArgumentType.floatArg(-6.0E7F, 6.0E7F))
/*  53 */             .executes(debug0 -> setSize((CommandSourceStack)debug0.getSource(), FloatArgumentType.getFloat(debug0, "distance"), 0L)))
/*  54 */             .then(
/*  55 */               Commands.argument("time", (ArgumentType)IntegerArgumentType.integer(0))
/*  56 */               .executes(debug0 -> setSize((CommandSourceStack)debug0.getSource(), FloatArgumentType.getFloat(debug0, "distance"), IntegerArgumentType.getInteger(debug0, "time") * 1000L))))))
/*     */ 
/*     */ 
/*     */         
/*  60 */         .then(
/*  61 */           Commands.literal("center")
/*  62 */           .then(
/*  63 */             Commands.argument("pos", (ArgumentType)Vec2Argument.vec2())
/*  64 */             .executes(debug0 -> setCenter((CommandSourceStack)debug0.getSource(), Vec2Argument.getVec2(debug0, "pos"))))))
/*     */ 
/*     */         
/*  67 */         .then((
/*  68 */           (LiteralArgumentBuilder)Commands.literal("damage")
/*  69 */           .then(
/*  70 */             Commands.literal("amount")
/*  71 */             .then(
/*  72 */               Commands.argument("damagePerBlock", (ArgumentType)FloatArgumentType.floatArg(0.0F))
/*  73 */               .executes(debug0 -> setDamageAmount((CommandSourceStack)debug0.getSource(), FloatArgumentType.getFloat(debug0, "damagePerBlock"))))))
/*     */ 
/*     */           
/*  76 */           .then(
/*  77 */             Commands.literal("buffer")
/*  78 */             .then(
/*  79 */               Commands.argument("distance", (ArgumentType)FloatArgumentType.floatArg(0.0F))
/*  80 */               .executes(debug0 -> setDamageBuffer((CommandSourceStack)debug0.getSource(), FloatArgumentType.getFloat(debug0, "distance")))))))
/*     */ 
/*     */ 
/*     */         
/*  84 */         .then(
/*  85 */           Commands.literal("get")
/*  86 */           .executes(debug0 -> getSize((CommandSourceStack)debug0.getSource()))))
/*     */         
/*  88 */         .then((
/*  89 */           (LiteralArgumentBuilder)Commands.literal("warning")
/*  90 */           .then(
/*  91 */             Commands.literal("distance")
/*  92 */             .then(
/*  93 */               Commands.argument("distance", (ArgumentType)IntegerArgumentType.integer(0))
/*  94 */               .executes(debug0 -> setWarningDistance((CommandSourceStack)debug0.getSource(), IntegerArgumentType.getInteger(debug0, "distance"))))))
/*     */ 
/*     */           
/*  97 */           .then(
/*  98 */             Commands.literal("time")
/*  99 */             .then(
/* 100 */               Commands.argument("time", (ArgumentType)IntegerArgumentType.integer(0))
/* 101 */               .executes(debug0 -> setWarningTime((CommandSourceStack)debug0.getSource(), IntegerArgumentType.getInteger(debug0, "time")))))));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int setDamageBuffer(CommandSourceStack debug0, float debug1) throws CommandSyntaxException {
/* 109 */     WorldBorder debug2 = debug0.getLevel().getWorldBorder();
/* 110 */     if (debug2.getDamageSafeZone() == debug1) {
/* 111 */       throw ERROR_SAME_DAMAGE_BUFFER.create();
/*     */     }
/* 113 */     debug2.setDamageSafeZone(debug1);
/* 114 */     debug0.sendSuccess((Component)new TranslatableComponent("commands.worldborder.damage.buffer.success", new Object[] { String.format(Locale.ROOT, "%.2f", new Object[] { Float.valueOf(debug1) }) }), true);
/* 115 */     return (int)debug1;
/*     */   }
/*     */   
/*     */   private static int setDamageAmount(CommandSourceStack debug0, float debug1) throws CommandSyntaxException {
/* 119 */     WorldBorder debug2 = debug0.getLevel().getWorldBorder();
/* 120 */     if (debug2.getDamagePerBlock() == debug1) {
/* 121 */       throw ERROR_SAME_DAMAGE_AMOUNT.create();
/*     */     }
/* 123 */     debug2.setDamagePerBlock(debug1);
/* 124 */     debug0.sendSuccess((Component)new TranslatableComponent("commands.worldborder.damage.amount.success", new Object[] { String.format(Locale.ROOT, "%.2f", new Object[] { Float.valueOf(debug1) }) }), true);
/* 125 */     return (int)debug1;
/*     */   }
/*     */   
/*     */   private static int setWarningTime(CommandSourceStack debug0, int debug1) throws CommandSyntaxException {
/* 129 */     WorldBorder debug2 = debug0.getLevel().getWorldBorder();
/* 130 */     if (debug2.getWarningTime() == debug1) {
/* 131 */       throw ERROR_SAME_WARNING_TIME.create();
/*     */     }
/* 133 */     debug2.setWarningTime(debug1);
/* 134 */     debug0.sendSuccess((Component)new TranslatableComponent("commands.worldborder.warning.time.success", new Object[] { Integer.valueOf(debug1) }), true);
/* 135 */     return debug1;
/*     */   }
/*     */   
/*     */   private static int setWarningDistance(CommandSourceStack debug0, int debug1) throws CommandSyntaxException {
/* 139 */     WorldBorder debug2 = debug0.getLevel().getWorldBorder();
/* 140 */     if (debug2.getWarningBlocks() == debug1) {
/* 141 */       throw ERROR_SAME_WARNING_DISTANCE.create();
/*     */     }
/* 143 */     debug2.setWarningBlocks(debug1);
/* 144 */     debug0.sendSuccess((Component)new TranslatableComponent("commands.worldborder.warning.distance.success", new Object[] { Integer.valueOf(debug1) }), true);
/* 145 */     return debug1;
/*     */   }
/*     */   
/*     */   private static int getSize(CommandSourceStack debug0) {
/* 149 */     double debug1 = debug0.getLevel().getWorldBorder().getSize();
/* 150 */     debug0.sendSuccess((Component)new TranslatableComponent("commands.worldborder.get", new Object[] { String.format(Locale.ROOT, "%.0f", new Object[] { Double.valueOf(debug1) }) }), false);
/* 151 */     return Mth.floor(debug1 + 0.5D);
/*     */   }
/*     */   
/*     */   private static int setCenter(CommandSourceStack debug0, Vec2 debug1) throws CommandSyntaxException {
/* 155 */     WorldBorder debug2 = debug0.getLevel().getWorldBorder();
/* 156 */     if (debug2.getCenterX() == debug1.x && debug2.getCenterZ() == debug1.y) {
/* 157 */       throw ERROR_SAME_CENTER.create();
/*     */     }
/*     */     
/* 160 */     debug2.setCenter(debug1.x, debug1.y);
/* 161 */     debug0.sendSuccess((Component)new TranslatableComponent("commands.worldborder.center.success", new Object[] { String.format(Locale.ROOT, "%.2f", new Object[] { Float.valueOf(debug1.x) }), String.format("%.2f", new Object[] { Float.valueOf(debug1.y) }) }), true);
/*     */     
/* 163 */     return 0;
/*     */   }
/*     */   
/*     */   private static int setSize(CommandSourceStack debug0, double debug1, long debug3) throws CommandSyntaxException {
/* 167 */     WorldBorder debug5 = debug0.getLevel().getWorldBorder();
/* 168 */     double debug6 = debug5.getSize();
/*     */     
/* 170 */     if (debug6 == debug1) {
/* 171 */       throw ERROR_SAME_SIZE.create();
/*     */     }
/* 173 */     if (debug1 < 1.0D) {
/* 174 */       throw ERROR_TOO_SMALL.create();
/*     */     }
/* 176 */     if (debug1 > 6.0E7D) {
/* 177 */       throw ERROR_TOO_BIG.create();
/*     */     }
/*     */     
/* 180 */     if (debug3 > 0L) {
/* 181 */       debug5.lerpSizeBetween(debug6, debug1, debug3);
/* 182 */       if (debug1 > debug6) {
/* 183 */         debug0.sendSuccess((Component)new TranslatableComponent("commands.worldborder.set.grow", new Object[] { String.format(Locale.ROOT, "%.1f", new Object[] { Double.valueOf(debug1) }), Long.toString(debug3 / 1000L) }), true);
/*     */       } else {
/* 185 */         debug0.sendSuccess((Component)new TranslatableComponent("commands.worldborder.set.shrink", new Object[] { String.format(Locale.ROOT, "%.1f", new Object[] { Double.valueOf(debug1) }), Long.toString(debug3 / 1000L) }), true);
/*     */       } 
/*     */     } else {
/* 188 */       debug5.setSize(debug1);
/* 189 */       debug0.sendSuccess((Component)new TranslatableComponent("commands.worldborder.set.immediate", new Object[] { String.format(Locale.ROOT, "%.1f", new Object[] { Double.valueOf(debug1) }) }), true);
/*     */     } 
/*     */     
/* 192 */     return (int)(debug1 - debug6);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\commands\WorldBorderCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */