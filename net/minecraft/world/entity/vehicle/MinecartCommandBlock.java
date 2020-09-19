/*     */ package net.minecraft.world.entity.vehicle;
/*     */ 
/*     */ import net.minecraft.commands.CommandSource;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.TextComponent;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.level.BaseCommandBlock;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ 
/*     */ public class MinecartCommandBlock
/*     */   extends AbstractMinecart
/*     */ {
/*  24 */   private static final EntityDataAccessor<String> DATA_ID_COMMAND_NAME = SynchedEntityData.defineId(MinecartCommandBlock.class, EntityDataSerializers.STRING);
/*  25 */   private static final EntityDataAccessor<Component> DATA_ID_LAST_OUTPUT = SynchedEntityData.defineId(MinecartCommandBlock.class, EntityDataSerializers.COMPONENT);
/*     */   
/*  27 */   private final BaseCommandBlock commandBlock = new MinecartCommandBase();
/*     */   
/*     */   private int lastActivated;
/*     */ 
/*     */   
/*     */   public MinecartCommandBlock(EntityType<? extends MinecartCommandBlock> debug1, Level debug2) {
/*  33 */     super(debug1, debug2);
/*     */   }
/*     */   
/*     */   public MinecartCommandBlock(Level debug1, double debug2, double debug4, double debug6) {
/*  37 */     super(EntityType.COMMAND_BLOCK_MINECART, debug1, debug2, debug4, debug6);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void defineSynchedData() {
/*  42 */     super.defineSynchedData();
/*  43 */     getEntityData().define(DATA_ID_COMMAND_NAME, "");
/*  44 */     getEntityData().define(DATA_ID_LAST_OUTPUT, TextComponent.EMPTY);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(CompoundTag debug1) {
/*  49 */     super.readAdditionalSaveData(debug1);
/*  50 */     this.commandBlock.load(debug1);
/*  51 */     getEntityData().set(DATA_ID_COMMAND_NAME, getCommandBlock().getCommand());
/*  52 */     getEntityData().set(DATA_ID_LAST_OUTPUT, getCommandBlock().getLastOutput());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(CompoundTag debug1) {
/*  57 */     super.addAdditionalSaveData(debug1);
/*  58 */     this.commandBlock.save(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public AbstractMinecart.Type getMinecartType() {
/*  63 */     return AbstractMinecart.Type.COMMAND_BLOCK;
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getDefaultDisplayBlockState() {
/*  68 */     return Blocks.COMMAND_BLOCK.defaultBlockState();
/*     */   }
/*     */   
/*     */   public BaseCommandBlock getCommandBlock() {
/*  72 */     return this.commandBlock;
/*     */   }
/*     */ 
/*     */   
/*     */   public void activateMinecart(int debug1, int debug2, int debug3, boolean debug4) {
/*  77 */     if (debug4 && 
/*  78 */       this.tickCount - this.lastActivated >= 4) {
/*  79 */       getCommandBlock().performCommand(this.level);
/*  80 */       this.lastActivated = this.tickCount;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public InteractionResult interact(Player debug1, InteractionHand debug2) {
/*  87 */     return this.commandBlock.usedBy(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onSyncedDataUpdated(EntityDataAccessor<?> debug1) {
/*  92 */     super.onSyncedDataUpdated(debug1);
/*     */     
/*  94 */     if (DATA_ID_LAST_OUTPUT.equals(debug1)) {
/*     */       try {
/*  96 */         this.commandBlock.setLastOutput((Component)getEntityData().get(DATA_ID_LAST_OUTPUT));
/*  97 */       } catch (Throwable throwable) {}
/*     */     }
/*  99 */     else if (DATA_ID_COMMAND_NAME.equals(debug1)) {
/* 100 */       this.commandBlock.setCommand((String)getEntityData().get(DATA_ID_COMMAND_NAME));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean onlyOpCanSetNbt() {
/* 106 */     return true;
/*     */   }
/*     */   
/*     */   public class MinecartCommandBase
/*     */     extends BaseCommandBlock {
/*     */     public ServerLevel getLevel() {
/* 112 */       return (ServerLevel)MinecartCommandBlock.this.level;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onUpdated() {
/* 117 */       MinecartCommandBlock.this.getEntityData().set(MinecartCommandBlock.DATA_ID_COMMAND_NAME, getCommand());
/* 118 */       MinecartCommandBlock.this.getEntityData().set(MinecartCommandBlock.DATA_ID_LAST_OUTPUT, getLastOutput());
/*     */     }
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
/*     */     public CommandSourceStack createCommandSourceStack() {
/* 132 */       return new CommandSourceStack((CommandSource)this, MinecartCommandBlock.this.position(), MinecartCommandBlock.this.getRotationVector(), getLevel(), 2, getName().getString(), MinecartCommandBlock.this.getDisplayName(), getLevel().getServer(), MinecartCommandBlock.this);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\vehicle\MinecartCommandBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */