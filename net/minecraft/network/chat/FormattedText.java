/*    */ package net.minecraft.network.chat;
/*    */ 
/*    */ import java.util.Optional;
/*    */ import net.minecraft.util.Unit;
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface FormattedText
/*    */ {
/* 10 */   public static final Optional<Unit> STOP_ITERATION = Optional.of(Unit.INSTANCE);
/*    */   
/* 12 */   public static final FormattedText EMPTY = new FormattedText()
/*    */     {
/*    */       public <T> Optional<T> visit(FormattedText.ContentConsumer<T> debug1) {
/* 15 */         return Optional.empty();
/*    */       }
/*    */     };
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   <T> Optional<T> visit(ContentConsumer<T> paramContentConsumer);
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static FormattedText of(final String text) {
/* 29 */     return new FormattedText()
/*    */       {
/*    */         public <T> Optional<T> visit(FormattedText.ContentConsumer<T> debug1) {
/* 32 */           return debug1.accept(text);
/*    */         }
/*    */       };
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   default String getString() {
/* 89 */     StringBuilder debug1 = new StringBuilder();
/*    */     
/* 91 */     visit(debug1 -> {
/*    */           debug0.append(debug1);
/*    */           
/*    */           return Optional.empty();
/*    */         });
/* 96 */     return debug1.toString();
/*    */   }
/*    */   
/*    */   public static interface ContentConsumer<T> {
/*    */     Optional<T> accept(String param1String);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\chat\FormattedText.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */