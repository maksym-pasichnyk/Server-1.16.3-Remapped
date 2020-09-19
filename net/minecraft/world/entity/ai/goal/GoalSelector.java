/*     */ package net.minecraft.world.entity.ai.goal;
/*     */ 
/*     */ import com.google.common.collect.Sets;
/*     */ import java.util.EnumMap;
/*     */ import java.util.EnumSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.function.Supplier;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.util.profiling.ProfilerFiller;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ 
/*     */ public class GoalSelector
/*     */ {
/*  17 */   private static final Logger LOGGER = LogManager.getLogger();
/*  18 */   private static final WrappedGoal NO_GOAL = new WrappedGoal(2147483647, new Goal()
/*     */       {
/*     */         public boolean canUse() {
/*  21 */           return false;
/*     */         }
/*     */       })
/*     */     {
/*     */       public boolean isRunning() {
/*  26 */         return false;
/*     */       }
/*     */     };
/*     */   
/*  30 */   private final Map<Goal.Flag, WrappedGoal> lockedFlags = new EnumMap<>(Goal.Flag.class);
/*  31 */   private final Set<WrappedGoal> availableGoals = Sets.newLinkedHashSet();
/*     */   private final Supplier<ProfilerFiller> profiler;
/*  33 */   private final EnumSet<Goal.Flag> disabledFlags = EnumSet.noneOf(Goal.Flag.class);
/*     */   
/*  35 */   private int newGoalRate = 3;
/*     */   
/*     */   public GoalSelector(Supplier<ProfilerFiller> debug1) {
/*  38 */     this.profiler = debug1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addGoal(int debug1, Goal debug2) {
/*  46 */     this.availableGoals.add(new WrappedGoal(debug1, debug2));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeGoal(Goal debug1) {
/*  55 */     this.availableGoals.stream().filter(debug1 -> (debug1.getGoal() == debug0)).filter(WrappedGoal::isRunning).forEach(WrappedGoal::stop);
/*  56 */     this.availableGoals.removeIf(debug1 -> (debug1.getGoal() == debug0));
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/*  61 */     ProfilerFiller debug1 = this.profiler.get();
/*  62 */     debug1.push("goalCleanup");
/*  63 */     getRunningGoals().filter(debug1 -> (!debug1.isRunning() || debug1.getFlags().stream().anyMatch(this.disabledFlags::contains) || !debug1.canContinueToUse())).forEach(Goal::stop);
/*  64 */     this.lockedFlags.forEach((debug1, debug2) -> {
/*     */           if (!debug2.isRunning()) {
/*     */             this.lockedFlags.remove(debug1);
/*     */           }
/*     */         });
/*  69 */     debug1.pop();
/*     */     
/*  71 */     debug1.push("goalUpdate");
/*  72 */     this.availableGoals.stream()
/*  73 */       .filter(debug0 -> !debug0.isRunning())
/*  74 */       .filter(debug1 -> debug1.getFlags().stream().noneMatch(this.disabledFlags::contains))
/*  75 */       .filter(debug1 -> debug1.getFlags().stream().allMatch(()))
/*  76 */       .filter(WrappedGoal::canUse)
/*  77 */       .forEach(debug1 -> {
/*     */           debug1.getFlags().forEach(());
/*     */ 
/*     */           
/*     */           debug1.start();
/*     */         });
/*     */ 
/*     */     
/*  85 */     debug1.pop();
/*     */     
/*  87 */     debug1.push("goalTick");
/*  88 */     getRunningGoals().forEach(WrappedGoal::tick);
/*  89 */     debug1.pop();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Stream<WrappedGoal> getRunningGoals() {
/*  97 */     return this.availableGoals.stream().filter(WrappedGoal::isRunning);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void disableControlFlag(Goal.Flag debug1) {
/* 105 */     this.disabledFlags.add(debug1);
/*     */   }
/*     */   
/*     */   public void enableControlFlag(Goal.Flag debug1) {
/* 109 */     this.disabledFlags.remove(debug1);
/*     */   }
/*     */   
/*     */   public void setControlFlag(Goal.Flag debug1, boolean debug2) {
/* 113 */     if (debug2) {
/* 114 */       enableControlFlag(debug1);
/*     */     } else {
/* 116 */       disableControlFlag(debug1);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\goal\GoalSelector.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */