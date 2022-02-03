package BotBinance;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class HMAC {
	/**
	 * 
	 * Utility class that map the bytes of the parameters using secretKey and sha256 algorithm
	 * @param secretKey
	 * @param message
	 * @return
	 */
	  static public byte[] calcHmacSha256(byte[] secretKey, byte[] message) {
	    byte[] hmacSha256 = null;
	    try {
	      Mac mac = Mac.getInstance("HmacSHA256");
	      SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey, "HmacSHA256");
	      mac.init(secretKeySpec);
	      hmacSha256 = mac.doFinal(message);
	    } catch (Exception e) {
	      throw new RuntimeException("Failed to calculate hmac-sha256", e);
	    }
	    return hmacSha256;
	  }
	}