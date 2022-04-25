package board.board.service;

import board.board.domain.Board;
import board.board.dto.BoardDto;
import board.board.repository.BoardRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class BoardService {

    private BoardRepository boardRepository;

    private static final int BLOCK_PAGE_NUM_COUNT = 5;  //블럭에 존재하는 페이지 번호 수

    private static final int PAGE_POST_COUNT = 4;  //한 페이지에 존재하는 게시글

    private BoardDto convertEntityToDto(Board board) {
        return BoardDto.builder()
                .id(board.getId())
                .title(board.getTitle())
                .content(board.getContent())
                .writer(board.getWriter())
                .createDate(board.getCreateDate())
                .modifiedDate(board.getModifiedDate())
                .build();
    }

    @Transactional
    public List<BoardDto> getBoardlist(Integer pageNum) {
        Page<Board> page = boardRepository.findAll(PageRequest.of(
                pageNum - 1, PAGE_POST_COUNT, Sort.by(Sort.DEFAULT_DIRECTION.ASC, "createdDate")
        ));

        List<Board> boardEntities = page.getContent();
        List<BoardDto> boardDtoList = new ArrayList<>();

        for (Board board : boardEntities) {
            boardDtoList.add(this.convertEntityToDto(board));
        }

        return boardDtoList;
    }

    @Transactional
    public BoardDto getPost(Long id) {

        //옵셔널로 널포인트 익셉션 방지
        Optional<Board> boardWrapper = boardRepository.findById(id);
        Board board = boardWrapper.get();

        BoardDto boardDto = BoardDto.builder()
                .id(board.getId())
                .title(board.getTitle())
                .content(board.getContent())
                .writer(board.getWriter())
                .createDate(board.getCreateDate())
                .modifiedDate(board.getModifiedDate())
                .build();

        return boardDto;
    }

    @Transactional
    public Long savePost(BoardDto boardDto) {
        return boardRepository.save(boardDto.toEntity()).getId();
    }

    @Transactional
    public void deletePost(Long id) {
        boardRepository.deleteById(id);
    }

    //검색 API
    /*
    프론트로 키워드를 검색하면 컨트롤러러부터 키워드를 전달받게 된다.
     */
    @Transactional
    public List<BoardDto> searchPosts(String keyword) {
        List<Board> boardEntities = boardRepository.findByTitleContaining(keyword);
        List<BoardDto> boardDtoList = new ArrayList<>();

        if (boardEntities.isEmpty()) {
            return boardDtoList;
        }

        for (Board board : boardEntities) {
            boardDtoList.add(this.convertEntityToDto(board));
        }

        return boardDtoList;
    }

    //페이징
    @Transactional
    public Long getBoardCount() {  //전체 게시글 갯수
        return boardRepository.count();
    }

    public Integer[] getPageList(Integer curPageNum) {
        Integer[] pageList = new Integer[BLOCK_PAGE_NUM_COUNT];

        //총 게시글 갯수
        Double postsTotalCount = Double.valueOf(this.getBoardCount());

        //총 게시글 기준으로 계산한 마지막 페이지 번호 계산(올림으로 계산)
        Integer totalListPageNum = (int) (Math.ceil((postsTotalCount / PAGE_POST_COUNT)));

        //현재 페이지 기준으로 블럭의 마지막 페이지 번호 계산
        Integer blockLastPageNum =
                (totalListPageNum > curPageNum + BLOCK_PAGE_NUM_COUNT)
                        ? curPageNum + BLOCK_PAGE_NUM_COUNT : totalListPageNum;

        //페이지 시작 번호 조정
        curPageNum = (curPageNum <= 3) ? 1 : curPageNum - 2;

        for (int val = curPageNum, idx = 0; val <= blockLastPageNum; val++, idx++) {
            pageList[idx] = val;
        }

        return pageList;
    }
}
