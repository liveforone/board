package board.board.controller;

import board.board.dto.BoardDto;
import board.board.service.BoardService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("board")
public class BoardController {

    private BoardService boardService;

    //게시판 글 목록
    //리스트 경로에 요청 파라미터가 있을 경우 그에 따른 페이지를 수행함
    @GetMapping({"", "/list"})
    public String list(Model model, @RequestParam(value = "page", defaultValue = "1") Integer pageNum) {
        List<BoardDto> boardList = boardService.getBoardlist(pageNum);
        Integer[] pageList = boardService.getPageList(pageNum);

        model.addAttribute("boardList", boardList);
        model.addAttribute("pageList", pageList);

        return "board/list";
    }

    //글 쓰는 페이지
    @GetMapping("/post")
    public String write() {
        return "board/write";
    }

    //글을 쓴 후 포스트 메서드로 내용을 디비에 저장
    @PostMapping("/post")
    public String write(BoardDto boardDto) {
        boardService.savePost(boardDto);

        return "redirect:/board/list";
    }

    //게시물 상세 페이지, {no} 로 페이지 넘버 입력받음
    //pathvariable로 no 받음
    @GetMapping("/post/{no}")
    public String detail(@PathVariable("no") Long no, Model model) {
        BoardDto boardDto = boardService.getPost(no);

        model.addAttribute("boardDto", boardDto);

        return "redirect:/detail";
    }

    //게시물 수정 페이지
    @GetMapping("/post/edit/{no}")
    public String edit(@PathVariable("no") Long no, Model model) {
        BoardDto boardDto = boardService.getPost(no);

        model.addAttribute("boardDto", boardDto);

        return "board/update";
    }

    //put메소드로 게시물 수정 적용
    @PutMapping("/post/edit/{no}")
    public String update(BoardDto boardDto) {
        boardService.savePost(boardDto);

        return "redirect:/board/list";
    }

    //게시물 삭제
    @DeleteMapping("/post/{no}")
    public String delete(@PathVariable("no") Long no) {
        boardService.deletePost(no);

        return "redirect:/board/list";
    }

    //검색, 키워드를 뷰로 전달받고 서비스로 받은 리스트를 모델의 attribute로 전달
    @GetMapping("/board/search")
    public String search(@RequestParam(value = "keyword") String keyword, Model model) {
        List<BoardDto> boardDtoList = boardService.searchPosts(keyword);

        model.addAttribute("boardList", boardDtoList);

        return "board/list";
    }
}
