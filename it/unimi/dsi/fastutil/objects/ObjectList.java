package it.unimi.dsi.fastutil.objects;

import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public interface ObjectList<K> extends List<K>, Comparable<List<? extends K>>, ObjectCollection<K> {
  ObjectListIterator<K> iterator();
  
  ObjectListIterator<K> listIterator();
  
  ObjectListIterator<K> listIterator(int paramInt);
  
  ObjectList<K> subList(int paramInt1, int paramInt2);
  
  void size(int paramInt);
  
  void getElements(int paramInt1, Object[] paramArrayOfObject, int paramInt2, int paramInt3);
  
  void removeElements(int paramInt1, int paramInt2);
  
  void addElements(int paramInt, K[] paramArrayOfK);
  
  void addElements(int paramInt1, K[] paramArrayOfK, int paramInt2, int paramInt3);
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\i\\unimi\dsi\fastutil\objects\ObjectList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */