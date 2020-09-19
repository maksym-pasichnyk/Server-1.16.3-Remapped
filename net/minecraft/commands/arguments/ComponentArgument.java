/*    */ package net.minecraft.commands.arguments;
/*    */ import com.google.gson.JsonParseException;
/*    */ import com.mojang.brigadier.Message;
/*    */ import com.mojang.brigadier.StringReader;
/*    */ import com.mojang.brigadier.arguments.ArgumentType;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
/*    */ import java.util.Arrays;
/*    */ import java.util.Collection;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.MutableComponent;
/*    */ import net.minecraft.network.chat.TranslatableComponent;
/*    */ 
/*    */ public class ComponentArgument implements ArgumentType<Component> {
/* 17 */   private static final Collection<String> EXAMPLES = Arrays.asList(new String[] { "\"hello world\"", "\"\"", "\"{\"text\":\"hello world\"}", "[\"\"]" }); static {
/* 18 */     ERROR_INVALID_JSON = new DynamicCommandExceptionType(debug0 -> new TranslatableComponent("argument.component.invalid", new Object[] { debug0 }));
/*    */   }
/*    */   
/*    */   public static final DynamicCommandExceptionType ERROR_INVALID_JSON;
/*    */   
/*    */   public static Component getComponent(CommandContext<CommandSourceStack> debug0, String debug1) {
/* 24 */     return (Component)debug0.getArgument(debug1, Component.class);
/*    */   }
/*    */   
/*    */   public static ComponentArgument textComponent() {
/* 28 */     return new ComponentArgument();
/*    */   }
/*    */ 
/*    */   
/*    */   public Component parse(StringReader debug1) throws CommandSyntaxException {
/*    */     try {
/* 34 */       MutableComponent mutableComponent = Component.Serializer.fromJson(debug1);
/* 35 */       if (mutableComponent == null) {
/* 36 */         throw ERROR_INVALID_JSON.createWithContext(debug1, "empty");
/*    */       }
/* 38 */       return (Component)mutableComponent;
/* 39 */     } catch (JsonParseException debug2) {
/* 40 */       String debug3 = (debug2.getCause() != null) ? debug2.getCause().getMessage() : debug2.getMessage();
/* 41 */       throw ERROR_INVALID_JSON.createWithContext(debug1, debug3);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public Collection<String> getExamples() {
/* 47 */     return EXAMPLES;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\commands\arguments\ComponentArgument.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */