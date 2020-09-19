/*    */ package net.minecraft.commands.arguments.item;
/*    */ import com.mojang.brigadier.Message;
/*    */ import com.mojang.brigadier.StringReader;
/*    */ import com.mojang.brigadier.arguments.ArgumentType;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
/*    */ import com.mojang.datafixers.util.Either;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import java.util.Arrays;
/*    */ import java.util.Collection;
/*    */ import java.util.Collections;
/*    */ import net.minecraft.commands.CommandFunction;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.network.chat.TranslatableComponent;
/*    */ import net.minecraft.resources.ResourceLocation;
/*    */ import net.minecraft.tags.Tag;
/*    */ 
/*    */ public class FunctionArgument implements ArgumentType<FunctionArgument.Result> {
/*    */   private static final DynamicCommandExceptionType ERROR_UNKNOWN_TAG;
/* 21 */   private static final Collection<String> EXAMPLES = Arrays.asList(new String[] { "foo", "foo:bar", "#foo" }); private static final DynamicCommandExceptionType ERROR_UNKNOWN_FUNCTION; static {
/* 22 */     ERROR_UNKNOWN_TAG = new DynamicCommandExceptionType(debug0 -> new TranslatableComponent("arguments.function.tag.unknown", new Object[] { debug0 }));
/* 23 */     ERROR_UNKNOWN_FUNCTION = new DynamicCommandExceptionType(debug0 -> new TranslatableComponent("arguments.function.unknown", new Object[] { debug0 }));
/*    */   }
/*    */   public static FunctionArgument functions() {
/* 26 */     return new FunctionArgument();
/*    */   }
/*    */ 
/*    */   
/*    */   public Result parse(StringReader debug1) throws CommandSyntaxException {
/* 31 */     if (debug1.canRead() && debug1.peek() == '#') {
/* 32 */       debug1.skip();
/* 33 */       final ResourceLocation id = ResourceLocation.read(debug1);
/* 34 */       return new Result()
/*    */         {
/*    */           public Collection<CommandFunction> create(CommandContext<CommandSourceStack> debug1) throws CommandSyntaxException {
/* 37 */             Tag<CommandFunction> debug2 = FunctionArgument.getFunctionTag(debug1, id);
/* 38 */             return debug2.getValues();
/*    */           }
/*    */ 
/*    */           
/*    */           public Pair<ResourceLocation, Either<CommandFunction, Tag<CommandFunction>>> unwrap(CommandContext<CommandSourceStack> debug1) throws CommandSyntaxException {
/* 43 */             return Pair.of(id, Either.right(FunctionArgument.getFunctionTag(debug1, id)));
/*    */           }
/*    */         };
/*    */     } 
/*    */     
/* 48 */     final ResourceLocation id = ResourceLocation.read(debug1);
/* 49 */     return new Result()
/*    */       {
/*    */         public Collection<CommandFunction> create(CommandContext<CommandSourceStack> debug1) throws CommandSyntaxException {
/* 52 */           return Collections.singleton(FunctionArgument.getFunction(debug1, id));
/*    */         }
/*    */ 
/*    */         
/*    */         public Pair<ResourceLocation, Either<CommandFunction, Tag<CommandFunction>>> unwrap(CommandContext<CommandSourceStack> debug1) throws CommandSyntaxException {
/* 57 */           return Pair.of(id, Either.left(FunctionArgument.getFunction(debug1, id)));
/*    */         }
/*    */       };
/*    */   }
/*    */   
/*    */   private static CommandFunction getFunction(CommandContext<CommandSourceStack> debug0, ResourceLocation debug1) throws CommandSyntaxException {
/* 63 */     return (CommandFunction)((CommandSourceStack)debug0.getSource()).getServer().getFunctions().get(debug1)
/* 64 */       .orElseThrow(() -> ERROR_UNKNOWN_FUNCTION.create(debug0.toString()));
/*    */   }
/*    */   
/*    */   private static Tag<CommandFunction> getFunctionTag(CommandContext<CommandSourceStack> debug0, ResourceLocation debug1) throws CommandSyntaxException {
/* 68 */     Tag<CommandFunction> debug2 = ((CommandSourceStack)debug0.getSource()).getServer().getFunctions().getTag(debug1);
/* 69 */     if (debug2 == null) {
/* 70 */       throw ERROR_UNKNOWN_TAG.create(debug1.toString());
/*    */     }
/* 72 */     return debug2;
/*    */   }
/*    */   
/*    */   public static Collection<CommandFunction> getFunctions(CommandContext<CommandSourceStack> debug0, String debug1) throws CommandSyntaxException {
/* 76 */     return ((Result)debug0.getArgument(debug1, Result.class)).create(debug0);
/*    */   }
/*    */   
/*    */   public static Pair<ResourceLocation, Either<CommandFunction, Tag<CommandFunction>>> getFunctionOrTag(CommandContext<CommandSourceStack> debug0, String debug1) throws CommandSyntaxException {
/* 80 */     return ((Result)debug0.getArgument(debug1, Result.class)).unwrap(debug0);
/*    */   }
/*    */ 
/*    */   
/*    */   public Collection<String> getExamples() {
/* 85 */     return EXAMPLES;
/*    */   }
/*    */   
/*    */   public static interface Result {
/*    */     Collection<CommandFunction> create(CommandContext<CommandSourceStack> param1CommandContext) throws CommandSyntaxException;
/*    */     
/*    */     Pair<ResourceLocation, Either<CommandFunction, Tag<CommandFunction>>> unwrap(CommandContext<CommandSourceStack> param1CommandContext) throws CommandSyntaxException;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\commands\arguments\item\FunctionArgument.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */