/*    */ package net.minecraft.commands.arguments.coordinates;
/*    */ 
/*    */ import com.mojang.brigadier.Message;
/*    */ import com.mojang.brigadier.StringReader;
/*    */ import com.mojang.brigadier.arguments.ArgumentType;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*    */ import com.mojang.brigadier.suggestion.Suggestions;
/*    */ import com.mojang.brigadier.suggestion.SuggestionsBuilder;
/*    */ import java.util.Arrays;
/*    */ import java.util.Collection;
/*    */ import java.util.Collections;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.commands.SharedSuggestionProvider;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.network.chat.TranslatableComponent;
/*    */ import net.minecraft.server.level.ColumnPos;
/*    */ 
/*    */ public class ColumnPosArgument implements ArgumentType<Coordinates> {
/* 23 */   private static final Collection<String> EXAMPLES = Arrays.asList(new String[] { "0 0", "~ ~", "~1 ~-2", "^ ^", "^-1 ^0" });
/* 24 */   public static final SimpleCommandExceptionType ERROR_NOT_COMPLETE = new SimpleCommandExceptionType((Message)new TranslatableComponent("argument.pos2d.incomplete"));
/*    */   
/*    */   public static ColumnPosArgument columnPos() {
/* 27 */     return new ColumnPosArgument();
/*    */   }
/*    */   
/*    */   public static ColumnPos getColumnPos(CommandContext<CommandSourceStack> debug0, String debug1) {
/* 31 */     BlockPos debug2 = ((Coordinates)debug0.getArgument(debug1, Coordinates.class)).getBlockPos((CommandSourceStack)debug0.getSource());
/* 32 */     return new ColumnPos(debug2.getX(), debug2.getZ());
/*    */   }
/*    */ 
/*    */   
/*    */   public Coordinates parse(StringReader debug1) throws CommandSyntaxException {
/* 37 */     int debug2 = debug1.getCursor();
/* 38 */     if (!debug1.canRead()) {
/* 39 */       throw ERROR_NOT_COMPLETE.createWithContext(debug1);
/*    */     }
/* 41 */     WorldCoordinate debug3 = WorldCoordinate.parseInt(debug1);
/* 42 */     if (!debug1.canRead() || debug1.peek() != ' ') {
/* 43 */       debug1.setCursor(debug2);
/* 44 */       throw ERROR_NOT_COMPLETE.createWithContext(debug1);
/*    */     } 
/* 46 */     debug1.skip();
/* 47 */     WorldCoordinate debug4 = WorldCoordinate.parseInt(debug1);
/* 48 */     return new WorldCoordinates(debug3, new WorldCoordinate(true, 0.0D), debug4);
/*    */   }
/*    */ 
/*    */   
/*    */   public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> debug1, SuggestionsBuilder debug2) {
/* 53 */     if (debug1.getSource() instanceof SharedSuggestionProvider) {
/* 54 */       Collection<SharedSuggestionProvider.TextCoordinates> debug4; String debug3 = debug2.getRemaining();
/*    */ 
/*    */ 
/*    */       
/* 58 */       if (!debug3.isEmpty() && debug3.charAt(0) == '^') {
/* 59 */         debug4 = Collections.singleton(SharedSuggestionProvider.TextCoordinates.DEFAULT_LOCAL);
/*    */       } else {
/* 61 */         debug4 = ((SharedSuggestionProvider)debug1.getSource()).getRelevantCoordinates();
/*    */       } 
/*    */       
/* 64 */       return SharedSuggestionProvider.suggest2DCoordinates(debug3, debug4, debug2, Commands.createValidator(this::parse));
/*    */     } 
/* 66 */     return Suggestions.empty();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Collection<String> getExamples() {
/* 72 */     return EXAMPLES;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\commands\arguments\coordinates\ColumnPosArgument.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */