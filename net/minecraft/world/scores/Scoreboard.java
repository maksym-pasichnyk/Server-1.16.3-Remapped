/*     */ package net.minecraft.world.scores;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Maps;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.function.Consumer;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.ListTag;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.scores.criteria.ObjectiveCriteria;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Scoreboard
/*     */ {
/*  29 */   private final Map<String, Objective> objectivesByName = Maps.newHashMap();
/*  30 */   private final Map<ObjectiveCriteria, List<Objective>> objectivesByCriteria = Maps.newHashMap();
/*  31 */   private final Map<String, Map<Objective, Score>> playerScores = Maps.newHashMap();
/*  32 */   private final Objective[] displayObjectives = new Objective[19];
/*  33 */   private final Map<String, PlayerTeam> teamsByName = Maps.newHashMap();
/*  34 */   private final Map<String, PlayerTeam> teamsByPlayer = Maps.newHashMap();
/*     */ 
/*     */   
/*     */   private static String[] displaySlotNames;
/*     */ 
/*     */   
/*     */   public Objective getOrCreateObjective(String debug1) {
/*  41 */     return this.objectivesByName.get(debug1);
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public Objective getObjective(@Nullable String debug1) {
/*  46 */     return this.objectivesByName.get(debug1);
/*     */   }
/*     */   
/*     */   public Objective addObjective(String debug1, ObjectiveCriteria debug2, Component debug3, ObjectiveCriteria.RenderType debug4) {
/*  50 */     if (debug1.length() > 16) {
/*  51 */       throw new IllegalArgumentException("The objective name '" + debug1 + "' is too long!");
/*     */     }
/*  53 */     if (this.objectivesByName.containsKey(debug1)) {
/*  54 */       throw new IllegalArgumentException("An objective with the name '" + debug1 + "' already exists!");
/*     */     }
/*     */     
/*  57 */     Objective debug5 = new Objective(this, debug1, debug2, debug3, debug4);
/*     */     
/*  59 */     ((List<Objective>)this.objectivesByCriteria.computeIfAbsent(debug2, debug0 -> Lists.newArrayList())).add(debug5);
/*  60 */     this.objectivesByName.put(debug1, debug5);
/*  61 */     onObjectiveAdded(debug5);
/*  62 */     return debug5;
/*     */   }
/*     */   
/*     */   public final void forAllObjectives(ObjectiveCriteria debug1, String debug2, Consumer<Score> debug3) {
/*  66 */     ((List)this.objectivesByCriteria.getOrDefault(debug1, Collections.emptyList())).forEach(debug3 -> debug1.accept(getOrCreatePlayerScore(debug2, debug3)));
/*     */   }
/*     */   
/*     */   public boolean hasPlayerScore(String debug1, Objective debug2) {
/*  70 */     Map<Objective, Score> debug3 = this.playerScores.get(debug1);
/*  71 */     if (debug3 == null) {
/*  72 */       return false;
/*     */     }
/*  74 */     Score debug4 = debug3.get(debug2);
/*  75 */     return (debug4 != null);
/*     */   }
/*     */   
/*     */   public Score getOrCreatePlayerScore(String debug1, Objective debug2) {
/*  79 */     if (debug1.length() > 40) {
/*  80 */       throw new IllegalArgumentException("The player name '" + debug1 + "' is too long!");
/*     */     }
/*  82 */     Map<Objective, Score> debug3 = this.playerScores.computeIfAbsent(debug1, debug0 -> Maps.newHashMap());
/*     */     
/*  84 */     return debug3.computeIfAbsent(debug2, debug2 -> {
/*     */           Score debug3 = new Score(this, debug2, debug1);
/*     */           debug3.setScore(0);
/*     */           return debug3;
/*     */         });
/*     */   }
/*     */   
/*     */   public Collection<Score> getPlayerScores(Objective debug1) {
/*  92 */     List<Score> debug2 = Lists.newArrayList();
/*     */     
/*  94 */     for (Map<Objective, Score> debug4 : this.playerScores.values()) {
/*  95 */       Score debug5 = debug4.get(debug1);
/*  96 */       if (debug5 != null) {
/*  97 */         debug2.add(debug5);
/*     */       }
/*     */     } 
/*     */     
/* 101 */     debug2.sort(Score.SCORE_COMPARATOR);
/*     */     
/* 103 */     return debug2;
/*     */   }
/*     */   
/*     */   public Collection<Objective> getObjectives() {
/* 107 */     return this.objectivesByName.values();
/*     */   }
/*     */   
/*     */   public Collection<String> getObjectiveNames() {
/* 111 */     return this.objectivesByName.keySet();
/*     */   }
/*     */   
/*     */   public Collection<String> getTrackedPlayers() {
/* 115 */     return Lists.newArrayList(this.playerScores.keySet());
/*     */   }
/*     */   
/*     */   public void resetPlayerScore(String debug1, @Nullable Objective debug2) {
/* 119 */     if (debug2 == null) {
/* 120 */       Map<Objective, Score> debug3 = this.playerScores.remove(debug1);
/* 121 */       if (debug3 != null) {
/* 122 */         onPlayerRemoved(debug1);
/*     */       }
/*     */     } else {
/* 125 */       Map<Objective, Score> debug3 = this.playerScores.get(debug1);
/* 126 */       if (debug3 != null) {
/* 127 */         Score debug4 = debug3.remove(debug2);
/* 128 */         if (debug3.size() < 1) {
/* 129 */           Map<Objective, Score> debug5 = this.playerScores.remove(debug1);
/* 130 */           if (debug5 != null) {
/* 131 */             onPlayerRemoved(debug1);
/*     */           }
/* 133 */         } else if (debug4 != null) {
/* 134 */           onPlayerScoreRemoved(debug1, debug2);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public Map<Objective, Score> getPlayerScores(String debug1) {
/* 141 */     Map<Objective, Score> debug2 = this.playerScores.get(debug1);
/* 142 */     if (debug2 == null) {
/* 143 */       debug2 = Maps.newHashMap();
/*     */     }
/* 145 */     return debug2;
/*     */   }
/*     */   
/*     */   public void removeObjective(Objective debug1) {
/* 149 */     this.objectivesByName.remove(debug1.getName());
/*     */     
/* 151 */     for (int i = 0; i < 19; i++) {
/* 152 */       if (getDisplayObjective(i) == debug1) {
/* 153 */         setDisplayObjective(i, null);
/*     */       }
/*     */     } 
/*     */     
/* 157 */     List<Objective> debug2 = this.objectivesByCriteria.get(debug1.getCriteria());
/* 158 */     if (debug2 != null) {
/* 159 */       debug2.remove(debug1);
/*     */     }
/*     */     
/* 162 */     for (Map<Objective, Score> debug4 : this.playerScores.values()) {
/* 163 */       debug4.remove(debug1);
/*     */     }
/*     */     
/* 166 */     onObjectiveRemoved(debug1);
/*     */   }
/*     */   
/*     */   public void setDisplayObjective(int debug1, @Nullable Objective debug2) {
/* 170 */     this.displayObjectives[debug1] = debug2;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public Objective getDisplayObjective(int debug1) {
/* 175 */     return this.displayObjectives[debug1];
/*     */   }
/*     */   
/*     */   public PlayerTeam getPlayerTeam(String debug1) {
/* 179 */     return this.teamsByName.get(debug1);
/*     */   }
/*     */   
/*     */   public PlayerTeam addPlayerTeam(String debug1) {
/* 183 */     if (debug1.length() > 16) {
/* 184 */       throw new IllegalArgumentException("The team name '" + debug1 + "' is too long!");
/*     */     }
/* 186 */     PlayerTeam debug2 = getPlayerTeam(debug1);
/* 187 */     if (debug2 != null) {
/* 188 */       throw new IllegalArgumentException("A team with the name '" + debug1 + "' already exists!");
/*     */     }
/*     */     
/* 191 */     debug2 = new PlayerTeam(this, debug1);
/* 192 */     this.teamsByName.put(debug1, debug2);
/* 193 */     onTeamAdded(debug2);
/*     */     
/* 195 */     return debug2;
/*     */   }
/*     */   
/*     */   public void removePlayerTeam(PlayerTeam debug1) {
/* 199 */     this.teamsByName.remove(debug1.getName());
/*     */ 
/*     */ 
/*     */     
/* 203 */     for (String debug3 : debug1.getPlayers()) {
/* 204 */       this.teamsByPlayer.remove(debug3);
/*     */     }
/*     */     
/* 207 */     onTeamRemoved(debug1);
/*     */   }
/*     */   
/*     */   public boolean addPlayerToTeam(String debug1, PlayerTeam debug2) {
/* 211 */     if (debug1.length() > 40) {
/* 212 */       throw new IllegalArgumentException("The player name '" + debug1 + "' is too long!");
/*     */     }
/*     */     
/* 215 */     if (getPlayersTeam(debug1) != null) {
/* 216 */       removePlayerFromTeam(debug1);
/*     */     }
/*     */     
/* 219 */     this.teamsByPlayer.put(debug1, debug2);
/* 220 */     return debug2.getPlayers().add(debug1);
/*     */   }
/*     */   
/*     */   public boolean removePlayerFromTeam(String debug1) {
/* 224 */     PlayerTeam debug2 = getPlayersTeam(debug1);
/*     */     
/* 226 */     if (debug2 != null) {
/* 227 */       removePlayerFromTeam(debug1, debug2);
/* 228 */       return true;
/*     */     } 
/* 230 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void removePlayerFromTeam(String debug1, PlayerTeam debug2) {
/* 235 */     if (getPlayersTeam(debug1) != debug2) {
/* 236 */       throw new IllegalStateException("Player is either on another team or not on any team. Cannot remove from team '" + debug2.getName() + "'.");
/*     */     }
/*     */     
/* 239 */     this.teamsByPlayer.remove(debug1);
/* 240 */     debug2.getPlayers().remove(debug1);
/*     */   }
/*     */   
/*     */   public Collection<String> getTeamNames() {
/* 244 */     return this.teamsByName.keySet();
/*     */   }
/*     */   
/*     */   public Collection<PlayerTeam> getPlayerTeams() {
/* 248 */     return this.teamsByName.values();
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public PlayerTeam getPlayersTeam(String debug1) {
/* 253 */     return this.teamsByPlayer.get(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onObjectiveAdded(Objective debug1) {}
/*     */ 
/*     */   
/*     */   public void onObjectiveChanged(Objective debug1) {}
/*     */ 
/*     */   
/*     */   public void onObjectiveRemoved(Objective debug1) {}
/*     */ 
/*     */   
/*     */   public void onScoreChanged(Score debug1) {}
/*     */ 
/*     */   
/*     */   public void onPlayerRemoved(String debug1) {}
/*     */ 
/*     */   
/*     */   public void onPlayerScoreRemoved(String debug1, Objective debug2) {}
/*     */ 
/*     */   
/*     */   public void onTeamAdded(PlayerTeam debug1) {}
/*     */ 
/*     */   
/*     */   public void onTeamChanged(PlayerTeam debug1) {}
/*     */ 
/*     */   
/*     */   public void onTeamRemoved(PlayerTeam debug1) {}
/*     */   
/*     */   public static String getDisplaySlotName(int debug0) {
/* 284 */     switch (debug0) {
/*     */       case 0:
/* 286 */         return "list";
/*     */       case 1:
/* 288 */         return "sidebar";
/*     */       case 2:
/* 290 */         return "belowName";
/*     */     } 
/* 292 */     if (debug0 >= 3 && debug0 <= 18) {
/* 293 */       ChatFormatting debug1 = ChatFormatting.getById(debug0 - 3);
/* 294 */       if (debug1 != null && debug1 != ChatFormatting.RESET) {
/* 295 */         return "sidebar.team." + debug1.getName();
/*     */       }
/*     */     } 
/* 298 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public static int getDisplaySlotByName(String debug0) {
/* 303 */     if ("list".equalsIgnoreCase(debug0))
/* 304 */       return 0; 
/* 305 */     if ("sidebar".equalsIgnoreCase(debug0))
/* 306 */       return 1; 
/* 307 */     if ("belowName".equalsIgnoreCase(debug0)) {
/* 308 */       return 2;
/*     */     }
/* 310 */     if (debug0.startsWith("sidebar.team.")) {
/* 311 */       String debug1 = debug0.substring("sidebar.team.".length());
/* 312 */       ChatFormatting debug2 = ChatFormatting.getByName(debug1);
/* 313 */       if (debug2 != null && debug2.getId() >= 0) {
/* 314 */         return debug2.getId() + 3;
/*     */       }
/*     */     } 
/* 317 */     return -1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String[] getDisplaySlotNames() {
/* 324 */     if (displaySlotNames == null) {
/* 325 */       displaySlotNames = new String[19];
/* 326 */       for (int debug0 = 0; debug0 < 19; debug0++) {
/* 327 */         displaySlotNames[debug0] = getDisplaySlotName(debug0);
/*     */       }
/*     */     } 
/* 330 */     return displaySlotNames;
/*     */   }
/*     */   
/*     */   public void entityRemoved(Entity debug1) {
/* 334 */     if (debug1 == null || debug1 instanceof net.minecraft.world.entity.player.Player || debug1.isAlive()) {
/*     */       return;
/*     */     }
/* 337 */     String debug2 = debug1.getStringUUID();
/* 338 */     resetPlayerScore(debug2, null);
/* 339 */     removePlayerFromTeam(debug2);
/*     */   }
/*     */   
/*     */   protected ListTag savePlayerScores() {
/* 343 */     ListTag debug1 = new ListTag();
/*     */     
/* 345 */     this.playerScores.values().stream().map(Map::values).forEach(debug1 -> debug1.stream().filter(()).forEach(()));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 356 */     return debug1;
/*     */   }
/*     */   
/*     */   protected void loadPlayerScores(ListTag debug1) {
/* 360 */     for (int debug2 = 0; debug2 < debug1.size(); debug2++) {
/* 361 */       CompoundTag debug3 = debug1.getCompound(debug2);
/*     */       
/* 363 */       Objective debug4 = getOrCreateObjective(debug3.getString("Objective"));
/* 364 */       String debug5 = debug3.getString("Name");
/* 365 */       if (debug5.length() > 40)
/*     */       {
/* 367 */         debug5 = debug5.substring(0, 40);
/*     */       }
/* 369 */       Score debug6 = getOrCreatePlayerScore(debug5, debug4);
/* 370 */       debug6.setScore(debug3.getInt("Score"));
/* 371 */       if (debug3.contains("Locked"))
/* 372 */         debug6.setLocked(debug3.getBoolean("Locked")); 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\scores\Scoreboard.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */