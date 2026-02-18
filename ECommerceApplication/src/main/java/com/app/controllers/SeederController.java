package com.app.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.payloads.APIResponse;
import com.app.services.SeederService;

@RestController
@RequestMapping("/api")
public class SeederController {

    private final SeederService seederService;

    public SeederController(SeederService seederService) {
        this.seederService = seederService;
    }

    @PostMapping("/seed")
    public ResponseEntity<APIResponse> seed() {
        String result = seederService.seedDatabase();
        return ResponseEntity.ok(new APIResponse(result, true));
    }

}
