package net.minecraft.nbt;

public abstract class NumericTag implements Tag {
  public abstract long getAsLong();
  
  public abstract int getAsInt();
  
  public abstract short getAsShort();
  
  public abstract byte getAsByte();
  
  public abstract double getAsDouble();
  
  public abstract float getAsFloat();
  
  public abstract Number getAsNumber();
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\nbt\NumericTag.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */