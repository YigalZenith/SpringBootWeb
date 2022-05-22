package com.imooc.dao;

import com.imooc.domain.CourseClickCount;
import com.imooc.utils.HBaseUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class CourseClickCountDao {

    /**
     * 利用utils工具类按天查询HBase数据
     */
    public List<CourseClickCount> query(String day) throws Exception {
        List<CourseClickCount> queryResults = new ArrayList<>();

        // 去HBase表中根据day获取实战课程对应的访问量
        Map<String, Long> map = HBaseUtils.getInstance().scanPrefixRows("imooc_course_clickcount", day);

        // 遍历保存查询结果的Map, 存入CourseClickCount实体类对象
        for (Map.Entry<String, Long> entry : map.entrySet()) {
            CourseClickCount model = new CourseClickCount();
            model.setName(entry.getKey());
            model.setValue(entry.getValue());
            queryResults.add(model);
        }

        return queryResults;
    }

    public static void main(String[] args) throws Exception {
        CourseClickCountDao dao = new CourseClickCountDao();
        List<CourseClickCount> res = dao.query("20220517");
        for (CourseClickCount model : res) {
            System.out.println(model.getName() + ":" + model.getValue().toString());
        }
    }
}
