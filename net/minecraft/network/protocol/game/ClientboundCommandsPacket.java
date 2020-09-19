/*     */ package net.minecraft.network.protocol.game;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Queues;
/*     */ import com.mojang.brigadier.arguments.ArgumentType;
/*     */ import com.mojang.brigadier.builder.ArgumentBuilder;
/*     */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*     */ import com.mojang.brigadier.builder.RequiredArgumentBuilder;
/*     */ import com.mojang.brigadier.context.CommandContext;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.tree.ArgumentCommandNode;
/*     */ import com.mojang.brigadier.tree.CommandNode;
/*     */ import com.mojang.brigadier.tree.LiteralCommandNode;
/*     */ import com.mojang.brigadier.tree.RootCommandNode;
/*     */ import it.unimi.dsi.fastutil.objects.Object2IntMap;
/*     */ import it.unimi.dsi.fastutil.objects.Object2IntMaps;
/*     */ import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectIterator;
/*     */ import java.io.IOException;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Queue;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.commands.SharedSuggestionProvider;
/*     */ import net.minecraft.commands.synchronization.ArgumentTypes;
/*     */ import net.minecraft.commands.synchronization.SuggestionProviders;
/*     */ import net.minecraft.network.FriendlyByteBuf;
/*     */ import net.minecraft.network.PacketListener;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ClientboundCommandsPacket
/*     */   implements Packet<ClientGamePacketListener>
/*     */ {
/*     */   private RootCommandNode<SharedSuggestionProvider> root;
/*     */   
/*     */   public ClientboundCommandsPacket() {}
/*     */   
/*     */   public ClientboundCommandsPacket(RootCommandNode<SharedSuggestionProvider> debug1) {
/*  46 */     this.root = debug1;
/*     */   }
/*     */ 
/*     */   
/*     */   public void read(FriendlyByteBuf debug1) throws IOException {
/*  51 */     Entry[] debug2 = new Entry[debug1.readVarInt()];
/*  52 */     for (int debug3 = 0; debug3 < debug2.length; debug3++) {
/*  53 */       debug2[debug3] = readNode(debug1);
/*     */     }
/*     */     
/*  56 */     resolveEntries(debug2);
/*  57 */     this.root = (RootCommandNode<SharedSuggestionProvider>)(debug2[debug1.readVarInt()]).node;
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(FriendlyByteBuf debug1) throws IOException {
/*  62 */     Object2IntMap<CommandNode<SharedSuggestionProvider>> debug2 = enumerateNodes(this.root);
/*  63 */     CommandNode[] arrayOfCommandNode = (CommandNode[])getNodesInIdOrder(debug2);
/*     */     
/*  65 */     debug1.writeVarInt(arrayOfCommandNode.length);
/*  66 */     for (CommandNode<SharedSuggestionProvider> debug7 : arrayOfCommandNode) {
/*  67 */       writeNode(debug1, debug7, (Map)debug2);
/*     */     }
/*  69 */     debug1.writeVarInt(debug2.get(this.root).intValue());
/*     */   }
/*     */   
/*     */   private static void resolveEntries(Entry[] debug0) {
/*  73 */     List<Entry> debug1 = Lists.newArrayList((Object[])debug0);
/*  74 */     while (!debug1.isEmpty()) {
/*  75 */       boolean debug2 = debug1.removeIf(debug1 -> debug1.build(debug0));
/*  76 */       if (!debug2) {
/*  77 */         throw new IllegalStateException("Server sent an impossible command tree");
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private static Object2IntMap<CommandNode<SharedSuggestionProvider>> enumerateNodes(RootCommandNode<SharedSuggestionProvider> debug0) {
/*  83 */     Object2IntOpenHashMap object2IntOpenHashMap = new Object2IntOpenHashMap();
/*  84 */     Queue<CommandNode<SharedSuggestionProvider>> debug2 = Queues.newArrayDeque();
/*  85 */     debug2.add(debug0);
/*     */     
/*     */     CommandNode<SharedSuggestionProvider> debug3;
/*  88 */     while ((debug3 = debug2.poll()) != null) {
/*  89 */       if (object2IntOpenHashMap.containsKey(debug3)) {
/*     */         continue;
/*     */       }
/*  92 */       int debug4 = object2IntOpenHashMap.size();
/*  93 */       object2IntOpenHashMap.put(debug3, debug4);
/*  94 */       debug2.addAll(debug3.getChildren());
/*  95 */       if (debug3.getRedirect() != null) {
/*  96 */         debug2.add(debug3.getRedirect());
/*     */       }
/*     */     } 
/*  99 */     return (Object2IntMap<CommandNode<SharedSuggestionProvider>>)object2IntOpenHashMap;
/*     */   }
/*     */ 
/*     */   
/*     */   private static CommandNode<SharedSuggestionProvider>[] getNodesInIdOrder(Object2IntMap<CommandNode<SharedSuggestionProvider>> debug0) {
/* 104 */     CommandNode[] arrayOfCommandNode = new CommandNode[debug0.size()];
/* 105 */     for (ObjectIterator<Object2IntMap.Entry<CommandNode<SharedSuggestionProvider>>> objectIterator = Object2IntMaps.fastIterable(debug0).iterator(); objectIterator.hasNext(); ) { Object2IntMap.Entry<CommandNode<SharedSuggestionProvider>> debug3 = objectIterator.next();
/* 106 */       arrayOfCommandNode[debug3.getIntValue()] = (CommandNode)debug3.getKey(); }
/*     */     
/* 108 */     return (CommandNode<SharedSuggestionProvider>[])arrayOfCommandNode;
/*     */   }
/*     */   
/*     */   private static Entry readNode(FriendlyByteBuf debug0) {
/* 112 */     byte debug1 = debug0.readByte();
/* 113 */     int[] debug2 = debug0.readVarIntArray();
/* 114 */     int debug3 = ((debug1 & 0x8) != 0) ? debug0.readVarInt() : 0;
/* 115 */     ArgumentBuilder<SharedSuggestionProvider, ?> debug4 = createBuilder(debug0, debug1);
/* 116 */     return new Entry(debug4, debug1, debug3, debug2);
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private static ArgumentBuilder<SharedSuggestionProvider, ?> createBuilder(FriendlyByteBuf debug0, byte debug1) {
/* 121 */     int debug2 = debug1 & 0x3;
/* 122 */     if (debug2 == 2) {
/* 123 */       String debug3 = debug0.readUtf(32767);
/* 124 */       ArgumentType<?> debug4 = ArgumentTypes.deserialize(debug0);
/* 125 */       if (debug4 == null) {
/* 126 */         return null;
/*     */       }
/* 128 */       RequiredArgumentBuilder<SharedSuggestionProvider, ?> debug5 = RequiredArgumentBuilder.argument(debug3, debug4);
/* 129 */       if ((debug1 & 0x10) != 0) {
/* 130 */         debug5.suggests(SuggestionProviders.getProvider(debug0.readResourceLocation()));
/*     */       }
/* 132 */       return (ArgumentBuilder<SharedSuggestionProvider, ?>)debug5;
/*     */     } 
/* 134 */     if (debug2 == 1) {
/* 135 */       return (ArgumentBuilder<SharedSuggestionProvider, ?>)LiteralArgumentBuilder.literal(debug0.readUtf(32767));
/*     */     }
/* 137 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   private static void writeNode(FriendlyByteBuf debug0, CommandNode<SharedSuggestionProvider> debug1, Map<CommandNode<SharedSuggestionProvider>, Integer> debug2) {
/* 142 */     byte debug3 = 0;
/* 143 */     if (debug1.getRedirect() != null) {
/* 144 */       debug3 = (byte)(debug3 | 0x8);
/*     */     }
/* 146 */     if (debug1.getCommand() != null) {
/* 147 */       debug3 = (byte)(debug3 | 0x4);
/*     */     }
/*     */     
/* 150 */     if (debug1 instanceof RootCommandNode) {
/* 151 */       debug3 = (byte)(debug3 | 0x0);
/* 152 */     } else if (debug1 instanceof ArgumentCommandNode) {
/* 153 */       debug3 = (byte)(debug3 | 0x2);
/* 154 */       if (((ArgumentCommandNode)debug1).getCustomSuggestions() != null) {
/* 155 */         debug3 = (byte)(debug3 | 0x10);
/*     */       }
/* 157 */     } else if (debug1 instanceof LiteralCommandNode) {
/* 158 */       debug3 = (byte)(debug3 | 0x1);
/*     */     } else {
/* 160 */       throw new UnsupportedOperationException("Unknown node type " + debug1);
/*     */     } 
/*     */     
/* 163 */     debug0.writeByte(debug3);
/*     */     
/* 165 */     debug0.writeVarInt(debug1.getChildren().size());
/* 166 */     for (CommandNode<SharedSuggestionProvider> debug5 : (Iterable<CommandNode<SharedSuggestionProvider>>)debug1.getChildren()) {
/* 167 */       debug0.writeVarInt(((Integer)debug2.get(debug5)).intValue());
/*     */     }
/*     */     
/* 170 */     if (debug1.getRedirect() != null) {
/* 171 */       debug0.writeVarInt(((Integer)debug2.get(debug1.getRedirect())).intValue());
/*     */     }
/*     */     
/* 174 */     if (debug1 instanceof ArgumentCommandNode) {
/* 175 */       ArgumentCommandNode<SharedSuggestionProvider, ?> debug4 = (ArgumentCommandNode)debug1;
/* 176 */       debug0.writeUtf(debug4.getName());
/* 177 */       ArgumentTypes.serialize(debug0, debug4.getType());
/* 178 */       if (debug4.getCustomSuggestions() != null) {
/* 179 */         debug0.writeResourceLocation(SuggestionProviders.getName(debug4.getCustomSuggestions()));
/*     */       }
/* 181 */     } else if (debug1 instanceof LiteralCommandNode) {
/* 182 */       debug0.writeUtf(((LiteralCommandNode)debug1).getLiteral());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void handle(ClientGamePacketListener debug1) {
/* 188 */     debug1.handleCommands(this);
/*     */   }
/*     */ 
/*     */   
/*     */   static class Entry
/*     */   {
/*     */     @Nullable
/*     */     private final ArgumentBuilder<SharedSuggestionProvider, ?> builder;
/*     */     
/*     */     private final byte flags;
/*     */     
/*     */     private final int redirect;
/*     */     private final int[] children;
/*     */     @Nullable
/*     */     private CommandNode<SharedSuggestionProvider> node;
/*     */     
/*     */     private Entry(@Nullable ArgumentBuilder<SharedSuggestionProvider, ?> debug1, byte debug2, int debug3, int[] debug4) {
/* 205 */       this.builder = debug1;
/* 206 */       this.flags = debug2;
/* 207 */       this.redirect = debug3;
/* 208 */       this.children = debug4;
/*     */     }
/*     */     
/*     */     public boolean build(Entry[] debug1) {
/* 212 */       if (this.node == null) {
/* 213 */         if (this.builder == null) {
/* 214 */           this.node = (CommandNode<SharedSuggestionProvider>)new RootCommandNode();
/*     */         } else {
/* 216 */           if ((this.flags & 0x8) != 0) {
/* 217 */             if ((debug1[this.redirect]).node == null) {
/* 218 */               return false;
/*     */             }
/* 220 */             this.builder.redirect((debug1[this.redirect]).node);
/*     */           } 
/*     */           
/* 223 */           if ((this.flags & 0x4) != 0) {
/* 224 */             this.builder.executes(debug0 -> 0);
/*     */           }
/*     */           
/* 227 */           this.node = this.builder.build();
/*     */         } 
/*     */       }
/*     */       
/* 231 */       for (int debug5 : this.children) {
/* 232 */         if ((debug1[debug5]).node == null) {
/* 233 */           return false;
/*     */         }
/*     */       } 
/*     */       
/* 237 */       for (int debug5 : this.children) {
/* 238 */         CommandNode<SharedSuggestionProvider> debug6 = (debug1[debug5]).node;
/* 239 */         if (!(debug6 instanceof RootCommandNode)) {
/* 240 */           this.node.addChild(debug6);
/*     */         }
/*     */       } 
/*     */       
/* 244 */       return true;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundCommandsPacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */