package chen.util.support;

import chen.util.StringUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.lang.func.Func1;
import cn.hutool.core.lang.func.LambdaUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.keyvalue.MultiKey;
import org.apache.commons.collections4.map.MultiKeyMap;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.util.*;
import java.util.function.Consumer;

/**
 * 用于合并数据
 * @author chenwh3
 */
public class CombineTool<T,N,A> {

    Class<A> aimClazz = null;
    List<T> driveTable ;
    List<N> nonDriveTables ;
    List<Tuple2<String, String>> allEqCol = new ArrayList<>();

    /**
     * 判断是否把驱动表复制到目标对象 , 否则沿用原来的驱动表的对象
     */
    private boolean copyFlag;

    private boolean onlyTopOne = true;



    public CombineTool(List<T> driveTable, List<N> nonDriveTables, Class<A> aimClazz){
        this.driveTable = driveTable;
        this.nonDriveTables = nonDriveTables;
        this.aimClazz = aimClazz;
        copyFlag = true;
    }

    public CombineTool(List<T> driveTable, List<N> nonDriveTables){
        this.driveTable = driveTable;
        this.nonDriveTables = nonDriveTables;
        if (driveTable.size() > 0) {
            this.aimClazz = (Class<A>) driveTable.get(0).getClass();
        }
        copyFlag = false;
    }
    /**
     * 默认left join
     */
    private boolean innerJoin = false;

    /**
     * 根据{@link #allEqCol}的数量 和 {@link #nonDriveTables} 生成n层索引 ;
     */
    private MultiKeyMap<Object, List<N>> index ;

    private CopyOptions copyOptions = new CopyOptions();

    private boolean trimKey = true;



    public CombineTool<T, N, A> onlyTopOne(boolean onlyTopOne) {
        this.onlyTopOne = onlyTopOne;
        return this;
    }
    public CombineTool<T, N, A> eq(Func1<T, ?> f1, Func1<N, ?> f2) {
        String fieldName1 = LambdaUtil.getFieldName(f1);
        String fieldName2 = LambdaUtil.getFieldName(f2);
        allEqCol.add(Tuples.of(fieldName1, fieldName2));
        return this;
    }

    public CombineTool<T, N, A> innerJoin(boolean val) {
        this.innerJoin = val;
        return this;
    }

    private Map<String, String> fieldNameMap = new HashMap<>();

    public CombineTool<T, N, A> fileNameMapping(String key, String value) {
        fieldNameMap.put(key, value);
        return this;
    }


    private MultiKey getKey(Object map, int index) {
        Object[] keys = new Object[allEqCol.size()];
        for (int i = 0; i < allEqCol.size(); i++) {
            Object key = ReflectUtil.getFieldValue(map, (String) allEqCol.get(i).get(index));
            if (trimKey && key instanceof String) {
                key = StringUtil.toNotNullStr(key);
            }
            keys[i] = key;
        }
        return new MultiKey(keys);
    }

    private MultiKey getRightKey(Object map) {
        return getKey(map, 1);
    }
    private MultiKey getLeftKey(Object map) {
        return getKey(map, 0);
    }

    /**
     * 使用索引快速找到对应的非驱动表相关的行
     */
    private List<N> getByIndex(Object row) {
        MultiKey key = getLeftKey(row);
        return index.get(key);
    }

    protected void buildIndex() {
        MultiKeyMap<Object, List<N>> indexMap = new MultiKeyMap<>();
        for (N map : nonDriveTables) {
            indexMap.computeIfAbsent(getRightKey(map), e -> new ArrayList<>())
                    .add(map);
        }
        index = indexMap;
    }


    private Consumer<A> afterMerge;

    private Consumer<A> forEachNoJoinRow;

    public CombineTool<T,N,A> afterMerge(Consumer<A> afterMerge){
        this.afterMerge = afterMerge;
        return this;
    }

    public CombineTool<T,N,A> forEachNoJoinRow(Consumer<A> forEachNoJoinRow){
        this.forEachNoJoinRow = forEachNoJoinRow;
        return this;
    }


    /**
     * 忽略非驱动表空值 , 避免覆盖主表有数据的值
     */
    public CombineTool<T,N,A> ignoreBlank(){
        this.copyOptions.setPropertiesFilter((field,val)->{
            if (val == null || val instanceof String && StrUtil.isBlank((String) val)) {
                return false;
            }
            return true;
        });
        return this;
    }

    public CombineTool<T, N, A> ignorePropertys(String...propertys) {
        this.copyOptions.setIgnoreProperties(propertys);
        return this;
    }

    public final List<A> combine() {
        if (CollUtil.isNotEmpty(fieldNameMap)) {
            copyOptions.setFieldMapping(fieldNameMap);
        }
        if (CollectionUtils.isEmpty(driveTable)  || CollectionUtils.isEmpty(allEqCol)) {
            return Collections.emptyList();
        }
        //对非驱动表建立索引
        buildIndex();

        List<A> res = new ArrayList<>();

        //遍历驱动表
        for (T driveRow : driveTable) {
            //查找对应的非驱动表数据
            List<N> nonDriveRows = getByIndex(driveRow);
            if (CollectionUtils.isEmpty(nonDriveRows)) {
                if (!innerJoin) {
                    res.addAll(buildRow(driveRow, null));
                }
            } else {
                //根据找到的数据拼接
                res.addAll(buildRow(driveRow, nonDriveRows));
            }
        }

        return res;
    }

    /**
     * 合并数据
     * 会合并同键的值 , 如有需要可继承定制
     */
    protected List<A> buildRow(T driveRow, List<N> nonDriveRows) {
        if (CollUtil.isEmpty(nonDriveRows)) {
            A res = null;
            if (!copyFlag) {
                res = (A) driveRow;
            } else{
                res = BeanUtil.toBean(driveRow, aimClazz);
            }
            if (forEachNoJoinRow != null) {
                forEachNoJoinRow.accept(res);

            }
            return ListUtil.of(res);
        }
        List<A> res = new ArrayList<>();
        for (N nonDriveRow : nonDriveRows) {
            A copy ;
            if (!copyFlag && aimClazz.isAssignableFrom(driveRow.getClass())) {
                copy = (A)driveRow;
            } else {
                copy = BeanUtil.toBean(driveRow, aimClazz);
            }
            BeanUtil.copyProperties(nonDriveRow, copy, copyOptions);
            if (afterMerge != null) {
                afterMerge.accept(copy);
            }
            res.add(copy);
            if (onlyTopOne || !copyFlag) {
                break;
            }

        }
        return res;
    }

}
