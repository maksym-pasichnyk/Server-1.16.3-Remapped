/*    */ package net.minecraft.network.chat;
/*    */ 
/*    */ import java.util.function.UnaryOperator;
/*    */ import net.minecraft.ChatFormatting;
/*    */ 
/*    */ public interface MutableComponent
/*    */   extends Component
/*    */ {
/*    */   MutableComponent setStyle(Style paramStyle);
/*    */   
/*    */   default MutableComponent append(String debug1) {
/* 12 */     return append(new TextComponent(debug1));
/*    */   }
/*    */   
/*    */   MutableComponent append(Component paramComponent);
/*    */   
/*    */   default MutableComponent withStyle(UnaryOperator<Style> debug1) {
/* 18 */     setStyle(debug1.apply(getStyle()));
/* 19 */     return this;
/*    */   }
/*    */   
/*    */   default MutableComponent withStyle(Style debug1) {
/* 23 */     setStyle(debug1.applyTo(getStyle()));
/* 24 */     return this;
/*    */   }
/*    */   
/*    */   MutableComponent withStyle(ChatFormatting... debug1) {
/* 28 */     setStyle(getStyle().applyFormats(debug1));
/* 29 */     return this;
/*    */   }
/*    */   
/*    */   default MutableComponent withStyle(ChatFormatting debug1) {
/* 33 */     setStyle(getStyle().applyFormat(debug1));
/* 34 */     return this;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\chat\MutableComponent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */