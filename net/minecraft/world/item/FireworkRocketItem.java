/*     */ package net.minecraft.world.item;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.Comparator;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.InteractionResultHolder;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.entity.projectile.FireworkRocketEntity;
/*     */ import net.minecraft.world.item.context.UseOnContext;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FireworkRocketItem
/*     */   extends Item
/*     */ {
/*     */   public FireworkRocketItem(Item.Properties debug1) {
/*  40 */     super(debug1);
/*     */   }
/*     */   
/*     */   public enum Shape { private static final Shape[] BY_ID;
/*  44 */     SMALL_BALL(0, "small_ball"),
/*  45 */     LARGE_BALL(1, "large_ball"),
/*  46 */     STAR(2, "star"),
/*  47 */     CREEPER(3, "creeper"),
/*  48 */     BURST(4, "burst");
/*     */     
/*     */     static {
/*  51 */       BY_ID = (Shape[])Arrays.<Shape>stream(values()).sorted(Comparator.comparingInt(debug0 -> debug0.id)).toArray(debug0 -> new Shape[debug0]);
/*     */     }
/*     */     private final int id;
/*     */     private final String name;
/*     */     
/*     */     Shape(int debug3, String debug4) {
/*  57 */       this.id = debug3;
/*  58 */       this.name = debug4;
/*     */     }
/*     */     
/*     */     public int getId() {
/*  62 */       return this.id;
/*     */     } }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public InteractionResult useOn(UseOnContext debug1) {
/*  79 */     Level debug2 = debug1.getLevel();
/*  80 */     if (!debug2.isClientSide) {
/*  81 */       ItemStack debug3 = debug1.getItemInHand();
/*     */       
/*  83 */       Vec3 debug4 = debug1.getClickLocation();
/*  84 */       Direction debug5 = debug1.getClickedFace();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  90 */       FireworkRocketEntity debug6 = new FireworkRocketEntity(debug2, (Entity)debug1.getPlayer(), debug4.x + debug5.getStepX() * 0.15D, debug4.y + debug5.getStepY() * 0.15D, debug4.z + debug5.getStepZ() * 0.15D, debug3);
/*     */ 
/*     */       
/*  93 */       debug2.addFreshEntity((Entity)debug6);
/*     */       
/*  95 */       debug3.shrink(1);
/*     */     } 
/*  97 */     return InteractionResult.sidedSuccess(debug2.isClientSide);
/*     */   }
/*     */ 
/*     */   
/*     */   public InteractionResultHolder<ItemStack> use(Level debug1, Player debug2, InteractionHand debug3) {
/* 102 */     if (debug2.isFallFlying()) {
/* 103 */       ItemStack debug4 = debug2.getItemInHand(debug3);
/* 104 */       if (!debug1.isClientSide) {
/* 105 */         debug1.addFreshEntity((Entity)new FireworkRocketEntity(debug1, debug4, (LivingEntity)debug2));
/* 106 */         if (!debug2.abilities.instabuild) {
/* 107 */           debug4.shrink(1);
/*     */         }
/*     */       } 
/*     */       
/* 111 */       return InteractionResultHolder.sidedSuccess(debug2.getItemInHand(debug3), debug1.isClientSide());
/*     */     } 
/* 113 */     return InteractionResultHolder.pass(debug2.getItemInHand(debug3));
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\item\FireworkRocketItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */