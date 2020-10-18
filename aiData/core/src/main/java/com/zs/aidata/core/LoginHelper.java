package com.zs.aidata.core;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.json.GsonJsonParser;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import sun.net.www.http.HttpClient;

import java.io.*;
import java.net.InetSocketAddress;
import java.util.*;

/**
 * @author 张顺
 * @since 2020/10/12
 */
@Slf4j
public class LoginHelper {

    public static final String DUANXIN_YANZHENGMA_URL = "http://gd.10086.cn/gmccapp/login/?sessionid=1d8674941571a48538f4d0fc24b2d26a3&servicename=GMCCAPP_000_000_001_028";
    public static final String IMG_YANZHENGMA_URL = "http://gd.10086.cn/gmccapp/login/?sessionid=12ca338224a154734b183e85680dc511c&servicename=identifyingCode&businessId=singleProd&id=0.5712613275994671";
    public static final String LOGIN_URL = "http://gd.10086.cn/gmccapp/login/?sessionid=1d4e3758832ae4c74822344ede28d5a6f&servicename=GMCCAPP_000_000_001_010";

    String URL_ZJ = "http://gd.dccp.liuliangjia.cn/dccp-portal/my/sendSmsCode.ajax";

    String URL_ZJ_2T = "http://gd.dccp.liuliangjia.cn/dccp-portal/service/serviceSubscribe.ajax";

    String LZY_PHONE = "13502898808";
    String ZS_PHONE = "15022084174";


    public static void main(String[] args) throws IOException {
        LoginHelper test = new LoginHelper();
//        test.getgmccapp();
//        test.getIdentifyingCode();
        test.handle2T();
    }

    /**
     * 和彩云2T
     */
    private void handle2T() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", "application/json, text/javascript, */*; q=0.01");
        headers.add("Accept-Language", "zh-CN,zh;q=0.9");
        headers.add("Connection", "keep-alive");
        headers.add("Cookie", "dp_vstif=FF413092FD1F695FE5EC10E6A5EE8E538D5D9D7BD3E428AEEA8DE859789D2452230671A642BBA30552A3AEDB1DDD0186A0B544C3A6E776BFFA98C849E41FDBC40F6872AA5F8AD7F50E1877E316F27F703165CF394E996BF56CA51793BC3ED68DEE42AE51F698463D5BF4C0C0E7E7182F4307A0E0C7C949FF895B92B6DE285183B98EC4F2E6796F96682C3F426B98F1C91E3B97AA321A86D8EE29BBB629F89B89D319D9F99098B4B71879D834771B1CEDAD5D8EFFBAA33298037C814C125E9BE00CB5BF6F605E0B0409AB35708254800BB4D18094868FA802F3C50F03E4CD8E76B8B14DADFD0C13B51E673A3F0FC866AA47D0AF09B51F8F289CDF8840EDF18F6F0177DAF44DEC94751FA0147A0969506667A63E182A3C309D309B97AF25A19558178808053CE99808ABC29DAF351D86461F1D609411C1E5772277FB0D4E954652BA04332BBAD9603649E752AA7ED77BB6336F70327FA3CC3B49737F9A02902F734AAF905D17918E6C540332CB76246ECAD82BAEF29B29AA7C3BAE42CE04BF495917595B421905BD5696E5E092DF035E11; dp_vtfs=4BA72B0A831F97D280FC7D4C2553468E36A25AD3F8FFE76286EC2FACE1C1DDF3553E0A25413083006FDB6CBB5B8DEA52E75801CDFEBF336BBA466F0A61B3FE447F3D75455D246E8A3973C1B8F53E7481878951045116D30B6CC63797AEC445268C6CF8ED9151907799DD0EE0CE300C28ABC20D3C62B4AEF3120E0202CE4FF28CBC62D27BC0E7DFA608CBB77622EBA5C0DE861BD809FBB56F69186A04E29EEC1110930D206CE705F2B2F3CC667E0ECCFF89210CF90334BF13A653F644B183ECBE24ED8C939B85A98D732E84BAF11DE279BAA95A1181CD9F384A9F92F1D82723C2ECAF9E4A20C5ED0D8A9049580A1409FF7F0C3491A701C0CCF2C65FFE8DE15E54DC505D3856BF6CB30510E4A3CF8CB37A5011D86DAA6F0C8CC50F46927601B547F48E6FB6842C59E881DA24B62131DB3EF4BA4F6687B32CCBB7E88008D01E6BDA8CE4CE2CA66E09BCA22CC472A984BA16B3D9450DD6C3CD0951FF8B46D6657ACFCC5E3F3A00465A24533D63BDE00CE3021367858C99C9F3CFC87E188F660872E1DF2D0C839B71190876297F164D2EC6EDE8E870E3E76BF565256A4B608F07157096627A7FD991DFBFE8AC0179455FF36675E90A31F05FE583AF34F50239D8BF018726CB012CE71EA318941B29C3E71E1B506D8CA898FE0C2B074F8D3E0EB07B099D80E948EB37665E2B91DA6B598CE836BE172A5503DC2931904BE9B7EA37545A576070F49FB3E994F73E58891D927CEE; SESSION=57fe0a21-4544-4821-bb56-cd2a32250b5e; mobile=43160-18440-4934-20518; SERVERID=61975982a92bc81e76e457bc0dd0b2b3|1603020107|1603017317; WT_FPC=id=2c1547408247ca554601602402559509:lv=1603020121383:ss=1603017316717");
        headers.add("Host", "gd.dccp.liuliangjia.cn");
        headers.add("Origin", "http://gd.dccp.liuliangjia.cn");
        headers.add("Referer", "http://gd.dccp.liuliangjia.cn/dccp-portal/guangdong/views/zero/index.jsp");
        headers.add("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/77.0.3865.90 Safari/537.36");
        headers.add("X-Requested-With", "XMLHttpRequest");
        headers.add("charset", "UTF-8");
        RestTemplate rest = RestTemplateUtils.getRestemplate();
        MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
        param.add("prodId", "PD202004297569062073");
        param.add("srcType", "2");
        param.add("srcId", "ACT20200427000000011");
        param.add("syncType", "YES");
        param.add("orderType", "1");
        param.add("salesId", "");
        param.add("channelId", "");
        HttpEntity<Map> requestEntity = new HttpEntity<Map>(param, headers);
        ResponseEntity<String> response = rest.exchange(URL_ZJ_2T, HttpMethod.POST, requestEntity, String.class);
        String str = response.getBody();
        log.info(str.toString());
    }


    /**
     * 获取宅家乐享不停手机短信验证码
     */
    private void getdccp() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", "application/json, text/javascript, */*; q=0.01");
        headers.add("Accept-Language", "zh-CN,zh;q=0.9");
        headers.add("Connection", "keep-alive");
        headers.add("Cookie", "dp_vstif=FF413092FD1F695FE5EC10E6A5EE8E538D5D9D7BD3E428AEEA8DE859789D2452230671A642BBA30552A3AEDB1DDD0186A0B544C3A6E776BFFA98C849E41FDBC40F6872AA5F8AD7F50E1877E316F27F703165CF394E996BF56CA51793BC3ED68DEE42AE51F698463D5BF4C0C0E7E7182F4307A0E0C7C949FF895B92B6DE285183B98EC4F2E6796F96682C3F426B98F1C91E3B97AA321A86D8EE29BBB629F89B89D319D9F99098B4B71879D834771B1CEDAD5D8EFFBAA33298037C814C125E9BE00CB5BF6F605E0B0409AB35708254800BB4D18094868FA802F3C50F03E4CD8E76B8B14DADFD0C13B51E673A3F0FC866AA47D0AF09B51F8F289CDF8840EDF18F6F0177DAF44DEC94751FA0147A0969506667A63E182A3C309D309B97AF25A19558178808053CE99808ABC29DAF351D86461F1D609411C1E5772277FB0D4E954652BA04332BBAD9603649E752AA7ED77BB6336F70327FA3CC3B49737F9A02902F734AAF905D17918E6C540332CB76246ECAD82BAEF29B29AA7C3BAE42CE04BF495917595B421905BD5696E5E092DF035E11; SESSION=710e8ed8-ef17-46e1-94cb-d081168fc16b; SERVERID=61975982a92bc81e76e457bc0dd0b2b3|1603017329|1603017317; WT_FPC=id=2c1547408247ca554601602402559509:lv=1603017344672:ss=1603017316717");
        headers.add("Host", "gd.dccp.liuliangjia.cn");
        headers.add("Origin", "http://gd.dccp.liuliangjia.cn");
        headers.add("Referer", "http://gd.dccp.liuliangjia.cn/dccp-portal/guangdong/views/zero/index.jsp");
        headers.add("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/77.0.3865.90 Safari/537.36");
        headers.add("X-Requested-With", "XMLHttpRequest");
        headers.add("charset", "UTF-8");
        RestTemplate rest = RestTemplateUtils.getRestemplate();
        MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
        param.add("loginTel", "13f3d5027cdb9d79487335ec57b499263d0d5abec002c518c5c145237cc4f71e7e2f1e02088bd153ddda05af20b6c34ef7136d77fc94d24815c6d4f6e99ee44c4e98351901b7ab460094d2495c7bea33b94e86f9389384ba94b4af8068c1e30b9d95ca1a2067cd2b907caa957c1e3a08c8e8f7b34c7f6118e41ffbb9ccf5e3b03b780bc5693f2f5a9305e9849ba9a04c2c1c1d6e706ccc2ee6522f40921a06279a39133ecc0efab34a39f503d81a458f8f4d6b7ce8928cdf49c8f1f4fbca8e0054d527f53d87b4c6ebd5f74601d27989e41e51ad1ab9a901b198de1a04a10772252f8fc8856d16422458bb8c8ce89f0cdbe35fa566904ac2055c020d1f0459e2");
        param.add("smsCodeType", "LOGIN");
        param.add("implClass", "gdAuthSmsCodeService");
        HttpEntity<Map> requestEntity = new HttpEntity<Map>(param, headers);
        ResponseEntity<String> response = rest.exchange(URL_ZJ, HttpMethod.POST, requestEntity, String.class);
        String str = response.getBody();

        log.info(str.toString());
    }


    /**
     * 获取手机短信验证码
     */
    private void getgmccapp() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.ALL));
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        RestTemplate rest = RestTemplateUtils.getRestemplate();
        MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
        param.add("reqJson", "{\"mobileNumber\":\"15022084174\",\"bussnissId\":\"differentNets\",\"header\":{\"nonce\":\"bgacedfbfhjegsjqtorfyibmnigm86dv\",\"timestamp\":1602435157946,\"sign\":\"AD31EAC421AF9717FC3763BA4DF68B5F\"}}");
        HttpEntity<Map> requestEntity = new HttpEntity<Map>(param, headers);
        ResponseEntity<String> response = rest.exchange(DUANXIN_YANZHENGMA_URL, HttpMethod.POST, requestEntity, String.class);
        String str = response.getBody();
        log.info(str);
    }


    /**
     * 获取图形验证码
     */
    private void getIdentifyingCode() throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        RestTemplate rest = RestTemplateUtils.getRestemplate();
        HttpEntity<Map> requestEntity = new HttpEntity<Map>(null, headers);
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            ResponseEntity<byte[]> response = rest.exchange(IMG_YANZHENGMA_URL, HttpMethod.GET, requestEntity, byte[].class);
            byte[] result = response.getBody();

            inputStream = new ByteArrayInputStream(result);

            File file = new File("D://a.jpeg");
            if (!file.exists()) {
                file.createNewFile();
            }
            outputStream = new FileOutputStream(file);
            int len = 0;
            byte[] buf = new byte[1024];
            while ((len = inputStream.read(buf, 0, 1024)) != -1) {
                outputStream.write(buf, 0, len);
            }
            outputStream.flush();
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            if (outputStream != null) {
                outputStream.close();
            }
        }
    }


    /**
     * 登录
     */
    private void login() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.ALL));
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        RestTemplate rest = RestTemplateUtils.getRestemplate();
        MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
        param.add("reqJson", "{\"mobileNumber\":\"15022084174\",\"password\":\"850328\",\"identifyingCode\":\"\",\"isGDMobile\":\"1\",\"bussnissId\":\"differentNets\",\"header\":{\"nonce\":\"bgacfbgccgdag0uzio7seq6sz7qbu5s8\",\"timestamp\":1602516226306,\"sign\":\"C84DA64FCDA0DAD7FD1B03124B1E2598\"}}");
        HttpEntity<Map> requestEntity = new HttpEntity<Map>(param, headers);
        ResponseEntity<String> response = rest.exchange(LOGIN_URL, HttpMethod.POST, requestEntity, String.class);
        String str = response.getBody();
        log.info(str);
    }

}
