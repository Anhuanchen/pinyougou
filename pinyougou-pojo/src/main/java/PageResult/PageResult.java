package PageResult;

import java.io.Serializable;
import java.util.List;

/**
 * 分页查询结果类
 * 传递总共的条数
 * 传递每页的数据
 */
public class PageResult implements Serializable {
    private Long totalCount;
    private List rows;

    public PageResult() {
    }

    public PageResult(Long totalCount, List rows) {
        this.totalCount = totalCount;
        this.rows = rows;
    }

    public Long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Long totalCount) {
        this.totalCount = totalCount;
    }

    public List getRows() {
        return rows;
    }

    public void setRows(List rows) {
        this.rows = rows;
    }
}
