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
/*    */ import net.minecraft.world.phys.Vec2;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class Vec2Argument implements ArgumentType<Coordinates> {
/* 23 */   private static final Collection<String> EXAMPLES = Arrays.asList(new String[] { "0 0", "~ ~", "0.1 -0.5", "~1 ~-2" });
/* 24 */   public static final SimpleCommandExceptionType ERROR_NOT_COMPLETE = new SimpleCommandExceptionType((Message)new TranslatableComponent("argument.pos2d.incomplete"));
/*    */   
/*    */   private final boolean centerCorrect;
/*    */   
/*    */   public Vec2Argument(boolean debug1) {
/* 29 */     this.centerCorrect = debug1;
/*    */   }
/*    */   
/*    */   public static Vec2Argument vec2() {
/* 33 */     return new Vec2Argument(true);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static Vec2 getVec2(CommandContext<CommandSourceStack> debug0, String debug1) throws CommandSyntaxException {
/* 42 */     Vec3 debug2 = ((Coordinates)debug0.getArgument(debug1, Coordinates.class)).getPosition((CommandSourceStack)debug0.getSource());
/* 43 */     return new Vec2((float)debug2.x, (float)debug2.z);
/*    */   }
/*    */ 
/*    */   
/*    */   public Coordinates parse(StringReader debug1) throws CommandSyntaxException {
/* 48 */     int debug2 = debug1.getCursor();
/* 49 */     if (!debug1.canRead()) {
/* 50 */       throw ERROR_NOT_COMPLETE.createWithContext(debug1);
/*    */     }
/* 52 */     WorldCoordinate debug3 = WorldCoordinate.parseDouble(debug1, this.centerCorrect);
/* 53 */     if (!debug1.canRead() || debug1.peek() != ' ') {
/* 54 */       debug1.setCursor(debug2);
/* 55 */       throw ERROR_NOT_COMPLETE.createWithContext(debug1);
/*    */     } 
/* 57 */     debug1.skip();
/* 58 */     WorldCoordinate debug4 = WorldCoordinate.parseDouble(debug1, this.centerCorrect);
/* 59 */     return new WorldCoordinates(debug3, new WorldCoordinate(true, 0.0D), debug4);
/*    */   }
/*    */ 
/*    */   
/*    */   public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> debug1, SuggestionsBuilder debug2) {
/* 64 */     if (debug1.getSource() instanceof SharedSuggestionProvider) {
/* 65 */       Collection<SharedSuggestionProvider.TextCoordinates> debug4; String debug3 = debug2.getRemaining();
/*    */ 
/*    */ 
/*    */       
/* 69 */       if (!debug3.isEmpty() && debug3.charAt(0) == '^') {
/* 70 */         debug4 = Collections.singleton(SharedSuggestionProvider.TextCoordinates.DEFAULT_LOCAL);
/*    */       } else {
/* 72 */         debug4 = ((SharedSuggestionProvider)debug1.getSource()).getAbsoluteCoordinates();
/*    */       } 
/*    */       
/* 75 */       return SharedSuggestionProvider.suggest2DCoordinates(debug3, debug4, debug2, Commands.createValidator(this::parse));
/*    */     } 
/* 77 */     return Suggestions.empty();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Collection<String> getExamples() {
/* 83 */     return EXAMPLES;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\commands\arguments\coordinates\Vec2Argument.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */