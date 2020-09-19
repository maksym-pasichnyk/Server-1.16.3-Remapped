/*     */ package net.minecraft.world.scores;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.ListTag;
/*     */ import net.minecraft.nbt.StringTag;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.world.level.saveddata.SavedData;
/*     */ import net.minecraft.world.scores.criteria.ObjectiveCriteria;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public class ScoreboardSaveData extends SavedData {
/*  17 */   private static final Logger LOGGER = LogManager.getLogger();
/*     */   
/*     */   private Scoreboard scoreboard;
/*     */   
/*     */   private CompoundTag delayLoad;
/*     */   
/*     */   public ScoreboardSaveData() {
/*  24 */     super("scoreboard");
/*     */   }
/*     */   
/*     */   public void setScoreboard(Scoreboard debug1) {
/*  28 */     this.scoreboard = debug1;
/*     */     
/*  30 */     if (this.delayLoad != null) {
/*  31 */       load(this.delayLoad);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void load(CompoundTag debug1) {
/*  37 */     if (this.scoreboard == null) {
/*  38 */       this.delayLoad = debug1;
/*     */       
/*     */       return;
/*     */     } 
/*  42 */     loadObjectives(debug1.getList("Objectives", 10));
/*  43 */     this.scoreboard.loadPlayerScores(debug1.getList("PlayerScores", 10));
/*     */     
/*  45 */     if (debug1.contains("DisplaySlots", 10)) {
/*  46 */       loadDisplaySlots(debug1.getCompound("DisplaySlots"));
/*     */     }
/*     */     
/*  49 */     if (debug1.contains("Teams", 9)) {
/*  50 */       loadTeams(debug1.getList("Teams", 10));
/*     */     }
/*     */   }
/*     */   
/*     */   protected void loadTeams(ListTag debug1) {
/*  55 */     for (int debug2 = 0; debug2 < debug1.size(); debug2++) {
/*  56 */       CompoundTag debug3 = debug1.getCompound(debug2);
/*     */       
/*  58 */       String debug4 = debug3.getString("Name");
/*  59 */       if (debug4.length() > 16)
/*     */       {
/*  61 */         debug4 = debug4.substring(0, 16);
/*     */       }
/*  63 */       PlayerTeam debug5 = this.scoreboard.addPlayerTeam(debug4);
/*  64 */       MutableComponent mutableComponent = Component.Serializer.fromJson(debug3.getString("DisplayName"));
/*  65 */       if (mutableComponent != null) {
/*  66 */         debug5.setDisplayName((Component)mutableComponent);
/*     */       }
/*  68 */       if (debug3.contains("TeamColor", 8)) {
/*  69 */         debug5.setColor(ChatFormatting.getByName(debug3.getString("TeamColor")));
/*     */       }
/*  71 */       if (debug3.contains("AllowFriendlyFire", 99)) {
/*  72 */         debug5.setAllowFriendlyFire(debug3.getBoolean("AllowFriendlyFire"));
/*     */       }
/*  74 */       if (debug3.contains("SeeFriendlyInvisibles", 99)) {
/*  75 */         debug5.setSeeFriendlyInvisibles(debug3.getBoolean("SeeFriendlyInvisibles"));
/*     */       }
/*  77 */       if (debug3.contains("MemberNamePrefix", 8)) {
/*  78 */         MutableComponent mutableComponent1 = Component.Serializer.fromJson(debug3.getString("MemberNamePrefix"));
/*  79 */         if (mutableComponent1 != null) {
/*  80 */           debug5.setPlayerPrefix((Component)mutableComponent1);
/*     */         }
/*     */       } 
/*  83 */       if (debug3.contains("MemberNameSuffix", 8)) {
/*  84 */         MutableComponent mutableComponent1 = Component.Serializer.fromJson(debug3.getString("MemberNameSuffix"));
/*  85 */         if (mutableComponent1 != null) {
/*  86 */           debug5.setPlayerSuffix((Component)mutableComponent1);
/*     */         }
/*     */       } 
/*  89 */       if (debug3.contains("NameTagVisibility", 8)) {
/*  90 */         Team.Visibility debug7 = Team.Visibility.byName(debug3.getString("NameTagVisibility"));
/*  91 */         if (debug7 != null) {
/*  92 */           debug5.setNameTagVisibility(debug7);
/*     */         }
/*     */       } 
/*  95 */       if (debug3.contains("DeathMessageVisibility", 8)) {
/*  96 */         Team.Visibility debug7 = Team.Visibility.byName(debug3.getString("DeathMessageVisibility"));
/*  97 */         if (debug7 != null) {
/*  98 */           debug5.setDeathMessageVisibility(debug7);
/*     */         }
/*     */       } 
/* 101 */       if (debug3.contains("CollisionRule", 8)) {
/* 102 */         Team.CollisionRule debug7 = Team.CollisionRule.byName(debug3.getString("CollisionRule"));
/* 103 */         if (debug7 != null) {
/* 104 */           debug5.setCollisionRule(debug7);
/*     */         }
/*     */       } 
/*     */       
/* 108 */       loadTeamPlayers(debug5, debug3.getList("Players", 8));
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void loadTeamPlayers(PlayerTeam debug1, ListTag debug2) {
/* 113 */     for (int debug3 = 0; debug3 < debug2.size(); debug3++) {
/* 114 */       this.scoreboard.addPlayerToTeam(debug2.getString(debug3), debug1);
/*     */     }
/*     */   }
/*     */   
/*     */   protected void loadDisplaySlots(CompoundTag debug1) {
/* 119 */     for (int debug2 = 0; debug2 < 19; debug2++) {
/* 120 */       if (debug1.contains("slot_" + debug2, 8)) {
/* 121 */         String debug3 = debug1.getString("slot_" + debug2);
/* 122 */         Objective debug4 = this.scoreboard.getObjective(debug3);
/* 123 */         this.scoreboard.setDisplayObjective(debug2, debug4);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void loadObjectives(ListTag debug1) {
/* 129 */     for (int debug2 = 0; debug2 < debug1.size(); debug2++) {
/* 130 */       CompoundTag debug3 = debug1.getCompound(debug2);
/* 131 */       ObjectiveCriteria.byName(debug3.getString("CriteriaName")).ifPresent(debug2 -> {
/*     */             String debug3 = debug1.getString("Name");
/*     */             if (debug3.length() > 16) {
/*     */               debug3 = debug3.substring(0, 16);
/*     */             }
/*     */             MutableComponent mutableComponent = Component.Serializer.fromJson(debug1.getString("DisplayName"));
/*     */             ObjectiveCriteria.RenderType debug5 = ObjectiveCriteria.RenderType.byId(debug1.getString("RenderType"));
/*     */             this.scoreboard.addObjective(debug3, debug2, (Component)mutableComponent, debug5);
/*     */           });
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public CompoundTag save(CompoundTag debug1) {
/* 146 */     if (this.scoreboard == null) {
/* 147 */       LOGGER.warn("Tried to save scoreboard without having a scoreboard...");
/* 148 */       return debug1;
/*     */     } 
/*     */     
/* 151 */     debug1.put("Objectives", (Tag)saveObjectives());
/* 152 */     debug1.put("PlayerScores", (Tag)this.scoreboard.savePlayerScores());
/* 153 */     debug1.put("Teams", (Tag)saveTeams());
/*     */     
/* 155 */     saveDisplaySlots(debug1);
/*     */     
/* 157 */     return debug1;
/*     */   }
/*     */   
/*     */   protected ListTag saveTeams() {
/* 161 */     ListTag debug1 = new ListTag();
/* 162 */     Collection<PlayerTeam> debug2 = this.scoreboard.getPlayerTeams();
/*     */     
/* 164 */     for (PlayerTeam debug4 : debug2) {
/* 165 */       CompoundTag debug5 = new CompoundTag();
/*     */       
/* 167 */       debug5.putString("Name", debug4.getName());
/* 168 */       debug5.putString("DisplayName", Component.Serializer.toJson(debug4.getDisplayName()));
/* 169 */       if (debug4.getColor().getId() >= 0) {
/* 170 */         debug5.putString("TeamColor", debug4.getColor().getName());
/*     */       }
/* 172 */       debug5.putBoolean("AllowFriendlyFire", debug4.isAllowFriendlyFire());
/* 173 */       debug5.putBoolean("SeeFriendlyInvisibles", debug4.canSeeFriendlyInvisibles());
/* 174 */       debug5.putString("MemberNamePrefix", Component.Serializer.toJson(debug4.getPlayerPrefix()));
/* 175 */       debug5.putString("MemberNameSuffix", Component.Serializer.toJson(debug4.getPlayerSuffix()));
/* 176 */       debug5.putString("NameTagVisibility", (debug4.getNameTagVisibility()).name);
/* 177 */       debug5.putString("DeathMessageVisibility", (debug4.getDeathMessageVisibility()).name);
/* 178 */       debug5.putString("CollisionRule", (debug4.getCollisionRule()).name);
/*     */       
/* 180 */       ListTag debug6 = new ListTag();
/*     */       
/* 182 */       for (String debug8 : debug4.getPlayers()) {
/* 183 */         debug6.add(StringTag.valueOf(debug8));
/*     */       }
/*     */       
/* 186 */       debug5.put("Players", (Tag)debug6);
/*     */       
/* 188 */       debug1.add(debug5);
/*     */     } 
/*     */     
/* 191 */     return debug1;
/*     */   }
/*     */   
/*     */   protected void saveDisplaySlots(CompoundTag debug1) {
/* 195 */     CompoundTag debug2 = new CompoundTag();
/* 196 */     boolean debug3 = false;
/*     */     
/* 198 */     for (int debug4 = 0; debug4 < 19; debug4++) {
/* 199 */       Objective debug5 = this.scoreboard.getDisplayObjective(debug4);
/*     */       
/* 201 */       if (debug5 != null) {
/* 202 */         debug2.putString("slot_" + debug4, debug5.getName());
/* 203 */         debug3 = true;
/*     */       } 
/*     */     } 
/*     */     
/* 207 */     if (debug3) {
/* 208 */       debug1.put("DisplaySlots", (Tag)debug2);
/*     */     }
/*     */   }
/*     */   
/*     */   protected ListTag saveObjectives() {
/* 213 */     ListTag debug1 = new ListTag();
/* 214 */     Collection<Objective> debug2 = this.scoreboard.getObjectives();
/*     */     
/* 216 */     for (Objective debug4 : debug2) {
/* 217 */       if (debug4.getCriteria() == null) {
/*     */         continue;
/*     */       }
/*     */       
/* 221 */       CompoundTag debug5 = new CompoundTag();
/* 222 */       debug5.putString("Name", debug4.getName());
/* 223 */       debug5.putString("CriteriaName", debug4.getCriteria().getName());
/* 224 */       debug5.putString("DisplayName", Component.Serializer.toJson(debug4.getDisplayName()));
/* 225 */       debug5.putString("RenderType", debug4.getRenderType().getId());
/* 226 */       debug1.add(debug5);
/*     */     } 
/*     */     
/* 229 */     return debug1;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\scores\ScoreboardSaveData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */