package chen.struct.tree;


/**
 * 前缀树
 * 对比hash的优点 , 可以动态查询 , 即查prefix
 * 时间复杂度 logN
 *
 */
public class Trie {

    int count;
    int prefix;
    private Trie[] nextNode = new Trie[26];

    private void insert(String s){
        if (s == null || s.length() == 0) {
            return;
        }
        Trie cur = this;
        char[] chars = s.toCharArray();
        for (char c : chars) {
            int idx = c - 'a';
            if (cur.nextNode[idx] == null) {
                cur.nextNode[idx] = new Trie();
            }
            cur = cur.nextNode[idx];
            cur.prefix++;
        }
        cur.count++;
    }

    private boolean search(String s){
        char[] chars = s.toCharArray();
        Trie cur = this;
        for (char c: chars) {
            int idx = c - 'a';
            if (cur.nextNode[idx] == null || cur.nextNode[idx].prefix == 0) {
                return false;
            }
            cur = cur.nextNode[idx];
        }
        if (cur.count == 0) {
            return false;
        }
        return true;
    }


    private boolean searchPrefix(String s){
        char[] chars = s.toCharArray();
        Trie cur = this;
        for (char c: chars) {
            int idx = c - 'a';
            if (cur.nextNode[idx] == null || cur.nextNode[idx].prefix == 0) {
                return false;
            }
            cur = cur.nextNode[idx];
        }
        return true;
    }


    public static void main(String[] args) {
        System.out.println(~(10-1));
    }

}
