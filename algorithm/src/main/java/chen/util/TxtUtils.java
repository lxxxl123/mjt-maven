package chen.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringSubstitutor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TxtUtils {

    public static List<Map<String, String>> getAttr(String s, Pattern pattern, String... attr) {
        Matcher matcher = pattern.matcher(s);
        List<Map<String, String>> list = new ArrayList<>();
        while (matcher.find()) {
            Map<String, String> map = new HashMap<>();
            for (String a : attr) {
                map.put(a, matcher.group(a));
            }
            list.add(map);
        }
        return list;
    }

    public static String pullute(String content, List<Map<String, String>> list) {
        StringBuffer sb = new StringBuffer();
        int i = 0;
        for (Map<String, String> map : list) {
            map.put("index", String.valueOf(i++));
            StringSubstitutor sub = new StringSubstitutor(map);
            sb.append(sub.replace(content) + "\n");
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        List<Map<String, String>> attr = getAttr("        { field: 'complainYear', headerAlign: 'center', width: 100, title: '投诉年份' },\n" +
                "          { field: 'complainMonth', headerAlign: 'center', width: 100, title: '投诉月份' },\n" +
                "          { field: 'feedbackTime', headerAlign: 'center', width: 100, title: '反馈日期' },\n" +
                "          { field: 'productName', headerAlign: 'center', width: 100, title: '成品名称' },\n" +
                "          { field: 'productType', headerAlign: 'center', width: 100, title: '产品类别' },\n" +
                "          { field: 'sysCode', headerAlign: 'center', width: 100, title: '系统编码' },\n" +
                "          { field: 'complainantAttr', headerAlign: 'center', width: 100, title: '投诉人属性' },\n" +
                "          { field: 'handler', headerAlign: 'center', width: 130, title: '投诉信息跟进人' },\n" +
                "          { field: 'complainCategory', headerAlign: 'center', width: 100, title: '投诉大类' },\n" +
                "          { field: 'complainType', headerAlign: 'center', width: 100, title: '投诉类别' },\n" +
                "          { field: 'keyword', headerAlign: 'center', width: 130, title: '投诉案关键词' },\n" +
                "          { field: 'charg', headerAlign: 'center', width: 100, title: '批次信息' },\n" +
                "          { field: 'productTime', headerAlign: 'center', width: 100, title: '生产日期' },\n" +
                "          { field: 'modelSpec', headerAlign: 'center', width: 100, title: '规格' },\n" +
                "          { field: 'content', headerAlign: 'center', width: 100, title: '内容' },\n" +
                "          { field: 'province', headerAlign: 'center', width: 100, title: '省份' },\n" +
                "          { field: 'location', headerAlign: 'center', width: 100, title: '地市' },\n" +
                "          { field: 'remark', headerAlign: 'center', width: 100, title: '备注' },\n" +
                "          { field: 'instruction', headerAlign: 'center', width: 100, title: '补充说明' },\n" +
                "          { field: 'busiDepartment', headerAlign: 'center', width: 100, title: '事业部' },\n" +
                "          { field: 'base', headerAlign: 'center', width: 100, title: '基地' }", Pattern.compile("field:\\s+'(?<field>\\w+)'.+?title:\\s+'(?<title>[^']+?)'"), "field", "title");
        System.out.println(attr);
/*
        System.out.println(pullute("\t<vxe-form-item field=\"${field}\" title=\"${title}\" :span=\"12\" :item-render=\"{}\">\n" +
                "    <template #default=\"{ data }\">\n" +
                "      <vxe-input v-model=\"data.${field}\" ></vxe-input>\n" +
                "    </template>\n" +
                "  </vxe-form-item>", attr));
*/
//        System.out.println(pullute("${field}:'',", attr));
        System.out.println(pullute("${field}: this.$trim(arr[${index}]),", attr));

    }





}
