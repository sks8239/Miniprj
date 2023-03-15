package jdbc.controller;
import jdbc.dao.LoLChamDAO;
import jdbc.dao.LoLSkinDAO;
import jdbc.dao.MemberDAO;
import jdbc.vo.LoLChamVO;
import jdbc.vo.LoLSkinVO;
import jdbc.vo.MemberVO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping(value = "/shop")
public class shopController {
    @GetMapping
    public String shop(){
        return "shop";
    }

    @GetMapping("/shop_cham")
        public String shop_cham(Model model){
            LoLChamDAO dao = new LoLChamDAO();
            List<LoLChamVO> test = dao.LoLChamSelect();
            model.addAttribute("shop_cham", test);
            return "shop_cham";
        }

    @GetMapping("/shop_skin")
        public String shop_skin(Model model){
        LoLSkinDAO dao = new LoLSkinDAO();
        List<LoLSkinVO> test = dao.LoLSkinSelect();
        model.addAttribute("shop_skin", test);
            return "shop_skin";
        }

    @PostMapping("/logout")
        public String logout(){
            return "main";
        }
}
