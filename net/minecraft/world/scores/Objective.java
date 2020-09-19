/*    */ package net.minecraft.world.scores;
/*    */ 
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.ComponentUtils;
/*    */ import net.minecraft.network.chat.HoverEvent;
/*    */ import net.minecraft.network.chat.Style;
/*    */ import net.minecraft.network.chat.TextComponent;
/*    */ import net.minecraft.world.scores.criteria.ObjectiveCriteria;
/*    */ 
/*    */ public class Objective
/*    */ {
/*    */   private final Scoreboard scoreboard;
/*    */   private final String name;
/*    */   private final ObjectiveCriteria criteria;
/*    */   private Component displayName;
/*    */   private Component formattedDisplayName;
/*    */   private ObjectiveCriteria.RenderType renderType;
/*    */   
/*    */   public Objective(Scoreboard debug1, String debug2, ObjectiveCriteria debug3, Component debug4, ObjectiveCriteria.RenderType debug5) {
/* 20 */     this.scoreboard = debug1;
/* 21 */     this.name = debug2;
/* 22 */     this.criteria = debug3;
/* 23 */     this.displayName = debug4;
/* 24 */     this.formattedDisplayName = createFormattedDisplayName();
/* 25 */     this.renderType = debug5;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getName() {
/* 33 */     return this.name;
/*    */   }
/*    */   
/*    */   public ObjectiveCriteria getCriteria() {
/* 37 */     return this.criteria;
/*    */   }
/*    */   
/*    */   public Component getDisplayName() {
/* 41 */     return this.displayName;
/*    */   }
/*    */   
/*    */   private Component createFormattedDisplayName() {
/* 45 */     return (Component)ComponentUtils.wrapInSquareBrackets((Component)this.displayName
/* 46 */         .copy().withStyle(debug1 -> debug1.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponent(this.name)))));
/*    */   }
/*    */ 
/*    */   
/*    */   public Component getFormattedDisplayName() {
/* 51 */     return this.formattedDisplayName;
/*    */   }
/*    */   
/*    */   public void setDisplayName(Component debug1) {
/* 55 */     this.displayName = debug1;
/* 56 */     this.formattedDisplayName = createFormattedDisplayName();
/* 57 */     this.scoreboard.onObjectiveChanged(this);
/*    */   }
/*    */   
/*    */   public ObjectiveCriteria.RenderType getRenderType() {
/* 61 */     return this.renderType;
/*    */   }
/*    */   
/*    */   public void setRenderType(ObjectiveCriteria.RenderType debug1) {
/* 65 */     this.renderType = debug1;
/* 66 */     this.scoreboard.onObjectiveChanged(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\scores\Objective.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */