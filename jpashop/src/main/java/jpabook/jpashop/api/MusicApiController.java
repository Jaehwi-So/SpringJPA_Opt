package jpabook.jpashop.api;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.test.Music;
import jpabook.jpashop.dto.MusicDto;
import jpabook.jpashop.service.MemberService;
import jpabook.jpashop.service.MusicService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

//@Controller @ResponseBody
@RestController
@RequiredArgsConstructor
public class MusicApiController {
    private final MusicService musicService;

    @GetMapping("/api/v1/music")
    public List<MusicDto> findMusic() {
        List<Music> musics =  musicService.findMusics();
        List<MusicDto> result = musics.stream()
                .map(o -> new MusicDto(o))
                .collect(Collectors.toList());
        return result;
    }

    @GetMapping("/api/v2/music")
    public List<MusicDto> findMusicV2() {
        List<MusicDto> result =  musicService.findMusicsV2();
        return result;
    }



}