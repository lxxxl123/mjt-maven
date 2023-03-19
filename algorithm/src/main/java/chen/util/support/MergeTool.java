package chen.util.support;

import chen.util.DateUtils;
import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.lang.func.Func1;
import cn.hutool.core.lang.func.LambdaUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author chenwh3
 */
public class MergeTool<T, E> {

    private String tableName;
    private List<E> data;

    private String tableAlias = "t";

    private String listAlias = "l";

    private List<Cond> onConds;


    private class MatchThen {

        boolean byTarget = true;

        boolean whenMatch = true;

        private List<Cond> matchConds;

        public void update(){

        }
    }

    private class NotMatchByTarget {

        private List<Cond> matchConds;

        public void insert(){

        }

    }

    private class NotMatchBySource {

        private List<Cond> matchConds;

        public void delete(){

        }

    }




    private class Cond{
        String left;
        String right;
        private Cond(String left, String right) {
            this.left = left;
            this.right = right;
        }

        @Override
        public String toString() {
            return StrUtil.format("{}.{} = {}.{}", MergeTool.this.tableAlias, left, MergeTool.this.listAlias, right);
        }
    }

    private class LeftConf extends Cond {

        private LeftConf(String left, String right) {
            super(left, right);
        }

        @Override
        public String toString() {
            return StrUtil.format("{}.{} = '{}'", MergeTool.this.tableAlias, left, right);
        }
    }

    private class RightConf extends Cond{
        private RightConf(String left, String right) {
            super(left, right);
        }

        @Override
        public String toString() {
            return StrUtil.format("{}.{} = '{}'", MergeTool.this.listAlias, left, right);
        }
    }



    MergeTool(Class<T> clazz, List<E> list) {
        TableName anno = AnnotationUtil.getAnnotation(clazz, TableName.class);
        this.tableName = anno.value();
        this.data = list;
    }

    /**
     * 用于列相同情况
     */
    public MergeTool<T, E> on(String... column) {
        return this;
    }

    public MergeTool<T, E> on(Consumer<MergeTool<T, E>> consumer) {
        consumer.accept(this);
        return this;
    }


    public MergeTool<T, E> eq(Func1<T, ?> t1, Func1<E, ?> t2) {
        String f1 = LambdaUtil.getFieldName(t1);
        String f2 = LambdaUtil.getFieldName(t2);
        onConds.add(new Cond(f1, f2));
        return this;
    }

    public MergeTool<T, E> eq(Func1<T, ?> t1, String v) {
        String f1 = LambdaUtil.getFieldName(t1);
        onConds.add(new LeftConf(f1, v));
        return this;
    }

    private String cols;

    private String getTableFieldName(Field field){
        TableField tField = (TableField) AnnotationUtil.getAnnotation(field, TableField.class);
        if (field != null && StringUtils.isNotEmpty(tField.value())) {
            return tField.value();
        }
        return field.getName();
    }

    private String objToStr(Object right){
        if (right instanceof String) {
            return StrUtil.format("'{}'", right);
        }
        else if (right instanceof Date){
            return StrUtil.format("'{}'", DateUtils.formatDate((Date) right));
        }
        else if (right instanceof Number){
            return StrUtil.format("{}", right);
        }
        return "null";
    }

    public MergeTool<T,E> selectAll(){
        List<String> cols = new ArrayList<>();
        for (E datum : data) {
            Field[] fields = ReflectUtil.getFields(datum.getClass());
            List<String> row = new ArrayList<>();
            for (Field field : fields) {
                String right = getTableFieldName(field);
                Object left = ReflectUtil.getFieldValue(datum, field);
                row.add(StrUtil.format("{} as {}", objToStr(left), right));
            }
            cols.add("select " + StringUtils.join(row, ","));
        }
        this.cols = StringUtils.join(cols, " union ");
        return this;

    }

    public void execute(){

    }

    private String matchSql = "";

    public MergeTool<T,E> whenMatchThen(Consumer<Merge<T,E>> consumer){
        Merge<T, E> merge = new Merge<>();
        consumer.accept(merge);
        this.matchSql = "when matched and " + merge ;
        return this;
    }

    public MergeTool<T,E> whenNoMatchThen(Consumer<Merge<T,E>> consumer){
        return this;
    }

    public MergeTool<T,E> whenNoMatchBySourceThen(Consumer<Merge<T,E>> consumer){
        return this;
    }

    private class Merge<T, E> {

        String onSql = "";

        public void onT(Func1<E, ?> func1, String val) {

        }
        public void onS(Func1<T,?> func1 , String val){

        }

        public void onSrNotBlank(Func1<T, ?> func1) {

        }

        public void onTaNotBlank(Func1<T, ?> func1) {

        }


        public void update(Func1<T, ?> ...t1) {
        }

        public void insert(Func1<T, ?> ...t1){
        }
        public void insertAll(){
        }
        public void delete(Func1<T, ?> ...t1){
        }
    }



//    public static void main(String[] args) {
//        ArrayList<ReserveRate> list = new ArrayList<>();
//        new MergeTool<>(ReserveRate.class, list)
//                .selectAll()
//                .on(e->{
//                    e.eq(ReserveRate::getWerk, ReserveRate::getWerk);
//                    e.eq(ReserveRate::getWerk, ReserveRate::getWerk);
//                })
//                .whenMatchThen(e->{
//                    e.update();
//                })
//                .whenNoMatchThen(e->{
//                    e.insertAll();
//                }).whenNoMatchBySourceThen(e->{
//                    e.delete();
//                }).
//                execute();
//    }


}
