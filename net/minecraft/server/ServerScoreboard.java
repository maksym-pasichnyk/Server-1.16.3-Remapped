/*     */ package net.minecraft.server;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Sets;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ClientboundSetDisplayObjectivePacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundSetObjectivePacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundSetPlayerTeamPacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundSetScorePacket;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.world.scores.Objective;
/*     */ import net.minecraft.world.scores.PlayerTeam;
/*     */ import net.minecraft.world.scores.Score;
/*     */ import net.minecraft.world.scores.Scoreboard;
/*     */ 
/*     */ public class ServerScoreboard
/*     */   extends Scoreboard {
/*     */   private final MinecraftServer server;
/*  23 */   private final Set<Objective> trackedObjectives = Sets.newHashSet();
/*  24 */   private Runnable[] dirtyListeners = new Runnable[0];
/*     */   
/*     */   public ServerScoreboard(MinecraftServer debug1) {
/*  27 */     this.server = debug1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void onScoreChanged(Score debug1) {
/*  36 */     super.onScoreChanged(debug1);
/*     */     
/*  38 */     if (this.trackedObjectives.contains(debug1.getObjective())) {
/*  39 */       this.server.getPlayerList().broadcastAll((Packet)new ClientboundSetScorePacket(Method.CHANGE, debug1.getObjective().getName(), debug1.getOwner(), debug1.getScore()));
/*     */     }
/*     */     
/*  42 */     setDirty();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onPlayerRemoved(String debug1) {
/*  47 */     super.onPlayerRemoved(debug1);
/*  48 */     this.server.getPlayerList().broadcastAll((Packet)new ClientboundSetScorePacket(Method.REMOVE, null, debug1, 0));
/*  49 */     setDirty();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onPlayerScoreRemoved(String debug1, Objective debug2) {
/*  54 */     super.onPlayerScoreRemoved(debug1, debug2);
/*  55 */     if (this.trackedObjectives.contains(debug2)) {
/*  56 */       this.server.getPlayerList().broadcastAll((Packet)new ClientboundSetScorePacket(Method.REMOVE, debug2.getName(), debug1, 0));
/*     */     }
/*  58 */     setDirty();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setDisplayObjective(int debug1, @Nullable Objective debug2) {
/*  63 */     Objective debug3 = getDisplayObjective(debug1);
/*     */     
/*  65 */     super.setDisplayObjective(debug1, debug2);
/*     */     
/*  67 */     if (debug3 != debug2 && debug3 != null) {
/*  68 */       if (getObjectiveDisplaySlotCount(debug3) > 0) {
/*  69 */         this.server.getPlayerList().broadcastAll((Packet)new ClientboundSetDisplayObjectivePacket(debug1, debug2));
/*     */       } else {
/*  71 */         stopTrackingObjective(debug3);
/*     */       } 
/*     */     }
/*     */     
/*  75 */     if (debug2 != null) {
/*  76 */       if (this.trackedObjectives.contains(debug2)) {
/*  77 */         this.server.getPlayerList().broadcastAll((Packet)new ClientboundSetDisplayObjectivePacket(debug1, debug2));
/*     */       } else {
/*  79 */         startTrackingObjective(debug2);
/*     */       } 
/*     */     }
/*     */     
/*  83 */     setDirty();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean addPlayerToTeam(String debug1, PlayerTeam debug2) {
/*  88 */     if (super.addPlayerToTeam(debug1, debug2)) {
/*  89 */       this.server.getPlayerList().broadcastAll((Packet)new ClientboundSetPlayerTeamPacket(debug2, Arrays.asList(new String[] { debug1 }, ), 3));
/*  90 */       setDirty();
/*     */       
/*  92 */       return true;
/*     */     } 
/*     */     
/*  95 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void removePlayerFromTeam(String debug1, PlayerTeam debug2) {
/* 100 */     super.removePlayerFromTeam(debug1, debug2);
/*     */     
/* 102 */     this.server.getPlayerList().broadcastAll((Packet)new ClientboundSetPlayerTeamPacket(debug2, Arrays.asList(new String[] { debug1 }, ), 4));
/*     */     
/* 104 */     setDirty();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onObjectiveAdded(Objective debug1) {
/* 109 */     super.onObjectiveAdded(debug1);
/* 110 */     setDirty();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onObjectiveChanged(Objective debug1) {
/* 115 */     super.onObjectiveChanged(debug1);
/*     */     
/* 117 */     if (this.trackedObjectives.contains(debug1)) {
/* 118 */       this.server.getPlayerList().broadcastAll((Packet)new ClientboundSetObjectivePacket(debug1, 2));
/*     */     }
/*     */     
/* 121 */     setDirty();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onObjectiveRemoved(Objective debug1) {
/* 126 */     super.onObjectiveRemoved(debug1);
/*     */     
/* 128 */     if (this.trackedObjectives.contains(debug1)) {
/* 129 */       stopTrackingObjective(debug1);
/*     */     }
/*     */     
/* 132 */     setDirty();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onTeamAdded(PlayerTeam debug1) {
/* 137 */     super.onTeamAdded(debug1);
/*     */     
/* 139 */     this.server.getPlayerList().broadcastAll((Packet)new ClientboundSetPlayerTeamPacket(debug1, 0));
/*     */     
/* 141 */     setDirty();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onTeamChanged(PlayerTeam debug1) {
/* 146 */     super.onTeamChanged(debug1);
/*     */     
/* 148 */     this.server.getPlayerList().broadcastAll((Packet)new ClientboundSetPlayerTeamPacket(debug1, 2));
/*     */     
/* 150 */     setDirty();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onTeamRemoved(PlayerTeam debug1) {
/* 155 */     super.onTeamRemoved(debug1);
/*     */     
/* 157 */     this.server.getPlayerList().broadcastAll((Packet)new ClientboundSetPlayerTeamPacket(debug1, 1));
/*     */     
/* 159 */     setDirty();
/*     */   }
/*     */   
/*     */   public void addDirtyListener(Runnable debug1) {
/* 163 */     this.dirtyListeners = Arrays.<Runnable>copyOf(this.dirtyListeners, this.dirtyListeners.length + 1);
/* 164 */     this.dirtyListeners[this.dirtyListeners.length - 1] = debug1;
/*     */   }
/*     */   
/*     */   protected void setDirty() {
/* 168 */     for (Runnable debug4 : this.dirtyListeners) {
/* 169 */       debug4.run();
/*     */     }
/*     */   }
/*     */   
/*     */   public List<Packet<?>> getStartTrackingPackets(Objective debug1) {
/* 174 */     List<Packet<?>> debug2 = Lists.newArrayList();
/* 175 */     debug2.add(new ClientboundSetObjectivePacket(debug1, 0));
/*     */     
/* 177 */     for (int debug3 = 0; debug3 < 19; debug3++) {
/* 178 */       if (getDisplayObjective(debug3) == debug1) {
/* 179 */         debug2.add(new ClientboundSetDisplayObjectivePacket(debug3, debug1));
/*     */       }
/*     */     } 
/*     */     
/* 183 */     for (Score debug4 : getPlayerScores(debug1)) {
/* 184 */       debug2.add(new ClientboundSetScorePacket(Method.CHANGE, debug4.getObjective().getName(), debug4.getOwner(), debug4.getScore()));
/*     */     }
/*     */     
/* 187 */     return debug2;
/*     */   }
/*     */   
/*     */   public void startTrackingObjective(Objective debug1) {
/* 191 */     List<Packet<?>> debug2 = getStartTrackingPackets(debug1);
/*     */     
/* 193 */     for (ServerPlayer debug4 : this.server.getPlayerList().getPlayers()) {
/* 194 */       for (Packet<?> debug6 : debug2) {
/* 195 */         debug4.connection.send(debug6);
/*     */       }
/*     */     } 
/*     */     
/* 199 */     this.trackedObjectives.add(debug1);
/*     */   }
/*     */   
/*     */   public List<Packet<?>> getStopTrackingPackets(Objective debug1) {
/* 203 */     List<Packet<?>> debug2 = Lists.newArrayList();
/* 204 */     debug2.add(new ClientboundSetObjectivePacket(debug1, 1));
/*     */     
/* 206 */     for (int debug3 = 0; debug3 < 19; debug3++) {
/* 207 */       if (getDisplayObjective(debug3) == debug1) {
/* 208 */         debug2.add(new ClientboundSetDisplayObjectivePacket(debug3, debug1));
/*     */       }
/*     */     } 
/*     */     
/* 212 */     return debug2;
/*     */   }
/*     */   
/*     */   public void stopTrackingObjective(Objective debug1) {
/* 216 */     List<Packet<?>> debug2 = getStopTrackingPackets(debug1);
/*     */     
/* 218 */     for (ServerPlayer debug4 : this.server.getPlayerList().getPlayers()) {
/* 219 */       for (Packet<?> debug6 : debug2) {
/* 220 */         debug4.connection.send(debug6);
/*     */       }
/*     */     } 
/*     */     
/* 224 */     this.trackedObjectives.remove(debug1);
/*     */   }
/*     */   
/*     */   public int getObjectiveDisplaySlotCount(Objective debug1) {
/* 228 */     int debug2 = 0;
/*     */     
/* 230 */     for (int debug3 = 0; debug3 < 19; debug3++) {
/* 231 */       if (getDisplayObjective(debug3) == debug1) {
/* 232 */         debug2++;
/*     */       }
/*     */     } 
/*     */     
/* 236 */     return debug2;
/*     */   }
/*     */   
/*     */   public enum Method {
/* 240 */     CHANGE,
/* 241 */     REMOVE;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\ServerScoreboard.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */