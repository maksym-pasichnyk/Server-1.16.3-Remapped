/*     */ package com.mojang.datafixers;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.datafixers.functions.PointFreeRule;
/*     */ import com.mojang.datafixers.schemas.Schema;
/*     */ import com.mojang.datafixers.types.Type;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import it.unimi.dsi.fastutil.ints.Int2ObjectSortedMap;
/*     */ import it.unimi.dsi.fastutil.ints.IntSortedSet;
/*     */ import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
/*     */ import it.unimi.dsi.fastutil.longs.Long2ObjectMaps;
/*     */ import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
/*     */ import java.util.List;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DataFixerUpper
/*     */   implements DataFixer
/*     */ {
/*     */   public static boolean ERRORS_ARE_FATAL = false;
/*  44 */   private static final Logger LOGGER = LogManager.getLogger();
/*     */   static {
/*  46 */     OPTIMIZATION_RULE = DataFixUtils.<PointFreeRule>make(() -> {
/*     */           PointFreeRule opSimple = PointFreeRule.orElse(PointFreeRule.orElse((PointFreeRule)PointFreeRule.CataFuseSame.INSTANCE, PointFreeRule.orElse((PointFreeRule)PointFreeRule.CataFuseDifferent.INSTANCE, (PointFreeRule)PointFreeRule.LensAppId.INSTANCE)), PointFreeRule.orElse((PointFreeRule)PointFreeRule.LensComp.INSTANCE, PointFreeRule.orElse((PointFreeRule)PointFreeRule.AppNest.INSTANCE, (PointFreeRule)PointFreeRule.LensCompFunc.INSTANCE)));
/*     */           PointFreeRule opLeft = PointFreeRule.many(PointFreeRule.once(PointFreeRule.orElse(opSimple, (PointFreeRule)PointFreeRule.CompAssocLeft.INSTANCE)));
/*     */           PointFreeRule opComp = PointFreeRule.many(PointFreeRule.once(PointFreeRule.orElse((PointFreeRule)PointFreeRule.SortInj.INSTANCE, (PointFreeRule)PointFreeRule.SortProj.INSTANCE)));
/*     */           PointFreeRule opRight = PointFreeRule.many(PointFreeRule.once(PointFreeRule.orElse(opSimple, (PointFreeRule)PointFreeRule.CompAssocRight.INSTANCE)));
/*     */           return PointFreeRule.seq((List)ImmutableList.of((), (), (), (), ()));
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static final PointFreeRule OPTIMIZATION_RULE;
/*     */ 
/*     */ 
/*     */   
/*     */   private final Int2ObjectSortedMap<Schema> schemas;
/*     */ 
/*     */ 
/*     */   
/*     */   private final List<DataFix> globalList;
/*     */ 
/*     */   
/*     */   private final IntSortedSet fixerVersions;
/*     */ 
/*     */   
/*  72 */   private final Long2ObjectMap<TypeRewriteRule> rules = Long2ObjectMaps.synchronize((Long2ObjectMap)new Long2ObjectOpenHashMap());
/*     */   
/*     */   protected DataFixerUpper(Int2ObjectSortedMap<Schema> schemas, List<DataFix> globalList, IntSortedSet fixerVersions) {
/*  75 */     this.schemas = schemas;
/*  76 */     this.globalList = globalList;
/*  77 */     this.fixerVersions = fixerVersions;
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> Dynamic<T> update(DSL.TypeReference type, Dynamic<T> input, int version, int newVersion) {
/*  82 */     if (version < newVersion) {
/*  83 */       Type<?> dataType = getType(type, version);
/*  84 */       DataResult<T> read = dataType.readAndWrite(input.getOps(), getType(type, newVersion), getRule(version, newVersion), OPTIMIZATION_RULE, input.getValue());
/*  85 */       T result = read.resultOrPartial(LOGGER::error).orElse((T)input.getValue());
/*  86 */       return new Dynamic(input.getOps(), result);
/*     */     } 
/*  88 */     return input;
/*     */   }
/*     */ 
/*     */   
/*     */   public Schema getSchema(int key) {
/*  93 */     return (Schema)this.schemas.get(getLowestSchemaSameVersion(this.schemas, key));
/*     */   }
/*     */   
/*     */   protected Type<?> getType(DSL.TypeReference type, int version) {
/*  97 */     return getSchema(DataFixUtils.makeKey(version)).getType(type);
/*     */   }
/*     */   
/*     */   protected static int getLowestSchemaSameVersion(Int2ObjectSortedMap<Schema> schemas, int versionKey) {
/* 101 */     if (versionKey < schemas.firstIntKey())
/*     */     {
/* 103 */       return schemas.firstIntKey();
/*     */     }
/* 105 */     return schemas.subMap(0, versionKey + 1).lastIntKey();
/*     */   }
/*     */   
/*     */   private int getLowestFixSameVersion(int versionKey) {
/* 109 */     if (versionKey < this.fixerVersions.firstInt())
/*     */     {
/* 111 */       return this.fixerVersions.firstInt() - 1;
/*     */     }
/* 113 */     return this.fixerVersions.subSet(0, versionKey + 1).lastInt();
/*     */   }
/*     */   
/*     */   protected TypeRewriteRule getRule(int version, int dataVersion) {
/* 117 */     if (version >= dataVersion) {
/* 118 */       return TypeRewriteRule.nop();
/*     */     }
/*     */     
/* 121 */     int expandedVersion = getLowestFixSameVersion(DataFixUtils.makeKey(version));
/* 122 */     int expandedDataVersion = DataFixUtils.makeKey(dataVersion);
/*     */     
/* 124 */     long key = expandedVersion << 32L | expandedDataVersion;
/* 125 */     return (TypeRewriteRule)this.rules.computeIfAbsent(Long.valueOf(key), k -> {
/*     */           List<TypeRewriteRule> rules = Lists.newArrayList();
/*     */           for (DataFix fix : this.globalList) {
/*     */             int fixVersion = fix.getVersionKey();
/*     */             if (fixVersion > expandedVersion && fixVersion <= expandedDataVersion) {
/*     */               TypeRewriteRule fixRule = fix.getRule();
/*     */               if (fixRule == TypeRewriteRule.nop()) {
/*     */                 continue;
/*     */               }
/*     */               rules.add(fixRule);
/*     */             } 
/*     */           } 
/*     */           return TypeRewriteRule.seq(rules);
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   protected IntSortedSet fixerVersions() {
/* 143 */     return this.fixerVersions;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\datafixers\DataFixerUpper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */