/*     */ package net.minecraft.util.datafix.fixes;
/*     */ 
/*     */ import com.mojang.datafixers.DSL;
/*     */ import com.mojang.datafixers.DataFix;
/*     */ import com.mojang.datafixers.OpticFinder;
/*     */ import com.mojang.datafixers.TypeRewriteRule;
/*     */ import com.mojang.datafixers.Typed;
/*     */ import com.mojang.datafixers.schemas.Schema;
/*     */ import com.mojang.datafixers.types.Type;
/*     */ import com.mojang.datafixers.types.templates.List;
/*     */ import com.mojang.datafixers.types.templates.TaggedChoice;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
/*     */ import it.unimi.dsi.fastutil.ints.IntSet;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import javax.annotation.Nullable;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ 
/*     */ public class TrappedChestBlockEntityFix
/*     */   extends DataFix
/*     */ {
/*  27 */   private static final Logger LOGGER = LogManager.getLogger();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TrappedChestBlockEntityFix(Schema debug1, boolean debug2) {
/*  33 */     super(debug1, debug2);
/*     */   }
/*     */ 
/*     */   
/*     */   public TypeRewriteRule makeRule() {
/*  38 */     Type<?> debug1 = getOutputSchema().getType(References.CHUNK);
/*  39 */     Type<?> debug2 = debug1.findFieldType("Level");
/*  40 */     Type<?> debug3 = debug2.findFieldType("TileEntities");
/*  41 */     if (!(debug3 instanceof List.ListType)) {
/*  42 */       throw new IllegalStateException("Tile entity type is not a list type.");
/*     */     }
/*  44 */     List.ListType<?> debug4 = (List.ListType)debug3;
/*     */     
/*  46 */     OpticFinder<? extends List<?>> debug5 = DSL.fieldFinder("TileEntities", (Type)debug4);
/*     */     
/*  48 */     Type<?> debug6 = getInputSchema().getType(References.CHUNK);
/*     */     
/*  50 */     OpticFinder<?> debug7 = debug6.findField("Level");
/*  51 */     OpticFinder<?> debug8 = debug7.type().findField("Sections");
/*  52 */     Type<?> debug9 = debug8.type();
/*  53 */     if (!(debug9 instanceof List.ListType)) {
/*  54 */       throw new IllegalStateException("Expecting sections to be a list.");
/*     */     }
/*  56 */     Type<?> debug10 = ((List.ListType)debug9).getElement();
/*  57 */     OpticFinder<?> debug11 = DSL.typeFinder(debug10);
/*     */     
/*  59 */     return TypeRewriteRule.seq((new AddNewChoices(
/*  60 */           getOutputSchema(), "AddTrappedChestFix", References.BLOCK_ENTITY)).makeRule(), 
/*  61 */         fixTypeEverywhereTyped("Trapped Chest fix", debug6, debug5 -> debug5.updateTyped(debug1, ())));
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
/*     */   public static final class TrappedChestSection
/*     */     extends LeavesFix.Section
/*     */   {
/*     */     @Nullable
/*     */     private IntSet chestIds;
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
/*     */ 
/*     */     
/*     */     public TrappedChestSection(Typed<?> debug1, Schema debug2) {
/* 117 */       super(debug1, debug2);
/*     */     }
/*     */ 
/*     */     
/*     */     protected boolean skippable() {
/* 122 */       this.chestIds = (IntSet)new IntOpenHashSet();
/*     */       
/* 124 */       for (int debug1 = 0; debug1 < this.palette.size(); debug1++) {
/* 125 */         Dynamic<?> debug2 = this.palette.get(debug1);
/* 126 */         String debug3 = debug2.get("Name").asString("");
/* 127 */         if (Objects.equals(debug3, "minecraft:trapped_chest")) {
/* 128 */           this.chestIds.add(debug1);
/*     */         }
/*     */       } 
/*     */       
/* 132 */       return this.chestIds.isEmpty();
/*     */     }
/*     */     
/*     */     public boolean isTrappedChest(int debug1) {
/* 136 */       return this.chestIds.contains(debug1);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\datafix\fixes\TrappedChestBlockEntityFix.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */