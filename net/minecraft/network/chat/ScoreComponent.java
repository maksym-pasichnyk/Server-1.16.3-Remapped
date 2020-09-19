/*     */ package net.minecraft.network.chat;
/*     */ 
/*     */ import com.mojang.brigadier.StringReader;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import java.util.List;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.commands.arguments.EntityArgument;
/*     */ import net.minecraft.commands.arguments.selector.EntitySelector;
/*     */ import net.minecraft.commands.arguments.selector.EntitySelectorParser;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.server.ServerScoreboard;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.scores.Objective;
/*     */ import net.minecraft.world.scores.Score;
/*     */ 
/*     */ public class ScoreComponent
/*     */   extends BaseComponent
/*     */   implements ContextAwareComponent
/*     */ {
/*     */   private final String name;
/*     */   @Nullable
/*     */   private final EntitySelector selector;
/*     */   private final String objective;
/*     */   
/*     */   @Nullable
/*     */   private static EntitySelector parseSelector(String debug0) {
/*     */     try {
/*  29 */       return (new EntitySelectorParser(new StringReader(debug0))).parse();
/*  30 */     } catch (CommandSyntaxException commandSyntaxException) {
/*     */       
/*  32 */       return null;
/*     */     } 
/*     */   }
/*     */   public ScoreComponent(String debug1, String debug2) {
/*  36 */     this(debug1, parseSelector(debug1), debug2);
/*     */   }
/*     */   
/*     */   private ScoreComponent(String debug1, @Nullable EntitySelector debug2, String debug3) {
/*  40 */     this.name = debug1;
/*  41 */     this.selector = debug2;
/*  42 */     this.objective = debug3;
/*     */   }
/*     */   
/*     */   public String getName() {
/*  46 */     return this.name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getObjective() {
/*  55 */     return this.objective;
/*     */   }
/*     */   
/*     */   private String findTargetName(CommandSourceStack debug1) throws CommandSyntaxException {
/*  59 */     if (this.selector != null) {
/*  60 */       List<? extends Entity> debug2 = this.selector.findEntities(debug1);
/*  61 */       if (!debug2.isEmpty()) {
/*  62 */         if (debug2.size() != 1) {
/*  63 */           throw EntityArgument.ERROR_NOT_SINGLE_ENTITY.create();
/*     */         }
/*  65 */         return ((Entity)debug2.get(0)).getScoreboardName();
/*     */       } 
/*     */     } 
/*  68 */     return this.name;
/*     */   }
/*     */   
/*     */   private String getScore(String debug1, CommandSourceStack debug2) {
/*  72 */     MinecraftServer debug3 = debug2.getServer();
/*  73 */     if (debug3 != null) {
/*  74 */       ServerScoreboard serverScoreboard = debug3.getScoreboard();
/*  75 */       Objective debug5 = serverScoreboard.getObjective(this.objective);
/*  76 */       if (serverScoreboard.hasPlayerScore(debug1, debug5)) {
/*  77 */         Score debug6 = serverScoreboard.getOrCreatePlayerScore(debug1, debug5);
/*  78 */         return Integer.toString(debug6.getScore());
/*     */       } 
/*     */     } 
/*  81 */     return "";
/*     */   }
/*     */ 
/*     */   
/*     */   public ScoreComponent plainCopy() {
/*  86 */     return new ScoreComponent(this.name, this.selector, this.objective);
/*     */   }
/*     */ 
/*     */   
/*     */   public MutableComponent resolve(@Nullable CommandSourceStack debug1, @Nullable Entity debug2, int debug3) throws CommandSyntaxException {
/*  91 */     if (debug1 == null) {
/*  92 */       return new TextComponent("");
/*     */     }
/*     */     
/*  95 */     String debug4 = findTargetName(debug1);
/*  96 */     String debug5 = (debug2 != null && debug4.equals("*")) ? debug2.getScoreboardName() : debug4;
/*  97 */     return new TextComponent(getScore(debug5, debug1));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object debug1) {
/* 102 */     if (this == debug1) {
/* 103 */       return true;
/*     */     }
/*     */     
/* 106 */     if (debug1 instanceof ScoreComponent) {
/* 107 */       ScoreComponent debug2 = (ScoreComponent)debug1;
/* 108 */       return (this.name.equals(debug2.name) && this.objective.equals(debug2.objective) && super.equals(debug1));
/*     */     } 
/*     */     
/* 111 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 116 */     return "ScoreComponent{name='" + this.name + '\'' + "objective='" + this.objective + '\'' + ", siblings=" + this.siblings + ", style=" + 
/*     */ 
/*     */ 
/*     */       
/* 120 */       getStyle() + '}';
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\chat\ScoreComponent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */