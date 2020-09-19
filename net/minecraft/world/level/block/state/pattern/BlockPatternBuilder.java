/*    */ package net.minecraft.world.level.block.state.pattern;
/*    */ 
/*    */ import com.google.common.base.Joiner;
/*    */ import com.google.common.base.Predicates;
/*    */ import com.google.common.collect.Lists;
/*    */ import com.google.common.collect.Maps;
/*    */ import java.lang.reflect.Array;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.function.Predicate;
/*    */ import org.apache.commons.lang3.ArrayUtils;
/*    */ import org.apache.commons.lang3.StringUtils;
/*    */ 
/*    */ public class BlockPatternBuilder
/*    */ {
/* 16 */   private static final Joiner COMMA_JOINED = Joiner.on(",");
/*    */   
/* 18 */   private final List<String[]> pattern = Lists.newArrayList();
/* 19 */   private final Map<Character, Predicate<BlockInWorld>> lookup = Maps.newHashMap();
/*    */   private int height;
/*    */   private int width;
/*    */   
/*    */   private BlockPatternBuilder() {
/* 24 */     this.lookup.put(Character.valueOf(' '), Predicates.alwaysTrue());
/*    */   }
/*    */   
/*    */   public BlockPatternBuilder aisle(String... debug1) {
/* 28 */     if (ArrayUtils.isEmpty((Object[])debug1) || StringUtils.isEmpty(debug1[0])) {
/* 29 */       throw new IllegalArgumentException("Empty pattern for aisle");
/*    */     }
/*    */     
/* 32 */     if (this.pattern.isEmpty()) {
/* 33 */       this.height = debug1.length;
/* 34 */       this.width = debug1[0].length();
/*    */     } 
/*    */     
/* 37 */     if (debug1.length != this.height) {
/* 38 */       throw new IllegalArgumentException("Expected aisle with height of " + this.height + ", but was given one with a height of " + debug1.length + ")");
/*    */     }
/*    */     
/* 41 */     for (String debug5 : debug1) {
/* 42 */       if (debug5.length() != this.width) {
/* 43 */         throw new IllegalArgumentException("Not all rows in the given aisle are the correct width (expected " + this.width + ", found one with " + debug5.length() + ")");
/*    */       }
/* 45 */       for (char debug9 : debug5.toCharArray()) {
/* 46 */         if (!this.lookup.containsKey(Character.valueOf(debug9))) {
/* 47 */           this.lookup.put(Character.valueOf(debug9), null);
/*    */         }
/*    */       } 
/*    */     } 
/*    */     
/* 52 */     this.pattern.add(debug1);
/*    */     
/* 54 */     return this;
/*    */   }
/*    */   
/*    */   public static BlockPatternBuilder start() {
/* 58 */     return new BlockPatternBuilder();
/*    */   }
/*    */   
/*    */   public BlockPatternBuilder where(char debug1, Predicate<BlockInWorld> debug2) {
/* 62 */     this.lookup.put(Character.valueOf(debug1), debug2);
/*    */     
/* 64 */     return this;
/*    */   }
/*    */   
/*    */   public BlockPattern build() {
/* 68 */     return new BlockPattern(createPattern());
/*    */   }
/*    */ 
/*    */   
/*    */   private Predicate<BlockInWorld>[][][] createPattern() {
/* 73 */     ensureAllCharactersMatched();
/*    */     
/* 75 */     Predicate[][][] arrayOfPredicate = (Predicate[][][])Array.newInstance(Predicate.class, new int[] { this.pattern.size(), this.height, this.width });
/*    */     
/* 77 */     for (int debug2 = 0; debug2 < this.pattern.size(); debug2++) {
/* 78 */       for (int debug3 = 0; debug3 < this.height; debug3++) {
/* 79 */         for (int debug4 = 0; debug4 < this.width; debug4++) {
/* 80 */           arrayOfPredicate[debug2][debug3][debug4] = this.lookup.get(Character.valueOf(((String[])this.pattern.get(debug2))[debug3].charAt(debug4)));
/*    */         }
/*    */       } 
/*    */     } 
/*    */     
/* 85 */     return (Predicate<BlockInWorld>[][][])arrayOfPredicate;
/*    */   }
/*    */   
/*    */   private void ensureAllCharactersMatched() {
/* 89 */     List<Character> debug1 = Lists.newArrayList();
/*    */     
/* 91 */     for (Map.Entry<Character, Predicate<BlockInWorld>> debug3 : this.lookup.entrySet()) {
/* 92 */       if (debug3.getValue() == null) {
/* 93 */         debug1.add(debug3.getKey());
/*    */       }
/*    */     } 
/*    */     
/* 97 */     if (!debug1.isEmpty())
/* 98 */       throw new IllegalStateException("Predicates for character(s) " + COMMA_JOINED.join(debug1) + " are missing"); 
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\state\pattern\BlockPatternBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */