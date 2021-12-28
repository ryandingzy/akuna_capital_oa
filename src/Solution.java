import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class Solution {

    // Hash Table
    private HashMap<String, Integer> map = new HashMap<>();
    public long watermark = 0l;

    private void insert(String key, int value) {
        if (!map.containsKey(key)) {
            map.put(key, value);
        }
    }

    private void update(String key, int value) {
        map.put(key, value);
    }

    private void delete(String key) {
        if (map.containsKey(key)) {
            map.remove(key);
        }
    }

    public void event(String[] evernts) {
        for (int i = 0; i < evernts.length; i++) {
            String[] strs = evernts[i].split("\\|");
            watermark = Long.valueOf(strs[0]);

            if (strs[1] == "INSERT") {
                insert(strs[2], Integer.valueOf(strs[3]));
            } else if (strs[1] == "UPSERT") {
                update(strs[2], Integer.valueOf(strs[3]));
            } else {
                delete(strs[2]);
            }
        }
    }

    // Parse
    ArrayList<String> res = new ArrayList<>();

    // byte to hex
    private static String bytesToHex(byte[] arr, int start, int end) {
        StringBuilder sb = new StringBuilder();
        for (int i = start; i < end; i++) {
            String hex = Integer.toHexString(arr[i] & 0xFF);
            if (hex.length() == 1) {
                sb.append("0");
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    // hex to dec
    private static String hexToDec(String str) {
        BigInteger data = new BigInteger(str, 16);
        return data.toString(10);
    }

    // byte to string
    private static String bytesToStr(byte[] arr, int start, int end) {
        byte[] bs = new byte[end - start];
        for (int i = 0; i < end - start; i++) {
            bs[i] = arr[i + start];
        }
        return new String(bs);
    }


    public void parse(byte[] input) {
        int start = 0;
        while (start < input.length) {
            StringBuilder sb = new StringBuilder();

            // epoch_milli
            String epoch = hexToDec(bytesToHex(input, start, start + 8));
            sb.append(epoch);
            start += 8;

            // message type
            int type = Integer.parseInt(hexToDec(bytesToHex(input, start, start + 1)));
            if (type == 0) {
                sb.append("|INSERT|");
            } else if (type == 1) {
                sb.append("|UPSERT|");
            } else {
                sb.append("|DELETE|");
            }
            start += 1;

            // key
            String strKeyNum = hexToDec(bytesToHex(input, start, start + 2));
            int keyNum = Integer.parseInt(strKeyNum);
            start += 2;

            String key = bytesToStr(input, start, start + keyNum);
            sb.append(key);
            start += keyNum;

            // value
            if (type != 2) {
                String strValueNum = hexToDec(bytesToHex(input, start, start + 2));
                int valueNum = Integer.parseInt(strValueNum);
                start += 2;

                String value = bytesToStr(input, start, start + valueNum);
                sb.append("|");
                sb.append(value);
                start += valueNum;
            }

            res.add(sb.toString());
        }
    }

    private static byte[] hex2bytes(String hex) {
        int len = hex.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4)
                    + Character.digit(hex.charAt(i+1), 16));
        }
        return data;
    }

    public void test() {
        String data = "0000016c052dcf4100000e746573745f6b65795f30393831320010746573745f76616c75655f3132383736";
        byte[] input = hex2bytes(data);

        parse(input);
        for (int i = 0; i < res.size(); i++) {
            System.out.println(res.get(i));
        }
    }

}
