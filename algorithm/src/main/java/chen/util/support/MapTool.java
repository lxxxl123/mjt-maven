package chen.util.support;

import lombok.Getter;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.keyvalue.MultiKey;
import org.apache.commons.collections4.map.MultiKeyMap;

import java.util.*;


/**
 * 对非驱动表建立索引 ， 遍历驱动表 ， 根据索引找到对应的数据 ， 然后拼接数据
 */
public class MapTool {

    public MapTool(List<Map<String, Object>> driveTable, List<Map<String, Object>> nonDriveTables, List<String> allEqCol) {
        this.driveTable = driveTable;
        this.nonDriveTables = nonDriveTables;
        this.allEqCol = allEqCol;
    }


    /**
     * 默认left join
     */
    private boolean innerJoin = false;

    /**
     *  inner join = true 开启的情况下回记录驱动表合并失败的行
     */
    private boolean recordJoinFailRow = false;

    /**
     * 记录没有用上的非驱动表行
     */
    private boolean recordNoUseNonDriveRow = false;

    public MapTool innerJoin(boolean val) {
        this.innerJoin = val;
        return this;
    }

    public MapTool recordJoinFailRow(boolean val) {
        this.recordJoinFailRow = val;
        return this;
    }

    public MapTool recordNoUseNonDriveRow(boolean val) {
        this.recordNoUseNonDriveRow = val;
        return this;
    }

    public MapTool onlyTopOne(boolean val) {
        this.onlyTopOne = val;
        return this;
    }

    /**
     * 驱动表
     */
    private List<Map<String,Object>> driveTable;

    /**
     * 非驱动表
     */
    private List<Map<String,Object>> nonDriveTables;


    /**
     * 根据多列进行合并
     */
    private List<String> allEqCol ;

    private boolean onlyTopOne = false;

    /**
     * 合并失败的列表
     */
    @Getter
    private List<Map<String, Object>> noJoinRows = new ArrayList<>();

    /**
     * 根据{@link #allEqCol}的数量 和 {@link #nonDriveTables} 生成n层索引 ;
     * 索引树
     * 一层索引结构如下: map<string,list>
     * 二层索引结构如下: map<string,map<string,list>>
     */
    private MultiKeyMap<Object, List<Map<String,Object>>> index ;


    public final List<Map<String, Object>> combine() {
        if (CollectionUtils.isEmpty(driveTable) || CollectionUtils.isEmpty(nonDriveTables) || CollectionUtils.isEmpty(allEqCol)) {
            if (innerJoin) {
                if (recordJoinFailRow) {
                    this.noJoinRows = driveTable;
                }
                return new ArrayList<>();
            }else{
                return driveTable;
            }
        }
        //对非驱动表建立索引
        buildIndex();

        List<Map<String, Object>> res = new ArrayList<>();

        //遍历驱动表
        for (Map<String, Object> driveRow : driveTable) {
            //查找对应的非驱动表数据
            List<Map<String, Object>> nonDriveRows = getByIndex(driveRow);
            if (CollectionUtils.isEmpty(nonDriveRows)) {
                if (!innerJoin) {
                    res.add(driveRow);
                }
                if (recordJoinFailRow) {
                    noJoinRows.add(driveRow);
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
    protected List<Map<String,Object>> buildRow(Map<String, Object> driveRow, List<Map<String, Object>> nonDriveRows) {
        List<Map<String, Object>> res = new ArrayList<>();
        for (Map<String, Object> nonDriveRow : nonDriveRows) {
            HashMap<String, Object> row = new HashMap<>(nonDriveRow.size() + driveRow.size());
            row.putAll(nonDriveRow);
            row.putAll(driveRow);
            res.add(row);
            if (onlyTopOne) {
                break;
            }
        }
        return res;
    }

    /**
     * 使用索引快速找到对应的非驱动表相关的行
     */
    private List<Map<String, Object>> getByIndex(Map<String,Object> row) {
        MultiKey key = getKey(row);
        if (recordNoUseNonDriveRow) {
            usedKeys.add(key);
        }
        return index.get(key);
    }

    /**
     * 获取没有使用过的非驱动表
     */
    public List<Map<String, Object>> getNoUseNonDriveTable() {
        List<Map<String, Object>> list = new ArrayList<>();

        for (Map<String, Object> nonDriveRow : nonDriveTables) {
            MultiKey key = getKey(nonDriveRow);
            if (!usedKeys.contains(key)) {
                list.add(nonDriveRow);
            }
        }
        return list;
    }

    private Set<MultiKey> usedKeys = new HashSet<>();


    private MultiKey getKey(Map<String, Object> map) {
        List<Object> list = new ArrayList<>();
        for (String col : allEqCol) {
            list.add(map.get(col));
        }
        return new MultiKey(list.toArray(new Object[]{}));
    }
    /**
     * 创建索引
     */
    protected void buildIndex() {
        MultiKeyMap<Object, List<Map<String,Object>>> indexMap = new MultiKeyMap<>();
        for (Map<String, Object> map : nonDriveTables) {
            indexMap.computeIfAbsent(getKey(map), e -> new ArrayList<>())
                    .add(map);
        }
        index = indexMap;
    }


}
