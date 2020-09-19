/*    */ package net.minecraft.world.damagesource;
/*    */ 
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.network.chat.Component;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CombatEntry
/*    */ {
/*    */   private final DamageSource source;
/*    */   private final int time;
/*    */   private final float damage;
/*    */   private final float health;
/*    */   private final String location;
/*    */   private final float fallDistance;
/*    */   
/*    */   public CombatEntry(DamageSource debug1, int debug2, float debug3, float debug4, String debug5, float debug6) {
/* 18 */     this.source = debug1;
/* 19 */     this.time = debug2;
/* 20 */     this.damage = debug4;
/* 21 */     this.health = debug3;
/* 22 */     this.location = debug5;
/* 23 */     this.fallDistance = debug6;
/*    */   }
/*    */   
/*    */   public DamageSource getSource() {
/* 27 */     return this.source;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public float getDamage() {
/* 35 */     return this.damage;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isCombatRelated() {
/* 47 */     return this.source.getEntity() instanceof net.minecraft.world.entity.LivingEntity;
/*    */   }
/*    */   
/*    */   @Nullable
/*    */   public String getLocation() {
/* 52 */     return this.location;
/*    */   }
/*    */   
/*    */   @Nullable
/*    */   public Component getAttackerName() {
/* 57 */     return (getSource().getEntity() == null) ? null : getSource().getEntity().getDisplayName();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public float getFallDistance() {
/* 66 */     if (this.source == DamageSource.OUT_OF_WORLD) {
/* 67 */       return Float.MAX_VALUE;
/*    */     }
/* 69 */     return this.fallDistance;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\damagesource\CombatEntry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */