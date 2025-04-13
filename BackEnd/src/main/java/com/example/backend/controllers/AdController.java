package com.example.backend.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.models.Ad;
import com.example.backend.services.AdServiceImpl;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/ad")
public class AdController {

    private final AdServiceImpl adService;

    @PostMapping("/addAdv/{email}")
    public ResponseEntity<?> addAdvertToMember(@RequestBody Ad ad, @PathVariable String email){
        adService.addAdv(email, ad);
        return ResponseEntity.status(201).body("Ad made");
    }

    @GetMapping("/list/{email}")
    public ResponseEntity<List<Ad>> getFriendsAds(@PathVariable String email) {
        return ResponseEntity.ok().body(adService.getFriendsAds(email));
    }

    @GetMapping("/list/recommended/{email}")
    public ResponseEntity<List<Ad>> getRecommendedNonFriendAds(@PathVariable String email){
        return ResponseEntity.ok().body(adService.getNonFriendsAds(email));
    }

    @GetMapping("/own_list/{email}")
    public ResponseEntity<List<Ad>> getOwnAds(@PathVariable String email) {
        return ResponseEntity.ok().body(adService.getOwnAds(email));
    }

    @PostMapping("/apply")
    public ResponseEntity<?> doApply(@RequestHeader Long ad_id, @RequestBody String email){
        adService.applyForAd(email, ad_id);
        return ResponseEntity.status(201).body("Job applied");
    }

    @PostMapping("/unapply")
    public ResponseEntity<?> doUnApply(@RequestHeader Long ad_id, @RequestBody String email){
        adService.unApplyForAd(email, ad_id);
        return ResponseEntity.status(201).body("Job not applied");
    }

    @GetMapping("/isApplied")
    public ResponseEntity<?> isApplied(@RequestHeader List<Long> array, @RequestHeader String email){
        return ResponseEntity.ok().body(adService.appliedAds(array, email));
    }

    @GetMapping("/applied_members")
    public ResponseEntity<?> appliedMembers(@RequestHeader List<Long> array){
        return ResponseEntity.ok().body(adService.getAllAppliedMembers(array));
    }
}
