package com.proj.batchtutorial.job.filedata.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * packageName   : com.proj.batchtutorial.job.filedata.dto
 * fileName      : Player
 * author        : kang_jungwoo
 * date          : 2023/05/01
 * description   :
 * ===========================================================
 * DATE              AUTHOR               NOTE
 * -----------------------------------------------------------
 * 2023/05/01       kang_jungwoo         최초 생성
 */
@Data
public class Player implements Serializable {

    private String ID;
    private String lastName;
    private String firstName;
    private String position;
    private int birthYear;
    private int debutYear;
}


