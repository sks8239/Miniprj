package jdbc.controller;
import jdbc.dao.GameDAO;
import jdbc.dao.LoLChamDAO;
import jdbc.dao.LoLSkinDAO;
import jdbc.dao.MemberDAO;
import jdbc.vo.LoLChamVO;
import jdbc.vo.LoLSkinVO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping(value="/")
public class mainController {
    String cid = "";
    String selCham;

    public String getCid() {
        return cid;
    }

    @GetMapping
    public String main() {
        return "main";
    }

    @PostMapping("login")
    public String loginAction(@RequestParam("id") String id, @RequestParam("password") String pwd, Model model) {
        cid = id;
        model.addAttribute("currentID", id);
        model.addAttribute("password", pwd);
        MemberDAO memberdao = new MemberDAO();
        while (true) {
            if (!memberdao.lolLogin(id, pwd).equals("실패")) {
                return "start";
            } else {
                return "main";
            }
        }
    }

    @GetMapping("shop/shop_buycham")
    public String shop_buycham(@RequestParam("chaminput") String ChamBuyName, Model model) {
        String id = cid;
        LoLChamDAO lolchamdao = new LoLChamDAO();
        lolchamdao.LoLChamInsert(ChamBuyName, id);
        return "shop";
    }


    @GetMapping("shop/shop_chamskin")
    public String shop_buycham1(@RequestParam("chaminput") String selCham, Model model) {
        String id = cid;

        LoLChamDAO lolchamdao = new LoLChamDAO();
        List<LoLChamVO> list2 = lolchamdao.UserHaveCham(id,selCham);
        if(list2.size()==0) {
            model.addAttribute("currentID", id);
            model.addAttribute("nocham", "보유하지 않은 챔피언입니다.");
            return "start";
        }
        LoLSkinDAO lolskindao = new LoLSkinDAO();
        List<LoLSkinVO> list = lolskindao.UserLoLSkinDAO(selCham,id);
        model.addAttribute("list2", list);
        return"game_skin";
    }

    @GetMapping("game")
    public String game(Model model) {
        String id = cid;
        LoLChamDAO lolchamdao = new LoLChamDAO();
        List<LoLChamVO> list = lolchamdao.UserLoLChamDAO(id, selCham);
        model.addAttribute("list", list);
        return "game";
    }
    @GetMapping("game/skinSel")
    public String gameStart(@RequestParam("skininput") String skin, Model model) {
        String id = cid;
        GameDAO gameDAO = new GameDAO();
        if (gameDAO.WinLose(id).equals("1")) {
            return "game_victory";
        } else if (gameDAO.WinLose(id).equals("-1")){
            return "game_defeat";
        }
        return "game";
    }
}
