/*     */ package net.minecraft.util.datafix.fixes;
/*     */ import com.mojang.datafixers.DSL;
/*     */ import com.mojang.datafixers.DataFix;
/*     */ import com.mojang.datafixers.DataFixUtils;
/*     */ import com.mojang.datafixers.TypeRewriteRule;
/*     */ import com.mojang.datafixers.Typed;
/*     */ import com.mojang.datafixers.schemas.Schema;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
/*     */ import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
/*     */ import java.util.Map;
/*     */ import java.util.stream.Collectors;
/*     */ 
/*     */ public class OptionsKeyLwjgl3Fix extends DataFix {
/*     */   public OptionsKeyLwjgl3Fix(Schema debug1, boolean debug2) {
/*  17 */     super(debug1, debug2);
/*     */   }
/*     */   private static final Int2ObjectMap<String> MAP;
/*     */   static {
/*  21 */     MAP = (Int2ObjectMap<String>)DataFixUtils.make(new Int2ObjectOpenHashMap(), debug0 -> {
/*     */           debug0.put(0, "key.unknown");
/*     */           debug0.put(11, "key.0");
/*     */           debug0.put(2, "key.1");
/*     */           debug0.put(3, "key.2");
/*     */           debug0.put(4, "key.3");
/*     */           debug0.put(5, "key.4");
/*     */           debug0.put(6, "key.5");
/*     */           debug0.put(7, "key.6");
/*     */           debug0.put(8, "key.7");
/*     */           debug0.put(9, "key.8");
/*     */           debug0.put(10, "key.9");
/*     */           debug0.put(30, "key.a");
/*     */           debug0.put(40, "key.apostrophe");
/*     */           debug0.put(48, "key.b");
/*     */           debug0.put(43, "key.backslash");
/*     */           debug0.put(14, "key.backspace");
/*     */           debug0.put(46, "key.c");
/*     */           debug0.put(58, "key.caps.lock");
/*     */           debug0.put(51, "key.comma");
/*     */           debug0.put(32, "key.d");
/*     */           debug0.put(211, "key.delete");
/*     */           debug0.put(208, "key.down");
/*     */           debug0.put(18, "key.e");
/*     */           debug0.put(207, "key.end");
/*     */           debug0.put(28, "key.enter");
/*     */           debug0.put(13, "key.equal");
/*     */           debug0.put(1, "key.escape");
/*     */           debug0.put(33, "key.f");
/*     */           debug0.put(59, "key.f1");
/*     */           debug0.put(68, "key.f10");
/*     */           debug0.put(87, "key.f11");
/*     */           debug0.put(88, "key.f12");
/*     */           debug0.put(100, "key.f13");
/*     */           debug0.put(101, "key.f14");
/*     */           debug0.put(102, "key.f15");
/*     */           debug0.put(103, "key.f16");
/*     */           debug0.put(104, "key.f17");
/*     */           debug0.put(105, "key.f18");
/*     */           debug0.put(113, "key.f19");
/*     */           debug0.put(60, "key.f2");
/*     */           debug0.put(61, "key.f3");
/*     */           debug0.put(62, "key.f4");
/*     */           debug0.put(63, "key.f5");
/*     */           debug0.put(64, "key.f6");
/*     */           debug0.put(65, "key.f7");
/*     */           debug0.put(66, "key.f8");
/*     */           debug0.put(67, "key.f9");
/*     */           debug0.put(34, "key.g");
/*     */           debug0.put(41, "key.grave.accent");
/*     */           debug0.put(35, "key.h");
/*     */           debug0.put(199, "key.home");
/*     */           debug0.put(23, "key.i");
/*     */           debug0.put(210, "key.insert");
/*     */           debug0.put(36, "key.j");
/*     */           debug0.put(37, "key.k");
/*     */           debug0.put(82, "key.keypad.0");
/*     */           debug0.put(79, "key.keypad.1");
/*     */           debug0.put(80, "key.keypad.2");
/*     */           debug0.put(81, "key.keypad.3");
/*     */           debug0.put(75, "key.keypad.4");
/*     */           debug0.put(76, "key.keypad.5");
/*     */           debug0.put(77, "key.keypad.6");
/*     */           debug0.put(71, "key.keypad.7");
/*     */           debug0.put(72, "key.keypad.8");
/*     */           debug0.put(73, "key.keypad.9");
/*     */           debug0.put(78, "key.keypad.add");
/*     */           debug0.put(83, "key.keypad.decimal");
/*     */           debug0.put(181, "key.keypad.divide");
/*     */           debug0.put(156, "key.keypad.enter");
/*     */           debug0.put(141, "key.keypad.equal");
/*     */           debug0.put(55, "key.keypad.multiply");
/*     */           debug0.put(74, "key.keypad.subtract");
/*     */           debug0.put(38, "key.l");
/*     */           debug0.put(203, "key.left");
/*     */           debug0.put(56, "key.left.alt");
/*     */           debug0.put(26, "key.left.bracket");
/*     */           debug0.put(29, "key.left.control");
/*     */           debug0.put(42, "key.left.shift");
/*     */           debug0.put(219, "key.left.win");
/*     */           debug0.put(50, "key.m");
/*     */           debug0.put(12, "key.minus");
/*     */           debug0.put(49, "key.n");
/*     */           debug0.put(69, "key.num.lock");
/*     */           debug0.put(24, "key.o");
/*     */           debug0.put(25, "key.p");
/*     */           debug0.put(209, "key.page.down");
/*     */           debug0.put(201, "key.page.up");
/*     */           debug0.put(197, "key.pause");
/*     */           debug0.put(52, "key.period");
/*     */           debug0.put(183, "key.print.screen");
/*     */           debug0.put(16, "key.q");
/*     */           debug0.put(19, "key.r");
/*     */           debug0.put(205, "key.right");
/*     */           debug0.put(184, "key.right.alt");
/*     */           debug0.put(27, "key.right.bracket");
/*     */           debug0.put(157, "key.right.control");
/*     */           debug0.put(54, "key.right.shift");
/*     */           debug0.put(220, "key.right.win");
/*     */           debug0.put(31, "key.s");
/*     */           debug0.put(70, "key.scroll.lock");
/*     */           debug0.put(39, "key.semicolon");
/*     */           debug0.put(53, "key.slash");
/*     */           debug0.put(57, "key.space");
/*     */           debug0.put(20, "key.t");
/*     */           debug0.put(15, "key.tab");
/*     */           debug0.put(22, "key.u");
/*     */           debug0.put(200, "key.up");
/*     */           debug0.put(47, "key.v");
/*     */           debug0.put(17, "key.w");
/*     */           debug0.put(45, "key.x");
/*     */           debug0.put(21, "key.y");
/*     */           debug0.put(44, "key.z");
/*     */         });
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
/*     */   public TypeRewriteRule makeRule() {
/* 159 */     return fixTypeEverywhereTyped("OptionsKeyLwjgl3Fix", getInputSchema().getType(References.OPTIONS), debug0 -> debug0.update(DSL.remainderFinder(), ()));
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\datafix\fixes\OptionsKeyLwjgl3Fix.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */