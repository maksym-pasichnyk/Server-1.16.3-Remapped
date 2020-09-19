/*    */ package com.mojang.brigadier.context;
/*    */ 
/*    */ import com.mojang.brigadier.tree.CommandNode;
/*    */ import java.util.Objects;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ParsedCommandNode<S>
/*    */ {
/*    */   private final CommandNode<S> node;
/*    */   private final StringRange range;
/*    */   
/*    */   public ParsedCommandNode(CommandNode<S> node, StringRange range) {
/* 17 */     this.node = node;
/* 18 */     this.range = range;
/*    */   }
/*    */   
/*    */   public CommandNode<S> getNode() {
/* 22 */     return this.node;
/*    */   }
/*    */   
/*    */   public StringRange getRange() {
/* 26 */     return this.range;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 31 */     return this.node + "@" + this.range;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object o) {
/* 36 */     if (this == o) return true; 
/* 37 */     if (o == null || getClass() != o.getClass()) return false; 
/* 38 */     ParsedCommandNode<?> that = (ParsedCommandNode)o;
/* 39 */     return (Objects.equals(this.node, that.node) && 
/* 40 */       Objects.equals(this.range, that.range));
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 45 */     return Objects.hash(new Object[] { this.node, this.range });
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\brigadier\context\ParsedCommandNode.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */