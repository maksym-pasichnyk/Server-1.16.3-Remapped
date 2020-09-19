/*     */ package net.minecraft.network.chat;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.authlib.GameProfile;
/*     */ import com.mojang.brigadier.Message;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.function.Function;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ 
/*     */ public class ComponentUtils
/*     */ {
/*     */   public static MutableComponent mergeStyles(MutableComponent debug0, Style debug1) {
/*  18 */     if (debug1.isEmpty()) {
/*  19 */       return debug0;
/*     */     }
/*     */     
/*  22 */     Style debug2 = debug0.getStyle();
/*  23 */     if (debug2.isEmpty()) {
/*  24 */       return debug0.setStyle(debug1);
/*     */     }
/*     */     
/*  27 */     if (debug2.equals(debug1)) {
/*  28 */       return debug0;
/*     */     }
/*     */     
/*  31 */     return debug0.setStyle(debug2.applyTo(debug1));
/*     */   }
/*     */   
/*     */   public static MutableComponent updateForEntity(@Nullable CommandSourceStack debug0, Component debug1, @Nullable Entity debug2, int debug3) throws CommandSyntaxException {
/*  35 */     if (debug3 > 100) {
/*  36 */       return debug1.copy();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*  41 */     MutableComponent debug4 = (debug1 instanceof ContextAwareComponent) ? ((ContextAwareComponent)debug1).resolve(debug0, debug2, debug3 + 1) : debug1.plainCopy();
/*     */     
/*  43 */     for (Component debug6 : debug1.getSiblings()) {
/*  44 */       debug4.append(updateForEntity(debug0, debug6, debug2, debug3 + 1));
/*     */     }
/*     */     
/*  47 */     return debug4.withStyle(resolveStyle(debug0, debug1.getStyle(), debug2, debug3));
/*     */   }
/*     */   
/*     */   private static Style resolveStyle(@Nullable CommandSourceStack debug0, Style debug1, @Nullable Entity debug2, int debug3) throws CommandSyntaxException {
/*  51 */     HoverEvent debug4 = debug1.getHoverEvent();
/*  52 */     if (debug4 != null) {
/*  53 */       Component debug5 = debug4.<Component>getValue(HoverEvent.Action.SHOW_TEXT);
/*  54 */       if (debug5 != null) {
/*  55 */         HoverEvent debug6 = new HoverEvent((HoverEvent.Action)HoverEvent.Action.SHOW_TEXT, (T)updateForEntity(debug0, debug5, debug2, debug3 + 1));
/*  56 */         return debug1.withHoverEvent(debug6);
/*     */       } 
/*     */     } 
/*     */     
/*  60 */     return debug1;
/*     */   }
/*     */   
/*     */   public static Component getDisplayName(GameProfile debug0) {
/*  64 */     if (debug0.getName() != null)
/*  65 */       return new TextComponent(debug0.getName()); 
/*  66 */     if (debug0.getId() != null) {
/*  67 */       return new TextComponent(debug0.getId().toString());
/*     */     }
/*  69 */     return new TextComponent("(unknown)");
/*     */   }
/*     */ 
/*     */   
/*     */   public static Component formatList(Collection<String> debug0) {
/*  74 */     return formatAndSortList(debug0, debug0 -> (new TextComponent(debug0)).withStyle(ChatFormatting.GREEN));
/*     */   }
/*     */   
/*     */   public static <T extends Comparable<T>> Component formatAndSortList(Collection<T> debug0, Function<T, Component> debug1) {
/*  78 */     if (debug0.isEmpty())
/*  79 */       return TextComponent.EMPTY; 
/*  80 */     if (debug0.size() == 1) {
/*  81 */       return debug1.apply(debug0.iterator().next());
/*     */     }
/*     */     
/*  84 */     List<T> debug2 = Lists.newArrayList(debug0);
/*  85 */     debug2.sort(Comparable::compareTo);
/*  86 */     return formatList(debug2, debug1);
/*     */   }
/*     */   
/*     */   public static <T> MutableComponent formatList(Collection<T> debug0, Function<T, Component> debug1) {
/*  90 */     if (debug0.isEmpty())
/*  91 */       return new TextComponent(""); 
/*  92 */     if (debug0.size() == 1) {
/*  93 */       return ((Component)debug1.apply(debug0.iterator().next())).copy();
/*     */     }
/*     */     
/*  96 */     MutableComponent debug2 = new TextComponent("");
/*  97 */     boolean debug3 = true;
/*  98 */     for (T debug5 : debug0) {
/*  99 */       if (!debug3) {
/* 100 */         debug2.append((new TextComponent(", ")).withStyle(ChatFormatting.GRAY));
/*     */       }
/* 102 */       debug2.append(debug1.apply(debug5));
/* 103 */       debug3 = false;
/*     */     } 
/*     */     
/* 106 */     return debug2;
/*     */   }
/*     */   
/*     */   public static MutableComponent wrapInSquareBrackets(Component debug0) {
/* 110 */     return new TranslatableComponent("chat.square_brackets", new Object[] { debug0 });
/*     */   }
/*     */   
/*     */   public static Component fromMessage(Message debug0) {
/* 114 */     if (debug0 instanceof Component) {
/* 115 */       return (Component)debug0;
/*     */     }
/* 117 */     return new TextComponent(debug0.getString());
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\chat\ComponentUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */