/*    */ package net.minecraft.world.scores;
/*    */ 
/*    */ import java.util.Arrays;
/*    */ import java.util.Collection;
/*    */ import java.util.Map;
/*    */ import java.util.stream.Collectors;
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.MutableComponent;
/*    */ import net.minecraft.network.chat.TranslatableComponent;
/*    */ 
/*    */ 
/*    */ public abstract class Team
/*    */ {
/*    */   public boolean isAlliedTo(@Nullable Team debug1) {
/* 16 */     if (debug1 == null) {
/* 17 */       return false;
/*    */     }
/* 19 */     if (this == debug1) {
/* 20 */       return true;
/*    */     }
/* 22 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public abstract String getName();
/*    */ 
/*    */   
/*    */   public abstract MutableComponent getFormattedName(Component paramComponent);
/*    */ 
/*    */   
/*    */   public abstract boolean isAllowFriendlyFire();
/*    */ 
/*    */   
/*    */   public abstract Collection<String> getPlayers();
/*    */ 
/*    */   
/*    */   public abstract Visibility getDeathMessageVisibility();
/*    */   
/*    */   public abstract CollisionRule getCollisionRule();
/*    */   
/*    */   public enum Visibility
/*    */   {
/* 44 */     ALWAYS("always", 0),
/* 45 */     NEVER("never", 1),
/* 46 */     HIDE_FOR_OTHER_TEAMS("hideForOtherTeams", 2),
/* 47 */     HIDE_FOR_OWN_TEAM("hideForOwnTeam", 3);
/*    */     static {
/* 49 */       BY_NAME = (Map<String, Visibility>)Arrays.<Visibility>stream(values()).collect(Collectors.toMap(debug0 -> debug0.name, debug0 -> debug0));
/*    */     }
/*    */     private static final Map<String, Visibility> BY_NAME;
/*    */     public final String name;
/*    */     public final int id;
/*    */     
/*    */     @Nullable
/*    */     public static Visibility byName(String debug0) {
/* 57 */       return BY_NAME.get(debug0);
/*    */     }
/*    */ 
/*    */ 
/*    */ 
/*    */     
/*    */     Visibility(String debug3, int debug4) {
/* 64 */       this.name = debug3;
/* 65 */       this.id = debug4;
/*    */     }
/*    */     
/*    */     public Component getDisplayName() {
/* 69 */       return (Component)new TranslatableComponent("team.visibility." + this.name);
/*    */     }
/*    */   }
/*    */   
/*    */   public enum CollisionRule {
/* 74 */     ALWAYS("always", 0),
/* 75 */     NEVER("never", 1),
/* 76 */     PUSH_OTHER_TEAMS("pushOtherTeams", 2),
/* 77 */     PUSH_OWN_TEAM("pushOwnTeam", 3); private static final Map<String, CollisionRule> BY_NAME;
/*    */     static {
/* 79 */       BY_NAME = (Map<String, CollisionRule>)Arrays.<CollisionRule>stream(values()).collect(Collectors.toMap(debug0 -> debug0.name, debug0 -> debug0));
/*    */     }
/*    */     
/*    */     public final String name;
/*    */     public final int id;
/*    */     
/*    */     @Nullable
/*    */     public static CollisionRule byName(String debug0) {
/* 87 */       return BY_NAME.get(debug0);
/*    */     }
/*    */ 
/*    */ 
/*    */ 
/*    */     
/*    */     CollisionRule(String debug3, int debug4) {
/* 94 */       this.name = debug3;
/* 95 */       this.id = debug4;
/*    */     }
/*    */     
/*    */     public Component getDisplayName() {
/* 99 */       return (Component)new TranslatableComponent("team.collision." + this.name);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\scores\Team.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */