package com.yucei.admin.buiness.service;

import com.yucei.admin.common.base.PageDataResult;
import com.yucei.admin.manage.pojo.NewsContent;

public interface NewsService {

    public PageDataResult findByPage(NewsContent content, int pageNo, int PageSize);

    public NewsContent findById(Integer id);

    public boolean addUpdate(NewsContent content) throws Exception;

    public boolean updatePublish(Integer id, String status) throws Exception;

    public boolean delNews(Integer id);

}
