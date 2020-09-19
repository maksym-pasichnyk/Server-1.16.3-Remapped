/*    */ package com.mojang.authlib.yggdrasil;
/*    */ 
/*    */ import com.google.common.base.Strings;
/*    */ import com.google.common.collect.Iterables;
/*    */ import com.google.common.collect.Sets;
/*    */ import com.mojang.authlib.Agent;
/*    */ import com.mojang.authlib.Environment;
/*    */ import com.mojang.authlib.GameProfile;
/*    */ import com.mojang.authlib.GameProfileRepository;
/*    */ import com.mojang.authlib.HttpAuthenticationService;
/*    */ import com.mojang.authlib.ProfileLookupCallback;
/*    */ import com.mojang.authlib.exceptions.AuthenticationException;
/*    */ import com.mojang.authlib.yggdrasil.response.ProfileSearchResultsResponse;
/*    */ import java.util.List;
/*    */ import java.util.Set;
/*    */ import org.apache.logging.log4j.LogManager;
/*    */ import org.apache.logging.log4j.Logger;
/*    */ 
/*    */ public class YggdrasilGameProfileRepository
/*    */   implements GameProfileRepository {
/* 21 */   private static final Logger LOGGER = LogManager.getLogger();
/*    */   
/*    */   private final String searchPageUrl;
/*    */   private static final int ENTRIES_PER_PAGE = 2;
/*    */   private static final int MAX_FAIL_COUNT = 3;
/*    */   private static final int DELAY_BETWEEN_PAGES = 100;
/*    */   private static final int DELAY_BETWEEN_FAILURES = 750;
/*    */   private final YggdrasilAuthenticationService authenticationService;
/*    */   
/*    */   public YggdrasilGameProfileRepository(YggdrasilAuthenticationService authenticationService, Environment environment) {
/* 31 */     this.authenticationService = authenticationService;
/* 32 */     this.searchPageUrl = environment.getAccountsHost() + "/profiles/";
/*    */   }
/*    */ 
/*    */   
/*    */   public void findProfilesByNames(String[] names, Agent agent, ProfileLookupCallback callback) {
/* 37 */     Set<String> criteria = Sets.newHashSet();
/*    */     
/* 39 */     for (String name : names) {
/* 40 */       if (!Strings.isNullOrEmpty(name)) {
/* 41 */         criteria.add(name.toLowerCase());
/*    */       }
/*    */     } 
/*    */     
/* 45 */     int page = 0;
/*    */     
/* 47 */     label48: for (List<String> request : (Iterable<List<String>>)Iterables.partition(criteria, 2)) {
/* 48 */       int failCount = 0;
/*    */ 
/*    */       
/*    */       while (true) {
/* 52 */         boolean failed = false;
/*    */         
/*    */         try {
/* 55 */           ProfileSearchResultsResponse response = this.authenticationService.<ProfileSearchResultsResponse>makeRequest(HttpAuthenticationService.constantURL(this.searchPageUrl + agent.getName().toLowerCase()), request, ProfileSearchResultsResponse.class);
/* 56 */           failCount = 0;
/*    */           
/* 58 */           LOGGER.debug("Page {} returned {} results, parsing", new Object[] { Integer.valueOf(0), Integer.valueOf((response.getProfiles()).length) });
/*    */           
/* 60 */           Set<String> missing = Sets.newHashSet(request);
/* 61 */           for (GameProfile profile : response.getProfiles()) {
/* 62 */             LOGGER.debug("Successfully looked up profile {}", new Object[] { profile });
/* 63 */             missing.remove(profile.getName().toLowerCase());
/* 64 */             callback.onProfileLookupSucceeded(profile);
/*    */           } 
/*    */           
/* 67 */           for (String name : missing) {
/* 68 */             LOGGER.debug("Couldn't find profile {}", new Object[] { name });
/* 69 */             callback.onProfileLookupFailed(new GameProfile(null, name), new ProfileNotFoundException("Server did not find the requested profile"));
/*    */           } 
/*    */           
/*    */           try {
/* 73 */             Thread.sleep(100L);
/* 74 */           } catch (InterruptedException interruptedException) {}
/*    */         }
/* 76 */         catch (AuthenticationException e) {
/* 77 */           failCount++;
/*    */           
/* 79 */           if (failCount == 3) {
/* 80 */             for (String name : request) {
/* 81 */               LOGGER.debug("Couldn't find profile {} because of a server error", new Object[] { name });
/* 82 */               callback.onProfileLookupFailed(new GameProfile(null, name), (Exception)e);
/*    */             } 
/*    */           } else {
/*    */             try {
/* 86 */               Thread.sleep(750L);
/* 87 */             } catch (InterruptedException interruptedException) {}
/*    */             
/* 89 */             failed = true;
/*    */           } 
/*    */         } 
/* 92 */         if (!failed)
/*    */           continue label48; 
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\authlib\yggdrasil\YggdrasilGameProfileRepository.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */