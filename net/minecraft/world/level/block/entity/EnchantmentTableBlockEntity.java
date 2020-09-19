/*     */ package net.minecraft.world.level.block.entity;
/*     */ 
/*     */ import java.util.Random;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.TranslatableComponent;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.Nameable;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ 
/*     */ public class EnchantmentTableBlockEntity
/*     */   extends BlockEntity
/*     */   implements Nameable, TickableBlockEntity {
/*     */   public int time;
/*     */   public float flip;
/*     */   public float oFlip;
/*     */   public float flipT;
/*     */   public float flipA;
/*     */   public float open;
/*     */   public float oOpen;
/*     */   public float rot;
/*     */   public float oRot;
/*     */   public float tRot;
/*  26 */   private static final Random RANDOM = new Random();
/*     */   private Component name;
/*     */   
/*     */   public EnchantmentTableBlockEntity() {
/*  30 */     super(BlockEntityType.ENCHANTING_TABLE);
/*     */   }
/*     */ 
/*     */   
/*     */   public CompoundTag save(CompoundTag debug1) {
/*  35 */     super.save(debug1);
/*  36 */     if (hasCustomName()) {
/*  37 */       debug1.putString("CustomName", Component.Serializer.toJson(this.name));
/*     */     }
/*     */     
/*  40 */     return debug1;
/*     */   }
/*     */ 
/*     */   
/*     */   public void load(BlockState debug1, CompoundTag debug2) {
/*  45 */     super.load(debug1, debug2);
/*  46 */     if (debug2.contains("CustomName", 8)) {
/*  47 */       this.name = (Component)Component.Serializer.fromJson(debug2.getString("CustomName"));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/*  53 */     this.oOpen = this.open;
/*  54 */     this.oRot = this.rot;
/*     */     
/*  56 */     Player debug1 = this.level.getNearestPlayer(this.worldPosition.getX() + 0.5D, this.worldPosition.getY() + 0.5D, this.worldPosition.getZ() + 0.5D, 3.0D, false);
/*  57 */     if (debug1 != null) {
/*  58 */       double d1 = debug1.getX() - this.worldPosition.getX() + 0.5D;
/*  59 */       double d2 = debug1.getZ() - this.worldPosition.getZ() + 0.5D;
/*     */       
/*  61 */       this.tRot = (float)Mth.atan2(d2, d1);
/*     */       
/*  63 */       this.open += 0.1F;
/*     */       
/*  65 */       if (this.open < 0.5F || RANDOM.nextInt(40) == 0) {
/*  66 */         float debug6 = this.flipT;
/*     */         do {
/*  68 */           this.flipT += (RANDOM.nextInt(4) - RANDOM.nextInt(4));
/*  69 */         } while (debug6 == this.flipT);
/*     */       } 
/*     */     } else {
/*  72 */       this.tRot += 0.02F;
/*  73 */       this.open -= 0.1F;
/*     */     } 
/*     */     
/*  76 */     while (this.rot >= 3.1415927F) {
/*  77 */       this.rot -= 6.2831855F;
/*     */     }
/*  79 */     while (this.rot < -3.1415927F) {
/*  80 */       this.rot += 6.2831855F;
/*     */     }
/*  82 */     while (this.tRot >= 3.1415927F) {
/*  83 */       this.tRot -= 6.2831855F;
/*     */     }
/*  85 */     while (this.tRot < -3.1415927F) {
/*  86 */       this.tRot += 6.2831855F;
/*     */     }
/*  88 */     float debug2 = this.tRot - this.rot;
/*  89 */     while (debug2 >= 3.1415927F) {
/*  90 */       debug2 -= 6.2831855F;
/*     */     }
/*  92 */     while (debug2 < -3.1415927F) {
/*  93 */       debug2 += 6.2831855F;
/*     */     }
/*     */     
/*  96 */     this.rot += debug2 * 0.4F;
/*     */     
/*  98 */     this.open = Mth.clamp(this.open, 0.0F, 1.0F);
/*     */     
/* 100 */     this.time++;
/* 101 */     this.oFlip = this.flip;
/*     */     
/* 103 */     float debug3 = (this.flipT - this.flip) * 0.4F;
/* 104 */     float debug4 = 0.2F;
/* 105 */     debug3 = Mth.clamp(debug3, -0.2F, 0.2F);
/* 106 */     this.flipA += (debug3 - this.flipA) * 0.9F;
/*     */     
/* 108 */     this.flip += this.flipA;
/*     */   }
/*     */ 
/*     */   
/*     */   public Component getName() {
/* 113 */     if (this.name != null) {
/* 114 */       return this.name;
/*     */     }
/* 116 */     return (Component)new TranslatableComponent("container.enchant");
/*     */   }
/*     */   
/*     */   public void setCustomName(@Nullable Component debug1) {
/* 120 */     this.name = debug1;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Component getCustomName() {
/* 126 */     return this.name;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\entity\EnchantmentTableBlockEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */