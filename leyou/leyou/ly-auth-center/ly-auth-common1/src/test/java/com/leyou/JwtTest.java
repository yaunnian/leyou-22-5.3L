package com.leyou;


import com.leyou.entity.UserInfo1;
import com.leyou.utils.JwtUtils;
import com.leyou.utils.RsaUtils;
import org.junit.Before;
import org.junit.Test;

import java.security.PrivateKey;
import java.security.PublicKey;

public class JwtTest {
    
    //先创建tmp/rsa目录
    private static final String pubKeyPath = "G:\\temp\\rsa\\rsa.pub";

    private static final String priKeyPath = "G:\\temp\\rsa\\rsa.pri";

    private PublicKey publicKey;

    private PrivateKey privateKey;

    //第一步产生公钥 私钥 产生后注释
/*    @Test
    public void testRsa() throws Exception {
        RsaUtils.generateKey(pubKeyPath, priKeyPath, "1234");
    }*/

    //在所有的测试执行之前，先加载公钥和私钥
    @Before
    public void testGetRsa() throws Exception {
        this.publicKey = RsaUtils.getPublicKey(pubKeyPath);
        this.privateKey = RsaUtils.getPrivateKey(priKeyPath);
    }

    @Test
    public void testGenerateToken() throws Exception {
        // 生成token
        String token = JwtUtils.generateToken(new UserInfo1(666L, "tom"), privateKey, 5);
        System.out.println("token = " + token);
    }

    @Test
    public void testParseToken() throws Exception {
        //String token = "eyJhbGciOiJSUzI1NiJ9.eyJpZCI6MzIsInVzZXJuYW1lIjoiaGVpbWE2MiIsImV4cCI6MTU1NTQwNTgwMH0.p_5U7oEsS2soIEibyPhcjeUWbu6781tAwMblD-kHs_H_wtZqldceYrEUNtphNeUpZED9gaPI28gPbJqf3i3DiF1s4phOw-T7bJG5aNAG2BNm0iuG0FXO40j451OkTCWPNg4HCIXwIXRpq6ez0LJ4AKWBwvF_a0ii6bp7H3kZ6po";
        String token="eyJhbGciOiJSUzI1NiJ9.eyJpZCI6NjY2LCJ1c2VybmFtZSI6InRvbSIsImV4cCI6MTU3OTAwMDc0OH0.L5BT9MD5sm_pMnUgTV_4OIiJdWWhfoVPpM6bdH7s11AJ9kHME3l-h8FC_Hta-tCy_eaRSmu--ugywnSv61FLqMFdmav41MxPqUJrAvGMijdIMNg7-jzPjacB2lCsfLiFVEo-q3jMhS5amzJs_pmQgGMk7CjDD2GAuxk80xlYRqQ";
        // 解析token
        UserInfo1 user = JwtUtils.getInfoFromToken(token, publicKey);
        System.out.println("id: " + user.getId());
        System.out.println("userName: " + user.getUsername());
    }
}