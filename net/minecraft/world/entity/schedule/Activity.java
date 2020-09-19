/*    */ package net.minecraft.world.entity.schedule;
/*    */ 
/*    */ import net.minecraft.core.Registry;
/*    */ 
/*    */ public class Activity {
/*  6 */   public static final Activity CORE = register("core");
/*  7 */   public static final Activity IDLE = register("idle");
/*  8 */   public static final Activity WORK = register("work");
/*  9 */   public static final Activity PLAY = register("play");
/* 10 */   public static final Activity REST = register("rest");
/* 11 */   public static final Activity MEET = register("meet");
/* 12 */   public static final Activity PANIC = register("panic");
/* 13 */   public static final Activity RAID = register("raid");
/* 14 */   public static final Activity PRE_RAID = register("pre_raid");
/* 15 */   public static final Activity HIDE = register("hide");
/* 16 */   public static final Activity FIGHT = register("fight");
/* 17 */   public static final Activity CELEBRATE = register("celebrate");
/* 18 */   public static final Activity ADMIRE_ITEM = register("admire_item");
/* 19 */   public static final Activity AVOID = register("avoid");
/* 20 */   public static final Activity RIDE = register("ride");
/*    */   
/*    */   private final String name;
/*    */   private final int hashCode;
/*    */   
/*    */   private Activity(String debug1) {
/* 26 */     this.name = debug1;
/* 27 */     this.hashCode = debug1.hashCode();
/*    */   }
/*    */   
/*    */   public String getName() {
/* 31 */     return this.name;
/*    */   }
/*    */   
/*    */   private static Activity register(String debug0) {
/* 35 */     return (Activity)Registry.register(Registry.ACTIVITY, debug0, new Activity(debug0));
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object debug1) {
/* 40 */     if (this == debug1) {
/* 41 */       return true;
/*    */     }
/* 43 */     if (debug1 == null || getClass() != debug1.getClass()) {
/* 44 */       return false;
/*    */     }
/*    */     
/* 47 */     Activity debug2 = (Activity)debug1;
/*    */     
/* 49 */     return this.name.equals(debug2.name);
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 54 */     return this.hashCode;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 59 */     return getName();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\schedule\Activity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */