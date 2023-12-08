package cn.com.liurz.test.BuildBeetl;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

public class BeetlController{
    @RequestMapping("/test")
    public ModelAndView test(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("username","jackson");
        modelAndView.setViewName("beetl/test");
        return modelAndView;
    }
}
