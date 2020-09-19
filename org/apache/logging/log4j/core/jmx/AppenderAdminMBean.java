package org.apache.logging.log4j.core.jmx;

public interface AppenderAdminMBean {
  public static final String PATTERN = "org.apache.logging.log4j2:type=%s,component=Appenders,name=%s";
  
  String getName();
  
  String getLayout();
  
  boolean isIgnoreExceptions();
  
  String getErrorHandler();
  
  String getFilter();
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\jmx\AppenderAdminMBean.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */