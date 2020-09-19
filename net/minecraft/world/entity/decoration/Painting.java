/*     */ package net.minecraft.world.entity.decoration;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ClientboundAddPaintingPacket;
/*     */ import net.minecraft.resources.ResourceLocation;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.GameRules;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ 
/*     */ public class Painting extends HangingEntity {
/*     */   public Motive motive;
/*     */   
/*     */   public Painting(EntityType<? extends Painting> debug1, Level debug2) {
/*  27 */     super((EntityType)debug1, debug2);
/*     */   }
/*     */   
/*     */   public Painting(Level debug1, BlockPos debug2, Direction debug3) {
/*  31 */     super(EntityType.PAINTING, debug1, debug2);
/*     */     
/*  33 */     List<Motive> debug4 = Lists.newArrayList();
/*  34 */     int debug5 = 0;
/*  35 */     for (Motive debug7 : Registry.MOTIVE) {
/*  36 */       this.motive = debug7;
/*  37 */       setDirection(debug3);
/*     */       
/*  39 */       if (survives()) {
/*  40 */         debug4.add(debug7);
/*  41 */         int debug8 = debug7.getWidth() * debug7.getHeight();
/*  42 */         if (debug8 > debug5) {
/*  43 */           debug5 = debug8;
/*     */         }
/*     */       } 
/*     */     } 
/*  47 */     if (!debug4.isEmpty()) {
/*     */       
/*  49 */       Iterator<Motive> debug6 = debug4.iterator();
/*  50 */       while (debug6.hasNext()) {
/*  51 */         Motive debug7 = debug6.next();
/*  52 */         if (debug7.getWidth() * debug7.getHeight() < debug5) {
/*  53 */           debug6.remove();
/*     */         }
/*     */       } 
/*  56 */       this.motive = debug4.get(this.random.nextInt(debug4.size()));
/*     */     } 
/*  58 */     setDirection(debug3);
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
/*     */   public void addAdditionalSaveData(CompoundTag debug1) {
/*  70 */     debug1.putString("Motive", Registry.MOTIVE.getKey(this.motive).toString());
/*  71 */     debug1.putByte("Facing", (byte)this.direction.get2DDataValue());
/*  72 */     super.addAdditionalSaveData(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public void readAdditionalSaveData(CompoundTag debug1) {
/*  77 */     this.motive = (Motive)Registry.MOTIVE.get(ResourceLocation.tryParse(debug1.getString("Motive")));
/*  78 */     this.direction = Direction.from2DDataValue(debug1.getByte("Facing"));
/*  79 */     super.readAdditionalSaveData(debug1);
/*  80 */     setDirection(this.direction);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getWidth() {
/*  85 */     if (this.motive == null) {
/*  86 */       return 1;
/*     */     }
/*  88 */     return this.motive.getWidth();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getHeight() {
/*  93 */     if (this.motive == null) {
/*  94 */       return 1;
/*     */     }
/*  96 */     return this.motive.getHeight();
/*     */   }
/*     */ 
/*     */   
/*     */   public void dropItem(@Nullable Entity debug1) {
/* 101 */     if (!this.level.getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
/*     */       return;
/*     */     }
/*     */     
/* 105 */     playSound(SoundEvents.PAINTING_BREAK, 1.0F, 1.0F);
/*     */     
/* 107 */     if (debug1 instanceof Player) {
/* 108 */       Player debug2 = (Player)debug1;
/*     */       
/* 110 */       if (debug2.abilities.instabuild) {
/*     */         return;
/*     */       }
/*     */     } 
/*     */     
/* 115 */     spawnAtLocation((ItemLike)Items.PAINTING);
/*     */   }
/*     */ 
/*     */   
/*     */   public void playPlacementSound() {
/* 120 */     playSound(SoundEvents.PAINTING_PLACE, 1.0F, 1.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   public void moveTo(double debug1, double debug3, double debug5, float debug7, float debug8) {
/* 125 */     setPos(debug1, debug3, debug5);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Packet<?> getAddEntityPacket() {
/* 136 */     return (Packet<?>)new ClientboundAddPaintingPacket(this);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\decoration\Painting.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */