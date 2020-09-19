/*    */ package net.minecraft.commands.arguments.coordinates;
/*    */ 
/*    */ import com.mojang.brigadier.Message;
/*    */ import com.mojang.brigadier.StringReader;
/*    */ import com.mojang.brigadier.arguments.ArgumentType;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*    */ import java.util.Arrays;
/*    */ import java.util.Collection;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.network.chat.TranslatableComponent;
/*    */ 
/*    */ public class RotationArgument implements ArgumentType<Coordinates> {
/* 15 */   private static final Collection<String> EXAMPLES = Arrays.asList(new String[] { "0 0", "~ ~", "~-5 ~5" });
/* 16 */   public static final SimpleCommandExceptionType ERROR_NOT_COMPLETE = new SimpleCommandExceptionType((Message)new TranslatableComponent("argument.rotation.incomplete"));
/*    */   
/*    */   public static RotationArgument rotation() {
/* 19 */     return new RotationArgument();
/*    */   }
/*    */   
/*    */   public static Coordinates getRotation(CommandContext<CommandSourceStack> debug0, String debug1) {
/* 23 */     return (Coordinates)debug0.getArgument(debug1, Coordinates.class);
/*    */   }
/*    */ 
/*    */   
/*    */   public Coordinates parse(StringReader debug1) throws CommandSyntaxException {
/* 28 */     int debug2 = debug1.getCursor();
/* 29 */     if (!debug1.canRead()) {
/* 30 */       throw ERROR_NOT_COMPLETE.createWithContext(debug1);
/*    */     }
/* 32 */     WorldCoordinate debug3 = WorldCoordinate.parseDouble(debug1, false);
/* 33 */     if (!debug1.canRead() || debug1.peek() != ' ') {
/* 34 */       debug1.setCursor(debug2);
/* 35 */       throw ERROR_NOT_COMPLETE.createWithContext(debug1);
/*    */     } 
/* 37 */     debug1.skip();
/* 38 */     WorldCoordinate debug4 = WorldCoordinate.parseDouble(debug1, false);
/* 39 */     return new WorldCoordinates(debug4, debug3, new WorldCoordinate(true, 0.0D));
/*    */   }
/*    */ 
/*    */   
/*    */   public Collection<String> getExamples() {
/* 44 */     return EXAMPLES;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\commands\arguments\coordinates\RotationArgument.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */