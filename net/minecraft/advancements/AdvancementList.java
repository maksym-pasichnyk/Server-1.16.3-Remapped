/*     */ package net.minecraft.advancements;
/*     */ 
/*     */ import com.google.common.base.Function;
/*     */ import com.google.common.base.Functions;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.common.collect.Sets;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.function.Function;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.resources.ResourceLocation;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public class AdvancementList {
/*  18 */   private static final Logger LOGGER = LogManager.getLogger();
/*     */   
/*  20 */   private final Map<ResourceLocation, Advancement> advancements = Maps.newHashMap();
/*  21 */   private final Set<Advancement> roots = Sets.newLinkedHashSet();
/*  22 */   private final Set<Advancement> tasks = Sets.newLinkedHashSet();
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
/*     */   private Listener listener;
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
/*     */   public void add(Map<ResourceLocation, Advancement.Builder> debug1) {
/*  57 */     Function function = Functions.forMap(this.advancements, null);
/*     */     
/*  59 */     while (!debug1.isEmpty()) {
/*  60 */       boolean debug3 = false; Iterator<Map.Entry<ResourceLocation, Advancement.Builder>> debug4;
/*  61 */       for (debug4 = debug1.entrySet().iterator(); debug4.hasNext(); ) {
/*  62 */         Map.Entry<ResourceLocation, Advancement.Builder> debug5 = debug4.next();
/*  63 */         ResourceLocation debug6 = debug5.getKey();
/*  64 */         Advancement.Builder debug7 = debug5.getValue();
/*  65 */         if (debug7.canBuild((Function<ResourceLocation, Advancement>)function)) {
/*  66 */           Advancement debug8 = debug7.build(debug6);
/*  67 */           this.advancements.put(debug6, debug8);
/*  68 */           debug3 = true;
/*  69 */           debug4.remove();
/*     */           
/*  71 */           if (debug8.getParent() == null) {
/*  72 */             this.roots.add(debug8);
/*  73 */             if (this.listener != null)
/*  74 */               this.listener.onAddAdvancementRoot(debug8); 
/*     */             continue;
/*     */           } 
/*  77 */           this.tasks.add(debug8);
/*  78 */           if (this.listener != null) {
/*  79 */             this.listener.onAddAdvancementTask(debug8);
/*     */           }
/*     */         } 
/*     */       } 
/*     */       
/*  84 */       if (!debug3) {
/*  85 */         for (debug4 = debug1.entrySet().iterator(); debug4.hasNext(); ) { Map.Entry<ResourceLocation, Advancement.Builder> debug5 = debug4.next();
/*  86 */           LOGGER.error("Couldn't load advancement {}: {}", debug5.getKey(), debug5.getValue()); }
/*     */ 
/*     */         
/*     */         break;
/*     */       } 
/*     */     } 
/*  92 */     LOGGER.info("Loaded {} advancements", Integer.valueOf(this.advancements.size()));
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
/*     */   public Iterable<Advancement> getRoots() {
/* 105 */     return this.roots;
/*     */   }
/*     */   
/*     */   public Collection<Advancement> getAllAdvancements() {
/* 109 */     return this.advancements.values();
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public Advancement get(ResourceLocation debug1) {
/* 114 */     return this.advancements.get(debug1);
/*     */   }
/*     */   
/*     */   public static interface Listener {
/*     */     void onAddAdvancementRoot(Advancement param1Advancement);
/*     */     
/*     */     void onAddAdvancementTask(Advancement param1Advancement);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\advancements\AdvancementList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */