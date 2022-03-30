public static boolean checkFlag(Context ctx, String flag) {
        if (!flag.startsWith("FLAG{") || !flag.endsWith("}")) {   
            
                                                         // FLAG{             }

        String core = flag.substring(5, 31); 
        Log.e(TAG, core);
        if (core.length() != 26) {
                                                         // FLAG{XXXXXXXXXXXXXXXXXXXXXXXXXXX} there are 26 letters
            return false;
        }

        String[] ps = core.split(foo()); 
        public static String foo() {
        String s = "Vm0wd2QyVkZNVWRYV0docFVtMVNWVmx0ZEhkVlZscDBUVlpPVmsxWGVIbFdiVFZyVm0xS1IyTkliRmRXTTFKTVZsVmFWMVpWTVVWaGVqQTk=";
        for (int i = 0; i < 10; i++) {
            s = new String(Base64.decode(s, 0));
        }
        return s;
    }

                                                        // all this stuff return a symbol "-", so we can suppose that there is at least a symbol -



        if (ps.length != 4) {
            Log.e(TAG, "Bad section amount");
            return false;
        }
                                                        // 26/4 there is 4 substring divided by symbol -




        if (!bim(ps[0]) || !bum(ps[2]) || !bam(ps[3])) {
            Log.e(TAG, "Bad capitalization and/or numbers");
        } else if (!core.replaceAll("[A-Z]", "X").replaceAll("[a-z]", "x").replaceAll("[0-9]", " ").matches("[a-z]+.[a-z]+.[X ]+.  xXx    +")) {
            Log.e(TAG, "Bad formatting");
            return false;
        }

                    private static boolean bim(String s) {
                        return s.matches("^[a-z]+$");
                    }

                    private static boolean bum(String s) {
                        return s.matches("^[A-Z]+$");
                    }

                    private static boolean bam(String s) {
                        return s.matches("^[a-zA-Z0-9]+$");
                    }


                                                    // FLAG{XXXXXXXXXXXXXXXXXXXXXXXXXXX} 
                                                    //    first part is lowercase 
                                                    //    second part I don't know
                                                    //    third part is uppercase
                                                    //    fourth part is mix lowercase, uppercase and number




        char[] syms = new char[3]; // array of 3 char
        int[] idxs = {8, 15, 21};   // array of int to represent indexes   --> 135 is divided by 3 and 135:3 = 45 that in ascii is "-" 
                                    //  //  FLAG{XXX-XXXXXX-XXXXX-XXXXXXXXX}
                                    //      FLAG{xxx-xxxxxx-XXXXX-  xXx    }


        Set<Character> chars = new HashSet<>();             // HashSet of Characters



        for (int i = 0; i < syms.length; i++) {             //execute three times
            syms[i] = flag.charAt(idxs[i]);                     // put the value of characters of the flag in position 8, 15 and 21 in syms
            chars.add(Character.valueOf(syms[i]));              // add this characters in chars[]
        }       



        int sum = 0;                                           // sum = 0
        for (char c : syms) {
            sum += c;
        }
        if (sum != 135 || chars.size() != 1) {                  // the sum of the value of this thre carchers is 135
            Log.e(TAG, "Bad separators");
        } 
         else if (!me(ctx, dh(gs(ctx.getString(R.string.ct1), ctx.getString(R.string.k1)), ps[0]), ctx.getString(R.string.t1)) || !me(ctx, dh(gs(ctx.getString(R.string.ct2), ctx.getString(R.string.k2)), ps[1]), ctx.getString(R.string.t2)) || !me(ctx, dh(gs(ctx.getString(R.string.ct3), ctx.getString(R.string.k3)), ps[2]), ctx.getString(R.string.t3)) || !me(ctx, dh(gs(ctx.getString(R.string.ct4), ctx.getString(R.string.k4)), ps[3]), ctx.getString(R.string.t4))) {
        //     Log.e(TAG, "Bad flag section value");
         } 
         else if (me(ctx, dh(gs(ctx.getString(R.string.ct5), ctx.getString(R.string.k5)), flag), ctx.getString(R.string.t5))) { //correct flag
            return true;
        } 
        // else {
        //     Log.e(TAG, "Bad flag value");
        // }

        // return false;
    }


    public static final int ct5 = 2131492896;
   //<string name="ct5">70 IJTR</string>

    public static final int k5 = 2131492902
    //<string name="k5">dxa</string>



    // this is an hash
    public static final int t5 = 2131492910;
    //<string name="t5">1b8f972f3aace5cf0107cca2cd4bdb3160293c97a9f1284e5dbc440c2aa7e5a2</string>


    public static final int m1 ;
    //<string name="m1">slauqe</string>


                        
                        // this is a part of the code  //this is a part of the code
                                                                                        // this is an hash
        me(ctx, dh(gs(ctx.getString(R.string.ct5), ctx.getString(R.string.k5)), flag), ctx.getString(R.string.t5))


     // me -> check if two hash are equals
        me = stringCompare(context, string, string)
                    private static boolean me(Context ctx, String s1, String s2) {
                        Log.e(TAG, "s1: " + s1 + " s2: " + s2);
                        try {                                                         // "slauqe" 
                            return ((Boolean) s1.getClass().getMethod(r(ctx.getString(R.string.m1)), Object.class).invoke(s1, s2)).booleanValue();
                                                                        // r_reverse_the_string(slauqe) --> equals

                        } catch (Exception e) {
                            Log.e(TAG, "Exception: " + Log.getStackTraceString(e));
                            return false;
                        }
                    }

                    r = reverse_the_string()
                    public static String r(String s) {
                        return new StringBuffer(s).reverse().toString(); 
                    }

    // dh --> create an hash given algorithm and string
        dh =  dh(hash-typology, string)
                    private static String dh(String hash, String s) {
                        try {
                            MessageDigest md = MessageDigest.getInstance(hash);
                            md.update(s.getBytes());
                            return toHexString(md.digest()); // return the hash
                        } catch (Exception e) {
                            return null;
                        }
                    }


                    private static String toHexString(byte[] bytes) {
                        StringBuilder hexString = new StringBuilder();
                        for (byte b : bytes) {
                            String hex = Integer.toHexString(b & 255);
                            if (hex.length() == 1) {
                                hexString.append('0');
                            }
                            hexString.append(hex);
                        }
                        return hexString.toString();
                    }




       // gs --> recover the string     --> return string literally: "SHA-256"
                        // 70 IJTR                //dxa
        gs = gs(ctx.getString(R.string.ct5), ctx.getString(R.string.k5))

                    private static String gs(String a, String b) {
                        String s = "";
                        for (int i = 0; i < a.length(); i++) {
                            s = s + Character.toString((char) (a.charAt(i) ^ b.charAt(i % b.length())));
                        }
                        return s;
                    }




    // <string name="ct1">xwe</string>
    // <string name="ct2">asd</string>
    // <string name="ct3">uyt</string>
    // <string name="ct4">42s</string>
    // <string name="ct5">70 IJTR</string>
    // <string name="flag" />
    // <string name="k1">53P</string>
    // <string name="k2">,7Q</string>
    // <string name="k3">8=A</string>
    // <string name="k4">yvF</string>
    // <string name="k5">dxa</string>
    // <string name="m1">slauqe</string>
    // <string name="search_menu_title">Search</string>
    // <string name="status_bar_notification_info_overflow">999+</string>
    // <string name="t1">82f5c1c9be89c68344d5c6bcf404c804</string>
    // <string name="t2">e86d706732c0578713b5a2eed1e6fb81</string>
    // <string name="t3">7ff1301675eb857f345614f9d9e47c89</string>
    // <string name="t4">b446830c23bf4d49d64a5c753b35df9a</string>

    // me = stringCompare()
    // dh = hash(algorithm=sha2, string_to_hash)
    // gs = return somethin, maybe the value of the algorithm of hashing



 //  //  FLAG{XXX-XXXXXX-XXXXX-XXXXXXXXX}
 //      FLAG{xxx-xxxxxx-XXXXX-  xXx    }
                        // else if (!core.replaceAll("[A-Z]", "X").replaceAll("[a-z]", "x").replaceAll("[0-9]", " ").matches("[a-z]+.[a-z]+.[X ]+.  xXx    +")) {
                        // [a-z]+.[a-z]+.[X ]+.  xXx    +
                        /*
                                            Match a single character present in the list below [a-z]
                                            + matches the previous token between one and unlimited times, as many times as possible, giving back as needed (greedy)
                                            a-z matches a single character in the range between a (index 97) and z (index 122) (case sensitive)
. is for indicate "-" separator                  . matches any character (except for line terminators)
                                            Match a single character present in the list below [a-z]
                                            + matches the previous token between one and unlimited times, as many times as possible, giving back as needed (greedy)
                                            a-z matches a single character in the range between a (index 97) and z (index 122) (case sensitive)
                                       Candidates.#1....: 91gJw8081 -> 86yRr5081
     . matches any character (except for line terminators)
                                            Match a single character present in the list below [X ]
                                            + matches the previous token between one and unlimited times, as many times as possible, giving back as needed (greedy)
                                            X 
                                            matches a single character in the list X (case sensitive)
                                            . matches any character (except for line terminators)
                                            xXx   
                                            matches the characters   xXx    literally (case sensitive)
                                            
                                            matches the character   with index 3210 (2016 or 408) literally (case sensitive)
                                            Global pattern flags
                                            g modifier: global. All matches (don't return after first match)
                                            m modifier: multi line. Causes ^ and $ to match the begin/end of each line (not only begin/end of string) */


                            // hashcat -m 0 -a 3 82f5c1c9be89c68344d5c6bcf404c804 ?l?l?l
                            // 82f5c1c9be89c68344d5c6bcf404c804:sic
    gs(xwe 53P) --> MD5 --> 82f5c1c9be89c68344d5c6bcf404c804 --> sic
if (!me(ctx, dh(gs(ctx.getString(R.string.ct1), ctx.getString(R.string.k1)), ps[0]), ctx.getString(R.string.t1)) || 

                            // hashcat -m 0 -a 3 e86d706732c0578713b5a2eed1e6fb81 ?l?l?l?l?l?l
                            // e86d706732c0578713b5a2eed1e6fb81:parvis     
    gs(asd ,7Q) --> MD5 -->  e86d706732c0578713b5a2eed1e6fb81 --> parvis
    !me(ctx, dh(gs(ctx.getString(R.string.ct2), ctx.getString(R.string.k2)), ps[1]), ctx.getString(R.string.t2)) || 
    
                            // hashcat -m 0 -a 3 7ff1301675eb857f345614f9d9e47c89 ?u?u?u?u?u
                            // 7ff1301675eb857f345614f9d9e47c89:MAGNA 
    gs(asd 8=A) --> MD5 -->7ff1301675eb857f345614f9d9e47c89 --> MAGNA
    !me(ctx, dh(gs(ctx.getString(R.string.ct3), ctx.getString(R.string.k3)), ps[2]), ctx.getString(R.string.t3)) || 
    
                            // hashcat -m 0 -a 3 b446830c23bf4d49d64a5c753b35df9a ?d?d?l?u?l?d?d?d?d
                            // b446830c23bf4d49d64a5c753b35df9a:28jAn1596
    fs(42s yvF) --> MD5 --> b446830c23bf4d49d64a5c753b35df9a --> 
    !me(ctx, dh(gs(ctx.getString(R.string.ct4), ctx.getString(R.string.k4)), ps[3]), ctx.getString(R.string.t4))) {



        /*Built-in charsets
                    ?l = abcdefghijklmnopqrstuvwxyz
                    ?u = ABCDEFGHIJKLMNOPQRSTUVWXYZ
                    ?d = 0123456789
                    ?h = 0123456789abcdef
                    ?H = 0123456789ABCDEF
                    ?s =  !"#$%&'()*+,-./:;<=>?@[]^_`{|}~
                    ?a = ?l?u?d?s
                    ?b = 0x00 - 0xff
        */






    // SHA256("FLAG{sic-parvis-MAGNA-28jAn1596}")--> 1b8f972f3aace5cf0107cca2cd4bdb3160293c97a9f1284e5dbc440c2aa7e5a21b8f972f3aace5cf0107cca2cd4bdb3160293c97a9f1284e5dbc440c2aa7e5a2

      // this is an hash
      public static final int t5 = 2131492910;
      //<string name="t5">1b8f972f3aace5cf0107cca2cd4bdb3160293c97a9f1284e5dbc440c2aa7e5a2</string>