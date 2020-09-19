/*     */ package net.minecraft.commands.arguments;
/*     */ 
/*     */ import com.mojang.brigadier.Message;
/*     */ import com.mojang.brigadier.StringReader;
/*     */ import com.mojang.brigadier.arguments.ArgumentType;
/*     */ import com.mojang.brigadier.context.CommandContext;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*     */ import com.mojang.brigadier.suggestion.Suggestions;
/*     */ import com.mojang.brigadier.suggestion.SuggestionsBuilder;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.commands.SharedSuggestionProvider;
/*     */ import net.minecraft.network.chat.TranslatableComponent;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.scores.Score;
/*     */ 
/*     */ public class OperationArgument implements ArgumentType<OperationArgument.Operation> {
/*  21 */   private static final Collection<String> EXAMPLES = Arrays.asList(new String[] { "=", ">", "<" });
/*  22 */   private static final SimpleCommandExceptionType ERROR_INVALID_OPERATION = new SimpleCommandExceptionType((Message)new TranslatableComponent("arguments.operation.invalid"));
/*  23 */   private static final SimpleCommandExceptionType ERROR_DIVIDE_BY_ZERO = new SimpleCommandExceptionType((Message)new TranslatableComponent("arguments.operation.div0"));
/*     */   
/*     */   public static OperationArgument operation() {
/*  26 */     return new OperationArgument();
/*     */   }
/*     */   
/*     */   public static Operation getOperation(CommandContext<CommandSourceStack> debug0, String debug1) throws CommandSyntaxException {
/*  30 */     return (Operation)debug0.getArgument(debug1, Operation.class);
/*     */   }
/*     */ 
/*     */   
/*     */   public Operation parse(StringReader debug1) throws CommandSyntaxException {
/*  35 */     if (debug1.canRead()) {
/*  36 */       int debug2 = debug1.getCursor();
/*  37 */       while (debug1.canRead() && debug1.peek() != ' ') {
/*  38 */         debug1.skip();
/*     */       }
/*  40 */       return getOperation(debug1.getString().substring(debug2, debug1.getCursor()));
/*     */     } 
/*     */     
/*  43 */     throw ERROR_INVALID_OPERATION.create();
/*     */   }
/*     */ 
/*     */   
/*     */   public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> debug1, SuggestionsBuilder debug2) {
/*  48 */     return SharedSuggestionProvider.suggest(new String[] { "=", "+=", "-=", "*=", "/=", "%=", "<", ">", "><" }, debug2);
/*     */   }
/*     */ 
/*     */   
/*     */   public Collection<String> getExamples() {
/*  53 */     return EXAMPLES;
/*     */   }
/*     */   
/*     */   private static Operation getOperation(String debug0) throws CommandSyntaxException {
/*  57 */     if (debug0.equals("><")) {
/*  58 */       return (debug0, debug1) -> {
/*     */           int debug2 = debug0.getScore();
/*     */           
/*     */           debug0.setScore(debug1.getScore());
/*     */           debug1.setScore(debug2);
/*     */         };
/*     */     }
/*  65 */     return getSimpleOperation(debug0);
/*     */   }
/*     */   
/*     */   private static SimpleOperation getSimpleOperation(String debug0) throws CommandSyntaxException {
/*  69 */     switch (debug0) {
/*     */       case "=":
/*  71 */         return (debug0, debug1) -> debug1;
/*     */       case "+=":
/*  73 */         return (debug0, debug1) -> debug0 + debug1;
/*     */       case "-=":
/*  75 */         return (debug0, debug1) -> debug0 - debug1;
/*     */       case "*=":
/*  77 */         return (debug0, debug1) -> debug0 * debug1;
/*     */       case "/=":
/*  79 */         return (debug0, debug1) -> {
/*     */             if (debug1 == 0) {
/*     */               throw ERROR_DIVIDE_BY_ZERO.create();
/*     */             }
/*     */             return Mth.intFloorDiv(debug0, debug1);
/*     */           };
/*     */       case "%=":
/*  86 */         return (debug0, debug1) -> {
/*     */             if (debug1 == 0) {
/*     */               throw ERROR_DIVIDE_BY_ZERO.create();
/*     */             }
/*     */             return Mth.positiveModulo(debug0, debug1);
/*     */           };
/*     */       case "<":
/*  93 */         return Math::min;
/*     */       case ">":
/*  95 */         return Math::max;
/*     */     } 
/*  97 */     throw ERROR_INVALID_OPERATION.create();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @FunctionalInterface
/*     */   static interface SimpleOperation
/*     */     extends Operation
/*     */   {
/*     */     int apply(int param1Int1, int param1Int2) throws CommandSyntaxException;
/*     */ 
/*     */ 
/*     */     
/*     */     default void apply(Score debug1, Score debug2) throws CommandSyntaxException {
/* 111 */       debug1.setScore(apply(debug1.getScore(), debug2.getScore()));
/*     */     }
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   public static interface Operation {
/*     */     void apply(Score param1Score1, Score param1Score2) throws CommandSyntaxException;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\commands\arguments\OperationArgument.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */