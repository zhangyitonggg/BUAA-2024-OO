import java.util.HashMap;

public class DisjointSet {
    private final HashMap<Integer, Integer> son2Father;
    private final HashMap<Integer, Integer> ranks;
    
    public DisjointSet() {
        this.son2Father = new HashMap<>();
        this.ranks = new HashMap<>();
    }
    
    // 增加一个人
    public void add(int id) {
        son2Father.put(id, id);
        ranks.put(id, 0);
    }
    
    // 查询某人所在集合的代表元
    public int find(int id) { // 采用路径压缩
        // 寻找根节点
        int root = id;
        while (root != son2Father.get(root)) {
            root = son2Father.get(root);
        }
        
        // 更新路径上的所有点的father为root
        int temp = id;
        while (temp != root) {
            int nextTemp = son2Father.get(temp);
            son2Father.put(temp, root);
            temp = nextTemp;
        }
        
        return root;
    }
    
    public boolean merge(int id1, int id2) {
        int root1 = find(id1);
        int root2 = find(id2);
        if (root1 == root2) {
            return false;
        }
        int rank1 = ranks.get(root1);
        int rank2 = ranks.get(root2);
        if (rank1 < rank2) {
            son2Father.put(root1, root2);
        }
        else if (rank1 > rank2) {
            son2Father.put(root2, root1);
        }
        else {
            son2Father.put(root1, root2);
            ranks.put(root2, rank2 + 1);
        }
        return true;
    }
}
