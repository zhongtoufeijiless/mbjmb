编写一个Demo App，将接口返回结果打印在界面上

接口地址：http://47.117.39.225:10018/
通过POST请求接口  /auth/v3.1/user/sendVerifiyCode
请求参数mobile = “81991415335”， type = “text”



加密的key配置
包名：com.tcssj.mbjmb
应用版本号：12.0.0
请求参数加密key：allWUzg1eFJ3ekpNQklUeQ==
请求头加密key：xDBrgJdnnY2w1Do7Ik6otonXQRgQyt46
请求参数加密名称：BFBPY
请求头参数名称：HCFQ
包别名：mbjmb

Post请求方式说明
请求head参数如下：

“packageName” : "包别名"
"请求头参数名称" : AESUtil2.encrypt(token, "请求头加密key") //对token 字符串进行加密
备注：
token = {"sourceChannel":"Orange","packageName":"com.tcssj.mbjmb","adid":"","version":"12.0.0","uuId":"","userId":""}
// 此处token时json对象格式字符

请求Body传参说明：采用RequestBody方式提交
接口原本参数进行AES加密
例如 原本参数 { "type": "text",  "mobile": "81991419936"}
进行AES加密  AESUtil2.encrypt("", 请求参数加密key);
提交参数得最终方式：RequestBody.create("加密后得到字符串",MediaType.parse("text/plain"));
接口返回数据：需AES解密才能正常使用， 解密key， 解密示例：AESUtil2.decrypt(body, “请求参数加密key”));

备注:  加解密实现
public static byte[] base64ToDecode(String str) {
byte[] byteStr = Base64.decode(str, Base64.DEFAULT);
return byteStr;
}

public static String encode(byte[] key) {
return Base64.encodeToString(key, Base64.DEFAULT);
}

/**
* 加密
*
* @param content 需要加密的内容
* @param key     加密密码
* @return
  */
  public static String encrypt(String content, String key) {
  return encrypt(content,key.getBytes());
  }

public static String encrypt(String content, byte[] key) {
try {
//构造密钥
SecretKeySpec skey = new SecretKeySpec(key, "utf-8");
//创建初始向量iv用于指定密钥偏移量(可自行指定但必须为128位)，因为AES是分组加密，下一组的iv就用上一组加密的密文来充当
IvParameterSpec iv = new IvParameterSpec(key, 0, 16);
//创建AES加密器
Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
byte[] byteContent = content.getBytes(StandardCharsets.UTF_8);
//使用加密器的加密模式
cipher.init(Cipher.ENCRYPT_MODE, skey, iv);
// 加密
byte[] result = cipher.doFinal(byteContent);
//使用BASE64对加密后的二进制数组进行编码
return encode(result);
} catch (Exception e) {
e.printStackTrace();
return content;
}
}

/**
* 解密
* @param content
* @param key
* @return
  */
  public static String decrypt(String content,  byte[] key) {
  try {

       SecretKeySpec skey = new SecretKeySpec(key, "utf-8");
       IvParameterSpec iv = new IvParameterSpec(key, 0, 16);
       Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
       //解密时使用加密器的解密模式
       // 初始化
       cipher.init(Cipher.DECRYPT_MODE, skey, iv);
       byte[] result = cipher.doFinal(base64ToDecode(content));
       // 解密
       return new String(result);
  } catch (Exception e) {
  return content;
  }
  }