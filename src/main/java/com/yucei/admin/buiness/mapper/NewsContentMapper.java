package com.yucei.admin.buiness.mapper;

import com.yucei.admin.manage.pojo.NewsContent;
import com.yucei.admin.manage.pojo.NewsContentExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface NewsContentMapper {
    long countByExample(NewsContentExample example);

    int deleteByExample(NewsContentExample example);

    int insert(NewsContent record);

    int insertSelective(NewsContent record);

    List<NewsContent> selectByExample(NewsContentExample example);

    int updateByExampleSelective(@Param("record") NewsContent record, @Param("example") NewsContentExample example);

    int updateByExample(@Param("record") NewsContent record, @Param("example") NewsContentExample example);
}