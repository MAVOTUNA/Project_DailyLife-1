package com.DailyLife.service;

import com.DailyLife.dto.Board;
import com.DailyLife.dto.BoardInfos;
import com.DailyLife.dto.BoardPhoto;
import com.DailyLife.dto.Upload;
import com.DailyLife.mapper.BoardMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardMapper boardMapper;
    private final Upload upload;

    public void addBoard(Board board , Long uno) {

        board.setUno(uno);
        board.setThumbnail(thumnailName(board));
        boardMapper.addBoard(board);

        if(board.getPhotos()!= null) {
            ArrayList<String> fileNameList;
            try {
                fileNameList = (ArrayList<String>) upload.FileUpload(board.getPhotos()); // 받아온 파일 이름들을 리스트에 저장
                for (String saveFileName: fileNameList) {
                    boardMapper.addPhoto(new BoardPhoto(saveFileName));
                }
            } catch (IOException e) {
                throw new RuntimeException("파일 오류입니다.");
            }
            
        }

    }

    private String thumnailName(Board board) {
        MultipartFile photo = board.getPhotos()[0];
        String orgFileName = photo.getOriginalFilename();  //파일 실제이름
        String orgFileExtension = orgFileName.substring(orgFileName.lastIndexOf(".")); // 파일 확장자 exe같은거
        String saveFileName = UUID.randomUUID().toString().replaceAll("-", "") + orgFileExtension;
        return saveFileName;
    }

    public List<BoardInfos> findByUno(Long uno) {

        List<BoardInfos> boardInfos = boardMapper.findByUno(uno);

        return boardInfos;
    }
}
