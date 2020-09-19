/*    */ package com.mojang.brigadier.context;
/*    */ 
/*    */ import com.mojang.brigadier.tree.CommandNode;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SuggestionContext<S>
/*    */ {
/*    */   public final CommandNode<S> parent;
/*    */   public final int startPos;
/*    */   
/*    */   public SuggestionContext(CommandNode<S> parent, int startPos) {
/* 13 */     this.parent = parent;
/* 14 */     this.startPos = startPos;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\brigadier\context\SuggestionContext.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */