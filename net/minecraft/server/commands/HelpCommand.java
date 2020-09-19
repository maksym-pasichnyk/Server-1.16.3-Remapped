/*    */ package net.minecraft.server.commands;
/*    */ import com.google.common.collect.Iterables;
/*    */ import com.mojang.brigadier.CommandDispatcher;
/*    */ import com.mojang.brigadier.ParseResults;
/*    */ import com.mojang.brigadier.arguments.StringArgumentType;
/*    */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.context.ParsedCommandNode;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*    */ import com.mojang.brigadier.tree.CommandNode;
/*    */ import java.util.Map;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.TextComponent;
/*    */ import net.minecraft.network.chat.TranslatableComponent;
/*    */ 
/*    */ public class HelpCommand {
/* 20 */   private static final SimpleCommandExceptionType ERROR_FAILED = new SimpleCommandExceptionType((Message)new TranslatableComponent("commands.help.failed"));
/*    */   
/*    */   public static void register(CommandDispatcher<CommandSourceStack> debug0) {
/* 23 */     debug0.register(
/* 24 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("help")
/* 25 */         .executes(debug1 -> {
/*    */             Map<CommandNode<CommandSourceStack>, String> debug2 = debug0.getSmartUsage((CommandNode)debug0.getRoot(), debug1.getSource());
/*    */             
/*    */             for (String debug4 : debug2.values()) {
/*    */               ((CommandSourceStack)debug1.getSource()).sendSuccess((Component)new TextComponent("/" + debug4), false);
/*    */             }
/*    */             return debug2.size();
/* 32 */           })).then(
/* 33 */           Commands.argument("command", (ArgumentType)StringArgumentType.greedyString())
/* 34 */           .executes(debug1 -> {
/*    */               ParseResults<CommandSourceStack> debug2 = debug0.parse(StringArgumentType.getString(debug1, "command"), debug1.getSource());
/*    */               if (debug2.getContext().getNodes().isEmpty())
/*    */                 throw ERROR_FAILED.create(); 
/*    */               Map<CommandNode<CommandSourceStack>, String> debug3 = debug0.getSmartUsage(((ParsedCommandNode)Iterables.getLast(debug2.getContext().getNodes())).getNode(), debug1.getSource());
/*    */               for (String debug5 : debug3.values())
/*    */                 ((CommandSourceStack)debug1.getSource()).sendSuccess((Component)new TextComponent("/" + debug2.getReader().getString() + " " + debug5), false); 
/*    */               return debug3.size();
/*    */             })));
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\commands\HelpCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */