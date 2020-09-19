/*     */ package net.minecraft.gametest.framework;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.common.collect.Sets;
/*     */ import java.util.Collection;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.stream.Collectors;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class GameTestRegistry
/*     */ {
/*  24 */   private static final Collection<TestFunction> testFunctions = Lists.newArrayList();
/*  25 */   private static final Set<String> testClassNames = Sets.newHashSet();
/*  26 */   private static final Map<String, Consumer<ServerLevel>> beforeBatchFunctions = Maps.newHashMap();
/*  27 */   private static final Collection<TestFunction> lastFailedTests = Sets.newHashSet();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Collection<TestFunction> getTestFunctionsForClassName(String debug0) {
/*  59 */     return (Collection<TestFunction>)testFunctions.stream()
/*  60 */       .filter(debug1 -> isTestFunctionPartOfClass(debug1, debug0))
/*  61 */       .collect(Collectors.toList());
/*     */   }
/*     */   
/*     */   public static Collection<TestFunction> getAllTestFunctions() {
/*  65 */     return testFunctions;
/*     */   }
/*     */   
/*     */   public static Collection<String> getAllTestClassNames() {
/*  69 */     return testClassNames;
/*     */   }
/*     */   
/*     */   public static boolean isTestClass(String debug0) {
/*  73 */     return testClassNames.contains(debug0);
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public static Consumer<ServerLevel> getBeforeBatchFunction(String debug0) {
/*  78 */     return beforeBatchFunctions.get(debug0);
/*     */   }
/*     */   
/*     */   public static Optional<TestFunction> findTestFunction(String debug0) {
/*  82 */     return getAllTestFunctions().stream()
/*  83 */       .filter(debug1 -> debug1.getTestName().equalsIgnoreCase(debug0))
/*  84 */       .findFirst();
/*     */   }
/*     */ 
/*     */   
/*     */   public static TestFunction getTestFunction(String debug0) {
/*  89 */     Optional<TestFunction> debug1 = findTestFunction(debug0);
/*  90 */     if (!debug1.isPresent()) {
/*  91 */       throw new IllegalArgumentException("Can't find the test function for " + debug0);
/*     */     }
/*  93 */     return debug1.get();
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
/*     */   private static boolean isTestFunctionPartOfClass(TestFunction debug0, String debug1) {
/* 138 */     return debug0.getTestName().toLowerCase().startsWith(debug1.toLowerCase() + ".");
/*     */   }
/*     */   
/*     */   public static Collection<TestFunction> getLastFailedTests() {
/* 142 */     return lastFailedTests;
/*     */   }
/*     */   
/*     */   public static void rememberFailedTest(TestFunction debug0) {
/* 146 */     lastFailedTests.add(debug0);
/*     */   }
/*     */   
/*     */   public static void forgetFailedTests() {
/* 150 */     lastFailedTests.clear();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\gametest\framework\GameTestRegistry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */