package com.zerobase.weather;

import com.zerobase.weather.domain.Memo;
import com.zerobase.weather.repository.JpaMemoRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
public class JpaMemoRepositoryTest {
    @Autowired
    JpaMemoRepository jpaMemoRepository;

    @Test
    void insertMemoTest(){
        //given
        Memo newMemo = new Memo(null, "jpa memo");
        //when
        jpaMemoRepository.save(newMemo);
        //then
        List<Memo> memoList = jpaMemoRepository.findAll();
        assertTrue(!memoList.isEmpty());
    }

    @Test
    void findByIdTest(){
        // given
        Memo newMemo = new Memo(null,"jpa memo2");
        // when
        jpaMemoRepository.save(newMemo);
        // then
        System.out.println(newMemo.getId());
        Optional<Memo> result = jpaMemoRepository.findById(newMemo.getId());
        assertEquals(result.get().getText(), "jpa memo2");
    }
}
