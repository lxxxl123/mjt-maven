//package chen.util.support;
//
//import cn.hutool.core.collection.CollUtil;
//import cn.hutool.core.map.MapBuilder;
//
//import java.util.HashMap;
//import java.util.LinkedList;
//import java.util.Map;
//import java.util.Set;
//
///**
// * @author chenwh3
// */
//public class CodeSpliter {
//
//    private String text;
//
//
//    /**
//     * 分割符号
//     */
//    private String split;
//
//    public static CodeSpliter of() {
//        return new CodeSpliter();
//    }
//
//
//    public CodeSpliter setSplit(String split){
//        this.split = split;
//        return this;
//    }
//
//    public CodeSpliter setText(String text){
//        this.text = text;
//        return this;
//    }
//
//    private int start;
//
//    public CodeSpliter setStart(int start){
//        this.start = start;
//        return this;
//    }
//
//
//
//    private static final Map<Character, Long> pairMatch = MapBuilder.create(new HashMap<Character, Long>())
//            .put('(', 0b1L)
//            .put(')', ~0b1L)
//            .put('{', 0b1L<<1)
//            .put('}', ~(0b1L<<1))
//            .put('[', 0b1L<<2)
//            .put(']', ~(0b1L<<2))
//            .build();
//
//    private static final Set<Character> pairSet = CollUtil.newHashSet('\'', '"');
//
//    public void find(){
//        LinkedList<Character> stack = new LinkedList<>();
//        /**
//         * 0 - 这个状态下遇到()会把它当成字符串
//         * 1 - 这个状态下遇到( 会尝试拆分
//         */
//        int state = 1;
//        for (int i = start; i < text.length(); i++) {
//            //找到与之匹配的右括号
//            char c = text.charAt(i);
//            if (stack.size() == 0 && true) {
//
//
//            }
//        }
//        return -1;
//    }
//
//    public static void main(String[] args) {
//        System.out.println((pairMatch.get('{') & pairMatch.get(')')));
//    }
//
//}
