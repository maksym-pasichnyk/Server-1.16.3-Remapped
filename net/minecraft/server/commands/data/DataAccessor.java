package net.minecraft.server.commands.data;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.arguments.NbtPathArgument;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;

public interface DataAccessor {
  void setData(CompoundTag paramCompoundTag) throws CommandSyntaxException;
  
  CompoundTag getData() throws CommandSyntaxException;
  
  Component getModifiedSuccess();
  
  Component getPrintSuccess(Tag paramTag);
  
  Component getPrintSuccess(NbtPathArgument.NbtPath paramNbtPath, double paramDouble, int paramInt);
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\commands\data\DataAccessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */