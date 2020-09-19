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
/*    */ import net.minecraft.network.chat.TranslatableComponent;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class Vec3Argument implements ArgumentType<Coordinates> {
/* 22 */   private static final Collection<String> EXAMPLES = Arrays.asList(new String[] { "0 0 0", "~ ~ ~", "^ ^ ^", "^1 ^ ^-5", "0.1 -0.5 .9", "~0.5 ~1 ~-5" });
/*    */   
/* 24 */   public static final SimpleCommandExceptionType ERROR_NOT_COMPLETE = new SimpleCommandExceptionType((Message)new TranslatableComponent("argument.pos3d.incomplete"));
/* 25 */   public static final SimpleCommandExceptionType ERROR_MIXED_TYPE = new SimpleCommandExceptionType((Message)new TranslatableComponent("argument.pos.mixed"));
/*    */   
/*    */   private final boolean centerCorrect;
/*    */   
/*    */   public Vec3Argument(boolean debug1) {
/* 30 */     this.centerCorrect = debug1;
/*    */   }
/*    */   
/*    */   public static Vec3Argument vec3() {
/* 34 */     return new Vec3Argument(true);
/*    */   }
/*    */   
/*    */   public static Vec3Argument vec3(boolean debug0) {
/* 38 */     return new Vec3Argument(debug0);
/*    */   }
/*    */   
/*    */   public static Vec3 getVec3(CommandContext<CommandSourceStack> debug0, String debug1) throws CommandSyntaxException {
/* 42 */     return ((Coordinates)debug0.getArgument(debug1, Coordinates.class)).getPosition((CommandSourceStack)debug0.getSource());
/*    */   }
/*    */   
/*    */   public static Coordinates getCoordinates(CommandContext<CommandSourceStack> debug0, String debug1) {
/* 46 */     return (Coordinates)debug0.getArgument(debug1, Coordinates.class);
/*    */   }
/*    */ 
/*    */   
/*    */   public Coordinates parse(StringReader debug1) throws CommandSyntaxException {
/* 51 */     if (debug1.canRead() && debug1.peek() == '^') {
/* 52 */       return LocalCoordinates.parse(debug1);
/*    */     }
/* 54 */     return WorldCoordinates.parseDouble(debug1, this.centerCorrect);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> debug1, SuggestionsBuilder debug2) {
/* 60 */     if (debug1.getSource() instanceof SharedSuggestionProvider) {
/* 61 */       Collection<SharedSuggestionProvider.TextCoordinates> debug4; String debug3 = debug2.getRemaining();
/*    */ 
/*    */ 
/*    */       
/* 65 */       if (!debug3.isEmpty() && debug3.charAt(0) == '^') {
/* 66 */         debug4 = Collections.singleton(SharedSuggestionProvider.TextCoordinates.DEFAULT_LOCAL);
/*    */       } else {
/* 68 */         debug4 = ((SharedSuggestionProvider)debug1.getSource()).getAbsoluteCoordinates();
/*    */       } 
/*    */       
/* 71 */       return SharedSuggestionProvider.suggestCoordinates(debug3, debug4, debug2, Commands.createValidator(this::parse));
/*    */     } 
/* 73 */     return Suggestions.empty();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Collection<String> getExamples() {
/* 79 */     return EXAMPLES;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\commands\arguments\coordinates\Vec3Argument.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */