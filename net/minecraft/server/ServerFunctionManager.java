/*     */ package net.minecraft.server;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.brigadier.CommandDispatcher;
/*     */ import java.util.ArrayDeque;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.commands.CommandFunction;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.resources.ResourceLocation;
/*     */ import net.minecraft.tags.Tag;
/*     */ import net.minecraft.world.level.GameRules;
/*     */ 
/*     */ 
/*     */ public class ServerFunctionManager
/*     */ {
/*  18 */   private static final ResourceLocation TICK_FUNCTION_TAG = new ResourceLocation("tick");
/*  19 */   private static final ResourceLocation LOAD_FUNCTION_TAG = new ResourceLocation("load");
/*     */   
/*     */   private final MinecraftServer server;
/*     */   
/*     */   private boolean isInFunction;
/*  24 */   private final ArrayDeque<QueuedCommand> commandQueue = new ArrayDeque<>();
/*  25 */   private final List<QueuedCommand> nestedCalls = Lists.newArrayList();
/*     */   
/*  27 */   private final List<CommandFunction> ticking = Lists.newArrayList();
/*     */   
/*     */   private boolean postReload;
/*     */   private ServerFunctionLibrary library;
/*     */   
/*     */   public ServerFunctionManager(MinecraftServer debug1, ServerFunctionLibrary debug2) {
/*  33 */     this.server = debug1;
/*  34 */     this.library = debug2;
/*  35 */     postReload(debug2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getCommandLimit() {
/*  43 */     return this.server.getGameRules().getInt(GameRules.RULE_MAX_COMMAND_CHAIN_LENGTH);
/*     */   }
/*     */   
/*     */   public CommandDispatcher<CommandSourceStack> getDispatcher() {
/*  47 */     return this.server.getCommands().getDispatcher();
/*     */   }
/*     */   
/*     */   public void tick() {
/*  51 */     executeTagFunctions(this.ticking, TICK_FUNCTION_TAG);
/*     */     
/*  53 */     if (this.postReload) {
/*  54 */       this.postReload = false;
/*  55 */       Collection<CommandFunction> debug1 = this.library.getTags().getTagOrEmpty(LOAD_FUNCTION_TAG).getValues();
/*  56 */       executeTagFunctions(debug1, LOAD_FUNCTION_TAG);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void executeTagFunctions(Collection<CommandFunction> debug1, ResourceLocation debug2) {
/*  61 */     this.server.getProfiler().push(debug2::toString);
/*  62 */     for (CommandFunction debug4 : debug1) {
/*  63 */       execute(debug4, getGameLoopSender());
/*     */     }
/*  65 */     this.server.getProfiler().pop();
/*     */   }
/*     */   
/*     */   public int execute(CommandFunction debug1, CommandSourceStack debug2) {
/*  69 */     int debug3 = getCommandLimit();
/*     */     
/*  71 */     if (this.isInFunction) {
/*     */       
/*  73 */       if (this.commandQueue.size() + this.nestedCalls.size() < debug3) {
/*  74 */         this.nestedCalls.add(new QueuedCommand(this, debug2, (CommandFunction.Entry)new CommandFunction.FunctionEntry(debug1)));
/*     */       }
/*  76 */       return 0;
/*     */     } 
/*     */     
/*     */     try {
/*  80 */       this.isInFunction = true;
/*  81 */       int debug4 = 0;
/*  82 */       CommandFunction.Entry[] debug5 = debug1.getEntries(); int debug6;
/*  83 */       for (debug6 = debug5.length - 1; debug6 >= 0; debug6--) {
/*  84 */         this.commandQueue.push(new QueuedCommand(this, debug2, debug5[debug6]));
/*     */       }
/*     */       
/*  87 */       while (!this.commandQueue.isEmpty()) {
/*     */         try {
/*  89 */           QueuedCommand queuedCommand = this.commandQueue.removeFirst();
/*  90 */           this.server.getProfiler().push(queuedCommand::toString);
/*  91 */           queuedCommand.execute(this.commandQueue, debug3);
/*  92 */           if (!this.nestedCalls.isEmpty()) {
/*  93 */             Lists.reverse(this.nestedCalls).forEach(this.commandQueue::addFirst);
/*  94 */             this.nestedCalls.clear();
/*     */           } 
/*     */         } finally {
/*  97 */           this.server.getProfiler().pop();
/*     */         } 
/*  99 */         debug4++;
/*     */         
/* 101 */         if (debug4 >= debug3) {
/* 102 */           debug6 = debug4; return debug6;
/*     */         } 
/*     */       } 
/*     */       
/* 106 */       debug6 = debug4; return debug6;
/*     */     } finally {
/*     */       
/* 109 */       this.commandQueue.clear();
/* 110 */       this.nestedCalls.clear();
/* 111 */       this.isInFunction = false;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void replaceLibrary(ServerFunctionLibrary debug1) {
/* 116 */     this.library = debug1;
/* 117 */     postReload(debug1);
/*     */   }
/*     */   
/*     */   private void postReload(ServerFunctionLibrary debug1) {
/* 121 */     this.ticking.clear();
/* 122 */     this.ticking.addAll(debug1.getTags().getTagOrEmpty(TICK_FUNCTION_TAG).getValues());
/* 123 */     this.postReload = true;
/*     */   }
/*     */   
/*     */   public CommandSourceStack getGameLoopSender() {
/* 127 */     return this.server.createCommandSourceStack().withPermission(2).withSuppressedOutput();
/*     */   }
/*     */   
/*     */   public Optional<CommandFunction> get(ResourceLocation debug1) {
/* 131 */     return this.library.getFunction(debug1);
/*     */   }
/*     */   
/*     */   public Tag<CommandFunction> getTag(ResourceLocation debug1) {
/* 135 */     return this.library.getTag(debug1);
/*     */   }
/*     */   
/*     */   public Iterable<ResourceLocation> getFunctionNames() {
/* 139 */     return this.library.getFunctions().keySet();
/*     */   }
/*     */   
/*     */   public Iterable<ResourceLocation> getTagNames() {
/* 143 */     return this.library.getTags().getAvailableTags();
/*     */   }
/*     */   
/*     */   public static class QueuedCommand {
/*     */     private final ServerFunctionManager manager;
/*     */     private final CommandSourceStack sender;
/*     */     private final CommandFunction.Entry entry;
/*     */     
/*     */     public QueuedCommand(ServerFunctionManager debug1, CommandSourceStack debug2, CommandFunction.Entry debug3) {
/* 152 */       this.manager = debug1;
/* 153 */       this.sender = debug2;
/* 154 */       this.entry = debug3;
/*     */     }
/*     */     
/*     */     public void execute(ArrayDeque<QueuedCommand> debug1, int debug2) {
/*     */       try {
/* 159 */         this.entry.execute(this.manager, this.sender, debug1, debug2);
/* 160 */       } catch (Throwable throwable) {}
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public String toString() {
/* 166 */       return this.entry.toString();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\ServerFunctionManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */