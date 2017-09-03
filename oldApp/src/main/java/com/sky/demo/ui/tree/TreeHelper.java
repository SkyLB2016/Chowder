package com.sky.demo.ui.tree;

import com.sky.demo.R;
import com.sky.demo.ui.tree.annotation.TreeNode;
import com.sky.demo.utils.LogUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * @author LiBin
 * @Description: TODO
 * @date 2015/9/29 9:54
 */
public class TreeHelper {
    /**
     * @param datas
     * @param <T>
     * @return
     * @throws IllegalAccessException
     */
    private static <T> List<Node> convertDatasToNodes(List<T> datas) throws IllegalAccessException {
        List<Node> nodes = new ArrayList<>();
        Node node = null;
        for (T data : datas) {
            node = new Node();
            Class cla = data.getClass();
            Field[] fields = cla.getDeclaredFields();
            for (Field field : fields) {
                boolean exist = field.isAnnotationPresent(TreeNode.class);
                if (!exist) continue;
                TreeNode nodeId = field.getAnnotation(TreeNode.class);
                field.setAccessible(true);
                String value = nodeId.value();
                if (value.equals("id")) {
                    node.setId((String) field.get(data));
                } else if (value.equals("pId")) {
                    node.setpId((String) field.get(data));
                } else if (value.equals("name")) {
                    node.setName((String) field.get(data));
                }
//                try {
//                    //通过get方法获取字段值
//                    String fieldName = field.getName();
//                    String methodName = "get" + fieldName.substring(0, 1).toUpperCase()
//                            + fieldName.substring(1).toLowerCase();
//                    Method method = data.getClass().getMethod(methodName);
//                    String ids = (String) method.invoke(data);
//                    LogUtils.i(ids);
//                } catch (NoSuchMethodException e) {
//                    e.printStackTrace();
//                } catch (InvocationTargetException e) {
//                    e.printStackTrace();
//                }
            }
            nodes.add(node);
        }
        //设置节点间的关系
        for (int i = 0; i < nodes.size(); i++) {
            Node node1 = nodes.get(i);
            for (int j = i + 1; j < nodes.size(); j++) {
                Node node2 = nodes.get(j);
                if (node2.getpId().equals(node1.getId())) {
                    node1.getChild().add(node2);
                    node2.setParent(node1);
                } else if (node1.getpId().equals(node2.getId())) {
                    node2.getChild().add(node1);
                    node1.setParent(node2);
                }
            }
        }
        for (Node n : nodes) {
            setNodeIcon(n);
        }
        return nodes;
    }

    /**
     * 为icon设置图标
     *
     * @param n
     */
    private static void setNodeIcon(Node n) {
        if (n.getChild().size() > 0 && n.isExpand()) n.setIcon(R.mipmap.ic_up);
        else if (n.getChild().size() > 0 && !n.isExpand()) n.setIcon(R.mipmap.ic_down);
        else n.setIcon(-1);
    }

    /**
     * 排序
     *
     * @param datas
     * @param defaultExpandLevel
     * @param <T>
     * @return
     * @throws IllegalAccessException
     */
    public static <T> List<Node> getSortedNodes(List<T> datas, int defaultExpandLevel) throws IllegalAccessException {
        List<Node> result = new ArrayList<>();
        List<Node> nodes = convertDatasToNodes(datas);
        List<Node> rootNodes = getRootNodes(nodes);
        LogUtils.i("rootNodes=" + rootNodes.size());
        for (Node node : rootNodes) {
            addNode(result, node, defaultExpandLevel, 1);
        }
        return result;
    }

    /**
     * 把一个节点的所有孩子节点都放入result
     *
     * @param result
     * @param node
     * @param defaultExpandLevel
     * @param currentLevel
     */
    private static void addNode(List<Node> result, Node node, int defaultExpandLevel, int currentLevel) {
        result.add(node);
        if (defaultExpandLevel > currentLevel) node.setIsExpand(true);
        if (node.hasLeaf()) return;
        for (int i = 0; i < node.getChild().size(); i++) {
            addNode(result, node.getChild().get(i), defaultExpandLevel, currentLevel + 1);
        }
    }

    /**
     * 过滤出根节点
     *
     * @param nodes
     * @return
     */
    private static List<Node> getRootNodes(List<Node> nodes) {
        List<Node> rootNodes = new ArrayList<>();
        for (Node node : nodes) {
            if (node.isRoot()) rootNodes.add(node);
        }
        return rootNodes;
    }

    /**
     * 获取可显示的节点
     *
     * @param nodes
     * @return
     */
    public static List<Node> filterVisibleNodes(List<Node> nodes) {

        List<Node> result = new ArrayList<>();
        for (Node node : nodes) {
            if (node.isRoot() || node.isParentExpand()) {
                setNodeIcon(node);
                result.add(node);
            }
        }
        return result;
    }

}
