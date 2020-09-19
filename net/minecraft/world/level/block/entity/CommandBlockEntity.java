/*     */ package net.minecraft.world.level.block.entity;
/*     */ 
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.commands.CommandSource;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.level.BaseCommandBlock;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.CommandBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.phys.Vec2;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class CommandBlockEntity extends BlockEntity {
/*     */   private boolean powered;
/*     */   private boolean auto;
/*     */   
/*     */   public CommandBlockEntity() {
/*  26 */     super(BlockEntityType.COMMAND_BLOCK);
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
/* 163 */     this.commandBlock = new BaseCommandBlock()
/*     */       {
/*     */         public void setCommand(String debug1) {
/* 166 */           super.setCommand(debug1);
/* 167 */           CommandBlockEntity.this.setChanged();
/*     */         }
/*     */ 
/*     */         
/*     */         public ServerLevel getLevel() {
/* 172 */           return (ServerLevel)CommandBlockEntity.this.level;
/*     */         }
/*     */ 
/*     */         
/*     */         public void onUpdated() {
/* 177 */           BlockState debug1 = CommandBlockEntity.this.level.getBlockState(CommandBlockEntity.this.worldPosition);
/* 178 */           getLevel().sendBlockUpdated(CommandBlockEntity.this.worldPosition, debug1, debug1, 3);
/*     */         }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         public CommandSourceStack createCommandSourceStack() {
/* 188 */           return new CommandSourceStack((CommandSource)this, Vec3.atCenterOf((Vec3i)CommandBlockEntity.this.worldPosition), Vec2.ZERO, getLevel(), 2, getName().getString(), getName(), getLevel().getServer(), null);
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   private boolean conditionMet;
/*     */   private boolean sendToClient;
/*     */   private final BaseCommandBlock commandBlock;
/*     */   
/*     */   public CompoundTag save(CompoundTag debug1) {
/*     */     super.save(debug1);
/*     */     this.commandBlock.save(debug1);
/*     */     debug1.putBoolean("powered", isPowered());
/*     */     debug1.putBoolean("conditionMet", wasConditionMet());
/*     */     debug1.putBoolean("auto", isAutomatic());
/*     */     return debug1;
/*     */   }
/*     */   
/*     */   public void load(BlockState debug1, CompoundTag debug2) {
/*     */     super.load(debug1, debug2);
/*     */     this.commandBlock.load(debug2);
/*     */     this.powered = debug2.getBoolean("powered");
/*     */     this.conditionMet = debug2.getBoolean("conditionMet");
/*     */     setAutomatic(debug2.getBoolean("auto"));
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public ClientboundBlockEntityDataPacket getUpdatePacket() {
/*     */     if (isSendToClient()) {
/*     */       setSendToClient(false);
/*     */       CompoundTag debug1 = save(new CompoundTag());
/*     */       return new ClientboundBlockEntityDataPacket(this.worldPosition, 2, debug1);
/*     */     } 
/*     */     return null;
/*     */   }
/*     */   
/*     */   public boolean onlyOpCanSetNbt() {
/*     */     return true;
/*     */   }
/*     */   
/*     */   public BaseCommandBlock getCommandBlock() {
/*     */     return this.commandBlock;
/*     */   }
/*     */   
/*     */   public void setPowered(boolean debug1) {
/*     */     this.powered = debug1;
/*     */   }
/*     */   
/*     */   public boolean isPowered() {
/*     */     return this.powered;
/*     */   }
/*     */   
/*     */   public boolean isAutomatic() {
/*     */     return this.auto;
/*     */   }
/*     */   
/*     */   public void setAutomatic(boolean debug1) {
/*     */     boolean debug2 = this.auto;
/*     */     this.auto = debug1;
/*     */     if (!debug2 && debug1 && !this.powered && this.level != null && getMode() != Mode.SEQUENCE)
/*     */       scheduleTick(); 
/*     */   }
/*     */   
/*     */   public void onModeSwitch() {
/*     */     Mode debug1 = getMode();
/*     */     if (debug1 == Mode.AUTO && (this.powered || this.auto) && this.level != null)
/*     */       scheduleTick(); 
/*     */   }
/*     */   
/*     */   private void scheduleTick() {
/*     */     Block debug1 = getBlockState().getBlock();
/*     */     if (debug1 instanceof CommandBlock) {
/*     */       markConditionMet();
/*     */       this.level.getBlockTicks().scheduleTick(this.worldPosition, debug1, 1);
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean wasConditionMet() {
/*     */     return this.conditionMet;
/*     */   }
/*     */   
/*     */   public boolean markConditionMet() {
/*     */     this.conditionMet = true;
/*     */     if (isConditional()) {
/*     */       BlockPos debug1 = this.worldPosition.relative(((Direction)this.level.getBlockState(this.worldPosition).getValue((Property)CommandBlock.FACING)).getOpposite());
/*     */       if (this.level.getBlockState(debug1).getBlock() instanceof CommandBlock) {
/*     */         BlockEntity debug2 = this.level.getBlockEntity(debug1);
/*     */         this.conditionMet = (debug2 instanceof CommandBlockEntity && ((CommandBlockEntity)debug2).getCommandBlock().getSuccessCount() > 0);
/*     */       } else {
/*     */         this.conditionMet = false;
/*     */       } 
/*     */     } 
/*     */     return this.conditionMet;
/*     */   }
/*     */   
/*     */   public boolean isSendToClient() {
/*     */     return this.sendToClient;
/*     */   }
/*     */   
/*     */   public void setSendToClient(boolean debug1) {
/*     */     this.sendToClient = debug1;
/*     */   }
/*     */   
/*     */   public Mode getMode() {
/*     */     BlockState debug1 = getBlockState();
/*     */     if (debug1.is(Blocks.COMMAND_BLOCK))
/*     */       return Mode.REDSTONE; 
/*     */     if (debug1.is(Blocks.REPEATING_COMMAND_BLOCK))
/*     */       return Mode.AUTO; 
/*     */     if (debug1.is(Blocks.CHAIN_COMMAND_BLOCK))
/*     */       return Mode.SEQUENCE; 
/*     */     return Mode.REDSTONE;
/*     */   }
/*     */   
/*     */   public boolean isConditional() {
/*     */     BlockState debug1 = this.level.getBlockState(getBlockPos());
/*     */     if (debug1.getBlock() instanceof CommandBlock)
/*     */       return ((Boolean)debug1.getValue((Property)CommandBlock.CONDITIONAL)).booleanValue(); 
/*     */     return false;
/*     */   }
/*     */   
/*     */   public void clearRemoved() {
/*     */     clearCache();
/*     */     super.clearRemoved();
/*     */   }
/*     */   
/*     */   public enum Mode {
/*     */     SEQUENCE, AUTO, REDSTONE;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\entity\CommandBlockEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */