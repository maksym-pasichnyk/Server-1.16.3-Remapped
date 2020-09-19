/*     */ package net.minecraft.world.scores;
/*     */ 
/*     */ import com.google.common.collect.Sets;
/*     */ import java.util.Collection;
/*     */ import java.util.Set;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.ComponentUtils;
/*     */ import net.minecraft.network.chat.HoverEvent;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.network.chat.Style;
/*     */ import net.minecraft.network.chat.TextComponent;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PlayerTeam
/*     */   extends Team
/*     */ {
/*     */   private final Scoreboard scoreboard;
/*     */   private final String name;
/*  24 */   private final Set<String> players = Sets.newHashSet();
/*     */   private Component displayName;
/*  26 */   private Component playerPrefix = TextComponent.EMPTY;
/*  27 */   private Component playerSuffix = TextComponent.EMPTY;
/*     */   private boolean allowFriendlyFire = true;
/*     */   private boolean seeFriendlyInvisibles = true;
/*  30 */   private Team.Visibility nameTagVisibility = Team.Visibility.ALWAYS;
/*  31 */   private Team.Visibility deathMessageVisibility = Team.Visibility.ALWAYS;
/*  32 */   private ChatFormatting color = ChatFormatting.RESET;
/*  33 */   private Team.CollisionRule collisionRule = Team.CollisionRule.ALWAYS;
/*     */   private final Style displayNameStyle;
/*     */   
/*     */   public PlayerTeam(Scoreboard debug1, String debug2) {
/*  37 */     this.scoreboard = debug1;
/*  38 */     this.name = debug2;
/*  39 */     this.displayName = (Component)new TextComponent(debug2);
/*     */     
/*  41 */     this
/*     */       
/*  43 */       .displayNameStyle = Style.EMPTY.withInsertion(debug2).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponent(debug2)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getName() {
/*  52 */     return this.name;
/*     */   }
/*     */   
/*     */   public Component getDisplayName() {
/*  56 */     return this.displayName;
/*     */   }
/*     */   
/*     */   public MutableComponent getFormattedDisplayName() {
/*  60 */     MutableComponent debug1 = ComponentUtils.wrapInSquareBrackets((Component)this.displayName.copy().withStyle(this.displayNameStyle));
/*     */     
/*  62 */     ChatFormatting debug2 = getColor();
/*  63 */     if (debug2 != ChatFormatting.RESET) {
/*  64 */       debug1.withStyle(debug2);
/*     */     }
/*     */     
/*  67 */     return debug1;
/*     */   }
/*     */   
/*     */   public void setDisplayName(Component debug1) {
/*  71 */     if (debug1 == null) {
/*  72 */       throw new IllegalArgumentException("Name cannot be null");
/*     */     }
/*  74 */     this.displayName = debug1;
/*  75 */     this.scoreboard.onTeamChanged(this);
/*     */   }
/*     */   
/*     */   public void setPlayerPrefix(@Nullable Component debug1) {
/*  79 */     this.playerPrefix = (debug1 == null) ? TextComponent.EMPTY : debug1;
/*  80 */     this.scoreboard.onTeamChanged(this);
/*     */   }
/*     */   
/*     */   public Component getPlayerPrefix() {
/*  84 */     return this.playerPrefix;
/*     */   }
/*     */   
/*     */   public void setPlayerSuffix(@Nullable Component debug1) {
/*  88 */     this.playerSuffix = (debug1 == null) ? TextComponent.EMPTY : debug1;
/*  89 */     this.scoreboard.onTeamChanged(this);
/*     */   }
/*     */   
/*     */   public Component getPlayerSuffix() {
/*  93 */     return this.playerSuffix;
/*     */   }
/*     */ 
/*     */   
/*     */   public Collection<String> getPlayers() {
/*  98 */     return this.players;
/*     */   }
/*     */ 
/*     */   
/*     */   public MutableComponent getFormattedName(Component debug1) {
/* 103 */     MutableComponent debug2 = (new TextComponent("")).append(this.playerPrefix).append(debug1).append(this.playerSuffix);
/*     */     
/* 105 */     ChatFormatting debug3 = getColor();
/* 106 */     if (debug3 != ChatFormatting.RESET) {
/* 107 */       debug2.withStyle(debug3);
/*     */     }
/*     */     
/* 110 */     return debug2;
/*     */   }
/*     */   
/*     */   public static MutableComponent formatNameForTeam(@Nullable Team debug0, Component debug1) {
/* 114 */     if (debug0 == null) {
/* 115 */       return debug1.copy();
/*     */     }
/* 117 */     return debug0.getFormattedName(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isAllowFriendlyFire() {
/* 122 */     return this.allowFriendlyFire;
/*     */   }
/*     */   
/*     */   public void setAllowFriendlyFire(boolean debug1) {
/* 126 */     this.allowFriendlyFire = debug1;
/* 127 */     this.scoreboard.onTeamChanged(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canSeeFriendlyInvisibles() {
/* 132 */     return this.seeFriendlyInvisibles;
/*     */   }
/*     */   
/*     */   public void setSeeFriendlyInvisibles(boolean debug1) {
/* 136 */     this.seeFriendlyInvisibles = debug1;
/* 137 */     this.scoreboard.onTeamChanged(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public Team.Visibility getNameTagVisibility() {
/* 142 */     return this.nameTagVisibility;
/*     */   }
/*     */ 
/*     */   
/*     */   public Team.Visibility getDeathMessageVisibility() {
/* 147 */     return this.deathMessageVisibility;
/*     */   }
/*     */   
/*     */   public void setNameTagVisibility(Team.Visibility debug1) {
/* 151 */     this.nameTagVisibility = debug1;
/* 152 */     this.scoreboard.onTeamChanged(this);
/*     */   }
/*     */   
/*     */   public void setDeathMessageVisibility(Team.Visibility debug1) {
/* 156 */     this.deathMessageVisibility = debug1;
/* 157 */     this.scoreboard.onTeamChanged(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public Team.CollisionRule getCollisionRule() {
/* 162 */     return this.collisionRule;
/*     */   }
/*     */   
/*     */   public void setCollisionRule(Team.CollisionRule debug1) {
/* 166 */     this.collisionRule = debug1;
/* 167 */     this.scoreboard.onTeamChanged(this);
/*     */   }
/*     */   
/*     */   public int packOptions() {
/* 171 */     int debug1 = 0;
/*     */     
/* 173 */     if (isAllowFriendlyFire()) {
/* 174 */       debug1 |= 0x1;
/*     */     }
/* 176 */     if (canSeeFriendlyInvisibles()) {
/* 177 */       debug1 |= 0x2;
/*     */     }
/*     */     
/* 180 */     return debug1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setColor(ChatFormatting debug1) {
/* 189 */     this.color = debug1;
/* 190 */     this.scoreboard.onTeamChanged(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public ChatFormatting getColor() {
/* 195 */     return this.color;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\scores\PlayerTeam.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */