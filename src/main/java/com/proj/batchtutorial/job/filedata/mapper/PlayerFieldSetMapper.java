package com.proj.batchtutorial.job.filedata.mapper;

import com.proj.batchtutorial.job.filedata.dto.Player;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;

/**
 * packageName   : com.proj.batchtutorial.job.filedata.mapper
 * fileName      : PlayerFieldSetMapper
 * author        : kang_jungwoo
 * date          : 2023/05/01
 * description   :
 * ===========================================================
 * DATE              AUTHOR               NOTE
 * -----------------------------------------------------------
 * 2023/05/01       kang_jungwoo         최초 생성
 */
public class PlayerFieldSetMapper implements FieldSetMapper<Player> {
    public Player mapFieldSet(FieldSet fieldSet) {
        Player player = new Player();

        player.setID(fieldSet.readString(0));
        player.setLastName(fieldSet.readString(1));
        player.setFirstName(fieldSet.readString(2));
        player.setPosition(fieldSet.readString(3));
        player.setBirthYear(fieldSet.readInt(4));
        player.setDebutYear(fieldSet.readInt(5));

        return player;
    }
}


