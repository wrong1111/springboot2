package com.yucei.admin.common.jms;

import com.alibaba.fastjson.JSON;
import com.yucei.admin.buiness.service.IssueService;
import lombok.extern.slf4j.Slf4j;
import net.sf.oval.constraint.AssertURL;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.util.HashMap;


/**
 * 广播接收
 */
@Slf4j
@Component
public class TopicReceiver {

    @Autowired
    IssueService issueService;

    @JmsListener(destination = "topic", containerFactory = "jmsTopicListenerContainerFactory")
    public void receive(String msg) {
        if (log.isDebugEnabled()) {
            log.debug("==== topic 监听到的消息内容为: " + msg);
        }
        HashMap<String, Object> params = JSON.parseObject(msg, HashMap.class);
        String lotteryid = params.get("lotteryid").toString();
        String number = params.get("awardnumber").toString();
        String issueno = params.get("issueno").toString();
        String opentime = params.get("opentime").toString();
        if ("1".equalsIgnoreCase(lotteryid) || "4".equalsIgnoreCase(lotteryid)) {
            if ("1".equalsIgnoreCase(lotteryid) && 5 != number.length()) {
                String[] str = StringUtils.splitPreserveAllTokens(number, ",");
                int a = 0, b = 0, c = 0;
                for (int i = 0; i < str.length; i++) {
                    if (i >= 0 && i < 6) {
                        a += Integer.valueOf(str[i]);
                    } else if (i >= 6 && i < 12) {
                        b += Integer.valueOf(str[i]);
                    } else if (i >= 12 && i < 18) {
                        c += Integer.valueOf(str[i]);
                    }
                }
                number = (a % 100 % 10) + "," + (b % 100 % 10) + "," + (c % 100 % 10);
            } else if ("4".equalsIgnoreCase(lotteryid) && 5 != number.length()) {
                int first = 0;
                int second = 0;
                int third = 0;
                String[] awardnumbers = StringUtils.splitPreserveAllTokens(number, ",");
                for (int i = 1; i < awardnumbers.length; i++) {
                    if (i == 1 || i == 4 || i == 7 || i == 10 || i == 13 || i == 16) {
                        first = first + Integer.valueOf(awardnumbers[i]);
                    } else if (i == 2 || i == 5 || i == 8 || i == 11 || i == 14 || i == 17) {
                        second = second + Integer.valueOf(awardnumbers[i]);
                    } else if (i == 3 || i == 6 || i == 9 || i == 12 || i == 15 || i == 18) {
                        third = third + Integer.valueOf(awardnumbers[i]);
                    }
                }
                first = first % 100 % 10;
                second = second % 100 % 10;
                third = third % 100 % 10;
                number = first + "," + second + "," + third;
            }
            if (log.isInfoEnabled()) {
                log.info("==== 更新[{}],[{}],[{}],[{}]期号列表同时准备生成下期预猜结果" + lotteryid, issueno, number, opentime);
            }
            try {
                issueService.updateIssueResult(lotteryid, issueno, number, opentime);
            } catch (Exception e) {
                log.error("====更新[{}],[{}],[{}],[{}]期号异常!", lotteryid, issueno, number, opentime);
            }
        }


    }

}
