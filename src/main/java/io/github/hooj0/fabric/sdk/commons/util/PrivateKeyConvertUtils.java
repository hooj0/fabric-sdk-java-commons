package io.github.hooj0.fabric.sdk.commons.util;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.bouncycastle.openssl.jcajce.JcaPEMWriter;

/**
 * private key converter utils
 * @author hoojo
 * @createDate 2018年8月9日 下午4:42:39
 * @file PrivateKeyConvertUtils.java
 * @package io.github.hooj0.fabric.sdk.commons.util
 * @project fabric-sdk-commons
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public abstract class PrivateKeyConvertUtils {

	private static final Pattern compile = Pattern.compile("^-----BEGIN CERTIFICATE-----$" + "(.*?)" + "\n-----END CERTIFICATE-----\n", Pattern.DOTALL | Pattern.MULTILINE);
	
	/**
	 * 将byte字节私钥转换成 PrivateKey 对象，适用于 文件类型的证书
	 * @author hoojo
	 * @createDate 2018年6月13日 上午11:28:54
	 * @param data key
	 * @return PrivateKey
	 */
	public static PrivateKey getPrivateKeyFromBytes(byte[] data) throws Exception {
	
		final Reader pemReader = new StringReader(new String(data));

		final PrivateKeyInfo pemPair;
		try (PEMParser pemParser = new PEMParser(pemReader)) {
			pemPair = (PrivateKeyInfo) pemParser.readObject();
		}

		PrivateKey privateKey = new JcaPEMKeyConverter().setProvider(BouncyCastleProvider.PROVIDER_NAME).getPrivateKey(pemPair);

		return privateKey;
	}
	
	/**
	 * 将 私钥证书 转换成字符串，适合是文件类型的私钥证书
	 * @author hoojo
	 * @createDate 2018年8月10日 上午10:44:42
	 * @param privateKey PrivateKey
	 * @return String
	 * @throws IOException
	 */
	public static String getStringFromPrivateKey(PrivateKey privateKey) throws IOException {
		StringWriter stringWriter = new StringWriter();

		JcaPEMWriter writer = new JcaPEMWriter(stringWriter);
		writer.writeObject(privateKey);
		writer.close();
		
		return stringWriter.toString();
	}
	
    public static String toString(PrivateKey privateKey) {
		return new String(Base64.getEncoder().encodeToString(privateKey.getEncoded()));
    }
    
    @Deprecated
	public static PrivateKey toPrivateKey(String privateKey) {
		byte[] base64 = Base64.getDecoder().decode(privateKey.getBytes());

		KeyFactory keyFactory;
		try {
			keyFactory = KeyFactory.getInstance("RSA");
			PKCS8EncodedKeySpec pKCS8EncodedKeySpec = new PKCS8EncodedKeySpec(base64);
			return keyFactory.generatePrivate(pKCS8EncodedKeySpec);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static String formatPEM(String pemFormat) {
        String ret = pemFormat;

        final Matcher matcher = compile.matcher(pemFormat);
        if (matcher.matches()) {
            final String base64Part = matcher.group(1).replaceAll("\n", "");
            Base64.Decoder b64dec = Base64.getDecoder();
            ret = new String(b64dec.decode(base64Part.getBytes(UTF_8)));
        } 

        return ret;
    }
}
