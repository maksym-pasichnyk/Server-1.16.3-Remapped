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
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.arguments.coordinates.WorldCoordinate;
/*    */ import net.minecraft.network.chat.TranslatableComponent;
/*    */ import net.minecraft.util.Mth;
/*    */ 
/*    */ public class AngleArgument implements ArgumentType<AngleArgument.SingleAngle> {
/* 17 */   private static final Collection<String> EXAMPLES = Arrays.asList(new String[] { "0", "~", "~-5" });
/* 18 */   public static final SimpleCommandExceptionType ERROR_NOT_COMPLETE = new SimpleCommandExceptionType((Message)new TranslatableComponent("argument.angle.incomplete"));
/*    */   
/*    */   public static AngleArgument angle() {
/* 21 */     return new AngleArgument();
/*    */   }
/*    */   
/*    */   public static float getAngle(CommandContext<CommandSourceStack> debug0, String debug1) {
/* 25 */     return ((SingleAngle)debug0.getArgument(debug1, SingleAngle.class)).getAngle((CommandSourceStack)debug0.getSource());
/*    */   }
/*    */ 
/*    */   
/*    */   public SingleAngle parse(StringReader debug1) throws CommandSyntaxException {
/* 30 */     if (!debug1.canRead()) {
/* 31 */       throw ERROR_NOT_COMPLETE.createWithContext(debug1);
/*    */     }
/*    */     
/* 34 */     boolean debug2 = WorldCoordinate.isRelative(debug1);
/* 35 */     float debug3 = (debug1.canRead() && debug1.peek() != ' ') ? debug1.readFloat() : 0.0F;
/* 36 */     return new SingleAngle(debug3, debug2);
/*    */   }
/*    */ 
/*    */   
/*    */   public Collection<String> getExamples() {
/* 41 */     return EXAMPLES;
/*    */   }
/*    */   
/*    */   public static final class SingleAngle {
/*    */     private final float angle;
/*    */     private final boolean isRelative;
/*    */     
/*    */     private SingleAngle(float debug1, boolean debug2) {
/* 49 */       this.angle = debug1;
/* 50 */       this.isRelative = debug2;
/*    */     }
/*    */     
/*    */     public float getAngle(CommandSourceStack debug1) {
/* 54 */       return Mth.wrapDegrees(this.isRelative ? (this.angle + (debug1.getRotation()).y) : this.angle);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\commands\arguments\AngleArgument.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */