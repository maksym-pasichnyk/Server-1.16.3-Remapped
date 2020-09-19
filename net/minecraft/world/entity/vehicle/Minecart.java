/*    */ package net.minecraft.world.entity.vehicle;
/*    */ 
/*    */ import net.minecraft.world.InteractionHand;
/*    */ import net.minecraft.world.InteractionResult;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public class Minecart extends AbstractMinecart {
/*    */   public Minecart(EntityType<?> debug1, Level debug2) {
/* 11 */     super(debug1, debug2);
/*    */   }
/*    */   
/*    */   public Minecart(Level debug1, double debug2, double debug4, double debug6) {
/* 15 */     super(EntityType.MINECART, debug1, debug2, debug4, debug6);
/*    */   }
/*    */ 
/*    */   
/*    */   public InteractionResult interact(Player debug1, InteractionHand debug2) {
/* 20 */     if (debug1.isSecondaryUseActive()) {
/* 21 */       return InteractionResult.PASS;
/*    */     }
/*    */     
/* 24 */     if (isVehicle()) {
/* 25 */       return InteractionResult.PASS;
/*    */     }
/* 27 */     if (!this.level.isClientSide) {
/* 28 */       return debug1.startRiding(this) ? InteractionResult.CONSUME : InteractionResult.PASS;
/*    */     }
/*    */     
/* 31 */     return InteractionResult.SUCCESS;
/*    */   }
/*    */ 
/*    */   
/*    */   public void activateMinecart(int debug1, int debug2, int debug3, boolean debug4) {
/* 36 */     if (debug4) {
/* 37 */       if (isVehicle()) {
/* 38 */         ejectPassengers();
/*    */       }
/* 40 */       if (getHurtTime() == 0) {
/* 41 */         setHurtDir(-getHurtDir());
/* 42 */         setHurtTime(10);
/* 43 */         setDamage(50.0F);
/* 44 */         markHurt();
/*    */       } 
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public AbstractMinecart.Type getMinecartType() {
/* 51 */     return AbstractMinecart.Type.RIDEABLE;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\vehicle\Minecart.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */