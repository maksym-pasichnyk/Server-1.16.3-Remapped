/*    */ package net.minecraft.world.entity.player;
/*    */ 
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.nbt.Tag;
/*    */ 
/*    */ public class Abilities {
/*    */   public boolean invulnerable;
/*    */   public boolean flying;
/*    */   public boolean mayfly;
/*    */   public boolean instabuild;
/*    */   public boolean mayBuild = true;
/* 12 */   private float flyingSpeed = 0.05F;
/* 13 */   private float walkingSpeed = 0.1F;
/*    */   
/*    */   public void addSaveData(CompoundTag debug1) {
/* 16 */     CompoundTag debug2 = new CompoundTag();
/*    */     
/* 18 */     debug2.putBoolean("invulnerable", this.invulnerable);
/* 19 */     debug2.putBoolean("flying", this.flying);
/* 20 */     debug2.putBoolean("mayfly", this.mayfly);
/* 21 */     debug2.putBoolean("instabuild", this.instabuild);
/* 22 */     debug2.putBoolean("mayBuild", this.mayBuild);
/* 23 */     debug2.putFloat("flySpeed", this.flyingSpeed);
/* 24 */     debug2.putFloat("walkSpeed", this.walkingSpeed);
/* 25 */     debug1.put("abilities", (Tag)debug2);
/*    */   }
/*    */   
/*    */   public void loadSaveData(CompoundTag debug1) {
/* 29 */     if (debug1.contains("abilities", 10)) {
/* 30 */       CompoundTag debug2 = debug1.getCompound("abilities");
/*    */       
/* 32 */       this.invulnerable = debug2.getBoolean("invulnerable");
/* 33 */       this.flying = debug2.getBoolean("flying");
/* 34 */       this.mayfly = debug2.getBoolean("mayfly");
/* 35 */       this.instabuild = debug2.getBoolean("instabuild");
/*    */       
/* 37 */       if (debug2.contains("flySpeed", 99)) {
/* 38 */         this.flyingSpeed = debug2.getFloat("flySpeed");
/* 39 */         this.walkingSpeed = debug2.getFloat("walkSpeed");
/*    */       } 
/* 41 */       if (debug2.contains("mayBuild", 1)) {
/* 42 */         this.mayBuild = debug2.getBoolean("mayBuild");
/*    */       }
/*    */     } 
/*    */   }
/*    */   
/*    */   public float getFlyingSpeed() {
/* 48 */     return this.flyingSpeed;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public float getWalkingSpeed() {
/* 56 */     return this.walkingSpeed;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\player\Abilities.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */