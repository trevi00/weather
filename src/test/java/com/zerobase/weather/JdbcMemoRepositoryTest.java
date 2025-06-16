package com.zerobase.weather;

import com.zerobase.weather.domain.Memo;
import com.zerobase.weather.repository.JdbcMemoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Transactional
public class JdbcMemoRepositoryTest {
    @Autowired
    JdbcMemoRepository jdbcMemoRepository;

    @Test
    void insertMemoTest(){
        // given
        Memo newMemo = new Memo(2, "this is a second memo");
        // when
        jdbcMemoRepository.save(newMemo);
        // then
        Optional<Memo> result = jdbcMemoRepository.findById(newMemo.getId());
        assertEquals(result.get().getText(), newMemo.getText());
    }

    @Test
    void findAllMemoTest(){
        List<Memo> memoList = jdbcMemoRepository.findAll();
        System.out.println(memoList);
        assertNotNull(memoList);
    }




}
