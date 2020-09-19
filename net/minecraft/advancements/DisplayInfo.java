/*     */ package net.minecraft.advancements;
/*     */ 
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.google.gson.JsonParseException;
/*     */ import com.google.gson.JsonSyntaxException;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.TagParser;
/*     */ import net.minecraft.network.FriendlyByteBuf;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.resources.ResourceLocation;
/*     */ import net.minecraft.util.GsonHelper;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DisplayInfo
/*     */ {
/*     */   private final Component title;
/*     */   private final Component description;
/*     */   private final ItemStack icon;
/*     */   private final ResourceLocation background;
/*     */   private final FrameType frame;
/*     */   
/*     */   public DisplayInfo(ItemStack debug1, Component debug2, Component debug3, @Nullable ResourceLocation debug4, FrameType debug5, boolean debug6, boolean debug7, boolean debug8) {
/*  33 */     this.title = debug2;
/*  34 */     this.description = debug3;
/*  35 */     this.icon = debug1;
/*  36 */     this.background = debug4;
/*  37 */     this.frame = debug5;
/*  38 */     this.showToast = debug6;
/*  39 */     this.announceChat = debug7;
/*  40 */     this.hidden = debug8;
/*     */   }
/*     */   private final boolean showToast; private final boolean announceChat; private final boolean hidden; private float x; private float y;
/*     */   public void setLocation(float debug1, float debug2) {
/*  44 */     this.x = debug1;
/*  45 */     this.y = debug2;
/*     */   }
/*     */   
/*     */   public Component getTitle() {
/*  49 */     return this.title;
/*     */   }
/*     */   
/*     */   public Component getDescription() {
/*  53 */     return this.description;
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
/*     */   public FrameType getFrame() {
/*  66 */     return this.frame;
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
/*     */   public boolean shouldAnnounceChat() {
/*  82 */     return this.announceChat;
/*     */   }
/*     */   
/*     */   public boolean isHidden() {
/*  86 */     return this.hidden;
/*     */   }
/*     */   
/*     */   public static DisplayInfo fromJson(JsonObject debug0) {
/*  90 */     MutableComponent mutableComponent1 = Component.Serializer.fromJson(debug0.get("title"));
/*  91 */     MutableComponent mutableComponent2 = Component.Serializer.fromJson(debug0.get("description"));
/*  92 */     if (mutableComponent1 == null || mutableComponent2 == null) {
/*  93 */       throw new JsonSyntaxException("Both title and description must be set");
/*     */     }
/*  95 */     ItemStack debug3 = getIcon(GsonHelper.getAsJsonObject(debug0, "icon"));
/*  96 */     ResourceLocation debug4 = debug0.has("background") ? new ResourceLocation(GsonHelper.getAsString(debug0, "background")) : null;
/*  97 */     FrameType debug5 = debug0.has("frame") ? FrameType.byName(GsonHelper.getAsString(debug0, "frame")) : FrameType.TASK;
/*  98 */     boolean debug6 = GsonHelper.getAsBoolean(debug0, "show_toast", true);
/*  99 */     boolean debug7 = GsonHelper.getAsBoolean(debug0, "announce_to_chat", true);
/* 100 */     boolean debug8 = GsonHelper.getAsBoolean(debug0, "hidden", false);
/* 101 */     return new DisplayInfo(debug3, (Component)mutableComponent1, (Component)mutableComponent2, debug4, debug5, debug6, debug7, debug8);
/*     */   }
/*     */   
/*     */   private static ItemStack getIcon(JsonObject debug0) {
/* 105 */     if (!debug0.has("item")) {
/* 106 */       throw new JsonSyntaxException("Unsupported icon type, currently only items are supported (add 'item' key)");
/*     */     }
/* 108 */     Item debug1 = GsonHelper.getAsItem(debug0, "item");
/* 109 */     if (debug0.has("data")) {
/* 110 */       throw new JsonParseException("Disallowed data tag found");
/*     */     }
/* 112 */     ItemStack debug2 = new ItemStack((ItemLike)debug1);
/* 113 */     if (debug0.has("nbt")) {
/*     */       try {
/* 115 */         CompoundTag debug3 = TagParser.parseTag(GsonHelper.convertToString(debug0.get("nbt"), "nbt"));
/* 116 */         debug2.setTag(debug3);
/* 117 */       } catch (CommandSyntaxException debug3) {
/* 118 */         throw new JsonSyntaxException("Invalid nbt tag: " + debug3.getMessage());
/*     */       } 
/*     */     }
/*     */     
/* 122 */     return debug2;
/*     */   }
/*     */   
/*     */   public void serializeToNetwork(FriendlyByteBuf debug1) {
/* 126 */     debug1.writeComponent(this.title);
/* 127 */     debug1.writeComponent(this.description);
/* 128 */     debug1.writeItem(this.icon);
/* 129 */     debug1.writeEnum(this.frame);
/* 130 */     int debug2 = 0;
/* 131 */     if (this.background != null) {
/* 132 */       debug2 |= 0x1;
/*     */     }
/* 134 */     if (this.showToast) {
/* 135 */       debug2 |= 0x2;
/*     */     }
/* 137 */     if (this.hidden) {
/* 138 */       debug2 |= 0x4;
/*     */     }
/* 140 */     debug1.writeInt(debug2);
/* 141 */     if (this.background != null) {
/* 142 */       debug1.writeResourceLocation(this.background);
/*     */     }
/* 144 */     debug1.writeFloat(this.x);
/* 145 */     debug1.writeFloat(this.y);
/*     */   }
/*     */   
/*     */   public static DisplayInfo fromNetwork(FriendlyByteBuf debug0) {
/* 149 */     Component debug1 = debug0.readComponent();
/* 150 */     Component debug2 = debug0.readComponent();
/* 151 */     ItemStack debug3 = debug0.readItem();
/* 152 */     FrameType debug4 = (FrameType)debug0.readEnum(FrameType.class);
/* 153 */     int debug5 = debug0.readInt();
/* 154 */     ResourceLocation debug6 = ((debug5 & 0x1) != 0) ? debug0.readResourceLocation() : null;
/* 155 */     boolean debug7 = ((debug5 & 0x2) != 0);
/* 156 */     boolean debug8 = ((debug5 & 0x4) != 0);
/* 157 */     DisplayInfo debug9 = new DisplayInfo(debug3, debug1, debug2, debug6, debug4, debug7, false, debug8);
/* 158 */     debug9.setLocation(debug0.readFloat(), debug0.readFloat());
/* 159 */     return debug9;
/*     */   }
/*     */   
/*     */   public JsonElement serializeToJson() {
/* 163 */     JsonObject debug1 = new JsonObject();
/*     */     
/* 165 */     debug1.add("icon", (JsonElement)serializeIcon());
/* 166 */     debug1.add("title", Component.Serializer.toJsonTree(this.title));
/* 167 */     debug1.add("description", Component.Serializer.toJsonTree(this.description));
/* 168 */     debug1.addProperty("frame", this.frame.getName());
/* 169 */     debug1.addProperty("show_toast", Boolean.valueOf(this.showToast));
/* 170 */     debug1.addProperty("announce_to_chat", Boolean.valueOf(this.announceChat));
/* 171 */     debug1.addProperty("hidden", Boolean.valueOf(this.hidden));
/*     */     
/* 173 */     if (this.background != null) {
/* 174 */       debug1.addProperty("background", this.background.toString());
/*     */     }
/*     */     
/* 177 */     return (JsonElement)debug1;
/*     */   }
/*     */   
/*     */   private JsonObject serializeIcon() {
/* 181 */     JsonObject debug1 = new JsonObject();
/* 182 */     debug1.addProperty("item", Registry.ITEM.getKey(this.icon.getItem()).toString());
/* 183 */     if (this.icon.hasTag()) {
/* 184 */       debug1.addProperty("nbt", this.icon.getTag().toString());
/*     */     }
/* 186 */     return debug1;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\advancements\DisplayInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */