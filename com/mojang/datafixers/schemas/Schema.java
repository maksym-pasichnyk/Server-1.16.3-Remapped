/*     */ package com.mojang.datafixers.schemas;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.mojang.datafixers.DSL;
/*     */ import com.mojang.datafixers.DataFixUtils;
/*     */ import com.mojang.datafixers.types.Type;
/*     */ import com.mojang.datafixers.types.families.RecursiveTypeFamily;
/*     */ import com.mojang.datafixers.types.families.TypeFamily;
/*     */ import com.mojang.datafixers.types.templates.TaggedChoice;
/*     */ import com.mojang.datafixers.types.templates.TypeTemplate;
/*     */ import it.unimi.dsi.fastutil.objects.Object2IntMap;
/*     */ import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectIterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Supplier;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Schema
/*     */ {
/*  25 */   protected final Object2IntMap<String> RECURSIVE_TYPES = (Object2IntMap<String>)new Object2IntOpenHashMap();
/*  26 */   private final Map<String, Supplier<TypeTemplate>> TYPE_TEMPLATES = Maps.newHashMap();
/*     */   private final Map<String, Type<?>> TYPES;
/*     */   private final int versionKey;
/*     */   private final String name;
/*     */   protected final Schema parent;
/*     */   
/*     */   public Schema(int versionKey, Schema parent) {
/*  33 */     this.versionKey = versionKey;
/*  34 */     int subVersion = DataFixUtils.getSubVersion(versionKey);
/*  35 */     this.name = "V" + DataFixUtils.getVersion(versionKey) + ((subVersion == 0) ? "" : ("." + subVersion));
/*  36 */     this.parent = parent;
/*  37 */     registerTypes(this, registerEntities(this), registerBlockEntities(this));
/*  38 */     this.TYPES = buildTypes();
/*     */   }
/*     */   
/*     */   protected Map<String, Type<?>> buildTypes() {
/*  42 */     Map<String, Type<?>> types = Maps.newHashMap();
/*     */     
/*  44 */     List<TypeTemplate> templates = Lists.newArrayList();
/*     */     
/*  46 */     for (ObjectIterator<Object2IntMap.Entry<String>> objectIterator = this.RECURSIVE_TYPES.object2IntEntrySet().iterator(); objectIterator.hasNext(); ) { Object2IntMap.Entry<String> entry = objectIterator.next();
/*  47 */       templates.add(DSL.check((String)entry.getKey(), entry.getIntValue(), getTemplate((String)entry.getKey()))); }
/*     */ 
/*     */     
/*  50 */     TypeTemplate choice = templates.stream().reduce(DSL::or).get();
/*  51 */     RecursiveTypeFamily recursiveTypeFamily = new RecursiveTypeFamily(this.name, choice);
/*     */     
/*  53 */     for (String name : this.TYPE_TEMPLATES.keySet()) {
/*     */       Type<?> type;
/*  55 */       int recurseId = ((Integer)this.RECURSIVE_TYPES.getOrDefault(name, Integer.valueOf(-1))).intValue();
/*  56 */       if (recurseId != -1) {
/*  57 */         type = recursiveTypeFamily.apply(recurseId);
/*     */       } else {
/*  59 */         type = getTemplate(name).apply((TypeFamily)recursiveTypeFamily).apply(-1);
/*     */       } 
/*  61 */       types.put(name, type);
/*     */     } 
/*  63 */     return types;
/*     */   }
/*     */   
/*     */   public Set<String> types() {
/*  67 */     return this.TYPES.keySet();
/*     */   }
/*     */   
/*     */   public Type<?> getTypeRaw(DSL.TypeReference type) {
/*  71 */     String name = type.typeName();
/*  72 */     return this.TYPES.computeIfAbsent(name, key -> {
/*     */           throw new IllegalArgumentException("Unknown type: " + name);
/*     */         });
/*     */   }
/*     */   
/*     */   public Type<?> getType(DSL.TypeReference type) {
/*  78 */     String name = type.typeName();
/*  79 */     Type<?> type1 = this.TYPES.computeIfAbsent(name, key -> {
/*     */           throw new IllegalArgumentException("Unknown type: " + name);
/*     */         });
/*  82 */     if (type1 instanceof com.mojang.datafixers.types.templates.RecursivePoint.RecursivePointType) {
/*  83 */       return (Type)type1.findCheckedType(-1).orElseThrow(() -> new IllegalStateException("Could not find choice type in the recursive type"));
/*     */     }
/*  85 */     return type1;
/*     */   }
/*     */   
/*     */   public TypeTemplate resolveTemplate(String name) {
/*  89 */     return ((Supplier<TypeTemplate>)this.TYPE_TEMPLATES.getOrDefault(name, () -> {
/*     */           throw new IllegalArgumentException("Unknown type: " + name);
/*  91 */         })).get();
/*     */   }
/*     */   
/*     */   public TypeTemplate id(String name) {
/*  95 */     int id = ((Integer)this.RECURSIVE_TYPES.getOrDefault(name, Integer.valueOf(-1))).intValue();
/*  96 */     if (id != -1) {
/*  97 */       return DSL.id(id);
/*     */     }
/*  99 */     return getTemplate(name);
/*     */   }
/*     */   
/*     */   protected TypeTemplate getTemplate(String name) {
/* 103 */     return DSL.named(name, resolveTemplate(name));
/*     */   }
/*     */   
/*     */   public Type<?> getChoiceType(DSL.TypeReference type, String choiceName) {
/* 107 */     TaggedChoice.TaggedChoiceType<?> choiceType = findChoiceType(type);
/* 108 */     if (!choiceType.types().containsKey(choiceName)) {
/* 109 */       throw new IllegalArgumentException("Data fixer not registered for: " + choiceName + " in " + type.typeName());
/*     */     }
/* 111 */     return (Type)choiceType.types().get(choiceName);
/*     */   }
/*     */   
/*     */   public TaggedChoice.TaggedChoiceType<?> findChoiceType(DSL.TypeReference type) {
/* 115 */     return (TaggedChoice.TaggedChoiceType)getType(type).findChoiceType("id", -1).orElseThrow(() -> new IllegalArgumentException("Not a choice type"));
/*     */   }
/*     */   
/*     */   public void registerTypes(Schema schema, Map<String, Supplier<TypeTemplate>> entityTypes, Map<String, Supplier<TypeTemplate>> blockEntityTypes) {
/* 119 */     this.parent.registerTypes(schema, entityTypes, blockEntityTypes);
/*     */   }
/*     */   
/*     */   public Map<String, Supplier<TypeTemplate>> registerEntities(Schema schema) {
/* 123 */     return this.parent.registerEntities(schema);
/*     */   }
/*     */   
/*     */   public Map<String, Supplier<TypeTemplate>> registerBlockEntities(Schema schema) {
/* 127 */     return this.parent.registerBlockEntities(schema);
/*     */   }
/*     */   
/*     */   public void registerSimple(Map<String, Supplier<TypeTemplate>> map, String name) {
/* 131 */     register(map, name, DSL::remainder);
/*     */   }
/*     */   
/*     */   public void register(Map<String, Supplier<TypeTemplate>> map, String name, Function<String, TypeTemplate> template) {
/* 135 */     register(map, name, () -> (TypeTemplate)template.apply(name));
/*     */   }
/*     */   
/*     */   public void register(Map<String, Supplier<TypeTemplate>> map, String name, Supplier<TypeTemplate> template) {
/* 139 */     map.put(name, template);
/*     */   }
/*     */   
/*     */   public void registerType(boolean recursive, DSL.TypeReference type, Supplier<TypeTemplate> template) {
/* 143 */     this.TYPE_TEMPLATES.put(type.typeName(), template);
/*     */     
/* 145 */     if (recursive && !this.RECURSIVE_TYPES.containsKey(type.typeName())) {
/* 146 */       this.RECURSIVE_TYPES.put(type.typeName(), this.RECURSIVE_TYPES.size());
/*     */     }
/*     */   }
/*     */   
/*     */   public int getVersionKey() {
/* 151 */     return this.versionKey;
/*     */   }
/*     */   
/*     */   public Schema getParent() {
/* 155 */     return this.parent;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\datafixers\schemas\Schema.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */