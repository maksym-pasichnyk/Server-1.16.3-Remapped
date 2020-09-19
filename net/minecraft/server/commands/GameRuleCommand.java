/*    */ package net.minecraft.server.commands;
/*    */ 
/*    */ import com.mojang.brigadier.CommandDispatcher;
/*    */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.TranslatableComponent;
/*    */ import net.minecraft.world.level.GameRules;
/*    */ 
/*    */ public class GameRuleCommand {
/*    */   public static void register(CommandDispatcher<CommandSourceStack> debug0) {
/* 15 */     final LiteralArgumentBuilder<CommandSourceStack> base = (LiteralArgumentBuilder<CommandSourceStack>)Commands.literal("gamerule").requires(debug0 -> debug0.hasPermission(2));
/*    */     
/* 17 */     GameRules.visitGameRuleTypes(new GameRules.GameRuleTypeVisitor()
/*    */         {
/*    */           public <T extends GameRules.Value<T>> void visit(GameRules.Key<T> debug1, GameRules.Type<T> debug2) {
/* 20 */             base.then((
/* 21 */                 (LiteralArgumentBuilder)Commands.literal(debug1.getId())
/* 22 */                 .executes(debug1 -> GameRuleCommand.queryRule((CommandSourceStack)debug1.getSource(), debug0)))
/* 23 */                 .then(debug2
/* 24 */                   .createArgument("value")
/* 25 */                   .executes(debug1 -> GameRuleCommand.setRule(debug1, debug0))));
/*    */           }
/*    */         });
/*    */ 
/*    */ 
/*    */     
/* 31 */     debug0.register(debug1);
/*    */   }
/*    */   
/*    */   private static <T extends GameRules.Value<T>> int setRule(CommandContext<CommandSourceStack> debug0, GameRules.Key<T> debug1) {
/* 35 */     CommandSourceStack debug2 = (CommandSourceStack)debug0.getSource();
/* 36 */     GameRules.Value value = debug2.getServer().getGameRules().getRule(debug1);
/* 37 */     value.setFromArgument(debug0, "value");
/* 38 */     debug2.sendSuccess((Component)new TranslatableComponent("commands.gamerule.set", new Object[] { debug1.getId(), value.toString() }), true);
/* 39 */     return value.getCommandResult();
/*    */   }
/*    */   
/*    */   private static <T extends GameRules.Value<T>> int queryRule(CommandSourceStack debug0, GameRules.Key<T> debug1) {
/* 43 */     GameRules.Value value = debug0.getServer().getGameRules().getRule(debug1);
/* 44 */     debug0.sendSuccess((Component)new TranslatableComponent("commands.gamerule.query", new Object[] { debug1.getId(), value.toString() }), false);
/* 45 */     return value.getCommandResult();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\commands\GameRuleCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */