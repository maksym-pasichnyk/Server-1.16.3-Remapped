/*     */ package net.minecraft.server.packs.repository;
/*     */ import com.mojang.brigadier.arguments.StringArgumentType;
/*     */ import java.io.IOException;
/*     */ import java.util.List;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Supplier;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.ComponentUtils;
/*     */ import net.minecraft.network.chat.HoverEvent;
/*     */ import net.minecraft.network.chat.Style;
/*     */ import net.minecraft.network.chat.TextComponent;
/*     */ import net.minecraft.network.chat.TranslatableComponent;
/*     */ import net.minecraft.server.packs.PackResources;
/*     */ import net.minecraft.server.packs.metadata.MetadataSectionSerializer;
/*     */ import net.minecraft.server.packs.metadata.pack.PackMetadataSection;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public class Pack implements AutoCloseable {
/*  23 */   private static final Logger LOGGER = LogManager.getLogger();
/*  24 */   private static final PackMetadataSection BROKEN_ASSETS_FALLBACK = new PackMetadataSection((Component)(new TranslatableComponent("resourcePack.broken_assets")).withStyle(new ChatFormatting[] { ChatFormatting.RED, ChatFormatting.ITALIC }, ), SharedConstants.getCurrentVersion().getPackVersion());
/*     */   
/*     */   private final String id;
/*     */   private final Supplier<PackResources> supplier;
/*     */   private final Component title;
/*     */   private final Component description;
/*     */   private final PackCompatibility compatibility;
/*     */   private final Position defaultPosition;
/*     */   private final boolean required;
/*     */   private final boolean fixedPosition;
/*     */   private final PackSource packSource;
/*     */   
/*     */   @Nullable
/*     */   public static Pack create(String debug0, boolean debug1, Supplier<PackResources> debug2, PackConstructor debug3, Position debug4, PackSource debug5) {
/*  38 */     try (PackResources debug6 = (PackResources)debug2.get()) {
/*  39 */       PackMetadataSection debug8 = (PackMetadataSection)debug6.getMetadataSection((MetadataSectionSerializer)PackMetadataSection.SERIALIZER);
/*  40 */       if (debug1 && debug8 == null) {
/*  41 */         LOGGER.error("Broken/missing pack.mcmeta detected, fudging it into existance. Please check that your launcher has downloaded all assets for the game correctly!");
/*  42 */         debug8 = BROKEN_ASSETS_FALLBACK;
/*     */       } 
/*     */       
/*  45 */       if (debug8 != null) {
/*  46 */         return debug3.create(debug0, debug1, debug2, debug6, debug8, debug4, debug5);
/*     */       }
/*  48 */       LOGGER.warn("Couldn't find pack meta for pack {}", debug0);
/*     */     }
/*  50 */     catch (IOException debug6) {
/*  51 */       LOGGER.warn("Couldn't get pack info for: {}", debug6.toString());
/*     */     } 
/*  53 */     return null;
/*     */   }
/*     */   
/*     */   public Pack(String debug1, boolean debug2, Supplier<PackResources> debug3, Component debug4, Component debug5, PackCompatibility debug6, Position debug7, boolean debug8, PackSource debug9) {
/*  57 */     this.id = debug1;
/*  58 */     this.supplier = debug3;
/*  59 */     this.title = debug4;
/*  60 */     this.description = debug5;
/*  61 */     this.compatibility = debug6;
/*  62 */     this.required = debug2;
/*  63 */     this.defaultPosition = debug7;
/*  64 */     this.fixedPosition = debug8;
/*  65 */     this.packSource = debug9;
/*     */   }
/*     */   
/*     */   public Pack(String debug1, boolean debug2, Supplier<PackResources> debug3, PackResources debug4, PackMetadataSection debug5, Position debug6, PackSource debug7) {
/*  69 */     this(debug1, debug2, debug3, (Component)new TextComponent(debug4.getName()), debug5.getDescription(), PackCompatibility.forFormat(debug5.getPackFormat()), debug6, false, debug7);
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
/*     */   public Component getChatLink(boolean debug1) {
/*  81 */     return (Component)ComponentUtils.wrapInSquareBrackets(this.packSource.decorate((Component)new TextComponent(this.id))).withStyle(debug2 -> debug2.withColor(debug1 ? ChatFormatting.GREEN : ChatFormatting.RED).withInsertion(StringArgumentType.escapeIfRequired(this.id)).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, (new TextComponent("")).append(this.title).append("\n").append(this.description))));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PackCompatibility getCompatibility() {
/*  89 */     return this.compatibility;
/*     */   }
/*     */   
/*     */   public PackResources open() {
/*  93 */     return this.supplier.get();
/*     */   }
/*     */   
/*     */   public String getId() {
/*  97 */     return this.id;
/*     */   }
/*     */   
/*     */   public boolean isRequired() {
/* 101 */     return this.required;
/*     */   }
/*     */   
/*     */   public boolean isFixedPosition() {
/* 105 */     return this.fixedPosition;
/*     */   }
/*     */   
/*     */   public Position getDefaultPosition() {
/* 109 */     return this.defaultPosition;
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
/*     */ 
/*     */   
/*     */   public boolean equals(Object debug1) {
/* 124 */     if (this == debug1) {
/* 125 */       return true;
/*     */     }
/* 127 */     if (!(debug1 instanceof Pack)) {
/* 128 */       return false;
/*     */     }
/*     */     
/* 131 */     Pack debug2 = (Pack)debug1;
/*     */     
/* 133 */     return this.id.equals(debug2.id);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 138 */     return this.id.hashCode();
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() {}
/*     */   
/*     */   public enum Position
/*     */   {
/* 146 */     TOP,
/* 147 */     BOTTOM;
/*     */ 
/*     */     
/*     */     public <T> int insert(List<T> debug1, T debug2, Function<T, Pack> debug3, boolean debug4) {
/* 151 */       Position debug5 = debug4 ? opposite() : this;
/* 152 */       if (debug5 == BOTTOM) {
/* 153 */         int i = 0;
/* 154 */         while (i < debug1.size()) {
/* 155 */           Pack debug7 = debug3.apply(debug1.get(i));
/* 156 */           if (debug7.isFixedPosition() && debug7.getDefaultPosition() == this) {
/* 157 */             i++;
/*     */           }
/*     */         } 
/*     */ 
/*     */         
/* 162 */         debug1.add(i, debug2);
/* 163 */         return i;
/*     */       } 
/* 165 */       int debug6 = debug1.size() - 1;
/* 166 */       while (debug6 >= 0) {
/* 167 */         Pack debug7 = debug3.apply(debug1.get(debug6));
/* 168 */         if (debug7.isFixedPosition() && debug7.getDefaultPosition() == this) {
/* 169 */           debug6--;
/*     */         }
/*     */       } 
/*     */ 
/*     */       
/* 174 */       debug1.add(debug6 + 1, debug2);
/* 175 */       return debug6 + 1;
/*     */     }
/*     */ 
/*     */     
/*     */     public Position opposite() {
/* 180 */       return (this == TOP) ? BOTTOM : TOP;
/*     */     }
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   public static interface PackConstructor {
/*     */     @Nullable
/*     */     Pack create(String param1String, boolean param1Boolean, Supplier<PackResources> param1Supplier, PackResources param1PackResources, PackMetadataSection param1PackMetadataSection, Pack.Position param1Position, PackSource param1PackSource);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\packs\repository\Pack.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */