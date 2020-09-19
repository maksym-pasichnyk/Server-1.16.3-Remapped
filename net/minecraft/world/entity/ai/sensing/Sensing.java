/*    */ package net.minecraft.world.entity.ai.sensing;
/*    */ 
/*    */ import com.google.common.collect.Lists;
/*    */ import java.util.List;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ 
/*    */ public class Sensing
/*    */ {
/*    */   private final Mob mob;
/* 11 */   private final List<Entity> seen = Lists.newArrayList();
/* 12 */   private final List<Entity> unseen = Lists.newArrayList();
/*    */   
/*    */   public Sensing(Mob debug1) {
/* 15 */     this.mob = debug1;
/*    */   }
/*    */   
/*    */   public void tick() {
/* 19 */     this.seen.clear();
/* 20 */     this.unseen.clear();
/*    */   }
/*    */   
/*    */   public boolean canSee(Entity debug1) {
/* 24 */     if (this.seen.contains(debug1)) {
/* 25 */       return true;
/*    */     }
/* 27 */     if (this.unseen.contains(debug1)) {
/* 28 */       return false;
/*    */     }
/*    */     
/* 31 */     this.mob.level.getProfiler().push("canSee");
/* 32 */     boolean debug2 = this.mob.canSee(debug1);
/* 33 */     this.mob.level.getProfiler().pop();
/* 34 */     if (debug2) {
/* 35 */       this.seen.add(debug1);
/*    */     } else {
/* 37 */       this.unseen.add(debug1);
/*    */     } 
/* 39 */     return debug2;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\sensing\Sensing.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */