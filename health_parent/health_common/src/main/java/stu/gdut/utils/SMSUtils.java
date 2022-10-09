package stu.gdut.utils;

import com.aliyun.tea.*;
import com.aliyun.dysmsapi20170525.*;
import com.aliyun.dysmsapi20170525.models.*;
import com.aliyun.teaopenapi.*;
import com.aliyun.teaopenapi.models.*;
import com.aliyun.teautil.*;
import com.aliyun.teautil.models.*;

/**
 * 短信发送工具类
 */
public class SMSUtils {
	private static String accessKeyId="LTAI5tL1129qRoda8e9BsDv4";
	private static String accessKeySecret="mKw2rZmQ6k8LK0H3FBxpt7inLo5IGN";
	// 申请不了签名、模板，只能用阿里云提供的测试签名及模板
	public static final String VALIDATE_CODE = "SMS_154950909";

	/**
	 * 使用AK&SK初始化账号Client
	 * @param accessKeyId
	 * @param accessKeySecret
	 * @return Client
	 * @throws Exception
	 */
	public static com.aliyun.dysmsapi20170525.Client createClient(String accessKeyId, String accessKeySecret) throws Exception {
		Config config = new Config()
				// 您的 AccessKey ID
				.setAccessKeyId(accessKeyId)
				// 您的 AccessKey Secret
				.setAccessKeySecret(accessKeySecret);
		// 访问的域名
		config.endpoint = "dysmsapi.aliyuncs.com";
		return new com.aliyun.dysmsapi20170525.Client(config);
	}

	public static void sendShortMessage(String phoneNumbers,String param) throws Exception{
		com.aliyun.dysmsapi20170525.Client client = SMSUtils.createClient(accessKeyId, accessKeySecret);
		SendSmsRequest sendSmsRequest = new SendSmsRequest()
				.setSignName("阿里云短信测试")
				.setTemplateCode(VALIDATE_CODE)
				.setPhoneNumbers(phoneNumbers)
				.setTemplateParam("{\"code\":\""+param+"\"}");
		RuntimeOptions runtime = new RuntimeOptions();
		try {
			// 复制代码运行请自行打印 API 的返回值
			client.sendSmsWithOptions(sendSmsRequest, runtime);
		} catch (TeaException error) {
			// 如有需要，请打印 error
			com.aliyun.teautil.Common.assertAsString(error.message);
		} catch (Exception _error) {
			TeaException error = new TeaException(_error.getMessage(), _error);
			// 如有需要，请打印 error
			com.aliyun.teautil.Common.assertAsString(error.message);
		}
	}
}
