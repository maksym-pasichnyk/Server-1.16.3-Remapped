/*    */ package net.minecraft.gametest.framework;
/*    */ 
/*    */ import com.mojang.brigadier.StringReader;
/*    */ import com.mojang.brigadier.arguments.ArgumentType;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*    */ import com.mojang.brigadier.suggestion.Suggestions;
/*    */ import com.mojang.brigadier.suggestion.SuggestionsBuilder;
/*    */ import java.util.Arrays;
/*    */ import java.util.Collection;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.SharedSuggestionProvider;
/*    */ import net.minecraft.network.chat.TextComponent;
/*    */ 
/*    */ public class TestClassNameArgument
/*    */   implements ArgumentType<String>
/*    */ {
/* 20 */   private static final Collection<String> EXAMPLES = Arrays.asList(new String[] { "techtests", "mobtests" });
/*    */ 
/*    */   
/*    */   public String parse(StringReader debug1) throws CommandSyntaxException {
/* 24 */     String debug2 = debug1.readUnquotedString();
/* 25 */     if (GameTestRegistry.isTestClass(debug2)) {
/* 26 */       return debug2;
/*    */     }
/* 28 */     TextComponent textComponent = new TextComponent("No such test class: " + debug2);
/* 29 */     throw new CommandSyntaxException(new SimpleCommandExceptionType(textComponent), textComponent);
/*    */   }
/*    */ 
/*    */   
/*    */   public static TestClassNameArgument testClassName() {
/* 34 */     return new TestClassNameArgument();
/*    */   }
/*    */   
/*    */   public static String getTestClassName(CommandContext<CommandSourceStack> debug0, String debug1) {
/* 38 */     return (String)debug0.getArgument(debug1, String.class);
/*    */   }
/*    */ 
/*    */   
/*    */   public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> debug1, SuggestionsBuilder debug2) {
/* 43 */     return SharedSuggestionProvider.suggest(GameTestRegistry.getAllTestClassNames().stream(), debug2);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Collection<String> getExamples() {
/* 49 */     return EXAMPLES;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\gametest\framework\TestClassNameArgument.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */