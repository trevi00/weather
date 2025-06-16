package com.zerobase.weather.controller;

import com.zerobase.weather.domain.Diary;
import com.zerobase.weather.service.DiaryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Tag(name = "Diary API", description = "날씨 일기 관련 API")
@RestController
public class DiaryController {
    private final DiaryService diraryService;
    private final DiaryService diaryService;

    public DiaryController(DiaryService diraryService, DiaryService diaryService) {
        this.diraryService = diraryService;
        this.diaryService = diaryService;
    }

    @Operation(summary = "일기 생성", description = "날짜와 내용을 바탕으로 일기를 생성합니다.")
    @PostMapping("/create/diary")
    void createDiary(
            @Parameter(description = "작성 날짜", example = "2025-06-16")
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date,

            @Parameter(description = "일기 내용", example = "오늘은 맑고 기분 좋은 하루였다.")
            @RequestBody String text
    ) {
        diraryService.createDiary(date, text);
    }

    @Operation(summary = "일기 조회", description = "하루 치 일기를 조회합니다.")
    @GetMapping("/read/diary")
    List<Diary> readDiary(
            @Parameter(description = "조회 날짜", example = "2025-06-16")
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date
    ) {
        return diraryService.readDiary(date);
    }

    @Operation(summary = "기간별 일기 조회", description = "시작일과 종료일 사이의 일기를 모두 조회합니다.")
    @GetMapping("/read/diaries")
    List<Diary> readDiaries(
            @Parameter(description = "조회 시작일", example = "2025-06-01")
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate startDate,

            @Parameter(description = "조회 종료일", example = "2025-06-16")
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate endDate
    ) {
        return diaryService.readDiaries(startDate, endDate);
    }

    @Operation(summary = "일기 수정", description = "해당 날짜의 일기를 새 텍스트로 수정합니다.")
    @PutMapping("/update/diary")
    void updateDiary(
            @Parameter(description = "수정할 날짜", example = "2025-06-16")
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date,

            @Parameter(description = "새 일기 내용", example = "내용이 수정되었습니다.")
            @RequestBody String text
    ) {
        diaryService.updateDiary(date, text);
    }

    @Operation(summary = "일기 삭제", description = "해당 날짜의 일기를 삭제합니다.")
    @DeleteMapping("/delete/diary")
    void deleteDiary(
            @Parameter(description = "삭제할 날짜", example = "2025-06-16")
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date
    ) {
        diaryService.deleteDiary(date);
    }
}
