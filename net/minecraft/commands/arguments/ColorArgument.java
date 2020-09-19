/*    */ package net.minecraft.commands.arguments;
/*    */ 
/*    */ import com.mojang.brigadier.Message;
/*    */ import com.mojang.brigadier.StringReader;
/*    */ import com.mojang.brigadier.arguments.ArgumentType;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
/*    */ import com.mojang.brigadier.suggestion.Suggestions;
/*    */ import com.mojang.brigadier.suggestion.SuggestionsBuilder;
/*    */ import java.util.Arrays;
/*    */ import java.util.Collection;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import net.minecraft.ChatFormatting;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.SharedSuggestionProvider;
/*    */ import net.minecraft.network.chat.TranslatableComponent;
/*    */ 
/*    */ public class ColorArgument implements ArgumentType<ChatFormatting> {
/* 20 */   private static final Collection<String> EXAMPLES = Arrays.asList(new String[] { "red", "green" }); static {
/* 21 */     ERROR_INVALID_VALUE = new DynamicCommandExceptionType(debug0 -> new TranslatableComponent("argument.color.invalid", new Object[] { debug0 }));
/*    */   }
/*    */   
/*    */   public static final DynamicCommandExceptionType ERROR_INVALID_VALUE;
/*    */   
/*    */   public static ColorArgument color() {
/* 27 */     return new ColorArgument();
/*    */   }
/*    */   
/*    */   public static ChatFormatting getColor(CommandContext<CommandSourceStack> debug0, String debug1) {
/* 31 */     return (ChatFormatting)debug0.getArgument(debug1, ChatFormatting.class);
/*    */   }
/*    */ 
/*    */   
/*    */   public ChatFormatting parse(StringReader debug1) throws CommandSyntaxException {
/* 36 */     String debug2 = debug1.readUnquotedString();
/* 37 */     ChatFormatting debug3 = ChatFormatting.getByName(debug2);
/* 38 */     if (debug3 == null || debug3.isFormat()) {
/* 39 */       throw ERROR_INVALID_VALUE.create(debug2);
/*    */     }
/* 41 */     return debug3;
/*    */   }
/*    */ 
/*    */   
/*    */   public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> debug1, SuggestionsBuilder debug2) {
/* 46 */     return SharedSuggestionProvider.suggest(ChatFormatting.getNames(true, false), debug2);
/*    */   }
/*    */ 
/*    */   
/*    */   public Collection<String> getExamples() {
/* 51 */     return EXAMPLES;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\commands\arguments\ColorArgument.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */