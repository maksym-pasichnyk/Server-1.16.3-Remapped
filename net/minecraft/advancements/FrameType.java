/*    */ package net.minecraft.advancements;
/*    */ 
/*    */ import net.minecraft.ChatFormatting;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.TranslatableComponent;
/*    */ 
/*    */ public enum FrameType {
/*  8 */   TASK("task", 0, ChatFormatting.GREEN),
/*  9 */   CHALLENGE("challenge", 26, ChatFormatting.DARK_PURPLE),
/* 10 */   GOAL("goal", 52, ChatFormatting.GREEN);
/*    */   
/*    */   private final String name;
/*    */   
/*    */   private final int texture;
/*    */   private final ChatFormatting chatColor;
/*    */   private final Component displayName;
/*    */   
/*    */   FrameType(String debug3, int debug4, ChatFormatting debug5) {
/* 19 */     this.name = debug3;
/* 20 */     this.texture = debug4;
/* 21 */     this.chatColor = debug5;
/* 22 */     this.displayName = (Component)new TranslatableComponent("advancements.toast." + debug3);
/*    */   }
/*    */   
/*    */   public String getName() {
/* 26 */     return this.name;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static FrameType byName(String debug0) {
/* 34 */     for (FrameType debug4 : values()) {
/* 35 */       if (debug4.name.equals(debug0)) {
/* 36 */         return debug4;
/*    */       }
/*    */     } 
/* 39 */     throw new IllegalArgumentException("Unknown frame type '" + debug0 + "'");
/*    */   }
/*    */   
/*    */   public ChatFormatting getChatColor() {
/* 43 */     return this.chatColor;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\advancements\FrameType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */