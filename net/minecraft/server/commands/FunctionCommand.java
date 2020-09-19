/*    */ package net.minecraft.server.commands;
/*    */ import com.mojang.brigadier.CommandDispatcher;
/*    */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.brigadier.suggestion.SuggestionsBuilder;
/*    */ import java.util.Collection;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import net.minecraft.commands.CommandFunction;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.commands.SharedSuggestionProvider;
/*    */ import net.minecraft.commands.arguments.item.FunctionArgument;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.TranslatableComponent;
/*    */ import net.minecraft.server.ServerFunctionManager;
/*    */ 
/*    */ public class FunctionCommand {
/*    */   static {
/* 20 */     SUGGEST_FUNCTION = ((debug0, debug1) -> {
/*    */         ServerFunctionManager debug2 = ((CommandSourceStack)debug0.getSource()).getServer().getFunctions();
/*    */         SharedSuggestionProvider.suggestResource(debug2.getTagNames(), debug1, "#");
/*    */         return SharedSuggestionProvider.suggestResource(debug2.getFunctionNames(), debug1);
/*    */       });
/*    */   } public static final SuggestionProvider<CommandSourceStack> SUGGEST_FUNCTION;
/*    */   public static void register(CommandDispatcher<CommandSourceStack> debug0) {
/* 27 */     debug0.register(
/* 28 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("function")
/* 29 */         .requires(debug0 -> debug0.hasPermission(2)))
/* 30 */         .then(
/* 31 */           Commands.argument("name", (ArgumentType)FunctionArgument.functions())
/* 32 */           .suggests(SUGGEST_FUNCTION)
/* 33 */           .executes(debug0 -> runFunction((CommandSourceStack)debug0.getSource(), FunctionArgument.getFunctions(debug0, "name")))));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private static int runFunction(CommandSourceStack debug0, Collection<CommandFunction> debug1) {
/* 39 */     int debug2 = 0;
/*    */     
/* 41 */     for (CommandFunction debug4 : debug1) {
/* 42 */       debug2 += debug0.getServer().getFunctions().execute(debug4, debug0.withSuppressedOutput().withMaximumPermission(2));
/*    */     }
/*    */     
/* 45 */     if (debug1.size() == 1) {
/* 46 */       debug0.sendSuccess((Component)new TranslatableComponent("commands.function.success.single", new Object[] { Integer.valueOf(debug2), ((CommandFunction)debug1.iterator().next()).getId() }), true);
/*    */     } else {
/* 48 */       debug0.sendSuccess((Component)new TranslatableComponent("commands.function.success.multiple", new Object[] { Integer.valueOf(debug2), Integer.valueOf(debug1.size()) }), true);
/*    */     } 
/*    */     
/* 51 */     return debug2;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\commands\FunctionCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */