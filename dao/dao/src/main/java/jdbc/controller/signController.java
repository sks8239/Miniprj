package jdbc.controller;

import jdbc.dao.MemberDAO;
import jdbc.vo.MemberVO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Date;
import java.util.List;

@Controller
@RequestMapping(value="/sign")
public class signController {
    @PostMapping
    public String signMain(){
        return "sign";
    }
    @PostMapping("/signing")
    public String home(@RequestParam("id") String id, @RequestParam("password") String pwd, @RequestParam("nickname") String nickName, @RequestParam("day") String birthDay, @RequestParam("phone") int phNumber, Model model) {
        MemberDAO memberDAO = new MemberDAO();
        model.addAttribute("currentID", id);
        if(!memberDAO.userInfoInsert(id,pwd,nickName,nickName,birthDay,phNumber).equals("error")){
            return "main";
        }
        return "sign";
    }
}
