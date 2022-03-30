class Main {
  public static void main(String[] args) {
        // String hex = "d04988522ddfed3133cc24fb6924eae9";
        // byte[] result = hexStringToByteArray(hex);
        // System.out.println(result);

    String pinCode = "700000";
    
    CheckPin mChecker = new CheckPin();
     boolean b;
    for (int i = 0; i< 1000000; i++){
      b = mChecker.checkPin(pinCode);
      if(b == true) 
        return;
      pinCode = String.format("%06d" , Integer.parseInt(pinCode)+1);
      System.out.println(pinCode);
    }
    

  }
}
