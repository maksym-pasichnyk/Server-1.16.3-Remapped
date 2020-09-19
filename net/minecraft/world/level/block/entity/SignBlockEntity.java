/*     */ package net.minecraft.world.level.block.entity;
/*     */ 
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.commands.CommandSource;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.network.chat.ClickEvent;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.ComponentUtils;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.network.chat.Style;
/*     */ import net.minecraft.network.chat.TextComponent;
/*     */ import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.util.FormattedCharSequence;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.DyeColor;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.phys.Vec2;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ 
/*     */ public class SignBlockEntity
/*     */   extends BlockEntity
/*     */ {
/*  30 */   private final Component[] messages = new Component[] { TextComponent.EMPTY, TextComponent.EMPTY, TextComponent.EMPTY, TextComponent.EMPTY };
/*     */   
/*     */   private boolean isEditable = true;
/*     */   
/*     */   private Player playerWhoMayEdit;
/*     */   
/*  36 */   private final FormattedCharSequence[] renderMessages = new FormattedCharSequence[4];
/*  37 */   private DyeColor color = DyeColor.BLACK;
/*     */   
/*     */   public SignBlockEntity() {
/*  40 */     super(BlockEntityType.SIGN);
/*     */   }
/*     */ 
/*     */   
/*     */   public CompoundTag save(CompoundTag debug1) {
/*  45 */     super.save(debug1);
/*  46 */     for (int debug2 = 0; debug2 < 4; debug2++) {
/*  47 */       String debug3 = Component.Serializer.toJson(this.messages[debug2]);
/*  48 */       debug1.putString("Text" + (debug2 + 1), debug3);
/*     */     } 
/*  50 */     debug1.putString("Color", this.color.getName());
/*     */     
/*  52 */     return debug1;
/*     */   }
/*     */ 
/*     */   
/*     */   public void load(BlockState debug1, CompoundTag debug2) {
/*  57 */     this.isEditable = false;
/*  58 */     super.load(debug1, debug2);
/*     */     
/*  60 */     this.color = DyeColor.byName(debug2.getString("Color"), DyeColor.BLACK);
/*     */     
/*  62 */     for (int debug3 = 0; debug3 < 4; debug3++) {
/*  63 */       String debug4 = debug2.getString("Text" + (debug3 + 1));
/*  64 */       MutableComponent mutableComponent = Component.Serializer.fromJson(debug4.isEmpty() ? "\"\"" : debug4);
/*  65 */       if (this.level instanceof ServerLevel) {
/*     */         try {
/*  67 */           this.messages[debug3] = (Component)ComponentUtils.updateForEntity(createCommandSourceStack((ServerPlayer)null), (Component)mutableComponent, null, 0);
/*  68 */         } catch (CommandSyntaxException debug6) {
/*  69 */           this.messages[debug3] = (Component)mutableComponent;
/*     */         } 
/*     */       } else {
/*  72 */         this.messages[debug3] = (Component)mutableComponent;
/*     */       } 
/*  74 */       this.renderMessages[debug3] = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMessage(int debug1, Component debug2) {
/*  83 */     this.messages[debug1] = debug2;
/*  84 */     this.renderMessages[debug1] = null;
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
/*     */   @Nullable
/*     */   public ClientboundBlockEntityDataPacket getUpdatePacket() {
/*  98 */     return new ClientboundBlockEntityDataPacket(this.worldPosition, 9, getUpdateTag());
/*     */   }
/*     */ 
/*     */   
/*     */   public CompoundTag getUpdateTag() {
/* 103 */     return save(new CompoundTag());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean onlyOpCanSetNbt() {
/* 108 */     return true;
/*     */   }
/*     */   
/*     */   public boolean isEditable() {
/* 112 */     return this.isEditable;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAllowedPlayerEditor(Player debug1) {
/* 123 */     this.playerWhoMayEdit = debug1;
/*     */   }
/*     */   
/*     */   public Player getPlayerWhoMayEdit() {
/* 127 */     return this.playerWhoMayEdit;
/*     */   }
/*     */   
/*     */   public boolean executeClickCommands(Player debug1) {
/* 131 */     for (Component debug5 : this.messages) {
/* 132 */       Style debug6 = (debug5 == null) ? null : debug5.getStyle();
/* 133 */       if (debug6 != null && debug6.getClickEvent() != null) {
/*     */ 
/*     */ 
/*     */         
/* 137 */         ClickEvent debug7 = debug6.getClickEvent();
/* 138 */         if (debug7.getAction() == ClickEvent.Action.RUN_COMMAND)
/* 139 */           debug1.getServer().getCommands().performCommand(createCommandSourceStack((ServerPlayer)debug1), debug7.getValue()); 
/*     */       } 
/*     */     } 
/* 142 */     return true;
/*     */   }
/*     */   
/*     */   public CommandSourceStack createCommandSourceStack(@Nullable ServerPlayer debug1) {
/* 146 */     String debug2 = (debug1 == null) ? "Sign" : debug1.getName().getString();
/* 147 */     Component debug3 = (debug1 == null) ? (Component)new TextComponent("Sign") : debug1.getDisplayName();
/* 148 */     return new CommandSourceStack(CommandSource.NULL, Vec3.atCenterOf((Vec3i)this.worldPosition), Vec2.ZERO, (ServerLevel)this.level, 2, debug2, debug3, this.level.getServer(), (Entity)debug1);
/*     */   }
/*     */   
/*     */   public DyeColor getColor() {
/* 152 */     return this.color;
/*     */   }
/*     */   
/*     */   public boolean setColor(DyeColor debug1) {
/* 156 */     if (debug1 != getColor()) {
/* 157 */       this.color = debug1;
/* 158 */       setChanged();
/* 159 */       this.level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
/* 160 */       return true;
/*     */     } 
/* 162 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\entity\SignBlockEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */