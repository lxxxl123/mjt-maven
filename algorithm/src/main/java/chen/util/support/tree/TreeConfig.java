package chen.util.support.tree;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author chenwh3
 */
@Data
@NoArgsConstructor
public class TreeConfig {
    /**
     * 初始深度
     */
    private int initialDepth = 1;

    private int rootPid = -10;
    private String childrenKey = "children";

    private String dataKey = "data";

    private String idKey = "id";
    private String depthKey = "depth";

    private String parentIdKey = "pid";

    private String nameKey = "name";

    public static TreeConfig DEFAULT_CONFIG = new TreeConfig();

    public TreeConfig childrenKey(String childrenKey){
        this.childrenKey = childrenKey;
        return this;
    }


    public TreeConfig depthKey(String depthKey) {
        this.depthKey = depthKey;
        return this;
    }
}
