package chen.util;

import lombok.Data;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Data
public class Table {

    public Map<String, Integer> cols;
    public List<List<Object>> rows;

    public Table (List<String> list){
        Map<String, Integer> map = new HashMap();
        for (int i = 0; i < list.size(); i++) {
            map.put(list.get(i), i);
        }
        cols = map;
        rows = new ArrayList<>();
    }

    public Table (Map<String, Integer> cols){
        cols = cols;
        rows = new ArrayList<>();
    }

    public Table(){}

    public static Table of(List<Map<String, Object>> list) {
        Table table = new Table();
        Map<String, Integer> cols = new HashMap<>();
        List<List<Object>> rows = new ArrayList<>();

        for (Map<String, Object> map : list) {
            int i = 0;
            List<Object> row = new ArrayList<>();
            row.addAll(map.values());
            if (cols.size() == 0) {
                for (String key : map.keySet()) {
                    cols.put(key, i++);
                }
            }
            rows.add(row);
        }
        table.setCols(cols);
        table.setRows(rows);
        return table;
    }


    public List<String> getColsList(){
        return Arrays.asList(getColsArr());
    }

    public String[] getColsArr(){
        String[] arr = new String[cols.size()];
        for (Map.Entry<String, Integer> entry : cols.entrySet()) {
            arr[entry.getValue()] = entry.getKey();
        }
        return arr;
    }



    private List<String> buildKey(int rowIndex, List<String> groupBy) {
        ArrayList<String> res = new ArrayList<>(groupBy.size());
        for (int j = 0; j < groupBy.size(); j++) {
            res.add(this.getNotNullStr(rowIndex, groupBy.get(j)));
        }
        return res;
    }


    /**
     * 合并行 , 并且并把某列值转列 , 列值需要唯一
     */
    public Table colToCols(String oriCol, List<String> newCols, String defaultVal, Function<Object, String> valueToColName, Function<List<Object>, Object> newValue, String... groupByCols) {
        HashMap<List<String>, Integer> rowMap = new HashMap<>();
        List<String> cols = Arrays.asList(groupByCols);

        cols.addAll(newCols);
        Table table = new Table(cols);

        for (int i = 0; i < rows.size(); i++) {
            List<String> keys = buildKey(i, Arrays.asList(groupByCols));
            if (!rowMap.containsKey(keys)) {
                List<Object> row = new ArrayList<>();
                for (String s : groupByCols) {
                    row.add(get(i, s));
                }
                for (String ignored : newCols) {
                    row.add(get(i, defaultVal));
                }
                rowMap.put(keys, table.getRowSize());
                table.addRow(row);
            }
            Integer newRowIndex = rowMap.get(keys);
            Object val = get(i, oriCol);
            String newCol = valueToColName.apply(val);

            Object newVal = newValue.apply(rows.get(i));
            table.set(newRowIndex, newCol, newVal);
        }
        return table;
    }

    public final static  Function<List<Object>, Object> add = list -> list.stream().mapToInt(e -> (Integer) e).sum();
    public final static Function<List<Object>, Object> doubleAdd = list -> list.stream().map(e -> new BigDecimal(e.toString())).reduce(new BigDecimal(0), BigDecimal::add);

    public List<Object> mergeRow(List<Integer> rowIndex, List<String> columnNames, Function<List<Object>, Object> func) {
        List<Object> res;
        res = cloneRow(rowIndex.get(0));
        for (String columnName : columnNames) {
            List<Object> list = new ArrayList<>(rowIndex.size());
            for (int i = 0; i < rowIndex.size(); i++) {
                list.add(get(i, columnName));
            }
            Object apply = func.apply(list);
            setRow(res, columnName, apply);
        }
        return res;
    }

    public List<Map<String,Object>> toMap(){
        List<Map<String, Object>> list = new ArrayList<>(rows.get(0).size());
        for (List<Object> row : rows) {
            Map<String, Object> map = new HashMap<>();
            for (Map.Entry<String, Integer> entry : cols.entrySet()) {
                map.put(entry.getKey(), row.get(entry.getValue()));
            }
            list.add(map);
        }
        return list;
    }


    public void addCol(String colName, String defaultVal){
        this.cols.put(colName, cols.size());
        for (List<Object> row : rows) {
            row.add(defaultVal);
        }
    }



    public void insertRow(List<Object> list ,int i){
        this.rows.add(i, list);

    }



    public List<Object> cloneRow(int index) {
        List<Object> list = rows.get(index);
        return new ArrayList<>(list);
    }


    public int getRowSize() {
        return rows.size();
    }

    public List<Object> getRow(int rowIndex) {
        return rows.get(rowIndex);
    }

    public void set(int rowI, int colI, Object val) {
        rows.get(rowI).set(colI, val);
    }

    public void set(int rowI, String col, Object val) {
        Integer ci = cols.get(col);
        if (ci == null) {
            return;
        }
        rows.get(rowI).set(ci, val);
    }

    public void setRow(List<Object> list, String key, Object val) {
        Integer ci = cols.get(key);
        if (ci == null) {
            return;
        }
        list.set(ci, val);
    }

    public Object get(List<Object> list, String key) {
        return list.get(cols.get(key));
    }


    public void addRow(List<Object> row) {
        rows.add(row);
    }

    public void addAllRow(List<List<Object>> rows) {
        this.rows.addAll(rows);
    }


    public Object get(int row, int col) {
        return rows.get(row).get(col);
    }

    public Object get(int row, String col) {
        return rows.get(row).get(cols.get(col));
    }

    public String getNotNullStr(int row, String col) {
        return toNotNullStr(rows.get(row).get(cols.get(col)));
    }

    public static String toNotNullStr(Object o) {
        if (o == null) {
            return "";
        }
        return o.toString();
    }



    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(cols.entrySet().stream().map(e -> e.getKey()).collect(Collectors.joining("\t")));
        sb.append("\n");
        for (List<Object> row : rows) {
            sb.append(row.stream().map(e -> toNotNullStr(e.toString())).collect(Collectors.joining("\t")));
            sb.append("\n");
        }
        return sb.toString();
    }


    public Table[] split(int length) {
        List<List<List<Object>>> split = StreamUtils.split(rows, length);
        return split(split);
    }

    private Table[] split(List<List<List<Object>>> split) {
        Table[] tables = new Table[split.size()];
        for (int i = 0; i < split.size(); i++) {
            Table table = new Table(cols);
            table.addAllRow(split.get(i));
            tables[i] = table;
        }
        return tables;
    }

    public Table[] split(int length,int maxSize) {
        List<List<List<Object>>> split = StreamUtils.split(rows, length, maxSize);
        return split(split);
    }

    /**
     * 纵向合并表格 , 列需要一致
     */
    public static Table merge(Table... tables) {
        if (tables.length == 0) {
            return null;
        }
        if (tables.length == 1) {
            return tables[0];
        }
        Set<String> keys = tables[0].cols.keySet();
        Table table = new Table(tables[0].cols);
        table.addAllRow(tables[0].rows);

        for (int i = 1; i < tables.length; i++) {
            if (!tables[i].cols.keySet().equals(keys)) {
                throw new IllegalArgumentException("无法合并");
            }
            table.addAllRow(tables[i].rows);
        }
        return table;
    }

    public void sort(){

    }


}
