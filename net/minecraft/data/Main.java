/*    */ package net.minecraft.data;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.nio.file.Path;
/*    */ import java.nio.file.Paths;
/*    */ import java.util.Collection;
/*    */ import java.util.stream.Collectors;
/*    */ import joptsimple.AbstractOptionSpec;
/*    */ import joptsimple.ArgumentAcceptingOptionSpec;
/*    */ import joptsimple.OptionParser;
/*    */ import joptsimple.OptionSet;
/*    */ import joptsimple.OptionSpec;
/*    */ import joptsimple.OptionSpecBuilder;
/*    */ import net.minecraft.data.advancements.AdvancementProvider;
/*    */ import net.minecraft.data.info.BlockListReport;
/*    */ import net.minecraft.data.info.CommandsReport;
/*    */ import net.minecraft.data.info.RegistryDumpReport;
/*    */ import net.minecraft.data.loot.LootTableProvider;
/*    */ import net.minecraft.data.models.ModelProvider;
/*    */ import net.minecraft.data.recipes.RecipeProvider;
/*    */ import net.minecraft.data.structures.NbtToSnbt;
/*    */ import net.minecraft.data.structures.SnbtToNbt;
/*    */ import net.minecraft.data.structures.StructureUpdater;
/*    */ import net.minecraft.data.tags.BlockTagsProvider;
/*    */ import net.minecraft.data.tags.EntityTypeTagsProvider;
/*    */ import net.minecraft.data.tags.FluidTagsProvider;
/*    */ import net.minecraft.data.tags.ItemTagsProvider;
/*    */ import net.minecraft.data.worldgen.biome.BiomeReport;
/*    */ 
/*    */ public class Main {
/*    */   public static void main(String[] debug0) throws IOException {
/* 32 */     OptionParser debug1 = new OptionParser();
/* 33 */     AbstractOptionSpec abstractOptionSpec = debug1.accepts("help", "Show the help menu").forHelp();
/* 34 */     OptionSpecBuilder optionSpecBuilder1 = debug1.accepts("server", "Include server generators");
/* 35 */     OptionSpecBuilder optionSpecBuilder2 = debug1.accepts("client", "Include client generators");
/* 36 */     OptionSpecBuilder optionSpecBuilder3 = debug1.accepts("dev", "Include development tools");
/* 37 */     OptionSpecBuilder optionSpecBuilder4 = debug1.accepts("reports", "Include data reports");
/* 38 */     OptionSpecBuilder optionSpecBuilder5 = debug1.accepts("validate", "Validate inputs");
/* 39 */     OptionSpecBuilder optionSpecBuilder6 = debug1.accepts("all", "Include all generators");
/* 40 */     ArgumentAcceptingOptionSpec argumentAcceptingOptionSpec1 = debug1.accepts("output", "Output folder").withRequiredArg().defaultsTo("generated", (Object[])new String[0]);
/* 41 */     ArgumentAcceptingOptionSpec argumentAcceptingOptionSpec2 = debug1.accepts("input", "Input folder").withRequiredArg();
/* 42 */     OptionSet debug11 = debug1.parse(debug0);
/*    */     
/* 44 */     if (debug11.has((OptionSpec)abstractOptionSpec) || !debug11.hasOptions()) {
/* 45 */       debug1.printHelpOn(System.out);
/*    */       
/*    */       return;
/*    */     } 
/* 49 */     Path debug12 = Paths.get((String)argumentAcceptingOptionSpec1.value(debug11), new String[0]);
/* 50 */     boolean debug13 = debug11.has((OptionSpec)optionSpecBuilder6);
/* 51 */     boolean debug14 = (debug13 || debug11.has((OptionSpec)optionSpecBuilder2));
/* 52 */     boolean debug15 = (debug13 || debug11.has((OptionSpec)optionSpecBuilder1));
/* 53 */     boolean debug16 = (debug13 || debug11.has((OptionSpec)optionSpecBuilder3));
/* 54 */     boolean debug17 = (debug13 || debug11.has((OptionSpec)optionSpecBuilder4));
/* 55 */     boolean debug18 = (debug13 || debug11.has((OptionSpec)optionSpecBuilder5));
/* 56 */     DataGenerator debug19 = createStandardGenerator(debug12, (Collection<Path>)debug11.valuesOf((OptionSpec)argumentAcceptingOptionSpec2).stream().map(debug0 -> Paths.get(debug0, new String[0])).collect(Collectors.toList()), debug14, debug15, debug16, debug17, debug18);
/* 57 */     debug19.run();
/*    */   }
/*    */   
/*    */   public static DataGenerator createStandardGenerator(Path debug0, Collection<Path> debug1, boolean debug2, boolean debug3, boolean debug4, boolean debug5, boolean debug6) {
/* 61 */     DataGenerator debug7 = new DataGenerator(debug0, debug1);
/* 62 */     if (debug2 || debug3) {
/* 63 */       debug7.addProvider((DataProvider)(new SnbtToNbt(debug7))
/* 64 */           .addFilter((SnbtToNbt.Filter)new StructureUpdater()));
/*    */     }
/*    */ 
/*    */     
/* 68 */     if (debug2) {
/* 69 */       debug7.addProvider((DataProvider)new ModelProvider(debug7));
/*    */     }
/* 71 */     if (debug3) {
/* 72 */       debug7.addProvider((DataProvider)new FluidTagsProvider(debug7));
/* 73 */       BlockTagsProvider debug8 = new BlockTagsProvider(debug7);
/* 74 */       debug7.addProvider((DataProvider)debug8);
/* 75 */       debug7.addProvider((DataProvider)new ItemTagsProvider(debug7, debug8));
/* 76 */       debug7.addProvider((DataProvider)new EntityTypeTagsProvider(debug7));
/* 77 */       debug7.addProvider((DataProvider)new RecipeProvider(debug7));
/* 78 */       debug7.addProvider((DataProvider)new AdvancementProvider(debug7));
/* 79 */       debug7.addProvider((DataProvider)new LootTableProvider(debug7));
/*    */     } 
/* 81 */     if (debug4) {
/* 82 */       debug7.addProvider((DataProvider)new NbtToSnbt(debug7));
/*    */     }
/* 84 */     if (debug5) {
/* 85 */       debug7.addProvider((DataProvider)new BlockListReport(debug7));
/* 86 */       debug7.addProvider((DataProvider)new RegistryDumpReport(debug7));
/* 87 */       debug7.addProvider((DataProvider)new CommandsReport(debug7));
/* 88 */       debug7.addProvider((DataProvider)new BiomeReport(debug7));
/*    */     } 
/* 90 */     return debug7;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\data\Main.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */