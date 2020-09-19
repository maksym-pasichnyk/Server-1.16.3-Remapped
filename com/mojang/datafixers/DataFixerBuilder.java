/*    */ package com.mojang.datafixers;
/*    */ 
/*    */ import com.google.common.collect.Lists;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import it.unimi.dsi.fastutil.ints.Int2ObjectAVLTreeMap;
/*    */ import it.unimi.dsi.fastutil.ints.Int2ObjectSortedMap;
/*    */ import it.unimi.dsi.fastutil.ints.IntAVLTreeSet;
/*    */ import it.unimi.dsi.fastutil.ints.IntBidirectionalIterator;
/*    */ import it.unimi.dsi.fastutil.ints.IntSortedSet;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Iterator;
/*    */ import java.util.List;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import java.util.concurrent.Executor;
/*    */ import java.util.function.BiFunction;
/*    */ import org.apache.logging.log4j.LogManager;
/*    */ import org.apache.logging.log4j.Logger;
/*    */ 
/*    */ 
/*    */ public class DataFixerBuilder
/*    */ {
/* 23 */   private static final Logger LOGGER = LogManager.getLogger();
/*    */   
/*    */   private final int dataVersion;
/* 26 */   private final Int2ObjectSortedMap<Schema> schemas = (Int2ObjectSortedMap<Schema>)new Int2ObjectAVLTreeMap();
/* 27 */   private final List<DataFix> globalList = Lists.newArrayList();
/* 28 */   private final IntSortedSet fixerVersions = (IntSortedSet)new IntAVLTreeSet();
/*    */   
/*    */   public DataFixerBuilder(int dataVersion) {
/* 31 */     this.dataVersion = dataVersion;
/*    */   }
/*    */   
/*    */   public Schema addSchema(int version, BiFunction<Integer, Schema, Schema> factory) {
/* 35 */     return addSchema(version, 0, factory);
/*    */   }
/*    */   
/*    */   public Schema addSchema(int version, int subVersion, BiFunction<Integer, Schema, Schema> factory) {
/* 39 */     int key = DataFixUtils.makeKey(version, subVersion);
/* 40 */     Schema parent = this.schemas.isEmpty() ? null : (Schema)this.schemas.get(DataFixerUpper.getLowestSchemaSameVersion(this.schemas, key - 1));
/* 41 */     Schema schema = factory.apply(Integer.valueOf(DataFixUtils.makeKey(version, subVersion)), parent);
/* 42 */     addSchema(schema);
/* 43 */     return schema;
/*    */   }
/*    */   
/*    */   public void addSchema(Schema schema) {
/* 47 */     this.schemas.put(schema.getVersionKey(), schema);
/*    */   }
/*    */   
/*    */   public void addFixer(DataFix fix) {
/* 51 */     int version = DataFixUtils.getVersion(fix.getVersionKey());
/*    */     
/* 53 */     if (version > this.dataVersion) {
/* 54 */       LOGGER.warn("Ignored fix registered for version: {} as the DataVersion of the game is: {}", Integer.valueOf(version), Integer.valueOf(this.dataVersion));
/*    */       
/*    */       return;
/*    */     } 
/* 58 */     this.globalList.add(fix);
/* 59 */     this.fixerVersions.add(fix.getVersionKey());
/*    */   }
/*    */   
/*    */   public DataFixer build(Executor executor) {
/* 63 */     DataFixerUpper fixerUpper = new DataFixerUpper((Int2ObjectSortedMap<Schema>)new Int2ObjectAVLTreeMap(this.schemas), new ArrayList<>(this.globalList), (IntSortedSet)new IntAVLTreeSet(this.fixerVersions));
/*    */     
/* 65 */     IntBidirectionalIterator iterator = fixerUpper.fixerVersions().iterator();
/* 66 */     while (iterator.hasNext()) {
/* 67 */       int versionKey = iterator.nextInt();
/* 68 */       Schema schema = (Schema)this.schemas.get(versionKey);
/* 69 */       for (Iterator<String> iterator1 = schema.types().iterator(); iterator1.hasNext(); ) { String typeName = iterator1.next();
/* 70 */         CompletableFuture.runAsync(() -> { Type<?> dataType = schema.getType(()); TypeRewriteRule rule = fixerUpper.getRule(DataFixUtils.getVersion(versionKey), this.dataVersion); dataType.rewrite(rule, DataFixerUpper.OPTIMIZATION_RULE); }executor)
/*    */ 
/*    */ 
/*    */           
/* 74 */           .exceptionally(e -> {
/*    */               LOGGER.error("Unable to build datafixers", e);
/*    */               
/*    */               Runtime.getRuntime().exit(1);
/*    */               return null;
/*    */             }); }
/*    */     
/*    */     } 
/* 82 */     return fixerUpper;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\datafixers\DataFixerBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */