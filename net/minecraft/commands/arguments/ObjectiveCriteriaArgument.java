/*    */ package net.minecraft.commands.arguments;
/*    */ 
/*    */ import com.google.common.collect.Lists;
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
/*    */ import java.util.List;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.SharedSuggestionProvider;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.network.chat.TranslatableComponent;
/*    */ import net.minecraft.stats.Stat;
/*    */ import net.minecraft.stats.StatType;
/*    */ import net.minecraft.world.scores.criteria.ObjectiveCriteria;
/*    */ 
/*    */ public class ObjectiveCriteriaArgument implements ArgumentType<ObjectiveCriteria> {
/* 25 */   private static final Collection<String> EXAMPLES = Arrays.asList(new String[] { "foo", "foo.bar.baz", "minecraft:foo" }); static {
/* 26 */     ERROR_INVALID_VALUE = new DynamicCommandExceptionType(debug0 -> new TranslatableComponent("argument.criteria.invalid", new Object[] { debug0 }));
/*    */   }
/*    */   
/*    */   public static final DynamicCommandExceptionType ERROR_INVALID_VALUE;
/*    */   
/*    */   public static ObjectiveCriteriaArgument criteria() {
/* 32 */     return new ObjectiveCriteriaArgument();
/*    */   }
/*    */   
/*    */   public static ObjectiveCriteria getCriteria(CommandContext<CommandSourceStack> debug0, String debug1) {
/* 36 */     return (ObjectiveCriteria)debug0.getArgument(debug1, ObjectiveCriteria.class);
/*    */   }
/*    */ 
/*    */   
/*    */   public ObjectiveCriteria parse(StringReader debug1) throws CommandSyntaxException {
/* 41 */     int debug2 = debug1.getCursor();
/* 42 */     while (debug1.canRead() && debug1.peek() != ' ') {
/* 43 */       debug1.skip();
/*    */     }
/* 45 */     String debug3 = debug1.getString().substring(debug2, debug1.getCursor());
/* 46 */     return (ObjectiveCriteria)ObjectiveCriteria.byName(debug3).orElseThrow(() -> {
/*    */           debug0.setCursor(debug1);
/*    */           return ERROR_INVALID_VALUE.create(debug2);
/*    */         });
/*    */   }
/*    */ 
/*    */   
/*    */   public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> debug1, SuggestionsBuilder debug2) {
/* 54 */     List<String> debug3 = Lists.newArrayList(ObjectiveCriteria.CRITERIA_BY_NAME.keySet());
/* 55 */     for (StatType<?> debug5 : (Iterable<StatType<?>>)Registry.STAT_TYPE) {
/* 56 */       for (Object debug7 : debug5.getRegistry()) {
/* 57 */         String debug8 = getName(debug5, debug7);
/* 58 */         debug3.add(debug8);
/*    */       } 
/*    */     } 
/* 61 */     return SharedSuggestionProvider.suggest(debug3, debug2);
/*    */   }
/*    */ 
/*    */   
/*    */   public <T> String getName(StatType<T> debug1, Object debug2) {
/* 66 */     return Stat.buildName(debug1, debug2);
/*    */   }
/*    */ 
/*    */   
/*    */   public Collection<String> getExamples() {
/* 71 */     return EXAMPLES;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\commands\arguments\ObjectiveCriteriaArgument.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */