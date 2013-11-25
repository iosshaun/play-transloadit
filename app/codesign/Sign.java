package codesign;

import javax.crypto.*;
import javax.crypto.spec.*;

public class Sign {
    
    public String signature(String rawValue, String secret) {
        try {
            byte[] secretBytes = secret.getBytes();
            SecretKey key = new SecretKeySpec(secretBytes, "HmacSHA1");
            Mac m = Mac.getInstance("HmacSHA1");
            m.init(key);
            m.update(rawValue.getBytes());
            byte[] mac = m.doFinal();
            return bytesToHex(mac);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean signatureMatches(String receivedValue, 
                                    String secret,
                                    String receivedHmac) {
        return receivedHmac.equals(signature(receivedValue, secret));
    }

    public String bytesToHex(byte[] data) {
        if (data == null) return null;
        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < data.length; i++) {
            hexValue.append(Integer.toString((data[i] & 0xff) + 0x100, 16)
                    .substring(1));
        }
        return hexValue.toString();
    }
}
