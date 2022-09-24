package com.example.backend.controllers;

import com.example.backend.models.Ad;
import com.example.backend.services.AdServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "https://localhost:4200")
@RequestMapping("/api/ad")
public class AdController {

    private final AdServiceImpl adService;

    @PostMapping("/addAdv/{email}")
    public ResponseEntity<?> addAdvertToMember(@RequestBody Ad ad, @PathVariable String email){
        adService.addAdv(email, ad);
        return ResponseEntity.status(201).body("Ad made");
    }

    @GetMapping("/list/{email}")
    public ResponseEntity<List<Ad>> getFriendsAds(@PathVariable("email") String email) {
        return ResponseEntity.ok().body(adService.getFriendsAds(email));
    }

    @GetMapping("/list/recommended/{email}")
    public ResponseEntity<List<Ad>> getRecommendedNonFriendAds(@PathVariable("email") String email){
        return ResponseEntity.ok().body(adService.getNonFriendsAds(email));
    }

    @GetMapping("/own_list/{email}")
    public ResponseEntity<List<Ad>> getOwnAds(@PathVariable("email") String email) {
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
