/*    */ package net.minecraft.commands.arguments;
/*    */ 
/*    */ import com.mojang.brigadier.Message;
/*    */ import com.mojang.brigadier.StringReader;
/*    */ import com.mojang.brigadier.arguments.ArgumentType;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
/*    */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*    */ import com.mojang.brigadier.suggestion.Suggestions;
/*    */ import com.mojang.brigadier.suggestion.SuggestionsBuilder;
/*    */ import it.unimi.dsi.fastutil.objects.Object2IntMap;
/*    */ import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
/*    */ import java.util.Arrays;
/*    */ import java.util.Collection;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import net.minecraft.commands.SharedSuggestionProvider;
/*    */ import net.minecraft.network.chat.TranslatableComponent;
/*    */ 
/*    */ public class TimeArgument
/*    */   implements ArgumentType<Integer>
/*    */ {
/* 23 */   private static final Collection<String> EXAMPLES = Arrays.asList(new String[] { "0d", "0s", "0t", "0" }); private static final DynamicCommandExceptionType ERROR_INVALID_TICK_COUNT;
/* 24 */   private static final SimpleCommandExceptionType ERROR_INVALID_UNIT = new SimpleCommandExceptionType((Message)new TranslatableComponent("argument.time.invalid_unit")); static {
/* 25 */     ERROR_INVALID_TICK_COUNT = new DynamicCommandExceptionType(debug0 -> new TranslatableComponent("argument.time.invalid_tick_count", new Object[] { debug0 }));
/*    */   }
/* 27 */   private static final Object2IntMap<String> UNITS = (Object2IntMap<String>)new Object2IntOpenHashMap();
/*    */   
/*    */   static {
/* 30 */     UNITS.put("d", 24000);
/* 31 */     UNITS.put("s", 20);
/* 32 */     UNITS.put("t", 1);
/* 33 */     UNITS.put("", 1);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static TimeArgument time() {
/* 40 */     return new TimeArgument();
/*    */   }
/*    */ 
/*    */   
/*    */   public Integer parse(StringReader debug1) throws CommandSyntaxException {
/* 45 */     float debug2 = debug1.readFloat();
/* 46 */     String debug3 = debug1.readUnquotedString();
/* 47 */     int debug4 = UNITS.getOrDefault(debug3, 0);
/* 48 */     if (debug4 == 0) {
/* 49 */       throw ERROR_INVALID_UNIT.create();
/*    */     }
/*    */     
/* 52 */     int debug5 = Math.round(debug2 * debug4);
/* 53 */     if (debug5 < 0) {
/* 54 */       throw ERROR_INVALID_TICK_COUNT.create(Integer.valueOf(debug5));
/*    */     }
/*    */     
/* 57 */     return Integer.valueOf(debug5);
/*    */   }
/*    */ 
/*    */   
/*    */   public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> debug1, SuggestionsBuilder debug2) {
/* 62 */     StringReader debug3 = new StringReader(debug2.getRemaining());
/*    */     try {
/* 64 */       debug3.readFloat();
/* 65 */     } catch (CommandSyntaxException debug4) {
/* 66 */       return debug2.buildFuture();
/*    */     } 
/*    */     
/* 69 */     return SharedSuggestionProvider.suggest((Iterable)UNITS.keySet(), debug2.createOffset(debug2.getStart() + debug3.getCursor()));
/*    */   }
/*    */ 
/*    */   
/*    */   public Collection<String> getExamples() {
/* 74 */     return EXAMPLES;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\commands\arguments\TimeArgument.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */