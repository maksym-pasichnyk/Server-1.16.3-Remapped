/*    */ package net.minecraft.world.entity.schedule;
/*    */ 
/*    */ import com.google.common.collect.Lists;
/*    */ import java.util.List;
/*    */ import java.util.Set;
/*    */ import java.util.stream.Collectors;
/*    */ 
/*    */ public class ScheduleBuilder {
/*    */   private final Schedule schedule;
/* 10 */   private final List<ActivityTransition> transitions = Lists.newArrayList();
/*    */   
/*    */   public ScheduleBuilder(Schedule debug1) {
/* 13 */     this.schedule = debug1;
/*    */   }
/*    */   
/*    */   public ScheduleBuilder changeActivityAt(int debug1, Activity debug2) {
/* 17 */     this.transitions.add(new ActivityTransition(debug1, debug2));
/* 18 */     return this;
/*    */   }
/*    */   
/*    */   public Schedule build() {
/* 22 */     ((Set)this.transitions.stream()
/* 23 */       .map(ActivityTransition::getActivity)
/* 24 */       .collect(Collectors.toSet()))
/* 25 */       .forEach(this.schedule::ensureTimelineExistsFor);
/*    */     
/* 27 */     this.transitions.forEach(debug1 -> {
/*    */           Activity debug2 = debug1.getActivity();
/*    */ 
/*    */ 
/*    */           
/*    */           this.schedule.getAllTimelinesExceptFor(debug2).forEach(());
/*    */ 
/*    */           
/*    */           this.schedule.getTimelineFor(debug2).addKeyframe(debug1.getTime(), 1.0F);
/*    */         });
/*    */ 
/*    */     
/* 39 */     return this.schedule;
/*    */   }
/*    */   
/*    */   static class ActivityTransition {
/*    */     private final int time;
/*    */     private final Activity activity;
/*    */     
/*    */     public ActivityTransition(int debug1, Activity debug2) {
/* 47 */       this.time = debug1;
/* 48 */       this.activity = debug2;
/*    */     }
/*    */     
/*    */     public int getTime() {
/* 52 */       return this.time;
/*    */     }
/*    */     
/*    */     public Activity getActivity() {
/* 56 */       return this.activity;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\schedule\ScheduleBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */