//package chen.util;
//
//import cn.hutool.core.collection.CollUtil;
//import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
//import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
//import com.baomidou.mybatisplus.core.metadata.OrderItem;
//import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
//import com.haday.qms.core.log.exception.ServiceException;
//import com.haday.qms.core.tool.support.BracketFind;
//import com.haday.tp.query.core.utils.SpringUtil;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.codec.binary.StringUtils;
//import org.springframework.dao.DataAccessException;
//import org.springframework.jdbc.core.JdbcTemplate;
//
//import javax.sql.DataSource;
//import java.util.List;
//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//import java.util.stream.Collectors;
//
///**
// * @author chenwh3
// */
//@Slf4j
//public class JdbcTemplateUtils {
//
//    public static final int QUERY_TIMEOUT = 60;
//
//    private JdbcTemplateUtils() {
//    }
//
//    public static final String ORDER_BY = "order by ";
//
//    private static Map<String, DataSource> dataSourceMap;
//    private static final Map<String, JdbcTemplate> JDBC_TEMPLATE_MAP = new ConcurrentHashMap<>(4);
//
//    private static DataSource getDatasource(String ds) {
//        if (dataSourceMap == null) {
//            DynamicRoutingDataSource dataSource = (DynamicRoutingDataSource) SpringUtil.getBean(JdbcTemplate.class).getDataSource();
//            if (dataSource == null) {
//                throw new ServiceException("获取{}数据源出错", ds);
//            }
//            dataSourceMap = dataSource.getCurrentDataSources();
//        }
//        return dataSourceMap.get(ds);
//    }
//
//    public static JdbcTemplate getJdbtTemplate(String ht) {
//        return JDBC_TEMPLATE_MAP.computeIfAbsent(ht, key -> {
//            JdbcTemplate jdbcTemplate = new JdbcTemplate(getDatasource(ht));
//            //设置超时时间 (s)
//            jdbcTemplate.setQueryTimeout(QUERY_TIMEOUT);
//            return jdbcTemplate;
//        });
//    }
//
//    /**
//     * 根据@Ds注解动态切换数据源
//     */
//    public static JdbcTemplate getJdbtTemplate() {
//        String ht = DynamicDataSourceContextHolder.peek();
//        try {
//            ht = StringUtil.isBlank(ht) ? QcDataSrcUtil.getDbSrc().getAlias() : ht;
//            ht = StringUtil.isBlank(ht) ? StringUtil.STR_VAL_GM : ht;
//        } catch (Exception e) {
//            throw new ServiceException("token过期 , 请重新登录");
//        }
//        return getJdbtTemplate(ht);
//    }
//
//
//    public static final String PAGE_WRAPPER = "select * from (select ROW_NUMBER() OVER (%s) as __rw__, %s) __table__ where __rw__ between %s and %s; ";
//
//
//    /**
//     * 按mapper及方法查询
//     *
//     * @param mapper     mapper对象
//     * @param methodName maper方法名
//     * @param args       mapper方法对应的参数
//     * @return
//     */
//    public static List<Map<String, Object>> getListByMapper(Object mapper, String methodName, Object... args) {
//        String sql = StringPool.EMPTY;
//        try {
//            sql = SqlUtil.getMapperSql(mapper, methodName, args);
//            return getJdbtTemplate().queryForList(sql);
//        } catch (Exception e) {
//            log.error("JdbcTemplate 查询getListByMapper, methodName = {}, sql = {}, 出错", methodName, sql, e);
//            return null;
//        }
//    }
//
//    private static final Pattern SQL_START = Pattern.compile("(?is)select[^#]*?\\sfrom\\s");
//    private static final Pattern SQL_WITH = Pattern.compile("(?is)With\\s+\\w+\\s+as\\s*");
//
//
//
//    /**
//     * 分页查询
//     * @param countMethodName 分页查询独立的sql
//     * @param args [0] - page 分页查询 , 必须包含current 和 size , 分页必须包含 isSearchCount
//     * @return 分页结果
//     */
//    public static Page<Map<String, Object>> getPageByMapper(Object mapper , String methodName, String countMethodName, Object... args) {
//        Page<?> page = (Page<?>) args[0];
//        long pageIndex = page.getCurrent();
//        long pageSize = page.getSize();
//
//        String sql = SqlUtil.getMapperSql(mapper, countMethodName, args);
//        if (page.isSearchCount()) {
//            //查数量
//            /**
//             * 处理with as
//             */
//            BracketFind bracketFind = new BracketFind();
//            String selectCountSql = sql;
//            String withPrefix = "";
//            boolean find = bracketFind.find(selectCountSql, SQL_WITH);
//            if (find) {
//                int right = bracketFind.right();
//                withPrefix = bracketFind.group();
//                selectCountSql = selectCountSql.substring(right);
//            }
//            selectCountSql = withPrefix + selectCountSql .replaceFirst("(?is)select[^#]*?\\sfrom\\s", "select count(*) as __num__ from ");
//            try {
//                page.setTotal(getJdbtTemplate().queryForObject(selectCountSql, Long.class));
//            } catch (Exception e) {
//                if (e.getMessage().contains("该语句没有返回结果集")) {
//                    log.warn("尝试重新查询", e);
//                    page.setTotal(getJdbtTemplate().queryForObject(selectCountSql, Long.class));
//                } else {
//                    throw e;
//                }
//            }
//        }
//
//        List<OrderItem> orders = page.getOrders();
//
//        //获取order by
//        String orderBy = ORDER_BY;
//        if (CollUtil.isNotEmpty(orders)) {
//            orderBy += orders.stream().map(e -> e.getColumn() + (e.isAsc() ? " asc" : " desc"))
//                    .collect(Collectors.joining(","));
//        }
//        if (ORDER_BY.equals(orderBy)) {
//            orderBy = "ORDER BY CURRENT_TIMESTAMP";
//        }
//
//
//        if (!StringUtils.equals(countMethodName,methodName)) {
//            sql = SqlUtil.getMapperSql(mapper, methodName, args);
//        }
//
//        /**
//         * 处理with
//         */
//        String selectSql = sql;
//        String withPrefix = "";
//        BracketFind bracketFind = new BracketFind();
//        boolean find = bracketFind.find(selectSql, SQL_WITH);
//        if (find) {
//            int right = bracketFind.right();
//            withPrefix = bracketFind.group();
//            selectSql = selectSql.substring(right);
//        }
//        Matcher matcher = SQL_START.matcher(selectSql);
//        if (matcher.find()) {
//            String prefix = selectSql.substring(0, matcher.start());
//            String main = selectSql.substring(matcher.start());
//            //拼接分页Sql
//            main = String.format(PAGE_WRAPPER, orderBy, main.replaceFirst("(?is)^\\s*select", ""), pageSize * (pageIndex - 1) + 1, pageSize * pageIndex);
//            selectSql = withPrefix + prefix + main;
//        } else {
//            throw new ServiceException("JdbcTemplate 查询出错 , sql = {}", sql);
//        }
//
//        try {
//            log.debug(selectSql);
//            List<Map<String, Object>> list = getJdbtTemplate().queryForList(selectSql);
//            page.setRecords((List) list);
//            return (Page<Map<String, Object>>) page;
//        } catch (DataAccessException e) {
//            log.error("JdbcTemplate 查询出错 , sql = {}", selectSql, e);
//            throw new ServiceException(e.getMessage());
//        }
//    }
//
//
//    /**
//     * 分页查询
//     *
//     * @param args [0] - page 分页查询 , 必须包含current 和 size , 分页必须包含 isSearchCount
//     * @return 分页结果
//     */
//    public static Page<Map<String, Object>> getPageByMapper(Object mapper , String methodName, Object... args) {
//        return getPageByMapper(mapper, methodName, methodName, args);
//    }
//
//}
