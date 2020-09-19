/*     */ package net.minecraft.world.entity.ai.behavior;
/*     */ 
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.Sets;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.GlobalPos;
/*     */ import net.minecraft.core.Position;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.tags.Tag;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.ai.Brain;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.DoorBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.pathfinder.Node;
/*     */ import net.minecraft.world.level.pathfinder.Path;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class InteractWithDoor
/*     */   extends Behavior<LivingEntity>
/*     */ {
/*     */   @Nullable
/*     */   private Node lastCheckedNode;
/*     */   private int remainingCooldown;
/*     */   
/*     */   public InteractWithDoor() {
/*  42 */     super((Map<MemoryModuleType<?>, MemoryStatus>)ImmutableMap.of(MemoryModuleType.PATH, MemoryStatus.VALUE_PRESENT, MemoryModuleType.DOORS_TO_CLOSE, MemoryStatus.REGISTERED));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean checkExtraStartConditions(ServerLevel debug1, LivingEntity debug2) {
/*  50 */     Path debug3 = debug2.getBrain().getMemory(MemoryModuleType.PATH).get();
/*  51 */     if (debug3.notStarted() || debug3.isDone()) {
/*  52 */       return false;
/*     */     }
/*  54 */     if (!Objects.equals(this.lastCheckedNode, debug3.getNextNode())) {
/*     */       
/*  56 */       this.remainingCooldown = 20;
/*  57 */       return true;
/*     */     } 
/*  59 */     if (this.remainingCooldown > 0) {
/*  60 */       this.remainingCooldown--;
/*     */     }
/*  62 */     return (this.remainingCooldown == 0);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void start(ServerLevel debug1, LivingEntity debug2, long debug3) {
/*  67 */     Path debug5 = debug2.getBrain().getMemory(MemoryModuleType.PATH).get();
/*  68 */     this.lastCheckedNode = debug5.getNextNode();
/*     */     
/*  70 */     Node debug6 = debug5.getPreviousNode();
/*  71 */     Node debug7 = debug5.getNextNode();
/*     */     
/*  73 */     BlockPos debug8 = debug6.asBlockPos();
/*  74 */     BlockState debug9 = debug1.getBlockState(debug8);
/*  75 */     if (debug9.is((Tag)BlockTags.WOODEN_DOORS)) {
/*  76 */       DoorBlock doorBlock = (DoorBlock)debug9.getBlock();
/*  77 */       if (!doorBlock.isOpen(debug9)) {
/*  78 */         doorBlock.setOpen((Level)debug1, debug9, debug8, true);
/*     */       }
/*  80 */       rememberDoorToClose(debug1, debug2, debug8);
/*     */     } 
/*     */     
/*  83 */     BlockPos debug10 = debug7.asBlockPos();
/*  84 */     BlockState debug11 = debug1.getBlockState(debug10);
/*  85 */     if (debug11.is((Tag)BlockTags.WOODEN_DOORS)) {
/*  86 */       DoorBlock debug12 = (DoorBlock)debug11.getBlock();
/*  87 */       if (!debug12.isOpen(debug11)) {
/*     */         
/*  89 */         debug12.setOpen((Level)debug1, debug11, debug10, true);
/*  90 */         rememberDoorToClose(debug1, debug2, debug10);
/*     */       } 
/*     */     } 
/*     */     
/*  94 */     closeDoorsThatIHaveOpenedOrPassedThrough(debug1, debug2, debug6, debug7);
/*     */   }
/*     */   
/*     */   public static void closeDoorsThatIHaveOpenedOrPassedThrough(ServerLevel debug0, LivingEntity debug1, @Nullable Node debug2, @Nullable Node debug3) {
/*  98 */     Brain<?> debug4 = debug1.getBrain();
/*     */     
/* 100 */     if (debug4.hasMemoryValue(MemoryModuleType.DOORS_TO_CLOSE)) {
/* 101 */       Iterator<GlobalPos> debug5 = ((Set<GlobalPos>)debug4.getMemory(MemoryModuleType.DOORS_TO_CLOSE).get()).iterator();
/* 102 */       while (debug5.hasNext()) {
/* 103 */         GlobalPos debug6 = debug5.next();
/* 104 */         BlockPos debug7 = debug6.pos();
/*     */ 
/*     */         
/* 107 */         if (debug2 != null && debug2.asBlockPos().equals(debug7)) {
/*     */           continue;
/*     */         }
/* 110 */         if (debug3 != null && debug3.asBlockPos().equals(debug7)) {
/*     */           continue;
/*     */         }
/*     */         
/* 114 */         if (isDoorTooFarAway(debug0, debug1, debug6)) {
/* 115 */           debug5.remove();
/*     */           continue;
/*     */         } 
/* 118 */         BlockState debug8 = debug0.getBlockState(debug7);
/* 119 */         if (!debug8.is((Tag)BlockTags.WOODEN_DOORS)) {
/* 120 */           debug5.remove();
/*     */           continue;
/*     */         } 
/* 123 */         DoorBlock debug9 = (DoorBlock)debug8.getBlock();
/* 124 */         if (!debug9.isOpen(debug8)) {
/* 125 */           debug5.remove();
/*     */           continue;
/*     */         } 
/* 128 */         if (areOtherMobsComingThroughDoor(debug0, debug1, debug7)) {
/* 129 */           debug5.remove();
/*     */           continue;
/*     */         } 
/* 132 */         debug9.setOpen((Level)debug0, debug8, debug7, false);
/* 133 */         debug5.remove();
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private static boolean areOtherMobsComingThroughDoor(ServerLevel debug0, LivingEntity debug1, BlockPos debug2) {
/* 139 */     Brain<?> debug3 = debug1.getBrain();
/* 140 */     if (!debug3.hasMemoryValue(MemoryModuleType.LIVING_ENTITIES)) {
/* 141 */       return false;
/*     */     }
/*     */     
/* 144 */     return ((List)debug3.getMemory(MemoryModuleType.LIVING_ENTITIES).get()).stream()
/* 145 */       .filter(debug1 -> (debug1.getType() == debug0.getType()))
/* 146 */       .filter(debug1 -> debug0.closerThan((Position)debug1.position(), 2.0D))
/* 147 */       .anyMatch(debug2 -> isMobComingThroughDoor(debug0, debug2, debug1));
/*     */   }
/*     */   
/*     */   private static boolean isMobComingThroughDoor(ServerLevel debug0, LivingEntity debug1, BlockPos debug2) {
/* 151 */     if (!debug1.getBrain().hasMemoryValue(MemoryModuleType.PATH)) {
/* 152 */       return false;
/*     */     }
/* 154 */     Path debug3 = debug1.getBrain().getMemory(MemoryModuleType.PATH).get();
/* 155 */     if (debug3.isDone())
/*     */     {
/* 157 */       return false;
/*     */     }
/*     */     
/* 160 */     Node debug4 = debug3.getPreviousNode();
/* 161 */     if (debug4 == null) {
/* 162 */       return false;
/*     */     }
/*     */     
/* 165 */     Node debug5 = debug3.getNextNode();
/* 166 */     return (debug2.equals(debug4.asBlockPos()) || debug2.equals(debug5.asBlockPos()));
/*     */   }
/*     */   
/*     */   private static boolean isDoorTooFarAway(ServerLevel debug0, LivingEntity debug1, GlobalPos debug2) {
/* 170 */     return (debug2.dimension() != debug0.dimension() || 
/* 171 */       !debug2.pos().closerThan((Position)debug1.position(), 2.0D));
/*     */   }
/*     */   
/*     */   private void rememberDoorToClose(ServerLevel debug1, LivingEntity debug2, BlockPos debug3) {
/* 175 */     Brain<?> debug4 = debug2.getBrain();
/* 176 */     GlobalPos debug5 = GlobalPos.of(debug1.dimension(), debug3);
/* 177 */     if (debug4.getMemory(MemoryModuleType.DOORS_TO_CLOSE).isPresent()) {
/* 178 */       ((Set<GlobalPos>)debug4.getMemory(MemoryModuleType.DOORS_TO_CLOSE).get()).add(debug5);
/*     */     } else {
/* 180 */       debug4.setMemory(MemoryModuleType.DOORS_TO_CLOSE, Sets.newHashSet((Object[])new GlobalPos[] { debug5 }));
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\behavior\InteractWithDoor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */