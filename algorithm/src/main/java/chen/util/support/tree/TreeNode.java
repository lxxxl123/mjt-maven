package chen.util.support.tree;

import cn.hutool.core.convert.Convert;
import com.alibaba.fastjson.util.TypeUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
public class TreeNode<T> extends HashMap<String, Object> {

    public Object getId() {
        return get(config.getIdKey());
    }

    public void setId(Object id) {
        put(config.getIdKey(), id);
    }

    public Object getPid() {
        return get(config.getParentIdKey());
    }

    public void setPid(Object pid) {
        put(config.getParentIdKey(), pid);
    }

    public T getData() {
        return (T)get(config.getDataKey());
    }

    public void setData(T data) {
        put(config.getDataKey(), data);
    }

    public String getName() {
        return String.valueOf(get(config.getNameKey()));
    }

    public void setName(String name) {
        put(config.getNameKey(), name);
    }

    public Integer getDepth() {
        return Convert.toInt(get(config.getDepthKey()));
    }

    public void setDepth(Integer depth) {
        put(config.getDepthKey(), depth);
    }

    public List<TreeNode<T>> getChildren() {
        return (List<TreeNode<T>>) get(config.getChildrenKey());
    }

    public void setChildren(List<TreeNode<T>> children) {
        put(config.getChildrenKey(), children);
    }

    @Getter
    @Setter
    private boolean isLeaf;

    @Getter
    @Setter
    private TreeConfig config ;

    public TreeNode(int id , int pid , T data , String name , int depth , List<TreeNode<T>> children , boolean isLeaf , TreeConfig treeConfig){
        setConfig(treeConfig);
        setId(id);
        setPid(pid);
        setData(data);
        setName(name);
        setDepth(depth);
        setChildren(children);
        setLeaf(isLeaf);
    }


}
