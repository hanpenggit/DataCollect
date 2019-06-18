package com.sinodata.rest;

import com.sinodata.h2.DBDao;
import com.sinodata.vo.PageVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/")
public class IndexController {

    @Autowired
    DBDao dao;

    @RequestMapping(value="/jobInfo", method= RequestMethod.GET)
    public String jobInfo(@RequestParam(name = "id",required = false) String id,ModelMap map) {
        if(StringUtils.isNotBlank(id)){
            //如果id不为空,则此时为更新
            Map<String, String> jobInfo = dao.getJobInfo(Integer.parseInt(id));
            map.addAllAttributes(jobInfo);
        }
        map.addAttribute("ID",id);
        return "jobInfo";
    }


    @RequestMapping(value="/main", method= RequestMethod.GET)
    public String main(ModelMap map) {
        return "main";
    }

    @RequestMapping(value="/jobs", method= RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Object jobs(@RequestParam(name = "unitname",required = false) String unitname,
                       @RequestParam(name = "page") int pageIndex,
                       @RequestParam(name = "limit") int limit,ModelMap map) {
        Map<String,String> searchParam=new HashMap<>();
        searchParam.put("UNITNAME",unitname);
        PageVo page=new PageVo();
        int count=dao.countJobInfo(searchParam);
        page.setCount(count);
        if(count>0){
            List<Object> data = dao.selectJobInfo(pageIndex,limit,searchParam);
            page.setData(data);
        }
        page.setCode(0);
        page.setMsg("");
        return page;
    }

    @RequestMapping(value="/deletejob", method= RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Object deletejob(@RequestParam(name = "id") int id) {
        int result=dao.deleteJobInfo(id);
        return result;
    }

    @RequestMapping(value="/editCellJob", method= RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Object editCellJob(@RequestParam(name = "id") int id,
                          @RequestParam(name = "field") String field,
                          @RequestParam(name = "value",required = false) String value
                        ) {
        int result=dao.editCellJob(id,field,value);
        return result;
    }

    @RequestMapping(value="/editJobInfo", method= RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Object editJobInfo(HttpServletRequest request) {
        int result=dao.editJobInfo(request);
        return result;
    }

}
