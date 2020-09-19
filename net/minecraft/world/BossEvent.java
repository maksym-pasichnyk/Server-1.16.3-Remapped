/*     */ package net.minecraft.world;
/*     */ 
/*     */ import java.util.UUID;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.network.chat.Component;
/*     */ 
/*     */ public abstract class BossEvent
/*     */ {
/*     */   private final UUID id;
/*     */   protected Component name;
/*     */   protected float percent;
/*     */   protected BossBarColor color;
/*     */   protected BossBarOverlay overlay;
/*     */   protected boolean darkenScreen;
/*     */   protected boolean playBossMusic;
/*     */   protected boolean createWorldFog;
/*     */   
/*     */   public BossEvent(UUID debug1, Component debug2, BossBarColor debug3, BossBarOverlay debug4) {
/*  19 */     this.id = debug1;
/*  20 */     this.name = debug2;
/*  21 */     this.color = debug3;
/*  22 */     this.overlay = debug4;
/*  23 */     this.percent = 1.0F;
/*     */   }
/*     */   
/*     */   public UUID getId() {
/*  27 */     return this.id;
/*     */   }
/*     */   
/*     */   public Component getName() {
/*  31 */     return this.name;
/*     */   }
/*     */   
/*     */   public void setName(Component debug1) {
/*  35 */     this.name = debug1;
/*     */   }
/*     */   
/*     */   public float getPercent() {
/*  39 */     return this.percent;
/*     */   }
/*     */   
/*     */   public void setPercent(float debug1) {
/*  43 */     this.percent = debug1;
/*     */   }
/*     */   
/*     */   public BossBarColor getColor() {
/*  47 */     return this.color;
/*     */   }
/*     */   
/*     */   public void setColor(BossBarColor debug1) {
/*  51 */     this.color = debug1;
/*     */   }
/*     */   
/*     */   public BossBarOverlay getOverlay() {
/*  55 */     return this.overlay;
/*     */   }
/*     */   
/*     */   public void setOverlay(BossBarOverlay debug1) {
/*  59 */     this.overlay = debug1;
/*     */   }
/*     */   
/*     */   public boolean shouldDarkenScreen() {
/*  63 */     return this.darkenScreen;
/*     */   }
/*     */   
/*     */   public BossEvent setDarkenScreen(boolean debug1) {
/*  67 */     this.darkenScreen = debug1;
/*  68 */     return this;
/*     */   }
/*     */   
/*     */   public boolean shouldPlayBossMusic() {
/*  72 */     return this.playBossMusic;
/*     */   }
/*     */   
/*     */   public BossEvent setPlayBossMusic(boolean debug1) {
/*  76 */     this.playBossMusic = debug1;
/*  77 */     return this;
/*     */   }
/*     */   
/*     */   public BossEvent setCreateWorldFog(boolean debug1) {
/*  81 */     this.createWorldFog = debug1;
/*  82 */     return this;
/*     */   }
/*     */   
/*     */   public boolean shouldCreateWorldFog() {
/*  86 */     return this.createWorldFog;
/*     */   }
/*     */   
/*     */   public enum BossBarColor {
/*  90 */     PINK("pink", ChatFormatting.RED),
/*  91 */     BLUE("blue", ChatFormatting.BLUE),
/*  92 */     RED("red", ChatFormatting.DARK_RED),
/*  93 */     GREEN("green", ChatFormatting.GREEN),
/*  94 */     YELLOW("yellow", ChatFormatting.YELLOW),
/*  95 */     PURPLE("purple", ChatFormatting.DARK_BLUE),
/*  96 */     WHITE("white", ChatFormatting.WHITE);
/*     */     
/*     */     private final String name;
/*     */     
/*     */     private final ChatFormatting formatting;
/*     */     
/*     */     BossBarColor(String debug3, ChatFormatting debug4) {
/* 103 */       this.name = debug3;
/* 104 */       this.formatting = debug4;
/*     */     }
/*     */     
/*     */     public ChatFormatting getFormatting() {
/* 108 */       return this.formatting;
/*     */     }
/*     */     
/*     */     public String getName() {
/* 112 */       return this.name;
/*     */     }
/*     */     
/*     */     public static BossBarColor byName(String debug0) {
/* 116 */       for (BossBarColor debug4 : values()) {
/* 117 */         if (debug4.name.equals(debug0)) {
/* 118 */           return debug4;
/*     */         }
/*     */       } 
/* 121 */       return WHITE;
/*     */     }
/*     */   }
/*     */   
/*     */   public enum BossBarOverlay {
/* 126 */     PROGRESS("progress"),
/* 127 */     NOTCHED_6("notched_6"),
/* 128 */     NOTCHED_10("notched_10"),
/* 129 */     NOTCHED_12("notched_12"),
/* 130 */     NOTCHED_20("notched_20");
/*     */     
/*     */     private final String name;
/*     */ 
/*     */     
/*     */     BossBarOverlay(String debug3) {
/* 136 */       this.name = debug3;
/*     */     }
/*     */     
/*     */     public String getName() {
/* 140 */       return this.name;
/*     */     }
/*     */     
/*     */     public static BossBarOverlay byName(String debug0) {
/* 144 */       for (BossBarOverlay debug4 : values()) {
/* 145 */         if (debug4.name.equals(debug0)) {
/* 146 */           return debug4;
/*     */         }
/*     */       } 
/* 149 */       return PROGRESS;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\BossEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */