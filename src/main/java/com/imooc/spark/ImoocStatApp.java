package com.imooc.spark;

import com.imooc.dao.CourseClickCountDao;
import com.imooc.domain.CourseClickCount;
import net.sf.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ImoocStatApp {
    private static Map<String,String> map = new HashMap<>();
    static {
        map.put("112","Spark SQL慕课网日志分析");
        map.put("128","10小时入门大数据");
        map.put("145","深度学习之神经网络");
        map.put("146","强大的Node.js在Web开发的应用");
        map.put("131","Vue+Django实战");
        map.put("130","web前端性能调优");
    }

    @Autowired
    private CourseClickCountDao dao;

    // 用来处理ajax请求获取要展示的数据
    @PostMapping ("/getCourseData")
    @ResponseBody
    public List<CourseClickCount> getCourseData(@RequestParam(name = "day",defaultValue = "20171111") String day) throws Exception {
        List<CourseClickCount> list = dao.query(day);

        for (CourseClickCount model : list) {
            model.setName(map.get(model.getName().substring(9)));
        }

        return list;
    }

    // 动态饼图的主页,内嵌ajax调用
    @GetMapping("/dynamic_pic")
    public ModelAndView dynamic_pic(@RequestParam(name = "day",defaultValue = "20171111") String day){
        ModelAndView view = new ModelAndView("dynamic_pic");
        view.addObject("day",day);
        return view;
    }
}
