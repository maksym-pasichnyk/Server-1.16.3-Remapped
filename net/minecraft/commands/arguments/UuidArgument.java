/*    */ package net.minecraft.commands.arguments;
/*    */ 
/*    */ import com.mojang.brigadier.Message;
/*    */ import com.mojang.brigadier.StringReader;
/*    */ import com.mojang.brigadier.arguments.ArgumentType;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*    */ import java.util.Arrays;
/*    */ import java.util.Collection;
/*    */ import java.util.UUID;
/*    */ import java.util.regex.Matcher;
/*    */ import java.util.regex.Pattern;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.network.chat.TranslatableComponent;
/*    */ 
/*    */ public class UuidArgument implements ArgumentType<UUID> {
/* 18 */   public static final SimpleCommandExceptionType ERROR_INVALID_UUID = new SimpleCommandExceptionType((Message)new TranslatableComponent("argument.uuid.invalid"));
/*    */   
/* 20 */   private static final Collection<String> EXAMPLES = Arrays.asList(new String[] { "dd12be42-52a9-4a91-a8a1-11c01849e498" });
/*    */   
/* 22 */   private static final Pattern ALLOWED_CHARACTERS = Pattern.compile("^([-A-Fa-f0-9]+)");
/*    */   
/*    */   public static UUID getUuid(CommandContext<CommandSourceStack> debug0, String debug1) {
/* 25 */     return (UUID)debug0.getArgument(debug1, UUID.class);
/*    */   }
/*    */   
/*    */   public static UuidArgument uuid() {
/* 29 */     return new UuidArgument();
/*    */   }
/*    */ 
/*    */   
/*    */   public UUID parse(StringReader debug1) throws CommandSyntaxException {
/* 34 */     String debug2 = debug1.getRemaining();
/* 35 */     Matcher debug3 = ALLOWED_CHARACTERS.matcher(debug2);
/* 36 */     if (debug3.find()) {
/* 37 */       String debug4 = debug3.group(1);
/*    */       try {
/* 39 */         UUID debug5 = UUID.fromString(debug4);
/* 40 */         debug1.setCursor(debug1.getCursor() + debug4.length());
/* 41 */         return debug5;
/* 42 */       } catch (IllegalArgumentException illegalArgumentException) {}
/*    */     } 
/*    */ 
/*    */ 
/*    */     
/* 47 */     throw ERROR_INVALID_UUID.create();
/*    */   }
/*    */ 
/*    */   
/*    */   public Collection<String> getExamples() {
/* 52 */     return EXAMPLES;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\commands\arguments\UuidArgument.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */