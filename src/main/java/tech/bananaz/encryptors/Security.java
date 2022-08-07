package tech.bananaz.encryptors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

@Component
public class Security {
	
	private static Encoder hash = Base64.getEncoder();
	private static Decoder unHash = Base64.getDecoder();
	private static final String ENCRYPTION_METHOD = "Blowfish";
	private static final Logger LOGGER = LoggerFactory.getLogger(Security.class);
	
	public static String encrypt(String key, String value) throws Exception {
		String valToReturn = value;
		try {
			byte[] keyData = key.getBytes();
	        SecretKeySpec secretKeySpec = new SecretKeySpec(keyData, ENCRYPTION_METHOD);
	        Cipher cipher = Cipher.getInstance(ENCRYPTION_METHOD);
	        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
	        byte[] hasil = cipher.doFinal(value.getBytes());
	        valToReturn = hash.encodeToString(hasil);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			throw new Exception(e.getMessage(), e.getCause());
		}
		return valToReturn;
	}
	
	public static String decrypt(String key, String value) throws Exception {
		String valToReturn = value;
		try {
			byte[] keyData = key.getBytes();
	        SecretKeySpec secretKeySpec = new SecretKeySpec(keyData, ENCRYPTION_METHOD);
	        Cipher cipher = Cipher.getInstance(ENCRYPTION_METHOD);
	        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
	        byte[] valueUndoBase64 = unHash.decode(value) ;
	        byte[] hasil = cipher.doFinal(valueUndoBase64);
	        valToReturn = new String(hasil, StandardCharsets.UTF_8);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			throw new Exception(e.getMessage(), e.getCause());
		}
		return valToReturn;
	}

}
