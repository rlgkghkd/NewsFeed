package com.example.newsFeed.likes.repository;

import com.example.newsFeed.boards.entity.Board;
import com.example.newsFeed.likes.entity.Like;
import com.example.newsFeed.users.entity.User;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {

    Optional<Like> findByBoardAndUser(Board board, User user);
    Optional<List<Like>> findAllByBoard(Board board);

    Optional<List<Like>> findAllByUser(User user);
    default PageImpl<Like> findAllByUserOrElseThrow(User user, PageRequest pageRequest){
        List<Like> likeList = findAllByUser(user).orElseThrow();
        int start = (int) pageRequest.getOffset();
        int end = Math.min(start+pageRequest.getPageSize(), likeList.size());
        return new PageImpl<>(likeList.subList(start, end), pageRequest, likeList.size());
    }
}
