/*    */ package net.minecraft.network.chat;
/*    */ 
/*    */ import java.util.Optional;
/*    */ import java.util.function.Function;
/*    */ import java.util.function.Supplier;
/*    */ 
/*    */ public class KeybindComponent
/*    */   extends BaseComponent {
/*    */   private static Function<String, Supplier<Component>> keyResolver = debug0 -> ();
/*    */   private final String name;
/*    */   private Supplier<Component> nameResolver;
/*    */   
/*    */   public KeybindComponent(String debug1) {
/* 14 */     this.name = debug1;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private Component getNestedComponent() {
/* 22 */     if (this.nameResolver == null) {
/* 23 */       this.nameResolver = keyResolver.apply(this.name);
/*    */     }
/*    */     
/* 26 */     return this.nameResolver.get();
/*    */   }
/*    */ 
/*    */   
/*    */   public <T> Optional<T> visitSelf(FormattedText.ContentConsumer<T> debug1) {
/* 31 */     return getNestedComponent().visit(debug1);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public KeybindComponent plainCopy() {
/* 41 */     return new KeybindComponent(this.name);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object debug1) {
/* 46 */     if (this == debug1) {
/* 47 */       return true;
/*    */     }
/*    */     
/* 50 */     if (debug1 instanceof KeybindComponent) {
/* 51 */       KeybindComponent debug2 = (KeybindComponent)debug1;
/* 52 */       return (this.name.equals(debug2.name) && super.equals(debug1));
/*    */     } 
/*    */     
/* 55 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 60 */     return "KeybindComponent{keybind='" + this.name + '\'' + ", siblings=" + this.siblings + ", style=" + 
/*    */ 
/*    */       
/* 63 */       getStyle() + '}';
/*    */   }
/*    */ 
/*    */   
/*    */   public String getName() {
/* 68 */     return this.name;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\chat\KeybindComponent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */