/*     */ package net.minecraft.server.commands;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.mojang.brigadier.CommandDispatcher;
/*     */ import com.mojang.brigadier.Message;
/*     */ import com.mojang.brigadier.arguments.ArgumentType;
/*     */ import com.mojang.brigadier.arguments.BoolArgumentType;
/*     */ import com.mojang.brigadier.arguments.IntegerArgumentType;
/*     */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*     */ import com.mojang.brigadier.builder.RequiredArgumentBuilder;
/*     */ import com.mojang.brigadier.context.CommandContext;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*     */ import java.util.Collection;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.commands.Commands;
/*     */ import net.minecraft.commands.arguments.EntityArgument;
/*     */ import net.minecraft.commands.arguments.MobEffectArgument;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.TranslatableComponent;
/*     */ import net.minecraft.world.effect.MobEffect;
/*     */ import net.minecraft.world.effect.MobEffectInstance;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class EffectCommands
/*     */ {
/*  31 */   private static final SimpleCommandExceptionType ERROR_GIVE_FAILED = new SimpleCommandExceptionType((Message)new TranslatableComponent("commands.effect.give.failed"));
/*  32 */   private static final SimpleCommandExceptionType ERROR_CLEAR_EVERYTHING_FAILED = new SimpleCommandExceptionType((Message)new TranslatableComponent("commands.effect.clear.everything.failed"));
/*  33 */   private static final SimpleCommandExceptionType ERROR_CLEAR_SPECIFIC_FAILED = new SimpleCommandExceptionType((Message)new TranslatableComponent("commands.effect.clear.specific.failed"));
/*     */   
/*     */   public static void register(CommandDispatcher<CommandSourceStack> debug0) {
/*  36 */     debug0.register(
/*  37 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("effect")
/*  38 */         .requires(debug0 -> debug0.hasPermission(2)))
/*  39 */         .then((
/*  40 */           (LiteralArgumentBuilder)Commands.literal("clear")
/*  41 */           .executes(debug0 -> clearEffects((CommandSourceStack)debug0.getSource(), (Collection<? extends Entity>)ImmutableList.of(((CommandSourceStack)debug0.getSource()).getEntityOrException()))))
/*  42 */           .then((
/*  43 */             (RequiredArgumentBuilder)Commands.argument("targets", (ArgumentType)EntityArgument.entities())
/*  44 */             .executes(debug0 -> clearEffects((CommandSourceStack)debug0.getSource(), EntityArgument.getEntities(debug0, "targets"))))
/*  45 */             .then(
/*  46 */               Commands.argument("effect", (ArgumentType)MobEffectArgument.effect())
/*  47 */               .executes(debug0 -> clearEffect((CommandSourceStack)debug0.getSource(), EntityArgument.getEntities(debug0, "targets"), MobEffectArgument.getEffect(debug0, "effect")))))))
/*     */ 
/*     */ 
/*     */         
/*  51 */         .then(
/*  52 */           Commands.literal("give")
/*  53 */           .then(
/*  54 */             Commands.argument("targets", (ArgumentType)EntityArgument.entities())
/*  55 */             .then((
/*  56 */               (RequiredArgumentBuilder)Commands.argument("effect", (ArgumentType)MobEffectArgument.effect())
/*  57 */               .executes(debug0 -> giveEffect((CommandSourceStack)debug0.getSource(), EntityArgument.getEntities(debug0, "targets"), MobEffectArgument.getEffect(debug0, "effect"), null, 0, true)))
/*  58 */               .then((
/*  59 */                 (RequiredArgumentBuilder)Commands.argument("seconds", (ArgumentType)IntegerArgumentType.integer(1, 1000000))
/*  60 */                 .executes(debug0 -> giveEffect((CommandSourceStack)debug0.getSource(), EntityArgument.getEntities(debug0, "targets"), MobEffectArgument.getEffect(debug0, "effect"), Integer.valueOf(IntegerArgumentType.getInteger(debug0, "seconds")), 0, true)))
/*  61 */                 .then((
/*  62 */                   (RequiredArgumentBuilder)Commands.argument("amplifier", (ArgumentType)IntegerArgumentType.integer(0, 255))
/*  63 */                   .executes(debug0 -> giveEffect((CommandSourceStack)debug0.getSource(), EntityArgument.getEntities(debug0, "targets"), MobEffectArgument.getEffect(debug0, "effect"), Integer.valueOf(IntegerArgumentType.getInteger(debug0, "seconds")), IntegerArgumentType.getInteger(debug0, "amplifier"), true)))
/*  64 */                   .then(
/*  65 */                     Commands.argument("hideParticles", (ArgumentType)BoolArgumentType.bool())
/*  66 */                     .executes(debug0 -> giveEffect((CommandSourceStack)debug0.getSource(), EntityArgument.getEntities(debug0, "targets"), MobEffectArgument.getEffect(debug0, "effect"), Integer.valueOf(IntegerArgumentType.getInteger(debug0, "seconds")), IntegerArgumentType.getInteger(debug0, "amplifier"), !BoolArgumentType.getBool(debug0, "hideParticles"))))))))));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int giveEffect(CommandSourceStack debug0, Collection<? extends Entity> debug1, MobEffect debug2, @Nullable Integer debug3, int debug4, boolean debug5) throws CommandSyntaxException {
/*  77 */     int debug7, debug6 = 0;
/*     */ 
/*     */     
/*  80 */     if (debug3 != null) {
/*  81 */       if (debug2.isInstantenous()) {
/*  82 */         debug7 = debug3.intValue();
/*     */       } else {
/*  84 */         debug7 = debug3.intValue() * 20;
/*     */       }
/*     */     
/*  87 */     } else if (debug2.isInstantenous()) {
/*  88 */       debug7 = 1;
/*     */     } else {
/*  90 */       debug7 = 600;
/*     */     } 
/*     */ 
/*     */     
/*  94 */     for (Entity debug9 : debug1) {
/*  95 */       if (debug9 instanceof LivingEntity) {
/*  96 */         MobEffectInstance debug10 = new MobEffectInstance(debug2, debug7, debug4, false, debug5);
/*  97 */         if (((LivingEntity)debug9).addEffect(debug10)) {
/*  98 */           debug6++;
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 103 */     if (debug6 == 0) {
/* 104 */       throw ERROR_GIVE_FAILED.create();
/*     */     }
/*     */     
/* 107 */     if (debug1.size() == 1) {
/* 108 */       debug0.sendSuccess((Component)new TranslatableComponent("commands.effect.give.success.single", new Object[] { debug2.getDisplayName(), ((Entity)debug1.iterator().next()).getDisplayName(), Integer.valueOf(debug7 / 20) }), true);
/*     */     } else {
/* 110 */       debug0.sendSuccess((Component)new TranslatableComponent("commands.effect.give.success.multiple", new Object[] { debug2.getDisplayName(), Integer.valueOf(debug1.size()), Integer.valueOf(debug7 / 20) }), true);
/*     */     } 
/*     */     
/* 113 */     return debug6;
/*     */   }
/*     */   
/*     */   private static int clearEffects(CommandSourceStack debug0, Collection<? extends Entity> debug1) throws CommandSyntaxException {
/* 117 */     int debug2 = 0;
/*     */     
/* 119 */     for (Entity debug4 : debug1) {
/* 120 */       if (debug4 instanceof LivingEntity && (
/* 121 */         (LivingEntity)debug4).removeAllEffects()) {
/* 122 */         debug2++;
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 127 */     if (debug2 == 0) {
/* 128 */       throw ERROR_CLEAR_EVERYTHING_FAILED.create();
/*     */     }
/*     */     
/* 131 */     if (debug1.size() == 1) {
/* 132 */       debug0.sendSuccess((Component)new TranslatableComponent("commands.effect.clear.everything.success.single", new Object[] { ((Entity)debug1.iterator().next()).getDisplayName() }), true);
/*     */     } else {
/* 134 */       debug0.sendSuccess((Component)new TranslatableComponent("commands.effect.clear.everything.success.multiple", new Object[] { Integer.valueOf(debug1.size()) }), true);
/*     */     } 
/*     */     
/* 137 */     return debug2;
/*     */   }
/*     */   
/*     */   private static int clearEffect(CommandSourceStack debug0, Collection<? extends Entity> debug1, MobEffect debug2) throws CommandSyntaxException {
/* 141 */     int debug3 = 0;
/*     */     
/* 143 */     for (Entity debug5 : debug1) {
/* 144 */       if (debug5 instanceof LivingEntity && (
/* 145 */         (LivingEntity)debug5).removeEffect(debug2)) {
/* 146 */         debug3++;
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 151 */     if (debug3 == 0) {
/* 152 */       throw ERROR_CLEAR_SPECIFIC_FAILED.create();
/*     */     }
/*     */     
/* 155 */     if (debug1.size() == 1) {
/* 156 */       debug0.sendSuccess((Component)new TranslatableComponent("commands.effect.clear.specific.success.single", new Object[] { debug2.getDisplayName(), ((Entity)debug1.iterator().next()).getDisplayName() }), true);
/*     */     } else {
/* 158 */       debug0.sendSuccess((Component)new TranslatableComponent("commands.effect.clear.specific.success.multiple", new Object[] { debug2.getDisplayName(), Integer.valueOf(debug1.size()) }), true);
/*     */     } 
/*     */     
/* 161 */     return debug3;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\commands\EffectCommands.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */