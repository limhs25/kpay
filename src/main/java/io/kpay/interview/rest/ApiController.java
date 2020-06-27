package io.kpay.interview.rest;

import io.kpay.interview.rest.dto.CreateRequestFlexMoney;
import io.kpay.interview.rest.dto.CreatedFlexMoney;
import io.kpay.interview.rest.dto.GetTakeMoney;
import io.kpay.interview.rest.dto.ShowFlexMoney;
import io.kpay.interview.service.FlexMoneyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
//@Tag(name = "KakaoPayFlex ", description = "KakaoPay Flex API")
public class ApiController {

    private final FlexMoneyService service;


    @Operation(summary = ". 뿌리기 API", description = "뿌릴 금액, 뿌릴 인원을 요청값으로 받습니다.")
    @PostMapping("/create")
    public ResponseEntity<CreatedFlexMoney> create(@RequestHeader("X-USER-ID") Long userId, @RequestHeader("X-ROOM-ID") String roomId, @Validated @RequestBody CreateRequestFlexMoney createFlexMoney) {
        CreatedFlexMoney tokenInfo = service.create(userId, roomId, createFlexMoney.getMoney(), createFlexMoney.getCapacity());
        return ResponseEntity.ok(tokenInfo);
    }
    @Operation(summary = ". 받기 API", description = "뿌리기 시 발급된 token을 요청값으로 받습니다.")
    @PutMapping("/take/{token}")
    public ResponseEntity<GetTakeMoney> take(@RequestHeader("X-USER-ID") Long userId, @RequestHeader("X-ROOM-ID") String roomId, @PathVariable("token") String token) {
        return ResponseEntity.ok(service.takeMoney(userId, roomId, token));
    }
    @Operation(summary = ". 조회 API", description = "뿌리기 시 발급된 token을 요청값으로 받습니다.")
    @GetMapping("/find/{token}")
    public ResponseEntity<ShowFlexMoney> find(@RequestHeader("X-USER-ID") Long userId, @RequestHeader("X-ROOM-ID") String roomId, @PathVariable("token") String token) {
        return ResponseEntity.ok(service.show(userId, roomId, token));
    }
}
