/*    */ package net.minecraft.commands.arguments.coordinates;
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
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ 
/*    */ public class BlockPosArgument implements ArgumentType<Coordinates> {
/* 22 */   private static final Collection<String> EXAMPLES = Arrays.asList(new String[] { "0 0 0", "~ ~ ~", "^ ^ ^", "^1 ^ ^-5", "~0.5 ~1 ~-5" });
/*    */   
/* 24 */   public static final SimpleCommandExceptionType ERROR_NOT_LOADED = new SimpleCommandExceptionType((Message)new TranslatableComponent("argument.pos.unloaded"));
/* 25 */   public static final SimpleCommandExceptionType ERROR_OUT_OF_WORLD = new SimpleCommandExceptionType((Message)new TranslatableComponent("argument.pos.outofworld"));
/*    */   
/*    */   public static BlockPosArgument blockPos() {
/* 28 */     return new BlockPosArgument();
/*    */   }
/*    */   
/*    */   public static BlockPos getLoadedBlockPos(CommandContext<CommandSourceStack> debug0, String debug1) throws CommandSyntaxException {
/* 32 */     BlockPos debug2 = ((Coordinates)debug0.getArgument(debug1, Coordinates.class)).getBlockPos((CommandSourceStack)debug0.getSource());
/* 33 */     if (!((CommandSourceStack)debug0.getSource()).getLevel().hasChunkAt(debug2)) {
/* 34 */       throw ERROR_NOT_LOADED.create();
/*    */     }
/* 36 */     ((CommandSourceStack)debug0.getSource()).getLevel(); if (!ServerLevel.isInWorldBounds(debug2)) {
/* 37 */       throw ERROR_OUT_OF_WORLD.create();
/*    */     }
/* 39 */     return debug2;
/*    */   }
/*    */   
/*    */   public static BlockPos getOrLoadBlockPos(CommandContext<CommandSourceStack> debug0, String debug1) throws CommandSyntaxException {
/* 43 */     return ((Coordinates)debug0.getArgument(debug1, Coordinates.class)).getBlockPos((CommandSourceStack)debug0.getSource());
/*    */   }
/*    */ 
/*    */   
/*    */   public Coordinates parse(StringReader debug1) throws CommandSyntaxException {
/* 48 */     if (debug1.canRead() && debug1.peek() == '^') {
/* 49 */       return LocalCoordinates.parse(debug1);
/*    */     }
/* 51 */     return WorldCoordinates.parseInt(debug1);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> debug1, SuggestionsBuilder debug2) {
/* 57 */     if (debug1.getSource() instanceof SharedSuggestionProvider) {
/* 58 */       Collection<SharedSuggestionProvider.TextCoordinates> debug4; String debug3 = debug2.getRemaining();
/*    */ 
/*    */ 
/*    */       
/* 62 */       if (!debug3.isEmpty() && debug3.charAt(0) == '^') {
/* 63 */         debug4 = Collections.singleton(SharedSuggestionProvider.TextCoordinates.DEFAULT_LOCAL);
/*    */       } else {
/* 65 */         debug4 = ((SharedSuggestionProvider)debug1.getSource()).getRelevantCoordinates();
/*    */       } 
/*    */       
/* 68 */       return SharedSuggestionProvider.suggestCoordinates(debug3, debug4, debug2, Commands.createValidator(this::parse));
/*    */     } 
/* 70 */     return Suggestions.empty();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Collection<String> getExamples() {
/* 76 */     return EXAMPLES;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\commands\arguments\coordinates\BlockPosArgument.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */