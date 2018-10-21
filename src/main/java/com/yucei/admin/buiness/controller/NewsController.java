package com.yucei.admin.buiness.controller;

import com.yucei.admin.buiness.service.NewsService;
import com.yucei.admin.common.base.PageDataResult;
import com.yucei.admin.common.base.Result;
import com.yucei.admin.common.utils.DateUtil;
import com.yucei.admin.manage.pojo.NewsContent;
import com.yucei.admin.manage.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@RestController
@RequestMapping("/admin/news")
public class NewsController {


    @Autowired
    NewsService newsService;


    @RequestMapping("toList")
    public ModelAndView toList() {
        ModelAndView view = new ModelAndView("buisses/newsmid");
        return view;
    }


    @RequestMapping("toAdd")
    public ModelAndView toAdd() {
        ModelAndView view = new ModelAndView("buisses/newsedit");
        return view;
    }

    @RequestMapping("list")
    @ResponseBody
    public PageDataResult list(@RequestParam(required = false, value = "pageNo", defaultValue = "1") Integer pageNo,
                               @RequestParam(required = false, value = "pageSize", defaultValue = "20") Integer pageSize,
                               NewsContent newsContent) {
        if (newsContent != null && StringUtils.isNotBlank(newsContent.getStartDate())) {
            newsContent.setEndDate(DateUtil.date2string(DateUtil.addDays(DateUtil.string2date(newsContent.getStartDate(), DateUtil.YYYY_MM_DD), 1), DateUtil.YYYY_MM_DD));
        }
        return newsService.findByPage(newsContent, pageNo, pageSize);
    }

    @RequestMapping("get")
    @ResponseBody
    public Result get(@RequestParam("id") Integer id) {
        NewsContent content = newsService.findById(id);
        return Result.getInstance().ok(content);
    }

    @RequestMapping("create")
    @ResponseBody
    public Result create(NewsContent content) {
        try {
            if (newsService.addUpdate(content)) {
                return Result.getInstance().ok("成功");
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return Result.getInstance().fail("失败");
    }

    @RequestMapping("publish")
    @ResponseBody
    public Result publish(@RequestParam("id") Integer id, @RequestParam("publishflag") String publishflag) {
        try {
            if (newsService.updatePublish(id, publishflag)) {
                return Result.getInstance().ok("成功");
            }
        } catch (Exception e) {

        }
        return Result.getInstance().fail("失败");
    }

    @RequestMapping("delnews")
    @ResponseBody
    public Result delnews(@RequestParam("id") Integer id) {
        try {
            if (newsService.delNews(id)) {
                return Result.getInstance().ok("成功");
            }
        } catch (Exception e) {

        }
        return Result.getInstance().fail("失败");
    }
}
