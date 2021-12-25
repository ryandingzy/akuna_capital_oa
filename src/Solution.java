import java.util.HashMap;

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


}
