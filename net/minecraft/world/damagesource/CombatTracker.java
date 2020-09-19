/*     */ package net.minecraft.world.damagesource;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.TranslatableComponent;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.tags.Tag;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CombatTracker
/*     */ {
/*  24 */   private final List<CombatEntry> entries = Lists.newArrayList();
/*     */   private final LivingEntity mob;
/*     */   private int lastDamageTime;
/*     */   private int combatStartTime;
/*     */   private int combatEndTime;
/*     */   private boolean inCombat;
/*     */   private boolean takingDamage;
/*     */   private String nextLocation;
/*     */   
/*     */   public CombatTracker(LivingEntity debug1) {
/*  34 */     this.mob = debug1;
/*     */   }
/*     */   
/*     */   public void prepareForDamage() {
/*  38 */     resetPreparedStatus();
/*     */     
/*  40 */     Optional<BlockPos> debug1 = this.mob.getLastClimbablePos();
/*  41 */     if (debug1.isPresent()) {
/*  42 */       BlockState debug2 = this.mob.level.getBlockState(debug1.get());
/*     */       
/*  44 */       if (debug2.is(Blocks.LADDER) || debug2.is((Tag)BlockTags.TRAPDOORS)) {
/*  45 */         this.nextLocation = "ladder";
/*  46 */       } else if (debug2.is(Blocks.VINE)) {
/*  47 */         this.nextLocation = "vines";
/*  48 */       } else if (debug2.is(Blocks.WEEPING_VINES) || debug2.is(Blocks.WEEPING_VINES_PLANT)) {
/*  49 */         this.nextLocation = "weeping_vines";
/*  50 */       } else if (debug2.is(Blocks.TWISTING_VINES) || debug2.is(Blocks.TWISTING_VINES_PLANT)) {
/*  51 */         this.nextLocation = "twisting_vines";
/*  52 */       } else if (debug2.is(Blocks.SCAFFOLDING)) {
/*  53 */         this.nextLocation = "scaffolding";
/*     */       } else {
/*  55 */         this.nextLocation = "other_climbable";
/*     */       } 
/*  57 */     } else if (this.mob.isInWater()) {
/*  58 */       this.nextLocation = "water";
/*     */     } 
/*     */   }
/*     */   
/*     */   public void recordDamage(DamageSource debug1, float debug2, float debug3) {
/*  63 */     recheckStatus();
/*  64 */     prepareForDamage();
/*     */     
/*  66 */     CombatEntry debug4 = new CombatEntry(debug1, this.mob.tickCount, debug2, debug3, this.nextLocation, this.mob.fallDistance);
/*     */     
/*  68 */     this.entries.add(debug4);
/*  69 */     this.lastDamageTime = this.mob.tickCount;
/*  70 */     this.takingDamage = true;
/*     */     
/*  72 */     if (debug4.isCombatRelated() && !this.inCombat && this.mob.isAlive()) {
/*  73 */       this.inCombat = true;
/*  74 */       this.combatStartTime = this.mob.tickCount;
/*  75 */       this.combatEndTime = this.combatStartTime;
/*  76 */       this.mob.onEnterCombat();
/*     */     } 
/*     */   }
/*     */   public Component getDeathMessage() {
/*     */     Component debug3;
/*  81 */     if (this.entries.isEmpty()) {
/*  82 */       return (Component)new TranslatableComponent("death.attack.generic", new Object[] { this.mob.getDisplayName() });
/*     */     }
/*     */     
/*  85 */     CombatEntry debug1 = getMostSignificantFall();
/*  86 */     CombatEntry debug2 = this.entries.get(this.entries.size() - 1);
/*     */     
/*  88 */     Component debug4 = debug2.getAttackerName();
/*  89 */     Entity debug5 = debug2.getSource().getEntity();
/*     */     
/*  91 */     if (debug1 != null && debug2.getSource() == DamageSource.FALL) {
/*  92 */       Component debug6 = debug1.getAttackerName();
/*     */       
/*  94 */       if (debug1.getSource() == DamageSource.FALL || debug1.getSource() == DamageSource.OUT_OF_WORLD) {
/*  95 */         TranslatableComponent translatableComponent = new TranslatableComponent("death.fell.accident." + getFallLocation(debug1), new Object[] { this.mob.getDisplayName() });
/*  96 */       } else if (debug6 != null && (debug4 == null || !debug6.equals(debug4))) {
/*  97 */         Entity debug7 = debug1.getSource().getEntity();
/*  98 */         ItemStack debug8 = (debug7 instanceof LivingEntity) ? ((LivingEntity)debug7).getMainHandItem() : ItemStack.EMPTY;
/*     */         
/* 100 */         if (!debug8.isEmpty() && debug8.hasCustomHoverName()) {
/* 101 */           TranslatableComponent translatableComponent = new TranslatableComponent("death.fell.assist.item", new Object[] { this.mob.getDisplayName(), debug6, debug8.getDisplayName() });
/*     */         } else {
/* 103 */           TranslatableComponent translatableComponent = new TranslatableComponent("death.fell.assist", new Object[] { this.mob.getDisplayName(), debug6 });
/*     */         } 
/* 105 */       } else if (debug4 != null) {
/* 106 */         ItemStack debug7 = (debug5 instanceof LivingEntity) ? ((LivingEntity)debug5).getMainHandItem() : ItemStack.EMPTY;
/* 107 */         if (!debug7.isEmpty() && debug7.hasCustomHoverName()) {
/* 108 */           TranslatableComponent translatableComponent = new TranslatableComponent("death.fell.finish.item", new Object[] { this.mob.getDisplayName(), debug4, debug7.getDisplayName() });
/*     */         } else {
/* 110 */           TranslatableComponent translatableComponent = new TranslatableComponent("death.fell.finish", new Object[] { this.mob.getDisplayName(), debug4 });
/*     */         } 
/*     */       } else {
/* 113 */         TranslatableComponent translatableComponent = new TranslatableComponent("death.fell.killer", new Object[] { this.mob.getDisplayName() });
/*     */       } 
/*     */     } else {
/* 116 */       debug3 = debug2.getSource().getLocalizedDeathMessage(this.mob);
/*     */     } 
/*     */     
/* 119 */     return debug3;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public LivingEntity getKiller() {
/* 124 */     LivingEntity debug1 = null;
/* 125 */     Player debug2 = null;
/* 126 */     float debug3 = 0.0F;
/* 127 */     float debug4 = 0.0F;
/*     */     
/* 129 */     for (CombatEntry debug6 : this.entries) {
/* 130 */       if (debug6.getSource().getEntity() instanceof Player && (debug2 == null || debug6.getDamage() > debug4)) {
/* 131 */         debug4 = debug6.getDamage();
/* 132 */         debug2 = (Player)debug6.getSource().getEntity();
/*     */       } 
/*     */       
/* 135 */       if (debug6.getSource().getEntity() instanceof LivingEntity && (debug1 == null || debug6.getDamage() > debug3)) {
/* 136 */         debug3 = debug6.getDamage();
/* 137 */         debug1 = (LivingEntity)debug6.getSource().getEntity();
/*     */       } 
/*     */     } 
/*     */     
/* 141 */     if (debug2 != null && debug4 >= debug3 / 3.0F) {
/* 142 */       return (LivingEntity)debug2;
/*     */     }
/* 144 */     return debug1;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private CombatEntry getMostSignificantFall() {
/* 150 */     CombatEntry debug1 = null;
/* 151 */     CombatEntry debug2 = null;
/* 152 */     float debug3 = 0.0F;
/* 153 */     float debug4 = 0.0F;
/*     */     
/* 155 */     for (int debug5 = 0; debug5 < this.entries.size(); debug5++) {
/* 156 */       CombatEntry debug6 = this.entries.get(debug5);
/* 157 */       CombatEntry debug7 = (debug5 > 0) ? this.entries.get(debug5 - 1) : null;
/*     */       
/* 159 */       if ((debug6.getSource() == DamageSource.FALL || debug6.getSource() == DamageSource.OUT_OF_WORLD) && debug6.getFallDistance() > 0.0F && (debug1 == null || debug6.getFallDistance() > debug4)) {
/* 160 */         if (debug5 > 0) {
/* 161 */           debug1 = debug7;
/*     */         } else {
/* 163 */           debug1 = debug6;
/*     */         } 
/* 165 */         debug4 = debug6.getFallDistance();
/*     */       } 
/*     */       
/* 168 */       if (debug6.getLocation() != null && (debug2 == null || debug6.getDamage() > debug3)) {
/* 169 */         debug2 = debug6;
/* 170 */         debug3 = debug6.getDamage();
/*     */       } 
/*     */     } 
/*     */     
/* 174 */     if (debug4 > 5.0F && debug1 != null)
/* 175 */       return debug1; 
/* 176 */     if (debug3 > 5.0F && debug2 != null) {
/* 177 */       return debug2;
/*     */     }
/* 179 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   private String getFallLocation(CombatEntry debug1) {
/* 184 */     return (debug1.getLocation() == null) ? "generic" : debug1.getLocation();
/*     */   }
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
/*     */   public int getCombatDuration() {
/* 198 */     if (this.inCombat) {
/* 199 */       return this.mob.tickCount - this.combatStartTime;
/*     */     }
/* 201 */     return this.combatEndTime - this.combatStartTime;
/*     */   }
/*     */ 
/*     */   
/*     */   private void resetPreparedStatus() {
/* 206 */     this.nextLocation = null;
/*     */   }
/*     */   
/*     */   public void recheckStatus() {
/* 210 */     int debug1 = this.inCombat ? 300 : 100;
/*     */     
/* 212 */     if (this.takingDamage && (!this.mob.isAlive() || this.mob.tickCount - this.lastDamageTime > debug1)) {
/* 213 */       boolean debug2 = this.inCombat;
/* 214 */       this.takingDamage = false;
/* 215 */       this.inCombat = false;
/* 216 */       this.combatEndTime = this.mob.tickCount;
/*     */       
/* 218 */       if (debug2) {
/* 219 */         this.mob.onLeaveCombat();
/*     */       }
/* 221 */       this.entries.clear();
/*     */     } 
/*     */   }
/*     */   
/*     */   public LivingEntity getMob() {
/* 226 */     return this.mob;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\damagesource\CombatTracker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */