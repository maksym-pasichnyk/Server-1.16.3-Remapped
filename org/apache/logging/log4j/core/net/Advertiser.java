package org.apache.logging.log4j.core.net;

import java.util.Map;

public interface Advertiser {
  Object advertise(Map<String, String> paramMap);
  
  void unadvertise(Object paramObject);
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\net\Advertiser.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */