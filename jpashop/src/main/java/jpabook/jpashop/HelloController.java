package jpabook.jpashop;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HelloController {

    @GetMapping("hello")
    public String hello(Model model){
        //스프링 UI에 해당하는 model 객체에 데이터를 담아서 넘김
        model.addAttribute("data", "이건 서버에서 담은 데이터");    //data는 Hello
        return "hello";
        //return은 view의 이름, resources/templates의 HTML을 렌더링한다.
        //resources/static에는 정적 View, index.html이 디폴트
    }
}
