/*    */ package net.minecraft.network.chat;
/*    */ 
/*    */ import com.google.common.collect.Lists;
/*    */ import java.util.List;
/*    */ import java.util.Objects;
/*    */ import net.minecraft.util.FormattedCharSequence;
/*    */ 
/*    */ 
/*    */ public abstract class BaseComponent
/*    */   implements MutableComponent
/*    */ {
/* 12 */   protected final List<Component> siblings = Lists.newArrayList();
/* 13 */   private FormattedCharSequence visualOrderText = FormattedCharSequence.EMPTY;
/*    */ 
/*    */ 
/*    */   
/* 17 */   private Style style = Style.EMPTY;
/*    */ 
/*    */   
/*    */   public MutableComponent append(Component debug1) {
/* 21 */     this.siblings.add(debug1);
/* 22 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getContents() {
/* 27 */     return "";
/*    */   }
/*    */ 
/*    */   
/*    */   public List<Component> getSiblings() {
/* 32 */     return this.siblings;
/*    */   }
/*    */ 
/*    */   
/*    */   public MutableComponent setStyle(Style debug1) {
/* 37 */     this.style = debug1;
/* 38 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public Style getStyle() {
/* 43 */     return this.style;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public final MutableComponent copy() {
/* 51 */     BaseComponent debug1 = plainCopy();
/* 52 */     debug1.siblings.addAll(this.siblings);
/* 53 */     debug1.setStyle(this.style);
/* 54 */     return debug1;
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
/*    */   public boolean equals(Object debug1) {
/* 69 */     if (this == debug1) {
/* 70 */       return true;
/*    */     }
/*    */     
/* 73 */     if (debug1 instanceof BaseComponent) {
/* 74 */       BaseComponent debug2 = (BaseComponent)debug1;
/* 75 */       return (this.siblings.equals(debug2.siblings) && Objects.equals(getStyle(), debug2.getStyle()));
/*    */     } 
/*    */     
/* 78 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 83 */     return Objects.hash(new Object[] { getStyle(), this.siblings });
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 88 */     return "BaseComponent{style=" + this.style + ", siblings=" + this.siblings + '}';
/*    */   }
/*    */   
/*    */   public abstract BaseComponent plainCopy();
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\chat\BaseComponent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */