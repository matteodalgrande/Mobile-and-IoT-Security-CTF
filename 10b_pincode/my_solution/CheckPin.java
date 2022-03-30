import java.util.*;
import java.security.*;

class CheckPin {
  CheckPin(){

  }
  
   public boolean checkPin( String pin) {
        if (pin.length() != 6) {
            return false;
        }
        try {
            byte[] pinBytes = pin.getBytes();
            for (int i = 0; i < 25; i++) {
                for (int j = 0; j < 400; j++) {
                    MessageDigest md = MessageDigest.getInstance("MD5");
                    md.update(pinBytes);
                    pinBytes = (byte[]) md.digest().clone();
                }
            }
            if (toHexString(pinBytes).equals("d04988522ddfed3133cc24fb6924eae9")) {
                System.out.println("Pin is: " + pin);
                return true;
            }
            return false;
        } catch (Exception e) {
            System.out.println("MOBIOTSEC Exception while checking pin");
            return false;
        }
    }

    public String toHexString(byte[] bytes) {
        StringBuilder hexString = new StringBuilder(); //create a new StringBuilder
        for (byte b : bytes) {                          // for each byte in bytes 
            String hex = Integer.toHexString(b & 255); // Integer.toHexString(byte & 255)
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
