/*    */ package net.minecraft.world.entity.schedule;
/*    */ 
/*    */ import com.google.common.collect.Maps;
/*    */ import java.util.Comparator;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.stream.Collectors;
/*    */ import net.minecraft.core.Registry;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Schedule
/*    */ {
/* 15 */   public static final Schedule EMPTY = register("empty")
/* 16 */     .changeActivityAt(0, Activity.IDLE)
/* 17 */     .build();
/* 18 */   public static final Schedule SIMPLE = register("simple")
/* 19 */     .changeActivityAt(5000, Activity.WORK)
/* 20 */     .changeActivityAt(11000, Activity.REST)
/* 21 */     .build();
/* 22 */   public static final Schedule VILLAGER_BABY = register("villager_baby")
/* 23 */     .changeActivityAt(10, Activity.IDLE)
/* 24 */     .changeActivityAt(3000, Activity.PLAY)
/* 25 */     .changeActivityAt(6000, Activity.IDLE)
/* 26 */     .changeActivityAt(10000, Activity.PLAY)
/* 27 */     .changeActivityAt(12000, Activity.REST)
/* 28 */     .build();
/* 29 */   public static final Schedule VILLAGER_DEFAULT = register("villager_default")
/* 30 */     .changeActivityAt(10, Activity.IDLE)
/* 31 */     .changeActivityAt(2000, Activity.WORK)
/* 32 */     .changeActivityAt(9000, Activity.MEET)
/* 33 */     .changeActivityAt(11000, Activity.IDLE)
/* 34 */     .changeActivityAt(12000, Activity.REST)
/* 35 */     .build();
/* 36 */   private final Map<Activity, Timeline> timelines = Maps.newHashMap();
/*    */   
/*    */   protected static ScheduleBuilder register(String debug0) {
/* 39 */     Schedule debug1 = (Schedule)Registry.register(Registry.SCHEDULE, debug0, new Schedule());
/* 40 */     return new ScheduleBuilder(debug1);
/*    */   }
/*    */   
/*    */   protected void ensureTimelineExistsFor(Activity debug1) {
/* 44 */     if (!this.timelines.containsKey(debug1)) {
/* 45 */       this.timelines.put(debug1, new Timeline());
/*    */     }
/*    */   }
/*    */   
/*    */   protected Timeline getTimelineFor(Activity debug1) {
/* 50 */     return this.timelines.get(debug1);
/*    */   }
/*    */   
/*    */   protected List<Timeline> getAllTimelinesExceptFor(Activity debug1) {
/* 54 */     return (List<Timeline>)this.timelines.entrySet()
/* 55 */       .stream()
/* 56 */       .filter(debug1 -> (debug1.getKey() != debug0))
/* 57 */       .map(Map.Entry::getValue)
/* 58 */       .collect(Collectors.toList());
/*    */   }
/*    */   
/*    */   public Activity getActivityAt(int debug1) {
/* 62 */     return this.timelines.entrySet()
/* 63 */       .stream()
/* 64 */       .max(Comparator.comparingDouble(debug1 -> ((Timeline)debug1.getValue()).getValueAt(debug0)))
/* 65 */       .map(Map.Entry::getKey)
/* 66 */       .orElse(Activity.IDLE);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\schedule\Schedule.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */