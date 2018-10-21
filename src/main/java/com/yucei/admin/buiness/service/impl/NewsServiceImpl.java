package com.yucei.admin.buiness.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yucei.admin.buiness.mapper.NewsContentMapper;
import com.yucei.admin.buiness.service.NewsService;
import com.yucei.admin.common.base.PageDataResult;
import com.yucei.admin.common.utils.DateUtil;
import com.yucei.admin.manage.pojo.NewsContent;
import com.yucei.admin.manage.pojo.NewsContentExample;
import com.yucei.admin.manage.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;


@Slf4j
@Service
public class NewsServiceImpl implements NewsService {

    @Autowired
    NewsContentMapper newsContentMapper;

    @Override
    public PageDataResult findByPage(NewsContent content, int pageNo, int pageSize) {
        Page<NewsContent> page = PageHelper.startPage(pageNo, pageSize);
        NewsContentExample exmaple = new NewsContentExample();
        NewsContentExample.Criteria criteria = exmaple.createCriteria();
        criteria.andIsdelEqualTo("0");
        if (content != null) {
            if (content.getId() != null) {
                criteria.andIdEqualTo(content.getId());
            }
            if (StringUtils.isNotBlank(content.getAuthor())) {
                criteria.andAuthorEqualTo(content.getAuthor());
            }
            if (content.getInsertuserid() != null) {
                criteria.andInsertuseridEqualTo(content.getInsertuserid());
            }
            if (content.getLotteryid() != null) {
                criteria.andLotteryidEqualTo(content.getLotteryid());
            }
            if (StringUtils.isNotBlank(content.getStartDate())) {
                criteria.andCreatetimeGreaterThanOrEqualTo(DateUtil.string2date(content.getStartDate(), DateUtil.YYYY_MM_DD));
            }
            if (StringUtils.isNotBlank(content.getEndDate())) {
                criteria.andCreatetimeLessThanOrEqualTo(DateUtil.string2date(content.getEndDate(), DateUtil.YYYY_MM_DD));
            }
            if (StringUtils.isNotBlank(content.getPublishflag())) {
                criteria.andPublishflagEqualTo(content.getPublishflag());
            }
        }
        exmaple.setOrderByClause(" publishflag DESC ,seq desc ,publishtime DESC,createtime DESC ");
        List<NewsContent> contentList = newsContentMapper.selectByExample(exmaple);
        PageDataResult result = new PageDataResult();
        if (!CollectionUtils.isEmpty(contentList)) {
            result.setTotals(Long.valueOf(page.getTotal()).intValue());
            result.setList(page.getResult());
        }
        return result;
    }

    @Override
    public NewsContent findById(Integer id) {
        NewsContentExample exmaple = new NewsContentExample();
        exmaple.createCriteria().andIdEqualTo(id);
        List<NewsContent> news = newsContentMapper.selectByExample(exmaple);
        if (!CollectionUtils.isEmpty(news)) {
            return news.get(0);
        }
        return null;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = 30000, rollbackFor = {
            RuntimeException.class, Exception.class})
    public boolean addUpdate(NewsContent content) throws Exception {
        boolean update = content.getId() == null ? false : true;
        try {
            if (update) {
                NewsContent oldcontent = findById(content.getId());
                if (("0".equalsIgnoreCase(oldcontent.getPublishflag())||StringUtils.isBlank(oldcontent.getPublishflag()))
                        && "1".equalsIgnoreCase(content.getPublishflag())) {
                    content.setPublishtime(new Date());
                }

                NewsContentExample example = new NewsContentExample();
                example.createCriteria().andIdEqualTo(content.getId());
                return newsContentMapper.updateByExampleSelective(content, example) > 0 ? true : false;
            } else {
                content.setIsdel("0");
                content.setCreatetime(new Date());
                content.setUpdatetime(content.getCreatetime());
                content.setIscoment("1");
                User user = (User) SecurityUtils.getSubject().getPrincipal();
                if (user != null) {
                    content.setInsertuserid(user.getId());
                }
                if ("1".equalsIgnoreCase(content.getPublishflag())) {
                    content.setPublishtime(content.getCreatetime());
                }
                return newsContentMapper.insert(content) > 0 ? true : false;
            }
        } catch (Exception e) {
            log.error("====保存修改新闻,content[{}]====", content);
            throw new Exception(e.getMessage());
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = 30000, rollbackFor = {
            RuntimeException.class, Exception.class})
    public boolean updatePublish(Integer id, String status) {
        try {
            NewsContent updatec = new NewsContent();
            if ("1".equalsIgnoreCase(status)) {
                //发布
                updatec.setPublishflag(status);
                updatec.setPublishtime(new Date());
            } else {
                updatec.setPublishflag("0");
                updatec.setPublishtime(null);
            }
            NewsContentExample example = new NewsContentExample();
            example.createCriteria().andIdEqualTo(id);
            return newsContentMapper.updateByExampleSelective(updatec, example) > 0 ? true : false;
        } catch (Exception e) {
            log.error("==== 修改状态异常 id[{}]，status[{}]==== ", id, status);
        }
        return false;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = 30000, rollbackFor = {
            RuntimeException.class, Exception.class})
    public boolean delNews(Integer id) {
        NewsContentExample example = new NewsContentExample();
        example.createCriteria().andIdEqualTo(id);
        return newsContentMapper.deleteByExample(example) > 0 ? true : false;
    }
}
