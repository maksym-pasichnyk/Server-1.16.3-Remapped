/*    */ package net.minecraft.network.chat;
/*    */ 
/*    */ public class TextComponent extends BaseComponent {
/*  4 */   public static final Component EMPTY = new TextComponent("");
/*    */   
/*    */   private final String text;
/*    */   
/*    */   public TextComponent(String debug1) {
/*  9 */     this.text = debug1;
/*    */   }
/*    */   
/*    */   public String getText() {
/* 13 */     return this.text;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getContents() {
/* 18 */     return this.text;
/*    */   }
/*    */ 
/*    */   
/*    */   public TextComponent plainCopy() {
/* 23 */     return new TextComponent(this.text);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object debug1) {
/* 28 */     if (this == debug1) {
/* 29 */       return true;
/*    */     }
/*    */     
/* 32 */     if (debug1 instanceof TextComponent) {
/* 33 */       TextComponent debug2 = (TextComponent)debug1;
/* 34 */       return (this.text.equals(debug2.getText()) && super.equals(debug1));
/*    */     } 
/*    */     
/* 37 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 42 */     return "TextComponent{text='" + this.text + '\'' + ", siblings=" + this.siblings + ", style=" + 
/*    */ 
/*    */       
/* 45 */       getStyle() + '}';
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\chat\TextComponent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */