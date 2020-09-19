/*     */ package net.minecraft.world.level;
/*     */ 
/*     */ import com.mojang.brigadier.context.CommandContext;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Date;
/*     */ import java.util.UUID;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.CrashReport;
/*     */ import net.minecraft.CrashReportCategory;
/*     */ import net.minecraft.ReportedException;
/*     */ import net.minecraft.commands.CommandSource;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.TextComponent;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.StringUtil;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ 
/*     */ public abstract class BaseCommandBlock
/*     */   implements CommandSource
/*     */ {
/*  25 */   private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm:ss");
/*  26 */   private static final Component DEFAULT_NAME = (Component)new TextComponent("@");
/*     */   
/*  28 */   private long lastExecution = -1L;
/*     */   private boolean updateLastExecution = true;
/*     */   private int successCount;
/*     */   private boolean trackOutput = true;
/*     */   @Nullable
/*     */   private Component lastOutput;
/*  34 */   private String command = "";
/*  35 */   private Component name = DEFAULT_NAME;
/*     */   
/*     */   public int getSuccessCount() {
/*  38 */     return this.successCount;
/*     */   }
/*     */   
/*     */   public void setSuccessCount(int debug1) {
/*  42 */     this.successCount = debug1;
/*     */   }
/*     */   
/*     */   public Component getLastOutput() {
/*  46 */     return (this.lastOutput == null) ? TextComponent.EMPTY : this.lastOutput;
/*     */   }
/*     */   
/*     */   public CompoundTag save(CompoundTag debug1) {
/*  50 */     debug1.putString("Command", this.command);
/*  51 */     debug1.putInt("SuccessCount", this.successCount);
/*  52 */     debug1.putString("CustomName", Component.Serializer.toJson(this.name));
/*  53 */     debug1.putBoolean("TrackOutput", this.trackOutput);
/*  54 */     if (this.lastOutput != null && this.trackOutput) {
/*  55 */       debug1.putString("LastOutput", Component.Serializer.toJson(this.lastOutput));
/*     */     }
/*  57 */     debug1.putBoolean("UpdateLastExecution", this.updateLastExecution);
/*  58 */     if (this.updateLastExecution && this.lastExecution > 0L) {
/*  59 */       debug1.putLong("LastExecution", this.lastExecution);
/*     */     }
/*     */     
/*  62 */     return debug1;
/*     */   }
/*     */   
/*     */   public void load(CompoundTag debug1) {
/*  66 */     this.command = debug1.getString("Command");
/*  67 */     this.successCount = debug1.getInt("SuccessCount");
/*  68 */     if (debug1.contains("CustomName", 8)) {
/*  69 */       setName((Component)Component.Serializer.fromJson(debug1.getString("CustomName")));
/*     */     }
/*  71 */     if (debug1.contains("TrackOutput", 1)) {
/*  72 */       this.trackOutput = debug1.getBoolean("TrackOutput");
/*     */     }
/*  74 */     if (debug1.contains("LastOutput", 8) && this.trackOutput) {
/*     */       try {
/*  76 */         this.lastOutput = (Component)Component.Serializer.fromJson(debug1.getString("LastOutput"));
/*  77 */       } catch (Throwable debug2) {
/*  78 */         this.lastOutput = (Component)new TextComponent(debug2.getMessage());
/*     */       } 
/*     */     } else {
/*  81 */       this.lastOutput = null;
/*     */     } 
/*  83 */     if (debug1.contains("UpdateLastExecution")) {
/*  84 */       this.updateLastExecution = debug1.getBoolean("UpdateLastExecution");
/*     */     }
/*  86 */     if (this.updateLastExecution && debug1.contains("LastExecution")) {
/*  87 */       this.lastExecution = debug1.getLong("LastExecution");
/*     */     } else {
/*  89 */       this.lastExecution = -1L;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void setCommand(String debug1) {
/*  94 */     this.command = debug1;
/*  95 */     this.successCount = 0;
/*     */   }
/*     */   
/*     */   public String getCommand() {
/*  99 */     return this.command;
/*     */   }
/*     */   
/*     */   public boolean performCommand(Level debug1) {
/* 103 */     if (debug1.isClientSide || debug1.getGameTime() == this.lastExecution) {
/* 104 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 108 */     if ("Searge".equalsIgnoreCase(this.command)) {
/* 109 */       this.lastOutput = (Component)new TextComponent("#itzlipofutzli");
/* 110 */       this.successCount = 1;
/* 111 */       return true;
/*     */     } 
/*     */     
/* 114 */     this.successCount = 0;
/*     */     
/* 116 */     MinecraftServer debug2 = getLevel().getServer();
/* 117 */     if (debug2.isCommandBlockEnabled() && !StringUtil.isNullOrEmpty(this.command)) {
/*     */       try {
/* 119 */         this.lastOutput = null;
/* 120 */         CommandSourceStack debug3 = createCommandSourceStack().withCallback((debug1, debug2, debug3) -> {
/*     */               if (debug2) {
/*     */                 this.successCount++;
/*     */               }
/*     */             });
/* 125 */         debug2.getCommands().performCommand(debug3, this.command);
/* 126 */       } catch (Throwable debug3) {
/* 127 */         CrashReport debug4 = CrashReport.forThrowable(debug3, "Executing command block");
/* 128 */         CrashReportCategory debug5 = debug4.addCategory("Command to be executed");
/*     */         
/* 130 */         debug5.setDetail("Command", this::getCommand);
/*     */         
/* 132 */         debug5.setDetail("Name", () -> getName().getString());
/*     */         
/* 134 */         throw new ReportedException(debug4);
/*     */       } 
/*     */     }
/*     */     
/* 138 */     if (this.updateLastExecution) {
/* 139 */       this.lastExecution = debug1.getGameTime();
/*     */     } else {
/* 141 */       this.lastExecution = -1L;
/*     */     } 
/*     */     
/* 144 */     return true;
/*     */   }
/*     */   
/*     */   public Component getName() {
/* 148 */     return this.name;
/*     */   }
/*     */   
/*     */   public void setName(@Nullable Component debug1) {
/* 152 */     if (debug1 != null) {
/* 153 */       this.name = debug1;
/*     */     } else {
/* 155 */       this.name = DEFAULT_NAME;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void sendMessage(Component debug1, UUID debug2) {
/* 161 */     if (this.trackOutput) {
/* 162 */       this.lastOutput = (Component)(new TextComponent("[" + TIME_FORMAT.format(new Date()) + "] ")).append(debug1);
/* 163 */       onUpdated();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLastOutput(@Nullable Component debug1) {
/* 172 */     this.lastOutput = debug1;
/*     */   }
/*     */   
/*     */   public void setTrackOutput(boolean debug1) {
/* 176 */     this.trackOutput = debug1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public InteractionResult usedBy(Player debug1) {
/* 184 */     if (!debug1.canUseGameMasterBlocks()) {
/* 185 */       return InteractionResult.PASS;
/*     */     }
/* 187 */     if ((debug1.getCommandSenderWorld()).isClientSide) {
/* 188 */       debug1.openMinecartCommandBlock(this);
/*     */     }
/* 190 */     return InteractionResult.sidedSuccess(debug1.level.isClientSide);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean acceptsSuccess() {
/* 199 */     return (getLevel().getGameRules().getBoolean(GameRules.RULE_SENDCOMMANDFEEDBACK) && this.trackOutput);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean acceptsFailure() {
/* 204 */     return this.trackOutput;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean shouldInformAdmins() {
/* 209 */     return getLevel().getGameRules().getBoolean(GameRules.RULE_COMMANDBLOCKOUTPUT);
/*     */   }
/*     */   
/*     */   public abstract ServerLevel getLevel();
/*     */   
/*     */   public abstract void onUpdated();
/*     */   
/*     */   public abstract CommandSourceStack createCommandSourceStack();
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\BaseCommandBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */