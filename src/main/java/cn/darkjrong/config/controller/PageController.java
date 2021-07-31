package cn.darkjrong.config.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 页面
 *
 * @author Rong.Jia
 * @date 2021/06/02 19:26
 */
@Slf4j
@Controller
public class PageController {

    /**
     * 跳转首页
     */
    @RequestMapping(value = "/")
    public String index() {
        return "index";
    }


}
