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
/*    */ import java.util.Optional;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.SharedSuggestionProvider;
/*    */ import net.minecraft.network.chat.TextComponent;
/*    */ 
/*    */ public class TestFunctionArgument
/*    */   implements ArgumentType<TestFunction>
/*    */ {
/* 22 */   private static final Collection<String> EXAMPLES = Arrays.asList(new String[] { "techtests.piston", "techtests" });
/*    */ 
/*    */   
/*    */   public TestFunction parse(StringReader debug1) throws CommandSyntaxException {
/* 26 */     String debug2 = debug1.readUnquotedString();
/* 27 */     Optional<TestFunction> debug3 = GameTestRegistry.findTestFunction(debug2);
/* 28 */     if (debug3.isPresent()) {
/* 29 */       return debug3.get();
/*    */     }
/* 31 */     TextComponent textComponent = new TextComponent("No such test: " + debug2);
/* 32 */     throw new CommandSyntaxException(new SimpleCommandExceptionType(textComponent), textComponent);
/*    */   }
/*    */ 
/*    */   
/*    */   public static TestFunctionArgument testFunctionArgument() {
/* 37 */     return new TestFunctionArgument();
/*    */   }
/*    */   
/*    */   public static TestFunction getTestFunction(CommandContext<CommandSourceStack> debug0, String debug1) {
/* 41 */     return (TestFunction)debug0.getArgument(debug1, TestFunction.class);
/*    */   }
/*    */ 
/*    */   
/*    */   public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> debug1, SuggestionsBuilder debug2) {
/* 46 */     Stream<String> debug3 = GameTestRegistry.getAllTestFunctions().stream().map(TestFunction::getTestName);
/* 47 */     return SharedSuggestionProvider.suggest(debug3, debug2);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Collection<String> getExamples() {
/* 53 */     return EXAMPLES;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\gametest\framework\TestFunctionArgument.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */