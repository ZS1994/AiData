package com.zs.aidata.core;

import lombok.extern.slf4j.Slf4j;
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


    public static void main(String[] args) throws IOException {
        LoginHelper test = new LoginHelper();
//        test.getgmccapp();
        test.getIdentifyingCode();
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


}
