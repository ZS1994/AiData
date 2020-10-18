package com.zs.aidata.dccp.service;

import com.zs.aidata.core.BaseCoreService;
import com.zs.aidata.core.RestTemplateUtils;
import com.zs.aidata.dccp.vo.DccpOutVO;
import org.springframework.http.HttpMethod;

import javax.inject.Named;
import java.util.HashMap;
import java.util.Map;

/**
 * 和彩云
 *
 * @author 张顺
 * @since 2020/10/18
 */
@Named
public class DccpService extends BaseCoreService implements IDccpService {


    /**
     * 获取验证码
     */
    private String getVerificationCode() {
        Map<String, String> header = new HashMap<>();
        header.put("Accept", "application/json, text/javascript, */*; q=0.01");
        header.put("Connection", "keep-alive");
        header.put("Cookie", "dp_vstif=FF413092FD1F695FE5EC10E6A5EE8E538D5D9D7BD3E428AEEA8DE859789D2452230671A642BBA30552A3AEDB1DDD0186A0B544C3A6E776BFFA98C849E41FDBC40F6872AA5F8AD7F50E1877E316F27F703165CF394E996BF56CA51793BC3ED68DEE42AE51F698463D5BF4C0C0E7E7182F4307A0E0C7C949FF895B92B6DE285183B98EC4F2E6796F96682C3F426B98F1C91E3B97AA321A86D8EE29BBB629F89B89D319D9F99098B4B71879D834771B1CEDAD5D8EFFBAA33298037C814C125E9BE00CB5BF6F605E0B0409AB35708254800BB4D18094868FA802F3C50F03E4CD8E76B8B14DADFD0C13B51E673A3F0FC866AA47D0AF09B51F8F289CDF8840EDF18F6F0177DAF44DEC94751FA0147A0969506667A63E182A3C309D309B97AF25A19558178808053CE99808ABC29DAF351D86461F1D609411C1E5772277FB0D4E954652BA04332BBAD9603649E752AA7ED77BB6336F70327FA3CC3B49737F9A02902F734AAF905D17918E6C540332CB76246ECAD82BAEF29B29AA7C3BAE42CE04BF495917595B421905BD5696E5E092DF035E11; SESSION=710e8ed8-ef17-46e1-94cb-d081168fc16b; SERVERID=61975982a92bc81e76e457bc0dd0b2b3|1603017329|1603017317; WT_FPC=id=2c1547408247ca554601602402559509:lv=1603017344672:ss=1603017316717");
        header.put("Host", "gd.dccp.liuliangjia.cn");
        header.put("Origin", "http://gd.dccp.liuliangjia.cn");
        header.put("Referer", "http://gd.dccp.liuliangjia.cn/dccp-portal/guangdong/views/zero/index.jsp");
        header.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/77.0.3865.90 Safari/537.36");
        header.put("X-Requested-With", "XMLHttpRequest");

        Map<String, String> params = new HashMap<>();
        params.put("loginTel", "13f3d5027cdb9d79487335ec57b499263d0d5abec002c518c5c145237cc4f71e7e2f1e02088bd153ddda05af20b6c34ef7136d77fc94d24815c6d4f6e99ee44c4e98351901b7ab460094d2495c7bea33b94e86f9389384ba94b4af8068c1e30b9d95ca1a2067cd2b907caa957c1e3a08c8e8f7b34c7f6118e41ffbb9ccf5e3b03b780bc5693f2f5a9305e9849ba9a04c2c1c1d6e706ccc2ee6522f40921a06279a39133ecc0efab34a39f503d81a458f8f4d6b7ce8928cdf49c8f1f4fbca8e0054d527f53d87b4c6ebd5f74601d27989e41e51ad1ab9a901b198de1a04a10772252f8fc8856d16422458bb8c8ce89f0cdbe35fa566904ac2055c020d1f0459e2");
        params.put("smsCodeType", "LOGIN");
        params.put("implClass", "gdAuthSmsCodeService");
        return RestTemplateUtils.execHttpRequest(URL_SEND_SMS_CODE, HttpMethod.POST, params, header);
    }


    @Override
    public DccpOutVO sendSmsCode() {
        DccpOutVO out = new DccpOutVO();
        out.setMessage(getVerificationCode());
        return out;
    }
}
