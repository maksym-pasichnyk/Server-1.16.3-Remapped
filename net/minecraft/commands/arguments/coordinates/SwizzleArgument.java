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
/*    */ import java.util.EnumSet;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.network.chat.TranslatableComponent;
/*    */ 
/*    */ public class SwizzleArgument implements ArgumentType<EnumSet<Direction.Axis>> {
/* 17 */   private static final Collection<String> EXAMPLES = Arrays.asList(new String[] { "xyz", "x" });
/* 18 */   private static final SimpleCommandExceptionType ERROR_INVALID = new SimpleCommandExceptionType((Message)new TranslatableComponent("arguments.swizzle.invalid"));
/*    */   
/*    */   public static SwizzleArgument swizzle() {
/* 21 */     return new SwizzleArgument();
/*    */   }
/*    */ 
/*    */   
/*    */   public static EnumSet<Direction.Axis> getSwizzle(CommandContext<CommandSourceStack> debug0, String debug1) {
/* 26 */     return (EnumSet<Direction.Axis>)debug0.getArgument(debug1, EnumSet.class);
/*    */   }
/*    */ 
/*    */   
/*    */   public EnumSet<Direction.Axis> parse(StringReader debug1) throws CommandSyntaxException {
/* 31 */     EnumSet<Direction.Axis> debug2 = EnumSet.noneOf(Direction.Axis.class);
/*    */     
/* 33 */     while (debug1.canRead() && debug1.peek() != ' ') {
/* 34 */       Direction.Axis debug4; char debug3 = debug1.read();
/*    */ 
/*    */       
/* 37 */       switch (debug3) {
/*    */         case 'x':
/* 39 */           debug4 = Direction.Axis.X;
/*    */           break;
/*    */         case 'y':
/* 42 */           debug4 = Direction.Axis.Y;
/*    */           break;
/*    */         case 'z':
/* 45 */           debug4 = Direction.Axis.Z;
/*    */           break;
/*    */         default:
/* 48 */           throw ERROR_INVALID.create();
/*    */       } 
/*    */       
/* 51 */       if (debug2.contains(debug4)) {
/* 52 */         throw ERROR_INVALID.create();
/*    */       }
/* 54 */       debug2.add(debug4);
/*    */     } 
/*    */     
/* 57 */     return debug2;
/*    */   }
/*    */ 
/*    */   
/*    */   public Collection<String> getExamples() {
/* 62 */     return EXAMPLES;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\commands\arguments\coordinates\SwizzleArgument.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */