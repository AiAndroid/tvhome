/**
 *   Copyright(c) 2012 DuoKan TV Group
 *    
 *   TokenInfo.java
 *
 *   @author tianli(tianli@duokan.com)
 *
 *   2012-9-6 
 */
package com.tv.ui.metro.model;

import android.text.TextUtils;

import java.io.Serializable;

/**
 * @author tianli
 *
 */
public class TokenInfo implements Serializable {
	
	private static final long serialVersionUID = 4L;
    public static final String AUTH_TOKEY_TYPE_XIAOMI = "video";
    public static final String AUTH_TOKEY_TYPE_BSS = "tvideo";

    public TokenInfo(){
        tick = System.currentTimeMillis();
    }

    public long        tick;
	public String      uid;          //  unique identifier for device
	public UserAccount userAccount;  // token info for XiaoMi account.
    public LoginData   loginData;
//	public String      userToken;
//	public String      userKey;

    public UserAccount bssUserAccount;

    public void setUserAccount(String accountName, String authToken, String ssec, String tokenType){
        if (AUTH_TOKEY_TYPE_XIAOMI.equals(tokenType)){
            if (userAccount == null)
                userAccount = new UserAccount();

            if (!TextUtils.isEmpty(accountName)){
                userAccount.accountName = accountName;
            }
            userAccount.authToken = authToken;
            userAccount.ssec = ssec;
        }else if (AUTH_TOKEY_TYPE_BSS.equals(tokenType)){
            if (bssUserAccount == null)
                bssUserAccount = new UserAccount();

            if (!TextUtils.isEmpty(accountName)){
                bssUserAccount.accountName = accountName;
            }
            bssUserAccount.authToken = authToken;
            bssUserAccount.ssec = ssec;
        }
    }

    public void setLoginData(String access_key, String access_token){
        if (loginData == null){
            loginData = new LoginData();
        }

        loginData.access_token = access_token;
        loginData.access_key = access_key;
    }

    //after auth from server
    public static class UserAccount implements Serializable {
        private static final long serialVersionUID = 2L;

        public String accountName; //mi id
        public String authToken;   //for sign
        public String ssec;        //for sign
        public String tokenType;
    }

    public static class LoginData implements Serializable {
        public int    level;
        public String access_token;
        public String access_key;
    }

}
