/*     */ package net.minecraft.server;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.util.Set;
/*     */ import java.util.TreeSet;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.commands.Commands;
/*     */ import net.minecraft.commands.arguments.selector.options.EntitySelectorOptions;
/*     */ import net.minecraft.commands.synchronization.ArgumentTypes;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.dispenser.DispenseItemBehavior;
/*     */ import net.minecraft.locale.Language;
/*     */ import net.minecraft.resources.ResourceLocation;
/*     */ import net.minecraft.tags.StaticTags;
/*     */ import net.minecraft.world.effect.MobEffect;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.ai.attributes.Attribute;
/*     */ import net.minecraft.world.entity.ai.attributes.DefaultAttributes;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.alchemy.PotionBrewing;
/*     */ import net.minecraft.world.item.enchantment.Enchantment;
/*     */ import net.minecraft.world.level.GameRules;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.ComposterBlock;
/*     */ import net.minecraft.world.level.block.FireBlock;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public class Bootstrap {
/*  31 */   public static final PrintStream STDOUT = System.out;
/*     */   
/*     */   private static boolean isBootstrapped;
/*  34 */   private static final Logger LOGGER = LogManager.getLogger();
/*     */   
/*     */   public static void bootStrap() {
/*  37 */     if (isBootstrapped) {
/*     */       return;
/*     */     }
/*  40 */     isBootstrapped = true;
/*     */     
/*  42 */     if (Registry.REGISTRY.keySet().isEmpty()) {
/*  43 */       throw new IllegalStateException("Unable to load registries");
/*     */     }
/*     */     
/*  46 */     FireBlock.bootStrap();
/*  47 */     ComposterBlock.bootStrap();
/*     */     
/*  49 */     if (EntityType.getKey(EntityType.PLAYER) == null) {
/*  50 */       throw new IllegalStateException("Failed loading EntityTypes");
/*     */     }
/*     */     
/*  53 */     PotionBrewing.bootStrap();
/*     */     
/*  55 */     EntitySelectorOptions.bootStrap();
/*     */     
/*  57 */     DispenseItemBehavior.bootStrap();
/*     */     
/*  59 */     ArgumentTypes.bootStrap();
/*     */     
/*  61 */     StaticTags.bootStrap();
/*     */     
/*  63 */     wrapStreams();
/*     */   }
/*     */   
/*     */   private static <T> void checkTranslations(Iterable<T> debug0, Function<T, String> debug1, Set<String> debug2) {
/*  67 */     Language debug3 = Language.getInstance();
/*  68 */     debug0.forEach(debug3 -> {
/*     */           String debug4 = debug0.apply(debug3);
/*     */           if (!debug1.has(debug4)) {
/*     */             debug2.add(debug4);
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   private static void checkGameruleTranslations(final Set<String> missing) {
/*  77 */     final Language language = Language.getInstance();
/*  78 */     GameRules.visitGameRuleTypes(new GameRules.GameRuleTypeVisitor()
/*     */         {
/*     */           public <T extends GameRules.Value<T>> void visit(GameRules.Key<T> debug1, GameRules.Type<T> debug2) {
/*  81 */             if (!language.has(debug1.getDescriptionId())) {
/*  82 */               missing.add(debug1.getId());
/*     */             }
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   public static Set<String> getMissingTranslations() {
/*  89 */     Set<String> debug0 = new TreeSet<>();
/*  90 */     checkTranslations((Iterable<?>)Registry.ATTRIBUTE, Attribute::getDescriptionId, debug0);
/*  91 */     checkTranslations((Iterable<?>)Registry.ENTITY_TYPE, EntityType::getDescriptionId, debug0);
/*  92 */     checkTranslations((Iterable<?>)Registry.MOB_EFFECT, MobEffect::getDescriptionId, debug0);
/*  93 */     checkTranslations((Iterable<?>)Registry.ITEM, Item::getDescriptionId, debug0);
/*  94 */     checkTranslations((Iterable<?>)Registry.ENCHANTMENT, Enchantment::getDescriptionId, debug0);
/*  95 */     checkTranslations((Iterable<?>)Registry.BLOCK, Block::getDescriptionId, debug0);
/*  96 */     checkTranslations((Iterable<?>)Registry.CUSTOM_STAT, debug0 -> "stat." + debug0.toString().replace(':', '.'), debug0);
/*     */     
/*  98 */     checkGameruleTranslations(debug0);
/*  99 */     return debug0;
/*     */   }
/*     */   
/*     */   public static void validate() {
/* 103 */     if (!isBootstrapped) {
/* 104 */       throw new IllegalArgumentException("Not bootstrapped");
/*     */     }
/*     */     
/* 107 */     if (SharedConstants.IS_RUNNING_IN_IDE) {
/* 108 */       getMissingTranslations().forEach(debug0 -> LOGGER.error("Missing translations: " + debug0));
/* 109 */       Commands.validate();
/*     */     } 
/*     */     
/* 112 */     DefaultAttributes.validate();
/*     */   }
/*     */   
/*     */   private static void wrapStreams() {
/* 116 */     if (LOGGER.isDebugEnabled()) {
/* 117 */       System.setErr(new DebugLoggedPrintStream("STDERR", System.err));
/* 118 */       System.setOut(new DebugLoggedPrintStream("STDOUT", STDOUT));
/*     */     } else {
/* 120 */       System.setErr(new LoggedPrintStream("STDERR", System.err));
/* 121 */       System.setOut(new LoggedPrintStream("STDOUT", STDOUT));
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void realStdoutPrintln(String debug0) {
/* 126 */     STDOUT.println(debug0);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\Bootstrap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */