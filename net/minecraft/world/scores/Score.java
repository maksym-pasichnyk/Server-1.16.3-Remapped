/*    */ package net.minecraft.world.scores;
/*    */ import java.util.Comparator;
/*    */ import javax.annotation.Nullable;
/*    */ 
/*    */ public class Score {
/*    */   static {
/*  7 */     SCORE_COMPARATOR = ((debug0, debug1) -> (debug0.getScore() > debug1.getScore()) ? 1 : ((debug0.getScore() < debug1.getScore()) ? -1 : debug1.getOwner().compareToIgnoreCase(debug0.getOwner())));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final Comparator<Score> SCORE_COMPARATOR;
/*    */   
/*    */   private final Scoreboard scoreboard;
/*    */   
/*    */   @Nullable
/*    */   private final Objective objective;
/*    */   
/*    */   private final String owner;
/*    */   
/*    */   private int count;
/*    */   
/*    */   private boolean locked;
/*    */   private boolean forceUpdate;
/*    */   
/*    */   public Score(Scoreboard debug1, Objective debug2, String debug3) {
/* 26 */     this.scoreboard = debug1;
/* 27 */     this.objective = debug2;
/* 28 */     this.owner = debug3;
/* 29 */     this.locked = true;
/* 30 */     this.forceUpdate = true;
/*    */   }
/*    */   
/*    */   public void add(int debug1) {
/* 34 */     if (this.objective.getCriteria().isReadOnly()) {
/* 35 */       throw new IllegalStateException("Cannot modify read-only score");
/*    */     }
/* 37 */     setScore(getScore() + debug1);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void increment() {
/* 45 */     add(1);
/*    */   }
/*    */   
/*    */   public int getScore() {
/* 49 */     return this.count;
/*    */   }
/*    */   
/*    */   public void reset() {
/* 53 */     setScore(0);
/*    */   }
/*    */   
/*    */   public void setScore(int debug1) {
/* 57 */     int debug2 = this.count;
/* 58 */     this.count = debug1;
/* 59 */     if (debug2 != debug1 || this.forceUpdate) {
/* 60 */       this.forceUpdate = false;
/* 61 */       getScoreboard().onScoreChanged(this);
/*    */     } 
/*    */   }
/*    */   
/*    */   @Nullable
/*    */   public Objective getObjective() {
/* 67 */     return this.objective;
/*    */   }
/*    */   
/*    */   public String getOwner() {
/* 71 */     return this.owner;
/*    */   }
/*    */   
/*    */   public Scoreboard getScoreboard() {
/* 75 */     return this.scoreboard;
/*    */   }
/*    */   
/*    */   public boolean isLocked() {
/* 79 */     return this.locked;
/*    */   }
/*    */   
/*    */   public void setLocked(boolean debug1) {
/* 83 */     this.locked = debug1;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\scores\Score.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */