package net.minecraft.nbt;

import java.util.AbstractList;

public abstract class CollectionTag<T extends Tag> extends AbstractList<T> implements Tag {
  public abstract T set(int paramInt, T paramT);
  
  public abstract void add(int paramInt, T paramT);
  
  public abstract T remove(int paramInt);
  
  public abstract boolean setTag(int paramInt, Tag paramTag);
  
  public abstract boolean addTag(int paramInt, Tag paramTag);
  
  public abstract byte getElementType();
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\nbt\CollectionTag.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */