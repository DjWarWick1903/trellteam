package ro.dev.trellteam.web.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ro.dev.trellteam.domain.Board;
import ro.dev.trellteam.exceptions.TrellGenericException;
import ro.dev.trellteam.web.dto.BoardDto;
import ro.dev.trellteam.web.mapper.BoardMapper;
import ro.dev.trellteam.web.repository.BoardRepository;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class BoardService {
    private final BoardRepository boardRepository;
    private final BoardMapper boardMapper;

    public BoardDto createBoard(BoardDto request) {
        log.debug("BoardService--createBoard--request: {}", request);
        Board board = boardMapper.dtoToDomain(request);
        board.setDateCreated(new java.sql.Date((new Date()).getTime()));
        board = createBoard(board);
        return boardMapper.domainToDto(board);
    }

    /**
     * Method to get a list of boards starting from a department id.
     * @param idDepartment
     * @return List
     */
    public List<Board> listDepartmentBoards(Long idDepartment) {
        log.debug("BoardService--listDepartmentBoards--IN");
        log.debug("BoardService--listDepartmentBoards--idDepartment: {}", idDepartment);

        final List<Board> boards = boardRepository.findBoardsByIdDep(idDepartment);

        log.debug("BoardService--listDepartmentBoards--boards: {}", boards.toString());
        log.debug("BoardService--listDepartmentBoards--OUT");

        return boards;
    }

    /**
     * Method used to create a board.
     * @param board
     * @return Board
     */
    public Board createBoard(Board board) {
        log.debug("BoardService--createBoard--IN");

        board = boardRepository.save(board);

        log.debug("BoardService--createBoard--board: {}", board.toString());
        log.debug("BoardService--createBoard--OUT");

        return board;
    }

    /**
     * Method used to delete a board by it's title.
     * @param title
     */
    public void deleteBoardByTitle(String title) {
        log.debug("BoardService--deleteBoardByTitle--IN");
        log.debug("BoardService--deleteBoardByTitle--title: {}", title);

        boardRepository.deleteByTitle(title);

        log.debug("BoardService--deleteBoardByTitle--OUT");
    }

    /**
     * Method used to delete a board by it's id.
     * @param idDepartment
     */
    public void deleteBoardById(Long idDepartment) {
        log.debug("BoardService--deleteBoardById--IN");
        log.debug("BoardService--deleteBoardById--idDepartment: {}", idDepartment);

        boardRepository.deleteById(idDepartment);

        log.debug("BoardService--deleteBoardById--OUT");
    }

    public Board getBoardById(Long id) {
        log.debug("BoardService--getBoardById--IN");

        Board board = null;
        try {
            board = boardRepository.findById(id).get();
        } catch(Exception e) {
            log.error(e.getMessage());
            throw new TrellGenericException("TRELL_ERR_4");
        }

        log.debug("BoardService--getBoardById--board: {}", board.toString());
        log.debug("BoardService--getBoardById--OUT");
        return board;
    }
}
