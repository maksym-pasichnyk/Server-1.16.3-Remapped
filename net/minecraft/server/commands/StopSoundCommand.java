/*    */ package net.minecraft.server.commands;
/*    */ 
/*    */ import com.mojang.brigadier.CommandDispatcher;
/*    */ import com.mojang.brigadier.arguments.ArgumentType;
/*    */ import com.mojang.brigadier.builder.ArgumentBuilder;
/*    */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*    */ import com.mojang.brigadier.builder.RequiredArgumentBuilder;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import java.util.Collection;
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.commands.arguments.EntityArgument;
/*    */ import net.minecraft.commands.arguments.ResourceLocationArgument;
/*    */ import net.minecraft.commands.arguments.selector.EntitySelector;
/*    */ import net.minecraft.commands.synchronization.SuggestionProviders;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.TranslatableComponent;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.game.ClientboundStopSoundPacket;
/*    */ import net.minecraft.resources.ResourceLocation;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.sounds.SoundSource;
/*    */ 
/*    */ public class StopSoundCommand
/*    */ {
/*    */   public static void register(CommandDispatcher<CommandSourceStack> debug0) {
/* 29 */     RequiredArgumentBuilder<CommandSourceStack, EntitySelector> debug1 = (RequiredArgumentBuilder<CommandSourceStack, EntitySelector>)((RequiredArgumentBuilder)Commands.argument("targets", (ArgumentType)EntityArgument.players()).executes(debug0 -> stopSound((CommandSourceStack)debug0.getSource(), EntityArgument.getPlayers(debug0, "targets"), null, null))).then(
/* 30 */         Commands.literal("*")
/* 31 */         .then(
/* 32 */           Commands.argument("sound", (ArgumentType)ResourceLocationArgument.id())
/* 33 */           .suggests(SuggestionProviders.AVAILABLE_SOUNDS)
/* 34 */           .executes(debug0 -> stopSound((CommandSourceStack)debug0.getSource(), EntityArgument.getPlayers(debug0, "targets"), null, ResourceLocationArgument.getId(debug0, "sound")))));
/*    */ 
/*    */ 
/*    */     
/* 38 */     for (SoundSource debug5 : SoundSource.values()) {
/* 39 */       debug1.then((
/* 40 */           (LiteralArgumentBuilder)Commands.literal(debug5.getName())
/* 41 */           .executes(debug1 -> stopSound((CommandSourceStack)debug1.getSource(), EntityArgument.getPlayers(debug1, "targets"), debug0, null)))
/* 42 */           .then(
/* 43 */             Commands.argument("sound", (ArgumentType)ResourceLocationArgument.id())
/* 44 */             .suggests(SuggestionProviders.AVAILABLE_SOUNDS)
/* 45 */             .executes(debug1 -> stopSound((CommandSourceStack)debug1.getSource(), EntityArgument.getPlayers(debug1, "targets"), debug0, ResourceLocationArgument.getId(debug1, "sound")))));
/*    */     }
/*    */ 
/*    */ 
/*    */     
/* 50 */     debug0.register(
/* 51 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("stopsound")
/* 52 */         .requires(debug0 -> debug0.hasPermission(2)))
/* 53 */         .then((ArgumentBuilder)debug1));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static int stopSound(CommandSourceStack debug0, Collection<ServerPlayer> debug1, @Nullable SoundSource debug2, @Nullable ResourceLocation debug3) {
/* 60 */     ClientboundStopSoundPacket debug4 = new ClientboundStopSoundPacket(debug3, debug2);
/* 61 */     for (ServerPlayer debug6 : debug1) {
/* 62 */       debug6.connection.send((Packet)debug4);
/*    */     }
/*    */     
/* 65 */     if (debug2 != null) {
/* 66 */       if (debug3 != null) {
/* 67 */         debug0.sendSuccess((Component)new TranslatableComponent("commands.stopsound.success.source.sound", new Object[] { debug3, debug2.getName() }), true);
/*    */       } else {
/* 69 */         debug0.sendSuccess((Component)new TranslatableComponent("commands.stopsound.success.source.any", new Object[] { debug2.getName() }), true);
/*    */       }
/*    */     
/* 72 */     } else if (debug3 != null) {
/* 73 */       debug0.sendSuccess((Component)new TranslatableComponent("commands.stopsound.success.sourceless.sound", new Object[] { debug3 }), true);
/*    */     } else {
/* 75 */       debug0.sendSuccess((Component)new TranslatableComponent("commands.stopsound.success.sourceless.any"), true);
/*    */     } 
/*    */ 
/*    */     
/* 79 */     return debug1.size();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\commands\StopSoundCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */