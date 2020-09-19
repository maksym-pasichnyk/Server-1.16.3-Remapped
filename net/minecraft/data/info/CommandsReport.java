/*    */ package net.minecraft.data.info;
/*    */ import com.google.gson.Gson;
/*    */ import com.google.gson.GsonBuilder;
/*    */ import com.google.gson.JsonElement;
/*    */ import com.mojang.brigadier.CommandDispatcher;
/*    */ import com.mojang.brigadier.tree.CommandNode;
/*    */ import java.io.IOException;
/*    */ import java.nio.file.Path;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.commands.synchronization.ArgumentTypes;
/*    */ import net.minecraft.data.DataGenerator;
/*    */ import net.minecraft.data.DataProvider;
/*    */ import net.minecraft.data.HashCache;
/*    */ 
/*    */ public class CommandsReport implements DataProvider {
/* 17 */   private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();
/*    */   private final DataGenerator generator;
/*    */   
/*    */   public CommandsReport(DataGenerator debug1) {
/* 21 */     this.generator = debug1;
/*    */   }
/*    */ 
/*    */   
/*    */   public void run(HashCache debug1) throws IOException {
/* 26 */     Path debug2 = this.generator.getOutputFolder().resolve("reports/commands.json");
/* 27 */     CommandDispatcher<CommandSourceStack> debug3 = (new Commands(Commands.CommandSelection.ALL)).getDispatcher();
/* 28 */     DataProvider.save(GSON, debug1, (JsonElement)ArgumentTypes.serializeNodeToJson(debug3, (CommandNode)debug3.getRoot()), debug2);
/*    */   }
/*    */ 
/*    */   
/*    */   public String getName() {
/* 33 */     return "Command Syntax";
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\data\info\CommandsReport.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */